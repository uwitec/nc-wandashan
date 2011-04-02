package nc.ui.dm.cost.trans;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.base.AbstractBillUI;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.dm.cost.trans.TransmlVO;

public class ClientCardEventHandler extends CardEventHandler {

	public ClientCardEventHandler(BillCardUI billUI, ICardController control) {
		super(billUI, control);

	}

	@Override
	protected void onBoEdit() throws Exception {

		int[] selectRows = getBillCardPanelWrapper().getBillCardPanel()
				.getBillTable().getSelectedRows();
		if (selectRows.length <= 0) {
			throw new Exception("请选中某一行后在修改");
		}

		super.onBoEdit();

		BillModel model = getBillCardPanelWrapper().getBillCardPanel()
				.getBodyPanel().getTableModel();
		// 设定model为行编辑状态
		model.setRowEditState(true);
		// 如果选中多行只把最后一行设为编辑状态
		for (int i = 0; i < selectRows.length; i++) {

			model.setEditRow(selectRows[i]);

		}

	}

	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {

		// 获得单据UI设置单据的操作状态为可编辑
		getBillUI().setBillOperate(IBillOperate.OP_EDIT);
		// 增加一行的操作
		onBoLineAdd();
		// 如何获取当前被选中的单据的某行记录
		int selectRow = getBillCardPanelWrapper().getBillCardPanel()
				.getBillTable().getSelectedRow();
		// 数据业务模型
		BillModel model = getBillCardPanelWrapper().getBillCardPanel()
				.getBodyPanel().getTableModel();
		// 设置 model为行编辑状态
		model.setRowEditState(true);
		// 设置model的可编辑行为
		model.setEditRow(selectRow);
	}

	@Override
	protected void onBoDelete() throws Exception {
		// 删除界面中被选中的行
		getBillCardPanelWrapper().deleteSelectedLines();
		// 执行保存方法（取界面变化VO，存盘，设置buffer ，设定当前行）
		onBoSave();
	}

	// 响应按钮事件之前的预处理
	@Override
	protected void buttonActionBefore(AbstractBillUI billUI, int intBtn)
			throws Exception {

		super.buttonActionBefore(billUI, intBtn);
		//下面是学习测试代码
	BillItem[] bills=getBillCardPanelWrapper().getBillCardPanel().getBillData().getHeadTailItems();
	
		
		
		//学习测试代码结束
		if (IBillButton.Save == intBtn) {
			Object[] obs = getBillCardPanelWrapper().getBillVOFromUI()
					.getChildrenVO();

			if (obs != null) {
				TransmlVO[] tras = (TransmlVO[]) obs;
				for (int i = 0; i < tras.length; i++) {
					if (tras[i].getDfirstdate() == null
							|| tras[i].getDlastdate() == null
							|| tras[i].getDmile() == null
							|| tras[i].getPk_vfirstrecord() == null
							|| tras[i].getPk_vlastrecord() == null
							|| tras[i].getPk_vrecplace() == null
							|| tras[i].getPk_vrecplace() == null) {

						throw new Exception("元素不能为空");

					}

					if (tras[i].getDfirstdate().after(tras[i].getDlastdate())) {

						throw new Exception("最后维护日期不能小于初次记录日期");
					}
					if (tras[i].getPk_vdelplace().equalsIgnoreCase(
							tras[i].getPk_vrecplace())) {

						throw new Exception("收货地点不能和发货地点一样！");
					}

				}
			}

		}

	}

}
