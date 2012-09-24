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
			MessageDialog.showErrorDlg(getBillUI(), "У��", e.getMessage());
			return;
		}
	    valuteIndex();
		super.onBoSave();
	}
	/**
	 * ��ťm_boDel���ʱִ�еĶ���,���б�Ҫ���븲��. ������ɾ������
	 */
	protected void onBoDelete() throws Exception {
		// ����û�����ݻ��������ݵ���û��ѡ���κ���
		if (getBufferData().getCurrentVO() == null)
			return;
		valuteIndex1();
		
	    super.onBoDelete();
	}

	
	/**
	 * ɾ��ʱ У���������Ƿ�����
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2012-9-22����04:08:55
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
					"���ڲ�ѯ...", 1, "nc.vo.wdsnew.pub.BaseDocValuteTool", null, "valuteBaseDocDelete",
					ParameterTypes, ParameterValues);
		
	
	
		
	}
	/**
	 * У���޸������Ƿ�����
	 * @throws Exception 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2012-9-22����03:50:56
	 */
	private void valuteIndex() throws Exception {
		AggregatedValueObject billVO = getBillUI().getChangedVOFromUI();
	    if(billVO==null || billVO.getParentVO()==null){
	    	return;
	    }	
			Class[] ParameterTypes = new Class[] { AggregatedValueObject.class };
			Object[] ParameterValues = new Object[] { billVO };
			Object o = LongTimeTask.calllongTimeService("wds", null,
					"���ڲ�ѯ...", 1, "nc.vo.wdsnew.pub.BaseDocValuteTool", null, "valuteBaseDocEdit",
					ParameterTypes, ParameterValues);
		
	}
	//������У��
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
	

}
