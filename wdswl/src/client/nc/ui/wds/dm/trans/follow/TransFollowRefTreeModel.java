package nc.ui.wds.dm.trans.follow;

import nc.ui.bd.ref.AbstractRefTreeModel;
/**
 * 
 * @author Administrator
 *���˸�����Ϣ����
 */
public class TransFollowRefTreeModel extends AbstractRefTreeModel {
	
    private String m_sRefTitle = "���˸�����Ϣ����";
	 
	 private String tablename=" wds_transfollow";
	
	 private String[] fieldcode={"vcode","vname"};	 
	 
	 private String[] fieldname={"����","����"};
	
	 private String[] hidecode={"pk_wds_transfollow","pk_parent"};
	 
	 private String pkFieldCode="pk_wds_transfollow";	 
	 
	 private String sqlWherePart=" isnull(wds_transfollow.dr,0)=0";
	 
	 private int defaultFieldCount=2;
	 //--������Ҫ���ӵ�
	 private String m_strChildField="pk_wds_transfollow";
	 private String m_strFatherField ="pk_parent";
	 
	 @Override
	public String getFatherField() {
		// TODO Auto-generated method stub
		return m_strFatherField;
	}
	//--������Ҫ���ӵ�
	 @Override
	public String getChildField() {
		// TODO Auto-generated method stub
		return m_strChildField;
	}
	    /**
	     * RouteRefModel ������ע�⡣
	     */
	    public TransFollowRefTreeModel() {
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
