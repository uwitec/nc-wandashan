package nc.ui.zb.price.pub;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.IBillRelaSortListener2;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.zb.price.SubmitPriceVO;
import nc.vo.zb.pub.ZbPubConst;

public abstract class AbstractPricePubUI extends ToftPanel implements IBillRelaSortListener2,BillEditListener,BillEditListener2 {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private  BillCardPanel m_dataPanel = null;
	public  ClientLink cl = null;
	
	
	protected ButtonObject m_btnCommit = new ButtonObject(ZbPubConst.BTN_TAB_COMMIT,ZbPubConst.BTN_TAB_COMMIT,2,ZbPubConst.BTN_TAB_COMMIT);
	protected ButtonObject m_btnCancel = new ButtonObject(ZbPubConst.BTN_TAB_CANCEL,ZbPubConst.BTN_TAB_CANCEL,2,ZbPubConst.BTN_TAB_CANCEL);
	protected ButtonObject m_btnRefresh = new ButtonObject(ZbPubConst.BTN_TAB_REFRESH,ZbPubConst.BTN_TAB_REFRESH,2,ZbPubConst.BTN_TAB_REFRESH);
//	protected ButtonObject m_btnFollow = new ButtonObject(ZbPubConst.BTN_TAB_FOLLOW,ZbPubConst.BTN_TAB_FOLLOW,ZbPubConst.BTN_TAB_FOLLOW);
	
	protected PricePubEventHandler m_handler = null;
	
	protected AbstractPriceDataBuffer m_dataBuffer = null;
	
	public AbstractPricePubUI() {
		super();
		init();
	}
	public void setCircalInforToHead(boolean isclear){
		
	}
	public void init(){
		cl = new ClientLink(nc.ui.pub.ClientEnvironment.getInstance());
		setLayout(new java.awt.BorderLayout());
		add(getBillCardPanel(), java.awt.BorderLayout.CENTER);
		createEventHandler();
		createDataBuffer();
		setButton();
		initListener();	
		setButtonState(false);
	}
	protected abstract void createDataBuffer();
	public  AbstractPriceDataBuffer getDataBuffer(){
		return m_dataBuffer;
	}
	public  BillCardPanel getBillCardPanel(){
		if(m_dataPanel == null){
			m_dataPanel = new BillCardPanel();
			m_dataPanel.loadTemplet(getBillType(), null, cl.getUser(), cl.getCorp(),cl.getModuleCode());
			m_dataPanel.setEnabled(true);
			m_dataPanel.setAutoExecHeadEditFormula(true);
		}
		return m_dataPanel;
	}
	
	public  void setButton(){
		this.setButtons(getButtonAry());
	}
	
	public abstract ButtonObject[] getButtonAry();
	
	@Override
	public void onButtonClicked(ButtonObject btn) {
		if(getDataBuffer().getPara()==null&&!btn.getCode().equalsIgnoreCase(ZbPubConst.BTN_TAB_REFRESH))
		
		{
			showErrorMessage("招标系统参数设置为空");
			return;
		}
		m_handler.onButtonClicked(btn.getCode());
	}
		
	public BillModel getDataPanel(){
		return getBillCardPanel().getBillModel();
	}

	public abstract String getBillType();

	public void initListener(){
		getDataPanel().addSortRelaObjectListener2(this);//表体数据排序
		getBillCardPanel().addEditListener(this);//表头编辑后事件
	}
	public void setHeadValue(String fieldname,Object oValue){
		getBillCardPanel().getHeadItem(fieldname).setValue(oValue);
	}
	@Override
	public String getTitle() {
		return "招标报价";
	}
	
	public void bodyRowChange(BillEditEvent e) {

	}


	public SubmitPriceVO[] getPriceDatas() {
		// TODO Auto-generated method stub
		return getDataBuffer().getM_prices();
	}
	
	public void setDataToUI(){
		SubmitPriceVO[] prices = getDataBuffer().getM_prices();
		if(prices == null || prices.length ==0)
			return;
		getDataPanel().setBodyDataVO(prices);
		getDataPanel().execLoadFormula();
	}
	
	public void setButtonState(boolean isEnable){
		m_btnCommit.setEnabled(isEnable);
		m_btnCancel.setEnabled(true);
//		m_btnRefresh.setEnabled(isEnable);
//		if(isfollow){
//			m_btnFollow.setEnabled(true);
//		}else
//			m_btnFollow.setEnabled(false);
		updateButtons();
	}

	public void createEventHandler() {
		// TODO Auto-generated method stub
		m_handler = getEventHandler();
	}

	public abstract PricePubEventHandler getEventHandler();
	
	
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		m_handler.afterEdit(e);
	}
	
	public Object[] getRelaSortObjectArray() {
		// TODO Auto-generated method stub
		return getDataBuffer().getM_prices();
	}
	
	public void clearHeadData(){
		BillItem[] headitmes = getBillCardPanel().getHeadItems();
		for(BillItem item:headitmes){
			item.setValue(null);
		}
	}

	public void clearData() {
		clearHeadData();
		getDataPanel().clearBodyData();
		getDataBuffer().clear();
	}
	public boolean beforeEdit(BillEditEvent e) {
		return true;
	}
}
