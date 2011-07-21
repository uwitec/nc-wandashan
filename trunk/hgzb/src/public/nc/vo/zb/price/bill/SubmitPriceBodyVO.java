package nc.vo.zb.price.bill;

import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;

public class SubmitPriceBodyVO extends SuperVO {

	private String csubmitbillid;
	private String csubmitbill_bid;
	
	private String cinvclid;//品种分类
//	private String pk_corp;//操作公司
	private String cinvbasid;//品种基本id
	private String cinvmanid;//品种管理id
	private String cunitid;//主计量单位
	private String castunitid;//辅计量单位
	private UFDouble nnum = null;//招标主数量
	private UFDouble nasnum= null;//招标辅数量
	private String crowno;
//	private String cvendorid;//供应商管理id
	private String ccircalnoid;//轮次阶段id  默认次数：第一次、第二次、第三次
	
//	private Integer isubmittype;//报价类型 0web 1local 2手工录入 3恶意报价（只有网上招标有恶意报价）
	private UFDouble nprice = null;//报价
	
	private String csourcebillbid;
	private String  csourcebillhid;
	private String  cupsourcebilltype;
	private String csourcetype ;
	private String  cupsourcebillid;
	private String  cupsourcebillrowid ;
	
	private String vmemo;
	
	private String vdef1;
	private String vdef2;
	private String vdef3;
	private String vdef4;
	private String vdef5;
	
	private UFDouble ndef1;
	private UFDouble ndef2;
	private UFDouble ndef3;
	private UFDouble ndef4;
	private UFDouble ndef5;
	
	private UFBoolean bdef1;
	private UFBoolean bdef2;
	private UFBoolean bdef3;
//	private UFBoolean bdef4;
	
	private UFDateTime ts;
	private Integer dr;
	
	
	public String getCrowno() {
		return crowno;
	}

	public void setCrowno(String crowno) {
		this.crowno = crowno;
	}

	public String getCsubmitbillid() {
		return csubmitbillid;
	}

	public void setCsubmitbillid(String csubmitbillid) {
		this.csubmitbillid = csubmitbillid;
	}

	public String getCsubmitbill_bid() {
		return csubmitbill_bid;
	}

	public void setCsubmitbill_bid(String csubmitbill_bid) {
		this.csubmitbill_bid = csubmitbill_bid;
	}

	public String getCinvclid() {
		return cinvclid;
	}

	public void setCinvclid(String cinvclid) {
		this.cinvclid = cinvclid;
	}

	public String getCinvbasid() {
		return cinvbasid;
	}

	public void setCinvbasid(String cinvbasid) {
		this.cinvbasid = cinvbasid;
	}

	public String getCinvmanid() {
		return cinvmanid;
	}

	public void setCinvmanid(String cinvmanid) {
		this.cinvmanid = cinvmanid;
	}

	public String getCunitid() {
		return cunitid;
	}

	public void setCunitid(String cunitid) {
		this.cunitid = cunitid;
	}

	public String getCastunitid() {
		return castunitid;
	}

	public void setCastunitid(String castunitid) {
		this.castunitid = castunitid;
	}

	public UFDouble getNnum() {
		return nnum;
	}

	public void setNnum(UFDouble nnum) {
		this.nnum = nnum;
	}

	public UFDouble getNasnum() {
		return nasnum;
	}

	public void setNasnum(UFDouble nasnum) {
		this.nasnum = nasnum;
	}

	public String getCcircalnoid() {
		return ccircalnoid;
	}

	public void setCcircalnoid(String ccircalnoid) {
		this.ccircalnoid = ccircalnoid;
	}

	public UFDouble getNprice() {
		return nprice;
	}

	public void setNprice(UFDouble nprice) {
		this.nprice = nprice;
	}

	public String getVmemo() {
		return vmemo;
	}

	public void setVmemo(String vmemo) {
		this.vmemo = vmemo;
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

	public UFDouble getNdef4() {
		return ndef4;
	}

	public void setNdef4(UFDouble ndef4) {
		this.ndef4 = ndef4;
	}

	public UFDouble getNdef5() {
		return ndef5;
	}

	public void setNdef5(UFDouble ndef5) {
		this.ndef5 = ndef5;
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

	public UFBoolean getBdef3() {
		return bdef3;
	}

	public void setBdef3(UFBoolean bdef3) {
		this.bdef3 = bdef3;
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
		return "csubmitbill_bid";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return "csubmitbillid";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "zb_submitbill_b";
	}
	
	public void validate() throws ValidationException {
		if(PuPubVO.getString_TrimZeroLenAsNull(getCrowno())==null)
			throw new ValidationException("存在行号为空的数据");
		if(PuPubVO.getString_TrimZeroLenAsNull(getCinvbasid())==null||PuPubVO.getString_TrimZeroLenAsNull(getCinvmanid())==null){
			throw new ValidationException("存货信息为空，行号为"+getCrowno());
		}
		if(PuPubVO.getUFDouble_NullAsZero(getNnum()).equals(UFDouble.ZERO_DBL))
			throw new ValidationException("招标数量为空或0，行号为"+getCrowno());
		if(PuPubVO.getUFDouble_NullAsZero(getNnum()).equals(UFDouble.ZERO_DBL))
			throw new ValidationException("报价为空或0，行号为"+getCrowno());
		
	}

	public String getCsourcebillbid() {
		return csourcebillbid;
	}

	public void setCsourcebillbid(String csourcebillbid) {
		this.csourcebillbid = csourcebillbid;
	}

	public String getCsourcebillhid() {
		return csourcebillhid;
	}

	public void setCsourcebillhid(String csourcebillhid) {
		this.csourcebillhid = csourcebillhid;
	}

	public String getCupsourcebilltype() {
		return cupsourcebilltype;
	}

	public void setCupsourcebilltype(String cupsourcebilltype) {
		this.cupsourcebilltype = cupsourcebilltype;
	}

	public String getCsourcetype() {
		return csourcetype;
	}

	public void setCsourcetype(String csourcetype) {
		this.csourcetype = csourcetype;
	}

	public String getCupsourcebillid() {
		return cupsourcebillid;
	}

	public void setCupsourcebillid(String cupsourcebillid) {
		this.cupsourcebillid = cupsourcebillid;
	}

	public String getCupsourcebillrowid() {
		return cupsourcebillrowid;
	}

	public void setCupsourcebillrowid(String cupsourcebillrowid) {
		this.cupsourcebillrowid = cupsourcebillrowid;
	}

}
