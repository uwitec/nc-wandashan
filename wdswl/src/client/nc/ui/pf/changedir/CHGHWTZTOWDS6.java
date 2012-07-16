package nc.ui.pf.changedir;

import nc.ui.pub.ClientEnvironment;
import nc.ui.wl.pub.LoginInforHelper;
import nc.vo.pf.change.UserDefineFunction;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * 发运订单－其它出库
 * 
 */
public class CHGHWTZTOWDS6 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHG20TO21 构造子注解。
 */
public CHGHWTZTOWDS6() {
	super();
}
/**
* 获得后续类的全录经名称。
* @return java.lang.String[]
*/
public String getAfterClassName() {
	return "nc.vo.ic.other.out.ChangTOOtherOutVO";
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
//			"H_vsourcebillcode->H_vbillno",
//			"H_voperatorid->H_voperatorid",
			"H_srl_pk->H_srl_pk",//出库仓库
			"H_srl_pkr->H_srl_pk",//入库仓库
			"H_vdiliveraddress->H_vdiliveraddress",//收货地址
			"H_isxnap->H_isxnap",//是否虚拟欠发
			"H_vnote->H_vnote",			
			
			"H_cdispatcherid->H_cdispatcherid",//出库类别
			"B_vbatchcode->B_vbatchcode",//批次
			"B_pk_defdoc1->B_pk_cargdoc2",//调入货位，下传其他入库单用
			"B_vuserdef9->B_vuserdef9",//存货状态
			"B_vuserdef7->B_vuserdef7",//生成日期
			
			"B_csourcebillhid->B_general_pk",
			"B_csourcebillbid->B_general_b_pk",
			"B_vsourcebillcode->H_vbillno",
			"B_csourcetype->H_pk_billtype",
			
			"B_cfirstbillhid->B_cfirstbillhid",
			"B_cfirstbillbid->B_cfirstbillbid",
			"B_cfirsttype->B_cfirsttype",
			
			"B_cinventoryid->B_cinventoryid",//
			"B_cinvbasid->B_cinvbasid",//
			"B_unitid->B_unitid",//主 计量单位
			"B_castunitid->B_castunitid",//辅计量单位
			"B_nhsl->B_nhsl",
			
			"B_nshouldoutnum->B_nshouldoutnum",
			"B_nshouldoutassistnum->B_nshouldoutassistnum",
			"B_noutnum->B_noutnum",
			"B_noutassistnum->B_noutassistnum",
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
//			"H_cwhsmanagerid->\""+cl.getUser()+"\"",
			"H_tmaketime->\""+time+"\"",
			"H_dbilldate->\""+cl.getLogonDate()+"\"",
			"H_coperatorid->\""+cl.getUser()+"\"",
			"H_vbilltype->\""+WdsWlPubConst.BILLTYPE_OTHER_OUT+"\"",
			"H_is_yundan->\"Y\"",
			"H_pk_corp->\""+cl.getCorp()+"\"",
			"H_pk_calbody->\""+WdsWlPubConst.DEFAULT_CALBODY+"\"",
			"B_dbizdate->\""+cl.getLogonDate()+"\"",
	};
}
/**
* 返回用户自定义函数。
*/
public UserDefineFunction[] getUserDefineFunction() {
	return null;
}
}
