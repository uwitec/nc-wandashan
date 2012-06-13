package nc.ui.wds.ic.other.in;

import nc.vo.trade.field.IBillField;

public  class BillField implements IBillField {	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String field_Corp = "pk_corp"; //单据表头上的公司主键
	private String field_Busitype = "geh_cbiztype"; //单据表头上的业务类型
	private String field_Billtype = "geh_cbilltypecode"; //单据表头上的单据类型
	private String field_BillStatus = "pwb_fbillflag"; //单据表头上的单状态

	private String field_CheckMan = ""; //审批人
	private String field_CheckDate = ""; //审批日期
	private String field_Operator = "coperatorid"; //操作人
	private String field_BillNo = "geh_billcode"; //单据编号
//	private String field_CheckNote = "vapprovenote"; //审核批语
//	private String field_BusiCode = "vbusicode";  //业务类型代码

//	private String field_LastBilltype="vlastbilltype";//上层单据类型
//	private String field_LastBillId="vlastbillid";//上层单据ID
//	private String field_LastBillRowId="vlastbillrowid"; //上层单据RowID
//	private String field_SourceBilltype="vsourcebilltype";//来源单据类型
//	private String field_SourceBillId="vsourcebillid";//来源单据ID
//	private String field_SourceBillRowId="vsourcebillrowid";//来源单据RowID

	public java.lang.String getField_BillNo() {
		return field_BillNo;
	}
	/**
	 * 此处插入方法说明。
	 * 创建日期：(2004-1-4 14:27:13)
	 * @return java.lang.String
	 */
	public java.lang.String getField_BillStatus() {
		return field_BillStatus;
	}
	/**
	 * 此处插入方法说明。
	 * 创建日期：(2004-1-4 14:27:13)
	 * @return java.lang.String
	 */
	public java.lang.String getField_Billtype() {
		return field_Billtype;
	}
	/**
	 * 此处插入方法说明。
	 * 创建日期：(2004-1-4 14:27:13)
	 * @return java.lang.String
	 */
	public java.lang.String getField_BusiCode() {
		return null;
	}
	/**
	 * 此处插入方法说明。
	 * 创建日期：(2004-1-4 14:27:13)
	 * @return java.lang.String
	 */
	public java.lang.String getField_Busitype() {
		return field_Busitype;
	}
	/**
	 * 此处插入方法说明。
	 * 创建日期：(2004-1-4 14:27:13)
	 * @return java.lang.String
	 */
	public java.lang.String getField_CheckDate() {
		return field_CheckDate;
	}
	/**
	 * 此处插入方法说明。
	 * 创建日期：(2004-1-4 14:27:13)
	 * @return java.lang.String
	 */
	public java.lang.String getField_CheckMan() {
		return field_CheckMan;
	}
	/**
	 * 此处插入方法说明。
	 * 创建日期：(2004-1-4 14:27:13)
	 * @return java.lang.String
	 */
	public java.lang.String getField_CheckNote() {
		return null;
	}
	/**
	 * 此处插入方法说明。
	 * 创建日期：(2004-1-4 14:27:13)
	 * @return java.lang.String
	 */
	public java.lang.String getField_Corp() {
		return field_Corp;
	}
	/**
	 * 此处插入方法说明。
	 * 创建日期：(2004-1-4 14:27:13)
	 * @return java.lang.String
	 */
	public java.lang.String getField_LastBillId() {
		return null;
	}
	/**
	 * 此处插入方法说明。
	 * 创建日期：(2004-1-4 14:27:13)
	 * @return java.lang.String
	 */
	public java.lang.String getField_LastBillRowId() {
		return null;
	}
	/**
	 * 此处插入方法说明。
	 * 创建日期：(2004-1-4 14:27:13)
	 * @return java.lang.String
	 */
	public java.lang.String getField_LastBillType() {
		return null;
	}
	/**
	 * 此处插入方法说明。
	 * 创建日期：(2004-1-4 14:27:13)
	 * @return java.lang.String
	 */
	public java.lang.String getField_Operator() {
		return field_Operator;
	}
	/**
	 * 此处插入方法说明。
	 * 创建日期：(2004-1-4 14:27:13)
	 * @return java.lang.String
	 */
	public java.lang.String getField_SourceBillId() {
		return null;
	}
	/**
	 * 此处插入方法说明。
	 * 创建日期：(2004-1-4 14:27:13)
	 * @return java.lang.String
	 */
	public java.lang.String getField_SourceBillRowId() {
		return null;
	}
	/**
	 * 此处插入方法说明。
	 * 创建日期：(2004-1-4 14:27:13)
	 * @return java.lang.String
	 */
	public java.lang.String getField_SourceBillType() {
		return null;
	}
//	/**
//	 * 获取该实例。
//	 * 创建日期：(2004-1-11 22:18:27)
//	 */
//	public static BillField getInstance() {
//		return m_billField;
//	}

}
