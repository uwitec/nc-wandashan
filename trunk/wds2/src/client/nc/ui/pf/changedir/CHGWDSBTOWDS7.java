package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * ������ⵥ��������ȷ�ϵ����� zhf
 *
 * �������ڣ�(2004-11-18)
 * @author��ƽ̨�ű�����
 */
public class CHGWDSBTOWDS7 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHG20TO21 ������ע�⡣
 */
public CHGWDSBTOWDS7() {
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
/**
* ����ֶζ�Ӧ��
* @return java.lang.String[]
*/
public String[] getField() {
	return new String[] {
			"H_geh_vbillcode->H_fyd_ddh",//��Դ���ݺ� --����ȷ�ϵ����ݺ�
			"H_geh_cgeneralhid->H_fyd_pk",//��Դ����ͷid
			"H_geh_cotherwhid->H_srl_pk",//����ֿ�
			"H_geh_cbizid->H_pk_psndoc",//ҵ��Ա
			"H_geh_cwarehouseid->H_srl_pkr",//���ֿ�
			"H_geh_cbiztype->H_pk_busitype",//ҵ������
//			"H_copetadate->SYSOPERATOR",
//			"H_geh_dbilldate->",
			"B_geb_cinvbasid->B_pk_invbasdoc",
			"B_geb_cgeneralhid->B_fyd_pk",//��Դ�˵�id
			"B_geb_cgeneralbid->B_cfd_pk",//��Դ��id
			"B_geb_vbatchcode->B_cfd_lpc",//���κ�
			"B_geb_flargess->B_blargessflag",//�Ƿ���Ʒ
			"B_geb_bsnum->B_cfd_sffsl",// Ӧ�ո�����
			"B_geb_snum->B_cfd_sfsl",//Ӧ��������
			"B_crowno->B_crowno"
	};
}
/**
* ��ù�ʽ��
* @return java.lang.String[]
*/
public String[] getFormulas() {
	return new String[] {
			
			"H_geh_billtype->\"WDS7\"",
//			"";
//			
//		"H_cvendorbaseid->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,B_cvendormangid)",
//		"H_forderstatus->0",
//		"H_dr->0",
//		"H_bislatest->\"Y\"",
//		"H_bisreplenish->\"N\"",
//		"H_nversion->1",
//		"H_cdeptid->getColValue(bd_psndoc,pk_deptdoc,pk_psndoc,H_cemployeeid)",
//		"H_breturn->\"N\"",
//		"H_bdeliver->\"N\"",
//		"B_cupsourcebilltype->\"20\"",
//		"B_iisactive->0",
//		"B_forderrowstatus->0",
//		"B_idiscounttaxtype->1",
//		"B_iisreplenish->0",
//		"B_ndiscountrate->100",
//		"B_dr->0",
//		"B_status->2",
//		"B_vreceiveaddress->getColValue(bd_stordoc,storaddr,pk_stordoc,B_cwarehouseid)",
//		"B_breceiveplan->\"N\"",
//		"B_blargess->\"N\""
	};
}
/**
* �����û��Զ��庯����
*/
public UserDefineFunction[] getUserDefineFunction() {
	return null;
}
}
