package nc.ui.wds.ic.cargtray;

import nc.ui.bd.ref.AbstractRefModel;

public class InvmandocRefModel extends AbstractRefModel{
	
	
	 private String m_sRefTitle = "当前货位下存货";
	 
	 private String tablename="tb_spacegoods join " +
	 		"wds_invbasdoc on tb_spacegoods.pk_invmandoc=wds_invbasdoc.pk_invmandoc  join " +
	 		"bd_invmandoc on tb_spacegoods.pk_invmandoc=bd_invmandoc.pk_invmandoc  join  " +
	 		"bd_invbasdoc on tb_spacegoods.pk_invbasdoc= bd_invbasdoc.pk_invbasdoc ";
	
	 private String[] fieldcode={"invcode","invname","invspec","invtype",
			 "tb_spacegoods.pk_invbasdoc","tb_spacegoods.pk_invmandoc"};
	 
	 
	 private String[] fieldname={"存货编码","存货名称","规格","型号"};
	
	 private String[] hidecode={"tb_spacegoods.pk_invbasdoc","tb_spacegoods.pk_invmandoc"};
	 
	 private int defaultFieldCount=4;
	    /**
	     * RouteRefModel 构造子注解。
	     */
	    public InvmandocRefModel() {
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
		return "tb_spacegoods.pk_invmandoc";
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
	    	StringBuffer strWhere = new StringBuffer();
	    	strWhere.append(" isnull(tb_spacegoods.dr,0)=0 and isnull(wds_invbasdoc.dr,0) = 0 and isnull(bd_invbasdoc.dr,0) = 0" +
	    			" and isnull(wds_invbasdoc.dr,0) = 0 ")   
	    	        .append(" and bd_invmandoc.pk_corp='"+getPk_corp()+"'");
	    	return strWhere.toString();
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
