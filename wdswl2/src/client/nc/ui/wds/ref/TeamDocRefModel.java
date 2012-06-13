package nc.ui.wds.ref;

import nc.ui.bd.ref.AbstractRefModel;

public class TeamDocRefModel extends AbstractRefModel{

	 private String m_sRefTitle = "װж���鵵��";
	 
	 private String tablename=" wds_teamdoc_h ";
	
	 private String[] fieldcode={"teamcode","teamname","teamaddr"};
	 
	 
	 private String[] fieldname={"�������","��������","�����ַ"};
	
	 private String[] hidecode={"pk_wds_teamdoc_h"};
	 
	 private String pkFieldCode="pk_wds_teamdoc_h";
	 
	 
	 private String sqlWherePart=" isnull(dr,0)=0 and pk_corp = '"+getPk_corp()+"'";
	 
	 private int defaultFieldCount=3;
	    /**
	     * RouteRefModel ������ע�⡣
	     */
	    public TeamDocRefModel() {
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
