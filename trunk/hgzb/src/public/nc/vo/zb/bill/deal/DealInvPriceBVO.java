package nc.vo.zb.bill.deal;

import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zb.bidding.BiddingBodyVO;

/**
 * 
 * @author zhf
 * @˵�������׸ڿ�ҵ��������� Ʒ�ֱ�����Ϣvo   ��ʵ��vo
 * 2011-5-4����02:45:23
 */
public class DealInvPriceBVO extends BiddingBodyVO {
	private UFDouble nllowerprice = null;//��ͱ���
	private UFDouble nprice = null;//�б��--���۷�ά���ڵ�ʹ������Ϊ��߼ۺ���ͼ۵�ƽ��ֵ--613�㷨������¼��߼�
	private UFBoolean bisgb = UFBoolean.FALSE;//�Ƿ����
	
	private UFDouble ngrade = null;//���۷�-----ƽ����
	private UFDouble nadjgrade = null;//���۷ֵ���
	
	private  UFDouble nmaxgrade = null;//��߱��۵÷�   611 �㷨����������
	private  UFDouble nmingrade = null;//��ͱ��۵÷�  611 �㷨����������
	
	
	
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
