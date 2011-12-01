package nc.bs.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
import nc.vo.pub.lang.UFDate;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * 用于发运计划  安排时 生成 发运订单 数据转换  
 *  注意  该处发运计划的表体使用的是 plandealvo  计划安排的vo  zhf
 *
 * 创建日期：(2004-11-18)
 * @author：平台脚本生成
 */
public class CHGWDS1TOWDS3 extends nc.bs.pf.change.VOConversion {
/**
 * CHG20TO21 构造子注解。
 */
public CHGWDS1TOWDS3() {
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
			"H_pk_outwhouse->H_pk_outwhouse",
			"H_pk_inwhouse->H_pk_inwhouse",
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
			//自定义项
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
			
			"B_csourcebillhid->B_pk_sendplanin",
			"B_csourcebillbid->B_pk_sendplanin_b",
			"B_vsourcebillcode->B_vbillno",
			"B_csourcetype->H_pk_billtype",
			
			"B_cfirstbillhid->B_pk_sendplanin",
			"B_cfirstbillbid->B_pk_sendplanin_b",
			"B_vfirstbillcode->B_vbillno",
			
			"B_pk_invmandoc->B_pk_invmandoc",
			"B_pk_invbasdoc->B_pk_invbasdoc",
			"B_unit->B_unit",//主 计量单位
			"B_assunit->B_assunit",//辅计量单位
			"B_nassplannum->B_nassplannum",//计划安排辅数量
			"B_nplannum->B_nplannum",//计划安排数量
			"B_ndealnum->B_nnum",//本次安排数量
			"B_nassdealnum->B_nassnum",//本次安排辅数量
			"B_nhsl->B_hsl",//换算率
			"B_bisdate->B_bisdate",//是否大日期
			"B_reserve2->B_reserve2",
			"B_reserve1->B_reserve1", 
			"B_pk_fdsyzc_h->B_reserve1", 
			"B_reserve3->B_reserve3",
			"B_reserve4->B_reserve4",
			"B_reserve5->B_reserve5",
			"B_reserve6->B_reserve6",
			"B_reserve7->B_reserve7",
			"B_reserve8->B_reserve8",
			"B_reserve9->B_reserve9",
			"B_reserve10->B_reserve10",
			"B_reserve11->B_reserve11",
			"B_reserve12->B_reserve12",
			"B_reserve13->B_reserve13",
			"B_reserve14->B_reserve14",
			"B_reserve15->B_reserve15",
			"B_reserve16->B_reserve16",
			
		
			//自定义项
			"B_vdef4->B_vdef4",
			"B_vdef3->B_vdef3",
			"B_vdef2->B_vdef2",
			"B_vdef1->B_vdef1",
			"B_vdef5->B_vdef5",
			"B_vdef6->B_vdef6",
			"B_vdef7->B_vdef7",
			"B_vdef8->B_vdef8",
			"B_vdef9->B_vdef9",
			"B_vdef10->B_vdef10",
			"B_vdef11->B_vdef11",
			"B_vdef12->B_vdef12",
			"B_vdef13->B_vdef13",
			"B_vdef14->B_vdef14",
			"B_vdef15->B_vdef15",
			"B_vdef16->B_vdef16",
			"B_vdef17->B_vdef17",
			"B_vdef18->B_vdef18",
			"B_vdef19->B_vdef19",
			"B_vdef20->B_vdef20",
		};
}
/**
* 获得公式。
* @return java.lang.String[]
*/
public String[] getFormulas() {
	if(m_strDate == null){
		new UFDate(System.currentTimeMillis());
		super.setSysDate(new UFDate(System.currentTimeMillis()).toString());
	}
	return new String[] {
			"H_pk_corp->\""+m_strCorp+"\"",
			"H_voperatorid->\""+m_strOperator+"\"",
			"H_pk_billtype->\""+WdsWlPubConst.WDS3+"\"",
			"H_vbillstatus->int(8)",
		    "B_csourcetype->\""+WdsWlPubConst.WDS1+"\"",
		    "H_fisbigglour->\"N\"",
		    "H_dmakedate->\""+m_strDate+"\"",
		    "H_dbilldate->\""+m_strDate+"\"",
		    "H_dbegindate->\""+m_strDate+"\"",
		    "H_itransstatus->\""+0+"\""
	};
}
/**
* 返回用户自定义函数。
*/
public UserDefineFunction[] getUserDefineFunction() {
	return null;
}
}
