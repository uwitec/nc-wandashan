package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;

public class CHG30TOWDS8 extends nc.ui.pf.change.VOConversionUI{


	public CHG30TOWDS8() {
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
				"H_srl_pk->H_cwarehouseid",// ����ֿ�
				"H_cbiztype->H_cbiztype",// ҵ����������
				"H_cdptid->H_cdeptid",// ����
				"H_cbizid->H_cemployeeid",// ҵ��Ա
				"H_ccustomerid->H_ccustomerid",// �ͻ�
				"H_vdiliveraddress->B_vreceiveaddress",// �ջ���ַ
				"H_vnote->H_vnote",// ��ע
				"H_pk_calbody->H_ccalbodyid",// �����֯
				"H_creceiptcustomerid->H_creceiptcustomerid",//�ջ���λ
				"H_vsourcebillcode->H_vreceiptcode", // ��Դ���ݺ�			
				"B_csourcebillhid->B_csaleid",// ��Դ���ݱ�ͷ����
				"B_csourcebillbid->B_corder_bid",// ��Դ���ݱ�������
				"B_vsourcebillcode->H_vreceiptcode", // ��Դ���ݺ�
				"B_csourcetype->H_creceipttype",// ��Դ��������
				"H_srl_pk->B_cbodywarehouseid",//����վ(����ֿ�)
//				"B_cfirstbillhid->B_cfirstbillhid", // Դͷ���ݱ�ͷ����
//				"B_cfirstbillbid->B_cfirstbillbid",// Դͷ���ݱ�������
//				"B_cfirsttyp->B_cfirsttype",//Դͷ��������
//				"B_vfirstbillcode->B_vfirstbillcode",//Դͷ���ݺ�
				"B_nshouldoutnum->B_nnumber",// Ӧ������
				"B_nshouldoutassistnum->B_npacknumber", // Ӧ��������
				"B_cinventoryid->B_cinventoryid",// �������
				"B_cinvbasid->B_cinvbasdocid",//�������id
				"B_flargess->B_blargessflag",// �Ƿ���Ʒ
//				"B_lvbatchcode->B_",//��Դ���κ�
//				"B_vbatchcode->B_", //���κ�
				"B_unitid->B_cunitid",//����λ
				"B_castunitid->B_cpackunitid",//����λ
				"B_hsl->B_scalefactor"//������
		};
	}
	/**
	* ��ù�ʽ��
	*/
	public String[] getFormulas() {
		return new String[] {
				"B_isoper->\"Y\"", //�Ƿ���в���
				"H_vdiliveraddress->getColValue(bd_custaddr,addrname ,pk_custaddr , H_vreceiveaddress)"
//				"B_crowno->\"10\""//�к�
		};
	}
	/**
	* �����û��Զ��庯����
	*/
	public UserDefineFunction[] getUserDefineFunction() {
		return null;
	}


}
