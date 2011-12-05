package nc.ui.wds.dm.wljsq;

import java.awt.Container;

import nc.ui.pub.beans.UIPanel;
import nc.ui.wl.pub.WdsQueryDlg;


public class MyQueryDIG extends WdsQueryDlg{

	
	public MyQueryDIG(Container parent, UIPanel normalPnl, String pk_corp,
			String moduleCode, String operator, String busiType) {
		super(parent, normalPnl, pk_corp, moduleCode, operator, busiType,"pk_stordoc",null);

//	 init();
	}
//	public void init(){
//		
//		setDefaultValue("bd_stordoc.dbilldate",null,ClientEnvironment.getInstance().getDate().toString());
//	}
	private static final long serialVersionUID = -8327804194852113256L;

}
