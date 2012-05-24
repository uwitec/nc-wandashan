package nc.ui.dm.so.order;

import nc.ui.pub.ToftPanel;
import nc.ui.wl.pub.LongTimeTask;
import nc.vo.pub.SuperVO;
import nc.vo.wl.pub.WdsWlPubConst;

public class TranOrderHelper {

	private static String bo = "nc.bs.wl.so.order.SoorderBO";
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2012-5-23����05:37:18
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
		calllongTimeService(WdsWlPubConst.WDS_WL_MODULENAME, tp, "���ڲ�ѯ...", 1, bo, null, "querySoOrders", ParameterTypes, ParameterValues);
		return (SuperVO[])o;
	}
}
