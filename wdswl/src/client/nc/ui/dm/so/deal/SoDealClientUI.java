package nc.ui.dm.so.deal;

import javax.swing.JComponent;

import nc.bs.logging.Logger;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.wl.pub.LoginInforHelper;
import nc.vo.dm.so.deal.SoDealVO;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.wl.pub.LoginInforVO;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * ���ۼƻ�����
 * 
 * @author Administrator
 * 
 */
public class SoDealClientUI extends ToftPanel implements BillEditListener2 {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// ���尴ť
	private ButtonObject m_btnQry = new ButtonObject(
			WdsWlPubConst.DM_PLANDEAL_BTNTAG_QRY,
			WdsWlPubConst.DM_PLANDEAL_BTNTAG_QRY, 2,
			WdsWlPubConst.DM_PLANDEAL_BTNTAG_QRY);
	private ButtonObject m_btnDeal = new ButtonObject(
			WdsWlPubConst.DM_PLANDEAL_BTNTAG_DEAL,
			WdsWlPubConst.DM_PLANDEAL_BTNTAG_DEAL, 2,
			WdsWlPubConst.DM_PLANDEAL_BTNTAG_DEAL);
	private ButtonObject m_btnSelAll = new ButtonObject(
			WdsWlPubConst.DM_PLANDEAL_BTNTAG_SELALL,
			WdsWlPubConst.DM_PLANDEAL_BTNTAG_SELALL, 2,
			WdsWlPubConst.DM_PLANDEAL_BTNTAG_SELALL);
	private ButtonObject m_btnSelno = new ButtonObject(
			WdsWlPubConst.DM_PLANDEAL_BTNTAG_SELNO,
			WdsWlPubConst.DM_PLANDEAL_BTNTAG_SELNO, 2,
			WdsWlPubConst.DM_PLANDEAL_BTNTAG_SELNO);
	private ButtonObject m_btnXnDeal = new ButtonObject(
			WdsWlPubConst.DM_PLANDEAL_BTNTAG_XNDEAL,
			WdsWlPubConst.DM_PLANDEAL_BTNTAG_XNDEAL, 2,
			WdsWlPubConst.DM_PLANDEAL_BTNTAG_XNDEAL);

	protected ClientEnvironment m_ce = null;
	protected ClientLink cl = null;
	private SoDealEventHandler m_handler = null;

	// �������ģ��

	private BillListPanel m_panel = null;

	// ��ť�¼�����

	private LoginInforHelper helper = null;

	public LoginInforHelper getLoginInforHelper() {
		if (helper == null) {
			helper = new LoginInforHelper();
		}
		return helper;
	}

	public SoDealClientUI() {
		super();
		m_ce = ClientEnvironment.getInstance();
		cl = new ClientLink(m_ce);
		init();
	}

	public SoDealClientUI(String pk_corp, String pk_billType,
			String pk_busitype, String operater, String billId) {
		super();
		m_ce = ClientEnvironment.getInstance();
		cl = new ClientLink(m_ce);
		init();
		loadData(billId);
	}

	protected BillListPanel getPanel() {
		if (m_panel == null) {
			m_panel = new BillListPanel();
			m_panel.loadTemplet(WdsWlPubConst.WDS4, null, m_ce.getUser()
					.getPrimaryKey(), m_ce.getCorporation().getPrimaryKey());
			m_panel.setEnabled(true);
		}
		return m_panel;
	}

	private void init() {
		setLayout(new java.awt.CardLayout());
		add(getPanel(), "a");

		createEventHandler();

		setButton();

		initListener();
	}

	private void initListener() {
		// getPanel().addBodyEditListener(this);
		getPanel().addEditListener(m_handler);
		getPanel().getParentListPanel().addEditListener2(this);
		// getPanel().addBodyEditListener(l)
		// getPanel().addHeadEditListener(l)

	}

	private void setButton() {
		ButtonObject[] m_objs = new ButtonObject[] { m_btnSelAll, m_btnSelno,
				m_btnQry, m_btnDeal ,m_btnXnDeal};
		this.setButtons(m_objs);
	}

	private void createEventHandler() {

		m_handler = new SoDealEventHandler(this);

	}

	public void loadData(String billId) {
		try {
			SoDealVO[] billdatas = SoDealHealper.doQuery(" h.CSALEID = '"
					+ billId + "' ");
			if (billdatas == null || billdatas.length == 0) {
				showHintMessage("��ѯ��ɣ�û����������������");
				return;
			}
			// �����ѯ���ļƻ� ���� ����
			getPanel().getHeadBillModel().setBodyDataVO(billdatas);
			getPanel().getHeadBillModel().execLoadFormula();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onButtonClicked(ButtonObject btn) {
		// TODO Auto-generated method stub

		m_handler.onButtonClicked(btn.getCode());
	}

	public void updateButtonStatus(String btnTag, boolean flag) {
		if (btnTag.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_DEAL)) {
			m_btnDeal.setEnabled(flag);
		} else if (btnTag
				.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_SELNO)) {
			m_btnSelno.setEnabled(flag);
		} else if (btnTag
				.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_SELALL)) {
			m_btnSelAll.setEnabled(flag);
		} else if (btnTag
				.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_QRY)) {
			m_btnQry.setEnabled(flag);
		}else if(btnTag
				.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_XNDEAL)){
			m_btnXnDeal.setEnabled(flag);
		}
		updateButtons();
	}

	// public boolean beforeEdit(BillItemEvent e) {
	// String key=e.getItem().getKey();
	// //��Ŀ���� warehousename
	// if(key==null){
	// return false;
	// }
	// if(key.equalsIgnoreCase("warehousename")){
	//		
	// try {
	// LoginInforVO
	// login=getLoginInforHelper().getLogInfor(m_ce.getUser().getPrimaryKey());
	//		
	// if(login.getBistp()==null){
	// return false;
	// }
	// //����Ȩ�޵Ĺ��ˣ�ֻ�о�������Ȩ�޵ı���Ա�����ܱ༭����վ
	// if(login.getBistp().booleanValue()==true){
	// getPanel().getHeadItem("warehousename").setEnabled(true);
	//			
	// //����ֱ�������� �Ĳֿ�
	// JComponent c =getPanel().getHeadItem("warehousename").getComponent();
	// if( c instanceof UIRefPane){
	// UIRefPane ref = (UIRefPane)c;
	// ref.getRefModel().addWherePart(" def1 = '1' and isnull(dr,0) = 0");
	// }
	//				
	// }
	//		
	//		
	//		
	// } catch (Exception e1) {
	// // TODO Auto-generated catch block
	// e1.printStackTrace();
	// }
	//		 
	//		 
	// }
	//		
	// return false;
	// }

	public boolean beforeEdit(BillEditEvent e) {
		String key = e.getKey();
		// ��Ŀ���� warehousename
		if (key == null) {
			return false;
		}
		if (key.equalsIgnoreCase("warehousename")) {
			try {
				LoginInforVO login = getLoginInforHelper().getLogInfor(
						m_ce.getUser().getPrimaryKey());
				if (login.getBistp() == null) {
					return false;
				}
				// ����Ȩ�޵Ĺ��ˣ�ֻ�о�������Ȩ�޵ı���Ա�����ܱ༭����վ
				if (login.getBistp().booleanValue() == true) {
					getPanel().getHeadItem("warehousename").setEnabled(true);
					// ����ֱ�������� �Ĳֿ�
					JComponent c = getPanel().getHeadItem("warehousename")
							.getComponent();
					if (c instanceof UIRefPane) {
						UIRefPane ref = (UIRefPane) c;
						ref.getRefModel().addWherePart(
								" and def1 = '1' and isnull(dr,0) = 0");
					}
					return true;
				} else {
					getPanel().getHeadItem("warehousename").setEnabled(false);
					return false;
				}
			} catch (Exception e1) {
				Logger.error(e1);
			}
		}
		return true;
	}

	public ClientLink getCl() {
		return cl;
	}

	public void setCl(ClientLink cl) {
		this.cl = cl;
	}
}
