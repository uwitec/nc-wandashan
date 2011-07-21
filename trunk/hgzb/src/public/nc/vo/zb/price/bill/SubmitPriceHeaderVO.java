package nc.vo.zb.price.bill;

import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;

public class SubmitPriceHeaderVO extends SuperVO {

	private String csubmitbillid;//id
	private String cbiddingid;//标书id
	private String cvendorid;//供应商管理id
	private String ccustbasid;//供应商基本id
	private UFBoolean bistemp;//是否临时供应商
	private String pk_deptdoc;//招标部门
	private String vemployeeid;//业务员
	
	private String vbillno;//单据号
	private String pk_billtype;
	private String cname;//名称
	private UFDate dbilldate;//单据日期
	private String voperatorid;//制单人	
	private UFDate dmakedate;//制单日期
	private String vapproveid;//审批人
	private UFDate dapprovedate;//审批日期
	private Integer vbillstatus;//单据状态
	private String pk_corp;
	private String vapprovenote;
	
	private String vmemo;
	
	private String vdef1;
	private String vdef2;
	private String vdef3;
	private String vdef4;
	private String vdef5;
	
	private UFDouble ndef1;
	private UFDouble ndef2;
	private UFDouble ndef3;
	
	private UFBoolean bdef1;
	private UFBoolean bdef2;
	
	private UFDateTime ts;
	private Integer dr;
	
	/**
	 * 属性vapprovenote的Getter方法.
	 * 创建日期:2011-04-28 16:51:42
	 * @return String
	 */
	public String getVapprovenote () {
		return vapprovenote;
	}   
	/**
	 * 属性vapprovenote的Setter方法.
	 * 创建日期:2011-04-28 16:51:42
	 * @param newVapprovenote String
	 */
	public void setVapprovenote (String newVapprovenote ) {
	 	this.vapprovenote = newVapprovenote;
	} 
	public UFBoolean getBistemp() {
		return bistemp;
	}
	public void setBistemp(UFBoolean bistemp) {
		this.bistemp = bistemp;
	}
	/**
	 * 属性pk_billtype的Getter方法.
	 * 创建日期:2011-04-28 16:51:42
	 * @return String
	 */
	public String getPk_billtype () {
		return pk_billtype;
	}   
	/**
	 * 属性pk_billtype的Setter方法.
	 * 创建日期:2011-04-28 16:51:42
	 * @param newPk_billtype String
	 */
	public void setPk_billtype (String newPk_billtype ) {
	 	this.pk_billtype = newPk_billtype;
	} 
	
	public String getVmemo() {
		return vmemo;
	}

	public void setVmemo(String vmemo) {
		this.vmemo = vmemo;
	}
	
	public String getCsubmitbillid() {
		return csubmitbillid;
	}

	public void setCsubmitbillid(String csubmitbillid) {
		this.csubmitbillid = csubmitbillid;
	}

	public String getCbiddingid() {
		return cbiddingid;
	}

	public void setCbiddingid(String cbiddingid) {
		this.cbiddingid = cbiddingid;
	}

	public String getCvendorid() {
		return cvendorid;
	}

	public void setCvendorid(String cvendorid) {
		this.cvendorid = cvendorid;
	}

	public String getPk_deptdoc() {
		return pk_deptdoc;
	}

	public void setPk_deptdoc(String pk_deptdoc) {
		this.pk_deptdoc = pk_deptdoc;
	}

	public String getVemployeeid() {
		return vemployeeid;
	}

	public void setVemployeeid(String vemployeeid) {
		this.vemployeeid = vemployeeid;
	}

	public String getVbillno() {
		return vbillno;
	}

	public void setVbillno(String vbillno) {
		this.vbillno = vbillno;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public UFDate getDbilldate() {
		return dbilldate;
	}

	public void setDbilldate(UFDate dbilldate) {
		this.dbilldate = dbilldate;
	}

	public String getVoperatorid() {
		return voperatorid;
	}

	public void setVoperatorid(String voperatorid) {
		this.voperatorid = voperatorid;
	}

	public UFDate getDmakedate() {
		return dmakedate;
	}

	public void setDmakedate(UFDate dmakedate) {
		this.dmakedate = dmakedate;
	}

	public String getVapproveid() {
		return vapproveid;
	}

	public void setVapproveid(String vapproveid) {
		this.vapproveid = vapproveid;
	}

	public UFDate getDapprovedate() {
		return dapprovedate;
	}

	public void setDapprovedate(UFDate dapprovedate) {
		this.dapprovedate = dapprovedate;
	}

	public Integer getVbillstatus() {
		return vbillstatus;
	}

	public void setVbillstatus(Integer vbillstatus) {
		this.vbillstatus = vbillstatus;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public String getVdef1() {
		return vdef1;
	}

	public void setVdef1(String vdef1) {
		this.vdef1 = vdef1;
	}

	public String getVdef2() {
		return vdef2;
	}

	public void setVdef2(String vdef2) {
		this.vdef2 = vdef2;
	}

	public String getVdef3() {
		return vdef3;
	}

	public void setVdef3(String vdef3) {
		this.vdef3 = vdef3;
	}

	public String getVdef4() {
		return vdef4;
	}

	public void setVdef4(String vdef4) {
		this.vdef4 = vdef4;
	}

	public String getVdef5() {
		return vdef5;
	}

	public void setVdef5(String vdef5) {
		this.vdef5 = vdef5;
	}

	public UFDouble getNdef1() {
		return ndef1;
	}

	public void setNdef1(UFDouble ndef1) {
		this.ndef1 = ndef1;
	}

	public UFDouble getNdef2() {
		return ndef2;
	}

	public void setNdef2(UFDouble ndef2) {
		this.ndef2 = ndef2;
	}

	public UFDouble getNdef3() {
		return ndef3;
	}

	public void setNdef3(UFDouble ndef3) {
		this.ndef3 = ndef3;
	}

	public UFBoolean getBdef1() {
		return bdef1;
	}

	public void setBdef1(UFBoolean bdef1) {
		this.bdef1 = bdef1;
	}

	public UFBoolean getBdef2() {
		return bdef2;
	}

	public void setBdef2(UFBoolean bdef2) {
		this.bdef2 = bdef2;
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

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "csubmitbillid";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "zb_submitbill";
	}
	
	public void validate() throws ValidationException {
		if(PuPubVO.getString_TrimZeroLenAsNull(getCbiddingid())==null){
			throw new ValidationException("标书信息为空");
		}
		if(PuPubVO.getString_TrimZeroLenAsNull(getCvendorid())==null)
			throw new ValidationException("供应商信息为空");
//		if(PuPubVO.getString_TrimZeroLenAsNull(getVbillno())==null)
//			throw new ValidationException("单据号不能为空");
	}
	public String getCcustbasid() {
		return ccustbasid;
	}
	public void setCcustbasid(String ccustbasid) {
		this.ccustbasid = ccustbasid;
	}

}
