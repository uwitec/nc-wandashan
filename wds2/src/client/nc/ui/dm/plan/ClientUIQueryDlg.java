package nc.ui.dm.plan;

import java.awt.Container;
import nc.ui.querytemplate.normalpanel.INormalQueryPanel;
import nc.ui.trade.query.HYQueryConditionDLG;
import nc.vo.querytemplate.TemplateInfo;

/**
 *  新版查询对话框
 * @author Administrator
 *
 */
public class ClientUIQueryDlg extends HYQueryConditionDLG {

	private static final long serialVersionUID = 645772211659590777L;

	public ClientUIQueryDlg(Container parent, INormalQueryPanel normalPnl, TemplateInfo ti) {
		super(parent, normalPnl, ti);
	}
}
