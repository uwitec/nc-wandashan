package nc.ui.wds.w80060406;

import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wds.w800604.tcButtun.ITcButtun;

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
		
			    case  ITcButtun.cxmx :
		      oncxmx(); 
		      break;
				
		}
			}
	
		  protected  void oncxmx() throws Exception{} 
		   	
	
}