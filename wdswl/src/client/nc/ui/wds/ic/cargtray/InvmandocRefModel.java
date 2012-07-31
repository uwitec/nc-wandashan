package nc.ui.wds.ic.cargtray;

import nc.ui.bd.ref.AbstractRefModel;
/**
 * 完达山存货档案参照 
 * @author mlr
 */
public class InvmandocRefModel extends AbstractRefModel{
	
	 private String m_sRefTitle = "当前货位下存货";
	 

	 private String tablename1="wds_invbasdoc join " +
		" tb_spacegoods on wds_invbasdoc.pk_invmandoc=tb_spacegoods.pk_invmandoc   " +
		" join bd_invbasdoc on wds_invbasdoc.pk_invbasdoc= bd_invbasdoc.pk_invbasdoc "+
	    " join wds_cargdoc1 on tb_spacegoods.pk_wds_cargdoc=wds_cargdoc1.pk_wds_cargdoc ";
	
	 private String[] fieldcode={"invcode","invname","invspec","invtype"};
	 
	 
	 private String[] fieldname={"存货编码","存货名称","规格","型号"};
	
	 private String[] hidecode={"wds_invbasdoc.pk_invmandoc","wds_invbasdoc.pk_invbasdoc"};
	 
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
		return "wds_invbasdoc.pk_invmandoc";
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
	    	strWhere.append(" isnull(tb_spacegoods.dr,0)=0 and isnull(wds_invbasdoc.dr,0) = 0 " +
	    			        " and isnull(bd_invbasdoc.dr,0) = 0 and isnull(wds_cargdoc1.dr,0) = 0 ")  ;
	        strWhere.append(" and wds_invbasdoc.pk_corp='"+getPk_corp()+"'");
	        strWhere.append(" and wds_cargdoc1.pk_corp='"+getPk_corp()+"'");


	    	return strWhere.toString();
	    }
	    /**
	     * 参照数据库表或者视图名 创建日期：(01-4-4 0:57:23)
	     * 
	     * @return java.lang.String
	     */
	    public String getTableName() {
	    
		return tablename1;
	    }
	    @Override
	    public boolean isCacheEnabled() {
	    	
	    	return false;
	    }
}
