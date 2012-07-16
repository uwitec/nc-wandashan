package nc.ui.wds.ic.cargtray;

import nc.ui.bd.ref.AbstractRefModel;
/**
 * ���ƶ����Ĵ������
 * @author mlr
 */
public class InvmandocRefModel1 extends AbstractRefModel{
	
	 private String m_sRefTitle = "�������";
	 

	 private String tablename1="wds_invbasdoc  " +
    //	" tb_spacegoods on wds_invbasdoc.pk_invmandoc=tb_spacegoods.pk_invmandoc   " +
		" join bd_invbasdoc on wds_invbasdoc.pk_invbasdoc= bd_invbasdoc.pk_invbasdoc ";
	  // " join wds_cargdoc1 on tb_spacegoods.pk_wds_cargdoc=wds_cargdoc1.pk_wds_cargdoc ";
	
	 private String[] fieldcode={"invcode","invname","invspec","invtype"};
	 
	 
	 private String[] fieldname={"�������","�������","���","�ͺ�"};
	
	 private String[] hidecode={"wds_invbasdoc.pk_invmandoc","wds_invbasdoc.pk_invbasdoc"};
	 
	 private int defaultFieldCount=4;
	    /**
	     * RouteRefModel ������ע�⡣
	     */
	    public InvmandocRefModel1() {
	    	super();
	    }

	    /**
	     * getDefaultFieldCount ����ע�⡣
	     */
	    public int getDefaultFieldCount() {
		return defaultFieldCount;
	    }

	    /**
	     * ��ʾ�ֶ��б� �������ڣ�(01-4-4 0:57:23)
	     * 
	     * @return java.lang.String
	     */
	    public java.lang.String[] getFieldCode() {
	    	return fieldcode ;
	    }

	    /**
	     * ��ʾ�ֶ������� �������ڣ�(01-4-4 0:57:23)
	     * 
	     * @return java.lang.String
	     */
	    public java.lang.String[] getFieldName() {
	    	return fieldname;
	    }

	    /**
	     * �˴����뷽��˵���� �������ڣ�(2001-9-6 10:56:48)
	     */
	    public String[] getHiddenFieldCode() {
	    	return hidecode;
	    }

	    /**
	     * �����ֶ���
	     * 
	     * @return java.lang.String
	     */
	    public String getPkFieldCode() {
		return "wds_invbasdoc.pk_invmandoc";
	    }

	    /**
	     * ���ձ��� �������ڣ�(01-4-4 0:57:23)
	     * 
	     * @return java.lang.String
	     */
	    public String getRefTitle() {
		return m_sRefTitle;
	    }
	    @Override
	    public String getWherePart() {
	    	StringBuffer strWhere = new StringBuffer();
	    	strWhere.append("  isnull(wds_invbasdoc.dr,0)=0 ");
	    	strWhere.append(" and isnull(bd_invbasdoc.dr,0) = 0 ")  ;
	        strWhere.append(" and wds_invbasdoc.pk_corp='"+getPk_corp()+"'");
	    	return strWhere.toString();
	    }
	    /**
	     * �������ݿ�������ͼ�� �������ڣ�(01-4-4 0:57:23)
	     * 
	     * @return java.lang.String
	     */
	    public String getTableName() {
	    
		return tablename1;
	    }
	    @Override
	    public boolean isCacheEnabled() {
	    	
	    	return false;
	    }
}
