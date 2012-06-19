package nc.vo.zmpub.pub.bill;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
/**
 * 标准单据主表 vo
 * @author zhf
 *
 */
public abstract class HYHeadSuperVO extends SuperVO {
/**
 * 行业supervo   标准单据信息  10个自定义项   5个pk_defdoc  5个字符串预留项   3个数字预留项  2个布尔预留项
 */
	public String pk_corp; //公司
	public Integer vbillstatus;
	public String vbillno;
	public String pk_billtype;
	public UFDate dbilldate;
	public String pk_busitype;//业务类型
	
	public String vmemo;//备注
	
	public String vemployeeid;//业务员
	
	public UFBoolean fisclose;//  是否作废	
	public UFBoolean fisself;//  是否自制
	
	public String voperatorid;
	public UFDate dmakedate;
	public String vapproveid;
	public UFDate dapprovedate;
	public String vapprovenote;
	
	public UFDateTime ts;		
	
	public Integer dr;
//	----------------------------------
	public String vdef1;
	public String vdef2;
	public String vdef3;
	public String vdef4;
	public String vdef5;
	public String vdef6;
	public String vdef7;
	public String vdef8;
	public String vdef9;
	public String vdef10;
	
	public String pk_defdoc1;
	public String pk_defdoc2;
	public String pk_defdoc3;
	public String pk_defdoc4;
	public String pk_defdoc5;
	
	public String vreserve1;
	public String vreserve2;
	public String vreserve3;
	public String vreserve4;
	public String vreserve5;
	
	public UFDouble nreserve1;
	public UFDouble nreserve2;
	public UFDouble nreserve3;
	
	public UFBoolean ureserve1;
	public UFBoolean ureserve2;
	public UFBoolean ureserve3;
	
	
	public String getVmemo() {
		return vmemo;
	}
	public void setVmemo(String vmemo) {
		this.vmemo = vmemo;
	}
	public String getVemployeeid() {
		return vemployeeid;
	}
	public void setVemployeeid(String vemployeeid) {
		this.vemployeeid = vemployeeid;
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
	public String getPk_corp() {
		return pk_corp;
	}
	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}
	public Integer getVbillstatus() {
		return vbillstatus;
	}
	public void setVbillstatus(Integer vbillstatus) {
		this.vbillstatus = vbillstatus;
	}
	public String getVbillno() {
		return vbillno;
	}
	public void setVbillno(String vbillno) {
		this.vbillno = vbillno;
	}
	public String getPk_billtype() {
		return pk_billtype;
	}
	public void setPk_billtype(String pk_billtype) {
		this.pk_billtype = pk_billtype;
	}
	public UFDate getDbilldate() {
		return dbilldate;
	}
	public void setDbilldate(UFDate dbilldate) {
		this.dbilldate = dbilldate;
	}
	public String getPk_busitype() {
		return pk_busitype;
	}
	public void setPk_busitype(String pk_busitype) {
		this.pk_busitype = pk_busitype;
	}
	public UFBoolean getFisclose() {
		return fisclose;
	}
	public void setFisclose(UFBoolean fisclose) {
		this.fisclose = fisclose;
	}
	public UFBoolean getFisself() {
		return fisself;
	}
	public void setFisself(UFBoolean fisself) {
		this.fisself = fisself;
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
	public String getVapprovenote() {
		return vapprovenote;
	}
	public void setVapprovenote(String vapprovenote) {
		this.vapprovenote = vapprovenote;
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
	public String getVdef6() {
		return vdef6;
	}
	public void setVdef6(String vdef6) {
		this.vdef6 = vdef6;
	}
	public String getVdef7() {
		return vdef7;
	}
	public void setVdef7(String vdef7) {
		this.vdef7 = vdef7;
	}
	public String getVdef8() {
		return vdef8;
	}
	public void setVdef8(String vdef8) {
		this.vdef8 = vdef8;
	}
	public String getVdef9() {
		return vdef9;
	}
	public void setVdef9(String vdef9) {
		this.vdef9 = vdef9;
	}
	public String getVdef10() {
		return vdef10;
	}
	public void setVdef10(String vdef10) {
		this.vdef10 = vdef10;
	}
	public String getPk_defdoc1() {
		return pk_defdoc1;
	}
	public void setPk_defdoc1(String pk_defdoc1) {
		this.pk_defdoc1 = pk_defdoc1;
	}
	public String getPk_defdoc2() {
		return pk_defdoc2;
	}
	public void setPk_defdoc2(String pk_defdoc2) {
		this.pk_defdoc2 = pk_defdoc2;
	}
	public String getPk_defdoc3() {
		return pk_defdoc3;
	}
	public void setPk_defdoc3(String pk_defdoc3) {
		this.pk_defdoc3 = pk_defdoc3;
	}
	public String getPk_defdoc4() {
		return pk_defdoc4;
	}
	public void setPk_defdoc4(String pk_defdoc4) {
		this.pk_defdoc4 = pk_defdoc4;
	}
	public String getPk_defdoc5() {
		return pk_defdoc5;
	}
	public void setPk_defdoc5(String pk_defdoc5) {
		this.pk_defdoc5 = pk_defdoc5;
	}
	public String getVreserve1() {
		return vreserve1;
	}
	public void setVreserve1(String vreserve1) {
		this.vreserve1 = vreserve1;
	}
	public String getVreserve2() {
		return vreserve2;
	}
	public void setVreserve2(String vreserve2) {
		this.vreserve2 = vreserve2;
	}
	public String getVreserve3() {
		return vreserve3;
	}
	public void setVreserve3(String vreserve3) {
		this.vreserve3 = vreserve3;
	}
	public String getVreserve4() {
		return vreserve4;
	}
	public void setVreserve4(String vreserve4) {
		this.vreserve4 = vreserve4;
	}
	public String getVreserve5() {
		return vreserve5;
	}
	public void setVreserve5(String vreserve5) {
		this.vreserve5 = vreserve5;
	}
	public UFDouble getNreserve1() {
		return nreserve1;
	}
	public void setNreserve1(UFDouble nreserve1) {
		this.nreserve1 = nreserve1;
	}
	public UFDouble getNreserve2() {
		return nreserve2;
	}
	public void setNreserve2(UFDouble nreserve2) {
		this.nreserve2 = nreserve2;
	}
	public UFDouble getNreserve3() {
		return nreserve3;
	}
	public void setNreserve3(UFDouble nreserve3) {
		this.nreserve3 = nreserve3;
	}
	public UFBoolean getUreserve1() {
		return ureserve1;
	}
	public void setUreserve1(UFBoolean ureserve1) {
		this.ureserve1 = ureserve1;
	}
	public UFBoolean getUreserve2() {
		return ureserve2;
	}
	public void setUreserve2(UFBoolean ureserve2) {
		this.ureserve2 = ureserve2;
	}
	public UFBoolean getUreserve3() {
		return ureserve3;
	}
	public void setUreserve3(UFBoolean ureserve3) {
		this.ureserve3 = ureserve3;
	}

}
