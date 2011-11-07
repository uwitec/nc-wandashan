package nc.ui.wl.pub;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.trade.button.IBillButton;
import nc.vo.trade.button.ButtonVO;
import nc.vo.wl.pub.Button.CommonButtonDef;

//import nc.bs.logging.Logger;
//import nc.ui.pub.ButtonObject;
//import nc.ui.pub.linkoperate.ILinkQueryData;
//import nc.ui.trade.bill.AbstractManageController;
//import nc.ui.trade.bill.BillTemplateWrapper;
//import nc.ui.trade.button.IBillButton;
//import nc.vo.pub.AggregatedValueObject;
//import nc.vo.pub.SuperVO;
//import nc.vo.trade.button.ButtonVO;
//import nc.vo.wl.pub.Button.CommonButtonDef;

public abstract class MutiChildForOutInUI extends MutilChildUI{

	private LoginInforHelper helper = null;
	
	public LoginInforHelper getLoginInforHelper(){
		if(helper == null){
			helper = new LoginInforHelper();
		}
		return helper;
	}
	public MutiChildForOutInUI() {
		super();
	}
	
	public MutiChildForOutInUI(Boolean useBillSource) {
		super(useBillSource);
	}

	public MutiChildForOutInUI(String pk_corp, String pk_billType, String pk_busitype,
			String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
	}
	@Override
	public void doQueryAction(ILinkQueryData querydata) {
		// TODO Auto-generated method stub
		super.doQueryAction(querydata);
		ButtonObject[] btns = getButtons();
		for (ButtonObject btn : btns) {
			if (("" + (IBillButton.Print)).equals(btn.getTag())) {
				btn.setEnabled(true);
				btn.setVisible(true);
			}else{
				btn.setEnabled(false);
				btn.setVisible(false);
			}
		}
	}
	@Override
	protected void initPrivateButton() {
		super.initPrivateButton();
		CommonButtonDef def = new CommonButtonDef();
		ButtonVO  joinup = def.getJoinUPButton();
		addPrivateButton(joinup);
	}
}
