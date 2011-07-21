package nc.ui.zb.bidding.view;

import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bill.BillTemplateWrapper;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.zb.bidding.make.ClientBusinessDelegator;
import nc.ui.zb.pub.MultiChildBillManageUI;
import nc.vo.trade.button.ButtonVO;
import nc.vo.zb.bidding.BidViewBillVO;
import nc.vo.zb.bidding.BiddingBillVO;
import nc.vo.zb.bidding.BiddingHeaderVO;
import nc.vo.zb.pub.ZbPuBtnConst;

public class ViewBiddingUI extends MultiChildBillManageUI {

	public ViewBiddingUI() {
		super();
		init();
		
	}

	public boolean flag =true;
	@Override
	protected AbstractManageController createController() {
		flag =ViewBiddingHelper.reValue();
		return new ViewController(flag);
	}
	
	@Override
	protected ManageEventHandler createEventHandler() {
		return new ViewEventHandler(this, getUIControl(),flag);
	}
	
	protected BusinessDelegator createBusinessDelegator() {
		if(flag)
		    return new ViewBusinessDelegator();
		else
			return new ClientBusinessDelegator();
	}
	
	private void init() {
		BidViewBillVO[] bills = null;
		if(flag){
			try {
				bills = ViewBiddingHelper.loadDatas(_getOperator()); 
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (bills != null && bills.length > 0){
			setDatasTOUI(bills);

			getBufferData().addVOsToBuffer(bills);
			updateUI();
		}
	}
	
	private void setBillOperate(){
		try {
			setBillOperate(IBillOperate.OP_NOTEDIT);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void setDatasTOUI(BidViewBillVO[] bills){
		if(bills == null || bills.length == 0){
			setBillOperate();
			return;
		}
		int len = bills.length;
		if(len==1){
			if (isListPanelSelected()){
				getBillListPanel().getHeadBillModel().setBodyDataVO(new BiddingHeaderVO[]{bills[0].getHeader()});
				getBillListPanel().getHeadBillModel().execLoadFormula();
				setCurrentPanel(BillTemplateWrapper.CARDPANEL);
			}
			setBillOperate();
			getBillCardPanel().getBillData().setHeaderValueVO(bills[0].getHeader());
			getBillCardPanel().getBillModel(BiddingBillVO.tablecode_body).setBodyDataVO(bills[0].getTableVO(BiddingBillVO.tablecode_body));
			getBillCardPanel().getBillModel(BiddingBillVO.tablecode_times).setBodyDataVO(bills[0].getTableVO(BiddingBillVO.tablecode_times));
			getBillCardPanel().execHeadTailLoadFormulas();
			getBillCardPanel().getBillModel(BiddingBillVO.tablecode_body).execLoadFormula();
			getBillCardPanel().getBillModel(BiddingBillVO.tablecode_times).execLoadFormula();
		}
		
		if(len>1){
			BiddingHeaderVO[] heads = new BiddingHeaderVO[len];
			setBillOperate();
			for(int i=0;i<len;i++){
				BiddingHeaderVO head =bills[i].getHeader();
				heads[i]=head;
			}
			getBillListPanel().getHeadBillModel().setBodyDataVO(heads);
			getBillListPanel().getHeadBillModel().execLoadFormula();
		}
		
	}
	
	@Override
	protected void initPrivateButton() {
		
		// 打印管理
		ButtonVO btnvo9 = new ButtonVO();
		btnvo9.setBtnNo(ZbPuBtnConst.ASSPRINT);
		btnvo9.setBtnName("打印管理");
		btnvo9.setBtnChinaName("打印管理");
		btnvo9.setBtnCode(null);// code最好设置为空
		btnvo9.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT});
		btnvo9.setChildAry(new int[] { IBillButton.Print,IBillButton.DirectPrint});
		addPrivateButton(btnvo9);
		super.initPrivateButton();
	}
}
