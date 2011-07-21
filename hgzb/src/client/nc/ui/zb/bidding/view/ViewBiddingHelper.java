package nc.ui.zb.bidding.view;

import nc.ui.pub.ClientEnvironment;
import nc.ui.zb.pub.LongTimeTask;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zb.bidding.BidViewBillVO;

public class ViewBiddingHelper {
	
private static String bo = "nc.bs.zb.bidding.view.BidViewBO";
	
	public static BidViewBillVO[]  loadDatas(String userid) throws Exception{
		Class[] ParameterTypes = new Class[]{String.class};
		Object[] ParameterValues = new Object[]{userid};
		Object o = LongTimeTask.callRemoteService("pu",bo, "loadDatas", ParameterTypes, ParameterValues, 2);
		return o==null?null:(BidViewBillVO[])o;
	}
	
	public static String  getCvendoridByLogUser(String userid) throws Exception{
		Class[] ParameterTypes = new Class[]{String.class};
		Object[] ParameterValues = new Object[]{userid};
		Object o = LongTimeTask.callRemoteService("pu","nc.bs.zb.pub.ZbPubBO", "getCvendoridByLogUser", ParameterTypes, ParameterValues, 2);
		return PuPubVO.getString_TrimZeroLenAsNull(o);
	}
	
	public static boolean reValue(){
		String vendor= null;
		try {
			vendor = ViewBiddingHelper.getCvendoridByLogUser(ClientEnvironment.getInstance().getUser().getPrimaryKey());
		} catch (Exception e) {
			vendor=null;
		}
		if(vendor==null)
			return false;
		return true;
	}

}
