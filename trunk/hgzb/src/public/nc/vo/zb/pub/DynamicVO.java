package nc.vo.zb.pub;


import nc.vo.pub.*;
import java.util.Hashtable;

/**
 * 类型说明: 单据模板数据精度设置参数类
 * 创建日期：(2003-03-03 16:57:40)
 * @author：方益
 */
public class DynamicVO extends CircularlyAccessibleValueObject {
	Hashtable hash = new Hashtable();
/**
 * DynamicVO 构造子注解。
 */
public DynamicVO() {
	super();
}
/**
 * 此处插入方法说明。
 * 创建日期：(01-3-20 17:26:03)
 * @return java.lang.String[]
 */
public java.lang.String[] getAttributeNames() {
	java.util.Set s = hash.keySet();

	if(s == null)
		return null;
		
	Object[] obs=(Object[])s.toArray();
	
	if(obs == null || obs.length == 0)
		return null;
		
	String[] attrs = new String[obs.length];
	for(int i=0;i<obs.length;i++)
		attrs[i] = (String)obs[i];
	return attrs;
}
/**
 * 此处插入方法说明。
 * 创建日期：(01-3-20 17:24:29)
 * @param key java.lang.String
 */
public Object getAttributeValue(String attributeName) {
	if(attributeName == null)
		return null;
	return hash.get(attributeName);
}
/**
 * 返回数值对象的显示名称。
 * 
 * 创建日期：(2001-2-15 14:18:08)
 * @return java.lang.String 返回数值对象的显示名称。
 */
public String getEntityName() {
	return "Dynamic";
}
/**
 * 此处插入方法说明。
 * 创建日期：(01-3-20 17:24:29)
 * @param key java.lang.String
 * 2003-11-21	王印芬	修改原来value为NULL时不进行设置，改为设置一个空串，
 *						以避免合并显示时某合并字段为空时报错的问题。
 *						另，如果设置的字段类型不为String，可能会有错，请再修改
 */
public void setAttributeValue(String name, Object value) {
	if(name == null) 
		return;
	Object ob = hash.get(name);
	if(ob != null)
		hash.remove(name);
	if (value==null) {
		hash.put(name,"");
	}else{
		hash.put(name,value);
	}
}
/**
 * 验证对象各属性之间的数据逻辑正确性。
 * 
 * 创建日期：(2001-2-15 11:47:35)
 * @exception nc.vo.pub.ValidationException 如果验证失败，抛出
 *     ValidationException，对错误进行解释。
 */
public void validate() throws ValidationException {}
}