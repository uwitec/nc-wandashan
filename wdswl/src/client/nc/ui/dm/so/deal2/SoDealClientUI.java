package nc.ui.dm.so.deal2;

import javax.swing.JComponent;

import nc.bs.logging.Logger;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.IBillModelRowStateChangeEventListener;
import nc.ui.pub.bill.RowStateChangeEvent;
import nc.ui.wl.pub.LoginInforHelper;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.wl.pub.LoginInforVO;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * 销售计划安排2
 * 
 * @author Administrator
 * 
 */
public class SoDealClientUI extends ToftPanel implements BillEditListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 定义按钮
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
	//	private ButtonObject m_btnXnDeal = new ButtonObject(
	//			WdsWlPubConst.DM_PLANDEAL_BTNTAG_XNDEAL,
	//			WdsWlPubConst.DM_PLANDEAL_BTNTAG_XNDEAL, 2,
	//			WdsWlPubConst.DM_PLANDEAL_BTNTAG_XNDEAL);

	protected ClientEnvironment m_ce = null;
	protected ClientLink cl = null;
	private SoDealEventHandler m_handler = null;

	private String cwhid;//当前登录客户所属仓库

	public String getWhid(){
		return cwhid;
	}

	// 定义界面模板

	private BillListPanel m_panel = null;

	// 按钮事件处理

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

	protected BillListPanel getPanel() {
		if (m_panel == null) {
			m_panel = new BillListPanel();
			m_panel.loadTemplet(WdsWlPubConst.WDS4_2, null, m_ce.getUser()
					.getPrimaryKey(), m_ce.getCorporation().getPrimaryKey());
			m_panel.setEnabled(true);
//			m_panel.getParentListPanel().setTotalRowShow(true);
			m_panel.getChildListPanel().setTotalRowShow(true);
			m_panel.setMultiSelect(true);
			m_panel.getHeadTable().removeSortListener();
		}
		return m_panel;
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
//		getPanel().getHeadBillModel().addRowStateChangeEventListener(new HeadRowStateListener());
	}

	public void headRowChange(int iNewRow) {
		if (!getPanel().setBodyModelData(iNewRow)) {
			//1.初次载入表体数据
			loadBodyData(iNewRow);
			//2.备份到模型中
			getPanel().setBodyModelDataCopy(iNewRow);
		}
		getPanel().repaint();
	}
	
	private void loadBodyData(int row){
		getPanel().getBodyBillModel().clearBodyData();
		getPanel().getBodyBillModel().setBodyDataVO(m_handler.getDataBuffer()[row].getBodyVos());
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
				m_btnSelAll, m_btnSelno,
				m_btnQry, m_btnDeal};
		this.setButtons(m_objs);
	}

	private void createEventHandler() {

		m_handler = new SoDealEventHandler(this);

	}

	//	public void loadData(String billId) {
	//		try {
	//			SoDealVO[] billdatas = SoDealHealper.doQuery(" h.CSALEID = '"
	//					+ billId + "' ");
	//			if (billdatas == null || billdatas.length == 0) {
	//				showHintMessage("查询完成：没有满足条件的数据");
	//				return;
	//			}
	//			// 处理查询出的计划 缓存 界面
	//			getPanel().getHeadBillModel().setBodyDataVO(billdatas);
	//			getPanel().getHeadBillModel().execLoadFormula();
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//		}
	//	}

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
			//			m_btnXnDeal.setEnabled(flag);
		}
		updateButtons();
	}

	public boolean beforeEdit(BillEditEvent e) {
		String key = e.getKey();
		// 项目主键 warehousename
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
				// 特批权限的过滤，只有具有特批权限的保管员，才能编辑发货站
				if (login.getBistp().booleanValue() == true) {
					getPanel().getHeadItem("warehousename").setEnabled(true);
					// 过滤直属于物流 的仓库
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

	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void bodyRowChange(BillEditEvent e) {
		// TODO Auto-generated method stub
		if(e.getRow()<0)
			return;
		headRowChange(e.getRow());
		
	}
}
