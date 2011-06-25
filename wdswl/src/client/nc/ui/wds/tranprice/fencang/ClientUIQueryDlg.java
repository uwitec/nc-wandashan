package nc.ui.wds.tranprice.fencang;

import java.awt.Container;

import nc.ui.pub.beans.UIPanel;
import nc.ui.trade.query.HYQueryDLG;

/**
 *  
 * @author Administrator
 *
 */
public class ClientUIQueryDlg extends HYQueryDLG {

	
	
	
	private static final long serialVersionUID = 645772211659590777L;


	public ClientUIQueryDlg(Container parent, UIPanel normalPnl,
			String pk_corp, String moduleCode, String operator,
			String busiType, String nodeKey) {
		super(parent, normalPnl, pk_corp, moduleCode, operator, busiType, nodeKey);
		init();
	}
	public void init(){
//		setDefaultValue("wds_fyd_h.dbilldate",null,ClientEnvironment.getInstance().getDate().toString());
	}

	@Override
	public String getWhereSql() {
		String sql=super.getWhereSql();
		
		if(sql==null){
	      return null;
		}
		if(sql.contains("wds_transprice_h.vbillstatus = 0")){
			
		  return sql.replace("wds_transprice_h.vbillstatus = 0", "wds_transprice_h.vbillstatus = 8");
		}
		return sql;
	}
	


}
