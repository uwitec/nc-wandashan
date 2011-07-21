package nc.bs.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;

public class CHGBiddingBodyTOSubPrice extends nc.bs.pf.change.VOConversion {
	/**
     * 标书表体生成  报价明细 zhf
     */
    public CHGBiddingBodyTOSubPrice() {
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
    			
//    			private String csubmitpriceid->"//id
    			"H_cbiddingid->H_cbiddingid"//标书id
    			,"H_cinvclid->H_cinvclid"//品种分类
//    			,"H_pk_corp->"//操作公司
    			,"H_cinvbasid->H_cinvbasid"//品种基本id
    			,"H_cinvmanid->H_cinvmanid"//品种管理id
    			,"H_cunitid->H_cunitid"//主计量单位
//    			,"H_castunitid->H_castunitid"//辅计量单位
    			,"H_nnum->H_nzbnum"//招标主数->"
//    			,"H_nasnum->"//招标辅数量
//    			,"H_cvendorid->"//供应商管理id
//    			,"H_ccircalnoid->"//轮次阶段id  默认次数：第一次、第二次、第三次
    			
//    			private Integer isubmittype->"//报价类型 0web 1local 2手工录入 3恶意报价（只有网上招标有恶意报价）
//    			,"H_nprice->"//报价
//    			,"H_nlastprice->"//上轮报价
//    			,"H_nllowerprice->"//上轮最低报价
    			
    			,"H_nmarkprice->H_nmarkprice"//标底价  zhf 
    			
//    			zhf add  现场报价单需用  后续追加  不需要存入报价明细表内
    			,"H_nplanprice->H_nplanprice"//计划价
    			,"H_nmarketprice->H_nmarketprice"//市价
    			,"H_naverageprice->H_naverageprice"//历史平均价
    			
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
