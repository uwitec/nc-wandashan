package nc.vo.wl.pub;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;

public class LoginInforVO extends SuperVO {
	
	private String loguser;//登录人
	private String whid;//人员所属分仓
	private String spaceid;//人员所属货位  只有仓储人员该字段才有意义
	private String area;//分仓区域
	private UFBoolean bistp;//人员是否具有特批权限
	private String wharea;//仓库地址
	private Integer type;//人员类型---人员所属部门   0保管员 1信息科 2发运科 3内勤
	
	
	public String getLoguser() {
		return loguser;
	}
	public void setLoguser(String loguser) {
		this.loguser = loguser;
	}
	public String getWhid() {
		return whid;
	}
	public void setWhid(String whid) {
		this.whid = whid;
	}
	public String getSpaceid() {
		return spaceid;
	}
	public void setSpaceid(String spaceid) {
		this.spaceid = spaceid;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public UFBoolean getBistp() {
		return bistp;
	}
	public void setBistp(UFBoolean bistp) {
		this.bistp = bistp;
	}
	public String getWharea() {
		return wharea;
	}
	public void setWharea(String wharea) {
		this.wharea = wharea;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
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
