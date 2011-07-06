package nc.vo.ic.other.out;

import java.util.Arrays;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.other.out.TbOutgeneralHVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.trade.pub.HYBillVO;


/**
 * 
 * ���ӱ�/����ͷ/������ۺ�VO
 *
 * ��������:Your Create Data
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
	private Integer itype = 8;//�˵���Դ��� 0 �����Ƶ� 1 ���۶��� 2 �ֳ�ֱ�� 3��ֶ���4 �ϲ����� 8 �������Ƶ������ɵ��˵�
	private Object oUserObj = null;//�û�����

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
