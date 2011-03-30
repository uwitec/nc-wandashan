package nc.bs.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
import nc.vo.pub.lang.UFDate;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * 用于销售计划  安排时 生成 销售发运单 数据转换  
 *  注意  该处发运计划的表体使用的是 sodealvo 表头使用 saleorderhvo  计划安排的vo  zhf
 *
 * 创建日期：(2004-11-18)
 * @author：平台脚本生成
 */
public class CHGWDS4TOWDS5 extends nc.bs.pf.change.VOConversion {
/**
 * CHG20TO21 构造子注解。
 */
public CHGWDS4TOWDS5() {
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
			
			"H_pk_corp->SYSCORP",
			"H_voperatorid->SYSOPERATOR",
			"H_pk_outwhouse->B_cbodywarehouseid",//发货站
			"H_pk_cumandoc->B_ccustomerid",//收货客商
//			批次
			"B_picicode->B_cbatchid",
//          是否赠品
			"B_fisgift->B_blargessflag",
			
			
			"B_csourcebillhid->B_csaleid",
			"B_csourcebillbid->B_order_bid",
			"B_vsourcebillcode->H_vreceiptcode",
			
			"B_cfirstbillhid->B_csaleid",
			"B_cfirstbillbid->B_order_bid",
			"B_vfirstbillcode->H_vreceiptcode",
			
			"B_pk_invmandoc->B_cinventoryid",
			"B_pk_invbasdoc->B_cinvbasdocid",
			"B_unit->B_cunitid",//主 计量单位
			"B_assunit->B_cpackunitid",//辅计量单位
			
			"B_nassarrangnum->B_nassnum",//安排辅数量
			"B_narrangnmu->B_nnum",//安排数量
//			"B_ndealnum->B_nnum",//安排数量
			
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
			"H_pk_billtype->\""+WdsWlPubConst.WDS5+"\"",
			"H_vbillstatus->int(8)",
		    "B_csourcetype->\""+WdsWlPubConst.WDS4+"\"",
//		    "H_fisbigglour->\"N\"",
		    "H_dmakedate->\""+m_strDate+"\"",
		    "H_dbilldate->\""+m_strDate+"\"",
		    "H_dbegindate->\""+m_strDate+"\"",
		    "H_pk_cubasdoc->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,H_ccustomerid)",
//			来源单据类型
			"B_csourcetype->\""+WdsWlPubConst.WDS4+"\"",
	};
}
/**
* 返回用户自定义函数。
*/
public UserDefineFunction[] getUserDefineFunction() {
	return null;
}
}
