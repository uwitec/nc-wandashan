// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 
// Source File Name:   CHG5ATO5D.java

package nc.ui.pf.changedir;

import nc.ui.pf.change.VOConversionUI;
import nc.vo.pf.change.UserDefineFunction;

public class CHGHG02TO5X extends VOConversionUI
{

	public CHGHG02TO5X()
	{
	}

	public String getAfterClassName()
	{
		return 	"nc.bs.hg.pu.conversion.PlanVOTO5X";
	}

	public String[] getField() {
		return (
				new String[] {
						"H_vdef8->H_vdef8",
						//			"H_dbilldate->H_dbilldate",
						"H_vdef7->H_vdef7",
						"H_vdef6->H_vdef6",
						"H_vdef5->H_vdef5",
						"H_vdef4->H_vdef4",
						"H_cincorpid->H_pk_corp",//调入公司
						"H_vdef3->H_vdef3",
						"H_vdef10->H_vdef10",
						"H_vdef11->H_vdef11",
						"H_vdef12->H_vdef12",
						"H_vdef13->H_vdef13",
						"H_vdef14->H_vdef14",
						"H_vdef15->H_vdef15",
						"H_vdef16->H_vdef16",
						"H_vdef17->H_vdef17",
						"H_vdef18->H_vdef18",
						"H_vdef19->H_vdef19",
						"H_vdef20->H_vdef20",
						"H_vdef2->H_vdef2",
						//			"H_cbiztypeid->H_cbiztypeid",
						"H_vdef1->H_vdef1",
//						"H_fallocflag->H_fallocflag",//-----------------------------
						
						"H_ctakeoutcorpid->H_csupplycorpid",
						"H_coutwhid->B_csupplywarehouseid",
						"H_cemployeeid->H_capplypsnid",
						"H_cinwhid->H_creqwarehouseid",
						"H_coutcorpid->H_csupplycorpid",
						
						"H_ctakeoutcbid->B_csupplycalbodyid",
						//			"B_cprojectid->H_cprojectid",
						"H_vnote->H_vmemo",
						"H_coutcbid->H_csupplycalbodyid",
						"H_cincbid->H_creqcalbodyid",
						//			"B_cinpsnid->B_cinpsnid",
						"H_vdef9->H_vdef9",
						//			"H_coutcurrtype->H_coutcurrtype",//调出公司本位币
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

						//			"B_crelationid->B_crelationid",调拨关系id
						"B_vbatch->B_vbatchcode",
						"B_ctakeoutdeptid->B_csupplydeptid",
						"B_castunitid->B_castunitid",
						"B_vfree5->B_vfree5",
						//			"B_vbomcode->B_vbomcode",
						"B_vfree4->B_vfree4",
						//"B_dbizdate->B_dbizdate",
						"B_vfree3->B_vfree3",
						//			"B_dvalidate->B_dvalidate",失效日期
						"B_vfree2->B_vfree2",
						"B_csourceid->B_pk_plan",
						"B_SDATE->SYSDATE",
						"B_vfree1->B_vfree1",
						"B_coutcbid->B_csupplycalbodyid",
						//			"B_dplanarrivedate->B_dplanarrivedate",
						"B_ctakeoutcorpid->H_csupplycorpid",
						"B_ctakeoutcbid->B_csupplycalbodyid",
						"B_cincbid->B_creqcalbodyid",
						//			"B_cbiztypeid->B_cbiztypeid",
						//			"B_coutspaceid->B_coutspaceid",
						"B_cinvbasid->B_pk_invbasdoc",
						"B_vsourcerowno->B_crowno",
						//			"B_pk_areacl->B_pk_areacl",
						//			"B_crelation_bid->B_crelation_bid",
						"B_vbdef20->B_vbdef20",
						"B_vbdef19->B_vbdef19",
						"B_vbdef18->B_vbdef18",
						"B_vbdef17->B_vbdef17",
						"B_vbdef16->B_vbdef16",
						"B_vbdef15->B_vbdef15",
						"B_vbdef14->B_vbdef14",
						"B_vbdef13->B_vbdef13",
						"B_vbdef12->B_vbdef12",
						"B_vbdef11->B_vbdef11",
						"B_vbdef10->B_vbdef10",
						"B_vbdef9->B_vbdef9",
						"B_vbdef8->B_vbdef8",
						"B_vbdef7->B_vbdef7",
						"B_vbdef6->B_vbdef6",
						"B_vbdef5->B_vbdef5",
						//			"B_dplanoutdate->B_dplanoutdate",
						"B_vbdef4->B_vbdef4",
						"B_cindeptid->H_capplydeptid",
						"B_vbdef3->B_vbdef3",
						"B_vbdef2->B_vbdef2",
						"B_vbdef1->B_vbdef1",
						"B_nmny->B_nmny",
						//			"B_ctakerelationid->B_ctakerelationid",
						//			"B_fallocflag->B_fallocflag",//----------------------
						"B_vsourcecode->B_vbillno",
						//			"B_cprojectid->B_cprojectid",
						"B_SOPERATOR->SYSOPERATOR",
						//			"B_dproducedate->B_dproducedate",
						//			"B_vproducebatch->B_vproducebatch",
						"B_coutwhid->B_csupplywarehouseid",
						"B_cincorpid->H_pk_corp",
						//			"B_pk_rt_bid->B_pk_rt_bid",
						"B_coutinvid->B_cinventoryid",//-------------------------------
						//			"B_cworkcenterid->B_cworkcenterid",
						"B_ctakeoutwhid->B_csupplywarehouseid",
						"B_cinwhid->B_creqwarehouseid",
						"B_csourcebid->B_pk_planother_b",
						"B_ctakeoutinvid->B_cinventoryid",
						//			"B_ctakerelation_bid->B_ctakerelation_bid",
						//			"B_creceieveid->B_creceieveid",
						//      "B_creceiverbasid->B_creceiverbasid",
						//			"B_ctakeoutspaceid->B_ctakeoutspaceid",
						//			"B_pk_tldid->B_pk_tldid",
						//			"B_cinspaceid->B_cinspaceid",
						"B_nassistnum->B_nassistnum",
						//			"B_frowstatuflag->B_frowstatuflag",//------------------------------
						"B_nchangerate->B_hsl",
						"B_coutdeptid->B_csupplydeptid",
						"B_cininvid->B_cinventoryid",//------------------------------
						//			"B_pk_sendtype->B_pk_sendtype",
						//			"B_naddpricerate->B_naddpricerate",
						"B_nprice->B_nprice",
						"B_coutcorpid->B_csupplycorpid",
						//			"B_cprojectphase->B_cprojectphase",
						"B_crowno->B_crowno",
						//			"B_tplanouttime->B_tplanouttime",
						//			"B_tplanarrivetime->B_tplanarrivetime",

						//			"B_pk_arrivearea->B_pk_arrivearea",
						//			"B_vreceiveaddress->B_vreceiveaddress",
									"B_cquoteunitid->B_pk_measdoc",
									
									
						//			"B_norgqttaxnetprc->B_norgqttaxnetprc",
						//			"B_norgqtnetprc->B_norgqtnetprc",

						//			"B_cvendorid->B_cvendorid",
						//      "B_flargess->B_flargess",//---------------------------------------------
						//			"H_batpcheck->H_batpcheck"

						"B_cfirstid->B_pk_plan",
						"B_cfirstbid->B_pk_planother_b",
						"B_cfirsttypecode->H_pk_billtype",
						"B_vfirstcode->H_vbillno",
						"B_vfirstrowno->B_crowno",
				});
}

	public String[] getFormulas()
	{
		return new String[]{
        "H_iversion->1.0",
        "B_nquoteunitrate->1.0",
				"B_csourcetypecode->\"HG02\"",
				"B_bsettleendflag->\"N\"",
				"B_btakesettleendflag->\"N\"",
				"B_bsendendflag->\"N\"",
				"B_boutendflag->\"N\"",
        "B_nnum->B_nreserve10-zeroifnull(B_nouttotalnum)",
        "B_nquoteunitnum->B_nreserve10-zeroifnull(B_nouttotalnum)",
        //单据类型
        "H_cbilltype->\"5X\"",
        "B_cbilltype->\"5X\"",
        "H_fallocflag->1",
        "B_fallocflag->1",
      //是否直运
		"H_bdrictflag->\"N\"",//--------------------------------
        "B_bretractflag->\"N\""//-------------------------------
        //源头
//        "B_cfirstid->iif(B_cfirstid==null,B_cbillid,B_cfirstid)",
//        "B_cfirstbid->iif(B_cfirstbid==null,B_cbill_bid,B_cfirstbid)",
//        "B_cfirsttypecode->iif(B_cfirsttypecode==null,\"5A\",B_cfirsttypecode)",
//        "B_vfirstcode->iif(B_vfirstcode==null,B_vcode,B_vfirstcode)",
//        "B_vfirstrowno->iif(B_vfirstrowno==null,B_crowno,B_vfirstrowno)",
        };
	}

	public String getOtherClassName()
	{
		return null;
	}

	public UserDefineFunction[] getUserDefineFunction()
	{
		return null;
	}
}