package nc.ui.wds.w8004040210;

import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wds.w8004040210.ssButtun.ISsButtun;

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

		case ISsButtun.SsAdd:
			onSsAdd();
			break;
		case ISsButtun.sp:
			onsp();
			break;
		case ISsButtun.pd:
			onpd();
			break;
		case ISsButtun.CtAdd:
			onCtAdd();
			break;
		case ISsButtun.Td:
			onTd();
			break;
		case ISsButtun.Ai:
			onAi();
			break;
		case ISsButtun.Vd:
			onVd();
			break;
		case nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz:
			onQxqz();
			break;
		case nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr:
			onQzqr();
			break;

		}
	}
	
	protected void onQxqz() throws Exception {

	}

	protected void onQzqr() throws Exception {

	}

	protected void onSsAdd() {
	}

	protected void onsp() {
	}

	protected void onpd() {
	}

	protected void onCtAdd() {
	}

	protected void onTd() throws Exception {
	}

	protected void onAi() throws Exception {
	}

	protected void onVd() throws Exception {
	}

}