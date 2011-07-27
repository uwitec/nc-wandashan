package nc.ui.wds.ic.tpyd;

import nc.ui.bd.ref.AbstractRefModel;
/**
 * 
 * @author mlr
 *���̵���
 */
public class TaryRefModel  extends AbstractRefModel{
	  private String m_sRefTitle = "�������̵���";
	    /**
	     * RouteRefModel ������ע�⡣
	     * @return 
	     */
	    public TaryRefModel() {
	    	super();
	    }

	    /**
	     * getDefaultFieldCount ����ע�⡣
	     */
	    public int getDefaultFieldCount() {
		return 3;
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
	    	};
	    }

	    /**
	     * �˴����뷽��˵���� �������ڣ�(2001-9-6 10:56:48)
	     */
	    public String[] getHiddenFieldCode() {
	    	return new String[] { 
//	    			"tb_warehousestock.pk_invmandoc",
//	    			"tb_warehousestock.pk_invbasdoc",
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
	    
	    	return strWhere.toString();
	    }
	    /**
	     * �������ݿ��������ͼ�� �������ڣ�(01-4-4 0:57:23)
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