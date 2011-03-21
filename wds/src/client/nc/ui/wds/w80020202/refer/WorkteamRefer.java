package nc.ui.wds.w80020202.refer;

import nc.ui.bd.ref.AbstractRefModel;

public class WorkteamRefer extends AbstractRefModel {
	private int m_DefaultFieldCount= 2;
	private String[] m_aryFieldCode= {  "wt_wtcode","wt_wtname" };
	private String[] m_aryFieldName= { "班组编码","班组名称"  };
	private String m_sPkFieldCode= "wt_pk";
	private String m_sRefTitle= "班组信息";
	private String m_sTableName= "(select wt_pk,wt_wtcode,wt_wtname from tb_workteam where dr=0)tmp ";
	/**
	 * RouteRefModel 构造子注解。
	 */
	public WorkteamRefer() {
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
	
}
