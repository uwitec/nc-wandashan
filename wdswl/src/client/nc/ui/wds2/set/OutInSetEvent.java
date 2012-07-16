package nc.ui.wds2.set;

import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.zmpub.pub.bill.DBTManageEventHandler;

public class OutInSetEvent extends DBTManageEventHandler {

	public OutInSetEvent(BillCardUI billUI, ICardController control) {
		super(billUI, control);
	}

}
