package nc.vo.dm.sbr;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

public class SbDocRVO extends SuperVO{
	
	public String pk_sbdocr;
	public String vsbodcname;
	public String vsbdocid;
	public UFDouble fares;
	public UFDouble funit;
	public Integer fstauts;
	public String vdef1;
    public String vdef2;
    public String vdef3;
    public UFDouble ndef1;
    public UFDouble ndef2;
    public UFDouble ndef3;
    public UFDateTime ts;
    public Integer dr;

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "pk_sbdocr";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "wds_sbdocr";
	}

	public String getPk_sbdocr() {
		return pk_sbdocr;
	}

	public void setPk_sbdocr(String pk_sbdocr) {
		this.pk_sbdocr = pk_sbdocr;
	}

	public String getVsbodcname() {
		return vsbodcname;
	}

	public void setVsbodcname(String vsbodcname) {
		this.vsbodcname = vsbodcname;
	}

	public String getVsbdocid() {
		return vsbdocid;
	}

	public void setVsbdocid(String vsbdocid) {
		this.vsbdocid = vsbdocid;
	}

	public UFDouble getFares() {
		return fares;
	}

	public void setFares(UFDouble fares) {
		this.fares = fares;
	}

	public UFDouble getFunit() {
		return funit;
	}

	public void setFunit(UFDouble funit) {
		this.funit = funit;
	}

	public Integer getFstauts() {
		return fstauts;
	}

	public void setFstauts(Integer fstauts) {
		this.fstauts = fstauts;
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

}
