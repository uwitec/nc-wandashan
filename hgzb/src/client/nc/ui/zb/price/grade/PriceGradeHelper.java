package nc.ui.zb.price.grade;

import nc.ui.pub.ToftPanel;
import nc.ui.zb.pub.LongTimeTask;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.zb.bidding.BiddingHeaderVO;
import nc.vo.zb.bill.deal.DealVendorBillVO;

public class PriceGradeHelper {

	private static String bo = "nc.bs.zb.price.grade.PriceGradeBO";
	
	public static BiddingHeaderVO[] initData(String pk_corp) throws Exception{
		Class[] ParameterTypes = new Class[]{String.class};
		Object[] ParameterValues = new Object[]{pk_corp};
		return (BiddingHeaderVO[])LongTimeTask.callRemoteService("pu",bo, "queryAllBiddingByCorp", ParameterTypes, ParameterValues, 2);
	}
	
    public static Object initBodyData(ToftPanel tp,String cbiddingid) throws Exception{//��ʼʱֻ���ر�������  ѡ�нڵ��  ����  ���������Ϣ��Ʒ����Ϣ  ��������Ϣ  ������Ϣ
    	Class[] ParameterTypes = new Class[]{String.class};
		Object[] ParameterValues = new Object[]{cbiddingid};
		Object o = LongTimeTask.
		calllongTimeService("pu", tp, "���ڻ��б���Ϣ...", 1, bo, null, "getBiddingInfor", ParameterTypes, ParameterValues);
		return o;
	}
    
    public static void doOK(ToftPanel tp,DealVendorBillVO bills,ClientLink cl) throws Exception{
    	if(bills == null)
    		return;
    	Class[] ParameterTypes = new Class[]{DealVendorBillVO.class,ClientLink.class};
		Object[] ParameterValues = new Object[]{bills,cl};
		LongTimeTask.
		calllongTimeService("pu", tp, "���ڴ���...", 1, bo, null, "doOK", ParameterTypes, ParameterValues);
    }
    
    public static void doCol(ToftPanel tp,DealVendorBillVO[] bills,ClientLink cl) throws Exception{
    	if(bills == null || bills.length == 0)
    		return;
    	Class[] ParameterTypes = new Class[]{DealVendorBillVO[].class,ClientLink.class};
		Object[] ParameterValues = new Object[]{bills,cl};
		LongTimeTask.
		calllongTimeService("pu", tp, "���ڴ���...", 1, bo, null, "doCol", ParameterTypes, ParameterValues);
    }
    
   
}
