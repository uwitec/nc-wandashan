package nc.ui.zb.bidfloor;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;

public class ViewDetailVO extends SuperVO {
	
	private String  ccustbasid;//��Ӧ�̻���ID
	private String  ccustmanid;//��Ӧ�̹���ID
	private UFDouble nprice;//һ����ͱ���
	private String ccircalnoid;//�ִ�
	private String custcode;//��Ӧ������ ��������
	private String vname;//�ִ����� ��������

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

	public String getCcustbasid() {
		return ccustbasid;
	}

	public void setCcustbasid(String ccustbasid) {
		this.ccustbasid = ccustbasid;
	}

	public String getCcustmanid() {
		return ccustmanid;
	}

	public void setCcustmanid(String ccustmanid) {
		this.ccustmanid = ccustmanid;
	}

	public UFDouble getNprice() {
		return nprice;
	}

	public void setNprice(UFDouble nprice) {
		this.nprice = nprice;
	}

	public String getCcircalnoid() {
		return ccircalnoid;
	}

	public void setCcircalnoid(String ccircalnoid) {
		this.ccircalnoid = ccircalnoid;
	}

	public String getCustcode() {
		return custcode;
	}

	public void setCustcode(String custcode) {
		this.custcode = custcode;
	}

	public String getVname() {
		return vname;
	}

	public void setVname(String vname) {
		this.vname = vname;
	}
	
}
