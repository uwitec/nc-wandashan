package nc.vo.dm.db.deal;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.pub.HYBillVO;
/**
 * 
 * @author mlr
 *
 */
public class DbDealBillVO extends HYBillVO {
	private static final long serialVersionUID = 1L;
	private CircularlyAccessibleValueObject parent = null;
	private CircularlyAccessibleValueObject[] bodys = null;

	public DbDeHeaderVo getHeader(){
		return (DbDeHeaderVo)getParentVO();
	}
	public DbDealVO[] getBodyVos(){
		return (DbDealVO[])getChildrenVO();
	}
	
	@Override
	public CircularlyAccessibleValueObject[] getChildrenVO() {
		return bodys;
	}

	@Override
	public CircularlyAccessibleValueObject getParentVO() {
		return parent;
	}

	@Override
	public void setChildrenVO(CircularlyAccessibleValueObject[] arg0) {
		bodys = arg0;
	}

	@Override
	public void setParentVO(CircularlyAccessibleValueObject arg0) {
		parent = arg0;
	}

}
