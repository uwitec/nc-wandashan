package nc.ui.wds.tranprice.specbusi;

import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;

/**
 * 特殊业务档案
 * @author mlr
 *
 */

public class ClientUI extends BillCardUI{

	@Override
	protected ICardController createController() {
		
		return new ClientController();
	}

	@Override
	public String getRefBillType() {
		
		return null;
	}

	
	
	@Override
	protected CardEventHandler createEventHandler() {
		
		return new ClientHandler(this,getUIControl());
	}

	@Override
	protected void initSelfData() {
		
		
	}
     
	

	@Override
	protected void initPrivateButton() {	
		super.initPrivateButton();
	}

	@Override
	public Object getUserObject() {
		
		return new ClientUICheckRuleGetter();
	}

	@Override
	public void setDefaultData() throws Exception {
		getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPk_corp());
		
	}

}
