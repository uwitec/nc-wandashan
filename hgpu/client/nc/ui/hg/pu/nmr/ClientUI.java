package nc.ui.hg.pu.nmr;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.hg.pu.pub.DefBillManageUI;
import nc.ui.hg.pu.pub.PlanPubHelper;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.hg.pu.pub.HgPuBtnConst;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.hg.pu.pub.PlanApplyInforVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.pub.IBillStatus;

/**
 * 新物资申请
 * 
 * @author zhw
 * 
 */
public class ClientUI extends DefBillManageUI implements
BillCardBeforeEditListener {

	public ClientUI() {
		super();	
		initAppInfor();
	}

	public PlanApplyInforVO m_appInfor = null;// 系统集采公司

	private void initAppInfor() {
		try {
			m_appInfor = PlanPubHelper.getAppInfor(_getCorp().getPrimaryKey(),
					_getOperator());
		} catch (Exception e) {
			e.printStackTrace();// 获取申请信息失败 并不影响界面加载
			m_appInfor = null;
		}
	}

	@Override
	protected void initSelfData() {
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);//表头编辑事件
	}

	@Override
	public void setDefaultData() throws Exception {
		setHeadItemValue("vbillstatus", IBillStatus.FREE);// 单据状态
		setHeadItemValue("reserve5", _getOperator());// 
		setHeadItemValue("dbilldate", _getDate());
		setHeadItemValue("ccorpid", _getCorp().getPrimaryKey());
		setHeadItemValue("pk_billtype", HgPubConst.NEW_MATERIALS_BILLTYPE);
		setHeadItemValue("dmakedate", _getDate());
		setHeadItemValue("ijjway", 0);
		setHeadItemValue("biszywz",UFBoolean.FALSE);
		setHeadItemValue("bisjjwz",UFBoolean.FALSE);
		setHeadItemValue("bisdcdx",UFBoolean.FALSE);
		setHeadItemValue("discountflag",UFBoolean.FALSE);
		setHeadItemValue("laborflag",UFBoolean.FALSE);
		
		
		if (m_appInfor == null) {
			showWarningMessage("当前用户未设置关联业务员");
			return;
		}
		setHeadItemValue("pk_deptdoc", m_appInfor.getCapplydeptid());
		setHeadItemValue("vemployeeid",m_appInfor.getCapplypsnid());
	}
	
	protected void setHeadItemValue(String item, Object value) {
		getBillCardPanel().setHeadItem(item, value);
	}
	protected void setTailItemValue(String item, Object value) {
		getBillCardPanel().setTailItem(item, value);
	}
	
	// 添加自定义按钮
	public void initPrivateButton() {
		ButtonVO btnvo = new ButtonVO();
		btnvo.setBtnNo(HgPuBtnConst.CODING);
		btnvo.setBtnName("编码");
		btnvo.setBtnChinaName("编码");
		btnvo.setBtnCode(null);// code最好设置为空
		btnvo.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT});
		btnvo.setBusinessStatus(new int[]{IBillStatus.FREE});
		addPrivateButton(btnvo);
		
		ButtonVO btnvo1 = new ButtonVO();
		btnvo1.setBtnNo(HgPuBtnConst.OK);
		btnvo1.setBtnName("确定");
		btnvo1.setBtnChinaName("确定");
		btnvo1.setBtnCode(null);// code最好设置为空
		btnvo1.setOperateStatus(new int[] { IBillOperate.OP_EDIT});
//		btnvo1.setBusinessStatus(new int[]{IBillStatus.FREE});
		addPrivateButton(btnvo1);
		super.initPrivateButton();
	}

	@Override
	protected ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this, getUIControl());
	}

	@Override
	protected AbstractManageController createController() {
		return new ClientController();
	}

	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {

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
	protected String getBillNo() throws Exception {
		return HYPubBO_Client.getBillNo(HgPubConst.NEW_MATERIALS_BILLTYPE,
				_getCorp().getPrimaryKey(), null, null);
	}

	@Override
	protected BusinessDelegator createBusinessDelegator() {
		return new ClientBusinessDelegator(this);
	}

	@Override
	public boolean beforeEdit(BillEditEvent e) {
		
		return super.beforeEdit(e);
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		String key = e.getKey();
		if (e.getPos() == BillItem.HEAD) {
			if ("vemployeeid".equalsIgnoreCase(key)) {// 申请人
				getBillCardPanel().execHeadTailEditFormulas(
						getBillCardPanel().getHeadItem("vemployeeid"));
			}else if ("pk_deptdoc".equalsIgnoreCase(key)) {// 申请人
				String deptdoc = PuPubVO
				.getString_TrimZeroLenAsNull(getBillCardPanel()
						.getHeadItem("pk_deptdoc").getValueObject());
				if(deptdoc ==null){
					getBillCardPanel().getHeadItem("vemployeeid").setValue(null);
				}
			}
		}
		super.afterEdit(e);
	}

	public boolean beforeEdit(BillItemEvent e) {
		String key = e.getItem().getKey();
		if ("vemployeeid".equalsIgnoreCase(key)) {// 申请人
			String deptdoc = PuPubVO
					.getString_TrimZeroLenAsNull(getBillCardPanel()
							.getHeadItem("pk_deptdoc").getValueObject());
			UIRefPane invRefPane = (UIRefPane) e.getItem().getComponent();
			AbstractRefModel refModel = (AbstractRefModel) invRefPane
					.getRefModel();
			if (deptdoc == null) {
				refModel.addWherePart(" and 1=1");
				return true;
			}
			refModel.addWherePart("  and bd_psndoc.pk_deptdoc = '" + deptdoc
					+ "'");
			return true;
		}
		return false;
	}
}
