package nc.vo.zmpub.pub.report2;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;

import nc.ui.scm.util.ObjectUtils;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.zmpub.pub.report.IUFTypes;
import nc.vo.zmpub.pub.report.ReportBaseVO;
public class CombinVO {
	   /**
	    * @作者：mlr
	    * @说明：完达山物流项目 
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
		 * @说明：完达山物流项目
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
		public static ReportBaseVO[] combinVoByFields(ReportBaseVO[] vos, ReportBaseVO[] vos1,
				String[] voCombinConds, int[] types,String[] combinFields) {
			//记录  vos1中已经被合并过的vo
			List<ReportBaseVO> list=new ArrayList<ReportBaseVO>();
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
				ReportBaseVO avo = vos[i];
				for (int j = 0; j < size1; j++) {
					ReportBaseVO bvo = vos1[j];
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
			List<ReportBaseVO> list1=new ArrayList<ReportBaseVO>();//纪录vos1中没有被匹配的vo
			int csize=vos1.length;
			for(int i=0;i<csize;i++){			
				ReportBaseVO avo=vos1[i];				
				int csize1=list.size();
				for(int j=0;j<csize1;j++){
					boolean isEqual = true;
					ReportBaseVO bvo=list.get(j);
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
			    return list1.toArray(new ReportBaseVO[0]);
			  }else{
				return vos;
			 }	
		}	
	/**
	 * 	将 vos1 中的数据 追加的 vos 中
	 * @author mlr
	 * @说明：（鹤岗矿业）
	 * 2011-12-25上午09:55:11
	 * @param vos
	 * @param vos1
	 */
	public static void  add(ReportBaseVO vos,ReportBaseVO vos1,String xid){
		if(vos==null){
			return;
		}
		if(vos1==null){
			return;
		}
	   String[] fields= vos1.getAttributeNames();
	   if(fields==null || fields.length==0)
		   return;
	   for(int i=0;i<fields.length;i++){
		   if(xid==null || xid.length()==0){
			 vos.setAttributeValue(fields[i], vos1.getAttributeValue(fields[i]));  
		   }else{
		     vos.setAttributeValue(fields[i]+xid, vos1.getAttributeValue(fields[i]));
		   }
	   }
		
	}
	/**
	 * 将两个数组中的数据合并到一起
	 * @author mlr
	 * @说明：（鹤岗矿业）
	 * 2011-12-28上午09:33:15
	 * @param vos
	 * @param vos1
	 * @return
	 * @throws Exception
	 */
	public static ReportBaseVO[] comin(ReportBaseVO[] vos,ReportBaseVO[] vos1)throws Exception{
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
		List<ReportBaseVO> list=new ArrayList<ReportBaseVO>();
		for(int i=0;i<vos.length;i++){
			list.add(vos[i]);
		}
		for(int j=0;j<vos1.length;j++){
			list.add(vos1[j]);
		}
		return list.toArray(new ReportBaseVO[0]);
	}
	/**
	 * 	数据追加 按某个条件维度  将符合条件的数据  
	 *  拼接到一起
	 *  限制条件： 按维度条件查询  各组数据必须是 唯一的
	 *  要想保证数据唯一的话 可以先调用combinVoByFields方法进行维度合并\
	 *  将vos2的数据追加到vos1中
	 * @author mlr
	 * @说明：（鹤岗矿业）
	 * 2011-12-25上午09:44:00
	 * @param vos1
	 * @param vos2
	 * @param conds
	 * @return
	 * @throws Exception
	 */
     public static ReportBaseVO[] addByContion1(ReportBaseVO[] vos,ReportBaseVO[] vos1,String[] voCombinConds,String xid)throws Exception{
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
		//记录  vos1中已经被合并过的vo
		List<ReportBaseVO> list=new ArrayList<ReportBaseVO>();

		int size=vos.length;
		int size1=vos1.length;
			//拿 vos中的每个vo 按条件遍历vos1
			//将符合条件的 vos1 加到 vo上
			for (int i = 0; i < size; i++) {
				ReportBaseVO avo = vos[i];
				for (int j = 0; j < size1; j++) {
					ReportBaseVO bvo = vos1[j];
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
						add(avo,bvo,xid);
					}
				}
			}			
		return vos;
	}			
		
		
     /**
 	 * 	数据追加 按某个条件维度  将符合条件的数据  
 	 *  拼接到一起
 	 *  限制条件： 按维度条件查询  各组数据必须是 唯一的
 	 *  要想保证数据唯一的话 可以先调用combinVoByFields方法进行维度合并
 	 *  
 	 *  是对  addByContion1的改进
 	 *  会将  vos1中按维度查询到的 而在vos中按同样的维度查询却查不到的
 	 *  会在vos中新增一个vo将vos1中的该数据追加到vos中
 	 *  
 	 * @author mlr
 	 * @说明：（鹤岗矿业）
 	 * 2011-12-25上午09:44:00
 	 * @param vos1 报表集合
 	 * @param vos2 报表集合
 	 * @param conds 追加条件
 	 * @param xid 追加标识   将vos1中的数据追加到vos中时 会将 vos1中属性名字 改为 属性名+xid
 	 *             如果 xid为空 则不追加标示
 	 * @return
 	 * @throws Exception
 	 */
 public static ReportBaseVO[] addByContion2(ReportBaseVO[] vos,ReportBaseVO[] vos1,String[] voCombinConds,String xid)throws Exception{
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
 		//记录  vos1中已经被合并过的vo
 		List<ReportBaseVO> list=new ArrayList<ReportBaseVO>();

 		int size=vos.length;
 		int size1=vos1.length;
 			//拿 vos中的每个vo 按条件遍历vos1
 			//将符合条件的 vos1 加到 vo上
 			for (int i = 0; i < size; i++) {
 				ReportBaseVO avo = vos[i];
 				for (int j = 0; j < size1; j++) {
 					ReportBaseVO bvo = vos1[j];
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
 						add(avo,bvo,xid);
 						list.add(bvo);
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
 			    return (ReportBaseVO[]) list1.toArray((CircularlyAccessibleValueObject[]) java.lang.reflect.Array.newInstance(vos[0].getClass(), 0));
 			  }else{
 				return vos;
 			 }		
 	}			
		/**
		 * 
		 * @作者：mlr
		 * @说明：完达山物流项目 
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
		 /**
	     * 按最小维度进行数据合并
	     */
		/**
		 * @author mlr  按台账统计维度合并数据
		 * @param accounts
		 * @return
		 * @throws Exception 
		 */
		public static  ReportBaseVO[] combinVoByFields(ReportBaseVO[] accounts,String[] num_condition_fields,String[] combinFields) throws Exception{
			if(accounts==null || accounts.length==0)
				return null;
			if(num_condition_fields==null || num_condition_fields.length==0)
				throw new Exception(" 合并条件字段为空");	
			
			CircularlyAccessibleValueObject[][] os = SplitBillVOs.getSplitVOs(accounts, num_condition_fields);
			int len = os.length;
			ReportBaseVO[] newAccouts = new ReportBaseVO[os.length];
			ReportBaseVO[] datas = null;
			ReportBaseVO tmp = null;
			for(int i=0;i<len;i++){
				datas = (ReportBaseVO[])os[i];
				tmp = datas[0];
				for(int j=0;j<datas.length;j++){
					if(j == 0)
						continue;
					for(String num:combinFields){
						tmp.setAttributeValue(num, 
								PuPubVO.getUFDouble_NullAsZero(tmp.getAttributeValue(num))
								.add(PuPubVO.getUFDouble_NullAsZero(datas[j].getAttributeValue(num))));
					}
				}
				newAccouts[i] = tmp;
			}
			return newAccouts;
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
			CircularlyAccessibleValueObject[] newVos=(CircularlyAccessibleValueObject[]) java.lang.reflect.Array.newInstance(vos[0].getClass(),voss.length);
			int size=voss.length;
			for(int i=0;i<size;i++){
				CircularlyAccessibleValueObject newVo=null;
				int size1=voss[i].length;
			    for(int j=0;j<size1;j++){
			    	CircularlyAccessibleValueObject oldVo=(CircularlyAccessibleValueObject) voss[i][j];
			    	if(newVo==null){
			    		try {
							newVo=(CircularlyAccessibleValueObject) ObjectUtils.serializableClone(oldVo);
						} catch (Exception e) {
							e.printStackTrace();
						}				
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
	 * @author zhf
	 * @说明：（鸡西矿业）vo合并
	 * 2011-9-26下午02:57:12
	 * @param vos
	 * @param voCombinConds 合并维度
	 * @param combinFields 求和字段   说明：这些字段均仅支持UFDouble
	 * @return
	 * @throws BusinessException
	 */
	public static CircularlyAccessibleValueObject[] combinData(
			CircularlyAccessibleValueObject[] vos,
			String[] voCombinConds, 
			String[] combinFields,
			Class classname){
		if(vos == null || vos.length == 0 || voCombinConds == null || voCombinConds.length == 0)
			return null;
		java.util.Map<String,CircularlyAccessibleValueObject> dataMap = new HashMap<String, CircularlyAccessibleValueObject>();
		StringBuffer key = new StringBuffer();
		String skey = null;
		CircularlyAccessibleValueObject tmpvo = null;
		for(CircularlyAccessibleValueObject vo:vos){
			key.setLength(0);
			for(String name:voCombinConds){
				key.append(vo.getAttributeValue(name));
			}
			skey = key.toString();
			if(dataMap.containsKey(skey)){
				tmpvo = dataMap.get(skey);
				for(String name:combinFields){
					vo.setAttributeValue(name, PuPubVO.getUFDouble_NullAsZero(vo.getAttributeValue(name))
							.add(PuPubVO.getUFDouble_NullAsZero(tmpvo.getAttributeValue(name))));
				}
			}
			
			dataMap.put(skey, vo);
		}
		if(dataMap.size() == 0)
			return null;
		CircularlyAccessibleValueObject[] tarVos = (CircularlyAccessibleValueObject[])java.lang.reflect.Array.newInstance(classname, dataMap.size());
	    dataMap.values().toArray(tarVos);
	    return tarVos;
	}

}
