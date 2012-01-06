package nc.vo.wds.ic.allo.in.close;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public class AlloCloseBVO extends SuperVO {
	
//	-----------调拨出库单---表体信息
//	private String cgeneralhid;
	private String crowno;
	private String cgeneralbid;
	private String vfirstbillcode;
	private String vnotebody;
	private Integer fbillrowflag;//行状态
	private UFDouble ncorrespondnum;//累计出库数量
	private UFDouble ncorrespondastnum;//累计出库辅数量
	private UFBoolean flargess;//是否赠品
	private UFDouble noutassistnum; //实出辅数量 DECIMAL 20 8         
	private UFDouble noutnum; //实出数量 DECIMAL 20 8         
	private UFDouble nshouldoutassistnum;// 应出辅数量 DECIMAL 20 8         
	private UFDouble nshouldoutnum;// 应出数量 
	private UFDouble hsl;
	private UFDate dbizdate;// 业务日期 
//	private String cbodywarehouseid;
//	private String pk_bodycalbody;
	private String vbatchcode;
	private String cinventoryid;
	private String cinvbasid;
	private UFDouble nkdnum;//累计转物流数量
	private UFDouble ntranoutnum;//转出数量
	private UFDouble ntranoutastnum;//转出辅数量	

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "cgeneralbid";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return "cgeneralhid";
	}

	

	public String getCrowno() {
		return crowno;
	}

	public void setCrowno(String crowno) {
		this.crowno = crowno;
	}

	public String getCgeneralbid() {
		return cgeneralbid;
	}

	public void setCgeneralbid(String cgeneralbid) {
		this.cgeneralbid = cgeneralbid;
	}

	public String getVfirstbillcode() {
		return vfirstbillcode;
	}

	public void setVfirstbillcode(String vfirstbillcode) {
		this.vfirstbillcode = vfirstbillcode;
	}

	public String getVnotebody() {
		return vnotebody;
	}

	public void setVnotebody(String vnotebody) {
		this.vnotebody = vnotebody;
	}

	public Integer getFbillrowflag() {
		return fbillrowflag;
	}

	public void setFbillrowflag(Integer fbillrowflag) {
		this.fbillrowflag = fbillrowflag;
	}

	public UFDouble getNcorrespondnum() {
		return ncorrespondnum;
	}

	public void setNcorrespondnum(UFDouble ncorrespondnum) {
		this.ncorrespondnum = ncorrespondnum;
	}

	public UFDouble getNcorrespondastnum() {
		return ncorrespondastnum;
	}

	public void setNcorrespondastnum(UFDouble ncorrespondastnum) {
		this.ncorrespondastnum = ncorrespondastnum;
	}

	public UFBoolean getFlargess() {
		return flargess;
	}

	public void setFlargess(UFBoolean flargess) {
		this.flargess = flargess;
	}

	public UFDouble getNoutassistnum() {
		return noutassistnum;
	}

	public void setNoutassistnum(UFDouble noutassistnum) {
		this.noutassistnum = noutassistnum;
	}

	public UFDouble getNoutnum() {
		return noutnum;
	}

	public void setNoutnum(UFDouble noutnum) {
		this.noutnum = noutnum;
	}

	public UFDouble getNshouldoutassistnum() {
		return nshouldoutassistnum;
	}

	public void setNshouldoutassistnum(UFDouble nshouldoutassistnum) {
		this.nshouldoutassistnum = nshouldoutassistnum;
	}

	public UFDouble getNshouldoutnum() {
		return nshouldoutnum;
	}

	public void setNshouldoutnum(UFDouble nshouldoutnum) {
		this.nshouldoutnum = nshouldoutnum;
	}

	public UFDouble getHsl() {
		return hsl;
	}

	public void setHsl(UFDouble hsl) {
		this.hsl = hsl;
	}

	public UFDate getDbizdate() {
		return dbizdate;
	}

	public void setDbizdate(UFDate dbizdate) {
		this.dbizdate = dbizdate;
	}

//	public String getCbodywarehouseid() {
//		return cbodywarehouseid;
//	}
//
//	public void setCbodywarehouseid(String cbodywarehouseid) {
//		this.cbodywarehouseid = cbodywarehouseid;
//	}
//
//	public String getPk_bodycalbody() {
//		return pk_bodycalbody;
//	}
//
//	public void setPk_bodycalbody(String pk_bodycalbody) {
//		this.pk_bodycalbody = pk_bodycalbody;
//	}

	public String getVbatchcode() {
		return vbatchcode;
	}

	public void setVbatchcode(String vbatchcode) {
		this.vbatchcode = vbatchcode;
	}

	public String getCinventoryid() {
		return cinventoryid;
	}

	public void setCinventoryid(String cinventoryid) {
		this.cinventoryid = cinventoryid;
	}

	public String getCinvbasid() {
		return cinvbasid;
	}

	public void setCinvbasid(String cinvbasid) {
		this.cinvbasid = cinvbasid;
	}

	public UFDouble getNkdnum() {
		return nkdnum;
	}

	public void setNkdnum(UFDouble nkdnum) {
		this.nkdnum = nkdnum;
	}

	public UFDouble getNtranoutnum() {
		return ntranoutnum;
	}

	public void setNtranoutnum(UFDouble ntranoutnum) {
		this.ntranoutnum = ntranoutnum;
	}

	public UFDouble getNtranoutastnum() {
		return ntranoutastnum;
	}

	public void setNtranoutastnum(UFDouble ntranoutastnum) {
		this.ntranoutastnum = ntranoutastnum;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "ic_general_b";
	}

}
