package nc.ui.wds.tranprice.specbusiprice;

import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;

/**
 * ����ҵ���˼۱�
 * @author mlr
 *
 */

public class ClientUI extends BillCardUI{

	private static final long serialVersionUID = -5066517874552843714L;

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
	public Object getUserObject() {
		
		return new GetCheckClass();
	}
	/**
	 * @author yf
	 * Ĭ�� �۸�λpriceunit = 0
	 */
	@Override
	public void setDefaultData() throws Exception {

	}
}
