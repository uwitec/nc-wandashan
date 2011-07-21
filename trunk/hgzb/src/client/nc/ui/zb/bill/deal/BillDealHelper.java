package nc.ui.zb.bill.deal;

import java.util.List;

import nc.ui.pub.ToftPanel;
import nc.ui.zb.pub.LongTimeTask;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.zb.bidding.BiddingHeaderVO;
import nc.vo.zb.bill.deal.DealVendorBillVO;

public class BillDealHelper {

	private static String bo = "nc.bs.zb.bill.deal.BillDealBO";
	
	public static BiddingHeaderVO[] initData(String pk_corp) throws Exception{
		Class[] ParameterTypes = new Class[]{String.class};
		Object[] ParameterValues = new Object[]{pk_corp};
		return (BiddingHeaderVO[])LongTimeTask.callRemoteService("pu",bo, "queryAllBiddingByCorp", ParameterTypes, ParameterValues, 2);
	}
	
    public static Object initBodyData(ToftPanel tp,String cbiddingid,UFBoolean isinv) throws Exception{//初始时只加载标书数据  选中节点后  加载  标书相关信息：品种信息  供货商信息  报价信息
    	Class[] ParameterTypes = new Class[]{String.class,UFBoolean.class};
		Object[] ParameterValues = new Object[]{cbiddingid,isinv};
		Object o = LongTimeTask.
		calllongTimeService("pu", tp, "正在获招标信息...", 1, bo, null, "getBiddingInfor", ParameterTypes, ParameterValues);
		return o;
	}
    
    public static void doOK(ToftPanel tp,List<DealVendorBillVO> ldata,ClientLink cl) throws Exception{
    	if(ldata == null || ldata.size() == 0)
    		return;
    	Class[] ParameterTypes = new Class[]{List.class,ClientLink.class};
		Object[] ParameterValues = new Object[]{ldata,cl};
		Object o = LongTimeTask.
		calllongTimeService("pu", tp, "正在处理...", 1, bo, null, "doOK", ParameterTypes, ParameterValues);
//		return o;
    }
}
