package nc.vo.wl.pub;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.List;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
public class CombinVO {
	  /**
	    * @作者：mlr
	    * @说明：
	    *        判断两个对象是否相等
	    * @时间：2011-7-12上午10:34:05
	    * @param o1 要比较的对象
	    * @param o2 要比较的对象
	    */
		private static boolean isEqual(Object o1, Object o2) {
			if(isEmpty(o1) && !isEmpty(o2)){
				return false;
			}
			if(!isEmpty(o1) && isEmpty(o2)){
				return false;
			}
		    if(!isEmpty(o1) && !isEmpty(o2)){
		    	if(!o1.equals(o2)){
		    		return false;
		    	}
		    }
		    return true;
		}
		/**
		 * @作者：mlr
		 * @说明：
		 *        根据某个维度(条件)
		 *        将两个数组中条件字段对应值相同的合并
		 *        根据 求值字段数组和类型数组 判断需要求和的字段
		 *        进行求和运算
		 *        
		 *        使用本方法的前提条件：
	     *        两个vo数组按维度条件只能查到一个符合条件的vo           
		 * @时间：2011-7-11下午09:12:25
		 * @param vos  要合并的报表vos
		 * @param vos1 要合并的报表vos1
		 * @param voCombinConds 条件字段数组
		 * @param types 求值类型
		 * @param combinFields 求值字段
		 * @return
		 */
		public static CircularlyAccessibleValueObject[] combinVoByFields(CircularlyAccessibleValueObject[] vos, CircularlyAccessibleValueObject[] vos1,
				String[] voCombinConds, int[] types,String[] combinFields) {
			//记录  vos1中已经被合并过的vo
			List<CircularlyAccessibleValueObject> list=new ArrayList<CircularlyAccessibleValueObject>();
			if (isEmpty(vos)) {
				if (!isEmpty(vos1)) {
					return vos1;
				}
			}
			if (isEmpty(vos1)) {
				if (!isEmpty(vos)) {
					return vos;
				}
			}
			if (isEmpty(vos) &&isEmpty(vos1)) {
				return null;
			}
			int size=vos.length;
			int size1=vos1.length;
			//拿 vos中的每个vo 按条件遍历vos1
			//将符合条件的 vos1中的vo 对应的值 加到vos的vo中
			for (int i = 0; i < size; i++) {
				CircularlyAccessibleValueObject avo = vos[i];
				for (int j = 0; j < size1; j++) {
					CircularlyAccessibleValueObject bvo = vos1[j];
					boolean isEqual = true;
					for (int k = 0; k < voCombinConds.length; k++) {
						Object o1 = avo.getAttributeValue(voCombinConds[k]);
						Object o2 = bvo.getAttributeValue(voCombinConds[k]);
						if (!isEqual(o1, o2)) {
							isEqual = false;
							break;
						}
					}
					if (isEqual) {
						list.add(bvo);
						int csize = combinFields.length;
						for (int n = 0; n < csize; n++) {
							Object resultobj = avo.getAttributeValue(combinFields[n]);
							Object tmpobj = bvo.getAttributeValue(combinFields[n]);
							switch (types[n]) {
							case IUFTypes.INT:
								int iresult = (resultobj == null ? 0: ((Integer) resultobj).intValue());
								int itmp = (tmpobj == null ? 0: ((Integer) tmpobj).intValue());
								avo.setAttributeValue(combinFields[n],new Integer(iresult + itmp));
								continue;
							case IUFTypes.LONG:
								long lgtmp = (tmpobj == null ? 0:((Long) tmpobj).longValue());
								long lgresult = (resultobj == null ? 0:((Long) resultobj).longValue());
								if (tmpobj != null)
									avo.setAttributeValue(combinFields[n],new Long(lgresult + lgtmp));
								continue;
							case IUFTypes.UFD:
								UFDouble ufdtmp = (tmpobj == null ? new UFDouble("0"): (UFDouble) tmpobj);
								UFDouble ufdResult = (resultobj == null ? new UFDouble("0"):(UFDouble) resultobj);
								avo.setAttributeValue(combinFields[n],ufdResult.add(ufdtmp));
								continue;
							case IUFTypes.STR:
								String strtmp = (tmpobj == null ? "" : tmpobj.toString());
								String strresult = (resultobj == null ? "": resultobj.toString());
								avo.setAttributeValue(combinFields[n], strtmp+ strresult);
								continue;
							}
						}
					    break;
					}
				}
			}
			//记录vos1中没有被vos匹配上的vo 进行二次合并
			//现有 vos1 和  list
		    // 按照某个维度条件 将vos1中符合条件的  但 list中不符合条件的找出来
			//如何找呢？
			//两层循环  每次拿vos1中的一个vo 去list中按条件查找符合条件的 vo
			//如果有符合条件的vo 
			//就断开 list 循环 继续下一次循环
			//如何将不符合条件的 vo从vos1中找出来
			//当循环到 list的最后一个元素 还没有符合条件的元素时, 把vos中的对应vo取出来即可			
			List<CircularlyAccessibleValueObject> list1=new ArrayList<CircularlyAccessibleValueObject>();//纪录vos1中没有被匹配的vo
			int csize=vos1.length;
			for(int i=0;i<csize;i++){			
				CircularlyAccessibleValueObject avo=vos1[i];				
				int csize1=list.size();
				for(int j=0;j<csize1;j++){
					boolean isEqual = true;
					CircularlyAccessibleValueObject bvo=list.get(j);
					for (int k = 0; k < voCombinConds.length; k++) {
						Object o1 = avo.getAttributeValue(voCombinConds[k]);
						Object o2 = bvo.getAttributeValue(voCombinConds[k]);
						if (!isEqual(o1, o2)) {
							isEqual = false;
							break;
						}
					}
					if(isEqual){					
						break;
					}
					if(j==csize1-1){
						list1.add(vos1[i]);
					}
				}
			}
			//如果list 长度为0 说明vos 和vos1 没有一个匹配上的
			if(list.size()==0){
				for(int i=0;i<vos1.length;i++){
					list1.add(vos1[i]);
				}				
			}		
			//将没有匹配上的vo合并上
			if(list1.size()>0){
			  for(int i=0;i<vos.length;i++){
				 list1.add(vos[i]);  
			  }
			    return list1.toArray(new CircularlyAccessibleValueObject[0]);
			  }else{
				return vos;
			 }	
		}			
		/**
		 * 
		 * @作者：mlr
		 * @说明：
		 *        根据某个维度(条件)
		 *        将数组中条件字段对应值相同的合并
		 *                  
		 * @时间：2011-7-11下午09:12:25
		 * @param vos
		 * @param voCombinConds 条件字段数组
		 * @param types 求值类型
		 * @param combinFields 求和字段
		 * @return
		 */
		public static CircularlyAccessibleValueObject[] combinVoByFields(CircularlyAccessibleValueObject[] vos,String[] voCombinConds, int[] types,String[] combinFields) {

		 if(vos==null || vos.length==0){
			  return vos;
		  }
		 CircularlyAccessibleValueObject[][] voss= SplitBillVOs.getSplitVOs(vos,voCombinConds);
			//new 开头的vo为重新组装放入界面的vo
			CircularlyAccessibleValueObject[] newVos=new CircularlyAccessibleValueObject[voss.length];
			int size=voss.length;
			for(int i=0;i<size;i++){
				CircularlyAccessibleValueObject newVo=null;
				int size1=voss[i].length;
			    for(int j=0;j<size1;j++){
			    	CircularlyAccessibleValueObject oldVo=(CircularlyAccessibleValueObject) voss[i][j];
			    	if(newVo==null){
			    	   newVo=(CircularlyAccessibleValueObject) oldVo.clone();
			        }else{
						int csize = combinFields.length;
						for (int n = 0; n < csize; n++) {
							Object resultobj = newVo.getAttributeValue(combinFields[n]);
							Object tmpobj = oldVo.getAttributeValue(combinFields[n]);
							switch (types[n]) {
							case IUFTypes.INT:
								int iresult = (resultobj == null ? 0: ((Integer) resultobj).intValue());
								int itmp = (tmpobj == null ? 0:((Integer) tmpobj).intValue());
								newVo.setAttributeValue(combinFields[n],new Integer(iresult + itmp));
								continue;
							case IUFTypes.LONG:
								long lgtmp = (tmpobj == null ? 0: ((Long) tmpobj).longValue());
								long lgresult = (resultobj == null ? 0:((Long) resultobj).longValue());
								if (tmpobj != null)
									newVo.setAttributeValue(combinFields[n],new Long(lgresult + lgtmp));
								continue;
							case IUFTypes.UFD:
								UFDouble ufdtmp = (tmpobj == null ? new UFDouble("0"): (UFDouble) tmpobj);
								UFDouble ufdResult = (resultobj == null ? new UFDouble("0"): (UFDouble) resultobj);
								newVo.setAttributeValue(combinFields[n],ufdResult.add(ufdtmp));
								continue;
							case IUFTypes.STR:
								String strtmp = (tmpobj == null ? "" : tmpobj.toString());
								String strresult = (resultobj == null ? "": resultobj.toString());
								newVo.setAttributeValue(combinFields[n], strtmp+ strresult);
								continue;
							}
						}		        	
			        }	    	
			    }
			    newVos[i]=newVo;
			}
			return newVos;				
		}	
	/**
	 * 
	 * @作者：mlr
	 * @说明：
	 *       判断传入的对象是否为空
	 * @时间：2011-7-5下午09:02:51
	 * @param value
	 * @return
	 */
	public static boolean isEmpty(Object value)
	{
		if (value == null)
			return true;
		if ((value instanceof String)
				&& (((String) value).trim().length() <= 0))
			return true;
		if ((value instanceof Object[]) && (((Object[]) value).length <= 0))
			return true;
		if ((value instanceof Collection) && ((Collection) value).size() <= 0)
			return true;
		if ((value instanceof Dictionary) && ((Dictionary) value).size() <= 0)
			return true;
		return false;
	}		
}
