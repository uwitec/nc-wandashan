package nc.vo.wds2.set;

import nc.vo.pub.SuperVO;

public class OutInSetVO extends SuperVO {
	
	private String pk_corp;
	private String cid;
	private Integer ibiztype;
	private String couttypeid;
	private String cintypeid;
	private String vmemo;
	
	private String vdef1;
	private String vdef2;
	private String vdef3;
	
	

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public Integer getIbiztype() {
		return ibiztype;
	}

	public void setIbiztype(Integer ibiztype) {
		this.ibiztype = ibiztype;
	}

	public String getCouttypeid() {
		return couttypeid;
	}

	public void setCouttypeid(String couttypeid) {
		this.couttypeid = couttypeid;
	}

	public String getCintypeid() {
		return cintypeid;
	}

	public void setCintypeid(String cintypeid) {
		this.cintypeid = cintypeid;
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

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "cid";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "wds2_outinset";
	}

}
