package nc.vo.hg.pu.pub;

public class HgPubConst {
	
	//鹤岗精度
	public static int NUM_DIGIT = 4;
	public static int PRICE_DIGIT = 6;
	public static int MNY_DIGIT = 2;
	
	public static int PLAN_YEAR_IMPORT_BILLSTATUS = 9;
	
	//到货   验收合格   不合格   无合同   入库
	
	public static final int purchaseIn_arr = 0;
	public static final int purchaseIn_ok = 1;
	public static final int purchaseIn_no = 2;
	public static final int purchaseIn_nopact = 3;
	public static final int purchaseIn_in = 4;
	
	//系统请购类型---采购    2
	
	public static final int DEFAULT_NUM_DIGIT = 8;
	public final static int IPRAYTYPE = 2;
	
	public final static String PLAN_DEAL_MONTHADJUST_TEMPLETID = "0001AZ100000000034O1";
	
    /*考核设置*/
	public final static String ZJSZ = "ZJSZ";//资金设置
	public final static String DESZ = "DESZ";//定额设置
	public final static String ZXSZ = "ZXSZ";//专项设置
	public final static String XJWZ = "XJWZ";//新旧物资比例设置
	public final static String RCBL = "RCBL";//容差比例设置
	
    /*计划管理*/
	public static String PLAN_YEAR_BILLTYPE = "HG01";//物资需求计划
	public static String PLAN_MONTH_BILLTYPE = "HG02";//月需求计划
	public static String PLAN_TEMP_BILLTYPE = "HG03";//临时计划
	public static String PLAN_MNY_BILLTYPE = "HG04";//专项资金计划
	public static String JHXM = "JHXM";//计划项目
	public static String PLAN_MONDEAL_BILLTYPE = "HG05";//月计划调整
	public static String NEW_MATERIALS_BILLTYPE = "HG06";//新物资申请
	public static String USER_AND_CUST = "HG07";//登录用户与供应商之间的关系
	public static String PLAN_BALANCE_BILLTYPE = "HG08";//需求计划平衡
	public static String PLAN_BAOZHANG_BILLTYPE = "HG10";//报账单 40040407
	
	public static final int PLAN_ROW_STATUS_FREE = 0;//未上报
	public static final int PLAN_ROW_STATUS_COMMIT = 1;//计划表体用已上报
	
	
	public static final String PLAN_BODY_ROWNO = "crowno";
	
	
	
	public static String PLAN_DEAL_BILLTYPE = "DEAL";//计划处理虚拟单据类型
	public static String PLAN_DEAL_BILLTYPE2 = "DEAL2";//计划处理虚拟单据类型  模板使用 该模板没有 选中框
	
//	public static int PLAN_TYPE_YEAR = 0;//年计划
//	public static int PLAN_TYPE_TEMP = 1;//临时计划
//	public static int PLAN_TYPE_MNY = 2;//专项资金计划
	
	public static String PLAN_DEAL_FUNCODE = "40050401";
	public static String PLAN_DEAL_FUNCODE2 = "0001A310000000006C0W";
	public static String PLAN_MONDEAL_NODEKEY = "PLANBYYEAR";
	
	public static final int FUND_CHECK_FUND = 0;//类型---资金
	public static final int FUND_CHECK_QUATO = 1;//类型---限额
	public static final int FUND_CHECK_SPECIALFUND = 2;//类型---专项资金
	public static final int FUND_CHECK_ERRORTYPE = -2;
	public static final int FUND_CHECK_FUND_QUATO = -1;//资金+限额
	
	public static String[] NAFTERNUM={ "naftenum1", "naftenum2", "naftenum3","naftenum4", "naftenum5", "naftenum6",
			"naftenum7","naftenum8", "naftenum9", "naftenum10", "nafternum11","nafternum12"};//12月份调整后数量字段
	public static String[] NMONTHNUM={"nmonnum1", "nmonnum2", "nmonnum3", "nmonnum4","nmonnum5", "nmonnum6",
			"nmonnum7", "nmonnum8","nmonnum9", "nmonnum10", "nmonnum11", "nmonnum12"};//12月份初始数量字段
	public static String[] NTOTAILNUM={"ntotailnum1", "ntotailnum2", "ntotailnum3", "ntotailnum4","ntotailnum5", "ntotailnum6",
			"ntotailnum7", "ntotailnum8","ntotailnum9", "ntotailnum10", "ntotailnum11", "ntotailnum12"};//12月份累计数量字段
	public static String[] NADJUSTNUM={"nadjustnum1", "nadjustnum2", "nadjustnum3", "nadjustnum4","nadjustnum5", "nadjustnum6",
		"nadjustnum7", "nadjustnum8","nadjustnum9", "nadjustnum10", "nadjustnum11", "nadjustnum12"};//12月份累计数量字段


	public static  String[] VUSERDEF={"vuserdef13","vuserdef14","vuserdef15","vuserdef16","vuserdef17","vuserdef18",
		"vuserdef19","vuserdef20"};//表头自定义项13-20 是否合格   质量问题  质量问题原因 验收人 验收时间 封存标志  处理日期 处理人
	public static String F_IS_SELF = "vuserdef12";
	public static String SELF ="自制";
	public static final String CHECK_TEMP_ID = "0001AA1000000000480H";//验收信息PK_TEMP_ID  CHEC
	public static final String UNCHECK_DEAL_TEMP_ID = "0001AA10000000004UH1";//不合格处理PK_TEMP_ID  quality

	public static final String MODIFY_TEMP_ID = "0001AA10000000005MXS";//调整明细PK_TEMP_ID  TZMX

	public static final String SUPPLY_PACT_DLG_TEMPLET_ID= "0001AZ10000000006KDB";//不合格处理PK_TEMP_ID


	public static String PLAN_PROJECT_DEF = "vdef11";//用于专项资金计划的计划项目(表头)
	public static String NUM_DEF_QUA = "vuserdef20";//用于验收合格数量（表体）
	public static String NUM_DEF_ARR = "vuserdef18";//用于到货数量（表体）
	public static String NUM_DEF_FAC= "vuserdef19";//用于实收数量（表体）
	public static String HG_TO_PARA_01 = "HGTO0101";
	public static String HG_SO_PARA_01 = "HGSO01";
	
	public static String[] Plan_Head_EditItems = new String[]{"creqwarehouseid","capplydeptid","capplypsnid","cinvclassid","caccperiodschemeid","cplanprojectid","creqcalbodyid","csupplycorpid","csupplydeptid"};

	
	public static String[] PurchaseIn_ButtonName = {"集采流程","供应商寄存","普通采购","受托代销","借入转采购","供应商寄存管理采购","自制单据","采购到货","查询","刷新","首页",
			"上页","下页","末页","列表显示","自制退库","采购订单退库","不合格处理","卡片显示"};
	
	public static String[] PurchaseIn_HeadItems ={"dbilldate","cwarehouseid","cbiztype","cdispatcherid","cwhsmanagerid","cdptid","cbizid","cproviderid","vnote"};

	public static String[] MATERIALS_HEAD_EDITITEMS = {"invcode","pk_taxitems","ijjway","biszywz","bisjjwz","bisdcdx","nplanprice","invmnecode","vmaterial","vtechstan"};
	
	public static String VENDOR_FREEZE_EWASON = "到货质检不合格";
	public static String VENDOR_UNFREEZE_EWASON = "到货质检不合格处理完成";
	
	public static String PRAY_BILL_VMI_FIELDNAME = "vdef2";//存货代储代销属性
	public static String PRAY_BILL_PURTYPE_FIELDNAME = "vdef1";//存货自采/集采 属性
	public static String PRAY_BILL_PURTYPE_FIELDNAME2 = "pk_defdoc1";//存货自采/集采 属性
	
	public static String INVBAS_PURTYPE_FIELDNAME = "def1";//存货自采/集采 属性
	public static String INVBAS_VMI_FIELDNAME = "def2";//存货代储代销属性
	public static String INVBAS_IMPROT_FIELDNAME = "def3";//重要物资
	public static String INVBAS_OLD_FIELDNAME = "def4";//交旧物资
	public static String INVBAS_VTS_FIELDNAME = "def5";//技术标准
	public static String INVBAS_VMT_FIELDNAME = "def6";//材质
	public static String INVBAS_NPP_FIELDNAME = "def20";//计划价
	
	
	public static String INVBAS_WAY = "采购方式";//采购方式
	public static String INVBAS_WAY_SELF = "自结";//
	public static String INVBAS_WAY_CENTRAL = "统结";//
	public static String PK_CORP = "0001";//
	
	public static final boolean IS_DBILLDATE_WHEN_USE = true;
	
	public static final String[] TO_BILLITME_SORT= new String[]{"cbillbid"};
	
	public static final String PU_PACT_ITEM_TABLECODE = "pact";

	public static final String PLAN_VBILLSTATUS_SELT="自制";
	
	// 用户的当前及操作状态:卡片浏览,编辑,普通列表、毛利预估
	public static final int PO_STATE_BILL_BROWSE = 0;

	public static final int PO_STATE_BILL_EDIT = 1;

	public static final int PO_STATE_BILL_GROSS_EVALUATE = 3;

	public static final int PO_STATE_LIST_BROWSE = 2;


	//liuys add 2010-12-21 代储代销业务类型 业务类型定义 busitype 
	public static final String PI_INVOICE_BUSITYPE_EDIT = "CG02";

	public static final int PO_PACT_TABLECODE_INDEX = 4;
	
	public static final String DEFDOC_NAME="龙煤内部厂处";
	
	public static final String TO_FROWSTATUS = "7";
	public static final String IC_CBIZTYPE ="0001A11000000000CLQG"; //调拨出库单 业务类型PK  内销实拨
//	public static final String Balance_Flag_Deal = "deal";
//	public static final String Balance_Flag_UnDeal ="undeal";
	public static String PLAN_Balance_BILLTYPE = "BALANCE";//计划平衡
//	public static String PLAN_Balance_BILLTYPE1 = "BALANCE1";//计划平衡单据类型  模板使用 该模板没有 选中框
	public static String PLAN_Balance_ID = "zhw1aa10000000007JMP"; //查询对话框
	
	
	//lyf add 2011-02-21
	/** 供应商税率默认值 =17	 */
	public static final int DEFAULT_TAXRATE=17;



}
