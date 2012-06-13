package nc.lm.ui.classinfor;
import nc.ui.bd.ref.AbstractRefModel;
public class ClassRefModel extends AbstractRefModel{
     /*
      * ����
      */
	 private String m_sRefTitle = " ѧ����Ϣ����";
	 /*
	  * ����
	  */
	 private String tablename=" lm_class_h ";
	 /*
	  * ����Ҫ�õ��ֶ�
	  */
	 private String[] fieldcode={"cclasscode","vclassname","pk_class"};
	 /*
	  * ������ʾ����������
	  */
	 private String[] fieldname={"�༶���","�༶����"};
	 /*
	 * Ҫ���ص��ֶ�
	 */
	 private String[] hidecode={"pk_class"};
	 /*
	  * ���ձ�����
	  */
	 private String pkFieldCode="pk_class";
	 /*
	  * ���ձ�������� 
	  */
	 private String sqlWherePart=" isnull(lm_class_h.dr,0)=0 and lm_class_h.pk_corp='"+nc.ui.pub.ClientEnvironment.getInstance()
		.getCorporation().getPk_corp()+"'";
	 
	 private int defaultFieldCount=2;
	    /**
	     * RouteRefModel ������ע�⡣
	     */
	    public ClassRefModel() {
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
