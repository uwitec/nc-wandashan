package nc.ui.wds.w80040606;

import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.pub.BusinessRuntimeException;

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
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
		Object tate = getBillCardPanelWrapper().getBillCardPanel()
		.getHeadTailItem("ss_state").getValueObject();
		if(null==tate||"".equals(tate)){
			getBillUI().showErrorMessage("����д״̬");
			return ;
		}
		// ��ȡ�Ƿ�ɳ���״̬״̬
		Object temp = getBillCardPanelWrapper().getBillCardPanel()
				.getHeadTailItem("cchuku").getValueObject();
		if (null != temp && !"".equals(temp)) {
			getBillCardPanelWrapper().getBillCardPanel().setHeadItem(
					"ss_isout", temp);
		}
		super.onBoSave();
	}
}