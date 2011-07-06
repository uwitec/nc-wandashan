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
	 
	 private String tablename=" bd_cargdoc";
	
	 private String[] fieldcode={"cscode","csname"};
	 
	 
	 private String[] fieldname={"货位编码","货位名称"};
	
	 private String[] hidecode={"pk_cargdoc"};
	 
	 private String pkFieldCode="pk_cargdoc";
	 
	 
//	 private String sqlWherePart=" isnull(dr,0)=0 ";
	 
	 private int defaultFieldCount=1;
	 
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
	    	String sql =  "isnull(dr,0) = 0 ";
	    	if(PuPubVO.getString_TrimZeroLenAsNull(cwarehouseid)!=null)
	    		sql = sql + " and pk_stordoc = '"+cwarehouseid+"'";
	    	
	    	return sql;
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
