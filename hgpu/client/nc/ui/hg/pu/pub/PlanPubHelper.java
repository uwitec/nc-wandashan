package nc.ui.hg.pu.pub;

import java.util.HashMap;
import java.util.Map;

import nc.itf.pp.ask.IAsk;
import nc.ui.pub.ClientEnvironment;
import nc.vo.hg.pu.pact.PactItemVO;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.hg.pu.pub.PlanApplyInforVO;
import nc.vo.pp.ask.VendorVO;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;

public class PlanPubHelper {

	private static String bo = "nc.bs.hg.pu.plan.pub.PlanPubBO";

	private static Map<String, PlanApplyInforVO> planAppInfor = null;

	private static Map<String, PlanApplyInforVO> getPlanAppMap(){
		if(planAppInfor == null){
			planAppInfor = new HashMap<String, PlanApplyInforVO>();
		}
		return planAppInfor;
	}

	

	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）获取申请信息  有当前用户关联带出
	 * 2011-1-25下午01:50:25
	 * @param sLogCorp
	 * @param sLogUser
	 * @return
	 * @throws BusinessException
	 */
	public static PlanApplyInforVO getAppInfor(String sLogCorp,String sLogUser) throws Exception{
		String key = HgPubTool.getString_NullAsTrimZeroLen(sLogCorp)+HgPubTool.getString_NullAsTrimZeroLen(sLogUser);
		if(getPlanAppMap().containsKey(key)){
			return getPlanAppMap().get(key);
		}
		Class[] ParameterTypes = new Class[]{String.class,String.class};
		Object[] ParameterValues = new Object[]{sLogCorp,sLogUser};
		Object o = LongTimeTask.callRemoteService("pu",bo, "getPlanAppInfor", ParameterTypes, ParameterValues, 2);
		if(o == null)
			return null;
		PlanApplyInforVO infor = (PlanApplyInforVO)o;
		getPlanAppMap().put(key, infor);
		return infor;
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）获取合同条款  信息
	 * 2012-2-22下午05:46:38
	 * @param corderid
	 * @return
	 * @throws Exception
	 */
	public static PactItemVO[] getPactItemsForPO(String corderid) throws Exception{
		if(PuPubVO.getString_TrimZeroLenAsNull(corderid)==null)
			return null;
		Class[] ParameterTypes = new Class[]{String.class};
		Object[] ParameterValues = new Object[]{corderid};
		Object o = LongTimeTask.callRemoteService("pu",bo, "getPactItemsForPO", ParameterTypes, ParameterValues, 2);
		if(o == null){
			return null;
		}
		return (PactItemVO[])o;
	}
	
	public static Object savePactItemsForPO(PactItemVO[] pactvos,String headid) throws Exception{
		if(pactvos == null||pactvos.length == 0||PuPubVO.getString_TrimZeroLenAsNull(headid)==null)
			return null;
		
		for(PactItemVO pact:pactvos){
			pact.setPk_ct_manage(headid);
//			pact.setTermtypename("aa");//数据库层有非空校验
		}
		
		//保证vo状态  正确性
		
		Class[] ParameterTypes = new Class[]{PactItemVO[].class};
		Object[] ParameterValues = new Object[]{pactvos};
		Object o = LongTimeTask.callRemoteService("pu",bo, "savePactItemsForPO", ParameterTypes, ParameterValues, 2);
		return o;
	}
	
	public static void vchangevo(nc.vo.hg.pu.pub.HgYearExcelFileVO[] vos,String m_user)throws Exception{
		Class[] ParameterTypes = new Class[]{nc.vo.hg.pu.pub.HgYearExcelFileVO[].class,String.class};
		Object[] ParameterValues = new Object[]{vos,m_user};
		LongTimeTask.callRemoteService("pu",bo, "vHgChangeVo", ParameterTypes, ParameterValues, 2);
	}
}
