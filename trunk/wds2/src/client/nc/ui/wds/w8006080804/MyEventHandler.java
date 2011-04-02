package nc.ui.wds.w8006080804;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.wds.w8006080806.AbstractMyEventHandler;
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

	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
		showZeroLikeNull(false);
	/*	Object evennumbermin = getBillCardPanelWrapper().getBillCardPanel()
				.getBodyValueAt(
						getBillCardPanelWrapper().getBillCardPanel()
								.getBillTable().getSelectedRow(),
						"evennumbermin");
		Object evennumbermax = getBillCardPanelWrapper().getBillCardPanel()
				.getBodyValueAt(
						getBillCardPanelWrapper().getBillCardPanel()
								.getBillTable().getSelectedRow(),
						"evennumbermax");*/
		Object packnumbermin = getBillCardPanelWrapper().getBillCardPanel()
				.getBodyValueAt(
						getBillCardPanelWrapper().getBillCardPanel()
								.getBillTable().getSelectedRow(),
						"packnumbermin");
		Object packnumbermax = getBillCardPanelWrapper().getBillCardPanel()
				.getBodyValueAt(
						getBillCardPanelWrapper().getBillCardPanel()
								.getBillTable().getSelectedRow(),
						"packnumbermax");
		TbFreightstandradBTVO[] o = (TbFreightstandradBTVO[]) getBillCardPanelWrapper()
		.getBillCardPanel().getBillModel().getBodyValueVOs(
				TbFreightstandradBTVO.class.getName());
		if (o.length == 0) {

			super.onBoSave();
		} else {
		/*if (evennumbermax == null) {
			getBillUI().showErrorMessage("公里数最大值不能为空");
		} else*/ if (packnumbermax == null) {
			getBillUI().showErrorMessage("件数最大值不能为空");
		} else {
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

		/*
		 * Object pk =
		 * getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pp_pk").getValueObject();
		 * if(null!=pk&&!"".equals(pk)){ String strWhere = " where dr=0 and
		 * station_a='"+pk.toString()+"'";
		 * 
		 * UIRefPane uipane = (UIRefPane)
		 * getBillCardPanelWrapper().getBillCardPanel().getBodyItem("cdhz").getComponent();
		 * 
		 * uipane.getRefModel().setWherePart(strWhere);
		 * 
		 *  }
		 */

	}

	@Override
	protected void onBoQuery() throws Exception {
		// TODO Auto-generated method stub
		StringBuffer strWhere = new StringBuffer();

		//strWhere.append(" tc_archive=0 and tc_isld='Y' and ");
		if (askForQueryCondition(strWhere) == false)
			return;// 用户放弃了查询

		SuperVO[] queryVos = queryHeadVOs(strWhere.toString());

		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
		showZeroLikeNull(false);
	}

}