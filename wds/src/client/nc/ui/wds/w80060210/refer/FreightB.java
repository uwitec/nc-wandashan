package nc.ui.wds.w80060210.refer;

import nc.ui.bd.ref.AbstractRefModel;

public class FreightB extends AbstractRefModel {
	private int m_DefaultFieldCount= 6;
	private String[] m_aryFieldCode= {  "fjf","fs_yuanp","packnumbermin","packnumbermax","evennumbermin","evennumbermax"};
	private String[] m_aryFieldName= { "���ӷ�","�۸� Ԫ/ÿ��","������Сֵ","�������ֵ","��������Сֵ","���������ֵ"};
	private String m_sPkFieldCode= "fs_pkb";
	private String m_sRefTitle= "������׼��Ϣ";
	private String m_sTableName= "tb_freightstandrad_b";
	/**
	 * RouteRefModel ������ע�⡣
	 */
	public FreightB() {
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
