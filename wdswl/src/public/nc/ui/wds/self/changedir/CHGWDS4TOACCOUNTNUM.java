package nc.ui.wds.self.changedir;
import nc.ui.pf.change.VOConversionUI;
/**
 * 物流台账 销售计划安排->现存量
 * @author mlr
 */
public class CHGWDS4TOACCOUNTNUM extends VOConversionUI{
	/**
	 * "pk_corp","pk_customize1","pk_cargdoc",
    		"pk_invmandoc","pk_invbasdoc","whs_batchcode",
    		"ss_pk","creadate"};"whs_stocktonnage","whs_stockpieces"
	 */
	public String[] getField() {
		return new String[]{
				 "H_pk_corp->H_pk_corp",
				 "H_pk_customize1->H_cbodywarehouseid",
				 "H_pk_invmandoc->H_cinventoryid",
				 "H_pk_invbasdoc->H_cinvbasdocid",
				 "H_ss_pk->H_vdef1"//存货状态
			};
	}
	public String[] getFormulas() {
		return null;
	}
}
