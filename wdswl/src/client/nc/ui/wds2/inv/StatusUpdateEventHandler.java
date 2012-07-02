package nc.ui.wds2.inv;

import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.wl.pub.WdsPubEnventHandler;
import nc.vo.wl.pub.BillRowNo;
import nc.vo.wl.pub.Wds2WlPubConst;

public class StatusUpdateEventHandler extends WdsPubEnventHandler {

	public StatusUpdateEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onBoLineAdd() throws Exception {
		super.onBoLineAdd();
//		setBodySpace();
		BillRowNo.addLineRowNo(getBillCardPanelWrapper().getBillCardPanel(),
				Wds2WlPubConst.billtype_statusupdate, "crowno");
	}

}
