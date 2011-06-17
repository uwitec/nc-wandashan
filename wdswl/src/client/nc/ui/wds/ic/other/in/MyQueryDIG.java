package nc.ui.wds.ic.other.in;

import java.awt.Container;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIPanel;
import nc.ui.trade.query.HYQueryDLG;

public class MyQueryDIG extends HYQueryDLG{

	private static final long serialVersionUID = -7460088213752355190L;
	
	public MyQueryDIG(Container parent, UIPanel normalPnl, String pk_corp,
			String moduleCode, String operator, String busiType) {
		super(parent, normalPnl, pk_corp, moduleCode, operator, busiType);
		init();
	}

	public void init(){
		setDefaultValue("tb_general_h.geh_dbilldate",null,ClientEnvironment.getInstance().getDate().toString());
	}
	
	
	@Override
	public String getWhereSql() {
		String sql=super.getWhereSql();
		
		if(sql==null){
	      return null;
		}
		if(sql.contains("tb_general_h.pwb_fbillflag = 0")){
			
		  return sql.replace("tb_general_h.pwb_fbillflag = 0", "tb_general_h.pwb_fbillflag = 8");
		}
		return sql;
	}
	

}