package nc.ui.wds2.set;

import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.card.BillCardUI;
import nc.ui.zmpub.pub.bill.DBTManageEventHandler;
import nc.uif.pub.exception.UifException;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.wds2.set.OutInSetVO;

public class OutInSetEvent extends DBTManageEventHandler {

	public OutInSetEvent(BillCardUI billUI, ICardController control) {
		super(billUI, control);
	}
	
	protected void onBoLineAdd() throws Exception {
		super.onBoLineAdd();
		int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
		if(row < 0)
			return;
		getBillCardPanelWrapper().getBillCardPanel().getBillModel().setValueAt(_getCorp().getPrimaryKey(), row, "pk_corp");
	}
	
	protected void onBoDelete() throws Exception {
		int selectRow = getBillCardPanelWrapper().getBillCardPanel()
				.getBillTable().getSelectedRow();
		if (selectRow < 0) {
			MessageDialog.showHintDlg(getBillUI(), "删除", "请选中要删除的行!");
			return;
		}
		if (MessageDialog.showYesNoDlg(getBillUI(), "删除", "是否确认删除选中的数据?") != UIDialog.ID_YES) {
			return;
		}
		
		OutInSetVO vo = (OutInSetVO)getBillCardPanelWrapper().getBillCardPanel()
		.getBillModel().getBodyValueRowVO(selectRow, OutInSetVO.class.getName());
		
		HYPubBO_Client.delete(vo);
		
		onBoRefresh();
	}
	
	protected void onBoRefresh() throws Exception {

		SuperVO[] vos = null;
		try {
			vos = HYPubBO_Client.queryByCondition(OutInSetVO.class, 
					" pk_corp = '"+_getCorp().getPrimaryKey()+"'");
		} catch (UifException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getBillCardPanelWrapper().getBillCardPanel().getBillModel().setBodyDataVO(vos);
		getBillCardPanelWrapper().getBillCardPanel().getBillModel().execLoadFormula();
		HYBillVO bill = new HYBillVO();
		bill.setChildrenVO(vos);
		getBufferData().addVOToBuffer(bill);
	
	}

}
