package nc.vo.wds.w80020206;

import java.util.Arrays;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.pub.HYBillVO;

import nc.vo.wds.w80060604.SoSaleVO;
import nc.vo.wds.w80060406.TbFydnewVO;	

/**
 * 
 * ���ӱ�/����ͷ/������ۺ�VO
 *
 * ��������:Your Create Data
 * @author Your Author Name
 * @version Your Project 1.0
 */
public class  MyBillVO extends HYBillVO {

	public CircularlyAccessibleValueObject[] getChildrenVO() {
		return (TbFydnewVO[]) super.getChildrenVO();
	}

	public CircularlyAccessibleValueObject getParentVO() {
		return (SoSaleVO) super.getParentVO();
	}

	public void setChildrenVO(CircularlyAccessibleValueObject[] children) {
		if( children == null || children.length == 0 ){
			super.setChildrenVO(null);
		}
		else{
			super.setChildrenVO((CircularlyAccessibleValueObject[]) Arrays.asList(children).toArray(new TbFydnewVO[0]));
		}
	}

	public void setParentVO(CircularlyAccessibleValueObject parent) {
		super.setParentVO((SoSaleVO)parent);
	}

}
