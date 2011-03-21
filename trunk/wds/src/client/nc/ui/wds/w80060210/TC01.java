package nc.ui.wds.w80060210;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;
import nc.ui.wds.w8000.CommonUnit;
import nc.vo.pub.BusinessException;

public class TC01 extends AbstractRefModel{
	
	
	
	private int m_DefaultFieldCount= 2;
	private String[] m_aryFieldCode= {  "tc_comcode","tc_comname"};
	private String[] m_aryFieldName= { "运输公司编码","运输公司名称" };
	private String m_sPkFieldCode= "tc_pk";
	private String m_sRefTitle= "运输公司信息";
	private String m_sTableName= "(select tc_pk,tc_comcode,tc_comname from tb_transcompany where  dr=0 and pp_pk='"+TC01.getStPk()+"' )tmp ";
	/**
	 * RouteRefModel 构造子注解。
	 */
	public TC01() {
		super();
	}
	//根据当前登录人获得仓库主键
	public static String getStPk(){
		String s ="";
		try {
			 s= CommonUnit.getStordocName(ClientEnvironment.getInstance().getUser().getPrimaryKey());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
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
