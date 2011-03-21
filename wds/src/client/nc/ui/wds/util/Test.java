package nc.ui.wds.util;

import nc.ui.pub.ButtonObject;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.wds.w8006080806.AbstractMyEventHandler;
import nc.vo.pub.SuperVO;

public class Test extends AbstractMyEventHandler{

	public Test(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
		
		
		super.onBoSave();
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
