package nc.ui.wds.dm.wljsq;

import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.wl.pub.WdsPubEnventHandler;

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

	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
	
		super.onBoSave();
	}
	@Override
	protected void onBoQuery() throws Exception {
		super.onBoQuery();

	}
	
}