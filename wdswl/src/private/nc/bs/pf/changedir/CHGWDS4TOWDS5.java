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
			
			"H_pk_corp->SYSCORP",//公司
			"H_voperatorid->SYSOPERATOR",//操作员
			//
			"H_pk_cumandoc->H_ccustomerid",//收货客户
			"H_pk_deptdoc->H_cdeptid",//部门
			"H_vemployeeid->H_cemployeeid",//业务员
			"H_pk_busitype->H_cbiztype",//业务类型(销售订单表头)
			"H_csalecorpid->H_csalecorpid",//销售组织(销售订单表头)
			"H_ccalbodyid->B_cadvisecalbodyid",//库存组织(销售订单表体)(建议发货库存组织)
			"H_creceiptcustomerid->B_creceiptcorpid",//收货单位(销售订单表体)
			"H_vinaddress->B_vreceiveaddress",//收货地址(销售订单表体)
			"B_csourcebillhid->B_csaleid",
			"B_csourcebillbid->B_corder_bid",
			"B_vsourcebillcode->B_vreceiptcode",
			"B_csourcetype->H_creceipttype",//单据类型
			"B_vsourcerowno->B_crowno",
			"B_cfirstbillhid->B_csaleid",
			"B_cfirstbillbid->B_corder_bid",
			"B_vfirstbillcode->B_vreceiptcode",
			"B_vfirstrowno->B_crowno",
			
			"B_pk_invmandoc->B_cinventoryid",
			"B_pk_invbasdoc->B_cinvbasdocid",
			
			"B_uint->B_cunitid",//主计量单位
			"B_assunit->B_cpackunitid",//辅计量单位			
			"B_picicode->B_cbatchid",//批次
			"B_fisgift->B_blargessflag",//是否赠品
			"B_bisdate->B_disdate",//是否大日期
			"B_nassarrangnum->B_nassnum",//安排辅数量
			"B_narrangnmu->B_nnum",//安排数量
			"B_isxnap->B_isxnap"//是否虚拟安排 liuys add
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
			"H_pk_outwhouse->B_cbodywarehouseid",
			"H_pk_billtype->\""+WdsWlPubConst.WDS5+"\"",
			"H_vbillstatus->int(8)",
			"H_itranstype->int(0)",//运输方式
		    "B_cfirsttype->\""+30+"\"",
		    "H_dmakedate->\""+m_strDate+"\"",
		    "H_dbilldate->\""+m_strDate+"\"",
		    "H_dbegindate->\""+m_strDate+"\"",
		    "H_pk_cubasdoc->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,H_ccustomerid)",
			"B_csourcetype->\""+30+"\"",
			"B_nhgrate->getColValue(bd_measdoc,scalefactor,pk_measdoc,B_assunit)",//换算率[辅单位]
	};
}
/**
* 返回用户自定义函数。
*/
public UserDefineFunction[] getUserDefineFunction() {
	return null;
}
}
