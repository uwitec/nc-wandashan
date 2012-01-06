package nc.vo.wds.ic.allo.in.close;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;

public class AlloCloseHVO extends SuperVO {
	
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


	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "cgeneralhid";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
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


	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "ic_general_h";
	}

}
