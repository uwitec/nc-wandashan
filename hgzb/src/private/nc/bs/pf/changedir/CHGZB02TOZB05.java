package nc.bs.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
//中标审批表生成中标结果录入子表转换
public class CHGZB02TOZB05 extends nc.bs.pf.change.VOConversion{

	 public CHGZB02TOZB05() {
	        super();
	    }

	    /**
	     * 获得后续类的全录经名称。
	     * 
	     * @return java.lang.String[]
	     */
	    public String getAfterClassName() {
//	        return "nc.bs.zb.conversion.Zb01toZb02AfterChange";
	    	return null;
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
	    			"H_cinvclid->H_cinvclid",
	        		"H_cinvbasid->H_cinvbasid",
	        		"H_cinvmanid->H_cinvmanid",
	        		"H_cunitid->H_cunitid",
//	        		"B_nzbprice->B_nprice",
	        		"H_nzbnum->H_nzbnum",
	    			"H_crowno->H_crowno",
	    			"H_nzbprice->H_nzbprice",
	    			"H_norderprice->H_nzbprice",

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
	    			
	    			"H_csourcebillbid->H_cevaluationbid",
	    			"H_csourcebillhid->H_cevaluationid",
	    			
	    			"H_cupsourcebillrowid->H_cupsourcebillrowid",
	    			"H_cupsourcebillid->H_cupsourcebillid",
	    			"H_cupsourcebilltype->H_cupsourcebilltype"
	    			
	    	};
	    }

	    /**
	     * 获得公式。
	     * @return java.lang.String[]
	     */
	    public String[] getFormulas() {
	    	return new String[]{"H_nzbnmy->mul(H_nzbprice,H_nzbnum)","H_csourcetype->\"ZB02\""};
	    }

	    /**
	     * 返回用户自定义函数。
	     */
	    public UserDefineFunction[] getUserDefineFunction() {
	        return null;
	    }
}
