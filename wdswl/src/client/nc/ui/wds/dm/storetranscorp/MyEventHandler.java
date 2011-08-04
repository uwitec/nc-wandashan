package nc.ui.wds.dm.storetranscorp;

import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.wl.pub.WdsPubEnventHandler;
import nc.vo.wl.pub.WdsWlPubTool;

/**
 * 
 * ������AbstractMyEventHandler�������ʵ���࣬ ��Ҫ�������˰�ť��ִ�ж������û����Զ���Щ����������Ҫ�����޸�
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
//			return;// �û������˲�ѯ
//		strWhere.append(" and def1='1' ");
//		SuperVO[] queryVos = queryHeadVOs(strWhere.toString());
//		getBufferData().clear();
//		// �������ݵ�Buffer
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
				getBillCardPanelWrapper().getBillCardPanel().getBillModel(), new String[]{"pk_wds_tanscorp_h","careaid"}, new String[]{"������","�ջ�����"});
		super.onBoSave();
		onBoRefresh();
	}

	/**
	 * @author yf
	 * ����pk_corp�ֶθ�ֵ
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
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getCorp(), row, "pk_corp");
	}

}