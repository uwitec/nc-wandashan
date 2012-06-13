package nc.ui.wds.tranprice.specbusiprice;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.query.HYQueryDLG;
import nc.ui.wl.pub.BeforeSaveValudate;
import nc.ui.wl.pub.SingleBodyEventHandler;
import nc.vo.wl.pub.WdsWlPubConst;

public class ClientHandler extends SingleBodyEventHandler{

	public ClientHandler(BillCardUI billUI, ICardController control) {
		super(billUI, control);
		
	}
	
	/**
	 * 前台校验
	 */
	@Override
	protected void beforeSaveValudate() throws Exception {		
		//校验 特殊业务编码的唯一性
		BeforeSaveValudate.FieldBodyUnique(getBillCardPanelWrapper().getBillCardPanel().getBillTable(),
				getBillCardPanelWrapper().getBillCardPanel().getBillModel(), "speccode", "特殊业务编码");
	}

	@Override
	protected UIDialog createQueryUI() {
		return new HYQueryDLG(getBillUI(), null, getBillUI()._getCorp().getPrimaryKey(), WdsWlPubConst.TRANS_SPECBUSIPRICE_NODECODE, getBillUI()._getOperator(), null);
	}

	//增加
	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		super.onBoAdd(bo);
	}
	
	//加行
	@Override
	protected void onBoLineAdd() throws Exception {
		//加行前
		
		super.onBoLineAdd();
		//加行后
	}

	//onBoLineAdd方法中 包含的方法
	@Override
	protected void onAddRowNo() {
//		CircularlyAccessibleValueObject[] os = getBillCardPanelWrapper().getSelectedBodyVOs();
//		CircularlyAccessibleValueObject o = os[0];
//		o.setAttributeValue("priceunit", new Integer(0));
//		getBillCardPanelWrapper().getBillCardPanel().getBillTable().updateUI();
		//获得当前 增加行的行号
		int selectRow = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
		//设置表体 某行某字段的值
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(new Integer(0), selectRow, "priceunit");
		
		//继续执行 增行方法
		super.onAddRowNo();
	}
	
	

}
