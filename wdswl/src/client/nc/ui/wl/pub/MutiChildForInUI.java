package nc.ui.wl.pub;

import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.trade.bill.AbstractManageController;
import nc.vo.trade.button.ButtonVO;
import nc.vo.wl.pub.Button.CommonButtonDef;

public class MutiChildForInUI extends MutilChildUI {

	@Override
	protected AbstractManageController createController() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	protected void initPrivateButton() {
		super.initPrivateButton();
		CommonButtonDef def = new CommonButtonDef();
		ButtonVO  joinup = def.getJoinUPButton();
		addPrivateButton(joinup);
	}
	public MutiChildForInUI(String pk_corp, String pk_billType,String pk_busitype, String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
	}
	public MutiChildForInUI() {
		super();
		
	}
}


