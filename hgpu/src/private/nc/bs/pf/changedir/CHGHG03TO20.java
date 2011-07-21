package nc.bs.pf.changedir;

import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.pf.change.UserDefineFunction;

//临时计划生成 采购计划

public class CHGHG03TO20 extends nc.bs.pf.change.VOConversion {
	/**
     * CHG30TO20 构造子注解。
     */
    public CHGHG03TO20() {
        super();
    }

    /**
     * 获得后续类的全录经名称。
     * 
     * @return java.lang.String[]
     */
    public String getAfterClassName() {
        return "nc.bs.hg.pu.conversion.PlanVOTOPPrayVO";
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
        		"H_dpraydate->SYSDATE",
        		"H_coperator->SYSOPERATOR",
        		"H_caccountyear->ACCONTYEAR",
        		"H_vmemo->H_vmemo",
                "H_vdef1->H_vdef1",
                "H_vdef2->H_vdef2", 
                "H_vdef3->H_vdef3", "H_vdef4->H_vdef4",
                "H_vdef5->H_vdef5", "H_vdef7->H_vdef7", "H_vdef8->H_vdef8",
                "H_vdef6->H_vdef6", "H_vdef9->H_vdef9", "H_vdef10->H_vdef10",
                "H_vdef11->H_vdef11", "H_vdef12->H_vdef12",
                "H_vdef13->H_vdef13", "H_vdef14->H_vdef14",
                "H_vdef15->H_vdef15", "H_vdef16->H_vdef16",
                "H_vdef17->H_vdef17", "H_vdef18->H_vdef18",
                "H_vdef19->H_vdef19", "H_vdef20->H_vdef20",
                "H_pk_defdoc1->H_pk_defdoc1", "H_pk_defdoc2->H_pk_defdoc2",
                "H_pk_defdoc3->H_pk_defdoc3", "H_pk_defdoc4->H_pk_defdoc4",
                "H_pk_defdoc5->H_pk_defdoc5", "H_pk_defdoc6->H_pk_defdoc6",
                "H_pk_defdoc7->H_pk_defdoc7", "H_pk_defdoc8->H_pk_defdoc8",
                "H_pk_defdoc9->H_pk_defdoc9", "H_pk_defdoc10->H_pk_defdoc10",
                "H_pk_defdoc11->H_pk_defdoc11", "H_pk_defdoc12->H_pk_defdoc12",
                "H_pk_defdoc13->H_pk_defdoc13", "H_pk_defdoc14->H_pk_defdoc14",
                "H_pk_defdoc15->H_pk_defdoc15", "H_pk_defdoc16->H_pk_defdoc16",
                "H_pk_defdoc17->H_pk_defdoc17", "H_pk_defdoc18->H_pk_defdoc18",
                "H_pk_defdoc19->H_pk_defdoc19", "H_pk_defdoc20->H_pk_defdoc20",
                "H_"+HgPubConst.PLAN_PROJECT_DEF+"->H_cplanprojectid",
                
                "B_pk_corp->SYSCORP",
                "B_cbaseid->B_pk_invbasdoc",
                "B_cmangid->B_cinventoryid",
                "B_npraynum->B_nnetnum",
                "B_nexchangerate->B_hsl",
                "B_cassistunit->B_castunitid",
//                "B_nassistnum->B_nassistnum", //附属量可能存在问题  计划的辅数量为毛需求的辅数量
                
                "B_ddemanddate->SYSDATE", //需求日期
                
                "B_csourcebilltype->H_pk_billtype",
                "B_csourcebillid->B_pk_plan",
                "B_csourcebillrowid->B_pk_planother_b",
                "B_cupsourcebillid->B_pk_plan",
                "B_cupsourcebillrowid->B_pk_planother_b",
                "B_vmemo->B_vmemo",
                "B_vproducenum->B_vbatchcode",
                "B_vfree1->B_vfree1",
                "B_vfree2->B_vfree2", "B_vfree3->B_vfree3",
                "B_vfree4->B_vfree4", "B_vfree5->B_vfree5",
                
                "B_cwarehouseid->B_creqwarehouseid",
                "B_pk_reqcorp->B_pk_corp",
                "B_pk_reqstoorg->B_creqcalbodyid",
                "B_vdef1->B_vdef1",
                "B_vdef2->B_vdef2", "B_vdef3->B_vdef3", "B_vdef4->B_vdef4",
                "B_vdef5->B_vdef5", "B_vdef6->B_vdef6", "B_vdef7->B_vdef7",
                "B_vdef8->B_vdef8", "B_vdef9->B_vdef9", "B_vdef10->B_vdef10",
                "B_vdef11->B_vdef11", "B_vdef12->B_vdef12",
                "B_vdef13->B_vdef13", "B_vdef14->B_vdef14",
                "B_vdef15->B_vdef15", "B_vdef16->B_vdef16",
                "B_vdef17->B_vdef17", "B_vdef18->B_vdef18",
                "B_vdef19->B_vdef19", "B_vdef20->B_vdef20",
                "B_pk_defdoc1->B_pk_defdoc1", "B_pk_defdoc2->B_pk_defdoc2",
                "B_pk_defdoc3->B_pk_defdoc3", "B_pk_defdoc4->B_pk_defdoc4",
                "B_pk_defdoc5->B_pk_defdoc5", "B_pk_defdoc6->B_pk_defdoc6",
                "B_pk_defdoc7->B_pk_defdoc7", "B_pk_defdoc8->B_pk_defdoc8",
                "B_pk_defdoc9->B_pk_defdoc9", "B_pk_defdoc10->B_pk_defdoc10",
                "B_pk_defdoc11->B_pk_defdoc11", "B_pk_defdoc12->B_pk_defdoc12",
                "B_pk_defdoc13->B_pk_defdoc13", "B_pk_defdoc14->B_pk_defdoc14",
                "B_pk_defdoc15->B_pk_defdoc15", "B_pk_defdoc16->B_pk_defdoc16",
                "B_pk_defdoc17->B_pk_defdoc17", "B_pk_defdoc18->B_pk_defdoc18",
                "B_pk_defdoc19->B_pk_defdoc19", "B_pk_defdoc20->B_pk_defdoc20",
                "B_cprojectid->B_cprojectid","B_cprojectphaseid->B_cprojectphaseid",
                "B_pk_purcorp->SYSCORP",
                "B_vproducenum->B_vbatchcode"};
    }

    /**
     * 获得公式。
     * @return java.lang.String[]
     */
    public String[] getFormulas() {
        return new String[] { 
        		"H_ibillstatus->int(0)",
        		"H_iprintcount->int(0)", 
        		"H_ipraysource->int(8)",
                "H_nversion->int(1)", 
                "B_cupsourcebilltype->\"HG03\"", 
                "B_nassistnum->B_nnetnum/B_hsl", //附属量可能存在问题  计划的辅数量为毛需求的辅数量
                "B_status->int(2)",
                "B_"+HgPubConst.PRAY_BILL_VMI_FIELDNAME+"->getColValue(bd_invbasdoc,"+HgPubConst.INVBAS_VMI_FIELDNAME+",pk_invbasdoc,B_pk_invbasdoc)",
                "B_"+HgPubConst.PRAY_BILL_PURTYPE_FIELDNAME+"->getColValue(bd_invbasdoc,"+HgPubConst.INVBAS_PURTYPE_FIELDNAME+",pk_invbasdoc,B_pk_invbasdoc)"
                };
    }

    /**
     * 返回用户自定义函数。
     */
    public UserDefineFunction[] getUserDefineFunction() {
        return null;
    }
}
