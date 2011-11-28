package nc.ui.wds.tranprice.tonkilometre;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wl.pub.LoginInforHelper;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wl.pub.LoginInforVO;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * 吨公里运价表
 * 
 * @author Administrator
 * 
 */
public class ClientUI extends BillManageUI implements
		BillCardBeforeEditListener {

	private static final long serialVersionUID = -3998675844592858916L;
	protected LoginInforVO m_loginInfor = null;

	public ClientUI() {
		super();
		init();
	}

	public ClientUI(Boolean useBillSource) {
		super(useBillSource);
		init();
	}

	public ClientUI(String pk_corp, String pk_billType, String pk_busitype,
			String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
		init();
	}

	private void init() {
		// 初始当前登录人 角色
		LoginInforHelper login = new LoginInforHelper();
		try {
			m_loginInfor = login.getLogInfor(_getOperator());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			m_loginInfor = null;
		}
	}

	@Override
	protected AbstractManageController createController() {
		return new ClientController();
	}

	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
	}

	@Override
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	}

	@Override
	protected void initSelfData() {
		ButtonObject btn = getButtonManager().getButton(IBillButton.Line);
		if (btn != null) {
			btn.removeChildButton(getButtonManager().getButton(
					IBillButton.CopyLine));
			btn.removeChildButton(getButtonManager().getButton(
					IBillButton.PasteLine));
			btn.removeChildButton(getButtonManager().getButton(
					IBillButton.InsLine));
		}
		// 初始当前登录人 角色
		LoginInforHelper login = new LoginInforHelper();
		try {
			m_loginInfor = login.getLogInfor(_getOperator());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			m_loginInfor = null;
		}
	}

	@Override
	protected void initEventListener() {
		super.initEventListener();
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
	}

	@Override
	public void setDefaultData() throws Exception {
		getBillCardPanel().setHeadItem("vbillstatus", IBillStatus.FREE);
		getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPk_corp());
		getBillCardPanel().setTailItem("voperatorid", _getOperator());
		getBillCardPanel().setHeadItem("pk_billtype", WdsWlPubConst.WDSI);
		getBillCardPanel().setTailItem("dmakedate", _getDate());
		getBillCardPanel().setHeadItem("dbilldate", _getDate());
		// zhf add 初始化 默认 发货仓库
		getBillCardPanel().setHeadItem("reserve1", m_loginInfor.getWhid());
	}

	protected ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this, getUIControl());
	}
	@Override
	public boolean beforeEdit(BillEditEvent e) {
		String key = e.getKey();
		int row = e.getRow();
		//只用分仓才有零担标准
		if ("nsmallnum".equalsIgnoreCase(key)) {//零担数量
			Integer ifw = PuPubVO.getInteger_NullAs(getBillCardPanel().getBillModel().getValueAt(row, "ifw"), -1);
			if(2!=ifw){
				return false;
			}
		}
		if ("nsmallprice".equalsIgnoreCase(key)) {//零担价格
			Integer ifw = PuPubVO.getInteger_NullAs(getBillCardPanel().getBillModel().getValueAt(row, "ifw"), -1);
			if(2!=ifw){
				return false;
			}
		}
		return super.beforeEdit(e);
	}
	@Override
	public void afterEdit(BillEditEvent e) {
		String key = e.getKey();
		Object value= e.getValue();
		int row =e.getRow();
		if (e.getPos() == BillItem.HEAD) {
		}else{	
			if("ifw".equalsIgnoreCase(key)){
				getBillCardPanel().getBodyItem("ifw").getComponent();
				Integer ifw = PuPubVO.getInteger_NullAs(value, -1);
				if(2 != ifw){
					getBillCardPanel().getBillModel().setValueAt(null, row, "nsmallnum");
					getBillCardPanel().getBillModel().setValueAt(null, row, "nsmallprice");
				}
			}
		}
		super.afterEdit(e);
	}

	public Object getUserObject() {
		return null;
	}

	@Override
	public boolean isSaveAndCommitTogether() {

		return true;
	}

	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {

	}

	/**
	 * 表体编辑前事件
	 */
	public boolean beforeEdit(BillItemEvent e) {
		return false;
	}

}