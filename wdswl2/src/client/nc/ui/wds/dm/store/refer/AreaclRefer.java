package nc.ui.wds.dm.store.refer;

import nc.ui.bd.ref.AbstractRefModel;

/**
 * 地区自定义参照
 * @author Administrator
 *
 */
public class AreaclRefer extends AbstractRefModel {
	private int m_DefaultFieldCount= 4;
	private String[] m_aryFieldCode= {  "areaclcode","areaclname"};
	private String[] m_aryFieldName= { "地区编码","地区名称" };
	private String m_sPkFieldCode= "pk_areacl";
	private String m_sRefTitle= "地区信息";
	private String m_sTableName= "(select pk_areacl,areaclcode,areaclname from bd_areacl where pk_fatherarea is null and dr=0  )tmp ";
	/**
	 * RouteRefModel 构造子注解。
	 */
	public AreaclRefer() {
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
