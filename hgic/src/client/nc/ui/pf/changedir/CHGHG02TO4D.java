package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * 用于422XTO4D的VO的动态转换类。
 *
 * 创建日期：(2006-4-25)
 * @author：平台脚本生成
 */
public class CHGHG02TO4D extends nc.bs.pf.change.VOConversion {
/**
 * CHG422XTO4D 构造子注解。
 */
public CHGHG02TO4D() {
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
			"H_pk_corp->H_pk_corp",
			"H_vnote->H_vmemo",
			"H_dbilldate->SYSDATE",   
			"B_vfree5->B_vfree5",
			"B_vfree4->B_vfree4",
			"B_vfree3->B_vfree3",
			"B_vfree2->B_vfree2",
			"B_vfree1->B_vfree1",

			"H_pk_defdoc20->H_pk_defdoc20",
			"H_pk_defdoc19->H_pk_defdoc19",
			"H_pk_defdoc18->H_pk_defdoc18",
			"H_pk_defdoc17->H_pk_defdoc17",
			"H_pk_defdoc16->H_pk_defdoc16",
			"H_pk_defdoc15->H_pk_defdoc15",
			"H_pk_defdoc14->H_pk_defdoc14",
			"H_pk_defdoc13->H_pk_defdoc13",
			"H_pk_defdoc12->H_pk_defdoc12",
			"H_pk_defdoc11->H_pk_defdoc11",
			"H_pk_defdoc10->H_pk_defdoc10",
			"H_pk_defdoc9->H_pk_defdoc9",
			"H_pk_defdoc8->H_pk_defdoc8",
			"H_pk_defdoc7->H_pk_defdoc7",
			"H_pk_defdoc6->H_pk_defdoc6",
			"H_pk_defdoc5->H_pk_defdoc5",
			"H_pk_defdoc4->H_pk_defdoc4",
			"H_pk_defdoc3->H_pk_defdoc3",
			"H_pk_defdoc2->H_pk_defdoc2",
			"H_pk_defdoc1->H_pk_hdefdoc1",

			"H_vuserdef20->H_vdef20",
			"H_vuserdef19->H_vdef19",
			"H_vuserdef18->H_vdef18",
			"H_vuserdef17->H_vdef17",
			"H_vuserdef16->H_vdef16",
			"H_vuserdef15->H_vdef15",
			"H_vuserdef14->H_vdef14",
			"H_vuserdef13->H_vdef13",
			"H_vuserdef12->H_vdef12",
			"H_vuserdef11->H_vdef11",
			"H_vuserdef10->H_vdef10",
			"H_vuserdef9->H_vdef9",
			"H_vuserdef8->H_vdef8",
			"H_vuserdef7->H_vdef7",
			"H_vuserdef6->H_vdef6",
			"H_vuserdef5->H_vdef5",    
			"H_vuserdef4->H_vdef4",    
			"H_vuserdef3->H_vdef3",    
			"H_vuserdef2->H_vdef2",    
			"H_vuserdef1->H_vdef1",

			"B_vuserdef20->B_vdef20",
			"B_vuserdef19->B_vdef19",
			"B_vuserdef18->B_vdef18",
			"B_vuserdef17->B_vdef17",
			"B_vuserdef16->B_vdef16",
			"B_vuserdef15->B_vdef15",
			"B_vuserdef14->B_vdef14",
			"B_vuserdef13->B_vdef13",
			"B_vuserdef12->B_vdef12",
			"B_vuserdef11->B_vdef11",
			"B_vuserdef10->B_vdef10",
			"B_vuserdef9->B_vdef9",
			"B_vuserdef8->B_vdef8",
			"B_vuserdef7->B_vdef7",
			"B_vuserdef6->B_vdef6",
			"B_vuserdef5->B_vdef5",
			"B_vuserdef4->B_vdef4",
			"B_vuserdef3->B_vdef3",
			"B_vuserdef2->B_vdef2",
			"B_vuserdef1->B_vdef1",

			"B_pk_defdoc20->B_pk_defdoc20",
			"B_pk_defdoc19->B_pk_defdoc19",
			"B_pk_defdoc18->B_pk_defdoc18",
			"B_pk_defdoc17->B_pk_defdoc17",
			"B_pk_defdoc16->B_pk_defdoc16",
			"B_pk_defdoc15->B_pk_defdoc15",
			"B_pk_defdoc14->B_pk_defdoc14",
			"B_pk_defdoc13->B_pk_defdoc13",
			"B_pk_defdoc12->B_pk_defdoc12",
			"B_pk_defdoc11->B_pk_defdoc11",
			"B_pk_defdoc10->B_pk_defdoc10",
			"B_pk_defdoc9->B_pk_defdoc9",
			"B_pk_defdoc8->B_pk_defdoc8",
			"B_pk_defdoc7->B_pk_defdoc7",
			"B_pk_defdoc6->B_pk_defdoc6",
			"B_pk_defdoc5->B_pk_defdoc5",
			"B_pk_defdoc4->B_pk_defdoc4",
			"B_pk_defdoc3->B_pk_defdoc3",
			"B_pk_defdoc2->B_pk_defdoc2",
			"B_pk_defdoc1->B_pk_defdoc1",

			"B_cinventoryid->B_cinventoryid",
			"B_castunitid->B_castunitid",
			"B_csourcebillhid->B_pk_plan",
			"B_csourcebillbid->B_pk_planother_b",
			"B_vsourcebillcode->H_vbillno",
			"B_vsourcerowno->B_crowno",
			"B_hsl->B_hsl",
			"B_cfirstbillhid->B_pk_plan",
			"B_cfirstbillbid->B_pk_plan_b",
			"B_vfirstbillcode->H_vbillno",
			"B_vfirstrowno->B_crowno",

			"H_pk_calbody->B_creqcalbodyid",
			"H_cwarehouseid->B_creqwarehouseid",
			"B_csourceheadts->H_ts",
			"B_csourcebodyts->B_ts",
			
			"H_cdptid->H_capplydeptid",
			"H_cbizid->H_capplypsnid",

			// 刘智宇
			//		"B_cprojectid->B_cprojectid",
			//		"B_cprojectphaseid->B_cprojectphaseid",
			"B_vbatchcode->B_vbatchcode",
			"B_cinvbasid->B_pk_invbasdoc"
	};
}
/**
* 获得公式。
* @return java.lang.String[]
*/
public String[] getFormulas() {
	return new String[] {
		"H_cbilltypecode->\"4D\"",
		"B_nshouldoutnum->B_nnum-B_nouttotalnum",
		"B_nshouldoutassistnum->(B_nnum-B_nouttotalnum)*(B_nassistnum/B_nnum)",
		"B_csourcetype->\"HG02\"",
		"B_cfirsttype->\"HG02\"",
		"B_flargess->\"N\"",
		"B_isok->\"N\""
	};
}
/**
* 返回用户自定义函数。
*/
public UserDefineFunction[] getUserDefineFunction() {
	try {
		//第0个自定义函数
		UserDefineFunction func0 = new UserDefineFunction();
		func0.setClassName("nc.bs.arap.change.PubchangeBO");
		func0.setMethodName("getdjlx");
		func0.setReturnType(Class.forName("java.lang.String"));
		func0.setArgTypes(new Class[] { 
			Class.forName("java.lang.String"), 
			Class.forName("java.lang.String") 
			});
		func0.setArgNames(new String[] { 
			"&corp", 
			"&djlxbm" 
			});

		UserDefineFunction[] allFuncs = new UserDefineFunction[1];
		allFuncs[0] = func0;

		return allFuncs;

	} catch (ClassNotFoundException e) {
		nc.vo.scm.pub.SCMEnv.error(e);
	}
	return null;
}
}
