package nc.ui.pf.changedir;

import nc.ui.pub.ClientEnvironment;
import nc.ui.wl.pub.LoginInforHelper;
import nc.vo.pf.change.UserDefineFunction;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * ���˶�������������
 * 
 */
public class CHGHWTZTOWDS6 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHG20TO21 ������ע�⡣
 */
public CHGHWTZTOWDS6() {
	super();
}
/**
* ��ú������ȫ¼�����ơ�
* @return java.lang.String[]
*/
public String getAfterClassName() {
	return "nc.vo.ic.other.out.ChangTOOtherOutVO";
}
/**
* �����һ���������ȫ¼�����ơ�
* @return java.lang.String[]
*/
public String getOtherClassName() {
	return null;
}
/**
* ����ֶζ�Ӧ��
* @return java.lang.String[]
*/
public String[] getField() {
	return new String[] {			
			"H_pk_corp->H_pk_corp",
//			"H_vsourcebillcode->H_vbillno",
//			"H_voperatorid->H_voperatorid",
			"H_srl_pk->H_srl_pk",//����ֿ�
			"H_srl_pkr->H_srl_pk",//���ֿ�
			"H_vdiliveraddress->H_vdiliveraddress",//�ջ���ַ
			"H_isxnap->H_isxnap",//�Ƿ�����Ƿ��
			"H_vnote->H_vnote",			
			
			"H_cdispatcherid->H_cdispatcherid",//�������
			"B_vbatchcode->B_vbatchcode",//����
			"B_pk_defdoc1->B_pk_cargdoc2",//�����λ���´�������ⵥ��
			"B_vuserdef9->B_vuserdef9",//���״̬
			"B_vuserdef7->B_vuserdef7",//��������
			
			"B_csourcebillhid->B_general_pk",
			"B_csourcebillbid->B_general_b_pk",
			"B_vsourcebillcode->H_vbillno",
			"B_csourcetype->H_pk_billtype",
			
			"B_cfirstbillhid->B_cfirstbillhid",
			"B_cfirstbillbid->B_cfirstbillbid",
			"B_cfirsttype->B_cfirsttype",
			
			"B_cinventoryid->B_cinventoryid",//
			"B_cinvbasid->B_cinvbasid",//
			"B_unitid->B_unitid",//�� ������λ
			"B_castunitid->B_castunitid",//��������λ
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
* ��ù�ʽ��
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
			"H_pk_cargdoc->\""+spaceid+"\"",//��λ
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
* �����û��Զ��庯����
*/
public UserDefineFunction[] getUserDefineFunction() {
	return null;
}
}
