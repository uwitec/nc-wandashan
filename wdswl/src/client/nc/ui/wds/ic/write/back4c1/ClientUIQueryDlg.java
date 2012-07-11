package nc.ui.wds.ic.write.back4c1;

import java.awt.Container;

import nc.ui.pub.beans.UIPanel;
import nc.ui.trade.query.HYQueryDLG;

/**
 *  
 * @author Administrator
 *
 */
public class ClientUIQueryDlg extends HYQueryDLG {
	
	private static final long serialVersionUID = 645772211659590777L;
	public ClientUIQueryDlg(Container parent, UIPanel normalPnl,
			String pk_corp, String moduleCode, String operator,
			String busiType, String nodeKey)
	{
		super(parent, normalPnl, pk_corp, moduleCode, operator, busiType);
		init();
	}
	public void init(){
	}
}
