package nc.ui.wds.load.price;

import nc.ui.pub.FramePanel;
import nc.ui.trade.multiappinterface.MultiAppManager;

/**
 * װж�Ѽ۸�
 * @author lyf
 * @create 2010-9-13 ����11:25:17
 */
public class ClientUI extends MultiAppManager {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ClientUI(FramePanel panel) {
		super(panel);
	}
	@Override
	public String getFirstClassName() {
		// TODO Auto-generated method stub
		return "nc.ui.wds.load.price.ClientCartUI";
	}}