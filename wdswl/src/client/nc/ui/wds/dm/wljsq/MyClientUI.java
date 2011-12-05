package nc.ui.wds.dm.wljsq;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;

public class MyClientUI extends BillManageUI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected ManageEventHandler createEventHandler() {
		return new MyEventHandler(this, getUIControl());
	}

	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
	}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	}

	@Override
	protected void initSelfData() {
		//
		ButtonObject btn = getButtonManager().getButton(IBillButton.Line);
		if (btn != null) {
			btn.removeChildButton(getButtonManager().getButton(
					IBillButton.CopyLine));
			btn.removeChildButton(getButtonManager().getButton(
					IBillButton.PasteLine));
			btn.removeChildButton(getButtonManager().getButton(
					IBillButton.InsLine));
		}
	}

	public void setDefaultData() throws Exception {
		getBillCardPanel().getHeadItem("pk_corp").setValue(_getCorp().getPrimaryKey());
	}
	@Override
	public boolean beforeEdit(BillEditEvent e) {

		return super.beforeEdit(e);
	}

	@Override
	public void afterEdit(BillEditEvent e) {
	
	}


	@Override
	protected AbstractManageController createController() {
		// TODO Auto-generated method stub
		return new MyClientUICtrl();
	}


}
