package nc.ui.hg.pu.plan.month;

import nc.ui.hg.pu.pub.DefBillManageUI;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.scm.pub.panel.RelationsCal;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.hg.pu.pub.HgPuBtnConst;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.button.ButtonVO;
/**
 * 月份领用计划
 * @author zhw
 *
 */
public class ClientUI extends DefBillManageUI  {
	
	public ClientUI() {
		super();
	}
	@Override
	protected void initSelfData() {

	}

	@Override
	public void setDefaultData() throws Exception {
	}

	// 添加自定义按钮
	public void initPrivateButton() {
		//打开
		ButtonVO btnvo1 = new ButtonVO();
		btnvo1.setBtnNo(HgPuBtnConst.OPEN);
		btnvo1.setBtnName("打开");
		btnvo1.setBtnChinaName("打开");
		btnvo1.setBtnCode(null);// code最好设置为空
		btnvo1.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT,
				IBillOperate.OP_NOADD_NOTEDIT });
		addPrivateButton(btnvo1);
		//关闭
		ButtonVO btnvo2 = new ButtonVO();
		btnvo2.setBtnNo(HgPuBtnConst.CLOSE);
		btnvo2.setBtnName("关闭");
		btnvo1.setBtnChinaName("关闭");
		btnvo2.setBtnCode(null);// code最好设置为空
		btnvo2.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT,
				IBillOperate.OP_NOADD_NOTEDIT });
		addPrivateButton(btnvo2);

	}

	@Override
	protected ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this, getUIControl());
	}

	@Override
	protected AbstractManageController createController() {
		return new ClientController();
	}

	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
	throws Exception {

	}

	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
	}

	@Override
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
	throws Exception {

	}

	@Override
	protected BusinessDelegator createBusinessDelegator() {
		return new ClientBusinessDelegator(this);
	}

	@Override
	public boolean beforeEdit(BillEditEvent e) {
		return super.beforeEdit(e);	
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
	}
	
}
