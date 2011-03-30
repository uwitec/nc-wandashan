package nc.vo.dm.sb;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

public class SbDocVO extends SuperVO{
	
	public String pk_sbdoc;
	public String vsbdocid;
	public String vsbdocname;
	public UFBoolean fisincity;
	//时间戳
    public UFDateTime ts;
    //删除标志位
    public Integer dr;
    public String vdef1;
    public String vdef2;
    public String vdef3;
    public UFDouble ndef1;
    public UFDouble ndef2;
    public UFDouble ndef3;
    
	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "pk_sbdoc";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "wds_sbdoc";
	}

	public String getPk_sbdocid() {
		return pk_sbdoc;
	}

	public void setPk_sbdocid(String pk_sbdocid) {
		this.pk_sbdoc = pk_sbdocid;
	}

	public String getVsbdocid() {
		return vsbdocid;
	}

	public void setVsbdocid(String vsbdocid) {
		this.vsbdocid = vsbdocid;
	}

	public String getVsbdocname() {
		return vsbdocname;
	}

	public void setVsbdocname(String vsbdocname) {
		this.vsbdocname = vsbdocname;
	}

	public UFBoolean getFisincity() {
		return fisincity;
	}

	public void setFisincity(UFBoolean fisincity) {
		this.fisincity = fisincity;
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

}
