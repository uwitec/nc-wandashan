package nc.ui.wds.self.changedir;
import nc.bs.pf.change.VOConversion;
/**
 * ����̨�� ���ۼƻ�����2 ��̨����ʱ ��������ת��
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
				 "H_ss_pk->H_ss_pk",//���״̬
				 
				 "H_nstocknum->H_whs_stocktonnage",
				 "H_nstockassnum->H_whs_stockpieces",
				 "H_nplannum->H_whs_nprice",//���ΰ���������
				 "H_nplanassnum->H_whs_nmny"//���ΰ��Ÿ�������
			};
	}
	public String[] getFormulas() {
		return null;
	}
}
