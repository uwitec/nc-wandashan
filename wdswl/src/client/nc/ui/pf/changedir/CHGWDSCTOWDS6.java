package nc.ui.pf.changedir;

import nc.ui.pf.change.VOConversionUI;
import nc.ui.pub.ClientEnvironment;
import nc.ui.wl.pub.LoginInforHelper;
import nc.vo.pf.change.UserDefineFunction;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * 
 * @author Administrator
 *采购取样--其他出库
 */
public class CHGWDSCTOWDS6 extends VOConversionUI {
	/**
	 * CHG20TO21 构造子注解。
	 */
	public CHGWDSCTOWDS6() {
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
//				"H_vsourcebillcode->H_vbillno",
//				"H_voperatorid->H_voperatorid",
//				"H_srl_pkr->H_pk_inwhouse",//入库仓库
//				"H_vdiliveraddress->H_vinaddress",//收货地址
				"H_vnote->H_vmemo",			
				"B_csourcebillhid->B_pk_cgqy_h",
				"B_csourcebillbid->B_pk_cgqy_b",
				"B_vsourcebillcode->H_vbillno",
				"B_csourcetype->H_pk_billtype",
				
				"B_cfirstbillhid->B_cfirstbillhid",
				"B_cfirstbillbid->B_cfirstbillbid",
				"B_cfirsttyp->B_cfirsttype",				
				"B_vfirstbillcode->B_vfirstbillcode",
				
				"B_cinventoryid->B_pk_invmandoc",//
				"B_cinvbasid->B_pk_invbasdoc",//
				"B_unitid->B_cunitid",//主 计量单位
				"B_castunitid->B_cassunitid",//辅计量单位
				"B_hsl->B_nhsl",
				"B_vbatchcode->B_vbatchecode"
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
				"H_srl_pk->\""+whid+"\"",//出库仓库
				"H_pk_cargdoc->\""+spaceid+"\"",//货位
//				"H_cwhsmanagerid->\""+cl.getUser()+"\"",
				"H_tmaketime->\""+time+"\"",
			
				"H_dbilldate->\""+cl.getLogonDate()+"\"",
				"H_coperatorid->\""+cl.getUser()+"\"",
				"H_vbilltype->\""+WdsWlPubConst.BILLTYPE_OTHER_OUT+"\"",
				"H_is_yundan->\"Y\"",
				"H_pk_corp->\""+cl.getCorp()+"\"",
				"H_pk_calbody->\""+WdsWlPubConst.DEFAULT_CALBODY+"\"",
				"B_dbizdate->\""+cl.getLogonDate()+"\"",
				"B_nshouldoutnum->B_nplannum-B_noutnum",
				"B_nshouldoutassistnum->B_nassplannum-B_nassoutnum"	
		};
	}
	/**
	* 返回用户自定义函数。
	*/
	public UserDefineFunction[] getUserDefineFunction() {
		return null;
	}
}
