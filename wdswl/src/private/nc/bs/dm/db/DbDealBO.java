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
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2011-3-29����02:08:02
	 * @param whereSql
	 * @return
	 * @throws Exception
	 */
	public DbDealVO[] doQuery(String whereSql,String pk_storedoc,UFBoolean isclose) throws Exception {
		DbDealVO[] datas = null;
		// ʵ�ֲ�ѯ���۶������߼�
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
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2011-3-25����03:58:14
	 * @param ldata
	 * @param infor
	 *            :��¼�ˣ���¼��˾����¼����
	 * @throws Exception
	 */
	public void doDeal(List<DbDealVO> ldata, List<String> infor)
			throws Exception {
		if (ldata == null || ldata.size() == 0)
			return;	
		//��  ����ֿ�  �� ���ֿ�
		CircularlyAccessibleValueObject[][] datas = SplitBillVOs.getSplitVOs(
				(CircularlyAccessibleValueObject[]) (ldata
						.toArray(new DbDealVO[0])),
				WdsWlPubConst.DB_PLAN_DEAL_SPLIT_FIELDS);
		if (datas == null || datas.length == 0)
			return;
		int len = datas.length;
		DbDealVO[] tmpVOs = null;
		//���� �������ŵľۺ�vo   ���� ��������--->�����˵��� ���ݽ��� 
		DbDealBillVO[] planBillVos = new DbDealBillVO[len];
		for (int i = 0; i < len; i++) {
			tmpVOs = (DbDealVO[]) datas[i];
			planBillVos[i] = new DbDealBillVO();
			planBillVos[i].setParentVO(getPlanHead(tmpVOs[0]));
			planBillVos[i].setChildrenVO(tmpVOs);
		}
		//�������ݽ��������ɵ����˵�
		PfParameterVO paraVo = new PfParameterVO();
		paraVo.m_operator = infor.get(0);
		paraVo.m_coId = infor.get(1);
		paraVo.m_currentDate = infor.get(2);
		AggregatedValueObject[] orderVos = (AggregatedValueObject[]) PfUtilTools
				.runChangeDataAry(WdsWlPubConst.WDSB, WdsWlPubConst.WDSG,
						planBillVos, paraVo);
		
		
		//���������˵�����ű�����������˵�
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
