package nc.vo.wl.pub.report;

/**
 * 创建日期：(2004-1-15 14:01:18)
 * @author：刘建波 
 */
public class SumInfoVO {
	private String[] m_strFields = null;
	private int[] m_iTypes = null;
	private String m_strSubTotalFld = null;
	private String m_strTotalFld = null;
/**
 * SumInfoVO 构造子注解。
 */
public SumInfoVO() {
	super();
}
	public String[] getFields() {
		return m_strFields;
	}
	public String getSubtotalFld() {
		return m_strSubTotalFld;
	}
	public String getTotalFld() {
		return m_strTotalFld;
	}
	public int[] getTypes() {
		return m_iTypes;
	}
	public void setFields(String[] strFlds) {
		m_strFields = strFlds;
	}
	public void setSubtotalFld(String strFld) {
		m_strSubTotalFld = strFld;
	}
	public void setTotalFld(String strFld) {
		m_strTotalFld = strFld;
	}
	public void setTypes(int[] iTypes) {
		m_iTypes = iTypes;
	}
}
