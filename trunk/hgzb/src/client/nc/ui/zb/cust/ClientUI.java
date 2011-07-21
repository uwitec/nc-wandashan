package nc.ui.zb.cust;

import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.pub.bill.IBillItem;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.zb.pub.DefBillManageUI;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.zb.pub.ZbPuBtnConst;
import nc.vo.zb.pub.ZbPubConst;

/**
 * 供应商注册
 * 4004090301
 * @author zhw
 * 
 */
public class ClientUI extends DefBillManageUI implements
BillCardBeforeEditListener {

	public ClientUI() {
		super();	
		
	}
	@Override
	protected void initSelfData() {
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);//表头编辑事件
		getBillCardPanel().setAutoExecHeadEditFormula(true);
	}

	@Override
	public void setDefaultData() throws Exception {
		setHeadItemValue("vbillstatus", IBillStatus.FREE);// 单据状态
		setHeadItemValue("voperatorid", _getOperator());// 
		setHeadItemValue("dbilldate", _getDate());
		setHeadItemValue("pk_corp", _getCorp().getPrimaryKey());
		setHeadItemValue("pk_billtype",ZbPubConst.ZB_CUSTBAS_BILLTYPE );
		setHeadItemValue("dmakedate", _getDate());
	}
	
	protected void setHeadItemValue(String item, Object value) {
		getBillCardPanel().setHeadItem(item, value);
	}
	protected void setTailItemValue(String item, Object value) {
		getBillCardPanel().setTailItem(item, value);
	}
	
	// 添加自定义按钮
	public void initPrivateButton() {
		// 修订供应商
		ButtonVO btnvo6 = new ButtonVO();
		btnvo6.setBtnNo(ZbPuBtnConst.MODIFY);
		btnvo6.setBtnName("修订");
		btnvo6.setBtnChinaName("修订");
		btnvo6.setBtnCode(null);// code最好设置为空
		btnvo6.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT});
		btnvo6.setBusinessStatus( new int[]{IBillStatus.CHECKPASS});
		addPrivateButton(btnvo6);
		// 辅助查询
		ButtonVO btnvo8 = new ButtonVO();
		btnvo8.setBtnNo(ZbPuBtnConst.ASSQUERY);
		btnvo8.setBtnName("辅助查询");
		btnvo8.setBtnChinaName("辅助查询");
		btnvo8.setBtnCode(null);// code最好设置为空
		btnvo8.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT});
		btnvo8.setChildAry(new int[] { ZbPuBtnConst.LINKQUERY,IBillButton.ApproveInfo});
		addPrivateButton(btnvo8);
		
		// 打印管理
		ButtonVO btnvo9 = new ButtonVO();
		btnvo9.setBtnNo(ZbPuBtnConst.ASSPRINT);
		btnvo9.setBtnName("打印管理");
		btnvo9.setBtnChinaName("打印管理");
		btnvo9.setBtnCode(null);// code最好设置为空
		btnvo9.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT});
		btnvo9.setChildAry(new int[] { IBillButton.Print,IBillButton.DirectPrint});
		addPrivateButton(btnvo9);
		//修改
		ButtonVO btnvo10 = new ButtonVO();
		btnvo10.setBtnNo(ZbPuBtnConst.Editor);
		btnvo10.setBtnName("修改");
		btnvo10.setBtnChinaName("修改");
		btnvo10.setBtnCode(null);// code最好设置为空
		btnvo10.setOperateStatus(new int[] { IBillOperate.OP_INIT,IBillOperate.OP_NOTEDIT});
		btnvo10.setBusinessStatus(new int[]{IBillStatus.FREE});
		addPrivateButton(btnvo10);
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
		return null;
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
		if(e.getPos()==IBillItem.HEAD){
			if("ccustmanid".equalsIgnoreCase(key)){//正式供应商
				getBillCardPanel().execHeadFormula(key);
			}
		}
		super.afterEdit(e);
	}

	public boolean beforeEdit(BillItemEvent e) {
		
		return false;
	}
}
