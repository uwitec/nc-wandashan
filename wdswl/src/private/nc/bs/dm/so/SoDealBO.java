package nc.bs.dm.so;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nc.bs.dao.BaseDAO;
import nc.bs.pub.pf.PfUtilBO;
import nc.bs.pub.pf.PfUtilTools;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.dm.so.deal.SoDeHeaderVo;
import nc.vo.dm.so.deal.SoDealBillVO;
import nc.vo.dm.so.deal.SoDealVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.wl.pub.WdsWlPubConst;

public class SoDealBO {
	
	private BaseDAO m_dao = null;
	private BaseDAO getDao() {
		if (m_dao == null) {
			m_dao = new BaseDAO();
		}
		// int a = 0;
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
	public SoDealVO[] doQuery(String whereSql) throws Exception {
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
		sql.append(" from so_sale h inner join so_saleorder_b b on h.csaleid = b.csaleid " +
				" inner join so_saleexecute c on b.corder_bid = c.csale_bid " +
				"join  tb_storcubasdoc tbst on tbst.pk_cumandoc = h.creceiptcustomerid");
	
		sql.append(" where");
		sql.append("  isnull(h.dr,0)=0  and isnull(b.dr,0)=0  and isnull(c.dr,0)=0 and isnull(tbst.dr,0)=0");
		if (whereSql != null && whereSql.length() > 0) {
			sql.append(" and " + whereSql);
		}

		Object o = getDao().executeQuery(sql.toString(),
				new BeanListProcessor(SoDealVO.class));
		if (o == null)
			return null;
		ArrayList<SoDealVO> list = (ArrayList<SoDealVO>) o;
		datas = list.toArray(new SoDealVO[0]);

		/**
		 * liuys add 
		 * �������̲�ѯ, �������۶�����ѯERP���۳���,����ж�Ӧ�����۳��ⵥ���Ҹõ���ʶΪ���ⰲ��,�������ۼƻ�����ֻ�ܲ�ѯ���������̵ĵ���
		 *  ����ʵ�����ܺ�(�����Ӧ����ʵ�����) - ���۶����Ѱ����� > �����ŵ�����
		 * 
		 */ 
		
		String[] pks = new String[datas.length];
		Map map = new HashMap();
		for (int i = 0; i < datas.length; i++) {
			pks[i] = datas[i].getCorder_bid();
			StringBuffer generalSql = new StringBuffer(
					" select bb.noutnum, bb.csourcebillbid from ic_general_h hh, ic_general_b bb where hh.cgeneralhid = bb.cgeneralhid  and hh.vuserdef9 = 'Y' and  nvl(bb.dr, 0) = 0 and nvl(hh.dr,0)=0and bb.csourcebillbid = '"
							+ datas[i].getCorder_bid() + "'");
			Object obj = getDao().executeQuery(generalSql.toString(),
					WdsPubResulSetProcesser.ARRAYLISTPROCESSOR);
			// ���1 : ���δ��ѯ�����۳����Ӧ���嵥��,�������ⰲ��,����������������
			if (obj == null)
				continue;
			List<?> kcList = (ArrayList<?>) obj;
			if (kcList.size() == 0)
				continue;
			//���2 : ���ֻ��ѯ��һ����Ӧ���������ĵ���,��ôֱ�Ӵ���map
			if (kcList.size() == 1) {
				Object[] mapobj = (Object[])kcList.get(0);
				map.put(mapobj[1], mapobj[0]);
				continue;
			}
			//���3 : ���ֻ��ѯ�������Ӧ���������ĵ���,��ô���������ϼƲ�����map
			UFDouble hj = new UFDouble();
			for (int j = 0; j < kcList.size(); i++) {
				Object[] mapobj = (Object[])kcList.get(j);
				hj= hj.add(new UFDouble(mapobj[0].toString()));
				if(i == kcList.size()-1)
					map.put(mapobj[1], hj);
			}
		}
		//������ڶ�Ӧ��������ⵥ,��У�����Ƿ�����
		if(map  != null && map.size()>0 ){
			for(int y=0;y<datas.length;y++){
				SoDealVO vo = (SoDealVO)datas[y];
				Object obje = map.get(vo.getCorder_bid());
				if(obje != null && obje.toString().length()>0){
					//���������ϼ�
					UFDouble cksl1 = new UFDouble(obje.toString());
					//�����Ѱ�����
					UFDouble ntaldcnum  = new UFDouble(0);
					if(vo.getNtaldcnum()!=null)
						ntaldcnum = vo.getNtaldcnum();
					//���(����ʵ�����ܺ�(�����Ӧ����ʵ�����) - ���۶����Ѱ����� > �����ŵ�����,�����list,���Բ�ѯ����
					if(cksl1.sub(ntaldcnum).doubleValue()>0){
						vo.setIsxnap(new UFBoolean(true));
						vo.setNnum(cksl1.sub(ntaldcnum));
					}
				}
			}
		}
			
		return datas;
	}

	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ �����ΰ�����������д�����۶��������ۼƷ�������
	 * @ʱ�䣺2011-3-25����04:44:08
	 * @param dealnumInfor
	 * @throws BusinessException
	 */
	private void reWriteDealNumForPlan(Map<String, UFDouble> map)
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
				throw new BusinessException("�����쳣���÷��˼ƻ������ѱ�ɾ���������²�ѯ����");
			};
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
//		//1.����ϸ���� �Ƿ������ �ֵ���Ȼ��ֱ����ִ���������
		SoDealBoUtils util= new SoDealBoUtils();
		CircularlyAccessibleValueObject[][] splitVos = SplitBillVOs.getSplitVOs(
				(CircularlyAccessibleValueObject[]) (ldata
						.toArray(new SoDealVO[0])),
				new String[]{"cbodywarehouseid","disdate"});//���ݷ����ֿ���Ƿ�����ڷֵ�
		if(splitVos == null || splitVos.length==0){
			return ;
		}
		SoDealVO[] vos = null;
		for(int i=0;i<splitVos.length;i++){
			vos = (SoDealVO[])splitVos[i];
			if(vos != null && vos.length>0){
				String pk_outwhouse= PuPubVO.getString_TrimZeroLenAsNull(vos[0].getCbodywarehouseid());
				if(pk_outwhouse  == null){
					throw  new BusinessException("�����ֿⲻ��δ��");
				}
				UFBoolean fisdate = PuPubVO.getUFBoolean_NullAs(vos[0].getDisdate(), UFBoolean.FALSE);
				if(fisdate.booleanValue()){
					util.initInvNumInfor(true,infor.get(1), pk_outwhouse, Arrays.asList(vos));
				}else{
					util.initInvNumInfor(false,infor.get(1), pk_outwhouse, Arrays.asList(vos));

				}
			}
		}
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

	private SoDeHeaderVo getPlanHead(SoDealVO dealVo) {
		if (dealVo == null)
			return null;
		SoDeHeaderVo head = new SoDeHeaderVo();
		String[] names = head.getAttributeNames();
		for (String name : names) {
			head.setAttributeValue(name, dealVo.getAttributeValue(name));
		}
		return head;
	}
	//	

}
