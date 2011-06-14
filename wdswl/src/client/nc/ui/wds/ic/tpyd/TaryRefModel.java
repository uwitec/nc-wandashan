package nc.ui.wds.ic.tpyd;

import nc.ui.bd.ref.AbstractRefModel;
/**
 * 
 * @author mlr
 *托盘档案
 */
public class TaryRefModel  extends AbstractRefModel{
	  private String m_sRefTitle = "移入托盘档案";
	    /**
	     * RouteRefModel 构造子注解。
	     * @return 
	     */
	    public TaryRefModel() {
	    	super();
	    }

	    /**
	     * getDefaultFieldCount 方法注解。
	     */
	    public int getDefaultFieldCount() {
		return 3;
	    }

	    /**
	     * 显示字段列表 创建日期：(01-4-4 0:57:23)
	     * 
	     * @return java.lang.String
	     */
	    public java.lang.String[] getFieldCode() {
	    	return new String[] {
				"bd_cargdoc_tray.cdt_traycode",
				"bd_invbasdoc.invcode",
				"bd_invbasdoc.invname",
				"bd_cargdoc_tray.cdt_pk",
	    	};
	    }

	    /**
	     * 显示字段中文名 创建日期：(01-4-4 0:57:23)
	     * 
	     * @return java.lang.String
	     */
	    public java.lang.String[] getFieldName() {
	    	return new String[] {
				"托盘编码",
				"存货编码",
				"存货名称",
	    	};
	    }

	    /**
	     * 此处插入方法说明。 创建日期：(2001-9-6 10:56:48)
	     */
	    public String[] getHiddenFieldCode() {
	    	return new String[] { 
//	    			"tb_warehousestock.pk_invmandoc",
//	    			"tb_warehousestock.pk_invbasdoc",
	    			"bd_cargdoc_tray.cdt_pk"};
	    }

	    /**
	     * 主键字段名
	     * 
	     * @return java.lang.String
	     */
	    public String getPkFieldCode() {
		return "bd_cargdoc_tray.cdt_pk";
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
	    	strWhere.append(" isnull(bd_cargdoc_tray.dr,0)=0 ");
	    
	    	return strWhere.toString();
	    }
	    /**
	     * 参照数据库表或者视图名 创建日期：(01-4-4 0:57:23)
	     * 
	     * @return java.lang.String
	     */
	    public String getTableName() {
	    	StringBuffer m_sTableName = new StringBuffer();
	       	m_sTableName.append(" wds_cargdoc ");
	       	m_sTableName.append(" join bd_cargdoc_tray on wds_cargdoc.pk_wds_cargdoc=bd_cargdoc_tray.pk_wds_cargdoc");
	       	m_sTableName.append(" join bd_invmandoc on bd_invmandoc.pk_invmandoc = bd_cargdoc_tray.cdt_invmandoc");
	       	m_sTableName.append(" join bd_invbasdoc on bd_invbasdoc.pk_invbasdoc=bd_cargdoc_tray.cdt_invbasdoc");
	       	
	       	
	       	
	       	
		return m_sTableName.toString();
	    }
	    @Override
	    public boolean isCacheEnabled() {
	    
	    	return false;
	    }
}
