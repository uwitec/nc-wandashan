package nc.bs.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;

/**
 * �ֲֿ��̰�  excel ����  ����ת��
 *
 * �������ڣ�(2004-11-18)
 * @author��ƽ̨�ű�����
 */

public class CHGEXCELTOWS90 extends nc.bs.pf.change.VOConversion {
/**
 * CHG20TO21 ������ע�⡣
 */
public CHGEXCELTOWS90() {
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
			"H_pk_corp->H_corp",
			"H_pk_stordoc->H_outwh",
			"H_pk_sendareacl->H_outarea",
			
			"B_pk_corp->H_corp",
			"B_pk_cumandoc->B_cust",
			"B_pk_stordoc1->B_inwh",
			"B_kilometer->B_gls",
			"B_custareaid->B_area",
			"B_vnote->B_memo",
			"B_ndef1->B_outnum",
			"B_pk_defdoc->B_saleareaname",
			"B_pk_stordoc->H_outwh"//�����ֿ�
	};
}
/**
* ��ù�ʽ��
* @return java.lang.String[]
*/
public String[] getFormulas() {
	return new String[]{
			
	};
}
/**
* �����û��Զ��庯����
*/
public UserDefineFunction[] getUserDefineFunction() {
	return null;
}
}
