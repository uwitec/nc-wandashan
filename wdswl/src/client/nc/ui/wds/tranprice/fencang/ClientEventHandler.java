package nc.ui.wds.tranprice.fencang;

import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.wl.pub.BeforeSaveValudate;
import nc.ui.wl.pub.WdsPubEnventHandler;
import nc.vo.wl.pub.WdsWlPubConst;

public class ClientEventHandler extends WdsPubEnventHandler {

	public ClientUIQueryDlg queryDialog = null;

	public ClientEventHandler(ClientUI clientUI, IControllerBase control) {
		super(clientUI, control);
	}

	protected UIDialog createQueryUI() {
		if (queryDialog == null) {
			queryDialog=new ClientUIQueryDlg(getBillUI(),
					null,
					_getCorp().getPrimaryKey(),
					getBillUI()._getModuleCode(),
					_getOperator(),
					getBillUI().getBusinessType(),
					getBillUI().getNodeKey());
			//queryDialog = new ClientUIQueryDlg(getBillUI(), null, tempinfo);
		}
		return queryDialog;
	}
	@Override
	protected String getHeadCondition() {
		String strWhere = super.getHeadCondition();
		if(strWhere == null || "".equals(strWhere)){
			return " and pk_billtype='"+WdsWlPubConst.WDSK+"'";
		}else {
			return strWhere+" and pk_billtype='"+WdsWlPubConst.WDSK+"'";
		}
	}
	@Override
	protected void onBoSave() throws Exception {
		beforeSaveValidate();
		super.onBoSave();
	}
	protected void beforeSaveValidate() throws Exception {
		//表体不为空
		BeforeSaveValudate.BodyNotNULL(getBillCardPanelWrapper().getBillCardPanel().getBillTable());
		//发货地区和收获地区组合唯一
		
	}
	@Override
	protected void onBoCopy() throws Exception {
		// TODO Auto-generated method stub
		super.onBoCopy();
		getBillUI().setDefaultData();
		getBillCardPanelWrapper().getBillCardPanel().setTailItem("vapproveid", null);
		getBillCardPanelWrapper().getBillCardPanel().setTailItem("dapprovedate", null);
	}
	
}
