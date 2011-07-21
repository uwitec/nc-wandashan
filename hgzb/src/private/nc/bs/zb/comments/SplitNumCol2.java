package nc.bs.zb.comments;

import java.util.HashMap;
import java.util.Map;

import nc.bs.zb.pub.ZbBsPubTool;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.zb.comments.BiEvaluationBodyVO;
import nc.vo.zb.comments.BidSlvendorVO;
import nc.vo.zb.comments.ISplitNumPara;
import nc.vo.zb.parmset.ParamSetVO;
import nc.vo.zb.pub.ZbPubConst;
import nc.vo.zb.pub.ZbPubTool;

public class SplitNumCol2 implements AbstractSplitNumCol{
/**
 * 入围供应商分量算法类 算法描述：  取招标数量按比例分摊
 * 1、获取供应商得分情况
 * 2、获取标段的入围供应商分摊比例
 * 3、根据总分和分摊比例计算出各个供应商的限额
 * 4、捉个品种根据供应商报价分由高到低进行分量  考虑各个供应商的限额
 */
	public SplitNumCol2(ISplitNumPara para){
		super();
		setPara(para);
	}
	public SplitNumCol2(){
		super();
//		setPara(para);
	}
	private SplitNumColPara2 para = null;
	public void setPara(ISplitNumPara para){
		this.para = (SplitNumColPara2)para;
	}
	
	public void col() throws BusinessException{
		if(para == null)
			throw new BusinessException("数据为空");
//		遍历品种   各个供应商根据得分按分摊比例 分量，分量过程中 注意数量上挤，最后数量全部累计到最后一个供应商，保证数量全部分出去
		HYBillVO bill = para.getBill();
		if(bill == null)
			throw new BusinessException("数据为空");
		BiEvaluationBodyVO[] bodys = (BiEvaluationBodyVO[])bill.getChildrenVO();
		if(bodys == null || bodys.length == 0)
			throw new BusinessException("品种数据为空");
		
		BidSlvendorVO[] vendors = null;
		for(BiEvaluationBodyVO body:bodys){
			vendors = body.getBidSlvendorVOs();
			if(vendors == null || vendors.length == 0)
				throw new BusinessException("供应商数据异常");
			

			split(body, vendors);
		}
	}	
	
	public void split(BiEvaluationBodyVO body, BidSlvendorVO[] vendors)
			throws BusinessException {
		
		
		UFDouble nallnum = PuPubVO.getUFDouble_NullAsZero(body.getNzbnum());
		if(nallnum.equals(UFDouble.ZERO_DBL)){
			throw new BusinessException("存在招标数量为空的品种");
		}
		
		String srate = para.getSplitrate();
		
		int[] rates = ZbPubTool.colBiddingVendorRates(srate);
		Object[] grades = para.getGrades();
		int len = rates.length;
		
		ParamSetVO vo = ZbBsPubTool.getParam();//获取参数 是否按总分分量  add by zhw  2011-06-21 
		if(!PuPubVO.getUFBoolean_NullAs(vo.getFiscoltotal(),UFBoolean.FALSE).booleanValue()){
			if(rates.length != vendors.length || rates.length != grades.length)
				throw new BusinessException("【入围供应商数量】和【分量比例】设置不一致");
		}
		
		
		//供应商分量信息
		Map<String, UFDouble> vendorNumInfor = new HashMap<String, UFDouble>();
		
		int iall = 0;
        for(int i = 0;i<len;i++){
		iall += rates[i];	
		}
        if(iall == 0)
        	throw new BusinessException("【供应商分量比例】设置异常，0");
		
		UFDouble nunitnum = nallnum.div(iall);//单位数量    注意中标数量尽量不出现小数位  上挤   最后余数全部给最后一位供应商
		UFDouble tmpNum = null;
		UFDouble nallsplitnum = UFDouble.ZERO_DBL;
		Object[] os = null;
		for(int i = 0;i<len;i++){
			os = (Object[])grades[i];
			if(i<len-1){//非最后一个供应商
				tmpNum = nunitnum.multiply(rates[i]);
				if(tmpNum.doubleValue()-tmpNum.intValue()>0){
					tmpNum = new UFDouble(tmpNum.intValue()+1);//出现小数部分凑整 上挤
				}
				nallsplitnum = nallsplitnum.add(tmpNum);
				vendorNumInfor.put(ZbPubTool.getString_NullAsTrimZeroLen(os[0]), tmpNum);
			}else{
				tmpNum = nallnum.sub(nallsplitnum);
				if(tmpNum.doubleValue()<=0){//最后供应商没有量了    tmpNum  不可能大于1
					vendorNumInfor.put(ZbPubTool.getString_NullAsTrimZeroLen(os[0]), tmpNum.add(UFDouble.ONE_DBL));
					tmpNum = vendorNumInfor.get(ZbPubTool.getString_NullAsTrimZeroLen(((Object[])grades[i-1])[0]));
					//上一个供应商减去 1个量  送给最后一个供应商
					vendorNumInfor.put(ZbPubTool.getString_NullAsTrimZeroLen(((Object[])grades[i-1])[0]), tmpNum.sub(UFDouble.ONE_DBL));
				}else{
					vendorNumInfor.put(ZbPubTool.getString_NullAsTrimZeroLen(os[0]), tmpNum);
				}
			}			
		}
		
		
		for(BidSlvendorVO vendor:vendors){
			tmpNum = vendorNumInfor.get(vendor.getCcustmanid());
			if(tmpNum == null)
				throw new BusinessException("供应商分量异常，出现空值");
			vendor.setNzbnum(tmpNum);
			vendor.setNzbmny(tmpNum.multiply(
				PuPubVO.getUFDouble_NullAsZero(body.getNzbprice()), ZbPubConst.MNY_DIGIT));
			//设置中标比例
			vendor.setNwinpercent(vendor.getNzbnum().div(body.getNzbnum(), ZbPubConst.NUM_DIGIT).multiply(100));
		}
	}
}
