package nc.ui.pf.changedir;

import nc.ui.pf.change.VOConversionUI;
/**
 * 
 * @author Administrator
 *  其他出库->其他入库前台交换类
 */
public class CHGWDS6TOWDS7 extends VOConversionUI {
	
	public CHGWDS6TOWDS7 (){
		super();
	}
	
	@Override
	public String[] getField() {
		return new String[] {
			"H_geh_cothercorpid->H_pk_corp",
			"H_geh_cotherwhid->H_srl_pk",//出库仓库
			"H_geh_cwarehouseid->H_srl_pkr",//入库仓库
			"B_geb_cinvbasid->B_cinvbasid",
			"B_geb_cinventoryid->B_cinventoryid",
//			"B_geb_hsl->B_",//换算率
			"B_geb_vbatchcode->B_vbatchcode",//批次号 
//			"B_vfirstbillcode->B_",
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
		// TODO Auto-generated method stub
		return new String[]{
				"B_geb_snum->B_noutnum-B_nacceptnum",  //实出库数量-已入库数量
		};
	}
	

}
