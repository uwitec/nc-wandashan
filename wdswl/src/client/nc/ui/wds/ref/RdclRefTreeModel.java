package nc.ui.wds.ref;

import nc.ui.bd.ref.AbstractRefTreeModel;

public class RdclRefTreeModel extends AbstractRefTreeModel {
	private String m_sRefTitle = "收发类别";
	private String tablename = "wds_rdcl";
	private String[] fieldcode = {"rdcode","rdname"};
	private String[] fieldname = {"编码","名称"};
	private String[] hidecode = {"pk_rdcl","pk_frdcl"};
	private String pkFieldCode = "pk_rdcl";
	private String pkFatherfield = "pk_frdcl";
	private String sqlWherePart = " isnull(dr,0)=0 and pk_corp = '"+getPk_corp()+"' ";

	/**
	 * RouteRefModel 构造子注解。
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
	 * 显示字段列表 创建日期：(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String[] getFieldCode() {
		return fieldcode ;
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
}
