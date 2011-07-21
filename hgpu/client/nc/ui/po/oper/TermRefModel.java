package nc.ui.po.oper;

import nc.ui.bd.ref.*;
/**
 * �˴���������˵����
 * �������ڣ�(2001-9-25 14:40:30)
 * @author��ct
 */
public class TermRefModel extends AbstractRefModel {
	public String m_Pkcorp = null;
/**
 * TermRefModel ������ע�⡣
 */
public TermRefModel() {
	super();
}
	/**
 * TermRefModel ������ע�⡣
 */
public TermRefModel(String pkCorp) {
	super();
	this.m_Pkcorp=pkCorp;
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2001-9-25 14:41:07)
 * @return int
 */
public int getDefaultFieldCount() {
	return 5;
}
/**
 * ��ʾ�ֶ��б�
 * �������ڣ�(01-4-4 0:57:23)
 * @return java.lang.String
 */
public java.lang.String[] getFieldCode() {
	return new String[] {"t.termsetcode","t.termname","e.termtypecode","e.termtypename","t.termcontent"};
}
/**
 * ��ʾ�ֶ�������
 * �������ڣ�(01-4-4 0:57:23)
 * @return java.lang.String
 */
public java.lang.String[] getFieldName() {
	return new String[] {"�������","��������", "�������ͱ���","������������","��������"};
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2001-10-28 12:48:37)
 * @return java.lang.String[]
 */
public String[] getHiddenFieldCode() {
	return new String[] {"t.pk_ct_termset"};
}
/**
 * �����ֶ���
 * @return java.lang.String
 */
public String getPkFieldCode() {
	return "t.pk_ct_termset";
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2001-10-28 12:40:03)
 * @return java.lang.String
 */
public String getPkValue() {
	String strValue = (String) getValue(getPkFieldCode());
	return strValue;
}
/**
 * ���ձ���
 * �������ڣ�(01-4-4 0:57:23)
 * @return java.lang.String
 */
public String getRefTitle() {
	return nc.ui.ml.NCLangRes.getInstance().getStrByID("scmpub","UPPscmpub-000212")/*@res "��ͬ�����"*/;
}
/**
 * �������ݿ�������ͼ��
 * �������ڣ�(01-4-4 0:57:23)
 * @return java.lang.String
 */
public String getTableName() {
	return "ct_termset t join ct_termtype e on t.pk_ct_termtype = e.pk_ct_termtype ";
}
	public String getWherePart() {
	return "isnull(t.dr,0) = 0 and t.pk_corp='"+m_Pkcorp+"' and isnull(e.dr,0)=0 " ;
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2004-4-1 14:23:02)
 * @author��������
 * @param pk_corp java.lang.String
 */
public void setPK_Corp(String pk_corp) {
	m_Pkcorp = pk_corp;
}
}