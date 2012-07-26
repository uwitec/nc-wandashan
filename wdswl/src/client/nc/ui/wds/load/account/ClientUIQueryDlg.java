package nc.ui.wds.load.account;

import java.awt.Container;

import nc.ui.pub.ClientEnvironment;
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
			String busiType, String nodeKey) {
//		super(parent, normalPnl, pk_corp, moduleCode, operator, busiType,"wds_loadprice_h.pk_stordoc",null);
		super(parent, normalPnl, pk_corp, moduleCode, operator, busiType,"wds_loadprice.cwarehouseid",null);
		init();
	}
	public void init(){
		setDefaultValue("wds_loadprice_h.dmakedate",null,ClientEnvironment.getInstance().getDate().toString());
	}

	@Override
	public String getWhereSql() {
		String sql=super.getWhereSql();
		
		if(sql==null){
	      return null;
		}
		if(sql.contains("wds_loadprice_h.vbillstatus = 0")){			
		  return sql.replace("wds_loadprice_h.vbillstatus = 0", "wds_loadprice_h.vbillstatus = 8");
		}
		return sql;
	}


}
