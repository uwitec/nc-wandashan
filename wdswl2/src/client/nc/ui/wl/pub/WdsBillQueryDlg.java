package nc.ui.wl.pub;

import java.awt.Container;

import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.pf.IinitQueryData2;
import nc.ui.trade.query.HYQueryDLG;

/**
 * 参照查询基类
 * @author zpm
 *
 */
public abstract class WdsBillQueryDlg extends HYQueryDLG implements IinitQueryData2 {
	
	static Container parent = null;
	static UIPanel normalPnl = null;
	static String pk_corp =  null;
	static String moduleCode =  null;
	static String operator =  null;
	static String busiType =  null;
	static String nodeKey =  null;
	//复写这个方法
	public WdsBillQueryDlg(Container parent){
		super(parent, normalPnl, pk_corp, moduleCode, operator, busiType,nodeKey);
	}

	public WdsBillQueryDlg(Container parent, UIPanel normalPnl, String pk_corp,
			String moduleCode, String operator, String busiType, String nodeKey) {
		super(parent, normalPnl, pk_corp, moduleCode, operator, busiType,nodeKey);
	}

	@Override
	public String getWhereSQL() {
		return super.getWhereSQL();
	}
}
