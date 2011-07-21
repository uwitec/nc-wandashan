package nc.vo.zb.bill.deal;

import nc.vo.pub.lang.UFDouble;
import nc.vo.zb.bidding.BiddingSuppliersVO;

/**
 * 
 * @author zhf
 * @˵�������׸ڿ�ҵ��������� ��Ӧ�̱�����Ϣvo  ��ʵ��vo
 * 2011-5-4����02:45:23
 */
public class DealVendorPriceBVO extends BiddingSuppliersVO {

	private String ccircalnoid = null;
	private UFDouble nprice = null;
	
	private transient UFDouble nallgrade;//zhf ����ʱ��ʱʹ��  ��Ӧ���ܷ�
	
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
