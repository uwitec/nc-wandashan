package nc.vo.wds.ic.allo.in.close;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public class AlloCloseVO extends SuperVO {
	
//	调拨出库单   表头信息
	private String cwarehouseid = null;//调出仓库
	private UFDate dbilldate = null;//单据日期
	private String vbillcode = null;//单据号
	private String cbiztype = null;//业务类型
	private String cgeneralhid = null;//表头id
	private String pk_corp = null;//公司
	private UFBoolean boutretflag = UFBoolean.FALSE;//是否退回
	private String coutcalbodyid;//调出组织
	private String coutcorpid;//调出公司
	private String cothercalbodyid;//对方组织
	private String cothercorpid;//对方公司
	private UFBoolean freplenishflag = UFBoolean.FALSE;//退货
	private String pk_calbody;//库存组织
	private String cwhsmanagerid;//库管员
	private String cdptid;//部门
	private String coperatorid;//制单人
	private UFDate daccountdate;//签字日期
	private String cregister;//签字人
	private String vnote;
	
	
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
	private String cbodywarehouseid;
	private String pk_bodycalbody;
	private String vbatchcode;
	private String cinventoryid;
	private String cinvbasid;
	private UFDouble nkdnum;//累计转物流数量
	private UFDouble ntranoutnum;//转出数量
	private UFDouble ntranoutastnum;//转出辅数量
	
	
	

	

	public String getCrowno() {
		return crowno;
	}

	public void setCrowno(String crowno) {
		this.crowno = crowno;
	}

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

	public String getCwarehouseid() {
		return cwarehouseid;
	}

	public void setCwarehouseid(String cwarehouseid) {
		this.cwarehouseid = cwarehouseid;
	}

	public UFDate getDbilldate() {
		return dbilldate;
	}

	public void setDbilldate(UFDate dbilldate) {
		this.dbilldate = dbilldate;
	}

	public String getVbillcode() {
		return vbillcode;
	}

	public void setVbillcode(String vbillcode) {
		this.vbillcode = vbillcode;
	}

	public String getCbiztype() {
		return cbiztype;
	}

	public void setCbiztype(String cbiztype) {
		this.cbiztype = cbiztype;
	}

	public String getCgeneralhid() {
		return cgeneralhid;
	}

	public void setCgeneralhid(String cgeneralhid) {
		this.cgeneralhid = cgeneralhid;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public UFBoolean getBoutretflag() {
		return boutretflag;
	}

	public void setBoutretflag(UFBoolean boutretflag) {
		this.boutretflag = boutretflag;
	}

	public String getCoutcalbodyid() {
		return coutcalbodyid;
	}

	public void setCoutcalbodyid(String coutcalbodyid) {
		this.coutcalbodyid = coutcalbodyid;
	}

	public String getCoutcorpid() {
		return coutcorpid;
	}

	public void setCoutcorpid(String coutcorpid) {
		this.coutcorpid = coutcorpid;
	}

	public String getCothercalbodyid() {
		return cothercalbodyid;
	}

	public void setCothercalbodyid(String cothercalbodyid) {
		this.cothercalbodyid = cothercalbodyid;
	}

	public String getCothercorpid() {
		return cothercorpid;
	}

	public void setCothercorpid(String cothercorpid) {
		this.cothercorpid = cothercorpid;
	}

	public UFBoolean getFreplenishflag() {
		return freplenishflag;
	}

	public void setFreplenishflag(UFBoolean freplenishflag) {
		this.freplenishflag = freplenishflag;
	}

	public String getPk_calbody() {
		return pk_calbody;
	}

	public void setPk_calbody(String pk_calbody) {
		this.pk_calbody = pk_calbody;
	}

	public String getCwhsmanagerid() {
		return cwhsmanagerid;
	}

	public void setCwhsmanagerid(String cwhsmanagerid) {
		this.cwhsmanagerid = cwhsmanagerid;
	}

	public String getCdptid() {
		return cdptid;
	}

	public void setCdptid(String cdptid) {
		this.cdptid = cdptid;
	}

	public String getCoperatorid() {
		return coperatorid;
	}

	public void setCoperatorid(String coperatorid) {
		this.coperatorid = coperatorid;
	}

	public UFDate getDaccountdate() {
		return daccountdate;
	}

	public void setDaccountdate(UFDate daccountdate) {
		this.daccountdate = daccountdate;
	}

	public String getCregister() {
		return cregister;
	}

	public void setCregister(String cregister) {
		this.cregister = cregister;
	}

	public String getVnote() {
		return vnote;
	}

	public void setVnote(String vnote) {
		this.vnote = vnote;
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

	public String getCbodywarehouseid() {
		return cbodywarehouseid;
	}

	public void setCbodywarehouseid(String cbodywarehouseid) {
		this.cbodywarehouseid = cbodywarehouseid;
	}

	public String getPk_bodycalbody() {
		return pk_bodycalbody;
	}

	public void setPk_bodycalbody(String pk_bodycalbody) {
		this.pk_bodycalbody = pk_bodycalbody;
	}

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
		return "ic_general_h h inner join ic_general_b b on h.cgeneralhid = b.cgeneralhid";
	}

}
