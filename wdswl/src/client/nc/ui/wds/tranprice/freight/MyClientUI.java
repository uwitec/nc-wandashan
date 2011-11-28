package nc.ui.wds.tranprice.freight;

import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wds.tranprice.freight.ZhbzBVO;
import nc.vo.wl.pub.WdsWlPubConst;

public class MyClientUI extends BillManageUI{

	/**
	 * 折合标准
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
		// TODO Auto-generated method stub
		
	}

//	protected String getBillNo() throws Exception {
//		return HYPubBO_Client.getBillNo(WdsWlPubConst.ZHBZ,_getOperator(),null, null);
//	}
	
	@Override
	public void setDefaultData() throws Exception {
		getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPk_corp());
	//	getBillCardPanel().getHeadItem("pk_corp").setValue(getCorpPrimaryKey());	
	//	getBillCardPanel().getHeadItem("pk_billtype").setValue(WdsWlPubConst.LM_CHENGJI_BILLTYPE);
	//	getBillCardPanel().setHeadItem("vbillstatus",IBillStatus.FREE);//单据状态
		getBillCardPanel().getHeadItem("dbilldate").setValue(_getDate());
		getBillCardPanel().getTailItem("voperatorid").setValue(_getOperator());
		getBillCardPanel().getTailItem("dmakedate").setValue(_getDate());
		
	}
	//校验
	public Object getUserObject() {
		return new GetCheck();
	}


}
