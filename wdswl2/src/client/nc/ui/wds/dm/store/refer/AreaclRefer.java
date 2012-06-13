package nc.ui.wds.dm.store.refer;

import nc.ui.bd.ref.AbstractRefModel;

/**
 * �����Զ������
 * @author Administrator
 *
 */
public class AreaclRefer extends AbstractRefModel {
	private int m_DefaultFieldCount= 4;
	private String[] m_aryFieldCode= {  "areaclcode","areaclname"};
	private String[] m_aryFieldName= { "��������","��������" };
	private String m_sPkFieldCode= "pk_areacl";
	private String m_sRefTitle= "������Ϣ";
	private String m_sTableName= "(select pk_areacl,areaclcode,areaclname from bd_areacl where pk_fatherarea is null and dr=0  )tmp ";
	/**
	 * RouteRefModel ������ע�⡣
	 */
	public AreaclRefer() {
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
