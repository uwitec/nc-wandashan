package nc.ui.dm.db.deal;
import javax.swing.JComponent;

import nc.bs.logging.Logger;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.IBillModelRowStateChangeEventListener;
import nc.ui.pub.bill.RowStateChangeEvent;
import nc.ui.wl.pub.LoginInforHelper;
import nc.vo.dm.db.deal.DbDealVO;
import nc.vo.ic.pub.StockInvOnHandVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.wl.pub.LoginInforVO;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * ������������
 * @author mlr
 */
public class DbDealClientUI extends ToftPanel implements BillEditListener,
		BillEditListener2 {
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

	// zhf add ֧�����۶��������ر� �� ��
	private ButtonObject m_btnClose = new ButtonObject("�ر�", "�ر�", 2, "�ر�");

	private ButtonObject m_btnOpen = new ButtonObject("��", "��", 2, "��");
	protected ClientEnvironment m_ce = null;
	protected ClientLink cl = null;
	private DbDealEventHandler event = null;

	// �������ģ��
	private BillListPanel m_panel = null;

	// ��ť�¼�����

	private LoginInforHelper helper = null;

	private String cwhid;// ��ǰ��¼�ͻ������ֿ�

	public String getWhid() {
		return cwhid;
	}

	public LoginInforHelper getLoginInforHelper() {
		if (helper == null) {
			helper = new LoginInforHelper();
		}
		return helper;
	}

	public DbDealClientUI() {
		super();
		m_ce = ClientEnvironment.getInstance();
		cl = new ClientLink(m_ce);
		init();
	}

	public DbDealClientUI(String pk_corp, String pk_billType,
			String pk_busitype, String operater, String billId) {
		super();
		m_ce = ClientEnvironment.getInstance();
		cl = new ClientLink(m_ce);
		init();
		loadData(billId);
	}

	private void init() {
		setLayout(new java.awt.CardLayout());
		add(getPanel(), "a");
		createEventHandler();
		setButton();
		initListener();
		try {
			cwhid = new LoginInforHelper().getLogInfor(
					m_ce.getUser().getPrimaryKey()).getWhid();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cwhid = null;
		}
	}

	private void initListener() {
		// ��ͷ�༭ǰ�����
		getPanel().addEditListener(this);
		getPanel().getParentListPanel().addEditListener2(this);
		// ����༭ǰ�����
		BodyEditListener bodyEditListener = new BodyEditListener();
		getPanel().addBodyEditListener(bodyEditListener);
		getPanel().getBodyScrollPane("base").addEditListener2(bodyEditListener);
		getPanel().getHeadBillModel().addRowStateChangeEventListener(
				new HeadRowStateListener());
	}

	/**
	 * lyf:����༭����
	 * 
	 * @author
	 * 
	 */
	private class BodyEditListener implements BillEditListener,
			BillEditListener2 {
		private LoginInforHelper log = new LoginInforHelper();

		public void afterEdit(BillEditEvent e) {
			String key = e.getKey();// ���������븺��

			String value = PuPubVO.getString_TrimZeroLenAsNull(e.getValue());
			int row = e.getRow();
			if ("nassnum".equalsIgnoreCase(key)) {
				UFDouble num = PuPubVO.getUFDouble_NullAsZero(getPanel()
						.getBodyBillModel().getValueAt(row, "nassnum"));
				if (num.doubleValue() < 0) {
					showWarningMessage("���������Ÿ���");
					getPanel().getBodyBillModel().setValueAt(e.getOldValue(),
							e.getRow(), key);
					return;
				}
				// ���Ÿ����� �༭�� ���� for add mlr
				UFDouble oldvalue = e.getOldValue() == null ? new UFDouble(0)
						: (UFDouble) e.getOldValue();
				if (num == null || num.doubleValue() == 0
						|| num.doubleValue() > oldvalue.doubleValue()) {
					MessageDialog.showHintDlg(getPanel(), "����",
							"�������ֵ����,�����֮ǰ��ֵҪС!");
					getPanel().getBodyBillModel().setValueAt(oldvalue, row,
							"nassnum");
					getPanel().getBodyBillModel().execEditFormulasByKey(row,
							"nassnum");
					return;
				}
				String tablecode = getPanel().getChildListPanel()
						.getTableCode();
				getPanel().getBodyScrollPane(tablecode).copyLine();
				getPanel().getBodyScrollPane(tablecode).pasteLine();
				getPanel().getBodyBillModel().setValueAt(oldvalue.sub(num),
						row, "nassnum");
				getPanel().getBodyBillModel().execEditFormulasByKey(row,
						"nassnum");
			}
			if ("ss_state".equalsIgnoreCase(key)) {
				if (value == null) {
					getPanel().getBodyBillModel().setValueAt(null, row,
							"nstorenumout");// ���������
					getPanel().getBodyBillModel().setValueAt(null, row,
							"anstorenumout");// ��渨����

				}
				String pk_corp = ClientEnvironment.getInstance()
						.getCorporation().getPrimaryKey();
				String pk_strodoc = null;
				try {
					pk_strodoc = PuPubVO.getString_TrimZeroLenAsNull(log
							.getLogInfor(m_ce.getUser().getPrimaryKey())
							.getWhid());
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				String pk_invmandoc = PuPubVO
						.getString_TrimZeroLenAsNull(getPanel()
								.getBodyBillModel().getValueAt(row,
										"ctakeoutinvid"));
				String pk_invbasdoc = PuPubVO
						.getString_TrimZeroLenAsNull(getPanel()
								.getBodyBillModel().getValueAt(row,
										"cinvbasid"));
				String pk_ss = PuPubVO.getString_TrimZeroLenAsNull(getPanel()
						.getBodyBillModel().getValueAt(row, "vdef1"));
				if (pk_corp == null || pk_strodoc == null
						|| pk_invmandoc == null || pk_invbasdoc == null
						|| pk_ss == null) {
					return;
				}
				StockInvOnHandVO vo = new StockInvOnHandVO();
				vo.setPk_corp(pk_corp);
				vo.setPk_customize1(pk_strodoc);
				vo.setPk_invmandoc(pk_invmandoc);
				vo.setPk_invbasdoc(pk_invbasdoc);
				vo.setSs_pk(pk_ss);
				StockInvOnHandVO[] vos = null;
				try {
					vos = (StockInvOnHandVO[]) event.getStock()
							.queryStockCombinForClient(
									new StockInvOnHandVO[] { vo });
				} catch (Exception e1) {
					getPanel().getBodyBillModel().setValueAt(null, row,
							"nstorenumout");// ���������
					getPanel().getBodyBillModel().setValueAt(null, row,
							"anstorenumout");// ��渨����
					e1.printStackTrace();
					showErrorMessage("��ȡ�ִ���ʧ��");
				}
				if (vos == null || vos.length == 0 || vos[0]==null) {
					getPanel().getBodyBillModel().setValueAt(null, row,
							"nstorenumout");// ���������
					getPanel().getBodyBillModel().setValueAt(null, row,
							"anstorenumout");// ��渨����
					return;
				}
			//	anstorenumout->nstorenumout/hsl;

				getPanel().getBodyBillModel().setValueAt(
						vos[0].getWhs_stocktonnage(), row, "nstorenumout");// ���������
				getPanel().getBodyBillModel().setValueAt(
						vos[0].getWhs_stocktonnage(), row, "anstorenumout");// ��渨����
			}
		}

		public void bodyRowChange(BillEditEvent e) {

		}

		public boolean beforeEdit(BillEditEvent e) {
			String key = e.getKey();
			if ("nassnum".equalsIgnoreCase(key)) {
				if (isGift()) {
					return false;
				} else {
					return true;
				}
			}
			return true;
		}

		/**
		 * 
		 * @���ߣ�lyf:�ж��Ƿ���Ʒ��
		 * @˵�������ɽ������Ŀ
		 * @ʱ�䣺2011-11-17����09:41:46
		 * @return
		 */
		public boolean isGift() {
			boolean isGift = false;
			int count = getPanel().getBodyBillModel().getRowCount();
			for (int row = 0; row < count; row++) {
				Object value = getPanel().getBodyBillModel().getValueAt(row,
						"flargess");
				isGift = PuPubVO.getUFBoolean_NullAs(value, UFBoolean.FALSE)
						.booleanValue();
				if (isGift) {
					return isGift;
				}
			}
			return isGift;
		}
	}

	protected BillListPanel getPanel() {
		if (m_panel == null) {
			m_panel = new BillListPanel();
			m_panel.loadTemplet(WdsWlPubConst.WDSB, null, m_ce.getUser()
					.getPrimaryKey(), m_ce.getCorporation().getPrimaryKey());
			m_panel.setEnabled(true);
			m_panel.getChildListPanel().setTotalRowShow(true);
			m_panel.setMultiSelect(true);
			//m_panel.getHeadTable().removeDbrtListener();
		}
		return m_panel;
	}

	public void headRowChange(int iNewRow) {
		if (!getPanel().setBodyModelData(iNewRow)) {
			// 1.���������������
			loadBodyData(iNewRow);
			// 2.���ݵ�ģ����
			getPanel().setBodyModelDataCopy(iNewRow);
		}
		getPanel().repaint();
	}

	private void loadBodyData(int row) {
		getPanel().getBodyBillModel().clearBodyData();
		String key = (String) getPanel().getHeadBillModel().getValueAt(row,
				"vcode");
		getPanel().getBodyBillModel().setBodyDataVO(
				event.getSelectBufferData(key));// ���ñ���
		getPanel().getBodyBillModel().execLoadFormula();
	}

	private class HeadRowStateListener implements
			IBillModelRowStateChangeEventListener {
		public void valueChanged(RowStateChangeEvent e) {
			if (e.getRow() != getPanel().getHeadTable().getSelectedRow()) {
				headRowChange(e.getRow());
			}
			BillModel model = getPanel().getBodyBillModel();
			IBillModelRowStateChangeEventListener l = model
					.getRowStateChangeEventListener();
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
		ButtonObject[] m_objs = new ButtonObject[] { m_btnQry, m_btnSelAll,
				m_btnSelno, m_btnDeal };
		this.setButtons(m_objs);
	}

	private void createEventHandler() {
		event = new DbDealEventHandler(this);
	}

	public void loadData(String billId) {
		try {
			DbDealVO[] billdatas = DbDealHealper.doQuery(" h.cbillid = '"
					+ billId + "' ", getWhid(), UFBoolean.FALSE);
			if (billdatas == null || billdatas.length == 0) {
				showHintMessage("��ѯ��ɣ�û����������������");
				return;
			}
			// ������ѯ���ļƻ� ���� ����
			getPanel().getHeadBillModel().setBodyDataVO(billdatas);
			getPanel().getHeadBillModel().execLoadFormula();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getTitle() {
		return null;
	}

	@Override
	public void onButtonClicked(ButtonObject btn) {
		event.onButtonClicked(btn.getCode());
	}

	public void updateButtonStatus(String btnTag, boolean flag) {
		if (btnTag.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_DEAL)) {
			m_btnDeal.setEnabled(flag);
		} else if (btnTag
				.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_QRY)) {
			m_btnQry.setEnabled(flag);
		}

		UFBoolean isclose = event.getOrderType();
		m_btnClose.setEnabled(!isclose.booleanValue());
		m_btnOpen.setEnabled(isclose.booleanValue());
		m_btnDeal.setEnabled(!isclose.booleanValue());
		updateButtons();
	}

	// ��ͷ���л��¼�
	public void bodyRowChange(BillEditEvent e) {
		// TODO Auto-generated method stub
		if (e.getRow() < 0)
			return;
		e.getValue();
		headRowChange(e.getRow());
		getPanel().getBodyBillModel().reCalcurateAll();
	}

	// ��ͷ�༭ǰ�¼�
	public boolean beforeEdit(BillEditEvent e) {
		String key = e.getKey();
		int row = e.getRow();
		if (e.getPos() == BillItem.HEAD) {
			if ("warehousename".equalsIgnoreCase(key)) {
				try {
					LoginInforVO login = getLoginInforHelper().getLogInfor(
							m_ce.getUser().getPrimaryKey());
					if (login.getBistp() == null) {
						return false;
					}
					// ����Ȩ�޵Ĺ��ˣ�ֻ�о�������Ȩ�޵ı���Ա�����ܱ༭����վ
					if (login.getBistp().booleanValue() == true) {
						getPanel().getHeadItem("warehousename")
								.setEnabled(true);
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
						getPanel().getHeadItem("warehousename").setEnabled(
								false);
						return false;
					}
				} catch (Exception e1) {
					Logger.error(e1);
				}
			}
		} else {// ����༭
			if ("nassnum".equalsIgnoreCase(key) || "nnum".equalsIgnoreCase(key)) {// ������Ʒ�����Ա����
				Object value = getPanel().getBodyBillModel().getValueAt(row,
						"blargessflag");
				if (PuPubVO.getUFBoolean_NullAs(value, UFBoolean.FALSE)
						.booleanValue()) {
					return false;
				}
			}
		}

		return true;
	}

	// ��ͷ�༭���¼�
	public void afterEdit(BillEditEvent e) {
		String key = e.getKey();
		if ("warehousename".equalsIgnoreCase(key)) {

		}
	}

	public ClientLink getCl() {
		return cl;
	}

	public void setCl(ClientLink cl) {
		this.cl = cl;
	}
}