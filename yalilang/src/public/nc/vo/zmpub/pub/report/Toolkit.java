package nc.vo.zmpub.pub.report;

import java.util.Arrays;
import java.util.Collection;
import java.util.Dictionary;

import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * 工具箱
 *
 * @author：刘建波 
 */
public class Toolkit {

	/**
	 * Toolkit 构造子注解。
	 */
	private Toolkit() {
		super();
	}

	/**
	 * 检查传入的参数是否为空。
	 * 
	 * @return boolean 
	 *      如果被检查值为null，返回true。
	 *      如果value的类型为String，并且value.length()为0，返回true。
	 *		如果value的类型为Object[]，并且value.length为0，返回true。
	 *		如果value的类型为Collection，并且value.size()为0，返回true。
	 *		如果value的类型为Dictionary，并且value.size()为0，返回true。
	 *      否则返回false。
	 * @param value 被检查值。
	 */
	public static boolean isEmpty(Object value) {
	    if (value == null)
	        return true;
	    if ((value instanceof String) && (((String) value).trim().length() <= 0))
	        return true;
	    if ((value instanceof Object[]) && (((Object[]) value).length <= 0))
	        return true;
	    if ((value instanceof Collection) && ((Collection) value).size() <= 0)
	        return true;
	    if ((value instanceof Dictionary) && ((Dictionary) value).size() <= 0)
	        return true;
	    return false;
	}




public static String[] addTwoStringArray(String[] s1, String[] s2) {
	if (s2 == null || s2.length == 0)
		return s1;
	if (s1 == null)
		s1 = new String[0];
	String[] s = new String[s1.length + s2.length];
	System.arraycopy(s1, 0, s, 0, s1.length);
	System.arraycopy(s2, 0, s, s1.length, s2.length);
	return s;
}

	public static CircularlyAccessibleValueObject[] addTwoVos(CircularlyAccessibleValueObject[] vos1, CircularlyAccessibleValueObject[] vos2) {
		if (vos2 == null || vos2.length == 0)
			return vos1;
		if (vos1 == null)
			vos1 = new CircularlyAccessibleValueObject[0];
		CircularlyAccessibleValueObject[] vos = new CircularlyAccessibleValueObject[vos1.length + vos2.length];
		System.arraycopy(vos1, 0, vos, 0, vos1.length);
		System.arraycopy(vos2, 0, vos, vos1.length, vos2.length);
		return vos;
	}

/*
功能：对数据对象的查找。liujianbo 2004/4/8+
*/
public static int binarySearch(Object[] ary, Object o)
{
		if (isEmpty(ary))
			return -1;
		Arrays.sort(ary);
		return Arrays.binarySearch(ary, o);
	}

/**
*将合计vo和明细vo 放在合成一个VO。
*@parameter:vos:total vos and detailvos have been added.;
@			joinFlds,detailvos and totalvos join fields.
@			detailFlds,detailvos fields names array.
@			detailKey,the fields that can sign a detail vo.
@return :combined vos
*/
public static CircularlyAccessibleValueObject[] combinReportVOsVsDetailVOs(
	CircularlyAccessibleValueObject[] vos,
	String[] joinFlds,
	String[] detailFlds,
	String detailKey)
	throws Exception {
	if (vos == null || vos.length == 0)
		return null;
	if (detailFlds == null || detailFlds.length == 0)
		throw new Exception("输入明细字段数组不能为空。");
	if (joinFlds == null || joinFlds.length == 0)
		throw new Exception("输入Join字段数组不能为空。");
	if (detailKey == null)
		throw new Exception("输入明细关键字列不能为空。");
	//对join列，和明细的关键字列进行降序排序。这样保证合计行在上边。
	SimpleSortVOTool sortTool = new SimpleSortVOTool();
	sortTool.sortVO(vos, addTwoStringArray(joinFlds, new String[] { detailKey }), true);
	//设置堆栈。
	java.util.Stack st = new java.util.Stack();
	for (int i = 0; i < vos.length; i++) { //如果堆栈为空则直接放入堆栈。
		if (st.isEmpty()) {
			st.push(vos[i]);
			continue;
		} //取栈顶的对象。并得到其明细。
		CircularlyAccessibleValueObject vo = (CircularlyAccessibleValueObject) st.pop();
		Object oTop = vo.getAttributeValue(detailKey);
		Object oCur = vos[i].getAttributeValue(detailKey);
		//当前明细VO为空则加入堆栈。
		if (oCur == null) {
			st.push(vo);
			st.push(vos[i]);
			continue;
		} //对象都不为空则为明细，将非明细列设为空，加入堆栈。
		if (oTop != null && oCur != null) {
			st.push(vo);
			String[] attrnames = vos[i].getAttributeNames();
			for (int j = 0; j < attrnames.length; j++) {
				if (Toolkit.binarySearch(detailFlds, attrnames[j]) < 0)
					vos[i].setAttributeValue(attrnames[j], null);
			}
			st.push(vos[i]);
			continue;
		} //栈顶对象明细为空，当前对象明细不为空进行合并。
		if (oTop == null && oCur != null) {
			for (int j = 0; j < detailFlds.length; j++) {
				vo.setAttributeValue(detailFlds[j], vos[i].getAttributeValue(detailFlds[j]));
			}
			st.push(vo);
			continue;
		}
	} //栈转成数组。
	CircularlyAccessibleValueObject[] vosResult = new CircularlyAccessibleValueObject[st.size()];
	st.copyInto(vosResult);
	return vosResult;
}

/**
*将合计vo和明细vo 放在合成一个VO。
*@parameter:vosTotal,totalvos;vosDetial,detail vos;
@			joinFlds,detailvos and totalvos join fields.
@			detailFlds,detailvos fields names array.
@			detailKey,the fields that can sign a detail vo.
@return :combined vos
*/
public static CircularlyAccessibleValueObject[] combinReportVOsVsDetailVOs(
	CircularlyAccessibleValueObject[] vosTotal,
	CircularlyAccessibleValueObject[] vosDetail,
	String[] joinFlds,
	String[] detailFlds,
	String detailKey)
	throws Exception {
	//将两vo数组放入一个数组。
	CircularlyAccessibleValueObject[] vos = addTwoVos(vosTotal, vosDetail);
	return combinReportVOsVsDetailVOs(vos, joinFlds, detailFlds, detailKey);
}

/**
 * 根据指定数组中数据生成部分SQL语句。
 *
 * @author: 刘建波 2004/3/24+
 * @param arsKey 数据。
 * @return String
 *      
 */
public static String getWherePartByKeys(String fld, String[] arsKey)
{
	final int MAX = 500;
	if (isEmpty(arsKey))
		return " 1 = 1 ";
	if (arsKey.length == 1)
		return fld + "='" + arsKey[0] + "'";
	if (arsKey.length <= MAX)
	{
		String sTmp = fld + " in (";
		for (int i = 0; i < arsKey.length; i++)
		{
			if (i == arsKey.length - 1)
			{
				sTmp += "'" + arsKey[i] + "')";
				break;
			}
			sTmp += "'" + arsKey[i] + "',";
		}
		return sTmp;
	}
	int ipos = 0;
	int itimes = arsKey.length / MAX;
	int mode = arsKey.length % MAX;
	String where = null;
	for (int i = 0; i < itimes; i++)
	{
		if (where == null)
			where = " ( " + fld + " in ( ";
		else
			where += " or " + fld + " in (";
		for (int j = 0; j < MAX; j++)
		{
			if (j == MAX - 1)
			{
				where += "'" + arsKey[ipos + j] + "')";
				break;
			}
			where += "'" + arsKey[ipos + j] + "',";
		}
		ipos += MAX;
	}
	if (mode == 0)
		where += " )";
	else
	{
		where += " or " + fld + " in (";
		for (int k = 0; k < mode; k++)
		{
			if (k == mode - 1)
			{
				where += "'" + arsKey[ipos + k] + "'))";
				break;
			}
			where += "'" + arsKey[ipos + k] + "',";
		}
	}
	return where;
}

	/*
	去掉List中为空的对象。刘建波2004/4/09+
	*/
	public static void packList(java.util.List list) {
		if (list.isEmpty())
			return;
		for (int i = 0; i < list.size(); i++) {
			if (isEmpty(list.get(i))) {
				list.remove(i);
				i--;
			}
		}
	}
}