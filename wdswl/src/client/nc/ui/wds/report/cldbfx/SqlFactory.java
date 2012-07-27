package nc.ui.wds.report.cldbfx;

import nc.ui.trade.report.query.QueryDLG;

public class SqlFactory {

	public static final String nerpstornum = "";
	public static final String nwlstornum = "";
	public static final String nwlxnddnum = "";
	public static final String nwlxnydnum = "";
	public static final String nerpcgrknum = "";
	
	private SqlFactory sf = null;
	private QueryDLG queryDlg = null;
	public SqlFactory getInstance(){
		if(sf == null){
			sf = new SqlFactory();
		}
		return sf;
	}
	
	ISqlforcldbfx sql = null;
	
	public ISqlforcldbfx getSqlFunction(String selectfield){
		if(nerpstornum.equalsIgnoreCase(selectfield)){
			return new SqlNerpstornum(queryDlg);
		}
		return null;
	}
	
	public void setQueryDlg(QueryDLG dlg){
		this.queryDlg = dlg;
	}
}
