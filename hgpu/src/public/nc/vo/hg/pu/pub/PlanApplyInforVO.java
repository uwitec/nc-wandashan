package nc.vo.hg.pu.pub;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;

//zhf   虚拟vo  载体   计划的申请信息

public class PlanApplyInforVO extends SuperVO {
	
	private String m_pocorp;//系统采购公司
	private String m_sLogCorp;//
	private UFDate m_uLogDate;
	private String m_sLogUser;
	private String capplydeptid;//申请部门
	private String capplypsnid;//申请人
	private String creqcalbodyid;//需求组织
	private String csupplycalbodyid;//供货组织
	private String csupplydeptid;//供应部门
	private String csupplycorpid;//供货单位
	private Object m_useObj = null;
	
	

	public Object getM_useObj() {
		return m_useObj;
	}

	public void setM_useObj(Object obj) {
		m_useObj = obj;
	}

	public UFDate getM_uLogDate() {
		return m_uLogDate;
	}

	public void setM_uLogDate(UFDate logDate) {
		m_uLogDate = logDate;
	}

	public String getM_pocorp() {
		return m_pocorp;
	}

	public void setM_pocorp(String m_pocorp) {
		this.m_pocorp = m_pocorp;
	}

	public String getM_sLogCorp() {
		return m_sLogCorp;
	}

	public void setM_sLogCorp(String logCorp) {
		m_sLogCorp = logCorp;
	}

	public String getM_sLogUser() {
		return m_sLogUser;
	}

	public void setM_sLogUser(String logUser) {
		m_sLogUser = logUser;
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

	public String getCreqcalbodyid() {
		return creqcalbodyid;
	}

	public void setCreqcalbodyid(String creqcalbodyid) {
		this.creqcalbodyid = creqcalbodyid;
	}

	public String getCsupplycalbodyid() {
		return csupplycalbodyid;
	}

	public void setCsupplycalbodyid(String csupplycalbodyid) {
		this.csupplycalbodyid = csupplycalbodyid;
	}

	public String getCsupplydeptid() {
		return csupplydeptid;
	}

	public void setCsupplydeptid(String csupplydeptid) {
		this.csupplydeptid = csupplydeptid;
	}

	public String getCsupplycorpid() {
		return csupplycorpid;
	}

	public void setCsupplycorpid(String csupplycorpid) {
		this.csupplycorpid = csupplycorpid;
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
