package nc.ui.hg.pu.plan.balance;

import java.util.List;

import nc.ui.hg.pu.pub.LongTimeTask;
import nc.ui.pub.ToftPanel;
import nc.vo.hg.pu.plan.balance.PlanMonDealVO;
import nc.vo.scm.pub.session.ClientLink;
   
public class BalancePlanHealper {

	private static String bo = "nc.bs.hg.pu.plan.balance.BalancePlanBO";
	
	/**
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业）计划平衡 查询下级待平衡计划
	 * 2010-11-24上午10:27:12
	 * @param whereSql
	 * @param cl
	 * @param iplantype
	 * @return
	 * @throws Exception
	 */
	public static PlanMonDealVO[] queryPlans(String whereSql,ClientLink cl,String str) throws Exception{
		Class[] ParameterTypes = new Class[]{String.class,ClientLink.class,String.class};
		Object[] ParameterValues = new Object[]{whereSql,cl,str};
		Object o = LongTimeTask.callRemoteService("pu",bo, "queryPlanForDeal", ParameterTypes, ParameterValues, 2);
		return o==null?null:(PlanMonDealVO[])o;
	}

	/**
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业）平衡月计划
	 * 2010-11-24上午10:28:02
	 * @param tp
	 * @param datas
	 * @param cl
	 * @param iplantype
	 * @throws Exception
	 */
	public static void commitBalancePlans(ToftPanel tp,List datas,boolean isbalance) throws Exception{
		Class[] ParameterTypes = new Class[]{List.class,boolean.class};
		Object[] ParameterValues = new Object[]{datas,isbalance};
		LongTimeTask.calllongTimeService("pu", tp, "正在平衡...", 2, bo, null, "balancePlan", ParameterTypes, ParameterValues);
	}
	
	/**
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业）平衡月计划
	 * 2010-11-24上午10:28:02
	 * @param tp
	 * @param datas
	 * @param cl
	 * @param iplantype
	 * @throws Exception
	 */
	public static void aduitPlans(ToftPanel tp,List datas,boolean flag) throws Exception{
		Class[] ParameterTypes = new Class[]{List.class,boolean.class};
		Object[] ParameterValues = new Object[]{datas,flag};
		LongTimeTask.calllongTimeService("pu", tp, "正在审批...", 2, bo, null, "aduitPlans", ParameterTypes, ParameterValues);
	}
}
