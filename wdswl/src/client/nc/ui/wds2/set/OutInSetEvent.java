package nc.ui.wds2.set;

import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.zmpub.pub.bill.DBTManageEventHandler;

public class OutInSetEvent extends DBTManageEventHandler {

	public OutInSetEvent(BillCardUI billUI, ICardController control) {
		super(billUI, control);
	}
	
	protected void onBoLineAdd() throws Exception {
		super.onBoLineAdd();
		int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
		if(row < 0)
			return;
		getBillCardPanelWrapper().getBillCardPanel().getBillModel().setValueAt(_getCorp().getPrimaryKey(), row, "pk_corp");
	}

}
