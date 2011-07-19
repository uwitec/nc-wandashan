package nc.ui.dm.plan;

import java.awt.Container;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIPanel;
import nc.ui.trade.query.HYQueryDLG;
import nc.ui.wl.pub.WdsQueryDlg;

/**²éÑ¯¶Ô»°¿ò
 * @author Administrator
 *
 */
public class ClientUIQueryDlg extends WdsQueryDlg {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public ClientUIQueryDlg(Container parent, UIPanel normalPnl,
			String pk_corp, String moduleCode, String operator,
			String busiType, String nodeKey) {//wds_sendplanin.pk_outwhouse
		super(parent, normalPnl, pk_corp, moduleCode, operator, busiType,"wds_sendplanin.pk_outwhouse",null);
		init();
	}
	public void init(){
		setDefaultValue("wds_sendplanin.dmakedate",null,ClientEnvironment.getInstance().getDate().toString());	
	}

	
	@Override
	public String getWhereSQL() {
		String sql=super.getWhereSQL();
		
		if(sql==null){
	      return null;
		}
		if(sql.contains("wds_sendplanin.vbillstatus = 0")){
			
		  return sql.replace("wds_sendplanin.vbillstatus = 0", "wds_sendplanin.vbillstatus = 8");
		}
		return sql;
	}
	

}
