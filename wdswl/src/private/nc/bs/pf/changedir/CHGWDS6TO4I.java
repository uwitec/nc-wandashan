package nc.bs.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * 本地其它出库->供应链其它出库
 * @author zpm
 *
 */
public class CHGWDS6TO4I  extends nc.bs.pf.change.VOConversion {

	public CHGWDS6TO4I() {
		super();
	}


	public String getAfterClassName() {
		return null;
	}

	public String getOtherClassName() {
	  return null;
	}

	public String[] getField() {
		return new String[] {
//				"H_vbillcode",//单据号--------系统判断，如果不存在，则自动生成
//				"B_crowno",//行号
//				"coperatorid", //操作员
//				"H_dbilldate->SYSDATE",//单据日期
				
				"H_cotherwhid->H_srl_pkr",//入库仓库
				"H_cwarehouseid->H_srl_pk",//仓库
				"H_pk_calbody->H_pk_calbody",//库存组织
				"H_cbiztype->H_cbiztype",//业务类型
				"H_cdispatcherid->H_cdispatcherid",//收发类别
				"H_cwhsmanagerid->H_cwhsmanagerid",//库管员
				"H_cdptid->H_cdptid",//部门
				"H_cbizid->H_cbizid",//业务员
				"H_ccustomerid->H_ccustomerid",//客户
				"H_vdiliveraddress->H_vdiliveraddress",//收货地址[发运地址]
				"H_pk_corp->H_pk_corp",//公司
				"H_coperatorid->H_coperatorid",//制单人
				
				"B_creceieveid->H_creceiptcustomerid",//收货单位管理id
//				"B_pk_cubasdocrev",//收货单位基本ID
				
				"B_cinvbasid->B_cinvbasid",//存货基本档案ID   
				"B_cinventoryid->B_cinventoryid",//存货管理ID  	
				"B_pk_measdoc->B_unitid",//主单位
				"B_castunitid->B_castunitid",//辅单位
				"B_hsl->B_hsl",//换算率 
				"B_scrq->B_cshengchanriqi",//生产日期-----------------------------zpm
				"B_dvalidate->B_cshixiaoriqi",//失效日期------------------------------zpm
				"B_nshouldoutassistnum->B_nshouldoutassistnum",//应发辅数量
				"B_nshouldoutnum->B_nshouldoutnum",//应发数量 
				"B_noutassistnum->B_noutassistnum",//实发辅数量
				"B_noutnum->B_noutnum",//实发数量
				"B_nprice->B_nprice",//单价
				"B_nmny->B_nmny",//金额
				"B_vbatchcode->B_vbatchcode",//批次号	------------------------------zpm
				
				"B_flargess->B_flargess",//是否赠品
				"B_cspaceid->B_cspaceid",//货位ID
				 
				"B_csourcebillhid->B_general_pk",//[供应链字段 [其他出库]-------理论为空，实际保存 本地其他出库值]
				"B_csourcebillbid->B_general_b_pk",//[供应链字段 [其他出库]-------理论为空，实际保存 本地其他出库值]
				"B_vsourcebillcode->H_vbillno",//[供应链字段 [其他出库]-------理论为空，实际保存 本地其他出库值]
				"B_csourcetype->H_vbilltype",//[供应链字段 [其他出库]-------理论为空，实际保存 本地其他出库值]
				
				"B_cfirstbillhid->B_cfirstbillhid",// [供应链字段 [其他出库]-------理论为空，实际保存 本地其他出库值]
				"B_cfirstbillbid->B_cfirstbillbid",//  [供应链字段 [其他出库]-------理论为空，实际保存 本地其他出库值] 
				"B_vfirstbillcode->B_vfirstbillcode",//[供应链字段 [其他出库]-------理论为空，实际保存 本地其他出库值]
				"B_cfirsttype->B_cfirsttyp",//[供应链字段 [其他出库]-------理论为空，实际保存 本地其他出库值]
				
				"B_dbizdate->B_dbizdate"//出库日期->业务日期-------------------------zpm
				//nc.itf.pd.pd4010.IBomOperate报错
				//nplannedprice计划单价，如果没有计划单价，则从单据的计划价应该取自相应的成本型型库存组织，转向生产制造
				//正常其它出库，计划单价也为空
		};
	}
	

	public String[] getFormulas() {
		
		return new String[] {
				"H_cbilltypecode->\"4I\""
		};
	}
	/**
	* 返回用户自定义函数。
	*/
	public UserDefineFunction[] getUserDefineFunction() {
		return null;
	}
}
