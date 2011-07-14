package nc.ui.wds.ic.inv;

import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.pub.SuperVO;
import nc.vo.scm.pu.PuPubVO;


/**
 * 
 * ���״̬
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	MyClientUI myClientUI = null;
	


	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
		myClientUI = (MyClientUI) billUI;
	}



	@Override
	protected UIDialog createQueryUI() {
		
		return new MyQueryDIG(getBillUI() , null,getBillUI()._getCorp().getPrimaryKey(), getBillUI()._getModuleCode(),getBillUI()._getOperator(),null);
	}
	
	@Override
	protected void onBoSave() throws Exception {
		super.onBoSave();
		doRefresh();
	}
	protected void doRefresh() throws Exception {
		if(PuPubVO.getString_TrimZeroLenAsNull(whereSql)==null)
			getBillUI().showHintMessage("ˢ�²�������");
		int row = getBufferData().getCurrentRow();
		SuperVO[] queryVos = queryHeadVOs(whereSql);

		getBufferData().clear();
		// �������ݵ�Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
		if(((BillManageUI)getBillUI()).isListPanelSelected())
			return;
		onBoReturn();
		if(row<getBufferData().getVOBufferSize()){
			((BillManageUI)getBillUI()).getBillListPanel().getHeadTable().getSelectionModel().setSelectionInterval(row, row);
		}
	}
	private String whereSql;
	protected void onBoQuery() throws Exception {

		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// �û������˲�ѯ

		whereSql = strWhere.toString();
		SuperVO[] queryVos = queryHeadVOs(whereSql);

		getBufferData().clear();
		// �������ݵ�Buffer
		addDataToBuffer(queryVos);

		updateBuffer();

	}
}