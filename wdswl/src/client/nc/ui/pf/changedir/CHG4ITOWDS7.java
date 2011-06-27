package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;

/**
 * 供应链其他出库->本地其他入库
 * @author Administrator
 *
 */
public class CHG4ITOWDS7 extends nc.ui.pf.change.VOConversionUI{
	

	public CHG4ITOWDS7() {
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
			
			"H_pk_corp ->H_pk_corp",//公司（同一家公司）
			"H_geh_calbody->H_cothercalbodyid",//入库存组织
			"H_geh_cwarehouseid->H_cotherwhid",//入库仓库			
			
			"H_geh_cothercalbodyid->H_pk_calbody",//供应链出库存组织
			"H_geh_cotherwhid->H_cwarehouseid",//供应链出库仓库
			
			"H_geh_cbiztype->H_cbiztype",//业务类型
			
//			"H_geh_cdispatcherid->H_cdispatcherid",//收发类别
//			"H_geh_cwhsmanagerid->H_cwhsmanagerid",//库管员
			
			
//			"H_ ->H_ccustomerid",//客户
//			"H_ ->H_vdiliveraddress",//收货地址[发运地址]
//			"H_ ->H_coperatorid",//制单人
//			"B_creceieveid->H_creceiptcustomerid",//收货单位管理id
//			"B_pk_cubasdocrev",//收货单位基本ID	
			
			
			
			"H_geh_cdptid->H_cdptid",//部门
			"H_geh_cbizid->H_cbizid",//业务员
			

			"B_geb_cinvbasid->B_cinvbasid",//存货基本档案ID   
			"B_geb_cinventoryid->B_cinventoryid",//存货管理ID  	
			"B_pk_measdoc->B_pk_measdoc",//主单位
			"B_castunitid->B_castunitid",//辅单位
			"B_geb_hsl->B_hsl",//换算率 
			"B_geb_proddate->B_scrq",//生产日期
			"B_geb_dvalidate->B_dvalidate",//失效日期
//			"B_geb_bsnum->B_nshouldoutassistnum",//应发辅数量
//			"B_geb_snum->B_nshouldoutnum",//应发数量 
//			
//			"B_geb_banum->B_noutassistnum",//实发辅数量
//			"B_geb_anum->B_noutnum",//实发数量
			
			
			"B_geb_bsnum->B_noutassistnum",//实发辅数量->应发辅数量
			"B_geb_snum->B_noutnum",//实发数量->应发数量
			
			"B_geb_nprice->B_nprice",//单价
			"B_geb_nmny->B_nmny",//金额
			"B_geb_vbatchcode->B_vbatchcode",//批次号	
			"B_geb_backvbatchcode->B_vbatchcode",//来源批次号
			"B_geb_flargess->B_flargess",//是否赠品
			"B_geb_space->B_cspaceid",//货位ID
			 
			"B_csourcebillhid->B_csourcebillhid",//[保存  物流 其他出字段]
			"B_csourcebillbid->B_csourcebillbid",//[保存  物流 其他出字段]
			"B_vsourcebillcode->B_vsourcebillcode",//[保存  物流 其他出字段]
			"B_csourcetype->B_csourcetype",//[保存  物流 其他出字段]
			
			"B_cfirstbillhid->B_cfirstbillhid",// [保存  物流 其他出字段]
			"B_cfirstbillbid->B_cfirstbillbid",//   [保存  物流 其他出字段]
			"B_vfirstbillcode->B_vfirstbillcode",//[保存  物流 其他出字段 ]
			"B_cfirsttype->B_cfirsttype",//
			
			"B_gylbillcode->H_vbillcode",//[保存  保存供应链 其他出字段 ]
			"B_gylbilltype->H_cbilltypecode",//[保存  保存供应链 其他出字段 ]
			"B_gylbillhid->B_cgeneralhid",//[保存  保存供应链 其他出字段 ]
			"B_gylbillbid->B_cgeneralbid"//[保存  保存供应链 其他出字段 ]
//			"B_geb_dbizdate->B_dbizdate"//业务日期 [重新赋值，入库日期]
		};
	}

	public String[] getFormulas() {
		return new String[] {
			"H_geh_cbilltypecode->\"WDS7\"",
			"B_geb_vbatchcode->getColValue(tb_outgeneral_b,vbatchcode,general_b_pk,B_csourcebillbid)"
		//	"B_status->2",//不行，vo状态
		//	"H_status->2"//不行，vo状态
		};
	}
	/**
	* 返回用户自定义函数。
	*/
	public UserDefineFunction[] getUserDefineFunction() {
		return null;
	}
}
