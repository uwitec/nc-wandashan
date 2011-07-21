package nc.bs.self.changedir;

import nc.bs.pf.change.VOConversion;

/**
 * 采购计划生成标段体
 * @author Administrator
 *
 */

public class CHGPlanTOBidding extends VOConversion {

	public String[] getField() {
		return new String[]{
//				private String crowno;//行号
//				"H_cunitid->H_"//计量单位
//				,"H_ naverageprice->H_"//历史平均价
				"H_cinvbasid->H_cbaseid"//存货基本ID
//				,"H_ nmarkprice->H_"//标底价
//				,"H_cupsourcebillrowid->H_"//
				
//				,"H_cinvclid->H_"//存货分类ID
//				,"H_ unitweight->H_"//单重
//				,"H_ nplanprice->H_"//计划价
//				,"H_ nmarketprice->H_"//市价
//				,"H_cbiddingbid->H_"//子表主键
				,"H_cinvmanid->H_cmangid"//存货管理ID
//				,"H_cupsourcebilltype->"
				,"H_cupsourcebillid->H_cpraybillid"
				,"H_cupsourcebillrowid->H_cpraybill_bid"//
				,"H_csourcetype->H_csourcebilltype"
				,"H_csourcebillhid->H_csourcebillid"
				,"H_csourcebillbid->H_csourcebillrowid"
//				,""
		};
	}
	public String[] getFormulas() {
		return new String[]{
				"H_cupsourcebilltype->\"20\"" 
				,"H_nzbnum->H_npraynum"//招标数量  该量已经是  npraynum - 累计订货量
				,"H_cunitid->getColValue(bd_invbasdoc,pk_measdoc,pk_invbasdoc,H_cbaseid)"
				,"H_unitweight->getColValue(bd_invbasdoc,unitweight,pk_invbasdoc,H_cbaseid)"
				,"H_nplanprice->getColValue(bd_invmandoc,planprice,pk_invmandoc,H_cmangid)"
				,"H_invclcode->getColValue(bd_invbasdoc,invcode,pk_invbasdoc,H_cbaseid)"
		};
	}
}
