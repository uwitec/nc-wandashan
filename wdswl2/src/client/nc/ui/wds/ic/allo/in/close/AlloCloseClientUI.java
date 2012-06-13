package nc.ui.wds.ic.allo.in.close;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.IBillModelRowStateChangeEventListener;
import nc.ui.pub.bill.RowStateChangeEvent;
import nc.ui.wl.pub.LoginInforHelper;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * �������ر� ���ر�erp�������ⵥ�������������־��
 * ����������ڵ������ⵥ������һ���ֶΣ�bisclose  Y��ת�����ر� N��ת��������
 * 
 * @author Administrator
 * 
 */
public class AlloCloseClientUI extends ToftPanel implements BillEditListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// ���尴ť
	private ButtonObject m_btnQry = new ButtonObject(
			WdsWlPubConst.DM_PLANDEAL_BTNTAG_QRY,
			WdsWlPubConst.DM_PLANDEAL_BTNTAG_QRY, 2,
			WdsWlPubConst.DM_PLANDEAL_BTNTAG_QRY);

	private ButtonObject m_btnSelAll = new ButtonObject(
			WdsWlPubConst.DM_PLANDEAL_BTNTAG_SELALL,
			WdsWlPubConst.DM_PLANDEAL_BTNTAG_SELALL, 2,
			WdsWlPubConst.DM_PLANDEAL_BTNTAG_SELALL);

	private ButtonObject m_btnSelno = new ButtonObject(
			WdsWlPubConst.DM_PLANDEAL_BTNTAG_SELNO,
			WdsWlPubConst.DM_PLANDEAL_BTNTAG_SELNO, 2,
			WdsWlPubConst.DM_PLANDEAL_BTNTAG_SELNO);

	//	zhf add  ֧�����۶��������ر� �� ��
	private ButtonObject m_btnClose = new ButtonObject(
			"�ر�",
			"�ر�", 2,
	"�ر�");

	private ButtonObject m_btnOpen = new ButtonObject(
			"��",
			"��", 2,
	"��");

	protected ClientEnvironment m_ce = null;
	protected ClientLink cl = null;
	private AlloCloseEventHandler event = null;

	// �������ģ��
	private BillListPanel m_panel = null;

	// ��ť�¼�����
	private LoginInforHelper helper = null;


	private String cwhid;//��ǰ��¼�ͻ������ֿ�

	public String getWhid(){
		return cwhid;
	}

	public LoginInforHelper getLoginInforHelper() {
		if (helper == null) {
			helper = new LoginInforHelper();
		}
		return helper;
	}

	public AlloCloseClientUI() {
		super();
		m_ce = ClientEnvironment.getInstance();
		cl = new ClientLink(m_ce);
		init();
	}

	public AlloCloseClientUI(String pk_corp, String pk_billType,
			String pk_busitype, String operater, String billId) {
		super();
		m_ce = ClientEnvironment.getInstance();
		cl = new ClientLink(m_ce);
		init();
	}
	private void init() {
		setLayout(new java.awt.CardLayout());
		add(getPanel(), "a");
		createEventHandler();
		setButton();
		initListener();
		try {
			cwhid  = new LoginInforHelper().getLogInfor(m_ce.getUser().getPrimaryKey()).getWhid();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cwhid = null;
		}
	}

	private void initListener() {
		getPanel().addEditListener(this);
//		getPanel().getParentListPanel().addEditListener2(this);
		getPanel().getHeadBillModel().addRowStateChangeEventListener(new HeadRowStateListener());
	}


	protected BillListPanel getPanel() {
		if (m_panel == null) {
			m_panel = new BillListPanel();
			m_panel.loadTemplet(WdsWlPubConst.BILLTYPE_ALLO_IN_CLOSE, null, m_ce.getUser()
					.getPrimaryKey(), m_ce.getCorporation().getPrimaryKey());
			m_panel.setEnabled(true);
			m_panel.getChildListPanel().setTotalRowShow(true);
			m_panel.setMultiSelect(true);			
			m_panel.getHeadTable().removeSortListener();
			m_panel.setEnabled(false);
		}
		return m_panel;
	}


	public void headRowChange(int iNewRow) {
		event.m_headrow = iNewRow;
		if (!getPanel().setBodyModelData(iNewRow)) {
			//1.���������������
			loadBodyData(iNewRow);
			//2.���ݵ�ģ����
			getPanel().setBodyModelDataCopy(iNewRow);
		}
		getPanel().repaint();
	}

	private void loadBodyData(int row){
		getPanel().getBodyBillModel().clearBodyData();
		//		String key = (String)getPanel().getHeadBillModel().getValueAt(row, "vreceiptcode");		
		getPanel().getBodyBillModel().setBodyDataVO( event.getCurrBodys());//���ñ���
		getPanel().getBodyBillModel().execLoadFormula();
	}


	private class HeadRowStateListener implements IBillModelRowStateChangeEventListener {
		public void valueChanged(RowStateChangeEvent e) {
			if (e.getRow() != getPanel().getHeadTable().getSelectedRow()) {
				headRowChange(e.getRow());
			}
			BillModel model = getPanel().getBodyBillModel();
			IBillModelRowStateChangeEventListener l = model.getRowStateChangeEventListener();
			model.removeRowStateChangeEventListener();
			if (e.isSelectState()) {
				getPanel().getChildListPanel().selectAllTableRow();
			} else {
				getPanel().getChildListPanel().cancelSelectAllTableRow();
			}
			model.addRowStateChangeEventListener(l);
			getPanel().updateUI();
		}

	}


	private void setButton() {
		ButtonObject[] m_objs = new ButtonObject[] { 
				m_btnQry,m_btnSelAll,m_btnSelno,m_btnClose,m_btnOpen};
		this.setButtons(m_objs);
	}

	private void createEventHandler() {
		event = new AlloCloseEventHandler(this);
	}


	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onButtonClicked(ButtonObject btn) {
		// TODO Auto-generated method stub
		event.onButtonClicked(btn.getCode());
	}


	public void updateButtonStatus(String btnTag, boolean flag) {
		UFBoolean isclose = event.getOrderType();
		m_btnClose.setEnabled(!isclose.booleanValue());
		m_btnOpen.setEnabled(isclose.booleanValue());
		updateButtons();
	}
	//��ͷ���л��¼�
	public void bodyRowChange(BillEditEvent e) {
		// TODO Auto-generated method stub
		if(e.getRow()<0)
			return;
		e.getValue();
		headRowChange(e.getRow());
		getPanel().getBodyBillModel().reCalcurateAll();	
	}


	public ClientLink getCl() {
		return cl;
	}

	public void setCl(ClientLink cl) {
		this.cl = cl;
	}

	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub

	}
}
