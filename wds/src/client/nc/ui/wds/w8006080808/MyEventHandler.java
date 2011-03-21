package nc.ui.wds.w8006080808;

import java.util.ArrayList;

import nc.ui.pub.ButtonObject;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.pub.SuperVO;
import nc.vo.pub.bill.BillRendererVO;
import nc.vo.wds.w8006080802.TbFreightstandradBTVO;

/**
 * 
 * 该类是AbstractMyEventHandler抽象类的实现类， 主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
 * 
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	public void showZeroLikeNull(boolean bShowZeroLikeNull) {
		BillRendererVO vo = getBillCardPanelWrapper().getBillCardPanel()
				.getBodyPanel().getRendererVO();
		vo.setShowZeroLikeNull(bShowZeroLikeNull);
		getBillCardPanelWrapper().getBillCardPanel().getBodyPanel()
				.setShowFlags(vo);
	}

	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
		showZeroLikeNull(false);
		TbFreightstandradBTVO[] o = (TbFreightstandradBTVO[]) getBillCardPanelWrapper()
				.getBillCardPanel().getBillModel().getBodyValueVOs(
						TbFreightstandradBTVO.class.getName());
		ArrayList arr = new ArrayList();
		Object oo = getBillCardPanelWrapper().getBillCardPanel()
				.getBodyValueAt(
						getBillCardPanelWrapper().getBillCardPanel()
								.getBillTable().getSelectedRow(), "area");

		// 卡片下获得表体的总行数
		int rowNum = getBillCardPanelWrapper().getBillCardPanel()
				.getBillTable().getRowCount();

		boolean b = true;
		if (o.length == 0) {

			super.onBoSave();
		} else {
			if (oo == null) {
				getBillUI().showErrorMessage("到货站不能为空");
				b = false;
			} else {
				for (int i = 0; i < rowNum; i++) {
					String pk_wlxx = (String) getBillCardPanelWrapper()
							.getBillCardPanel().getBillModel().getValueAt(i,
									"pk_wlxx");
					if (!arr.contains(pk_wlxx)) {

						arr.add(pk_wlxx);
					} else {
						getBillUI().showErrorMessage("到货站有重复");
						b = false;
					}
				}
			}
			if (b) {
				super.onBoSave();
			}

		}

	}

	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		// TODO Auto-generated method stub
		super.onBoAdd(bo);
		showZeroLikeNull(false);
	}

	@Override
	protected void onBoEdit() throws Exception {
		// TODO Auto-generated method stub
		super.onBoEdit();
		showZeroLikeNull(false);
	}

	@Override
	protected void onBoQuery() throws Exception {
		// TODO Auto-generated method stub
		super.onBoQuery();
	}

}