package nc.vo.ld;

import nc.vo.pub.SuperVO;

public class InvclVO extends SuperVO{
	private String pk_invcl;
	private String invclasscode ;
	private String invclassname;
	private String invclasslev;
	
	

	public String getPk_invcl() {
		return pk_invcl;
	}

	public void setPk_invcl(String pk_invcl) {
		this.pk_invcl = pk_invcl;
	}

	public String getInvclasscode() {
		return invclasscode;
	}

	public void setInvclasscode(String invclasscode) {
		this.invclasscode = invclasscode;
	}

	public String getInvclassname() {
		return invclassname;
	}

	public void setInvclassname(String invclassname) {
		this.invclassname = invclassname;
	}

	public String getInvclasslev() {
		return invclasslev;
	}

	public void setInvclasslev(String invclasslev) {
		this.invclasslev = invclasslev;
	}

	@Override
	public String getPKFieldName() {
		
		return "pk_invcl";
	}

	@Override
	public String getParentPKFieldName() {
		
		return null;
	}

	@Override
	public String getTableName() {
		
		return null;
	}

}
