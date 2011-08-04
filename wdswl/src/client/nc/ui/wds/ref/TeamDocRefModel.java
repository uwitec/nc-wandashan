package nc.ui.wds.ref;

import nc.ui.bd.ref.AbstractRefModel;

public class TeamDocRefModel extends AbstractRefModel{

	 private String m_sRefTitle = "装卸班组档案";
	 
	 private String tablename=" wds_teamdoc_h ";
	
	 private String[] fieldcode={"teamcode","teamname","teamaddr"};
	 
	 
	 private String[] fieldname={"班组编码","班组名称","班组地址"};
	
	 private String[] hidecode={"pk_wds_teamdoc_h"};
	 
	 private String pkFieldCode="pk_wds_teamdoc_h";
	 
	 
	 private String sqlWherePart=" isnull(dr,0)=0 and pk_corp = '"+getPk_corp()+"'";
	 
	 private int defaultFieldCount=3;
	    /**
	     * RouteRefModel 构造子注解。
	     */
	    public TeamDocRefModel() {
	    	super();
	    }

	    /**
	     * getDefaultFieldCount 方法注解。
	     */
	    public int getDefaultFieldCount() {
		return defaultFieldCount;
	    }

	    /**
	     * 显示字段列表 创建日期：(01-4-4 0:57:23)
	     * 
	     * @return java.lang.String
	     */
	    public java.lang.String[] getFieldCode() {
	    	return fieldcode ;
	    }

	    /**
	     * 显示字段中文名 创建日期：(01-4-4 0:57:23)
	     * 
	     * @return java.lang.String
	     */
	    public java.lang.String[] getFieldName() {
	    	return fieldname;
	    }

	    /**
	     * 此处插入方法说明。 创建日期：(2001-9-6 10:56:48)
	     */
	    public String[] getHiddenFieldCode() {
	    	return hidecode;
	    }

	    /**
	     * 主键字段名
	     * 
	     * @return java.lang.String
	     */
	    public String getPkFieldCode() {
		return pkFieldCode;
	    }

	    /**
	     * 参照标题 创建日期：(01-4-4 0:57:23)
	     * 
	     * @return java.lang.String
	     */
	    public String getRefTitle() {
		return m_sRefTitle;
	    }
	    @Override
	    public String getWherePart() {
	    	
	    	        
	    	return sqlWherePart;
	    }
	    /**
	     * 参照数据库表或者视图名 创建日期：(01-4-4 0:57:23)
	     * 
	     * @return java.lang.String
	     */
	    public String getTableName() {
	    
		return tablename;
	    }
	    @Override
	    public boolean isCacheEnabled() {
	    	
	    	return false;
	    }

}
