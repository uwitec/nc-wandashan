package nc.ui.wds.load.teamdoc;

import java.awt.Container;

import nc.ui.pub.beans.UIPanel;
import nc.ui.trade.query.HYQueryDLG;

public class MyQueryDIG extends HYQueryDLG{

	public MyQueryDIG(Container parent, UIPanel normalPnl, String pk_corp,
			String moduleCode, String operator, String busiType) {
		super(parent, normalPnl, pk_corp, moduleCode, operator, busiType);
		
	}
	private static final long serialVersionUID = -5863155515975977568L;

}
