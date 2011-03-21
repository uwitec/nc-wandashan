package nc.ui.wds.w8004061004.refer;

import java.util.Hashtable;
import nc.ui.bd.ref.AbstractRefModel;

public class CargRefer extends AbstractRefModel {

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
	private String[] m_aryFieldCode= {  "cscode","csname"};
	private String[] m_aryFieldName= { "��λ����","��λ����" };
	private String m_sPkFieldCode= "pk_cargdoc";
	private String m_sRefTitle= "��λ��Ϣ";
	private String m_sTableName= "(select pk_cargdoc, cscode,csname from bd_cargdoc where pk_stordoc='1021A91000000004YZ0P' and  dr=0 )tmp ";
	/**
	 * RouteRefModel ������ע�⡣
	 */
	public CargRefer() {
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
