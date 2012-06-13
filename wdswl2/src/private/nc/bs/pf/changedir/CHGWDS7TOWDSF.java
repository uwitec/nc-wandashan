package nc.bs.pf.changedir;

import nc.bs.pf.change.VOConversion;
import nc.vo.pub.lang.UFDate;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * �������->װж�ѽ���
 * @author Administrator
 *
 */
public class CHGWDS7TOWDSF extends VOConversion {
	
	/**
	* ��ú������ȫ¼�����ơ�
	* @return java.lang.String[]
	*/
	public String getAfterClassName() {
		return "nc.bs.pub.chgafter.WDS7TOWDSFAfterDeal";
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
		
		return new String[]{
				"H_pk_corp->H_pk_corp",//��˾
				"H_pk_stordoc->H_geh_cwarehouseid",//�ֿ�
				"B_csourcebillhid->B_geh_pk",
				"B_csourcebillbid->B_geb_pk",
				"B_vsourcebillcode->H_geh_billcode",
				"B_csourcetype->H_geh_billtype",
				
				"B_cfirstbillhid->B_cfirstbillhid",
				"B_cfirstbillbid->B_cfirstbillbid",
				"B_vfirstbillcode->B_vfirstbillcode",
				"B_cfirsttype->B_cfirsttype",
				"B_pk_invmandoc->B_geb_cinventoryid",
				"B_pk_invbasdoc->B_geb_cinvbasid",
				"B_cunitid->B_pk_measdoc",//�� ������λ
				"B_cassunitid->B_castunitid",//��������λ
				"B_noutnum->B_geb_anum",//ʵ������
				"B_nassoutnum->B_geb_banum",//ʵ�븨����
				"B_nshouldoutnum->B_geb_snum",//Ӧ������
				"B_nassshouldoutnum->B_geb_bsnum",//Ӧ�븨����
				
				"B_nhsl->B_geb_hsl",
				"B_vbatchecode->B_geb_vbatchcode",
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
