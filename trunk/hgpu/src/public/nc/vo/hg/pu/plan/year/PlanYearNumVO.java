package nc.vo.hg.pu.plan.year;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

public class PlanYearNumVO extends SuperVO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String pk_planyear_b;
	private String cinventoryid;//存货管理
	private String pk_invbasdoc;//存货基本
	private String pk_measdoc;//主计量
	private UFBoolean fisload; //是否领用过
	private String cnextbillbid;
	private String cnextbillid;
//	private String crowno;
	private String pk_plan;
	private String vbatchcode;	
	 // --------------------------------年计划体 月份分量
	private UFDouble nmonnum1;
	private UFDouble nmonnum2;
	private UFDouble nmonnum3;
	private UFDouble nmonnum4;
	private UFDouble nmonnum5;
	private UFDouble nmonnum6;
	private UFDouble nmonnum7;
	private UFDouble nmonnum8;
	private UFDouble nmonnum9;
	private UFDouble nmonnum10;
	private UFDouble nmonnum11;	
	private UFDouble nmonnum12;
 // --------------------------------年计划体 月份调整后量
	private UFDouble naftenum1;
	private UFDouble naftenum2; 
	private UFDouble naftenum3; 
	private UFDouble naftenum4; 
	private UFDouble naftenum5; 
	private UFDouble naftenum6; 
	private UFDouble naftenum7; 
	private UFDouble naftenum8;
	private UFDouble naftenum9; 
	private UFDouble naftenum10; 
	private UFDouble nafternum11; 
	private UFDouble nafternum12; 
	
	//-----------------------月计划单体 累计领用量
	private UFDouble ntotailnum1;
	private UFDouble ntotailnum2; 
	private UFDouble ntotailnum3; 
	private UFDouble ntotailnum4; 
	private UFDouble ntotailnum5; 
	private UFDouble ntotailnum6; 
	private UFDouble ntotailnum7; 
	private UFDouble ntotailnum8;
	private UFDouble ntotailnum9; 
	private UFDouble ntotailnum10; 
	private UFDouble ntotailnum11; 
	private UFDouble ntotailnum12; 
	
	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "pk_planyear_b";
	}
	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return "pk_plan";
	}
	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "HG_PLANYEAR_B";
	}
	public String getPk_planyear_b() {
		return pk_planyear_b;
	}
	public void setPk_planyear_b(String pk_planyear_b) {
		this.pk_planyear_b = pk_planyear_b;
	}
	public String getCinventoryid() {
		return cinventoryid;
	}
	public void setCinventoryid(String cinventoryid) {
		this.cinventoryid = cinventoryid;
	}
	public String getPk_invbasdoc() {
		return pk_invbasdoc;
	}
	public void setPk_invbasdoc(String pk_invbasdoc) {
		this.pk_invbasdoc = pk_invbasdoc;
	}
	public String getPk_measdoc() {
		return pk_measdoc;
	}
	public void setPk_measdoc(String pk_measdoc) {
		this.pk_measdoc = pk_measdoc;
	}
	public String getCnextbillbid() {
		return cnextbillbid;
	}
	public void setCnextbillbid(String cnextbillbid) {
		this.cnextbillbid = cnextbillbid;
	}
	public String getCnextbillid() {
		return cnextbillid;
	}
	public void setCnextbillid(String cnextbillid) {
		this.cnextbillid = cnextbillid;
	}
//	public String getCrowno() {
//		return crowno;
//	}
//	public void setCrowno(String crowno) {
//		this.crowno = crowno;
//	}
	public UFDouble getNmonnum1() {
		return nmonnum1;
	}
	public void setNmonnum1(UFDouble nmonnum1) {
		this.nmonnum1 = nmonnum1;
	}
	public UFDouble getNmonnum2() {
		return nmonnum2;
	}
	public void setNmonnum2(UFDouble nmonnum2) {
		this.nmonnum2 = nmonnum2;
	}
	public UFDouble getNmonnum3() {
		return nmonnum3;
	}
	public void setNmonnum3(UFDouble nmonnum3) {
		this.nmonnum3 = nmonnum3;
	}
	public UFDouble getNmonnum4() {
		return nmonnum4;
	}
	public void setNmonnum4(UFDouble nmonnum4) {
		this.nmonnum4 = nmonnum4;
	}
	public UFDouble getNmonnum5() {
		return nmonnum5;
	}
	public void setNmonnum5(UFDouble nmonnum5) {
		this.nmonnum5 = nmonnum5;
	}
	public UFDouble getNmonnum6() {
		return nmonnum6;
	}
	public void setNmonnum6(UFDouble nmonnum6) {
		this.nmonnum6 = nmonnum6;
	}
	public UFDouble getNmonnum7() {
		return nmonnum7;
	}
	public void setNmonnum7(UFDouble nmonnum7) {
		this.nmonnum7 = nmonnum7;
	}
	public UFDouble getNmonnum8() {
		return nmonnum8;
	}
	public void setNmonnum8(UFDouble nmonnum8) {
		this.nmonnum8 = nmonnum8;
	}
	public UFDouble getNmonnum9() {
		return nmonnum9;
	}
	public void setNmonnum9(UFDouble nmonnum9) {
		this.nmonnum9 = nmonnum9;
	}
	public UFDouble getNmonnum10() {
		return nmonnum10;
	}
	public void setNmonnum10(UFDouble nmonnum10) {
		this.nmonnum10 = nmonnum10;
	}
	public UFDouble getNmonnum11() {
		return nmonnum11;
	}
	public void setNmonnum11(UFDouble nmonnum11) {
		this.nmonnum11 = nmonnum11;
	}
	public UFDouble getNmonnum12() {
		return nmonnum12;
	}
	public void setNmonnum12(UFDouble nmonnum12) {
		this.nmonnum12 = nmonnum12;
	}
	public UFDouble getNaftenum1() {
		return naftenum1;
	}
	public void setNaftenum1(UFDouble naftenum1) {
		this.naftenum1 = naftenum1;
	}
	public UFDouble getNaftenum2() {
		return naftenum2;
	}
	public void setNaftenum2(UFDouble naftenum2) {
		this.naftenum2 = naftenum2;
	}
	public UFDouble getNaftenum3() {
		return naftenum3;
	}
	public void setNaftenum3(UFDouble naftenum3) {
		this.naftenum3 = naftenum3;
	}
	public UFDouble getNaftenum4() {
		return naftenum4;
	}
	public void setNaftenum4(UFDouble naftenum4) {
		this.naftenum4 = naftenum4;
	}
	public UFDouble getNaftenum5() {
		return naftenum5;
	}
	public void setNaftenum5(UFDouble naftenum5) {
		this.naftenum5 = naftenum5;
	}
	public UFDouble getNaftenum6() {
		return naftenum6;
	}
	public void setNaftenum6(UFDouble naftenum6) {
		this.naftenum6 = naftenum6;
	}
	public UFDouble getNaftenum7() {
		return naftenum7;
	}
	public void setNaftenum7(UFDouble naftenum7) {
		this.naftenum7 = naftenum7;
	}
	public UFDouble getNaftenum8() {
		return naftenum8;
	}
	public void setNaftenum8(UFDouble naftenum8) {
		this.naftenum8 = naftenum8;
	}
	public UFDouble getNaftenum9() {
		return naftenum9;
	}
	public void setNaftenum9(UFDouble naftenum9) {
		this.naftenum9 = naftenum9;
	}
	public UFDouble getNaftenum10() {
		return naftenum10;
	}
	public void setNaftenum10(UFDouble naftenum10) {
		this.naftenum10 = naftenum10;
	}
	public UFDouble getNafternum11() {
		return nafternum11;
	}
	public void setNafternum11(UFDouble nafternum11) {
		this.nafternum11 = nafternum11;
	}
	public UFDouble getNafternum12() {
		return nafternum12;
	}
	public void setNafternum12(UFDouble nafternum12) {
		this.nafternum12 = nafternum12;
	}
	public UFDouble getNtotailnum1() {
		return ntotailnum1;
	}
	public void setNtotailnum1(UFDouble ntotailnum1) {
		this.ntotailnum1 = ntotailnum1;
	}
	public UFDouble getNtotailnum2() {
		return ntotailnum2;
	}
	public void setNtotailnum2(UFDouble ntotailnum2) {
		this.ntotailnum2 = ntotailnum2;
	}
	public UFDouble getNtotailnum3() {
		return ntotailnum3;
	}
	public void setNtotailnum3(UFDouble ntotailnum3) {
		this.ntotailnum3 = ntotailnum3;
	}
	public UFDouble getNtotailnum4() {
		return ntotailnum4;
	}
	public void setNtotailnum4(UFDouble ntotailnum4) {
		this.ntotailnum4 = ntotailnum4;
	}
	public UFDouble getNtotailnum5() {
		return ntotailnum5;
	}
	public void setNtotailnum5(UFDouble ntotailnum5) {
		this.ntotailnum5 = ntotailnum5;
	}
	public UFDouble getNtotailnum6() {
		return ntotailnum6;
	}
	public void setNtotailnum6(UFDouble ntotailnum6) {
		this.ntotailnum6 = ntotailnum6;
	}
	public UFDouble getNtotailnum7() {
		return ntotailnum7;
	}
	public void setNtotailnum7(UFDouble ntotailnum7) {
		this.ntotailnum7 = ntotailnum7;
	}
	public UFDouble getNtotailnum8() {
		return ntotailnum8;
	}
	public void setNtotailnum8(UFDouble ntotailnum8) {
		this.ntotailnum8 = ntotailnum8;
	}
	public UFDouble getNtotailnum9() {
		return ntotailnum9;
	}
	public void setNtotailnum9(UFDouble ntotailnum9) {
		this.ntotailnum9 = ntotailnum9;
	}
	public UFDouble getNtotailnum10() {
		return ntotailnum10;
	}
	public void setNtotailnum10(UFDouble ntotailnum10) {
		this.ntotailnum10 = ntotailnum10;
	}
	public UFDouble getNtotailnum11() {
		return ntotailnum11;
	}
	public void setNtotailnum11(UFDouble ntotailnum11) {
		this.ntotailnum11 = ntotailnum11;
	}
	public UFDouble getNtotailnum12() {
		return ntotailnum12;
	}
	public void setNtotailnum12(UFDouble ntotailnum12) {
		this.ntotailnum12 = ntotailnum12;
	}
	public String getPk_plan() {
		return pk_plan;
	}
	public void setPk_plan(String pk_plan) {
		this.pk_plan = pk_plan;
	}
	public String getVbatchcode() {
		return vbatchcode;
	}
	public void setVbatchcode(String vbatchcode) {
		this.vbatchcode = vbatchcode;
	}
	public UFBoolean getFisload() {
		return fisload;
	}
	public void setFisload(UFBoolean fisload) {
		this.fisload = fisload;
	}
}
