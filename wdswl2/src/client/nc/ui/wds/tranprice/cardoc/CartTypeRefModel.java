package nc.ui.wds.tranprice.cardoc;

import nc.ui.bd.ref.AbstractRefModel;

public class CartTypeRefModel extends AbstractRefModel{


	 private String m_sRefTitle = "���͵���";
	 
	 private String tablename=" wds_cartyedoc";
	
	 private String[] fieldcode={"ccartypecode","vcartypename","nloadnum","iloadunit"};
	 
	 
	 private String[] fieldname={"�������ͱ���","������������","����","���ص�λ"};
	
	 private String[] hidecode={"pk_wds_cartyedoc"};
	 
	 private String pkFieldCode="pk_wds_cartyedoc";
	 
	 
	 private String sqlWherePart=" isnull(dr,0)=0";
	 
	 private int defaultFieldCount=4;
	    /**
	     * 
	     */
	    public CartTypeRefModel() {
	    	super();
	    }

	    /**
	     * 
	     */
	    public int getDefaultFieldCount() {
		return defaultFieldCount;
	    }

	    /**
	     * 
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
