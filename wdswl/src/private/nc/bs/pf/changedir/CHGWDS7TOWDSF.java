package nc.bs.pf.changedir;

import nc.bs.pf.change.VOConversion;
import nc.vo.pub.lang.UFDate;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * 其他入库->装卸费结算
 * @author Administrator
 *
 */
public class CHGWDS7TOWDSF extends VOConversion {
	
	/**
	* 获得后续类的全录经名称。
	* @return java.lang.String[]
	*/
	public String getAfterClassName() {
		return "nc.bs.pub.chgafter.WDS7TOWDSFAfterDeal";
	}
	/**
	* 获得另一个后续类的全录径名称。
	* @return java.lang.String[]
	*/
	public String getOtherClassName() {
		return null;
	}
	@Override
	public String[] getField() {
		
		return new String[]{
				"H_pk_corp->H_pk_corp",//公司
				"H_pk_stordoc->H_geh_cwarehouseid",//仓库
				"B_csourcebillhid->B_geh_pk",
				"B_csourcebillbid->B_geb_pk",
				"B_vsourcebillcode->H_geh_billcode",
				"B_csourcetype->H_geh_billtype",
				
				"B_cfirstbillhid->B_cfirstbillhid",
				"B_cfirstbillbid->B_cfirstbillbid",
				"B_vfirstbillcode->B_vfirstbillcode",
				"B_cfirsttype->B_cfirsttype",
				"B_pk_invmandoc->B_geb_cinventoryid",
				"B_pk_invbasdoc->B_geb_cinvbasid",
				"B_cunitid->B_pk_measdoc",//主 计量单位
				"B_cassunitid->B_castunitid",//辅计量单位
				"B_noutnum->B_geb_anum",//实入数量
				"B_nassoutnum->B_geb_banum",//实入辅数量
				"B_nshouldoutnum->B_geb_snum",//应入数量
				"B_nassshouldoutnum->B_geb_bsnum",//应入辅数量
				
				"B_nhsl->B_geb_hsl",
				"B_vbatchecode->B_geb_vbatchcode",
		};
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
				"H_pk_billtype->\""+WdsWlPubConst.WDSF+"\"",
				"H_vbillstatus->int(8)",
			    "H_dmakedate->\""+m_strDate+"\"",
			    "H_dbilldate->\""+m_strDate+"\""
		};
	}

}
