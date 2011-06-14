package nc.ui.wds.tranprice.bill;

import nc.ui.pub.ToftPanel;
import nc.ui.wl.pub.LongTimeTask;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.wds.tranprice.bill.SendBillVO;
import nc.vo.wl.pub.WdsWlPubConst;

public class SendBillHelper {

	private static String bo = "nc.bs.wds.tranprice.bill.TranPriceAccount";
	/**
	 * 运费计算
	 * @param tp
	 * @param billvo
	 * @param logDate
	 * @return
	 * @throws Exception
	 */
	public static SendBillVO col(ToftPanel tp,SendBillVO billvo,UFDate logDate,UFBoolean issale) throws Exception{
		billvo.setM_logDate(logDate);
		billvo.setIsSale(issale);
		Class[] ParameterTypes = new Class[]{SendBillVO.class};
		Object[] ParameterValues = new Object[]{billvo};
		Object o = LongTimeTask.
		calllongTimeService(WdsWlPubConst.WDS_WL_MODULENAME, tp, "正在核算运费...", 1, bo, null, "colTransCost", ParameterTypes, ParameterValues);
		return (SendBillVO)o;
	}
}
