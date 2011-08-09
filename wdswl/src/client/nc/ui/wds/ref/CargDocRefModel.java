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
	 
	 private String tablename=" bd_cargdoc join bd_stordoc on bd_cargdoc.pk_stordoc=bd_stordoc.pk_stordoc";
	
	 private String[] fieldcode={"bd_cargdoc.cscode","bd_cargdoc.csname"};
	 
	 
	 private String[] fieldname={"��λ����","��λ����"};
	
	 private String[] hidecode={"bd_cargdoc.pk_cargdoc"};
	 
	 private String pkFieldCode="bd_cargdoc.pk_cargdoc";
	 
	 
	 private String sqlWherePart=" isnull(bd_cargdoc.dr,0)=0 and isnull(bd_stordoc.dr,0)=0 and bd_stordoc.pk_corp ='"+getPk_corp()+"' ";
	 
	 private int defaultFieldCount=3;
	 
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
	    	if(PuPubVO.getString_TrimZeroLenAsNull(cwarehouseid)!=null)
	    		sqlWherePart = sqlWherePart + " and bd_cargdoc.pk_stordoc = '"+cwarehouseid+"'";
	    	
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
