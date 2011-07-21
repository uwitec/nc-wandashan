package nc.ui.zb.price.bill;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.zb.pub.FlowManageEventHandler;
import nc.vo.querytemplate.TemplateInfo;
import nc.vo.zb.pub.ZbPuBtnConst;

public class ClientEventHandler extends FlowManageEventHandler {
	public ClientUIQueryDlg queryDialog = null;

	public ClientEventHandler(SubmintPriceBillUI clientUI, IControllerBase control) {
		super(clientUI, control);
	}
	
	protected UIDialog createQueryUI() {
		if (queryDialog == null) {
			TemplateInfo tempinfo = new TemplateInfo();
			tempinfo.setPk_Org(_getCorp().getPk_corp());
			tempinfo.setCurrentCorpPk(_getCorp().getPk_corp());
			tempinfo.setFunNode(getBillUI()._getModuleCode());
			tempinfo.setUserid(getBillUI()._getOperator());
			queryDialog = new ClientUIQueryDlg(getBillUI(), null, tempinfo);
		}
		return queryDialog;
	}


	@Override
	protected String getHeadCondition() {
		return " zb_submitbill.pk_corp = '" + _getCorp().getPrimaryKey() + "'  ";
	}
	/**
	 * 修改时标段不可重选
	 */
	protected void onBoEdit() throws Exception {
		
		super.onBoEdit();
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("cbiddingid").setEnabled(false);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("temp").setEnabled(false);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("venname").setEnabled(false);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("bistemp").setEnabled(false);
		
	}
	
	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		// TODO Auto-generated method stub
		super.onBoAdd( bo);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("cbiddingid").setEnabled(true);//标段可编辑
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("venname").setEnabled(true);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("bistemp").setEnabled(true);
	}
	
	protected void onBoElse(int intBtn) throws Exception {
		if(intBtn == ZbPuBtnConst.Editor){//修改
			  onBoEdit();
		}else if(intBtn == ZbPuBtnConst.REVISED){//修订
			  onBoEdit();
		}else{
			super.onBoElse(intBtn);
		}
	}
}
