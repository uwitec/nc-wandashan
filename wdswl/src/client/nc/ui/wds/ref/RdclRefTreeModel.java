package nc.ui.wds.ref;

import nc.ui.bd.ref.AbstractRefTreeModel;

public class RdclRefTreeModel extends AbstractRefTreeModel {
	private String m_sRefTitle = "�շ����";
	private String tablename = "wds_rdcl";
	private String[] fieldcode = {"rdcode","rdname"};
	private String[] fieldname = {"����","����"};
	private String[] hidecode = {"pk_rdcl","pk_frdcl"};
	private String pkFieldCode = "pk_rdcl";
	private String pkFatherfield = "pk_frdcl";
	private String sqlWherePart = " isnull(dr,0)=0 and pk_corp = '"+getPk_corp()+"' ";

	/**
	 * RouteRefModel ������ע�⡣
	 */
	public RdclRefTreeModel() {
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
