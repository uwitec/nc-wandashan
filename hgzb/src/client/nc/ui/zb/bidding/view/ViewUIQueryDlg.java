package nc.ui.zb.bidding.view;

import java.awt.Container;

import nc.ui.querytemplate.normalpanel.INormalQueryPanel;
import nc.ui.trade.query.HYQueryConditionDLG;
import nc.vo.querytemplate.TemplateInfo;
import nc.vo.zb.pub.ZbPubTool;

/**
 *  新版查询对话框
 * @author Administrator
 *
 */
public class ViewUIQueryDlg extends HYQueryConditionDLG {

	private static final long serialVersionUID = 645772211659590777L;

	public ViewUIQueryDlg(Container parent, INormalQueryPanel normalPnl, TemplateInfo ti) {
		super(parent, normalPnl, ti);
	}
	
	@Override
	public String getWhereSql() {
		String sql=super.getWhereSql();
		
		if(sql==null){
	      return null;
		}
		sql =ZbPubTool.getSql(sql);
		sql =ZbPubTool.getSql1(sql);
		sql =ZbPubTool.getSql2(sql);
		return sql;
	}

}
