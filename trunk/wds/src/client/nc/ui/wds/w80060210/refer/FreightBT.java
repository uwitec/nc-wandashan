package nc.ui.wds.w80060210.refer;

import nc.ui.bd.ref.AbstractRefModel;

public class FreightBT extends AbstractRefModel {
	private int m_DefaultFieldCount= 4;
	private String[] m_aryFieldCode= {  "fjf","price","tnumbermin","tnumbermax"};
	private String[] m_aryFieldName= { "附加费","价格 吨/元公里","吨位最小值","吨位最大值"};
	private String m_sPkFieldCode= "fs_pkbt";
	private String m_sRefTitle= "吨数标准信息";
	private String m_sTableName= "tb_freightstandrad_b_t";
	/**
	 * RouteRefModel 构造子注解。
	 */
	public FreightBT() {
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
