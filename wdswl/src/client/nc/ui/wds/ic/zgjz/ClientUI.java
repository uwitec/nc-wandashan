package nc.ui.wds.ic.zgjz;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 *  暂估处理
 * @author Administrator
 * 
 */
public class ClientUI extends BillManageUI implements BillCardBeforeEditListener{

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

	@Override
	protected void initSelfData() {
		//
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
	@Override
	protected void initEventListener() {
		// TODO Auto-generated method stub
		super.initEventListener();
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
	}
	@Override
	protected void initPrivateButton() {
		// TODO Auto-generated method stub
		super.initPrivateButton();
		ButtonVO soOrder = new ButtonVO();
		soOrder.setBtnNo(nc.ui.wds.w80020206.buttun0206.ISsButtun.Reduc_lastmonth);
		soOrder.setBtnCode(null);
		soOrder.setBtnName("冲本月前");
		soOrder.setBtnChinaName("冲本月前");
		soOrder.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});	
		soOrder.setBusinessStatus(new int[]{IBillStatus.FREE});
		addPrivateButton(soOrder);
	}

	@Override
	public void setDefaultData() throws Exception {
		getBillCardPanel().setHeadItem("vbillstatus", IBillStatus.FREE);
		getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPk_corp());
		getBillCardPanel().setHeadItem("pk_billtype", WdsWlPubConst.WDSQ);
		getBillCardPanel().setTailItem("voperatorid", _getOperator());
		getBillCardPanel().setTailItem("dmakedate", _getDate());	
	}

	protected ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this, getUIControl());
	}

	// 单据号
	public String getBillNo() throws java.lang.Exception {
		return HYPubBO_Client.getBillNo(getUIControl().getBillType(),
				_getCorp().getPrimaryKey(), null, null);
	}
	public boolean beforeEdit(BillItemEvent e) {	
		return true;
	}
	
	@Override
	public boolean beforeEdit(BillEditEvent e) {
		return super.beforeEdit(e);
	}
	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
	}

	/**
	 * 增加后台校验
	 */
	public Object getUserObject() {
		return null;
	}

	@Override
	public boolean isSaveAndCommitTogether() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}