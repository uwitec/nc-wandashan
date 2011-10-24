package nc.ui.wds.tray.relock;

import nc.ui.trade.listtolist.IListToListController;
import nc.ui.trade.listtolist.UIListToListPanel;
import nc.ui.trade.pub.IVOTreeData;

/**
 * 
 * @author zhf  ÍÐÅÌËø¶¨ panel
 *
 */
public class ReLockTrayListToListPanel extends UIListToListPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ReLockTrayListToListPanel(){
		super();
	}
	

	@Override
	protected IVOTreeData createLeftData() {
		// TODO Auto-generated method stub
		return new ReLockTrayLeftTreeData();
	}

	@Override
	protected IListToListController createListController() {
		// TODO Auto-generated method stub
		return new ReLockTrayListToListCtrl();
	}

	@Override
	protected IVOTreeData createRightData() {
		// TODO Auto-generated method stub
		return new ReLockTrayRightTreeData();
	}
}
