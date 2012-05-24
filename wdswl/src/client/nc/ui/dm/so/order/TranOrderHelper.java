package nc.ui.dm.so.order;

import nc.ui.pub.ToftPanel;
import nc.ui.wl.pub.LongTimeTask;
import nc.vo.pub.SuperVO;
import nc.vo.wl.pub.WdsWlPubConst;

public class TranOrderHelper {

	private static String bo = "nc.bs.wl.so.order.SoorderBO";
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 
	 * @时间：2012-5-23下午05:37:18
	 * @param tp
	 * @param billvo
	 * @param logDate
	 * @param operator
	 * @return
	 * @throws Exception
	 */
	public static SuperVO[] queryOrders(ToftPanel tp,String strWhere) throws Exception{
		Class[] ParameterTypes = new Class[]{String.class};
		Object[] ParameterValues = new Object[]{strWhere};
		Object o = LongTimeTask.
		calllongTimeService(WdsWlPubConst.WDS_WL_MODULENAME, tp, "正在查询...", 1, bo, null, "querySoOrders", ParameterTypes, ParameterValues);
		return (SuperVO[])o;
	}
}
