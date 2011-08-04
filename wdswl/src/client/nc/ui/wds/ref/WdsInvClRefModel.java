package nc.ui.wds.ref;

import nc.ui.bd.ref.AbstractRefModel;

//public class WdsInvClRefModel extends AbstractRefTreeModel {
//
//	public WdsInvClRefModel(String refNodeName) {
//		setRefNodeName(refNodeName);
//		// TODO �Զ����ɹ��캯�����
//	}
//	public void setRefNodeName(String refNodeName) {
//		m_strRefNodeName = refNodeName;
//		// *��������������Ӧ����
//		setFieldCode(new String[] { "vinvclcode", "vinvclname",
//				"invclasslev", "pk_invcl" });
//		setFieldName(new String[] {
//				NCLangRes4VoTransl.getNCLangRes().getStrByID("common",
//						"UC000-0001480")/* @res "�������" */,
//				NCLangRes4VoTransl.getNCLangRes().getStrByID("common",
//						"UC000-0001453") /* @res "�������" */});
//		setPkFieldCode("pk_invcl");
//		setRefCodeField("vinvclcode");
//		setRefNameField("vinvclname");
//		setTableName("wds_invcl");
//		setCodingRule(UFRefManage.getCodeRuleFromPara("BD101"));
//		// sxj 2003-02-20
//		setWherePart(" pk_corp='" + getPk_corp() + "' or pk_corp= '" + "0001"
//				+ "'");
//		resetFieldName();
//	}
//}

public class WdsInvClRefModel extends AbstractRefModel {

 private String m_sRefTitle = "�������";
	 
	 private String tablename=" wds_invcl";
	
	 private String[] fieldcode={"vinvclcode","vinvclname","pk_invcl"};
	 
	 
	 private String[] fieldname={"�������","��������"};
	
	 private String[] hidecode={"pk_invcl"};
	 
	 private String pkFieldCode="pk_invcl";
	 
	 
	 private String sqlWherePart=" isnull(dr,0)=0 and pk_corp = '"+getPk_corp()+"'";
	 
	 private int defaultFieldCount=2;
	    /**
	     * RouteRefModel ������ע�⡣
	     */
	    public WdsInvClRefModel() {
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
