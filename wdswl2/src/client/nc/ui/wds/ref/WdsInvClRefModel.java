package nc.ui.wds.ref;

import nc.ui.bd.ref.AbstractRefModel;

//public class WdsInvClRefModel extends AbstractRefTreeModel {
//
//	public WdsInvClRefModel(String refNodeName) {
//		setRefNodeName(refNodeName);
//		// TODO 自动生成构造函数存根
//	}
//	public void setRefNodeName(String refNodeName) {
//		m_strRefNodeName = refNodeName;
//		// *根据需求设置相应参数
//		setFieldCode(new String[] { "vinvclcode", "vinvclname",
//				"invclasslev", "pk_invcl" });
//		setFieldName(new String[] {
//				NCLangRes4VoTransl.getNCLangRes().getStrByID("common",
//						"UC000-0001480")/* @res "存货编码" */,
//				NCLangRes4VoTransl.getNCLangRes().getStrByID("common",
//						"UC000-0001453") /* @res "存货名称" */});
//		setPkFieldCode("pk_invcl");
//		setRefCodeField("vinvclcode");
//		setRefNameField("vinvclname");
//		setTableName("wds_invcl");
//		setCodingRule(UFRefManage.getCodeRuleFromPara("BD101"));
//		// sxj 2003-02-20
//		setWherePart(" pk_corp='" + getPk_corp() + "' or pk_corp= '" + "0001"
//				+ "'");
//		resetFieldName();
//	}
//}

public class WdsInvClRefModel extends AbstractRefModel {

 private String m_sRefTitle = "存货分类";
	 
	 private String tablename=" wds_invcl";
	
	 private String[] fieldcode={"vinvclcode","vinvclname","pk_invcl"};
	 
	 
	 private String[] fieldname={"分类编码","分类名称"};
	
	 private String[] hidecode={"pk_invcl"};
	 
	 private String pkFieldCode="pk_invcl";
	 
	 
	 private String sqlWherePart=" isnull(dr,0)=0 and pk_corp = '"+getPk_corp()+"'";
	 
	 private int defaultFieldCount=2;
	    /**
	     * RouteRefModel 构造子注解。
	     */
	    public WdsInvClRefModel() {
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
