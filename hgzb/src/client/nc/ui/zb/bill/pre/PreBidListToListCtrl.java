package nc.ui.zb.bill.pre;

import nc.ui.trade.listtolist.IListToListController;
import nc.vo.zb.pub.ZbPubConst;

public class PreBidListToListCtrl implements IListToListController {

	public String getLeftTitle() {
		// TODO Auto-generated method stub
		return ZbPubConst.PRE_BID_LEFT_TITLE;
	}

	public String getRightTitle() {
		// TODO Auto-generated method stub
		return ZbPubConst.PRE_BID_RIGHT_TITLE;
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
