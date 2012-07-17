package nc.ui.wds.ref;

import nc.ui.bd.ref.AbstractRefModel;

public class TeamDocBRefModel extends AbstractRefModel {

	private String m_sRefTitle = "装卸班组人员档案";

	private String tablename = " wds_teamdoc_b b ";

	private String[] fieldcode = { "psncode", "psnname" };

	private String[] fieldname = { "人员编码", "人员名称" };

	private String[] hidecode = { "pk_wds_teamdoc_b" };

	private String pkFieldCode = "pk_wds_teamdoc_b";

	private String sqlWherePart = " isnull(b.dr,0)=0 ";

	private int defaultFieldCount = 2;

	private String pk_wds_teamdoc_h = null;

	/**
	 * RouteRefModel 构造子注解。
	 */
	public TeamDocBRefModel() {
		super();
	}

	/**
	 * getDefaultFieldCount 方法注解。
	 */
	public int getDefaultFieldCount() {
		return defaultFieldCount;
	}

	/**
	 * 显示字段列表 创建日期：(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String[] getFieldCode() {
		return fieldcode;
	}

	/**
	 * 显示字段中文名 创建日期：(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String[] getFieldName() {
		return fieldname;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-6 10:56:48)
	 */
	public String[] getHiddenFieldCode() {
		return hidecode;
	}

	/**
	 * 主键字段名
	 * 
	 * @return java.lang.String
	 */
	public String getPkFieldCode() {
		return pkFieldCode;
	}

	/**
	 * 参照标题 创建日期：(01-4-4 0:57:23)
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
	 * 参照数据库表或者视图名 创建日期：(01-4-4 0:57:23)
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
