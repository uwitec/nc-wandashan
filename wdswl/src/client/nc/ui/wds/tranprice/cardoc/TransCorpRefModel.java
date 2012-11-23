package nc.ui.wds.tranprice.cardoc;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

public class TransCorpRefModel extends AbstractRefModel{
	
     private String m_sRefTitle = "运输公司档案";
	 
	 private String tablename=" wds_tanscorp_h ";
	
	 private String[] fieldcode={"ctranscorpcode","vtranscorpname"};
	 
	 
	 private String[] fieldname={"运输公司编码","运输公司名称"};
	
	 private String[] hidecode={"pk_wds_tanscorp_h"};
	 
	 private String pkFieldCode="pk_wds_tanscorp_h";
	 
	 
	 private String sqlWherePart=" isnull(wds_tanscorp_h.dr,0)=0 and wds_tanscorp_h.pk_corp='"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'";
	 
	 private int defaultFieldCount=2;
	    /**
	     * RouteRefModel 构造子注解。
	     */
	    public TransCorpRefModel() {
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
