package nc.vo.wds.transfer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import nc.vo.ic.other.out.TbOutgeneralHVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.pub.IExAggVO;


/**
 * 
 * 单子表/单表头/单表体聚合VO
 *
 * 创建日期:Your Create Data
 * @author Your Author Name
 * @version Your Project 1.0
 */
public class  MyBillVO extends HYBillVO implements IExAggVO{
	
	/**
	 * 
	 */
	private HashMap hmChildVOs = new HashMap();
	
	
	
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
		return getTableVO(getDefaultTableCode());
	}

	public CircularlyAccessibleValueObject getParentVO() {
		return super.getParentVO();
	}

	public void setChildrenVO(CircularlyAccessibleValueObject[] children) {
		if( children == null || children.length == 0 ){
			super.setChildrenVO(null);
		}
		else{
			super.setChildrenVO(children);
		}
		
		setTableVO(getDefaultTableCode(), children);
	}

	public void setParentVO(CircularlyAccessibleValueObject parent) {
		super.setParentVO((TbOutgeneralHVO)parent);
	}

	public CircularlyAccessibleValueObject[] getAllChildrenVO() {
		// TODO Auto-generated method stub
		ArrayList al = new ArrayList();
		for (int i = 0; i < getTableCodes().length; i++) {
			CircularlyAccessibleValueObject[] cvos
			= getTableVO(getTableCodes()[i]);
			if (cvos != null)
				al.addAll(Arrays.asList(cvos));
		}
		return (SuperVO[]) al.toArray(new SuperVO[0]);
	}

	public SuperVO[] getChildVOsByParentId(String tableCode, String parentid) {
		return null;
	}

	public String getDefaultTableCode() {
		return getTableCodes()[0];
	}

	public HashMap getHmEditingVOs() throws Exception {
		return null;
	}

	public String getParentId(SuperVO item) {
		return null;
	}

	public String[] getTableCodes() {
		return new String[]{"tb_outgeneral_b"};
	}

	public String[] getTableNames() {
		return new String[]{"存货设置"};
	}

	public CircularlyAccessibleValueObject[] getTableVO(String tableCode) {
		// TODO Auto-generated method stub
		return (CircularlyAccessibleValueObject[])
		hmChildVOs.get(tableCode);
	}

	public void setParentId(SuperVO item, String id) {
		// TODO Auto-generated method stub
		
	}

	public void setTableVO(String tableCode,
			CircularlyAccessibleValueObject[] values) {
		hmChildVOs.put(tableCode, values);
	}

}
