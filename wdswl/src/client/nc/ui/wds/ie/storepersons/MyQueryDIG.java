package nc.ui.wds.ie.storepersons;

import java.awt.Container;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIPanel;
import nc.ui.wl.pub.WdsQueryDlg;

public class MyQueryDIG extends WdsQueryDlg{

	private static final long serialVersionUID = 1L;
	public MyQueryDIG(Container parent, UIPanel normalPnl, String pk_corp,
			String moduleCode, String operator, String busiType) {
		super(parent, normalPnl, pk_corp, moduleCode, operator, busiType,"tb_stockstaff.pk_stordoc","tb_stockstaff.pk_cargdoc");
		init();
	}
	public void init(){
		setDefaultValue("tb_stockstaff.dbilldate",null,ClientEnvironment.getInstance().getDate().toString());
	}
	

	

}
