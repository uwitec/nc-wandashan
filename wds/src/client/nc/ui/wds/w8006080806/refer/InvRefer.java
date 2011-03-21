package nc.ui.wds.w8006080806.refer;

import nc.ui.bd.ref.AbstractRefModel;

public class InvRefer extends AbstractRefModel {
    private int m_DefaultFieldCount = 3;
    private String[] m_aryFieldCode = { "invcode", "invname","invspec" };
    private String[] m_aryFieldName = { "�������", "�������","������" };
    private String m_sPkFieldCode = "pk_invbasdoc";
    private String m_sRefTitle = "�������";
    private String m_sTableName = "(select pk_invbasdoc , invcode ,invname,invspec from bd_invbasdoc t where dr = 0 and ( def14 ='0' or def14 = '1')and dr=0)tmp ";
    
    /**
     * RouteRefModel ������ע�⡣
     */
    public InvRefer() {
	super();
    }

    /**
     * getDefaultFieldCount ����ע�⡣
     */
    public int getDefaultFieldCount() {
	return m_DefaultFieldCount;
    }

    /**
     * ��ʾ�ֶ��б� �������ڣ�(01-4-4 0:57:23)
     * 
     * @return java.lang.String
     */
    public java.lang.String[] getFieldCode() {
	return m_aryFieldCode;
    }

    /**
     * ��ʾ�ֶ������� �������ڣ�(01-4-4 0:57:23)
     * 
     * @return java.lang.String
     */
    public java.lang.String[] getFieldName() {
	return m_aryFieldName;
    }

    /**
     * �˴����뷽��˵���� �������ڣ�(2001-9-6 10:56:48)
     */
    public String[] getHiddenFieldCode() {
	return new String[] { m_sPkFieldCode };
    }

    /**
     * �����ֶ���
     * 
     * @return java.lang.String
     */
    public String getPkFieldCode() {
	return m_sPkFieldCode;
    }

    /**
     * ���ձ��� �������ڣ�(01-4-4 0:57:23)
     * 
     * @return java.lang.String
     */
    public String getRefTitle() {
	return m_sRefTitle;
    }

    /**
     * �������ݿ�������ͼ�� �������ڣ�(01-4-4 0:57:23)
     * 
     * @return java.lang.String
     */
    public String getTableName() {
	return m_sTableName;
    }

}
