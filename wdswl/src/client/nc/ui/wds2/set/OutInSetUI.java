package nc.ui.wds2.set;

import nc.ui.trade.bill.ICardController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.uif.pub.exception.UifException;
import nc.vo.pub.SuperVO;
import nc.vo.wds2.set.OutInSetVO;

public class OutInSetUI extends BillCardUI {
	
	public OutInSetUI(){
		super();
		init();
	}
	private void init(){
		SuperVO[] vos = null;
		try {
			vos = HYPubBO_Client.queryByCondition(OutInSetVO.class, 
					" pk_corp = '"+_getCorp().getPrimaryKey()+"'");
		} catch (UifException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getBillCardPanel().getBillModel().setBodyDataVO(vos);
		getBillCardPanel().getBillModel().execLoadFormula();
	}

	@Override
	protected ICardController createController() {
		// TODO Auto-generated method stub
		return new OutInSetCtrl();
	}
	
	protected CardEventHandler createEventHandler() {
		return new OutInSetEvent(this,this.getUIControl());
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
