package nc.vo.zb.bill.deal;

import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zb.bidding.BiddingBodyVO;

/**
 * 
 * @author zhf
 * @说明：（鹤岗矿业）评标管理 品种报价信息vo   非实体vo
 * 2011-5-4下午02:45:23
 */
public class DealInvPriceBVO extends BiddingBodyVO {
	private UFDouble nllowerprice = null;//最低报价
	private UFDouble nprice = null;//中标价--报价分维护节点使用他作为最高价和最低价的平均值--613算法调整记录最高价
	private UFBoolean bisgb = UFBoolean.FALSE;//是否跟标
	
	private UFDouble ngrade = null;//报价分-----平均分
	private UFDouble nadjgrade = null;//报价分调整
	
	private  UFDouble nmaxgrade = null;//最高报价得分   611 算法调整后新增
	private  UFDouble nmingrade = null;//最低报价得分  611 算法调整后新增
	
	
	
	public UFDouble getNmaxgrade() {
		return nmaxgrade;
	}

	public void setNmaxgrade(UFDouble nmaxgrade) {
		this.nmaxgrade = nmaxgrade;
	}

	public UFDouble getNmingrade() {
		return nmingrade;
	}

	public void setNmingrade(UFDouble nmingrade) {
		this.nmingrade = nmingrade;
	}

	public UFDouble getNadjgrade() {
		return nadjgrade;
	}

	public void setNadjgrade(UFDouble nadjgrade) {
		this.nadjgrade = nadjgrade;
	}

	public UFDouble getNgrade() {
		return ngrade;
	}

	public void setNgrade(UFDouble ngrade) {
		this.ngrade = ngrade;
	}

	public String getInvID(boolean isinv){
		return isinv?getCinvmanid():getCinvclid();
	}
	
	public UFDouble getNllowerprice() {
		return nllowerprice;
	}
	public void setNllowerprice(UFDouble nllowerprice) {
		this.nllowerprice = nllowerprice;
	}
	public UFDouble getNprice() {
		return nprice;
	}
	public void setNprice(UFDouble nprice) {
		this.nprice = nprice;
	}
	public UFBoolean getBisgb() {
		return bisgb;
	}
	public void setBisgb(UFBoolean bisgb) {
		this.bisgb = bisgb;
	}
	
	public Object getAttributeValue(String name) {
		if(name.equalsIgnoreCase("nprice")){
			return nprice;
		}else if(name.equalsIgnoreCase("nllowerprice")){
			return nllowerprice;
		}else if(name.equalsIgnoreCase("bisgb")){
			return bisgb;
		}
		return super.getAttributeValue(name);	
	}
	public void setAttributeValue(String name, Object value) {
		if(name.equalsIgnoreCase("nprice")){
			setNprice((UFDouble)value);
		}else if(name.equalsIgnoreCase("nllowerprice")){
			setNllowerprice((UFDouble)value);
		}else if(name.equalsIgnoreCase("bisgb")){
			setAttributeValue(name, PuPubVO.getUFBoolean_NullAs(value, UFBoolean.FALSE));
		}
		super.setAttributeValue(name,value);	
	}
}
