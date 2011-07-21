package nc.ui.zb.price.view;


import java.awt.Container;

import nc.ui.querytemplate.normalpanel.INormalQueryPanel;
import nc.ui.trade.query.HYQueryConditionDLG;
import nc.vo.querytemplate.TemplateInfo;

public class PriceViewQueryDlg extends HYQueryConditionDLG{
	
	private static final long serialVersionUID = 645772211659590777L;

	public PriceViewQueryDlg(Container parent, INormalQueryPanel normalPnl, TemplateInfo ti) {
		super(parent, normalPnl, ti);
	}
}