package nc.ui.hg.pu.plan.project;

import java.awt.Container;

import nc.ui.pub.beans.UIPanel;
import nc.ui.trade.query.HYQueryDLG;


public class ClientQueryDLG extends HYQueryDLG{
	
	private static final long serialVersionUID = 9153238819070518045L;
	
	public ClientQueryDLG(Container parent, UIPanel normalPnl, String pk_corp,
			String moduleCode, String operator, String busiType, String nodeKey) {
		super(parent, normalPnl, pk_corp, moduleCode, operator, busiType, nodeKey);
	}

}
