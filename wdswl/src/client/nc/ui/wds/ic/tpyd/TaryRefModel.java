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
		   return 6;
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
				"tb_warehousestock.whs_stocktonnage",//主数量
				"tb_warehousestock.whs_stockpieces",//辅数量
				"tb_warehousestock.whs_batchcode",//批次号
				"tb_warehousestock.whs_pk",
				"bd_cargdoc_tray.cdt_pk"
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
				"库存主数量",
				"库存辅数量",
				"批次号",
				"存货状态id",
				"托盘id"
	    	};
	    }

	    /**
	     * 此处插入方法说明。 创建日期：(2001-9-6 10:56:48)
	     */
	    public String[] getHiddenFieldCode() {
	    	return new String[] {
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
	    	strWhere.append(" isnull(wds_cargdoc.dr,0)= 0 ");
	    	strWhere.append(" and isnull(bd_cargdoc_tray.dr,0)=0 ");
	    	strWhere.append(" and isnull(bd_invbasdoc.dr,0)=0 ");
	    	strWhere.append(" and isnull(tb_warehousestock.dr,0)=0 ");
	    	return strWhere.toString();
	    }
	    /**
	     * 参照数据库表或者视图名 创建日期：(01-4-4 0:57:23)
	     * 
	     * @return java.lang.String
	     */
	    public String getTableName() {
	    	StringBuffer m_sTableName = new StringBuffer();
	       	m_sTableName.append(" wds_cargdoc ");//货位托盘绑定主表
	       	m_sTableName.append(" join bd_cargdoc_tray on wds_cargdoc.pk_wds_cargdoc=bd_cargdoc_tray.pk_wds_cargdoc");//货位托盘绑定子表
	       	m_sTableName.append(" join bd_invmandoc on bd_invmandoc.pk_invmandoc = bd_cargdoc_tray.cdt_invmandoc");
	       	m_sTableName.append(" join bd_invbasdoc on bd_invbasdoc.pk_invbasdoc=bd_cargdoc_tray.cdt_invbasdoc");
	       	// liuys add 增加过滤条件,不能大于托盘的最大容量(当前未考虑有容量的托盘,只考虑参照出空托盘)
	       	m_sTableName.append(" left join tb_warehousestock on tb_warehousestock.pplpt_pk = bd_cargdoc_tray.cdt_pk ");//存货状态表
	    	m_sTableName.append(" left join wds_invbasdoc ");//存货档案表
	    	m_sTableName.append(" on  bd_cargdoc_tray.cdt_invmandoc= wds_invbasdoc.pk_invmandoc");
	       	
	       	
		return m_sTableName.toString();
	    }
	    @Override
	    public boolean isCacheEnabled() {
	    
	    	return false;
	    }
}
