package nc.ui.zb.gen;

import java.awt.CardLayout;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.IBillRelaSortListener2;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.zb.gen.GenOrderVO;
import nc.vo.zb.pub.ZbPubConst;

public class GenrationOrderUI extends ToftPanel implements IBillRelaSortListener2{

	//按钮系列   全选 全消 查询 生成合同

	private ButtonObject m_btnAllSel = new ButtonObject("全选","全选",2,"全选");
	private ButtonObject m_btnNoSel = new ButtonObject("全消","全消",2,"全消");
	private ButtonObject m_btnQry = new ButtonObject("查询","查询",2,"查询");
	private ButtonObject m_btnOrder = new ButtonObject("生成合同","生成合同",2,"生成合同");
	private ButtonObject[] m_btnArray1 = null;
	//模板系列
	private BillCardPanel m_panel = null;
	//数据缓存
	private GenOrderVO[] m_billdatas = null;
	
	ClientLink cl = null;
	private GenrationEventHandler m_handler = null;
	

	public GenrationOrderUI() {
		super();
		init();
	}
	private void init(){
		cl = new ClientLink(nc.ui.pub.ClientEnvironment.getInstance());
		setLayout(new java.awt.CardLayout());
		add(getBillPanel(),ZbPubConst.GENORDER_BILLTYPE);
		
		createEventHandler();
		
		setButton();
		
		initListener();	
	}
	
	private void initListener(){
		getBillPanel().addEditListener(m_handler);
		getBillPanel().getBillModel().addSortRelaObjectListener2(this);
	}
	
	private void setButton(){
		m_btnArray1 = new ButtonObject[]{m_btnAllSel,m_btnNoSel,m_btnQry,m_btnOrder};
		
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
			m_panel.loadTemplet(ZbPubConst.GENORDER_BILLTYPE, null,cl.getUser() , cl.getCorp());
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
		m_handler = new GenrationEventHandler(this);
	}
	
	public GenOrderVO[] getDataBuffer(){
		return m_billdatas;
	}
	
	public void setDataBuffer(GenOrderVO[] datas){
		m_billdatas = datas;
	}
	
	void  clearData(){
		getBillPanel().getBillModel().clearBodyData();
		setDataBuffer(null);
	}

	@Override
	public void onButtonClicked(ButtonObject bo) {
		if(bo == m_btnAllSel){
			m_handler.onAllSel();
		}else if(bo == m_btnNoSel){
			m_handler.onNoSel();
		}else if(bo == m_btnQry){
			m_handler.onQuery();
		}else if(bo == m_btnOrder){
			m_handler.onGenOrder();
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
