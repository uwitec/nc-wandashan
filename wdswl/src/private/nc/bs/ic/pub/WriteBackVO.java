package nc.bs.ic.pub;
import java.io.Serializable;

import nc.vo.pub.lang.UFDouble;
/**
 * 用于封装回写数据的VO
 * @author mlr
 * @date 2011-9-13
 */
public class WriteBackVO implements Serializable{
	
	private static final long serialVersionUID = -8671328730444062598L;
	private String vsourcebillrowid;//来源单据行id
	private String vsourcebilltype;//来源单据类型
	private String vsourcebillid;//来源单据id
	private UFDouble[] nums;//要回写的数组
	private String[] numsnames;//要回写的来源表的数组的名字
	private String[] numsnames1;//表的字段数组
	private String idname;//要回写的表的主键名字
	private String idvalue;//要会写的表的主键值
	private String sourcetablename;//要会写的表的名字
	private String tablename;//表名
	private String id;//表主键
	private String idname1;//表主键名字
	//回写的sql语句
	private StringBuffer writeBackSql=new StringBuffer();
	//查询 旧的数据 的sql
	private StringBuffer queryOldSql=new StringBuffer();
	
	public WriteBackVO(String vsourcebillrowid, String vsourcebilltype,
			String vsourcebillid, UFDouble[] nums, String idname,
			String idvalue, String sourcetablename, String tablename) {
		super();
		this.vsourcebillrowid = vsourcebillrowid;
		this.vsourcebilltype = vsourcebilltype;
		this.vsourcebillid = vsourcebillid;
		this.nums = nums;
		this.idname = idname;
		this.idvalue = idvalue;
		this.sourcetablename = sourcetablename;
		this.tablename = tablename;
	}
	public WriteBackVO(){
		super();
	}
	public String getWriteBackSql(){
		writeBackSql.setLength(0);
		writeBackSql.append(" update " + sourcetablename);
		writeBackSql.append(" set ");
		for (int i = 0; i < numsnames.length; i++) {
			writeBackSql.append(numsnames[i] + "=coalesce(" + numsnames[i]+ ",0.0)+ " + nums[i] + " ");
			if (i < numsnames.length - 1) {
				writeBackSql.append(" ,");
			}
		}
		writeBackSql.append(" where  ");
		writeBackSql.append(" isnull(dr,0)=0 ");
		writeBackSql.append(" and " + idname + "='"+idvalue+"'");
		return writeBackSql.toString();
 	}
	public String getQueryOldSql(){
		queryOldSql.setLength(0);
		queryOldSql.append(" select ");
		for (int i = 0; i < numsnames1.length; i++) {
			
			queryOldSql.append( numsnames1[i]);
			if (i < numsnames1.length - 1) {
				writeBackSql.append(" ,");
			}
		}
		queryOldSql.append(" from "+tablename+" ");
		queryOldSql.append( " where ");
		queryOldSql.append( idname1+"='"+id+"'");
		return queryOldSql.toString();
	}
	
	public String[] getNumsnames1() {
		return numsnames1;
	}
	public void setNumsnames1(String[] numsnames1) {
		this.numsnames1 = numsnames1;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIdname1() {
		return idname1;
	}
	public void setIdname1(String idname1) {
		this.idname1 = idname1;
	}
	public String[] getNumsnames() {
		return numsnames;
	}
	public void setNumsnames(String[] numsnames) {
		this.numsnames = numsnames;
	}
	public String getIdname() {
		return idname;
	}
	public void setIdname(String idname) {
		this.idname = idname;
	}
	public String getIdvalue() {
		return idvalue;
	}
	public void setIdvalue(String idvalue) {
		this.idvalue = idvalue;
	}
	public String getVsourcebillrowid() {
		return vsourcebillrowid;
	}
	public void setVsourcebillrowid(String vsourcebillrowid) {
		this.vsourcebillrowid = vsourcebillrowid;
	}
	public String getVsourcebilltype() {
		return vsourcebilltype;
	}
	public void setVsourcebilltype(String vsourcebilltype) {
		this.vsourcebilltype = vsourcebilltype;
	}
	public String getVsourcebillid() {
		return vsourcebillid;
	}
	public void setVsourcebillid(String vsourcebillid) {
		this.vsourcebillid = vsourcebillid;
	}
	public UFDouble[] getNums() {
		return nums;
	}
	public void setNums(UFDouble[] nums) {
		this.nums = nums;
	}
	public String getSourcetablename() {
		return sourcetablename;
	}
	public void setSourcetablename(String sourcetablename) {
		this.sourcetablename = sourcetablename;
	}
	public String getTablename() {
		return tablename;
	}
	public void setTablename(String tablename) {
		this.tablename = tablename;
	}
}
