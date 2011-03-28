package nc.ui.dm.plan;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wl.pub.LoginInforHelper;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 *  ���˼ƻ�¼�� 
 * @author Administrator
 * 
 */
public class ClientUI extends BillManageUI {

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
	public void setDefaultData() throws Exception {
		getBillCardPanel().setHeadItem("iplantype", 0);//�ƻ�����Ĭ���¼ƻ�
		getBillCardPanel().setHeadItem("vbillstatus", IBillStatus.FREE);
		getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPk_corp());
		getBillCardPanel().setTailItem("voperatorid", _getOperator());
		getBillCardPanel().setHeadItem("pk_billtype", WdsWlPubConst.WDS1);
		getBillCardPanel().setTailItem("dmakedate", _getDate());		
		getBillCardPanel().setHeadItem("pk_outwhouse", //�����ֿ�Ĭ�ϵ�ǰ��¼�˹����Ĳֿ�				
				LoginInforHelper.getLogInfor(_getOperator()).getWhid());
		
		
		
	}

	protected ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this, getUIControl());
	}

	// ���ݺ�
	public String getBillNo() throws java.lang.Exception {
		return HYPubBO_Client.getBillNo(getUIControl().getBillType(),
				_getCorp().getPrimaryKey(), null, null);
	}

	protected BusinessDelegator createBusinessDelegator() {
		return new ClientBusinessDelegator(this);
	}

	@Override
	public boolean beforeEdit(BillEditEvent e) {
		return super.beforeEdit(e);
	}
		@Override
	public void afterEdit(BillEditEvent e) {
			super.afterEdit(e);
			String key = e.getKey();
			Object value =e.getValue();
			Object oldValue = e.getOldValue();
			if(e.getPos() == BillItem.HEAD){
				if("pk_inwhouse".equalsIgnoreCase(key)){
					Object pk_inwhouse = getBillCardPanel().getHeadItem("pk_inwhouse").getValueObject();
					Object pk_outwhouse=PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem("pk_outwhouse").getValueObject());
					if(pk_outwhouse == null){
						showErrorMessage("��ǰ��¼��û�а󶨲ֿ�");
					}
					if(pk_outwhouse.equals(pk_inwhouse)){
						showWarningMessage("����ֿⲻ�ܺ͵����ֿ���ͬ");
						getBillCardPanel().setHeadItem("pk_inwhouse", null);
					}
				}
			}else{
				
			}
	}

	/**
	 * ���Ӻ�̨У��
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