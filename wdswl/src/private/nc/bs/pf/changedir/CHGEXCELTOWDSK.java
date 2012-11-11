package nc.bs.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * 分仓运价表  excel 导入  数据转换
 *
 * 创建日期：(2004-11-18)
 * @author：平台脚本生成
 */

public class CHGEXCELTOWDSK extends nc.bs.pf.change.VOConversion {
/**
 * CHG20TO21 构造子注解。
 */
public CHGEXCELTOWDSK() {
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
* 获得公式。
* @return java.lang.String[]
*/
public String[] getFormulas() {
	return new String[]{
			"H_pk_corp->1021",	
			"H_"
	};
}
/**
* 返回用户自定义函数。
*/
public UserDefineFunction[] getUserDefineFunction() {
	return null;
}
}
