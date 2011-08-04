package nc.ui.hg.pu.plan.month;

import nc.ui.hg.pu.pub.PlanPubEventHandler;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.hg.pu.pub.HgPuBtnConst;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.hg.pu.pub.PlanVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;

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
		//修改字段
		return " h.pk_corp = '"+_getCorp().getPrimaryKey()+"'  and  h.pk_billtype = '"+HgPubConst.PLAN_MONTH_BILLTYPE+"' ";
	}
	
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		if (HgPuBtnConst.OPEN == intBtn) {// 打开
			onBoOpen();
		} else if (HgPuBtnConst.CLOSE == intBtn) {// 关闭
			onBoClose();
		} else {
			super.onBoElse(intBtn);
		}
	}

	public void onBoOpen() {
		PlanVO hvo = getPlanVO();
		if(hvo !=null){
			if (UFBoolean.FALSE.equals(hvo.getFisclose())) {
				getBillUI().showErrorMessage("操作异常，选中台帐已经被打开!");
				return;
			} else {
				hvo.setFisclose(UFBoolean.FALSE);
				hvo.setDr(0);
				updateVo(hvo);
			}
		}
		
	}

	public void onBoClose() {
		PlanVO hvo = getPlanVO();
		if( hvo != null){
			if (UFBoolean.TRUE.equals(hvo.getFisclose())) {
				getBillUI().showErrorMessage("操作异常，选中台帐已经是关闭!");
				return;
			} else {
				hvo.setFisclose(UFBoolean.TRUE);
				hvo.setDr(0);
				updateVo(hvo);
			}
		}
	}

	/**
	 * 获取界面的台帐VO
	 * @return 台帐VO
	 * @throws Exception 
	 */
	public PlanVO getPlanVO() {
		CircularlyAccessibleValueObject vo = null;
		if (!getBillManageUI().isListPanelSelected()) {// 卡片显示
			vo = getBillCardPanelWrapper().getBillCardPanel().getBillData().getHeaderValueVO(PlanVO.class.getName());			
		} else {
			int row = getBillListPanel().getHeadTable().getSelectedRow();
			if (row < 0) {
				getBillUI().showErrorMessage("操作异常，请选中要操作的数据行");
				return null;
			}
			vo = getBillListPanel().getHeadBillModel().getBodyValueRowVO(row,
					PlanVO.class.getName());
		}
		if (vo == null) {
			getBillUI().showErrorMessage("获取界面数据异常");
			return null;
		}
		PlanVO hvo = (PlanVO) vo;
		return hvo;
	}

	/**
	 * 更新数据库操作
	 * @param hvo 台帐VO
	 */
	public void updateVo(PlanVO hvo) {
		try {
			HYPubBO_Client.update(hvo);
			onBoRefresh();
		} catch (Exception e) {
			MessageDialog.showErrorDlg(getBillUI(), "数据更新出错", e.getMessage());
		}
	}

	@Override
	protected void onBoRefresh() throws Exception {
		super.onBoRefresh();
	}

	@Override
	protected void beforeSave() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
}
