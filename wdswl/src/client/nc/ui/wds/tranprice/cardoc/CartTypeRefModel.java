package nc.ui.wds.tranprice.cardoc;

import nc.ui.bd.ref.AbstractRefModel;

public class CartTypeRefModel extends AbstractRefModel{


	 private String m_sRefTitle = "车型档案";
	 
	 private String tablename=" wds_cartyedoc";
	
	 private String[] fieldcode={"ccartypecode","vcartypename","nloadnum","iloadunit"};
	 
	 
	 private String[] fieldname={"车辆类型编码","车辆类型名称","载重","载重单位"};
	
	 private String[] hidecode={"pk_wds_cartyedoc"};
	 
	 private String pkFieldCode="pk_wds_cartyedoc";
	 
	 
	 private String sqlWherePart=" isnull(dr,0)=0";
	 
	 private int defaultFieldCount=4;
	    /**
	     * 
	     */
	    public CartTypeRefModel() {
	    	super();
	    }

	    /**
	     * 
	     */
	    public int getDefaultFieldCount() {
		return defaultFieldCount;
	    }

	    /**
	     * 
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
