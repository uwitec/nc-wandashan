package nc.ui.wds.w80060202;

import nc.ui.bd.ref.AbstractRefModel;

public class CZ01 extends AbstractRefModel {

	private int m_DefaultFieldCount= 2;
	private String[] m_aryFieldCode= {  "tc_comcode","tc_comname"};
	private String[] m_aryFieldName= { "���乫˾����","���乫˾����" };
	private String m_sPkFieldCode= "f";
	private String m_sRefTitle= "���乫˾��Ϣ";
	private String m_sTableName= "(select tc_pk f ,tc_comcode,tc_comname  from tb_transcompany where dr=0 and (tc_archive = 0 or tc_archive is null) )tmp ";
	/**
	 * RouteRefModel ������ע�⡣
	 */
	public CZ01() {
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
	 * �������ݿ��������ͼ��
	 * �������ڣ�(01-4-4 0:57:23)
	 * @return java.lang.String
	 */
	public String getTableName() {
		return m_sTableName;
	}
	
}