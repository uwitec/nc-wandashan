package nc.ui.wds.load.account;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.ButtonVOFactory;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wl.pub.MutilChildUI;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wl.pub.ButtonCommon;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.Button.CommonButtonDef;

/**
 *  装卸费结算
 * @author Administrator
 * 
 */
public class ClientUI extends MutilChildUI {

	private static final long serialVersionUID = -3998675844592858916L;
	
	private String curRefBilltype;
	
	public ClientUI() {
		super();
	}

	public ClientUI(Boolean useBillSource) {
		super(useBillSource);
	}

	public ClientUI(String pk_corp, String pk_billType, String pk_busitype,
			String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
	}
	@Override
	protected AbstractManageController createController() {
		return new ClientController();
	}
	@Override
	protected BusinessDelegator createBusinessDelegator() {
		// TODO Auto-generated method stub
		return new ClientBusinessDelegator();
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
		//
		ButtonObject btn = getButtonManager().getButton(IBillButton.Line);
		if (btn != null) {
			btn.removeChildButton(getButtonManager().getButton(
					IBillButton.CopyLine));
			btn.removeChildButton(getButtonManager().getButton(
					IBillButton.PasteLine));
			btn.removeChildButton(getButtonManager().getButton(
					IBillButton.InsLine));
		}
	}

	@Override
	public void setDefaultData() throws Exception {
		getBillCardPanel().setHeadItem("vbillstatus", IBillStatus.FREE);
		getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPk_corp());
		getBillCardPanel().setHeadItem("pk_billtype", WdsWlPubConst.WDSF);
		getBillCardPanel().setHeadItem("dbilldate", _getDate());
		getBillCardPanel().setTailItem("voperatorid", _getOperator());
		getBillCardPanel().setTailItem("dmakedate", _getDate());		
	}
	public void setRefDefalutData() throws Exception{
		getBillCardPanel().setHeadItem("vbillstatus", IBillStatus.FREE);
		getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPk_corp());
		getBillCardPanel().setHeadItem("pk_billtype", WdsWlPubConst.WDSF);
		getBillCardPanel().setHeadItem("dbilldate", _getDate());
		getBillCardPanel().setTailItem("voperatorid", _getOperator());
		getBillCardPanel().setTailItem("dmakedate", _getDate());
		getBillCardPanel().setHeadItem("floadtype", getFloadType());
		setBillNo();	
		
	}
	private int  getFloadType(){
		if(WdsWlPubConst.BILLTYPE_OTHER_OUT.equalsIgnoreCase(curRefBilltype)
				|| WdsWlPubConst.BILLTYPE_SALE_OUT.equalsIgnoreCase(curRefBilltype)){
			return 0;
		}else if(WdsWlPubConst.BILLTYPE_OTHER_IN.equalsIgnoreCase(curRefBilltype)
				|| WdsWlPubConst.BILLTYPE_ALLO_IN.equalsIgnoreCase(curRefBilltype)){
			return 1;
			
		}
		return 0;
	}
	@Override
	protected void initPrivateButton() {
		// TODO Auto-generated method stub
		super.initPrivateButton();
		CommonButtonDef def = new CommonButtonDef();
		ButtonVO  joinup = def.getJoinUPButton();
		addPrivateButton(joinup);
		ButtonVO ref1 = new ButtonVO();
		ref1.setBtnNo(ButtonCommon.REFWDS6);
		ref1.setBtnCode(null);
		ref1.setBtnName("其他出库");
		ref1.setBtnChinaName("其他出库");
		addPrivateButton(ref1);
		ButtonVO ref2 = new ButtonVO();
		ref2.setBtnNo(ButtonCommon.REFWDS7);
		ref2.setBtnCode(null);
		ref2.setBtnName("其他入库");
		ref2.setBtnChinaName("其他入库");
		addPrivateButton(ref2);
		ButtonVO ref3 = new ButtonVO();
		ref3.setBtnNo(ButtonCommon.REFWDS8);
		ref3.setBtnCode(null);
		ref3.setBtnName("销售出库");
		ref3.setBtnChinaName("销售出库");
		addPrivateButton(ref3);
		ButtonVO ref4 = new ButtonVO();
		ref4.setBtnNo(ButtonCommon.REFWDS9);
		ref4.setBtnCode(null);
		ref4.setBtnName("调拨入库");
		ref4.setBtnChinaName("调拨入库");
		addPrivateButton(ref4);
		ButtonVO refbill = ButtonVOFactory.getInstance().build(IBillButton.Refbill);
		refbill.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT, IBillOperate.OP_INIT });
		refbill.setChildAry(new int[]{ref1.getBtnNo(),ref2.getBtnNo(),ref3.getBtnNo(),ref4.getBtnNo()});
		addPrivateButton(refbill);
	}

	protected ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this, getUIControl());
	}

	// 单据号
	public String getBillNo() throws java.lang.Exception {
		return HYPubBO_Client.getBillNo(getUIControl().getBillType(),
				_getCorp().getPrimaryKey(), null, null);
	}
	/**
	 * 增加后台校验
	 */
	public Object getUserObject() {
		return null;
	}

	@Override
	public boolean isSaveAndCommitTogether() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String getRefBillType() {
		// TODO Auto-generated method stub
		return curRefBilltype;
	}

	
	
	@Override
	public void afterEdit(BillEditEvent e) {
		
		super.afterEdit(e);
		//key e.getKey()
		
	}

	protected void setRefBillType(String curRefBilltype) {
		this.curRefBilltype = curRefBilltype;
	}

}