package nc.ui.wds.ic.write.back4y;

import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wl.pub.MutilChildUI;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.button.ButtonVO;
import nc.vo.wl.pub.Button.CommonButtonDef;

public class ClientUI extends MutilChildUI implements BillCardBeforeEditListener{
	/**
	 * 调拨入库回传ERP
	 */
	private static final long serialVersionUID = -7171216648620033184L;

	
	public ClientUI() {
		super();
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
	protected ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this,getUIControl());
	}
	
	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
		super.setHeadSpecialData(vo, intRow);
	}

	@Override
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
		super.setTotalHeadSpecialData(vos);
	}
	
	
	@Override
	protected void initSelfData() {
		super.initSelfData();
	}
	
	@Override
	protected void initEventListener() {
		super.initEventListener();
	}
	@Override
	public boolean isSaveAndCommitTogether() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void setDefaultData() throws Exception {
		super.setDefaultData();
	}

	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
		super.setBodySpecialData(vos);
	}

	@Override
	protected BusinessDelegator createBusinessDelegator() {
		return new Writeback4yBusinessDelegator();
	}
	
	/*
	 * 增加后台校验
	 */
	public Object getUserObject(){
		return _getOperator();
	}

	public boolean beforeEdit(BillItemEvent e) {
		return false;
	}

}
