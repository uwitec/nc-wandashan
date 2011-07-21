package nc.ui.zb.price.view;

import nc.ui.zb.pub.LongTimeTask;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.zb.price.SubmitPriceVO;
    
public class PriceViewHealper {

	private static String bo = "nc.bs.zb.price.view.PriceViewBO";
	
	/**
	 * 
	 * @author zhw  查询出报价信息
	 * @说明：（鹤岗矿业）
	 * 2010-11-24上午10:27:12
	 * @param whereSql
	 * @param cl
	 * @param iplantype
	 * @return
	 * @throws Exception
	 */
	public static SubmitPriceVO[] queryDatas(String whereSql,ClientLink cl) throws Exception{
		Class[] ParameterTypes = new Class[]{String.class,ClientLink.class};
		Object[] ParameterValues = new Object[]{whereSql,cl};
		Object o = LongTimeTask.callRemoteService("pu",bo, "queryDataForInfo", ParameterTypes, ParameterValues, 2);
		return o==null?null:(SubmitPriceVO[])o;
	}
}
