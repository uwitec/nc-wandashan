package nc.ui.wds.w80020202;

import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wds.w80020202.buttun0202.ISsButtun;

/**
 * 
 * ������һ�������࣬��ҪĿ�������ɰ�ť�¼�����Ŀ��
 * 
 * @author author
 * @version tempProject version
 */

public class AbstractMyEventHandler extends ManageEventHandler {

	public AbstractMyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	protected void onBoElse(int intBtn) throws Exception {
		switch (intBtn) {

		case ISsButtun.Zxfzj:
			onZxfzj();
			break;
		}
	}

	protected void onZxfzj() throws Exception {
	}

}