package nc.bs.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * �ֲ��˼۱�  excel ����  ����ת��
 *
 * �������ڣ�(2004-11-18)
 * @author��ƽ̨�ű�����
 */

public class CHGEXCELTOWDSK extends nc.bs.pf.change.VOConversion {
/**
 * CHG20TO21 ������ע�⡣
 */
public CHGEXCELTOWDSK() {
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
			"H_reserve1->H_storcode",
			"H_carriersid->H_transcode",
			"H_nmincase->H_min",
			"H_nmaxcase->H_max",
			
			"B_pk_replace->B_areaname",
			"B_reserve1->B_yf",
			"B_ntransprice->B_fee",
			"B_reserve2->B_type",
			"B_denddate->B_days",
	};
}
/**
* ��ù�ʽ��
* @return java.lang.String[]
*/
public String[] getFormulas() {
	return new String[]{
			"H_pk_corp->1021",	
			"H_"
	};
}
/**
* �����û��Զ��庯����
*/
public UserDefineFunction[] getUserDefineFunction() {
	return null;
}
}
