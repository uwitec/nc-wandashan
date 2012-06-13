package nc.ui.dm.so.order;
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
			String busiType, String nodeKey) {
		super(parent, normalPnl, pk_corp, moduleCode, operator, busiType,"wds_soorder.pk_outwhouse",null);
	init();
	}
	
	public void init(){
		setDefaultValue("wds_soorder.dbilldate",null,ClientEnvironment.getInstance().getDate().toString());
	}
	@Override
	public String getWhereSql() {
		String sql=super.getWhereSql();
		
		if(sql==null){
	      return null;
		}
		if(sql.contains("wds_soorder.vbillstatus = 0")){
			
		  return sql.replace("wds_soorder.vbillstatus = 0", "wds_soorder.vbillstatus = 8");
		}
		return sql;
	}
	
	
	
	
}
