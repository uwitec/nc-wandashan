package nc.vo.wl.pub;

public class WdsWlPubConst {
	
	
	public static String WDS_WL_MODULENAME = "wds";
	/**
	 *库存状态表大日期状态主键
	 */
	public static String WDS_STORSTATE_PK="1021S31000000009FS9A";
	/**
	 *库存状态表合格状态主键
	 */
	public static String WDS_STORSTATE_PK_hg="1021S31000000009FS98";
	/**
	 *库存状态表待检状态主键
	 */
	public static String WDS_STORSTATE_PK_dj="0001AA100000000BJINJ";
	/**发运计划录入节点号 */
	public static String DM_PLAN_LURU_NODECODE="80060405";
	/**发运计划处理节点号  */
	public static String DM_PLAN_DEAL_NODECODE = "80060410";
	/**发运订单节点号  */
	public static String DM_ORDER_NODECODE="80060415";
	/**其他出库单节点号 */
	public static String  OTHER_OUT_FUNCODE="8004040208";
	/**参照发运订单的节点标示 */
   public static String  OTHER_OUT_REFWDS3_NODECODE="800404028WDS3";
   /**参照发运订单的节点标示 */
   public static String  OTHER_OUT_REFWDSC_NODECODE="800404028WDSC";
	/**销售运单节点号 */
	public static String DM_SO_ORDER_NODECODE="80060425";
	/**销售订单安排节点号 */
	public static String DM_SO_DEAL_NODECODE = "80060201";
	
	/**销售出库单节点号 */
	public static String  SO_OUT_FUNCODE="8004040204";
	/**参照销售运单的NODEKEY */
	public static String  SO_OUT_REFWDS5_NODECODE="80060425";
	/**参照红字销售订单的NODEKEY */
	public static String  SO_OUT_REF30_NODECODE="800404020430";
	/**其他入库 节点号*/
	public static String  IC_OTHER_IN_NODECODE= "8004040214";
	/**其他入库参照 [供应链] 其他出库 参照查询 NODEKEY  **/
	public static String  IC_OTHER_IN_REF4I_NODECODE = "80040402144I";
	/**其他入库参照  其他出库 参照查询 NODEKEY  **/
	public static String  IC_OTHER_IN_REFWDS7_NODECODE = "8004040214WDS7";
	/**其他入库 货位调整 参照模板节点标示*/
	public static String  IC_OTHER_IN_REFHWTZ_NODECODE= "02140288";
	/**其他入库 运单确认 参照模板节点标示*/
	public static String  IC_OTHER_IN_REFFYDJ_NODECODE= "2011040801";
//	/**运单确认 节点号 */
//	public static String IC_TRANS_CONFIRM_NODECOED="80060210";
	/**采购取样 节点号*/
	public static String IE_CGQY_NODECODE="80021040";
	/**托盘移动 节点号*/
	public static String IC_TPYD_NODECODE="8004040212";
	/** 货位存货 节点号*/
	public static String INVSTORE_NODECODE="80040602";
	/** 调拨入库 节点号*/
	public static String IC_TRANSIN_NODECODE="8004040210";
	/** 调拨入库参照ERP调拨出库 节点标示*/
	public static String IC_TRANSIN_REF4Y_NODECODE="80040402104Y";	
	/** 发运存货档案*/
	public static String DM_PLAN_BASDOC_NODECODE="80060801";
	/** 分仓承运商绑定*/
	public static String DM_STORE_TRANSCORP_NODECODE="8006080202";	
	/** 学生成绩*/
	public static String LM_CHENGJI_NODECODE="802005";
	/**
	   单据类型
	 */
	public static String LM_CHENGJI_BILLTYPE="CHJI";
	/**
	/**班组档案*/
    public static String LOAD_TEAM_DOC="8008010101";
   /**装卸费价格设置*/
   public static String  LOAD_SET_PRICE="8008010101";
	/** 装卸费核算单*/
	public static String LOAD_ACCOUNT="80080201";
	/** 装卸费核算单 参照其他出库节点标示*/
	public static String LOAD_ACCOUNT_REFWDS6="80080201WDS6";
	/** 装卸费核算单 参照其他入库节点标示*/
	public static String LOAD_ACCOUNT_REFWDS7="80080201WDS7";
	/** 装卸费核算单 参照销售出库节点标示*/
	public static String LOAD_ACCOUNT_REFWDS8="80080201WDS8";
	/** 装卸费核算单 参照调拨人库节点标示*/
	public static String LOAD_ACCOUNT_REFWDS9="80080201WDS9";
	
	
	

	/** 运输里程表档案 节点号*/
	public static String TRANS_MIL_NODECODE="8008010201";
	/**特殊运价表 节点号*/
	public static String TRANS_SPECPRICE_NODECODE = "8008010216";
	/** 运输公司档案*/
	public static String TRANS_CORP_NODECODE="8008010202";
	/**特殊业务档案节点号 */
	public static String TRANS_SPECBUSI_NODECODE = "800801022001";
	/**特殊业务运价表 节点号 */
	public static String TRANS_SPECBUSIPRICE_NODECODE = "800801022002";
	/**折合标准单据模板类型 */
	public static String ZHBZ ="ZHBZ";
	/**车辆档案节点号 */
	public static String TRANS_CARDOC_NODECODE = "8008010203";
	/**车型档案节点号 */
	public static String TRANS_CARTYPE_NODECODE = "8008010204";
	/**费用核算单节点号 */
	public static String TRANS_PRICE_NODECODE = "80080202";
	/**费用核算单 参照发运订单节点标示 */
	public static String TRANS_PRICE_NODECODEWDS3 = "REFWDS3";
	/**费用核算单 参照销售运单订单节点标示 */
	public static String TRANS_PRICE_NODECODEWDS5 = "REFWDS5";
	/** 客户公司图章 节点号 */
	public static String DM_CUST_CORPSEAL="80060804";
	
	
	/**特殊费用核算单节点号 */
	public static String TRANS_SEPCLPRICE_NODECODE = "80080203";
	/**库存实时状态--报表*/
	public static String  REPORT02="80100202";
	/**原料粉收发存汇总表--报表*/
	public static String  REPORT04="80100204";
	/**非正常箱粉报表--报表*/
	public static String  REPORT06="80100205";
	/**出入库流水账--报表*/
	public static String  REPORT08="80100208";
	/**物流箱粉各仓库单品库存明细--报表*/
	public static String  REPORT10="80100210";
	/**物流箱粉总库存--报表*/
	public static String  REPORT15="80100215";
	/**运费月汇总表--报表*/
	public static String  REPORT11="80080501";
	/**运费明细表--报表*/
	public static String  REPORT12="80080503";
	/**装卸费月汇总表--报表*/
	public static String  REPORT13="80080502";
	/**装卸运费明细表--报表*/
	public static String  REPORT14="80080504";
	/**箱粉库存明细--报表*/
	public static String  REPORT16="80100211";
	/**箱粉库存明细--报表*/
	public static String  REPORT17="80100212";
	
	public static String report_unusenum_node = "80061010";//yf add发运管理-报表统计-可用量查询报表
	
	public static String pk_cargdoc_30 = "1021S3100000000AGE95";//yf add存货绑定货位 分捡仓  货位编码30的pk值,注意更换帐套要重新获取bd_cargdoc  
	
	public static String def_soorder_30 = "reserve14";//yf add 销售运单 启动标识 《是否指定分拣仓出库》
	
	public static String IC_INV_SALE_ALERT_DAYNO = "def16";//存货销售警戒天数字段
	/**虚拟托盘 开头命名方式*/
	public static String XN_CARGDOC_TRAY_NAME="XN";
	/**虚拟托盘 默认存货的容量*/
	public static Integer XN_CARGDOC_TRAY_VO=100000000;
	
	/**默认回写ERP的批次*/
	public static String ERP_BANCHCODE="2009";
	
	public static String[] out_split_names = new String[]{"vbatchcode"};
	
    /**-----------下拉值---------*/
	public static int DM_PLAN_LURU_IPLANTYPE0=0;
	public static int DM_PLAN_LURU_IPLANTYPE1=1;
	/** ----------下拉值---------*/

	// 按钮tag
	public static String DM_PLANDEAL_BTNTAG_QRY = "查询";
	public static String DM_PLANDEAL_BTNTAG_DEAL = "安排";
	public static String DM_PLANDEAL_BTNTAG_SELALL = "全选";
	public static String DM_PLANDEAL_BTNTAG_SELNO = "全消";
	public static String DM_PLANDEAL_BTNTAG_XNDEAL="模拟安排";

	public static String WDS_WL_ZC = "1021A91000000004YZ0P";//总仓是双城------------select * from BD_STORDOC aa where aa.PK_STORDOC ='1021A91000000004YZ0P' 
	
	public static String[] DM_PLAN_DEAL_SPLIT_FIELDS = new String[]{"vbillno","pk_outwhouse","pk_inwhouse"};//"vbillno",

	public static String[] SO_PLAN_DEAL_SPLIT_FIELDS = new String[]{"cbodywarehouseid","ccustomerid"};//"vbillno",
	
	public static String DM_PLAN_TO_ORDER_PUSHSAVE = "PUSHSAVE";
	public static String DM_PLAN_TO_ORDER_SAVE="SAVE";
	
	public static String DM_SO_DEALNUM_FIELD_NAME = "ntaldcnum";//利用系统销售订单  已参与价保数量 作为  累计发运数量

	public static String DEFAULT_CALBODY = "1021B1100000000001JL";//默认库存组织---------------------select * from BD_CALBODY where PK_CALBODY ='1021B1100000000001JL'
	
	/**发运计划录入 */
	public static String WDS1="WDS1";
	/**发运计划安排 */
	public static String DM_PLAN_DEAL_BILLTYPE="WDS2";
	/**发运订单 */
	public static String WDS3="WDS3";
	/**销售运单安排 */
	public static String WDS4 = "WDS4";//销售计划安排  原版
	public static String WDS4_2 = "WDS42";//销售计划安排2
	public static String WDS4_2_1 = "cust";//销售计划安排2
	public static String WDS4_2_2 = "deal";//销售计划安排2
	public static String WDS30 = "30";//销售订单
	/**销售运单 */
	public static String WDS5="WDS5";
	/**其他出库 */
	public static String BILLTYPE_OTHER_OUT = "WDS6";
	/**其他入库*/
	public static String BILLTYPE_OTHER_IN = "WDS7";
	/** 销售出库 */
	public static String BILLTYPE_SALE_OUT = "WDS8";
	/** 调拨入库*/
	public static String BILLTYPE_ALLO_IN = "WDS9";
	/** 供应链调拨出库 */
	public static String GYL4Y = "4Y";
	/** 供应链调拨订单 */
	public static String GYL5D = "5D";
	/** 供应链调拨入库 */
	public static String GYL4E = "4E";
	
//	/** 退货入库*/
//	public static String BILLTYPE_BACK_IN = "WDSA";
	/**运输确认单*/
	public static String BILLTYPE_SEND_CONFIRM = "WDSB";
	/**库存状态基础信息 */
	public static String BILLTYPE_STORE_STATE="80040606";
	/**货位托盘 */
	public static String BILLTYPE_CARG_TARY="8004061002";
	/**仓库人员绑定*/
	public static String BILLTYPE_IE_STOR_PERSONS="80021008";
	/**存货明细*/
	public static String BILLTYPE_IE_STOR_DETAIL="80021010";
	/**存货档案*/
	public static String BILLTYPE_IC_INV_DOC="8004061008";		
	/**存货状态*/
	public static String BILLTYPE_IC_INV_STATUS="8004040602";
	/**采购取样 */
	public static String BILLTYPE_LM_CLASSINFOR="802003";
	public static String WDSC="WDSC";

	/**托盘移动 */
	public static String WDSD="WDSD";
	/** 货位存货*/
	public static String INVSTORE="80040602";
	/**装卸费价格 */
	public static String WDSE="WDSE";
	/**装卸费结算 */
	public static String WDSF="WDSF";
	/**价格区间定义 */
	public static String WDSH="WDSH";
	/**特殊业务档案*/
    public static String BILLTYPE_SPEC_BUSI_DOC="8008010220";	
    /**特殊业务运价表*/
	public static final String BILLTYPE_SPEC_BUSIPRICE_DOC = "8008010221";
	
	/**吨公里运价表 */
	public static String WDSI="WDSI";

	/**箱数运价表 */
	public static String WDSJ="WDSJ";
	
	/**分仓运价表 */
	public static String WDSK="WDSK";
	
	/**特殊业务运价表 */
	public static String WDSL="WDSL";
	/**运费核算单 */
	public static String WDSM="WDSM";	
	/**特殊运费核算单 */
	public static String WDSN="WDSN";	
	/**销售出库回传 */
	public static String WDSO="WDSO";
	/**调拨入库回传 */
	public static String WDSP="WDSP";
	
	//---------------仓储管理中涉及的 几个对话框注册模板类型
//	出库单据  出库托盘指定
	public static String DLG_OUT_TRAY_APPOINT = "OTA";
//	入库单据  入库托盘指定
	public static String DLG_IN_TRAY_APPOINT = "ITA";
//	发运订单 ，销售运单 安排
	public static String XNAP = "XNAP";
//	zhd   为打印定义的单据模板
//	调拨入库单 
	public static String PRINT_BILL_TEMPLET = "0001S3100000000KPO7G";
	public static String default_inv_state = "1021S31000000009FS98";//入库时设置默认库存状态=====+++++select * from tb_stockstate
	//wds模块日志记录
	public static String wds_logger_name = "wds";
//	销售安排时  过滤 客户的最小发货量  是辅计量 还是  主计量
	public static boolean sale_send_isass = true; 
//	zhf   仓库档案自定义项2   转分仓入库时   实际入数量 低于 实际出 数量  是否自动 调整 补录
	public static String wds_warehouse_sytz = "def2";
	
//	zhf 存货状态 浏览绑定关系 对话框 模板类型
	public static String INV_VIEW_LOCK_TEMPLET_TYPE = "lock";
	
//	库存状态  清理 查询对话框  模板ID
	public static String INV_STATE_QRY_TEMPLET_ID = "0001S3100000000L231C";
	
	
	//mlr 完达山物流报表  开发功能节点
	public static String report1="80100201";//各仓产品库存 产品明细表
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
