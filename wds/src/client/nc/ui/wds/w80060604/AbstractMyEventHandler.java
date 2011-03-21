package nc.ui.wds.w80060604;

import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wds.w8004040204.ssButtun.ISsButtun;

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
    		switch (intBtn) {

    		case ISsButtun.cz:
    			oncz();
    			break;
    		case ISsButtun.cfdd:
    			oncfdd();
    			break;
    		case ISsButtun.hbdd:
    			onhbdd();
    			break;
    		case ISsButtun.fczl:
    			onfczl();
    			break;
    		case ISsButtun.zcdd:
    			onzcdd();
    			break;

    		}
    	}

        /**
         * 操作类别
         * @throws Exception
         */
    	protected void oncz() throws Exception {
    	}
    	
    	/**
    	 * 拆分订单
    	 * @throws Exception
    	 */
    	protected void oncfdd() throws Exception {
    	}

    	/**
    	 * 合并订单
    	 * @throws Exception
    	 */
    	protected void onhbdd() throws Exception {
    	}

    	/**
    	 * 分厂直流
    	 * @throws Exception
    	 */
    	protected void onfczl() throws Exception {
    	}
    	/**
    	 * 正常订单
    	 * @throws Exception
    	 */
    	protected void onzcdd() throws Exception {
    	}

	
		   	
	
}