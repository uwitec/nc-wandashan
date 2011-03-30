package nc.ui.hg.pu.plan.deal;

import java.awt.CardLayout;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.hg.pu.pub.PlanApplyInforVO;
import nc.ui.hg.pu.pub.PlanPubHelper;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.IBillRelaSortListener2;
import nc.vo.hg.pu.plan.deal.PlanDealVO;
import nc.vo.scm.pub.session.ClientLink;

public class DealPlanUI extends ToftPanel implements IBillRelaSortListener2{

	//按钮系列   全选 全消 查询 汇总 库存平衡  提交  取消  汇总条件设置

	private ButtonObject m_btnAllSel = new ButtonObject("全选","全选",2,"全选");
	private ButtonObject m_btnNoSel = new ButtonObject("全消","全消",2,"全消");
	private ButtonObject m_btnQry = new ButtonObject("查询","查询",2,"查询");
	private ButtonObject m_btnCombin = new ButtonObject("汇总处理","汇总处理",2,"汇总处理");
	private ButtonObject m_btnDeal = new ButtonObject("直接处理","直接处理",2,"直接处理");
	private ButtonObject m_btnDo = new ButtonObject("处理","处理",2,"处理");
	private ButtonObject m_btnBalance = new ButtonObject("库存平衡","库存平衡",2,"库存平衡");
	private ButtonObject m_btnAdjust = new ButtonObject("调整月份分量","调整月份分量",2,"调整月份分量");
	private ButtonObject m_btnCommit = new ButtonObject("确定","确定",2,"确定");
	private ButtonObject m_btnCancel = new ButtonObject("取消","取消",2,"取消");
	//lyf  2011-01-18 回写需求日期
	private ButtonObject m_btnWtriteBack = new ButtonObject("回写到货日期","回写到货日期","回写到货日期");
	
	private ButtonObject[] m_btnArray1 = null;
	private ButtonObject[] m_btnArray2 = null;

	//模板系列
	private BillCardPanel m_panel = null;
	private BillCardPanel m_noSelPanel = null;

	//数据缓存
	private PlanDealVO[] m_billdatas = null;
	
	ClientLink cl = null;
	private DealPlanEventHandler m_handler = null;
	
	PlanApplyInforVO m_appInfor = null;
	//	状态
    private int op_type = 0;//0--查询数据界面   1  汇总平衡界面

	public DealPlanUI() {
		super();
		init();
	}
	private void init(){
		cl = new ClientLink(nc.ui.pub.ClientEnvironment.getInstance());
		setLayout(new java.awt.CardLayout());
		add(getBillPanel(), HgPubConst.PLAN_DEAL_BILLTYPE);
		add(getBillNoSelPanel(), HgPubConst.PLAN_DEAL_BILLTYPE2);
//		getBillPanel().setEnabled(false);
//		getBillNoSelPanel().setEnabled(true);
		
		createEventHandler();
		
		setButton();
		
		initListener();	
		
		initAppInfor();
	}
	

	
	private void initAppInfor(){
		try{
			m_appInfor = PlanPubHelper.getAppInfor(cl.getCorp(), cl.getUser());
			if(m_appInfor!=null)
				m_appInfor.setM_uLogDate(cl.getLogonDate());
		}catch(Exception e){
			e.printStackTrace();//获取申请信息失败  并不影响界面加载
			m_appInfor = null;
		}		
	}
	
	private void initListener(){
		getBillPanel().addEditListener(m_handler);
		getBillPanel().getBillModel().addSortRelaObjectListener2(this);
	}
	
	private void setButton(){
		m_btnDo.addChildButton(m_btnDeal);
		m_btnDo.addChildButton(m_btnCombin);
		m_btnArray1 = new ButtonObject[]{m_btnAllSel,m_btnNoSel,m_btnQry,m_btnDo,m_btnWtriteBack};
		m_btnArray2 = new ButtonObject[]{m_btnCommit,m_btnCancel,m_btnBalance,m_btnAdjust};
		setButtons(m_btnArray1);
	}
	
	private CardLayout getLayOut(){
		return (CardLayout)getLayout();
	}
	
	public void switchUI(){
		if(op_type==0){
			getLayOut().show(this, HgPubConst.PLAN_DEAL_BILLTYPE2);
			setButtons(m_btnArray2);
			op_type = 1;
		}else{
			getLayOut().show(this, HgPubConst.PLAN_DEAL_BILLTYPE);
			setButtons(m_btnArray1);
			op_type = 0;
		}
		updateUI();
	}
	
	public void setBtnEditable(int i ,boolean flag){
		switch(i){
		case 10:
			m_btnWtriteBack.setEnabled(flag);			
			break;
		}
		this.updateButton(m_btnWtriteBack);
	}
	
	public BillCardPanel getBillPanel(){
		if(m_panel == null){
			m_panel = new BillCardPanel();
			m_panel.loadTemplet(HgPubConst.PLAN_DEAL_BILLTYPE, null,cl.getUser() , cl.getCorp());
			m_panel.setEnabled(true);
		}
		return m_panel;
	}

	public BillCardPanel getBillNoSelPanel(){
		if(m_noSelPanel == null){
			m_noSelPanel = new BillCardPanel();
			m_noSelPanel.loadTemplet(HgPubConst.PLAN_DEAL_BILLTYPE2, null,cl.getUser() , cl.getCorp());
			m_noSelPanel.setEnabled(true);
			m_noSelPanel.getBillTable().removeSortListener();
		}
		return m_noSelPanel;
	}
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void createEventHandler(){
		m_handler = new DealPlanEventHandler(this);
	}
	
	public PlanDealVO[] getDataBuffer(){
		return m_billdatas;
	}
	
	public void setDataBuffer(PlanDealVO[] datas){
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
		}else if(bo == m_btnCombin){
			m_handler.onCombin();
		}else if(bo == m_btnCommit){
			m_handler.onCommit();
		}else if(bo == m_btnBalance){
			m_handler.onBalance();
		}else if(bo == m_btnCancel){
			m_handler.onCancel();
		}else if(bo == m_btnDeal){
			m_handler.onDeal();
		}else if(bo == m_btnAdjust){
			m_handler.onAdjust();
		}
		else if(bo == m_btnWtriteBack){
			m_handler.onWriteBack();
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

}
