package nc.ui.zmpub.pub.bill;

import java.sql.SQLException;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.BillCardPanelWrapper;
import nc.ui.trade.bill.BusiTypeChangeEvent;
import nc.ui.trade.bill.IBillBusiListener;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.bill.RefBillTypeChangeEvent;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.pub.VOTreeNode;
import nc.ui.trade.treecard.BillTreeCardUI;
import nc.ui.trade.treecard.ITreeCardController;
import nc.ui.trade.treecard.TreeCardEventHandler;
import nc.vo.pf.pub.BusitypeVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.trade.button.ButtonVO;

/**
 * 树卡单表体基类
 * 
 * @author mlr
 */
public abstract class SingleTreeEventHandler extends TreeCardEventHandler {
	private static final String staticACTION = "BOACTION";
	private static final String staticASS = "BOASS";
	private IBillBusiListener m_bbl = null;

	public void onTreeSelected(VOTreeNode selectnode) {
		try {
			if ((getUITreeCardController() instanceof ISingleController)) {
				onQueryBodyData(selectnode);
			}
		} catch (BusinessException ex) {
			getBillUI().showErrorMessage(ex.getMessage());
			ex.printStackTrace();
		} catch (Exception e) {
			getBillUI().showErrorMessage(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 按钮单击事件
	 */
	public void onButton(ButtonObject bo) {
		if (getUITreeCardController().isTableTree()) {
			ButtonObject parentBtn = bo.getParent();
			if (parentBtn != null) {
				int intBtn = Integer.parseInt(parentBtn.getTag());
			}
		}
		if (getBillUI().getBillOperate() == IBillOperate.OP_ADD
				|| getBillUI().getBillOperate() == IBillOperate.OP_EDIT) {
			if (getBillCardPanelWrapper() != null)
				getBillCardPanelWrapper().getBillCardPanel().stopEditing();

		}
		try {
			ButtonObject parentBtn = bo.getParent();

			if (parentBtn != null && Integer.parseInt(parentBtn.getTag()) < 100) {
				int intParentBtn = Integer.parseInt(parentBtn.getTag());
				complexOnButton(intParentBtn, bo);
			} else {
				if (bo.getTag() == null)
					System.out.println("新增按钮必须设置TAG,TAG>100的整数.....");
				int intBtn = Integer.parseInt(bo.getTag());
				if (intBtn > 100)// 编号大于100认为是自定义按钮
					onBoElse(intBtn);
				else
					// 否则认为是预置按钮
					simpleOnButton(intBtn, bo);
			}
		} catch (BusinessException ex) {
			onBusinessException(ex);
		} catch (SQLException ex) {
			getBillUI().showErrorMessage(ex.getMessage());
		} catch (Exception e) {
			getBillUI().showErrorMessage(e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * 父类按钮的事件处理。 创建日期：(2004-2-25 21:04:27)
	 * 
	 * @param intBtn
	 *            int
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	private void simpleOnButton(int intBtn, ButtonObject bo)
			throws java.lang.Exception {
		buttonActionBefore(getBillUI(), intBtn);
		switch (intBtn) {
		case IBillButton.Add: {
			if (!getBillUI().isBusinessType().booleanValue()) {
				getBillUI().showHintMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"uifactory", "UPPuifactory-000061")/*
																	 * @res
																	 * "开始进行增加单据，请等待......"
																	 */);
				onBoAdd(bo);
				// 动作执行后处理
				buttonActionAfter(getBillUI(), intBtn);
			}
			break;
		}
		case IBillButton.Edit: {
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000067")/*
												 * @res "开始进行编辑单据，请等待......"
												 */);
			onBoEdit();
			// 动作执行后处理
			buttonActionAfter(getBillUI(), intBtn);
			break;
		}
		case IBillButton.Del: {
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000068")/*
												 * @res "开始进行作废单据，请等待......"
												 */);
			onBoDel();
			// 动作执行后处理
			buttonActionAfter(getBillUI(), intBtn);
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000069")/* @res "单据作废操作结束" */
			);
			break;
		}
		case IBillButton.Delete: {
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000070")/*
												 * @res "开始进行档案删除，请等待......"
												 */);
			onBoDelete();
			// 动作执行后处理
			buttonActionAfter(getBillUI(), intBtn);
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000071")/* @res "档案删除完成" */
			);
			break;
		}
		case IBillButton.Query: {
			getBillUI().showHintMessage(bo.getName());

			if (super.getUIController() instanceof ISingleController) {
				ISingleController strl = (ISingleController) super
						.getUIController();
				if (strl.isSingleDetail())
					onBoBodyQuery();
				else
					onBoQuery();
			} else
				onBoQuery();
			// 动作执行后处理
			buttonActionAfter(getBillUI(), intBtn);

			break;
		}
		case IBillButton.Save: {
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000072")/*
												 * @res "开始进行单据保存，请等待......"
												 */);
			onBoSave();
			// 动作执行后处理
			buttonActionAfter(getBillUI(), intBtn);
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000073")/* @res "单据保存操作结束" */
			);
			break;
		}
		case IBillButton.Cancel: {
			onBoCancel();
			// 清除提示状态栏提示信息
			getBillUI().showHintMessage("");
			// 动作执行后处理
			buttonActionAfter(getBillUI(), intBtn);
			break;
		}
		case IBillButton.Print: {
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000074")/*
												 * @res "开始进行打印单据，请等待......"
												 */);
			onBoPrint();
			// 动作执行后处理
			buttonActionAfter(getBillUI(), intBtn);
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000075")/* @res "单据打印操作结束" */
			);
			break;
		}
		case IBillButton.DirectPrint: {
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000074")/*
												 * @res "开始进行打印单据，请等待......"
												 */);
			onBoDirectPrint();
			// 动作执行后处理
			buttonActionAfter(getBillUI(), intBtn);
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000075")/* @res "单据打印操作结束" */
			);
			break;
		}

		case IBillButton.Return: {
			onBoReturn();
			// 动作执行后处理
			buttonActionAfter(getBillUI(), intBtn);
			break;
		}
		case IBillButton.Card: {
			onBoCard();
			// 动作执行后处理
			buttonActionAfter(getBillUI(), intBtn);
			break;
		}
		case IBillButton.Refresh: {
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000076")/*
												 * @res "开始进行刷新单据，请等待......"
												 */);
			onBoRefresh();
			// 动作执行后处理
			buttonActionAfter(getBillUI(), intBtn);
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000077")/* @res "单据刷新操作结束" */
			);

			break;
		}
		case IBillButton.Refbill: {
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000078")/*
												 * @res "开始进行参照单据，请等待......"
												 */);
			onBillRef();
			// 动作执行后处理
			buttonActionAfter(getBillUI(), intBtn);
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000079")/* @res "单据参照操作结束" */
			);
			break;
		}
		case IBillButton.Copy: {
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000080")/*
												 * @res "开始进行数据复制，请等待......"
												 */);
			onBoCopy();
			// 动作执行后处理
			buttonActionAfter(getBillUI(), intBtn);
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000081")/* @res "数据复制操作结束" */
			);
			break;
		}
		case IBillButton.Audit: {
			// 因为下面的提示不符合易用性规范，现在删掉它们
			// getBillUI().showHintMessage(
			// nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
			// "UPPuifactory-000179")/*
			// * @res "开始执行操作,请等待..."
			// */);
			onBoAudit();
			// getBillUI().showHintMessage(
			// nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
			// "UPPuifactory-000180")/*
			// * @res "执行完毕. "
			// */
			// );
			break;
		}
		case IBillButton.CancelAudit: {
			// getBillUI().showHintMessage(
			// nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
			// "UPPuifactory-000179")/*
			// * @res 开始执行操作, 请等待...
			// */
			// );
			onBoCancelAudit();
			// getBillUI().showHintMessage(
			// nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
			// "UPPuifactory-000180")/*
			// * @res 执行完毕.
			// */
			// );
			break;
		}
		case IBillButton.Commit: {
			// getBillUI().showHintMessage(
			// nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
			// "UPPuifactory-000179")/*
			// * @res 开始执行操作, 请等待...
			// */
			// );
			onBoCommit();
			// getBillUI().showHintMessage(
			// nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
			// "UPPuifactory-000180")/*
			// * @res 执行完毕.
			// */
			// );
			break;
		}
		case IBillButton.SelAll: {
			// getBillUI().showHintMessage(
			// nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
			// "UPPuifactory-000179")/*
			// * @res 开始执行操作, 请等待...
			// */
			// );

			onBoSelAll();

			// getBillUI().showHintMessage(
			// nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
			// "UPPuifactory-000180")/*
			// * @res 执行完毕.
			// */
			// );
			break;
		}
		case IBillButton.SelNone: {
			// getBillUI().showHintMessage(
			// nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
			// "UPPuifactory-000179")/*
			// * @res 开始执行操作, 请等待...
			// */
			// );
			onBoSelNone();
			// getBillUI().showHintMessage(
			// nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
			// "UPPuifactory-000180")/*
			// * @res 执行完毕.
			// */
			// );
			break;
		}
		case IBillButton.ImportBill: {
			// getBillUI().showHintMessage(
			// nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
			// "UPPuifactory-000179")/*
			// * @res 开始执行操作, 请等待...
			// */
			// );
			onBoImport();
			// getBillUI().showHintMessage(
			// nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
			// "UPPuifactory-000180")/*
			// * @res 执行完毕.
			// */
			// );
			break;
		}
		case IBillButton.ExportBill: {
			// getBillUI().showHintMessage(
			// nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
			// "UPPuifactory-000179")/*
			// * @res 开始执行操作, 请等待...
			// */
			// );
			onBoExport();
			// getBillUI().showHintMessage(
			// nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
			// "UPPuifactory-000180")/*
			// * @res 执行完毕.
			// */
			// );
			break;
		}
		case IBillButton.ApproveInfo: {
			onBoApproveInfo();
			break;
		}
		default: {
			// getBillUI().showHintMessage(
			// nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
			// "UPPuifactory-000179")/*
			// * @res 开始执行操作, 请等待...
			// */
			// );

			onBoActionElse(bo);

			// getBillUI().showHintMessage(
			// nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
			// "UPPuifactory-000180")/*
			// * @res 执行完毕.
			// */
			// );
			break;
		}
		}

	}

	/**
	 * 按钮m_boBusiType点击时执行的动作,如有必要，请覆盖.
	 */
	private final void onBoBusiType(ButtonObject bo) throws Exception {
		// 执行前处理
		busiTypeBefore(getBillUI(), bo);
		bo.setSelected(true);
		// 设置业务类型
		BusitypeVO vo = (BusitypeVO) bo.getData();
		// 处理增加按钮
		getBusiDelegator()
				.retAddBtn(getButtonManager().getButton(IBillButton.Add),
						_getCorp().getPrimaryKey(),
						getUIController().getBillType(), bo);
		// //处理执行按钮(与单据状态无关）
		getBusiDelegator().retElseBtn(
				getButtonManager().getButton(IBillButton.Action),
				getUIController().getBillType(), staticACTION);

		// getButtonManager().setActionButtonVO(
		// getBillUI().isSaveAndCommitTogether());

		String oldtype = getBillUI().getBusinessType();
		String newtype = vo.getPrimaryKey();
		String oldcode = getBillUI().getBusicode();
		String newcode = vo.getBusicode();

		// 业务类型主键
		getBillUI().setBusinessType(newtype);
		// 业务类型代码
		getBillUI().setBusicode(newcode);

		// 重新刷新UI
		getBillUI().initUI();
		// 清空UI缓存
		getBillUI().getBufferData().clear();
		getBillUI().getBufferData().setCurrentRow(-1);

		getBillUI().updateButtonUI();

		if (m_bbl != null) {
			BusiTypeChangeEvent e = new BusiTypeChangeEvent(this, oldtype,
					newtype, oldcode, newcode);
			m_bbl.busiTypeChange(e);
		}
	}

	/**
	 * 父类按钮的事件处理。 创建日期：(2004-2-25 21:04:27)
	 * 
	 * @param intBtn
	 *            int
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	private void complexOnButton(int intBtn, ButtonObject bo)
			throws java.lang.Exception {
		switch (intBtn) {
		case IBillButton.Busitype: {
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000060")/*
												 * @res "开始选择业务类型，请等待......"
												 */);
			onBoBusiType(bo);
			break;
		}
		case IBillButton.Add: {
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000061")/*
												 * @res "开始进行增加单据，请等待......"
												 */);
			onBoBusiTypeAdd(bo, null);
			break;
		}
		case IBillButton.Action: {
			onBoAction(bo);
			break;
		}
		case IBillButton.Ass: {
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000062")/*
												 * @res "开始对单据的辅助进行操作，请等待......"
												 */);
			onBoAss(bo);
			break;
		}
		case IBillButton.Line: {
			onBoLine(bo);
			break;
		}
		case IBillButton.File: {
			onBoFile(bo);
		}
		case IBillButton.Brow: {
			onBoBrow(bo);
			break;
		}
		case IBillButton.NodeKey: {
			onBoNodekey(bo);
			break;
		}
		case IBillButton.Refbill: {
			ButtonVO btnVo = (ButtonVO) bo.getData();
			onBoElse(btnVo.getBtnNo());
			break;
		}
		default: {
			if (bo.getData() != null && bo.getData() instanceof ButtonVO) {
				ButtonVO btnVo = (ButtonVO) bo.getData();
				onBoElse(btnVo.getBtnNo());
			}
			break;
		}
		}
	}

	/**
	 * 按钮m_boNodekey点击时执行的动作,如有必要，请覆盖.
	 */
	private final void onBoNodekey(ButtonObject bo) throws Exception {
		bo.setSelected(true);
		// 设置NodeKey
		getBillUI().setNodeKey(bo.getTag());

		// 重新刷新UI
		getBillUI().initUI();
		// 清空查询模版
		setQueryUI(null);
		// 清空UI缓存
		getBillUI().getBufferData().clear();
		getBillUI().getBufferData().setCurrentRow(-1);

		getBillUI().updateButtonUI();
	}

	/**
	 * 浏览操作处理。 创建日期：(2004-1-7 8:57:02)
	 * 
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	private void onBoBrow(ButtonObject bo) throws java.lang.Exception {
		int intBtn = Integer.parseInt(bo.getTag());
		// 动作执行前处理
		buttonActionBefore(getBillUI(), intBtn);
		switch (intBtn) {
		case IBillButton.First: {
			getBufferData().first();
			break;
		}
		case IBillButton.Prev: {
			getBufferData().prev();
			break;
		}
		case IBillButton.Next: {
			getBufferData().next();
			break;
		}
		case IBillButton.Last: {
			getBufferData().last();
			break;
		}
		}
		// 动作执行后处理
		buttonActionAfter(getBillUI(), intBtn);
		getBillUI().showHintMessage(
				nc.ui.ml.NCLangRes.getInstance()
						.getStrByID(
								"uifactory",
								"UPPuifactory-000503",
								null,
								new String[] { nc.vo.format.Format
										.indexFormat(getBufferData()
												.getCurrentRow() + 1) })/*
																		 * @res
																		 * "转换第:"
																		 * +
																		 * getBufferData
																		 * ().
																		 * getCurrentRow
																		 * () +
																		 * "页完成)"
																		 */
		);

	}

	/**
	 * 行操作处理。 创建日期：(2004-1-7 8:57:02)
	 * 
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	private void onBoLine(ButtonObject bo) throws java.lang.Exception {
		int intBtn = -1;// Integer.parseInt(bo.getTag());

		if (bo.getData() != null && bo.getData() instanceof ButtonVO) {
			ButtonVO btnVo = (ButtonVO) bo.getData();
			intBtn = btnVo.getBtnNo();
		} else {
			intBtn = Integer.parseInt(bo.getTag());
		}

		// 动作执行前处理
		buttonActionBefore(getBillUI(), intBtn);

		getBillUI().showHintMessage(bo.getName());

		switch (intBtn) {
		case IBillButton.AddLine: {
			onBoLineAdd();
			break;
		}
		case IBillButton.DelLine: {
			onBoLineDel();
			break;
		}
		case IBillButton.CopyLine: {
			onBoLineCopy();
			break;
		}
		case IBillButton.InsLine: {
			onBoLineIns();
			break;
		}
		case IBillButton.PasteLine: {
			onBoLinePaste();
			break;
		}
		default:
			onBoElse(intBtn);
			break;
		}

		// 动作执行后处理
		buttonActionAfter(getBillUI(), intBtn);
	}

	/**
	 * 按钮m_boAction点击时执行的动作,如有必要，请覆盖. 单据执行动作处理
	 */
	private final void onBoAss(ButtonObject bo) throws Exception {
		beforeOnBoAss(bo);
		AggregatedValueObject modelVo = getBufferData().getCurrentVO();
		Object ret = getBusinessAction().processAction(bo.getTag(), modelVo,
				getUIController().getBillType(),
				getBillUI()._getDate().toString(), getBillUI().getUserObject());
		if (ret != null && ret instanceof AggregatedValueObject) {
			AggregatedValueObject vo = (AggregatedValueObject) ret;
			// 更新状态
			modelVo.getParentVO().setAttributeValue(
					getBillField().getField_BillStatus(),
					vo.getParentVO().getAttributeValue(
							getBillField().getField_BillStatus()));
			// 更新时间戳
			modelVo.getParentVO().setAttributeValue("ts",
					vo.getParentVO().getAttributeValue("ts"));

			getBufferData().setVOAt(getBufferData().getCurrentRow(), modelVo);
			getBufferData().setCurrentRow(getBufferData().getCurrentRow());
		}
		afterOnBoAss(bo);
	}

	/**
	 * 按钮m_boAction点击时执行的动作,如有必要，请覆盖. 单据执行动作处理
	 */
	private final void onBoAction(ButtonObject bo) throws Exception {
		// getBillUI().showHintMessage(
		// nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
		// "UPPuifactory-000179")/*
		// * @res "开始执行操作,请等待..."
		// */);
		ButtonVO btnVo = (ButtonVO) bo.getData();
		if (btnVo == null)
			return;
		switch (btnVo.getBtnNo()) {
		case IBillButton.Audit: {
			onBoAudit();
			break;
		}
		case IBillButton.CancelAudit: {
			onBoCancelAudit();
			break;
		}
		case IBillButton.Commit: {
			onBoCommit();
			break;
		}
		case IBillButton.Del: {
			onBoDel();
			break;
		}
		default: {
			onBoActionElse(bo);
			break;
		}
		}

		// getBillUI().showHintMessage(
		// nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
		// "UPPuifactory-000180")/*
		// * @res "执行完毕."
		// */
		// );
	}

	/**
	 * 按钮m_boAdd点击时执行的动作,如有必要，请覆盖.
	 * 
	 * @param bo
	 *            来源单据的平台生成按钮
	 * @param sourceBillId
	 *            参考来源单据Id
	 */
	private final void onBoBusiTypeAdd(ButtonObject bo, String sourceBillId)
			throws Exception {
		getBusiDelegator().childButtonClicked(bo, _getCorp().getPrimaryKey(),
				getBillUI()._getModuleCode(), _getOperator(),
				getUIController().getBillType(), getBillUI(),
				getBillUI().getUserObject(), sourceBillId);
		if (nc.ui.pub.pf.PfUtilClient.makeFlag) {
			// 设置单据状态
			getBillUI().setCardUIState();
			// 新增
			getBillUI().setBillOperate(IBillOperate.OP_ADD);
		} else {
			if (PfUtilClient.isCloseOK()) {
				if (m_bbl != null) {
					String tmpString = bo.getTag();
					int findIndex = tmpString.indexOf(":");
					String newtype = tmpString.substring(0, findIndex);
					RefBillTypeChangeEvent e = new RefBillTypeChangeEvent(this,
							null, newtype);
					m_bbl.refBillTypeChange(e);
				}
				if (isDataChange())
					setRefData(PfUtilClient.getRetVos());
				else
					setRefData(PfUtilClient.getRetOldVos());
			}
		}
	}

	/**
	 * 获取树形数据的主键名字
	 * 
	 * @author mlr
	 * @说明：（鹤岗矿业） 2011-11-2下午03:11:17
	 * @return
	 */
	public abstract String getSelectTreeIdName();

	/**
	 * 单据行操作
	 */
	@Override
	protected void onBoLineAdd() throws Exception {
		if (getBillTreeCardUI().getBillTreeSelectNode() != null) {
			if (getBillTreeCardUI().getBillTreeSelectNode().getChildCount() > 0) {
				throw new Exception("请选中具体的树节点   不能选则树节点的父类");
			}
			super.onBoLineAdd();
			String pk_father = getBillTreeCardUI().getBillTreeSelectNode()
					.getData().getPrimaryKey();

			int selectRow = getBillCardPanelWrapper().getBillCardPanel()
					.getBillTable().getSelectedRow();
			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(
					pk_father, selectRow, getSelectTreeIdName());// 赋值父类主键
		} else {
			throw new Exception("请选中树节点");
		}
	}

	/**
	 * 查询表体数据
	 * 
	 * @author mlr
	 * @说明：（鹤岗矿业） 2011-11-2下午01:38:28
	 * @param selectnode
	 * @throws Exception
	 */
	public void onQueryBodyData(VOTreeNode selectnode) throws Exception {
		Class voClass = Class.forName(getUIController().getBillVoName()[1]);

		SuperVO vo = (SuperVO) voClass.newInstance();

		String strWhere = "(isnull(dr,0)=0)";

		if (vo.getParentPKFieldName() != null)
			strWhere = "(" + strWhere + ") and " + vo.getParentPKFieldName()
					+ "='" + selectnode.getData().getPrimaryKey() + "'";

		SuperVO[] queryVos = getBusiDelegator().queryHeadAllData(voClass,
				getUIController().getBillType(), strWhere);

		if (queryVos != null && queryVos.length != 0) {
			AggregatedValueObject aVo = (AggregatedValueObject) Class.forName(
					getUIController().getBillVoName()[0]).newInstance();
			aVo.setParentVO(null);
			aVo.setChildrenVO(queryVos);
			getBufferData().addVOToBuffer(aVo);

			int num = getBufferData().getVOBufferSize();
			if (num == -1)
				num = 0;
			else
				num = num - 1;
			getBillTreeCardUI().getTreeToBuffer().put(selectnode.getNodeID(),
					num + "");

			getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
		} else {
			getBillUI().setBillOperate(IBillOperate.OP_INIT);
		}
	}

	/**
	 * CardButtonController 构造子注解。
	 * 
	 * @param billUI
	 *            nc.ui.trade.pub.BillCardUI
	 */
	public SingleTreeEventHandler(BillTreeCardUI billUI, ICardController control) {
		super(billUI, control);
	}

	/**
	 * 单据模版包装类的方法重载 创建日期：(2004-1-6 22:29:36)
	 * 
	 * @return nc.ui.pub.bill.BillCardPanel
	 */
	protected BillCardPanelWrapper getBillCardPanelWrapper() {
		return getBillTreeCardUI().getBillCardWrapper();
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-1-9 8:37:34)
	 * 
	 * @return nc.ui.trade.pub.BillCardUI
	 */
	protected BillTreeCardUI getBillTreeCardUI() {
		return (BillTreeCardUI) getBillUI();
	}

	/**
	 * Bill对应的缓存数据,CARD,LIST必须重载此方法。 创建日期：(2004-1-7 8:44:06)
	 * 
	 * @return nc.ui.trade.buffer.BillUIBuffer
	 */
	protected nc.ui.trade.buffer.BillUIBuffer getBufferData() {
		return getBillTreeCardUI().getBufferData();
	}

	/**
	 * 获得卡片控制器。 创建日期：(2004-1-7 11:42:27)
	 * 
	 * @return nc.ui.trade.controller.ICardController
	 */
	protected ITreeCardController getUITreeCardController() {
		return (ITreeCardController) getUIController();
	}

	/**
	 * 是否可以新增节点。 创建日期：(2004-02-06 13:12:42)
	 * 
	 * @return boolean
	 */
	public boolean isAllowAddNode(nc.ui.trade.pub.TableTreeNode node) {
		return true;
	}

	/**
	 * 是否可以删除节点。 创建日期：(2004-02-06 13:12:42)
	 * 
	 * @return boolean
	 */
	public boolean isAllowDelNode(nc.ui.trade.pub.TableTreeNode node) {
		return true;
	}

	/**
	 * 单据增加的处理 创建日期：(2002-12-23 12:43:15)
	 */
	public void onBoAdd(ButtonObject bo) throws Exception {
		if (isAllowAddNode(getBillTreeCardUI().getBillTreeSelectNode()))
			super.onBoAdd(bo);
	}

	/**
	 * 按钮m_boRefresh点击时执行的动作,如有必要，请覆盖.
	 */
	protected void onBoRefresh() throws Exception {

		getBillTreeCardUI().clearTreeSelect();

		getBillTreeCardUI().createBillTree(
				getBillTreeCardUI().getCreateTreeData());

		getBillTreeCardUI().afterInit();

		getBillTreeCardUI().setBillOperate(
				nc.ui.trade.base.IBillOperate.OP_INIT);
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-1-9 8:37:34)
	 * 
	 * @return nc.ui.trade.pub.BillCardUI
	 */
	protected void onBoSave() throws Exception {
		super.onBoSave();
	}

}
