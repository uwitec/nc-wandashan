package nc.ui.dm.so.deal;

import javax.swing.JComponent;

import nc.bs.logging.Logger;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
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
import nc.vo.dm.so.deal.SoDealVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.wl.pub.LoginInforVO;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * 销售计划安排
 * 
 * @author Administrator
 * 
 */
public class SoDealClientUI extends ToftPanel implements BillEditListener,BillEditListener2 {

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
	private SoDealEventHandler event = null;

	// 定义界面模板

	private BillListPanel m_panel = null;

	// 按钮事件处理

	private LoginInforHelper helper = null;
	

	private String cwhid;//当前登录客户所属仓库

	public String getWhid(){
		return cwhid;
	}

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
		//表头编辑前后监听
		getPanel().addEditListener(this);
		getPanel().getParentListPanel().addEditListener2(this);
		//表体编辑前后监听
		BodyEditListener bodyEditListener = new BodyEditListener(); 
		getPanel().addBodyEditListener(bodyEditListener);
		getPanel().getBodyScrollPane("body").addEditListener2(bodyEditListener);
		getPanel().getHeadBillModel().addRowStateChangeEventListener(new HeadRowStateListener());
	}
	/**
	 * lyf:表体编辑监听
	 * @author
	 *
	 */
	private class BodyEditListener  implements BillEditListener,BillEditListener2{
		public void afterEdit(BillEditEvent e) {
			String key  = e.getKey();//不允许输入负数
			Object value = e.getValue();
			if("nassnum".equalsIgnoreCase(key)){
				UFDouble num = PuPubVO.getUFDouble_NullAsZero(value);
				if(num.doubleValue() <0){
					showWarningMessage("不允许安排负数");
					getPanel().getBodyBillModel().setValueAt(e.getOldValue(), e.getRow(), key);
					return;
				}
			}else if("nnum".equalsIgnoreCase(key)){
				UFDouble num = PuPubVO.getUFDouble_NullAsZero(value);
				if(num.doubleValue() <0){
					showWarningMessage("不允许安排负数");
					getPanel().getBodyBillModel().setValueAt(e.getOldValue(), e.getRow(), key);
					return;
				}
			}
		}

		public void bodyRowChange(BillEditEvent e) {
		}

		public boolean beforeEdit(BillEditEvent e) {
			String key  = e.getKey();
			if("nassnum".equalsIgnoreCase(key)){
				if(isGift()){
					return false;
				}else{
					return true;
				}
			}else if("nnum".equalsIgnoreCase(key)){
				if(isGift()){
					return false;
				}else{
					return true;
				}
			}else if("disdate".equalsIgnoreCase(key)){
			
					return true;
				
			}
			return false;
		}
		/**
		 * 
		 * @作者：lyf:判断是否赠品单
		 * @说明：完达山物流项目 
		 * @时间：2011-11-17下午09:41:46
		 * @return
		 */
		public boolean isGift(){
			boolean isGift = false;
			int count = getPanel().getBodyBillModel().getRowCount();
			for(int row =0;row<count;row++){
				Object value = getPanel().getBodyBillModel().getValueAt(row, "blargessflag");
				isGift = PuPubVO.getUFBoolean_NullAs(value, UFBoolean.FALSE).booleanValue();
				if(isGift){
					return isGift;
				}
			}
			return isGift;
		}
	}
	
	protected BillListPanel getPanel() {
		if (m_panel == null) {
			m_panel = new BillListPanel();
			m_panel.loadTemplet(WdsWlPubConst.WDS4, null, m_ce.getUser()
					.getPrimaryKey(), m_ce.getCorporation().getPrimaryKey());
			m_panel.setEnabled(true);
			m_panel.getParentListPanel().setTotalRowShow(true);
			m_panel.setMultiSelect(true);
			m_panel.getHeadTable().removeSortListener();
		}
		return m_panel;
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
		String key = (String)getPanel().getHeadBillModel().getValueAt(row, "vreceiptcode");		
		getPanel().getBodyBillModel().setBodyDataVO( event.getSelectBufferData(key));//设置表体
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
				m_btnQry,m_btnSelAll,m_btnSelno, m_btnDeal};
		this.setButtons(m_objs);
	}

	private void createEventHandler() {
		event = new SoDealEventHandler(this);
	}

	public void loadData(String billId) {
		try {
			SoDealVO[] billdatas = SoDealHealper.doQuery(" h.CSALEID = '"
					+ billId + "' ");
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onButtonClicked(ButtonObject btn) {
		// TODO Auto-generated method stub
		event.onButtonClicked(btn.getCode());
	}
	

	public void updateButtonStatus(String btnTag, boolean flag) {
		if (btnTag.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_DEAL)) {
			m_btnDeal.setEnabled(flag);
		} else if (btnTag
				.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_QRY)) {
			m_btnQry.setEnabled(flag);
		}
		updateButtons();
	}
	//表头行切换事件
	public void bodyRowChange(BillEditEvent e) {
		// TODO Auto-generated method stub
		if(e.getRow()<0)
			return;
		e.getValue();
		headRowChange(e.getRow());
		
	}
	//表头编辑前事件
	public boolean beforeEdit(BillEditEvent e) {
		String key = e.getKey();
		int row = e.getRow();
		if(e.getPos() == BillItem.HEAD){
			if ("warehousename".equalsIgnoreCase(key)) {
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
		}else{//表体编辑
			if("nassnum".equalsIgnoreCase(key) || "nnum".equalsIgnoreCase(key)){//控制赠品不可以被拆分
				Object value = getPanel().getBodyBillModel().getValueAt(row, "blargessflag");
				if(PuPubVO.getUFBoolean_NullAs(value, UFBoolean.FALSE).booleanValue()){
					return false;
				}
			}
		}
	
		return true;
	}
	//表头编辑后事件
	public void afterEdit(BillEditEvent e) {
		String key = e.getKey();
		if("warehousename".equalsIgnoreCase(key)){
			
		}
	}
	public ClientLink getCl() {
		return cl;
	}

	public void setCl(ClientLink cl) {
		this.cl = cl;
	}
}
