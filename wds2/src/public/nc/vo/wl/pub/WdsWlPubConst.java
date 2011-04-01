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
	
	/**发运订单出库类型 */
	public static String WDS3="WDS3";
	
	/**lyf end--------------------发运订单-----------------------------------------end*/

	/**lyf begin--------------------销售运单-----------------------------------------begin*/
	 //1 功能节点
	public static String DM_SO_ORDER_NODECODE="80060425";
	public static String WDS4 = "WDS4";
	public static String DM_SO_DEAL_NODECODE = "80060201";
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
	
	public static String WDS_WL_MODULENAME = "wdswl";
	public static String WDS_WL_ZC = "1021A91000000004YZ0P";//总仓是双城
	
	public static String[] DM_PLAN_DEAL_SPLIT_FIELDS = new String[]{"vbillno","pk_outwhouse","pk_inwhouse"};//"vbillno",

	public static String[] SO_PLAN_DEAL_SPLIT_FIELDS = new String[]{"vreceiptcode","cbodywarehouseid","ccustomerid"};//"vbillno",
	
	public static String DM_PLAN_TO_ORDER_PUSHSAVE = "PUSHSAVE";
	public static String DM_PLAN_TO_ORDER_SAVE="SAVE";
	
	public static String DM_SO_DEALNUM_FIELD_NAME = "ntaldcnum";//利用系统销售订单  已参与价保数量 作为  累计发运数量

	/**zhf-----------------------发运计划安排---------------------------*/
	
	/**其他出库单节点号 */
	public static String  OTHER_OUT_FUNCODE="8004040208";
	/**参照发运订单的节点标示 */
	public static String  OTHER_OUT_REFWDS3_NODECODE="8004040210";
	public static String DEFAULT_CALBODY = "1021B1100000000001JL";
	
	public static String BILLTYPE_OTHER_OUT = "WDS6";//其他出
	public static String BILLTYPE_OTHER_IN = "WDS7";//其他入
	public static String BILLTYPE_SALE_OUT = "WDS8";//销售出
	public static String BILLTYPE_ALLO_OUT = "WDS9";//调拨入
	public static String BILLTYPE_BACK_IN = "WDSA";//退货入
}
