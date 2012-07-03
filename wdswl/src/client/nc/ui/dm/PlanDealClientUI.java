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
import nc.vo.wdsnew.pub.BillStockBO1;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * 发运计划安排
 * @author mlr
 */
public class PlanDealClientUI extends ToftPanel implements BillEditListener,
BillEditListener2{
	private static final long serialVersionUID = 1L;
	//定义按钮
	private ButtonObject m_btnQry = new ButtonObject(WdsWlPubConst.DM_PLANDEAL_BTNTAG_QRY,WdsWlPubConst.DM_PLANDEAL_BTNTAG_QRY,2,WdsWlPubConst.DM_PLANDEAL_BTNTAG_QRY);
	private ButtonObject m_btnDeal = new ButtonObject(WdsWlPubConst.DM_PLANDEAL_BTNTAG_DEAL,WdsWlPubConst.DM_PLANDEAL_BTNTAG_DEAL,2,WdsWlPubConst.DM_PLANDEAL_BTNTAG_DEAL);
	private ButtonObject m_btnSelAll = new ButtonObject(WdsWlPubConst.DM_PLANDEAL_BTNTAG_SELALL,WdsWlPubConst.DM_PLANDEAL_BTNTAG_SELALL,2,WdsWlPubConst.DM_PLANDEAL_BTNTAG_SELALL);
	private ButtonObject m_btnSelno = new ButtonObject(WdsWlPubConst.DM_PLANDEAL_BTNTAG_SELNO,WdsWlPubConst.DM_PLANDEAL_BTNTAG_SELNO,2,WdsWlPubConst.DM_PLANDEAL_BTNTAG_SELNO);
	protected ClientEnvironment m_ce = null;
	protected ClientLink cl=null;
	private PlanDealEventHandler m_handler = null;	
	//定义界面模板
	private BillListPanel m_panel = null;	
	//按钮事件处理	
	

	private LoginInforHelper helper = null;

	public LoginInforHelper getLoginInforHelper() {
		if (helper == null) {
			helper = new LoginInforHelper();
		}
		return helper;
	}
	public PlanDealClientUI(){
		super();
		m_ce = ClientEnvironment.getInstance();
		cl =new ClientLink(m_ce);
		init();
	}
	
	protected BillListPanel getPanel(){
		if(m_panel == null){
			m_panel = new BillListPanel();
			m_panel.loadTemplet(WdsWlPubConst.DM_PLAN_DEAL_BILLTYPE, null, m_ce.getUser().getPrimaryKey(), m_ce.getCorporation().getPrimaryKey()
					);
			m_panel.setEnabled(true);
			m_panel.getParentListPanel().setTotalRowShow(true);
		}
		return m_panel;
	}

	public ClientEnvironment getEviment(){
		return m_ce;
	}
	
	private void init(){
		setLayout(new java.awt.CardLayout());
		add(getPanel(),"a");

		createEventHandler();

		setButton();

		initListener();	
	}
	private void initListener(){
		getPanel().addEditListener(m_handler);	
		getPanel().addBodyEditListener(this);
		getPanel().getBodyScrollPane("body").addEditListener2(this);
	}
	
	private void setButton(){
		ButtonObject[] m_objs = new ButtonObject[]{m_btnSelAll,m_btnSelno,m_btnQry,m_btnDeal
				};
		this.setButtons(m_objs);
	}
	private void createEventHandler(){
		
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
	public void updateButtonStatus(String btnTag,boolean flag){
		if(btnTag.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_DEAL)){
			m_btnDeal.setEnabled(flag);
		}else if(btnTag.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_SELNO)){
			m_btnSelno.setEnabled(flag);
		}else if(btnTag.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_SELALL)){
			m_btnSelAll.setEnabled(flag);
		}else if(btnTag.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_QRY)){
			m_btnQry.setEnabled(flag);
		}else if(btnTag.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_XNDEAL)){
		 //	m_btnXnDeal.setEnabled(flag);	
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
				pk_strodoc = PuPubVO.getString_TrimZeroLenAsNull(getLoginInforHelper()
						.getLogInfor(m_ce.getUser().getPrimaryKey())
						.getWhid());
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			String pk_invmandoc = PuPubVO
					.getString_TrimZeroLenAsNull(getPanel()
							.getBodyBillModel().getValueAt(row,
									"pk_invmandoc"));
			String pk_invbasdoc = PuPubVO
					.getString_TrimZeroLenAsNull(getPanel()
							.getBodyBillModel().getValueAt(row,
									"pk_invbasdoc"));
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
				vos = (StockInvOnHandVO[]) m_handler.getStock()
						.queryStockCombinForClient(
								new StockInvOnHandVO[] { vo });
			} catch (Exception e1) {
				getPanel().getBodyBillModel().setValueAt(null, row,
						"nstorenumout");// 库存主数量
				getPanel().getBodyBillModel().setValueAt(null, row,
						"anstorenumout");// 库存辅数量
				e1.printStackTrace();
				showErrorMessage("获取现存量失败");
			}
			if (vos == null || vos.length == 0) {
				getPanel().getBodyBillModel().setValueAt(null, row,
						"nstorenumout");// 库存主数量
				getPanel().getBodyBillModel().setValueAt(null, row,
						"anstorenumout");// 库存辅数量
				return;
			}
			getPanel().getBodyBillModel().setValueAt(
					vos[0].getWhs_stocktonnage(), row, "nstorenumout");// 库存主数量
			getPanel().getBodyBillModel().setValueAt(
					vos[0].getWhs_stocktonnage(), row, "anstorenumout");// 库存辅数量
		}
	
		
	}

	public void bodyRowChange(BillEditEvent e) {
		
	}

	public boolean beforeEdit(BillEditEvent e) {
		return false;
	}

}
