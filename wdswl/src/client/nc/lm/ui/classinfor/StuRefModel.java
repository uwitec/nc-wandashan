package nc.lm.ui.classinfor;

import nc.ui.bd.ref.AbstractRefModel;

public class StuRefModel extends AbstractRefModel {

	 private String m_sRefTitle = "学生业务档案";
	 
	 private String tablename="lm_class_b join lm_class_h on lm_class_b.pk_class=lm_class_h.pk_class";
	
	 private String[] fieldcode={"ccstucode","vstuname","pk_class_b"};
	 
	 
	 private String[] fieldname={"学号","学生姓名"};
	
	 private String[] hidecode={"pk_class_b"};
	 
	 private String pkFieldCode="pk_class_b";
	 
	 
	 private String sqlWherePart=" isnull(lm_class_h.dr,0)=0 and isnull(lm_class_b.dr,0)=0 and lm_class_h.pk_corp='"+nc.ui.pub.ClientEnvironment.getInstance()
		.getCorporation().getPk_corp()+"'";
	 
	 private int defaultFieldCount=2;
	    /**
	     * RouteRefModel 构造子注解。
	     */
	    public StuRefModel() {
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
