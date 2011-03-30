package nc.ui.hg.pu.plan.year;

import nc.ui.hg.pu.pub.PlanPubClientUI;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.scm.pub.panel.RelationsCal;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.hg.pu.plan.year.PlanYearBVO;
import nc.vo.hg.pu.pub.HgPuBtnConst;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pu.VariableConst;
import nc.vo.trade.button.ButtonVO;

/**
 * 物资需求计划(支持自定义项)
 * 
 * @author zhw
 * 
 */
public class ClientUI extends PlanPubClientUI {

	public ClientUI() {
		super();
	}

	@Override
	public void setDefaultData() throws Exception {		
		 setHeadItemValue("pk_billtype", HgPubConst.PLAN_YEAR_BILLTYPE);
		UFDate billdate = _getDate();
		String year = PuPubVO
				.getString_TrimZeroLenAsNull(billdate.getYear() + 1);
		setHeadItemValue("cyear", year);// 年度赋值
		
		super.setDefaultData();
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
		return HYPubBO_Client.getBillNo(HgPubConst.PLAN_YEAR_BILLTYPE,
				_getCorp().getPrimaryKey(), null, null);
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		String key = e.getKey();
		if (e.getPos() == BillItem.BODY) {
			int bodyRow = e.getRow();
			if ("nmonnum1".equalsIgnoreCase(key)
					|| "nmonnum2".equalsIgnoreCase(key)
					|| "nmonnum3".equalsIgnoreCase(key)
					|| "nmonnum4".equalsIgnoreCase(key)
					|| "nmonnum5".equalsIgnoreCase(key)
					|| "nmonnum6".equalsIgnoreCase(key)
					|| "nmonnum7".equalsIgnoreCase(key)
					|| "nmonnum8".equalsIgnoreCase(key)
					|| "nmonnum9".equalsIgnoreCase(key)
					|| "nmonnum10".equalsIgnoreCase(key)
					|| "nmonnum11".equalsIgnoreCase(key)
					|| "nmonnum12".equalsIgnoreCase(key)) {
				// 12月份
				nmonnumAfterEdit(e, bodyRow);
			} 
		}
		super.afterEdit(e);
	}



	/**
	 * 12月份数量编辑后
	 * 
	 * @param e
	 * @param bodyRow
	 */
	private void nmonnumAfterEdit(BillEditEvent e, int bodyRow) {
		CircularlyAccessibleValueObject[] checkVO = getBillCardWrapper()
				.getSelectedBodyVOs();
		PlanYearBVO[] planBVO = (PlanYearBVO[]) checkVO;
		UFDouble sum = VariableConst.ZERO;
		for (int n = 0; n < 12; n++) {
			sum = sum.add(PuPubVO.getUFDouble_NullAsZero(planBVO[0]
					.getAttributeValue(HgPubConst.NMONTHNUM[n])));
		}
		getBillCardPanel().setBodyValueAt(sum, bodyRow, "nnetnum");
		getBillCardPanel().setBodyValueAt(sum, bodyRow, "nnum");
		UFBoolean isfixed = PuPubVO.getUFBoolean_NullAs(getBillCardPanel()
				.getBillModel().getValueAt(e.getRow(), "fixedflag"),
				UFBoolean.TRUE);
		if (isfixed.booleanValue())
			HgPubTool.m_saKey[0] = "Y";
		else
			HgPubTool.m_saKey[0] = "N";
		BillEditEvent ee = new BillEditEvent(BillItem.BODY, null, sum, "nnum",
				bodyRow, getBillCardPanel().getBillModel().getItemByKey("nnum")
						.getPos());
		RelationsCal.calculate(getBillCardPanel(), ee, getBillCardPanel()
				.getBillModel(), HgPubTool.m_iDescriptions, HgPubTool.m_saKey,
				PlanYearBVO.class.getName());
		getBillCardPanel().getBillModel().execEditFormulaByKey(bodyRow,
				"nnetnum");
	}
	
	@Override
	protected BusinessDelegator createBusinessDelegator() {
		return new ClientBusinessDelegator(this);
	}
	
	// 添加自定义按钮
	@Override
	public void initPrivateButton() {
		super.initPrivateButton();
		ButtonVO btnvo = new ButtonVO();
		btnvo.setBtnNo(HgPuBtnConst.IMPROT);
		btnvo.setBtnName("年计划导入");
		btnvo.setBtnChinaName("年计划导入");
		btnvo.setBtnCode(null);// code最好设置为空
		btnvo.setOperateStatus(new int[] { IBillOperate.OP_INIT,IBillOperate.OP_NO_ADDANDEDIT });
		addPrivateButton(btnvo);
	}
	
}
