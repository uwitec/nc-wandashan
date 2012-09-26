package nc.ui.pf.changedir;

import nc.ui.pf.change.VOConversionUI;
import nc.ui.pub.ClientEnvironment;
import nc.vo.pf.change.UserDefineFunction;
import nc.vo.scm.pub.session.ClientLink;

public class CHGWDS5TOWDS8 extends VOConversionUI{

	public CHGWDS5TOWDS8() {
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
				"H_srl_pk->H_pk_outwhouse",// 出库仓库
				"H_cbiztype->H_pk_busitype",// 业务类型主键
				"H_cdptid->H_pk_deptdoc",// 部门
				"H_cbizid->H_vemployeeid",// 业务员
				"H_ccustomerid->H_pk_cumandoc",// 客户
				"H_pk_cubasdocc->H_pk_cubasdoc",// 客户基本id
				"H_vdiliveraddress->H_vinaddress",// 收货地址
				"H_vnote->H_vmemo",// 备注
				"H_pk_calbody->H_ccalbodyid",// 库存组织
				"H_creceiptcustomerid->H_creceiptcustomerid",//收货单位
				
				
//				"H_vsourcebillcode->H_vbillno",// 来源单据号
//				"H_csourcebillhid->H_pk_soorder",// 来源单据表头主键
				
				"B_vuserdef9->B_vdef1",//存货状态
				"B_csourcebillhid->B_pk_soorder",// 来源单据表头主键
				"B_csourcebillbid->B_pk_soorder_b",// 来源单据表体主键
				"B_vsourcebillcode->H_vbillno", // 来源单据号
				"B_csourcetype->H_pk_billtype",// 来源单据类型
				"B_cfirstbillhid->B_cfirstbillhid", // 源头单据表头主键
				"B_cfirstbillbid->B_cfirstbillbid",// 源头单据表体主键
				"B_cfirsttype->B_cfirsttype",//源头单据类型
				"B_vfirstbillcode->B_vfirstbillcode",//源头单据号
				"H_isxnap->B_isxnap",//是否虚拟
				"B_cinventoryid->B_pk_invmandoc",// 存货主键
				"B_cinvbasid->B_pk_invbasdoc",//存货基本id
				"B_flargess->B_fisgift",// 是否赠品
				"B_lvbatchcode->B_picicode",//来源批次号
				"B_vbatchcode->B_picicode", //批次号
				"B_unitid->B_uint",//主单位
				"B_castunitid->B_assunit",//辅单位
				"B_hsl->B_nhgrate",//换算率
				"B_isxnap->B_isxnap"//是否虚拟安排 liuys add
		};
	}
	/**
	* 获得公式。
	*/
	public String[] getFormulas() {
		ClientLink cl = new ClientLink(ClientEnvironment.getInstance());
		return new String[] {
				"B_isoper->\"Y\"", //是否进行操作
				"B_dbizdate->\""+cl.getLogonDate()+"\"",
//				"B_crowno->\"10\""//行号
				"B_nshouldoutnum->B_narrangnmu-B_noutnum",// 应发数量
				"B_nshouldoutassistnum->B_nassarrangnum-B_nassoutnum", // 应发辅数量
		};
	}
	/**
	* 返回用户自定义函数。
	*/
	public UserDefineFunction[] getUserDefineFunction() {
		return null;
	}
}
