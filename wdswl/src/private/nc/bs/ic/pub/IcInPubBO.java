package nc.bs.ic.pub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.jdbc.framework.SQLParameter;
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
				// 存货档案主键
				tmpInvVO.setPk_invbasdoc(tray.getPk_invbasdoc());
				tmpInvVO.setPk_invmandoc(tray.getPk_invmandoc());
				// 批次号
				tmpInvVO.setWhs_batchcode(tray.getGebb_vbatchcode());
				// 回写批次号
				if (null != tray.getGebb_lvbatchcode()
						&& !"".equals(tray.getGebb_lvbatchcode())) {
					tmpInvVO.setWhs_lbatchcode(tray.getGebb_lvbatchcode());
				} else {
					tmpInvVO.setWhs_lbatchcode("2009");
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
			getDao().insertVOArray(linvInfor.toArray(new StockInvOnHandVO[0]));
		}
		//更新托盘状态
		if(lTrayID.size()>0){
			updateTrayState(1, lTrayID);
		}
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
	public void checkOnDelBill(String inbillid,TbGeneralBVO[] bvos)throws BusinessException, DAOException{
		if(PuPubVO.getString_TrimZeroLenAsNull(inbillid)==null)
			return;
		if(bvos == null || bvos.length == 0)
			return;
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
			checkOnHandNum(pk_corp, null,tray.getPk_cargdoc(), tray.getCdt_pk(), tray.getPk_invbasdoc(), tray.getGebb_vbatchcode(), tray.getNinassistnum());
		}	
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
	public void deleteOtherInforOnDelBill(String inbillid,TbGeneralBVO[] bvo) throws BusinessException{
//		zhf   modify 20110627   支持虚拟托盘后算法调整
		
//		托盘存量校验   入库的量 是否已经  出库了  如果已 出库该 入库单 不能再次作废
		checkOnDelBill(inbillid, bvo);
//		调整托盘状态  如果是总仓实际托盘  状态调整为 未占用    虚拟托盘 和 分仓托盘  状态 不进行维护
		
//		删除托盘明细流水表
		
		
		
		List<String> ltrayid = null;
		String sql = "select distinct cdt_pk from tb_general_b_b where isnull(dr,0)=0 and geh_pk = '"+inbillid+"'";
		ltrayid = (List<String>)getDao().executeQuery(sql, WdsPubResulSetProcesser.COLUMNLISTROCESSOR);
		if(ltrayid == null || ltrayid.size()==0)
			return;
		String numSql = " select sum(geb_banum) from  tb_general_b where isnull(dr,0)=0 and geh_pk = '"+inbillid+"'";
		UFDouble nbillAllAssNum = PuPubVO.getUFDouble_NullAsZero(getDao().executeQuery(numSql, WdsPubResulSetProcesser.COLUMNPROCESSOR));
		//		存量校验   
		checkOnHandNum(ltrayid, nbillAllAssNum);
		//清空托盘存量
		if(bvo!=null && bvo.length>0){
			for(int i = 0 ;i<bvo.length;i++){
				String bodyid = bvo[i].getGeb_pk();
				getDao().deleteByClause(StockInvOnHandVO.class, " pplpt_pk in "+getTempTableUtil().getSubSql(ltrayid.toArray(new String[0]))
						+" and pk_headsource='"+bodyid+"'");
			}
		}	
		
		//查出虚拟的托盘对应的托盘主键 cdt_pk
		String sql1=" select distinct cdt_pk from bd_cargdoc_tray where upper(bd_cargdoc_tray.cdt_traycode) like 'XN%' and" +
				" cdt_pk in "+getTempTableUtil().getSubSql(ltrayid.toArray(new String[0]));
		List<String> listxn = (List<String>)getDao().executeQuery(sql1, WdsPubResulSetProcesser.COLUMNLISTROCESSOR);
		List<String> listzc=new ArrayList<String>();		
		if(listxn==null ||listxn.size()<=0){
			listzc=ltrayid;
		}
		if(listxn!=null && listxn.size()>0){
			for(int i=0;i<ltrayid.size();i++){
				if(!listxn.contains(ltrayid.get(i))){
				   listzc.add(ltrayid.get(i));
				}				
			}			
		}		
		//修改托盘状态
		updateTrayState(0, listzc);
		//修改虚拟托盘的状态
		if(listxn!=null&& listxn.size()!=0){
		   List li=new ArrayList();
		   for(int i=0;i<listxn.size();i++){
			List lis= (List) getDao().retrieveByClause(StockInvOnHandVO.class, " pplpt_pk='"+listxn.get(i)+"'");
			if(lis!=null && lis.size()<=0){
				li.add(listxn.get(i));
			}
		   }
		   updateTrayState(0,li);
		}
		//删除单据托盘明细表
		if(bvo!=null && bvo.length>0){
			for(int i = 0 ;i<bvo.length;i++){
				String bodyid = bvo[i].getGeb_pk();
				getDao().deleteByClause(TbGeneralBBVO.class, " geb_pk = '"+bodyid+"'");
			}
		}

	}
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 存量校验   无论是对于 总仓实际托盘  虚拟托盘 还是分仓  该校验都包括了
	 * @时间：2011-6-28下午03:10:47
	 * @param corp 公司
	 * @param pk_cargdoc  货位
	 * @param ctrayid 托盘
	 * @param cinvbasid 存货
	 * @param vbatchcode 批次
	 * @param nassnum 数量   辅数量
	 * @throws BusinessException
	 */
	public void checkOnHandNum(String corp,String cwarehouseid,String pk_cargdoc,String ctrayid,String cinvbasid,String vbatchcode,UFDouble nassnum) throws BusinessException{
		StringBuffer whereSql = new StringBuffer();
		if(PuPubVO.getString_TrimZeroLenAsNull(pk_cargdoc)==null){
			throw new BusinessException("货位不能为空");
		}
		whereSql.append(" isnull(dr,0)=0 and pk_cargdoc = '"+pk_cargdoc+"' and pk_corp = '"+corp+"'");
		if(PuPubVO.getString_TrimZeroLenAsNull(cwarehouseid)!=null)
			whereSql.append(" and pk_customize1 = '"+cwarehouseid+"'");
		if(PuPubVO.getString_TrimZeroLenAsNull(ctrayid)!=null){
			whereSql.append(" and pplpt_pk = '"+ctrayid+"'");
		}
		if(PuPubVO.getString_TrimZeroLenAsNull(cinvbasid)!=null){
			whereSql.append(" and pk_invbasdoc = '"+cinvbasid+"'");
		}
		if(PuPubVO.getString_TrimZeroLenAsNull(vbatchcode)!=null){
			whereSql.append(" and whs_batchcode = '"+vbatchcode+"'");
		}
		
		List<StockInvOnHandVO> linv = (List<StockInvOnHandVO>)getDao().retrieveByClause(StockInvOnHandVO.class, whereSql.toString());
		if(linv == null || linv.size()>0){
			throw new BusinessException("货位存量不足");
		}
		UFDouble naAllAssNum = WdsWlPubTool.DOUBLE_ZERO;
		for(StockInvOnHandVO inv:linv){
			naAllAssNum = naAllAssNum.add(PuPubVO.getUFDouble_NullAsZero(inv.getWhs_stockpieces()));
		}
		if(nassnum.doubleValue()>naAllAssNum.doubleValue())
			throw new  BusinessException("货位存量不足");
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
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 更新托盘状态
	 * @时间：2011-4-9下午08:39:44
	 * @param state
	 * @param ltrayid
	 * @throws BusinessException
	 */
	public void updateTrayState(int state,List<String> ltrayid) throws BusinessException{
		String sql = "update bd_cargdoc_tray set cdt_traystatus = "+state+" where cdt_pk in "+getTempTableUtil().getSubSql(ltrayid.toArray(new String[0]));
		getDao().executeUpdate(sql);
	}
}
