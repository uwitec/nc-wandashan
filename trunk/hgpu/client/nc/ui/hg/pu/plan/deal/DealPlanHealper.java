package nc.ui.hg.pu.plan.deal;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import nc.ui.hg.pu.pub.LongTimeTask;
import nc.ui.pub.ToftPanel;
import nc.vo.hg.pu.plan.deal.PlanDealVO;
import nc.vo.hg.pu.pub.PlanApplyInforVO;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pub.session.ClientLink;

//zhf  �ƻ����� ǰ��̨���� ������    
public class DealPlanHealper {

	private static String bo = "nc.bs.hg.pu.plan.deal.DealPlanBO";
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ���ƻ�����  ��ѯ�¼�������ƻ�
	 * 2010-11-24����10:27:12
	 * @param whereSql
	 * @param cl
	 * @param iplantype
	 * @return
	 * @throws Exception
	 */
	public static PlanDealVO[] queryPlans(String whereSql,ClientLink cl,String splantype) throws Exception{
		Class[] ParameterTypes = new Class[]{String.class,ClientLink.class,String.class};
		Object[] ParameterValues = new Object[]{whereSql,cl,splantype};
		Object o = LongTimeTask.callRemoteService("pu",bo, "queryPlanForDeal", ParameterTypes, ParameterValues, 2);
		return o==null?null:(PlanDealVO[])o;
	}

	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ���ƻ�����---���ƽ����������
	 * 2010-11-24����10:27:35
	 * @param tp
	 * @param datas
	 * @param cl
	 * @param iplantype
	 * @return
	 * @throws Exception
	 */
	public static PlanDealVO[] balancePlans(ToftPanel tp,PlanDealVO[] datas,ClientLink cl,String splantype) throws Exception{
		Class[] ParameterTypes = new Class[]{PlanDealVO[].class,ClientLink.class,String.class};
		Object[] ParameterValues = new Object[]{datas,cl,splantype};
		Object o = LongTimeTask.
		calllongTimeService("pu", tp, "����ƽ����...", 1, bo, null, "balancePlan", ParameterTypes, ParameterValues);
		if(o == null){
			throw new BusinessException("ƽ�⴦���쳣����������Ϊ��");
		}
		return (PlanDealVO[])o;
	}

	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ���ƻ�����  --- ���ϼ��ύ�ƻ�
	 * 2010-11-24����10:28:02
	 * @param tp
	 * @param datas
	 * @param cl
	 * @param iplantype
	 * @throws Exception
	 */
	public static void commitPlans(ToftPanel tp,PlanDealVO[] datas,PlanApplyInforVO cl,String splantype,List lseldata) throws Exception{
		Class[] ParameterTypes = new Class[]{List.class,PlanApplyInforVO.class,String.class,List.class};
		Object[] ParameterValues = new Object[]{Arrays.asList(datas),cl,splantype,lseldata};
		LongTimeTask.
		calllongTimeService("pu", tp, "�����ύ...", 1, bo, null, "commitPlan", ParameterTypes, ParameterValues);
	}
	
	/**
	 * 
	 * @author lyf
	 * @˵�������׸ڿ�ҵ��
	 * 2011-11-18 ����10:28:02
	 * @param tp
	 * @param datas
	 * @param cl
	 * @param iplantype
	 * @throws Exception
	 */
	public static void writeBack(ToftPanel tp,List ldata,Map map) throws Exception{
		Class[] ParameterTypes = new Class[]{List.class,Map.class};
		Object[] ParameterValues = new Object[]{ldata,map};
		LongTimeTask.
		calllongTimeService("pu", tp, "���ڻ�д...", 2, bo, null, "writeBack", ParameterTypes, ParameterValues);	
	}
	
	/**
	 * ���û�е�������  ���ܴ���
	 * @author zhw
	 * @˵�������׸ڿ�ҵ��
	 * 2011-2-24����04:05:37
	 * @param lseldata
	 */
	public static void valDreqdate(List<PlanDealVO> ldata) throws Exception{
		Class[] ParameterTypes = new Class[]{List.class};
		Object[] ParameterValues = new Object[]{ldata};
		LongTimeTask.callRemoteService("pu",bo, "valDreqdate", ParameterTypes, ParameterValues, 1);
	}
	
}
