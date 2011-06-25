package nc.ui.dm;

import java.util.ArrayList;
import java.util.List;
import nc.ui.pub.ToftPanel;
import nc.ui.wl.pub.LongTimeTask;
import nc.vo.dm.PlanDealVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * 发运安排调用后台累
 * @author Administrator
 *
 */
public class PlanDealHealper {
	
	private static String bo = "nc.bs.wl.dm.PlanDealBO";
	
	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 
	 * 查询到货站是当前登录人员绑定的仓库的 发运安排
	 * 如果当前登录人是总仓的 可以查询所有的 发运安排
	 * @时间：2011-3-25上午09:16:20
	 * @param wheresql
	 * @return
	 * @throws Exception
	 */

	public static PlanDealVO[] doQuery(String wheresql) throws Exception{
		PlanDealVO[] dealVos=null;
		Class[] ParameterTypes = new Class[] { String.class };
		Object[] ParameterValues = new Object[] { wheresql };
		Object o = LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME, bo, "doQuery", ParameterTypes, ParameterValues, 2);
		if(o != null){
			dealVos = (PlanDealVO[])o;
		}
		return dealVos;
	}
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 
	 * 发运计划  安排按钮处理方法
	 * @时间：2011-3-25下午02:59:20
	 */
	public static void doDeal(List ldata, ToftPanel tp) throws Exception {
		if (ldata == null || ldata.size() == 0)
			return;
		if(tp instanceof PlanDealClientUI){
			PlanDealClientUI ui = (PlanDealClientUI)tp;
			List<String> infor = new ArrayList<String>();
			infor.add(ui.cl.getUser());
			infor.add(ui.cl.getCorp());
			infor.add(ui.cl.getLogonDate().toString());
			Class[] ParameterTypes = new Class[] { List.class,List.class };
			Object[] ParameterValues = new Object[] { ldata,infor};
			LongTimeTask.calllongTimeService(WdsWlPubConst.WDS_WL_MODULENAME, tp,
					"正在处理...", 2, bo, null, "doDeal", ParameterTypes,
					ParameterValues);
		}
	
	}
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 发运计划录入 整单关闭  暂未实现
	 * @时间：2011-6-25下午09:29:06
	 * @param billid
	 * @return
	 * @throws Exception
	 */
	public static HYBillVO closeBill(String billid) throws Exception{
//		if(lpara == null || lpara.size() ==0)
//			return null;
//		HYBillVO newbill = null;
//		Class[] ParameterTypes = new Class[] { java.util.List.class };
//		Object[] ParameterValues = new Object[] { lpara };
//		Object o = LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME, bo, "closeRows", ParameterTypes, ParameterValues, 2);
//		if(o != null){
//			newbill  = (HYBillVO)o;
//		}
		return null;
	}
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 计划录入行关闭
	 * @时间：2011-6-25下午09:12:02
	 * @param lpara
	 * @return
	 * @throws Exception
	 */
	public static HYBillVO closeRows(List lpara) throws Exception{
		if(lpara == null || lpara.size() ==0)
			return null;
		HYBillVO newbill = null;
		Class[] ParameterTypes = new Class[] { java.util.List.class };
		Object[] ParameterValues = new Object[] { lpara };
		Object o = LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME, bo, "closeRows", ParameterTypes, ParameterValues, 2);
		if(o != null){
			newbill  = (HYBillVO)o;
		}
		return newbill;
	}
}
