package nc.ui.wds.tranprice.box;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 *  �����˼۱�
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
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);//���ӱ�ͷ����༭ǰ����
	}

	@Override
	public void setDefaultData() throws Exception {
		getBillCardPanel().setHeadItem("vbillstatus", IBillStatus.FREE);
		getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPk_corp());
		getBillCardPanel().setTailItem("voperatorid", _getOperator());
		getBillCardPanel().setHeadItem("pk_billtype", WdsWlPubConst.WDSJ);
		getBillCardPanel().setTailItem("dmakedate", _getDate());
		getBillCardPanel().setHeadItem("dbilldate", _getDate());
	}

	protected ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this, getUIControl());
	}

	// ���ݺ�
	public String getBillNo() throws java.lang.Exception {
		return HYPubBO_Client.getBillNo(getUIControl().getBillType(),
				_getCorp().getPrimaryKey(), null, null);
	}
	/**
	 * ��ͷ�༭ǰ�¼�
	 */
	public boolean beforeEdit(BillItemEvent e) {
		String key=e.getItem().getKey();
		return true;
	}
	
	@Override
	public boolean beforeEdit(BillEditEvent e) {
		String key=e.getKey();
		int row = e.getRow();
		if("nmaxdistance".equalsIgnoreCase(key)){
			Object o = getBillCardPanel().getBodyValueAt(row, "nmindistance");
			if(o == null){
				showErrorMessage("����������С����");
				return false;
			}
		}
		return super.beforeEdit(e);
	}
	@Override
	public void afterEdit(BillEditEvent e) {
		String key=e.getKey();
		int row=e.getRow();
		if(e.getPos() == BillItem.HEAD){
			if("pk_deptdoc".equalsIgnoreCase(key)){
				getBillCardPanel().getHeadItem("vemployeeid").setValue(null);
			}
			if("vemployeeid".equalsIgnoreCase(key)){
			//ִ�б༭��ʽ
				getBillCardPanel().execHeadTailEditFormulas();
			}
		}else{
		 if("nmaxdistance".equalsIgnoreCase(key)){
				UFDouble nmindistance = PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBodyValueAt((row), "nmindistance"));
				UFDouble nmaxdistance =PuPubVO.getUFDouble_NullAsZero(e.getValue());
				if(nmindistance.sub(nmaxdistance).doubleValue()>=0){
					showWarningMessage("����С����С����");
					getBillCardPanel().setBodyValueAt(e.getOldValue(), row, key);
					return;
				}
			}
		}
		
		super.afterEdit(e);
	}

	
	public Object getUserObject() {
		return null;
	}

	@Override
	public boolean isSaveAndCommitTogether() {
		
		return true;
	}

	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	
		
	}

}