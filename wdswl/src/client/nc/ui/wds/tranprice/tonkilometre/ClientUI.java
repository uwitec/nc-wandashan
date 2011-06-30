package nc.ui.wds.tranprice.tonkilometre;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wds.tranprice.pub.TranPricePubUI;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pu.PuPubVO;

/**
 *  �ֹ����˼۱�
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
//		
////		��ʼ��ǰ��¼�� ��ɫ
//		LoginInforHelper login = new LoginInforHelper();
//		try {
//			m_loginInfor = login.getLogInfor(_getOperator());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			m_loginInfor = null;
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
//		getBillCardPanel().setHeadItem("pk_billtype", WdsWlPubConst.WDSI);
//		getBillCardPanel().setTailItem("dmakedate", _getDate());
//		getBillCardPanel().setHeadItem("dbilldate", _getDate());
////		zhf add  ��ʼ��  Ĭ�� �����ֿ� 
//		getBillCardPanel().setHeadItem("reserve1", m_loginInfor.getWhid());
//	}

	protected ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this, getUIControl());
	}

	
//	public boolean beforeEdit(BillItemEvent e) {
////		String key=e.getItem().getKey();
//		return true;
//	}
	
//	@Override
//	public boolean beforeEdit(BillEditEvent e) {
//		String key=e.getKey();
//		int row = e.getRow();
//		
//		return super.beforeEdit(e);		
//	}
	@Override
	public void afterEdit(BillEditEvent e) {
		String key=e.getKey();
		if(e.getPos() == BillItem.HEAD){
			if("pk_deptdoc".equalsIgnoreCase(key)){
				getBillCardPanel().getHeadItem("vemployeeid").setValue(null);
			}
			if("vemployeeid".equalsIgnoreCase(key)){
			//ִ�б༭��ʽ
			getBillCardPanel().execHeadTailEditFormulas();
			}
			//
			if("fiseffect".equalsIgnoreCase(key)){
				UFBoolean value =PuPubVO.getUFBoolean_NullAs(e.getValue(), UFBoolean.FALSE);
				if(value.equals(UFBoolean.TRUE)){
					
				}else{
					
				}
			}
			//�Ƿ���� �� �Ƿ��в���۸񱣳�һ��
			else if("fiseffect".equalsIgnoreCase(key)){
				if(!PuPubVO.getUFBoolean_NullAs(e.getValue(), UFBoolean.FALSE).booleanValue()){
					getBillCardPanel().setHeadItem("reserve6", null);
				}
			}else if("reserve6".equalsIgnoreCase(key)){
				if(PuPubVO.getString_TrimZeroLenAsNull( e.getValue())!=null){
					getBillCardPanel().setHeadItem("fiseffect", UFBoolean.TRUE);
				}
			}
		}
		super.afterEdit(e);
	}

//	
//	public Object getUserObject() {
//		return null;
//	}

//	@Override
//	public boolean isSaveAndCommitTogether() {
//		
//		return true;
//	}
    
	
	
	
	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
		
		
	}

}