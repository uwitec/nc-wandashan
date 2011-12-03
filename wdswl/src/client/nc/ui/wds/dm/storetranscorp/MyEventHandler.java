package nc.ui.wds.dm.storetranscorp;

import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.query.INormalQuery;
import nc.ui.wl.pub.WdsPubEnventHandler;
import nc.vo.wl.pub.WdsWlPubTool;

/**
 * 
 * 该类是AbstractMyEventHandler抽象类的实现类， 主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
 * 
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends WdsPubEnventHandler {

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	@Override
	protected UIDialog createQueryUI() {
		// TODO Auto-generated method stub
		return new MyQueryDIG(
				getBillUI(), null, 
				
				_getCorp().getPk_corp(), getBillUI().getModuleCode()
				
				, getBillUI()._getOperator(), null		
		);
	}

//	@Override
//	protected void onBoQuery() throws Exception {
//		// TODO Auto-generated method stub
//		StringBuffer strWhere = new StringBuffer();
//
//		if (askForQueryCondition(strWhere) == false)
//			return;// 用户放弃了查询
//		strWhere.append(" and def1='1' ");
//		SuperVO[] queryVos = queryHeadVOs(strWhere.toString());
//		getBufferData().clear();
//		// 增加数据到Buffer
//		addDataToBuffer(queryVos);
//		updateBuffer();
//	}
	@Override
	protected String getHeadCondition() {
		String sql =super.getHeadCondition();
		if(sql == null){
			sql = sql+"  and def1='1' ";
		}else{
			return " def1='1'";
		}
		return sql;
	}
	
	@Override
	protected void onBoSave() throws Exception {
		WdsWlPubTool.beforeSaveBodyUnique(getBillCardPanelWrapper().getBillCardPanel().getBillTable(), 
				getBillCardPanelWrapper().getBillCardPanel().getBillModel(), new String[]{"pk_wds_tanscorp_h","careaid"}, new String[]{"承运商","收货地区"});
		super.onBoSave();
		onBoRefresh();
	}

	/**
	 * @author yf
	 * 表体pk_corp字段赋值
	 */
	@Override
	protected void onBoLineAdd() throws Exception {
		super.onBoLineAdd();
		int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
		if (row == -1)//
			row = getBillCardPanelWrapper().getBillCardPanel()
					.getBillModel().getRowCount() - 1;
		if (row < 0)
			throw new RuntimeException("cann't get selected row");
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getCorp().getPrimaryKey(), row, "pk_corp");
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
		if (strWhere == null)
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

		strWhere = "(" + strWhere + ")";

		if (getHeadCondition() != null)
			strWhere = strWhere + " and " + getHeadCondition();
		// 现在我先直接把这个拼好的串放到StringBuffer中而不去优化拼串的过程
		sqlWhereBuf.append(strWhere);
		return true;
	}

}