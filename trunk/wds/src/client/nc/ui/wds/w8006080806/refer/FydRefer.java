package nc.ui.wds.w8006080806.refer;

import nc.ui.bd.ref.AbstractRefModel;

public class FydRefer extends AbstractRefModel {
    private int m_DefaultFieldCount = 4;
    private String[] m_aryFieldCode = { "ddh", "custcode","custname","tc_comname" };
    private String[] m_aryFieldName = { "�������", "�������","������","���乫˾����" };
    private String m_sPkFieldCode = "pk";
    private String m_sRefTitle = "�ѷ��˵�";
    private String m_sTableName = "(select b.fyd_pk pk,case when b.fyd_ddh is not null then b.fyd_ddh "+
			" when b.splitvbillno is not null then b.splitvbillno else b.vbillno  end ddh,"+
			" e.custcode custcode custcode,e.custname custname,f.tc_comname tc_comname"+
			" from tb_fydnew b,bd_cumandoc d,bd_cubasdoc e,tb_transcompany f"+
			" where b.pk_kh=d.pk_cumandoc and d.pk_cubasdoc=e.pk_cubasdoc and b.tc_pk=f.tc_pk "+
			" and b.fyd_constatus='1')tmp ";
    
    /**
     * RouteRefModel ������ע�⡣
     */
    public FydRefer() {
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
