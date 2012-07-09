package nc.ui.wds2.send;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.zmpub.pub.bill.DefBillManageUI;
import nc.vo.pub.CircularlyAccessibleValueObject;

public class AlloInSendUI extends DefBillManageUI {

	@Override
	public String getBillType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isLinkQueryEnable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected AbstractManageController createController() {
		// TODO Auto-generated method stub
		return null;
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

}
