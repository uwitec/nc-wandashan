package nc.ui.wds.ie.storepersons;

import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.wl.pub.GManageEventHandler;

/**
 * 仓库人员绑定
 */

public class MyEventHandler extends GManageEventHandler {

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}


   
   
	@Override
	protected UIDialog createQueryUI() {
		
		return new MyQueryDIG(getBillUI(), null,getBillUI()._getCorp().getPk_corp(), getBillUI().getModuleCode(),getBillUI()._getOperator(), null);
	}


//	@Override
//	protected String getHeadCondition() {
//		String where = " pk_corp = '"+_getCorp().getPrimaryKey()+"' and isnull(dr,0) = 0 ";
//		return where;
//	}

	@Override
	protected void onBoSave() throws Exception {
		//必输项:  -------
		//[仓库,货位,人员,人员类型]
		 //人员是否重复添加
	

		
		
		
		super.onBoSave();
	}
}