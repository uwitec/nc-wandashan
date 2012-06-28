package nc.vo.wds.transfer;

import nc.vo.trade.field.IBillField;

public class BillField implements IBillField {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5099996641414575835L;
	private String field_Corp = "pk_corp"; // 单据表头上的公司主键
	private String field_Busitype = "cbizid"; // 单据表头上的业务类型
	private String field_Billtype = "vbilltype"; // 单据表头上的单据类型
	private String field_BillStatus = "vbillstatus"; // 单据表头上的单状态

	private String field_CheckMan = "cauditorid"; // 审批人
	private String field_CheckDate = "dauditdate"; // 审批日期
	private String field_Operator = "voperatorid"; // 操作人
	private String field_BillNo = "vbillcode"; // 单据编号
	private String field_CheckNote = "vapprovenote"; // 审核批语
	private String field_BusiCode = "vbusicode"; // 业务类型代码
	
	private String field_LastBilltype = "vlastbilltype";// 上层单据类型
	private String field_LastBillId = "vlastbillid";// 上层单据ID
	private String field_LastBillRowId = "vlastbillrowid"; // 上层单据RowID
	private String field_SourceBilltype = "vsourcebilltype";// 来源单据类型
	private String field_SourceBillId = "vsourcebillid";// 来源单据ID
	private String field_SourceBillRowId = "vsourcebillrowid";// 来源单据RowID

	private static BillField m_billField = new BillField();

	/**
	 * BillField 构造子注解。
	 */
	private BillField() {
		super();
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-1-4 14:27:13)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getField_BillNo() {
		return field_BillNo;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-1-4 14:27:13)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getField_BillStatus() {
		return field_BillStatus;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-1-4 14:27:13)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getField_Billtype() {
		return field_Billtype;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-1-4 14:27:13)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getField_BusiCode() {
		return field_BusiCode;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-1-4 14:27:13)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getField_Busitype() {
		return field_Busitype;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-1-4 14:27:13)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getField_CheckDate() {
		return field_CheckDate;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-1-4 14:27:13)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getField_CheckMan() {
		return field_CheckMan;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-1-4 14:27:13)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getField_CheckNote() {
		return field_CheckNote;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-1-4 14:27:13)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getField_Corp() {
		return field_Corp;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-1-4 14:27:13)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getField_LastBillId() {
		return field_LastBillId;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-1-4 14:27:13)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getField_LastBillRowId() {
		return field_LastBillRowId;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-1-4 14:27:13)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getField_LastBillType() {
		return field_LastBilltype;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-1-4 14:27:13)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getField_Operator() {
		return field_Operator;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-1-4 14:27:13)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getField_SourceBillId() {
		return field_SourceBillId;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-1-4 14:27:13)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getField_SourceBillRowId() {
		return field_SourceBillRowId;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-1-4 14:27:13)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getField_SourceBillType() {
		return field_SourceBilltype;
	}

	/**
	 * 获取该实例。 创建日期：(2004-1-11 22:18:27)
	 */
	public static BillField getInstance() {
		return m_billField;
	}

}
