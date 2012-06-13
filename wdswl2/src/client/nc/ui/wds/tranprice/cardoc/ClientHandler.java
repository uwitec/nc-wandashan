package nc.ui.wds.tranprice.cardoc;

import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.query.HYQueryDLG;
import nc.ui.wl.pub.BaseManageEventHandler;
import nc.vo.wl.pub.WdsWlPubConst;

public class ClientHandler extends BaseManageEventHandler{

	public ClientHandler(BillManageUI billUI, ICardController control) {
		super(billUI, control);
		
	}


	@Override
	protected UIDialog createQueryUI() {
		
		return new HYQueryDLG(getBillUI(), null, getBillUI()._getCorp().getPrimaryKey(), WdsWlPubConst.TRANS_CARDOC_NODECODE, getBillUI()._getOperator(), null);
	}


	//������У���� ����ǰ��У��
	@Override
	protected void beforeSaveValute() {
	
		//У�鷽��
	}
	
	
	
	

}
