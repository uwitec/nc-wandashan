package nc.ui.pf.changedir;
import nc.ui.pub.ClientEnvironment;
import nc.ui.wl.pub.LoginInforHelper;
import nc.vo.pf.change.UserDefineFunction;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * mlr
 * 调出订单－其它出库
 * 
 */
public class CHGWDSGTOWDSH extends nc.ui.pf.change.VOConversionUI {
/**
 * CHG20TO21 构造子注解。
 */
public CHGWDSGTOWDSH() {
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
			"H_pk_corp->H_vdef3",//调出公司
			"H_pk_calbody->H_vdef2",// 出库库存组织
			"H_srl_pk->H_pk_outwhouse",// 出库仓库	
			"H_pk_fcorp->B_vdef5",//调入公司
			"H_pk_calbodyr->B_vdef7",//调入库存组织
			"H_srl_pkr->B_vdef9",//入库仓库
			"H_creceiptcustomerid->B_vdef3",//收货单位
			"H_vdiliveraddress->B_reserve1",// 收货地址
			"H_vnote->H_vmemo",// 备注		
//			"H_cbiztype->H_pk_busitype",// 业务类型主键
//			"H_cdptid->H_pk_deptdoc",// 部门
//			"H_cbizid->H_vemployeeid",// 业务员
//			"H_ccustomerid->H_pk_cumandoc",// 客户
//			"H_pk_cubasdocc->H_pk_cubasdoc",// 客户基本id						
//			"H_vsourcebillcode->H_vbillno",// 来源单据号
//			"H_csourcebillhid->H_pk_soorder",// 来源单据表头主键			
			"B_csourcebillhid->B_pk_sendorder",// 来源单据表头主键
			"B_csourcebillbid->B_pk_sendorder_b",// 来源单据表体主键
			"B_vsourcebillcode->H_vbillno", // 来源单据号
			"B_csourcetype->H_pk_billtype",// 来源单据类型			
			"B_cfirstbillhid->B_cfirstbillhid", // 源头单据表头主键 (都是调拨订单的信息)
			"B_cfirstbillbid->B_cfirstbillbid",// 源头单据表体主键
			"B_cfirsttype->B_cfirsttype",//源头单据类型
			"B_vfirstbillcode->B_vfirstbillcode",//源头单据号	
			"B_cinventoryid->B_pk_invmandoc",// 存货主键
			"B_cinvbasid->B_pk_invbasdoc",//存货基本id
			"B_vuserdef9->B_vdef1",//存货状态
			"B_unitid->B_unit",//主 计量单位
			"B_castunitid->B_assunit",//辅计量单位
			"B_nhsl->B_hsl",
			"B_flargess->B_bisdate",// 是否赠品
			"B_vbatchcode->B_vdef2", //批次号			
	};
}
private LoginInforHelper helper = null;
public LoginInforHelper getLoginInforHelper(){
	if(helper == null){
		helper = new LoginInforHelper();
	}
	return helper;
}
/**
* 获得公式。
* @return java.lang.String[]
*/
public String[] getFormulas() {
	ClientLink cl = new ClientLink(ClientEnvironment.getInstance());
	UFDateTime time = new UFDateTime(System.currentTimeMillis());
	String whid=null;
	String spaceid =null;
	try {
		whid = getLoginInforHelper().getLogInfor(cl.getUser()).getWhid();
		spaceid = getLoginInforHelper().getLogInfor(cl.getUser()).getSpaceid();
	} catch (Exception e) {
		e.printStackTrace();
	}
	return new String[] {	
			"H_pk_cargdoc->\""+spaceid+"\"",//货位
			"H_tmaketime->\""+time+"\"",
			"H_dbilldate->\""+cl.getLogonDate()+"\"",
			"H_coperatorid->\""+cl.getUser()+"\"",
			"H_vbilltype->\""+WdsWlPubConst.BILLTYPE_OTHER_OUT+"\"",
			"H_is_yundan->\"Y\"",
			"H_pk_corp->\""+cl.getCorp()+"\"",
			"B_dbizdate->\""+cl.getLogonDate()+"\"",
			"B_nshouldoutnum->B_ndealnum-B_noutnum",
			"B_nshouldoutassistnum->B_nassdealnum-B_nassoutnum" 
	};
}
/**
* 返回用户自定义函数。
*/
public UserDefineFunction[] getUserDefineFunction() {
	return null;
}
}
