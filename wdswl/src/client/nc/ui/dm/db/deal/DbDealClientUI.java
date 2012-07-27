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
 * 调拨定单安排
 * @author mlr
 */
public class DbDealClientUI extends ToftPanel implements BillEditListener,
		BillEditListener2 {
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

	// zhf add 支持销售订单物流关闭 和 打开
	private ButtonObject m_btnClose = new ButtonObject("关闭", "关闭", 2, "关闭");

	private ButtonObject m_btnOpen = new ButtonObject("打开", "打开", 2, "打开");
	protected ClientEnvironment m_ce = null;
	protected ClientLink cl = null;
	private DbDealEventHandler event = null;

	// 定义界面模板
	private BillListPanel m_panel = null;

	// 按钮事件处理

	private LoginInforHelper helper = null;

	private String cwhid;// 当前登录客户所属仓库

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
		// 表头编辑前后监听
		getPanel().addEditListener(this);
		getPanel().getParentListPanel().addEditListener2(this);
		// 表体编辑前后监听
		BodyEditListener bodyEditListener = new BodyEditListener();
		getPanel().addBodyEditListener(bodyEditListener);
		getPanel().getBodyScrollPane("base").addEditListener2(bodyEditListener);
		getPanel().getHeadBillModel().addRowStateChangeEventListener(
				new HeadRowStateListener());
	}

	/**
	 * lyf:表体编辑监听
	 * 
	 * @author
	 * 
	 */
	private class BodyEditListener implements BillEditListener,
			BillEditListener2 {
		private LoginInforHelper log = new LoginInforHelper();

		public void afterEdit(BillEditEvent e) {
			String key = e.getKey();// 不允许输入负数

			String value = PuPubVO.getString_TrimZeroLenAsNull(e.getValue());
			int row = e.getRow();
			if ("nassnum".equalsIgnoreCase(key)) {
				UFDouble num = PuPubVO.getUFDouble_NullAsZero(getPanel()
						.getBodyBillModel().getValueAt(row, "nassnum"));
				if (num.doubleValue() < 0) {
					showWarningMessage("不允许安排负数");
					getPanel().getBodyBillModel().setValueAt(e.getOldValue(),
							e.getRow(), key);
					return;
				}
				// 安排辅数量 编辑后 拆行 for add mlr
				UFDouble oldvalue = e.getOldValue() == null ? new UFDouble(0)
						: (UFDouble) e.getOldValue();
				if (num == null || num.doubleValue() == 0
						|| num.doubleValue() > oldvalue.doubleValue()) {
					MessageDialog.showHintDlg(getPanel(), "错误",
							"所输入的值错误,必须比之前的值要小!");
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
							"nstorenumout");// 库存主数量
					getPanel().getBodyBillModel().setValueAt(null, row,
							"anstorenumout");// 库存辅数量

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
				StockInvOnHandVO[] vos1 = null;// 可用量

				try {
					vos = (StockInvOnHandVO[]) event.getStock()
							.queryStockCombinForClient(
									new StockInvOnHandVO[] { vo });
					vos1 = (StockInvOnHandVO[]) event.getAbo()
					.queryStockCombinForClient(
							new StockInvOnHandVO[] { vo });

				} catch (Exception e1) {
					getPanel().getBodyBillModel().setValueAt(null, row,
							"nstorenumout");// 库存主数量
					getPanel().getBodyBillModel().setValueAt(null, row,
							"anstorenumout");// 库存辅数量
					getPanel().getBodyBillModel().setValueAt(null, row,
					"ndrqstorenumout");// 可用主数量
			getPanel().getBodyBillModel().setValueAt(null, row,
					"ndrqarrstorenumout");// 可用辅数量
					e1.printStackTrace();
					showErrorMessage("获取现存量失败");
				}
				if (vos == null || vos.length == 0 || vos[0]==null) {
					getPanel().getBodyBillModel().setValueAt(null, row,
							"nstorenumout");// 库存主数量
					getPanel().getBodyBillModel().setValueAt(null, row,
							"anstorenumout");// 库存辅数量
					getPanel().getBodyBillModel().setValueAt(null, row,
					"ndrqstorenumout");// 可用主数量
			getPanel().getBodyBillModel().setValueAt(null, row,
					"ndrqarrstorenumout");// 可用辅数量
					return;
				}
				getPanel().getBodyBillModel().setValueAt(
						vos[0].getWhs_stocktonnage(), row, "nstorenumout");// 库存主数量
				getPanel().getBodyBillModel().setValueAt(
						vos[0].getWhs_stocktonnage(), row, "anstorenumout");// 库存辅数量
				if (vos1 == null || vos1.length == 0 || vos1[0] == null) {
					getPanel().getBodyBillModel().setValueAt(null, row,
							"ndrqstorenumout");// 可用主数量
					getPanel().getBodyBillModel().setValueAt(null, row,
							"ndrqarrstorenumout");// 可用辅数量
					return;
				} else {
					getPanel().getBodyBillModel().setValueAt(
							vos1[0].getWhs_stocktonnage(), row, "ndrqstorenumout");// 可用主数量
					getPanel().getBodyBillModel()
							.setValueAt(vos1[0].getWhs_stockpieces(), row,
									"ndrqarrstorenumout");// 可用辅数量
				}
			}
		}

		public void bodyRowChange(BillEditEvent e) {

		}

		public boolean beforeEdit(BillEditEvent e) {
			String key = e.getKey();
//			if ("nassnum".equalsIgnoreCase(key)) {
//				if (isGift()) {
//					return false;
//				} else {
//					return true;
//				}
//			}
			return true;
		}

//		/**
//		 * 
//		 * @作者：lyf:判断是否赠品单
//		 * @说明：完达山物流项目
//		 * @时间：2011-11-17下午09:41:46
//		 * @return
//		 */
//		public boolean isGift() {
//			boolean isGift = false;
//			int count = getPanel().getBodyBillModel().getRowCount();
//			for (int row = 0; row < count; row++) {
//				Object value = getPanel().getBodyBillModel().getValueAt(row,
//						"flargess");
//				isGift = PuPubVO.getUFBoolean_NullAs(value, UFBoolean.FALSE)
//						.booleanValue();
//				if (isGift) {
//					return isGift;
//				}
//			}
//			return isGift;
//		}
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
			// 1.初次载入表体数据
			loadBodyData(iNewRow);
			// 2.备份到模型中
			getPanel().setBodyModelDataCopy(iNewRow);
		}
		getPanel().repaint();
	}

	private void loadBodyData(int row) {
		getPanel().getBodyBillModel().clearBodyData();
		String key = (String) getPanel().getHeadBillModel().getValueAt(row,
				"vcode");
		getPanel().getBodyBillModel().setBodyDataVO(
				event.getSelectBufferData(key));// 设置表体
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
				showHintMessage("查询完成：没有满足条件的数据");
				return;
			}
			// 处理查询出的计划 缓存 界面
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

	//	UFBoolean isclose = event.getOrderType();
	//	m_btnClose.setEnabled(!isclose.booleanValue());
//		m_btnOpen.setEnabled(isclose.booleanValue());
//		m_btnDeal.setEnabled(!isclose.booleanValue());
		updateButtons();
	}

	// 表头行切换事件
	public void bodyRowChange(BillEditEvent e) {
		// TODO Auto-generated method stub
		if (e.getRow() < 0)
			return;
		e.getValue();
		headRowChange(e.getRow());
		getPanel().getBodyBillModel().reCalcurateAll();
	}

	// 表头编辑前事件
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
					// 特批权限的过滤，只有具有特批权限的保管员，才能编辑发货站
					if (login.getBistp().booleanValue() == true) {
						getPanel().getHeadItem("warehousename")
								.setEnabled(true);
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
						getPanel().getHeadItem("warehousename").setEnabled(
								false);
						return false;
					}
				} catch (Exception e1) {
					Logger.error(e1);
				}
			}
		} else {// 表体编辑
			 if(e.getKey().equalsIgnoreCase("nassnum")){
				UFDouble hsl = PuPubVO.getUFDouble_NullAsZero(getPanel().getBodyBillModel().getValueAt(e.getRow(), "nchangerate"));
				if(hsl.equals(WdsWlPubConst.ufdouble_zero)){
					return false;
				}
			}
	}

		return true;
	}

	// 表头编辑后事件
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
