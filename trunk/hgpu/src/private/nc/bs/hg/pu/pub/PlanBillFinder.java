package nc.bs.hg.pu.pub;

import nc.bs.trade.billsource.DefaultBillFinder;
import nc.bs.trade.billsource.IBillDataFinder;

/**
 * 销售返还单据Finder
 * @author Administrator
 *
 */
public class PlanBillFinder extends DefaultBillFinder {
	
	public IBillDataFinder createBillDataFinder(String billType)
			throws Exception {
		return new PlanDataFinder();
	}
}
