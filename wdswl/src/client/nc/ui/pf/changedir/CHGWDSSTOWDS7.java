package nc.ui.pf.changedir;

import nc.ui.pf.change.VOConversionUI;
/**
 * 
 * @author mlr
 *  �����˵�->�������ǰ̨������
 */
public class CHGWDSSTOWDS7 extends VOConversionUI {
	
	public CHGWDSSTOWDS7 (){
		super();
	}
	
	@Override
	public String[] getField() {
		return new String[] {				
			"H_pk_corp ->H_pk_corp",//��˾	
			"H_geh_cwarehouseid->H_pk_outwhouse",//�ֿ�								
			"B_geb_cinvbasid->B_pk_invbasdoc",//�����������ID   
			"B_geb_cinventoryid->B_pk_invmandoc",//�������ID  	
			"B_pk_measdoc->B_unit",//����λ 
			"B_castunitid->B_assunit",//����λ
			"B_geb_hsl->B_hsl",//������ 
			"B_vfirstbillcode->B_vfirstbillcode",
			"B_cfirsttype->B_cfirsttype",
			"B_cfirstbillhid->B_cfirstbillhid",
			"B_cfirstbillbid->B_cfirstbillbid",			
			"B_vsourcebillcode->H_vbillno",
			"B_csourcetype->H_pk_billtype",
			"B_csourcebillhid->B_pk_sendorder",
			"B_csourcebillbid->B_pk_sendorder_b",
			"B_cdt_pk->B_vdef1",//���״̬
		};
	}
	
	@Override
	public String[] getFormulas() {	
		return new String[]{
			"B_geb_snum->B_ndealnum-B_noutnum",  //ʵ��������-���������
			"B_geb_bsnum->B_nassdealnum-B_nassoutnum",//ʵ��������-����⸨����
		};
	}
	

}
