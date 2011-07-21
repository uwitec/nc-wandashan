package nc.ui.zb.avnum;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.zb.pub.BillRowNo;
import nc.ui.zb.pub.FlowManageEventHandler;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValidationException;
import nc.vo.querytemplate.TemplateInfo;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.zb.pub.ZbPuBtnConst;
import nc.vo.zb.pub.ZbPubConst;
import nc.vo.zb.pub.ZbPubTool;

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
		super.onBoLineAdd();
		BillRowNo.addLineRowNo(getBillCardPanelWrapper().getBillCardPanel(),
				ZbPubConst.ZB_AVNUM_BILLTYPE, "crowno");
	}

	@Override
	protected ClientUI getBillManageUI() {
		return (ClientUI) getBillUI();
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
		String billcode = (String) getBufferData().getCurrentVO().getParentVO()
				.getAttributeValue("vbillno");
		String pk_billtype = getBillManageUI().getUIControl().getBillType();
		String pk_corp = _getCorp().getPrimaryKey();
		// ����ʱɾ�����е����ӱ���Ϣ δ���
		super.onBoDel();
		//
		returnBillNo(billcode, pk_billtype, pk_corp);
	}

	

	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		// TODO Auto-generated method stub
		super.onBoAdd(bo);
		getClientUI().amap.clear();// ���map
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("cbiddingid")
				.setEnabled(true);// ��οɱ༭

	}

	protected ClientUI getClientUI() {
		return (ClientUI) getBillManageUI();
	}

	/**
	 * �޸�ʱ��β�����ѡ
	 */
	protected void onBoEdit() throws Exception {

		super.onBoEdit();
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("cbiddingid")
				.setEnabled(false);
	}

	@Override
	protected void onBoSave() throws Exception {
		try {
			dataNotNullValidate();
		} catch (ValidationException e) {
			MessageDialog.showErrorDlg(getBillUI(), "У��", e.getMessage());
			return;
		}
		String cbiddingid = ZbPubTool.getString_NullAsTrimZeroLen(getBillCardPanelWrapper()
				.getBillCardPanel().getHeadItem("cbiddingid").getValueObject());
		if("".equalsIgnoreCase(cbiddingid)||cbiddingid==null)
			throw new BusinessException("���鲻��Ϊ��");
		HYBillVO billvo =getClientUI().getBuffer(cbiddingid);
		boolean isSave = true;

		// �ж��Ƿ��д�������
		if (billvo.getParentVO() == null
				&& (billvo.getChildrenVO() == null || billvo.getChildrenVO().length == 0)) {
			isSave = false;
		} else {
			billvo = AvNumHelper.saveHyBillVO(billvo);
		}
		setBufferData(isSave, billvo);
	}

	private void setBufferData(boolean flag, HYBillVO billvo) throws Exception {
		int nCurrentRow = -1;
		if (flag) {
			if (isEditing()) {
				if (getBufferData().isVOBufferEmpty()) {
					getBufferData().addVOToBuffer(billvo);
					nCurrentRow = 0;

				} else {
					getBufferData().setCurrentVO(billvo);
					nCurrentRow = getBufferData().getCurrentRow();
				}
			} else {
				getBufferData().addVOsToBuffer(
						new AggregatedValueObject[] { billvo });
				nCurrentRow = getBufferData().getVOBufferSize() - 1;
			}
		}

		if (nCurrentRow >= 0) {
			getBufferData().setCurrentRowWithOutTriggerEvent(nCurrentRow);
		}

		setAddNewOperate(isAdding(), billvo);

		// ���ñ����״̬
		setSaveOperateState();

		if (nCurrentRow >= 0) {
			getBufferData().setCurrentRow(nCurrentRow);
		}
	}
	public void onButton(ButtonObject bo) {
		setTabbedPane();
		super.onButton(bo);
	}
	//��������ҵǩ
	private void setTabbedPane(){
		if (getBillManageUI().isListPanelSelected()) {
			getBillListPanel().getBodyTabbedPane().setSelectedIndex(0);
		} else {
			getBillCardPanel().getBodyTabbedPane().setSelectedIndex(0);
		}
	}
	
	protected void onBoElse(int intBtn) throws Exception {
	 if(intBtn == ZbPuBtnConst.Editor){//�޸�
			   onBoEdit();
		}else{
			super.onBoElse(intBtn);
		}
	}
}
