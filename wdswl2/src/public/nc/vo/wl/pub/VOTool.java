package nc.vo.wl.pub;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IExAggVO;
import nc.vo.trade.summarize.IHashKey;


/**
 * 作者：李金巧 功能：VO工具 日期：(2004-2-11 15:20:16)
 * 
 * @modified by twh on 2006-7-12 上午09:54:40
 *           <p>
 *           修改内容：(代码中标记tag内容的位置)
 *           <li>1.<tag>聚合VO Clone</tag>；
 *           <li>2.<tag></tag>；
 *           <li>3.<tag></tag>。
 * 
 */
public class VOTool {
	/**
	 * 将数组元素依次增添至ArrayList 
	 * 作者：薛恩平 
	 * 创建日期：2004-12-20 10:01:09
	 * 
	 * @param vos
	 */
	public static void addVOsToArrayList(CircularlyAccessibleValueObject[] vos, ArrayList al) {
		for (int i = 0; vos != null && i < vos.length; i++) {
			al.add(vos[i]);
		}
	}

	/**
	 * 聚合VO Clone
	 * 
	 * @param vo
	 * @return twh (2006-7-12 上午09:56:24)<br>
	 */
	public static AggregatedValueObject aggregateVOClone(AggregatedValueObject vo) {
		if (vo == null)
			return null;

		AggregatedValueObject voClone = null;
		try {
			voClone = (AggregatedValueObject) vo.getClass().newInstance();
			if (vo.getParentVO() != null)
				voClone.setParentVO((CircularlyAccessibleValueObject) vo.getParentVO().clone());
			voClone.setChildrenVO(arrayVOClone((CircularlyAccessibleValueObject[]) vo.getChildrenVO()));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return voClone;
	}

	/**
	 * Object数组类型与内容类型一致，要求：objs中的类型必须一致 
	 * 作者：薛恩平 
	 * 创建日期：2005-4-7 11:07:36
	 * 
	 * @param objs
	 * @return
	 */
	public static Object[] arrayIdentical(Object[] objs) {

		if (objs == null || objs.length == 0)
			return null;

		Class arrayClass = objs[0].getClass();

		Object[] array = (Object[]) Array.newInstance(arrayClass, objs.length);
		for (int i = 0; i < objs.length; i++) {
			array[i] = objs[i];
		}
		return array;
	}

	/**
	 * VO数组Clone，要求：vos中的VO类型必须一致 
	 * 作者：薛恩平 
	 * 创建日期：(2004-8-20 16:13:18)
	 * 
	 * @return nc.vo.pub.CircularlyAccessibleValueObject[]
	 * @param vos
	 *            nc.vo.pub.CircularlyAccessibleValueObject
	 */
	public static CircularlyAccessibleValueObject[] arrayVOClone(CircularlyAccessibleValueObject[] vos) {

		if (vos == null || vos.length == 0)
			return null;

		Class vosClass = vos[0].getClass();

		CircularlyAccessibleValueObject[] voclone = (CircularlyAccessibleValueObject[]) Array.newInstance(vosClass, vos.length);
		for (int i = 0; i < vos.length; i++) {
			voclone[i] = (CircularlyAccessibleValueObject) vos[i].clone();
		}
		return voclone;
	}

	/**
	 * VO复制，支持多子表的复制
	 * 
	 * created by chenliang at 2007-11-7 上午10:37:23
	 * 
	 * @param billVO
	 * @return
	 * @throws Exception
	 */
	public static AggregatedValueObject duplicateBillVO(AggregatedValueObject billVO) throws Exception {
		if (billVO == null)
			return null;
		AggregatedValueObject result = (AggregatedValueObject) billVO.getClass().newInstance();
		CircularlyAccessibleValueObject sourceHead = billVO.getParentVO();
		CircularlyAccessibleValueObject headVO = null;
		if (sourceHead != null)
			headVO = (CircularlyAccessibleValueObject) sourceHead.clone();

		// 对于多子表做特殊处理
		if (billVO instanceof IExAggVO) {
			String[] tablecodes = ((IExAggVO) billVO).getTableCodes();
			if (tablecodes != null && tablecodes.length > 0) {
				for (String tableCode : tablecodes) {
					CircularlyAccessibleValueObject[] clonetablevos = null;
					CircularlyAccessibleValueObject[] tableVOs = ((IExAggVO) billVO).getTableVO(tableCode);
					if (tableVOs != null && tableVOs.length > 0) {
						clonetablevos = (CircularlyAccessibleValueObject[]) java.lang.reflect.Array.newInstance(
								tableVOs[0].getClass(), tableVOs.length);
						for (int i = 0; i < clonetablevos.length; i++) {
							clonetablevos[i] = (CircularlyAccessibleValueObject) tableVOs[i].clone();
						}
					}
					((IExAggVO) result).setTableVO(tableCode, clonetablevos);
				}

			}
		}

		CircularlyAccessibleValueObject[] sourceBodyVOs = billVO.getChildrenVO();
		CircularlyAccessibleValueObject[] bodyVOs = null;
		if (sourceBodyVOs != null && sourceBodyVOs.length != 0) {
			bodyVOs = (CircularlyAccessibleValueObject[]) java.lang.reflect.Array.newInstance(sourceBodyVOs[0]
					.getClass(), sourceBodyVOs.length);
			for (int i = 0; i < bodyVOs.length; i++) {
				bodyVOs[i] = (CircularlyAccessibleValueObject) sourceBodyVOs[i].clone();
			}
		}
		result.setParentVO(headVO);
		result.setChildrenVO(bodyVOs);
		return result;
	}

	/**
	 * 根据VO生成groupKey 
	 * 作者：薛恩平 
	 * 创建日期：2005-4-1 16:42:11
	 * 
	 * @param vo
	 * @return
	 */
	public static String getGroupKeyByVO(CircularlyAccessibleValueObject vo, String[] groupFields) {

		String key = "";
		for (int i = 0; groupFields != null && i < groupFields.length; i++) {
			key += (String) vo.getAttributeValue(groupFields[i]);
		}
		return key;
	}

	/**
	 * 根据编码规则、级次返回编码长度
	 * 1）无编码规则，或传入级次 <= 0，返回-1；
	 * 2）传入级次 > 编码规则分组长度，返回编码规则最大长度；
	 * 3）1 <= 传入级次 <= 编码规则分组长度，返回计算的编码长度； 
	 * 作者：薛恩平 
	 * 创建日期：2005-3-24 9:53:56
	 * 
	 * @param codeRule
	 * @param ilevel
	 * @return
	 */
	static public int getLengthByLevel(String codeRule, int ilevel) {

		if (codeRule == null || codeRule.trim().length() == 0 || ilevel <= 0)
			return -1;

		int ilength = 0;
		int[] codeSection = parsCodeRule(codeRule);
		for (int i = 0; i < ilevel && i < codeSection.length; i++) {
			ilength += codeSection[i];
		}

		return ilength;
	}

	/**
	 * 根据分组字段构造唯一的VO哈希，出现重复则抛异常 
	 * 作者：薛恩平 
	 * 创建日期：2005-4-6 15:14:07
	 * 
	 * @param vos
	 * @param groupFields
	 * @return
	 * @throws Exception
	 */
	public static HashMap getVOHashByGroupFields(CircularlyAccessibleValueObject[] vos, String[] groupFields)
			throws Exception {

		HashMap hash = new HashMap();

		for (int i = 0; vos != null && i < vos.length; i++) {
			String key = getGroupKeyByVO(vos[i], groupFields);
			if (!hash.containsKey(key)) {
				hash.put(key, vos[i]);
			} else {
				throw new BusinessException("根据分组字段，在vos中有重复记录，请检查！");
			}
		}

		return hash;
	}

	/**
	 * 根据IHashKey对泛型数组分组
	 * 作者：薛恩平
	 * 创建日期：2008-3-1 下午04:47:40
	 * @param vos
	 * @param iHashKey
	 * @return
	 */
	public static <T> HashMap<String, T[]> hashlizeVO(T[] ts, IHashKey iHashKey) {
		
		if (ts == null || ts.length == 0)
			throw new IllegalArgumentException("objs cann't be null");
		if (iHashKey == null)
			throw new IllegalArgumentException("iHashKey cann't be null");
		
		Map<String, ArrayList<T>> tempHs = new HashMap<String, ArrayList<T>>();
		for (int i = 0; i < ts.length; i++) {
			String key = iHashKey.getKey(ts[i]);
			ArrayList<T> al = tempHs.get(key);
			if (al == null) {
				al = new ArrayList<T>();
				tempHs.put(key, al);
			}
			al.add(ts[i]);
		}
		
		Class c = ts[0].getClass();
		HashMap<String, T[]> retHs = new HashMap<String, T[]>();
		Iterator<String> iter = tempHs.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			ArrayList<T> al = tempHs.get(key);
			if (al.size() > 0) {
				T[] children = (T[])Array.newInstance(c, al.size());
				children = al.toArray(children);
				retHs.put(key, children);
			}
		}
		
		return retHs;
	}

	/**
	 * 两个VO数组是否完全相等（对象id除外） 
	 * 作者：薛恩平 
	 * 创建日期：2007-4-5 下午08:13:30
	 * 
	 * @param vos1
	 * @param vos2
	 * @return
	 */
	public static boolean isArrayVOEqual(CircularlyAccessibleValueObject[] vos1, CircularlyAccessibleValueObject[] vos2) {

		if (vos1 == null && vos2 == null)
			return true;
		if (vos1 == null && vos2 != null || vos1 != null && vos2 == null)
			return false;
		if (vos1.length == 0 && vos2.length == 0)
			return true;
		if (vos1.length != vos2.length)
			return false;
		if (!vos1.getClass().equals(vos2.getClass()))
			return false;

		for (int i = 0; i < vos1.length; i++) {
			// if (!vos1[i].equals(vos2[i]))
			// return false;
			String[] vosattri1 = vos1[i].getAttributeNames();
			String[] vosattri2 = vos2[i].getAttributeNames();
			if (vosattri1.length != vosattri2.length)
				return false;
			for (int j = 0; j < vosattri1.length; j++) {
				Object obj1 = vos1[i].getAttributeValue(vosattri1[j]);
				Object obj2 = vos2[i].getAttributeValue(vosattri2[j]);
				if (obj1 != null && obj2 != null) {
					if (obj1 == null && obj2 != null || obj1 != null && obj2 == null)
						return false;
					if (!obj1.equals(obj2))
						return false;
				}
			}
		}

		return true;
	}

	/**
	 * 是否为null或空字符串 
	 * 作者：薛恩平 
	 * 创建日期：2005-4-7 22:45:53
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNull(Object obj) {
		if (obj == null || obj.toString().trim().length() == 0)
			return true;
		else
			return false;
	}

	/**
	 * 给定VO数组，给定字段，返回指定字段的最大值； 
	 * 注意：若VO数组中该字段值均为空，则返回NULL； 
	 * 作者：薛恩平 
	 * 创建日期：2005-3-22 19:57:24
	 * 
	 * @param vos
	 * @param field
	 * @return
	 */
	public static Object max(CircularlyAccessibleValueObject[] vos, String field) {

		int max = maxIndex(vos, field);
		if (max != -1)
			return vos[max].getAttributeValue(field);
		else
			return null;
	}

	/**
	 * 给定VO数组，给定字段，返回指定字段最大值VO的索引；
	 * 注意：若VO数组中该字段值均为空，或VO数组中该字段类型为非数值、日期/时间类型，则返回-1； 
	 * 作者：薛恩平 
	 * 创建日期：2005-3-22 19:57:21
	 * 
	 * @param vos
	 * @param field
	 * @return
	 */
	public static int maxIndex(CircularlyAccessibleValueObject[] vos, String field) {

		int maxindex = -1;

		if (vos == null || vos.length == 0)
			return maxindex;

		maxindex = 0;
		boolean isAllNull = true;
		for (int i = 0; i < vos.length; i++) {
			Object o = vos[i].getAttributeValue(field);
			if (o == null || o.toString().trim().length() == 0)
				continue;

			isAllNull = false;
			if (o instanceof Number) {
				Number value = (Number) o;
				Number oldvalue = (Number) vos[maxindex].getAttributeValue(field);
				if (oldvalue == null || oldvalue.toString().trim().length() == 0
						|| value.doubleValue() > oldvalue.doubleValue())
					maxindex = i;
			} else if (o instanceof UFDate) {
				UFDate value = (UFDate) o;
				UFDate oldvalue = (UFDate) vos[maxindex].getAttributeValue(field);
				if (oldvalue == null || oldvalue.toString().trim().length() == 0 || value.after(oldvalue))
					maxindex = i;
			} else if (o instanceof UFDateTime) {
				UFDateTime value = (UFDateTime) o;
				UFDateTime oldvalue = (UFDateTime) vos[maxindex].getAttributeValue(field);
				if (oldvalue == null || oldvalue.toString().trim().length() == 0 || value.after(oldvalue))
					maxindex = i;
			} else {
				isAllNull = true;
			}
		}

		if (isAllNull)
			maxindex = -1;

		return maxindex;
	}

	/**
	 * 给定VO数组，给定字段，返回指定字段的最小值； 
	 * 注意：若VO数组中该字段值均为空，则返回NULL； 
	 * 作者：薛恩平 
	 * 创建日期：2005-3-22 19:57:30
	 * 
	 * @param vos
	 * @param field
	 * @return
	 */
	public static Object min(CircularlyAccessibleValueObject[] vos, String field) {

		int min = minIndex(vos, field);
		if (min != -1)
			return vos[min].getAttributeValue(field);
		else
			return null;
	}

	/**
	 * 给定VO数组，给定字段，返回指定字段最小值VO的索引；
	 * 注意：若VO数组中该字段值均为空，或VO数组中该字段类型为非数值、日期/时间类型，则返回-1； 
	 * 作者：薛恩平 
	 * 创建日期：2005-3-22 19:57:27
	 * 
	 * @param vos
	 * @param field
	 * @return
	 */
	public static int minIndex(CircularlyAccessibleValueObject[] vos, String field) {

		int minindex = -1;

		if (vos == null || vos.length == 0)
			return minindex;

		minindex = 0;
		boolean isAllNull = true;
		for (int i = 0; i < vos.length; i++) {
			Object o = vos[i].getAttributeValue(field);
			if (o == null || o.toString().trim().length() == 0)
				continue;

			isAllNull = false;
			if (o instanceof Number) {
				Number value = (Number) o;
				Number oldvalue = (Number) vos[minindex].getAttributeValue(field);
				if (oldvalue == null || oldvalue.toString().trim().length() == 0
						|| value.doubleValue() < oldvalue.doubleValue())
					minindex = i;
			} else if (o instanceof UFDate) {
				UFDate value = (UFDate) o;
				UFDate oldvalue = (UFDate) vos[minindex].getAttributeValue(field);
				if (oldvalue == null || oldvalue.toString().trim().length() == 0 || value.before(oldvalue))
					minindex = i;
			} else if (o instanceof UFDateTime) {
				UFDateTime value = (UFDateTime) o;
				UFDateTime oldvalue = (UFDateTime) vos[minindex].getAttributeValue(field);
				if (oldvalue == null || oldvalue.toString().trim().length() == 0 || value.before(oldvalue))
					minindex = i;
			} else {
				isAllNull = true;
			}
		}

		if (isAllNull)
			minindex = -1;

		return minindex;
	}

	/**
	 * 解析编码规则 
	 * 作者：薛恩平 
	 * 创建日期：2005-3-24 9:18:04
	 * 
	 * @param coderule
	 * @return
	 */
	public static int[] parsCodeRule(String codeRule) {

		int[] codeSection = null;

		if (codeRule == null || codeRule.trim().length() == 0)
			return null;

		try {
			// 编码规则解析
			StringTokenizer st = new StringTokenizer(codeRule, " ,./\\", false);
			int count = st.countTokens();
			codeSection = new int[count];
			int index = 0;
			while (st.hasMoreTokens()) {
				codeSection[index++] = Integer.parseInt(st.nextToken().trim());
			}
		} catch (Exception e) {
			System.out.println("解析编码规则出错");
			e.printStackTrace();
			codeSection = null;
		}

		return codeSection;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-3-3 19:27:47)
	 * 
	 * @return nc.vo.pub.SuperVO[]
	 * @param vos
	 *            nc.vo.pub.SuperVO[]
	 * @param fieldKey
	 *            java.lang.String
	 * @deprecated
	 */
	public static CircularlyAccessibleValueObject[] sort(CircularlyAccessibleValueObject[] vos, String fieldKey) {

		if (vos == null || fieldKey == null)
			return null;

		Vector tempV = new Vector();
		for (int i = 0; i < vos.length; i++) {
			tempV.add(vos[i]);
		}

		int sortind = 0;
		while (tempV.size() > 0) {
			int ind = 0;
			SuperVO vo = (SuperVO) tempV.get(0);
			if (vo.getAttributeValue(fieldKey) == null) {
				vos[sortind] = vo;
				tempV.remove(ind);
				sortind++;
				continue;
			}
			for (int i = 0; i < tempV.size(); i++) {
				SuperVO vo1 = (SuperVO) tempV.get(i);
				if (vo.getAttributeValue(fieldKey) == null) {
					vos[sortind] = vo;
					tempV.remove(ind);
					sortind++;
					i++;
					continue;
				} else {
					double value1 = ((UFDouble) vo.getAttributeValue(fieldKey)).doubleValue();
					if (vo1.getAttributeValue(fieldKey) == null) {
						vo = vo1;
						ind = i;
					} else {
						double value2 = ((UFDouble) vo1.getAttributeValue(fieldKey)).doubleValue();
						if (value2 < value1) {
							vo = vo1;
							ind = i;
						}
					}
				}
			}
			vos[sortind] = vo;
			tempV.remove(ind);
			sortind++;
		}

		return vos;
	}
	
	/**
	 * 给定VO数组，给定字段，返回指定字段值的和； 
	 * 注意：若VO数组中该字段值均为空，或VO数组中该字段类型为非数值类型，则返回NULL； 
	 * 作者：薛恩平
	 * 创建日期：2005-3-22 19:57:34
	 * 
	 * @param vos
	 * @param field
	 * @return
	 */
	public static Object sum(CircularlyAccessibleValueObject[] vos, String field) {

		Object retObj = null;

		if (vos == null || vos.length == 0)
			return retObj;

		Object fieldValue = null;
		double sum = 0.0;
		boolean isHasZero = false;
		for (int i = 0; i < vos.length; i++) {
			Object o = vos[i].getAttributeValue(field);
			if (o == null || o.toString().trim().length() == 0)
				continue;

			fieldValue = o;
			if (o instanceof Number) {
				double value = ((Number) o).doubleValue();
				sum += value;
				if (!isHasZero && value == 0.0) {
					isHasZero = true;
				}
			}
		}

		if (sum == 0.0 && isHasZero || sum != 0.0) {
			if (fieldValue instanceof Integer) {
				retObj = new Integer((int) sum);
			} else if (fieldValue instanceof UFDouble) {
				retObj = new UFDouble(sum);
			}
		}

		return retObj;
	}
	
	/**
	 * 从字符串数组转换成字符串形式
	 * 传入String[]，返回可以直接用在SQL中in()的字符串
	 */
		  public static String arrayToString(String[] temp){
		   StringBuffer st = null;
		   if(temp != null){
		    st = new StringBuffer();
		    for(int i=0;i<temp.length;i++){
		     st.append("'");
		     st.append(temp[i]);
		     st.append("'");
		     st.append(",");
		    }	     
		   }
		   if (st != null){
		    return st.substring(0,st.length()-1).toString();
		   }  
		   else
		    return "";
		  }
		  
		  /**
		   * 从字符串数组转换成字符串形式
		   * 传入String[]，拼接的字符串
		   */
		  	  public static String arrayToStringNotinsql(String[] temp){
		  	   StringBuffer st = null;
		  	   if(temp != null){
		  	    st = new StringBuffer();
		  	    for(int i=0;i<temp.length;i++){
		  	     st.append(temp[i]);
		  	     st.append(",");
		  	    }	     
		  	   }
		  	   if (st != null){
		  	    return st.substring(0,st.length()-1).toString();
		  	   }  
		  	   else
		  	    return "";
		  	  }
		  
		  public static String arrayToString(ArrayList<String> arrItem){
			  return arrayToString(arrItem.toArray(new String[0]));
		  }

}