package nc.vo.ld;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

public class TestVO extends SuperVO {
   //左侧树要存放的字段
	private String pk_invcl;
	private String invclasscode ;
	private String invname;

	private String invclassname;
	private String invclasslev;
   //右侧单据的数据
   public String pk_invmandoc;
    public String pk_corp;
    public String reserve5;
    public UFDouble unloadprice;
    public String vdef4;
    public String reserve4;
    public String vdef7;
    public Integer dr;
    public UFDouble loadprice;
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
   
    public String vdef6;
    public String reserve2;
    public String vdef10;
    public String vdef5;
	@Override
	
	
	
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getInvname() {
		return invname;
	}

	public void setInvname(String invname) {
		this.invname = invname;
	}

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
	public String getPk_corp() {
		return pk_corp;
	}
	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}
	public String getReserve5() {
		return reserve5;
	}
	public void setReserve5(String reserve5) {
		this.reserve5 = reserve5;
	}
	public UFDouble getUnloadprice() {
		return unloadprice;
	}
	public void setUnloadprice(UFDouble unloadprice) {
		this.unloadprice = unloadprice;
	}
	public String getVdef4() {
		return vdef4;
	}
	public void setVdef4(String vdef4) {
		this.vdef4 = vdef4;
	}
	public String getReserve4() {
		return reserve4;
	}
	public void setReserve4(String reserve4) {
		this.reserve4 = reserve4;
	}
	public String getVdef7() {
		return vdef7;
	}
	public void setVdef7(String vdef7) {
		this.vdef7 = vdef7;
	}
	public Integer getDr() {
		return dr;
	}
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	public UFDouble getLoadprice() {
		return loadprice;
	}
	public void setLoadprice(UFDouble loadprice) {
		this.loadprice = loadprice;
	}
	public String getPk_invbasdoc() {
		return pk_invbasdoc;
	}
	public void setPk_invbasdoc(String pk_invbasdoc) {
		this.pk_invbasdoc = pk_invbasdoc;
	}
	public String getVemployeeid() {
		return vemployeeid;
	}
	public void setVemployeeid(String vemployeeid) {
		this.vemployeeid = vemployeeid;
	}
	public String getVdef2() {
		return vdef2;
	}
	public void setVdef2(String vdef2) {
		this.vdef2 = vdef2;
	}
	public UFDate getReserve12() {
		return reserve12;
	}
	public void setReserve12(UFDate reserve12) {
		this.reserve12 = reserve12;
	}
	public UFDate getReserve11() {
		return reserve11;
	}
	public void setReserve11(UFDate reserve11) {
		this.reserve11 = reserve11;
	}
	public UFBoolean getIscaima() {
		return iscaima;
	}
	public void setIscaima(UFBoolean iscaima) {
		this.iscaima = iscaima;
	}
	public String getVmemo() {
		return vmemo;
	}
	public void setVmemo(String vmemo) {
		this.vmemo = vmemo;
	}
	public UFDateTime getTs() {
		return ts;
	}
	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}
	public String getVdef1() {
		return vdef1;
	}
	public void setVdef1(String vdef1) {
		this.vdef1 = vdef1;
	}
	public String getReserve6() {
		return reserve6;
	}
	public void setReserve6(String reserve6) {
		this.reserve6 = reserve6;
	}
	public String getReserve3() {
		return reserve3;
	}
	public void setReserve3(String reserve3) {
		this.reserve3 = reserve3;
	}
	public UFBoolean getReserve14() {
		return reserve14;
	}
	public void setReserve14(UFBoolean reserve14) {
		this.reserve14 = reserve14;
	}
	public UFDate getReserve13() {
		return reserve13;
	}
	public void setReserve13(UFDate reserve13) {
		this.reserve13 = reserve13;
	}
	public UFDouble getReserve10() {
		return reserve10;
	}
	public void setReserve10(UFDouble reserve10) {
		this.reserve10 = reserve10;
	}
	public String getPk_ldprice() {
		return pk_ldprice;
	}
	public void setPk_ldprice(String pk_ldprice) {
		this.pk_ldprice = pk_ldprice;
	}
	public UFDouble getJoinprice() {
		return joinprice;
	}
	public void setJoinprice(UFDouble joinprice) {
		this.joinprice = joinprice;
	}
	public String getPk_deptdoc() {
		return pk_deptdoc;
	}
	public void setPk_deptdoc(String pk_deptdoc) {
		this.pk_deptdoc = pk_deptdoc;
	}
	public String getVdef3() {
		return vdef3;
	}
	public void setVdef3(String vdef3) {
		this.vdef3 = vdef3;
	}
	public UFDouble getReserve8() {
		return reserve8;
	}
	public void setReserve8(UFDouble reserve8) {
		this.reserve8 = reserve8;
	}
	public UFDouble getReserve9() {
		return reserve9;
	}
	public void setReserve9(UFDouble reserve9) {
		this.reserve9 = reserve9;
	}
	public String getVdef9() {
		return vdef9;
	}
	public void setVdef9(String vdef9) {
		this.vdef9 = vdef9;
	}
	public UFDouble getCaimaprice() {
		return caimaprice;
	}
	public void setCaimaprice(UFDouble caimaprice) {
		this.caimaprice = caimaprice;
	}
	public String getVdef8() {
		return vdef8;
	}
	public void setVdef8(String vdef8) {
		this.vdef8 = vdef8;
	}
	public String getReserve1() {
		return reserve1;
	}
	public void setReserve1(String reserve1) {
		this.reserve1 = reserve1;
	}
	public String getReserve7() {
		return reserve7;
	}
	public void setReserve7(String reserve7) {
		this.reserve7 = reserve7;
	}
	public UFBoolean getReserve15() {
		return reserve15;
	}
	public void setReserve15(UFBoolean reserve15) {
		this.reserve15 = reserve15;
	}
	public String getPk_invmandoc() {
		return pk_invmandoc;
	}
	public void setPk_invmandoc(String pk_invmandoc) {
		this.pk_invmandoc = pk_invmandoc;
	}
	public String getVdef6() {
		return vdef6;
	}
	public void setVdef6(String vdef6) {
		this.vdef6 = vdef6;
	}
	public String getReserve2() {
		return reserve2;
	}
	public void setReserve2(String reserve2) {
		this.reserve2 = reserve2;
	}
	public String getVdef10() {
		return vdef10;
	}
	public void setVdef10(String vdef10) {
		this.vdef10 = vdef10;
	}
	public String getVdef5() {
		return vdef5;
	}
	public void setVdef5(String vdef5) {
		this.vdef5 = vdef5;
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
