package nc.lm.ui.classinfor;

import nc.ui.bd.ref.AbstractRefModel;

public class StuRefModel extends AbstractRefModel {

	 private String m_sRefTitle = "ѧ��ҵ�񵵰�";
	 
	 private String tablename="lm_class_b join lm_class_h on lm_class_b.pk_class=lm_class_h.pk_class";
	
	 private String[] fieldcode={"ccstucode","vstuname","pk_class_b"};
	 
	 
	 private String[] fieldname={"ѧ��","ѧ������"};
	
	 private String[] hidecode={"pk_class_b"};
	 
	 private String pkFieldCode="pk_class_b";
	 
	 
	 private String sqlWherePart=" isnull(lm_class_h.dr,0)=0 and isnull(lm_class_b.dr,0)=0 and lm_class_h.pk_corp='"+nc.ui.pub.ClientEnvironment.getInstance()
		.getCorporation().getPk_corp()+"'";
	 
	 private int defaultFieldCount=2;
	    /**
	     * RouteRefModel ������ע�⡣
	     */
	    public StuRefModel() {
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
		return pkFieldCode;
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
	    	
	    	        
	    	return sqlWherePart;
	    }
	    /**
	     * �������ݿ�������ͼ�� �������ڣ�(01-4-4 0:57:23)
	     * 
	     * @return java.lang.String
	     */
	    public String getTableName() {
	    
		return tablename;
	    }
	    @Override
	    public boolean isCacheEnabled() {
	    	
	    	return false;
	    }
}
