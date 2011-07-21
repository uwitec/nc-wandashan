package nc.bs.zb.pub;

import nc.bs.trade.billsource.DefaultBillFinder;
import nc.bs.trade.billsource.IBillDataFinder;

/**
 * ÕÐ±êµ¥¾ÝFinder
 * @author Administrator
 *
 */
public class BidBillFinder extends DefaultBillFinder {
	
	public IBillDataFinder createBillDataFinder(String billType)
			throws Exception {
		return new BidDataFinder();
	}
}
