package nc.ui.wds.tranprice.specialbill;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.query.HYQueryDLG;
import nc.ui.wl.pub.WdsPubEnventHandler;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.wds.tranprice.bill.SendBillVO;
import nc.vo.wl.pub.ButtonCommon;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;

public class SendBillEventHandler extends WdsPubEnventHandler {

	public SendBillEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
		
	}
	@Override
	protected UIDialog createQueryUI() {
		return new HYQueryDLG(getBillUI(), null, getBillUI()._getCorp().getPrimaryKey(), WdsWlPubConst.TRANS_SEPCLPRICE_NODECODE, getBillUI()._getOperator(), null);
	}
	@Override
	protected String getHeadCondition() {
		String strWhere = super.getHeadCondition();
		if(strWhere == null || "".equals(strWhere)){
			return " and pk_billtype='"+WdsWlPubConst.WDSN+"'";
		}else {
			return strWhere+" and pk_billtype='"+WdsWlPubConst.WDSN+"'";
		}
	}
	
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		// TODO Auto-generated method stub
		super.onBoElse(intBtn);
		switch (intBtn){
			case ButtonCommon.REFWDS6:
				((SendBillUI)getBillUI()).setRefBillType(WdsWlPubConst.BILLTYPE_OTHER_OUT);
				onBillRef();
			break;
			case ButtonCommon.TRAN_COL:
				onCol();
		}
	}
	@Override
	public void onBillRef() throws Exception {
		// TODO Auto-generated method stub
		super.onBillRef();
		//参照新增后，设置界面
		((SendBillUI)getBillUI()).setRefDefalutData();
	}
	/**
	 * 运费计算
	 */
	public void onCol(){
		if(getBufferData().isVOBufferEmpty()){
			getBillUI().showHintMessage("无数据");
			return;
		}
		SendBillVO billvo = (SendBillVO)getBufferData().getCurrentVO();
		if(billvo == null)
			return;
		UFBoolean isSale = UFBoolean.TRUE;
		if(billvo.getBodyVos() == null || billvo.getBodyVos().length == 0){
			getBillUI().showHintMessage("表体数据为空");
			return;
		}else{
			if(billvo.getBodyVos()[0].getCsourcetype().equalsIgnoreCase(WdsWlPubConst.BILLTYPE_OTHER_OUT))
				isSale = UFBoolean.FALSE;
		}
		try{	
			billvo = SendBillHelper.col(getBillUI(), billvo, ClientEnvironment.getInstance().getDate(),isSale);
			if(getBillManageUI().isListPanelSelected()){
				onBoReturn();
			}

		}catch(Exception e){
			e.printStackTrace();
			getBillUI().showErrorMessage(WdsWlPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
			return;
		}

		getBillCardPanelWrapper().getBillCardPanel().getBillData().setBillValueVO(billvo);
		getBillCardPanelWrapper().getBillCardPanel().getBillModel().execLoadFormula();
		getBufferData().setCurrentVO(billvo);		
	}
	
	

}
