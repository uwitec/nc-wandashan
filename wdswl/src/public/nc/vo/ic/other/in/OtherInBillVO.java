package nc.vo.ic.other.in;

import java.util.Arrays;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.ic.pub.TbGeneralHVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.pub.HYBillVO;

/**
 * 
 * ���ӱ�/����ͷ/������ۺ�VO
 *
 * ��������:Your Create Data
 * @author Your Author Name
 * @version Your Project 1.0
 */
public class  OtherInBillVO extends HYBillVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CircularlyAccessibleValueObject[] getChildrenVO() {
		return (TbGeneralBVO[]) super.getChildrenVO();
	}

	public CircularlyAccessibleValueObject getParentVO() {
		return (TbGeneralHVO) super.getParentVO();
	}

	public void setChildrenVO(CircularlyAccessibleValueObject[] children) {
		if( children == null || children.length == 0 ){
			super.setChildrenVO(null);
		}
		else{
			super.setChildrenVO((CircularlyAccessibleValueObject[]) Arrays.asList(children).toArray(new TbGeneralBVO[0]));
		}
	}

	public void setParentVO(CircularlyAccessibleValueObject parent) {
		super.setParentVO((TbGeneralHVO)parent);
	}

}
