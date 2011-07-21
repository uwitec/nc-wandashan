package nc.bs.zb.bidding.make;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.ui.scm.util.ObjectUtils;
import nc.vo.pub.lang.UFDouble;
import nc.vo.zb.bidding.BiddingBillVO;
import nc.vo.zb.bidding.HistoryPriceVO;

/**
 * 
 * @author zhf
 * @说明：（鹤岗矿业）系统自动划分标段工具类    以存货为标段划分基本依据的算法
 * 2011-5-18下午02:04:32
 */


public class DivideBiddingTool_inv extends AbstractDivideBiddingTool{
	
	
	
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
//		按品种封装历史品均价
		Map<String,UFDouble> priceInfor = colHistoryPrice(prices);
//		重新组合数据
		initInfor(prices, invVendorInfor);		
		
		List ldata = col(invVendorInfor);
		
		if(ldata == null || ldata.size() == 0)
			return null;
		return new Object[]{priceInfor,ldata};
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）划分标段核心算法
	 * 算法描述：将供货历史信息按根据存货封装,具有相同供应商的存货放入一个标段，没有供应商历史信息的品种放入一个标段
	 * 2011-5-19上午10:27:16
	 * @param invVendorInfor 品种的历史供应信息
	 * @param vendorInvInfor 供应商的供应品种历史信息
	 * @return  划分好的标段信息
	 * @throws Exception
	 */
	private List<Object[]> col(Map<String, Set<String>> invVendorInfor)
	throws Exception{
		
		if(invVendorInfor == null || invVendorInfor.size() == 0)
			return null;
        List ret = new ArrayList();
		
//		划分到同一标段的供应商集合   用set保证供应商不出现重复
		Set<String> sVendor = null;
//		已经进入标段的品种          临时标志那些品种已经划分完成
		Set<String> tmpInvSet = new HashSet<String>();
//		划分到同一标段的品种集合
		List<String> tmpInvs = null;
		
		Set<String> sVendorTmp = null;
		
//		Iterator<Set<String>> it = null;
		Map<String, Set<String>> invVendorInfor_copy = (Map<String, Set<String>>)ObjectUtils.serializableClone(invVendorInfor);
		
		Object[] os = null;//标段数据结构   os[0]---标段入围品种   os[1]----标段入围供应商
		
		for(String key:invVendorInfor.keySet()){
			if(tmpInvSet.contains(key))
				continue;
			sVendor = invVendorInfor.get(key);//进入一个 标段的供应商
			tmpInvs = new ArrayList<String>();
			tmpInvs.add(key);
			tmpInvSet.add(key);
			
			for(String key2:invVendorInfor_copy.keySet()){
				if(tmpInvs.contains(key2)||tmpInvSet.contains(key2))
					continue;
				sVendorTmp = invVendorInfor_copy.get(key2);
				if(sVendorTmp.size() != sVendor.size())
					continue;
				if(sVendor.containsAll(sVendorTmp)){
//					需要校验key2和key是否一个大类的品种
					if(!BiddingBillVO.isClass(key,key2)){
						continue;
					}
					tmpInvs.add(key2);
					tmpInvSet.add(key2);
//					break;
				}
			}
			
			os = new Object[]{tmpInvs,sVendor};
			ret.add(os);
		}
		
		return ret;
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
	private void initInfor(HistoryPriceVO[] prices,Map invVendorInfor){
		Set<String> lvendor = null;
//		List<String> linv = null;
		String inv = null;
		String vendor = null;
//		Map<String,List<String>> vendorInvMap = new HashMap<String, List<String>>();
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
		}
	}
	
}
