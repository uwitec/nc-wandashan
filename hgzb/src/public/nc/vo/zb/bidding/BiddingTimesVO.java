package nc.vo.zb.bidding;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFTime;

/**
 * 
 * @author zhf
 * ������ִ�ʱ�䰲���ӱ�
 */
public class BiddingTimesVO extends SuperVO {
	
	private String cbiddingtimesid;//�ִ�ʱ���id
	private String cbiddingid;//����id
	private String crowno;//ʱ������
	private String vname;//�ִ�����
	private UFDateTime tbigintime = null;//�ִο�ʼʱ��
	private UFDateTime tendtime = null;//�ִν���ʱ��
	private Integer idelay = 0;//ʱ��
	private Integer idelayunit;//ʱ����λ    0 Сʱ 1���� 2��   Ĭ�Ϸ���
	private UFBoolean bisfollow = UFBoolean.FALSE;//�Ƿ����-----�޸�Ϊ�Ƿ������б� һ��һ�α���ʱ ��������
	private Integer dr;
	private UFDateTime ts;
	private UFTime tstart;//��ʼʱ��
	private UFTime tend;//����ʱ��
	private UFDate dbegindate;//��ʼ����
	private UFDate dendate;//��������
	
	private String cprecircalnoid;//��һ���ִ�id   �����ִ�ʱ�������ִ�֮��Ĺ�ϵ  ע��ʱ���ϵУ��
//	��Ҫ����������͵��� �ִ�ʱ��� ʱ����ά��
	private String cnextcircalnoid;//��һ���ִ�id
	
	private String vdef1;
	private String vdef2;
	private String vdef3;
	private String vdef4;
	private String vdef5;
	
	private UFDouble ndef1;
	private UFDouble ndef2;
	private UFDouble ndef3;
	
	private Integer idef1;
	private Integer idef2;
	
	

	public String getCnextcircalnoid() {
		return cnextcircalnoid;
	}

	public void setCnextcircalnoid(String cnextcircalnoid) {
		this.cnextcircalnoid = cnextcircalnoid;
	}

	public String getCprecircalnoid() {
		return cprecircalnoid;
	}

	public void setCprecircalnoid(String cprecircalnoid) {
		this.cprecircalnoid = cprecircalnoid;
	}

	public String getCbiddingtimesid() {
		return cbiddingtimesid;
	}

	public void setCbiddingtimesid(String cbiddingtimesid) {
		this.cbiddingtimesid = cbiddingtimesid;
	}

	public String getCbiddingid() {
		return cbiddingid;
	}

	public void setCbiddingid(String cbiddingid) {
		this.cbiddingid = cbiddingid;
	}

	public String getCrowno() {
		return crowno;
	}

	public void setCrowno(String crowno) {
		this.crowno = crowno;
	}

	public String getVname() {
		return vname;
	}

	public void setVname(String vname) {
		this.vname = vname;
	}

	public UFDateTime getTbigintime() {
		return tbigintime;
	}

	public void setTbigintime(UFDateTime tbigintime) {
		this.tbigintime = tbigintime;
	}

	public UFDateTime getTendtime() {
		return tendtime;
	}

	public void setTendtime(UFDateTime tendtime) {
		this.tendtime = tendtime;
	}

	public Integer getIdelay() {
		return idelay;
	}

	public void setIdelay(Integer idelay) {
		this.idelay = idelay;
	}

	public Integer getIdelayunit() {
		return idelayunit;
	}

	public void setIdelayunit(Integer idelayunit) {
		this.idelayunit = idelayunit;
	}

	public UFBoolean getBisfollow() {
		return bisfollow;
	}

	public void setBisfollow(UFBoolean bisfollow) {
		this.bisfollow = bisfollow;
	}

	public String getVdef1() {
		return vdef1;
	}

	public void setVdef1(String vdef1) {
		this.vdef1 = vdef1;
	}

	public String getVdef2() {
		return vdef2;
	}

	public void setVdef2(String vdef2) {
		this.vdef2 = vdef2;
	}

	public String getVdef3() {
		return vdef3;
	}

	public void setVdef3(String vdef3) {
		this.vdef3 = vdef3;
	}

	public String getVdef4() {
		return vdef4;
	}

	public void setVdef4(String vdef4) {
		this.vdef4 = vdef4;
	}

	public String getVdef5() {
		return vdef5;
	}

	public void setVdef5(String vdef5) {
		this.vdef5 = vdef5;
	}

	public UFDouble getNdef1() {
		return ndef1;
	}

	public void setNdef1(UFDouble ndef1) {
		this.ndef1 = ndef1;
	}

	public UFDouble getNdef2() {
		return ndef2;
	}

	public void setNdef2(UFDouble ndef2) {
		this.ndef2 = ndef2;
	}

	public UFDouble getNdef3() {
		return ndef3;
	}

	public void setNdef3(UFDouble ndef3) {
		this.ndef3 = ndef3;
	}

	public Integer getIdef1() {
		return idef1;
	}

	public void setIdef1(Integer idef1) {
		this.idef1 = idef1;
	}

	public Integer getIdef2() {
		return idef2;
	}

	public void setIdef2(Integer idef2) {
		this.idef2 = idef2;
	}

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "cbiddingtimesid";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return "cbiddingid";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "zb_biddingtimes";
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public UFDateTime getTs() {
		return ts;
	}

	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	public UFTime getTstart() {
		return tstart;
	}

	public void setTstart(UFTime tstart) {
		this.tstart = tstart;
	}

	public UFTime getTend() {
		return tend;
	}

	public void setTend(UFTime tend) {
		this.tend = tend;
	}

	public UFDate getDbegindate() {
		return dbegindate;
	}

	public void setDbegindate(UFDate dbegindate) {
		this.dbegindate = dbegindate;
	}

	public UFDate getDendate() {
		return dendate;
	}

	public void setDendate(UFDate dendate) {
		this.dendate = dendate;
	}

}
