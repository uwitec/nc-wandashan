package nc.ui.wds.dm.trans.follow;

import nc.ui.bd.ref.AbstractRefTreeModel;
/**
 * 
 * @author Administrator
 *发运跟踪信息跟踪
 */
public class TransFollowRefTreeModel extends AbstractRefTreeModel {
	
    private String m_sRefTitle = "发运跟踪信息跟踪";
	 
	 private String tablename=" wds_transfollow";
	
	 private String[] fieldcode={"vcode","vname"};	 
	 
	 private String[] fieldname={"编码","名称"};
	
	 private String[] hidecode={"pk_wds_transfollow","pk_parent"};
	 
	 private String pkFieldCode="pk_wds_transfollow";	 
	 
	 private String sqlWherePart=" isnull(wds_transfollow.dr,0)=0";
	 
	 private int defaultFieldCount=2;
	 //--数卡需要增加的
	 private String m_strChildField="pk_wds_transfollow";
	 private String m_strFatherField ="pk_parent";
	 
	 @Override
	public String getFatherField() {
		// TODO Auto-generated method stub
		return m_strFatherField;
	}
	//--数卡需要增加的
	 @Override
	public String getChildField() {
		// TODO Auto-generated method stub
		return m_strChildField;
	}
	    /**
	     * RouteRefModel 构造子注解。
	     */
	    public TransFollowRefTreeModel() {
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
