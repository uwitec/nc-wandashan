package nc.ui.wds.w8004061002.refer;

import java.util.Hashtable;
import nc.ui.bd.ref.AbstractRefModel;

public class InvRefer extends AbstractRefModel {

//	public InvRefer() {
//		setDefaultFieldCount(2);
//		setFieldCode(new String[]{  "invcode ","invname" });
//		setFieldName(new String[] { "存货编码","存货名称" });
//		setPkFieldCode("pk_invbasdoc");
//		setRefTitle("存货信息");
//		setTableName("bd_invbasdoc");
//		setWherePart(" invmnecode like '%C%' and dr = 0");
//
//	}
	private int m_DefaultFieldCount= 2;
	private String[] m_aryFieldCode= { "invcode","invname"};
	private String[] m_aryFieldName= { "存货编码","存货名称" };
	private String m_sPkFieldCode= "pk_invbasdoc";
	private String m_sRefTitle= "货品基础信息";
	private String m_sTableName= "(select * from bd_invbasdoc where pk_invcl in"+
 " (select pk_invcl from bd_invcl where invclasscode like '30101%' and dr=0 ) " +
 " and (def14='0' or def14='1' ) and  dr=0)tmp ";
	/**
	 * RouteRefModel 构造子注解。
	 */
	public InvRefer() {
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
