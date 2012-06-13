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
 * 必须向后台校验
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
 * @作者：mlr
 * @说明：完达山物流项目 
 *        字段不为空的校验 
 * 
 * @时间：2011-7-5下午04:59:49
 * @param vos 要校验的vos
 * @param fields 要校验的字段数组
 * @param fields 要用来错误提示的字段数组
 * @throws BusinessException
 */
public static void FieldNotNull(SuperVO[] vos,String[] fields,String[] names)throws BusinessException{
	if(isEmpty(vos) || isEmpty(fields)|| isEmpty(names)){
		return;
	}
	if(fields.length !=names.length){
		throw new BusinessException("校验字段和显示字段数组长度不一致");
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



















