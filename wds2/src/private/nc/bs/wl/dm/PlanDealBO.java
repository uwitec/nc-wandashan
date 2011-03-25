package nc.bs.wl.dm;

import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.pub.pf.PfUtilBO;
import nc.bs.pub.pf.PfUtilTools;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.dm.PlanDealVO;
import nc.vo.dm.SendplaninBVO;
import nc.vo.dm.SendplaninVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.wl.pub.WdsWlPubConst;

public class PlanDealBO {

	private BaseDAO m_dao = null;
	private BaseDAO getDao(){
		if(m_dao == null){
			m_dao = new BaseDAO();
		}
		return m_dao;
	}
	
	public PlanDealVO[] doQuery(String whereSql) throws Exception{
		PlanDealVO[] datas = null;
		//ʵ�ֲ�ѯ���˼ƻ����߼�   
		
		StringBuffer strb = new StringBuffer();
		strb.append("select 'aaa' ");
		SendplaninVO head = new SendplaninVO();
		String[] hnames = head.getAttributeNames();
		for(String name:hnames){
			strb.append(" ,h."+name);
		}
		SendplaninBVO  body = new SendplaninBVO();
		hnames = body.getAttributeNames();
		strb.append(",'bbb' ");
		for(String name:hnames){
			strb.append(" ,b."+name);
		}
		strb.append(" from wds_sendplanin h inner join wds_sendplanin_b b on h.pk_sendplanin = b.pk_sendplanin ");
		strb.append(" where isnull(h.dr,0)=0 and isnull(b.dr,0)=0  ");
		if(PuPubVO.getString_TrimZeroLenAsNull(whereSql)!=null)
			strb.append(" and "+whereSql);
		
		List ldata = (List)getDao().executeQuery(strb.toString(), new BeanListProcessor(PlanDealVO.class));
		if(ldata == null||ldata.size()==0)
			return null;
		datas = (PlanDealVO[])ldata.toArray(new PlanDealVO[0]);
		
		return datas;
	}
	
	private void reWriteDealNumForPlan(Map<String,UFDouble> dealnumInfor) throws BusinessException{
		if(dealnumInfor == null || dealnumInfor.size()==0)
			return;
		String sql = "update wds_sendplanin_b set ndealnum = ";
			
	}
	
	public void doDeal(List ldata, String uLogDate, String sLogUser)
			throws Exception {
		if (ldata == null || ldata.size() == 0)
			return;
		/**
		 * ���ţ����ɷ��˶��� ���˼ƻ��������ɷ��˶���
		 * 
		 * �ƻ����� �ƻ��к� ���ϲ��ƻ��� �ƻ��Ͷ���Ϊ1�Զ��ϵ �ֵ����� ����վ �ջ�վ��ͬ �����Ǽƻ�����
		 * 
		 * 
		 * 
		 */
		
		
		//��д�ƻ��ۼư�������
		

		// ���˰���vo---�����˼ƻ�vo
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
				WdsWlPubConst.WDS3, planBillVos, paraVo);
		// �ֵ�---�����涩��
		new PfUtilBO()
				.processBatch(WdsWlPubConst.DM_PLAN_TO_ORDER_PUSHSAVE,
						WdsWlPubConst.WDS3, uLogDate, orderVos,
						null, null);
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
