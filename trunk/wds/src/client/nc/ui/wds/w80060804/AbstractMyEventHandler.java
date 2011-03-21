package nc.ui.wds.w80060804;

import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wds.w80060804.tcButtun.ITcButtun;

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
		
			    case  ITcButtun.TcOpen :
		      onTcOpen(); 
		      break;
			    case  ITcButtun.TcStop :
		      onTcStop(); 
		      break;
				
		}
			}
	
		  protected  void onTcOpen() throws Exception{} 
		  protected  void onTcStop() throws Exception{} 
		   	
	
}