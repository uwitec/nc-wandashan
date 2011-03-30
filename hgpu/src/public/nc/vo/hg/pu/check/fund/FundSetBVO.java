package nc.vo.hg.pu.check.fund;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

public class FundSetBVO extends SuperVO {
	
	private String pk_fundset;//资金设置表id
	private String pk_fundset_b;//主键
	private String pk_plan;//计划id    计划的预留资金
	private UFDouble nlockmny; //预留金额
//	private UFDouble noldlockmny;//预扣前预留    预扣时产生   实扣时清除
	private String pk_corp;//公司
	private UFDateTime ts;
	private String vdef3;
	private String vdef1;//客户
	private String cdeptid;//用作部门id
	private Integer dr;
	private String imonth;//月份
	private String vdef4;
	private String vdef2;
	private String vdef5;
	private UFDouble nmny;//预扣金额
	private String iyear;//年度
	private String pk_planother_b;//计划子id    计划的预留资金

//	public UFDouble getNoldlockmny() {
//		return noldlockmny;
//	}
//
//	public void setNoldlockmny(UFDouble noldlockmny) {
//		this.noldlockmny = noldlockmny;
//	}

	public String getPk_fundset() {
		return pk_fundset;
	}

	public void setPk_fundset(String pk_fundset) {
		this.pk_fundset = pk_fundset;
	}

	public String getPk_fundset_b() {
		return pk_fundset_b;
	}

	public void setPk_fundset_b(String pk_fundset_b) {
		this.pk_fundset_b = pk_fundset_b;
	}

	public String getPk_plan() {
		return pk_plan;
	}

	public void setPk_plan(String pk_plan) {
		this.pk_plan = pk_plan;
	}

	public UFDouble getNlockmny() {
		return nlockmny;
	}

	public void setNlockmny(UFDouble nlockmny) {
		this.nlockmny = nlockmny;
	}

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "pk_fundset_b";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return "pk_fundset";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "hg_fundset_b";
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public UFDateTime getTs() {
		return ts;
	}

	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	public String getVdef3() {
		return vdef3;
	}

	public void setVdef3(String vdef3) {
		this.vdef3 = vdef3;
	}

	public String getVdef1() {
		return vdef1;
	}

	public void setVdef1(String vdef1) {
		this.vdef1 = vdef1;
	}

	public String getCdeptid() {
		return cdeptid;
	}

	public void setCdeptid(String cdeptid) {
		this.cdeptid = cdeptid;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public String getImonth() {
		return imonth;
	}

	public void setImonth(String imonth) {
		this.imonth = imonth;
	}

	public String getVdef4() {
		return vdef4;
	}

	public void setVdef4(String vdef4) {
		this.vdef4 = vdef4;
	}

	public String getVdef2() {
		return vdef2;
	}

	public void setVdef2(String vdef2) {
		this.vdef2 = vdef2;
	}

	public String getVdef5() {
		return vdef5;
	}

	public void setVdef5(String vdef5) {
		this.vdef5 = vdef5;
	}

	public UFDouble getNmny() {
		return nmny;
	}

	public void setNmny(UFDouble nmny) {
		this.nmny = nmny;
	}

	public String getIyear() {
		return iyear;
	}

	public void setIyear(String iyear) {
		this.iyear = iyear;
	}

	public String getPk_planother_b() {
		return pk_planother_b;
	}

	public void setPk_planother_b(String pk_planother_b) {
		this.pk_planother_b = pk_planother_b;
	}

}
