package nc.ui.hg.pu.plan.mny;

import nc.ui.hg.pu.pub.PlanPubClientUI;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.hg.pu.pub.HgPubConst;

/**
 * ר���ʽ�ƻ�(֧���Զ�����)
 * @author zhw
 * 
 */
public class ClientUI extends PlanPubClientUI {

	public ClientUI() {
		super();
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
	protected String getBillNo() throws Exception {
		return HYPubBO_Client.getBillNo(HgPubConst.PLAN_MNY_BILLTYPE,
				_getCorp().getPrimaryKey(), null, null);
	}

	@Override
	protected BusinessDelegator createBusinessDelegator() {
		return new ClientBusinessDelegator(this);
	}
	@Override
	public void setDefaultData() throws Exception {		
		 setHeadItemValue("pk_billtype", HgPubConst.PLAN_MNY_BILLTYPE);
		
		super.setDefaultData();
	}
}
