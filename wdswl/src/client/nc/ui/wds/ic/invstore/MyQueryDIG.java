package nc.ui.wds.ic.invstore;

import java.awt.Container;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIPanel;
import nc.ui.wl.pub.WdsQueryDlg;

public class MyQueryDIG extends WdsQueryDlg{
	private static final long serialVersionUID = -6031859442252111612L;
	public MyQueryDIG(Container parent, UIPanel normalPnl, String pk_corp,
			String moduleCode, String operator, String busiType) {
		super(parent, normalPnl, pk_corp, moduleCode, operator, busiType,"wds_cargdoc1.pk_stordoc",
				"wds_cargdoc1.pk_cargdoc");
		init();
	}
	public void init(){
		setDefaultValue("bd_cargdoc.dbilldate",null,ClientEnvironment.getInstance().getDate().toString());
	}
}