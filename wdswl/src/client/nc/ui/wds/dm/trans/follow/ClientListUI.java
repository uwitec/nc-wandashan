package nc.ui.wds.dm.trans.follow;

import nc.bs.logging.Logger;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.IListController;
import nc.ui.trade.list.BillListUI;
import nc.ui.trade.list.ListEventHandler;
import nc.ui.trade.multiappinterface.IParent;
import nc.vo.pub.BusinessRuntimeException;

public class ClientListUI extends BillListUI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected IListController createController() {
		// TODO Auto-generated method stub
		return new ClientCtrl();
	}

	@Override
	public void showMe(IParent parent) {
		// TODO Auto-generated method stub
		super.showMe(parent);
		getBufferData().clear();
		
		ClientCartUI uiFirst=(ClientCartUI)getMultiAppManager().getStack().get(0);
		for (int i = 0; i < uiFirst.getBufferData().getVOBufferSize(); i++) {
			getBufferData().addVOToBuffer(uiFirst.getBufferData().getVOByRowNo(i));
		}
		try {
			setListHeadData(getBufferData().getAllHeadVOsFromBuffer());
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessRuntimeException(e.getMessage(),e);
		}
		int row=uiFirst.getBufferData().getCurrentRow();
		if(row>=0){
			getBillListPanel().getHeadTable().getSelectionModel().setSelectionInterval(row, row);
		}else{
			getBillListPanel().getHeadTable().getSelectionModel().setSelectionInterval(-1, -1);
		}
		getBillListWrapper().getBillListPanel().getHeadBillModel().execLoadFormula();
	}

	@Override
	protected ListEventHandler createEventHandler() {
		return EventHandler.getListEventHandler(this,getUIControl());
	}

	@Override
	public void bodyRowChange(BillEditEvent e) {
		super.bodyRowChange(e);
		ClientCartUI uiFirst=(ClientCartUI)getMultiAppManager().getStack().get(0);
		try {
			uiFirst.selectNode(getBufferData().getCurrentVO().getParentVO());
		} catch (Exception e1) {
			Logger.error(e1.getMessage(), e1);
			throw new BusinessRuntimeException(e1.getMessage(),e1);
		}
	}

	@Override
	public String getRefBillType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void initSelfData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDefaultData() throws Exception {
		// TODO Auto-generated method stub
		
	}
}