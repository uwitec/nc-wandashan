package nc.ui.pf.changedir;

import nc.ui.pf.change.VOConversionUI;
/**
 * 
 * @author mlr
 *  ��������->�������ǰ̨������
 */
public class CHGWDS6TOWDS7 extends VOConversionUI {
	
	public CHGWDS6TOWDS7 (){
		super();
	}
	
	@Override
	public String[] getField() {
		return new String[] {				
			"H_pk_corp ->H_pk_corp",//��˾��ͬһ�ҹ�˾��		
			"H_geh_cwarehouseid->H_srl_pkr",//���ֿ�						
			"H_geh_cotherwhid->H_srl_pk",//����ֿ�			
			"H_geh_cbiztype->H_cbiztype",//ҵ������			
			"H_geh_cdispatcherid->H_cdispatcherid",//�շ����
			"H_geh_cwhsmanagerid->H_cwhsmanagerid",//���Ա											
			"H_geh_cdptid->H_cdptid",//����
			"H_geh_cbizid->H_cbizid",//ҵ��Ա			
			"B_geb_cinvbasid->B_cinvbasid",//�����������ID   
			"B_geb_crowno->B_crowno",//�к�
			"B_geb_cinventoryid->B_cinventoryid",//�������ID  	
			"B_pk_measdoc->B_unitid",//����λ 
			"B_castunitid->B_castunitid",//����λ
			"B_geb_hsl->B_hsl",//������ 
			"B_geb_proddate->B_cshengchanriqi",//��������
			"B_geb_dvalidate->B_cshixiaoriqi",//ʧЧ����			
			"B_geb_nprice->B_nprice",//����
			"B_geb_nmny->B_nmny",//���
			"B_geb_vbatchcode->B_vbatchcode",//���κ�	
			"B_geb_backvbatchcode->B_lvbatchcode",//��Դ���κ�
//			"B_geb_flargess->B_flargess",//�Ƿ���Ʒ--
//			"B_geb_space->B_cspaceid",//��λID--			 
			"B_vfirstbillcode->B_vfirstbillcode",
			"B_cfirsttype->B_cfirsttyp",
			"B_cfirstbillhid->B_cfirstbillhid",
			"B_cfirstbillbid->B_cfirstbillbid",			
			"B_vsourcebillcode->H_vbillcode",
			"B_csourcetype->H_vbilltype",
			"B_csourcebillhid->B_general_pk",
			"B_csourcebillbid->B_general_b_pk",
		};
	}
	
	@Override
	public String[] getFormulas() {
		
		return new String[]{
			"B_geb_snum->B_noutnum-B_nacceptnum",  //ʵ��������-���������
			"B_geb_bsnum->B_noutassistnum-nassacceptnum",//ʵ��������-����⸨����
		};
	}
	

}
