package nc.ui.wds.ref;

import nc.ui.bd.ref.AbstractRefModel;

public class InvmandocRefModel extends AbstractRefModel {

	private String m_sRefTitle = "常用存货档案";

	private String tablename = " wds_invbasdoc join bd_invbasdoc on " +
			"wds_invbasdoc.pk_invbasdoc=bd_invbasdoc.pk_invbasdoc";

	private String[] fieldcode = { "invcode", "invname", "invspec", "invtype",
			"tray_volume", "tray_volume_layers", "so_ywaring_days",
			"so_waring_days", "db_waring_days1", "db_waring_dyas2",
			"wds_invbasdoc.pk_invbasdoc", "wds_invbasdoc.pk_invmandoc",
			"wds_invbasdoc.pk_wds_invbasdoc" };

	private String[] fieldname = { "存货编码", "存货名称", "规格", "型号", "托盘容量", "存货层数",
			"销售预警天数", "销售警戒天数", "调拨警戒天数1", "调拨警戒天数2" };

	private String[] hidecode = { "wds_invbasdoc.pk_invbasdoc",
			"wds_invbasdoc.pk_invmandoc", "wds_invbasdoc.pk_wds_invbasdoc" };

	private int defaultFieldCount = 10;

	/**
	 * RouteRefModel 构造子注解。
	 */
	public InvmandocRefModel() {
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
		return "wds_invbasdoc.pk_invmandoc";
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
		StringBuffer strWhere = new StringBuffer();
		strWhere.append(" isnull(wds_invbasdoc.dr,0)=0 ");

		return strWhere.toString();
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
