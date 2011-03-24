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
			"H_cbiztype->H_cbiztype",
			//V5删除："H_cstoreorganization->H_pk_reqstoorg",
			"H_dorderdate->SYSDATE",
			"H_pk_corp->B_pk_purcorp",//对照请购单的采购公司
			"H_cvendormangid->B_cvendormangid",
			"H_cpurorganization->B_cpurorganization",
			"H_coperator->SYSOPERATOR",
			"H_caccountyear->SYSACCOUNTYEAR",
			"H_vdef1->H_vdef1",
			"H_vdef2->H_vdef2",
			"H_vdef3->H_vdef3",
			"H_vdef4->H_vdef4",
			"H_vdef5->H_vdef5",
			"H_vdef6->H_vdef6",
			"H_vdef7->H_vdef7",
			"H_vdef8->H_vdef8",
			"H_vdef9->H_vdef9",
			"H_vdef10->H_vdef10",
			"H_vmemo->H_vmemo",
			"H_cemployeeid->B_cemployeeid",
			"H_creciever->H_ccustomerid",
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
			"H_pk_defdoc1->H_pk_defdoc1",
			"H_pk_defdoc2->H_pk_defdoc2",
			"H_pk_defdoc3->H_pk_defdoc3",
			"H_pk_defdoc4->H_pk_defdoc4",
			"H_pk_defdoc5->H_pk_defdoc5",
			"H_pk_defdoc6->H_pk_defdoc6",
			"H_pk_defdoc7->H_pk_defdoc7",
			"H_pk_defdoc8->H_pk_defdoc8",
			"H_pk_defdoc9->H_pk_defdoc9",
			"H_pk_defdoc10->H_pk_defdoc10",
			"H_pk_defdoc11->H_pk_defdoc11",
			"H_pk_defdoc12->H_pk_defdoc12",
			"H_pk_defdoc13->H_pk_defdoc13",
			"H_pk_defdoc14->H_pk_defdoc14",
			"H_pk_defdoc15->H_pk_defdoc15",
			"H_pk_defdoc16->H_pk_defdoc16",
			"H_pk_defdoc17->H_pk_defdoc17",
			"H_pk_defdoc18->H_pk_defdoc18",
			"H_pk_defdoc19->H_pk_defdoc19",
			"H_pk_defdoc20->H_pk_defdoc20",
			"B_vdef7->B_vdef7",
			"B_vdef8->B_vdef8",
			"B_vdef9->B_vdef9",
			"B_vdef10->B_vdef10",
			"B_vdef11->B_vdef11",
			"B_vdef12->B_vdef12",
			"B_vdef13->B_vdef13",
			"B_vdef14->B_vdef14",
			"B_vdef15->B_vdef15",
			"B_vdef16->B_vdef16",
			"B_vdef17->B_vdef17",
			"B_vdef18->B_vdef18",
			"B_vdef19->B_vdef19",
			"B_vdef20->B_vdef20",
			"B_pk_defdoc1->B_pk_defdoc1",
			"B_pk_defdoc2->B_pk_defdoc2",
			"B_pk_defdoc3->B_pk_defdoc3",
			"B_pk_defdoc4->B_pk_defdoc4",
			"B_pk_defdoc5->B_pk_defdoc5",
			"B_pk_defdoc6->B_pk_defdoc6",
			"B_pk_defdoc7->B_pk_defdoc7",
			"B_pk_defdoc8->B_pk_defdoc8",
			"B_pk_defdoc9->B_pk_defdoc9",
			"B_pk_defdoc10->B_pk_defdoc10",
			"B_pk_defdoc11->B_pk_defdoc11",
			"B_pk_defdoc12->B_pk_defdoc12",
			"B_pk_defdoc13->B_pk_defdoc13",
			"B_pk_defdoc14->B_pk_defdoc14",
			"B_pk_defdoc15->B_pk_defdoc15",
			"B_pk_defdoc16->B_pk_defdoc16",
			"B_pk_defdoc17->B_pk_defdoc17",
			"B_pk_defdoc18->B_pk_defdoc18",
			"B_pk_defdoc19->B_pk_defdoc19",
			"B_pk_defdoc20->B_pk_defdoc20",
			"B_cassistunit->B_cassistunit",
			"B_cbaseid->B_cbaseid",
			"B_cmangid->B_cmangid",
			"B_cprojectid->B_cprojectid",
			"B_cprojectphaseid->B_cprojectphaseid",
			"B_csourcebillid->B_csourcebillid",
			"B_csourcebilltype->B_csourcebilltype",
			"B_cupsourcebillid->B_cpraybillid",
			"B_cupsourcebillrowid->B_cpraybill_bid",
			"B_cwarehouseid->B_cwarehouseid",//此处默认对照需求仓库，VO后续处理类中有调整
			"B_pk_corp->B_pk_purcorp",
			"B_vfree1->B_vfree1",
			"B_vfree2->B_vfree2",
			"B_vfree3->B_vfree3",
			"B_vfree4->B_vfree4",
			"B_vfree5->B_vfree5",
			"B_vmemo->B_vmemo",
//			"B_cupsourcehts->H_ts",
			"B_cupsourcehts->B_chts",
			"B_cupsourcebts->B_ts",
			"B_nordernum->B_npraynum",
			"B_cvendormangid->B_cvendormangid",
			"B_cvendorbaseid->B_cvendorbaseid",
			"B_csourcerowid->B_csourcebillrowid",
			"B_vdef1->B_vdef1",
			"B_vdef2->B_vdef2",
			"B_vdef3->B_vdef3",
			"B_vdef4->B_vdef4",
			"B_vdef5->B_vdef5",
			"B_vdef6->B_vdef6",
			//V5删除字段："B_cstoreorganization->H_cstoreorganization",
			"B_cusedeptid->H_cdeptid",
			"B_vmemo->B_vmemo",
			"B_vproducenum->B_vproducenum",
			"B_nassistnum->B_nassistnum",
			"B_dplanarrvdate->B_ddemanddate",
			"B_nsuggestprice->B_nsuggestprice",
			//V5新增：
			"B_pk_reqcorp->B_pk_reqcorp",
			"B_pk_reqstoorg->B_pk_reqstoorg",
			"B_pk_creqwareid->B_cwarehouseid",
			//以下属性在此设置初始值，在VO对照后续类中还有处理
			"B_pk_upsrccorp->H_pk_corp",
			"B_pk_arrvcorp->B_pk_reqcorp",
			"B_pk_arrvstoorg->B_pk_reqstoorg",
			"B_pk_invoicecorp->B_pk_reqcorp"
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
