package nc.ui.wds.tranprice.bill;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.ButtonVOFactory;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wl.pub.ButtonCommon;
import nc.vo.wl.pub.WdsWlPubConst;

//运费核算单  处理 销售运费核算和转分仓运费核算
public class SendBillUI extends BillManageUI {

	private static final long serialVersionUID = 6910905613905096443L;
	
	private String curRefBilltype;

	@Override
	protected AbstractManageController createController() {
		
		return new SendBillCtrl();
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
		// TODO Auto-generated method stub
		
	}
	public void setRefDefalutData() throws Exception{
		getBillCardPanel().setHeadItem("vbillstatus", IBillStatus.FREE);
		getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPk_corp());
		getBillCardPanel().setHeadItem("pk_billtype", WdsWlPubConst.WDSM);
		getBillCardPanel().setHeadItem("dbilldate", _getDate());
		getBillCardPanel().setTailItem("voperatorid", _getOperator());
		getBillCardPanel().setTailItem("dmakedate", _getDate());
		setBillNo();	
	}
	
	@Override
	protected void initPrivateButton() {
		
		ButtonVO col = new ButtonVO();
		col.setBtnNo(ButtonCommon.TRAN_COL);
		col.setBtnCode(null);
		col.setBtnName("运费核算");
		col.setBtnChinaName("运费核算");
		col.setOperateStatus(new int[]{IBillOperate.OP_NO_ADDANDEDIT});
		col.setBusinessStatus(new int[]{IBillStatus.FREE});
		addPrivateButton(col);
//		ButtonVO ref1 = new ButtonVO();
//		ref1.setBtnNo(ButtonCommon.REFWDS6);
//		ref1.setBtnCode(null);
//		ref1.setBtnName("其他出库");
//		ref1.setBtnChinaName("其他出库");
//		addPrivateButton(ref1);
//		ButtonVO ref3 = new ButtonVO();
//		ref3.setBtnNo(ButtonCommon.REFWDS8);
//		ref3.setBtnCode(null);
//		ref3.setBtnName("销售出库");
//		ref3.setBtnChinaName("销售出库");
//		addPrivateButton(ref3);
		
		ButtonVO ref4 = new ButtonVO();
		ref4.setBtnNo(ButtonCommon.REFWDS10);
		ref4.setBtnCode(null);
		ref4.setBtnName("发运订单");
		ref4.setBtnChinaName("发运订单");
		addPrivateButton(ref4);
		
		ButtonVO ref5 = new ButtonVO();
		ref5.setBtnNo(ButtonCommon.REFWDS11);
		ref5.setBtnCode(null);
		ref5.setBtnName("销售运单");
		ref5.setBtnChinaName("销售运单");
		addPrivateButton(ref5);
		
		ButtonVO refbill = ButtonVOFactory.getInstance().build(IBillButton.Refbill);
		refbill.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT, IBillOperate.OP_INIT });
		refbill.setChildAry(new int[]{ref4.getBtnNo(),ref5.getBtnNo()});
		addPrivateButton(refbill);
	}
	// 单据号
	public String getBillNo() throws java.lang.Exception {
		return HYPubBO_Client.getBillNo(getUIControl().getBillType(),
				_getCorp().getPrimaryKey(), null, null);
	}
	
	@Override
	protected ManageEventHandler createEventHandler() {
		return new SendBillEventHandler(this, getUIControl());
	}
	@Override
	public String getRefBillType() {
		// TODO Auto-generated method stub
		return curRefBilltype;
	}

	protected void setRefBillType(String curRefBilltype) {
		this.curRefBilltype = curRefBilltype;
	}
	
	@Override
	public boolean isSaveAndCommitTogether() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public void afterEdit(BillEditEvent e) {
		String key = e.getKey();
		if("icoltype".equalsIgnoreCase(key)){
			afterIcoltype(e);
		}
	}
	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 
	 * 运费计算类型，编辑后事件
	 * 来源相同的数据 运费计算类型相同
	 * @时间：2011-5-23下午12:05:12
	 * @param e
	 */
	private void afterIcoltype(BillEditEvent e){
		String value = (String)e.getValue();
		if(value == null)
			return;
		int count = getBillCardPanel().getBillTable().getRowCount();
		Object hid = getBillCardPanel().getBillModel().getValueAt(e.getRow(), "csourcebillhid");
		for(int row=0;row<count;row++){
			Object csourcebillhid = getBillCardPanel().getBillModel().getValueAt(row, "csourcebillhid");
			if(csourcebillhid == null || "".equals(csourcebillhid))
				continue;
			if(hid.equals(csourcebillhid)){
				getBillCardPanel().getBillModel().setValueAt(value, row, e.getKey());
			}
		}
	}
}
