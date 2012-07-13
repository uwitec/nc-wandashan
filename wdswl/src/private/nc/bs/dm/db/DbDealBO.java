package nc.bs.dm.db;
import java.util.ArrayList;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.pub.pf.PfUtilBO;
import nc.bs.pub.pf.PfUtilTools;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.dm.db.deal.DbDeHeaderVo;
import nc.vo.dm.db.deal.DbDealBillVO;
import nc.vo.dm.db.deal.DbDealVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.wl.pub.WdsWlPubConst;

public class DbDealBO {
	
	private BaseDAO m_dao = null;
	private BaseDAO getDao() {
		if (m_dao == null) {
			m_dao = new BaseDAO();
		}
		return m_dao;
	}
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目
	 * @时间：2011-3-29下午02:08:02
	 * @param whereSql
	 * @return
	 * @throws Exception
	 */
	public DbDealVO[] doQuery(String whereSql,String pk_storedoc,UFBoolean isclose) throws Exception {
		DbDealVO[] datas = null;
		// 实现查询销售订单的逻辑
		StringBuffer sql = new StringBuffer();
		sql.append("select  ");
		String[] names = DbDealVO.m_headNames;

		for (String name : names) {
			sql.append(name + ", ");
		}
		names = DbDealVO.m_bodyNames;
		for (String name : names) {
			sql.append(name + ", ");
		}
		sql.append(" 'aaa' ");
		sql.append(" from to_bill h  " );
		sql.append(" inner join to_bill_b b on h.cbillid = b.cbillid");
		sql.append(" where ");
		sql.append("  isnull(h.dr,0)=0  and isnull(b.dr,0)=0  ");
		
//		------------------------------------------------zhf add
//		sql.append(" and coalesce(h.bisclose,'N') = ");
//		if(isclose.booleanValue())
//			sql.append(" 'Y' ");
//		else
//			sql.append(" 'N' ");
//		------------------------------------------------
		
		if (whereSql != null && whereSql.length() > 0) {
			sql.append(" and " + whereSql);
		}
		sql.append(" and b.coutwhid ='"+pk_storedoc+"'");
		Object o = getDao().executeQuery(sql.toString(),
				new BeanListProcessor(DbDealVO.class));
		if (o == null)
			return null;
		ArrayList<DbDealVO> list = (ArrayList<DbDealVO>) o;
		datas = list.toArray(new DbDealVO[0]);
		return datas;
	}
	
	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目
	 * @时间：2011-3-25下午03:58:14
	 * @param ldata
	 * @param infor
	 *            :登录人，登录公司，登录日期
	 * @throws Exception
	 */
	public void doDeal(List<DbDealVO> ldata, List<String> infor)
			throws Exception {
		if (ldata == null || ldata.size() == 0)
			return;	
		//按  出库仓库  和 入库仓库
		CircularlyAccessibleValueObject[][] datas = SplitBillVOs.getSplitVOs(
				(CircularlyAccessibleValueObject[]) (ldata
						.toArray(new DbDealVO[0])),
				WdsWlPubConst.DB_PLAN_DEAL_SPLIT_FIELDS);
		if (datas == null || datas.length == 0)
			return;
		int len = datas.length;
		DbDealVO[] tmpVOs = null;
		//构造 调拨安排的聚合vo   便于 调拨安排--->调出运单的 数据交换 
		DbDealBillVO[] planBillVos = new DbDealBillVO[len];
		for (int i = 0; i < len; i++) {
			tmpVOs = (DbDealVO[]) datas[i];
			planBillVos[i] = new DbDealBillVO();
			planBillVos[i].setParentVO(getPlanHead(tmpVOs[0]));
			planBillVos[i].setChildrenVO(tmpVOs);
		}
		//进行数据交换，生成调出运单
		PfParameterVO paraVo = new PfParameterVO();
		paraVo.m_operator = infor.get(0);
		paraVo.m_coId = infor.get(1);
		paraVo.m_currentDate = infor.get(2);
		AggregatedValueObject[] orderVos = (AggregatedValueObject[]) PfUtilTools
				.runChangeDataAry(WdsWlPubConst.WDSB, WdsWlPubConst.WDSG,
						planBillVos, paraVo);
		
		
		//调用销售运单保存脚本，保存调出运单
		if (orderVos == null || orderVos.length == 0) {
			return;
		}	
		PfUtilBO pfbo = new PfUtilBO();
		for (AggregatedValueObject bill : orderVos) {
			pfbo.processAction(WdsWlPubConst.DM_PLAN_TO_ORDER_SAVE,
					WdsWlPubConst.WDSG, infor.get(2), null, bill, null);
		}
	}

	private DbDeHeaderVo getPlanHead(DbDealVO dealVo) {
		if (dealVo == null)
			return null;
		DbDeHeaderVo head = new DbDeHeaderVo();
		String[] names = head.getAttributeNames();
		for (String name : names) {
			head.setAttributeValue(name, dealVo.getAttributeValue(name));
		}
		return head;
	}
	//	
	
//	zhf add ---------------------------2012 1 4
	public void doCloseOrOpen(String[] orderbids,UFBoolean isclose) throws BusinessException{
		if(orderbids == null || orderbids.length == 0)
			return;
		TempTableUtil ut = new TempTableUtil();
		String sflag = isclose.booleanValue()?"N":"Y";//---------------
		String sql = "select csaleid from so_sale where isnull(dr,0) = 0 " +
				" and coalesce(bisclose,'N') = '"+sflag+"'" +
				" and csaleid in "+ut.getSubSql(orderbids);
		List ldata = (List)getDao().executeQuery(sql, WdsPubResulSetProcesser.COLUMNLISTROCESSOR);
		if(ldata == null || ldata.size() == 0)
			return;
		sflag = isclose.booleanValue()?"Y":"N";//-------------------
		sql = "update so_sale set bisclose = '"+sflag+"' where csaleid in "+ut.getSubSql((ArrayList)ldata);
		getDao().executeUpdate(sql);
	}

}
