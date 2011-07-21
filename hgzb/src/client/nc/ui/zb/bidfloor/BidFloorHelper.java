package nc.ui.zb.bidfloor;

import nc.ui.zb.pub.LongTimeTask;
import nc.vo.pub.SuperVO;

public class BidFloorHelper {
	
	private static String bo = "nc.bs.zb.bidfloor.BidFloorBO";
	
	public static ViewDetailVO[]  loadDatas(String cbiddingid,String cinvmanid) throws Exception{
		Class[] ParameterTypes = new Class[]{String.class,String.class};
		Object[] ParameterValues = new Object[]{cbiddingid,cinvmanid};
		Object o = LongTimeTask.callRemoteService("pu",bo, "loadDatas", ParameterTypes, ParameterValues, 2);
		return o==null?null:(ViewDetailVO[])o;
	}
	
	public static SuperVO[]  loadBodyData(String key, String name) throws Exception{
		Class[] ParameterTypes = new Class[]{String.class,String.class};
		Object[] ParameterValues = new Object[]{key,name};
		Object o = LongTimeTask.callRemoteService("pu",bo, "loadBodyData", ParameterTypes, ParameterValues, 2);
		return o==null?null:(SuperVO[])o;
	}

}
