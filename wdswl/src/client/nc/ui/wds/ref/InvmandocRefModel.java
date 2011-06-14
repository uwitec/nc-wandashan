package nc.ui.wds.ref;

import nc.ui.bd.ref.AbstractRefModel;

public class InvmandocRefModel extends AbstractRefModel {

	private String m_sRefTitle = "���ô������";

	private String tablename = " wds_invbasdoc join bd_invbasdoc on " +
			"wds_invbasdoc.pk_invbasdoc=bd_invbasdoc.pk_invbasdoc";

	private String[] fieldcode = { "invcode", "invname", "invspec", "invtype",
			"tray_volume", "tray_volume_layers", "so_ywaring_days",
			"so_waring_days", "db_waring_days1", "db_waring_dyas2",
			"wds_invbasdoc.pk_invbasdoc", "wds_invbasdoc.pk_invmandoc",
			"wds_invbasdoc.pk_wds_invbasdoc" };

	private String[] fieldname = { "�������", "�������", "���", "�ͺ�", "��������", "�������",
			"����Ԥ������", "���۾�������", "������������1", "������������2" };

	private String[] hidecode = { "wds_invbasdoc.pk_invbasdoc",
			"wds_invbasdoc.pk_invmandoc", "wds_invbasdoc.pk_wds_invbasdoc" };

	private int defaultFieldCount = 10;

	/**
	 * RouteRefModel ������ע�⡣
	 */
	public InvmandocRefModel() {
		super();
	}

	/**
	 * getDefaultFieldCount ����ע�⡣
	 */
	public int getDefaultFieldCount() {
		return defaultFieldCount;
	}

	/**
	 * ��ʾ�ֶ��б� �������ڣ�(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String[] getFieldCode() {
		return fieldcode;
	}

	/**
	 * ��ʾ�ֶ������� �������ڣ�(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String[] getFieldName() {
		return fieldname;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-9-6 10:56:48)
	 */
	public String[] getHiddenFieldCode() {
		return hidecode;
	}

	/**
	 * �����ֶ���
	 * 
	 * @return java.lang.String
	 */
	public String getPkFieldCode() {
		return "wds_invbasdoc.pk_invmandoc";
	}

	/**
	 * ���ձ��� �������ڣ�(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 */
	public String getRefTitle() {
		return m_sRefTitle;
	}

	@Override
	public String getWherePart() {
		StringBuffer strWhere = new StringBuffer();
		strWhere.append(" isnull(wds_invbasdoc.dr,0)=0 ");

		return strWhere.toString();
	}

	/**
	 * �������ݿ�������ͼ�� �������ڣ�(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 */
	public String getTableName() {

		return tablename;
	}

	@Override
	public boolean isCacheEnabled() {

		return false;
	}

}
