package nc.vo.dm.so.deal;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.pub.HYBillVO;

public class SoDealBillVO extends HYBillVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CircularlyAccessibleValueObject parent = null;
	private CircularlyAccessibleValueObject[] bodys = null;

	public SoDeHeaderVo getHeader(){
		return (SoDeHeaderVo)getParentVO();
	}
	public SoDealVO[] getBodyVos(){
		return (SoDealVO[])getChildrenVO();
	}
	
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
