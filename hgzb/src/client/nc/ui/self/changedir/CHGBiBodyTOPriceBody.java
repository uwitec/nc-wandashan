package nc.ui.self.changedir;

import nc.ui.pf.change.VOConversionUI;

/**
 * 出库单数据转换为运费数据
 * @author Administrator
 *
 */

public class CHGBiBodyTOPriceBody extends VOConversionUI {

	public String[] getField() {
		return new String[]{
				 "H_cinvclid->H_cinvclid"//品种分类
				,"H_cinvbasid->H_cinvbasid"//品种基本id
				,"H_cinvmanid->H_cinvmanid"//品种管理id
				,"H_cunitid->H_cunitid"//主计量单位
//				,"H_castunitid->H_"//辅计量单位
				,"H_nnum->H_nzbnum"//招标主数量
//				,"H_nasnum= null;//招标辅数量	
				,"H_csourcebillhid->H_cbiddingid"//来源ID
				,"H_csourcebillbid->H_cbiddingbid"//来源子ID
				//,"H_csourcetype->\"ZB01\""//来源类型
		};
	}
	public String[] getFormulas() {
		return new String[]{"H_csourcetype->\"ZB01\""};
	}
}
