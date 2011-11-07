package nc.ui.wl.pub;

import java.util.ArrayList;

import nc.bs.logging.Logger;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.trade.bill.BillListPanelWrapper;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.BusinessException;
import nc.vo.pub.NullFieldException;
import nc.vo.pub.ValidationException;
import nc.vo.wl.pub.ButtonCommon;

/**
 * ���ܣ���Ե�������--����
 * 
 * @author
 * 
 */
public class WdsPubEnventHandler extends ManageEventHandler {

	private String vbillno = getBillUI().getBillField().getField_BillNo(); // ���ݺ��ֶ�

	public WdsPubEnventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}
	protected BillListPanelWrapper getBillListPanelWrapper() {
		return getBillManageUI().getBillListWrapper();
	} 
	protected BillListPanel getBillListPanel(){
		return getBillListPanelWrapper().getBillListPanel();
	}	
	@Override
	protected void onBoSave() throws Exception {
		try {
			dataNotNullValidate();
		} catch (ValidationException e) {
			MessageDialog.showErrorDlg(getBillUI(), "У��", e.getMessage());
			return;
		}
		super.onBoSave();
		onBoRefresh();
	}

	protected void onBoCancel() throws Exception {
		String billcode = null;
		String pk_billtype = null;
		String pk_corp = null;
		if (isAdding()) {
			billcode = (String) getBillCardPanelWrapper().getBillCardPanel()
					.getHeadItem(vbillno).getValueObject();
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
		String billcode = (String) getBufferData().getCurrentVO().getParentVO()
				.getAttributeValue(vbillno);
		String pk_billtype = getBillManageUI().getUIControl().getBillType();
		String pk_corp = _getCorp().getPrimaryKey();
		super.onBoDel();
		//
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
				nc.ui.pub.billcodemanage.BillcodeRuleBO_Client
						.returnBillCodeOnDelete(pk_corp, pk_billtype, billcode,
								bcoVO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2011-6-7����04:24:33
	 * @throws ValidationException
	 */
	protected void dataNotNullValidate() throws ValidationException {
		StringBuffer message = null;
		BillItem[] headtailitems = getBillCardPanelWrapper().getBillCardPanel()
				.getBillData().getHeadTailItems();
		if (headtailitems != null) {
			for (int i = 0; i < headtailitems.length; i++) {
				if (headtailitems[i].isNull())
					if (isNULL(headtailitems[i].getValueObject())
							&& headtailitems[i].isShow()) {
						if (message == null)
							message = new StringBuffer();
						message.append("[");
						message.append(headtailitems[i].getName());
						message.append("]");
						message.append(",");
					}
			}
		}
		if (message != null) {
			message.deleteCharAt(message.length() - 1);
			throw new NullFieldException(message.toString());
		}

		// ���Ӷ��ӱ��ѭ��
		String[] tableCodes = getBillCardPanelWrapper().getBillCardPanel()
				.getBillData().getTableCodes(BillData.BODY);
		if (tableCodes != null) {
			for (int t = 0; t < tableCodes.length; t++) {
				String tablecode = tableCodes[t];
				for (int i = 0; i < getBillCardPanelWrapper()
						.getBillCardPanel().getBillModel(tablecode)
						.getRowCount(); i++) {
					StringBuffer rowmessage = new StringBuffer();

					rowmessage.append(" ");
					if (tableCodes.length > 1) {
						rowmessage.append(getBillCardPanelWrapper()
								.getBillCardPanel().getBillData().getTableName(
										BillData.BODY, tablecode));
						rowmessage.append("(");
						// "ҳǩ"
						rowmessage.append(nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("_Bill", "UPP_Bill-000003"));
						rowmessage.append(") ");
					}
					rowmessage.append(i + 1);
					rowmessage.append("(");
					// "��"
					rowmessage.append(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("_Bill", "UPP_Bill-000002"));
					rowmessage.append(") ");

					StringBuffer errormessage = null;
					BillItem[] items = getBillCardPanelWrapper()
							.getBillCardPanel().getBillData()
							.getBodyItemsForTable(tablecode);
					for (int j = 0; j < items.length; j++) {
						BillItem item = items[j];
						if (item.isShow() && item.isNull()) {// �����Ƭ��ʾ������Ϊ�գ��ŷǿ�У��
							Object aValue = getBillCardPanelWrapper()
									.getBillCardPanel().getBillModel(tablecode)
									.getValueAt(i, item.getKey());
							if (isNULL(aValue)) {
								errormessage = new StringBuffer();
								errormessage.append("[");
								errormessage.append(item.getName());
								errormessage.append("]");
								errormessage.append(",");
							}
						}
					}
					if (errormessage != null) {

						errormessage.deleteCharAt(errormessage.length() - 1);
						rowmessage.append(errormessage);
						if (message == null)
							message = new StringBuffer(rowmessage);
						else
							message.append(rowmessage);
						break;
					}
				}
				if (message != null)
					break;
			}
		}
		if (message != null) {
			throw new NullFieldException(message.toString());
		}

	}

	private boolean isNULL(Object o) {
		if (o == null || o.toString().trim().equals(""))
			return true;
		return false;
	}

	/**
	 * У������Ψһ��
	 */
	protected void beforeSaveBodyUnique(String[] strs) throws Exception {
		int num = getBillCardPanelWrapper().getBillCardPanel().getBillTable()
				.getRowCount();
		if (strs == null || strs.length == 0) {
			return;
		}
		if (num > 0) {
			ArrayList<String> list = new ArrayList<String>();
			for (int i = 0; i < num; i++) {
				String key = "";
				for (String str : strs) {
					Object o1 = getBillCardPanelWrapper().getBillCardPanel()
							.getBillModel().getValueAt(i, str);
					key = key + "," + String.valueOf(o1);
				}
				if (list.contains(key)) {
					throw new BusinessException("��[" + (i + 1) + "]�б�������ظ�!");
				} else {
					list.add(key);
				}
			}
		}
	}

	@Override
	protected void onBoElse(int intBtn) throws Exception {
		if (ButtonCommon.joinup == intBtn) {
			onJoinQuery();
		} else {
			super.onBoElse(intBtn);
		}
	}

	protected SourceBillFlowDlg soureDlg = null;

	/**
	 * ����Ի���
	 */
	public SourceBillFlowDlg getSourceDlg() throws BusinessException {
		try {

			soureDlg = new SourceBillFlowDlg(getBillUI(), getUIController()
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
