package nc.bs.wl.pub;
import java.util.Collection;
import java.util.Dictionary;

import nc.bs.dao.BaseDAO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.NullFieldException;
import nc.vo.pub.SuperVO;
/**
 * 
 * @author mlr
 * �������̨У��
 */
public class BsNotNullCheck {

	private static BaseDAO dao;

	private static BaseDAO getDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	 }
/**
 * 	
 * @���ߣ�mlr
 * @˵�������ɽ������Ŀ 
 *        �ֶβ�Ϊ�յ�У�� 
 * 
 * @ʱ�䣺2011-7-5����04:59:49
 * @param vos ҪУ���vos
 * @param fields ҪУ����ֶ�����
 * @param fields Ҫ����������ʾ���ֶ�����
 * @throws BusinessException
 */
public static void FieldNotNull(SuperVO[] vos,String[] fields,String[] names)throws BusinessException{
	if(isEmpty(vos) || isEmpty(fields)|| isEmpty(names)){
		return;
	}
	if(fields.length !=names.length){
		throw new BusinessException("У���ֶκ���ʾ�ֶ����鳤�Ȳ�һ��");
	}
	StringBuffer message=null;
	for(int i=0;i<vos.length;i++){
		SuperVO vo=vos[i];
		for(int j=0;j<fields.length;j++){
		Object o=vo.getAttributeValue(fields[j]);
		if(isNULL(o)){
			if (message == null)
				message = new StringBuffer();
			message.append("[");
			message.append(names[i]);
			message.append("]");
			message.append(",");
		  }
		}
	}
	if (message != null) {
		message.deleteCharAt(message.length() - 1);
		throw new NullFieldException(message.toString());
	}	
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



















