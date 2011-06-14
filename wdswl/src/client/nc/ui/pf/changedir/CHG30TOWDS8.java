package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;

public class CHG30TOWDS8 extends nc.ui.pf.change.VOConversionUI{


	public CHG30TOWDS8() {
		super();
	}

	public String getAfterClassName() {
		return "nc.vo.ic.so.out.ChangeSaleOutVO";
	}

	public String getOtherClassName() {
		return null;
	}

	public String[] getField() {
		return new String[] {
				"H_srl_pk->H_cwarehouseid",// 出库仓库
				"H_cbiztype->H_cbiztype",// 业务类型主键
				"H_cdptid->H_cdeptid",// 部门
				"H_cbizid->H_cemployeeid",// 业务员
				"H_ccustomerid->H_ccustomerid",// 客户
				"H_vdiliveraddress->B_vreceiveaddress",// 收货地址
				"H_vnote->H_vnote",// 备注
				"H_pk_calbody->H_ccalbodyid",// 库存组织
				"H_creceiptcustomerid->H_creceiptcustomerid",//收货单位
				"H_vsourcebillcode->H_vreceiptcode", // 来源单据号			
				"B_csourcebillhid->B_csaleid",// 来源单据表头主键
				"B_csourcebillbid->B_corder_bid",// 来源单据表体主键
				"B_vsourcebillcode->H_vreceiptcode", // 来源单据号
				"B_csourcetype->H_creceipttype",// 来源单据类型
				"H_srl_pk->B_cbodywarehouseid",//发货站(附表仓库)
//				"B_cfirstbillhid->B_cfirstbillhid", // 源头单据表头主键
//				"B_cfirstbillbid->B_cfirstbillbid",// 源头单据表体主键
//				"B_cfirsttyp->B_cfirsttype",//源头单据类型
//				"B_vfirstbillcode->B_vfirstbillcode",//源头单据号
				"B_nshouldoutnum->B_nnumber",// 应发数量
				"B_nshouldoutassistnum->B_npacknumber", // 应发辅数量
				"B_cinventoryid->B_cinventoryid",// 存货主键
				"B_cinvbasid->B_cinvbasdocid",//存货基本id
				"B_flargess->B_blargessflag",// 是否赠品
//				"B_lvbatchcode->B_",//来源批次号
//				"B_vbatchcode->B_", //批次号
				"B_unitid->B_cunitid",//主单位
				"B_castunitid->B_cpackunitid",//辅单位
				"B_hsl->B_scalefactor"//换算率
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
