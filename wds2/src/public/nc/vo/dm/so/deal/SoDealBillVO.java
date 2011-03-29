package nc.vo.dm.so.deal;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;

public class SoDealBillVO extends AggregatedValueObject {

	private CircularlyAccessibleValueObject parent = null;
	private CircularlyAccessibleValueObject[] bodys = null;

	@Override
	public CircularlyAccessibleValueObject[] getChildrenVO() {
		// TODO Auto-generated method stub
		return bodys;
	}

	@Override
	public CircularlyAccessibleValueObject getParentVO() {
		// TODO Auto-generated method stub
		return parent;
	}

	@Override
	public void setChildrenVO(CircularlyAccessibleValueObject[] arg0) {
		// TODO Auto-generated method stub
		bodys = arg0;
	}

	@Override
	public void setParentVO(CircularlyAccessibleValueObject arg0) {
		// TODO Auto-generated method stub
		parent = arg0;
	}

}
