package nc.ui.dm.db.order;

import java.awt.Container;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIPanel;
import nc.ui.wl.pub.WdsQueryDlg;

/**
 *  新版查询对话框
 * @author mlr
 *
 */
public class ClientUIQueryDlg extends WdsQueryDlg {

	public ClientUIQueryDlg(Container parent, UIPanel normalPnl,
			String pk_corp, String moduleCode, String operator, String busiType) {
		super(parent, normalPnl, pk_corp, moduleCode, operator, busiType,"wds_sendorder.pk_outwhouse",null);
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
