package nc.ui.wds.tranprice.freight;

import nc.ui.pub.ButtonObject;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.wds.tranprice.freight.GetCheck;

public class MyClientUI extends BillManageUI{

	/**
	 * �ۺϱ�׼
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected AbstractManageController createController() {
		return new MyControl();
	}
	
	protected ManageEventHandler createEventHandler(){
		return new MyEventHandler(this,getUIControl());
	}

	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initSelfData() {
		ButtonObject btn = getButtonManager().getButton(IBillButton.Line);
		if (btn != null) {
			btn.removeChildButton(getButtonManager().getButton(
					IBillButton.CopyLine));
			btn.removeChildButton(getButtonManager().getButton(
					IBillButton.PasteLine));
			btn.removeChildButton(getButtonManager().getButton(
					IBillButton.InsLine));
		}
		
	}

//	protected String getBillNo() throws Exception {
//		return HYPubBO_Client.getBillNo(WdsWlPubConst.ZHBZ,_getOperator(),null, null);
//	}
	
	@Override
	public void setDefaultData() throws Exception {
		getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPk_corp());
	//	getBillCardPanel().getHeadItem("pk_corp").setValue(getCorpPrimaryKey());	
	//	getBillCardPanel().getHeadItem("pk_billtype").setValue(WdsWlPubConst.LM_CHENGJI_BILLTYPE);
	//	getBillCardPanel().setHeadItem("vbillstatus",IBillStatus.FREE);//����״̬
		getBillCardPanel().getHeadItem("dbilldate").setValue(_getDate());
		getBillCardPanel().getTailItem("voperatorid").setValue(_getOperator());
		getBillCardPanel().getTailItem("dmakedate").setValue(_getDate());
		
	}
	//У��
	public Object getUserObject() {
		return new GetCheck();
	}


}
