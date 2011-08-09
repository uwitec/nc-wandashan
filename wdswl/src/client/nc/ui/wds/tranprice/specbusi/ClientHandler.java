package nc.ui.wds.tranprice.specbusi;

import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.query.HYQueryDLG;
import nc.ui.wl.pub.BeforeSaveValudate;
import nc.ui.wl.pub.SingleBodyEventHandler;
import nc.vo.wl.pub.WdsWlPubConst;

public class ClientHandler extends SingleBodyEventHandler{

	public ClientHandler(BillCardUI billUI, ICardController control) {
		super(billUI, control);
		
	}

	@Override
	protected void beforeSaveValudate() throws Exception {				
		BeforeSaveValudate.FieldBodyUnique(getBillCardPanelWrapper().getBillCardPanel().getBillTable(),
				getBillCardPanelWrapper().getBillCardPanel().getBillModel(), "speccode", "Ãÿ ‚“µŒÒ±‡¬Î");
	}

	@Override
	protected UIDialog createQueryUI() {
		
		return new HYQueryDLG(getBillUI(), null, getBillUI()._getCorp().getPrimaryKey(), WdsWlPubConst.TRANS_SPECBUSI_NODECODE, getBillUI()._getOperator(), null);
	}

	@Override
	protected void onBoSave() throws Exception {
		super.onBoSave();
		onBoRefresh();
	}
	
	

}
