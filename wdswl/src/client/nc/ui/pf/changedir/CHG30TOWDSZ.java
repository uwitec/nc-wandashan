package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;

public class CHG30TOWDSZ extends nc.ui.pf.change.VOConversionUI{


	public CHG30TOWDSZ() {
		super();
	}

	public String getAfterClassName() {
		return "nc.vo.ic.out.in.ChgtoOtherIn";
	}

	public String getOtherClassName() {
		return null;
	}

	public String[] getField() {
		return new String[] {
	//			"H_srl_pk->H_cwarehouseid",// ����ֿ�
				"H_geh_cbiztype->H_cbiztype",// ҵ����������
				"H_geh_cdptid->H_cdeptid",// ����
				"H_geh_cbizid->H_cemployeeid",// ҵ��Ա
				"H_geh_customize8->H_ccustomerid",// �ͻ�
		//		"H_vdiliveraddress->B_vreceiveaddress",// �ջ���ַ
				"H_vnote->H_vnote",// ��ע
		//		"H_geh_calbody->H_ccalbodyid",// �����֯
	//			"H_creceiptcustomerid->H_creceiptcustomerid",//�ջ���λ
				"H_vsourcebillcode->H_vreceiptcode", // ��Դ���ݺ�			
				"B_csourcebillhid->B_csaleid",// ��Դ���ݱ�ͷ����
				"B_csourcebillbid->B_corder_bid",// ��Դ���ݱ�������
				"B_vsourcebillcode->H_vreceiptcode", // ��Դ���ݺ�
				"B_csourcetype->H_creceipttype",// ��Դ��������
//				"H_srl_pk->B_cbodywarehouseid",//����վ(����ֿ�)
				"B_cfirstbillhid->B_csaleid", // Դͷ���ݱ�ͷ����
				"B_cfirstbillbid->B_corder_bid",// Դͷ���ݱ�������
				"B_cfirsttype->H_creceipttype",//Դͷ��������
				"B_vfirstbillcode->H_vreceiptcode",//Դͷ���ݺ�
				
				"B_geb_snum->B_nnumber",// Ӧ������
				"B_geb_bsnum->B_npacknumber", // Ӧ��������
				"B_geb_cinventoryid->B_cinventoryid",// �������
				"B_geb_cinvbasid->B_cinvbasdocid",//�������id
				"B_geb_flargess->B_blargessflag",// �Ƿ���Ʒ
//				"B_lvbatchcode->B_",//��Դ���κ�
//				"B_vbatchcode->B_", //���κ�
				"B_pk_measdoc->B_cunitid",//����λ
				"B_castunitid->B_cpackunitid",//����λ
				"B_geb_hsl->B_scalefactor",//������
				"B_geb_nprice->B_noriginalcurprice",//����
				"B_geb_nmny->B_noriginalcurmny",//���
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
