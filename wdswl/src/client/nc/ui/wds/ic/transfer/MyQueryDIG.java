package nc.ui.wds.ic.transfer;

import java.awt.Container;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIPanel;
import nc.ui.wl.pub.WdsQueryDlg;

public class MyQueryDIG extends WdsQueryDlg{

	private static final long serialVersionUID = -2818524076056880591L;
	public MyQueryDIG(Container parent, UIPanel normalPnl, String pk_corp,
			String moduleCode, String operator, String busiType) {
		super(parent, normalPnl, pk_corp, moduleCode, operator, busiType,"srl_pk","pk_cargdoc");
		init();

	}

	public void init(){
		setDefaultValue("dbilldate",null,ClientEnvironment.getInstance().getDate().toString());
	}
	@Override
	public String getWhereSql() {
		String sql=super.getWhereSql();
		
		if(sql==null){
	      return null;
		}
		if(sql.contains("vbillstatus = 0")){
			
		  return sql.replace("vbillstatus = 0", "vbillstatus = 8");
		}
		return sql;
	}


}
