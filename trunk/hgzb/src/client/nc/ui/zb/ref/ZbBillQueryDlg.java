package nc.ui.zb.ref;

import java.awt.Container;

import nc.ui.pub.pf.IinitQueryData2;
import nc.ui.querytemplate.normalpanel.INormalQueryPanel;
import nc.ui.trade.query.HYQueryConditionDLG;
import nc.vo.querytemplate.TemplateInfo;

/**
 * 参照查询基类,新版对话框
 * @author Administrator
 *
 */
public abstract class ZbBillQueryDlg extends HYQueryConditionDLG  implements IinitQueryData2 {
	
	public ZbBillQueryDlg(Container parent, TemplateInfo ti) {
		super(parent, ti);
	}
	
	public ZbBillQueryDlg(Container parent, INormalQueryPanel normalPnl, TemplateInfo ti) {
		super(parent, normalPnl, ti);
	}

	@Override
	public String getWhereSQL() {
		return super.getWhereSQL();
	}
}
