package nc.ui.hg.pu.plan.mny;


import java.awt.Container;

import nc.ui.hg.pu.pub.PlanPubQueryDlg;
import nc.ui.pub.beans.UIPanel;

public class ClientUIQueryDlg extends PlanPubQueryDlg{
	
	private static final long serialVersionUID = 9153238819070518045L;
	
	public ClientUIQueryDlg(Container parent, UIPanel normalPnl, String pk_corp,
			String moduleCode, String operator, String busiType, String nodeKey) {
		super(parent, normalPnl, pk_corp, moduleCode, operator, busiType, nodeKey);
	
	}
}