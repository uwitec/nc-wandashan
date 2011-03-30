package nc.ui.hg.pu.pub;

import java.lang.reflect.Array;
import java.util.ArrayList;

import nc.bs.logging.Logger;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.BillListPanelWrapper;
import nc.ui.trade.bill.BillTemplateWrapper;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.trade.multichild.MultiChildBillManageUI;
import nc.ui.trade.pub.CardPanelPRTS;
import nc.ui.trade.query.INormalQuery;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.NullFieldException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
/**
 *针对主子表、基本档案审批、弃审动作类
 *zpm
 */
public class CHManageEventHandler extends ManageEventHandler {
    
	//当前显示的Panel,LIST OR CARD
	protected String m_CurrentPanel = BillTemplateWrapper.LISTPANEL;
	
	public CHManageEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
			
	}
	
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		
			super.onBoElse(intBtn);
	}
	/**
	 * 基本档案审批，多子表
	 */
	protected void onAudit() throws java.lang.Exception{
		if(getBufferData().getCurrentVO()==null){
			getBillUI().showErrorMessage("请选择数据!");
			return;
		}
		/**多选单据审批**/
		Object[] obj = null;
		AggregatedValueObject[] aggvos = null;
		int[] rows = null;
		if(getBillListPanel().isShowing()){//列表状态
			Class cls = getBufferData().getCurrentVO().getClass();
			rows = getBillListPanel().getHeadTable().getSelectedRows();
			ArrayList<AggregatedValueObject> list = new ArrayList<AggregatedValueObject>();
			for(int i = 0 ;i<rows.length;i++){
				Object v = getBufferData().getVOByRowNo(rows[i]).getParentVO().getAttributeValue("vapproveid");
				if(v!=null && !"".equals(v))
					throw new BusinessException("所选中的行，存在已审批的数据");
				list.add(getBufferData().getVOByRowNo(rows[i]));
			}
			aggvos = list.toArray((AggregatedValueObject[])Array.newInstance(cls, 0));
		}else{
			aggvos = new AggregatedValueObject[]{getBufferData().getCurrentVO()};
			int row = getBufferData().getCurrentRow();
			rows = new int[]{row};
		}
		
		if(aggvos!=null && aggvos.length>0){
			obj = new Object[aggvos.length];
			for(int i = 0;i< aggvos.length;i++){
				ArrayList z = new ArrayList();
				z.add(_getOperator());
				z.add(new UFBoolean(true));
				obj[i] = z;
			}
		}
		//审批
		AggregatedValueObject[] objs = (AggregatedValueObject[])PfUtilClient.processBatch(getBillManageUI(), "Audit", getBillManageUI().getUIControl().getBillType(), _getDate().toString(), aggvos,obj );
		//
		if(rows!=null && rows.length>0 && objs!=null && objs.length>0){
			for(int i = 0 ;i<rows.length;i++){
				if(rows[i]>=0){
					getBufferData().setVOAt(rows[i], objs[i]);
				}
			}
			getBufferData().setCurrentRow(getBufferData().getCurrentRow());
			// 更新列表数据
			updateListVo(rows);
		}
	}
	/**
	 * 基本档案弃审，多子表
	 */
	protected void onUnAudit() throws java.lang.Exception{
		if(getBufferData().getCurrentVO()==null){
			getBillUI().showErrorMessage("请选择数据!");
			return;
		}
		/**多选单据弃审**/
		Object[] obj = null;
		AggregatedValueObject[] aggvos = null;
		int[] rows = null;
		if(getBillListPanel().isShowing()){//列表状态
			Class cls = getBufferData().getCurrentVO().getClass();
			rows = getBillListPanel().getHeadTable().getSelectedRows();
			ArrayList<AggregatedValueObject> list = new ArrayList<AggregatedValueObject>();
			for(int i = 0 ;i<rows.length;i++){
				Object v = getBufferData().getVOByRowNo(rows[i]).getParentVO().getAttributeValue("vapproveid");
				if(v==null || "".equals(v))
					throw new BusinessException("所选中的行，存在未审批的数据");
				list.add(getBufferData().getVOByRowNo(rows[i]));
			}
			aggvos = list.toArray((AggregatedValueObject[])Array.newInstance(cls, 0));
		}else{
			aggvos = new AggregatedValueObject[]{getBufferData().getCurrentVO()};
			int row = getBufferData().getCurrentRow();
			rows = new int[]{row};
		}
		
		if(aggvos!=null && aggvos.length>0){
			obj = new Object[aggvos.length];
			for(int i = 0;i< aggvos.length;i++){
				ArrayList z = new ArrayList();
				z.add(_getOperator());
				z.add(new UFBoolean(false));
				obj[i] = z;
			}
		}
		//弃审
		AggregatedValueObject[] objs = (AggregatedValueObject[])PfUtilClient.processBatch(getBillManageUI(), "Audit", getBillManageUI().getUIControl().getBillType(), _getDate().toString(), aggvos,obj );
		//
		if(rows!=null && rows.length>0 && objs!=null && objs.length>0){
			for(int i = 0 ;i<rows.length;i++){
				if(rows[i]>=0){
					getBufferData().setVOAt(rows[i], objs[i]);
				}
			}
			getBufferData().setCurrentRow(getBufferData().getCurrentRow());
			// 更新列表数据
			updateListVo(rows);
		}
	}
	/**
	 * 更新列表数据
	 */
	protected void updateListVo(int[] rows) throws java.lang.Exception {
		CircularlyAccessibleValueObject vo = null;
		if(rows!=null && rows.length >= 0){
			for(int i : rows){
				vo = getBufferData().getVOByRowNo(i).getParentVO();
				getBillListPanelWrapper().updateListVo(vo,i);
			}
		}
	}
	protected BillListPanel getBillListPanel(){
		return getBillListPanelWrapper().getBillListPanel();
	}
	
	public BillManageUI getBillManageUI() {
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
			MessageDialog.showErrorDlg(getBillUI(), "校验", e.getMessage());
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

		// 增加多子表的循环
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
						// "页签"
						rowmessage.append(nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("_Bill", "UPP_Bill-000003"));
						rowmessage.append(") ");
					}
					rowmessage.append(i + 1);
					rowmessage.append("(");
					// "行"
					rowmessage.append(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("_Bill", "UPP_Bill-000002"));
					rowmessage.append(") ");

					StringBuffer errormessage = null;
					BillItem[] items = getBillCardPanelWrapper()
							.getBillCardPanel().getBillData()
							.getBodyItemsForTable(tablecode);
					for (int j = 0; j < items.length; j++) {
						BillItem item = items[j];
						if (item.isShow() && item.isNull()) {// 如果卡片显示，并且为空，才非空校验
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
	public void onButton(ButtonObject bo) {
		super.onButton(bo);
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
	 * 取消、删除单据时退回单据号
	 */
	protected void returnBillNo(String billcode,String pk_billtype,String pk_corp) {
		if (billcode != null && billcode.trim().length() > 0) {// 单据号不为空，才进行回退
			try {
				nc.vo.pub.billcodemanage.BillCodeObjValueVO bcoVO = new nc.vo.pub.billcodemanage.BillCodeObjValueVO();
				bcoVO.setAttributeValue("公司", pk_corp);
				nc.ui.pub.billcodemanage.BillcodeRuleBO_Client.returnBillCodeOnDelete(pk_corp, pk_billtype, billcode, bcoVO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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
	
	protected void updateListVo() throws java.lang.Exception {
		CircularlyAccessibleValueObject vo = null;
		if (getBufferData().getCurrentVO() != null) {
			vo = getBufferData().getCurrentVO().getParentVO();
			getBillListPanelWrapper().updateListVo(vo,getBufferData().getCurrentRow());
		}
	}
	@Override
	protected void onBoDirectPrint() throws Exception {
		nc.ui.pub.print.IDataSource dataSource = new CardPanelPRTS(getBillUI()
				._getModuleCode(), getBillCardPanelWrapper().getBillCardPanel());
		nc.ui.pub.print.PrintEntry print = new nc.ui.pub.print.PrintEntry(null,
				dataSource);
		print.setTemplateID(getBillUI()._getCorp().getPrimaryKey(), getBillUI()
				._getModuleCode(), getBillUI()._getOperator(), getBillUI()
				.getBusinessType(), getBillUI().getNodeKey());
		if (print.selectTemplate() == 1)
			print.print();
	}
	protected void onBoToExcel() throws Exception {
		nc.ui.pub.print.IDataSource dataSource = new CardPanelPRTS(getBillUI()
				._getModuleCode(), getBillCardPanelWrapper().getBillCardPanel());
		nc.ui.pub.print.PrintEntry print = new nc.ui.pub.print.PrintEntry(null,
				dataSource);
		print.setTemplateID(getBillUI()._getCorp().getPrimaryKey(), getBillUI()
				._getModuleCode(), getBillUI()._getOperator(), getBillUI()
				.getBusinessType(), getBillUI().getNodeKey());
		if (print.selectTemplate() == 1)
			print.exportExcelFile();
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
				// 业务类型编码
				strWhere = "(" + strWhere + ") and "
						+ getBillField().getField_BusiCode() + "='"
						+ getBillUI().getBusicode() + "'";

			else
				// 业务类型
				strWhere = "(" + strWhere + ") and "
						+ getBillField().getField_Busitype() + "='"
						+ getBillUI().getBusinessType() + "'";

		}

		if (getHeadCondition() != null)
			strWhere = strWhere + " and " + getHeadCondition();
		// 现在我先直接把这个拼好的串放到StringBuffer中而不去优化拼串的过程
		sqlWhereBuf.append(strWhere);
		return true;
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
		String billcode = (String)getBufferData().getCurrentVO().getParentVO().getAttributeValue("vbillno");
		String pk_billtype =  getBillManageUI().getUIControl().getBillType();
		String pk_corp = _getCorp().getPrimaryKey();
		super.onBoDel();
		//
		returnBillNo(billcode,pk_billtype,pk_corp);
	}
	@Override
	public void onBillRef() throws Exception {
		// 参照前将表头表体的本币金额，原币金额，本币汇率的精度重置为8，防止参照后币种精度的丢失
		setMaxDigitBeforeRef();
		try {
			super.onBillRef();
		} catch (Exception e) {
			throw e;
		} finally {
			// 参照完成之后还原参照按钮tag
			ButtonObject btn = getButtonManager().getButton(IBillButton.Refbill);
			btn.setTag(String.valueOf(IBillButton.Refbill));
			if (PfUtilClient.isCloseOK()) {
				getBillUI().setBillOperate(IBillOperate.OP_REFADD);
			}
		}
	}
	/**
	 * 参照前将表头表体的本币金额，原币金额，本币汇率的精度重置为8，防止参照后币种精度的丢失
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

			// 更新金额精度
			getBillCardPanelWrapper().setCardDecimalDigits(headshowmaxdigitnums, bodyshowmaxdigitnums);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.debug("setMaxDigitBeforeRef:UI参照时初始化单据金额精度为8，出现异常." + e.getMessage());
		}

	}

}
