package nc.ui.wds.ic.allocation.in;

import java.awt.Container;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIPanel;
import nc.ui.wl.pub.WdsQueryDlg;

public class QueryDIG extends WdsQueryDlg{
	private static final long serialVersionUID = 8795739322245383734L;

	public QueryDIG(Container parent, UIPanel normalPnl, String pk_corp,
			String moduleCode, String operator, String busiType) {
		super(parent, normalPnl, pk_corp, moduleCode, operator, busiType,"tb_general_h.geh_cwarehouseid",
				"tb_general_h.pk_cargdoc");
		init();
	}
	public void init(){
		setDefaultValue("tb_general_h.geh_dbilldate",null,ClientEnvironment.getInstance().getDate().toString());
	}
	
	
}
