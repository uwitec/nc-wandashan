package nc.vo.dm.cost.trans;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

public class TransmlVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3247347051589244095L;

	private String pk_transml;
	private String vdef1;
	private String vdef2;
	private String vdef3;
	private String vdef4;
	private String vdef5;
	private String vdef6;
	private UFDateTime ts;
	private Integer dr;
	private String pk_corp;
	private String pk_vdelplace;
	private String pk_vrecplace;
	private UFDouble dmile;
	private String pk_vfirstrecord;
	private UFDate dfirstdate;
	private String pk_vlastrecord;
	private UFDate dlastdate;

	public String getPk_vdelplace() {
		return pk_vdelplace;
	}

	public void setPk_vdelplace(String pk_vdelplace) {
		this.pk_vdelplace = pk_vdelplace;
	}

	public String getPk_vrecplace() {
		return pk_vrecplace;
	}

	public void setPk_vrecplace(String pk_vrecplace) {
		this.pk_vrecplace = pk_vrecplace;
	}

	public String getPk_vfirstrecord() {
		return pk_vfirstrecord;
	}

	public void setPk_vfirstrecord(String pk_vfirstrecord) {
		this.pk_vfirstrecord = pk_vfirstrecord;
	}

	public String getPk_vlastrecord() {
		return pk_vlastrecord;
	}

	public void setPk_vlastrecord(String pk_vlastrecord) {
		this.pk_vlastrecord = pk_vlastrecord;
	}

	public String getPk_transml() {
		return pk_transml;
	}

	public void setPk_transml(String pk_transml) {
		this.pk_transml = pk_transml;
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



	public UFDouble getDmile() {
		return dmile;
	}

	public void setDmile(UFDouble dmile) {
		this.dmile = dmile;
	}



	public UFDate getDfirstdate() {
		return dfirstdate;
	}

	public void setDfirstdate(UFDate dfirstdate) {
		this.dfirstdate = dfirstdate;
	}




	public UFDate getDlastdate() {
		return dlastdate;
	}

	public void setDlastdate(UFDate dlastdate) {
		this.dlastdate = dlastdate;
	}

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "pk_transml";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "wds_transml";
	}

}
