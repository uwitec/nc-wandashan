package nc.ui.wds.ie.storedetail;


import nc.ui.trade.bill.IListController;
import nc.ui.trade.list.BillListUI;
import nc.ui.trade.list.ListEventHandler;

/**
  *
  *该类是一个抽象类，主要目的是生成按钮事件处理的框架
  *@author author
  *@version tempProject version
  */
  
  public class AbstractMyEventHandler 
                                          extends ListEventHandler{

 public AbstractMyEventHandler(BillListUI billUI, IListController control){
		super(billUI,control);		
	}
	
	protected void onBoElse(int intBtn) throws Exception {
		     	}
	
		   	
	
}