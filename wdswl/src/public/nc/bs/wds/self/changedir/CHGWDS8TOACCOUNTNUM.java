package nc.bs.wds.self.changedir;
import nc.bs.pf.change.VOConversion;
/**
 * 物流台账 销售出库单->现存量
 * @author mlr
 */
public class CHGWDS8TOACCOUNTNUM extends VOConversion{
	/**
	 * "pk_corp","pk_customize1","pk_cargdoc",
    		"pk_invmandoc","pk_invbasdoc","whs_batchcode",
    		"ss_pk","creadate"};"whs_stocktonnage","whs_stockpieces"
	 */
	public String[] getField() {
		return new String[]{
				 "B_pk_corp->H_pk_corp",
				 "B_pk_customize1->H_srl_pk",
				 "B_pk_cargdoc->H_pk_cargdoc",
				 "B_pk_invmandoc->B_cinventoryid",
				 "B_pk_invbasdoc->B_cinvbasid",
				 "B_whs_batchcode->B_vbatchcode"
				,"B_ss_pk->B_vuserdef9"//存货状态
				,"B_pplpt_pk->B_vuserdef8"//货架信息
				,"B_creadate->B_vuserdef7"//生产日期
				,"B_whs_stocktonnage->B_noutnum"//主数量
				,"B_whs_stockpieces->B_noutassistnum"//辅数量				
		};
	}
	public String[] getFormulas() {
		return null;
	}
}
