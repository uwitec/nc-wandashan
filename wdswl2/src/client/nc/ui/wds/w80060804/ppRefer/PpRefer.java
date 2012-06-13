package nc.ui.wds.w80060804.ppRefer;

import nc.ui.bd.ref.AbstractRefModel;

public class PpRefer extends AbstractRefModel {
	private int m_DefaultFieldCount= 2;
	private String[] m_aryFieldCode= {  "storcode","storname" };
	private String[] m_aryFieldName= { "分仓编码","分仓名称"  };
	private String m_sPkFieldCode= "pk_stordoc";
	private String m_sRefTitle= "分仓信息";
	private String m_sTableName= "bd_stordoc";
	private String sqlWherePart = " def1 = '1' and isnull(dr,0)=0 and pk_corp = '"+getPk_corp()+"'";
	/**
	 * RouteRefModel 构造子注解。
	 */
	public PpRefer() {
		super();
	}

	/**
	 * getDefaultFieldCount 方法注解。
	 */
	public int getDefaultFieldCount() {
		return m_DefaultFieldCount;
	}
	/**
	 * 显示字段列表
	 * 创建日期：(01-4-4 0:57:23)
	 * @return java.lang.String
	 */
	public java.lang.String[] getFieldCode() {
		return m_aryFieldCode;
	}
	/**
	 * 显示字段中文名
	 * 创建日期：(01-4-4 0:57:23)
	 * @return java.lang.String
	 */
	public java.lang.String[] getFieldName() {
		return m_aryFieldName;
	}
	/**
	 * 此处插入方法说明。
	 * 创建日期：(2001-9-6 10:56:48)
	 */
	public String[] getHiddenFieldCode() {
		return new String[] { m_sPkFieldCode };
	}
	/**
	 * 主键字段名
	 * @return java.lang.String
	 */
	public String getPkFieldCode() {
		return m_sPkFieldCode;
	}
	/**
	 * 参照标题
	 * 创建日期：(01-4-4 0:57:23)
	 * @return java.lang.String
	 */
	public String getRefTitle() {
		return m_sRefTitle;
	}
	/**
	 * 参照数据库表或者视图名
	 * 创建日期：(01-4-4 0:57:23)
	 * @return java.lang.String
	 */
	public String getTableName() {
		return m_sTableName;
	}
	public String getWherePart() {
		return sqlWherePart;
	}
}
