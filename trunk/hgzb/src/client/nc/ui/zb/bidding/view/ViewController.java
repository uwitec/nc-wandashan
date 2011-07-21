package nc.ui.zb.bidding.view;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.button.IBillButton;
import nc.vo.zb.bidding.BidViewBillVO;
import nc.vo.zb.bidding.BiddingBillVO;
import nc.vo.zb.bidding.BiddingBodyVO;
import nc.vo.zb.bidding.BiddingHeaderVO;
import nc.vo.zb.bidding.BiddingSuppliersVO;
import nc.vo.zb.bidding.BiddingTimesVO;
import nc.vo.zb.pub.ZbPuBtnConst;
import nc.vo.zb.pub.ZbPubConst;

public class ViewController extends AbstractManageController {
	
	private boolean temp =true;
	public ViewController(boolean flag) {
		temp=flag;
	}
	public int[] getCardButtonAry() {
		int[] buttonArray = new int[] {  
				IBillButton.Query,
				IBillButton.Return, 
				IBillButton.Brow,
				IBillButton.Refresh,
				IBillButton.Print
				};
		return buttonArray;
	}
	public int[] getListButtonAry() {
		int[] buttonArray = new int[] {  
				IBillButton.Query,
				IBillButton.Card, 
				IBillButton.Brow, 
				IBillButton.Refresh
				};
		return buttonArray;
	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public String getBillType() {
		if(temp)
			 return ZbPubConst.ZB_BIDVIEW_BILLTYPE;
		  return ZbPubConst.ZB_BIDDING_BILLTYPE;
	}

	public String[] getBillVoName() {
		if(temp){
			return new String[] { 
					BidViewBillVO.class.getName(),
					BiddingHeaderVO.class.getName(),
					BiddingBodyVO.class.getName(),
					BiddingTimesVO.class.getName()
					};
		}
		return new String[] { 
				BiddingBillVO.class.getName(),
				BiddingHeaderVO.class.getName(),
				BiddingBodyVO.class.getName(),
				BiddingSuppliersVO.class.getName(),
				BiddingTimesVO.class.getName()
				};
		
	}

	public String getBodyCondition() {
		return null;
	}


	public String getChildPkField() {
		return "";
	}

	public String getPkField() {
		return "cbiddingid";
	}

	public Boolean isEditInGoing() throws Exception {
		return false;
	}

	public boolean isExistBillStatus() {
		return true;
	}

	public boolean isLoadCardFormula() {
		return true;
	}

	

	public boolean isShowListRowNo() {
		return true;
	}

	public String[] getCardBodyHideCol() {
		return null;
	}

	public boolean isShowCardTotal() {
		return false;
	}

	public String getBodyZYXKey() {

		return null;
	}

	public String getHeadZYXKey() {

		return null;
	}

	public String[] getListBodyHideCol() {

		return null;
	}

	public String[] getListHeadHideCol() {

		return null;
	}

	public boolean isShowListTotal() {
		return false;
	}
	public int getBusinessActionType() {
		// TODO Auto-generated method stub
		return 0;
	}

}
