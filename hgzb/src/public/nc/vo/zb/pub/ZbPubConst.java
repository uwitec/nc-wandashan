package nc.vo.zb.pub;

public class ZbPubConst {
	
	//鹤岗精度
	public static int grade_digit = 2;
	public static int NUM_DIGIT = 4;
	public static int PRICE_DIGIT = 6;
	public static int MNY_DIGIT = 2;

    /*计划管理*/
	public static String ZB_WEB_BILLTYPE = "SPWE";//网上报价
	public static String ZB_LOCAL_BILLTYPE = "SPLO";//现场报价
//	public static String ZB_SUBMITPRICE_BILLTYPE = "SPBI";//报价单
	public static String ZB_BIDDING_BILLTYPE = "ZB01";//标书
	public static String ZB_BIDDING_BILLTYPE_REF = "ZB0101";
	public static String ZB_EVALUATION_BILLTYPE = "ZB02";//中标评审表
	public static String ZB_BILL_BILLTYPE_VENDOR = "ZB03";//评标管理 vendor
	public static String ZB_BILL_BILLTYPE_INV = "ZB04";//评标管理 inv
	public static String ZB_FLGN_BILLTYPE = "FLMB";//分量功能模板
	public static String ZB_Result_BILLTYPE = "ZB05";//中标结果录入
	public static String ZB_BIDFLOOR_BILLTYPE = "ZB06";//标底价维护
	public static String ZB_PARAMSET_BILLTYPE = "PSET";//报价参数设置
	public static String ZB_AVNUM_BILLTYPE = "ZB07";//	分量调整单
	public static String ZB_CUSTBAS_BILLTYPE = "ZB10";//供应商注册
	
	public static String ZB_PRICE_GRADE = "ZB08";//报价分维护
	public static String ZB_SUBMIT_BILL = "ZB09";//报价单
	public static String ZB_SUBMIT_VIEW = "ZB11";//查看报价信息
	public static String ZB_SUBMIT_VIEW_FUNDCODE ="4004090420";//报价信息节点编号
	public static String ZB_BIDVIEW_BILLTYPE = "ZB12";//标书浏览
	
	
	public static String zb_historyprice = "history";//标书供货历史信息查看模板
	public static String zb_view = "view";//标底价维护的查看明细模板
	public static String zb_flmb = "flmb";//中标审批表的分量模板
	
	
//	标书业务状态     0 --初始  1--投标  2--评标  3--中标  4--完成 5--流标
	public static int BIDDING_BUSINESS_STATUE_INIT = 0;
	public static int BIDDING_BUSINESS_STATUE_SUBMIT = 1;
	public static int BIDDING_BUSINESS_STATUE_BILL = 2;
	public static int BIDDING_BUSINESS_STATUE_RESULT = 3;
	public static int BIDDING_BUSINESS_STATUE_CLOSE = 4;
	public static int BIDDING_BUSINESS_STATUE_MISS = 5;
	
	
	public static String YEAR_PLAN_BILLTYPE = "HG03";
	
	public static boolean div_bidding_by_inv = true;//根据品种划分标段 还是根据历史供应商划分标段
	
	/**
	 * 议标数据准备
	 */
	public static String ADJ_BID_LEFT_TITLE = "标段";
	public static String ADJ_BID_RIGHT_TITLE = "标段";
	
	/**
	 * 议标数据准备
	 */
	public static String PRE_BID_LEFT_TITLE = "待评标标段";
	public static String PRE_BID_RIGHT_TITLE = "评标阶段";

	public static String TREE_ROOT_TAG = "root";
	
//	 * btntag
	 public static String BTN_TAB_COMMIT = "提报";
	 public static String BTN_TAB_CANCEL = "撤销";
	 public static String BTN_TAB_REFRESH = "刷新";
	 public static String BTN_TAB_FOLLOW = "跟标";
	 
	 public static int WEB_SUBMIT_PRICE = 0;//招标类型---网上招标
	 public static int LOCAL_SUBMIT_PRICE = 1;//招标类型---现场招标
	 public static int SELF_SUBMIT_PRICE = 2;//招标类型---手工业务员录入
	 public static int BAD_SUBMIT_PRICE = 3;//恶意报价
	 
	 
	 public static final String[] TIME_ASC_SORT_FILEDS = new String[]{"",""};
	 public static final long TIME_DIFFERRENCE = 0;//定时服务允许的时间偏差  ms
	 
	 public static final String[] SUBMIT_PRICE_UPDATE_FIELD = new String[]{"nprice","cmodifyid","tmodifytime"};
	 
	 public static String Evaluation_TableCode1 = "zb_evaluation_b";//中标评审表子表业签
	 public static String DATE_CAL_DLG_TEMP_ID ="0001A11000000000XOMU";//分量功能模板ID
	 public static String VIEW_DETAIL_DLG_TEMP_ID ="0001A110000000010YBJ";//查看明细模板ID
	 
	 public static String pk_currtype ="00010000000000000001";//币种主键
	 public final static int IZBRESULTTYPE = 2;
	 public static String GENORDER_BILLTYPE = "ORDE";//生成合同虚拟单据类型 
	 public static String GENORDER_MODLUECODE = "4004090603";//生成合同虚拟单据类型
	 
	 public static final String[] DEAL_SORT_FIELDNAMES = new String[] {"vbillno", "invcode" };
	 public static final String[] VIEW_SORT_FIELDNAMES = new String[] {"custcode","vname" };
	 public static final String[] SUB_SORT_FIELDNAMES = new String[] {"vbillno","custcode","invcode","vname" };
	 public static String GENORDER_DIALOG_ID = "0001A110000000010M44";//生成合同对话ID
	 public static String LOAD_DIALOG_ID = "9991A110000000011H0O";//加载评审表对话框
	 
	 public static int inv_class_coderule = 2;//存货分类编码规则  2位
	 
	 public static int bidding_inv_unique_time = 60;//单位  天：一个月范围内不能重复招标一个品种
	 //po_order_b  NACCUMDAYPLNUM NUMBER(20,8) CUSERID CHAR(20)
	 public static boolean comment_split_num_flag = true;//true  按招标数量  分  false 按招标总金额分
	 
	 public static String corp = "1002";//供应处 参数设置
	 
	 public static String ZB_TYPE_VENDOR="VEND";//供应商汇总
	 public static String ZB_TYPE_CORP="CORP";//公司+供应商汇总
	 public static String ZB_TYPE_DETAIL="DETA";//品种明细
	 public static String ZB_TYPE_ZBDETAIL="ZBDE";//品种明细
	 public static String ZB_TYPE_ZBBID="ZBBI";//分标段供应商中标
	 
	 
}
