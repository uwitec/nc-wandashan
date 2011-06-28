package nc.bs.wds.w8004040204;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.pf.PfUtilBO;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.itf.uif.pub.IUifService;
import nc.itf.wds.w8004040204.Iw8004040204;
import nc.itf.wds.w80060604.Iw80060604;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.vo.dm.confirm.TbFydnewVO;
import nc.vo.ic.other.out.MyBillVO;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.other.out.TbOutgeneralHVO;
import nc.vo.ic.other.out.TbOutgeneralTVO;
import nc.vo.ic.pub.StockInvOnHandVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.CommonUnit;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;
/***
 * 
 * @author Administrator
 *  出库自动捡货
 */
public class W8004040204Impl implements Iw8004040204 {

	// 获取数据库访问对象
	IVOPersistence ivo = null;
	// 数据库查询对象
	IUAPQueryBS iuap = null;

	public void deleteGeneralTVO(List<TbOutgeneralTVO> itemList)
			throws Exception {
		// TODO Auto-generated method stub
		// 判断参数是否为空
		if (null != itemList && itemList.size() > 0) {
			// 先执行删除 把原有的数据删除掉
			this.getIvo().deleteVOList(itemList);
		}
	}
	private BaseDAO m_dao = null;
	public BaseDAO getDao() {
		if (m_dao == null) {
			m_dao = new BaseDAO();
		}
		return m_dao;
	}



	/**
	 * 添加托盘指定信息
	 */
	public void insertGeneralTVO(List<TbOutgeneralTVO> itemList,
			List<TbOutgeneralTVO> itemList2) throws Exception {
		this.deleteGeneralTVO(itemList);
		if (null != itemList2 && itemList2.size() > 0) {
			// 再重新插入
			this.getIvo().insertVOList(itemList2);
		}
	}

	/**
	 * 添加库存信息
	 */
	public void insertWarehousestock(StockInvOnHandVO item) throws Exception {
		// TODO Auto-generated method stub
		if (null != item) {
			this.getIvo().insertVO(item);
		}
	}

	public List queryGeneralTVO(String cinventoryid, String cfirstbillhid,
			String cfirstbillbid) throws Exception {
		// TODO Auto-generated method stub
		String sWhere = " dr = 0 and cfirstbillhid = '" + cfirstbillhid
				+ "' and pk_invbasdoc ='" + cinventoryid
				+ "' and cfirstbillbid='" + cfirstbillbid + "'";
		// 操作数据库得到结果集
		ArrayList generaltList = (ArrayList) this.getIuap().retrieveByClause(
				TbOutgeneralTVO.class, sWhere);
		// 判断结果集是否为空
		if (null != generaltList && generaltList.size() > 0) {
			return generaltList;
		}
		return null;
	}

//	/**
//	 * 
//	 * @作者：zhf
//	 * @说明：完达山物流项目 根据出库流水信息表 更新 库存存量状态表
//	 * @时间：2011-4-7下午08:39:17
//	 * @param ltray
//	 * @throws Exception
//	 */
//	public  void updateWarehousestock(List<TbOutgeneralTVO> ltray) throws Exception {
//		// TODO Auto-generated method stub
//		if (null == ltray || ltray.size() == 0) {			
//			return;
//		}
//		UFDouble noutnum = WdsWlPubTool.DOUBLE_ZERO; // 实出数量
//		UFDouble noutassistnum = WdsWlPubTool.DOUBLE_ZERO; // 实出辅数量
////		int len = ltray.size();
////		int index = 0;
//		ArrayList linvhand = null;
//		for (TbOutgeneralTVO tray:ltray) {				
//			noutassistnum = tray.getNoutassistnum();
//			noutnum = tray.getNoutnum();				
//
//			//			if (index == len - 1) {
//			String sWhere = " isnull(dr,0) = 0 and whs_status = 0 and pplpt_pk = '"
//				+ tray.getCdt_pk()
//				+ "' and pk_invbasdoc = '"
//				+ tray.getPk_invbasdoc() + "' and whs_batchcode = '"+tray.getVbatchcode()+"'";
//			// 操作数据库得到结果集
//			linvhand = (ArrayList) getIuap()
//			.retrieveByClause(StockInvOnHandVO.class, sWhere);
//			// 判断结果集是否为空
//			//				if (null != linvhand && generaltList.size() > 0) {
//			if(linvhand == null || linvhand.size() == 0)
//				return;
//
//			StockInvOnHandVO item = (StockInvOnHandVO) linvhand.get(0);
//			UFDouble nhandnum = PuPubVO.getUFDouble_NullAsZero(item.getWhs_stocktonnage());
//			UFDouble nhandassnum = PuPubVO.getUFDouble_NullAsZero(item.getWhs_stockpieces());
//			if (noutassistnum.equals(nhandassnum)							
//					&& noutnum.equals(nhandnum)){
//				item.setWhs_status(1);
//				updateBdcargdocTray(item.getPplpt_pk());//将托盘状态更新为  空盘  
//			}
//			item.setWhs_stockpieces(nhandassnum.sub(noutassistnum));
//			item.setWhs_stocktonnage(nhandnum.sub(noutnum));
//			item.setStatus(VOStatus.UPDATED);
//			this.updateWarehousestock(item);
//		}
//
//	}

	/**
	 * 更新库存表
	 */
	public void updateWarehousestock(StockInvOnHandVO item) throws Exception {
		// TODO Auto-generated method stub
		if (null != item) {
			if(PuPubVO.getUFDouble_NullAsZero(item.getWhs_stockpieces()).doubleValue()<0|| PuPubVO.getUFDouble_NullAsZero(item.getWhs_stocktonnage()).doubleValue()<0){
				throw new BusinessException("出现负结存");
			}
			this.getIvo().updateVO(item);
		}
	}

	public IVOPersistence getIvo() {
		return (IVOPersistence) NCLocator.getInstance().lookup(
				IVOPersistence.class.getName());
	}
	public IUAPQueryBS getIuap() {
		return (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
	}

	// 更新托盘状态
	public void updateBdcargdocTray(String trayPK) throws Exception {
		// TODO Auto-generated method stub
		if (null != trayPK && !"".equals(trayPK)) {
			PersistenceManager sessioManager = null;
			try {
				sessioManager = PersistenceManager.getInstance();
				JdbcSession jdbcSession = sessioManager.getJdbcSession();

				String sql = "update bd_cargdoc_tray set cdt_traystatus = " + 0
						+ " where cdt_pk='" + trayPK + "'";
				jdbcSession.executeUpdate(sql);

			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw e;
			}
		}
	}
	
	private TempTableUtil ttutil = null;
	private TempTableUtil getTempTableUtil(){
		if(ttutil == null)
			ttutil = new TempTableUtil();
		return ttutil;
	}

	/*
	 * 根据商品主键进行自动取货(non-Javadoc)--不再使用，因为之前EJB部署需要用到这个方法，所有保留
	 * 
	 * @see nc.itf.wds.w8004040204.Iw8004040204#autoPickAction(java.lang.String)
	 */
	public Map<String,List<TbOutgeneralTVO>> autoPickAction(String pk_user, TbOutgeneralBVO[] bodys,
			String pk_stordoc) throws Exception {
		if(bodys == null || bodys.length == 0)
			return null;
		TbOutgeneralTVO trayVo = null;
		List<StockInvOnHandVO> lware = null; 
		Map<String,List<TbOutgeneralTVO>> trayInfor= new HashMap<String,List<TbOutgeneralTVO>>();//<行号，托盘>
		Map<String,UFDouble> usedNum  = new HashMap<String,UFDouble> ();//当前表体抢先出的货物<库存状态状态表主键，当前托盘已经出的数量>
		List<TbOutgeneralTVO> ltray = null;//可以使用的托盘
		for(TbOutgeneralBVO body:bodys){
			if(body == null)
				throw new BusinessException("非法传入数据");
			String oldCdt= getOldCdt(body);
			lware = CommonUnit.getStockDetailByPk_User(pk_user,null,body,oldCdt);//获得当前可用的存货
			if(lware == null||lware.size()==0)
				throw new BusinessException("行号："+body.getCrowno()+",货品无存量");
			UFDouble asum = PuPubVO.getUFDouble_NullAsZero(body.getNshouldoutassistnum());//应发辅数量
			StockInvOnHandVO warevo = null;
			for (int j = 0; j < lware.size(); j++) {
				warevo = lware.get(j);
				//减去之前的表体存货已经使用的数量
				if(usedNum.containsKey(warevo.getWhs_pk())){
					UFDouble oldnum = PuPubVO.getUFDouble_NullAsZero(warevo.getWhs_stockpieces());
					warevo.setWhs_stockpieces(oldnum.sub(usedNum.get(warevo.getWhs_pk())));
				}
				//判断是否有库存辅数量
				if(PuPubVO.getUFDouble_NullAsZero(warevo.getWhs_stockpieces()).doubleValue()<=0)
					continue;
				trayVo = new TbOutgeneralTVO();
				UFDouble tmpsum = asum;
				// 库存数量减去应出数量
				asum = warevo.getWhs_stockpieces().sub(asum);
				// 如果剩余数量大于等于0 当前托盘存货量够出
				if (asum.doubleValue()> WdsWlPubTool.DOUBLE_ZERO.doubleValue()) {
					// 把当前出货量存储实出数量
					trayVo.setNoutassistnum(tmpsum);
					// 计算实出主数量
					trayVo.setNoutnum(tmpsum.multiply(PuPubVO.getUFDouble_NullAsZero(body.getHsl())));
					usedNum.put(warevo.getWhs_pk(), tmpsum);
				} else {
					// 如果剩余数量小于0 当前托盘存货量不够出，当前托盘存货量为出货量
					trayVo.setNoutassistnum(warevo
							.getWhs_stockpieces());
					trayVo.setNoutnum(trayVo.getNoutassistnum().multiply(PuPubVO.getUFDouble_NullAsZero(body.getHsl())));
					usedNum.put(warevo.getWhs_pk(), warevo.getWhs_stockpieces());
				}
				trayVo
				.setCfirstbillhid(body
						.getCsourcebillhid()); // 源头主表
				// 源头子表
				trayVo
				.setCfirstbillbid(body
						.getCsourcebillbid());
				trayVo.setVsourcebillcode(body
						.getVsourcebillcode()); // 来源单据号
				trayVo.setCdt_pk(warevo
						.getPplpt_pk()); // 托盘主键
//				usedTary.add(warevo.getPplpt_pk());
				trayVo.setStockpieces(warevo
						.getWhs_stockpieces()); // 库存数量
				trayVo.setStocktonnage(warevo
						.getWhs_stocktonnage()); // 库存辅数量
				trayVo.setPk_invbasdoc(warevo
						.getPk_invbasdoc()); // 存货基本id
				trayVo.setPk_invmandoc(warevo.getPk_invmandoc());// 存货管理id
				trayVo.setVbatchcode(warevo
						.getWhs_batchcode()); // 批次
				trayVo.setWhs_pk(warevo
						.getWhs_pk()); // 库存表主键
				trayVo.setLvbatchcode(warevo
						.getWhs_lbatchcode()); // 来源批次
				trayVo.setNprice(warevo
						.getWhs_nprice()); // 单价
				trayVo.setNmny(warevo
						.getWhs_nmny()); // 金额
				trayVo.setDr(0); // 删除标志
				
				trayVo.setAunit(body.getCastunitid());//辅单位
				trayVo.setUnitid(body.getUnitid());//主单位
				trayVo.setPk_cargdoc(body.getCspaceid());//货位
				trayVo.setHsl(body.getHsl());// 换算
				
	
			
				// 缓存表插入
//				body.addTray(trayVo);
				if(trayInfor.containsKey(body.getCrowno())){
					ltray = trayInfor.get(body.getCrowno());
				}else
					ltray = new ArrayList<TbOutgeneralTVO>();
				ltray.add(trayVo);
				trayInfor.put(body.getCrowno(), ltray);
				if (asum.doubleValue() >= 0) 
					break;
				asum = PuPubVO.getUFDouble_NullAsZero(Math.abs(asum.doubleValue()));
			}
		}
		return trayInfor;
	}
	public Map<String,List<TbOutgeneralTVO>> autoPickAction(String pk_user, AggregatedValueObject bill,
			String pk_stordoc) throws Exception {
		if(bill == null || bill.getChildrenVO() ==null || bill.getChildrenVO().length ==0){
			return null;
		}
		TbOutgeneralHVO head =(TbOutgeneralHVO) bill.getParentVO();
		TbOutgeneralBVO[] bodys =(TbOutgeneralBVO[]) bill.getChildrenVO();
		if(bodys == null || bodys.length == 0)
			return null;
		TbOutgeneralTVO trayVo = null;
		List<StockInvOnHandVO> lware = null; 
		Map<String,List<TbOutgeneralTVO>> trayInfor= new HashMap<String,List<TbOutgeneralTVO>>();//<行号，托盘>
		Map<String,UFDouble> usedNum  = new HashMap<String,UFDouble> ();//当前表体抢先出的货物<库存状态状态表主键，当前托盘已经出的数量>
		List<TbOutgeneralTVO> ltray = null;//可以使用的托盘
		for(TbOutgeneralBVO body:bodys){
			if(body == null)
				throw new BusinessException("非法传入数据");
			String oldCdt= getOldCdt(body);
			//获得符合自动拣货要求的存货的存量信息
			lware = CommonUnit.getStockDetailByPk_User(pk_user,head,body,oldCdt);
			if(lware == null||lware.size()==0)
				throw new BusinessException("行号："+body.getCrowno()+",货品无存量或存量不够");
			UFDouble asum = PuPubVO.getUFDouble_NullAsZero(body.getNshouldoutassistnum());//应发辅数量
			StockInvOnHandVO warevo = null;
			for (int j = 0; j < lware.size(); j++) {
				warevo = lware.get(j);
				//减去之前的表体存货已经使用的数量
				if(usedNum.containsKey(warevo.getWhs_pk())){
					UFDouble oldnum = PuPubVO.getUFDouble_NullAsZero(warevo.getWhs_stockpieces());
					warevo.setWhs_stockpieces(oldnum.sub(usedNum.get(warevo.getWhs_pk())));
				}
				//判断是否有库存辅数量
				if(PuPubVO.getUFDouble_NullAsZero(warevo.getWhs_stockpieces()).doubleValue()<=0)
					continue;
				trayVo = new TbOutgeneralTVO();
				UFDouble tmpsum = asum;
				// 库存数量减去应出数量
				asum = warevo.getWhs_stockpieces().sub(asum);
				// 如果剩余数量大于等于0 当前托盘存货量够出
				if (asum.doubleValue()> WdsWlPubTool.DOUBLE_ZERO.doubleValue()) {
					// 把当前出货量存储实出数量
					trayVo.setNoutassistnum(tmpsum);
					// 计算实出主数量
					trayVo.setNoutnum(tmpsum.multiply(PuPubVO.getUFDouble_NullAsZero(body.getHsl())));
					usedNum.put(warevo.getWhs_pk(), tmpsum);
				} else {
					// 如果剩余数量小于0 当前托盘存货量不够出，当前托盘存货量为出货量
					trayVo.setNoutassistnum(warevo
							.getWhs_stockpieces());
					trayVo.setNoutnum(trayVo.getNoutassistnum().multiply(PuPubVO.getUFDouble_NullAsZero(body.getHsl())));
					usedNum.put(warevo.getWhs_pk(), warevo.getWhs_stockpieces());
				}
				trayVo
				.setCfirstbillhid(body
						.getCsourcebillhid()); // 源头主表
				// 源头子表
				trayVo
				.setCfirstbillbid(body
						.getCsourcebillbid());
				trayVo.setVsourcebillcode(body
						.getVsourcebillcode()); // 来源单据号
				trayVo.setCdt_pk(warevo
						.getPplpt_pk()); // 托盘主键
//				usedTary.add(warevo.getPplpt_pk());
				trayVo.setStockpieces(warevo
						.getWhs_stockpieces()); // 库存数量
				trayVo.setStocktonnage(warevo
						.getWhs_stocktonnage()); // 库存辅数量
				trayVo.setPk_invbasdoc(warevo
						.getPk_invbasdoc()); // 存货基本id
				trayVo.setPk_invmandoc(warevo.getPk_invmandoc());// 存货管理id
				trayVo.setVbatchcode(warevo
						.getWhs_batchcode()); // 批次
				trayVo.setWhs_pk(warevo
						.getWhs_pk()); // 库存表主键
				trayVo.setLvbatchcode(warevo
						.getWhs_lbatchcode()); // 来源批次
				trayVo.setNprice(warevo
						.getWhs_nprice()); // 单价
				trayVo.setNmny(warevo
						.getWhs_nmny()); // 金额
				trayVo.setDr(0); // 删除标志
				
				trayVo.setAunit(body.getCastunitid());//辅单位
				trayVo.setUnitid(body.getUnitid());//主单位
				trayVo.setPk_cargdoc(body.getCspaceid());//货位
				trayVo.setHsl(body.getHsl());// 换算
				
	
			
				// 缓存表插入
//				body.addTray(trayVo);
				if(trayInfor.containsKey(body.getCrowno())){
					ltray = trayInfor.get(body.getCrowno());
				}else
					ltray = new ArrayList<TbOutgeneralTVO>();
				ltray.add(trayVo);
				trayInfor.put(body.getCrowno(), ltray);
				if (asum.doubleValue() >= 0) 
					break;
				asum = PuPubVO.getUFDouble_NullAsZero(Math.abs(asum.doubleValue()));
			}
		}
		return trayInfor;
	}
	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 
	 * @时间：2011-4-20下午04:10:09
	 * @param body
	 * @return  当前占用的托盘id
	 * @throws BusinessException 
	 */
	private String getOldCdt(TbOutgeneralBVO body ) throws BusinessException{
		StringBuffer strWhere =  new StringBuffer(" or o.pplpt_pk in ");
		if(body.getGeneral_b_pk() !=null && !"".equalsIgnoreCase(body.getGeneral_b_pk())){
			String sql ="select cdt_pk  from tb_outgeneral_t where general_b_pk='"+body.getGeneral_b_pk()+"'";
			ArrayList<String> list = (ArrayList<String>)getDao().executeQuery(sql, WdsPubResulSetProcesser.COLUMNLISTROCESSOR);
			strWhere.append(getTempTableUtil().getSubSql(list));
		}else{
			strWhere.append("('aa')");
		}
		return strWhere.toString();
	}
	

	public AggregatedValueObject deleteBD8004040204(
			AggregatedValueObject billVO, Object userObj) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public AggregatedValueObject saveBD8004040204(AggregatedValueObject billVO,
			Object userObj) throws Exception {
		// TODO Auto-generated method stub
		if (null != billVO.getParentVO() && null != billVO.getChildrenVO()
				&& billVO.getChildrenVO().length > 0) {
			List objList = (List) userObj;
			boolean tmp = CommonUnit.operationWare((TbOutgeneralBVO[]) objList
					.get(1), Boolean.parseBoolean(objList.get(3).toString()),
					Boolean.parseBoolean(objList.get(4).toString()), Boolean
							.parseBoolean(objList.get(2).toString()));
			if (!tmp)
				return null;
			Object[] obj = (Object[]) objList.get(5);
			if (Boolean.parseBoolean(obj[0].toString())) {
				// W80060604Impl
				Iw80060604 iw = (Iw80060604) NCLocator.getInstance().lookup(
						Iw80060604.class.getName());
				iw.insertFyd((List) obj[1], (List) obj[2], null);
				List l1 = (List) obj[1];
				TbFydnewVO fyd = (TbFydnewVO) l1.get(0);
				TbOutgeneralBVO[] genb = (TbOutgeneralBVO[]) billVO
						.getChildrenVO();
				if (null != genb && genb.length > 0 && null != fyd) {
					for (int i = 0; i < genb.length; i++) {
						genb[i].setCfirstbillhid(fyd.getFyd_pk());// 设置源头单据表头主键
						genb[i].setVsourcebillcode(fyd.getVbillno());// 设置来源单据号
					}
				}

			}
			nc.itf.uif.pub.IUifService service = (IUifService) NCLocator
					.getInstance().lookup(IUifService.class.getName());
			return service.saveBD(billVO, objList.get(0));

		}

		return null;
	}

	/*
	 * 查询单品实出主辅数量和批次(non-Javadoc)
	 * 
	 * @see nc.itf.wds.w8004040204.Iw8004040204#getNoutNum(java.lang.String,
	 *      java.lang.String)
	 */
	public Object[] getNoutNum(String pk_cfirst, String pk_invbasdoc)
			throws Exception {
		// TODO Auto-generated method stub
		String sql = "select noutnum,noutassistnum,vbatchcode,nprice,nmny,lvbatchcode from tb_outgeneral_t where dr = 0 and cfirstbillbid = '"
				+ pk_cfirst + "' and pk_invbasdoc = '" + pk_invbasdoc + "'";
		ArrayList list = (ArrayList) this.getIuap().executeQuery(sql,
				new ArrayListProcessor());
		if (null != list && list.size() > 0) {
			Object[] results = new Object[6];
			// 实发辅数量
			double noutnum = 0;
			// 实发主数量
			double nassnum = 0;
			// 批次
			StringBuffer batch = new StringBuffer();
			String tmpbat = null;
			double nprice = 0;
			double nmny = 0;
			String lvbatchcode = null;
			for (int i = 0; i < list.size(); i++) {
				Object[] a = (Object[]) list.get(i);
				if (null != a && a.length > 0) {
					// 累加
					if (null != a[0] && !"".equals(a[0])) {
						noutnum = noutnum + Double.parseDouble(a[0].toString());
					}
					if (null != a[1] && !"".equals(a[1])) {
						nassnum = nassnum + Double.parseDouble(a[1].toString());
					}
					// 累加批次去重复
					if (null != a[2] && !"".equals(a[2])) {
						if (null == tmpbat) {
							tmpbat = a[2].toString();
							batch.append(a[2].toString());
						} else {
							if (!tmpbat.equals(a[2].toString())) {
								tmpbat = a[2].toString();
								batch.append(",").append(a[2].toString());
							}
						}
					}
					if (null != a[3] && !"".equals(a[3])) {
						nprice = Double.parseDouble(a[3].toString());
					}
					if (null != a[4] && !"".equals(a[4])) {
						nmny = Double.parseDouble(a[4].toString());
					}
					if (null != a[5] && !"".equals(a[5])) {
						lvbatchcode = a[5].toString();
					}

				}
			}
			results[0] = noutnum;
			results[1] = nassnum;
			results[2] = batch.toString();
			results[3] = nprice;
			results[4] = nmny;
			results[5] = lvbatchcode;
			return results;
		}
		return null;
	}

	public AggregatedValueObject saveBD8004040602(AggregatedValueObject billVO,
			Object userObj) throws Exception {
		if (null != billVO.getParentVO()) {
			StockInvOnHandVO ware = (StockInvOnHandVO) billVO.getParentVO();
			String sql = " update tb_warehousestock set ss_pk = '"
					+ ware.getSs_pk() + "' where " + "pk_invbasdoc = '"
					+ ware.getPk_invbasdoc() + "' " + "and pk_cargdoc = '"
					+ ware.getPk_cargdoc() + "' " + " and whs_batchcode = '"
					+ ware.getWhs_batchcode() + "'";

			ArrayList<?> results = null;
			PersistenceManager sessioManager = null;

			try {
				sessioManager = PersistenceManager.getInstance();
				JdbcSession jdbcSession = sessioManager.getJdbcSession();
				jdbcSession.executeUpdate(sql);
			} catch (DbException e) {
				e.printStackTrace();
			}
			String strWhere = " dr = 0 and ss_pk = '" + ware.getSs_pk()
					+ "' and " + "pk_invbasdoc = '" + ware.getPk_invbasdoc()
					+ "' " + "and pk_cargdoc = '" + ware.getPk_cargdoc() + "' "
					+ " and whs_batchcode = '" + ware.getWhs_batchcode()
					+ "' and rownum = 1";
			ArrayList tmp = (ArrayList) getIuap().retrieveByClause(
					StockInvOnHandVO.class, strWhere);
			if (null != tmp && tmp.size() > 0) {
				StockInvOnHandVO tbware = (StockInvOnHandVO) tmp.get(0);
				billVO.setParentVO(tbware);
				return billVO;
			}

		}

		return null;
	}
	
	private int getSaleAlertDatNo(String invbasid) throws BusinessException{

		// 根据单品主键查询出该单品的销售警戒天数
		String sql = "select "
			+ WdsWlPubConst.IC_INV_SALE_ALERT_DAYNO
			+ " from bd_invbasdoc  where pk_invbasdoc = '"
			+ invbasid
			+ "' and  isnull(dr,0) = 0 ";
		
		int iDay = PuPubVO.getInteger_NullAs(getIuap().executeQuery(sql, WdsPubResulSetProcesser.COLUMNPROCESSOR), -1);
		if(iDay < 0)
			throw new BusinessException("存货档案中没有维护销售警戒天数");
		return iDay;
	}

	public Object whs_processAction(String actionName, String actionName2,
			String billType, String currentDate, AggregatedValueObject vo,
			Object outgeneralVo) throws Exception {
		if (null != outgeneralVo)
			getIvo().updateVO((TbOutgeneralHVO) outgeneralVo);
		nc.bs.pub.pf.PfUtilBO pfutilbo = new PfUtilBO();
		// 保存ERP中销售出库
		Object o = pfutilbo.processAction(actionName, billType, currentDate,
				null, vo, null);
		if (actionName.equals("CANCELSIGN")) {
			boolean oper = Boolean.parseBoolean(((ArrayList) o).get(0)
					.toString());
			if (!oper)
				return null;
			o = pfutilbo.processAction(actionName2, billType, currentDate,
					null, vo, null);
			return o;
		}
		if (actionName.equals("SAVEPICKSIGN")) {
			return o;
		}
		AggregatedValueObject billVO = null;
		Object[] arrayO = (Object[]) o;
		billVO = (AggregatedValueObject) arrayO[0];
		// 销售出库签字
		o = pfutilbo.processAction(actionName2, billType, currentDate, null,
				billVO, null);
		return o;
	}

	public void queryWarehousestock(List itemList) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void saveGeneralVO(MyBillVO myBillVO) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
