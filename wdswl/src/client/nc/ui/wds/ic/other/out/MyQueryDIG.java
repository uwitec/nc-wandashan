package nc.ui.wds.ic.other.out;

import java.awt.Container;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIPanel;
import nc.ui.trade.query.HYQueryDLG;

public class MyQueryDIG extends HYQueryDLG{

	private static final long serialVersionUID = -2818524076056880591L;
	public MyQueryDIG(Container parent, UIPanel normalPnl, String pk_corp,
			String moduleCode, String operator, String busiType) {
		super(parent, normalPnl, pk_corp, moduleCode, operator, busiType);
		init();

	}

	public void init(){
		setDefaultValue("tb_outgeneral_h.dbilldate",null,ClientEnvironment.getInstance().getDate().toString());
	}
	@Override
	public String getWhereSql() {
		String sql=super.getWhereSql();
		
		if(sql==null){
	      return null;
		}
		if(sql.contains("tb_outgeneral_h.vbillstatus = 0")){
			
		  return sql.replace("tb_outgeneral_h.vbillstatus = 0", "tb_outgeneral_h.vbillstatus = 8");
		}
		return sql;
	}


}
