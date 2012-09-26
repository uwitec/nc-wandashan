package nc.vo.wdsnew.pub;
import java.util.Collection;
import java.util.Dictionary;
import java.util.List;
import nc.bs.dao.BaseDAO;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
/**
 * 后台字段唯一性校验
 * author:mlr
 * */
public class BsUniqueCheck {
	private static BaseDAO dao;	
	private static BaseDAO getDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *       校验某个字段的唯一性
	 * @时间：2011-7-6下午12:36:25
	 * @param vos 校验的vo数组
	 * @param checkField  校验的字段
	 * @param errorMessage 错误提示信息
	 * @throws Exception
	 */
	public static void FieldUniqueCheck(SuperVO[] vos,String checkField,String errorMessage)throws Exception{	
		 if(isEmpty(vos)){
			   throw new BusinessException("传入的VO数组不能为空");
		   }
		for(int i=0;i<vos.length;i++){		
	      	BsUniqueCheck.FieldUniqueCheck(vos[i], checkField,null, errorMessage);
		}	
	}
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *       在某个条件下：
	 *       校验某个字段组合的唯一性
	 *       条件 即 condition
	 * @时间：2011-7-6下午12:36:25
	 * @param vos 校验的vo数组
	 * @param checkField  校验的字段
	 * @param errorMessage 错误提示信息
	 * @param condition 校验条件
	 * @throws Exception
	 */	
	public static void FieldUniqueChecks(SuperVO[] vos,String[] checkFields,String condition,String errorMessage)throws Exception{	
		 if(isEmpty(vos)){
			   throw new BusinessException("传入的VO数组不能为空");
		   }
		for(int i=0;i<vos.length;i++){		
	      	FieldUniqueChecks(vos[i], checkFields, condition,errorMessage);
		}	
	}
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *       在某个条件下：
	 *       校验某个字段组合的唯一性
	 *       条件 即 condition
	 * @时间：2011-7-6下午12:36:25
	 * @param vos 校验的vo数组
	 * @param checkField  校验的字段
	 * @param errorMessage 错误提示信息
	 * @param condition 校验条件
	 * @throws Exception
	 */	
	public static void FieldUniqueChecks1(AggregatedValueObject billvo,String[] checkFields,String headcontion,String bodycontion,String errorMessage)throws Exception{	
		 if(isEmpty(billvo)){
			   throw new BusinessException("传入的聚合vo不能为空");
		   }
		 FieldUniqueChecks(billvo,checkFields,headcontion,bodycontion,errorMessage);
		 
	}
	/**
	    * 
	    * @作者：mlr
	    * @说明：完达山物流项目
	    *     在某个条件下 
	    *     校验表体组合字段的唯一性  
	    * @时间：2011-7-5下午08:49:35
	    * @param vo 校验vo
	    * @param checkFields 校验字段数组的名字
	    * @param errorMessage 返回错误提示信息
	    * @param headconditon  表头过滤条件
	    * @param bodyconditon  表体过滤条件
	    * @throws Exception
	    */
		public static void FieldUniqueChecks(AggregatedValueObject billvo, String[] checkFields,String headconditon,String bodyconditon,String errorMessage) throws Exception {
			if(isEmpty(billvo)){
				return;
			}
		    if(isEmpty(billvo.getParentVO())){
		    	return;
		    }
		    if(isEmpty(billvo.getChildrenVO())){
		    	return;
		    }
			if(isEmpty(checkFields)){
				throw new BusinessException("检验唯一性的的字段名字不能为空");
			}
			if(isEmpty(errorMessage)){
				throw new BusinessException("错误提示信息不能为空");
			}	
		    SuperVO headVo=(SuperVO) billvo.getParentVO();
		    SuperVO[] bodyVos=(SuperVO[]) billvo.getChildrenVO();
		    int size=bodyVos.length;
		    for(int j=0;j<size;j++){
			// 判断是修改后的保存还是新增后的保存
			if (isEmpty(bodyVos[j].getPrimaryKey())) {
				queryByCheckFields(headVo,bodyVos[j], checkFields,headconditon,bodyconditon,errorMessage);
			} else {
	            List list=queryByPrimaryKey(bodyVos[j]);
				// 判断修改后的记录，是否改变了值（即拿数据库中的记录和ui中的当前记录进行比较）
				SuperVO vo1 = (SuperVO) list.get(0);
				//判断是否修改了要校验字段的值
				boolean ismodrec = false;			
				//校验是否修改了要校验的字段的值
				for (int i = 0; i < checkFields.length; i++) {				
					if(isEmpty(bodyVos[j].getAttributeValue(checkFields[i]))&& isEmpty(vo1.getAttributeValue(checkFields[i]))){
						continue;
					}
					if(isEmpty(bodyVos[j].getAttributeValue(checkFields[i]))|| isEmpty(vo1.getAttributeValue(checkFields[i]))){
						ismodrec=true;
						break;
					}
					if (!bodyVos[j].getAttributeValue(checkFields[i]).equals(vo1.getAttributeValue(checkFields[i]))) {
						ismodrec = true;
						break;
					}
				}	
				if (ismodrec) {
					queryByCheckFields(headVo,bodyVos[j],checkFields,headconditon,bodyconditon,errorMessage);
				}
			 }
		    }
		}	
		/**
		    * 
		    * @作者：mlr
		    * @说明：完达山物流项目 
		    *       校验 表体组合字段的唯一性
		    * @时间：2011-7-5下午08:49:35
		    * @param vo 校验vo
		    * @param checkFields 校验字段数组的名字
		    * @param headconditon 表头条件
		    * @param bodyconditon 表体条件
		    * @param errorMessage 返回错误提示信息
		    * @throws Exception
		    */
	   private static void queryByCheckFields(SuperVO headVo,SuperVO bodyVo,String[] checkFields,String headconditon,String bodyconditon,String errorMessage) throws Exception{
			  if(isEmpty(headVo))
				  return;
			  if(isEmpty(bodyVo))
				  return;		  
		       StringBuffer cond = new StringBuffer();
				for (int i = 0; i < checkFields.length; i++) {
					String sign=" = ";
					if(isEmpty(bodyVo.getAttributeValue(checkFields[i]))){				
						if(!(bodyVo.getAttributeValue(checkFields[i]) instanceof String)&&!"".equalsIgnoreCase((String) bodyVo.getAttributeValue(checkFields[i]))){
							sign=" is ";
						}			
					}					
					if(isChar(bodyVo.getAttributeValue(checkFields[i]))&& sign.equalsIgnoreCase(" = ")){
						cond.append(" " + checkFields[i] +sign+"'"+ bodyVo.getAttributeValue(checkFields[i])+"'"+ " and");
						}else{
						cond.append(" " + checkFields[i] +sign+ bodyVo.getAttributeValue(checkFields[i])+" and");
						}
				}
				int length=cond.toString().length();
				String condition = cond.toString().substring(0,length-4) + " and  isNull("+ bodyVo.getEntityName() + ".dr,0)=0";
				List list = (List) getDao().retrieveByClause(bodyVo.getClass(),condition);
				String sql=" select count(*) from "+bodyVo.getEntityName()+" join "+headVo.getEntityName()+" on "+bodyVo.getEntityName()+"."+bodyVo.getParentPKFieldName()+"="+
				headVo.getEntityName()+"."+headVo.getPKFieldName()+" where "+condition+headconditon+bodyconditon;
				Object o=getDao().executeQuery(sql,new ColumnProcessor());
				if(o==null){
					return;
				}
				Integer num=(Integer)o;
				if (num.intValue()==0) {
					return;
				} else {
					throw new BusinessException(errorMessage);
				}			 
	     }
	
	
	
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *       在某个条件下 
	 *       校验数据库中某个字段的唯一性
	 *       条件 即：condition
	 * @时间：2011-7-5下午08:46:12
	 * @param vo  校验的vo
	 * @param checkField  校验的字段的名字 
	 * @param errorMessage 错误提示信息
	 * @param condition 过滤条件
	 * @throws Exception
	 */
	public static void FieldUniqueCheck(SuperVO vo,String checkField,String condition,String errorMessage) throws Exception{			
		if(isEmpty(vo)){
			return;
		}
		if(isEmpty(checkField)){
			throw new BusinessException("检验唯一性的的字段名字不能为口空");
		}
		if(isEmpty(errorMessage)){
			throw new BusinessException("错误提示信息不能为口空");
		}	
		// 判断是修改后的保存还是新增后的保存
		if (isEmpty(vo.getPrimaryKey())){
			queryByCheckField(vo,checkField,condition,errorMessage);
		}else{		
			List list=queryByPrimaryKey(vo);
			// 判断修改后的记录，是否改变了值（即拿数据库中的记录和ui中的当前记录进行比较）
			SuperVO vo1=(SuperVO) list.get(0);
			if(isEmpty(vo.getAttributeValue(checkField)) && isEmpty(vo1.getAttributeValue(checkField))){
                 return;			
			}	
			if(!isEmpty(vo.getAttributeValue(checkField)) && !isEmpty(vo1.getAttributeValue(checkField))){
				if(vo.getAttributeValue(checkField).equals(vo1.getAttributeValue(checkField))){
					return;
				}else{
					queryByCheckField(vo,checkField,condition,errorMessage);
				}
			}
			if(isEmpty(vo.getAttributeValue(checkField)) || isEmpty(vo.getAttributeValue(checkField))){			   				
				queryByCheckField(vo,checkField,condition,errorMessage);
			}
		}			
	}
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *       在某个条件下 
	 *       校验某个字段的数据库唯一性
	 * @时间：2011-7-6下午09:35:24
	 * @param vo
	 * @param checkField
	 * @param condition
	 * @param errorMessage
	 */
	private static void queryByCheckField(SuperVO vo, String checkField,
			String condition1, String errorMessage) throws Exception{
		 String sign=" = ";
			if(isEmpty(vo.getAttributeValue(checkField))){				
				if(!(vo.getAttributeValue(checkField) instanceof String)&&!"".equalsIgnoreCase((String) vo.getAttributeValue(checkField))){
					sign=" is ";
				}				
			}	
		    String value="";
		    if(isChar(value)&& sign.equalsIgnoreCase(" = ")){
		    	value="'"+vo.getAttributeValue(checkField)+"'";
		    }else{
		    	value=vo.getAttributeValue(checkField)+"";
		    }
			
			String condition = checkField +sign + value+ " and  isNull("+vo.getEntityName()+".dr,0)=0 "+condition1;
			List list = (List) getDao().retrieveByClause(vo.getClass(),
					condition);
			if (list == null || list.size() == 0) {
				return;				
			}else{
				throw new BusinessException(errorMessage);
			}			
		
	}
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *       组合字段的区间交叉的唯一性校验
	 *       即校验某个维度下，在最小值 和 最大值 之间只存在 一条记录
	 *       
	 * @时间：2011-7-6上午09:50:30
	 * @param vo
	 * @param checkFields 校验的字段名字
	 * @param minField    最小值
	 * @param maxField    最大值
	 * @param errorMessage 错误提示信息
	 * @throws Exception
	 */
	public static void FieldUniqueCheckInment(SuperVO vo,String[] checkFields,String minField,String maxField,String errorMessage)throws Exception{
	   if(isEmpty(vo)){
		   return ;
	   }
	   if(isEmpty(checkFields)){
		   throw new BusinessException("校验的字段不能为空");
	   }
	   if(isEmpty(minField) || isEmpty(maxField)){
		   throw new BusinessException("区间最小值或最大值不允许为空");
	   }
	   if(isEmpty(vo.getAttributeValue(minField)) || isEmpty(vo.getAttributeValue(maxField))){
		   throw new BusinessException("传入的vo 最小值 或 最大值不能为空"); 
	   }	   
	   if(isEmpty(vo.getPrimaryKey())){
		 //新增后的保存   
		   queryByCheckFieldsInMent(vo,checkFields,minField,maxField,errorMessage);
	   }else{
		  //修改后保存
		   List list=queryByPrimaryKey(vo);
		   SuperVO svo=(SuperVO) list.get(0);		   
		   boolean ismodrec = false;			
			//校验是否修改了要校验的字段的值
			for (int i = 0; i < checkFields.length; i++) {				
				if(isEmpty(vo.getAttributeValue(checkFields[i]))&& isEmpty(vo.getAttributeValue(checkFields[i]))){
					continue;
				}
				if(isEmpty(vo.getAttributeValue(checkFields[i]))|| isEmpty(vo.getAttributeValue(checkFields[i]))){
					ismodrec=true;
					break;
				}
				if (!vo.getAttributeValue(checkFields[i]).equals(svo.getAttributeValue(checkFields[i]))) {
					ismodrec = true;
					break;
				}
			}						
		    if(ismodrec){
		       queryByCheckFieldsInMent(vo,checkFields,minField,maxField,errorMessage);
		    }else{	    	
		    	queryByCheckFieldsInMent1(vo,svo,checkFields,minField,maxField,errorMessage);				
		    }   
	   }			
	}
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *       该方法用来在修改保存时：
	 *       
	 *       校验字段即 checkFields 存的值并没有做修改
	 *       保存 但区间最小值(minField) 和区间最大值(maxField)
	 *       可能做了修改 
	 *       
	 *       这种情况下用该法发校验       
	 * @时间：2011-7-6下午12:19:38
	 * @param vo 要保存的vo
	 * @param svo 根据主键从数据库中查出的vo
	 * @param checkFields 校验的字段
	 * @param minField    区间最小值
	 * @param maxField    区间最大值
	 * @param errorMessage 错误提示信息
	 * @throws Exception
	 */
	private static void queryByCheckFieldsInMent1(SuperVO vo,SuperVO svo,String[] checkFields,
			String minField, String maxField,String errorMessage) throws Exception {    	
    	Object voMin=vo.getAttributeValue(minField);
		Object voMax=vo.getAttributeValue(maxField);
		Object svoMin=svo.getAttributeValue(minField);
		Object svoMax=svo.getAttributeValue(maxField);	
		if(isEmpty(voMin)|| isEmpty(voMax)){
			throw new BusinessException("传入的vo最小值 和 最大值不能为空");
		}
		if(isEmpty(svoMin)|| isEmpty(svoMax)){
			return;
		}
		if(voMin.equals(svoMin) && voMax.equals(svoMax)){
			return;
		}else{
			List listo= queryByCheckFields(vo,checkFields);
			if(isEmpty(listo)){
				throw new BusinessException("该记录已经被删除,请刷新界面");
			}
			int size=listo.size();
			for(int i=0;i<size;i++){
			   SuperVO  ivo=(SuperVO) listo.get(i);
				if(ivo.getPrimaryKey().equalsIgnoreCase(vo.getPrimaryKey())){
					listo.remove(i);
					break;
				}
			}
			  size=listo.size();
			  for(int i=0;i<size;i++){
				SuperVO ssvo=(SuperVO) listo.get(i);
				if(isEmpty(ssvo.getAttributeValue(minField))|| isEmpty(ssvo.getAttributeValue(maxField))){
					continue;
				}	
				Object ssvoMin=ssvo.getAttributeValue(minField);
				Object ssvoMax=ssvo.getAttributeValue(maxField);
				if(compareInto(voMax, ssvoMin)==-1 || compareInto(voMin, ssvoMax)==1){
					continue;
				}else{
					throw new BusinessException(errorMessage);
				} 
			  }		   			
		}	
	}
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *        组合字段的区间交叉的唯一性校验
	 *       即校验某个维度下，在最小值 和 最大值 之间只存在 一条记录
	 *       用于单据的新增保存 和 修改保存时唯一校验字段(checkFields)已经被修改 的时候
	 * @时间：2011-7-6上午10:58:47
	 * @param vo 要保存的vo
	 * @param checkFields 校验的字段
	 * @param minField  区间最小值
	 * @param maxField  区间最大值
	 * @param errorMessage 错误提示信息
	 * @throws Exception 
	 */
	private static void queryByCheckFieldsInMent(SuperVO vo,String[] checkFields, String minField, String maxField,
			String errorMessage) throws Exception {
		  List list=queryByCheckFields(vo,checkFields);
		  if(isEmpty(list)){
			return;  
		  }
		  int size=list.size();
		  for(int i=0;i<size;i++){
			SuperVO svo=(SuperVO) list.get(i);
			if(isEmpty(svo.getAttributeValue(minField))|| isEmpty(svo.getAttributeValue(maxField))){
				continue;
			}
			Object voMin=vo.getAttributeValue(minField);
			Object voMax=vo.getAttributeValue(maxField);
			Object svoMin=svo.getAttributeValue(minField);
			Object svoMax=svo.getAttributeValue(maxField);
			if(compareInto(voMax, svoMin)==-1 || compareInto(voMin, svoMax)==1){
				continue;
			}else{
				throw new BusinessException(errorMessage);
			} 
		  }		   	
	}
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *        比较两个对象之间的大小
	 *        如果 o1>o2 返回 1
	 *        如果 o1=o2 返回 0
	 *        如果 o1<o2 返回 -1
	 *        如果 非法数据不能比较返回 -2 
	 * @时间：2011-7-6上午10:21:21
	 * @param o1
	 * @param o2
	 */
	 private static int compareInto(Object o1,Object o2) {
	   if(isEmpty(o1)|| isEmpty(o2)){
		   return -2;
	   }
	   if(o1 instanceof Integer && o2 instanceof Integer){
		  Integer i1=(Integer) o1;
		  Integer i2=(Integer) o2;
		  return  i1.compareTo(i2);	  
	   }
	   if(o1 instanceof UFDouble && o2 instanceof UFDouble){
		      UFDouble i1=(UFDouble) o1;
		      UFDouble i2=(UFDouble) o2;
			  return  i1.compareTo(i2);	  
	   }
	   if(o1 instanceof String && o2 instanceof String){
		      String i1=(String) o1;
		      String i2=(String) o2;
			  return  i1.compareTo(i2);	  
	   }
	   if(o1 instanceof UFDate && o2 instanceof UFDate){
		     UFDate i1=(UFDate) o1;
		     UFDate i2=(UFDate) o2;
		     if(i1.before(i2)){
		    	 return -1;
		     }else if(i1.after(i2)){
		    	 return 1;
		     }else{
		    	 return 0;
		     } 
	   }	 
	    return -2;
	 }
	 /**
	  * 
	  * @作者：mlr
	  * @说明：完达山物流项目 
	  *       校验某个字段的唯一性
	  * @时间：2011-7-6下午12:38:18
	  * @param vo
	  * @param checkField
	  * @param errorMessage
	  * @throws Exception
	  */
	private static void queryByCheckField(SuperVO vo,String checkField,String errorMessage) throws Exception{
		 String sign=" = ";
			if(isEmpty(vo.getAttributeValue(checkField))){				
				if(!(vo.getAttributeValue(checkField) instanceof String)&&!"".equalsIgnoreCase((String) vo.getAttributeValue(checkField))){
					sign=" is ";
				}				
			}	
		    String value="";
		    if(isChar(value)&& sign.equalsIgnoreCase(" = ")){
		    	value="'"+vo.getAttributeValue(checkField)+"'";
		    }else{
		    	value=vo.getAttributeValue(checkField)+"";
		    }
			
			String condition = checkField +sign + value+ " and  isNull("+vo.getEntityName()+".dr,0)=0";
			List list = (List) getDao().retrieveByClause(vo.getClass(),
					condition);
			if (list == null || list.size() == 0) {
				return;				
			}else{
				throw new BusinessException(errorMessage);
			}			
     }
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *       根据主键查询一个vo
	 * @时间：2011-7-6下午12:40:17
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	 private static List queryByPrimaryKey(SuperVO vo) throws Exception{
		 
			String condition = vo.getPKFieldName() +" ='" + vo.getPrimaryKey()
			+ "' and  isNull("+vo.getEntityName()+".dr,0)=0";
			List list = (List) getDao().retrieveByClause(vo.getClass(),condition);
			if (list == null || list.size()<=0) {
				throw new BusinessException("该记录已经被删除，请重新刷新界面");
			}
			return list;
     }
	/**
	 *  
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *       校验几个字段组合的唯一性
	 * @时间：2011-7-6下午12:41:00
	 * @param vos 校验的vo数组
	 * @param checkFields 校验的字段数组
	 * @param errorMessage 错误提示信息
	 * @throws Exception
	 */	 
	public static void FieldUniqueCheck(SuperVO[] vos, String[] checkFields,String errorMessage) throws Exception {
	   if(isEmpty(vos)){
		   throw new BusinessException("传入的VO数组不能为空");
	   }
	   for(int i=0;i<vos.length;i++){
		   FieldUniqueCheck(vos[i], checkFields, errorMessage);  
	   }
	}
   /**
    * 
    * @作者：mlr
    * @说明：完达山物流项目 
    *       校验组合字段的唯一性
    * @时间：2011-7-5下午08:49:35
    * @param vo 校验vo
    * @param checkFields 校验字段数组的名字
    * @param errorMessage 返回错误提示信息
    * @throws Exception
    */
	public static void FieldUniqueCheck(SuperVO vo, String[] checkFields,String errorMessage) throws Exception {
		if(isEmpty(vo)){
			return;
		}
		if(isEmpty(checkFields)){
			throw new BusinessException("检验唯一性的的字段名字不能为空");
		}
		if(isEmpty(errorMessage)){
			throw new BusinessException("错误提示信息不能为空");
		}				
		// 判断是修改后的保存还是新增后的保存
		if (isEmpty(vo.getPrimaryKey())) {
			queryByCheckFields(vo, checkFields,errorMessage);
		} else {
            List list=queryByPrimaryKey(vo);
			// 判断修改后的记录，是否改变了值（即拿数据库中的记录和ui中的当前记录进行比较）
			SuperVO vo1 = (SuperVO) list.get(0);
			//判断是否修改了要校验字段的值
			boolean ismodrec = false;			
			//校验是否修改了要校验的字段的值
			for (int i = 0; i < checkFields.length; i++) {				
				if(isEmpty(vo.getAttributeValue(checkFields[i]))&& isEmpty(vo.getAttributeValue(checkFields[i]))){
					continue;
				}
				if(isEmpty(vo.getAttributeValue(checkFields[i]))|| isEmpty(vo.getAttributeValue(checkFields[i]))){
					ismodrec=true;
					break;
				}
				if (!vo.getAttributeValue(checkFields[i]).equals(vo1.getAttributeValue(checkFields[i]))) {
					ismodrec = true;
					break;
				}
			}	
			if (ismodrec) {
				queryByCheckFields(vo,checkFields,errorMessage);
			}
		}
	}
	/**
	    * 
	    * @作者：mlr
	    * @说明：完达山物流项目 
	    *       判断一个vo中的字段 在数据库中  值有没有变
	    * @时间：2011-7-5下午08:49:35
	    * @param vo 校验vo
	    * @param checkFields 校验字段数组的名字
	    * @param errorMessage 返回错误提示信息
	    * @param conditon  过滤条件
	    * @throws Exception
	    */
		public static boolean valueFields(SuperVO vo, String[] checkFields,String conditon) throws Exception {
			if(isEmpty(vo)){
				return false;
			}
			if(isEmpty(checkFields)){
				throw new BusinessException("检验唯一性的的字段名字不能为空");
			}				
			// 判断是修改后的保存还是新增后的保存
			if (isEmpty(vo.getPrimaryKey())) {
				//queryByCheckFields(vo, checkFields,conditon,null);
			} else {
	            List list=queryByPrimaryKey(vo);
				// 判断修改后的记录，是否改变了值（即拿数据库中的记录和ui中的当前记录进行比较）
				SuperVO vo1 = (SuperVO) list.get(0);
				//判断是否修改了要校验字段的值
				boolean ismodrec = false;			
				//校验是否修改了要校验的字段的值
				for (int i = 0; i < checkFields.length; i++) {				
					if(isEmpty(vo.getAttributeValue(checkFields[i]))&& isEmpty(vo.getAttributeValue(checkFields[i]))){
						continue;
					}
					if(isEmpty(vo.getAttributeValue(checkFields[i]))|| isEmpty(vo.getAttributeValue(checkFields[i]))){
						ismodrec=true;
						break;
					}
					if (!vo.getAttributeValue(checkFields[i]).equals(vo1.getAttributeValue(checkFields[i]))) {
						ismodrec = true;
						break;
					}
				}	
			   return ismodrec;
			}
			return false;
		}
	/**
	    * 
	    * @作者：mlr
	    * @说明：完达山物流项目 
	    *       在某个提条件下
	    *       校验组合字段的唯一性
	    *       条件即：conditon
	    * @时间：2011-7-5下午08:49:35
	    * @param vo 校验vo
	    * @param checkFields 校验字段数组的名字
	    * @param errorMessage 返回错误提示信息
	    * @param conditon  过滤条件
	    * @throws Exception
	    */
		public static void FieldUniqueChecks(SuperVO vo, String[] checkFields,String conditon,String errorMessage) throws Exception {
			if(isEmpty(vo)){
				return;
			}
			if(isEmpty(checkFields)){
				throw new BusinessException("检验唯一性的的字段名字不能为空");
			}
			if(isEmpty(errorMessage)){
				throw new BusinessException("错误提示信息不能为空");
			}				
			// 判断是修改后的保存还是新增后的保存
			if (isEmpty(vo.getPrimaryKey())) {
				queryByCheckFields(vo, checkFields,conditon,errorMessage);
			} else {
	            List list=queryByPrimaryKey(vo);
				// 判断修改后的记录，是否改变了值（即拿数据库中的记录和ui中的当前记录进行比较）
				SuperVO vo1 = (SuperVO) list.get(0);
				//判断是否修改了要校验字段的值
				boolean ismodrec = false;			
				//校验是否修改了要校验的字段的值
				for (int i = 0; i < checkFields.length; i++) {				
					if(isEmpty(vo.getAttributeValue(checkFields[i]))&& isEmpty(vo.getAttributeValue(checkFields[i]))){
						continue;
					}
					if(isEmpty(vo.getAttributeValue(checkFields[i]))|| isEmpty(vo.getAttributeValue(checkFields[i]))){
						ismodrec=true;
						break;
					}
					if (!vo.getAttributeValue(checkFields[i]).equals(vo1.getAttributeValue(checkFields[i]))) {
						ismodrec = true;
						break;
					}
				}	
				if (ismodrec) {
					queryByCheckFields(vo, checkFields,conditon,errorMessage);
				}
			}
		}
		/**
		    * 
		    * @作者：mlr
		    * @说明：完达山物流项目 
		    *       在某个提条件下
		    *       校验组合字段的唯一性
		    *       条件即：conditon
		    * @时间：2011-7-5下午08:49:35
		    * @param vo 校验vo
		    * @param checkFields 校验字段数组的名字
		    * @param errorMessage 返回错误提示信息
		    * @param conditon  过滤条件
		    * @throws Exception
		    */
	   private static void queryByCheckFields(SuperVO vo, String[] checkFields,
			String conditon, String errorMessage)throws Exception {
		   StringBuffer cond = new StringBuffer();
			for (int i = 0; i < checkFields.length; i++) {
				String sign=" = ";
				if(isEmpty(vo.getAttributeValue(checkFields[i]))){				
					if(!(vo.getAttributeValue(checkFields[i]) instanceof String)&&!"".equalsIgnoreCase((String) vo.getAttributeValue(checkFields[i]))){
						sign=" is ";
					}			
				}					
				if(isChar(vo.getAttributeValue(checkFields[i]))&& sign.equalsIgnoreCase(" = ")){
					cond.append(" " + checkFields[i] +sign+"'"+ vo.getAttributeValue(checkFields[i])+"'"+ " and");
					}else{
					cond.append(" " + checkFields[i] +sign+ vo.getAttributeValue(checkFields[i])+" and");
					}
			}
			int length=cond.toString().length();
			String condition = cond.toString().substring(0,length-4) + " and  isNull("+ vo.getEntityName() + ".dr,0)=0 " +conditon;
			List list = (List) getDao().retrieveByClause(vo.getClass(),condition);
			if (list == null || list.size() == 0) {
				return;
			} else {
				throw new BusinessException(errorMessage);
			}
		
	}
	/**
	    * 
	    * @作者：mlr
	    * @说明：完达山物流项目 
	    *       校验组合字段的唯一性
	    * @时间：2011-7-5下午08:49:35
	    * @param vo 校验vo
	    * @param checkFields 校验字段数组的名字
	    * @param errorMessage 返回错误提示信息
	    * @throws Exception
	    */
   private static void queryByCheckFields(SuperVO vo,String[] checkFields,String errorMessage) throws Exception{
			StringBuffer cond = new StringBuffer();
			for (int i = 0; i < checkFields.length; i++) {
				String sign=" = ";
				if(isEmpty(vo.getAttributeValue(checkFields[i]))){				
					if(!(vo.getAttributeValue(checkFields[i]) instanceof String)&&!"".equalsIgnoreCase((String) vo.getAttributeValue(checkFields[i]))){
						sign=" is ";
					}			
				}					
				if(isChar(vo.getAttributeValue(checkFields[i]))&& sign.equalsIgnoreCase(" = ")){
					cond.append(" " + checkFields[i] +sign+"'"+ vo.getAttributeValue(checkFields[i])+"'"+ " and");
					}else{
					cond.append(" " + checkFields[i] +sign+ vo.getAttributeValue(checkFields[i])+" and");
					}
			}
			int length=cond.toString().length();
			String condition = cond.toString().substring(0,length-4) + " and  isNull("+ vo.getEntityName() + ".dr,0)=0";
			List list = (List) getDao().retrieveByClause(vo.getClass(),condition);
			if (list == null || list.size() == 0) {
				return;
			} else {
				throw new BusinessException(errorMessage);
			}
     }
    /**
    * 
    * @作者：mlr
    * @说明：完达山物流项目 
    *      根据 校验字段 从数据库中查询满足条件的 vo集合
    * @时间：2011-7-6下午12:43:34
    * @param vo 要查询的vo
    * @param checkFields 校验的字段数组
    * @return
    * @throws Exception
    */
     private static List queryByCheckFields(SuperVO vo,String[] checkFields) throws Exception{
		StringBuffer cond = new StringBuffer();
		for (int i = 0; i < checkFields.length; i++) {
			String sign=" = ";
			if(isEmpty(vo.getAttributeValue(checkFields[i]))){				
				if(!(vo.getAttributeValue(checkFields[i]) instanceof String)&&!"".equalsIgnoreCase((String) vo.getAttributeValue(checkFields[i]))){
					sign=" is ";
				}			
			}					
			if(isChar(vo.getAttributeValue(checkFields[i]))&& sign.equalsIgnoreCase(" = ")){
				cond.append(" " + checkFields[i] +sign+"'"+ vo.getAttributeValue(checkFields[i])+"'"+ " and");
				}else{
				cond.append(" " + checkFields[i] +sign+ vo.getAttributeValue(checkFields[i])+" and");
				}
		}
		int length=cond.toString().length();
		String condition = cond.toString().substring(0,length-4) + " and  isNull("+ vo.getEntityName() + ".dr,0)=0";
		List list = (List) getDao().retrieveByClause(vo.getClass(),condition);
		return list;
     }
   /**
    * 
    * @作者：mlr
    * @说明：完达山物流项目 
    *       判断传入的对象是否是字符串
    * @时间：2011-7-5下午08:51:48
    * @param value 
    * @return
    */
	private static boolean isChar(Object value) {
		if(value==null){
		 return false;
		}
		if(value instanceof String || value instanceof UFDate){
		 return true;	
		}
		return false;
	}
   /**
    * 
    * @作者：mlr
    * @说明：完达山物流项目 
    *        判断传入的对象是否为空(String)
    * @时间：2011-7-5下午08:53:00
    * @param o
    * @return
    */
	private static boolean isnull(Object o) {
		if (o == null || o.toString().trim().equals(""))
			return true;
		return false;
	}
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *       判断传入的对象是否为空
	 * @时间：2011-7-6上午10:31:42
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
