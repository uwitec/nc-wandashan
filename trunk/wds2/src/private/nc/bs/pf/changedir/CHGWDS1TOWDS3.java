package nc.bs.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * ���ڷ��˼ƻ�  ����ʱ ���� ���˶��� ����ת��  
 *  ע��  �ô����˼ƻ��ı���ʹ�õ��� plandealvo  �ƻ����ŵ�vo  zhf
 *
 * �������ڣ�(2004-11-18)
 * @author��ƽ̨�ű�����
 */
public class CHGWDS1TOWDS3 extends nc.bs.pf.change.VOConversion {
/**
 * CHG20TO21 ������ע�⡣
 */
public CHGWDS1TOWDS3() {
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
			//Ԥ���ֶ�
			"H_reserve2->H_reserve2",
			"H_reserve1->H_reserve1", 
			"H_pk_fdsyzc_h->H_reserve1", 
			"H_reserve3->H_reserve3",
			"H_reserve4->H_reserve4",
			"H_reserve5->H_reserve5",
			"H_reserve6->H_reserve6",
			"H_reserve7->H_reserve7",
			"H_reserve8->H_reserve8",
			"H_reserve9->H_reserve9",
			"H_reserve10->H_reserve10",
			"H_reserve11->H_reserve11",
			"H_reserve12->H_reserve12",
			"H_reserve13->H_reserve13",
			"H_reserve14->H_reserve14",
			"H_reserve15->H_reserve15",
			"H_reserve16->H_reserve16",
			//�Զ�����
			"H_vdef4->H_vdef4",
			"H_vdef3->H_vdef3",
			"H_vdef2->H_vdef2",
			"H_vdef1->H_vdef1",
			"H_vdef5->H_vdef5",
			"H_vdef6->H_vdef6",
			"H_vdef7->H_vdef7",
			"H_vdef8->H_vdef8",
			"H_vdef9->H_vdef9",
			"H_vdef10->H_vdef10",
			"H_vdef11->H_vdef11",
			"H_vdef12->H_vdef12",
			"H_vdef13->H_vdef13",
			"H_vdef14->H_vdef14",
			"H_vdef15->H_vdef15",
			"H_vdef16->H_vdef16",
			"H_vdef17->H_vdef17",
			"H_vdef18->H_vdef18",
			"H_vdef19->H_vdef19",
			"H_vdef20->H_vdef20",
		};
}
/**
* ��ù�ʽ��
* @return java.lang.String[]
*/
public String[] getFormulas() {
	return new String[] {
		"H_cvendorbaseid->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,B_cvendormangid)",
		"H_forderstatus->int(0)",
		"H_dr->int(0)",
		"H_bislatest->\"Y\"",
		"H_bisreplenish->\"N\"",
		"H_nversion->int(1)",
		"H_cdeptid->getColValue(bd_psndoc,pk_deptdoc,pk_psndoc,H_cemployeeid)",
		"H_breturn->\"N\"",
		"H_bdeliver->\"N\"",
		"B_cupsourcebilltype->\"20\"",
		"B_iisactive->int(0)",
		"B_forderrowstatus->int(0)",
		"B_idiscounttaxtype->int(1)",
		"B_iisreplenish->int(0)",
		"B_ndiscountrate->int(100)",
		"B_dr->int(0)",
		"B_status->int(2)",
		"B_vreceiveaddress->getColValue(bd_stordoc,storaddr,pk_stordoc,B_cwarehouseid)",
		"B_breceiveplan->\"N\"",
		"B_blargess->\"N\""
	};
}
/**
* �����û��Զ��庯����
*/
public UserDefineFunction[] getUserDefineFunction() {
	return null;
}
}
