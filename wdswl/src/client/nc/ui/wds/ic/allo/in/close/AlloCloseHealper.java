package nc.ui.wds.ic.allo.in.close;

import nc.ui.pub.ToftPanel;
import nc.ui.wl.pub.LongTimeTask;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.wds.ic.allo.in.close.AlloCloseBillVO;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * 发运安排调用后台累
 * @author Administrator
 *
 */
public class AlloCloseHealper {
	
	private static String bo = "nc.bs.wds.ic.allo.in.close.AlloCloseBO";
	
	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 
	 * 查询到货站是当前登录人员绑定的仓库的 发运安排
	 * 如果当前登录人是总仓的 可以查询所有的 发运安排
	 * @时间：2011-3-25上午09:16:20
	 * @param wheresql
	 * @param pk_stordoc :仓库
	 * @return
	 * @throws Exception
	 */

	public static AlloCloseBillVO[] doQuery(String wheresql,String pk_stordoc,String userid,UFBoolean isclose) throws Exception{
		AlloCloseBillVO[] dealVos=null;
		Class[] ParameterTypes = new Class[] { String.class,String.class,String.class,UFBoolean.class };
		Object[] ParameterValues = new Object[] { wheresql,pk_stordoc ,userid,isclose};
		Object o = LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME, bo, "doQuery", ParameterTypes, ParameterValues, 2);
		if(o != null){
			dealVos = (AlloCloseBillVO[])o;
		}
		return dealVos;
	}
	
	public static void doCloseOrOpen(String[] ids, ToftPanel tp,UFBoolean isclose) throws Exception {
		if (ids == null || ids.length == 0)
			return;
		Class[] ParameterTypes = new Class[] {String[].class,UFBoolean.class};
		Object[] ParameterValues = new Object[] {ids,isclose};
		LongTimeTask.calllongTimeService(WdsWlPubConst.WDS_WL_MODULENAME, tp,
				"正在处理...", 2, bo, null, "doCloseOrOpen", ParameterTypes,
				ParameterValues);
	}
}
