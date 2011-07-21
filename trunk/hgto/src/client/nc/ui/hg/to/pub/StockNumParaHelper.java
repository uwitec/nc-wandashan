package nc.ui.hg.to.pub;

import nc.ui.hg.pu.pub.LongTimeTask;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.bill.BillCardPanel;
import nc.vo.pub.lang.UFDate;
import nc.vo.to.pub.BillVO;

public class StockNumParaHelper {
	
	private static String bo = "nc.bs.hg.to.pub.StockNumParaBO";
	
	public static BillVO getStockNumParaVOsOnRefresh(BillVO billvo,ToftPanel ui) throws Exception{
		if(billvo == null)
			return null;		
		Class[] ParameterTypes = new Class[]{BillVO.class,UFDate.class};
		Object[] ParameterValues = new Object[]{billvo,ClientEnvironment.getInstance().getDate()};
		Object o = LongTimeTask.calllongTimeService("to",ui,"正在计算...",1,bo,null, "getStockNumParaOnRefresh", ParameterTypes, ParameterValues);
		return (BillVO)o;	
	}
	
	public static BillVO[]  getStockNumParaVOs(BillVO[] billvos) throws Exception{
		if(billvos == null || billvos.length == 0)
			return null;	
		Class[] ParameterTypes = new Class[]{BillVO[].class};
		Object[] ParameterValues = new Object[]{billvos};
		Object o = LongTimeTask.callRemoteService("to",bo, "getStockNumPara2", ParameterTypes, ParameterValues, 2);
		return (BillVO[])o;	
	
	}
	
	  /**
	   * 
	   * @author zhf
	   * @说明：（鹤岗矿业）zhf add
	   * 2011-4-25下午04:24:43
	   */
	  public static  void setFundValue(BillCardPanel card,BillVO bill){
//		  BillVO bill = (BillVO)getModel().getCurVO();
		  card.getHeadItem("nfund").setValue(bill.getNfund());
		  card.getHeadItem("nallfund").setValue(bill.getNallfund());
		  card.getHeadItem("nmny").setValue(bill.getNmny());
		  card.getHeadItem("nallmny").setValue(bill.getNallmny());
	  }

}
