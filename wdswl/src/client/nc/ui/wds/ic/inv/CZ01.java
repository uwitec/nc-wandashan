package nc.ui.wds.ic.inv;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;
/**
 * 
 * @author Administrator
 *  存货状态
 */
public class CZ01 extends AbstractRefModel{
	private int m_DefaultFieldCount= 3;
	private String[] m_aryFieldCode= {"ss_isout","ss_state","isok"};
	private String[] m_aryFieldName= {"是否可出库","状态","是否正常"};
	private String m_sPkFieldCode= "ss_pk";
	private String m_sRefTitle= "存货库存状态";
	private String m_sTableName= "tb_stockstate";
	/**
	 * RouteRefModel 构造子注解。
	 */
	public CZ01() {
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
		return " isnull(dr,0) = 0 and pk_corp='"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'";
	}

}
