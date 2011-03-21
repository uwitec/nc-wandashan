package nc.ui.wds.w8004040220.refer;

import nc.ui.bd.ref.AbstractRefModel;

public class CardocRefer extends AbstractRefModel {
    private int m_DefaultFieldCount = 3;
    private String[] m_aryFieldCode = {  "cscode","csname","storname" };
    private String[] m_aryFieldName = {  "��λ����","��λ����","�ֲ�����" };
    private String m_sPkFieldCode = "pk_cargdoc";
    private String m_sRefTitle = "��λ����";
    private String m_sTableName = "( select (select bd_stordoc.storname from bd_stordoc where bd_cargdoc.pk_stordoc=bd_stordoc.pk_stordoc and def1=1) storname,pk_cargdoc,cscode,csname from bd_cargdoc "+
" where (select bd_stordoc.storname from bd_stordoc where bd_cargdoc.pk_stordoc=bd_stordoc.pk_stordoc and def1=1) is not null and dr=0 )tmp ";
    
    /**
     * RouteRefModel ������ע�⡣
     */
    public CardocRefer() {
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
