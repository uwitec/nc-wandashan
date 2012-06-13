package nc.ui.wds.ic.pub;

import nc.ui.bd.ref.AbstractRefModel;

/**
 * 入库托盘参照
 */
public class TpRefModel  extends AbstractRefModel {
	
	private int m_DefaultFieldCount = 2;
	
	private String[] m_aryFieldCode = { "cdt_traycode", "cdt_traystatus" };
	
	private String[] m_aryFieldName = { "托盘编码", "托盘状态" };
	
	private String m_sPkFieldCode = "cdt_pk";
	
	private String m_sRefTitle = "托盘参照";
	
	private String m_sTableName = "bd_cargdoc_tray";

	/**
	 * TpRefModel 构造子注解。
	 */
	public TpRefModel() {
		super();
	}

	public int getDefaultFieldCount() {
		return m_DefaultFieldCount;
	}

	public java.lang.String[] getFieldCode() {
		return m_aryFieldCode;
	}

	public java.lang.String[] getFieldName() {
		return m_aryFieldName;
	}

	public String[] getHiddenFieldCode() {
		return new String[] { m_sPkFieldCode };
	}

	public String getPkFieldCode() {
		return m_sPkFieldCode;
	}

	public String getRefTitle() {
		return m_sRefTitle;
	}

	public String getTableName() {
		return m_sTableName;
	}
	@Override
	public boolean isCacheEnabled() {
		return false;
	}
	 @Override
	    public String getWherePart() {
	    	StringBuffer strWhere = new StringBuffer();
	    	strWhere.append(" isnull(bd_cargdoc_tray.dr,0)=0 ");

	    	return strWhere.toString();
	    }
	//@Override
	protected String[][] getFormulas() {
		String[][] str = {{"cdt_traystatus","cdt_traystatus->iif(cdt_traystatus == \"0\" ,\"未占用\",\"占用\")"}};
		return str;
	}
    public java.lang.String getOrderPart(){
    	StringBuffer strWhere = new StringBuffer();
        strWhere.append("  cdt_traycode,substr(cdt_traycode,length(cdt_traycode),1),substr(cdt_traycode,length(cdt_traycode)-1,1) desc");
        return strWhere.toString();
    }
	@Override
	public void setCacheEnabled(boolean cacheEnabled) {
		
		super.setCacheEnabled(false);
	}
}
