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
	// ����װ�ض��ӱ����ݵ�hashmap
	private HashMap hmChildVOs = new HashMap();

	/**
	 * ���ظ����ӱ�ı��� �����뵥��ģ���ҳǩ�����Ӧ
	 */
	public java.lang.String[] getTableCodes() {
		return new String[] { "wds_tanscorp_b1", "wds_tanscorp_b2" };
	}

	/**
	 * ���ظ����ӱ���������ơ� �������ڣ�(01-3-20 17:36:56)
	 */
	public java.lang.String[] getTableNames() {
		return new String[] { "��ϵ��", "�����˺�" };
	}

	/**
	 * ȡ�������ӱ������VO����
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
	 * ����ĳ���ӱ��VO���顣
	 */
	public CircularlyAccessibleValueObject[] getTableVO(String tableCode) {
		return (CircularlyAccessibleValueObject[]) hmChildVOs.get(tableCode);
	}

	public void setParentId(SuperVO item, java.lang.String id) {
	}

	/**
	 * Ϊ�ض��ӱ�����VO����
	 */
	public void setTableVO(String tableCode,
			CircularlyAccessibleValueObject[] vos) {
		hmChildVOs.put(tableCode, vos);
	}

	/**
	 * ȱʡ��ҳǩ����
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
