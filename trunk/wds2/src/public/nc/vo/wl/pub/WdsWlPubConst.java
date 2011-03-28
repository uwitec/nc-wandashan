package nc.vo.wl.pub;

public class WdsWlPubConst {
	/**lyf begin-------------------发运计划录入------------------------------------ begin*/
	 //1 功能节点
	public static String DM_PLAN_LURU_NODECODE="80060405";
	// 2. 单据类型
	public static String WDS1="WDS1";
    /**-----------下拉值---------*/
	public static int DM_PLAN_LURU_IPLANTYPE0=0;
	public static int DM_PLAN_LURU_IPLANTYPE1=1;
	/** ----------下拉值---------*/
	
	/**lyf end-------------------发运计划录入------------------------------------end */
	
	/**lyf begin--------------------发运订单-----------------------------------------begin*/
	 //1 功能节点
	public static String DM_ORDER_NODECODE="80060415";
	// 2. 单据类型
	public static String WDS3="WDS3";
	
	/**lyf end--------------------发运订单-----------------------------------------end*/

	/**lyf begin--------------------销售运单-----------------------------------------begin*/
	 //1 功能节点
	public static String DM_SO_ORDER_NODECODE="80060425";
	// 2. 单据类型
	public static String WDS5="WDS5";
	
	/**lyf end--------------------销售运单-----------------------------------------end*/

	/**zhf-----------------------发运计划安排---------------------------*/
	//发运计划安排功能节点号
	public static String DM_PLAN_DEAL_NODECODE = "80060410";
	public static String DM_PLAN_DEAL_BILLTYPE="WDS2";
	// 按钮tag
	public static String DM_PLANDEAL_BTNTAG_QRY = "查询";
	public static String DM_PLANDEAL_BTNTAG_DEAL = "安排";
	public static String DM_PLANDEAL_BTNTAG_SELALL = "全选";
	public static String DM_PLANDEAL_BTNTAG_SELNO = "全消";

	//zhf end
	
	public static String WDS_WL_MODULENAME = "wds2";
	public static String WDS_WL_ZC = "1021A91000000004YZ0P";//总仓是双城
	
	public static String[] DM_PLAN_DEAL_SPLIT_FIELDS = new String[]{"vbillno","pk_outwhouse","pk_inwhouse"};//"vbillno",

	public static String DM_PLAN_TO_ORDER_PUSHSAVE = "PUSHSAVE";
	public static String DM_PLAN_TO_ORDER_SAVE="SAVE";

	/**zhf-----------------------发运计划安排---------------------------*/
}
