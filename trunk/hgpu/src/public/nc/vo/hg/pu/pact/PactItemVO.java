package nc.vo.hg.pu.pact;

import nc.vo.pub.SuperVO;

public class PactItemVO extends SuperVO {
	
	private String pk_ct_term;
	private String pk_ct_manage;
	private String pk_ct_termset;
	private String pk_corp;
	private String termcode;
	private String termname;
	private String termtypename;
	private String termcontent;
	private String otherinfo;
	private String memo;
	

	@Override
	public String getPKFieldName() {
		return "pk_ct_term";
}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return "pk_ct_manage";
	}

	public String getPk_ct_term() {
		return pk_ct_term;
	}

	public void setPk_ct_term(String pk_ct_term) {
		this.pk_ct_term = pk_ct_term;
	}

	public String getPk_ct_manage() {
		return pk_ct_manage;
	}

	public void setPk_ct_manage(String pk_ct_manage) {
		this.pk_ct_manage = pk_ct_manage;
	}

	public String getPk_ct_termset() {
		return pk_ct_termset;
	}

	public void setPk_ct_termset(String pk_ct_termset) {
		this.pk_ct_termset = pk_ct_termset;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public String getTermcode() {
		return termcode;
	}

	public void setTermcode(String termcode) {
		this.termcode = termcode;
	}

	public String getTermname() {
		return termname;
	}

	public void setTermname(String termname) {
		this.termname = termname;
	}

	public String getTermtypename() {
		return termtypename;
	}

	public void setTermtypename(String termtypename) {
		this.termtypename = termtypename;
	}

	public String getTermcontent() {
		return termcontent;
	}

	public void setTermcontent(String termcontent) {
		this.termcontent = termcontent;
	}

	public String getOtherinfo() {
		return otherinfo;
	}

	public void setOtherinfo(String otherinfo) {
		this.otherinfo = otherinfo;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Override
	public String getTableName() {
		return "CT_TERM_BB4";
}
 	
}
