package nc.ui.dm.order;

import nc.ui.pub.ToftPanel;
import nc.ui.wl.pub.LongTimeTask;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.wl.pub.WdsWlPubConst;

public class TranColHelper {

	private static String bo = "nc.bs.wds.dm.order.TranPriceAccount";
	/**
	 * 运费计算
	 * @param tp
	 * @param billvo
	 * @param logDate
	 * @return
	 * @throws Exception
	 */
	public static AggregatedValueObject col(ToftPanel tp,AggregatedValueObject billvo,UFDate logDate,String operator) throws Exception{
		Class[] ParameterTypes = new Class[]{AggregatedValueObject.class,UFDate.class,String.class};
		Object[] ParameterValues = new Object[]{billvo,logDate,operator};
		Object o = LongTimeTask.
		calllongTimeService(WdsWlPubConst.WDS_WL_MODULENAME, tp, "正在核算运费...", 1, bo, null, "colTransCost", ParameterTypes, ParameterValues);
		return (AggregatedValueObject)o;
	}
}
