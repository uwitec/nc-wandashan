package nc.bs.pf.changedir;

import nc.bs.pf.change.VOConversion;
import nc.vo.pub.lang.UFDate;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 *��������->װж�ѽ���
 * @author Administrator
 *
 */
public class CHGWDS6TOWDSF extends VOConversion {
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
	
	/**
	* ��ù�ʽ��
	* @return java.lang.String[]
	*/
	public String[] getFormulas() {
		new UFDate(System.currentTimeMillis());
		super.setSysDate(new UFDate(System.currentTimeMillis()).toString());
		return new String[] {
//			"H_cvendorbaseid->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,B_cvendormangid)",
//			"H_forderstatus->int(0)",
				"H_pk_billtype->\""+WdsWlPubConst.WDSF+"\"",
				"H_vbillstatus->int(8)",
			    "H_dmakedate->\""+m_strDate+"\"",
			    "H_dbilldate->\""+m_strDate+"\""
		};
	}

}
