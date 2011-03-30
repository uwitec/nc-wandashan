package nc.bs.wl.dm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nc.bs.dao.BaseDAO;
import nc.bs.pub.pf.PfUtilBO;
import nc.bs.pub.pf.PfUtilTools;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.dm.PlanDealVO;
import nc.vo.dm.SendplaninVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * ���˼ƻ������̨��
 * @author Administrator
 *
 */
public class PlanDealBO {

	private BaseDAO m_dao = null;
	private BaseDAO getDao(){
		if(m_dao == null){
			m_dao = new BaseDAO();
		}
		return m_dao;
	}
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ 
	 * ��ѯ����վ�ǵ�ǰ��¼��Ա�󶨵Ĳֿ�� ���˰���
	 * �����ǰ��¼�����ֵܲ� ���Բ�ѯ���е� ���˰���
	 * 
	 * wds_sendplanin���˼ƻ�����
	 * wds_sendplanin_b ���˼ƻ��ӱ�
	 * @ʱ�䣺2011-3-25����09:16:20
	 * @param wheresql
	 * @return
	 * @throws Exception
	 */
	public PlanDealVO[] doQuery(String whereSql) throws Exception{
		PlanDealVO[] datas = null;
		//ʵ�ֲ�ѯ���˼ƻ����߼� 
		StringBuffer sql = new StringBuffer();
		sql.append("select  ");
		sql.append(" wds_sendplanin.pk_corp ,");
		sql.append(" wds_sendplanin.dmakedate ,");
		sql.append(" wds_sendplanin.voperatorid ,");
		sql.append(" wds_sendplanin.vapprovenote ,");
		sql.append(" wds_sendplanin.pk_billtype ,");
		sql.append(" wds_sendplanin.vbillstatus ,");
		sql.append(" wds_sendplanin.iplantype ,");
		sql.append(" wds_sendplanin. vemployeeid ,");
		sql.append(" wds_sendplanin.pk_busitype ,");
		sql.append(" wds_sendplanin.pk_sendplanin ,");
		sql.append(" wds_sendplanin.dbilldate ,");
		sql.append(" wds_sendplanin.dmakedate,");
		sql.append(" wds_sendplanin.vbillno vbillno,");
		sql.append(" wds_sendplanin.pk_inwhouse ,");
		sql.append(" wds_sendplanin.pk_deptdoc ,");
		sql.append(" wds_sendplanin.pk_outwhouse ,");
		sql.append(" wds_sendplanin.dapprovedate ,");
		sql.append(" wds_sendplanin.vapproveid ,");
		sql.append(" wds_sendplanin_b.pk_sendplanin_b,");
		sql.append(" wds_sendplanin_b.pk_invmandoc,");
		sql.append(" wds_sendplanin_b.pk_invbasdoc,");
		sql.append(" wds_sendplanin_b.unit,");
		sql.append(" wds_sendplanin_b.assunit,");
		sql.append(" wds_sendplanin_b.nplannum,");
		sql.append(" wds_sendplanin_b.nassplannum,");
		sql.append("wds_sendplanin_b.ndealnum");
		sql.append(" from wds_sendplanin ");
		sql.append(" join wds_sendplanin_b ");
		sql.append(" on wds_sendplanin.pk_sendplanin = wds_sendplanin_b.pk_sendplanin ");
		sql.append(" where"+whereSql);
		Object o = getDao().executeQuery(sql.toString(), new BeanListProcessor(PlanDealVO.class));
		if( o != null){
			ArrayList<PlanDealVO> list = (ArrayList<PlanDealVO>)o;
			datas = list.toArray(new PlanDealVO[0]);
		}
//		StringBuffer strb = new StringBuffer();
//		strb.append("select 'aaa' ");
//		SendplaninVO head = new SendplaninVO();
//		String[] hnames = head.getAttributeNames();
//		for(String name:hnames){
//			strb.append(" ,h."+name);
//		}
//		SendplaninBVO  body = new SendplaninBVO();
//		hnames = body.getAttributeNames();
//		strb.append(",'bbb' ");
//		for(String name:hnames){
//			strb.append(" ,b."+name);
//		}
//		strb.append(" from wds_sendplanin h inner join wds_sendplanin_b b on h.pk_sendplanin = b.pk_sendplanin ");
//		strb.append(" where isnull(h.dr,0)=0 and isnull(b.dr,0)=0  ");
//		if(PuPubVO.getString_TrimZeroLenAsNull(whereSql)!=null)
//			strb.append(" and "+whereSql);
//		
//		List ldata = (List)getDao().executeQuery(strb.toString(), new BeanListProcessor(PlanDealVO.class));
//		if(ldata == null||ldata.size()==0)
//			return null;
//		datas = (PlanDealVO[])ldata.toArray(new PlanDealVO[0]);
//		
		return datas;
	}
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ 
	 * �����ΰ�����������д�����˼ƻ������ۼƷ�������
	 * @ʱ�䣺2011-3-25����04:44:08
	 * @param dealnumInfor
	 * @throws BusinessException
	 */
	private void reWriteDealNumForPlan(Map<String,UFDouble> map) throws BusinessException{

		if(map == null || map.size()==0)
			return;
		for(Entry<String, UFDouble> entry:map.entrySet()){
			String sql = "update wds_sendplanin_b set ndealnum =coalesce(ndealnum,0)+"
				+entry.getValue()+" where pk_sendplanin_b='"+entry.getKey()+"'";
			if(getDao().executeUpdate(sql)==0){
				throw new BusinessException("�����쳣���÷��˼ƻ������ѱ�ɾ���������²�ѯ����");
			};

			//���ƻ�������nplannum�����ۼư�������(ndealnum)�Ƚ�

			//����ۼư����������ڼƻ��������׳��쳣

			String sql1="select count(0) from wds_sendplanin_b where pk_sendplanin_b='"+entry.getKey()+ "'and (coalesce(nplannum,0)-coalesce(ndealnum,0))>=0";			
			Object o=getDao().executeQuery(sql1,WdsPubResulSetProcesser.COLUMNPROCESSOR);
			if(o==null){
				throw new BusinessException("�ۼư����������ܴ��ڼƻ�������");
			}
		}
	}
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-3-25����03:58:14
	 * @param ldata
	 * @param infor :��¼�ˣ���¼��˾����¼����
	 * @throws Exception
	 */
	public void doDeal(List<PlanDealVO> ldata, List<String> infor)
			throws Exception {
		if (ldata == null || ldata.size() == 0)
			return;
		/**
		 * ���ţ����ɷ��˶��� ���˼ƻ��������ɷ��˶���
		 * 
		 * �ƻ����� �ƻ��к� ���ϲ��ƻ��� 
		 * �ƻ��Ͷ���Ϊ1�Զ��ϵ 
		 * �ֵ����� ����վ �ջ�վ��ͬ �����Ǽƻ�����
		 * 		 */
		//��д�ƻ��ۼư�������
		// ���˰���vo---�����˼ƻ�vo
		Map<String,UFDouble> map = new HashMap<String, UFDouble>();
		for(int i=0;i<ldata.size();i++){
			String key = ldata.get(i).getPk_sendplanin_b();
			UFDouble num= PuPubVO.getUFDouble_NullAsZero(ldata.get(i).getNnum());
			if(map.containsKey(key)){
				UFDouble oldValue =PuPubVO.getUFDouble_NullAsZero(map.get(key));
				map.put(key, oldValue.add(num));
			}
			map.put(key, num);
		}
		reWriteDealNumForPlan(map);
		// �� �ƻ��� ����վ �ջ�վ �ֵ�
		CircularlyAccessibleValueObject[][] datas = SplitBillVOs.getSplitVOs(
				(CircularlyAccessibleValueObject[]) (ldata
						.toArray(new PlanDealVO[0])),
				WdsWlPubConst.DM_PLAN_DEAL_SPLIT_FIELDS);
		if (datas == null || datas.length == 0)
			return;
		int len = datas.length;
		PlanDealVO[] tmpVOs = null;
		HYBillVO[] planBillVos = new HYBillVO[len];
		for (int i = 0; i < len; i++) {
			tmpVOs = (PlanDealVO[]) datas[i];
			planBillVos[i] = new HYBillVO();
			planBillVos[i].setParentVO(getPlanHead(tmpVOs[0]));
			planBillVos[i].setChildrenVO(tmpVOs);
		}
		// ���˼ƻ�vo---�����˶���vo
		PfParameterVO paraVo = new PfParameterVO();
		// ������ ���� ���� ������
		HYBillVO[] orderVos = (HYBillVO[]) PfUtilTools.runChangeDataAry(
				WdsWlPubConst.WDS1,
				WdsWlPubConst.WDS3, planBillVos, null);
		// �ֵ�---�����涩��
		if(orderVos ==null || orderVos.length==0){
			return;
		}
		PfUtilBO pfbo = new PfUtilBO();
		for(HYBillVO bill: orderVos){
			pfbo.processAction(WdsWlPubConst.DM_PLAN_TO_ORDER_SAVE, WdsWlPubConst.WDS3, infor.get(2), null, bill, null);
		}
	}
//	private SendplaninBVO[] getPlanBodyVOs(PlanDealVO[] dealVos){
//		if(dealVos == null||dealVos.length==0){
//			return null;
//		}
//		SendplaninBVO[] bodys = new SendplaninBVO[dealVos.length];
//		SendplaninBVO tmp = null;
//		String[] names = null;
//		int index = 0;
//		for(PlanDealVO deal:dealVos){
//			tmp = new SendplaninBVO();
//			if(names == null){
//				names = tmp.getAttributeNames();
//			}
//			for(String name:names){
//				tmp.setAttributeValue(name, deal.getAttributeValue(name));
//			}
//			bodys[index] = tmp;
//			index ++;
//		}
//		return bodys;
//	}
	private SendplaninVO getPlanHead(PlanDealVO dealVo){
		if(dealVo == null)
			return null;
		SendplaninVO head  = new SendplaninVO();
		String[] names  = head.getAttributeNames();
		for(String name:names){
			head.setAttributeValue(name, dealVo.getAttributeValue(name));
		}
		return head;
	}
	
}
