package nc.ui.hg.pu.check.oldmaterials;

import nc.ui.hg.pu.pub.DBTManageEventHandler;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.scm.pu.PuPubVO;

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
	protected void onBoSave() throws Exception {
		onCheckBeforeSave();
			super.onBoSave();
		
	}
	
	private void onCheckBeforeSave() throws BusinessException{
		int selectRow = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
		if(selectRow<0){
			return;
		}
		String oldid = PuPubVO.getString_TrimZeroLenAsNull(getBodyCelValue(selectRow,"coldmanid"));
		String newid = PuPubVO.getString_TrimZeroLenAsNull(getBodyCelValue(selectRow,"cnewmanid"));
		if(!isNULL(oldid) && !isNULL(newid)){
		    if(oldid.equalsIgnoreCase(newid)){
			    throw new BusinessException("���������������ʲ���Ϊͬһ�ֲ�Ʒ��������ѡ��");
		   }
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
	@Override
	public void onBoEdit() throws Exception {
		super.onBoEdit();
		int selectRow = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getOperator(), selectRow, "cmodifyman");//�޸���
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getDate(), selectRow, "dmodifydate");//�޸�����
		getBillCardPanelWrapper().getBillCardPanel().getBillModel().execLoadFormula();
	}
	
	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		getBillUI().setBillOperate(IBillOperate.OP_EDIT);
		onBoLineAdd();
		int selectRow = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
		BillModel model = getBillCardPanelWrapper().getBillCardPanel().getBodyPanel().getTableModel();
		model.setRowEditState(true);
		model.setEditRow(selectRow);
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getOperator(), selectRow, "coperatorid");//¼����
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getDate(), selectRow, "dmakedate");//¼������
		getBillCardPanelWrapper().getBillCardPanel().getBillModel().execLoadFormula();
	}
}
