package nc.ui.zmpub.pub.bill;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.pub.BusinessException;
import nc.vo.pub.NullFieldException;
import nc.vo.pub.ValidationException;

/**
 * 针对单表体情况
 * @author zpm
 *
 */
public abstract class DBTManageEventHandler extends CardEventHandler{
	
	private String sWhere = null;
	
	public DBTManageEventHandler(BillCardUI billUI, ICardController control) {
		super(billUI, control);
	}
	
	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		getBillUI().setBillOperate(IBillOperate.OP_EDIT);
		onBoLineAdd();
	}	
	 /**
     * 删除数据
     */
	@Override
	protected void onBoDelete() throws Exception {
		int selectRow = getBillCardPanelWrapper().getBillCardPanel()
				.getBillTable().getSelectedRow();
		if (selectRow < 0) {
			MessageDialog.showHintDlg(getBillUI(), "删除", "请选中要删除的行!");
			return;
		}
		if (MessageDialog.showYesNoDlg(getBillUI(), "删除", "是否确认删除选中的数据?") != UIDialog.ID_YES) {
			return;
		}
		
		getBillUI().setBillOperate(IBillOperate.OP_EDIT);
//		getBillCardPanelWrapper().getBillCardPanel().getBillModel().delLine(new int[]{selectRow});
		onBoLineDel();
		try {
			onBoSave();
		} catch (Exception e) {
			getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
			getBillCardPanelWrapper().getBillCardPanel().getBillTable()
					.getSelectionModel().setSelectionInterval(selectRow,
							selectRow);
			getBillUI().showErrorMessage(e.getMessage());
		}
	}
	/**
	* 修改
	*/
	@Override
	protected void onBoEdit() throws Exception {
		int selectRow = getBillCardPanelWrapper().getBillCardPanel()
				.getBillTable().getSelectedRow();
		if (selectRow < 0) {
			MessageDialog.showHintDlg(getBillUI(), "档案修改", "请选中要修改的行");
			return;
		}

		super.onBoEdit();
		BillModel model = getBillCardPanelWrapper().getBillCardPanel().getBodyPanel().getTableModel();
		model.setRowEditState(true);
		model.setEditRow(selectRow);
	}

	protected void onBoSave() throws Exception {
		try{
			dataNotNullValidate();
		   }catch (ValidationException e) {
		      MessageDialog.showErrorDlg(getBillUI(), "校验", e.getMessage());
		      return;
		}try {
			super.onBoSave();
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}
	protected void onBoCancel() throws Exception {
		super.onBoCancel();
	}		
	protected void dataNotNullValidate() throws ValidationException {
		StringBuffer message = null;
		BillItem[] headtailitems = getBillCardPanelWrapper().getBillCardPanel().getBillData().getHeadTailItems();
		if (headtailitems != null) {
			for (int i = 0; i < headtailitems.length; i++) {
				if (headtailitems[i].isNull())
					if (isNULL(headtailitems[i].getValueObject()) && headtailitems[i].isShow()) {
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

		// 增加多子表的循环
		String[] tableCodes = getBillCardPanelWrapper().getBillCardPanel().getBillData().getTableCodes(BillData.BODY);
		if (tableCodes != null) {
			for (int t = 0; t < tableCodes.length; t++) {
				String tablecode = tableCodes[t];
				for (int i = 0; i < getBillCardPanelWrapper().getBillCardPanel().getBillModel(tablecode).getRowCount(); i++) {
					StringBuffer rowmessage = new StringBuffer();

					rowmessage.append(" ");
					if (tableCodes.length > 1) {
						rowmessage.append(getBillCardPanelWrapper().getBillCardPanel().getBillData().getTableName(BillData.BODY, tablecode));
						rowmessage.append("(");
						// "页签"
						rowmessage.append(nc.ui.ml.NCLangRes.getInstance().getStrByID("_Bill", "UPP_Bill-000003"));
						rowmessage.append(") ");
					}
					rowmessage.append(i + 1);
					rowmessage.append("(");
					// "行"
					rowmessage.append(nc.ui.ml.NCLangRes.getInstance().getStrByID("_Bill", "UPP_Bill-000002"));
					rowmessage.append(") ");

					StringBuffer errormessage = null;
					BillItem[] items = getBillCardPanelWrapper().getBillCardPanel().getBillData().getBodyItemsForTable(tablecode);
					for (int j = 0; j < items.length; j++) {
						BillItem item = items[j];
						if (item.isShow() && item.isNull()) {// 如果卡片显示，并且为空，才非空校验
							Object aValue = getBillCardPanelWrapper().getBillCardPanel().getBillModel(tablecode).getValueAt(i, item.getKey());
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
	protected void onBoBodyQuery() throws Exception {
		StringBuffer strWhere = new StringBuffer();
		if (askForBodyQueryCondition(strWhere) == false)
			return;// 用户放弃了查询
		sWhere = strWhere.toString();
		doBodyQuery(strWhere.toString());
	}
	@Override
	protected void onBoRefresh() throws Exception {
		if(sWhere!=null && sWhere.length()>0){
			doBodyQuery(sWhere);
		}
	}
}
