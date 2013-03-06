package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * 
 *
 * 创建日期：(2004-11-18)
 * @author：平台脚本生成
 * ERP调拨出库单--物流调拨入库(WDS9)
 */
public class CHG4YTOWDS9 extends nc.ui.pf.change.VOConversionUI {
	

	public CHG4YTOWDS9() {
		super();
	}

	public String getAfterClassName() {
		return "nc.vo.ic.other.in.ChgtoOtherIn";
	}

	public String getOtherClassName() {
		return null;
	}

	public String[] getField() {
		return new String[] {
			"H_geh_cbiztype->H_cbiztype",//业务类型
			"H_geh_fallocflag->H_fallocflag",//调拨类型标志

//			"H_geh_cdispatcherid->H_cdispatcherid",//收发类别[需要手工输入] 这是入库类别
			
			
//			"H_geh_cwhsmanagerid->H_cwhsmanagerid",//库管员
//			"H_ ->H_ccustomerid",//客户
//			"H_ ->H_vdiliveraddress",//收货地址[发运地址]
//			"H_ ->H_coperatorid",//制单人
//			"B_creceieveid->H_creceiptcustomerid",//收货单位管理id
//			"B_pk_cubasdocrev",//收货单位基本ID	
			
			
			"H_pk_corp ->H_cothercorpid",//调拨入公司
			"H_geh_calbody->H_cothercalbodyid",//调拨入库存组织
			"H_geh_cwarehouseid->H_cotherwhid",//调拨入库仓库
			
			"H_geh_cothercorpid->H_pk_corp",//供应链调拨出公司
			"H_geh_cothercalbodyid->H_pk_calbody",//供应链调出库存组织
			"H_geh_cotherwhid->H_cwarehouseid",//供应链调拨出库仓库
			
//			"H_geh_cdptid->H_cdptid",//部门 [两公司不同步]
//			"H_geh_cbizid->H_cbizid",//业务员 [两公司不同步]
			

			"B_geb_cinvbasid->B_cinvbasid",//存货基本档案ID   
//			"B_geb_cinventoryid->B_cinventoryid",//存货管理ID  	
			"B_pk_measdoc->B_pk_measdoc",//主单位
			"B_castunitid->B_castunitid",//辅单位
//			"B_geb_hsl->B_hsl",//换算率 
			
			"B_geb_proddate->B_scrq",//生产日期
			"B_geb_dvalidate->B_dvalidate",//失效日期

			
//			"B_geb_banum->B_noutassistnum",//实发辅数量
//			"B_geb_anum->B_noutnum",//实发数量			
			"B_geb_nprice->B_nprice",//单价
			"B_geb_nmny->B_nmny",//金额
			
		//	"B_geb_vbatchcode->B_vbatchcode",//批次号			
			"B_geb_backvbatchcode->B_vbatchcode",//回写批次号			
			"B_geb_flargess->B_flargess",//是否赠品
			"B_geb_space->B_cspaceid",//货位ID
			 
			"B_csourcebillhid->B_csourcebillhid",//[调拨订单主键]
			"B_csourcebillbid->B_csourcebillbid",//[调拨订单子表主键]
			"B_vsourcebillcode->B_vsourcebillcode",//[调拨订单单据号]
			"B_csourcetype->B_csourcetype",//[调拨订单单据类型]
			
			"B_cfirstbillhid->B_cfirstbillhid",// [调拨订单主键]
			"B_cfirstbillbid->B_cfirstbillbid",//   [调拨订单子表主键]
			"B_vfirstbillcode->B_vfirstbillcode",//[调拨订单单据号 ]
			"B_cfirsttype->B_cfirsttype",//[调拨订单单据类型]
			
			
			"B_gylbillhid->B_cgeneralhid",//[调拨出库单主键 ]
			"B_gylbillbid->B_cgeneralbid",//[调拨出库单子表主键]
			"B_gylbillcode->H_vbillcode",//[ 调拨出库单单据号]
			"B_gylbilltype->H_cbilltypecode"//[调拨出库单单据类型--4Y ]
		
//			"B_geb_dbizdate->B_dbizdate"//业务日期 [重新赋值，入库日期]
		};
	}

	public String[] getFormulas() {
		return new String[] {
				"H_geh_billtype->\"WDS9\"",
				"H_geh_cbilltypecode->\"WDS9\"",
				"B_geb_hsl->iif(B_hsl==null,B_noutnum/B_noutassistnum,B_hsl)",
				"B_geb_cinventoryid->getColValue2(bd_invmandoc,pk_invmandoc,pk_invbasdoc,B_cinvbasid,pk_corp,H_cothercorpid)",//根据基本ID，公司,获取管理ID			
				
				"B_geb_snum->B_noutnum-B_"+WdsWlPubConst.erp_allo_outnum_fieldname,//应发数量 
//				"B_geb_bsnum->(B_noutnum-B_"+WdsWlPubConst.erp_allo_outnum_fieldname+")/B_geb_hsl",//应发辅数量
				
//				zhf add 运单类型
				"B_geb_customize9->\"WS21\""
		};
	}
	/**
	* 返回用户自定义函数。
	*/
	public UserDefineFunction[] getUserDefineFunction() {
		return null;
	}
}
