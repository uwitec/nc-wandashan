package nc.ui.wds.w80020202.refer;

import nc.ui.bd.ref.AbstractRefModel;

public class HandlingcolRefer extends AbstractRefModel {

	private int m_DefaultFieldCount = 2;
	private String[] m_aryFieldCode = { "invcode", "invname" };
	private String[] m_aryFieldName = { "��Ʒ����", "��Ʒ����" };
	private String m_sPkFieldCode = "pk_invbasdoc";
	private String m_sRefTitle = "�۸������Ϣ";
	private String m_sTableName = "(select pk_invbasdoc, invcode,invname from bd_invbasdoc "
			+ "where pk_invbasdoc in (select pk_invbasdoc from tb_handlingcol where chargecol is not null and dr=0) and dr=0 )tmp ";

	/**
	 * RouteRefModel ������ע�⡣
	 */
	public HandlingcolRefer() {
		super();
	}

	/**
	 * getDefaultFieldCount ����ע�⡣
	 */
	public int getDefaultFieldCount() {
		return m_DefaultFieldCount;
	}

	/**
	 * ��ʾ�ֶ��б� �������ڣ�(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String[] getFieldCode() {
		return m_aryFieldCode;
	}

	/**
	 * ��ʾ�ֶ������� �������ڣ�(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String[] getFieldName() {
		return m_aryFieldName;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-9-6 10:56:48)
	 */
	public String[] getHiddenFieldCode() {
		return new String[] { m_sPkFieldCode };
	}

	/**
	 * �����ֶ���
	 * 
	 * @return java.lang.String
	 */
	public String getPkFieldCode() {
		return m_sPkFieldCode;
	}

	/**
	 * ���ձ��� �������ڣ�(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 */
	public String getRefTitle() {
		return m_sRefTitle;
	}

	/**
	 * �������ݿ�������ͼ�� �������ڣ�(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 */
	public String getTableName() {
		return m_sTableName;
	}

}
