package nc.bs.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
import nc.vo.wl.pub.Wds2WlPubConst;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * ���ص������->�����˵�
 * @author zhf
 *
 */
public class CHGWDS9TOWS21 extends nc.bs.pf.change.VOConversion {

	public CHGWDS9TOWS21() {
		super();
	}
	/**
	* ��ú������ȫ¼�����ơ�
	* @return java.lang.String[]
	*/
	public String getAfterClassName() {
		return null;
	}
	/**
	* �����һ���������ȫ¼�����ơ�
	* @return java.lang.String[]
	*/
	public String getOtherClassName() {
	  return null;
	}

	public String[] getField() {
		return (
				new String[] {
						
						"H_pk_corp->H_pk_corp",//�����빫˾
						"H_pk_inwhouse->H_geh_cwarehouseid",//���ֿ�
						"H_vdef1->H_geh_cothercorpid",//������˾
						"H_pk_outwhouse->H_geh_cotherwhid",//����ֿ�						
						"H_pk_busitype->H_geh_cbiztype",//ҵ������
						
						"B_pk_invbasdoc->B_geb_cinvbasid",//�����������ID   
						"B_pk_invmandoc->B_geb_cinventoryid",//�������ID  	
						"B_unit->B_pk_measdoc",//����λ
						"B_assunit->B_castunitid",//����λ
						"B_nhsl->B_geb_hsl",//������ 
//						"B_scrq->B_geb_proddate",//��������-----------------------------zpm
//						"B_dvalidate->B_geb_dvalidate",//ʧЧ����------------------------------zpm
						
						
						"B_noutnum->B_geb_snum",//Ӧ������
						"B_nnassoutnum->B_geb_bsnum",//Ӧ�븨����  
						"B_ninacceptnum->B_geb_anum",//ʵ������
//						"B_ninassistnum->B_geb_banum",//ʵ�븨����   
						
						
//						"B_nprice->B_geb_nprice",//����
//						"B_nmny->B_geb_nmny",//���
					//	"B_vbatchcode->B_geb_vbatchcode",//���κ�	
//						"B_vbatchcode->B_geb_backvbatchcode",//ԭ���κŻ�д
						
//						"B_flargess->B_geb_flargess",//�Ƿ���Ʒ
//						"B_cspaceid->B_geb_space",//��λID B_cfirsttype

						"B_cfirstbillhid->B_gylbillhid",//[���� ��Ӧ�� �������ⵥ]
						"B_cfirstbillbid->B_gylbillbid",//[���� ��Ӧ�� �������ⵥ]
						"B_vfirstbillcode->B_gylbillcode",//[���� ��Ӧ�� �������ⵥ]
						"B_cfirsttype->B_gylbilltype",//[���� ��Ӧ�� �������ⵥ]
//						"B_"+WdsWlPubConst.csourcehid_wds+"->B_geb_pk",//Lyf:ERP����ⵥ����¼����ϵͳ��Դ��������,�Ա������ĵ����ܹ����鵽ERP����
//						"B_"+WdsWlPubConst.csourcebid_wds+"->B_geb_pk",//Lyf:ERP����ⵥ����¼����ϵͳ��Դ��������,�Ա������ĵ����ܹ����鵽ERP����
						"B_csourcebillhid->B_geh_pk",// [����  ���� ������ⵥ�ֶ�]��
						"B_csourcebillbid->B_geb_pk",//   [����  ���� ������ⵥ�ֶ�]
//						"B_vfirstbillcode->B_vfirstbillcode",//[����  ���� ������ⵥ�ֶ�]
						"B_csourcetype->H_geh_billtype",//[����  ���� ������ⵥ�ֶ�]
						
						"B_vdef1->B_vdef1",//���״̬
						
//						"B_dbizdate->B_geb_dbizdate",//�������--ҵ������
						
		
//						"H_coperatoridnow->SYSOPERATOR",
						"H_voperatorid->SYSOPERATOR",
						
						 "H_dmakedate->SYSDATE",
						 "H_dbilldate->SYSDATE",
						 "H_denddate->SYSDATE",
						
//						"B_nplannedmny->B_jhje",//�ƻ����
//						"B_nplannedprice->B_jhdj",//�ƻ�����
					
				});
	}     

		public String[] getFormulas()
		{	return new String[] {
//				"H_pk_corp->\""+m_strCorp+"\"",
//				"H_voperatorid->\""+m_strOperator+"\"",
				"H_pk_billtype->\""+Wds2WlPubConst.billtype_alloinsendorder+"\"",
				"H_vbillstatus->int(8)",
			    "B_csourcetype->\""+WdsWlPubConst.BILLTYPE_ALLO_IN+"\"",
			    "H_fisbigglour->\"N\"",
//			    "H_dmakedate->\""+m_strDate+"\"",
//			    "H_dbilldate->\""+m_strDate+"\"",
//			    "H_dbegindate->\""+m_strDate+"\"",
			    "H_itransstatus->\""+2+"\""
		};}
	/**
	* �����û��Զ��庯����
	*/
	public UserDefineFunction[] getUserDefineFunction() {
		return null;
	}
}
