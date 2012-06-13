
import nc.bs.logging.Logger;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.BusinessException;
/**
 * ���ܣ���Ե�������--����
 * @mlr 
 */
public class MEnventHandler extends ManageEventHandler {

	private String vbillno = getBillUI().getBillField().getField_BillNo(); // ���ݺ��ֶ�

	public MEnventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}
	@Override
	protected void onBoSave() throws Exception {
		super.onBoSave();
		onBoRefresh();
	}
    /**
     * ȡ�����˵��ݺ�
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
	 * �����ű�ƽ̨,���ϣ����յ��ݺ�
	 */
	@Override
	protected void onBoDel() throws Exception {
		//
		if (getBufferData().getCurrentVO() == null)
			return;

		if (MessageDialog.showYesNoDlg(getBillUI(), "����", "�Ƿ�ȷ�����ϵ�ǰ����?") != UIDialog.ID_YES) {
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
	 * ȡ����ɾ������ʱ�˻ص��ݺ�
	 */
	protected void returnBillNo(String billcode, String pk_billtype,
			String pk_corp) {
		if (billcode != null && billcode.trim().length() > 0) {// ���ݺŲ�Ϊ�գ��Ž��л���
			try {
				nc.vo.pub.billcodemanage.BillCodeObjValueVO bcoVO = new nc.vo.pub.billcodemanage.BillCodeObjValueVO();
				bcoVO.setAttributeValue("��˾", pk_corp);
				nc.ui.pub.billcodemanage.BillcodeRuleBO_Client.returnBillCodeOnDelete(pk_corp, pk_billtype, billcode,bcoVO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		if (999== intBtn) {//���鰴ť ����idΪ999
			onJoinQuery();
		} else {
			super.onBoElse(intBtn);
		}
	}

	protected LinkBillFlowDlg soureDlg = null;

	/**
	 * ����Ի���
	 */
	public LinkBillFlowDlg getSourceDlg() throws BusinessException {
		try {

			soureDlg = new LinkBillFlowDlg(getBillUI(), getUIController()
					.getBillType(), getBufferData().getCurrentVO()
					.getParentVO().getPrimaryKey(), null, _getOperator(),
					_getCorp().getPrimaryKey());
		} catch (BusinessException e) {
			Logger.error(e);
			throw new BusinessException("��ȡ����Ի������! ");
		}
		return soureDlg;
	}

	/**
	 * ����
	 */
	public void onJoinQuery() throws BusinessException {
		getBillManageUI().showHintMessage("����");
		if (getBufferData().getCurrentVO() == null) {
			return;
		}
		getSourceDlg().showModal();
	}
}
