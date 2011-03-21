package nc.ui.wds.w80060202;

import nc.ui.bd.ref.AbstractRefModel;

public class CZ03 extends AbstractRefModel{
	private int m_DefaultFieldCount= 2;
	private String[] m_aryFieldCode= {  "fyd_ddh","d"};
	private String[] m_aryFieldName= { "订单号","订单号" };
	private String m_sPkFieldCode= "f";
	private String m_sRefTitle= "订单信息";
	private String m_sTableName= "(select fyd_ddh f ,fyd_ddh,fyd_ddh d from tb_fydnew where dr=0 and vbillstatus = 0 and fyd_approstate = 1)tmp ";
	/**
	 * RouteRefModel 构造子注解。
	 */
	public CZ03() {
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
