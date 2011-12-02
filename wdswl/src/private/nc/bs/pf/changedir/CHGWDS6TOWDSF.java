package nc.bs.pf.changedir;

import nc.bs.pf.change.VOConversion;
import nc.vo.pub.lang.UFDate;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 *其他出库->装卸费核算单
 * @author Administrator
 *
 */
public class CHGWDS6TOWDSF extends VOConversion {
	/**
	* 获得后续类的全录经名称。
	* @return java.lang.String[]
	*/
	public String getAfterClassName() {
		return "nc.bs.pub.chgafter.WDS6TOWDSFAfterDeal";
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
		// TODO Auto-generated method stub
		return new String[]{
				"H_pk_corp->H_pk_corp",//公司	
				"B_csourcebillhid->B_general_pk",
				"B_csourcebillbid->B_general_b_pk",
				"B_vsourcebillcode->H_vbillcode",
				"B_csourcetype->H_vbilltype",
				
				"B_cfirstbillhid->B_cfirstbillhid",
				"B_cfirstbillbid->B_cfirstbillbid",
				"B_vfirstbillcode->B_vfirstbillcode",
				"B_cfirsttype->B_cfirsttyp",
				"B_pk_invmandoc->B_cinventoryid",
				"B_pk_invbasdoc->B_cinvbasid",
				"B_cunitid->B_unitid",//主 计量单位
				"B_cassunitid->B_castunitid",//辅计量单位
				"B_noutnum->B_noutnum",//实出数量
				"B_nassoutnum->B_noutassistnum",//实出辅数量
				"B_nshouldoutnum->B_nshouldoutnum",//应出数量
				"B_nassshouldoutnum->B_nshouldoutassistnum",//应出辅数量
				"B_fistag->B_fistag"
		};
	}
	
	/**
	* 获得公式。
	* @return java.lang.String[]
	*/
	public String[] getFormulas() {
		if(m_strDate == null){
			super.setSysDate(new UFDate(System.currentTimeMillis()).toString());
		}
		return new String[] {
				"H_pk_billtype->\""+WdsWlPubConst.WDSF+"\"",
				"H_vbillstatus->int(8)",
			    "H_dmakedate->\""+m_strDate+"\"",
			    "H_dbilldate->\""+m_strDate+"\""
		};
	}

}
