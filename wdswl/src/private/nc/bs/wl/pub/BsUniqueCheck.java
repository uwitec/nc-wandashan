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
 * ��̨�ֶ�Ψһ��У��

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
			   throw new BusinessException("�����VO���鲻��Ϊ��");
		   }
		for(int i=0;i<vos.length;i++){		
	      	BsUniqueCheck.FieldUniqueCheck(vos[i], checkField, errorMessage);
		}	
	}
	/**
	* У�����ݿ���ĳ���ֶε�Ψһ��
	*/
	public static void FieldUniqueCheck(SuperVO vo,String checkField,String errorMessage) throws Exception{			
		if(isNULL(vo)){
			throw new BusinessException("Ҫ�����VO����Ϊ��");
		}
		if(isNULL(checkField)){
			throw new BusinessException("����Ψһ�Եĵ��ֶ����ֲ���Ϊ�ڿ�");
		}
		if(isNULL(errorMessage)){
			throw new BusinessException("������ʾ��Ϣ����Ϊ�ڿ�");
		}	
		// �ж����޸ĺ�ı��滹��������ı���
		if (isNULL(vo.getPrimaryKey())){
			queryByCheckField(vo,checkField,errorMessage);
		}else{		
			List list=queryByPrimaryKey(vo);
			// �ж��޸ĺ�ļ�¼���Ƿ�ı���ֵ���������ݿ��еļ�¼��ui�еĵ�ǰ��¼���бȽϣ�
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
				throw new BusinessException("�ü�¼�Ѿ���ɾ����������ˢ�½���");
			}
			return list;
     }
	 
	 
	public static void FieldUniqueCheck(SuperVO[] vos, String[] checkFields,String errorMessage) throws Exception {
	   if(isEmpty(vos)){
		   throw new BusinessException("�����VO���鲻��Ϊ��");
	   }
	   for(int i=0;i<vos.length;i++){
		   FieldUniqueCheck(vos[i], checkFields, errorMessage);  
	   }
	}
	
	/**
	* У�����ݿ�������ֶε�Ψһ��
	*/
	public static void FieldUniqueCheck(SuperVO vo, String[] checkFields,String errorMessage) throws Exception {
		if(isNULL(vo)){
			throw new BusinessException("Ҫ�����VO����Ϊ��");
		}
		if(isEmpty(checkFields)){
			throw new BusinessException("����Ψһ�Եĵ��ֶ����ֲ���Ϊ��");
		}
		if(isNULL(errorMessage)){
			throw new BusinessException("������ʾ��Ϣ����Ϊ��");
		}			
		
		// �ж����޸ĺ�ı��滹��������ı���
		if (isNULL(vo.getPrimaryKey())) {
			queryByCheckFields(vo, checkFields,errorMessage);
		} else {
            List list=queryByPrimaryKey(vo);
			// �ж��޸ĺ�ļ�¼���Ƿ�ı���ֵ���������ݿ��еļ�¼��ui�еĵ�ǰ��¼���бȽϣ�
			SuperVO vo1 = (SuperVO) list.get(0);
			//�ж��Ƿ��޸���ҪУ���ֶε�ֵ
			boolean ismodrec = false;			
			//У���Ƿ��޸���ҪУ����ֶε�ֵ
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
