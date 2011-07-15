package nc.ui.wds.ref;

import nc.ui.bd.ref.AbstractRefTreeModel;

public class WdsInvClRefTreeModel extends AbstractRefTreeModel {
	private String m_sRefTitle = "�������";
	private String tablename = "wds_invcl";
	private String[] fieldcode = {"vinvclcode","vinvclname"};
	private String[] fieldname = {"�������","��������"};
	private String[] hidecode = {"pk_invcl","pk_father"};
	private String pkFieldCode = "pk_invcl";
	private String pkFatherfield = "pk_father";
	private String sqlWherePart = " isnull(dr,0)=0";

	/**
	 * RouteRefModel ������ע�⡣
	 */
	public WdsInvClRefTreeModel() {
		super();
	}

	public String getFatherField() {
		return this.pkFatherfield;
	}

	public String getChildField() {
		return this.pkFieldCode;
	}

	/**
	 * ��ʾ�ֶ��б� �������ڣ�(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String[] getFieldCode() {
		return fieldcode ;
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
}
