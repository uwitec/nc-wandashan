package nc.ui.wds.self.changedir;
import nc.bs.pf.change.VOConversion;
/**
 * 物流台账 销售计划安排2 后台安排时 用于数据转换
 * @author zhf
 */
public class CHGHANDTOSTOCK extends VOConversion{
	/**
	 * "pk_corp","pk_customize1","pk_cargdoc",
    		"pk_invmandoc","pk_invbasdoc","whs_batchcode",
    		"ss_pk","creadate"};"whs_stocktonnage","whs_stockpieces"
	 */
	public String[] getField() {
		return new String[]{
				 "H_pk_corp->H_pk_corp",
				 "H_cstoreid->H_pk_customize1",
				 "H_cinvmanid->H_pk_invmandoc",
				 "H_cinvbasid->H_pk_invbasdoc",
				 "H_ss_pk->H_ss_pk",//存货状态
				 
				 "H_nstocknum->H_whs_stocktonnage",
				 "H_nstockassnum->H_whs_stockpieces",
				 "H_nplannum->H_whs_nprice",//本次安排主数量
				 "H_nplanassnum->H_whs_nmny"//本次安排辅助数量
			};
	}
	public String[] getFormulas() {
		return null;
	}
}
