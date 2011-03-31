package nc.ui.wds.ic.other.out;

import nc.ui.pub.ButtonObject;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wds.w8004040204.ssButtun.ISsButtun;

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

		case ISsButtun.zj:
			onzj();
			break;
		case ISsButtun.zk:
			onzk();
			break;
		case ISsButtun.cgqy:
			oncgqy();
			break;
		case ISsButtun.zzdj:
			onzzdj(null);
			break;
		case ISsButtun.fzgn:
			onfzgn();
			break;
		case ISsButtun.tpzd:
			ontpzd();
			break;
		case ISsButtun.zdqh:
			onzdqh();
			break;
		case ISsButtun.ckmx:
			onckmx();
			break;
		case nc.ui.wds.w80060206.buttun0206.ISsButtun.Qxqz:
			onQxqz();
			break;
		case nc.ui.wds.w80060206.buttun0206.ISsButtun.Qzqr:
			onQzqr();
			break;
		}

	}

	protected void onfzgn() throws Exception {
	}

	protected void ontpzd() throws Exception {
	}

	protected void onzdqh() throws Exception {
	}

	protected void onckmx() throws Exception {
	}

	protected void onzj() throws Exception {
	}

	protected void onzk() throws Exception {
	}

	protected void oncgqy() throws Exception {
	}

	protected void onzzdj(ButtonObject bo) throws Exception {
	}

	protected void onQxqz() throws Exception {

	}

	protected void onQzqr() throws Exception {

	}
}