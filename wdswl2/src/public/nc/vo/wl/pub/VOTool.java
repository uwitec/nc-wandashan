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
 * ���ߣ������ ���ܣ�VO���� ���ڣ�(2004-2-11 15:20:16)
 * 
 * @modified by twh on 2006-7-12 ����09:54:40
 *           <p>
 *           �޸����ݣ�(�����б��tag���ݵ�λ��)
 *           <li>1.<tag>�ۺ�VO Clone</tag>��
 *           <li>2.<tag></tag>��
 *           <li>3.<tag></tag>��
 * 
 */
public class VOTool {
	/**
	 * ������Ԫ������������ArrayList 
	 * ���ߣ�Ѧ��ƽ 
	 * �������ڣ�2004-12-20 10:01:09
	 * 
	 * @param vos
	 */
	public static void addVOsToArrayList(CircularlyAccessibleValueObject[] vos, ArrayList al) {
		for (int i = 0; vos != null && i < vos.length; i++) {
			al.add(vos[i]);
		}
	}

	/**
	 * �ۺ�VO Clone
	 * 
	 * @param vo
	 * @return twh (2006-7-12 ����09:56:24)<br>
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
	 * Object������������������һ�£�Ҫ��objs�е����ͱ���һ�� 
	 * ���ߣ�Ѧ��ƽ 
	 * �������ڣ�2005-4-7 11:07:36
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
	 * VO����Clone��Ҫ��vos�е�VO���ͱ���һ�� 
	 * ���ߣ�Ѧ��ƽ 
	 * �������ڣ�(2004-8-20 16:13:18)
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
	 * VO���ƣ�֧�ֶ��ӱ�ĸ���
	 * 
	 * created by chenliang at 2007-11-7 ����10:37:23
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

		// ���ڶ��ӱ������⴦��
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
	 * ����VO����groupKey 
	 * ���ߣ�Ѧ��ƽ 
	 * �������ڣ�2005-4-1 16:42:11
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
	 * ���ݱ�����򡢼��η��ر��볤��
	 * 1���ޱ�����򣬻��뼶�� <= 0������-1��
	 * 2�����뼶�� > ���������鳤�ȣ����ر��������󳤶ȣ�
	 * 3��1 <= ���뼶�� <= ���������鳤�ȣ����ؼ���ı��볤�ȣ� 
	 * ���ߣ�Ѧ��ƽ 
	 * �������ڣ�2005-3-24 9:53:56
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
	 * ���ݷ����ֶι���Ψһ��VO��ϣ�������ظ������쳣 
	 * ���ߣ�Ѧ��ƽ 
	 * �������ڣ�2005-4-6 15:14:07
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
				throw new BusinessException("���ݷ����ֶΣ���vos�����ظ���¼�����飡");
			}
		}

		return hash;
	}

	/**
	 * ����IHashKey�Է����������
	 * ���ߣ�Ѧ��ƽ
	 * �������ڣ�2008-3-1 ����04:47:40
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
	 * ����VO�����Ƿ���ȫ��ȣ�����id���⣩ 
	 * ���ߣ�Ѧ��ƽ 
	 * �������ڣ�2007-4-5 ����08:13:30
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
	 * �Ƿ�Ϊnull����ַ��� 
	 * ���ߣ�Ѧ��ƽ 
	 * �������ڣ�2005-4-7 22:45:53
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
	 * ����VO���飬�����ֶΣ�����ָ���ֶε����ֵ�� 
	 * ע�⣺��VO�����и��ֶ�ֵ��Ϊ�գ��򷵻�NULL�� 
	 * ���ߣ�Ѧ��ƽ 
	 * �������ڣ�2005-3-22 19:57:24
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
	 * ����VO���飬�����ֶΣ�����ָ���ֶ����ֵVO��������
	 * ע�⣺��VO�����и��ֶ�ֵ��Ϊ�գ���VO�����и��ֶ�����Ϊ����ֵ������/ʱ�����ͣ��򷵻�-1�� 
	 * ���ߣ�Ѧ��ƽ 
	 * �������ڣ�2005-3-22 19:57:21
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
	 * ����VO���飬�����ֶΣ�����ָ���ֶε���Сֵ�� 
	 * ע�⣺��VO�����и��ֶ�ֵ��Ϊ�գ��򷵻�NULL�� 
	 * ���ߣ�Ѧ��ƽ 
	 * �������ڣ�2005-3-22 19:57:30
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
	 * ����VO���飬�����ֶΣ�����ָ���ֶ���СֵVO��������
	 * ע�⣺��VO�����и��ֶ�ֵ��Ϊ�գ���VO�����и��ֶ�����Ϊ����ֵ������/ʱ�����ͣ��򷵻�-1�� 
	 * ���ߣ�Ѧ��ƽ 
	 * �������ڣ�2005-3-22 19:57:27
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
	 * ����������� 
	 * ���ߣ�Ѧ��ƽ 
	 * �������ڣ�2005-3-24 9:18:04
	 * 
	 * @param coderule
	 * @return
	 */
	public static int[] parsCodeRule(String codeRule) {

		int[] codeSection = null;

		if (codeRule == null || codeRule.trim().length() == 0)
			return null;

		try {
			// ����������
			StringTokenizer st = new StringTokenizer(codeRule, " ,./\\", false);
			int count = st.countTokens();
			codeSection = new int[count];
			int index = 0;
			while (st.hasMoreTokens()) {
				codeSection[index++] = Integer.parseInt(st.nextToken().trim());
			}
		} catch (Exception e) {
			System.out.println("��������������");
			e.printStackTrace();
			codeSection = null;
		}

		return codeSection;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-3-3 19:27:47)
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
	 * ����VO���飬�����ֶΣ�����ָ���ֶ�ֵ�ĺͣ� 
	 * ע�⣺��VO�����и��ֶ�ֵ��Ϊ�գ���VO�����и��ֶ�����Ϊ����ֵ���ͣ��򷵻�NULL�� 
	 * ���ߣ�Ѧ��ƽ
	 * �������ڣ�2005-3-22 19:57:34
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
	 * ���ַ�������ת�����ַ�����ʽ
	 * ����String[]�����ؿ���ֱ������SQL��in()���ַ���
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
		   * ���ַ�������ת�����ַ�����ʽ
		   * ����String[]��ƴ�ӵ��ַ���
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