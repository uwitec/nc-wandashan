package nc.ui.wds.ic.write.back4y;

import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.wl.pub.WdsPubEnventHandler;

public class ClientEventHandler extends WdsPubEnventHandler {
	public ClientUIQueryDlg queryDialog = null;
	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}
	protected UIDialog createQueryUI() {
		if (queryDialog == null) {
			queryDialog=new ClientUIQueryDlg(	getBillUI(),
					null,
					_getCorp().getPrimaryKey(),
					getBillUI()._getModuleCode(),
					_getOperator(),
					getBillUI().getBusinessType(),
					getBillUI().getNodeKey());
		}
		return queryDialog;
	}

	@Override
	protected void onBoSave() throws Exception {
		super.onBoSave();
	}
	
	


}
