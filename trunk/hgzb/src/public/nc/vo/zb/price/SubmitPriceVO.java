package nc.vo.zb.price;

import java.util.HashSet;
import java.util.Set;

import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zb.pub.ZbPubConst;
import nc.vo.zb.query.ZbNmny.ZbNmnyVO;

/**
 * 
 * @author zhf
 * 20110424
 * ������Ϣvo  ���ϱ���  �ֳ����� ���۵�   ����ʵ��vo
 *
 */
public class SubmitPriceVO extends SuperVO {
	private String csubmitpriceid;//id
	private String cbiddingid;//����id
	private String cinvclid;//Ʒ�ַ���
	private String pk_corp;//������˾
	private String cinvbasid;//Ʒ�ֻ���id
	private String cinvmanid;//Ʒ�ֹ���id
	private String cunitid;//��������λ
	private String castunitid;//��������λ
	private UFDouble nnum = null;//�б�������
	private UFDouble nasnum= null;//�б긨����
	private String cvendorid;//��Ӧ�̹���id
	private String ccircalnoid;//�ִν׶�id  Ĭ�ϴ�������һ�Ρ��ڶ��Ρ�������
	
	private Integer isubmittype;//�������� 0web 1local 2�ֹ�¼�� 3���ⱨ�ۣ�ֻ�������б��ж��ⱨ�ۣ�
	private UFDouble nprice = null;//����
	private UFDouble nlastprice = null;//���ֱ���
	private UFDouble nllowerprice = null;//������ͱ���
	
	private UFDouble nmarkprice = null;//��׼�  zhf 
	
//	zhf add  �ֳ����۵�����  ����׷��  ����Ҫ���뱨����ϸ����
	private UFDouble nplanprice;//�ƻ���
	private UFDouble nmarketprice;//�м�
	private UFDouble naverageprice;//��ʷƽ����
	
//	zhf end
	private String coprator;
	private String tmaketime;
	private String cmodifyid;
	private String tmodifytime;
	
	private String vdef1;//��Դ�ڱ��۵�ʱ  ��ű��۵���id
	private String vdef2;
	private String vdef3;
	private String vdef4;
	private String vdef5;
	
	private UFDouble ndef1 = null;
	private UFDouble ndef2 = null;
	private UFDouble ndef3 = null;
	private UFDouble ndef4 = null;
	private UFDouble ndef5 = null;
	/**
	 * ����nplanprice��Getter����.
	 * ��������:2011-04-28 16:51:42
	 * @return UFDouble
	 */
	public UFDouble getNplanprice () {
		return nplanprice;
	}   
	/**
	 * ����nplanprice��Setter����.
	 * ��������:2011-04-28 16:51:42
	 * @param newNplanprice UFDouble
	 */
	public void setNplanprice (UFDouble newNplanprice ) {
	 	this.nplanprice = newNplanprice;
	} 	
	
	/**
	 * ����nmarketprice��Getter����.
	 * ��������:2011-04-28 16:51:42
	 * @return UFDouble
	 */
	public UFDouble getNmarketprice () {
		return nmarketprice;
	}   
	/**
	 * ����nmarketprice��Setter����.
	 * ��������:2011-04-28 16:51:42
	 * @param newNmarketprice UFDouble
	 */
	public void setNmarketprice (UFDouble newNmarketprice ) {
	 	this.nmarketprice = newNmarketprice;
	} 
	
	/**
	 * ����naverageprice��Getter����.
	 * ��������:2011-04-28 16:51:42
	 * @return UFDouble
	 */
	public UFDouble getNaverageprice () {
		return naverageprice;
	}   
	/**
	 * ����naverageprice��Setter����.
	 * ��������:2011-04-28 16:51:42
	 * @param newNaverageprice UFDouble
	 */
	public void setNaverageprice (UFDouble newNaverageprice ) {
	 	this.naverageprice = newNaverageprice;
	} 	  
	
	public UFDouble getNmarkprice() {
		return nmarkprice;
	}

	public void setNmarkprice(UFDouble nmarkprice) {
		this.nmarkprice = nmarkprice;
	}

	public String getInvID(UFBoolean isinv){
		return isinv.booleanValue()?getCinvmanid():getCinvclid();
	}

	public UFDouble getNlastprice() {
		return nlastprice;
	}

	public void setNlastprice(UFDouble nlastprice) {
		this.nlastprice = nlastprice;
	}

	public UFDouble getNllowerprice() {
		return nllowerprice;
	}

	public void setNllowerprice(UFDouble nllowerprice) {
		this.nllowerprice = nllowerprice;
	}

	public String getCbiddingid() {
		return cbiddingid;
	}

	public void setCbiddingid(String cbiddingid) {
		this.cbiddingid = cbiddingid;
	}

	public String getCinvclid() {
		return cinvclid;
	}

	public void setCinvclid(String cinvclid) {
		this.cinvclid = cinvclid;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public String getCinvbasid() {
		return cinvbasid;
	}

	public void setCinvbasid(String cinvbasid) {
		this.cinvbasid = cinvbasid;
	}

	public String getCinvmanid() {
		return cinvmanid;
	}

	public void setCinvmanid(String cinvmanid) {
		this.cinvmanid = cinvmanid;
	}

	public String getCunitid() {
		return cunitid;
	}

	public void setCunitid(String cunitid) {
		this.cunitid = cunitid;
	}

	public String getCastunitid() {
		return castunitid;
	}

	public void setCastunitid(String castunitid) {
		this.castunitid = castunitid;
	}

	public UFDouble getNnum() {
		return nnum;
	}

	public void setNnum(UFDouble nnum) {
		this.nnum = nnum;
	}

	public UFDouble getNasnum() {
		return nasnum;
	}

	public void setNasnum(UFDouble nasnum) {
		this.nasnum = nasnum;
	}

	public String getCvendorid() {
		return cvendorid;
	}

	public void setCvendorid(String cvendorid) {
		this.cvendorid = cvendorid;
	}

	public String getCcircalnoid() {
		return ccircalnoid;
	}

	public void setCcircalnoid(String ccircalnoid) {
		this.ccircalnoid = ccircalnoid;
	}

	public Integer getIsubmittype() {
		return isubmittype;
	}

	public void setIsubmittype(Integer isubmittype) {
		this.isubmittype = isubmittype;
	}

	public UFDouble getNprice() {
		return nprice;
	}

	public void setNprice(UFDouble nprice) {
		this.nprice = nprice;
	}

	public String getCoprator() {
		return coprator;
	}

	public void setCoprator(String coprator) {
		this.coprator = coprator;
	}

	public String getTmaketime() {
		return tmaketime;
	}

	public void setTmaketime(String tmaketime) {
		this.tmaketime = tmaketime;
	}

	public String getCmodifyid() {
		return cmodifyid;
	}

	public void setCmodifyid(String cmodifyid) {
		this.cmodifyid = cmodifyid;
	}

	public String getTmodifytime() {
		return tmodifytime;
	}

	public void setTmodifytime(String tmodifytime) {
		this.tmodifytime = tmodifytime;
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

	public UFDouble getNdef4() {
		return ndef4;
	}

	public void setNdef4(UFDouble ndef4) {
		this.ndef4 = ndef4;
	}

	public UFDouble getNdef5() {
		return ndef5;
	}

	public void setNdef5(UFDouble ndef5) {
		this.ndef5 = ndef5;
	}

	public String getCsubmitpriceid() {
		return csubmitpriceid;
	}

	public void setCsubmitpriceid(String csubmitpriceid) {
		this.csubmitpriceid = csubmitpriceid;
	}

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "csubmitpriceid";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "zb_submitprice";
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ���������������
	 * 2011-6-11����01:58:41
	 * @param nbiddingprice ��׼�
	 * @param nrate  ������ƫ��ϵ��
	 * @return
	 */
	public static UFDouble getMinPrice(UFDouble nbiddingprice,UFDouble nrate){
//		����������= ��׼�*��1-������ƫ��ϵ����
//		nbiddingprice = PuPubVO.getUFDouble_NullAsZero(
//				minPriceInfor.get(price.getCinvbasid()));
		UFDouble nminprice = UFDouble.ONE_DBL
				.sub(PuPubVO.getUFDouble_NullAsZero(nrate));
		nminprice = nbiddingprice.multiply(nminprice);
		return nminprice;
	}
	
	public void validationOnSubmit(boolean isinv,int isubtype) throws ValidationException{
//		if(PuPubVO.getString_TrimZeroLenAsNull(getCinvclid())==null)
//			throw new ValidationException("������಻��Ϊ��");
		if(isinv)
			if(PuPubVO.getString_TrimZeroLenAsNull(getCinvbasid())==null)
				throw new ValidationException("�������Ϊ��");
		if(PuPubVO.getString_TrimZeroLenAsNull(getCbiddingid())==null){
			throw new ValidationException("������ϢΪ��");
		}
		if(PuPubVO.getString_TrimZeroLenAsNull(getCvendorid())==null){
			throw new ValidationException("��Ӧ��Ϊ��");
		}
		if(isubtype == ZbPubConst.WEB_SUBMIT_PRICE && PuPubVO.getString_TrimZeroLenAsNull(getCcircalnoid())==null){
			throw new ValidationException("���۽׶�Ϊ��");
		}
		if(PuPubVO.getUFDouble_NullAsZero(getNprice()).equals(UFDouble.ZERO_DBL))
			throw new ValidationException("����Ϊ�ջ��㱨�۵�Ʒ��");
		if(PuPubVO.getUFDouble_NullAsZero(getNlastprice()).equals(UFDouble.ZERO_DBL))
			return;
//		���ֱ��۱���������ֵı���
		if(PuPubVO.getUFDouble_NullAsZero(getNlastprice()).doubleValue()<PuPubVO.getUFDouble_NullAsZero(getNprice()).doubleValue()){
			throw new ValidationException("���ֱ��۲��ܸ������ֱ���");
		}
	}
	
	/**
	 * 
	 * @author zhw
	 * @˵�������׸ڿ�ҵ��������ϸ 2011-7-8����06:04:54
	 */
	public static void sortSubmitPriceVO(SubmitPriceVO[] vos) {
		if (vos == null || vos.length == 0) 
			return ;
	//	VOUtil.sort(vos, vendor_sort,vendor_sort_rule,true);
		Set<String> ss = new HashSet<String>();
		Set<String> sss = new HashSet<String>();
		Set<String> s = new HashSet<String>();
		String key = null;
		String keys = null;
		String ke = null;
		for (SubmitPriceVO vo : vos) {
			key = vo.getCbiddingid();
			keys = vo.getCvendorid();
			ke = vo.getCinvmanid();
			if (ss.contains(key)) {
				String[] names = vo.getAttributeNames();
				if(sss.contains(keys)){
					if(s.contains(ke)){
						for (String name : names) {
							if (name.equalsIgnoreCase("cbiddingid")||name.equalsIgnoreCase("cvendorid")||name.equalsIgnoreCase("cinvbasid")||name.equalsIgnoreCase("cinvmanid"))
								 vo.setAttributeValue(name, null);
							  
						}
					}else{
						for (String name : names) {
							if (name.equalsIgnoreCase("cbiddingid")||name.equalsIgnoreCase("cvendorid"))
								 vo.setAttributeValue(name, null);
							  
						}
						s.add(ke);
					}
					
				}else{
					for (String name : names) {
						if (name.equalsIgnoreCase("cbiddingid")){
							 vo.setAttributeValue(name, null);
						}  
					}
					sss.add(keys);
					s.clear();
					s.add(ke);
				}
				
			} else{
				ss.add(key);
				s.clear();
				s.add(ke);
				sss.clear();
				sss.add(keys);
			}
		}
		
	}
}
