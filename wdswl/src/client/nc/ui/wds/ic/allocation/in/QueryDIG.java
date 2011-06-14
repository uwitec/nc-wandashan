package nc.ui.wds.ic.allocation.in;

import java.awt.Container;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIPanel;
import nc.ui.trade.query.HYQueryDLG;

public class QueryDIG extends HYQueryDLG{
	private static final long serialVersionUID = 8795739322245383734L;

	public QueryDIG(Container parent, UIPanel normalPnl, String pk_corp,
			String moduleCode, String operator, String busiType) {
		super(parent, normalPnl, pk_corp, moduleCode, operator, busiType);
		init();
	}
	public void init(){
		setDefaultValue("tb_general_h.geh_dbilldate",null,ClientEnvironment.getInstance().getDate().toString());
	}
	
	
}
