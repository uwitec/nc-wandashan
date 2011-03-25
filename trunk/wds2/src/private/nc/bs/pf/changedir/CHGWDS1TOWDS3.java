package nc.bs.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * 用于发运计划  安排时 生成 发运订单 数据转换  
 *  注意  该处发运计划的表体使用的是 plandealvo  计划安排的vo  zhf
 *
 * 创建日期：(2004-11-18)
 * @author：平台脚本生成
 */
public class CHGWDS1TOWDS3 extends nc.bs.pf.change.VOConversion {
/**
 * CHG20TO21 构造子注解。
 */
public CHGWDS1TOWDS3() {
	super();
}
/**
* 获得后续类的全录经名称。
* @return java.lang.String[]
*/
public String getAfterClassName() {
	return null;
}
/**
* 获得另一个后续类的全录径名称。
* @return java.lang.String[]
*/
public String getOtherClassName() {
	return null;
}
/**
* 获得字段对应。
* @return java.lang.String[]
*/
public String[] getField() {
	return new String[] {
			//预留字段
			"H_reserve2->H_reserve2",
			"H_reserve1->H_reserve1", 
			"H_pk_fdsyzc_h->H_reserve1", 
			"H_reserve3->H_reserve3",
			"H_reserve4->H_reserve4",
			"H_reserve5->H_reserve5",
			"H_reserve6->H_reserve6",
			"H_reserve7->H_reserve7",
			"H_reserve8->H_reserve8",
			"H_reserve9->H_reserve9",
			"H_reserve10->H_reserve10",
			"H_reserve11->H_reserve11",
			"H_reserve12->H_reserve12",
			"H_reserve13->H_reserve13",
			"H_reserve14->H_reserve14",
			"H_reserve15->H_reserve15",
			"H_reserve16->H_reserve16",
			//自定义项
			"H_vdef4->H_vdef4",
			"H_vdef3->H_vdef3",
			"H_vdef2->H_vdef2",
			"H_vdef1->H_vdef1",
			"H_vdef5->H_vdef5",
			"H_vdef6->H_vdef6",
			"H_vdef7->H_vdef7",
			"H_vdef8->H_vdef8",
			"H_vdef9->H_vdef9",
			"H_vdef10->H_vdef10",
			"H_vdef11->H_vdef11",
			"H_vdef12->H_vdef12",
			"H_vdef13->H_vdef13",
			"H_vdef14->H_vdef14",
			"H_vdef15->H_vdef15",
			"H_vdef16->H_vdef16",
			"H_vdef17->H_vdef17",
			"H_vdef18->H_vdef18",
			"H_vdef19->H_vdef19",
			"H_vdef20->H_vdef20",
		};
}
/**
* 获得公式。
* @return java.lang.String[]
*/
public String[] getFormulas() {
	return new String[] {
		"H_cvendorbaseid->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,B_cvendormangid)",
		"H_forderstatus->int(0)",
		"H_dr->int(0)",
		"H_bislatest->\"Y\"",
		"H_bisreplenish->\"N\"",
		"H_nversion->int(1)",
		"H_cdeptid->getColValue(bd_psndoc,pk_deptdoc,pk_psndoc,H_cemployeeid)",
		"H_breturn->\"N\"",
		"H_bdeliver->\"N\"",
		"B_cupsourcebilltype->\"20\"",
		"B_iisactive->int(0)",
		"B_forderrowstatus->int(0)",
		"B_idiscounttaxtype->int(1)",
		"B_iisreplenish->int(0)",
		"B_ndiscountrate->int(100)",
		"B_dr->int(0)",
		"B_status->int(2)",
		"B_vreceiveaddress->getColValue(bd_stordoc,storaddr,pk_stordoc,B_cwarehouseid)",
		"B_breceiveplan->\"N\"",
		"B_blargess->\"N\""
	};
}
/**
* 返回用户自定义函数。
*/
public UserDefineFunction[] getUserDefineFunction() {
	return null;
}
}
