package nc.vo.hg.pu.plan.balance;

import nc.vo.hg.pu.plan.month.PlanOtherBVO;
import nc.vo.pub.lang.UFDate;

public class PlanMonDealVO extends PlanOtherBVO {
	private String vbillno;//�¼ƻ�����
	private String cyear;//���
	private String cmonth;//�·�
	private String creqcalbodyid;//������֯
	private String creqwarehouseid;//����ֿ�
	private String capplydeptid;//���벿��
	private String capplypsnid;//������
	private UFDate dbilldate;//��������
	private String csourcebillno;//��ƻ�����
	private String invcode;//����ʹ��
	private String pk_corp;//���빫˾
	private String pk_plan_b;//�ƻ��ӱ�id	
	public String getInvcode() {
		return invcode;
	}
	public void setInvcode(String invcode) {
		this.invcode = invcode;
	}
	public String getPk_plan_b() {
		return pk_plan_b;
	}
	public void setPk_plan_b(String pk_plan_b) {
		this.pk_plan_b = pk_plan_b;
	}
	public String getVbillno() {
		return vbillno;
	}
	public void setVbillno(String vbillno) {
		this.vbillno = vbillno;
	}
	public String getCyear() {
		return cyear;
	}
	public void setCyear(String cyear) {
		this.cyear = cyear;
	}
	public String getCmonth() {
		return cmonth;
	}
	public void setCmonth(String cmonth) {
		this.cmonth = cmonth;
	}
	public String getCreqcalbodyid() {
		return creqcalbodyid;
	}
	public void setCreqcalbodyid(String creqcalbodyid) {
		this.creqcalbodyid = creqcalbodyid;
	}
	public String getCreqwarehouseid() {
		return creqwarehouseid;
	}
	public void setCreqwarehouseid(String creqwarehouseid) {
		this.creqwarehouseid = creqwarehouseid;
	}
	public String getCapplydeptid() {
		return capplydeptid;
	}
	public void setCapplydeptid(String capplydeptid) {
		this.capplydeptid = capplydeptid;
	}
	public String getCapplypsnid() {
		return capplypsnid;
	}
	public void setCapplypsnid(String capplypsnid) {
		this.capplypsnid = capplypsnid;
	}
	public UFDate getDbilldate() {
		return dbilldate;
	}
	public void setDbilldate(UFDate dbilldate) {
		this.dbilldate = dbilldate;
	}
	public String getCsourcebillno() {
		return csourcebillno;
	}
	public void setCsourcebillno(String csourcebillno) {
		this.csourcebillno = csourcebillno;
	}
	public String getPk_corp() {
		return pk_corp;
	}
	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

}
