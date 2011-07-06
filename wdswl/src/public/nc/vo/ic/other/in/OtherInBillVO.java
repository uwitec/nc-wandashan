package nc.vo.ic.other.in;

import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.ic.pub.TbGeneralHVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.pub.HYBillVO;

/**
 * 
 * 单子表/单表头/单表体聚合VO
 *
 * 创建日期:Your Create Data
 * @author Your Author Name
 * @version Your Project 1.0
 */
public class  OtherInBillVO extends HYBillVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Object oUserObj = null;
	

	public Object getOUserObj() {
		return oUserObj;
	}

	public void setOUserObj(Object userObj) {
		oUserObj = userObj;
	}
	
	public TbGeneralHVO getHeaderVo(){
		return (TbGeneralHVO)getParentVO();
	}

	public CircularlyAccessibleValueObject[] getChildrenVO() {
		return super.getChildrenVO();
	}

	public CircularlyAccessibleValueObject getParentVO() {
		return (TbGeneralHVO) super.getParentVO();
	}

	public void setChildrenVO(CircularlyAccessibleValueObject[] children) {
//		if( children == null || children.length == 0 ){
//			super.setChildrenVO(null);
//		}
//		else{
//			super.setChildrenVO((CircularlyAccessibleValueObject[]) Arrays.asList(children).toArray(new TbGeneralBVO[0]));
//		}
		super.setChildrenVO(children);
	}

	public void setParentVO(CircularlyAccessibleValueObject parent) {
		super.setParentVO((TbGeneralHVO)parent);
	}

}
