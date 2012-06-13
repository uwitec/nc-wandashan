package nc.ui.pf.changedir;

import nc.ui.pf.change.VOConversionUI;
import nc.vo.pf.change.UserDefineFunction;
import nc.vo.pub.lang.UFDate;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * 
 * @author Administrator
 *其他出库数转换成运费核算单
 */
public class CHGWDS6TOWDSM extends VOConversionUI {
	/**
	 * CHG4ETO4C 构造子注解。
	 */
	public CHGWDS6TOWDSM() {
		super();
	}
	/**
	* 获得后续类的全录经名称。
	* @return java.lang.String[]
	*/
	public String getAfterClassName() {
		return "nc.ui.pf.changedir.after.ChgWDS6TOWDSMAfter";
	}
	/**
	* 获得另一个后续类的全录径名称。
	* @return java.lang.String[]
	*/
	public String getOtherClassName() {
	  return null;
	}

	public String[] getField() {
		return (
				new String[] {
						"B_cinvbasdocid ->B_cinvbasid",
						"B_cinvmandocid->B_cinventoryid",
						"B_cassunitid->B_castunitid",
						"B_cunitid->B_unitid",
						"B_nhsl->B_hsl",
						"B_nnum->B_noutnum",
						"B_nassnum->B_noutassistnum",						
						"B_cupsourcebilltype->B_csourcetype",
						"B_cupsourcebillid->B_csourcebillhid",
						"B_cupsourcebillrowid->B_csourcebillbid",
						"B_vupbillcode->B_vsourcebillcode",
						"B_vsourcebillcode->H_vbillcode",
						"B_csourcebillbid->B_general_b_pk",
						"B_csourcebillhid->B_general_pk",
						"B_pk_destore->H_srl_pk",//发货仓库
						"B_pk_restore->H_srl_pkr",//收货仓库
						"B_pk_trader->H_ccustomerid",//客商id
				}
				);
	}     

	/**
	* 返回用户自定义函数。
	*/
	public UserDefineFunction[] getUserDefineFunction() {
		return null;
	}
	/**
	* 获得公式。
	* @return java.lang.String[]
	*/
	public String[] getFormulas() {
		new UFDate(System.currentTimeMillis());
		super.setSysDate(new UFDate(System.currentTimeMillis()).toString());
		return new String[] {
//			"H_cvendorbaseid->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,B_cvendormangid)",
//			"H_forderstatus->int(0)",
				"B_csourcetype->\""+WdsWlPubConst.BILLTYPE_OTHER_OUT+"\"",
				"H_icoltype->int(0)",
				"B_csendareaid->getColValue(tb_storareacl, pk_areacl,pk_stordoc ,H_srl_pk)",//分仓区域绑定：仓库获得区域
				"B_creceiverealid->getColValue(tb_storareacl, pk_areacl,pk_stordoc ,H_srl_pkr)"//分仓区域绑定：仓库获得区域
		};
	}
}
