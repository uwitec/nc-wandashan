package nc.ui.hg.pu.invoice;

import nc.ui.hg.pu.pub.FlowManageEventHandler;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.hg.pu.pub.HgPuBtnConst;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.trade.pub.IBillStatus;

public class ClientEventHandler extends FlowManageEventHandler {

	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	UIDialog m_ClientUIQueryDlg = null;

	@Override
	protected UIDialog createQueryUI() {
		return new ClientUIQueryDlg(getBillUI(), null, _getCorp()
				.getPrimaryKey(), getBillUI().getModuleCode(), _getOperator(),
				null, null);
	}

	protected UIDialog getQueryDlg() {
		if (m_ClientUIQueryDlg == null) {
			m_ClientUIQueryDlg = new ClientUIQueryDlg(getBillUI(), null,
					_getCorp().getPrimaryKey(), getBillUI().getModuleCode(),
					_getOperator(), null, HgPubConst.PLAN_BAOZHANG_BILLTYPE);
		}
		return m_ClientUIQueryDlg;

	}

	protected String getHeadCondition() {
		// 修改字段
		return " h.pk_corp = '" + _getCorp().getPrimaryKey()
				+ "'  and  h.pk_billtype = '"
				+ HgPubConst.PLAN_BAOZHANG_BILLTYPE + "' ";
	}

	@Override
	protected void onBoElse(int intBtn) throws Exception {
		if (intBtn == HgPuBtnConst.Editor) {// 修改
			onBoEdit();
		} else {
			super.onBoElse(intBtn);
		}
	}

	protected void onBoEdit() throws Exception {
		if (getBufferData().isVOBufferEmpty()) {
			return;
		}
		if (getBufferData().getCurrentRow() < 0) {
			getBillUI().showWarningMessage("请选择要修改的单据");
			return;
		}
		super.onBoEdit();
	
	}

	@Override
	public void onBillRef() throws Exception {
			super.onBillRef();
			setDefaultData();
			getBillCardPanelWrapper().getBillCardPanel().getBillModel().execLoadFormulaByKey("bizname");
			getBillCardPanelWrapper().getBillCardPanel().getBillModel().execLoadFormulaByKey("deptname");
	}
	
	public void setDefaultData() throws Exception {
		String vbillno = ((ClientUI)getBillUI()).getBillNo();
		setHeadItemValue("vbillstatus", IBillStatus.FREE);// 单据状态
		setTailItemValue("voperatorid", _getOperator());// 在表尾了
		setHeadItemValue("dbilldate", _getDate());
		setHeadItemValue("pk_corp", _getCorp().getPrimaryKey());
		setTailItemValue("dmakedate", _getDate());
		setHeadItemValue("pk_billtype", HgPubConst.PLAN_BAOZHANG_BILLTYPE);
		setHeadItemValue("vbillno", vbillno);
		
	}
	protected void setHeadItemValue(String item, Object value) {
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem(item, value);
	}

	protected void setTailItemValue(String item, Object value) {
		getBillCardPanelWrapper().getBillCardPanel().setTailItem(item, value);
	}
}
