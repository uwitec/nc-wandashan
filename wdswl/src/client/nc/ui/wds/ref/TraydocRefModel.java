package nc.ui.wds.ref;

import nc.ui.bd.ref.AbstractRefModel;
import nc.vo.scm.pu.PuPubVO;

public class TraydocRefModel extends AbstractRefModel{

	 private String m_sRefTitle = "托盘档案";
	 
	 private String tablename=" bd_cargdoc_tray";
	
	 private String[] fieldcode={"bd_cargdoc_tray.cdt_traycode","bd_cargdoc_tray.cdt_traycode"};
	 
	 
	 private String[] fieldname={"托盘编码","托盘编码"};
	
	 private String[] hidecode={"bd_cargdoc_tray.cdt_pk"};
	 
	 private String pkFieldCode="bd_cargdoc_tray.cdt_pk";
	 
	 
	 private String sqlWherePart=" isnull(dr,0)=0";
	 
	 private String pk_cargdoc = null;//货位ID
	 
	 public void setCargDocID(String cargdoc){
		 pk_cargdoc = cargdoc;
	 }
	 
	 private int defaultFieldCount=1;
	    /**
	     * RouteRefModel 构造子注解。
	     */
	    public TraydocRefModel() {
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
	    	if(PuPubVO.getString_TrimZeroLenAsNull(pk_cargdoc)==null)
	    		return sqlWherePart;
	    	else
	    		return sqlWherePart + " and pk_cargdoc = '"+pk_cargdoc+"'";
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
