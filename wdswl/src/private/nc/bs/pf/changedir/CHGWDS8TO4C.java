package nc.bs.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * 本地销售出库->供应链销售出库
 * @author zpm
 *
 */
public class CHGWDS8TO4C  extends nc.bs.pf.change.VOConversion {

	public CHGWDS8TO4C() {
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
				"H_cwarehouseid->H_srl_pk",//仓库
//				"H_pk_calbody->H_pk_calbody",//库存组织
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
				 
				"B_csourcebillhid->B_cfirstbillhid",//本地销售出库 源头单据表头ID[销售 订单]  
				"B_csourcebillbid->B_cfirstbillbid",//本地销售出库 源头单据表体ID  [销售 订单]  
				"B_vsourcebillcode->B_vfirstbillcode",//本地销售出库 源头单据号[销售 订单]   
				"B_csourcetype->B_cfirsttyp",//本地销售出库 源头单据类型编码[销售 订单]   
				
				///---------------------------用供应链 销售出库的源头记录 本地销售出库ID
//				"B_cfirstbillhid->B_general_pk",// --销售出库回单单 审批生成erp销售出库单的时候，用来保存销售出库回传单主键，才支持联查;所有生成的ERP销售出库
		
				"B_cfirstbillbid->B_general_b_pk",//--销售出库回传单，审批生成erp销售出库单的时候，根据表体ID来查找货位信息   
				"B_vfirstbillcode->H_vbillno",//
				"B_cfirsttype->H_vbilltype",//
				
				"H_freplenishflag->H_freplenishflag",//是否退货
				"H_boutretflag->H_boutretflag",//是否退回
				"B_dbizdate->B_dbizdate"//出库日期->业务日期-------------------------zpm
		};
	}

	public String[] getFormulas() {
		return new String[] {
				"H_cbilltypecode->\"4C\"",
				"H_pk_calbody->getColValue(bd_stordoc, pk_calbody,pk_stordoc,H_srl_pk)",//库存组织
				"H_cothercorpid->getColValue(bd_stordoc, pk_corp,pk_stordoc,H_srl_pk)",//入库公司
				"H_cothercalbodyid->getColValue(bd_stordoc, pk_calbody,pk_stordoc,H_srl_pk)",//入库库存组织 
				"B_cquotecurrency->getColValue(so_saleorder_b,ccurrencytypeid,corder_bid,B_cfirstbillbid)",

		};
	}
	/**
	* 返回用户自定义函数。
	*/
	public UserDefineFunction[] getUserDefineFunction() {
		return null;
	}
}
