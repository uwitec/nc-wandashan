package nc.ui.wds.ic.storestate;

import nc.ui.pub.beans.UIDialog;
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
		Object tate = getBillCardPanelWrapper().getBillCardPanel()
		.getHeadTailItem("ss_state").getValueObject();
		if(null==tate||"".equals(tate)){
			getBillUI().showErrorMessage("����д״̬");
			return ;
		}
		// ��ȡ�Ƿ�ɳ���״̬״̬
		Object temp = getBillCardPanelWrapper().getBillCardPanel()
				.getHeadTailItem("ss_isout").getValueObject();
		if (null != temp && !"".equals(temp)) {
			getBillCardPanelWrapper().getBillCardPanel().setHeadItem(
					"ss_isout", temp);
		}
		super.onBoSave();
	}
}