package nc.vo.wl.pub;

public class WdsWlPubConsts {

	
	/**发运订单节点号 */
	public static String DM_ORDER_NODECODE="80060415";	
	/**发运订单出库类型 */
	public static String WDS3="WDS3";	

	/**其他出库单节点号 */
	public static String  OTHER_OUT_FUNCODE="8004040208";
	/**参照发运订单的节点标示 */
	public static String  OTHER_OUT_REFWDS3_NODECODE="8004040210";
	/**总仓的id */
	public static String WDS_WL_ZC = "1021A91000000004YZ0P";//总仓是双城
	/**销售运单节点号 */
	public static String DM_SO_ORDER_NODECODE="80060425";	
	/**销售运单出库类型 */
	public static String WDS5="WDS5";

	
	/**捡货弹出窗口节点标示 */
	public static String  AUTO_CHECK_INV_NODEKEY="8004040208";
	
	public static String WDS_MODULENAME="wdswl";
	
	public static String[] stockinvonhand_fieldnames = new String[]{"whs_stockpieces","whs_stocktonnage","whs_status"};
//	8004040208
//	80060425

}
