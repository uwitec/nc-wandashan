package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * 
 *
 * �������ڣ�(2004-11-18)
 * @author��ƽ̨�ű�����
 * ERP�������ⵥ--�����������(WDS9)
 */
public class CHG4YTOWDS9 extends nc.ui.pf.change.VOConversionUI {
	

	public CHG4YTOWDS9() {
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
			"H_geh_cbiztype->H_cbiztype",//ҵ������
			"H_geh_fallocflag->H_fallocflag",//�������ͱ�־

//			"H_geh_cdispatcherid->H_cdispatcherid",//�շ����[��Ҫ�ֹ�����] ����������
			
			
//			"H_geh_cwhsmanagerid->H_cwhsmanagerid",//���Ա
//			"H_ ->H_ccustomerid",//�ͻ�
//			"H_ ->H_vdiliveraddress",//�ջ���ַ[���˵�ַ]
//			"H_ ->H_coperatorid",//�Ƶ���
//			"B_creceieveid->H_creceiptcustomerid",//�ջ���λ����id
//			"B_pk_cubasdocrev",//�ջ���λ����ID	
			
			
			"H_pk_corp ->H_cothercorpid",//�����빫˾
			"H_geh_calbody->H_cothercalbodyid",//����������֯
			"H_geh_cwarehouseid->H_cotherwhid",//�������ֿ�
			
			"H_geh_cothercorpid->H_pk_corp",//��Ӧ����������˾
			"H_geh_cothercalbodyid->H_pk_calbody",//��Ӧ�����������֯
			"H_geh_cotherwhid->H_cwarehouseid",//��Ӧ����������ֿ�
			
//			"H_geh_cdptid->H_cdptid",//���� [����˾��ͬ��]
//			"H_geh_cbizid->H_cbizid",//ҵ��Ա [����˾��ͬ��]
			

			"B_geb_cinvbasid->B_cinvbasid",//�����������ID   
//			"B_geb_cinventoryid->B_cinventoryid",//�������ID  	
			"B_pk_measdoc->B_pk_measdoc",//����λ
			"B_castunitid->B_castunitid",//����λ
//			"B_geb_hsl->B_hsl",//������ 
			
			"B_geb_proddate->B_scrq",//��������
			"B_geb_dvalidate->B_dvalidate",//ʧЧ����

			
//			"B_geb_banum->B_noutassistnum",//ʵ��������
//			"B_geb_anum->B_noutnum",//ʵ������			
			"B_geb_nprice->B_nprice",//����
			"B_geb_nmny->B_nmny",//���
			
		//	"B_geb_vbatchcode->B_vbatchcode",//���κ�			
			"B_geb_backvbatchcode->B_vbatchcode",//��д���κ�			
			"B_geb_flargess->B_flargess",//�Ƿ���Ʒ
			"B_geb_space->B_cspaceid",//��λID
			 
			"B_csourcebillhid->B_csourcebillhid",//[������������]
			"B_csourcebillbid->B_csourcebillbid",//[���������ӱ�����]
			"B_vsourcebillcode->B_vsourcebillcode",//[�����������ݺ�]
			"B_csourcetype->B_csourcetype",//[����������������]
			
			"B_cfirstbillhid->B_cfirstbillhid",// [������������]
			"B_cfirstbillbid->B_cfirstbillbid",//   [���������ӱ�����]
			"B_vfirstbillcode->B_vfirstbillcode",//[�����������ݺ� ]
			"B_cfirsttype->B_cfirsttype",//[����������������]
			
			
			"B_gylbillhid->B_cgeneralhid",//[�������ⵥ���� ]
			"B_gylbillbid->B_cgeneralbid",//[�������ⵥ�ӱ�����]
			"B_gylbillcode->H_vbillcode",//[ �������ⵥ���ݺ�]
			"B_gylbilltype->H_cbilltypecode"//[�������ⵥ��������--4Y ]
		
//			"B_geb_dbizdate->B_dbizdate"//ҵ������ [���¸�ֵ���������]
		};
	}

	public String[] getFormulas() {
		return new String[] {
				"H_geh_billtype->\"WDS9\"",
				"H_geh_cbilltypecode->\"WDS9\"",
				"B_geb_hsl->iif(B_hsl==null,B_noutnum/B_noutassistnum,B_hsl)",
				"B_geb_cinventoryid->getColValue2(bd_invmandoc,pk_invmandoc,pk_invbasdoc,B_cinvbasid,pk_corp,H_cothercorpid)",//���ݻ���ID����˾,��ȡ����ID			
				
				"B_geb_snum->B_noutnum-B_"+WdsWlPubConst.erp_allo_outnum_fieldname,//Ӧ������ 
//				"B_geb_bsnum->(B_noutnum-B_"+WdsWlPubConst.erp_allo_outnum_fieldname+")/B_geb_hsl",//Ӧ��������
				
//				zhf add �˵�����
				"B_geb_customize9->\"WS21\""
		};
	}
	/**
	* �����û��Զ��庯����
	*/
	public UserDefineFunction[] getUserDefineFunction() {
		return null;
	}
}
