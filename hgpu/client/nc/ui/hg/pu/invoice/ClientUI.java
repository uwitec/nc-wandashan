package nc.ui.hg.pu.invoice;

import nc.ui.hg.pu.pub.DefBillManageUI;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBillStatus;

/**
 * 报账单
 * 
 * @author zhw
 * 
 */
public class ClientUI extends DefBillManageUI {
	
	public ClientUI() {
		super();
	}

	@Override
	protected void initSelfData() {
	}

	// 添加自定义按钮
	public void initPrivateButton() {
		super.initPrivateButton();
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
	protected String getBillNo() throws Exception {
		return HYPubBO_Client.getBillNo(HgPubConst.PLAN_BAOZHANG_BILLTYPE,
				_getCorp().getPrimaryKey(), null, null);
	}

	@Override
	protected BusinessDelegator createBusinessDelegator() {
		return new ClientBusinessDelegator(this);
	}

	@Override
	public boolean beforeEdit(BillEditEvent e) {
		return true;
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
	}

	/**
	 * 保存即提交
	 */
//	@Override
//	public boolean isSaveAndCommitTogether() {
//		return true;
//	}
	
	@Override
	public void setDefaultData() throws Exception {
		setHeadItemValue("vbillstatus", IBillStatus.FREE);// 单据状态
		setTailItemValue("voperatorid", _getOperator());// 在表尾了
		setHeadItemValue("dbilldate", _getDate());
		setHeadItemValue("pk_corp", _getCorp().getPrimaryKey());
		setTailItemValue("dmakedate", _getDate());
		 setHeadItemValue("pk_billtype", HgPubConst.PLAN_BAOZHANG_BILLTYPE);

	}

	protected void setHeadItemValue(String item, Object value) {
		getBillCardPanel().setHeadItem(item, value);
	}

	protected void setTailItemValue(String item, Object value) {
		getBillCardPanel().setTailItem(item, value);
	}

	@Override   
	public String getRefBillType() {      
		return "25";  
		}
}
