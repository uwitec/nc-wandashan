package nc.ui.wds2.send;

import javax.swing.ListSelectionModel;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wl.pub.WdsBillManagUI;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.wl.pub.Wds2WlPubConst;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * 调入运单
 * @author zhf  copy by 发运订单
 * 
 */

public class ClientUI extends WdsBillManagUI {

	public ClientUI() {
		super();
	}

	public ClientUI(Boolean useBillSource) {
		super(useBillSource);
	}

	@Override
	protected void initEventListener() {
		// TODO Auto-generated method stub
		super.initEventListener();
		//		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
		//liuys add for 完达山项目  支持列表界面多选
		getBillListPanel().getHeadTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
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
	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
	}

	@Override
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
	throws Exception {
	}

	@Override
	protected void initSelfData() {}


	@Override
	public void setDefaultData() throws Exception {
		getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPk_corp());
		getBillCardPanel().setTailItem("voperatorid", _getOperator());
		getBillCardPanel().setHeadItem("pk_billtype", WdsWlPubConst.WDS3);
		getBillCardPanel().setTailItem("dmakedate", _getDate());	
	}

	protected ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this, getUIControl());
	}

//	protected BusinessDelegator createBusinessDelegator() {
//		return new ClientBusinessDelegator(this);
//	}

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
	public String getBillType() {
		// TODO Auto-generated method stub
		return Wds2WlPubConst.billtype_alloinsendorder;
	}

	@Override
	public boolean isLinkQueryEnable() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getAssNumFieldName() {
		// TODO Auto-generated method stub
		return "nonlinenum";
	}

	@Override
	public String getHslFieldName() {
		// TODO Auto-generated method stub
		return "nhsl";
	}
}