package nc.ui.pf.changedir;

import nc.ui.pf.change.VOConversionUI;
/**
 * 
 * @author mlr
 *  其他出库->其他入库前台交换类
 */
public class CHGWDS6TOWDS7 extends VOConversionUI {
	
	public CHGWDS6TOWDS7 (){
		super();
	}
	
	@Override
	public String[] getField() {
		return new String[] {				
			"H_pk_corp ->H_pk_corp",//公司（同一家公司）		
			"H_geh_cwarehouseid->H_srl_pkr",//入库仓库						
			"H_geh_cotherwhid->H_srl_pk",//出库仓库			
			"H_geh_cbiztype->H_cbiztype",//业务类型			
			"H_geh_cdispatcherid->H_cdispatcherid",//收发类别
			"H_geh_cwhsmanagerid->H_cwhsmanagerid",//库管员											
			"H_geh_cdptid->H_cdptid",//部门
			"H_geh_cbizid->H_cbizid",//业务员			
			"B_geb_cinvbasid->B_cinvbasid",//存货基本档案ID   
			"B_geb_crowno->B_crowno",//行号
			"B_geb_cinventoryid->B_cinventoryid",//存货管理ID  	
			"B_pk_measdoc->B_unitid",//主单位 
			"B_castunitid->B_castunitid",//辅单位
			"B_geb_hsl->B_hsl",//换算率 
			"B_geb_proddate->B_cshengchanriqi",//生产日期
			"B_geb_dvalidate->B_cshixiaoriqi",//失效日期			
			"B_geb_nprice->B_nprice",//单价
			"B_geb_nmny->B_nmny",//金额
			"B_geb_vbatchcode->B_vbatchcode",//批次号	
			"B_geb_backvbatchcode->B_lvbatchcode",//来源批次号
//			"B_geb_flargess->B_flargess",//是否赠品--
//			"B_geb_space->B_cspaceid",//货位ID--			 
			"B_vfirstbillcode->B_vfirstbillcode",
			"B_cfirsttype->B_cfirsttyp",
			"B_cfirstbillhid->B_cfirstbillhid",
			"B_cfirstbillbid->B_cfirstbillbid",			
			"B_vsourcebillcode->H_vbillcode",
			"B_csourcetype->H_vbilltype",
			"B_csourcebillhid->B_general_pk",
			"B_csourcebillbid->B_general_b_pk",
		};
	}
	
	@Override
	public String[] getFormulas() {
		
		return new String[]{
			"B_geb_snum->B_noutnum-B_nacceptnum",  //实出库数量-已入库数量
			"B_geb_bsnum->B_noutassistnum-nassacceptnum",//实出辅数量-已入库辅数量
		};
	}
	

}
