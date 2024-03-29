package nc.ui.hg.pu.plan.temp;

import nc.ui.hg.pu.pub.PlanPubClientUI;
import nc.ui.hg.pu.pub.PlanPubEventHandler;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.hg.pu.plan.month.PlanOtherBVO;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.hg.pu.pub.PlanApplyInforVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.scm.pu.PuPubVO;

public class ClientEventHandler extends PlanPubEventHandler {

	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	@Override
	protected UIDialog createQueryUI() {
		return new ClientUIQueryDlg(getBillUI(), null, _getCorp()
				.getPrimaryKey(), getBillUI().getModuleCode(), _getOperator(),
				null, null);
	}

	protected String getHeadCondition() {
		// 修改字段
		String sql = " h.pk_corp = '" + _getCorp().getPrimaryKey()
				+ "'  and  h.pk_billtype = '" + HgPubConst.PLAN_TEMP_BILLTYPE
				+ "' ";

		PlanApplyInforVO appInfor = ((PlanPubClientUI) getBillUI()).m_appInfor;
		if (appInfor != null) {
			String dept = ((PlanPubClientUI) getBillUI()).m_appInfor// 过滤部门
					// 只能查询出自己部门的计划
					.getCapplydeptid();
			if (PuPubVO.getString_TrimZeroLenAsNull(dept) != null) {
				sql = sql + " and h.capplydeptid = '" + dept + "'";
			}

		}
		return sql;
	}

	@Override
	protected void beforeSave() throws Exception {
		// TODO Auto-generated method stub

		AggregatedValueObject checkVO = getBillUI().getVOFromUI();
		PlanOtherBVO[] items=(PlanOtherBVO[])checkVO.getChildrenVO();
		
		for(PlanOtherBVO item:items){
			item.validata();
		}
	}
	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		// TODO Auto-generated method stub
		super.onBoAdd(bo);
		getBillCardPanelWrapper().getBillCardPanel().getBodyTabbedPane().setEnabledAt(1, false);
	}
	
	@Override
	protected void onBoEdit() throws Exception {
		// TODO Auto-generated method stub
		if(getBufferData().isVOBufferEmpty()){
			return;
		}
		super.onBoEdit();
		getBillCardPanelWrapper().getBillCardPanel().getBodyTabbedPane().setEnabledAt(1, false);
	}
	
	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
		super.onBoSave();
		getBillCardPanelWrapper().getBillCardPanel().getBodyTabbedPane().setEnabledAt(1, true);
		onBoRefresh();
	}
	@Override
	protected void onBoCancel() throws Exception {
		// TODO Auto-generated method stub
		super.onBoCancel();
		getBillCardPanelWrapper().getBillCardPanel().getBodyTabbedPane().setEnabledAt(1, true);
	}
	public void onButton(ButtonObject bo) {
		setTabbedPane();
		super.onButton(bo);
	}
	//调整表体业签
	private void setTabbedPane(){
		if (getBillManageUI().isListPanelSelected()) {
			getBillListPanel().getBodyTabbedPane().setSelectedIndex(0);
		} else {
			getBillCardPanelWrapper().getBillCardPanel().getBodyTabbedPane().setSelectedIndex(0);
		}
	}
}
