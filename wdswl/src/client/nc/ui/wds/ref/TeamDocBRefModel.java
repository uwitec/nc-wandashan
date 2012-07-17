package nc.ui.wds.ref;

import nc.ui.bd.ref.AbstractRefModel;

public class TeamDocBRefModel extends AbstractRefModel {

	private String m_sRefTitle = "װж������Ա����";

	private String tablename = " wds_teamdoc_b b ";

	private String[] fieldcode = { "psncode", "psnname" };

	private String[] fieldname = { "��Ա����", "��Ա����" };

	private String[] hidecode = { "pk_wds_teamdoc_b" };

	private String pkFieldCode = "pk_wds_teamdoc_b";

	private String sqlWherePart = " isnull(b.dr,0)=0 ";

	private int defaultFieldCount = 2;

	private String pk_wds_teamdoc_h = null;

	/**
	 * RouteRefModel ������ע�⡣
	 */
	public TeamDocBRefModel() {
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
		return pkFieldCode;
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
		if (getPk_wds_teamdoc_h() != null && getPk_wds_teamdoc_h().length() > 0) {
			return sqlWherePart
					+ " and b.pk_wds_teamdoc_h = '"
					+ getPk_wds_teamdoc_h()
					+ "' and exists (select h.pk_wds_teamdoc_h from wds_teamdoc_h h where h.pk_wds_teamdoc_h = b.pk_wds_teamdoc_h ) ";
		}

		return sqlWherePart;
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

	public void setPk_wds_teamdoc_h(String pk_wds_teamdoc_h) {
		this.pk_wds_teamdoc_h = pk_wds_teamdoc_h;
	}

	public String getPk_wds_teamdoc_h() {
		return pk_wds_teamdoc_h;
	}

}
