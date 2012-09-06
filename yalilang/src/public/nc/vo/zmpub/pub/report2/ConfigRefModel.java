package nc.vo.zmpub.pub.report2;
import nc.ui.bd.ref.AbstractRefModel;
/**
 * 报表配置文件参照
 * @author mlr
 */
public class ConfigRefModel extends AbstractRefModel {
	private int m_DefaultFieldCount= 4;
	private String[] m_aryFieldCode= { "detatil","nodecode", };
	private String[] m_aryFieldName= { "配置文件描述信息","功能节点"  };
	private String m_sPkFieldCode= "pk_config";
	private String m_sRefTitle= "报表配置文件选择参照框";
	private String m_sTableName= "zm_config";
	private String sqlWherePart = "  isnull(dr,0)=0 ";
	/**
	 * RouteRefModel 构造子注解。
	 */
	public ConfigRefModel() {
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
	public boolean isCacheEnabled() {
		return false;
	}
}
