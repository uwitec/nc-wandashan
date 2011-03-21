package nc.ui.wds.w80060401;

import nc.ui.bd.ref.AbstractRefModel;

public class CZ01 extends AbstractRefModel{
	
	private int m_DefaultFieldCount= 3;
	private String[] m_aryFieldCode= { "pname","pcode","pspec" };
	private String[] m_aryFieldName= { "��Ʒ����","��Ʒ����","���"};
	private String m_sPkFieldCode= "pk_invbasdoc";
	private String m_sRefTitle= "��Ʒ��Ϣ";
	private String m_sTableName= "(select pk_invbasdoc , t.invcode pcode,t.invname pname,t.invspec pspec from bd_invbasdoc t where dr = 0 and ( def14 ='0' or def14 = '1') )tmp";
	
	
	  
	/**
	 * RouteRefModel ������ע�⡣
	 */
	
	public CZ01() {
		super();
	}
	/**
	 * getDefaultFieldCount ����ע�⡣
	 */
	public int getDefaultFieldCount() {
		return m_DefaultFieldCount;
	}
	/**
	 * ��ʾ�ֶ��б�
	 * �������ڣ�(01-4-4 0:57:23)
	 * @return java.lang.String
	 */
	public java.lang.String[] getFieldCode() {
		return m_aryFieldCode;
	}
	/**
	 * ��ʾ�ֶ�������
	 * �������ڣ�(01-4-4 0:57:23)
	 * @return java.lang.String
	 */
	public java.lang.String[] getFieldName() {
		return m_aryFieldName;
	}
	/**
	 * �˴����뷽��˵����
	 * �������ڣ�(2001-9-6 10:56:48)
	 */
	public String[] getHiddenFieldCode() {
		return new String[] { m_sPkFieldCode };
	}
	/**
	 * �����ֶ���
	 * @return java.lang.String
	 */
	public String getPkFieldCode() {
		return m_sPkFieldCode;
	}
	/**
	 * ���ձ���
	 * �������ڣ�(01-4-4 0:57:23)
	 * @return java.lang.String
	 */
	public String getRefTitle() {
		return m_sRefTitle;
	}
	/**
	 * �������ݿ�������ͼ��
	 * �������ڣ�(01-4-4 0:57:23)
	 * @return java.lang.String
	 */
	public String getTableName() {
		return m_sTableName;
	}
	

}
