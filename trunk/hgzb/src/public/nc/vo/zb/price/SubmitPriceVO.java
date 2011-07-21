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
 * 报价信息vo  网上报价  现场报价 报价单   数据实体vo
 *
 */
public class SubmitPriceVO extends SuperVO {
	private String csubmitpriceid;//id
	private String cbiddingid;//标书id
	private String cinvclid;//品种分类
	private String pk_corp;//操作公司
	private String cinvbasid;//品种基本id
	private String cinvmanid;//品种管理id
	private String cunitid;//主计量单位
	private String castunitid;//辅计量单位
	private UFDouble nnum = null;//招标主数量
	private UFDouble nasnum= null;//招标辅数量
	private String cvendorid;//供应商管理id
	private String ccircalnoid;//轮次阶段id  默认次数：第一次、第二次、第三次
	
	private Integer isubmittype;//报价类型 0web 1local 2手工录入 3恶意报价（只有网上招标有恶意报价）
	private UFDouble nprice = null;//报价
	private UFDouble nlastprice = null;//上轮报价
	private UFDouble nllowerprice = null;//上轮最低报价
	
	private UFDouble nmarkprice = null;//标底价  zhf 
	
//	zhf add  现场报价单需用  后续追加  不需要存入报价明细表内
	private UFDouble nplanprice;//计划价
	private UFDouble nmarketprice;//市价
	private UFDouble naverageprice;//历史平均价
	
//	zhf end
	private String coprator;
	private String tmaketime;
	private String cmodifyid;
	private String tmodifytime;
	
	private String vdef1;//来源于报价单时  存放报价单行id
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
	 * 属性nplanprice的Getter方法.
	 * 创建日期:2011-04-28 16:51:42
	 * @return UFDouble
	 */
	public UFDouble getNplanprice () {
		return nplanprice;
	}   
	/**
	 * 属性nplanprice的Setter方法.
	 * 创建日期:2011-04-28 16:51:42
	 * @param newNplanprice UFDouble
	 */
	public void setNplanprice (UFDouble newNplanprice ) {
	 	this.nplanprice = newNplanprice;
	} 	
	
	/**
	 * 属性nmarketprice的Getter方法.
	 * 创建日期:2011-04-28 16:51:42
	 * @return UFDouble
	 */
	public UFDouble getNmarketprice () {
		return nmarketprice;
	}   
	/**
	 * 属性nmarketprice的Setter方法.
	 * 创建日期:2011-04-28 16:51:42
	 * @param newNmarketprice UFDouble
	 */
	public void setNmarketprice (UFDouble newNmarketprice ) {
	 	this.nmarketprice = newNmarketprice;
	} 
	
	/**
	 * 属性naverageprice的Getter方法.
	 * 创建日期:2011-04-28 16:51:42
	 * @return UFDouble
	 */
	public UFDouble getNaverageprice () {
		return naverageprice;
	}   
	/**
	 * 属性naverageprice的Setter方法.
	 * 创建日期:2011-04-28 16:51:42
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
	 * @说明：（鹤岗矿业）计算合理报价下限
	 * 2011-6-11下午01:58:41
	 * @param nbiddingprice 标底价
	 * @param nrate  合理报价偏差系数
	 * @return
	 */
	public static UFDouble getMinPrice(UFDouble nbiddingprice,UFDouble nrate){
//		合理报价下限= 标底价*（1-合理报价偏差系数）
//		nbiddingprice = PuPubVO.getUFDouble_NullAsZero(
//				minPriceInfor.get(price.getCinvbasid()));
		UFDouble nminprice = UFDouble.ONE_DBL
				.sub(PuPubVO.getUFDouble_NullAsZero(nrate));
		nminprice = nbiddingprice.multiply(nminprice);
		return nminprice;
	}
	
	public void validationOnSubmit(boolean isinv,int isubtype) throws ValidationException{
//		if(PuPubVO.getString_TrimZeroLenAsNull(getCinvclid())==null)
//			throw new ValidationException("存货分类不能为空");
		if(isinv)
			if(PuPubVO.getString_TrimZeroLenAsNull(getCinvbasid())==null)
				throw new ValidationException("存货不能为空");
		if(PuPubVO.getString_TrimZeroLenAsNull(getCbiddingid())==null){
			throw new ValidationException("标书信息为空");
		}
		if(PuPubVO.getString_TrimZeroLenAsNull(getCvendorid())==null){
			throw new ValidationException("供应商为空");
		}
		if(isubtype == ZbPubConst.WEB_SUBMIT_PRICE && PuPubVO.getString_TrimZeroLenAsNull(getCcircalnoid())==null){
			throw new ValidationException("报价阶段为空");
		}
		if(PuPubVO.getUFDouble_NullAsZero(getNprice()).equals(UFDouble.ZERO_DBL))
			throw new ValidationException("存在为空或零报价的品种");
		if(PuPubVO.getUFDouble_NullAsZero(getNlastprice()).equals(UFDouble.ZERO_DBL))
			return;
//		本轮报价必须低于上轮的报价
		if(PuPubVO.getUFDouble_NullAsZero(getNlastprice()).doubleValue()<PuPubVO.getUFDouble_NullAsZero(getNprice()).doubleValue()){
			throw new ValidationException("本轮报价不能高于上轮报价");
		}
	}
	
	/**
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业）报价明细 2011-7-8下午06:04:54
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
