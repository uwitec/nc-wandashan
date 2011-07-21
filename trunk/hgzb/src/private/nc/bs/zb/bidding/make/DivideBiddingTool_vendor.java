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
 * @˵�������׸ڿ�ҵ��ϵͳ�Զ����ֱ�ι�����    �Թ�Ӧ��Ϊ��λ��ֻ������ݵ��㷨
 * 2011-5-18����02:04:32
 */


public class DivideBiddingTool_vendor extends AbstractDivideBiddingTool{
	
	
	
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
		//��Ӧ��Ʒ��ӳ�� ��Ҫ����   ��ÿ����Ӧ�̿ɹ�Ӧ��Ʒ������ ��������  treemap ��key����
//		Map<String,Set<String>> vendorInvInfor = new TreeMap<String, Set<String>>();
		//�������ݽṹ  ÿһ���ڲ����ϵ�һ��ֵΪ��Ӧ�� ����ֵΪƷ��id   �ⲿ���ϰ��ڲ����ϴ�С��������
		Set<List<String>> vendorInvInfor = new TreeSet<List<String>>(new SizeComparator());
//		��Ʒ�ַ�װ��ʷƷ����
		Map<String,UFDouble> priceInfor = colHistoryPrice(prices);
//		�����������
		initInfor(prices, invVendorInfor, vendorInvInfor);		
		
		List ldata = col(invVendorInfor, vendorInvInfor);
		
		if(ldata == null || ldata.size() == 0)
			return null;
		return new Object[]{priceInfor,ldata};
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ�����ֱ�κ����㷨
	 * �㷨����������Ӧ�̵Ĺ�ӦƷ����Ŀ���ٵ��� ����  ��ΪԤ���ֱ�Σ�ɸѡ�Ѿ�����������ε�Ʒ�֣�����Ʒ�ֵ���ʷ��Ӧ��Ϣѡ���乩Ӧ��
	 *          �Ӷ����ֳ����
	 * 2011-5-19����10:27:16
	 * @param invVendorInfor Ʒ�ֵ���ʷ��Ӧ��Ϣ
	 * @param vendorInvInfor ��Ӧ�̵Ĺ�ӦƷ����ʷ��Ϣ
	 * @return  ���ֺõı����Ϣ
	 * @throws Exception
	 */
	private List<Object[]> col(Map<String, Set<String>> invVendorInfor,Set<List<String>> vendorInvInfor)
	throws Exception{
		
		if(invVendorInfor == null || invVendorInfor.size() == 0|| vendorInvInfor == null || vendorInvInfor.size() == 0)
			return null;
		
		List ret = new ArrayList();
		
//		���ֵ�ͬһ��εĹ�Ӧ�̼���   ��set��֤��Ӧ�̲������ظ�
		Set<String> sVendor = null;
//		�Ѿ������ε�Ʒ��          ��ʱ��־��ЩƷ���Ѿ��������
		Set<String> tmpInvSet = new HashSet<String>();
		
		String tmpVendor = null;//����Ӧ�̼���ʱ������ǰ��Ӧ��
		Object[] os = null;//������ݽṹ   os[0]---�����ΧƷ��   os[1]----�����Χ��Ӧ��
		
		Iterator it =vendorInvInfor.iterator();
		List<String> tmpInvs = null;
		while(it.hasNext()){//����Ӧ�̹�ӦƷ����Ŀ��С�������    ���ݽṹ������
			tmpInvs = (List<String>)it.next();
			tmpVendor = tmpInvs.get(0);//��һ��ֵ�ŵĹ�Ӧ��
			//ȥ����Ӧ��
			tmpInvs.remove(0);
//			ȥ���Ѿ�����������ε�Ʒ��
			dealInv(tmpInvSet,tmpInvs);
			if(tmpInvs.size()==0){
				continue;
			}
//			ʣ�µ�Ʒ����Ϊ���ѡ���乩Ӧ��
			sVendor = chooseVendor(tmpInvs,invVendorInfor);
			if(sVendor == null)
				sVendor = new HashSet<String>();
			sVendor.add(tmpVendor);//�ù�Ӧ������Χ�ñ��
			tmpInvSet.addAll(tmpInvs);//��Ʒ���ѱ�����
			os = new Object[]{tmpInvs,sVendor};
			ret.add(os);
		}
		
		return ret;
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ��ɸѡ����Ϊ���������Ʒ�ֹ����Ĺ�Ӧ��
	 * 2011-5-19����10:17:20
	 * @param invs  һ������ڵ�Ʒ��
	 * @param invVendorInofor  Ʒ�ֵĹ�����Ϣ
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
	 * @˵�������׸ڿ�ҵ��ȷ�����    Ԥ����һ��ε�Ʒ�ֹ��˵��ѽ���������ε�Ʒ��
	 * 2011-5-19����10:19:02
	 * @param tmpInvSet �ѽ���������ε�Ʒ��
	 * @param invs Ԥ����һ��ε�Ʒ��
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
	 * @˵�������׸ڿ�ҵ����Ʒ�ֵ���ʷ��Ӧ��Ϣ���з�װ
	 * 2011-5-19����10:44:34
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
//			��װ����
			if(invVendorInfor.containsKey(inv))
				lvendor = (Set)invVendorInfor.get(inv);
			else
				lvendor = new HashSet<String>();
			lvendor.add(vendor);
			invVendorInfor.put(inv, lvendor);
//			��װ����
			if(vendorInvMap.containsKey(vendor))
				linv = vendorInvMap.get(vendor);
			else{
				linv = new ArrayList<String>();
				linv.add(vendor);//��������  ��һ��¼��Ӧ��id  ��   ��key����value�ĵ�һ��λ�� ���� ��key�����ʹ��
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
