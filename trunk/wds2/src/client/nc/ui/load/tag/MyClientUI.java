package nc.ui.load.tag;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.pub.CircularlyAccessibleValueObject;

public class MyClientUI extends BillManageUI {
	
	
	@Override
	protected AbstractManageController createController() {
		// TODO Auto-generated method stub
		
		return new MyClientController();
	}

	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initSelfData() {
		//++++++++++

	}

	@Override
	public void setDefaultData() throws Exception {
		// TODO Auto-generated method stub

	}

}
