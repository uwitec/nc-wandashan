package nc.ui.wds.tranprice.fencang;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wds.tranprice.pub.TranPricePubUI;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 *  分仓运价表
 * @author Administrator
 * 
 */
public class ClientUI extends TranPricePubUI{

	private static final long serialVersionUID = -3998675844592858916L;
	
	public ClientUI() {
		super();
	}

	public ClientUI(Boolean useBillSource) {
		super(useBillSource);
	}

	public ClientUI(String pk_corp, String pk_billType, String pk_busitype,
			String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
	}
	@Override
	protected AbstractManageController createController() {
		return new ClientController();
	}

	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
	}

	@Override
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	}

//	@Override
//	protected void initSelfData() {
//		
//		ButtonObject btn = getButtonManager().getButton(IBillButton.Line);
//		if (btn != null) {
//			btn.removeChildButton(getButtonManager().getButton(
//					IBillButton.CopyLine));
//			btn.removeChildButton(getButtonManager().getButton(
//					IBillButton.PasteLine));
//			btn.removeChildButton(getButtonManager().getButton(
//					IBillButton.InsLine));
//		}
//	}
//	@Override
//	protected void initEventListener() {
//		
//		super.initEventListener();
//		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
//	}

//	@Override
//	public void setDefaultData() throws Exception {
//		getBillCardPanel().setHeadItem("vbillstatus", IBillStatus.FREE);
//		getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPk_corp());
//		getBillCardPanel().setTailItem("voperatorid", _getOperator());
//		getBillCardPanel().setHeadItem("pk_billtype", WdsWlPubConst.WDSK);
//		getBillCardPanel().setTailItem("dmakedate", _getDate());
//		getBillCardPanel().setHeadItem("dbilldate", _getDate());
//	}

	protected ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this, getUIControl());
	}

//	// 单据号a
//	public String getBillNo() throws java.lang.Exception {
//		return HYPubBO_Client.getBillNo(getUIControl().getBillType(),
//				_getCorp().getPrimaryKey(), null, null);
//	}
	
//	public Object getUserObject() {
//		return null;
//	}

	
    
	
	
	
	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
		
		
	}

//	public boolean beforeEdit(BillItemEvent e) {
//		// TODO Auto-generated method stub
//		return false;
//	}

}