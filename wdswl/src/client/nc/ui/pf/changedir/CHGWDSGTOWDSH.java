package nc.ui.pf.changedir;
import nc.ui.pub.ClientEnvironment;
import nc.ui.wl.pub.LoginInforHelper;
import nc.vo.pf.change.UserDefineFunction;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * mlr
 * ������������������
 * 
 */
public class CHGWDSGTOWDSH extends nc.ui.pf.change.VOConversionUI {
/**
 * CHG20TO21 ������ע�⡣
 */
public CHGWDSGTOWDSH() {
	super();
}
/**
* ��ú������ȫ¼�����ơ�
* @return java.lang.String[]
*/
public String getAfterClassName() {
	return null;
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
			"H_pk_corp->H_vdef3",//������˾
			"H_pk_calbody->H_vdef2",// ��������֯
			"H_srl_pk->H_pk_outwhouse",// ����ֿ�	
			"H_pk_fcorp->B_vdef5",//���빫˾
			"H_pk_calbodyr->B_vdef7",//��������֯
			"H_srl_pkr->B_vdef9",//���ֿ�
			"H_creceiptcustomerid->B_vdef3",//�ջ���λ
			"H_vdiliveraddress->B_reserve1",// �ջ���ַ
			"H_vnote->H_vmemo",// ��ע		
//			"H_cbiztype->H_pk_busitype",// ҵ����������
//			"H_cdptid->H_pk_deptdoc",// ����
//			"H_cbizid->H_vemployeeid",// ҵ��Ա
//			"H_ccustomerid->H_pk_cumandoc",// �ͻ�
//			"H_pk_cubasdocc->H_pk_cubasdoc",// �ͻ�����id						
//			"H_vsourcebillcode->H_vbillno",// ��Դ���ݺ�
//			"H_csourcebillhid->H_pk_soorder",// ��Դ���ݱ�ͷ����			
			"B_csourcebillhid->B_pk_sendorder",// ��Դ���ݱ�ͷ����
			"B_csourcebillbid->B_pk_sendorder_b",// ��Դ���ݱ�������
			"B_vsourcebillcode->H_vbillno", // ��Դ���ݺ�
			"B_csourcetype->H_pk_billtype",// ��Դ��������			
			"B_cfirstbillhid->B_cfirstbillhid", // Դͷ���ݱ�ͷ���� (���ǵ�����������Ϣ)
			"B_cfirstbillbid->B_cfirstbillbid",// Դͷ���ݱ�������
			"B_cfirsttype->B_cfirsttype",//Դͷ��������
			"B_vfirstbillcode->B_vfirstbillcode",//Դͷ���ݺ�	
			"B_cinventoryid->B_pk_invmandoc",// �������
			"B_cinvbasid->B_pk_invbasdoc",//�������id
			"B_vuserdef9->B_vdef1",//���״̬
			"B_unitid->B_unit",//�� ������λ
			"B_castunitid->B_assunit",//��������λ
			"B_nhsl->B_hsl",
			"B_flargess->B_bisdate",// �Ƿ���Ʒ
			"B_vbatchcode->B_vdef2", //���κ�			
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
* �����û��Զ��庯����
*/
public UserDefineFunction[] getUserDefineFunction() {
	return null;
}
}
