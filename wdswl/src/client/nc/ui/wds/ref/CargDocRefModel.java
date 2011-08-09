package nc.ui.wds.ref;

import nc.ui.bd.ref.AbstractRefModel;
import nc.vo.scm.pu.PuPubVO;


/**
 * 
 * @author zhf  货位档案参照  过滤 仓库
 *
 */
public class CargDocRefModel extends AbstractRefModel {
	
	 private String m_sRefTitle = "货位档案";
	 
	 private String tablename=" bd_cargdoc join bd_stordoc on bd_cargdoc.pk_stordoc=bd_stordoc.pk_stordoc";
	
	 private String[] fieldcode={"bd_cargdoc.cscode","bd_cargdoc.csname"};
	 
	 
	 private String[] fieldname={"货位编码","货位名称"};
	
	 private String[] hidecode={"bd_cargdoc.pk_cargdoc"};
	 
	 private String pkFieldCode="bd_cargdoc.pk_cargdoc";
	 
	 
	 private String sqlWherePart=" isnull(bd_cargdoc.dr,0)=0 and isnull(bd_stordoc.dr,0)=0 and bd_stordoc.pk_corp ='"+getPk_corp()+"' ";
	 
	 private int defaultFieldCount=3;
	 
	 private String cwarehouseid = null;
	 public void setStordocID(String stordocid){
		 cwarehouseid = stordocid;
	 }
	    /**
	     * RouteRefModel 构造子注解。
	     */
	    public CargDocRefModel() {
	    	super();
	    }
	    
	    public CargDocRefModel(String stordocid) {
	    	super();
	    	this.cwarehouseid = stordocid;
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
	    	if(PuPubVO.getString_TrimZeroLenAsNull(cwarehouseid)!=null)
	    		sqlWherePart = sqlWherePart + " and bd_cargdoc.pk_stordoc = '"+cwarehouseid+"'";
	    	
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
