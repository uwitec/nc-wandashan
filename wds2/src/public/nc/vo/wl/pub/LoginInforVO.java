package nc.vo.wl.pub;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;

public class LoginInforVO extends SuperVO {
	
	private String loguser;//��¼��
	private String whid;//��Ա�����ֲ�
	private String spaceid;//��Ա������λ  ֻ�вִ���Ա���ֶβ�������
	private String area;//�ֲ�����
	private UFBoolean bistp;//��Ա�Ƿ��������Ȩ��
	private String wharea;//�ֿ��ַ
	private Integer type;//��Ա����---��Ա��������   0����Ա 1��Ϣ�� 2���˿� 3����
	
	
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
