package nc.ui.wds.ic.inv;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;
/**
 * 
 * @author Administrator
 *  ���״̬
 */
public class CZ01 extends AbstractRefModel{
	private int m_DefaultFieldCount= 3;
	private String[] m_aryFieldCode= {"ss_isout","ss_state","isok"};
	private String[] m_aryFieldName= {"�Ƿ�ɳ���","״̬","�Ƿ�����"};
	private String m_sPkFieldCode= "ss_pk";
	private String m_sRefTitle= "������״̬";
	private String m_sTableName= "tb_stockstate";
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
	public String getWherePart() {
		return " isnull(dr,0) = 0 and pk_corp='"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'";
	}

}
