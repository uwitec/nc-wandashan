package nc.ui.zb.avnum;


import java.awt.Container;

import nc.ui.querytemplate.normalpanel.INormalQueryPanel;
import nc.ui.trade.query.HYQueryConditionDLG;
import nc.vo.querytemplate.TemplateInfo;
import nc.vo.zb.pub.ZbPubTool;

public class ClientUIQueryDlg extends HYQueryConditionDLG {

	private static final long serialVersionUID = 645772211659590777L;

	public ClientUIQueryDlg(Container parent, INormalQueryPanel normalPnl, TemplateInfo ti) {
		super(parent, normalPnl, ti);
	}
	
	@Override
	public String getWhereSql() {
		String sql=super.getWhereSql();
		
		if(sql==null){
	      return null;
		}
		sql =ZbPubTool.getSql(sql);
		return sql;
	}
}
