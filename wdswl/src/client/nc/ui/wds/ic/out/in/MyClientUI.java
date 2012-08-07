package nc.ui.wds.ic.out.in;

import javax.swing.JComponent;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.ButtonVOFactory;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wds.ic.pub.MutiInPubClientUI;
import nc.ui.wds.w80020206.buttun0206.QxqzBtn;
import nc.ui.wds.w80020206.buttun0206.QzqrBtn;
import nc.ui.wds.w80060206.buttun0206.ISsButtun;
import nc.uif.pub.exception.UifException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.field.IBillField;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wl.pub.Wds2WlPubConst;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * 退货入库
 */
public class MyClientUI extends MutiInPubClientUI implements
		BillCardBeforeEditListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String curRefBilltype = null;

	protected ManageEventHandler createEventHandler() {
		return new OutInEventHandler(this, getUIControl());
	}

	public MyClientUI() {
		super();
		init();
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

	private void init() {
		getButtonManager().getButton(IBillButton.AddLine).setEnabled(false);
		updateButtons();
	}

	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
	}

	protected BusinessDelegator createBusinessDelegator() {
		return new OutInDelegator();
	}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	}

	public boolean beforeEdit(BillEditEvent e) {

		int row = e.getRow();
		String key = e.getKey();

		// 对入库货位过滤，过滤只属于对应的入库仓库下面的货位
		if ("cargdocname".equalsIgnoreCase(key)) {
			String pk_store = (String) getBillCardPanel().getHeadItem(
					"geh_cwarehouseid").getValueObject();
			if (null == pk_store || "".equalsIgnoreCase(pk_store)) {
				showWarningMessage("请选择入库仓库");
				return false;
			}
			JComponent c = getBillCardPanel().getBodyItem("cargdocname")
					.getComponent();
			if (c instanceof UIRefPane) {
				UIRefPane ref = (UIRefPane) c;
				ref.getRefModel().addWherePart(
						"  and bd_cargdoc.pk_stordoc='" + pk_store
								+ "' and isnull(bd_cargdoc.dr,0) = 0");
			}
			return true;
		}

		return super.beforeEdit(e);
	}

	@Override
	protected void initEventListener() {

		super.initEventListener();
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
	}

	public MyClientUI(String pk_corp, String pk_billType, String pk_busitype,
			String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
	}

	protected void initSelfData() {
		// 除去行操作多余按钮
		ButtonObject btnobj = getButtonManager().getButton(IBillButton.Line);
		if (btnobj != null) {
			// btnobj.removeChildButton(getButtonManager().getButton(IBillButton.CopyLine));
			// btnobj.removeChildButton(getButtonManager().getButton(IBillButton.PasteLine));
			btnobj.removeChildButton(getButtonManager().getButton(
					IBillButton.InsLine));
			// btnobj.removeChildButton(getButtonManager().getButton(IBillButton.AddLine));
		}
		super.initSelfData();
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
		String key = e.getKey();
		if ("geh_cdptid".equalsIgnoreCase(key)) {
			getBillCardPanel().getHeadItem("geh_cbizid").setValue(null);
		} else if (key.equalsIgnoreCase("geh_cbizid")) {
			getBillCardPanel().execHeadTailLoadFormulas();
		}

	}

	public void setDefaultData() throws Exception {
		// 当前公司 当前库存组织 当前仓库 当前货位
		getBillCardPanel().setHeadItem("geh_corp", _getCorp().getPk_corp());// 入库公司
		getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPk_corp());// 当前登录公司
		getBillCardPanel().setHeadItem("geh_calbody",
				WdsWlPubConst.DEFAULT_CALBODY);
		if (getBillOperate() != IBillOperate.OP_REFADD) {
			try {
				getBillCardPanel().setHeadItem("geh_cwarehouseid",
						getLoginInforHelper().getCwhid(_getOperator()));
				getBillCardPanel().setHeadItem(
						"pk_cargdoc",
						getLoginInforHelper().getSpaceByLogUserForStore(
								_getOperator()));
			} catch (Exception e) {
				showHintMessage("当前登录人没有绑定到仓库");
			}

		}
		getBillCardPanel().setHeadItem("geh_billtype",
				WdsWlPubConst.BILLTYPE_OUT_IN);
		getBillCardPanel().setHeadItem("geh_cbilltypecode",
				WdsWlPubConst.BILLTYPE_OUT_IN);// 入库单据类型
		getBillCardPanel().setHeadItem("pwb_fbillflag", IBillStatus.FREE);// 单据状态
		getBillCardPanel().setHeadItem("geh_billcode", getBillNo());
		// 制单人 制单日期
		getBillCardPanel().setTailItem("copetadate", _getDate());
		getBillCardPanel().setTailItem("coperatorid", _getOperator());
		getBillCardPanel().setTailItem("tmaketime", _getServerTime());
		getBillCardPanel().setHeadItem("geh_dbilldate", _getDate());
		getBillCardPanel().setHeadItem("geh_cdispatcherid",
				getDefaultDispatcherid());
	}

	private String getDefaultDispatcherid() throws UifException {
		Object o = HYPubBO_Client.findColValue("wds_rdcl", "pk_rdcl",
				" isnull(dr,0)=0 and pk_corp = '" + _getCorp().getPrimaryKey()
						+ "' and isnull(sealflag,'N') = 'N' and rdcode = '"
						+ Wds2WlPubConst.wdsz_dispatchercode + "' ");// 默认收发类别"销售退货入库"
		return PuPubVO.getString_TrimZeroLenAsNull(o);
	}

	@Override
	protected String getBillNo() throws Exception {
		return HYPubBO_Client.getBillNo(WdsWlPubConst.BILLTYPE_OUT_IN,
				_getOperator(), null, null);
	}

	@Override
	protected IBillField createBillField() {
		return new BillField();
		// return null;
	}

	protected AbstractManageController createController() {
		return new OutInClientUICtrl();
	}

	// @Override
	// public boolean beforeEdit(BillEditEvent e) {
	// int row = e.getRow();
	// String key=e.getKey();
	//		
	// String csourcetype =
	// PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel()
	// .getBodyValueAt(row, "csourcetype"));
	// //如果是参照过来的不可以编辑 ，如果是自制单据可以编辑
	// if ("invcode".equalsIgnoreCase(key)) {
	// if(getBillOperate() == IBillOperate.OP_EDIT)//zhf add 20110624 修改时
	// 存货编码不能修改
	// return false;
	// if (csourcetype != null) {
	// return false;
	// } else {
	// String pk_cargdoc=(String)
	// getBillCardPanel().getHeadItem("pk_cargdoc").getValueObject();
	// if(null==pk_cargdoc || "".equalsIgnoreCase(pk_cargdoc)){
	// showWarningMessage("前选择入库货位");
	// return false;
	// }
	// JComponent c =getBillCardPanel().getBodyItem("invcode").getComponent();
	// if( c instanceof UIRefPane){
	// UIRefPane ref = (UIRefPane)c;
	// ref.getRefModel().addWherePart("  and tb_spacegoods.pk_cargdoc='"+pk_cargdoc+"' ");
	// }
	// return true;
	// }
	// }
	// if("geb_snum".equalsIgnoreCase(key)){
	// if(getBillOperate() == IBillOperate.OP_EDIT)//zhf add 20110624 修改时
	// 存货编码不能修改
	// return false;
	// if (csourcetype != null) {
	// return false;
	// } else {
	// return true;
	// }
	// }if("geb_bsnum".equalsIgnoreCase(key)){
	// if(getBillOperate() == IBillOperate.OP_EDIT)//zhf add 20110624 修改时
	// 存货编码不能修改
	// return false;
	// // if (csourcetype != null) {
	// // return false;
	// // } else {
	// return true;
	// // }
	// }
	//			
	// // //过滤当前货位下的存货
	// // if("invcode".equalsIgnoreCase(key)){
	// //
	// // return true;
	// //
	// // }
	//	
	//
	// return super.beforeEdit(e);
	// }
	/**
	 * 注册自定义按钮
	 */
	protected void initPrivateButton() {
		super.initPrivateButton();
		QzqrBtn customizeButton9 = new QzqrBtn();
		addPrivateButton(customizeButton9.getButtonVO());
		QxqzBtn customizeButton10 = new QxqzBtn();
		addPrivateButton(customizeButton10.getButtonVO());

		// 添加参照 其他出库 按钮 for add mlr
		ButtonVO refwds6 = new ButtonVO();
		refwds6.setBtnNo(nc.ui.wds.w80020206.buttun0206.ISsButtun.Ref4Z);
		refwds6.setBtnCode(null);
		refwds6.setBtnName("销售订单");
		refwds6.setBtnChinaName("销售订单");
		addPrivateButton(refwds6);

		ButtonVO refbill = ButtonVOFactory.getInstance().build(
				IBillButton.Refbill);
		refbill.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT,
				IBillOperate.OP_INIT });
		refbill.setChildAry(new int[] { refwds6.getBtnNo() });
		addPrivateButton(refbill);
	}

	@Override
	public String getRefBillType() {
		return curRefBilltype;
	}

	protected void setRefBillType(String curRefBilltype) {
		this.curRefBilltype = curRefBilltype;
	}

	public boolean beforeEdit(BillItemEvent e) {
		String key = e.getItem().getKey();
		// 对入库仓库过滤，过滤只属于物流的仓库
		if ("geh_cwarehouseid".equalsIgnoreCase(key)) {
			JComponent c = getBillCardPanel().getHeadItem("geh_cwarehouseid")
					.getComponent();
			if (c instanceof UIRefPane) {
				UIRefPane ref = (UIRefPane) c;
				ref.getRefModel().addWherePart(
						"  and def1 = '1' and isnull(dr,0) = 0");
			}
			return true;
		}
		// 对出库仓库过滤，过滤只属于物流的仓库
		if ("geh_cotherwhid".equalsIgnoreCase(key)) {
			JComponent c = getBillCardPanel().getHeadItem("geh_cotherwhid")
					.getComponent();
			if (c instanceof UIRefPane) {
				UIRefPane ref = (UIRefPane) c;
				ref.getRefModel().addWherePart(
						"  and def1 = '1' and isnull(dr,0) = 0");
			}
			return true;
		}
		// 对入库货位过滤，过滤只属于对应的入库仓库下面的货位
		if ("pk_cargdoc".equalsIgnoreCase(key)) {
			String pk_store = (String) getBillCardPanel().getHeadItem(
					"geh_cwarehouseid").getValueObject();
			if (null == pk_store || "".equalsIgnoreCase(pk_store)) {
				showWarningMessage("请选择入库仓库");
				return false;
			}
			JComponent c = getBillCardPanel().getHeadItem("pk_cargdoc")
					.getComponent();
			if (c instanceof UIRefPane) {
				UIRefPane ref = (UIRefPane) c;
				ref.getRefModel().addWherePart(
						"  and bd_cargdoc.pk_stordoc='" + pk_store
								+ "' and isnull(bd_cargdoc.dr,0) = 0");
			}
			return true;
		}
		// 对当前货位下的管理员进行过滤
		if ("geh_cwhsmanagerid".equalsIgnoreCase(key)) {
			String pk_cargdoc = (String) getBillCardPanel().getHeadItem(
					"pk_cargdoc").getValueObject();
			if (null == pk_cargdoc || "".equalsIgnoreCase(pk_cargdoc)) {
				showWarningMessage("前选择入库货位");
				return false;
			}
			JComponent c = getBillCardPanel().getHeadItem("geh_cwhsmanagerid")
					.getComponent();
			if (c instanceof UIRefPane) {
				UIRefPane ref = (UIRefPane) c;
				ref.getRefModel().addWherePart(
						"  and tb_stockstaff.pk_cargdoc='" + pk_cargdoc + "' ");
			}
			return true;
		}
		return true;
	}

	@Override
	public Object getUserObject() {
		return new GetCheck();
	}

}