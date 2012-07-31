package nc.ui.wds.report.cldbfx;

import nc.ui.trade.report.query.QueryDLG;

public class SqlFactory {

	public static final String N_nerpstornum = "nerpstornum";
	public static final String N_nwlstornum = "nwlstornum";
	public static final String N_nwlxnddnum = "nwlxnddnum";
	public static final String N_nwlxnydnum = "nwlxnydnum";
	public static final String N_nerpcgrknum = "nerpcgrknum";
	
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
		if(N_nerpstornum.equalsIgnoreCase(selectfield)){
			return new SqlNerpstornum(queryDlg);
		}
		if(N_nwlstornum.equalsIgnoreCase(selectfield)){
			return new SqlNwlstornum(queryDlg);
		}
		if(N_nwlxnddnum.equalsIgnoreCase(selectfield)){
			return new SqlNwlxnddnum(queryDlg);
		}
		if(N_nwlxnydnum.equalsIgnoreCase(selectfield)){
			return new SqlNwlxnydnum(queryDlg);
		}
		if(N_nerpcgrknum.equalsIgnoreCase(selectfield)){
			return new SqlNerpcgrknum(queryDlg);
		}
		return null;
	}
	
	public void setQueryDlg(QueryDLG dlg){
		this.queryDlg = dlg;
	}
}
