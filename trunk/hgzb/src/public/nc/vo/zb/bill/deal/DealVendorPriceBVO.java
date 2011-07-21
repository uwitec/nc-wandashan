package nc.vo.zb.bill.deal;

import nc.vo.pub.lang.UFDouble;
import nc.vo.zb.bidding.BiddingSuppliersVO;

/**
 * 
 * @author zhf
 * @说明：（鹤岗矿业）评标管理 供应商报价信息vo  非实体vo
 * 2011-5-4下午02:45:23
 */
public class DealVendorPriceBVO extends BiddingSuppliersVO {

	private String ccircalnoid = null;
	private UFDouble nprice = null;
	
	private transient UFDouble nallgrade;//zhf 排序时临时使用  供应商总分
	
    public static transient String[] asc_sort_fieldnames = new String[]{"nallgrade"};
	
	public UFDouble getNallgrade() {
		return nallgrade;
	}
	public void setNallgrade(UFDouble nallgrade) {
		this.nallgrade = nallgrade;
	}
	public String getCcircalnoid() {
		return ccircalnoid;
	}
	public void setCcircalnoid(String ccircalnoid) {
		this.ccircalnoid = ccircalnoid;
	}
	public UFDouble getNprice() {
		return nprice;
	}
	public void setNprice(UFDouble nprice) {
		this.nprice = nprice;
	}
	
	public Object getAttributeValue(String name) {
		if(name.equalsIgnoreCase("nprice")){
			return nprice;
		}else if(name.equalsIgnoreCase("ccircalnoid")){
			return ccircalnoid;
		}
		return super.getAttributeValue(name);	
	}
	public void setAttributeValue(String name, Object value) {
		if(name.equalsIgnoreCase("nprice")){
			setNprice((UFDouble)value);
		}else if(name.equalsIgnoreCase("ccircalnoid")){
			setCcircalnoid((String)value);
		}
		super.setAttributeValue(name,value);	
	}
}
