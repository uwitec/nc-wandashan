package nc.vo.zb.price.grade;

import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zb.bill.deal.DealInvPriceBVO;
import nc.vo.zb.bill.deal.DealVendorBillVO;
import nc.vo.zb.parmset.ParamSetVO;
/**
 * 
 * @author zhf  供应商报价分明细子表
 *
 */
public class PriceGradeVO extends SuperVO {
	
	private String cbiddingid;//标书id
	private String cvendorid;//供应商管理id
	private String cvendorbasid;//供应商基本id
	private String cinvbasid;//品种基本id
	private String cinvmanid;//品种管理id
	private UFDouble ngrade;//报价分
	private UFDouble nmaxgrade;//最高报价 报价分 611 算法调整后新增
	private UFDouble nmingrade;//最低报价报价分
	private UFDouble nadjgrade;//报价分调整值
	private String cpricegradeid;
	private String coparator;
	private UFDate dmakedate;
	private String cmodify;
	private UFDate dmodifydate;
	
	public UFDouble getNmaxgrade() {
		return nmaxgrade;
	}

	public void setNmaxgrade(UFDouble nmaxgrade) {
		this.nmaxgrade = nmaxgrade;
	}

	public UFDouble getNmingrade() {
		return nmingrade;
	}

	public void setNmingrade(UFDouble nmingrade) {
		this.nmingrade = nmingrade;
	}
	
	public String getCoparator() {
		return coparator;
	}

	public void setCoparator(String coparator) {
		this.coparator = coparator;
	}

	public UFDate getDmakedate() {
		return dmakedate;
	}

	public void setDmakedate(UFDate dmakedate) {
		this.dmakedate = dmakedate;
	}

	public String getCmodify() {
		return cmodify;
	}

	public void setCmodify(String cmodify) {
		this.cmodify = cmodify;
	}

	public UFDate getDmodifydate() {
		return dmodifydate;
	}

	public void setDmodifydate(UFDate dmodifydate) {
		this.dmodifydate = dmodifydate;
	}

	public String getCvendorbasid() {
		return cvendorbasid;
	}

	public void setCvendorbasid(String cvendorbasid) {
		this.cvendorbasid = cvendorbasid;
	}
	public String getCbiddingid() {
		return cbiddingid;
	}

	public void setCbiddingid(String cbiddingid) {
		this.cbiddingid = cbiddingid;
	}

	public String getCvendorid() {
		return cvendorid;
	}

	public void setCvendorid(String cvendorid) {
		this.cvendorid = cvendorid;
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

	public UFDouble getNgrade() {
		return ngrade;
	}

	public void setNgrade(UFDouble ngrade) {
		this.ngrade = ngrade;
	}

	public UFDouble getNadjgrade() {
		return nadjgrade;
	}

	public void setNadjgrade(UFDouble nadjgrade) {
		this.nadjgrade = nadjgrade;
	}

	public String getCpricegradeid() {
		return cpricegradeid;
	}

	public void setCpricegradeid(String cpricegradeid) {
		this.cpricegradeid = cpricegradeid;
	}

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "cpricegradeid";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "zb_pricegrade";
	}
	
	//报价分多次保存时   更新字段
	public static String[] update_fieldnames =new String[]{"ngrade","nadjgrade","cmodify","dmodifydate","nmaxgrade","nmingrade"};
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）计算报价分前得数据校验
	 * 2011-5-25下午07:30:11
	 * @param bills
	 * @param nmaxgrade
	 * @throws ValidationException
	 */
	public static void validateDataOnCol(DealVendorBillVO[] bills,ParamSetVO para) throws ValidationException{
		if(bills == null || bills.length ==0)
			throw new ValidationException("数据为空");
		if(PuPubVO.getUFDouble_NullAsZero(para.getNmaxquotatpoints()).equals(UFDouble.ZERO_DBL))
			throw new ValidationException("供应商最高资质分设置异常");
		
		if(para.getNquotationlower()==null)
			throw new ValidationException("招标参数【合理报价下限比例】未设置");
		UFDouble dd = PuPubVO.getUFDouble_NullAsZero(para.getNquotationlower());
		if(dd.doubleValue()<0||dd.doubleValue()>1){
			throw new ValidationException("招标参数【合理报价下限比例】值设置错误");
		}
		if(para.getNquotationscoring()==null)
			throw new ValidationException("招标参数【标底价以下报价得分系数】未设置");
		if(para.getReserve8()==null)
			throw new ValidationException("招标参数【标底价以上报价得分系数】未设置");
		
//		UFDouble tmp = null;
		DealInvPriceBVO[] bodys = null;
		for(DealVendorBillVO bill:bills){
//			tmp = bill.getHeader().getNquotatpoints();
//			if(PuPubVO.getUFDouble_NullAsZero(tmp).equals(UFDouble.ZERO_DBL)){
//				throw new ValidationException("存在报价分为空的供应商");
//			}
//			if(tmp.doubleValue()>nmaxgrade.doubleValue()){
//				throw new ValidationException("报价分超供应商报价分最大值设置");
//			}
			if(PuPubVO.getString_TrimZeroLenAsNull(bill.getHeader().getCbiddingid())==null)
				throw new ValidationException("数据异常，标书信息为空");
			if(PuPubVO.getString_TrimZeroLenAsNull(bill.getHeader().getCcustmanid())==null)
				throw new ValidationException("数据异常，供应商信息为空");
//			if(PuPubVO.getUFDouble_NullAsZero(body.getNprice()).equals(UFDouble.ZERO_DBL))
//				throw new ValidationException("存货信息为空");
			bodys = bill.getBodys();
			for(DealInvPriceBVO body:bodys){
				if(PuPubVO.getString_TrimZeroLenAsNull(body.getCinvbasid())==null || PuPubVO.getString_TrimZeroLenAsNull(body.getCinvmanid())==null)
					throw new ValidationException("存货信息为空");
				if(PuPubVO.getUFDouble_NullAsZero(body.getNmarkprice()).equals(UFDouble.ZERO_DBL))
					throw new ValidationException("标底价为空或为0");
				if(PuPubVO.getUFDouble_NullAsZero(body.getNprice()).equals(UFDouble.ZERO_DBL))
					throw new ValidationException("供应商最高报价为空或为0");		
				if(PuPubVO.getUFDouble_NullAsZero(body.getNllowerprice()).equals(UFDouble.ZERO_DBL))
					throw new ValidationException("供应商最低报价为空或为0");		
			}
		}
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）计算报价分前得数据校验
	 * 2011-5-25下午07:30:11
	 * @param bills
	 * @param nmaxgrade
	 * @throws ValidationException
	 */
	public static void validateDataOnSave(DealVendorBillVO[] bills,UFDouble nmaxgrade) throws ValidationException{
		for(DealVendorBillVO bill:bills){
			validateDataOnOk(bill, nmaxgrade);
		}
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）报价分维护节点确定时数据校验
	 * 2011-5-25下午07:31:31
	 * @param bill
	 * @param nmaxgrade
	 * @throws ValidationException
	 */
	public static void validateDataOnOk(DealVendorBillVO bill,UFDouble nmaxgrade) throws ValidationException{
		if(bill == null)
			throw new ValidationException("数据为空");
		UFDouble tmp = bill.getHeader().getNquotatpoints();
		if(PuPubVO.getUFDouble_NullAsZero(tmp).equals(UFDouble.ZERO_DBL)){
			throw new ValidationException("存在报价分为空的供应商");
		}
		if(tmp.doubleValue()>nmaxgrade.doubleValue()){
			throw new ValidationException("报价分超供应商报价分最大值设置");
		}

		DealInvPriceBVO[] bodys = bill.getBodys();
		if(bodys == null || bodys.length == 0)
			throw new ValidationException("品种数据为空");

//		UFDouble nallgrade = UFDouble.ZERO_DBL;
//		for(DealInvPriceBVO body: bodys){
//			nallgrade = nallgrade.add(PuPubVO.getUFDouble_NullAsZero(body.getNgrade()).add(PuPubVO.getUFDouble_NullAsZero(body.getNadjgrade())));
//		}
//		nallgrade = nallgrade.div(bodys.length);
//
//		if(!nallgrade.equals(tmp)){
//			if(nallgrade.doubleValue()>nmaxgrade.doubleValue()){
//				throw new ValidationException("供应商总报价分错误");
//			}
//			bill.getHeader().setNquotatpoints(nallgrade);
//		}
	}

}
