package nc.bs.wds.self.changedir;
import nc.bs.pf.change.VOConversion;
/**
 * ����̨�� ������ⵥ->�ִ���
 * @author mlr
 */
public class CHGWDS7TOACCOUNTNUM extends VOConversion{
	/**
	 * "pk_corp","pk_customize1","pk_cargdoc",
    		"pk_invmandoc","pk_invbasdoc","whs_batchcode",
    		"ss_pk","creadate"};"whs_stocktonnage","whs_stockpieces"
	 */
	public String[] getField() {
		return new String[]{
				 "B_pk_corp->H_pk_corp",
				 "B_pk_customize1->H_geh_cwarehouseid",
				 "B_pk_cargdoc->H_pk_cargdoc",
				 "B_pk_invmandoc->B_geb_cinventoryid",
				 "B_pk_invbasdoc->B_geb_cinvbasid",
				 "B_whs_batchcode->B_geb_vbatchcode"
				,"B_ss_pk->B_cdt_pk"
				,"B_creadate->B_geb_proddate"
				,"B_whs_stocktonnage->B_geb_anum"//������
				,"B_whs_stockpieces->B_geb_banum"//������
				,"B_pplpt_pk->B_geb_customize4"//������Ϣ
		};
	}
	public String[] getFormulas() {
		return null;
	}
}
