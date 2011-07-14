package nc.bs.pf.changedir;

import nc.bs.pf.change.VOConversion;

/**
 * 
 * @author Administrator
 *  �������������������
 */

 public class CHGWDS7TOWDS6 extends VOConversion {
	
	public CHGWDS7TOWDS6 (){
		super();
	}
	
	@Override
	public String[] getField() {
		return new String[] {					
				//				��ͷ���ݽ���
				/**ҵ������ */
				"H_cbiztype->H_geh_cbiztype",
				"H_pk_corp->H_pk_corp",//��˾				
				/**�������� */
				// " vbilltype->",
				/**�������� */
				"H_dbilldate->SYSDATE",				
				/**����ֿ� */
				"H_srl_pk->H_geh_cwarehouseid",
				/**���Ա */
				"H_cwhsmanagerid->H_geh_cwhsmanagerid",
				"H_vbillstatus->H_pwb_fbillflag",
				/**���� */
				"H_cdptid->H_geh_cdptid",
				/**�շ���� */
				"H_cdispatcherid->H_geh_cdispatcherid",
				/**��λ����*/
				"H_pk_cargdoc->H_pk_cargdoc",
				"H_csourcebillhid->H_geh_pk", // ��Դ���ݱ�ͷ
				"H_tmaketime->ENV_NOWTIME",
				"H_coperatorid->SYSOPERATOR",
				"H_cbizid->H_geh_cbizid",
				"H_pk_calbody->H_geh_calbody",

				//				 ����  ���� ����
				"B_crowno->B_geb_crowno",
				"B_cinvbasid->B_geb_cinvbasid",//�������ID
				"B_cinventoryid->B_geb_cinventoryid",//�������ID
				"B_unitid->B_pk_measdoc",	//��λ
				"B_castunitid->B_castunitid",//����λ

				"B_noutnum->B_geb_anum",//ʵ������
				"B_noutassistnum->B_geb_banum",//ʵ��������
				//					 "B_nacceptnum->B_geb_anum",//���������
				//					 "B_nassacceptnum->B_geb_banum",//����⸨����
				"B_nshouldoutnum->B_geb_anum",//Ӧ������
				"B_nprice->B_geb_nprice",
				"B_nshouldoutassistnum->B_geb_banum",//Ӧ��������
				"B_nmny->B_geb_nmny",		 
				"B_cspaceid->B_geb_space", //��λID
				//					 "B_comp->B_",
				"B_hsl->B_geb_hsl",
				"B_vbatchcode->B_geb_vbatchcode",//����
				//					 "B_dfirstbilldate->B_",
				"B_flargess->B_geb_flargess",
				"B_lvbatchcode->B_geb_backvbatchcode", //Դ����
				"B_dbizdate->SYSDATE",

				"B_csourcetype->H_geh_billtype",	
				"B_csourcebillbid->B_geb_pk",
				"B_vsourcebillcode->H_geh_billcode",	
				"B_csourcebillhid->B_geh_pk",


				"H_vuserdef15->H_geh_customize7",

				"B_cfirsttyp->B_cfirsttype",
				"B_cfirstbillhid->B_cfirstbillhid",
				"B_cfirstbillbid->B_cfirstbillbid"
				//					 "B_vfirstbillcode->B_",//Դͷ���ݺ�
		};
	}
	
	@Override
	public String[] getFormulas() {
		// TODO Auto-generated method stub
		return new String[]{
				"H_vbilltype->\"WDS6\"",
				
//				"B_geb_snum->B_noutnum-B_nacceptnum",  //ʵ��������-���������
		};
	}
	

}
