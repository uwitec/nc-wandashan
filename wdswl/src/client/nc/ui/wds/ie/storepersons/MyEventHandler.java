package nc.ui.wds.ie.storepersons;

import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.wl.pub.GManageEventHandler;

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


//	@Override
//	protected String getHeadCondition() {
//		String where = " pk_corp = '"+_getCorp().getPrimaryKey()+"' and isnull(dr,0) = 0 ";
//		return where;
//	}

	@Override
	protected void onBoSave() throws Exception {
		//������:  -------
		//[�ֿ�,��λ,��Ա,��Ա����]
		 //��Ա�Ƿ��ظ����
	

		
		
		
		super.onBoSave();
	}
}