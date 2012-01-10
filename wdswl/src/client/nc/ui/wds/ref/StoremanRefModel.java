package nc.ui.wds.ref;

import nc.ui.bd.ref.AbstractRefModel;

public class StoremanRefModel extends AbstractRefModel {
	private String m_sRefTitle = "保管员档案";

	private String tablename = " tb_stockstaff join bd_stordoc on tb_stockstaff.pk_stordoc=bd_stordoc.pk_stordoc"
			+ "  join  bd_cargdoc on tb_stockstaff.pk_cargdoc=bd_cargdoc.pk_cargdoc"
			+ " join sm_user on tb_stockstaff.cuserid=sm_user.cuserid ";

	private String[] fieldcode = { "user_code","user_name","storname", "csname",
			"tb_stockstaff.st_pk", "tb_stockstaff.pk_stordoc",
			"tb_stockstaff.pk_cargdoc", "tb_stockstaff.cuserid" };

	private String[] fieldname = {  "保管员编号","保管员名称","仓库名称", "货位名称",  };

	private String strWhere = " isnull(bd_cargdoc.dr,0)=0 " +
			"and isnull(sm_user.dr,0)=0 " +
			"and isnull(tb_stockstaff.dr,0)=0 " +
			"and tb_stockstaff.st_type=0 " +
			"and tb_stockstaff.pk_corp = '"+getPk_corp()+"'";
	
	private String[] hidecode = { "tb_stockstaff.st_pk",
			"tb_stockstaff.pk_stordoc", "tb_stockstaff.pk_cargdoc",
			"tb_stockstaff.cuserid" };

	private int defaultFieldCount = 4;

	/**
	 * RouteRefModel 构造子注解。
	 */
	public StoremanRefModel() {
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
//		return "tb_stockstaff.cuserid";
		return "tb_stockstaff.st_pk";//避免 重复  改为  仓库人员绑定表id
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
		return strWhere;
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
