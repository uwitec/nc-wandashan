package nc.vo.zb.gen;

import nc.ui.scm.util.ObjectUtils;
import nc.vo.pub.lang.UFDate;
import nc.vo.zb.entry.ZbResultBodyVO;

public class GenOrderVO extends ZbResultBodyVO  {

	private String pk_corp;//��˾
	private String cbiddingid;//�������
	private UFDate dbilldate;//��������
	private String ccustbasid;//��Ӧ�̻���ID
	private String vbillno;//���ݺ�
	private String ccustmanid;//��Ӧ�̹���ID
	private String pk_deptdoc;//����
	private String vemployeeid;//ҵ��Ա
	private String pk_billtype;//��������
	private String czbresultid;//��������
	
	private String invcode;//����ʹ��
	
	public String getInvcode() {
		return invcode;
	}

	public void setInvcode(String invcode) {
		this.invcode = invcode;
	}

	public String getPk_billtype() {
		return pk_billtype;
	}

	public void setPk_billtype(String pk_billtype) {
		this.pk_billtype = pk_billtype;
	}

	public String getCzbresultid() {
		return czbresultid;
	}

	public void setCzbresultid(String czbresultid) {
		this.czbresultid = czbresultid;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public String getCbiddingid() {
		return cbiddingid;
	}

	public void setCbiddingid(String cbiddingid) {
		this.cbiddingid = cbiddingid;
	}

	public UFDate getDbilldate() {
		return dbilldate;
	}

	public void setDbilldate(UFDate dbilldate) {
		this.dbilldate = dbilldate;
	}

	public String getCcustbasid() {
		return ccustbasid;
	}

	public void setCcustbasid(String ccustbasid) {
		this.ccustbasid = ccustbasid;
	}

	public String getVbillno() {
		return vbillno;
	}

	public void setVbillno(String vbillno) {
		this.vbillno = vbillno;
	}

	public String getCcustmanid() {
		return ccustmanid;
	}

	public void setCcustmanid(String ccustmanid) {
		this.ccustmanid = ccustmanid;
	}

	public String getPk_deptdoc() {
		return pk_deptdoc;
	}

	public void setPk_deptdoc(String pk_deptdoc) {
		this.pk_deptdoc = pk_deptdoc;
	}

	public String getVemployeeid() {
		return vemployeeid;
	}

	public void setVemployeeid(String vemployeeid) {
		this.vemployeeid = vemployeeid;
	}

	@Override
	public Object clone() {
		// TODO Auto-generated method stub
		Object o = this;
		try{
			o = ObjectUtils.serializableClone(this);
		}catch(Exception e){
			e.printStackTrace();
		}
		return o;
	}

}
