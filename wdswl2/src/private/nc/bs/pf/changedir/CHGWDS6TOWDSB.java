package nc.bs.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
import nc.vo.pub.lang.UFDate;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * 其他出库单生成  运输确认单
 *
 * 创建日期：(2004-11-18)
 * @author：平台脚本生成
 */
public class CHGWDS6TOWDSB extends nc.bs.pf.change.VOConversion {
/**
 * CHG20TO21 构造子注解。
 */
public CHGWDS6TOWDSB() {
	super();
}
/**
* 获得后续类的全录经名称。
* @return java.lang.String[]
*/
public String getAfterClassName() {
	return "nc.bs.wds2.conversion.AfterWDSChg";
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
			
	};
}
/**
* 获得公式。
* @return java.lang.String[]
*/
public String[] getFormulas() {
	new UFDate(System.currentTimeMillis());
	super.setSysDate(new UFDate(System.currentTimeMillis()).toString());
	return new String[] {
//		"H_cvendorbaseid->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,B_cvendormangid)",
//		"H_forderstatus->int(0)",
			"H_pk_billtype->\""+WdsWlPubConst.WDS3+"\"",
			"H_vbillstatus->int(8)",
		    "B_csourcetype->\""+WdsWlPubConst.WDS1+"\"",
		    "H_fisbigglour->\"N\"",
		    "H_dmakedate->\""+m_strDate+"\"",
		    "H_dbilldate->\""+m_strDate+"\"",
		    "H_dbegindate->\""+m_strDate+"\""
	};
}
/**
* 返回用户自定义函数。
*/
public UserDefineFunction[] getUserDefineFunction() {
	return null;
}
}
