package nc.ui.wds.w8006080202;

import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wds.w8006080202.tcButtun.ITcButtun;

/**
  *
  *������һ�������࣬��ҪĿ�������ɰ�ť�¼�����Ŀ��
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
		
			    case  ITcButtun.Con :
		      onCon(); 
		      break;
			    case  ITcButtun.Pp :
		      onPp(); 
		      break;
				
		}
			}
	
		  protected  void onCon() throws Exception{} 
		  protected  void onPp() throws Exception{} 
		   	
	
}