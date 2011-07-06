package nc.ui.wds.ref;

import nc.ui.bd.ref.AbstractRefModel;
import nc.vo.scm.pu.PuPubVO;


/**
 * 
 * @author zhf  ��λ��������  ���� �ֿ�
 *
 */
public class CargDocRefModel extends AbstractRefModel {
	
	 private String m_sRefTitle = "��λ����";
	 
	 private String tablename=" bd_cargdoc";
	
	 private String[] fieldcode={"cscode","csname"};
	 
	 
	 private String[] fieldname={"��λ����","��λ����"};
	
	 private String[] hidecode={"pk_cargdoc"};
	 
	 private String pkFieldCode="pk_cargdoc";
	 
	 
//	 private String sqlWherePart=" isnull(dr,0)=0 ";
	 
	 private int defaultFieldCount=1;
	 
	 private String cwarehouseid = null;
	 public void setStordocID(String stordocid){
		 cwarehouseid = stordocid;
	 }
	    /**
	     * RouteRefModel ������ע�⡣
	     */
	    public CargDocRefModel() {
	    	super();
	    }
	    
	    public CargDocRefModel(String stordocid) {
	    	super();
	    	this.cwarehouseid = stordocid;
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
	    	String sql =  "isnull(dr,0) = 0 ";
	    	if(PuPubVO.getString_TrimZeroLenAsNull(cwarehouseid)!=null)
	    		sql = sql + " and pk_stordoc = '"+cwarehouseid+"'";
	    	
	    	return sql;
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
