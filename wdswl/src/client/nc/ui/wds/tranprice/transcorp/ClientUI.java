package nc.ui.wds.tranprice.transcorp;
import nc.ui.pub.ButtonObject;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wl.pub.MutilChildUI;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 *  运输公司档案
 * @author mlr
 * 
 */
public class ClientUI extends MutilChildUI {

	@Override
	public Object getUserObject() {
		
		return new GetCheckClass();
	}

	private static final long serialVersionUID = -3998675844592858916L;
	

	
	public ClientUI() {
		super();
	}

	public ClientUI(Boolean useBillSource) {
		super(useBillSource);
	}

	public ClientUI(String pk_corp, String pk_billType, String pk_busitype,
			String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
	}
	@Override
	protected AbstractManageController createController() {
		return new ClientController();
	}
	@Override
	protected BusinessDelegator createBusinessDelegator() {
		
		return new ClientBusinessDelegator();
	}

	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
	}

	@Override
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	}

	@Override
	protected void initSelfData() {
		//
		ButtonObject btn = getButtonManager().getButton(IBillButton.Line);
		if (btn != null) {
			btn.removeChildButton(getButtonManager().getButton(
					IBillButton.CopyLine));
			btn.removeChildButton(getButtonManager().getButton(
					IBillButton.PasteLine));
			btn.removeChildButton(getButtonManager().getButton(
					IBillButton.InsLine));
		}
	}

	@Override
	public void setDefaultData() throws Exception {
	
		getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPk_corp());
		
	}
	
	
	
	protected ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this, getUIControl());
	}



	@Override
	public boolean isSaveAndCommitTogether() {
		
		return false;
	}

	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
		
		
	}
	

}