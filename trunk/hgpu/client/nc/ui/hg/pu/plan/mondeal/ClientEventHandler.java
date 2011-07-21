package nc.ui.hg.pu.plan.mondeal;

import nc.ui.hg.pu.pub.FlowManageEventHandler;
import nc.ui.hg.pu.pub.LongTimeTask;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.query.INormalQuery;
import nc.vo.hg.pu.plan.mondeal.MonUpdateBVO;
import nc.vo.hg.pu.pub.HgPuBtnConst;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.hg.pu.pub.PlanVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.HYBillVO;

public class ClientEventHandler extends FlowManageEventHandler {

	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	UIDialog m_ClientUIQueryDlg = null;

	@Override
	protected UIDialog createQueryUI() {
		return new ClientUIQueryDlg(getBillUI(), null, _getCorp()
				.getPrimaryKey(), getBillUI().getModuleCode(), _getOperator(),
				null, null);
	}

	protected UIDialog getQueryDlg() {
		if (m_ClientUIQueryDlg == null) {
			m_ClientUIQueryDlg = new ClientUIQueryDlg(getBillUI(), null,
					_getCorp().getPrimaryKey(), getBillUI().getModuleCode(),
					_getOperator(), null, HgPubConst.PLAN_MONDEAL_NODEKEY);
		}
		return m_ClientUIQueryDlg;

	}

	protected String getHeadCondition() {
		// 修改字段
		return " h.pk_corp = '" + _getCorp().getPrimaryKey()
				+ "'  and  h.pk_billtype = '"
				+ HgPubConst.PLAN_MONDEAL_BILLTYPE + "' ";
	}

	@Override
	protected void onBoElse(int intBtn) throws Exception {
		if (HgPuBtnConst.LOAD == intBtn) {// 加载
			onBoLoad();
		} else if (intBtn == HgPuBtnConst.Editor) {// 修改
			onBoEdit();
		} else {
			super.onBoElse(intBtn);
		}
	}

	protected void onBoLoad() throws Exception {

		StringBuffer strWhere = new StringBuffer();
		if (QueryCondition(strWhere) == false)
			return;// 用户放弃了查询
		strWhere.append(" and h.pk_corp = '");
		strWhere.append(_getCorp().getPrimaryKey());
		strWhere.append("'");
		Class[] ParameterTypes = new Class[] { String.class };
		Object[] ParameterValues = new Object[] { strWhere.toString() };
		Object o = LongTimeTask.callRemoteService("pu",
				"nc.bs.hg.pu.mondeal.MonDealBo", "loadYearPlanData",
				ParameterTypes, ParameterValues, 2);
		if (o == null) {
			throw new BusinessException("加载不到数据！");
		}

		HYBillVO billvo = (HYBillVO) o;
		PlanVO head = (PlanVO) billvo.getParentVO();
		head.setVoperatorid(_getOperator());
		head.setDmakedate(_getDate());
		head.setDbilldate(_getDate());
		MonUpdateBVO[] bvo = (MonUpdateBVO[]) billvo.getChildrenVO();
		if (bvo[0].getPk_monudate_b() == null) {
			onBoCard();
			getBillUI().setBillOperate(IBillOperate.OP_ADD);
			getBillCardPanelWrapper().getBillCardPanel().setBillValueVO(billvo);
			getBillCardPanelWrapper().getBillCardPanel().getBillModel()
					.execLoadFormulaByKey("invcode");
		} else {
			getBufferData().clear();
			addDataToBuffer(new SuperVO[] { (SuperVO) billvo.getParentVO() });
			updateBuffer();
			onBoEdit();
		}
		// setEnable(bvo[0]);
	}

	protected boolean QueryCondition(StringBuffer sqlWhereBuf) throws Exception {
		if (sqlWhereBuf == null)
			throw new IllegalArgumentException(
					"askForQueryCondition().sqlWhereBuf cann't be null");
		UIDialog querydialog = getQueryDlg();

		if (querydialog.showModal() != UIDialog.ID_OK)
			return false;
		INormalQuery query = (INormalQuery) querydialog;

		String strWhere = query.getWhereSql();
		if (strWhere == null || strWhere.trim().length() == 0)
			strWhere = "1=1";

		// 现在我先直接把这个拼好的串放到StringBuffer中而不去优化拼串的过程
		sqlWhereBuf.append(strWhere);
		return true;
	}

	protected void onBoEdit() throws Exception {
		if (getBufferData().isVOBufferEmpty()) {
			return;
		}
		if (getBufferData().getCurrentRow() < 0) {
			getBillUI().showWarningMessage("请选择要修改的单据");
			return;
		}
		super.onBoEdit();
		getButtonManager().getButton(IBillButton.Line).setEnabled(false);
		getBillUI().updateButton(getButtonManager().getButton(IBillButton.Line));
	}

	public void onBoSave() throws Exception {
		beforeSaveCheck();
		super.onBoSave();
	}

	public void beforeSaveCheck() throws Exception {
		AggregatedValueObject checkVO = getBillUI().getVOFromUI();
		MonUpdateBVO[] items = (MonUpdateBVO[]) checkVO.getChildrenVO();

		for (MonUpdateBVO item : items) {
			item.validata();
		}
	}
}
