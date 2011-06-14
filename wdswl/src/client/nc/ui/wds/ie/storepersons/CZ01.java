package nc.ui.wds.ie.storepersons;

import nc.ui.bd.ref.AbstractRefModel;

public class CZ01 extends AbstractRefModel {
	
	private int m_DefaultFieldCount = 2;
	
	private String[] m_aryFieldCode = { "cscode", "csname" };
	
	private String[] m_aryFieldName = { "��λ����", "��λ����" };
	
	private String m_sPkFieldCode = "pk_cargdoc";
	
	private String m_sRefTitle = "��λ��Ϣ";
	
	private String m_sTableName = "bd_cargdoc";

	/**
	 * RouteRefModel ������ע�⡣
	 */
	public CZ01() {
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

}
