package nc.ui.pf.changedir;

import nc.ui.pf.change.VOConversionUI;
/**
 * 
 * @author mlr
 *  特殊运单->其他入库前台交换类
 */
public class CHGWDSSTOWDS7 extends VOConversionUI {
	
	public CHGWDSSTOWDS7 (){
		super();
	}
	
	@Override
	public String[] getField() {
		return new String[] {				
			"H_pk_corp ->H_pk_corp",//公司	
			"H_geh_cwarehouseid->H_pk_outwhouse",//仓库								
			"B_geb_cinvbasid->B_pk_invbasdoc",//存货基本档案ID   
			"B_geb_cinventoryid->B_pk_invmandoc",//存货管理ID  	
			"B_pk_measdoc->B_unit",//主单位 
			"B_castunitid->B_assunit",//辅单位
			"B_geb_hsl->B_hsl",//换算率 
			"B_vfirstbillcode->B_vfirstbillcode",
			"B_cfirsttype->B_cfirsttype",
			"B_cfirstbillhid->B_cfirstbillhid",
			"B_cfirstbillbid->B_cfirstbillbid",			
			"B_vsourcebillcode->H_vbillno",
			"B_csourcetype->H_pk_billtype",
			"B_csourcebillhid->B_pk_sendorder",
			"B_csourcebillbid->B_pk_sendorder_b",
			"B_cdt_pk->B_vdef1",//存货状态
		};
	}
	
	@Override
	public String[] getFormulas() {	
		return new String[]{
			"B_geb_snum->B_ndealnum-B_noutnum",  //实出库数量-已入库数量
			"B_geb_bsnum->B_nassdealnum-B_nassoutnum",//实出辅数量-已入库辅数量
		};
	}
	

}
