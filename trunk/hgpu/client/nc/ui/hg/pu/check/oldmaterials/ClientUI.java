package nc.ui.hg.pu.check.oldmaterials;

import javax.swing.ListSelectionModel;

import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.hg.pu.check.oldmaterials.CritiCHK;



/**
 *新旧物资比例设置
 * @author zhw
 *
 */
public class ClientUI extends BillCardUI {

	private static final long serialVersionUID = 859146761348083688L;

	public ClientUI() {
		super();
	}

	public ClientUI(String pk_corp, String pk_billType, String pk_busitype, String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
	}
	/**
	 * 
	 */
	@Override
	protected ICardController createController() {
		return new ClientUICtrl();
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
	    getBillCardPanel().getBillTable().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
	    
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
	}

	public boolean beforeEdit(nc.ui.pub.bill.BillEditEvent e) {
		return super.beforeEdit(e);
	}
}
