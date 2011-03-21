package nc.ui.wds.w8004061002.refer;

import java.util.Hashtable;
import nc.ui.bd.ref.AbstractRefModel;

public class InvRefer extends AbstractRefModel {

//	public InvRefer() {
//		setDefaultFieldCount(2);
//		setFieldCode(new String[]{  "invcode ","invname" });
//		setFieldName(new String[] { "�������","�������" });
//		setPkFieldCode("pk_invbasdoc");
//		setRefTitle("�����Ϣ");
//		setTableName("bd_invbasdoc");
//		setWherePart(" invmnecode like '%C%' and dr = 0");
//
//	}
	private int m_DefaultFieldCount= 2;
	private String[] m_aryFieldCode= { "invcode","invname"};
	private String[] m_aryFieldName= { "�������","�������" };
	private String m_sPkFieldCode= "pk_invbasdoc";
	private String m_sRefTitle= "��Ʒ������Ϣ";
	private String m_sTableName= "(select * from bd_invbasdoc where pk_invcl in"+
 " (select pk_invcl from bd_invcl where invclasscode like '30101%' and dr=0 ) " +
 " and (def14='0' or def14='1' ) and  dr=0)tmp ";
	/**
	 * RouteRefModel ������ע�⡣
	 */
	public InvRefer() {
		super();
	}

	/**
	 * getDefaultFieldCount ����ע�⡣
	 */
	public int getDefaultFieldCount() {
		return m_DefaultFieldCount;
	}
	/**
	 * ��ʾ�ֶ��б�
	 * �������ڣ�(01-4-4 0:57:23)
	 * @return java.lang.String
	 */
	public java.lang.String[] getFieldCode() {
		return m_aryFieldCode;
	}
	/**
	 * ��ʾ�ֶ�������
	 * �������ڣ�(01-4-4 0:57:23)
	 * @return java.lang.String
	 */
	public java.lang.String[] getFieldName() {
		return m_aryFieldName;
	}
	/**
	 * �˴����뷽��˵����
	 * �������ڣ�(2001-9-6 10:56:48)
	 */
	public String[] getHiddenFieldCode() {
		return new String[] { m_sPkFieldCode };
	}
	/**
	 * �����ֶ���
	 * @return java.lang.String
	 */
	public String getPkFieldCode() {
		return m_sPkFieldCode;
	}
	/**
	 * ���ձ���
	 * �������ڣ�(01-4-4 0:57:23)
	 * @return java.lang.String
	 */
	public String getRefTitle() {
		return m_sRefTitle;
	}
	/**
	 * �������ݿ�������ͼ��
	 * �������ڣ�(01-4-4 0:57:23)
	 * @return java.lang.String
	 */
	public String getTableName() {
		return m_sTableName;
	}
	
}
