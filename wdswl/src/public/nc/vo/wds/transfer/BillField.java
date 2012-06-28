package nc.vo.wds.transfer;

import nc.vo.trade.field.IBillField;

public class BillField implements IBillField {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5099996641414575835L;
	private String field_Corp = "pk_corp"; // ���ݱ�ͷ�ϵĹ�˾����
	private String field_Busitype = "cbizid"; // ���ݱ�ͷ�ϵ�ҵ������
	private String field_Billtype = "vbilltype"; // ���ݱ�ͷ�ϵĵ�������
	private String field_BillStatus = "vbillstatus"; // ���ݱ�ͷ�ϵĵ�״̬

	private String field_CheckMan = "cauditorid"; // ������
	private String field_CheckDate = "dauditdate"; // ��������
	private String field_Operator = "voperatorid"; // ������
	private String field_BillNo = "vbillcode"; // ���ݱ��
	private String field_CheckNote = "vapprovenote"; // �������
	private String field_BusiCode = "vbusicode"; // ҵ�����ʹ���
	
	private String field_LastBilltype = "vlastbilltype";// �ϲ㵥������
	private String field_LastBillId = "vlastbillid";// �ϲ㵥��ID
	private String field_LastBillRowId = "vlastbillrowid"; // �ϲ㵥��RowID
	private String field_SourceBilltype = "vsourcebilltype";// ��Դ��������
	private String field_SourceBillId = "vsourcebillid";// ��Դ����ID
	private String field_SourceBillRowId = "vsourcebillrowid";// ��Դ����RowID

	private static BillField m_billField = new BillField();

	/**
	 * BillField ������ע�⡣
	 */
	private BillField() {
		super();
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-1-4 14:27:13)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getField_BillNo() {
		return field_BillNo;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-1-4 14:27:13)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getField_BillStatus() {
		return field_BillStatus;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-1-4 14:27:13)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getField_Billtype() {
		return field_Billtype;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-1-4 14:27:13)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getField_BusiCode() {
		return field_BusiCode;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-1-4 14:27:13)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getField_Busitype() {
		return field_Busitype;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-1-4 14:27:13)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getField_CheckDate() {
		return field_CheckDate;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-1-4 14:27:13)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getField_CheckMan() {
		return field_CheckMan;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-1-4 14:27:13)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getField_CheckNote() {
		return field_CheckNote;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-1-4 14:27:13)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getField_Corp() {
		return field_Corp;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-1-4 14:27:13)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getField_LastBillId() {
		return field_LastBillId;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-1-4 14:27:13)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getField_LastBillRowId() {
		return field_LastBillRowId;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-1-4 14:27:13)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getField_LastBillType() {
		return field_LastBilltype;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-1-4 14:27:13)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getField_Operator() {
		return field_Operator;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-1-4 14:27:13)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getField_SourceBillId() {
		return field_SourceBillId;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-1-4 14:27:13)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getField_SourceBillRowId() {
		return field_SourceBillRowId;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-1-4 14:27:13)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getField_SourceBillType() {
		return field_SourceBilltype;
	}

	/**
	 * ��ȡ��ʵ���� �������ڣ�(2004-1-11 22:18:27)
	 */
	public static BillField getInstance() {
		return m_billField;
	}

}
