package nc.bs.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;

/**
 * 调拨出库回传 ->供应链调拨出库
 * (调拨订单->供应链调拨出库)
 * @author mlr
 *
 */
public class CHGWDSXTO4Y extends nc.bs.pf.change.VOConversion{
	/**
	 * CHG5DTO4Y 构造子注解。
	 */
	public CHGWDSXTO4Y() {
		super();
	}
	/**
	 * nc.bs.ic.pub.pfconv.ChgAftTo2Ic
	* 获得后续类的全录经名称。
	* @return java.lang.String[]
	*/
	public String getAfterClassName() {
		return null;
	}
	/**
	* 获得字段对应。	noutassistnum
	* @return java.lang.String[]
	*/
	public String[] getField() {
		return new String[] {
			"H_cdptid->B_coutdeptid",
			"H_pk_corp->B_coutcorpid",
			"H_pk_calbody->B_coutcbid",
			"H_vuserdef9->H_vdef9",
			"H_vuserdef8->H_vdef8",
			"H_vuserdef7->H_vdef7",
			"H_vuserdef6->H_vdef6",
			"H_vuserdef5->H_vdef5",
			"H_vuserdef4->H_vdef4",
			"H_vuserdef3->H_vdef3",
			"H_vuserdef2->H_vdef2",
			"H_cwarehouseid->B_coutwhid",
			"H_cotherwhid->B_cinwhid",
			"H_vuserdef1->H_vdef1",
			"H_vuserdef10->H_vdef10",
			"H_vuserdef11->H_vdef11",
			"H_vuserdef12->H_vdef12",
			"H_vuserdef13->H_vdef13",
			"H_vuserdef14->H_vdef14",
			"H_vuserdef15->H_vdef15",
			"H_vuserdef16->H_vdef16",
			"H_vuserdef17->H_vdef17",
			"H_vuserdef18->H_vdef18",
			"H_vuserdef19->H_vdef19",
			"H_vuserdef20->H_vdef20",
			"H_coperatorid->SYSOPERATOR",
			"H_dbilldate->SYSDATE",
			"H_coperatoridnow->SYSOPERATOR",
			"H_cbizid->B_coutpsnid",
			"H_ts->H_ts",
			"H_vnote->H_vnote",
			"H_fallocflag->H_fallocflag",
			"H_cothercorpid->H_cincorpid",
			"H_cothercalbodyid->H_cincbid",
			"H_coutcorpid->H_coutcorpid",
			"H_coutcalbodyid->H_coutcbid",
			"H_freplenishflag->B_bretractflag",
	    "B_freplenishflag->B_bretractflag",
			"H_cdilivertypeid->B_pk_sendtype",
			"H_vdiliveraddress->B_vreceiveaddress",
	    "B_vdiliveraddress->B_vreceiveaddress",   
			"H_ccustomerid->B_ccustomerid",
			"H_clogdatenow->SYSDATE",
			
			"B_vfree2->B_vfree2",
			"B_vfree1->B_vfree1",
			"B_cinventoryid->B_ctakeoutinvid",
			"B_cinvbasid->B_cinvbasid",
			"B_cprojectphaseid->B_cprojectphase",
			"B_cprojectid->B_cprojectid",
			"B_vuserdef6->B_vbdef6",
			"B_vuserdef5->B_vbdef5",
			"B_vuserdef4->B_vbdef4",
			"B_vuserdef3->B_vbdef3",
			"B_vuserdef2->B_vbdef2",
			"B_vuserdef1->B_vbdef1",
			"B_vuserdef7->B_vbdef7",
			"B_vuserdef8->B_vbdef8",
			"B_vuserdef9->B_vbdef9",
			"B_vuserdef10->B_vbdef10",
			"B_vuserdef11->B_vbdef11",
			"B_vuserdef12->B_vbdef12",
			"B_vuserdef13->B_vbdef13",
			"B_vuserdef14->B_vbdef14",
			"B_vuserdef15->B_vbdef15",
			"B_vuserdef16->B_vbdef16",
			"B_vuserdef17->B_vbdef17",
			"B_vuserdef18->B_vbdef18",
			"B_vuserdef19->B_vbdef19",
			"B_vuserdef20->B_vbdef20",
			"B_csourcebillhid->B_cbillid",
			"B_vfree5->B_vfree5",
			"B_vfree4->B_vfree4",
			"B_vfree3->B_vfree3",
			"B_csourcebillbid->B_cbill_bid",
			"B_vbatchcode->B_vbatch",
			"B_vsourcebillcode->B_vcode",
			"B_vsourcerowno->B_crowno",
			"B_cfirstbillbid->B_cbill_bid",
			"B_cfirstbillhid->B_cbillid",
			"B_vfirstbillcode->B_vcode",
			"B_cfirsttype->B_ctypecode",
			"B_castunitid->B_castunitid",
			"B_creceieveid->B_creceieveid",
			"B_hsl->B_nchangerate",
			"B_cspaceid->B_ctakeoutspaceid",
			"B_dvalidate->B_dvalidate",
			"B_csourceheadts->H_ts",
			"B_csourcebodyts->B_ts",
			"B_cinvbasid->B_cinvbasid",
			"B_cinvmanid->B_cinvbasid",
			
			"B_cquoteunitid->B_cquoteunitid",		
			"B_nquoteunitrate->B_nquoteunitrate",
			
			"H_pk_defdoc1->H_pk_defdoc1",
			"H_pk_defdoc2->H_pk_defdoc2",
			"H_pk_defdoc3->H_pk_defdoc3",
			"H_pk_defdoc4->H_pk_defdoc4",
			"H_pk_defdoc5->H_pk_defdoc5",
			"H_pk_defdoc6->H_pk_defdoc6",
			"H_pk_defdoc7->H_pk_defdoc7",
			"H_pk_defdoc8->H_pk_defdoc8",
			"H_pk_defdoc9->H_pk_defdoc9",
			"H_pk_defdoc10->H_pk_defdoc10",
			"H_pk_defdoc11->H_pk_defdoc11",
			"H_pk_defdoc12->H_pk_defdoc12",
			"H_pk_defdoc13->H_pk_defdoc13",
			"H_pk_defdoc14->H_pk_defdoc14",
			"H_pk_defdoc15->H_pk_defdoc15",
			"H_pk_defdoc16->H_pk_defdoc16",
			"H_pk_defdoc17->H_pk_defdoc17",
			"H_pk_defdoc18->H_pk_defdoc18",
			"H_pk_defdoc19->H_pk_defdoc19",
			"H_pk_defdoc20->H_pk_defdoc20",
			
			"B_pk_defdoc1->B_pk_defdoc1",
			"B_pk_defdoc2->B_pk_defdoc2",
			"B_pk_defdoc3->B_pk_defdoc3",
			"B_pk_defdoc4->B_pk_defdoc4",
			"B_pk_defdoc5->B_pk_defdoc5",
			"B_pk_defdoc6->B_pk_defdoc6",
			"B_pk_defdoc7->B_pk_defdoc7",
			"B_pk_defdoc8->B_pk_defdoc8",
			"B_pk_defdoc9->B_pk_defdoc9",
			"B_pk_defdoc10->B_pk_defdoc10",
			"B_pk_defdoc11->B_pk_defdoc11",
			"B_pk_defdoc12->B_pk_defdoc12",
			"B_pk_defdoc13->B_pk_defdoc13",
			"B_pk_defdoc14->B_pk_defdoc14",
			"B_pk_defdoc15->B_pk_defdoc15",
			"B_pk_defdoc16->B_pk_defdoc16",
			"B_pk_defdoc17->B_pk_defdoc17",
			"B_pk_defdoc18->B_pk_defdoc18",
			"B_pk_defdoc19->B_pk_defdoc19",
			"B_pk_defdoc20->B_pk_defdoc20",
			"B_bsourcelargess->B_flargess",
			"B_flargess->B_flargess",
			"B_cvendorid->B_cvendorid"
		};
	}
	/**
	* 获得公式。
	* @return java.lang.String[]
	*/
	public String[] getFormulas() {
		return new String[] {
			"H_cbilltypecode->\"4Y\"",
			"B_nshouldoutnum->B_nnum   -   B_norderoutnum - iif(B_nordershouldoutnum==null,0,B_nordershouldoutnum) + iif(B_norderwaylossnum==null,0,B_norderwaylossnum) ",
			"B_csourcetype->\"5D\"",
			"B_nshouldoutassistnum->(  B_nassistnum   -   B_norderoutassnum    - iif(B_nordershouldoutnum==null,0,B_nordershouldoutnum)*(   B_nassistnum   /   B_nnum   ) ) + iif(B_norderwaylossnum==null,0,B_norderwaylossnum)*(iif(B_nassistnum==null,0,B_nassistnum )/B_nnum) "
		};
	}
	/**
	* 返回用户自定义函数。
	*/
	public UserDefineFunction[] getUserDefineFunction() {
		return null;
	}
}
