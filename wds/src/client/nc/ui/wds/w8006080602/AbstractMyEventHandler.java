package nc.ui.wds.w8006080602;

import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wds.w8006080602.cfButtun.ICfButtun;

/**
  *
  *该类是一个抽象类，主要目的是生成按钮事件处理的框架
  *@author author
  *@version tempProject version
  */
  
  public class AbstractMyEventHandler 
                                          extends ManageEventHandler{

        public AbstractMyEventHandler(BillManageUI billUI, IControllerBase control){
		super(billUI,control);		
	}
	
	protected void onBoElse(int intBtn) throws Exception {
		     		switch(intBtn){
		
			    case  ICfButtun.CfStop :
		      onCfStop(); 
		      break;
			    case  ICfButtun.CfOpen :
		      onCfOpen(); 
		      break;
				
		}
			}
	
		  protected  void onCfStop()throws Exception {} 
		  protected  void onCfOpen()throws Exception {} 
		   	
	
}