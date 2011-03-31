package nc.bs.wds.finder;

import nc.bs.trade.billsource.DefaultBillFinder;
import nc.bs.trade.billsource.IBillDataFinder;
/**
 * BillFinder
 * @author zpm
 *
 */
public class WdsBillFinder extends DefaultBillFinder {
	public IBillDataFinder createBillDataFinder(String billType)
			throws Exception {
		return new WdsDataFinder();
	}
}
