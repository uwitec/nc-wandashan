package nc.ui.hg.pu.plan.balance;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.IBillRelaSortListener2;
import nc.vo.hg.pu.plan.balance.PlanMonDealVO;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.scm.pub.session.ClientLink;

public class BalancePlanUI extends ToftPanel implements IBillRelaSortListener2{

	private ButtonObject m_btnAllSel = new ButtonObject("全选","全选",2,"全选");
	private ButtonObject m_btnNoSel = new ButtonObject("全消","全消",2,"全消");
	private ButtonObject m_btnQry = new ButtonObject("查询","查询",2,"查询");
	private ButtonObject m_btnBalance = new ButtonObject("平衡","平衡",2,"平衡");
	private ButtonObject m_btnUnBalance = new ButtonObject("取消平衡","取消平衡",2,"取消平衡");
	private ButtonObject m_btnADUT = new ButtonObject("审批","审批",2,"审批");
	private ButtonObject m_btnUnAudit = new ButtonObject("取消审批","取消审批",2,"取消审批");
	private ButtonObject m_btnView = new ButtonObject("查看","查看",2,"查看");
//	private ButtonObject m_btnCancel = new ButtonObject("取消","取消",2,"取消");
	
	private ButtonObject[] m_btnArray1 = null;
//	private ButtonObject[] m_btnArray2 = null;
	//模板系列
	private BillCardPanel m_panel = null;
//	private BillCardPanel m_noSelPanel = null;

	//数据缓存
	private PlanMonDealVO[] m_billdatas = null;
	
	ClientLink cl = null;
	private BalancePlanEventHandler m_handler = null;
	
//	PlanApplyInforVO m_appInfor = null;
//	状态
//    private int op_type = 0;//0--查询数据界面   1  汇总平衡界面
	public BalancePlanUI() {
		super();
		init();
	}
	private void init(){
		cl = new ClientLink(nc.ui.pub.ClientEnvironment.getInstance());
		setLayout(new java.awt.CardLayout());
		add(getBillPanel(),"aa");
//		add(getBillNoSelPanel(), HgPubConst.PLAN_Balance_BILLTYPE1);
		createEventHandler();
		
		setButton();
		
		initListener();	
		getBillPanel().setTatolRowShow(true);//
//		initAppInfor();
	}
	
	public void setButtonSatus(String flag){
		if("deal".equalsIgnoreCase(flag)){
			m_btnBalance.setEnabled(false);
			m_btnUnBalance.setEnabled(true);
			m_btnADUT.setEnabled(true);
			m_btnUnAudit.setEnabled(false);
		}else if("undeal".equalsIgnoreCase(flag)){
			m_btnBalance.setEnabled(true);
			m_btnUnBalance.setEnabled(false);
			m_btnADUT.setEnabled(false);
			m_btnUnAudit.setEnabled(false);
		}else if("audit".equalsIgnoreCase(flag)){
			m_btnBalance.setEnabled(false);
			m_btnUnBalance.setEnabled(false);
			m_btnADUT.setEnabled(false);
			m_btnUnAudit.setEnabled(true);
		}
		
		updateButtons();
	}

	
//	private void initAppInfor(){
//		try{
//			m_appInfor = PlanPubHelper.getAppInfor(cl.getCorp(), cl.getUser());
//			if(m_appInfor!=null)
//				m_appInfor.setM_uLogDate(cl.getLogonDate());
//		}catch(Exception e){
//			e.printStackTrace();//获取申请信息失败  并不影响界面加载
//			m_appInfor = null;
//		}		
//	}
	
	private void initListener(){
		getBillPanel().addEditListener(m_handler);
		getBillPanel().getBillModel().addSortRelaObjectListener2(this);
	}
	
	private void setButton(){
		m_btnArray1 = new ButtonObject[]{m_btnAllSel,m_btnNoSel,m_btnQry,m_btnBalance,m_btnUnBalance,m_btnADUT,m_btnUnAudit,m_btnView};
		setButtons(m_btnArray1);
	}


	
	public BillCardPanel getBillPanel(){
		if(m_panel == null){
			m_panel = new BillCardPanel();
			m_panel.loadTemplet(HgPubConst.PLAN_Balance_BILLTYPE, null,cl.getUser() , cl.getCorp());
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
		m_handler = new BalancePlanEventHandler(this);
	}
	
	public PlanMonDealVO[] getDataBuffer(){
		return m_billdatas;
	}
	
	public void setDataBuffer(PlanMonDealVO[] datas){
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
		}else if(bo == m_btnBalance){
			m_handler.onBalance();
		}else if(bo ==  m_btnADUT){
			m_handler.onAduit();
		}else if(bo == m_btnUnAudit){
			m_handler.onUnAduit();
		}
		else if(bo == m_btnView){
			m_handler.onView();
		}
//		else if(bo == m_btnCancel){
//			m_handler.onCancel();
//		}
		else if(bo == m_btnUnBalance){
			m_handler.onUnBalance();
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
//		updateUI();
	}
//	public BillCardPanel getBillNoSelPanel(){
//		if(m_noSelPanel == null){
//			m_noSelPanel = new BillCardPanel();
//			m_noSelPanel.loadTemplet(HgPubConst.PLAN_Balance_BILLTYPE1, null,cl.getUser() , cl.getCorp());
//			m_noSelPanel.setEnabled(true);
//			m_noSelPanel.getBillTable().removeSortListener();
//		}
//		return m_noSelPanel;
//	}
}
