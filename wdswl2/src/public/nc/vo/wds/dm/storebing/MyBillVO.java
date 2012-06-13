package nc.vo.wds.dm.storebing;

import java.util.Arrays;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.pub.HYBillVO;

import nc.vo.wds.dm.storebing.BdStordocVO;
import nc.vo.wds.dm.storebing.TbStorcubasdocVO;

/**
 * 
 * 单子表/单表头/单表体聚合VO
 *
 * 创建日期:Your Create Data
 * @author Your Author Name
 * @version Your Project 1.0
 */
public class  MyBillVO extends HYBillVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CircularlyAccessibleValueObject[] getChildrenVO() {
		return (TbStorcubasdocVO[]) super.getChildrenVO();
	}

	public CircularlyAccessibleValueObject getParentVO() {
		return (BdStordocVO) super.getParentVO();
	}

	public void setChildrenVO(CircularlyAccessibleValueObject[] children) {
		if( children == null || children.length == 0 ){
			super.setChildrenVO(null);
		}
		else{
			super.setChildrenVO((CircularlyAccessibleValueObject[]) Arrays.asList(children).toArray(new TbStorcubasdocVO[0]));
		}
	}

	public void setParentVO(CircularlyAccessibleValueObject parent) {
		super.setParentVO((BdStordocVO)parent);
	}

}
