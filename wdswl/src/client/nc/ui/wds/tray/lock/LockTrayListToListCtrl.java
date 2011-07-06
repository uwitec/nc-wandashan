package nc.ui.wds.tray.lock;

import nc.ui.trade.listtolist.IListToListController;

public class LockTrayListToListCtrl implements IListToListController {

	public String getLeftTitle() {
		// TODO Auto-generated method stub
		return "托盘信息";
	}

	public String getRightTitle() {
		// TODO Auto-generated method stub
		return "选定托盘";
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
