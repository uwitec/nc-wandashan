package nc.ui.hg.pu.plan.month;

import nc.ui.hg.pu.pub.CHManageEventHandler;
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

public class ClientEventHandler extends CHManageEventHandler {

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
		//�޸��ֶ�
		return " h.pk_corp = '"+_getCorp().getPrimaryKey()+"'  and  h.pk_billtype = '"+HgPubConst.PLAN_MONTH_BILLTYPE+"' ";
	}
	
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		if (HgPuBtnConst.OPEN == intBtn) {// ��
			onBoOpen();
		} else if (HgPuBtnConst.CLOSE == intBtn) {// �ر�
			onBoClose();
		} else {
			super.onBoElse(intBtn);
		}
	}

	public void onBoOpen() {
		PlanVO hvo = getPlanVO();
		if(hvo !=null){
			if (UFBoolean.FALSE.equals(hvo.getFisclose())) {
				getBillUI().showErrorMessage("�����쳣��ѡ��̨���Ѿ�����!");
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
				getBillUI().showErrorMessage("�����쳣��ѡ��̨���Ѿ��ǹر�!");
				return;
			} else {
				hvo.setFisclose(UFBoolean.TRUE);
				hvo.setDr(0);
				updateVo(hvo);
			}
		}
	}

	/**
	 * ��ȡ�����̨��VO
	 * @return ̨��VO
	 * @throws Exception 
	 */
	public PlanVO getPlanVO() {
		CircularlyAccessibleValueObject vo = null;
		if (!getBillManageUI().isListPanelSelected()) {// ��Ƭ��ʾ
			vo = getBillCardPanelWrapper().getBillCardPanel().getBillData().getHeaderValueVO(PlanVO.class.getName());			
		} else {
			int row = getBillListPanel().getHeadTable().getSelectedRow();
			if (row < 0) {
				getBillUI().showErrorMessage("�����쳣����ѡ��Ҫ������������");
				return null;
			}
			vo = getBillListPanel().getHeadBillModel().getBodyValueRowVO(row,
					PlanVO.class.getName());
		}
		if (vo == null) {
			getBillUI().showErrorMessage("��ȡ���������쳣");
			return null;
		}
		PlanVO hvo = (PlanVO) vo;
		return hvo;
	}

	/**
	 * �������ݿ����
	 * @param hvo ̨��VO
	 */
	public void updateVo(PlanVO hvo) {
		try {
			HYPubBO_Client.update(hvo);
			onBoRefresh();
		} catch (Exception e) {
			MessageDialog.showErrorDlg(getBillUI(), "���ݸ��³���", e.getMessage());
		}
	}

	@Override
	protected void onBoRefresh() throws Exception {
		super.onBoRefresh();
	}
	
}