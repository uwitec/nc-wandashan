package nc.ui.wds.ic.tpyd;

import nc.ui.bd.ref.AbstractRefModel;
/**
 * 
 * @author Administrator
 *���̵���
 */
public class CargeTaryRefModel extends AbstractRefModel {
    private String m_sRefTitle = "�������̵���";
    /**
     * RouteRefModel ������ע�⡣
     */
    public CargeTaryRefModel() {
    	super();
    }

    /**
     * getDefaultFieldCount ����ע�⡣
     */
    public int getDefaultFieldCount() {
	return 7;
    }

    /**
     * ��ʾ�ֶ��б� �������ڣ�(01-4-4 0:57:23)
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
			"tb_warehousestock.whs_pk",//zhf add  ���״̬��ID
			"bd_cargdoc_tray.cdt_pk",
    	};
    }

    /**
     * ��ʾ�ֶ������� �������ڣ�(01-4-4 0:57:23)
     * 
     * @return java.lang.String
     */
    public java.lang.String[] getFieldName() {
    	return new String[] {
			"���̱���",
			"�������",
			"�������",
			"���������",
			"��渨����",
			"���κ�",
			"���״̬ID",
			"��Դ���κ�"
    	};
    }

    /**
     * �˴����뷽��˵���� �������ڣ�(2001-9-6 10:56:48)
     */
    public String[] getHiddenFieldCode() {
    	return new String[] { 
//    			"tb_warehousestock.pk_invmandoc",
//    			"tb_warehousestock.pk_invbasdoc",
    			"bd_cargdoc_tray.cdt_pk"};
    }

    /**
     * �����ֶ���
     * 
     * @return java.lang.String
     */
    public String getPkFieldCode() {
	return "bd_cargdoc_tray.cdt_pk";
    }

    /**
     * ���ձ��� �������ڣ�(01-4-4 0:57:23)
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
     * �������ݿ�������ͼ�� �������ڣ�(01-4-4 0:57:23)
     * 
     * @return java.lang.String
     */
    public String getTableName() {
    	StringBuffer m_sTableName = new StringBuffer();
       	m_sTableName.append(" bd_cargdoc_tray ");//���̵���
    	m_sTableName.append(" join tb_warehousestock ");//���״̬
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
