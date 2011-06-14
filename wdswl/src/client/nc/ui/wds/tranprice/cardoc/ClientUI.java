package nc.ui.wds.tranprice.cardoc;

import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * ³µÁ¾µµ°¸
 * @author mlr
 *
 */

public class ClientUI extends BillManageUI{

	@Override
	protected AbstractManageController createController() {
		
		return new ClientController();
	}

	@Override
	public String getRefBillType() {
		
		return null;
	}

	
	
	@Override
	protected ClientHandler createEventHandler() {
		
		return new ClientHandler(this,getUIControl());
	}

	@Override
	protected void initSelfData() {
		
		
	}
     
	

	@Override
	protected void initPrivateButton() {	
		super.initPrivateButton();
	}

	@Override
	public Object getUserObject() {
		
		return new GetCheckClass();
	}

	@Override
	public void setDefaultData() throws Exception {		
		getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPk_corp());
	}

	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
		
		
	}

	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
		
		
	}

	@Override
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
		
		
	}

	@Override
	public void afterEdit(BillEditEvent e) {
	
		super.afterEdit(e);
       String key=e.getKey();
       if("pk_cartype".equalsIgnoreCase(key)){
    	   getBillCardPanel().execHeadTailEditFormulas();
       }
       if("pk_tanscorp".equalsIgnoreCase(key)){
    	   getBillCardPanel().execHeadTailEditFormulas();
       }
		
	}

	
	
	
	
}
