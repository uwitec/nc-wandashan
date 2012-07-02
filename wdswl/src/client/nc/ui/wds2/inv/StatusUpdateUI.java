package nc.ui.wds2.inv;

import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wl.pub.LoginInforHelper;
import nc.ui.zmpub.pub.bill.DefBillManageUI;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.wl.pub.LoginInforVO;
import nc.vo.wl.pub.Wds2WlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;

public class StatusUpdateUI extends DefBillManageUI {
	
private LoginInforHelper helper = null;
	
	public LoginInforHelper getLoginInforHelper(){
		if(helper == null){
			helper = new LoginInforHelper();
		}
		return helper;
	}

	@Override
	public boolean isLinkQueryEnable() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected AbstractManageController createController() {
		// TODO Auto-generated method stub
		return new StatusUpdateCtrl();
	}

	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDefaultData() throws Exception {
		// TODO Auto-generated method stub
		super.setDefaultData();
		LoginInforVO infor = null;
		try{
			infor = getLoginInforHelper().getLogInfor(_getOperator());
		}catch(BusinessException be){
			be.printStackTrace();
			showErrorMessage(WdsWlPubTool.getString_NullAsTrimZeroLen(be.getMessage()));
			return;
		}
		 
		
		if(infor == null){
			showErrorMessage("当前操作人未绑定货位");
			return;
		}
		
		getBillCardPanel().setHeadItem("cwarehouseid", infor.getWhid());
		getBillCardPanel().setHeadItem("ccargdocid", infor.getSpaceid());
//		getBillCardPanel().setHeadItem("vemployeeid", infor.get);
	}
	
	public void afterEdit(BillEditEvent e) {
		if(e.getPos() == BillData.BODY){
			String key = e.getKey();
			int row = e.getRow();
			if(key.equalsIgnoreCase("invstatus2")){
				String o = WdsWlPubTool.getString_NullAsTrimZeroLen(getBillCardPanel().getBodyValueAt(row, "cinvstatusid"));
				String o2 = WdsWlPubTool.getString_NullAsTrimZeroLen(getBillCardPanel().getBodyValueAt(row, "cinvstatusid2"));
				if(o.equalsIgnoreCase(o2)){
					showErrorMessage("调整后状态和调整前相同");
					return;
				}				
			}
		}
		super.afterEdit(e);
	}
	
	public boolean beforeEdit(BillEditEvent e) {
		String key = e.getKey();
		if("invstatus2".equalsIgnoreCase(key)){//调整后状态
			Object o = getBillCardPanel().getBodyValueAt(e.getRow(), "cinvstatusid");
			if(o == null)
				return false;
			return true;
		}
		if("vbatchcode".equalsIgnoreCase(key)){//批次
			Object o = getBillCardPanel().getBodyValueAt(e.getRow(), "cinvbasid");
			if(o == null)
				return false;
			return true;
		}
		if("invcode".equalsIgnoreCase(key)){//表头货位
			Object o = getBillCardPanel().getHeadItem("ccargdocid").getValueObject();
			if(o == null)
				return false;
			return true;
		}
		return super.beforeEdit(e);
	}

	@Override
	protected ManageEventHandler createEventHandler() {
		// TODO Auto-generated method stub
		return new StatusUpdateEventHandler(this,getUIControl());
	}

	@Override
	public String getBillType() {
		// TODO Auto-generated method stub
		return Wds2WlPubConst.billtype_statusupdate;
	}
}
