package nc.ui.dm.sb;

import nc.ui.pub.ButtonObject;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * �����˵�
 * @author Administrator
 * 
 */
public class ClientUI extends BillManageUI {

	private static final long serialVersionUID = -3998675844592858916L;

	public ClientUI() {
		super();
	}

	public ClientUI(Boolean useBillSource) {
		super(useBillSource);
	}

	@Override
	protected AbstractManageController createController() {
		// TODO Auto-generated method stub
		
		return new ClientController();
	}
	
	protected ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this, getUIControl());
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

	@Override
	public void setDefaultData() throws Exception {
		// TODO Auto-generated method stub

	}

}