package nc.ui.zb.entry;

import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.msg.PfLinkData;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.uap.sf.SFClientUtil;
import nc.ui.zb.pub.BillRowNo;
import nc.ui.zb.pub.FlowManageEventHandler;
import nc.vo.po.OrderVO;
import nc.vo.querytemplate.TemplateInfo;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.zb.pub.ZbPuBtnConst;
import nc.vo.zb.pub.ZbPubConst;

public class ClientEventHandler extends FlowManageEventHandler {

	public ClientUIQueryDlg queryDialog = null;

	public ClientEventHandler(ClientUI clientUI, IControllerBase control) {
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
		return " pk_corp = '" + _getCorp().getPrimaryKey() + "'  ";
	}

	@Override
	protected void onBoLineAdd() throws Exception {
		// TODO Auto-generated method stub
		super.onBoLineAdd();
		BillRowNo.addLineRowNo(getBillCardPanelWrapper().getBillCardPanel(),
				ZbPubConst.ZB_Result_BILLTYPE, "crowno");
	}
	
	protected void onBoElse(int intBtn) throws Exception {
		if(intBtn==ZbPuBtnConst.GenPurOrder){
			onGenerateOrderBill();
		}else if(intBtn == ZbPuBtnConst.Editor){//ÐÞ¸Ä
			   onBoEdit();
		}else{
			super.onBoElse(intBtn);
		}
	}
	
	protected void onGenerateOrderBill() throws Exception {

		HYBillVO billVO = null;
		billVO = (HYBillVO) getBillUI().getVOFromUI();
		
		OrderVO vo = (OrderVO)ZbEntryHelper.chgZB05TO21(billVO);
		
		PfLinkData linkData = new PfLinkData();
		linkData.setUserObject(vo);
		linkData.setSourceBillType(ZbPubConst.ZB_Result_BILLTYPE);
		SFClientUtil.openLinkedADDDialog("4004020201", getBillUI(), linkData);

	}
}
