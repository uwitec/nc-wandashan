package nc.ui.wds.ic.other.in;

import nc.vo.trade.field.IBillField;

public  class BillField implements IBillField {	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String field_Corp = "pk_corp"; //���ݱ�ͷ�ϵĹ�˾����
	private String field_Busitype = "geh_cbiztype"; //���ݱ�ͷ�ϵ�ҵ������
	private String field_Billtype = "geh_cbilltypecode"; //���ݱ�ͷ�ϵĵ�������
	private String field_BillStatus = "pwb_fbillflag"; //���ݱ�ͷ�ϵĵ�״̬

	private String field_CheckMan = ""; //������
	private String field_CheckDate = ""; //��������
	private String field_Operator = "coperatorid"; //������
	private String field_BillNo = "geh_billcode"; //���ݱ��
//	private String field_CheckNote = "vapprovenote"; //�������
//	private String field_BusiCode = "vbusicode";  //ҵ�����ʹ���

//	private String field_LastBilltype="vlastbilltype";//�ϲ㵥������
//	private String field_LastBillId="vlastbillid";//�ϲ㵥��ID
//	private String field_LastBillRowId="vlastbillrowid"; //�ϲ㵥��RowID
//	private String field_SourceBilltype="vsourcebilltype";//��Դ��������
//	private String field_SourceBillId="vsourcebillid";//��Դ����ID
//	private String field_SourceBillRowId="vsourcebillrowid";//��Դ����RowID

	public java.lang.String getField_BillNo() {
		return field_BillNo;
	}
	/**
	 * �˴����뷽��˵����
	 * �������ڣ�(2004-1-4 14:27:13)
	 * @return java.lang.String
	 */
	public java.lang.String getField_BillStatus() {
		return field_BillStatus;
	}
	/**
	 * �˴����뷽��˵����
	 * �������ڣ�(2004-1-4 14:27:13)
	 * @return java.lang.String
	 */
	public java.lang.String getField_Billtype() {
		return field_Billtype;
	}
	/**
	 * �˴����뷽��˵����
	 * �������ڣ�(2004-1-4 14:27:13)
	 * @return java.lang.String
	 */
	public java.lang.String getField_BusiCode() {
		return null;
	}
	/**
	 * �˴����뷽��˵����
	 * �������ڣ�(2004-1-4 14:27:13)
	 * @return java.lang.String
	 */
	public java.lang.String getField_Busitype() {
		return field_Busitype;
	}
	/**
	 * �˴����뷽��˵����
	 * �������ڣ�(2004-1-4 14:27:13)
	 * @return java.lang.String
	 */
	public java.lang.String getField_CheckDate() {
		return field_CheckDate;
	}
	/**
	 * �˴����뷽��˵����
	 * �������ڣ�(2004-1-4 14:27:13)
	 * @return java.lang.String
	 */
	public java.lang.String getField_CheckMan() {
		return field_CheckMan;
	}
	/**
	 * �˴����뷽��˵����
	 * �������ڣ�(2004-1-4 14:27:13)
	 * @return java.lang.String
	 */
	public java.lang.String getField_CheckNote() {
		return null;
	}
	/**
	 * �˴����뷽��˵����
	 * �������ڣ�(2004-1-4 14:27:13)
	 * @return java.lang.String
	 */
	public java.lang.String getField_Corp() {
		return field_Corp;
	}
	/**
	 * �˴����뷽��˵����
	 * �������ڣ�(2004-1-4 14:27:13)
	 * @return java.lang.String
	 */
	public java.lang.String getField_LastBillId() {
		return null;
	}
	/**
	 * �˴����뷽��˵����
	 * �������ڣ�(2004-1-4 14:27:13)
	 * @return java.lang.String
	 */
	public java.lang.String getField_LastBillRowId() {
		return null;
	}
	/**
	 * �˴����뷽��˵����
	 * �������ڣ�(2004-1-4 14:27:13)
	 * @return java.lang.String
	 */
	public java.lang.String getField_LastBillType() {
		return null;
	}
	/**
	 * �˴����뷽��˵����
	 * �������ڣ�(2004-1-4 14:27:13)
	 * @return java.lang.String
	 */
	public java.lang.String getField_Operator() {
		return field_Operator;
	}
	/**
	 * �˴����뷽��˵����
	 * �������ڣ�(2004-1-4 14:27:13)
	 * @return java.lang.String
	 */
	public java.lang.String getField_SourceBillId() {
		return null;
	}
	/**
	 * �˴����뷽��˵����
	 * �������ڣ�(2004-1-4 14:27:13)
	 * @return java.lang.String
	 */
	public java.lang.String getField_SourceBillRowId() {
		return null;
	}
	/**
	 * �˴����뷽��˵����
	 * �������ڣ�(2004-1-4 14:27:13)
	 * @return java.lang.String
	 */
	public java.lang.String getField_SourceBillType() {
		return null;
	}
//	/**
//	 * ��ȡ��ʵ����
//	 * �������ڣ�(2004-1-11 22:18:27)
//	 */
//	public static BillField getInstance() {
//		return m_billField;
//	}

}
