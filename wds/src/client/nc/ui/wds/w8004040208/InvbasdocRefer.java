package nc.ui.wds.w8004040208;

import nc.ui.bd.ref.AbstractRefModel;

public class InvbasdocRefer extends AbstractRefModel {
	private int m_DefaultFieldCount = 3;
	private String[] m_aryFieldCode = {  "invcode","invname", "invspec" };
	private String[] m_aryFieldName = { "产品编码","产品名称",  "规格" };
	private String m_sPkFieldCode = "pk_invbasdoc";
	private String m_sRefTitle = "产品信息";
	private String m_sTableName = "bd_invbasdoc ";

	/**
	 * RouteRefModel 构造子注解。
	 */
	public InvbasdocRefer() {
		super();
	}

	/**
	 * getDefaultFieldCount 方法注解。
	 */
	public int getDefaultFieldCount() {
		return m_DefaultFieldCount;
	}

	/**
	 * 显示字段列表 创建日期：(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String[] getFieldCode() {
		return m_aryFieldCode;
	}

	/**
	 * 显示字段中文名 创建日期：(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String[] getFieldName() {
		return m_aryFieldName;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-6 10:56:48)
	 */
	public String[] getHiddenFieldCode() {
		return new String[] { m_sPkFieldCode };
	}

	/**
	 * 主键字段名
	 * 
	 * @return java.lang.String
	 */
	public String getPkFieldCode() {
		return m_sPkFieldCode;
	}

	/**
	 * 参照标题 创建日期：(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 */
	public String getRefTitle() {
		return m_sRefTitle;
	}

	/**
	 * 参照数据库表或者视图名 创建日期：(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 */
	public String getTableName() {
		return m_sTableName;
	}

}
