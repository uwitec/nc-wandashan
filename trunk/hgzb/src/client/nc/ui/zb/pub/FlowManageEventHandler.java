package nc.ui.zb.pub;

import nc.bs.logging.Logger;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.BillListPanelWrapper;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.trade.multichild.MultiChildBillManageUI;
import nc.ui.trade.query.INormalQuery;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.NullFieldException;
import nc.vo.pub.ValidationException;
import nc.vo.zb.pub.ZbPuBtnConst;
/**
 * ����������
 * @author Administrator
 *
 */
public class FlowManageEventHandler extends ManageEventHandler {
	private SourceBillFlowDlg soureDlg = null;
	
	public FlowManageEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	protected BillListPanel getBillListPanel(){
		return getBillListPanelWrapper().getBillListPanel();
	}
	protected BillCardPanel getBillCardPanel(){
		return getBillCardPanelWrapper().getBillCardPanel();
	}
	
	protected BillManageUI getBillManageUI() {
		return (BillManageUI) getBillUI();
	}
	
	protected BillListPanelWrapper getBillListPanelWrapper() {
		return getBillManageUI().getBillListWrapper();
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
	}
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
	@Override
	protected String getHeadCondition() {
		return " pk_corp = '"+_getCorp().getPrimaryKey()+"' ";
	}

	protected void onBoCancel() throws Exception {
		String billcode = null;
		String pk_billtype = null;
		String pk_corp = null;
		if (isAdding()) {
			billcode = (String)getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillno").getValueObject();
			pk_billtype = getBillManageUI().getUIControl().getBillType();
			pk_corp = _getCorp().getPrimaryKey();
		}
		super.onBoCancel();
		if(billcode!=null && !"".equals(billcode)){
			returnBillNo(billcode,pk_billtype,pk_corp);
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
		String billcode = (String)getBufferData().getCurrentVO().getParentVO().getAttributeValue("vbillno");
		String pk_billtype =  getBillManageUI().getUIControl().getBillType();
		String pk_corp = _getCorp().getPrimaryKey();
		super.onBoDel();
		//
		returnBillNo(billcode,pk_billtype,pk_corp);
	}
	@Override
	protected void onBoDelete() throws Exception {
		//
		if (getBufferData().getCurrentVO() == null)
			return;
		String billcode = (String)getBufferData().getCurrentVO().getParentVO().getAttributeValue("vbillno");
		String pk_billtype =  getBillManageUI().getUIControl().getBillType();
		String pk_corp = _getCorp().getPrimaryKey();
		super.onBoDelete();
		//
		returnBillNo(billcode,pk_billtype,pk_corp);
	}
	/**
	 * ȡ����ɾ������ʱ�˻ص��ݺ�
	 */
	protected void returnBillNo(String billcode,String pk_billtype,String pk_corp) {
		if (billcode != null && billcode.trim().length() > 0) {// ���ݺŲ�Ϊ�գ��Ž��л���
			try {
				nc.vo.pub.billcodemanage.BillCodeObjValueVO bcoVO = new nc.vo.pub.billcodemanage.BillCodeObjValueVO();
				bcoVO.setAttributeValue("��˾", pk_corp);
				nc.ui.pub.billcodemanage.BillcodeRuleBO_Client.returnBillCodeOnDelete(pk_corp, pk_billtype, billcode, bcoVO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	protected void updateListVo() throws java.lang.Exception {
		CircularlyAccessibleValueObject vo = null;
		if (getBufferData().getCurrentVO() != null) {
			vo = getBufferData().getCurrentVO().getParentVO();
			getBillListPanelWrapper().updateListVo(vo,getBufferData().getCurrentRow());
		}
	}

	@Override
	public void onBillRef() throws Exception {
		// ����ǰ����ͷ����ı��ҽ�ԭ�ҽ����һ��ʵľ�������Ϊ8����ֹ���պ���־��ȵĶ�ʧ
		setMaxDigitBeforeRef();
		try {
			super.onBillRef();
			if (PfUtilClient.isCloseOK()) {
				getBillUI().setBillOperate(IBillOperate.OP_REFADD);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			// �������֮��ԭ���հ�ťtag
			ButtonObject btn = getButtonManager().getButton(IBillButton.Refbill);
			btn.setTag(String.valueOf(IBillButton.Refbill));
//			if (PfUtilClient.isCloseOK()) {
//				getBillUI().setBillOperate(IBillOperate.OP_REFADD);
//			}
		}
	}
	/**
	 * ����ǰ����ͷ����ı��ҽ�ԭ�ҽ����һ��ʵľ�������Ϊ8����ֹ���պ���־��ȵĶ�ʧ
	 */
	protected void setMaxDigitBeforeRef() {
		try {
			String MAX_DIGIT = "8";
			String[] headshowitems = null;
			String[][] headshowmaxdigitnums = null;
			String[] bodyshowitems = null;
			String[][] bodyshowmaxdigitnums = null;

			String[][] headshownums = null;
			String[][] bodyshownums = null;

			if (getBillUI() instanceof BillManageUI) {
				BillManageUI clientui = (BillManageUI) getBillUI();
				headshownums = clientui.getHeadShowNum();
				bodyshownums = clientui.getItemShowNum();
			} else if (getBillUI() instanceof MultiChildBillManageUI) {
				MultiChildBillManageUI clientui = (MultiChildBillManageUI) getBillUI();
				headshownums = clientui.getHeadShowNum();
				bodyshownums = clientui.getItemShowNum();
			}

			if (headshownums != null && headshownums.length > 0) {
				headshowitems = headshownums[0];
				if (headshownums[0] != null && headshownums[0].length > 0) {
					String[] headshowdigits = new String[headshowitems.length];
					for (int i = 0; i < headshowdigits.length; i++) {
						headshowdigits[i] = MAX_DIGIT;
					}
					headshowmaxdigitnums = new String[][] { headshowitems, headshowdigits };
				}
			}

			if (bodyshownums != null && bodyshownums.length > 0) {
				String[] tablecodes = getBillCardPanelWrapper().getBillCardPanel().getBillData().getBodyTableCodes();
				if (tablecodes != null && tablecodes.length > 0) {
					bodyshowmaxdigitnums = new String[tablecodes.length * 2][];
					for (int t = 0; t < tablecodes.length; t++) {
						bodyshowitems = bodyshownums[2 * t];
						if (bodyshowitems != null && bodyshowitems.length > 0) {
							String[] bodyshowdigits = new String[bodyshowitems.length];
							for (int i = 0; i < bodyshowdigits.length; i++) {
								bodyshowdigits[i] = MAX_DIGIT;
							}
							bodyshowmaxdigitnums[2 * t] = bodyshowitems;
							bodyshowmaxdigitnums[2 * t + 1] = bodyshowdigits;
						}

					}

				}
			}

			// ���½���
			getBillCardPanelWrapper().setCardDecimalDigits(headshowmaxdigitnums, bodyshowmaxdigitnums);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.debug("setMaxDigitBeforeRef:UI����ʱ��ʼ�����ݽ���Ϊ8�������쳣." + e.getMessage());
		}

	}

	@Override
	protected boolean askForQueryCondition(StringBuffer sqlWhereBuf)
			throws Exception {
		if (sqlWhereBuf == null)
			throw new IllegalArgumentException(
					"askForQueryCondition().sqlWhereBuf cann't be null");
		UIDialog querydialog = getQueryUI();

		if (querydialog.showModal() != UIDialog.ID_OK)
			return false;
		INormalQuery query = (INormalQuery) querydialog;

		String strWhere = query.getWhereSql();
		if (strWhere == null || strWhere.trim().length()==0)
			strWhere = "1=1";

		if (getButtonManager().getButton(IBillButton.Busitype) != null) {
			if (getBillIsUseBusiCode().booleanValue())
				// ҵ�����ͱ���
				strWhere = "(" + strWhere + ") and "
						+ getBillField().getField_BusiCode() + "='"
						+ getBillUI().getBusicode() + "'";

			else
				// ҵ������
				strWhere = "(" + strWhere + ") and "
						+ getBillField().getField_Busitype() + "='"
						+ getBillUI().getBusinessType() + "'";

		}

		if (getHeadCondition() != null)
			strWhere = strWhere + " and " + getHeadCondition();
		// ��������ֱ�Ӱ����ƴ�õĴ��ŵ�StringBuffer�ж���ȥ�Ż�ƴ���Ĺ���
		sqlWhereBuf.append(strWhere);
		return true;
	}
	
	  /**
	   * ����
	   */
	public void onJoinQuery()throws BusinessException {
		getBillManageUI().showHintMessage("����");
		if(getBufferData().getCurrentVO() == null ){
			return;
		}
		
		getSourceDlg().showModal();
	}
	/**
	 * ����Ի���
	 */
	public SourceBillFlowDlg getSourceDlg() throws BusinessException{
			try {
				soureDlg = new SourceBillFlowDlg(getBillManageUI(), getUIController().getBillType(),/* ��ǰ�������� */
						getBufferData().getCurrentVO().getParentVO().getPrimaryKey(), /* ��ǰ����ID */
						null, /* ��ǰҵ������ */
						_getOperator(), /* ��ǰ�û�ID */
						(String)getBufferData().getCurrentVO().getParentVO().getAttributeValue("vbillno") /* ���ݺ� */);
				soureDlg.setBillFinderClassname("nc.bs.scm.sourcebill.SCMBillFinder");
			} catch (BusinessException e) {
				Logger.error(e);
				throw new BusinessException("��ȡ����Ի������! ");
			}
			return soureDlg;
	}
	
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		if(intBtn == ZbPuBtnConst.LINKQUERY){//����
			onJoinQuery();
		}else{
			super.onBoElse(intBtn);
		}
	}
}
