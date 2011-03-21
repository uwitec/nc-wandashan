package nc.ui.wds.w80040604.refer;

import java.util.Hashtable;
import nc.ui.bd.ref.AbstractRefModel;

public class PsnRefer extends AbstractRefModel {

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
	private String[] m_aryFieldCode= {  "psncode","psnname"};
	private String[] m_aryFieldName= { "人员编码","人员名称" };
	private String m_sPkFieldCode= "pk_psndoc";
	private String m_sRefTitle= "人员信息";
	private String m_sTableName= "(select pk_psndoc,psncode,psnname from bd_psndoc where pk_psncl in(select pk_psncl from bd_psncl where psnclasscode like '302%') and dr=0 )tmp ";
	/**
	 * RouteRefModel 构造子注解。
	 */
	public PsnRefer() {
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
