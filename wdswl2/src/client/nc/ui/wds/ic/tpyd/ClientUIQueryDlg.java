package nc.ui.wds.ic.tpyd;

import java.awt.Container;

import nc.ui.pub.beans.UIPanel;

import nc.ui.wl.pub.WdsQueryDlg;

/**
 *  
 * @author Administrator
 *
 */
public class ClientUIQueryDlg extends WdsQueryDlg {

	
	
	
	private static final long serialVersionUID = 645772211659590777L;


	public ClientUIQueryDlg(Container parent, UIPanel normalPnl,
			String pk_corp, String moduleCode, String operator,
			String busiType, String nodeKey)
	{
		super(parent, normalPnl, pk_corp, moduleCode, operator, busiType,"wds_tpyd_h.pk_stordoc","wds_tpyd_h.pk_cargedoc");
		init();
	}
	public void init(){
//		setDefaultValue("wds_fyd_h.dbilldate",null,ClientEnvironment.getInstance().getDate().toString());
	}

	


}
