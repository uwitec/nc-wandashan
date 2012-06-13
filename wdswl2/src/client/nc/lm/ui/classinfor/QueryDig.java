package nc.lm.ui.classinfor;

import java.awt.Container;

import nc.ui.pub.beans.UIPanel;
import nc.ui.trade.query.HYQueryDLG;

public class QueryDig extends HYQueryDLG{
	public QueryDig(Container parent, UIPanel normalPnl, String pk_corp,
			String moduleCode, String operator, String busiType) {
		super(parent, normalPnl, pk_corp, moduleCode, operator, busiType);
	}
	private static final long serialVersionUID = -5292104009255407396L;

}
