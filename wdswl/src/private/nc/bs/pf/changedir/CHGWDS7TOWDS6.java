package nc.bs.pf.changedir;

import nc.bs.pf.change.VOConversion;

/**
 * 
 * @author Administrator
 *  其他入库生成其他出库
 */

 public class CHGWDS7TOWDS6 extends VOConversion {
	
	public CHGWDS7TOWDS6 (){
		super();
	}
	
	@Override
	public String[] getField() {
		return new String[] {					
				//				表头数据交换
				/**业务类型 */
				"H_cbiztype->H_geh_cbiztype",
				"H_pk_corp->H_pk_corp",//公司				
				/**单据类型 */
				// " vbilltype->",
				/**单据日期 */
				"H_dbilldate->SYSDATE",				
				/**出库仓库 */
				"H_srl_pk->H_geh_cwarehouseid",
				/**库管员 */
				"H_cwhsmanagerid->H_geh_cwhsmanagerid",
				"H_vbillstatus->H_pwb_fbillflag",
				/**部门 */
				"H_cdptid->H_geh_cdptid",
				/**收发类别 */
				"H_cdispatcherid->H_geh_cdispatcherid",
				/**货位主键*/
				"H_pk_cargdoc->H_pk_cargdoc",
				"H_csourcebillhid->H_geh_pk", // 来源单据表头
				"H_tmaketime->ENV_NOWTIME",
				"H_coperatorid->SYSOPERATOR",
				"H_cbizid->H_geh_cbizid",
				"H_pk_calbody->H_geh_calbody",

				//				 表体  数据 交换
				"B_crowno->B_geb_crowno",
				"B_cinvbasid->B_geb_cinvbasid",//存货基本ID
				"B_cinventoryid->B_geb_cinventoryid",//存货管理ID
				"B_unitid->B_pk_measdoc",	//单位
				"B_castunitid->B_castunitid",//辅单位

				"B_noutnum->B_geb_anum",//实发数量
				"B_noutassistnum->B_geb_banum",//实发辅数量
				//					 "B_nacceptnum->B_geb_anum",//已入库数量
				//					 "B_nassacceptnum->B_geb_banum",//已入库辅数量
				"B_nshouldoutnum->B_geb_anum",//应发数量
				"B_nprice->B_geb_nprice",
				"B_nshouldoutassistnum->B_geb_banum",//应发辅数量
				"B_nmny->B_geb_nmny",		 
				"B_cspaceid->B_geb_space", //货位ID
				//					 "B_comp->B_",
				"B_hsl->B_geb_hsl",
				"B_vbatchcode->B_geb_vbatchcode",//批次
				//					 "B_dfirstbilldate->B_",
				"B_flargess->B_geb_flargess",
				"B_lvbatchcode->B_geb_backvbatchcode", //源批次
				"B_dbizdate->SYSDATE",

				"B_csourcetype->H_geh_billtype",	
				"B_csourcebillbid->B_geb_pk",
				"B_vsourcebillcode->H_geh_billcode",	
				"B_csourcebillhid->B_geh_pk",


				"H_vuserdef15->H_geh_customize7",

				"B_cfirsttyp->B_cfirsttype",
				"B_cfirstbillhid->B_cfirstbillhid",
				"B_cfirstbillbid->B_cfirstbillbid"
				//					 "B_vfirstbillcode->B_",//源头单据号
		};
	}
	
	@Override
	public String[] getFormulas() {
		// TODO Auto-generated method stub
		return new String[]{
				"H_vbilltype->\"WDS6\"",
				
//				"B_geb_snum->B_noutnum-B_nacceptnum",  //实出库数量-已入库数量
		};
	}
	

}
