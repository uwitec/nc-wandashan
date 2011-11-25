package nc.ui.wds.tranprice.freight;

import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.wl.pub.WdsPubEnventHandler;

public class MyEventHandler extends WdsPubEnventHandler{
	public ClientUIQueryDlg queryDialog = null;

	@Override
	protected void onBoSave() throws Exception {
		super.onBoSave();
	}

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}
	protected UIDialog createQueryUI() {
		if (queryDialog == null) {
			queryDialog = new ClientUIQueryDlg(getBillUI(), null, _getCorp()
					.getPrimaryKey(), getBillUI()._getModuleCode(),
					_getOperator(), getBillUI().getBusinessType());
		}
		return queryDialog;
	}
	

}
