package nc.vo.zb.bill.deal;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * 
 * @author zhf
 * @说明：（鹤岗矿业）评标管理 供应商品种报价信息聚合vo
 * 2011-5-4下午02:45:23
 */
public class DealVendorBillVO extends AggregatedValueObject {

	private DealVendorPriceBVO head = null;
	private DealInvPriceBVO[] bodys = null;
	
	@Override
	public CircularlyAccessibleValueObject[] getChildrenVO() {
		// TODO Auto-generated method stub
		return bodys;
	}

	@Override
	public CircularlyAccessibleValueObject getParentVO() {
		// TODO Auto-generated method stub
		return head;
	}

	@Override
	public void setChildrenVO(CircularlyAccessibleValueObject[] children) {
		// TODO Auto-generated method stub

		bodys = (DealInvPriceBVO[])children;
	}

	@Override
	public void setParentVO(CircularlyAccessibleValueObject parent) {
		// TODO Auto-generated method stub

		head = (DealVendorPriceBVO)parent;
	}
	
	public DealVendorPriceBVO getHeader(){
		return head;
	}
	
	public DealInvPriceBVO[] getBodys(){
		return bodys;
	}

}
