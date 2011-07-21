package nc.bs.zb.comments;

import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.voutils.VOUtil;
import nc.vo.zb.comments.BiEvaluationBodyVO;
import nc.vo.zb.comments.BidSlvendorVO;
import nc.vo.zb.comments.ISplitNumPara;
import nc.vo.zb.pub.ZbPubConst;

public class SplitNumCol implements AbstractSplitNumCol {
/**
 * 入围供应商分量算法类 算法描述：  取招标总金额按比例分配限额   再  分量
 * 1、获取供应商得分情况
 * 2、获取标段的入围供应商分摊比例
 * 3、根据总分和分摊比例计算出各个供应商的限额
 * 4、捉个品种根据供应商报价分由高到低进行分量  考虑各个供应商的限额
 */
	public SplitNumCol(SplitNumColPara para){
		super();
		setPara(para);
	}
	public SplitNumCol(){
		super();
//		setPara(para);
	}
	private SplitNumColPara para = null;
	public void setPara(ISplitNumPara para){
		this.para = (SplitNumColPara)para;
	}
	
	public void col() throws BusinessException{
		if(para == null)
			throw new BusinessException("数据为空");
//		遍历品种   优先考虑该品种报价分最高的供应商  考虑该供应商限额  注意数量上挤
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
			VOUtil.descSort(vendors, BidSlvendorVO.sort_fields);//按供应商报价分降序排序
			for(BidSlvendorVO vendor:vendors){
				split(body, vendor);
				if(PuPubVO.getUFDouble_NullAsZero(body.getNsplitnum()).equals(UFDouble.ZERO_DBL))
					break;//该品种分量结束   否则继续 分配到下一个供应商上
			}
		}
	}	
	
	public void split(BiEvaluationBodyVO body, BidSlvendorVO vendor)
			throws BusinessException {
//		品种本次待分配数量
		UFDouble nnum = getNum(body);
		// 品种待分配总金额
		UFDouble nmny = nnum.multiply(
				PuPubVO.getUFDouble_NullAsZero(body.getNzbprice()), 8);
		// 可用限额
		UFDouble nxe = getXe(vendor.getCcustmanid());
		
		if (nxe.doubleValue() > nmny.doubleValue()
				- body.getNzbprice().doubleValue()) {// 该供应商该品种全部中标
			vendor.setNzbnum(nnum);
			
			vendor.setNzbmny(nmny);
			para.getOldMnyMap().put(vendor.getCcustmanid(),
					PuPubVO.getUFDouble_NullAsZero(para.getOldMnyMap().get(vendor.getCcustmanid())).add(nmny));
		} else {
			vendor.setNzbmny(nxe);
			vendor.setNzbnum(nxe.div(body.getNzbprice()));
			para.getOldMnyMap().put(vendor.getCcustmanid(),
					PuPubVO.getUFDouble_NullAsZero(para.getOldMnyMap().get(vendor.getCcustmanid())).add(nxe));
		}
		//设置中标比例
		vendor.setNwinpercent(vendor.getNzbnum().div(body.getNzbnum(), ZbPubConst.NUM_DIGIT).multiply(100));
		body.setNsplitnum(nnum.sub(vendor.getNzbnum()));
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业） 获取供应商可用限额  供应商最高限额-已使用金额
	 * 2011-5-24下午07:40:26
	 * @param cvendorid
	 * @return
	 */
	private UFDouble getXe(String cvendorid){
		 return PuPubVO.getUFDouble_NullAsZero(
					para.getMnyMap().get(cvendorid)).sub(
					PuPubVO.getUFDouble_NullAsZero(para.getOldMnyMap().get(cvendorid)));
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）获取品种待分配数量
	 * 2011-5-24下午07:40:44
	 * @param body
	 * @return
	 */
	private UFDouble getNum(BiEvaluationBodyVO body){
		return PuPubVO.getUFDouble_NullAsZero(
				body.getNsplitnum() == null ? body.getNzbnum() : body
						.getNsplitnum());
	}
}
