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
				
				"H_ccustomerid->H_geh_customize8",//�ͻ�
//				"H_coperatorid->SYSOPERATOR",
//				"H_dbilldate->SYSDATE",
//				"H_coperatoridnow->SYSOPERATOR",
				
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
				"B_pk_corp->H_pk_corp",
				"B_nprice->B_geb_nprice",//����
				
				"B_vbatchcode->B_geb_vbatchcode",//���κ�	------------------------------zpm
				
				"B_flargess->B_geb_flargess",//�Ƿ���Ʒ
				"B_cspaceid->B_geb_space",//��λID
				 
				"B_csourcebillhid->B_cfirstbillhid",//�������۳��� Դͷ���ݱ�ͷID[���� ����]  
				"B_csourcebillbid->B_cfirstbillbid",//�������۳��� Դͷ���ݱ���ID  [���� ����]  
				"B_vsourcebillcode->B_vfirstbillcode",//�������۳��� Դͷ���ݺ�[���� ����]   
				"B_csourcetype->B_cfirsttype",//�������۳��� Դͷ�������ͱ���[���� ����]   
				
				"B_cfirstbillhid->B_geh_pk",// --�����˻���ⵥ ��������erp���۳��ⵥ��ʱ�������������۳���ش�����������֧������;�������ɵ�ERP���۳���
		
				"B_cfirstbillbid->B_geb_pk",//--�����˻���ⵥ����������erp���۳��ⵥ��ʱ�򣬸��ݱ���ID�����һ�λ��Ϣ   
				"B_vfirstbillcode->H_geh_billcode",//
				"B_cfirsttype->H_geh_billtype",//
				
				"H_freplenishflag->H_freplenishflag",//�Ƿ��˻�
				"H_boutretflag->H_boutretflag",//�Ƿ��˻�
				"B_dbizdate->H_geh_dbilldate"//��������->ҵ������-------------------------zpm
		};
	}

	public String[] getFormulas() {
		return new String[] {
				"H_cbilltypecode->\"4C\"",
				"H_pk_calbody->getColValue(bd_stordoc, pk_calbody,pk_stordoc,H_srl_pk)",//�����֯
//				"H_cothercorpid->getColValue(bd_stordoc, pk_corp,pk_stordoc,H_srl_pk)",//��⹫˾
//				"H_cothercalbodyid->getColValue(bd_stordoc, pk_calbody,pk_stordoc,H_srl_pk)",//�������֯ 
				"B_cquotecurrency->getColValue(so_saleorder_b,ccurrencytypeid,corder_bid,B_cfirstbillbid)",
				"B_nshouldoutassistnum->-1*B_geb_bsnum",//Ӧ��������
				"B_nshouldoutnum->-1*B_geb_snum",//Ӧ������ 
				"B_noutassistnum->-1*B_geb_banum",//ʵ��������
				"B_noutnum->-1*B_geb_anum",//ʵ������
				"B_nmny->-1*B_geb_nmny",//���





//				"H_pk_calbody->iif(B_creccalbodyid =NULL,B_cadvisecalbodyid,B_creccalbodyid)",
				"B_pk_calbody->getColValue(bd_stordoc, pk_calbody,pk_stordoc,H_srl_pk)",//�����֯
//				"H_cwarehouseid->iif(B_creccalbodyid =NULL,B_cbodywarehouseid ,B_crecwareid)",
//				"B_cwarehouseid->iif(B_creccalbodyid =NULL,B_cbodywarehouseid ,B_crecwareid)",
//				"H_pk_calbody->iif(B_creccalbodyid =NULL,B_cadvisecalbodyid,B_creccalbodyid)",
				"H_pk_cubasdocC->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,H_geh_customize8)",
//				"H_cbilltypecode->\"4C\"",
//				"B_nshouldoutnum->B_nnumber   -   B_ntotalinventorynumber - iif(B_ntotalshouldoutnum==null,0,B_ntotalshouldoutnum) + iif(B_ntranslossnum==null,0,B_ntranslossnum) ",
//				"B_hsl->iif(B_scalefactor==null,B_nnumber/B_npacknumber,B_scalefactor)",
//				"B_csourcetype->\"30\"",
//				"B_nshouldoutassistnum->(  B_nnumber   -   B_ntotalinventorynumber    - iif(B_ntotalshouldoutnum==null,0,B_ntotalshouldoutnum) + iif(B_ntranslossnum==null,0,B_ntranslossnum) )   *  (   iif(B_npacknumber==null,0,B_npacknumber) / B_nnumber   ) "


		};
	}
	/**
	* �����û��Զ��庯����
	*/
	public UserDefineFunction[] getUserDefineFunction() {
		return null;
	}
}
