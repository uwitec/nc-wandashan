package nc.vo.hg.to.pub;

import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;

public class StockNumParaVO extends SuperVO {
	
	/**
	 * zhf add  �������� �༭ʱ  ʹ�õ� �˴��� ����vo
	 */
	private String coutcorpid = null;//������˾
	private String cincorpid = null;//���빫˾
	private String coutcalbodyid = null;//������֯
	private String cincalbodyid =  null;//������֯
	private String coutwarehouseid = null;//�����ֿ�
	private String cinwarehouseid = null;//����ֿ�
	private String cinvbasid = null;//���id
	private String coutinvid = null;//�������id
	private String cininvid = null;
	private String cbatchid = null;//���κ�
	private UFDate dlogdate = null;//��ǰ��Ȼ����
	private UFDouble noutonhand = null;//�������ִ���
	private UFDouble ninonhand = null;//���뷽�����
	private UFDouble noutallnum = null;//�������������е�����������  �ô����ǰ�����е�����������֮��
	private UFDouble ninallnum = null;//���뷽�������е�����������  �ô����ǰ����Ը��û������е������ⵥ����֮��
	private UFDouble noutnum = null;//�������ѷ�������  �ô����ǰ�����е��������ѷ�����֮��
	private UFDouble ninnum = null;//���뷽�ѳ�������  ������õ�λ�����Ѿ����������֮��=����������+���õ�λ������-��ǰ���
	private UFDouble nfund = null;//�����ʽ�
	private UFDouble nmny = null;//�����޶�
	private UFDouble nallfund = null;//���ʽ�
	private UFDouble nallmny = null;//���޶�

	private String vdef1 = null;
	private String vdef2 = null;
	private String vdef3 = null;
	private UFDouble ndef1 = null;//�������������� �йر� ������
	private UFDouble ndef2 = null;//�����������뷽�йر� ������
	private UFDouble ndef3 = null;
	
	
	public void validation() throws ValidationException{
		if(PuPubVO.getString_TrimZeroLenAsNull(getCoutcorpid())==null)
			throw new ValidationException("������˾����Ϊ��");
		if(PuPubVO.getString_TrimZeroLenAsNull(getCincorpid())==null)
			throw new ValidationException("���빫˾����Ϊ��");
		if(PuPubVO.getString_TrimZeroLenAsNull(getCoutcalbodyid())==null)
			throw new ValidationException("������֯����Ϊ��");
		if(PuPubVO.getString_TrimZeroLenAsNull(getCincalbodyid())==null)
			throw new ValidationException("������֯����Ϊ��");
		if(PuPubVO.getString_TrimZeroLenAsNull(getCinvbasid())==null)
			throw new ValidationException("���Ϊ��");
		if(PuPubVO.getString_TrimZeroLenAsNull(getCoutinvid())==null)
			throw new ValidationException("�����������Ϊ��");
		if(PuPubVO.getString_TrimZeroLenAsNull(getCininvid())==null)
			throw new ValidationException("��������ʾΪ��");
//		if(PuPubVO.getString_TrimZeroLenAsNull(getCoutcorpid())==null)
//			throw new ValidationException("");
//		if(PuPubVO.getString_TrimZeroLenAsNull(getCoutcorpid())==null)
//			throw new ValidationException("");
	}
	
	public UFDouble getNallfund() {
		return nallfund;
	}

	public void setNallfund(UFDouble nallfund) {
		this.nallfund = nallfund;
	}

	public UFDouble getNallmny() {
		return nallmny;
	}

	public void setNallmny(UFDouble nallmny) {
		this.nallmny = nallmny;
	}

	public String getCoutcorpid() {
		return coutcorpid;
	}

	public void setCoutcorpid(String coutcorpid) {
		this.coutcorpid = coutcorpid;
	}

	public String getCincorpid() {
		return cincorpid;
	}

	public void setCincorpid(String cincorpid) {
		this.cincorpid = cincorpid;
	}

	public String getCoutcalbodyid() {
		return coutcalbodyid;
	}

	public void setCoutcalbodyid(String coutcalbodyid) {
		this.coutcalbodyid = coutcalbodyid;
	}

	public String getCincalbodyid() {
		return cincalbodyid;
	}

	public void setCincalbodyid(String cincalbodyid) {
		this.cincalbodyid = cincalbodyid;
	}

	public String getCoutwarehouseid() {
		return coutwarehouseid;
	}

	public void setCoutwarehouseid(String coutwarehouseid) {
		this.coutwarehouseid = coutwarehouseid;
	}

	public String getCinwarehouseid() {
		return cinwarehouseid;
	}

	public void setCinwarehouseid(String cinwarehouseid) {
		this.cinwarehouseid = cinwarehouseid;
	}

	public String getCinvbasid() {
		return cinvbasid;
	}

	public void setCinvbasid(String cinvbasid) {
		this.cinvbasid = cinvbasid;
	}

	

	public String getCoutinvid() {
		return coutinvid;
	}

	public void setCoutinvid(String coutinvid) {
		this.coutinvid = coutinvid;
	}

	public String getCininvid() {
		return cininvid;
	}

	public void setCininvid(String cininvid) {
		this.cininvid = cininvid;
	}

	public String getCbatchid() {
		return cbatchid;
	}

	public void setCbatchid(String cbatchid) {
		this.cbatchid = cbatchid;
	}

	public UFDate getDlogdate() {
		return dlogdate;
	}

	public void setDlogdate(UFDate dlogdate) {
		this.dlogdate = dlogdate;
	}

	public UFDouble getNoutonhand() {
		return noutonhand;
	}

	public void setNoutonhand(UFDouble noutonhand) {
		this.noutonhand = noutonhand;
	}

	public UFDouble getNinonhand() {
		return ninonhand;
	}

	public void setNinonhand(UFDouble ninonhand) {
		this.ninonhand = ninonhand;
	}

	public UFDouble getNoutallnum() {
		return noutallnum;
	}

	public void setNoutallnum(UFDouble noutallnum) {
		this.noutallnum = noutallnum;
	}

	public UFDouble getNinallnum() {
		return ninallnum;
	}

	public void setNinallnum(UFDouble ninallnum) {
		this.ninallnum = ninallnum;
	}

	public UFDouble getNoutnum() {
		return noutnum;
	}

	public void setNoutnum(UFDouble noutnum) {
		this.noutnum = noutnum;
	}

	public UFDouble getNinnum() {
		return ninnum;
	}

	public void setNinnum(UFDouble ninnum) {
		this.ninnum = ninnum;
	}

	public UFDouble getNfund() {
		return nfund;
	}

	public void setNfund(UFDouble nfund) {
		this.nfund = nfund;
	}

	public UFDouble getNmny() {
		return nmny;
	}

	public void setNmny(UFDouble nmny) {
		this.nmny = nmny;
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
