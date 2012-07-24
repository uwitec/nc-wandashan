package nc.ui.dm.so.order;

import javax.swing.JComponent;
import javax.swing.ListSelectionModel;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wl.pub.WdsBillManagUI;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wl.pub.ButtonCommon;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * 销售运单
 * @author mlr
 * 
 */
public class ClientUI extends WdsBillManagUI implements BillCardBeforeEditListener {

	private static final long serialVersionUID = -3998675844592858916L;
	
	public ClientUI() {
		super();
	}

	@Override
	protected void initEventListener() {
		
		super.initEventListener();
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
		//liuys add for 完达山项目  支持列表界面多选
		getBillListPanel().getHeadTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
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
	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
	}

	@Override
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	}
	@Override
	protected void initPrivateButton() {
		// TODO Auto-generated method stub
		super.initPrivateButton();
		ButtonVO col = new ButtonVO();
		col.setBtnNo(ButtonCommon.TRAN_COL);
		col.setBtnCode(null);
		col.setBtnName("运费核算");
		col.setBtnChinaName("运费核算");
		col.setOperateStatus(new int[]{IBillOperate.OP_NO_ADDANDEDIT});
		col.setBusinessStatus(new int[]{IBillStatus.FREE});
		addPrivateButton(col);
		ButtonVO lock = new ButtonVO();
		lock.setBtnNo(ButtonCommon.LOCK);
		lock.setBtnCode(null);
		lock.setBtnName("冻结");
		lock.setBtnChinaName("冻结");
		lock.setOperateStatus(new int[]{IBillOperate.OP_NO_ADDANDEDIT});
		lock.setBusinessStatus(new int[]{IBillStatus.FREE});
		addPrivateButton(lock);
		ButtonVO unlock = new ButtonVO();
		unlock.setBtnNo(ButtonCommon.UNLOCK);
		unlock.setBtnCode(null);
		unlock.setBtnName("解冻");
		unlock.setBtnChinaName("解冻");
		unlock.setOperateStatus(new int[]{IBillOperate.OP_NO_ADDANDEDIT});
		unlock.setBusinessStatus(new int[]{IBillStatus.FREE});
		addPrivateButton(unlock);
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
		getBillCardPanel().setTailItem("voperatorid", _getOperator());
		getBillCardPanel().setHeadItem("pk_billtype", WdsWlPubConst.WDS5);
		getBillCardPanel().setTailItem("dmakedate", _getDate());
		

	}

	protected ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this, getUIControl());
	}

	// 单据号
	public String getBillNo() throws java.lang.Exception {
		return HYPubBO_Client.getBillNo(getUIControl().getBillType(),
				_getCorp().getPrimaryKey(), null, null);
	}


	@Override
	public boolean beforeEdit(BillEditEvent e) {
		String key = e.getKey();		
		if(e.getPos() ==BillItem.HEAD){
			//仓库过滤，只属于物流系统的
			if("pk_outwhouse".equalsIgnoreCase(key)){
				JComponent c =getBillCardPanel().getHeadItem("pk_outwhouse").getComponent();
				if( c instanceof UIRefPane){
					UIRefPane ref = (UIRefPane)c;
					ref.getRefModel().addWherePart(" def1 = '1' and isnull(dr,0) = 0");
				}
			}
			if("pk_inwhouse".equalsIgnoreCase(key)){
				JComponent c =getBillCardPanel().getHeadItem("pk_inwhouse").getComponent();
				if( c instanceof UIRefPane){
					UIRefPane ref = (UIRefPane)c;
					ref.getRefModel().addWherePart(" def1 = '1' and isnull(dr,0) = 0");
				}
			}			
		}else if(e.getPos() ==BillItem.BODY){}
		return super.beforeEdit(e);
	}
		@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
		Object value = e.getValue();
		if(e.getPos()==BillItem.HEAD){
			if("icoltype".equalsIgnoreCase(e.getKey())){
				if("手动".equals(value)){
					getBillCardPanel().getTailItem("ngls").setEnabled(true);
					getBillCardPanel().getTailItem("ntranprice").setEnabled(true);
					getBillCardPanel().getTailItem("iadjusttype").setEnabled(true);
					getBillCardPanel().getTailItem("nadjustprice").setEnabled(true);
					getBillCardPanel().getTailItem("ntransmny").setEnabled(true);
				}else{
					getBillCardPanel().getTailItem("ngls").setEnabled(false);
					getBillCardPanel().getTailItem("ntranprice").setEnabled(false);
					getBillCardPanel().getTailItem("iadjusttype").setEnabled(false);
					getBillCardPanel().getTailItem("nadjustprice").setEnabled(false);
					getBillCardPanel().getTailItem("ntransmny").setEnabled(false);
				
				}
			
			}
		}
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

	public boolean beforeEdit(BillItemEvent e) {
		String key = e.getItem().getKey();
		if(e.getItem().getPos() ==BillItem.HEAD){
			//仓库过滤，只属于物流系统的
			if("pk_outwhouse".equalsIgnoreCase(key)){
				JComponent c =getBillCardPanel().getHeadItem("pk_outwhouse").getComponent();
				if( c instanceof UIRefPane){
					UIRefPane ref = (UIRefPane)c;
					ref.getRefModel().addWherePart("  and def1 = '1' and isnull(dr,0) = 0");
				}
			}
			
			if("pk_inwhouse".equalsIgnoreCase(key)){
				JComponent c =getBillCardPanel().getHeadItem("pk_inwhouse").getComponent();
				if( c instanceof UIRefPane){
					UIRefPane ref = (UIRefPane)c;
					ref.getRefModel().addWherePart("  and def1 = '1' and isnull(dr,0) = 0");
				}
			}
		}else if(e.getItem().getPos() ==BillItem.BODY){}
		return true;
	}

	@Override
	public String getBillType() {
		// TODO Auto-generated method stub

		return WdsWlPubConst.WDS5;
	}

	@Override
	public boolean isLinkQueryEnable() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getAssNumFieldName() {
		// TODO Auto-generated method stub
		return "nassarrangnum";
	}

	@Override
	public String getHslFieldName() {
		// TODO Auto-generated method stub
		return "nhgrate";
	}

}