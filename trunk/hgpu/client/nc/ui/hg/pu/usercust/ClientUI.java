package nc.ui.hg.pu.usercust;

import javax.swing.ListSelectionModel;

import nc.ui.pub.bill.BillItem;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.hg.pu.usercust.CritiCHK;

/**
 * 操作员与供应商之间关系
 * 
 * @author zhw
 * 
 */
public class ClientUI extends BillCardUI {

	private static final long serialVersionUID = 859146761348083688L;

	public ClientUI() {
		super();
		
	}

	public ClientUI(String pk_corp, String pk_billType, String pk_busitype,
			String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
	}

	@Override
	protected ICardController createController() {
		return new ClientUICtrl(_getCorp().getPrimaryKey());
	}

	protected CardEventHandler createEventHandler() {
		return new ClientEventHandler(this, getUIControl());
	}

	@Override
	public String getRefBillType() {
		return null;
	}

	@Override
	protected void initSelfData() {
		//
		getBillCardPanel().getBillTable().setRowSelectionAllowed(true);
		getBillCardPanel().getBillTable().setSelectionMode(
				ListSelectionModel.SINGLE_INTERVAL_SELECTION);
	
	}

	

	@Override
	public void setDefaultData() throws Exception {

	}

	@Override
	public Object getUserObject() {
		return new CritiCHK();
	}

	public void afterEdit(nc.ui.pub.bill.BillEditEvent e) {
		super.afterEdit(e);
		String key = e.getKey();
		int row = e.getRow();
		if (e.getPos() == BillItem.BODY) {
			
		}
	}

	public boolean beforeEdit(nc.ui.pub.bill.BillEditEvent e) {
		return super.beforeEdit(e);
	}
}
