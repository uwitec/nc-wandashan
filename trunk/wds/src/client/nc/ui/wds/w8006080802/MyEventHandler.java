package nc.ui.wds.w8006080802;

import java.util.ArrayList;

import nc.itf.uap.IUAPQueryBS;
import nc.ui.pub.ButtonObject;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.wds.w8006080806.AbstractMyEventHandler;
import nc.vo.pub.SuperVO;
import nc.vo.pub.bill.BillRendererVO;
import nc.vo.wds.w8006080802.TbFreightstandradBTVO;

/**
 * 
 * ������AbstractMyEventHandler�������ʵ���࣬ ��Ҫ�������˰�ť��ִ�ж������û����Զ���Щ����������Ҫ�����޸�
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
		// UIRefPane uipane = (UIRefPane)
		// getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
		// "pk_wlxx").getComponent();
		//
		// String ss = uipane.getRefModel().getPkValue();
		// System.out.println(ss);
		TbFreightstandradBTVO[] o = (TbFreightstandradBTVO[]) getBillCardPanelWrapper()
				.getBillCardPanel().getBillModel().getBodyValueVOs(
						TbFreightstandradBTVO.class.getName());
		ArrayList arr = new ArrayList();
		Object oo = getBillCardPanelWrapper().getBillCardPanel()
				.getBodyValueAt(
						getBillCardPanelWrapper().getBillCardPanel()
								.getBillTable().getSelectedRow(), "area");
		
		//��Ƭ�»�ñ����������
		int rowNum = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();

		
		boolean b = true;
		if (o.length == 0) {

			super.onBoSave();
		} else {
			if (oo == null) {
				getBillUI().showErrorMessage("����վ����Ϊ��");
				b=false;
			} else {
				for(int i=0;i<rowNum;i++){
					String pk_wlxx = (String) getBillCardPanelWrapper()
					.getBillCardPanel().getBillModel()
					.getValueAt(i, "pk_wlxx");
					if (!arr.contains(pk_wlxx)) {
						
						arr.add(pk_wlxx);
					} else {
						getBillUI().showErrorMessage("����վ���ظ�");
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

		/*
		 * Object pk =
		 * getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pp_pk").getValueObject();
		 * if(null!=pk&&!"".equals(pk)){ String strWhere = " where dr=0 and
		 * station_a='"+pk.toString()+"'";
		 * 
		 * UIRefPane uipane = (UIRefPane)
		 * getBillCardPanelWrapper().getBillCardPanel().getBodyItem("cdhz").getComponent();
		 * 
		 * uipane.getRefModel().setWherePart(strWhere); }
		 */

	}

	@Override
	protected void onBoQuery() throws Exception {
		// TODO Auto-generated method stub

		StringBuffer strWhere = new StringBuffer();

		//strWhere.append(" tc_pacttype=0 and tc_archive=0 and ");
		if (askForQueryCondition(strWhere) == false)
			return;// �û������˲�ѯ

		SuperVO[] queryVos = queryHeadVOs(strWhere.toString());

		getBufferData().clear();
		// �������ݵ�Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
		showZeroLikeNull(false);
	}

}