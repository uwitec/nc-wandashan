package nc.ui.wds.dm.wljsq;

import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wl.pub.WdsPubEnventHandler;
import nc.vo.pub.BusinessException;

/**
 * 
 * 该类是AbstractMyEventHandler抽象类的实现类， 主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
 * 
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends ManageEventHandler {

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
		Integer i= (Integer)  getBillCardPanelWrapper().getBillCardPanel().getHeadItem("datavale").getValueObject();
		if(i==null){
			throw new BusinessException("请选择值！");
		}
		int datavale=i.intValue();
		
	
		super.onBoSave();
	}
	@Override
	protected void onBoQuery() throws Exception {
		super.onBoQuery();

	}
	@Override
	protected void onBoCancel() throws Exception {
		// TODO Auto-generated method stub
		super.onBoCancel();
	}
	
}