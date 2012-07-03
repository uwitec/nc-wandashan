package nc.ui.dm.speorder;

import java.awt.Container;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIPanel;
import nc.ui.trade.query.HYQueryDLG;

/**
 *  新版查询对话框
 * @author Administrator
 *
 */
public class ClientUIQueryDlg extends HYQueryDLG {

	public ClientUIQueryDlg(Container parent, UIPanel normalPnl,
			String pk_corp, String moduleCode, String operator, String busiType) {
		super(parent, normalPnl, pk_corp, moduleCode, operator, busiType);
		init();
	}
	public void init(){
		setDefaultValue("wds_sendorder.dmakedate",null,ClientEnvironment.getInstance().getDate().toString());
	}
	private static final long serialVersionUID = 645772211659590777L;
	@Override
	public String getWhereSql() {
		String sql=super.getWhereSql();
		
		if(sql==null){
	      return null;
		}
		if(sql.contains("wds_sendorder.vbillstatus = 0")){
			
		  return sql.replace("wds_sendorder.vbillstatus = 0", "wds_sendorder.vbillstatus = 8");
		}
		return sql;
	}
	
   
	
	
	
}
