package nc.ui.hg.pu.usercust;

import nc.ui.hg.pu.pub.DBTManageEventHandler;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.vo.pub.BusinessException;

public class ClientEventHandler extends DBTManageEventHandler {

	

	public ClientEventHandler(BillCardUI billUI, ICardController control) {
		super(billUI, control);

	}
	@Override
	protected UIDialog createQueryUI() {
		return new ClientQueryDLG(getBillUI(), null,
				_getCorp().getPrimaryKey(), getBillUI().getModuleCode(),
				_getOperator(), null, null);
	}
	
	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		getBillUI().setBillOperate(IBillOperate.OP_EDIT);
		onBoLineAdd();
		int selectRow = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
		BillModel model = getBillCardPanelWrapper().getBillCardPanel().getBodyPanel().getTableModel();
		model.setRowEditState(true);
		model.setEditRow(selectRow);
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getOperator(), selectRow, "voperatorid");//¼����
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getDate(), selectRow, "dmakedate");//¼������
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(this._getCorp().getPrimaryKey(), selectRow, "pk_corp");//��ֵ��ǰ��˾  ���ڲ�ѯ
		getBillCardPanelWrapper().getBillCardPanel().getBillModel().execLoadFormula();
	}
	
	@Override
	public void onBoEdit() throws Exception {
		getBillUI().setBillOperate(IBillOperate.OP_EDIT);
		super.onBoEdit();
		int selectRow = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getOperator(), selectRow, "cmodifyid");//�޸���
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getDate(), selectRow, "dmodifydate");//�޸�����
		getBillCardPanelWrapper().getBillCardPanel().getBillModel().execLoadFormula();
	}
	
	@Override
	protected void onBoSave() throws Exception {
		onCheckBeforeSave();
			super.onBoSave();
		
	}
	
	private void onCheckBeforeSave() throws BusinessException{
	
		int selectRow = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
		if(selectRow<0){
			return;
		}
		Object pk_cust = getBodyCelValue(selectRow,"pk_cust");//��Ӧ��
		Object pk_user = getBodyCelValue(selectRow,"pk_user");//��¼��
		if(isNULL(pk_cust)||isNULL(pk_user) ){
			 throw new BusinessException("��Ӧ�̺͵�½�˶�����Ϊ��");
		}
	}
	private Object getBodyCelValue(int row,String sitemname){
		return getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(row,sitemname);
	}
	
	private boolean isNULL(Object o) {
		if (o == null || o.toString().trim().equals(""))
			return true;
		return false;
	}
}
