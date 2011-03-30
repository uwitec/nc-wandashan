package nc.vo.hg.ic.ic201;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

public class PactVO extends SuperVO {
	
	private String corder_bid;
	private String corderid;
	private String vordercode;//��ͬ���ݺ�
	private String cmangid;
	private String cbaseid; 
	private UFDouble nordernum;
	private String cassistunit;
	private UFDouble nassistnum;
	private UFDouble naccumarrvnum;//�ۼƵ�������
	private UFDouble naccumstorenum;//�ۼ��������
	private UFBoolean blargess;//�Ƿ���Ʒ
	private String cvendormangid; //��Ӧ�̹���id                
	private String cvendorbaseid ;// ��Ӧ�̻���id 
	private String crowno;
	private UFDateTime ts;
	
	public UFDateTime getTs() {
		return ts;
	}

	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	//----------
	private UFDouble nusenum;//��ʹ������
	private UFDouble nnum;//����ʹ������
	

	
	public String getCrowno() {
		return crowno;
	}

	public void setCrowno(String crowno) {
		this.crowno = crowno;
	}

	public String getCorder_bid() {
		return corder_bid;
	}

	public void setCorder_bid(String corder_bid) {
		this.corder_bid = corder_bid;
	}

	public String getCorderid() {
		return corderid;
	}

	public void setCorderid(String corderid) {
		this.corderid = corderid;
	}	

	public String getVordercode() {
		return vordercode;
	}

	public void setVordercode(String vordercode) {
		this.vordercode = vordercode;
	}

	public String getCmangid() {
		return cmangid;
	}

	public void setCmangid(String cmangid) {
		this.cmangid = cmangid;
	}

	public String getCbaseid() {
		return cbaseid;
	}

	public void setCbaseid(String cbaseid) {
		this.cbaseid = cbaseid;
	}

	public UFDouble getNordernum() {
		return nordernum;
	}

	public void setNordernum(UFDouble nordernum) {
		this.nordernum = nordernum;
	}

	public String getCassistunit() {
		return cassistunit;
	}

	public void setCassistunit(String cassistunit) {
		this.cassistunit = cassistunit;
	}

	public UFDouble getNassistnum() {
		return nassistnum;
	}

	public void setNassistnum(UFDouble nassistnum) {
		this.nassistnum = nassistnum;
	}

	public UFDouble getNaccumarrvnum() {
		return naccumarrvnum;
	}

	public void setNaccumarrvnum(UFDouble naccumarrvnum) {
		this.naccumarrvnum = naccumarrvnum;
	}

	public UFDouble getNaccumstorenum() {
		return naccumstorenum;
	}

	public void setNaccumstorenum(UFDouble naccumstorenum) {
		this.naccumstorenum = naccumstorenum;
	}

	public UFBoolean getBlargess() {
		return blargess;
	}

	public void setBlargess(UFBoolean blargess) {
		this.blargess = blargess;
	}

	public String getCvendormangid() {
		return cvendormangid;
	}

	public void setCvendormangid(String cvendormangid) {
		this.cvendormangid = cvendormangid;
	}

	public String getCvendorbaseid() {
		return cvendorbaseid;
	}

	public void setCvendorbaseid(String cvendorbaseid) {
		this.cvendorbaseid = cvendorbaseid;
	}

	public UFDouble getNusenum() {
		return nusenum;
	}

	public void setNusenum(UFDouble nusenum) {
		this.nusenum = nusenum;
	}

	public UFDouble getNnum() {
		return nnum;
	}

	public void setNnum(UFDouble nnum) {
		this.nnum = nnum;
	}

	

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "corder_bid";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return "corderid";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "po_order_b";
	}

}
