package nc.ui.wds.ic.tpyd;

import nc.ui.bd.ref.AbstractRefModel;
/**
 * 
 * @author Administrator
 *托盘档案
 */
public class CargeTaryRefModel extends AbstractRefModel {
    private String m_sRefTitle = "出库托盘档案";
    /**
     * RouteRefModel 构造子注解。
     */
    public CargeTaryRefModel() {
    	super();
    }

    /**
     * getDefaultFieldCount 方法注解。
     */
    public int getDefaultFieldCount() {
	return 7;
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
			"tb_warehousestock.whs_stocktonnage",
			"tb_warehousestock.whs_stockpieces",
			"tb_warehousestock.whs_batchcode",
			"tb_warehousestock.whs_lbatchcode",
			"tb_warehousestock.pk_invmandoc",
			"tb_warehousestock.pk_invbasdoc",
			"tb_warehousestock.whs_pk",//zhf add  库存状态表ID
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
			"库存主数量",
			"库存辅数量",
			"批次号",
			"库存状态ID",
			"来源批次号"
    	};
    }

    /**
     * 此处插入方法说明。 创建日期：(2001-9-6 10:56:48)
     */
    public String[] getHiddenFieldCode() {
    	return new String[] { 
//    			"tb_warehousestock.pk_invmandoc",
//    			"tb_warehousestock.pk_invbasdoc",
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
    	strWhere.append(" and isnull(tb_warehousestock.dr,0)=0 ");
    	
    	strWhere.append(" and isnull(bd_invmandoc.dr,0)=0 ");
    	strWhere.append(" and isnull(bd_invbasdoc.dr,0)=0 ");
    	
    	strWhere.append(" and tb_warehousestock.pk_corp='"+getPk_corp()+"'");
    	return strWhere.toString();
    }
    /**
     * 参照数据库表或者视图名 创建日期：(01-4-4 0:57:23)
     * 
     * @return java.lang.String
     */
    public String getTableName() {
    	StringBuffer m_sTableName = new StringBuffer();
       	m_sTableName.append(" bd_cargdoc_tray ");//托盘档案
    	m_sTableName.append(" join tb_warehousestock ");//存货状态
    	m_sTableName.append(" on bd_cargdoc_tray.cdt_pk = tb_warehousestock.pplpt_pk");
    	m_sTableName.append(" join bd_invmandoc ");
    	m_sTableName.append(" on tb_warehousestock.pk_invmandoc =bd_invmandoc.pk_invmandoc ");
    	m_sTableName.append(" join bd_invbasdoc ");
    	m_sTableName.append(" on tb_warehousestock.pk_invbasdoc =bd_invbasdoc.pk_invbasdoc ");
	return m_sTableName.toString();
    }
    @Override
    public boolean isCacheEnabled() {
    
    	return false;
    }
    
}
