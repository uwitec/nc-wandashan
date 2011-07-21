package nc.vo.zb.pub;


import nc.vo.pub.*;
import java.util.Hashtable;

/**
 * ����˵��: ����ģ�����ݾ������ò�����
 * �������ڣ�(2003-03-03 16:57:40)
 * @author������
 */
public class DynamicVO extends CircularlyAccessibleValueObject {
	Hashtable hash = new Hashtable();
/**
 * DynamicVO ������ע�⡣
 */
public DynamicVO() {
	super();
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(01-3-20 17:26:03)
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
 * �˴����뷽��˵����
 * �������ڣ�(01-3-20 17:24:29)
 * @param key java.lang.String
 */
public Object getAttributeValue(String attributeName) {
	if(attributeName == null)
		return null;
	return hash.get(attributeName);
}
/**
 * ������ֵ�������ʾ���ơ�
 * 
 * �������ڣ�(2001-2-15 14:18:08)
 * @return java.lang.String ������ֵ�������ʾ���ơ�
 */
public String getEntityName() {
	return "Dynamic";
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(01-3-20 17:24:29)
 * @param key java.lang.String
 * 2003-11-21	��ӡ��	�޸�ԭ��valueΪNULLʱ���������ã���Ϊ����һ���մ���
 *						�Ա���ϲ���ʾʱĳ�ϲ��ֶ�Ϊ��ʱ��������⡣
 *						��������õ��ֶ����Ͳ�ΪString�����ܻ��д������޸�
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
 * ��֤���������֮��������߼���ȷ�ԡ�
 * 
 * �������ڣ�(2001-2-15 11:47:35)
 * @exception nc.vo.pub.ValidationException �����֤ʧ�ܣ��׳�
 *     ValidationException���Դ�����н��͡�
 */
public void validate() throws ValidationException {}
}