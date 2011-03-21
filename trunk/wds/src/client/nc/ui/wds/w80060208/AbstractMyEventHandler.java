package nc.ui.wds.w80060208;

import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wds.w80060208.cfButtun.ICfButtun;

/**
 *
 *������һ�������࣬��ҪĿ�������ɰ�ť�¼�����Ŀ��
 *@author author
 *@version tempProject version
 */

public class AbstractMyEventHandler extends ManageEventHandler {

	public AbstractMyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}


	protected void onBoElse(int intBtn) throws Exception {
		switch (intBtn) {

		case ICfButtun.cfzj:
			oncfzj();
			break;
		case ICfButtun.scpz:
			onscpz();
			break;

		}
	}
	protected void oncfzj() throws Exception {
	}
	protected void onscpz() throws Exception {
	}
}