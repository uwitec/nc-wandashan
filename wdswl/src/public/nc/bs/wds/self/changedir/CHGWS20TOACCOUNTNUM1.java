package nc.bs.wds.self.changedir;
import nc.bs.pf.change.VOConversion;
/**
 * 物流台账 状态变更单 ->现存量   处理存货状态变化后 新增量
 * @author mlr
 */
public class CHGWS20TOACCOUNTNUM1 extends VOConversion{
	/**
	 * "pk_corp","pk_customize1","pk_cargdoc",
    		"pk_invmandoc","pk_invbasdoc","whs_batchcode",
    		"ss_pk","creadate"};"whs_stocktonnage","whs_stockpieces"
	 */
	public String[] getField() {
		return new String[]{
				 "B_pk_corp->H_pk_corp",
				 "B_pk_customize1->H_cwarehouseid",
				 "B_pk_cargdoc->H_ccargdocid",
				 "B_pk_invmandoc->B_cinvmanid",
				 "B_pk_invbasdoc->B_cinvbasid",
				 "B_whs_batchcode->B_vbatchcode"
				,"B_ss_pk->B_cinvstatusid2"//变化前状态
				,"B_creadate->B_vdef1"
				,"B_whs_stocktonnage->B_nnum"//主数量
				,"B_whs_stockpieces->B_nassnum"//辅数量				
		};
	}
	public String[] getFormulas() {
		return null;
	}
}
