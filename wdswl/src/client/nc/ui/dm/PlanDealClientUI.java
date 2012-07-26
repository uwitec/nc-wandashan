package nc.ui.dm;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.wl.pub.LoginInforHelper;
import nc.vo.ic.pub.StockInvOnHandVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * 发运计划安排
 * 
 * @author mlr
 */
public class PlanDealClientUI extends ToftPanel implements BillEditListener,
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
	protected ClientEnvironment m_ce = null;
	protected ClientLink cl = null;
	private PlanDealEventHandler m_handler = null;
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

	public PlanDealClientUI() {
		super();
		m_ce = ClientEnvironment.getInstance();
		cl = new ClientLink(m_ce);
		init();
	}

	protected BillListPanel getPanel() {
		if (m_panel == null) {
			m_panel = new BillListPanel();
			m_panel.loadTemplet(WdsWlPubConst.DM_PLAN_DEAL_BILLTYPE, null, m_ce
					.getUser().getPrimaryKey(), m_ce.getCorporation()
					.getPrimaryKey());
			m_panel.setEnabled(true);
			m_panel.getParentListPanel().setTotalRowShow(true);
			m_panel.setMultiSelect(true);
			m_panel.getBodyTable().removeSortListener();
		}
		return m_panel;
	}

	public ClientEnvironment getEviment() {
		return m_ce;
	}

	private void init() {
		setLayout(new java.awt.CardLayout());
		add(getPanel(), "a");

		createEventHandler();

		setButton();

		initListener();
	}

	private void initListener() {
		// 表体编辑前后监听
		getPanel().addBodyEditListener(this);
		getPanel().getBodyScrollPane("wds_sendplanin_b").addEditListener2(this);
	}

	private void setButton() {
		ButtonObject[] m_objs = new ButtonObject[] { m_btnSelAll, m_btnSelno,
				m_btnQry, m_btnDeal };
		this.setButtons(m_objs);
	}

	private void createEventHandler() {

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
		} else if (btnTag
				.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_XNDEAL)) {
			// m_btnXnDeal.setEnabled(flag);
		}
		updateButtons();
	}

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
			String tablecode = getPanel().getChildListPanel().getTableCode();
			getPanel().getBodyScrollPane(tablecode).copyLine();
			getPanel().getBodyScrollPane(tablecode).pasteLine();
			getPanel().getBodyBillModel().setValueAt(oldvalue.sub(num), row,
					"nassnum");
			getPanel().getBodyBillModel().execEditFormulasByKey(row, "nassnum");
		}
		if ("ss_state".equalsIgnoreCase(key)) {
			if (value == null) {
				getPanel().getBodyBillModel().setValueAt(null, row,
						"nstorenumout");// 库存主数量
				getPanel().getBodyBillModel().setValueAt(null, row,
						"anstorenumout");// 库存辅数量

			}
			String pk_corp = ClientEnvironment.getInstance().getCorporation()
					.getPrimaryKey();
			String pk_strodoc = null;
			try {
				pk_strodoc = PuPubVO
						.getString_TrimZeroLenAsNull(getLoginInforHelper()
								.getLogInfor(m_ce.getUser().getPrimaryKey())
								.getWhid());
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			String pk_invmandoc = PuPubVO
					.getString_TrimZeroLenAsNull(getPanel().getBodyBillModel()
							.getValueAt(row, "pk_invmandoc"));
			String pk_invbasdoc = PuPubVO
					.getString_TrimZeroLenAsNull(getPanel().getBodyBillModel()
							.getValueAt(row, "pk_invbasdoc"));
			String pk_ss = PuPubVO.getString_TrimZeroLenAsNull(getPanel()
					.getBodyBillModel().getValueAt(row, "vdef1"));
			if (pk_corp == null || pk_strodoc == null || pk_invmandoc == null
					|| pk_invbasdoc == null || pk_ss == null) {
				return;
			}
			StockInvOnHandVO vo = new StockInvOnHandVO();
			vo.setPk_corp(pk_corp);
			vo.setPk_customize1(pk_strodoc);
			vo.setPk_invmandoc(pk_invmandoc);
			vo.setPk_invbasdoc(pk_invbasdoc);
			vo.setSs_pk(pk_ss);
			StockInvOnHandVO[] vos = null;// 现存量
			StockInvOnHandVO[] vos1 = null;// 可用量
			try {
				vos = (StockInvOnHandVO[]) m_handler.getStock()
						.queryStockCombinForClient(
								new StockInvOnHandVO[] { vo });
				vos1 = (StockInvOnHandVO[]) m_handler.getAbo()
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
			if (vos == null || vos.length == 0 || vos[0] == null) {
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
					vos[0].getWhs_stockpieces(), row, "anstorenumout");// 库存辅数量
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

		// m_handler.afterEdit(e);
	}

	public void bodyRowChange(BillEditEvent e) {

	}

	public String getHslFieldName() {
		return "hsl";
	};// 获取换算率字段名

	public String getAssNumFieldName() {
		return "nassnum";
	};// 获取附属楼字段名

	public boolean beforeEdit(BillEditEvent e) {
		if (PuPubVO.getString_TrimZeroLenAsNull(getHslFieldName()) != null
				&& PuPubVO.getString_TrimZeroLenAsNull(getAssNumFieldName()) != null) {
			if (e.getKey().equalsIgnoreCase(getAssNumFieldName())) {// 辅助数量
																	// 换算率为0
																	// 辅数量不可编辑
				UFDouble hsl = PuPubVO.getUFDouble_NullAsZero(getPanel()
						.getBodyBillModel().getValueAt(e.getRow(),
								getHslFieldName()));
				if (hsl.equals(WdsWlPubConst.ufdouble_zero)) {
					return false;
				}
			}
		}
		return true;
	}
}
