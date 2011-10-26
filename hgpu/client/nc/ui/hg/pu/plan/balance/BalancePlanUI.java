package nc.ui.hg.pu.plan.balance;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.IBillRelaSortListener2;
import nc.vo.hg.pu.plan.balance.PlanMonDealVO;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.scm.pub.session.ClientLink;

public class BalancePlanUI extends ToftPanel implements IBillRelaSortListener2{

	private ButtonObject m_btnAllSel = new ButtonObject("ȫѡ","ȫѡ",2,"ȫѡ");
	private ButtonObject m_btnNoSel = new ButtonObject("ȫ��","ȫ��",2,"ȫ��");
	private ButtonObject m_btnQry = new ButtonObject("��ѯ","��ѯ",2,"��ѯ");
	private ButtonObject m_btnBalance = new ButtonObject("ƽ��","ƽ��",2,"ƽ��");
	private ButtonObject m_btnUnBalance = new ButtonObject("ȡ��ƽ��","ȡ��ƽ��",2,"ȡ��ƽ��");
	private ButtonObject m_btnADUT = new ButtonObject("����","����",2,"����");
	private ButtonObject m_btnUnAudit = new ButtonObject("ȡ������","ȡ������",2,"ȡ������");
	private ButtonObject m_btnView = new ButtonObject("�鿴","�鿴",2,"�鿴");
//	private ButtonObject m_btnCancel = new ButtonObject("ȡ��","ȡ��",2,"ȡ��");
	
	private ButtonObject[] m_btnArray1 = null;
//	private ButtonObject[] m_btnArray2 = null;
	//ģ��ϵ��
	private BillCardPanel m_panel = null;
//	private BillCardPanel m_noSelPanel = null;

	//���ݻ���
	private PlanMonDealVO[] m_billdatas = null;
	
	ClientLink cl = null;
	private BalancePlanEventHandler m_handler = null;
	
//	PlanApplyInforVO m_appInfor = null;
//	״̬
//    private int op_type = 0;//0--��ѯ���ݽ���   1  ����ƽ�����
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
//			e.printStackTrace();//��ȡ������Ϣʧ��  ����Ӱ��������
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
