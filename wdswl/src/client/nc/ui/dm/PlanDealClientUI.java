package nc.ui.dm;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.bill.BillListPanel;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * 发运计划安排
 * @author Administrator
 *
 */
public class PlanDealClientUI extends ToftPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//定义按钮
	private ButtonObject m_btnQry = new ButtonObject(WdsWlPubConst.DM_PLANDEAL_BTNTAG_QRY,WdsWlPubConst.DM_PLANDEAL_BTNTAG_QRY,2,WdsWlPubConst.DM_PLANDEAL_BTNTAG_QRY);
	private ButtonObject m_btnDeal = new ButtonObject(WdsWlPubConst.DM_PLANDEAL_BTNTAG_DEAL,WdsWlPubConst.DM_PLANDEAL_BTNTAG_DEAL,2,WdsWlPubConst.DM_PLANDEAL_BTNTAG_DEAL);
	private ButtonObject m_btnSelAll = new ButtonObject(WdsWlPubConst.DM_PLANDEAL_BTNTAG_SELALL,WdsWlPubConst.DM_PLANDEAL_BTNTAG_SELALL,2,WdsWlPubConst.DM_PLANDEAL_BTNTAG_SELALL);
	private ButtonObject m_btnSelno = new ButtonObject(WdsWlPubConst.DM_PLANDEAL_BTNTAG_SELNO,WdsWlPubConst.DM_PLANDEAL_BTNTAG_SELNO,2,WdsWlPubConst.DM_PLANDEAL_BTNTAG_SELNO);
	private ButtonObject m_btnXnDeal=new ButtonObject(WdsWlPubConst.DM_PLANDEAL_BTNTAG_XNDEAL,WdsWlPubConst.DM_PLANDEAL_BTNTAG_XNDEAL,2,WdsWlPubConst.DM_PLANDEAL_BTNTAG_XNDEAL);
	
	protected ClientEnvironment m_ce = null;
	protected ClientLink cl=null;
	private PlanDealEventHandler m_handler = null;
	
	//定义界面模板
	
	private BillListPanel m_panel = null;
	
	//按钮事件处理
	
	
	public PlanDealClientUI(){
		super();
		m_ce = ClientEnvironment.getInstance();
		cl =new ClientLink(m_ce);
		init();
	}
	
	protected BillListPanel getPanel(){
		if(m_panel == null){
			m_panel = new BillListPanel();
			m_panel.loadTemplet(WdsWlPubConst.DM_PLAN_DEAL_BILLTYPE, null, m_ce.getUser().getPrimaryKey(), m_ce.getCorporation().getPrimaryKey()
					);
			m_panel.setEnabled(true);
			m_panel.getParentListPanel().setTotalRowShow(true);
		}
		return m_panel;
	}
//	public BillListPanel getBillListPanel(){
//		return m_panel;
//	}
	public ClientEnvironment getEviment(){
		return m_ce;
	}
	
	private void init(){
		setLayout(new java.awt.CardLayout());
		add(getPanel(),"a");

		createEventHandler();

		setButton();

		initListener();	
	}
	private void initListener(){
//		getPanel().addBodyEditListener(this);
		getPanel().addEditListener(m_handler);
	}
	
	private void setButton(){
		ButtonObject[] m_objs = new ButtonObject[]{m_btnSelAll,m_btnSelno,m_btnQry,m_btnDeal,m_btnXnDeal};
		this.setButtons(m_objs);
	}
	private void createEventHandler(){
		
			m_handler = new PlanDealEventHandler(this);
		
	}
	

	@Override
	public String getTitle() {
		
		return null;
	}

	@Override
	public void onButtonClicked(ButtonObject btn) {
		m_handler.onButtonClicked(btn.getCode());
	}
	public void updateButtonStatus(String btnTag,boolean flag){
		if(btnTag.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_DEAL)){
			m_btnDeal.setEnabled(flag);
		}else if(btnTag.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_SELNO)){
			m_btnSelno.setEnabled(flag);
		}else if(btnTag.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_SELALL)){
			m_btnSelAll.setEnabled(flag);
		}else if(btnTag.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_QRY)){
			m_btnQry.setEnabled(flag);
		}else if(btnTag.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_XNDEAL)){
			m_btnXnDeal.setEnabled(flag);	
		}
		updateButtons();
	}

}
