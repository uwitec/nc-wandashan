package nc.ui.dm;

import java.awt.Container;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIPanel;
import nc.ui.trade.query.HYQueryDLG;

public class PlanDealQryDlg extends HYQueryDLG {
	private static final long serialVersionUID = 1L;
	public PlanDealQryDlg(Container parent, UIPanel normalPnl, String pk_corp,
			String moduleCode, String operator, String busiType) {
		super(parent, normalPnl, pk_corp, moduleCode, operator, busiType);
	 init();
	}

	public void init(){
		setDefaultValue("wds_sendplanin.dmakedate",null,ClientEnvironment.getInstance().getDate().toString());
	}


}
