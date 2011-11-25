package nc.ui.wds.tranprice.freight;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;

public class MyClientUI extends BillManageUI{

	/**
	 * 折合标准
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected AbstractManageController createController() {
		return new MyControl();
	}
	
	protected ManageEventHandler createEventHandler(){
		return new MyEventHandler(this,getUIControl());
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDefaultData() throws Exception {
		getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPk_corp());
		
	}
	//校验
	public Object getUserObject() {
		return new GetCheck();
	}

}
