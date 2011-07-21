package nc.ui.zb.bill.pre;

import nc.ui.trade.listtolist.IListToListController;
import nc.ui.trade.listtolist.UIListToListPanel;
import nc.ui.trade.pub.IVOTreeData;

/**
 * 
 * @author zhf  议标数据准备 panel
 *
 */
public class PreBidListToListPanel extends UIListToListPanel {

	@Override
	protected IVOTreeData createLeftData() {
		// TODO Auto-generated method stub
		return new PreBiddingLeftTreeData();
	}

	@Override
	protected IListToListController createListController() {
		// TODO Auto-generated method stub
		return new PreBidListToListCtrl();
	}

	@Override
	protected IVOTreeData createRightData() {
		// TODO Auto-generated method stub
		return new PreBiddingRightTreeData();
	}

}
