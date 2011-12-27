package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;

public class CHG30TOWDSZ extends nc.ui.pf.change.VOConversionUI{


	public CHG30TOWDSZ() {
		super();
	}

	public String getAfterClassName() {
		return "nc.vo.ic.out.in.ChgtoOtherIn";
	}

	public String getOtherClassName() {
		return null;
	}

	public String[] getField() {
		return new String[] {
	//			"H_srl_pk->H_cwarehouseid",// 出库仓库
				"H_geh_cbiztype->H_cbiztype",// 业务类型主键
				"H_geh_cdptid->H_cdeptid",// 部门
				"H_geh_cbizid->H_cemployeeid",// 业务员
				"H_geh_customize8->H_ccustomerid",// 客户
		//		"H_vdiliveraddress->B_vreceiveaddress",// 收货地址
				"H_vnote->H_vnote",// 备注
		//		"H_geh_calbody->H_ccalbodyid",// 库存组织
	//			"H_creceiptcustomerid->H_creceiptcustomerid",//收货单位
				"H_vsourcebillcode->H_vreceiptcode", // 来源单据号			
				"B_csourcebillhid->B_csaleid",// 来源单据表头主键
				"B_csourcebillbid->B_corder_bid",// 来源单据表体主键
				"B_vsourcebillcode->H_vreceiptcode", // 来源单据号
				"B_csourcetype->H_creceipttype",// 来源单据类型
//				"H_srl_pk->B_cbodywarehouseid",//发货站(附表仓库)
				"B_cfirstbillhid->B_csaleid", // 源头单据表头主键
				"B_cfirstbillbid->B_corder_bid",// 源头单据表体主键
				"B_cfirsttype->H_creceipttype",//源头单据类型
				"B_vfirstbillcode->H_vreceiptcode",//源头单据号
				
				"B_geb_snum->B_nnumber",// 应发数量
				"B_geb_bsnum->B_npacknumber", // 应发辅数量
				"B_geb_cinventoryid->B_cinventoryid",// 存货主键
				"B_geb_cinvbasid->B_cinvbasdocid",//存货基本id
				"B_geb_flargess->B_blargessflag",// 是否赠品
//				"B_lvbatchcode->B_",//来源批次号
//				"B_vbatchcode->B_", //批次号
				"B_pk_measdoc->B_cunitid",//主单位
				"B_castunitid->B_cpackunitid",//辅单位
				"B_geb_hsl->B_scalefactor",//换算率
				"B_geb_nprice->B_noriginalcurprice",//单价
				"B_geb_nmny->B_noriginalcurmny",//金额
		};
	}
	/**
	* 获得公式。
	*/
	public String[] getFormulas() {
		return new String[] {
				"B_isoper->\"Y\"", //是否进行操作
				"H_vdiliveraddress->getColValue(bd_custaddr,addrname ,pk_custaddr , H_vreceiveaddress)"
//				"B_crowno->\"10\""//行号
		};
	}
	/**
	* 返回用户自定义函数。
	*/
	public UserDefineFunction[] getUserDefineFunction() {
		return null;
	}


}
