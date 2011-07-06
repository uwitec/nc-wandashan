package nc.bs.ic.pub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.wds.ic.stock.StockInvOnHandBO;
import nc.bs.wds.tray.lock.LockTrayBO;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.util.SQLHelper;
import nc.vo.ic.other.in.OtherInBillVO;
import nc.vo.ic.pub.StockInvOnHandVO;
import nc.vo.ic.pub.TbGeneralBBVO;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBDACTION;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;

public class IcInPubBO {
	
	private BaseDAO m_dao = null;

	public BaseDAO getDao() {
		if (m_dao == null) {
			m_dao = new BaseDAO();
		}
		return m_dao;
	}
	
	private StockInvOnHandBO stockBO = null;
	private StockInvOnHandBO getStockBO(){
		if(stockBO == null)
			stockBO = new StockInvOnHandBO(getDao());
		return stockBO;
	}
	
		
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 入库单保存时生成库存存量信息
	 * @时间：2011-4-9下午07:30:24
	 * @param bodys
	 * @param cargdocPK
	 * @throws BusinessException
	 */
	public void insertInvBatchState(TbGeneralBVO[] bodys,String cargdocPK) throws BusinessException{
		if(bodys == null || bodys.length == 0){
			return;
		}
		List<String> lTrayID = new ArrayList<String>();
		List<StockInvOnHandVO> linvInfor = new ArrayList<StockInvOnHandVO>();
		List<TbGeneralBBVO> ltray = null;
		StockInvOnHandVO tmpInvVO = null;
		for(TbGeneralBVO body:bodys){
			ltray = body.getTrayInfor();
			if(ltray == null || ltray.size() == 0)
				throw new BusinessException("行"+WdsWlPubTool.getString_NullAsTrimZeroLen(body.getGeb_crowno())+"无存放托盘信息");
			for(TbGeneralBBVO tray:ltray){
				tmpInvVO = new StockInvOnHandVO();
				tmpInvVO.setPk_corp(SQLHelper.getCorpPk());
				tmpInvVO.setPk_cargdoc(cargdocPK);//货位id
				// 托盘主键
				tmpInvVO.setPplpt_pk(tray.getCdt_pk());
				if(!lTrayID.contains(tray.getCdt_pk())){
					lTrayID.add(tray.getCdt_pk());
				}				
				//仓库
				tmpInvVO.setPk_customize1(getStroreByCargdoc(cargdocPK));
				// 存货档案主键
				tmpInvVO.setPk_invbasdoc(tray.getPk_invbasdoc());
				tmpInvVO.setPk_invmandoc(tray.getPk_invmandoc());
				//生成日期
				tmpInvVO.setCreadate(tray.getCreadate());
			    //失效日期
				tmpInvVO.setExpdate(tray.getExpdate());
				// 批次号
				tmpInvVO.setWhs_batchcode(tray.getGebb_vbatchcode());
				// 回写批次号
				if (null != tray.getGebb_lvbatchcode()
						&& !"".equals(tray.getGebb_lvbatchcode())) {
					tmpInvVO.setWhs_lbatchcode(tray.getGebb_lvbatchcode());
				} else {
					tmpInvVO.setWhs_lbatchcode(WdsWlPubConst.ERP_BANCHCODE);
				}
				tmpInvVO.setWhs_munit(tray.getUnitid());//主计量单位
				tmpInvVO.setWhs_aunit(tray.getAunit());//辅计量单位
				tmpInvVO.setWhs_hsl(tray.getGebb_hsl());//换算率
				// dr
				tmpInvVO.setDr(0);
				tmpInvVO.setWhs_stockpieces(tray.getNinassistnum());////库存辅数量
				tmpInvVO.setWhs_oanum(tray.getNinassistnum());//原始入库实收辅数量
				tmpInvVO.setWhs_stocktonnage(tray.getGebb_num());////库存主数量
				tmpInvVO.setWhs_omnum(tray.getGebb_num());////原始入库主数量
				// 换算率
				tmpInvVO.setWhs_hsl(tray.getGebb_hsl());
				// 单价
				tmpInvVO.setWhs_nprice(tray.getGebb_nprice());
				// 金额
				tmpInvVO.setWhs_nmny(tray.getGebb_nmny());
				
				// 库存检查状态(默认合适)
				tmpInvVO.setSs_pk(WdsWlPubConst.default_inv_state);
				// 库存表状态
				tmpInvVO.setWhs_status(0);
				// 类型
				tmpInvVO.setWhs_type(1);
				tmpInvVO.setPk_headsource(tray.getGeb_pk());//入库单子表id				
				// 来源单据表体主键， 缓存表主键
				tmpInvVO.setPk_bodysource(tray.getGebb_pk());//入库单子子表id
				linvInfor.add(tmpInvVO);
			}
		}
		if(linvInfor.size()>0){		
			try {
				getStockBO().inserStockForIn(linvInfor);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if(e instanceof BusinessException)
					throw (BusinessException)e;
				else
					throw new BusinessException(e);
			}
		}
		//更新托盘状态
		if(lTrayID.size()>0){
			getStockBO().updateTrayState(StockInvOnHandVO.stock_state_use, lTrayID,getTempTableUtil());
		}
	}
	/**
	 * 
	 * @作者：mlr
	 * @说明：通过货位得到仓库信息
	 * @时间：2011-4-8下午06:52:43
	 * @param newBillVo 要操作的单据数据
	 * @param iBdAction 操作状态
	 * @param isNew  是否新增保存
	 * @throws Exception
	 */
	private String getStroreByCargdoc(String cargdoc) throws DAOException{
		
		String sql=" select pk_stordoc from wds_cargdoc c where c.pk_cargdoc='"+cargdoc+"' and isnull(c.dr,0)=0";
		Object pk_storedoc=getDao().executeQuery(sql,new  ColumnProcessor());
		if(pk_storedoc !=null && !pk_storedoc.equals("") ){
			return (String)pk_storedoc;
		}
		return null;
	}

	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 完达山物流项目 
	 *   其他入库保存回写：ERP其他出库
	 *   调拨入库保存回写：ERP调拨出库
	 * @时间：2011-4-8下午06:52:43
	 * @param newBillVo 要操作的单据数据
	 * @param iBdAction 操作状态
	 * @param isNew  是否新增保存
	 * @throws Exception
	 */
	public void writeBackForInBill(OtherInBillVO newBillVo,int  iBdAction,boolean isNew)
			throws BusinessException {
		if(newBillVo == null || newBillVo.getParentVO() == null 
				||newBillVo.getChildrenVO() == null 
				|| newBillVo.getChildrenVO().length==0)
			return;
		TbGeneralBVO[] bodys = (TbGeneralBVO[])newBillVo.getChildrenVO();
		Map<String, UFDouble> numInfor = new HashMap<String, UFDouble>();
		Map<String, UFDouble> numassInfor = new HashMap<String, UFDouble>();
		String sourcetype = null;
		String csourbillbid= null;
		sourcetype = PuPubVO.getString_TrimZeroLenAsNull(bodys[0].getCsourcetype());//一张单子上只有一种来源
		if(sourcetype==null)
			return;
		if(iBdAction == IBDACTION.DELETE){
			for(TbGeneralBVO body:bodys){
				csourbillbid = PuPubVO.getString_TrimZeroLenAsNull(body.getCsourcebillbid());
				if(csourbillbid == null)
					continue;
				if(WdsWlPubConst.BILLTYPE_OTHER_OUT.equalsIgnoreCase(sourcetype)||ScmConst.m_allocationOut.equalsIgnoreCase(sourcetype)){		
						numInfor.put(body.getCsourcebillbid(),PuPubVO.getUFDouble_NullAsZero(body.getGeb_anum()).multiply(-1)); 
						numassInfor.put(body.getCsourcebillbid(),PuPubVO.getUFDouble_NullAsZero(body.getGeb_banum()).multiply(-1)); 

				}
			}
		}else if(iBdAction == IBDACTION.SAVE){
			if(isNew){
				for(TbGeneralBVO body:bodys){
					csourbillbid = PuPubVO.getString_TrimZeroLenAsNull(body.getCsourcebillbid());
					if(csourbillbid == null)
						continue;
					if(WdsWlPubConst.BILLTYPE_OTHER_OUT.equalsIgnoreCase(sourcetype)||ScmConst.m_allocationOut.equalsIgnoreCase(sourcetype)){		
							numInfor.put(body.getCsourcebillbid(),PuPubVO.getUFDouble_NullAsZero(body.getGeb_anum())); 
							numassInfor.put(body.getCsourcebillbid(),PuPubVO.getUFDouble_NullAsZero(body.getGeb_banum())); 
					}
				}
			}else{
				String sql = "select geb_anum,geb_banum from tb_general_b where isnull(dr,0)=0 and geb_pk = ?";
				UFDouble noldoutnum = null;
				UFDouble noutassistnum = null;
				SQLParameter para = null;
				for(TbGeneralBVO body:bodys){
					csourbillbid = PuPubVO.getString_TrimZeroLenAsNull(body.getCsourcebillbid());
					if(csourbillbid == null)
						continue;
					if(WdsWlPubConst.BILLTYPE_OTHER_OUT.equalsIgnoreCase(sourcetype)||ScmConst.m_allocationOut.equalsIgnoreCase(sourcetype)){		
						if( VOStatus.DELETED==body.getStatus()){
							numInfor.put(body.getCsourcebillbid(), 
									PuPubVO.getUFDouble_NullAsZero(body.getGeb_anum()).multiply(-1));
							numassInfor.put(body.getCsourcebillbid(),PuPubVO.getUFDouble_NullAsZero(body.getGeb_banum()).multiply(-1)); 
						}else if(VOStatus.UPDATED== body.getStatus()){
							//取出原来的数量
							if(para == null)
								para = new SQLParameter();
							else
								para.clearParams();
							para.addParam(body.getPrimaryKey());
							Object o =getDao().executeQuery(sql,para, WdsPubResulSetProcesser.ARRAYLISTPROCESSOR);
							if( o != null){
								ArrayList<Object[]>  list=(ArrayList<Object[]>) o;
								if(list.size() == 0){
									throw new BusinessException("获取原实入数量异常");
								}
								Object[] colum =(Object[]) list.get(0);
								 noldoutnum =PuPubVO.getUFDouble_NullAsZero(colum[0]);
								 noutassistnum =PuPubVO.getUFDouble_NullAsZero(colum[1]);
							}	
							
							numInfor.put(body.getCsourcebillbid(), 
									PuPubVO.getUFDouble_NullAsZero(body.getGeb_anum()).sub(noldoutnum));
							numassInfor.put(body.getCsourcebillbid(), 
									PuPubVO.getUFDouble_NullAsZero(body.getGeb_banum()).sub(noutassistnum));
						}
					}
				}
			}
		}
			if(numInfor.size()>0){
				String sql = null;
				if(WdsWlPubConst.BILLTYPE_OTHER_OUT.equalsIgnoreCase(sourcetype)){		
					sql = " update tb_outgeneral_b set nacceptnum=coalesce(nacceptnum,0)+? ,nassacceptnum=coalesce(nassacceptnum,0)+?" +
					" where general_b_pk=? ";
				}else if(ScmConst.m_allocationOut.equalsIgnoreCase(sourcetype)){
					sql = " update ic_general_b set ntranoutnum = coalesce(ntranoutnum,0)+? ,ntranoutastnum=coalesce(ntranoutastnum,0)+? where cgeneralbid = ?";
				}
				//				BaseDAO dao = new BaseDAO();
				SQLParameter para = null;
				for(String key:numInfor.keySet()){
					if(para == null)
						para = new SQLParameter();
					else
						para.clearParams();
					para.addParam(numInfor.get(key));
					para.addParam(numassInfor.get(key));
					para.addParam(key);
					getDao().executeUpdate(sql, para);
					para.clearParams();
				}			
	
				//			控制不能超数量发货
				checkNoutNumByOrderNum(sourcetype, numInfor.keySet().toArray(new String[0]));
			}
		}
	
	public void checkNoutNumByOrderNum(String sourcetype,String[] ids) throws BusinessException{
		String sql = null;
		if(WdsWlPubConst.BILLTYPE_OTHER_OUT.equalsIgnoreCase(sourcetype)){		
			sql = "select count(0) from tb_outgeneral_b where (coalesce(noutnum,0) - coalesce(nacceptnum,0)) < 0 " +
			" and general_b_pk in "+getTempTableUtil().getSubSql(ids);
		}else if(ScmConst.m_allocationOut.equalsIgnoreCase(sourcetype)){
			sql = "select count(0) from ic_general_b where (coalesce(noutnum, 0) - coalesce(ntranoutnum, 0) - coalesce(naccumwastnum, 0)) < 0 "+
			" and cgeneralbid in "+getTempTableUtil().getSubSql(ids);
		}
		if(PuPubVO.getInteger_NullAs(getDao().executeQuery(sql, WdsPubResulSetProcesser.COLUMNPROCESSOR), -1)>0){
			throw new BusinessException("实入数量超出来源单据发货数量");
		}
	}

	
	private TempTableUtil ttutil = null;
	private TempTableUtil getTempTableUtil(){
		if(ttutil == null)
			ttutil = new TempTableUtil();
		return ttutil;
	}

	/**
	 * 入库单删除时 存量校验  是否可删除  如果可以  调整库存状态   如果存量调为零 则删除该条记录
	 */
	public List<TbGeneralBBVO> checkOnDelBill(String inbillid,TbGeneralBVO[] bvos)throws BusinessException, DAOException{
		if(PuPubVO.getString_TrimZeroLenAsNull(inbillid)==null)
			return null;
		if(bvos == null || bvos.length == 0)
			return null;
		List<TbGeneralBBVO> ltray = null;//托盘明细数据
		if(bvos[0].getTrayInfor() == null || bvos[0].getTrayInfor().size() == 0){
//			没有托盘明细信息的话  从 数据库获取
			String whereSql = " isnull(dr,0)=0 and geh_pk = '"+inbillid+"'";
		    ltray = (List<TbGeneralBBVO>)getDao().retrieveByClause(TbGeneralBBVO.class, whereSql);
		}else{
			for(TbGeneralBVO bvo:bvos){
				if(ltray == null){
					ltray = new ArrayList<TbGeneralBBVO>();
				}
				if(bvo.getTrayInfor() == null || bvo.getTrayInfor().size() ==0)
					throw new ValidationException("数据异常，获取托盘明细信息为空");
				ltray.addAll(bvo.getTrayInfor());
			}
		}
		
		if(ltray == null || ltray.size() == 0){
			throw new ValidationException("数据异常，获取托盘明细信息为空");
		}
		String pk_corp = SQLHelper.getCorpPk();
		for(TbGeneralBBVO tray:ltray){
//			无论是对于 总仓实际托盘  虚拟托盘 还是分仓  该校验都包括了
			getStockBO().checkOnHandNum(pk_corp, null,tray.getPk_cargdoc(), tray.getCdt_pk(), tray.getPk_invbasdoc(), tray.getGebb_vbatchcode(), tray.getNinassistnum());
		}
		return ltray;
	}
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 入库后的逆操作的  回复处理  需要校验如果其他入库的量 已被出库了 就不能删除了
	 * @时间：2011-4-9下午07:50:28
	 * @param ltrayid
	 * @param inbillid
	 * @throws BusinessException
	 */
	public void deleteOtherInforOnDelBill(String inbillid,TbGeneralBVO[] bvos) throws BusinessException{
//		zhf   modify 20110627   支持虚拟托盘后算法调整
		
//		托盘存量校验   入库的量 是否已经  出库了  如果已 出库该 入库单 不能再次作废
		List<TbGeneralBBVO> ltray = checkOnDelBill(inbillid, bvos);
//		调整托盘状态  如果是总仓实际托盘  状态调整为 未占用    虚拟托盘 和 分仓托盘  状态 不进行维护
		if(ltray!=null && ltray.size()>0){
			try {
				getStockBO().updateStockOnDelForIn(ltray);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if(e instanceof BusinessException)
					throw (BusinessException)e;
				else
					throw new BusinessException(e);
			}			
			
//			存在虚拟托盘  入库的话  校验是否 绑定了 实际托盘 如果绑定  进行解锁
			
			List<String> lbbid = new ArrayList<String>();
			for(TbGeneralBBVO bb:ltray){
				lbbid.add(bb.getPrimaryKey());
			}
			LockTrayBO lockbo = new LockTrayBO(getDao(),getStockBO());
			lockbo.reLockTray(lbbid.toArray(new String[0]));
		}
		
//		删除托盘明细流水表
		if(bvos!=null && bvos.length>0){
			for(TbGeneralBVO bvo:bvos){
				String bodyid = bvo.getGeb_pk();
				getDao().deleteByClause(TbGeneralBBVO.class, " geb_pk = '"+bodyid+"'");
			}
		}
		
	}
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 只能用于对 总仓 实际托盘存量的校验  历史版本 保留zhf2011-06027
	 * @时间：2011-4-11下午01:38:05
	 * @param ltrayid 托盘信息
	 * @param nassnum 
	 * @throws BusinessException
	 */
	public void checkOnHandNum(List<String> ltrayid,UFDouble noutassnum) throws BusinessException{
		List<StockInvOnHandVO> linv = (List<StockInvOnHandVO>)getDao().retrieveByClause(StockInvOnHandVO.class, " pplpt_pk in "+getTempTableUtil().getSubSql(ltrayid.toArray(new String[0])));
		if(linv == null || linv.size() == 0){
			throw new BusinessException("货位存量不足");
		}
		UFDouble naAllAssNum = WdsWlPubTool.DOUBLE_ZERO;
		for(StockInvOnHandVO inv:linv){
			naAllAssNum = naAllAssNum.add(PuPubVO.getUFDouble_NullAsZero(inv.getWhs_stockpieces()));////库存辅数量
		}
		if(noutassnum.doubleValue()>naAllAssNum.doubleValue())
			throw new  BusinessException("货位存量不足");
	}
}
