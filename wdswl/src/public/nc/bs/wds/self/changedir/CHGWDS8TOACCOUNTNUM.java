package nc.bs.wds.self.changedir;
import nc.bs.pf.change.VOConversion;
/**
 * ����̨�� ���۳��ⵥ->�ִ���
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
				,"B_ss_pk->B_vuserdef9"//���״̬
				,"B_pplpt_pk->B_vuserdef8"//������Ϣ
				,"B_creadate->B_vuserdef7"//��������
				,"B_whs_stocktonnage->B_noutnum"//������
				,"B_whs_stockpieces->B_noutassistnum"//������				
		};
	}
	public String[] getFormulas() {
		return null;
	}
}
