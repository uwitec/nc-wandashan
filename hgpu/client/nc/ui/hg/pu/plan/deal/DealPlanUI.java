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

	//��ťϵ��   ȫѡ ȫ�� ��ѯ ���� ���ƽ��  �ύ  ȡ��  ������������

	private ButtonObject m_btnAllSel = new ButtonObject("ȫѡ","ȫѡ",2,"ȫѡ");
	private ButtonObject m_btnNoSel = new ButtonObject("ȫ��","ȫ��",2,"ȫ��");
	private ButtonObject m_btnQry = new ButtonObject("��ѯ","��ѯ",2,"��ѯ");
	private ButtonObject m_btnCombin = new ButtonObject("���ܴ���","���ܴ���",2,"���ܴ���");
	private ButtonObject m_btnDeal = new ButtonObject("ֱ�Ӵ���","ֱ�Ӵ���",2,"ֱ�Ӵ���");
	private ButtonObject m_btnDo = new ButtonObject("����","����",2,"����");
	private ButtonObject m_btnBalance = new ButtonObject("���ƽ��","���ƽ��",2,"���ƽ��");
	private ButtonObject m_btnAdjust = new ButtonObject("�����·ݷ���","�����·ݷ���",2,"�����·ݷ���");
	private ButtonObject m_btnCommit = new ButtonObject("ȷ��","ȷ��",2,"ȷ��");
	private ButtonObject m_btnCancel = new ButtonObject("ȡ��","ȡ��",2,"ȡ��");
	//lyf  2011-01-18 ��д��������
	private ButtonObject m_btnWtriteBack = new ButtonObject("��д��������","��д��������","��д��������");
	
	private ButtonObject[] m_btnArray1 = null;
	private ButtonObject[] m_btnArray2 = null;

	//ģ��ϵ��
	private BillCardPanel m_panel = null;
	private BillCardPanel m_noSelPanel = null;

	//���ݻ���
	private PlanDealVO[] m_billdatas = null;
	
	ClientLink cl = null;
	private DealPlanEventHandler m_handler = null;
	
	PlanApplyInforVO m_appInfor = null;
	//	״̬
    private int op_type = 0;//0--��ѯ���ݽ���   1  ����ƽ�����

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
			e.printStackTrace();//��ȡ������Ϣʧ��  ����Ӱ��������
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
