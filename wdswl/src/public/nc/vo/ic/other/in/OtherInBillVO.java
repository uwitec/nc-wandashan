package nc.vo.ic.other.in;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.ic.pub.TbGeneralHVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
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
public class  OtherInBillVO extends HYBillVO implements IExAggVO {
	private static final long serialVersionUID = 1L;
	
	private HashMap hmChildVOs = new HashMap();
	
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
		return (CircularlyAccessibleValueObject[]) hmChildVOs.get(getTableCodes()[0]);
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
		hmChildVOs.put(getTableCodes()[0], children);
	}

	public void setParentVO(CircularlyAccessibleValueObject parent) {
		super.setParentVO((TbGeneralHVO)parent);
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
		return new String[]{"tb_general_b","tb_outgeneral_b2"};
	}

	public String[] getTableNames() {
		return new String[]{"存货设置","班组设置"};
	}

	public CircularlyAccessibleValueObject[] getTableVO(String tableCode) {
		return (CircularlyAccessibleValueObject[])
		hmChildVOs.get(tableCode);
	}

	public void setParentId(SuperVO item, String id) {
		
	}

	public void setTableVO(String tableCode,
			CircularlyAccessibleValueObject[] values) {
		hmChildVOs.put(tableCode, values);
	}

}
