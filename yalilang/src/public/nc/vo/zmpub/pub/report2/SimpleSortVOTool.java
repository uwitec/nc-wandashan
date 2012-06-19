package nc.vo.zmpub.pub.report2;


import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
/**
 * 简单的对CirVOs排序的工具。
 * 创建日期：(2004-8-17 17:43:27)
 * @author：刘建波
 */
public class SimpleSortVOTool implements java.util.Comparator {
	private String[] sortItems = null;
	private boolean isDesc = true;
	private CircularlyAccessibleValueObject[] vos = null;
/**
 * SimpleSortVOTool 构造子注解。
 */
public SimpleSortVOTool() {
	super();
}
	public int compare(java.lang.Object o1, java.lang.Object o2) {
		CircularlyAccessibleValueObject vo1 = (CircularlyAccessibleValueObject) o1;
		CircularlyAccessibleValueObject vo2 = (CircularlyAccessibleValueObject) o2;
		for (int i = 0; i < sortItems.length; i++) {
			Object oValue1 = vo1.getAttributeValue(sortItems[i]);
			Object oValue2 = vo2.getAttributeValue(sortItems[i]);
			if (oValue1 == null && oValue2 == null)
				continue;
			if (oValue1 == null && oValue2 != null)
				return isDesc ? 1 : -1;
			if (oValue1 != null && oValue2 == null)
				return isDesc ? -1 : 1;
			if (oValue1 instanceof UFDouble || oValue2 instanceof UFDouble) {
				UFDouble ufd1 = (UFDouble) oValue1;
				UFDouble ufd2 = (UFDouble) oValue2;
				if (ufd1.doubleValue() == ufd2.doubleValue())
					continue;
				return ufd1.doubleValue() > ufd2.doubleValue() && isDesc ? -1 : 1;
			} else {
				if (oValue1.toString().compareTo(oValue2.toString()) == 0)
					continue;
				int r = oValue1.toString().compareTo(oValue2.toString());
				if (!isDesc)
					return r;
				if (r > 0)
					return -1;
				else
					return 1;
			}
		}
		return 0;
	}
public void sortVO(CircularlyAccessibleValueObject[] vos, String[] sortitems, boolean isDesc) {
	if (vos == null || vos.length < 2)
		return;
	if (sortitems == null || sortitems.length == 0)
		return;
	this.vos = vos;
	this.isDesc = isDesc;
	this.sortItems = sortitems;
	java.util.Arrays.sort(vos, this);
}
public void sortVO(java.util.Vector v, String[] sortitems, boolean isDesc) {
	if (v == null || v.size() < 2)
		return;
	if (sortitems == null || sortitems.length == 0)
		return;
	CircularlyAccessibleValueObject[] vos = new CircularlyAccessibleValueObject[v.size()];
	v.copyInto(vos);
	this.vos = vos;
	this.isDesc = isDesc;
	this.sortItems = sortitems;
	java.util.Arrays.sort(vos, this);
	v.removeAllElements();
	for (int i = 0; i < vos.length; i++)
		v.add(vos[i]);
}
}
