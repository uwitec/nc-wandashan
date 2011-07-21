package nc.bs.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;

public class CHGZB01TOZB02 extends nc.bs.pf.change.VOConversion {
	/**
     * 标书 经过评标 生成 评审表 时使用 zhf
     */
    public CHGZB01TOZB02() {
        super();
    }

    /**
     * 获得后续类的全录经名称。
     * 
     * @return java.lang.String[]
     */
    public String getAfterClassName() {
        return "nc.bs.zb.conversion.Zb01toZb02AfterChange";
//    	return null;
    }

    /**
     * 获得另一个后续类的全录径名称。
     * 
     * @return java.lang.String[]
     */
    public String getOtherClassName() {
        return null;
    }

    /**
     * 获得字段对应。
     * 
     * @return java.lang.String[]
     */
    public String[] getField() {
        return new String[] {  
        		"H_pk_corp->H_pk_corp",
        		"H_dmakedate->SYSDATE",
        		"H_dbilldate->SYSDATE",
        		"H_voperatorid->SYSOPERATOR",
        		"H_pk_deptdoc->H_pk_deptdoc",
        		"H_cbiddingid->H_cbiddingid",
        		"H_vemployeeid->H_vemployeeid",
        		
        		"B_cinvclid->B_cinvclid",
        		"B_cinvbasid->B_cinvbasid",
        		"B_cinvmanid->B_cinvmanid",
        		"B_cunitid->B_cunitid",
//        		"B_nzbprice->B_nprice",
        		"B_nzbnum->B_nzbnum",
        		
        		
        		"H_vdef1->H_vdef1",
    			"H_vdef2->H_vdef2",
    			"H_vdef3->H_vdef3",
    			"H_vdef4->H_vdef4",
    			"H_vdef5->H_vdef5",
    			"H_vdef6->H_vdef6",
    			"H_vdef7->H_vdef7",
    			"H_vdef8->H_vdef8",
    			"H_vdef9->H_vdef9",
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
    			
    			"B_vdef1->B_vdef1",
    			"B_vdef2->B_vdef2",
    			"B_vdef3->B_vdef3",
    			"B_vdef4->B_vdef4",
    			"B_vdef5->B_vdef5",
    			"B_vdef6->B_vdef6",
    			"B_vdef7->B_vdef7",
    			"B_vdef8->B_vdef8",
    			"B_vdef9->B_vdef9",
    			"B_vdef10->B_vdef10",
    			"B_vdef11->B_vdef11",
    			"B_vdef12->B_vdef12",
    			"B_vdef13->B_vdef13",
    			"B_vdef14->B_vdef14",
    			"B_vdef15->B_vdef15",
    			"B_vdef16->B_vdef16",
    			"B_vdef17->B_vdef17",
    			"B_vdef18->B_vdef18",
    			"B_vdef19->B_vdef19",
    			"B_vdef20->B_vdef20",
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
//    			zhf add
    			"B_csourcebillhid->B_cbiddingid",
    			"B_csourcebillbid->B_cbiddingbid",
    			"B_cupsourcebillrowid->B_cbiddingbid",
    			"B_cupsourcebillid->B_cbiddingid"
        		};
    }

    /**
     * 获得公式。
     * @return java.lang.String[]
     */
    public String[] getFormulas() {
        return new String[] { 
        		"H_vbillstatus->int(8)",
        		"H_pk_billtype->\"ZB02\"",
        		"B_csourcetype->\"ZB01\"",
        		"B_cupsourcebilltype->\"ZB01\"",
        		"H_dr->0",
        		"B_dr->0"
//        		"H_ipraysource->int(8)",
//                "H_nversion->int(1)", 
//                "B_cupsourcebilltype->\"HG01\"", 
//                "B_nassistnum->B_nnetnum/B_hsl", //附属量可能存在问题  计划的辅数量为毛需求的辅数量
//                "B_status->int(2)",
////                "B_"+HgPubConst.PRAY_BILL_VMI_FIELDNAME+"->getColValue(bd_invbasdoc,"+HgPubConst.INVBAS_VMI_FIELDNAME+",pk_invbasdoc,B_pk_invbasdoc)",
//                "B_"+HgPubConst.PRAY_BILL_PURTYPE_FIELDNAME+"->getColValue(bd_invbasdoc,"+HgPubConst.INVBAS_PURTYPE_FIELDNAME+",pk_invbasdoc,B_pk_invbasdoc)"
                };
    }

    /**
     * 返回用户自定义函数。
     */
    public UserDefineFunction[] getUserDefineFunction() {
        return null;
    }
}
