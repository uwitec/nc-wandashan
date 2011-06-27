package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;

/**
 * ��Ӧ����������->�����������
 * @author Administrator
 *
 */
public class CHG4ITOWDS7 extends nc.ui.pf.change.VOConversionUI{
	

	public CHG4ITOWDS7() {
		super();
	}

	public String getAfterClassName() {
		return "nc.vo.ic.other.in.ChgtoOtherIn";
	}

	public String getOtherClassName() {
		return null;
	}

	public String[] getField() {
		return new String[] {
			
			"H_pk_corp ->H_pk_corp",//��˾��ͬһ�ҹ�˾��
			"H_geh_calbody->H_cothercalbodyid",//������֯
			"H_geh_cwarehouseid->H_cotherwhid",//���ֿ�			
			
			"H_geh_cothercalbodyid->H_pk_calbody",//��Ӧ���������֯
			"H_geh_cotherwhid->H_cwarehouseid",//��Ӧ������ֿ�
			
			"H_geh_cbiztype->H_cbiztype",//ҵ������
			
//			"H_geh_cdispatcherid->H_cdispatcherid",//�շ����
//			"H_geh_cwhsmanagerid->H_cwhsmanagerid",//���Ա
			
			
//			"H_ ->H_ccustomerid",//�ͻ�
//			"H_ ->H_vdiliveraddress",//�ջ���ַ[���˵�ַ]
//			"H_ ->H_coperatorid",//�Ƶ���
//			"B_creceieveid->H_creceiptcustomerid",//�ջ���λ����id
//			"B_pk_cubasdocrev",//�ջ���λ����ID	
			
			
			
			"H_geh_cdptid->H_cdptid",//����
			"H_geh_cbizid->H_cbizid",//ҵ��Ա
			

			"B_geb_cinvbasid->B_cinvbasid",//�����������ID   
			"B_geb_cinventoryid->B_cinventoryid",//�������ID  	
			"B_pk_measdoc->B_pk_measdoc",//����λ
			"B_castunitid->B_castunitid",//����λ
			"B_geb_hsl->B_hsl",//������ 
			"B_geb_proddate->B_scrq",//��������
			"B_geb_dvalidate->B_dvalidate",//ʧЧ����
//			"B_geb_bsnum->B_nshouldoutassistnum",//Ӧ��������
//			"B_geb_snum->B_nshouldoutnum",//Ӧ������ 
//			
//			"B_geb_banum->B_noutassistnum",//ʵ��������
//			"B_geb_anum->B_noutnum",//ʵ������
			
			
			"B_geb_bsnum->B_noutassistnum",//ʵ��������->Ӧ��������
			"B_geb_snum->B_noutnum",//ʵ������->Ӧ������
			
			"B_geb_nprice->B_nprice",//����
			"B_geb_nmny->B_nmny",//���
			"B_geb_vbatchcode->B_vbatchcode",//���κ�	
			"B_geb_backvbatchcode->B_vbatchcode",//��Դ���κ�
			"B_geb_flargess->B_flargess",//�Ƿ���Ʒ
			"B_geb_space->B_cspaceid",//��λID
			 
			"B_csourcebillhid->B_csourcebillhid",//[����  ���� �������ֶ�]
			"B_csourcebillbid->B_csourcebillbid",//[����  ���� �������ֶ�]
			"B_vsourcebillcode->B_vsourcebillcode",//[����  ���� �������ֶ�]
			"B_csourcetype->B_csourcetype",//[����  ���� �������ֶ�]
			
			"B_cfirstbillhid->B_cfirstbillhid",// [����  ���� �������ֶ�]
			"B_cfirstbillbid->B_cfirstbillbid",//   [����  ���� �������ֶ�]
			"B_vfirstbillcode->B_vfirstbillcode",//[����  ���� �������ֶ� ]
			"B_cfirsttype->B_cfirsttype",//
			
			"B_gylbillcode->H_vbillcode",//[����  ���湩Ӧ�� �������ֶ� ]
			"B_gylbilltype->H_cbilltypecode",//[����  ���湩Ӧ�� �������ֶ� ]
			"B_gylbillhid->B_cgeneralhid",//[����  ���湩Ӧ�� �������ֶ� ]
			"B_gylbillbid->B_cgeneralbid"//[����  ���湩Ӧ�� �������ֶ� ]
//			"B_geb_dbizdate->B_dbizdate"//ҵ������ [���¸�ֵ���������]
		};
	}

	public String[] getFormulas() {
		return new String[] {
			"H_geh_cbilltypecode->\"WDS7\"",
			"B_geb_vbatchcode->getColValue(tb_outgeneral_b,vbatchcode,general_b_pk,B_csourcebillbid)"
		//	"B_status->2",//���У�vo״̬
		//	"H_status->2"//���У�vo״̬
		};
	}
	/**
	* �����û��Զ��庯����
	*/
	public UserDefineFunction[] getUserDefineFunction() {
		return null;
	}
}
