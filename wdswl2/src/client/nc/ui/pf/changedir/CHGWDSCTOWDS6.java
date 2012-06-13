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
 *�ɹ�ȡ��--��������
 */
public class CHGWDSCTOWDS6 extends VOConversionUI {
	/**
	 * CHG20TO21 ������ע�⡣
	 */
	public CHGWDSCTOWDS6() {
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
//				"H_vsourcebillcode->H_vbillno",
//				"H_voperatorid->H_voperatorid",
//				"H_srl_pkr->H_pk_inwhouse",//���ֿ�
//				"H_vdiliveraddress->H_vinaddress",//�ջ���ַ
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
				"B_unitid->B_cunitid",//�� ������λ
				"B_castunitid->B_cassunitid",//��������λ
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
				"H_srl_pk->\""+whid+"\"",//����ֿ�
				"H_pk_cargdoc->\""+spaceid+"\"",//��λ
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
	* �����û��Զ��庯����
	*/
	public UserDefineFunction[] getUserDefineFunction() {
		return null;
	}
}
