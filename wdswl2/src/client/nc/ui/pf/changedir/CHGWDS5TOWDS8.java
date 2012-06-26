package nc.ui.pf.changedir;

import nc.ui.pf.change.VOConversionUI;
import nc.vo.pf.change.UserDefineFunction;

public class CHGWDS5TOWDS8 extends VOConversionUI{

	public CHGWDS5TOWDS8() {
		super();
	}

	public String getAfterClassName() {
		return "nc.vo.ic.so.out.ChangeSaleOutVO";
	}

	public String getOtherClassName() {
		return null;
	}

	public String[] getField() {
		return new String[] {
				"H_srl_pk->H_pk_outwhouse",// ����ֿ�
				"H_cbiztype->H_pk_busitype",// ҵ����������
				"H_cdptid->H_pk_deptdoc",// ����
				"H_cbizid->H_vemployeeid",// ҵ��Ա
				"H_ccustomerid->H_pk_cumandoc",// �ͻ�
				"H_pk_cubasdocc->H_pk_cubasdoc",// �ͻ�����id
				"H_vdiliveraddress->H_vinaddress",// �ջ���ַ
				"H_vnote->H_vmemo",// ��ע
				"H_pk_calbody->H_ccalbodyid",// �����֯
				"H_creceiptcustomerid->H_creceiptcustomerid",//�ջ���λ
				
				
//				"H_vsourcebillcode->H_vbillno",// ��Դ���ݺ�
//				"H_csourcebillhid->H_pk_soorder",// ��Դ���ݱ�ͷ����
				
				
				"B_csourcebillhid->B_pk_soorder",// ��Դ���ݱ�ͷ����
				"B_csourcebillbid->B_pk_soorder_b",// ��Դ���ݱ�������
				"B_vsourcebillcode->H_vbillno", // ��Դ���ݺ�
				"B_csourcetype->H_pk_billtype",// ��Դ��������
				"B_cfirstbillhid->B_cfirstbillhid", // Դͷ���ݱ�ͷ����
				"B_cfirstbillbid->B_cfirstbillbid",// Դͷ���ݱ�������
				"B_cfirsttype->B_cfirsttype",//Դͷ��������
				"B_vfirstbillcode->B_vfirstbillcode",//Դͷ���ݺ�
			
				"B_cinventoryid->B_pk_invmandoc",// �������
				"B_cinvbasid->B_pk_invbasdoc",//�������id
				"B_flargess->B_fisgift",// �Ƿ���Ʒ
				"B_lvbatchcode->B_picicode",//��Դ���κ�
				"B_vbatchcode->B_picicode", //���κ�
				"B_unitid->B_uint",//����λ
				"B_castunitid->B_assunit",//����λ
				"B_hsl->B_nhgrate",//������
				"B_isxnap->B_isxnap"//�Ƿ����ⰲ�� liuys add
		};
	}
	/**
	* ��ù�ʽ��
	*/
	public String[] getFormulas() {
		return new String[] {
				"B_isoper->\"Y\"", //�Ƿ���в���
//				"B_crowno->\"10\""//�к�
				"B_nshouldoutnum->B_narrangnmu-B_noutnum",// Ӧ������
				"B_nshouldoutassistnum->B_nassarrangnum-B_nassoutnum", // Ӧ��������
		};
	}
	/**
	* �����û��Զ��庯����
	*/
	public UserDefineFunction[] getUserDefineFunction() {
		return null;
	}
}