package nc.ui.wds.tranprice.pub;

import nc.ui.pub.ButtonObject;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.wl.pub.LoginInforHelper;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wl.pub.LoginInforVO;
import nc.vo.wl.pub.WdsWlPubConst;

public abstract class TranPricePubUI extends BillManageUI {
	
	private  LoginInforVO m_loginInfor = null; 

	public TranPricePubUI(Boolean useBillSource) {
		super(useBillSource);
	}
	public TranPricePubUI() {
		super();
	}
	public TranPricePubUI(String pk_corp, String pk_billType, String pk_busitype,
			String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
	}
	
//	@Override
//	protected AbstractManageController createController() {
//		// TODO Auto-generated method stub
//		return null;
//	}

//	@Override
//	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
//			throws Exception {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
//			int intRow) throws Exception {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
//			throws Exception {
//		// TODO Auto-generated method stub
//
//	}

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
		
//		初始当前登录人 角色
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
	public boolean isSaveAndCommitTogether() {
		
		return true;
	}
	
	// 单据号a
	public String getBillNo() throws java.lang.Exception {
		return HYPubBO_Client.getBillNo(getUIControl().getBillType(),
				_getCorp().getPrimaryKey(), null, null);
	}

	@Override
	public void setDefaultData() throws Exception {
		getBillCardPanel().setHeadItem("vbillstatus", IBillStatus.FREE);
		getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPk_corp());
		getBillCardPanel().setTailItem("voperatorid", _getOperator());
		getBillCardPanel().setHeadItem("pk_billtype", WdsWlPubConst.WDSI);
		getBillCardPanel().setTailItem("dmakedate", _getDate());
		getBillCardPanel().setHeadItem("dbilldate", _getDate());
//		zhf add  初始化  默认 发货仓库 
		getBillCardPanel().setHeadItem("reserve1", m_loginInfor.getWhid());
	}

}
