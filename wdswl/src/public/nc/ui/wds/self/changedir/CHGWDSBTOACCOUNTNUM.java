package nc.ui.wds.self.changedir;

import nc.ui.pf.change.VOConversionUI;

/**
 * 调拨订单安排到现存 量
 * @author mlr
 *
 */
public class CHGWDSBTOACCOUNTNUM extends VOConversionUI{
	public String[] getField() {
		return new String[]{
				 "H_pk_corp->H_coutcorpid",
				 "H_pk_customize1->H_coutwhid",
				 "H_pk_invmandoc->H_ctakeoutinvid",
				 "H_pk_invbasdoc->H_cinvbasid",
				 "H_ss_pk->H_vdef1"//存货状态
			};
	}
	public String[] getFormulas() {
		return null;
	}
}
