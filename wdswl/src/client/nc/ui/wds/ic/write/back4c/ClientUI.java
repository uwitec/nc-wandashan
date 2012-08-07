package nc.ui.wds.ic.write.back4c;
import javax.swing.ListSelectionModel;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wl.pub.MutilChildUI;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.button.ButtonVO;
import nc.vo.wl.pub.Button.CommonButtonDef;

/**
 *  销售出库回传ERP
 * @author Administrator
 * 
 */
public class ClientUI extends MutilChildUI implements BillCardBeforeEditListener{

	private static final long serialVersionUID = -3998675844592858916L;

	public ClientUI() {
		super();
		init();
	}

	private void init() {
		getBillListPanel().getHeadTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
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
	protected void initPrivateButton() {
		super.initPrivateButton();
		CommonButtonDef def = new CommonButtonDef();
		ButtonVO  joinup = def.getJoinUPButton();
		addPrivateButton(joinup);
//		addPrivateButton(getButtonVO());
//		addPrivateButton(getButton1VO());
	}
//	public ButtonVO getButtonVO() {
//		ButtonVO btnVO = new ButtonVO();
//		btnVO.setBtnNo(ISsButtun.all_selected);
//		btnVO.setBtnName("全选");
//		btnVO.setBtnCode("全选");
//		btnVO.setBtnChinaName("全选");
//		return btnVO;
//	}
//	public ButtonVO getButton1VO() {
//		ButtonVO btnVO = new ButtonVO();
//		btnVO.setBtnNo(ISsButtun.all_not_selected);
//		btnVO.setBtnName("全消");
//		btnVO.setBtnCode("全消");
//		btnVO.setBtnChinaName("全消");
//		return btnVO;
//	}
	@Override
	protected void initEventListener() {
		// TODO Auto-generated method stub
		super.initEventListener();
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
	}

	@Override
	public void setDefaultData() throws Exception {

	}

	protected ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this, getUIControl());
	}
	public Object getUserObject() {
		return _getOperator();
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

	public boolean beforeEdit(BillItemEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	protected BusinessDelegator createBusinessDelegator() {
		// TODO Auto-generated method stub
		return new Writeback4cBusinessDelegator();
	}

//	@Override
//	public void mouse_doubleclick(BillMouseEnent e) {
//		if (isListPanelSelected() && e.getPos() == IBillItem.HEAD) {
//			int row = e.getRow();
//			Writeback4cHVO head = (Writeback4cHVO)getBufferData().getCurrentVO().getParentVO();
//			Object value = getBillListPanel().getBillListData().getHeadBillModel().getValueAt(row, "fselect");
//			UFBoolean fselect = PuPubVO.getUFBoolean_NullAs(value, UFBoolean.FALSE);
//			if(!fselect.booleanValue()){
//				getBillListPanel().getBillListData().getHeadBillModel().setValueAt(UFBoolean.TRUE, row, "fselect");
//				head.setFselect(UFBoolean.TRUE);
//			}else{
//				getBillListPanel().getBillListData().getHeadBillModel().setValueAt(null, row, "fselect");
//				head.setFselect(UFBoolean.FALSE);
//
//			}
//		}
//	}


}