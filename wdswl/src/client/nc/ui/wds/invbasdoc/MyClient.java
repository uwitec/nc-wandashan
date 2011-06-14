package nc.ui.wds.invbasdoc;

import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;
//存货常用档案
public class MyClient extends BillManageUI {

	private static final long serialVersionUID = 8036305571063747481L;

	@Override
	protected AbstractManageController createController() {
		
		return new MyClientController();
	}
	
	
	

	@Override
	protected ManageEventHandler createEventHandler() {
		
		return new MyEventHandler(this, getUIControl());
	}




	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {	
		
	}

	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
		
	}

	@Override
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
		
		
	}

	@Override
	protected void initSelfData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDefaultData() throws Exception {
		// TODO Auto-generated method stub
	
		
		
	}
	@Override
	public Object getUserObject() {
		
		return new GetCheckClass();
	}




	@Override
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		super.afterEdit(e);
		getBillCardPanel().execHeadEditFormulas();
	}
	

}
