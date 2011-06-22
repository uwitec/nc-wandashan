package nc.ui.wds.ic.other.out;

import javax.swing.JComponent;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.ButtonVOFactory;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wds.ic.pub.OutPubClientUI;
import nc.ui.wds.w80020206.buttun0206.QxqzBtn;
import nc.ui.wds.w80020206.buttun0206.QzqrBtn;
import nc.ui.wds.w8004040204.ssButtun.ckmxBtn;
import nc.ui.wds.w8004040204.ssButtun.fzgnBtn;
import nc.ui.wds.w8004040204.ssButtun.tpzdBtn;
import nc.ui.wds.w8004040204.ssButtun.zdqhBtn;
import nc.ui.wds.w80060206.buttun0206.ISsButtun;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * 其他出库
 */
public class MyClientUI extends OutPubClientUI implements
		BillCardBeforeEditListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String curRefBilltype = null;

	// private Map<String,List<TbOutgeneralTVO>> trayInfor = null;//缓存下 保存后更新到
	// buffer

	protected ManageEventHandler createEventHandler() {
		return new OtherOutEventHandler(this, getUIControl());
	}

	public MyClientUI() {
		super();
	}

	public MyClientUI(String pk_corp, String pk_billType, String pk_busitype,
			String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
	}

	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	}

	protected String getBillNo() throws java.lang.Exception {
		return HYPubBO_Client.getBillNo(WdsWlPubConst.BILLTYPE_OTHER_OUT,
				_getOperator(), null, null);
	}

	/**
	 * 保存和提交是否一起完成。 如果一起完成返回true 创建日期：(2004-2-27 11:32:56)
	 * 
	 * @return boolean
	 */
	public boolean isSaveAndCommitTogether() {
		return true;
	}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
	}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	}

	protected void initSelfData() {
		// 除去行操作多余按钮
		ButtonObject btnobj = getButtonManager().getButton(IBillButton.Line);
		if (btnobj != null) {
			btnobj.removeChildButton(getButtonManager().getButton(
					IBillButton.CopyLine));
			btnobj.removeChildButton(getButtonManager().getButton(
					IBillButton.PasteLine));
			btnobj.removeChildButton(getButtonManager().getButton(
					IBillButton.InsLine));
		}
	}

	@Override
	public boolean beforeEdit(BillEditEvent e) {
		String key = e.getKey();
	
		int row = e.getRow();
		if (e.getPos() == BillItem.BODY) {
		String csourcetype = PuPubVO
				.getString_TrimZeroLenAsNull(getBillCardPanel()
						.getBodyValueAt(row, "csourcetype"));
		   //如果是参照过来的不可以编辑 ，如果是自制单据可以编辑
			if ("ccunhuobianma".equalsIgnoreCase(key)) {
				if (csourcetype != null) {
					return false;
				} else {
					//
					String pk_cargdoc = (String) getBillCardPanel().getHeadItem("pk_cargdoc").getValueObject();
					if (null == pk_cargdoc || "".equalsIgnoreCase(pk_cargdoc)) {
						showWarningMessage("前选择入库货位");
						return false;
					}
					JComponent c = getBillCardPanel().getBodyItem("ccunhuobianma").getComponent();
					if (c instanceof UIRefPane) {
						UIRefPane ref = (UIRefPane) c;
						ref.getRefModel().addWherePart("  and 	tb_spacegoods.pk_cargdoc='"+ pk_cargdoc + "' ");
					}
					return true;
				}
			}else if("nshouldoutnum".equalsIgnoreCase(key)){
				if(csourcetype != null){

					return false;
				} else {
					return true;
				}
			}else if("nshouldoutassistnum".equalsIgnoreCase(key)){
				if (csourcetype != null) {
				return false;
				} else {
					return true;
				}
			}
		}

		

		return super.beforeEdit(e);
	}

	public void setDefaultData() throws Exception {
		// 当前公司 当前库存组织 当前仓库 当前货位
		getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPrimaryKey());
		getBillCardPanel().setHeadItem("pk_calbody",
				WdsWlPubConst.DEFAULT_CALBODY);
		if (getBillOperate() != IBillOperate.OP_REFADD) {
			try {
				getBillCardPanel().setHeadItem("srl_pk",
						getLoginInforHelper().getCwhid(_getOperator()));
				getBillCardPanel().setHeadItem(
						"pk_cargdoc",
						getLoginInforHelper().getSpaceByLogUserForStore(
								_getOperator()));
				// getBillCardPanel().setHeadItem("",
				// LoginInforHelper.getLogInfor(userid));
			} catch (Exception e) {
				e.printStackTrace();// zhf 异常不处理
			}
		}
		// 制单人 制单日期
		getBillCardPanel().setTailItem("tmaketime", _getServerTime());
		getBillCardPanel().setHeadItem("dbilldate", _getDate());
		getBillCardPanel().setTailItem("coperatorid", _getOperator());
		getBillCardPanel().setHeadItem("vbilltype",
				WdsWlPubConst.BILLTYPE_OTHER_OUT);
		//getBillCardPanel().setHeadItem("is_yundan", UFBoolean.TRUE);
		// 单据状态
		getBillCardPanel().getHeadTailItem("vbillstatus").setValue(
				IBillStatus.FREE); // 自由状态
		try {
			getBillCardPanel().setHeadItem("vbillcode", getBillNo());
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public void setDefaultDataOnRef() throws Exception {
		// 当前公司 当前库存组织 当前仓库 当前货位
		getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPrimaryKey());
		getBillCardPanel().setHeadItem("pk_calbody",
				WdsWlPubConst.DEFAULT_CALBODY);
		try {
			// getBillCardPanel().setHeadItem("srl_pk",
			// LoginInforHelper.getCwhid(_getOperator()));
			getBillCardPanel().setHeadItem(
					"pk_cargdoc",
					getLoginInforHelper().getSpaceByLogUserForStore(
							_getOperator()));
			// // getBillCardPanel().setHeadItem("",
			// LoginInforHelper.getLogInfor(userid));
		} catch (Exception e) {
			e.printStackTrace();// zhf 异常不处理
		}
		// 制单人 制单日期
		getBillCardPanel().setTailItem("tmaketime", _getServerTime());
		getBillCardPanel().setHeadItem("dbilldate", _getDate());
		getBillCardPanel().setTailItem("coperatorid", _getOperator());
		getBillCardPanel().setHeadItem("vbilltype",
				WdsWlPubConst.BILLTYPE_OTHER_OUT);
		//getBillCardPanel().setHeadItem("is_yundan", UFBoolean.TRUE);
		setBillNo();
		// getBillCardPanel().setHeadItem("pwb_fbillflag",2);
		// getBillCardPanel().setHeadItem("vbillcode",
		// HYPubBO_Client.getBillNo(WdsWlPubConst.BILLTYPE_OTHER_OUT,
		// _getOperator(), null, null));
	}

	
	
	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
		String key=e.getKey();
		if("cdptid".equalsIgnoreCase(key)){
		getBillCardPanel().getHeadItem("cbizid").setValue(null);	
		}else if(key.equalsIgnoreCase("cbizid")){		
			getBillCardPanel().execHeadTailLoadFormulas();
		}	
	}

	@Override
	protected void initEventListener() {
		
		super.initEventListener();
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);

	}

	/**
	 * 表头的编辑前事件
	 */
	
	
	public boolean beforeEdit(BillItemEvent e) {
		String key = e.getItem().getKey();
		if (e.getItem().getPos() == BillItem.HEAD) {
			// 仓库过滤，只属于物流系统的
			// srl_pkr 为入库仓库
			if ("srl_pk".equalsIgnoreCase(key)) {
				JComponent c = getBillCardPanel().getHeadItem("srl_pk")
						.getComponent();
				if (c instanceof UIRefPane) {
					UIRefPane ref = (UIRefPane) c;
					ref.getRefModel().addWherePart(
							" and def1 = '1' and isnull(dr,0) = 0");
				}
			}
			if ("srl_pkr".equalsIgnoreCase(key)) {
				JComponent c = getBillCardPanel().getHeadItem("srl_pkr")
						.getComponent();
				if (c instanceof UIRefPane) {
					UIRefPane ref = (UIRefPane) c;
					ref.getRefModel().addWherePart(
							" and def1 = '1' and isnull(dr,0) = 0");
				}
			}
		}

		// 对当前货位下的管理员进行过滤
		if ("cwhsmanagerid".equalsIgnoreCase(key)) {
			String pk_cargdoc = (String) getBillCardPanel().getHeadItem(
					"pk_cargdoc").getValueObject();
			if (null == pk_cargdoc || "".equalsIgnoreCase(pk_cargdoc)) {
				showWarningMessage("前选择入库货位");
				return false;
			}
			JComponent c = getBillCardPanel().getHeadItem

			("cwhsmanagerid").getComponent();
			if (c instanceof UIRefPane) {
				UIRefPane ref = (UIRefPane) c;
				ref.getRefModel().addWherePart(
						"  and tb_stockstaff.pk_cargdoc='" + pk_cargdoc + "' ");
			}
			return true;
		}
		return true;
	}

	protected AbstractManageController createController() {
		return new OtherOutClientUICtrl();
	}

	/**
	 * 如果单据不走平台时，UI类需要重载此方法，返回不走平台的业务代理类
	 * 
	 * @return BusinessDelegator 不走平台的业务代理类
	 */
	protected BusinessDelegator createBusinessDelegator() {
		return new OtherOutDelegator();
	}

	/**
	 * 注册自定义按钮
	 */
	protected void initPrivateButton() {

		super.initPrivateButton();
		// zjBtn customizeButton1 = new zjBtn();
		// addPrivateButton(customizeButton1.getButtonVO());
		// zkBtn customizeButton2 = new zkBtn();
		// addPrivateButton(customizeButton2.getButtonVO());
		// cgqyBtn customizeButton3 = new cgqyBtn();
		// addPrivateButton(customizeButton3.getButtonVO());
		// zzdjBtn customizeButton4 = new zzdjBtn();
		// addPrivateButton(customizeButton4.getButtonVO());
		fzgnBtn customizeButton5 = new fzgnBtn();
		addPrivateButton(customizeButton5.getButtonVO());
		tpzdBtn customizeButton6 = new tpzdBtn();
		addPrivateButton(customizeButton6.getButtonVO());
		zdqhBtn customizeButton7 = new zdqhBtn();
		addPrivateButton(customizeButton7.getButtonVO());
		ckmxBtn customizeButton8 = new ckmxBtn();
		addPrivateButton(customizeButton8.getButtonVO());

		QxqzBtn customizeButton9 = new QxqzBtn();
		addPrivateButton(customizeButton9.getButtonVO());
		QzqrBtn customizeButton10 = new QzqrBtn();
		addPrivateButton(customizeButton10.getButtonVO());

		getButtonManager().getButtonAry(
				new int[] { ISsButtun.Qzqr, ISsButtun.Qxqz });// 取消签字,签字确认

		ButtonVO soOrder = new ButtonVO();
		soOrder.setBtnNo(nc.ui.wds.w80020206.buttun0206.ISsButtun.RefSendOrder);
		soOrder.setBtnCode(null);
		soOrder.setBtnName("发运订单");
		soOrder.setBtnChinaName("发运订单");
		addPrivateButton(soOrder);
		ButtonVO redSoorder = new ButtonVO();
		redSoorder.setBtnNo(nc.ui.wds.w80020206.buttun0206.ISsButtun.RefWDSC);
		redSoorder.setBtnCode(null);
		redSoorder.setBtnName("采购取样");
		redSoorder.setBtnChinaName("采购取样");
		addPrivateButton(redSoorder);
		ButtonVO refbill = ButtonVOFactory.getInstance().build(
				IBillButton.Refbill);
		refbill.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT,
				IBillOperate.OP_INIT });
		refbill.setChildAry(new int[] { soOrder.getBtnNo(),
				redSoorder.getBtnNo() });
		addPrivateButton(refbill);
	}

	public void afterUpdate() {
		if (!getBufferData().isVOBufferEmpty()) {
			int row = getBufferData().getCurrentRow();
			if (row < 0) {
				return;
			}
			Object o = getBufferData().getCurrentVO().getParentVO()
					.getAttributeValue(getBillField().getField_BillStatus());
			if (o.equals(IBillStatus.FREE)) {// 自由
				getButtonManager().getButton(ISsButtun.Qxqz).setEnabled(false);
				getButtonManager().getButton(ISsButtun.Qzqr).setEnabled(true);
			} else {// 签字
				getButtonManager().getButton(ISsButtun.Qzqr).setEnabled(false);
				getButtonManager().getButton(ISsButtun.Qxqz).setEnabled(true);
			}
			updateButtons();
		}
	}

	@Override
	public String getRefBillType() {
		// TODO Auto-generated method stub
		return curRefBilltype;
	}

	protected void setRefBillType(String curRefBilltype) {
		this.curRefBilltype = curRefBilltype;
	}
}
