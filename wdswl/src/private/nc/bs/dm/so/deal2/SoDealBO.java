package nc.bs.dm.so.deal2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nc.bs.dao.BaseDAO;
import nc.bs.logging.Logger;
import nc.bs.pub.pf.PfUtilBO;
import nc.bs.pub.pf.PfUtilTools;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.dm.so.deal2.SoDealBillVO;
import nc.vo.dm.so.deal2.SoDealHeaderVo;
import nc.vo.dm.so.deal2.SoDealVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.trade.voutils.IFilter;
import nc.vo.trade.voutils.VOUtil;
import nc.vo.wl.pub.Wds2WlPubConst;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;

public class SoDealBO {

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
	public SoDealVO[] doQuery(String whereSql,String pk_storedoc) throws Exception {
		SoDealVO[] datas = null;
		// ʵ�ֲ�ѯ���˼ƻ����߼�
		StringBuffer sql = new StringBuffer();
		sql.append("select  ");
		String[] names = SoDealVO.m_headNames;
		for (String name : names) {
			sql.append(name + ", ");
		}
		names = SoDealVO.m_bodyNames;
		for (String name : names) {
			sql.append(name + ", ");
		}
		sql.append(" 'aaa' ");
		sql.append(" from so_sale h  " );
		sql.append(" inner join so_saleorder_b b on h.csaleid = b.csaleid");
		sql.append(" where ");
		sql.append("  isnull(h.dr,0)=0  and isnull(b.dr,0)=0  ");
		
		
//		------------------------------------------------zhf add
//		sql.append(" and coalesce(h.bisclose,'N') = 'N' ");//��ѯ��δ �رյ�  ������  zhf
//		if(isclose.booleanValue())
//			sql.append(" 'Y' ");
//		else
//			sql.append(" 'N' ");
//		------------------------------------------------
		
//		zhf modify  ֧��  �������۶������밲��
		sql.append(" and ( coalesce(h.bisclose,'N') = 'N' " +
				"and coalesce(h."+Wds2WlPubConst.so_virtual+",'"+Wds2WlPubConst.so_virtual_value_no+"') == '"+Wds2WlPubConst.so_virtual_value_no+"' ");
		sql.append(" or h."+Wds2WlPubConst.so_virtual+" = '"+Wds2WlPubConst.so_virtual_value_yes+"' )");
		
		
		
		if (whereSql != null && whereSql.length() > 0) {
			sql.append(" and " + whereSql);
		}
		sql.append(" and h.creceiptcustomerid in(");
		sql.append(" select tb_storcubasdoc.pk_cumandoc  ");
		sql.append(" from wds_storecust_h ");
		sql.append(" join tb_storcubasdoc ");
		sql.append(" on wds_storecust_h.pk_wds_storecust_h = tb_storcubasdoc.pk_wds_storecust_h ");
		sql.append(" where wds_storecust_h.pk_stordoc ='"+pk_storedoc+"'");
		sql.append("  and isnull(wds_storecust_h.dr,0)=0");
		sql.append("  and isnull(tb_storcubasdoc.dr,0)=0");
		sql.append(" )");
		Object o = getDao().executeQuery(sql.toString(),
				new BeanListProcessor(SoDealVO.class));
		if (o != null) {
			ArrayList<SoDealVO> list = (ArrayList<SoDealVO>) o;
			datas = list.toArray(new SoDealVO[0]);
		}
		
//		/**
//		 * liuys add 
//		 * �������̲�ѯ, �������۶�����ѯERP���۳���,����ж�Ӧ�����۳��ⵥ���Ҹõ���ʶΪ���ⰲ��,�������ۼƻ�����ֻ�ܲ�ѯ���������̵ĵ���
//		 *  ����ʵ�����ܺ�(�����Ӧ����ʵ�����) - ���۶����Ѱ����� > �����ŵ�����
//		 * 
//		 */ 
//		String pk = null;
//		for (int i = 0; i < datas.length; i++) {
//			pk = PuPubVO.getString_TrimZeroLenAsNull(datas[i].getCorder_bid());
//			if(pk == null){
//				continue;
//			}
//			StringBuffer generalSql = new StringBuffer();
//			generalSql.append(" select sum(ic_general_b.noutnum) from ic_general_h ");
//			generalSql.append(" join ic_general_b on ");
//			generalSql.append(" ic_general_h.cgeneralhid = ic_general_b.cgeneralhid");
//			generalSql.append(" where isnull(ic_general_h.dr,0)=0 ");
//			generalSql.append(" and isnull(ic_general_b.dr,0)=0 ");
//			generalSql.append(" and ic_general_h.fbillflag='"+BillStatus.AUDIT+"' ");
//			generalSql.append(" and ic_general_h."+WdsWlPubConst.WDS_IC_ZG_DEF+"='"+WdsWlPubConst.WDS_IC_FLAG_wu+"'");//�������
//			generalSql.append(" and ic_general_b.csourcebillbid='"+pk + "'");
//			UFDouble noutnum =PuPubVO.getUFDouble_NullAsZero( getDao().executeQuery(generalSql.toString(),
//					WdsPubResulSetProcesser.COLUMNPROCESSOR));
//			// ���1 : ���δ��ѯ�����۳����Ӧ���嵥��,�������ⰲ��,����������������
//			if (noutnum.doubleValue() ==0)
//				continue;
//			//�����Ѱ�����
//			UFDouble ntaldcnum = PuPubVO.getUFDouble_NullAsZero( datas[i].getNtaldcnum());
//			UFDouble nnum = PuPubVO.getUFDouble_NullAsZero(datas[i].getNnumber());
//			UFDouble npacknum = PuPubVO.getUFDouble_NullAsZero(datas[i].getNpacknumber());
//			UFDouble nhsl = new UFDouble(1);
//			if(npacknum.doubleValue() >0){
//				nhsl = nnum.div(npacknum);
//			}
//			//���(����ʵ�����ܺ�(�����Ӧ����ʵ�����) - ���۶����Ѱ����� > �����ŵ�����,�����list,���Բ�ѯ����
//			if(noutnum.sub(ntaldcnum).doubleValue()>0){
//				datas[i].setIsxnap(UFBoolean.TRUE);
//				UFDouble nlefnum = noutnum.sub(ntaldcnum);
//				datas[i].setNnumber(nlefnum);
//				if(npacknum.doubleValue() >0){
//					datas[i].setNpacknumber(nlefnum.div(nhsl));
//				}
//			}
//		}
//		SoDealBoUtils2 util = new SoDealBoUtils2();
//		util.arrangStornumout(SQLHelper.getCorpPk(), pk_storedoc, datas);
		return datas;
	}

	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ �����ΰ�����������д�����۶��������ۼƷ�������
	 * @ʱ�䣺2011-3-25����04:44:08
	 * @throws BusinessException
	 */
	public void reWriteDealNumForPlan(Map<String, UFDouble> map)
			throws BusinessException {

		if (map == null || map.size() == 0)
			return;
		for (Entry<String, UFDouble> entry : map.entrySet()) {
			String sql = "update so_saleorder_b set "
					+ WdsWlPubConst.DM_SO_DEALNUM_FIELD_NAME
					+ " = coalesce("
					+ WdsWlPubConst.DM_SO_DEALNUM_FIELD_NAME
					+ ",0)+"
					+ PuPubVO.getUFDouble_NullAsZero(entry.getValue())
							.doubleValue() + " where corder_bid='"
					+ entry.getKey() + "'";
			if (getDao().executeUpdate(sql) == 0) {
				throw new BusinessException("�����쳣�������۶��������ѱ�ɾ���������²�ѯ����");
			}
			;
			// ���ƻ�������nplannum�����ۼư�������(ndealnum)�Ƚ�
			// ����ۼư����������ڼƻ��������׳��쳣
			String sql1 = "select count(0) from so_saleorder_b where corder_bid='"
					+ entry.getKey()
					+ "'and (coalesce(nnumber,0)-coalesce("
					+ WdsWlPubConst.DM_SO_DEALNUM_FIELD_NAME + ",0))>=0";
			Object o = getDao().executeQuery(sql1,
					WdsPubResulSetProcesser.COLUMNPROCESSOR);
			if (o == null) {
				throw new BusinessException("���ƻ�������");
			}
		}
	}
	
	class FielterMinNum implements IFilter{
		private SoDealCol col = null;
		public FielterMinNum(SoDealCol col){
			super();
			this.col = col;
		}

		public boolean accept(Object o) {
			// TODO Auto-generated method stub
			if(!(o instanceof SoDealBillVO))
				return false;
			SoDealBillVO bill = (SoDealBillVO)o;
			//���ⰲ�Ų�������С������
			if(PuPubVO.getUFBoolean_NullAs(bill.getHeader().getIsonsell(), UFBoolean.FALSE).booleanValue()){
				return true;
			}
			String ccustid = PuPubVO.getString_TrimZeroLenAsNull(bill.getHeader().getCcustomerid());
			String pk_store = PuPubVO.getString_TrimZeroLenAsNull(bill.getHeader().getCbodywarehouseid());
			if(ccustid == null||pk_store == null)
			    return false;
			UFDouble nminnum = null;
			try {
				nminnum = col.getMinSendNumForCust(ccustid,pk_store);
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Logger.debug("��ȡ�ͻ���С���������ó����쳣���ͻ�id��"+ccustid);
				Logger.debug(e);
				return false;
			}
			
			bill.getHeader().setNminnum(nminnum);//��������С������
			
			UFDouble nallnum = WdsWlPubTool.DOUBLE_ZERO;
			SoDealVO[] bodys = bill.getBodyVos();
			if(bodys == null || bodys.length ==0){
				Logger.info("�ͻ�ID��"+ccustid+"���η�����Ʒ����Ϊ��,�����з������š�");
				return false;
			}
				
			for(SoDealVO body:bodys){
				nallnum = nallnum.add(PuPubVO.getUFDouble_NullAsZero(body.getNnumber())).sub(PuPubVO.getUFDouble_NullAsZero(body.getNtaldcnum()));
			}
			UFDouble npackNum =PuPubVO.getUFDouble_NullAsZero(bodys[0].getNpacknumber()) ;
			if(WdsWlPubConst.sale_send_isass && npackNum.doubleValue()>0){
				nallnum = nallnum.multiply(bodys[0].getNnumber()).div(npackNum);
			}
			if(nallnum.compareTo(nminnum)>0)
				return true;	
			Logger.info("�ͻ�ID��"+ccustid+"���η�����Ϊ"+nallnum+",������С������"+nminnum+"����,���β����з������š�");
			return false;
		}		
	}
	
	
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-7-8����02:03:17
	 * @param bills
	 * @param lpara
	 * @return
	 * @throws Exception
	 */
	public Object doDeal(SoDealBillVO[] bills,List lpara) throws Exception{
		if(bills == null || bills.length == 0)
			return null;		
		Logger.init(WdsWlPubConst.wds_logger_name);
		Logger.info("##########################################################");
		Logger.info("���ۼƻ����ţ������ſͻ�����"+bills.length+"--------------");
//		������С������  �ֲֿ��̰�  �ڵ� ά����  ÿ���ͻ�����С������
		SoDealCol dealCol = new SoDealCol();
		SoDealBillVO[] newbills = (SoDealBillVO[])VOUtil.filter(bills, new FielterMinNum(dealCol));
		if(newbills==null || newbills.length == 0)
			return null;
		Logger.info("�����ſͻ�����Ϊ"+newbills.length);
		Logger.info("���ݿ��������а���....");
//		�Ա���ͬһ�ͻ�ͬһ����Ʒ�����ٴν��кϲ�			
		dealCol.setData(newbills, lpara);
		Object o = dealCol.col();
		return o;
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
	public void doDeal(List<SoDealVO> ldata, List<String> infor)
			throws Exception {
		if (ldata == null || ldata.size() == 0)
			return;
		//2.��д���۶����ۼư�������
		Map<String, UFDouble> map = new HashMap<String, UFDouble>();
		for (int i = 0; i < ldata.size(); i++) {
			String key = ldata.get(i).getCorder_bid();
			UFDouble num = PuPubVO.getUFDouble_NullAsZero(ldata.get(i)
					.getNnum());
			if (map.containsKey(key)) {
				UFDouble oldValue = PuPubVO
						.getUFDouble_NullAsZero(map.get(key));
				map.put(key, oldValue.add(num));
			}
			map.put(key, num);
		}
		reWriteDealNumForPlan(map);
		// 3.���ۼƻ�����vo---�����۶���
		// 3.1��  ����վ �ͻ� �ֵ�
		CircularlyAccessibleValueObject[][] datas = SplitBillVOs.getSplitVOs(
				(CircularlyAccessibleValueObject[]) (ldata
						.toArray(new SoDealVO[0])),
				WdsWlPubConst.SO_PLAN_DEAL_SPLIT_FIELDS);
		if (datas == null || datas.length == 0)
			return;
		int len = datas.length;
		SoDealVO[] tmpVOs = null;
		SoDealBillVO[] planBillVos = new SoDealBillVO[len];
		for (int i = 0; i < len; i++) {
			tmpVOs = (SoDealVO[]) datas[i];
			planBillVos[i] = new SoDealBillVO();
			planBillVos[i].setParentVO(getPlanHead(tmpVOs[0]));
			planBillVos[i].setChildrenVO(tmpVOs);
		}
		//3.2�������ݽ��������������˵�
		PfParameterVO paraVo = new PfParameterVO();
		paraVo.m_operator = infor.get(0);
		paraVo.m_coId = infor.get(1);
		paraVo.m_currentDate = infor.get(2);
		AggregatedValueObject[] orderVos = (AggregatedValueObject[]) PfUtilTools
				.runChangeDataAry(WdsWlPubConst.WDS4, WdsWlPubConst.WDS5,
						planBillVos, paraVo);
		//3.3 ���������˵�����ű������������˵�
		if (orderVos == null || orderVos.length == 0) {
			return;
		}
		PfUtilBO pfbo = new PfUtilBO();
		for (AggregatedValueObject bill : orderVos) {
			pfbo.processAction(WdsWlPubConst.DM_PLAN_TO_ORDER_SAVE,
					WdsWlPubConst.WDS5, infor.get(2), null, bill, null);
		}
	}
	private SoDealHeaderVo getPlanHead(SoDealVO dealVo) {
		if (dealVo == null)
			return null;
		SoDealHeaderVo head = new SoDealHeaderVo();
		String[] names = head.getAttributeNames();
		for (String name : names) {
			head.setAttributeValue(name, dealVo.getAttributeValue(name));
		}
		return head;
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
	public void doHandDeal(List<SoDealVO> ldata, List<String> infor)
			throws Exception {
//		����У��
		
		SoDealBO dealbo = new SoDealBO();
		dealbo.doDeal(ldata, infor);		
	}
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ������ ͬһ�ͻ�ͬ��Ʒ�ֵ�  �ϲ�     �ݲ�ʹ��  ��Ϊ�漰�� ������д������  ���ܺϲ�
	 * @ʱ�䣺2011-7-8����09:56:09
	 * @param bills
	 */
	private void combinData(SoDealBillVO[] bills){
		if(bills == null || bills.length == 0)
			return;
//		SoDealBillVO bill = null;
		Map<String,SoDealVO> dataMap = new HashMap<String, SoDealVO>();
		SoDealVO[] bodys = null;
		SoDealVO tmpBody  =  null;
		String key = null;
		for(SoDealBillVO bill:bills){
			bodys = bill.getBodyVos();
			dataMap.clear();
			for(SoDealVO body:bodys){
				key = WdsWlPubTool.getString_NullAsTrimZeroLen(body.getCinvbasdocid());
				if(dataMap.containsKey(key)){
					tmpBody = dataMap.get(key);
					body.combin(tmpBody);
				}
				dataMap.put(key, body);
			}
			bill.setChildrenVO(dataMap.values().toArray(new SoDealVO[0]));
		}
	}

}
