
import nc.bs.logging.Logger;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.BusinessException;
/**
 * 功能：针对单据审批--基类
 * @mlr 
 */
public class MEnventHandler extends ManageEventHandler {

	private String vbillno = getBillUI().getBillField().getField_BillNo(); // 单据号字段

	public MEnventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}
	@Override
	protected void onBoSave() throws Exception {
		super.onBoSave();
		onBoRefresh();
	}
    /**
     * 取消回退单据号
     */
	protected void onBoCancel() throws Exception {
		String billcode = null;
		String pk_billtype = null;
		String pk_corp = null;
		if (isAdding()) {
			billcode = (String) getBillCardPanelWrapper().getBillCardPanel().getHeadItem(vbillno).getValueObject();
			pk_billtype = getBillManageUI().getUIControl().getBillType();
			pk_corp = _getCorp().getPrimaryKey();
		}
		super.onBoCancel();
		if (billcode != null && !"".equals(billcode)) {
			returnBillNo(billcode, pk_billtype, pk_corp);
		}
	}
	/**
	 * 动作脚本平台,作废，回收单据号
	 */
	@Override
	protected void onBoDel() throws Exception {
		//
		if (getBufferData().getCurrentVO() == null)
			return;

		if (MessageDialog.showYesNoDlg(getBillUI(), "作废", "是否确认作废当前单据?") != UIDialog.ID_YES) {
			return;
		}
		String billcode = (String) getBufferData().getCurrentVO().getParentVO().getAttributeValue(vbillno);
		String pk_billtype = getBillManageUI().getUIControl().getBillType();
		String pk_corp = _getCorp().getPrimaryKey();
		super.onBoDel();
		returnBillNo(billcode, pk_billtype, pk_corp);
	}

	protected BillManageUI getBillManageUI() {
		return (BillManageUI) getBillUI();
	}
	/**
	 * 取消、删除单据时退回单据号
	 */
	protected void returnBillNo(String billcode, String pk_billtype,
			String pk_corp) {
		if (billcode != null && billcode.trim().length() > 0) {// 单据号不为空，才进行回退
			try {
				nc.vo.pub.billcodemanage.BillCodeObjValueVO bcoVO = new nc.vo.pub.billcodemanage.BillCodeObjValueVO();
				bcoVO.setAttributeValue("公司", pk_corp);
				nc.ui.pub.billcodemanage.BillcodeRuleBO_Client.returnBillCodeOnDelete(pk_corp, pk_billtype, billcode,bcoVO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		if (999== intBtn) {//联查按钮 定制id为999
			onJoinQuery();
		} else {
			super.onBoElse(intBtn);
		}
	}

	protected LinkBillFlowDlg soureDlg = null;

	/**
	 * 联查对话框
	 */
	public LinkBillFlowDlg getSourceDlg() throws BusinessException {
		try {

			soureDlg = new LinkBillFlowDlg(getBillUI(), getUIController()
					.getBillType(), getBufferData().getCurrentVO()
					.getParentVO().getPrimaryKey(), null, _getOperator(),
					_getCorp().getPrimaryKey());
		} catch (BusinessException e) {
			Logger.error(e);
			throw new BusinessException("获取联查对话框出错! ");
		}
		return soureDlg;
	}

	/**
	 * 联查
	 */
	public void onJoinQuery() throws BusinessException {
		getBillManageUI().showHintMessage("联查");
		if (getBufferData().getCurrentVO() == null) {
			return;
		}
		getSourceDlg().showModal();
	}
}
