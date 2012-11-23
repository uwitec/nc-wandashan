package nc.ui.wds.tranprice.cardoc;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

public class TransCorpRefModel extends AbstractRefModel{
	
     private String m_sRefTitle = "���乫˾����";
	 
	 private String tablename=" wds_tanscorp_h ";
	
	 private String[] fieldcode={"ctranscorpcode","vtranscorpname"};
	 
	 
	 private String[] fieldname={"���乫˾����","���乫˾����"};
	
	 private String[] hidecode={"pk_wds_tanscorp_h"};
	 
	 private String pkFieldCode="pk_wds_tanscorp_h";
	 
	 
	 private String sqlWherePart=" isnull(wds_tanscorp_h.dr,0)=0 and wds_tanscorp_h.pk_corp='"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'";
	 
	 private int defaultFieldCount=2;
	    /**
	     * RouteRefModel ������ע�⡣
	     */
	    public TransCorpRefModel() {
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
