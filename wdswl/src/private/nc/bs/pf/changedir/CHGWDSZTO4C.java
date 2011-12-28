package nc.bs.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;

public class CHGWDSZTO4C extends nc.bs.pf.change.VOConversion {

	public CHGWDSZTO4C() {
		super();
	}


	public String getAfterClassName() {
		return "nc.bs.ic.pub.pfconv.HardLockChgVO";
	}

	public String getOtherClassName() {
		return "nc.ui.ic.pub.pfconv.HardLockChgVO";
	}

	public String[] getField() {
		return new String[] {
//				"H_vbillcode",//单据号--------系统判断，如果不存在，则自动生成
//				"B_crowno",//行号
//				"coperatorid", //操作员
//				"H_dbilldate->SYSDATE",//单据日期	
				
				"H_ccustomerid->H_geh_customize8",//客户
//				"H_coperatorid->SYSOPERATOR",
//				"H_dbilldate->SYSDATE",
//				"H_coperatoridnow->SYSOPERATOR",
				
				"H_cwarehouseid->H_geh_cwarehouseid",//仓库
//				"H_pk_calbody->H_pk_calbody",//库存组织
				"H_cbiztype->H_geh_cbiztype",//业务类型
				"H_cdispatcherid->H_geh_cdispatcherid",//收发类别
				"H_cwhsmanagerid->H_geh_cwhsmanagerid",//库管员
				"H_cdptid->H_geh_cdptid",//部门
				"H_cbizid->H_geh_cbizid",//业务员
		//		"H_ccustomerid->H_ccustomerid",//客户
				"H_vdiliveraddress->H_vdiliveraddress",//收货地址[发运地址]
				"H_pk_corp->H_pk_corp",//公司
				"H_coperatorid->H_coperatorid",//制单人
				
				"B_creceieveid->H_creceiptcustomerid",//收货单位管理id
//				"B_pk_cubasdocrev",//收货单位基本ID
				
				"B_cinvbasid->B_geb_cinvbasid",//存货基本档案ID   
				"B_cinventoryid->B_geb_cinventoryid",//存货管理ID  	
				"B_pk_measdoc->B_pk_measdoc",//主单位
				"B_castunitid->B_castunitid",//辅单位
				"B_hsl->B_geb_hsl",//换算率 
				"B_scrq->B_geb_proddate",//生产日期-----------------------------zpm
				"B_dvalidate->B_geb_dvalidate",//失效日期------------------------------zpm
				"B_pk_corp->H_pk_corp",
				"B_nprice->B_geb_nprice",//单价
				
				"B_vbatchcode->B_geb_vbatchcode",//批次号	------------------------------zpm
				
				"B_flargess->B_geb_flargess",//是否赠品
				"B_cspaceid->B_geb_space",//货位ID
				 
				"B_csourcebillhid->B_cfirstbillhid",//本地销售出库 源头单据表头ID[销售 订单]  
				"B_csourcebillbid->B_cfirstbillbid",//本地销售出库 源头单据表体ID  [销售 订单]  
				"B_vsourcebillcode->B_vfirstbillcode",//本地销售出库 源头单据号[销售 订单]   
				"B_csourcetype->B_cfirsttype",//本地销售出库 源头单据类型编码[销售 订单]   
				
				"B_cfirstbillhid->B_geh_pk",// --销售退货入库单 审批生成erp销售出库单的时候，用来保存销售出库回传单主键，才支持联查;所有生成的ERP销售出库
		
				"B_cfirstbillbid->B_geb_pk",//--销售退货入库单，审批生成erp销售出库单的时候，根据表体ID来查找货位信息   
				"B_vfirstbillcode->H_geh_billcode",//
				"B_cfirsttype->H_geh_billtype",//
				
				"H_freplenishflag->H_freplenishflag",//是否退货
				"H_boutretflag->H_boutretflag",//是否退回
				"B_dbizdate->H_geh_dbilldate"//出库日期->业务日期-------------------------zpm
		};
	}

	public String[] getFormulas() {
		return new String[] {
				"H_cbilltypecode->\"4C\"",
				"H_pk_calbody->getColValue(bd_stordoc, pk_calbody,pk_stordoc,H_srl_pk)",//库存组织
//				"H_cothercorpid->getColValue(bd_stordoc, pk_corp,pk_stordoc,H_srl_pk)",//入库公司
//				"H_cothercalbodyid->getColValue(bd_stordoc, pk_calbody,pk_stordoc,H_srl_pk)",//入库库存组织 
				"B_cquotecurrency->getColValue(so_saleorder_b,ccurrencytypeid,corder_bid,B_cfirstbillbid)",
				"B_nshouldoutassistnum->-1*B_geb_bsnum",//应发辅数量
				"B_nshouldoutnum->-1*B_geb_snum",//应发数量 
				"B_noutassistnum->-1*B_geb_banum",//实发辅数量
				"B_noutnum->-1*B_geb_anum",//实发数量
				"B_nmny->-1*B_geb_nmny",//金额





//				"H_pk_calbody->iif(B_creccalbodyid =NULL,B_cadvisecalbodyid,B_creccalbodyid)",
				"B_pk_calbody->getColValue(bd_stordoc, pk_calbody,pk_stordoc,H_srl_pk)",//库存组织
//				"H_cwarehouseid->iif(B_creccalbodyid =NULL,B_cbodywarehouseid ,B_crecwareid)",
//				"B_cwarehouseid->iif(B_creccalbodyid =NULL,B_cbodywarehouseid ,B_crecwareid)",
//				"H_pk_calbody->iif(B_creccalbodyid =NULL,B_cadvisecalbodyid,B_creccalbodyid)",
				"H_pk_cubasdocC->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,H_geh_customize8)",
//				"H_cbilltypecode->\"4C\"",
//				"B_nshouldoutnum->B_nnumber   -   B_ntotalinventorynumber - iif(B_ntotalshouldoutnum==null,0,B_ntotalshouldoutnum) + iif(B_ntranslossnum==null,0,B_ntranslossnum) ",
//				"B_hsl->iif(B_scalefactor==null,B_nnumber/B_npacknumber,B_scalefactor)",
//				"B_csourcetype->\"30\"",
//				"B_nshouldoutassistnum->(  B_nnumber   -   B_ntotalinventorynumber    - iif(B_ntotalshouldoutnum==null,0,B_ntotalshouldoutnum) + iif(B_ntranslossnum==null,0,B_ntranslossnum) )   *  (   iif(B_npacknumber==null,0,B_npacknumber) / B_nnumber   ) "


		};
	}
	/**
	* 返回用户自定义函数。
	*/
	public UserDefineFunction[] getUserDefineFunction() {
		return null;
	}
}
