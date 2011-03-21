package nc.ui.wds.w80020202.refer;

import nc.ui.bd.ref.AbstractRefModel;

public class Hfpfer extends AbstractRefModel {
	private int m_DefaultFieldCount= 4;
	private String[] m_aryFieldCode= {  "hfp_varietycode","hfp_variety","hfp_specificationcode" ,"hfp_specification"};
	private String[] m_aryFieldName= { "Ʒ�ֱ���","Ʒ������","������","�������"  };
	private String m_sPkFieldCode= "hfp_pk";
	private String m_sRefTitle= "�۸������Ϣ";
	private String m_sTableName= "(select hfp_pk,hfp_varietycode,hfp_variety,hfp_specificationcode,hfp_specification from tb_handlingfeeprice where dr=0)tmp ";
	/**
	 * RouteRefModel ������ע�⡣
	 */
	public Hfpfer() {
		super();
	}

	/**
	 * getDefaultFieldCount ����ע�⡣
	 */
	public int getDefaultFieldCount() {
		return m_DefaultFieldCount;
	}
	/**
	 * ��ʾ�ֶ��б�
	 * �������ڣ�(01-4-4 0:57:23)
	 * @return java.lang.String
	 */
	public java.lang.String[] getFieldCode() {
		return m_aryFieldCode;
	}
	/**
	 * ��ʾ�ֶ�������
	 * �������ڣ�(01-4-4 0:57:23)
	 * @return java.lang.String
	 */
	public java.lang.String[] getFieldName() {
		return m_aryFieldName;
	}
	/**
	 * �˴����뷽��˵����
	 * �������ڣ�(2001-9-6 10:56:48)
	 */
	public String[] getHiddenFieldCode() {
		return new String[] { m_sPkFieldCode };
	}
	/**
	 * �����ֶ���
	 * @return java.lang.String
	 */
	public String getPkFieldCode() {
		return m_sPkFieldCode;
	}
	/**
	 * ���ձ���
	 * �������ڣ�(01-4-4 0:57:23)
	 * @return java.lang.String
	 */
	public String getRefTitle() {
		return m_sRefTitle;
	}
	/**
	 * �������ݿ�������ͼ��
	 * �������ڣ�(01-4-4 0:57:23)
	 * @return java.lang.String
	 */
	public String getTableName() {
		return m_sTableName;
	}
	
}
