package nc.ui.wds.ic.write.back4c;

import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.wl.pub.WdsPubEnventHandler;
import nc.vo.wl.pub.WdsWlPubConst;

public class ClientEventHandler extends WdsPubEnventHandler {

	public ClientUIQueryDlg queryDialog = null;

	public ClientEventHandler(ClientUI clientUI, IControllerBase control) {
		super(clientUI, control);
	}

	protected UIDialog createQueryUI() {
		if (queryDialog == null) {
			queryDialog = new ClientUIQueryDlg(getBillUI(), null, _getCorp()
					.getPrimaryKey(), getBillUI()._getModuleCode(),
					_getOperator(), getBillUI().getBusinessType(), getBillUI()
							.getNodeKey());
		}
		return queryDialog;
	}
	@Override
	protected String getHeadCondition() {
		return " pk_corp = '"+_getCorp().getPrimaryKey()+"' and isnull(dr,0) = 0 and pk_billtype = '"+WdsWlPubConst.WDSO+"' ";
	}


//	@Override
//	protected void onBoElse(int intBtn) throws Exception {
//		super.onBoElse(intBtn);
//		if (intBtn == ISsButtun.all_selected) {
//			onAllSelect();
//		} else if (intBtn == ISsButtun.all_not_selected) {
//			onAllNoSelect();
//		}
//	}

//	/**
//	 * 
//	 * @作者：lyf
//	 * @说明：全选
//	 * @时间：2011-9-18上午09:19:18
//	 */
//	protected void onAllSelect() {
//		int count = getBillListPanel().getBillListData().getHeadBillModel()
//				.getRowCount();
//		for (int row = 0; row < count; row++) {
//			getBillListPanel().getBillListData().getHeadBillModel().setValueAt(
//					UFBoolean.TRUE, row, "fselect");
//		}
//		ArrayList<MultiBillVO> m_VOBuffer = (ArrayList<MultiBillVO>) getBufferData()
//				.getRelaSortObject();
//		if (m_VOBuffer != null) {
//			for (int i = 0; i < m_VOBuffer.size(); i++) {
//				MultiBillVO bill = m_VOBuffer.get(i);
//				bill.getHeadVO().setFselect(UFBoolean.TRUE);
//			}
//		}
//	}
//
//	/**
//	 * 
//	 * @作者：lyf
//	 * @说明：全消
//	 * @时间：2011-9-18上午09:19:28
//	 */
//	protected void onAllNoSelect() {
//		int count = getBillListPanel().getBillListData().getHeadBillModel()
//				.getRowCount();
//		for (int row = 0; row < count; row++) {
//			getBillListPanel().getBillListData().getHeadBillModel().setValueAt(
//					null, row, "fselect");
//
//		}
//		ArrayList<MultiBillVO> m_VOBuffer = (ArrayList<MultiBillVO>) getBufferData()
//				.getRelaSortObject();
//		if (m_VOBuffer != null) {
//			for (int i = 0; i < m_VOBuffer.size(); i++) {
//				MultiBillVO bill = m_VOBuffer.get(i);
//				bill.getHeadVO().setFselect(UFBoolean.FALSE);
//			}
//		}
//	}
//
//	@Override
//	public void onBoAudit() throws Exception {
//		if (getBillManageUI().isListPanelSelected()) {
//			// 
//			ArrayList<MultiBillVO> select = new ArrayList<MultiBillVO>();
//			ArrayList<MultiBillVO> m_VOBuffer = (ArrayList<MultiBillVO>) getBufferData()
//					.getRelaSortObject();
//			if (m_VOBuffer != null && m_VOBuffer.size() > 0) {
//				for (int i = 0; i < m_VOBuffer.size(); i++) {
//					MultiBillVO bill = m_VOBuffer.get(i);
//					UFBoolean fselsect = PuPubVO.getUFBoolean_NullAs(bill
//							.getHeadVO().getFselect(), UFBoolean.FALSE);
//					if (fselsect.booleanValue()) {
//						select.add(bill);
//					}
//				}
//			}
//			StringBuffer bur = new StringBuffer();
//			for (int i = 0; i < select.size(); i++) {
//				Writeback4cHVO hvo = (Writeback4cHVO) select.get(i).getParentVO();
//				String vbillno = hvo.getVbillno() == null ? "" : hvo
//						.getVbillno();
//				try {
//					// 获得数据
//					MultiBillVO modelVo = select.get(i);
//					// 如果状态一致则退出
//					if (checkVOStatus(modelVo,
//							new int[] { IBillStatus.CHECKPASS })) {
//						System.out.println("无效的鼠标处理机制");
//						return;
//					}
//					beforeOnBoAction(IBillButton.Audit, modelVo);
//					// *******************
//					MultiBillVO retVo = (MultiBillVO) getBusinessAction()
//							.approve(modelVo, getUIController().getBillType(),
//									getBillUI()._getDate().toString(),
//									getBillUI().getUserObject());
//
//					if (PfUtilClient.isSuccess()) {
//						afterOnBoAction(IBillButton.Audit, retVo);
//						CircularlyAccessibleValueObject[] childVos = ((IExAggVO) retVo)
//								.getAllChildrenVO();
//						if (childVos == null)
//							modelVo.setParentVO(retVo.getParentVO());
//						else
//							modelVo = retVo;
//						// 更新列表
//						modelVo.getParentVO().setAttributeValue(getBillField().getField_CheckDate(),
//								getBillUI()._getDate());
//						modelVo.getParentVO().setAttributeValue(getBillField().getField_CheckMan(),
//								getBillUI()._getOperator());
//						modelVo.getParentVO().setAttributeValue(getBillField().getField_BillStatus(),
//								IBillButton.Audit);
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//					bur.append(vbillno + ":" + e.getMessage() + "\n");
//				}
//			}
//			getBufferData().updateView();
//			if (bur.toString() != null && !"".equalsIgnoreCase(bur.toString())) {
//				getBillUI().showErrorMessage("审核失败的单据：\n" + bur.toString());
//
//			}
//		} else {
//			super.onBoAudit();
//		}
//	}
//	@Override
//	protected void onBoCancelAudit() throws Exception {
//		if (getBillManageUI().isListPanelSelected()) {
//			// 获得园中
//			ArrayList<MultiBillVO> select = new ArrayList<MultiBillVO>();
//			ArrayList<MultiBillVO> m_VOBuffer = (ArrayList<MultiBillVO>) getBufferData()
//					.getRelaSortObject();
//			if (m_VOBuffer != null && m_VOBuffer.size() > 0) {
//				for (int i = 0; i < m_VOBuffer.size(); i++) {
//					MultiBillVO bill = m_VOBuffer.get(i);
//					UFBoolean fselsect = PuPubVO.getUFBoolean_NullAs(bill
//							.getHeadVO().getFselect(), UFBoolean.FALSE);
//					if (fselsect.booleanValue()) {
//						select.add(bill);
//					}
//				}
//			}
//			StringBuffer bur = new StringBuffer();
//			for (int i = 0; i < select.size(); i++) {
//				Writeback4cHVO hvo = (Writeback4cHVO)select.get(i).getParentVO();
//				String vbillno = hvo.getVbillno() == null ? "" : hvo
//						.getVbillno();
//				try {
//					// 获得数据
//					MultiBillVO modelVo = select.get(i);
//					// 如果状态一致则退出
//					if (checkVOStatus(modelVo, new int[] { IBillStatus.FREE })) {
//						System.out.println("无效的鼠标处理机制");
//						return;
//					}
//					beforeOnBoAction(IBillButton.Audit, modelVo);
//					// *******************
//					MultiBillVO retVo = (MultiBillVO) getBusinessAction()
//							.unapprove(modelVo,
//									getUIController().getBillType(),
//									getBillUI()._getDate().toString(),
//									getBillUI().getUserObject());
//
//					if (PfUtilClient.isSuccess()) {
//						afterOnBoAction(IBillButton.CancelAudit, retVo);
//						CircularlyAccessibleValueObject[] childVos = ((IExAggVO) retVo)
//								.getAllChildrenVO();
//						if (childVos == null)
//							modelVo.setParentVO(retVo.getParentVO());
//						else
//							modelVo = retVo;
//						// 更新列表
//						modelVo.getParentVO().setAttributeValue(getBillField().getField_BillStatus(),
//								IBillStatus.FREE);
//						modelVo.getParentVO().setAttributeValue(
//								getBillField().getField_CheckMan(), null);
//						modelVo.getParentVO().setAttributeValue(
//								getBillField().getField_CheckDate(), null);
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//					bur.append(vbillno + ":" + e.getMessage() + "\n");
//				}
//			}
//			getBufferData().updateView();
//			if (bur.toString() != null && !"".equalsIgnoreCase(bur.toString())) {
//				getBillUI().showErrorMessage("弃审失败的单据：\n" + bur.toString());
//
//			}
//		} else {
//			super.onBoCancelAudit();
//		}
//
//	}

	@Override
	protected void onBoSave() throws Exception {
		super.onBoSave();
	}
}
