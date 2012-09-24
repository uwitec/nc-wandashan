package nc.ui.wds.invbasdoc;

import nc.bs.logging.Logger;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.zmpub.pub.tool.LongTimeTask;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.NullFieldException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;

public class MyEventHandler extends ManageEventHandler {


	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected UIDialog createQueryUI() {
		
		return new MyQueryDIG(getBillUI(), null,_getCorp().getPrimaryKey(),getBillUI()._getModuleCode(),getBillUI()._getOperator(), null);
	}	
	@Override
	protected void onBoSave() throws Exception {
		try {
			dataNotNullValidate();
		} catch (ValidationException e) {
			MessageDialog.showErrorDlg(getBillUI(), "校验", e.getMessage());
			return;
		}
	    valuteIndex();
		super.onBoSave();
	}
	/**
	 * 按钮m_boDel点击时执行的动作,如有必要，请覆盖. 档案的删除处理
	 */
	protected void onBoDelete() throws Exception {
		// 界面没有数据或者有数据但是没有选中任何行
		if (getBufferData().getCurrentVO() == null)
			return;
		valuteIndex1();
		
	    super.onBoDelete();
	}

	
	/**
	 * 删除时 校验存货档案是否被引用
	 * @作者：zhf
	 * @说明：完达山物流项目 
	 * @时间：2012-9-22下午04:08:55
	 * @throws Exception
	 */
	private void valuteIndex1() throws Exception {

		AggregatedValueObject billVO = getBillUI().getChangedVOFromUI();
	    if(billVO==null || billVO.getParentVO()==null){
	    	return;
	    }	
			Class[] ParameterTypes = new Class[] { AggregatedValueObject.class };
			Object[] ParameterValues = new Object[] { billVO };
			Object o = LongTimeTask.calllongTimeService("wds", null,
					"正在查询...", 1, "nc.vo.wdsnew.pub.BaseDocValuteTool", null, "valuteBaseDocDelete",
					ParameterTypes, ParameterValues);
		
	
	
		
	}
	/**
	 * 校验修改数据是否被引用
	 * @throws Exception 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * @时间：2012-9-22下午03:50:56
	 */
	private void valuteIndex() throws Exception {
		AggregatedValueObject billVO = getBillUI().getChangedVOFromUI();
	    if(billVO==null || billVO.getParentVO()==null){
	    	return;
	    }	
			Class[] ParameterTypes = new Class[] { AggregatedValueObject.class };
			Object[] ParameterValues = new Object[] { billVO };
			Object o = LongTimeTask.calllongTimeService("wds", null,
					"正在查询...", 1, "nc.vo.wdsnew.pub.BaseDocValuteTool", null, "valuteBaseDocEdit",
					ParameterTypes, ParameterValues);
		
	}
	//必输项校验
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
	

}
