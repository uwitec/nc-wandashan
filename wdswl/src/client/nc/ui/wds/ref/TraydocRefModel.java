package nc.ui.wds.ref;

import nc.ui.bd.ref.AbstractRefModel;
import nc.vo.scm.pu.PuPubVO;

public class TraydocRefModel extends AbstractRefModel{

	 private String m_sRefTitle = "���̵���";
	 
	 private String tablename=" bd_cargdoc_tray";
	
	 private String[] fieldcode={"bd_cargdoc_tray.cdt_traycode","bd_cargdoc_tray.cdt_traycode"};
	 
	 
	 private String[] fieldname={"���̱���","���̱���"};
	
	 private String[] hidecode={"bd_cargdoc_tray.cdt_pk"};
	 
	 private String pkFieldCode="bd_cargdoc_tray.cdt_pk";
	 
	 
	 private String sqlWherePart=" isnull(dr,0)=0";
	 
	 private String pk_cargdoc = null;//��λID
	 
	 public void setCargDocID(String cargdoc){
		 pk_cargdoc = cargdoc;
	 }
	 
	 private int defaultFieldCount=1;
	    /**
	     * RouteRefModel ������ע�⡣
	     */
	    public TraydocRefModel() {
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
	    	if(PuPubVO.getString_TrimZeroLenAsNull(pk_cargdoc)==null)
	    		return sqlWherePart;
	    	else
	    		return sqlWherePart + " and pk_cargdoc = '"+pk_cargdoc+"'";
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
