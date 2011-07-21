package nc.ui.hg.pu.check.pub;

import nc.ui.hg.pu.pub.LongTimeTask;
import nc.vo.hg.pu.check.fund.FUNDSETVO;
import nc.vo.trade.pub.HYBillVO;

public class CheckHelper {

	private static String bo ="nc.bs.hg.pu.check.CheckPubBO";
	
	public static FUNDSETVO onSave(FUNDSETVO[] billVOs,boolean flag) throws Exception{
		Class[] ParameterTypes = new Class[]{HYBillVO.class,boolean.class};
		Object[] ParameterValues = new Object[]{billVOs,flag};
		Object o = LongTimeTask.callRemoteService("pu",bo, "onSave", ParameterTypes, ParameterValues, 2);
		return o==null?null:(FUNDSETVO)o;
	}
}
