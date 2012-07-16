package nc.bs.pf.changedir;

import nc.bs.pf.change.VOConversion;
/**
 * 调拨出库->  装卸费结算
 * @author mlr
 *
 */
public class CHGWDSHTOWDSF extends VOConversion{
	/**
	* 获得后续类的全录经名称。
	* @return java.lang.String[]
	*/
	public String getAfterClassName() {
		return "nc.bs.pub.chgafter.WDS8TOWDSFAfterDeal";
	}
	/**
	* 获得另一个后续类的全录径名称。
	* @return java.lang.String[]
	*/
	public String getOtherClassName() {
		return null;
	}	
	@Override
	public String[] getField() {	
		return new String[]{
				"H_pk_corp->H_pk_corp",//公司	
				"H_pk_stordoc->H_srl_pk",//仓库
				"B_csourcebillhid->B_general_pk",
				"B_csourcebillbid->B_general_b_pk",
				"B_vsourcebillcode->H_vbillcode",
				"B_csourcetype->H_vbilltype",				
				"B_cfirstbillhid->B_cfirstbillhid",
				"B_cfirstbillbid->B_cfirstbillbid",
				"B_vfirstbillcode->B_vfirstbillcode",
				"B_cfirsttype->B_cfirsttype",
				"B_pk_invmandoc->B_cinventoryid",
				"B_pk_invbasdoc->B_cinvbasid",
				"B_cunitid->B_unitid",//主 计量单位
				"B_cassunitid->B_castunitid",//辅计量单位
				"B_noutnum->B_noutnum",//实出数量
				"B_nassoutnum->B_noutassistnum",//实出辅数量
				"B_nshouldoutnum->B_nshouldoutnum",//应出数量
				"B_ntagnum->B_ntagnum",//贴签数量
				"B_nassshouldoutnum->B_nshouldoutassistnum",//应出辅数	
			
				"B_fistag->B_fistag",
				
				"B_nhsl->B_hsl",
				"B_vbatchecode->B_vbatchcode",
		};
	}



}
