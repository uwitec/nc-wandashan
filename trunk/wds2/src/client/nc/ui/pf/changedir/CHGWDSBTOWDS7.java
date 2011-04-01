package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * 其他入库单参照运输确认单生成 zhf
 *
 * 创建日期：(2004-11-18)
 * @author：平台脚本生成
 */
public class CHGWDSBTOWDS7 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHG20TO21 构造子注解。
 */
public CHGWDSBTOWDS7() {
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
			"H_geh_vbillcode->H_fyd_ddh",//来源单据号 --运输确认单单据号
			"H_geh_cgeneralhid->H_fyd_pk",//来源单据头id
			"H_geh_cotherwhid->H_srl_pk",//出库仓库
			"H_geh_cbizid->H_pk_psndoc",//业务员
			"H_geh_cwarehouseid->H_srl_pkr",//入库仓库
			"H_geh_cbiztype->H_pk_busitype",//业务类型
//			"H_copetadate->SYSOPERATOR",
//			"H_geh_dbilldate->",
			"B_geb_cinvbasid->B_pk_invbasdoc",
			"B_geb_cgeneralhid->B_fyd_pk",//来源运单id
			"B_geb_cgeneralbid->B_cfd_pk",//来源体id
			"B_geb_vbatchcode->B_cfd_lpc",//批次号
			"B_geb_flargess->B_blargessflag",//是否赠品
			"B_geb_bsnum->B_cfd_sffsl",// 应收辅数量
			"B_geb_snum->B_cfd_sfsl",//应收主数量
			"B_crowno->B_crowno"
	};
}
/**
* 获得公式。
* @return java.lang.String[]
*/
public String[] getFormulas() {
	return new String[] {
			
			"H_geh_billtype->\"WDS7\"",
//			"";
//			
//		"H_cvendorbaseid->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,B_cvendormangid)",
//		"H_forderstatus->0",
//		"H_dr->0",
//		"H_bislatest->\"Y\"",
//		"H_bisreplenish->\"N\"",
//		"H_nversion->1",
//		"H_cdeptid->getColValue(bd_psndoc,pk_deptdoc,pk_psndoc,H_cemployeeid)",
//		"H_breturn->\"N\"",
//		"H_bdeliver->\"N\"",
//		"B_cupsourcebilltype->\"20\"",
//		"B_iisactive->0",
//		"B_forderrowstatus->0",
//		"B_idiscounttaxtype->1",
//		"B_iisreplenish->0",
//		"B_ndiscountrate->100",
//		"B_dr->0",
//		"B_status->2",
//		"B_vreceiveaddress->getColValue(bd_stordoc,storaddr,pk_stordoc,B_cwarehouseid)",
//		"B_breceiveplan->\"N\"",
//		"B_blargess->\"N\""
	};
}
/**
* 返回用户自定义函数。
*/
public UserDefineFunction[] getUserDefineFunction() {
	return null;
}
}
