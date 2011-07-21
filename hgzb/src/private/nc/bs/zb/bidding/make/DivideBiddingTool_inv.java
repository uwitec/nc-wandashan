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
 * @˵�������׸ڿ�ҵ��ϵͳ�Զ����ֱ�ι�����    �Դ��Ϊ��λ��ֻ������ݵ��㷨
 * 2011-5-18����02:04:32
 */


public class DivideBiddingTool_inv extends AbstractDivideBiddingTool{
	
	
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ��ϵͳ���ݹ�Ӧ����ʷ������Ϣ���ֱ��
	 * 2011-5-19����10:21:53
	 * @param prices  ��Ӧ����ʷ��Ӧ��Ϣ  ȡ�Բɹ���ͬ
	 * @return  ���ֺõı��   
	 * Object[]  0ΪƷ����ʷƷ����MAP  1 �����ϢObject[]  [0]---�����ΧƷ�� [1]----�����Χ��Ӧ��
	 * @throws Exception
	 */
	public Object divid(HistoryPriceVO[] prices) throws Exception{
		if(prices == null || prices.length == 0)
			return null;
		//Ʒ�ֹ�Ӧ��ӳ��
		Map<String, Set<String>> invVendorInfor = new HashMap<String, Set<String>>();
//		��Ʒ�ַ�װ��ʷƷ����
		Map<String,UFDouble> priceInfor = colHistoryPrice(prices);
//		�����������
		initInfor(prices, invVendorInfor);		
		
		List ldata = col(invVendorInfor);
		
		if(ldata == null || ldata.size() == 0)
			return null;
		return new Object[]{priceInfor,ldata};
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ�����ֱ�κ����㷨
	 * �㷨��������������ʷ��Ϣ�����ݴ����װ,������ͬ��Ӧ�̵Ĵ������һ����Σ�û�й�Ӧ����ʷ��Ϣ��Ʒ�ַ���һ�����
	 * 2011-5-19����10:27:16
	 * @param invVendorInfor Ʒ�ֵ���ʷ��Ӧ��Ϣ
	 * @param vendorInvInfor ��Ӧ�̵Ĺ�ӦƷ����ʷ��Ϣ
	 * @return  ���ֺõı����Ϣ
	 * @throws Exception
	 */
	private List<Object[]> col(Map<String, Set<String>> invVendorInfor)
	throws Exception{
		
		if(invVendorInfor == null || invVendorInfor.size() == 0)
			return null;
        List ret = new ArrayList();
		
//		���ֵ�ͬһ��εĹ�Ӧ�̼���   ��set��֤��Ӧ�̲������ظ�
		Set<String> sVendor = null;
//		�Ѿ������ε�Ʒ��          ��ʱ��־��ЩƷ���Ѿ��������
		Set<String> tmpInvSet = new HashSet<String>();
//		���ֵ�ͬһ��ε�Ʒ�ּ���
		List<String> tmpInvs = null;
		
		Set<String> sVendorTmp = null;
		
//		Iterator<Set<String>> it = null;
		Map<String, Set<String>> invVendorInfor_copy = (Map<String, Set<String>>)ObjectUtils.serializableClone(invVendorInfor);
		
		Object[] os = null;//������ݽṹ   os[0]---�����ΧƷ��   os[1]----�����Χ��Ӧ��
		
		for(String key:invVendorInfor.keySet()){
			if(tmpInvSet.contains(key))
				continue;
			sVendor = invVendorInfor.get(key);//����һ�� ��εĹ�Ӧ��
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
//					��ҪУ��key2��key�Ƿ�һ�������Ʒ��
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
	 * @˵�������׸ڿ�ҵ����Ʒ�ֵ���ʷ��Ӧ��Ϣ���з�װ
	 * 2011-5-19����10:44:34
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
//			��װ����
			if(invVendorInfor.containsKey(inv))
				lvendor = (Set)invVendorInfor.get(inv);
			else
				lvendor = new HashSet<String>();
			lvendor.add(vendor);
			invVendorInfor.put(inv, lvendor);
		}
	}
	
}
