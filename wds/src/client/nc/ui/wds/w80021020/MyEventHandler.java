package nc.ui.wds.w80021020;

import nc.ui.pub.bill.BillItem;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.SuperVO;

/**
 * 
 * 该类是AbstractMyEventHandler抽象类的实现类， 主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
 * 
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	private MyClientUI myUI = null;

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	@Override
	protected void onBoEdit() throws Exception {
		// TODO Auto-generated method stub
		super.onBoEdit();

	}

	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
		super.onBoSave();
	}

}