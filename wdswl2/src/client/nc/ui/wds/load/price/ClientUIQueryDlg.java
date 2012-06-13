package nc.ui.wds.load.price;

import java.awt.Container;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIPanel;
import nc.ui.wl.pub.WdsQueryDlg;

public class ClientUIQueryDlg extends WdsQueryDlg {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public ClientUIQueryDlg(Container parent, UIPanel normalPnl,
			String pk_corp, String moduleCode, String operator,
			String busiType, String nodeKey) {//wds_sendplanin.pk_outwhouse
		super(parent, normalPnl, pk_corp, moduleCode, operator, busiType,"wds_loadprice.pk_outwhouse",null);
		init();
	}
	public void init(){
		setDefaultValue("wds_loadprice.dmakedate",null,ClientEnvironment.getInstance().getDate().toString());	
	}

	
	@Override
	public String getWhereSQL() {
		String sql=super.getWhereSQL();
		
		if(sql==null){
	      return null;
		}
		if(sql.contains("wds_loadprice.vbillstatus = 0")){
			
		  return sql.replace("wds_loadprice.vbillstatus = 0", "wds_loadprice.vbillstatus = 8");
		}
		return sql;
	}
	

}
