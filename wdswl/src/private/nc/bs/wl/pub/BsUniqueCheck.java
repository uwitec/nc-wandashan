package nc.bs.wl.pub;
import java.util.Collection;
import java.util.Dictionary;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;

/**
 * 
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
	public static void FieldUniqueCheck(SuperVO[] vos,String checkField,String errorMessage)throws Exception{	
		 if(isEmpty(vos)){
			   throw new BusinessException("传入的VO数组不能为空");
		   }
		for(int i=0;i<vos.length;i++){		
	      	BsUniqueCheck.FieldUniqueCheck(vos[i], checkField, errorMessage);
		}	
	}
	/**
	* 校验数据库中某个字段的唯一性
	*/
	public static void FieldUniqueCheck(SuperVO vo,String checkField,String errorMessage) throws Exception{			
		if(isNULL(vo)){
			throw new BusinessException("要检验的VO不能为空");
		}
		if(isNULL(checkField)){
			throw new BusinessException("检验唯一性的的字段名字不能为口空");
		}
		if(isNULL(errorMessage)){
			throw new BusinessException("错误提示信息不能为口空");
		}	
		// 判断是修改后的保存还是新增后的保存
		if (isNULL(vo.getPrimaryKey())){
			queryByCheckField(vo,checkField,errorMessage);
		}else{		
			List list=queryByPrimaryKey(vo);
			// 判断修改后的记录，是否改变了值（即拿数据库中的记录和ui中的当前记录进行比较）
			SuperVO vo1=(SuperVO) list.get(0);
			if(isNULL(vo.getAttributeValue(checkField)) && isNULL(vo1.getAttributeValue(checkField))){
                 return;			
			}	
			if(!isNULL(vo.getAttributeValue(checkField)) && !isNULL(vo1.getAttributeValue(checkField))){
				if(vo.getAttributeValue(checkField).equals(vo1.getAttributeValue(checkField))){
					return;
				}else{
				  queryByCheckField(vo,checkField,errorMessage);
				}
			}
			if(isNULL(vo.getAttributeValue(checkField)) || isNULL(vo.getAttributeValue(checkField))){			   				
				  queryByCheckField(vo,checkField,errorMessage);
			}
		}			
	}
	 private static void queryByCheckField(SuperVO vo,String checkField,String errorMessage) throws Exception{
		 String sign=" = ";
			if(isNULL(vo.getAttributeValue(checkField))){				
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
			
			String condition = checkField +sign + value+ " and  isnull("+vo.getEntityName()+".dr,0)=0";
			List list = (List) getDao().retrieveByClause(vo.getClass(),
					condition);
			if (list == null || list.size() == 0) {
				return;				
			}else{
				throw new BusinessException(errorMessage);
			}			
     }
	 private static List queryByPrimaryKey(SuperVO vo) throws Exception{
		 
			String condition = vo.getPKFieldName() +" ='" + vo.getPrimaryKey()
			+ "' and  isnull("+vo.getEntityName()+".dr,0)=0";
			List list = (List) getDao().retrieveByClause(vo.getClass(),condition);
			if (list == null || list.size()<=0) {
				throw new BusinessException("该记录已经被删除，请重新刷新界面");
			}
			return list;
     }
	 
	 
	public static void FieldUniqueCheck(SuperVO[] vos, String[] checkFields,String errorMessage) throws Exception {
	   if(isEmpty(vos)){
		   throw new BusinessException("传入的VO数组不能为空");
	   }
	   for(int i=0;i<vos.length;i++){
		   FieldUniqueCheck(vos[i], checkFields, errorMessage);  
	   }
	}
	
	/**
	* 校验数据库中组合字段的唯一性
	*/
	public static void FieldUniqueCheck(SuperVO vo, String[] checkFields,String errorMessage) throws Exception {
		if(isNULL(vo)){
			throw new BusinessException("要检验的VO不能为空");
		}
		if(isEmpty(checkFields)){
			throw new BusinessException("检验唯一性的的字段名字不能为空");
		}
		if(isNULL(errorMessage)){
			throw new BusinessException("错误提示信息不能为空");
		}			
		
		// 判断是修改后的保存还是新增后的保存
		if (isNULL(vo.getPrimaryKey())) {
			queryByCheckFields(vo, checkFields,errorMessage);
		} else {
            List list=queryByPrimaryKey(vo);
			// 判断修改后的记录，是否改变了值（即拿数据库中的记录和ui中的当前记录进行比较）
			SuperVO vo1 = (SuperVO) list.get(0);
			//判断是否修改了要校验字段的值
			boolean ismodrec = false;			
			//校验是否修改了要校验的字段的值
			for (int i = 0; i < checkFields.length; i++) {				
				if(isNULL(vo.getAttributeValue(checkFields[i]))&& isNULL(vo.getAttributeValue(checkFields[i]))){
					continue;
				}
				if(isNULL(vo.getAttributeValue(checkFields[i]))|| isNULL(vo.getAttributeValue(checkFields[i]))){
					ismodrec=true;
					break;
				}
				if (!vo.getAttributeValue(checkFields[i]).equals(vo1.getAttributeValue(checkFields[i]))) {
					ismodrec = true;
					break;
				}
			}
			if (ismodrec) {
				queryByCheckFields(vo, checkFields,errorMessage);
			}
		}

	}
   private static void queryByCheckFields(SuperVO vo,String[] checkFields,String errorMessage) throws Exception{
			StringBuffer cond = new StringBuffer();
			for (int i = 0; i < checkFields.length; i++) {
				String sign=" = ";
				if(isNULL(vo.getAttributeValue(checkFields[i]))){				
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
			String condition = cond.toString().substring(0,length-4) + " and  isnull("+ vo.getEntityName() + ".dr,0)=0";
			List list = (List) getDao().retrieveByClause(vo.getClass(),condition);
			if (list == null || list.size() == 0) {
				return;
			} else {
				throw new BusinessException(errorMessage);
			}
     }
	private static boolean isChar(Object value) {
		if(value==null){
		 return false;
		}
		if(value instanceof String || value instanceof UFDate){
		 return true;	
		}
		return false;
	}
	private static boolean isNULL(Object o) {
		if (o == null || o.toString().trim().equals(""))
			return true;
		return false;
	}
	
	private static boolean isEmpty(Object value)
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
