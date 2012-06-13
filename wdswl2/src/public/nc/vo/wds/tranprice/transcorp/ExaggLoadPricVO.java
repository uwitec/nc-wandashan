package nc.vo.wds.tranprice.transcorp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.pub.IExAggVO;

@SuppressWarnings( { "unchecked" })
public class ExaggLoadPricVO extends HYBillVO implements IExAggVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 用来装载多子表数据的hashmap
	private HashMap hmChildVOs = new HashMap();

	/**
	 * 返回各个子表的编码 必须与单据模版的页签编码对应
	 */
	public java.lang.String[] getTableCodes() {
		return new String[] { "wds_tanscorp_b1", "wds_tanscorp_b2" };
	}

	/**
	 * 返回各个子表的中文名称。 创建日期：(01-3-20 17:36:56)
	 */
	public java.lang.String[] getTableNames() {
		return new String[] { "联系人", "银行账号" };
	}

	/**
	 * 取得所有子表的所有VO对象
	 */

	public CircularlyAccessibleValueObject[] getAllChildrenVO() {
		ArrayList al = new ArrayList();
		for (int i = 0; i < getTableCodes().length; i++) {
			CircularlyAccessibleValueObject[] cvos = getTableVO(getTableCodes()[i]);
			if (cvos != null)
				al.addAll(Arrays.asList(cvos));
		}
		return (SuperVO[]) al.toArray(new SuperVO[0]);
	}

	/**
	 * 返回某个子表的VO数组。
	 */
	public CircularlyAccessibleValueObject[] getTableVO(String tableCode) {
		return (CircularlyAccessibleValueObject[]) hmChildVOs.get(tableCode);
	}

	public void setParentId(SuperVO item, java.lang.String id) {
	}

	/**
	 * 为特定子表设置VO数据
	 */
	public void setTableVO(String tableCode,
			CircularlyAccessibleValueObject[] vos) {
		hmChildVOs.put(tableCode, vos);
	}

	/**
	 * 缺省的页签编码
	 */
	public String getDefaultTableCode() {
		return getTableCodes()[0];
	}

	public nc.vo.pub.SuperVO[] getChildVOsByParentId(String tableCode,
			String parentid) {
		return null;
	}

	public java.util.HashMap getHmEditingVOs() throws Exception {
		return null;
	}

	public java.lang.String getParentId(SuperVO item) {
		return null;
	}

}
