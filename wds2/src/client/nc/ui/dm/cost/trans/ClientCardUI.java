package nc.ui.dm.cost.trans;

import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;

public class ClientCardUI extends BillCardUI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1485173766892945137L;

	@Override
	protected ICardController createController() {
		// TODO Auto-generated method stub
		System.out.println("创建界面控制类！");
		return new ClientCardControl();
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
	protected CardEventHandler createEventHandler() {
		// TODO Auto-generated method stub
		System.out.println("创建处理事件类");
		return new ClientCardEventHandler(this, getUIControl());
	}

	@Override
	public void setDefaultData() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void initQueryCondition(UIDialog qryClient) throws Exception {
		// TODO Auto-generated method stub
		super.initQueryCondition(qryClient);
	}

}
