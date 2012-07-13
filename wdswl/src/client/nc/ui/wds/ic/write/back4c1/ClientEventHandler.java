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
	protected void onBoQuery() throws Exception {
		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// 用户放弃了查询
		AggregatedValueObject[] bodyVOs = HYPubBO_Client
				.queryBillVOByCondition(getUIController().getBillVoName(),
						strWhere.toString());
		int count = 0;
		getBufferData().clear();
		if (bodyVOs != null) {
			count = bodyVOs.length;
			getBufferData().addVOsToBuffer(bodyVOs);
			updateBuffer();
		}
		getBillUI().showHintMessage("查询完成：共查询到" + count + "条数据");
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
