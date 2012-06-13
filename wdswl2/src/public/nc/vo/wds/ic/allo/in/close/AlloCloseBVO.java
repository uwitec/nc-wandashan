package nc.vo.wds.ic.allo.in.close;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public class AlloCloseBVO extends SuperVO {
	
//	-----------�������ⵥ---������Ϣ
//	private String cgeneralhid;
	private String crowno;
	private String cgeneralbid;
	private String vfirstbillcode;
	private String vnotebody;
	private Integer fbillrowflag;//��״̬
	private UFDouble ncorrespondnum;//�ۼƳ�������
	private UFDouble ncorrespondastnum;//�ۼƳ��⸨����
	private UFBoolean flargess;//�Ƿ���Ʒ
	private UFDouble noutassistnum; //ʵ�������� DECIMAL 20 8         
	private UFDouble noutnum; //ʵ������ DECIMAL 20 8         
	private UFDouble nshouldoutassistnum;// Ӧ�������� DECIMAL 20 8         
	private UFDouble nshouldoutnum;// Ӧ������ 
	private UFDouble hsl;
	private UFDate dbizdate;// ҵ������ 
//	private String cbodywarehouseid;
//	private String pk_bodycalbody;
	private String vbatchcode;
	private String cinventoryid;
	private String cinvbasid;
	private UFDouble nkdnum;//�ۼ�ת��������
	private UFDouble ntranoutnum;//ת������
	private UFDouble ntranoutastnum;//ת��������	

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "cgeneralbid";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return "cgeneralhid";
	}

	

	public String getCrowno() {
		return crowno;
	}

	public void setCrowno(String crowno) {
		this.crowno = crowno;
	}

	public String getCgeneralbid() {
		return cgeneralbid;
	}

	public void setCgeneralbid(String cgeneralbid) {
		this.cgeneralbid = cgeneralbid;
	}

	public String getVfirstbillcode() {
		return vfirstbillcode;
	}

	public void setVfirstbillcode(String vfirstbillcode) {
		this.vfirstbillcode = vfirstbillcode;
	}

	public String getVnotebody() {
		return vnotebody;
	}

	public void setVnotebody(String vnotebody) {
		this.vnotebody = vnotebody;
	}

	public Integer getFbillrowflag() {
		return fbillrowflag;
	}

	public void setFbillrowflag(Integer fbillrowflag) {
		this.fbillrowflag = fbillrowflag;
	}

	public UFDouble getNcorrespondnum() {
		return ncorrespondnum;
	}

	public void setNcorrespondnum(UFDouble ncorrespondnum) {
		this.ncorrespondnum = ncorrespondnum;
	}

	public UFDouble getNcorrespondastnum() {
		return ncorrespondastnum;
	}

	public void setNcorrespondastnum(UFDouble ncorrespondastnum) {
		this.ncorrespondastnum = ncorrespondastnum;
	}

	public UFBoolean getFlargess() {
		return flargess;
	}

	public void setFlargess(UFBoolean flargess) {
		this.flargess = flargess;
	}

	public UFDouble getNoutassistnum() {
		return noutassistnum;
	}

	public void setNoutassistnum(UFDouble noutassistnum) {
		this.noutassistnum = noutassistnum;
	}

	public UFDouble getNoutnum() {
		return noutnum;
	}

	public void setNoutnum(UFDouble noutnum) {
		this.noutnum = noutnum;
	}

	public UFDouble getNshouldoutassistnum() {
		return nshouldoutassistnum;
	}

	public void setNshouldoutassistnum(UFDouble nshouldoutassistnum) {
		this.nshouldoutassistnum = nshouldoutassistnum;
	}

	public UFDouble getNshouldoutnum() {
		return nshouldoutnum;
	}

	public void setNshouldoutnum(UFDouble nshouldoutnum) {
		this.nshouldoutnum = nshouldoutnum;
	}

	public UFDouble getHsl() {
		return hsl;
	}

	public void setHsl(UFDouble hsl) {
		this.hsl = hsl;
	}

	public UFDate getDbizdate() {
		return dbizdate;
	}

	public void setDbizdate(UFDate dbizdate) {
		this.dbizdate = dbizdate;
	}

//	public String getCbodywarehouseid() {
//		return cbodywarehouseid;
//	}
//
//	public void setCbodywarehouseid(String cbodywarehouseid) {
//		this.cbodywarehouseid = cbodywarehouseid;
//	}
//
//	public String getPk_bodycalbody() {
//		return pk_bodycalbody;
//	}
//
//	public void setPk_bodycalbody(String pk_bodycalbody) {
//		this.pk_bodycalbody = pk_bodycalbody;
//	}

	public String getVbatchcode() {
		return vbatchcode;
	}

	public void setVbatchcode(String vbatchcode) {
		this.vbatchcode = vbatchcode;
	}

	public String getCinventoryid() {
		return cinventoryid;
	}

	public void setCinventoryid(String cinventoryid) {
		this.cinventoryid = cinventoryid;
	}

	public String getCinvbasid() {
		return cinvbasid;
	}

	public void setCinvbasid(String cinvbasid) {
		this.cinvbasid = cinvbasid;
	}

	public UFDouble getNkdnum() {
		return nkdnum;
	}

	public void setNkdnum(UFDouble nkdnum) {
		this.nkdnum = nkdnum;
	}

	public UFDouble getNtranoutnum() {
		return ntranoutnum;
	}

	public void setNtranoutnum(UFDouble ntranoutnum) {
		this.ntranoutnum = ntranoutnum;
	}

	public UFDouble getNtranoutastnum() {
		return ntranoutastnum;
	}

	public void setNtranoutastnum(UFDouble ntranoutastnum) {
		this.ntranoutastnum = ntranoutastnum;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "ic_general_b";
	}

}
