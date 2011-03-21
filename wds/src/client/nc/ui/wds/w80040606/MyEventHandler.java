package nc.ui.wds.w80040606;

import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.pub.BusinessRuntimeException;

/**
 * 
 * 该类是AbstractMyEventHandler抽象类的实现类， 主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
 * 
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
		Object tate = getBillCardPanelWrapper().getBillCardPanel()
		.getHeadTailItem("ss_state").getValueObject();
		if(null==tate||"".equals(tate)){
			getBillUI().showErrorMessage("请填写状态");
			return ;
		}
		// 获取是否可出库状态状态
		Object temp = getBillCardPanelWrapper().getBillCardPanel()
				.getHeadTailItem("cchuku").getValueObject();
		if (null != temp && !"".equals(temp)) {
			getBillCardPanelWrapper().getBillCardPanel().setHeadItem(
					"ss_isout", temp);
		}
		super.onBoSave();
	}
}