package nc.ui.wds.tranprice.cartype;

import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.query.HYQueryDLG;
import nc.ui.wl.pub.SingleBodyEventHandler;
import nc.vo.wl.pub.WdsWlPubConst;

public class ClientHandler extends SingleBodyEventHandler{

	public ClientHandler(BillCardUI billUI, ICardController control) {
		super(billUI, control);
		
	}

	//必输项校验后的 保存前的校验
	@Override
	protected void beforeSaveValudate() throws Exception {				
		
	}

	@Override
	protected UIDialog createQueryUI() {
		return new HYQueryDLG(getBillUI(), null, getBillUI()._getCorp().getPrimaryKey(), WdsWlPubConst.TRANS_CARTYPE_NODECODE, getBillUI()._getOperator(), null);
	}
	
	

}
