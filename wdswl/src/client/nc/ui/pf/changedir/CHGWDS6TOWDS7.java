package nc.ui.pf.changedir;

import nc.ui.pf.change.VOConversionUI;
/**
 * 
 * @author Administrator
 *  ��������->�������ǰ̨������
 */
public class CHGWDS6TOWDS7 extends VOConversionUI {
	
	public CHGWDS6TOWDS7 (){
		super();
	}
	
	@Override
	public String[] getField() {
		return new String[] {
			"H_geh_cothercorpid->H_pk_corp",
			"H_geh_cotherwhid->H_srl_pk",//����ֿ�
			"H_geh_cwarehouseid->H_srl_pkr",//���ֿ�
			"B_geb_cinvbasid->B_cinvbasid",
			"B_geb_cinventoryid->B_cinventoryid",
//			"B_geb_hsl->B_",//������
			"B_geb_vbatchcode->B_vbatchcode",//���κ� 
//			"B_vfirstbillcode->B_",
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
		// TODO Auto-generated method stub
		return new String[]{
				"B_geb_snum->B_noutnum-B_nacceptnum",  //ʵ��������-���������
		};
	}
	

}
