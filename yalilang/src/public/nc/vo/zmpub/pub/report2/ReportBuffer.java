package nc.vo.zmpub.pub.report2;
import java.io.Serializable;

import nc.vo.pub.lang.UFBoolean;
/**
 * 报表基本设置 缓存 点击保存按钮 自动更新数据库
 * @author mlr
 * 
 */
public class ReportBuffer implements Serializable{
	private static final long serialVersionUID = 144801743456602307L;
	private String pk_config=null;//主键
	private String detatil=null;//描述信息
	private String nodecode = null;// 功能节点号
	// 交叉数据
	private String[] strRows = null;// 交叉行
	private String[] strCols = null;// 交叉列
	private String[] strVals = null;// 交叉值
	private UFBoolean istotal=null;//
	public UFBoolean getIstotal() {
		return istotal;
	}

	public String getDetatil() {
		return detatil;
	}

	public void setDetatil(String detatil) {
		this.detatil = detatil;
	}

	public void setIstotal(UFBoolean istotal) {
		this.istotal = istotal;
	}

	private Integer  lel=null;//汇总级次
	// 合计数据
	private UFBoolean issub = null;// 是否小计
	private UFBoolean issum = null;// 是否合计
	private String[] totfields = null;// 合计纬度
	private String[] totfieldsNames = null;// 合计纬度名字
    
	
	
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

	public String[] getStrRows() {
		return strRows;
	}

	public void setStrRows(String[] strRows) {
		this.strRows = strRows;
	}

	public String[] getStrCols() {
		return strCols;
	}

	public void setStrCols(String[] strCols) {
		this.strCols = strCols;
	}

	public String[] getStrVals() {
		return strVals;
	}

	public void setStrVals(String[] strVals) {
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

	public String[] getTotfields() {
		return totfields;
	}

	public void setTotfields(String[] totfields) {
		this.totfields = totfields;
	}

	public String[] getTotfieldsNames() {
		return totfieldsNames;
	}

	public void setTotfieldsNames(String[] totfieldsNames) {
		this.totfieldsNames = totfieldsNames;
	}

}
