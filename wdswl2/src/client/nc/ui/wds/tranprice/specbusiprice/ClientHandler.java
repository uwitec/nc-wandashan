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
	 * ǰ̨У��
	 */
	@Override
	protected void beforeSaveValudate() throws Exception {		
		//У�� ����ҵ������Ψһ��
		BeforeSaveValudate.FieldBodyUnique(getBillCardPanelWrapper().getBillCardPanel().getBillTable(),
				getBillCardPanelWrapper().getBillCardPanel().getBillModel(), "speccode", "����ҵ�����");
	}

	@Override
	protected UIDialog createQueryUI() {
		return new HYQueryDLG(getBillUI(), null, getBillUI()._getCorp().getPrimaryKey(), WdsWlPubConst.TRANS_SPECBUSIPRICE_NODECODE, getBillUI()._getOperator(), null);
	}

	//����
	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		super.onBoAdd(bo);
	}
	
	//����
	@Override
	protected void onBoLineAdd() throws Exception {
		//����ǰ
		
		super.onBoLineAdd();
		//���к�
	}

	//onBoLineAdd������ �����ķ���
	@Override
	protected void onAddRowNo() {
//		CircularlyAccessibleValueObject[] os = getBillCardPanelWrapper().getSelectedBodyVOs();
//		CircularlyAccessibleValueObject o = os[0];
//		o.setAttributeValue("priceunit", new Integer(0));
//		getBillCardPanelWrapper().getBillCardPanel().getBillTable().updateUI();
		//��õ�ǰ �����е��к�
		int selectRow = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
		//���ñ��� ĳ��ĳ�ֶε�ֵ
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(new Integer(0), selectRow, "priceunit");
		
		//����ִ�� ���з���
		super.onAddRowNo();
	}
	
	

}
