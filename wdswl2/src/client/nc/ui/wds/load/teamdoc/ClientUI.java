package nc.ui.wds.load.teamdoc;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pu.PuPubVO;
//班组档案
public class ClientUI extends BillManageUI {

	private static final long serialVersionUID = 8036305571063747481L;

	@Override
	protected AbstractManageController createController() {
		
		return new MyClientController();
	}
		@Override
	protected ManageEventHandler createEventHandler() {
		
		return new MyEventHandler(this, getUIControl());
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
	protected void initSelfData() {
		ButtonObject btn=getButtonManager().getButton(IBillButton.Line);
		if(btn != null){
		 btn.removeChildButton(getButtonManager().getButton(IBillButton.CopyLine));
		 btn.removeChildButton(getButtonManager().getButton(IBillButton.PasteLine));
		 btn.removeChildButton(getButtonManager().getButton(IBillButton.InsLine));
		}
		
	}

	@Override
	public void setDefaultData() throws Exception {
		getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPk_corp());
	
	}
	@Override
	public Object getUserObject() {		
		return new GetCheckClass();
	}
	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
		String key = e.getKey();
		//最多只有一个人是组长
		if("isteam".equalsIgnoreCase(key)){
			int rowCount = getBillCardPanel().getBillTable().getRowCount();
			for(int i=0;i<rowCount;i++){
				if(i == e.getRow())
					continue;
				UFBoolean isteam = PuPubVO.getUFBoolean_NullAs(getBillCardPanel().getBillModel().getValueAt(i, "isteam"), UFBoolean.FALSE);
				if(isteam.booleanValue()){
					getBillCardPanel().getBillModel().setValueAt(null, e.getRow(), "isteam");
				}
			}
		}
	}
	

}
