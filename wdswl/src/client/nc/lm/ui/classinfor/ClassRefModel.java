package nc.lm.ui.classinfor;
import nc.ui.bd.ref.AbstractRefModel;
public class ClassRefModel extends AbstractRefModel{
     /*
      * 标题
      */
	 private String m_sRefTitle = " 学生信息档案";
	 /*
	  * 表名
	  */
	 private String tablename=" lm_class_h ";
	 /*
	  * 参照要用的字段
	  */
	 private String[] fieldcode={"cclasscode","vclassname","pk_class"};
	 /*
	  * 参照显示的中文名称
	  */
	 private String[] fieldname={"班级编号","班级名称"};
	 /*
	 * 要隐藏的字段
	 */
	 private String[] hidecode={"pk_class"};
	 /*
	  * 参照表主键
	  */
	 private String pkFieldCode="pk_class";
	 /*
	  * 参照表过率条件 
	  */
	 private String sqlWherePart=" isnull(lm_class_h.dr,0)=0 and lm_class_h.pk_corp='"+nc.ui.pub.ClientEnvironment.getInstance()
		.getCorporation().getPk_corp()+"'";
	 
	 private int defaultFieldCount=2;
	    /**
	     * RouteRefModel 构造子注解。
	     */
	    public ClassRefModel() {
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
