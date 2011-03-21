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
 * ������AbstractMyEventHandler�������ʵ���࣬ ��Ҫ�������˰�ť��ִ�ж������û����Զ���Щ����������Ҫ�����޸�
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

		// ����鳤�ֻ�
		String wt_leadermobile = (String) getBillCardPanelWrapper()
				.getBillCardPanel().getHeadItem("wt_leadermobile").getValue();
		// ���ֻ�������֤���ж�ʱ��Ϸ�
		if (null != wt_leadermobile && "".equals(wt_leadermobile)) {
			if (wt_leadermobile.matches("\\d+")) {
				getBillUI().showErrorMessage("�ֻ���ӦΪ���֣����������֣�");
				return;
			}
		}
		super.onBoSave();
	}

}