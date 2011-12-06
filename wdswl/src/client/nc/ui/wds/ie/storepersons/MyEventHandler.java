package nc.ui.wds.ie.storepersons;

import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.wl.pub.GManageEventHandler;
import nc.ui.wl.pub.LoginInforHelper;

/**
 * �ֿ���Ա��
 */

public class MyEventHandler extends GManageEventHandler {

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}
	@Override
	protected UIDialog createQueryUI() {
		return new MyQueryDIG(getBillUI(), null,getBillUI()._getCorp().getPk_corp(), getBillUI().getModuleCode(),getBillUI()._getOperator(), null);
	}

	@Override
	protected void onBoQuery() throws Exception {
		LoginInforHelper login = new LoginInforHelper();
		String pk_storedoc = login.getCwhid(_getOperator());
		if(pk_storedoc == null || "".equalsIgnoreCase(pk_storedoc)){
			getBillUI().showWarningMessage("��ǰ��¼��Աû�а󶨲ֿ�");
			return ;
		}
		super.onBoQuery();
	}
	@Override
	protected void onBoSave() throws Exception {
		
		super.onBoSave();
	}

}