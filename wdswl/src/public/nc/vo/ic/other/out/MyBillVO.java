package nc.vo.ic.other.out;

import java.util.Arrays;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.other.out.TbOutgeneralHVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.trade.pub.HYBillVO;


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
	private String sLogUser = null;
	private String sLogCorp = null;
	private UFDate uLogDate = null;
	private Integer itype = 8;//运单来源类别 0 发运制单 1 销售订单 2 分厂直流 3拆分订单4 合并订单 8 出库自制单据生成的运单
	private Object oUserObj = null;//用户数据

	public Object getOUserObj() {
		return oUserObj;
	}

	public void setOUserObj(Object userObj) {
		oUserObj = userObj;
	}

	public String getSLogUser() {
		return sLogUser;
	}

	public void setSLogUser(String logUser) {
		sLogUser = logUser;
	}

	public String getSLogCorp() {
		return sLogCorp;
	}

	public void setSLogCorp(String logCorp) {
		sLogCorp = logCorp;
	}

	public UFDate getULogDate() {
		return uLogDate;
	}

	public void setULogDate(UFDate logDate) {
		uLogDate = logDate;
	}

	public Integer getItype() {
		return itype;
	}

	public void setItype(Integer itype) {
		this.itype = itype;
	}

	public CircularlyAccessibleValueObject[] getChildrenVO() {
		return (TbOutgeneralBVO[]) super.getChildrenVO();
	}

	public CircularlyAccessibleValueObject getParentVO() {
		return (TbOutgeneralHVO) super.getParentVO();
	}

	public void setChildrenVO(CircularlyAccessibleValueObject[] children) {
		if( children == null || children.length == 0 ){
			super.setChildrenVO(null);
		}
		else{
			super.setChildrenVO((CircularlyAccessibleValueObject[]) Arrays.asList(children).toArray(new TbOutgeneralBVO[0]));
		}
	}

	public void setParentVO(CircularlyAccessibleValueObject parent) {
		super.setParentVO((TbOutgeneralHVO)parent);
	}

}
