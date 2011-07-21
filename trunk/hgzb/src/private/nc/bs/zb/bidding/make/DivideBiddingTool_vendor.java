package nc.bs.zb.bidding.make;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import nc.vo.pub.lang.UFDouble;
import nc.vo.zb.bidding.HistoryPriceVO;

/**
 * 
 * @author zhf
 * @说明：（鹤岗矿业）系统自动划分标段工具类    以供应商为标段划分基本依据的算法
 * 2011-5-18下午02:04:32
 */


public class DivideBiddingTool_vendor extends AbstractDivideBiddingTool{
	
	
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）系统根据供应商历史供货信息划分标段
	 * 2011-5-19上午10:21:53
	 * @param prices  供应商历史供应信息  取自采购合同
	 * @return  划分好的标段   
	 * Object[]  0为品种历史品均价MAP  1 标段信息Object[]  [0]---标段入围品种 [1]----标段入围供应商
	 * @throws Exception
	 */
	public Object divid(HistoryPriceVO[] prices) throws Exception{
		if(prices == null || prices.length == 0)
			return null;
		//品种供应商映射
		Map<String, Set<String>> invVendorInfor = new HashMap<String, Set<String>>();
		//供应商品种映射 需要排序   按每个供应商可供应的品种数量 升序排列  treemap 按key排序
//		Map<String,Set<String>> vendorInvInfor = new TreeMap<String, Set<String>>();
		//特殊数据结构  每一个内部集合第一个值为供应商 其他值为品种id   外部集合按内部集合大小升序排列
		Set<List<String>> vendorInvInfor = new TreeSet<List<String>>(new SizeComparator());
//		按品种封装历史品均价
		Map<String,UFDouble> priceInfor = colHistoryPrice(prices);
//		重新组合数据
		initInfor(prices, invVendorInfor, vendorInvInfor);		
		
		List ldata = col(invVendorInfor, vendorInvInfor);
		
		if(ldata == null || ldata.size() == 0)
			return null;
		return new Object[]{priceInfor,ldata};
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）划分标段核心算法
	 * 算法描述：将供应商的供应品种数目由少到多 依次  作为预划分标段，筛选已经进入其他标段的品种，根据品种的历史供应信息选择其供应商
	 *          从而划分出标段
	 * 2011-5-19上午10:27:16
	 * @param invVendorInfor 品种的历史供应信息
	 * @param vendorInvInfor 供应商的供应品种历史信息
	 * @return  划分好的标段信息
	 * @throws Exception
	 */
	private List<Object[]> col(Map<String, Set<String>> invVendorInfor,Set<List<String>> vendorInvInfor)
	throws Exception{
		
		if(invVendorInfor == null || invVendorInfor.size() == 0|| vendorInvInfor == null || vendorInvInfor.size() == 0)
			return null;
		
		List ret = new ArrayList();
		
//		划分到同一标段的供应商集合   用set保证供应商不出现重复
		Set<String> sVendor = null;
//		已经进入标段的品种          临时标志那些品种已经划分完成
		Set<String> tmpInvSet = new HashSet<String>();
		
		String tmpVendor = null;//按供应商计算时保留当前供应商
		Object[] os = null;//标段数据结构   os[0]---标段入围品种   os[1]----标段入围供应商
		
		Iterator it =vendorInvInfor.iterator();
		List<String> tmpInvs = null;
		while(it.hasNext()){//按供应商供应品种数目由小到大遍历    数据结构已排序
			tmpInvs = (List<String>)it.next();
			tmpVendor = tmpInvs.get(0);//第一个值放的供应商
			//去掉供应商
			tmpInvs.remove(0);
//			去掉已经进入其他标段的品种
			dealInv(tmpInvSet,tmpInvs);
			if(tmpInvs.size()==0){
				continue;
			}
//			剩下的品种作为标段选择其供应商
			sVendor = chooseVendor(tmpInvs,invVendorInfor);
			if(sVendor == null)
				sVendor = new HashSet<String>();
			sVendor.add(tmpVendor);//该供应商已入围该标段
			tmpInvSet.addAll(tmpInvs);//该品种已被划分
			os = new Object[]{tmpInvs,sVendor};
			ret.add(os);
		}
		
		return ret;
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）筛选出能为标段内所有品种供货的供应商
	 * 2011-5-19上午10:17:20
	 * @param invs  一个标段内的品种
	 * @param invVendorInofor  品种的供货信息
	 * @return
	 */
	private Set<String> chooseVendor(List<String> invs,Map<String,Set<String>> invVendorInofor){
		Set<String> sVendor = new HashSet<String>();		
		Set<String> vendors = null;
		Iterator<String> it = null;
		String vendor = null;
		int index = 0;
		for(String inv:invs){
			vendors = invVendorInofor.get(inv);	
			if(index == 0){
				sVendor.addAll(vendors);
			}
			it = sVendor.iterator();
			while(it.hasNext()){
				vendor = it.next();
				if(!vendors.contains(vendor)){
					sVendor.remove(vendor);
				}
				if(sVendor.size() == 0)
					return sVendor;
				index ++;
			}
		}
		return sVendor;
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）确定标段    预划分一标段的品种过滤掉已进入其他标段的品种
	 * 2011-5-19上午10:19:02
	 * @param tmpInvSet 已进入其他标段的品种
	 * @param invs 预划分一标段的品种
	 */
	private void dealInv(Set<String> tmpInvSet,List<String> invs){
		Iterator<String> it = tmpInvSet.iterator();
		String tmp = null;
		while(it.hasNext()){
			tmp = it.next();
			if(invs.contains(tmp)){
				invs.remove(tmp);
			}
		}
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）将品种的历史供应信息进行封装
	 * 2011-5-19上午10:44:34
	 * @param prices
	 * @param invVendorInfor
	 * @param vendorInvInfor
	 */
	private void initInfor(HistoryPriceVO[] prices,Map invVendorInfor,Set<List<String>> vendorInvInfor){
		Set<String> lvendor = null;
		List<String> linv = null;
		String inv = null;
		String vendor = null;
		Map<String,List<String>> vendorInvMap = new HashMap<String, List<String>>();
		for(HistoryPriceVO price:prices){
			inv = price.getCbaseid();
			vendor = price.getCvendorbaseid();
//			封装数据
			if(invVendorInfor.containsKey(inv))
				lvendor = (Set)invVendorInfor.get(inv);
			else
				lvendor = new HashSet<String>();
			lvendor.add(vendor);
			invVendorInfor.put(inv, lvendor);
//			封装数据
			if(vendorInvMap.containsKey(vendor))
				linv = vendorInvMap.get(vendor);
			else{
				linv = new ArrayList<String>();
				linv.add(vendor);//特殊意义  第一记录供应商id  及   把key放在value的第一个位置 后续 和key脱离后使用
			}
				
			linv.add(inv);
			vendorInvMap.put(vendor, linv);				
		}
		
		Iterator it = vendorInvMap.values().iterator();
		while(it.hasNext()){
			vendorInvInfor.add((List<String>)it.next());
		}		
	}
	
	
	
	class SizeComparator implements Comparator{

		public int compare(Object o1, Object o2) {
			// TODO Auto-generated method stub
			Collection c1 = (Collection)o1;
			Collection c2 = (Collection)o2;
			if(c1==null&&c2==null)
				return 0;
			if(c1==null)
				return -1;
			if(c2==null)
				return 1;
			return c1.size()-c2.size();
		}
		
	}

}
