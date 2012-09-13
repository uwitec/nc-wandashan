package nc.ui.wds.ic.pub;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;
/**
 * 入库托盘参照
 */
public class TpRefModel  extends AbstractRefModel {
	
	private int m_DefaultFieldCount = 1;
	
	private String[] m_aryFieldCode = { "cdt_traycode" };
	
	private String[] m_aryFieldName = { "货架编码" };
	
	private String m_sPkFieldCode = "cdt_pk";
	
	private String m_sRefTitle = "货架参照";
	
	private String m_sTableName = " bd_cargdoc_tray join wds_cargdoc on  bd_cargdoc_tray.pk_wds_cargdoc=wds_cargdoc.pk_wds_cargdoc ";

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
	    	strWhere.append(" isnull(bd_cargdoc_tray.dr,0)=0 and  isnull(wds_cargdoc.dr,0)=0 and wds_cargdoc.pk_corp='"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'");
	    	return strWhere.toString();
	    }
	 
    public java.lang.String getOrderPart(){
    	StringBuffer strWhere = new StringBuffer();
        strWhere.append("  cdt_traycode desc");
        return strWhere.toString();
    }
	@Override
	public void setCacheEnabled(boolean cacheEnabled) {
		
		super.setCacheEnabled(false);
	}
}
