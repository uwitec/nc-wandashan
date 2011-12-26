package nc.bs.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;

public class CHGWDSZTO4C extends nc.bs.pf.change.VOConversion {

	public CHGWDSZTO4C() {
		super();
	}


	public String getAfterClassName() {
		return "nc.bs.ic.pub.pfconv.HardLockChgVO";
	}

	public String getOtherClassName() {
		return "nc.ui.ic.pub.pfconv.HardLockChgVO";
	}

	public String[] getField() {
		return new String[] {
//				"H_vbillcode",//���ݺ�--------ϵͳ�жϣ���������ڣ����Զ�����
//				"B_crowno",//�к�
//				"coperatorid", //����Ա
//				"H_dbilldate->SYSDATE",//��������					
				"H_cwarehouseid->H_geh_cwarehouseid",//�ֿ�
//				"H_pk_calbody->H_pk_calbody",//�����֯
				"H_cbiztype->H_geh_cbiztype",//ҵ������
				"H_cdispatcherid->H_geh_cdispatcherid",//�շ����
				"H_cwhsmanagerid->H_geh_cwhsmanagerid",//���Ա
				"H_cdptid->H_geh_cdptid",//����
				"H_cbizid->H_geh_cbizid",//ҵ��Ա
		//		"H_ccustomerid->H_ccustomerid",//�ͻ�
				"H_vdiliveraddress->H_vdiliveraddress",//�ջ���ַ[���˵�ַ]
				"H_pk_corp->H_pk_corp",//��˾
				"H_coperatorid->H_coperatorid",//�Ƶ���
				
				"B_creceieveid->H_creceiptcustomerid",//�ջ���λ����id
//				"B_pk_cubasdocrev",//�ջ���λ����ID
				
				"B_cinvbasid->B_geb_cinvbasid",//�����������ID   
				"B_cinventoryid->B_geb_cinventoryid",//�������ID  	
				"B_pk_measdoc->B_pk_measdoc",//����λ
				"B_castunitid->B_castunitid",//����λ
				"B_hsl->B_geb_hsl",//������ 
				"B_scrq->B_geb_proddate",//��������-----------------------------zpm
				"B_dvalidate->B_geb_dvalidate",//ʧЧ����------------------------------zpm
				"B_nshouldoutassistnum->B_geb_bsnum",//Ӧ��������
				"B_nshouldoutnum->B_geb_snum",//Ӧ������ 
				"B_noutassistnum->B_geb_banum",//ʵ��������
				"B_noutnum->B_geb_anum",//ʵ������
				"B_nprice->B_geb_nprice",//����
				"B_nmny->B_geb_nmny",//���
				"B_vbatchcode->B_geb_vbatchcode",//���κ�	------------------------------zpm
				
				"B_flargess->B_flargess",//�Ƿ���Ʒ
		//		"B_cspaceid->B_cspaceid",//��λID
				 
				"B_csourcebillhid->B_cfirstbillhid",//�������۳��� Դͷ���ݱ�ͷID[���� ����]  
				"B_csourcebillbid->B_cfirstbillbid",//�������۳��� Դͷ���ݱ���ID  [���� ����]  
				"B_vsourcebillcode->B_vfirstbillcode",//�������۳��� Դͷ���ݺ�[���� ����]   
				"B_csourcetype->B_cfirsttyp",//�������۳��� Դͷ�������ͱ���[���� ����]   
				
				///---------------------------�ù�Ӧ�� ���۳����Դͷ��¼ �������۳���ID
//				"B_cfirstbillhid->B_general_pk",// --���۳���ص��� ��������erp���۳��ⵥ��ʱ�������������۳���ش�����������֧������;�������ɵ�ERP���۳���
		
				"B_cfirstbillbid->B_general_b_pk",//--���۳���ش�������������erp���۳��ⵥ��ʱ�򣬸��ݱ���ID�����һ�λ��Ϣ   
				"B_vfirstbillcode->H_vbillno",//
				"B_cfirsttype->H_vbilltype",//
				
				"H_freplenishflag->H_freplenishflag",//�Ƿ��˻�
				"H_boutretflag->H_boutretflag",//�Ƿ��˻�
				"B_dbizdate->B_dbizdate"//��������->ҵ������-------------------------zpm
		};
	}

	public String[] getFormulas() {
		return new String[] {
				"H_cbilltypecode->\"4C\"",
				"H_pk_calbody->getColValue(bd_stordoc, pk_calbody,pk_stordoc,H_srl_pk)",//�����֯
				"H_cothercorpid->getColValue(bd_stordoc, pk_corp,pk_stordoc,H_srl_pk)",//��⹫˾
				"H_cothercalbodyid->getColValue(bd_stordoc, pk_calbody,pk_stordoc,H_srl_pk)",//�������֯ 
				"B_cquotecurrency->getColValue(so_saleorder_b,ccurrencytypeid,corder_bid,B_cfirstbillbid)",

		};
	}
	/**
	* �����û��Զ��庯����
	*/
	public UserDefineFunction[] getUserDefineFunction() {
		return null;
	}
}
