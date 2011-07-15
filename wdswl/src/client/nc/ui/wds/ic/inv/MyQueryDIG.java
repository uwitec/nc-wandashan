package nc.ui.wds.ic.inv;

import java.awt.Container;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIPanel;
import nc.ui.wl.pub.WdsQueryDlg;

public class MyQueryDIG extends WdsQueryDlg {
	private static final long serialVersionUID = 3308256416361572220L;
	public MyQueryDIG(Container parent, UIPanel normalPnl, String pk_corp,
			String moduleCode, String operator, String busiType) {
		super(parent, normalPnl, pk_corp, moduleCode, operator, busiType,"tb_warehousestock.pk_customize1","tb_warehousestock.pk_cargdoc");
	}
	
	 public void initData() {
		setDefaultValue("tb_warehousestock.dbilldate",null,ClientEnvironment.getInstance().getDate().toString());
		super.initData();
	}
}
