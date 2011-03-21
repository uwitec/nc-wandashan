package nc.ui.wds.w80060604;

import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wds.w8004040204.ssButtun.ISsButtun;

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
         * �������
         * @throws Exception
         */
    	protected void oncz() throws Exception {
    	}
    	
    	/**
    	 * ��ֶ���
    	 * @throws Exception
    	 */
    	protected void oncfdd() throws Exception {
    	}

    	/**
    	 * �ϲ�����
    	 * @throws Exception
    	 */
    	protected void onhbdd() throws Exception {
    	}

    	/**
    	 * �ֳ�ֱ��
    	 * @throws Exception
    	 */
    	protected void onfczl() throws Exception {
    	}
    	/**
    	 * ��������
    	 * @throws Exception
    	 */
    	protected void onzcdd() throws Exception {
    	}

	
		   	
	
}