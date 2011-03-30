package nc.ui.dm.sb;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;

public class ClientEventHandler extends CardEventHandler {

//	@Override
//	protected UIDialog createQueryUI() {
//		return new ClientQueryDLG(getBillUI(), null,
//				_getCorp().getPrimaryKey(), getBillUI().getModuleCode(),
//				_getOperator(), null, null);
//	}
	
//	@Override
//	public void onBoEdit() throws Exception {
//		super.onBoEdit();
//		int selectRow = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
////		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getOperator(), selectRow, "cmodifyman");//�޸���
////		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getDate(), selectRow, "dmodifydate");//�޸�����
//		getBillCardPanelWrapper().getBillCardPanel().getBillModel().execLoadFormula();
//	}
	
//	@Override
	public ClientEventHandler(BillCardUI billUI, ICardController control) {
		super(billUI, control);
		// TODO Auto-generated constructor stub
	}
	
	public void onBoAdd(ButtonObject bo) throws Exception {
		getBillUI().setBillOperate(IBillOperate.OP_EDIT);
		onBoLineAdd();
		int selectRow = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
		BillModel model = getBillCardPanelWrapper().getBillCardPanel().getBodyPanel().getTableModel();
		model.setRowEditState(true);
		model.setEditRow(selectRow);
//		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getOperator(), selectRow, "coperatorid");//¼����
//		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getDate(), selectRow, "dmakedate");//¼������
//		getBillCardPanelWrapper().getBillCardPanel().getBillModel().execLoadFormula();
	}
	
//	public void onBoSave() throws Exception{
//		beforeSave();
//		super.onBoSave();
//	}
//	public void onBoDelete() throws Exception{
//		int selectRow = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
////		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt("Y", selectRow, "vdef1");//����ɾ����־
//		super.onBoDelete();
//	}
//	protected void beforeSave() throws BusinessException{
//		int selectRow = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
//		if(selectRow<0){
//			return;
//		}
//		String pk_invcl =PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(selectRow,"pk_invcl"));
//		String invcode = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(selectRow,"invcode"));
//		if(pk_invcl == null && invcode==null){
//			throw new BusinessException("�����Ϣ����Ϊ��");
//		}
//	
//	}
	
}