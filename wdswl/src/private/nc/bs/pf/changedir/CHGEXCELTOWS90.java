package nc.bs.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;

/**
 * 分仓客商绑定  excel 导入  数据转换
 *
 * 创建日期：(2004-11-18)
 * @author：平台脚本生成
 */

public class CHGEXCELTOWS90 extends nc.bs.pf.change.VOConversion {
/**
 * CHG20TO21 构造子注解。
 */
public CHGEXCELTOWS90() {
	super();
}
/**
* 获得后续类的全录经名称。
* @return java.lang.String[]
*/
public String getAfterClassName() {
	return null;
}
/**
* 获得另一个后续类的全录径名称。
* @return java.lang.String[]
*/
public String getOtherClassName() {
	return null;
}
/**
* 获得字段对应。
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
			"B_pk_stordoc->H_outwh"//发货仓库
	};
}
/**
* 获得公式。
* @return java.lang.String[]
*/
public String[] getFormulas() {
	return new String[]{
			
	};
}
/**
* 返回用户自定义函数。
*/
public UserDefineFunction[] getUserDefineFunction() {
	return null;
}
}
