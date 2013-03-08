package nc.bs.pf.changedir;
import nc.vo.pf.change.UserDefineFunction;
import nc.vo.pub.lang.UFDate;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * 用于调拨  安排时 生成  调拨订单->调出运单数据交换 *
 * 创建日期：(2004-11-18)
 * @author：平台脚本生成
 */
public class CHGWDSBTOWDSG extends nc.bs.pf.change.VOConversion {
/**
 * CHG20TO21 构造子注解。
 */
public CHGWDSBTOWDSG() {
	super();
}
/**
* 获得后续类的全录经名称。
* @return java.lang.String[]
*/
public String getAfterClassName() {
	return "nc.bs.wds2.conversion.AfterWDSBTOWDSG";
}
/**
* 获得另一个后续类的全录径名称。
* @return java.lang.String[]
*/
public String getOtherClassName() {
	return null;
}
public String[] getField() {
	return new String[] {		
			"H_pk_outwhouse->B_coutwhid",//调出仓库 --->发货仓库
			"H_vdef9->H_ctakeoutspaceid",//调出货位			
			"H_vmemo->H_vnote",//备注
			"H_vdef3->B_coutcorpid",//调出公司
			"H_vdef2->B_coutcbid",//调出库存组织			
			"H_vdef1->B_cincbid",//调入库存组织
			"H_vdef5->B_cincorpid", //调入公司			
			"H_vdef7->B_cinwhid",//调入仓库
    //		"H_vdef6->B_fallocflag",//调拨类型标志	
    //		"H_vdef4->B_coutdeptid",//调出部门
	//		"H_vdef8->B_coutpsnid",//调出部门业务员
	//	    "H_reserve14->B_bretractflag",//是否退回			
			"B_csourcebillhid->B_cbillid",
			"B_csourcebillbid->B_cbill_bid",
			"B_vsourcebillcode->B_vcode",
			"B_csourcetype->B_ctypecode",				
			"B_cfirstbillhid->B_cbillid",
			"B_cfirstbillbid->B_cbill_bid",
			"B_vfirstbillcode->H_vcode",	
			"B_cfirsttype->B_ctypecode",			
			"B_pk_invmandoc->B_ctakeoutinvid",//存货管理档案id
			"B_pk_invbasdoc->B_cinvbasid",//存货基本档案id
			"B_vdef1->B_vdef1",//存货状态	
			"B_vdef2->B_vbatch",//批次	
			"B_bisdate->B_flargess",//是赠品		
			"B_nhsl->B_nchangeratel",//换算率
			"B_assunit->B_castunitid",//辅计量单位
			"B_nassplannum->B_nassistnum",//调拨订单辅数量
			"B_nplannum->B_nnum",//调拨订单数量
			"B_ndealnum->B_num",//本次安排数量
			"B_nassdealnum->B_nassnum",//本次安排辅数量															
			"B_vdef9->B_cinwhid",//调入仓库
			"B_vdef7->B_cincbid",//调入库存组织
			"B_vdef5->B_cincorpid", //调入公司	   		
			"B_vdef3->B_creceieveid",//收货单位			
			"B_reserve1->B_vreceiveaddress",//收货地址
	//		"B_vdef10->B_pk_sendtype",//运送方式			
	//		"B_vdef6->B_fallocflag",//调拨类型标志		
    //		"B_vdef4->B_coutdeptid",//调出部门		
	//		"B_vdef8->B_coutpsnid",//调出部门业务员
	//	    "B_reserve14->B_bretractflag",//是否退回	
	//		"B_reserve2->B_cprojectphase",//项目阶段
	//		"B_reserve3->B_cprojectid",//项目主键
	//		"B_reserve4->B_cvendorid",//供应商
	//      "B_reserve5->B_ctypecode",//订单类型	
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
			"H_pk_billtype->\""+WdsWlPubConst.WDSG+"\"",
			"H_vbillstatus->int(8)",
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
