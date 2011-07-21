package nc.ui.zb.bidding.make;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.button.IBillButton;
import nc.vo.zb.bidding.BiddingBillVO;
import nc.vo.zb.bidding.BiddingBodyVO;
import nc.vo.zb.bidding.BiddingHeaderVO;
import nc.vo.zb.bidding.BiddingSuppliersVO;
import nc.vo.zb.bidding.BiddingTimesVO;
import nc.vo.zb.pub.ZbPuBtnConst;
import nc.vo.zb.pub.ZbPubConst;

public class ClientController extends AbstractManageController {
	
	public int[] getCardButtonAry() {
		int[] buttonArray = new int[] { 
				IBillButton.Add,
				IBillButton.Refbill,
				ZbPuBtnConst.Editor, 
				IBillButton.Save,
				IBillButton.Cancel, 
				IBillButton.Line,
				IBillButton.Query, 
				IBillButton.Del, 
				IBillButton.Return, 
				IBillButton.Brow,
				IBillButton.Action, 
				IBillButton.Audit,
				IBillButton.Refresh,
				ZbPuBtnConst.ASSQUERY,
				IBillButton.Print,
				ZbPuBtnConst.FZGN
				};
		return buttonArray;
	}
	public int[] getListButtonAry() {
		int[] buttonArray = new int[] { 
				IBillButton.Add, 
				IBillButton.Refbill,
				ZbPuBtnConst.Editor, 
				IBillButton.Query,
				IBillButton.Del, 
				IBillButton.Line, 
				IBillButton.Card, 
				IBillButton.Brow, 
				IBillButton.Action, 
				IBillButton.Audit,
				IBillButton.Refresh,
				ZbPuBtnConst.ASSQUERY
				};
		return buttonArray;
	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public String getBillType() {
		return ZbPubConst.ZB_BIDDING_BILLTYPE;
	}

	public String[] getBillVoName() {
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

	public int getBusinessActionType() {
		return nc.ui.trade.businessaction.IBusinessActionType.PLATFORM;
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

}
