package nc.ui.wds.w80021004;

import nc.ui.trade.businessaction.BdBusinessAction;
import nc.ui.trade.businessaction.BusinessAction;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.businessaction.IBusinessController;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.wds.w80021004.MyBillVO;
import nc.vo.wds.w80021004.TbWorkteamVO;

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
	protected IBusinessController createBusinessAction() {
		// TODO Auto-generated method stub
		switch (getUIController().getBusinessActionType()) {
		case IBusinessActionType.PLATFORM:
			return new BusinessAction(getBillUI());
		case IBusinessActionType.BD:
			return new BdBusinessAction(getBillUI());
		default:
			return new BusinessAction(getBillUI());
		}
	}

	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
		MyBillVO billvo = (MyBillVO) getBillCardPanelWrapper()
				.getChangedVOFromUI();
		TbWorkteamVO wvo = (TbWorkteamVO) billvo.getParentVO();
		if (null != wvo) {
			wvo.validate();
		}

		// 获得组长手机
		String wt_leadermobile = (String) getBillCardPanelWrapper()
				.getBillCardPanel().getHeadItem("wt_leadermobile").getValue();
		// 对手机进行验证，判断时候合法
		if (null != wt_leadermobile && "".equals(wt_leadermobile)) {
			if (wt_leadermobile.matches("\\d+")) {
				getBillUI().showErrorMessage("手机号应为数字，请输入数字！");
				return;
			}
		}
		super.onBoSave();
	}

}