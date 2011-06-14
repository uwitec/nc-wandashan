package nc.ui.wds.tranprice.transmil;

import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;

/**
 * 运输里程表档案
 * @author mlr
 *
 */
public class ClientUI extends BillCardUI{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2992031879642299756L;

	@Override
	protected ICardController createController() {
		
		return new Contorller();
	}

	@Override
	public String getRefBillType() {
		
		return null;
	}

	
	
	@Override
	protected CardEventHandler createEventHandler() {
		
		return new ClientEventHandler(this,getUIControl());
	}

	@Override
	protected void initSelfData() {
		
		
	}

	@Override
	public void setDefaultData() throws Exception {
		
		
	}

	@Override
	public Object getUserObject() {
		
		return new GetCheckClass();
	}
	
	
	

}
