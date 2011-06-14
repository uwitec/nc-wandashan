package nc.ui.wds.tranprice.cartype;

import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;

/**
 * ³µÐÍµµ°¸
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
		
		return new GetCheckClass();
	}

	@Override
	public void setDefaultData() throws Exception {
		
		
	}

}
