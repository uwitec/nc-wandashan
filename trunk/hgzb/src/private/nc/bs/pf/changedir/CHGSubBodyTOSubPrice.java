package nc.bs.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;

public class CHGSubBodyTOSubPrice extends nc.bs.pf.change.VOConversion {
	/**
     * 报价单体生成报价明细 zhf
     */
    public CHGSubBodyTOSubPrice() {
        super();
    }

    /**
     * 获得后续类的全录经名称。
     * 
     * @return java.lang.String[]
     */
    public String getAfterClassName() {
//        return "nc.bs.zb.conversion.Zb01toZb02AfterChange";
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
//    			"H_csubmitpriceid->"//id
//    			,"H_cbiddingid->"//标书id
    			"H_cinvclid->H_cinvclid"//品种分类
//    			,"H_pk_corp->"//操作公司
    			,"H_cinvbasid->H_cinvbasid"//品种基本id
    			,"H_cinvmanid->H_cinvmanid"//品种管理id
    			,"H_cunitid->H_cunitid"//主计量单位
    			,"H_castunitid->H_castunitid"//辅计量单位
    			,"H_nnum->H_nnum"//招标主数量
    			,"H_nasnum->H_nasnum"//招标辅数量
    			,"H_vdef1->H_csubmitbill_bid"
    			,"H_nprice->H_nprice"
//    			,"H_cvendorid->"//供应商管理id
//    			,"H_ccircalnoid->"//轮次阶段id  默认次数：第一次、第二次、第三次
    			
    	};
    }

    /**
     * 获得公式。
     * @return java.lang.String[]
     */
    public String[] getFormulas() {
    	return null;
    }

    /**
     * 返回用户自定义函数。
     */
    public UserDefineFunction[] getUserDefineFunction() {
        return null;
    }
}
