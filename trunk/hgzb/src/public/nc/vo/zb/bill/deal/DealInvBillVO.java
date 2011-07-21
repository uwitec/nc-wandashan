package nc.vo.zb.bill.deal;

import java.util.HashSet;
import java.util.Set;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.voutils.VOUtil;

/**
 * 
 * @author zhf
 * @说明：（鹤岗矿业）评标管理 品种供应商报价信息聚合vo
 * 2011-5-4下午02:45:23
 */
public class DealInvBillVO extends AggregatedValueObject {
	
	private DealInvPriceBVO head = null;
	private DealVendorPriceBVO[] bodys = null;
	
	public static String[] vendor_sort = new String[]{"nallgrade","nquotatpoints","nqualipoints","ccircalnoid"};
	public static int[]  vendor_sort_rule = new int[]{VOUtil.DESC,VOUtil.DESC,VOUtil.DESC,VOUtil.ASC};
	public static String[] vendor_all = new String[]{"ccircalnoid","circalname","nprice"};
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）将供应商报价明细数据排序并分类处理
	 * 2011-6-3下午06:04:54
	 */
	public static  void sortAndDealBodys(DealVendorPriceBVO[] bodys){
		if(bodys==null || bodys.length == 0){
			return;
		}
		for(DealVendorPriceBVO body:bodys){
			body.setNallgrade(PuPubVO.getUFDouble_NullAsZero(body.getNqualipoints()).add(PuPubVO.getUFDouble_NullAsZero(body.getNquotatpoints())));
		}
		VOUtil.sort(bodys, vendor_sort,vendor_sort_rule,true);
		Set<String> ss = new HashSet<String>();
		String key = null;
		for(DealVendorPriceBVO body:bodys){
			key = body.getCcustmanid();
			if(ss.contains(key)){
				String[] names = body.getAttributeNames();
				for(String name:names){
					if(name.equalsIgnoreCase(vendor_all[0])||name.equalsIgnoreCase(vendor_all[1])||name.equalsIgnoreCase(vendor_all[2]))
						continue;
					body.setAttributeValue(name, null);
				}
			}else
				ss.add(key);
		}
	}
	
	public DealInvPriceBVO getHeader(){
		return head;
	}
	public DealVendorPriceBVO[] getBodys(){
		return bodys;
	}

	@Override
	public CircularlyAccessibleValueObject[] getChildrenVO() {
		// TODO Auto-generated method stub
		return bodys;
	}

	@Override
	public CircularlyAccessibleValueObject getParentVO() {
		// TODO Auto-generated method stub
		return head;
	}

	@Override
	public void setChildrenVO(CircularlyAccessibleValueObject[] children) {
		// TODO Auto-generated method stub

		bodys = (DealVendorPriceBVO[])children;
	}

	@Override
	public void setParentVO(CircularlyAccessibleValueObject parent) {
		// TODO Auto-generated method stub

		head = (DealInvPriceBVO)parent;
	}

}
