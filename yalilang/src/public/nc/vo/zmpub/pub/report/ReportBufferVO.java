package nc.vo.zmpub.pub.report;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
/**
 * 报表配置vo
 * @author mlr
 * 
 * 
 * 
 *
 */
public class ReportBufferVO extends SuperVO{
	
	
//	create table zm_config(
//		    pk_config char(20),
//		    nodecode  char(50),
//		    strRows   varchar(1000),
//		    strCols   varchar(1000),
//		    strVals    varchar(1000),
//	        lel        varchar(1000),
//          istotal    char(1),
//	        issub      char(1),
//	        issum      char(1),
//	       totfields   varchar(1000),
//	       totfieldsNames varchar(1000)		 
//	 )
    private String pk_config;//主键
	
	
	private String nodecode = null;// 功能节点号
	
	// 交叉数据
	private String strRows = null;// 交叉行   用&作为分隔符
	private String strCols = null;// 交叉列 用&作为分隔符
	private String strVals = null;// 交叉值 用&作为分隔符
	private UFBoolean istotal=null;//
	private Integer  lel=null;//汇总级次
	
	// 合计数据
	private UFBoolean issub = null;// 是否小计 
	private UFBoolean issum = null;// 是否合计
	private String totfields = null;// 合计纬度  用&作为分隔符
	private String totfieldsNames = null;// 合计纬度名字  用&作为分隔符
	
	private UFDateTime ts;
	private Integer dr;

	
	
	
	public UFBoolean getIstotal() {
		return istotal;
	}

	public void setIstotal(UFBoolean istotal) {
		this.istotal = istotal;
	}

	public UFDateTime getTs() {
		return ts;
	}

	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public Integer getLel() {
		return lel;
	}

	public void setLel(Integer lel) {
		this.lel = lel;
	}

	public String getPk_config() {
		return pk_config;
	}

	public void setPk_config(String pk_config) {
		this.pk_config = pk_config;
	}

	public String getNodecode() {
		return nodecode;
	}

	public void setNodecode(String nodecode) {
		this.nodecode = nodecode;
	}

	public String getStrRows() {
		return strRows;
	}

	public void setStrRows(String strRows) {
		this.strRows = strRows;
	}

	public String getStrCols() {
		return strCols;
	}

	public void setStrCols(String strCols) {
		this.strCols = strCols;
	}

	public String getStrVals() {
		return strVals;
	}

	public void setStrVals(String strVals) {
		this.strVals = strVals;
	}

	public UFBoolean getIssub() {
		return issub;
	}

	public void setIssub(UFBoolean issub) {
		this.issub = issub;
	}

	public UFBoolean getIssum() {
		return issum;
	}

	public void setIssum(UFBoolean issum) {
		this.issum = issum;
	}

	public String getTotfields() {
		return totfields;
	}

	public void setTotfields(String totfields) {
		this.totfields = totfields;
	}

	public String getTotfieldsNames() {
		return totfieldsNames;
	}

	public void setTotfieldsNames(String totfieldsNames) {
		this.totfieldsNames = totfieldsNames;
	}

	@Override
	public String getPKFieldName() {
		
		return "pk_config";
	}

	@Override
	public String getParentPKFieldName() {
		
		return null;
	}

	@Override
	public String getTableName() {
		
		return "zm_config";
	}
	
	@Override
	public String getPrimaryKey() {
		
		return pk_config;
	}
	
}
