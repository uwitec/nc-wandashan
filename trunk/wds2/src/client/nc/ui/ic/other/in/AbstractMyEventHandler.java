package nc.ui.ic.other.in;

import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wds.w8004040214.buttun0214.ISsButtun;

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

		case ISsButtun.Fzgn:
			onFzgn();
			break;
		case ISsButtun.Zdtp:
			onZdtp();
			break;
		case ISsButtun.Ckmx:
			onCkmx();
			break;
		case ISsButtun.Zdrk:
			onZdrk();
			break;
		case ISsButtun.Zj:
			onZj();
			break;
		case ISsButtun.Zzdj:
			onZzdj();
			break;
		case ISsButtun.Fydj:
			onFydj();
			break;
		case ISsButtun.Hwtz:
			onHwtz();
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

	protected void onFzgn() throws Exception {
	}

	protected void onZdtp() throws Exception {
	}

	protected void onCkmx() throws Exception {
	}

	protected void onZdrk() throws Exception {
	}

	protected void onZj() throws Exception {
	}

	protected void onZzdj() throws Exception {
	}

	protected void onFydj() throws Exception {
	}

	protected void onHwtz() throws Exception {

	}
}