package nc.ui.wds.self.changedir;
import nc.ui.pf.change.VOConversionUI;
/**
 * ����̨�� ���ۼƻ�����->�ִ���
 * @author mlr
 */
public class CHGWDS2TOACCOUNTNUM extends VOConversionUI{
	/**
	 * "pk_corp","pk_customize1","pk_cargdoc",
    		"pk_invmandoc","pk_invbasdoc","whs_batchcode",
    		"ss_pk","creadate"};"whs_stocktonnage","whs_stockpieces"
	 */
	public String[] getField() {
		return new String[]{
				 "H_pk_corp->H_pk_corp",
				 "H_pk_customize1->H_pk_outwhouse",
				 "H_pk_invmandoc->H_pk_invmandoc",
				 "H_pk_invbasdoc->H_pk_invbasdoc",
				 "H_ss_pk->H_vdef1"//���״̬
			};
	}
	public String[] getFormulas() {
		return null;
	}
}
