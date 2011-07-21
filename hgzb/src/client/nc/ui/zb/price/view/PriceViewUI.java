package nc.ui.zb.price.view;

import java.awt.CardLayout;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.IBillRelaSortListener2;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.zb.price.SubmitPriceVO;
import nc.vo.zb.pub.ZbPubConst;

public class PriceViewUI extends ToftPanel implements IBillRelaSortListener2{

	//按钮系列  查询
	private ButtonObject m_btnQry = new ButtonObject("查询","查询",2,"查询");
	private ButtonObject m_btnPri = new ButtonObject("打印","打印",3,"打印");
	private ButtonObject[] m_btnArray1 = null;
	//模板系列
	private BillCardPanel m_panel = null;
	//数据缓存
	private SubmitPriceVO[] m_billdatas = null;
	
	ClientLink cl = null;
	private PriceViewEventHandler m_handler = null;
	

	public PriceViewUI() {
		super();
		init();
	}
	private void init(){
		cl = new ClientLink(nc.ui.pub.ClientEnvironment.getInstance());
		setLayout(new java.awt.CardLayout());
		add(getBillPanel(),ZbPubConst.ZB_SUBMIT_VIEW);
		
		createEventHandler();
		
		setButton();
		
		initListener();	
	}
	
	private void initListener(){
		getBillPanel().getBillModel().addSortRelaObjectListener2(this);
	}
	
	private void setButton(){
		m_btnArray1 = new ButtonObject[]{m_btnQry,m_btnPri};
		setButtons(m_btnArray1);
	}
	
	private CardLayout getLayOut(){
		return (CardLayout)getLayout();
	}
	
	public void switchUI(){
		
		getLayOut().show(this,ZbPubConst.GENORDER_BILLTYPE);
		setButtons(m_btnArray1);
		updateUI();
	}
	
	public BillCardPanel getBillPanel(){
		if(m_panel == null){
			m_panel = new BillCardPanel();
			m_panel.loadTemplet(ZbPubConst.ZB_SUBMIT_VIEW, null,cl.getUser() , cl.getCorp());
			m_panel.setEnabled(true);
		}
		return m_panel;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void createEventHandler(){
		m_handler = new PriceViewEventHandler(this);
	}
	
	public SubmitPriceVO[] getDataBuffer(){
		return m_billdatas;
	}
	
	public void setDataBuffer(SubmitPriceVO[] datas){
		m_billdatas = datas;
	}
	
	void  clearData(){
		getBillPanel().getBillModel().clearBodyData();
		setDataBuffer(null);
	}

	@Override
	public void onButtonClicked(ButtonObject bo) {
		 if(bo == m_btnQry){
			m_handler.onQuery();
		 }else  if(bo == m_btnPri){
			m_handler.onPrint();
		 }
	}
	public Object[] getRelaSortObjectArray() {
		// TODO Auto-generated method stub
		return getDataBuffer();
	}
	
	public void update(){
		getBillPanel().getBillModel().clearBodyData();
		getBillPanel().getBillModel().setBodyDataVO(getDataBuffer());
		getBillPanel().getBillModel().execLoadFormula();
	}

}

