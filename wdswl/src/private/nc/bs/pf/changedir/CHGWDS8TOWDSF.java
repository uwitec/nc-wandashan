package nc.bs.pf.changedir;

import nc.bs.pf.change.VOConversion;
/**
 * ���۳���->  װж�ѽ���
 * @author Administrator
 *
 */
public class CHGWDS8TOWDSF extends VOConversion {
	
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

	
	
	@Override
	public String[] getField() {
		// TODO Auto-generated method stub
		return new String[]{
				"B_csourcebillhid->B_general_pk",
				"B_csourcebillbid->B_general_b_pk",
				"B_vsourcebillcode->H_vbillcode",
				"B_csourcetype->H_vbilltype",
				
				"B_cfirstbillhid->B_cfirstbillhid",
				"B_cfirstbillbid->B_cfirstbillbid",
				"B_vfirstbillcode->B_vfirstbillcode",
				"B_cfirsttype->B_cfirsttyp",
				"B_pk_invmandoc->B_cinventoryid",
				"B_pk_invbasdoc->B_cinvbasid",
				"B_cunitid->B_unitid",//�� ������λ
				"B_cassunitid->B_castunitid",//��������λ
				"B_noutnum->B_noutnum",//ʵ������
				"B_nassoutnum->B_noutassistnum",//ʵ��������
				"B_nshouldoutnum->B_nshouldoutnum",//Ӧ������
				"B_nassshouldoutnum->B_nshouldoutassistnum",//Ӧ��������
		};
	}



}