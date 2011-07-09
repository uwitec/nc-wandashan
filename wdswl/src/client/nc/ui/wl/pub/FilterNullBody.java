package nc.ui.wl.pub;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.trade.voutils.IFilter;

/**
 * 
 * @author zhf  聚合vo  空表体过滤器
 *
 */
public class FilterNullBody implements IFilter {

	public boolean accept(Object o) {
		// TODO Auto-generated method stub
		if(!(o instanceof AggregatedValueObject))
			return true;
		AggregatedValueObject bill = (AggregatedValueObject)o;
		if(bill == null)
			return false;
		if(bill.getChildrenVO() == null || bill.getChildrenVO().length == 0)
			return false;
		return true;
	}

}
