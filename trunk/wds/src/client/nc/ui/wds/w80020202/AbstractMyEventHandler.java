package nc.ui.wds.w80020202;

import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wds.w80020202.buttun0202.ISsButtun;

/**
 * 
 * 该类是一个抽象类，主要目的是生成按钮事件处理的框架
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