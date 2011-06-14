package nc.ui.dm.so.deal;

import java.awt.Container;

import javax.swing.plaf.PanelUI;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIPanel;
import nc.ui.trade.query.HYQueryDLG;

public class SoDealQryDlg extends HYQueryDLG {
	private static final long serialVersionUID = 1L;
	public SoDealQryDlg(Container parent, UIPanel normalPnl, String pk_corp,
			String moduleCode, String operator, String busiType) {
		super(parent, normalPnl, pk_corp, moduleCode, operator, busiType);
		init();
	}
	public void init(){
		setDefaultValue("h.dbilldate",null,ClientEnvironment.getInstance().getDate().toString());
	}

}
