package nc.ui.zb.bidding.adjust;

import nc.ui.trade.listtolist.IListToListController;
import nc.vo.zb.pub.ZbPubConst;

public class AdjustBidListToListCtrl implements IListToListController {

	public String getLeftTitle() {
		// TODO Auto-generated method stub
		return ZbPubConst.ADJ_BID_LEFT_TITLE;
	}

	public String getRightTitle() {
		// TODO Auto-generated method stub
		return ZbPubConst.ADJ_BID_RIGHT_TITLE;
	}

	public boolean isLeftHoldData() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isRightHoldData() {
		// TODO Auto-generated method stub
		return false;
	}

}
