package nc.ui.wds.w80060208.refer;

import java.util.Hashtable;
import nc.ui.bd.ref.AbstractRefGridTreeModel;
import nc.ui.bd.ref.AbstractRefModel;

public class CZ02 extends AbstractRefModel {

	/**
	 * RouteRefModel ������ע�⡣
	 * ˾������
	 */
	private int m_DefaultFieldCount= 2;
	private String[] m_aryFieldCode= {  "cifb_drivername","cifb_drivermobile" };
	private String[] m_aryFieldName= { "˾��","��ϵ�绰" };
	private String m_sPkFieldCode= "cifb_pk";
	private String m_sRefTitle= "˾����Ϣ";
	private String m_sTableName= "tb_carinf_b";
	/**
	 * RouteRefModel ������ע�⡣
	 */
	public CZ02() {
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
