package nc.ui.pf.changedir;

import nc.ui.wl.pub.LoginInforHelper;
import nc.vo.pf.change.UserDefineFunction;
import nc.vo.pub.lang.UFDate;
/**
 * ���۶���--�˷Ѻ��㵥
 * @author Administrator
 *
 */
public class CHGWDS5TOWDSM extends nc.ui.pf.change.VOConversionUI{
	public CHGWDS5TOWDSM() {
		super();
	}
	/**
	* ��ú������ȫ¼�����ơ�
	* @return java.lang.String[]
	*/
	public String getAfterClassName() {
		return "nc.ui.pf.changedir.after.ChgWDS5TOWDSMAfter";
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
				"H_pk_corp->H_pk_corp",//��˾				
				"H_pk_busitype->H_pk_busitype",//ҵ������				
				//"H_->H_dbilldate",//��������
				"H_pk_deptdoc->H_pk_deptdoc",//����
				"H_vemployeeid->H_vemployeeid",//ҵ��Ա	
				
//				"H_->H_pk_cubasdoc",//���̻���id
				"B_pk_trader->H_pk_cumandoc",//���̹���id
//				"H_->H_custname",//��������
//				"H_->H_denddate",//���ʱ��
//				"H_->H_dbegindate",//װ��ʱ��
//				"H_->H_itranstype",//���䷽ʽ
				
//				"H_->H_pk_manageperson",//���Ա
//				"H_->H_pk_yedb",//ҵ�����
//				"H_->H_vyedbtel",//ҵ�����绰
//				"H_->H_pk_transer",//����Ա
//				"H_->H_transername",//����Ա����
//				"H_->H_creceiptcustomerid",//�ջ�λ
				
				
//				"H_->H_pk_receiveperson",//��ϵ��
//				"H_->H_vtelphone",//��ϵ�绰				
//				"H_->H_fisbigflour",//�Ƿ�����				
				"H_vdiliveraddress->H_vinaddress",//�ջ���ַ				
				"H_vmemo->H_vmemo",	//��ע					
				"B_cinvmandocid->B_pk_invmandoc",//�������id
				"B_cinvbasdocid->B_pk_invbasdoc",//�������id	
				"B_pk_destore->H_pk_outwhouse",//����ֿ�
				"B_pk_restore->H_pk_inwhouse",//���ֿ�
				"B_invcode->B_invcode",//�������
				"B_invname->B_invname",//�������
				"B_invspec->B_invspec",//���
				"B_invtype->B_invtype",//�ͺ�	
//				"B_->B_picicode",//����
//				"B_->B_unitvolume",//������
				"B_cunitid->B_uint",//������id
				"B_cassunitid->B_assunit",//������id
				"B_unitname->B_unitname",//����������
				"B_assunitname->B_assunitname",//����������
				"B_nhsl->B_nhgrate",//������
				"B_nnum->B_noutnum",//ʵ������
				"B_nassnum->B_nassoutnum",//ʵ��������	
			//	"B_pk_cardoc->",//����
				"B_csourcetype->H_pk_billtype",//��������
				"B_csourcebillhid->H_pk_sendorder",//��Դ���˶�������
				"B_csourcebillbid->B_pk_sendorder_b",//��������
				"B_vsourcebillcode->H_vbillno",//������(��Դ������)
				
				"B_cupsourcebillrowid->B_cfirstbillbid",//Դͷ���ݱ���id
				"B_cupsourcebillid->B_cfirstbillhid",//Դͷ���ݱ�ͷid
				"B_cupsourcebilltype->B_cfirsttype",//Դͷ��������
				"B_vupbillcode->B_vfirstbillcode",//Դͷ���ݺ�					
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
		new UFDate(System.currentTimeMillis());
		super.setSysDate(new UFDate(System.currentTimeMillis()).toString());
		return new String[] {
				"H_icoltype->int(1)",//
				"B_creceiverealid->getColValue2(tb_storcubasdoc, custareaid,pk_stordoc,H_pk_outwhouse,pk_cumandoc ,H_pk_cumandoc)",
				"B_ngl->getColValue2(tb_storcubasdoc, kilometer,pk_stordoc,H_pk_outwhouse,pk_cumandoc ,H_pk_cumandoc)"
		};
	}
	/**
	* �����û��Զ��庯����
	*/
	public UserDefineFunction[] getUserDefineFunction() {
		return null;
	}
	
}
