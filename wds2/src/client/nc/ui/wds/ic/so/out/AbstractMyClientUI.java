package nc.ui.wds.ic.so.out;

import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bill.BillTemplateWrapper;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.wds.w80020206.buttun0206.QxqzBtn;
import nc.ui.wds.w80020206.buttun0206.QzqrBtn;
import nc.ui.wds.w8004040204.ssButtun.ckmxBtn;
import nc.ui.wds.w8004040204.ssButtun.fzgnBtn;
import nc.ui.wds.w8004040204.ssButtun.tpzdBtn;
import nc.ui.wds.w8004040204.ssButtun.zdqhBtn;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.button.ButtonVO;

/**
 * <b> 在此处简要描述此类的功能 </b>
 * 
 * <p>
 * 在此处添加此类的描述信息
 * </p>
 * 
 * 
 * @author author
 * @version tempProject version
 */
public abstract class AbstractMyClientUI extends
		nc.ui.trade.manage.BillManageUI implements ILinkQuery {

	protected AbstractManageController createController() {
		return new MyClientUICtrl();
	}

	/**
	 * 如果单据不走平台时，UI类需要重载此方法，返回不走平台的业务代理类
	 * 
	 * @return BusinessDelegator 不走平台的业务代理类
	 */
	protected BusinessDelegator createBusinessDelegator() {
		return new nc.ui.wds.ic.so.out.MyDelegator();
	}

	/**
	 * 注册自定义按钮
	 */
	protected void initPrivateButton() {
		fzgnBtn customizeButton1 = new fzgnBtn();
		addPrivateButton(customizeButton1.getButtonVO());
		tpzdBtn customizeButton2 = new tpzdBtn();
		addPrivateButton(customizeButton2.getButtonVO());
		zdqhBtn customizeButton3 = new zdqhBtn();
		addPrivateButton(customizeButton3.getButtonVO());
		ckmxBtn customizeButton4 = new ckmxBtn();
		addPrivateButton(customizeButton4.getButtonVO());

		QxqzBtn customizeButton5 = new QxqzBtn();
		addPrivateButton(customizeButton5.getButtonVO());
		QzqrBtn customizeButton6 = new QzqrBtn();
		addPrivateButton(customizeButton6.getButtonVO());

		int[] listButns = getUIControl().getListButtonAry();
		boolean hasCommit = false;
		boolean hasAudit = false;
		boolean hasCancelAudit = false;
		for (int i = 0; i < listButns.length; i++) {
			if (listButns[i] == nc.ui.trade.button.IBillButton.Commit)
				hasCommit = true;
			if (listButns[i] == nc.ui.trade.button.IBillButton.Audit)
				hasAudit = true;
			if (listButns[i] == nc.ui.trade.button.IBillButton.CancelAudit)
				hasCancelAudit = true;
		}
		int[] cardButns = getUIControl().getCardButtonAry();
		for (int i = 0; i < cardButns.length; i++) {
			if (cardButns[i] == nc.ui.trade.button.IBillButton.Commit)
				hasCommit = true;
			if (cardButns[i] == nc.ui.trade.button.IBillButton.Audit)
				hasAudit = true;
			if (cardButns[i] == nc.ui.trade.button.IBillButton.CancelAudit)
				hasCancelAudit = true;
		}
		if (hasCommit) {
			ButtonVO btnVo = nc.ui.trade.button.ButtonVOFactory.getInstance()
					.build(nc.ui.trade.button.IBillButton.Commit);
			btnVo.setBtnCode(null);
			addPrivateButton(btnVo);
		}

		if (hasAudit) {
			ButtonVO btnVo2 = nc.ui.trade.button.ButtonVOFactory.getInstance()
					.build(nc.ui.trade.button.IBillButton.Audit);
			btnVo2.setBtnCode(null);
			addPrivateButton(btnVo2);
		}

		if (hasCancelAudit) {
			ButtonVO btnVo3 = nc.ui.trade.button.ButtonVOFactory.getInstance()
					.build(nc.ui.trade.button.IBillButton.CancelAudit);
			btnVo3.setBtnCode(null);
			addPrivateButton(btnVo3);
		}
	}

	/**
	 * 注册前台校验类
	 */
	public Object getUserObject() {
		return new nc.ui.wds.ic.so.out.check.MyClientUICheckRuleGetter();
	}

	public void doQueryAction(ILinkQueryData querydata) {
		String billId = querydata.getBillID();
		if (billId != null) {
			try {
				setCurrentPanel(BillTemplateWrapper.CARDPANEL);
				AggregatedValueObject vo = loadHeadData(billId);
				getBufferData().addVOToBuffer(vo);
				setListHeadData(new CircularlyAccessibleValueObject[] { vo
						.getParentVO() });
				getBufferData().setCurrentRow(getBufferData().getCurrentRow());
				setBillOperate(IBillOperate.OP_NO_ADDANDEDIT);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
