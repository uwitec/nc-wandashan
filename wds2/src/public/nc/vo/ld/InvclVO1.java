package nc.vo.ld;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

public class InvclVO1 extends SuperVO {
	
	
	private String invclasscode ;
	private String invclassname;
	private String invclasslev;
	
	
	private String pk_invcl;
	
	//¿â±íÖÐµÄ×Ö¶Î
	  public String pk_corp;
      public String reserve5;
      public UFDouble unloadprice;
      public String vdef4;
      public String reserve4;
      public String vdef7;
      public Integer dr;
      public UFDouble loadprice;
      //
      public String pk_invbasdoc;
      
      public String vemployeeid;
      public String vdef2;
      public UFDate reserve12;
      public UFDate reserve11;
      public UFBoolean iscaima;
      public String vmemo;
      public UFDateTime ts;
      public String vdef1;
      public String reserve6;
      public String reserve3;
      public UFBoolean reserve14;
      public UFDate reserve13;
      public UFDouble reserve10;
      public String pk_ldprice;
      public UFDouble joinprice;
      public String pk_deptdoc;
      public String vdef3;
      public UFDouble reserve8;
      public UFDouble reserve9;
      public String vdef9;
      public UFDouble caimaprice;
      public String vdef8;
      public String reserve1;
      public String reserve7;
      public UFBoolean reserve15;
      public String pk_invmandoc;
      public String vdef6;
      public String reserve2;
      public String vdef10;
      public String vdef5;
	
	
	
	
	
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
	public String getPk_invbasdoc() {
		return pk_invbasdoc;
	}
	public void setPk_invbasdoc(String pk_invbasdoc) {
		this.pk_invbasdoc = pk_invbasdoc;
	}
	public String getPk_invcl() {
		return pk_invcl;
	}
	public void setPk_invcl(String pk_invcl) {
		this.pk_invcl = pk_invcl;
	}
	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
	
	

}
