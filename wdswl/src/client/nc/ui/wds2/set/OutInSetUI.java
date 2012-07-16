package nc.ui.wds2.set;

import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;

public class OutInSetUI extends BillCardUI {

	@Override
	protected ICardController createController() {
		// TODO Auto-generated method stub
		return new OutInSetCtrl();
	}
	
	protected CardEventHandler createEventHandler() {
		return new OutInSetEvent(this,this.getUIControl());
	}

	@Override
	public String getRefBillType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void initSelfData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDefaultData() throws Exception {
		// TODO Auto-generated method stub

	}

}
