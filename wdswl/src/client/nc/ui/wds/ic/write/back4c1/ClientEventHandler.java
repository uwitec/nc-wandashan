package nc.ui.wds.ic.write.back4c1;

import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.wl.pub.WdsPubEnventHandler;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.wl.pub.WdsWlPubConst;

public class ClientEventHandler extends WdsPubEnventHandler {

	public ClientUIQueryDlg queryDialog = null;

	public ClientEventHandler(ClientUI clientUI, IControllerBase control) {
		super(clientUI, control);
	}

	protected UIDialog createQueryUI() {
		if (queryDialog == null) {
			queryDialog = new ClientUIQueryDlg(getBillUI(), null, _getCorp()
					.getPrimaryKey(), getBillUI()._getModuleCode(),
					_getOperator(), getBillUI().getBusinessType(), getBillUI()
							.getNodeKey());
		}
		return queryDialog;
	}

	

	@Override
	protected String getHeadCondition() {
		return " pk_corp = '"+_getCorp().getPrimaryKey()+"' and isnull(dr,0) = 0 and pk_billtype = '"+WdsWlPubConst.WDSX+"' ";
	}

	@Override
	protected void onBoSave() throws Exception {
		super.onBoSave();
	}
}
