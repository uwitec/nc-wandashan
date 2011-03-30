package nc.vo.hg.pu.plan.deal;

import nc.ui.scm.util.ObjectUtils;
import nc.vo.hg.pu.plan.year.PlanYearBVO;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;

/**
 * 
 * @author zhf
 * �ƻ�����vo
 *
 */
public class PlanDealVO extends PlanYearBVO {
	private String vbillno;
	private String pk_billtype;
	private UFDate dbilldate;
	private String capplydeptid;
	private String capplypsnid;
	private String csupplydeptid;
	private String csupplycorpid;
	private String pk_corp;
	private String cyear;
	private String cmonth;
	private String caccperiodschemeid;
	private String cinvclassid;
	
	private String cplanprojectid;
	private UFDate dmakedate;
	private String vapproveid;
	private String voperatorid;
	private UFDate dapprovedate;
	private UFBoolean fislimited;
	
	private String invcode;//����ʹ��
	
	private String pk_plan_b;//�ƻ��ӱ�id	
	
	//zhf add ��������
	private UFDate csupplydate;
	
	
	
	public UFDate getCsupplydate() {
		return csupplydate;
	}
	public void setCsupplydate(UFDate csupplydate) {
		this.csupplydate = csupplydate;
	}
	public String getInvcode() {
		return invcode;
	}
	public void setInvcode(String invcode) {
		this.invcode = invcode;
	}
	public String getPk_plan_b() {
		return pk_plan_b;
	}
	public void setPk_plan_b(String pk_plan_b) {
		this.pk_plan_b = pk_plan_b;
	}
	public String getCplanprojectid() {
		return cplanprojectid;
	}
	public void setCplanprojectid(String cplanprojectid) {
		this.cplanprojectid = cplanprojectid;
	}
	public UFDate getDmakedate() {
		return dmakedate;
	}
	public void setDmakedate(UFDate dmakedate) {
		this.dmakedate = dmakedate;
	}
	public String getVapproveid() {
		return vapproveid;
	}
	public void setVapproveid(String vapproveid) {
		this.vapproveid = vapproveid;
	}
	public String getVoperatorid() {
		return voperatorid;
	}
	public void setVoperatorid(String voperatorid) {
		this.voperatorid = voperatorid;
	}
	public UFDate getDapprovedate() {
		return dapprovedate;
	}
	public void setDapprovedate(UFDate dapprovedate) {
		this.dapprovedate = dapprovedate;
	}
	/**
	 * ����cinvclassid��Getter����.
	 * ��������:2011-11-01 10:37:18
	 * @return String
	 */
	public String getCinvclassid () {
		return cinvclassid;
	}   
	/**
	 * ����cinvclassid��Setter����.
	 * ��������:2011-11-01 10:37:18
	 * @param newCinvclassid String
	 */
	public void setCinvclassid (String newCinvclassid ) {
	 	this.cinvclassid = newCinvclassid;
	} 
	public String getCaccperiodschemeid() {
		return caccperiodschemeid;
	}
	public void setCaccperiodschemeid(String caccperiodschemeid) {
		this.caccperiodschemeid = caccperiodschemeid;
	}
	public String getVbillno() {
		return vbillno;
	}
	public void setVbillno(String vbillno) {
		this.vbillno = vbillno;
	}
	public String getPk_billtype() {
		return pk_billtype;
	}
	public void setPk_billtype(String pk_billtype) {
		this.pk_billtype = pk_billtype;
	}
	public UFDate getDbilldate() {
		return dbilldate;
	}
	public void setDbilldate(UFDate dbilldate) {
		this.dbilldate = dbilldate;
	}
	public String getCapplydeptid() {
		return capplydeptid;
	}
	public void setCapplydeptid(String capplydeptid) {
		this.capplydeptid = capplydeptid;
	}
	public String getCapplypsnid() {
		return capplypsnid;
	}
	public void setCapplypsnid(String capplypsnid) {
		this.capplypsnid = capplypsnid;
	}
	public String getCsupplydeptid() {
		return csupplydeptid;
	}
	public void setCsupplydeptid(String csupplydeptid) {
		this.csupplydeptid = csupplydeptid;
	}
	public String getCsupplycorpid() {
		return csupplycorpid;
	}
	public void setCsupplycorpid(String csupplycorpid) {
		this.csupplycorpid = csupplycorpid;
	}
	public String getPk_corp() {
		return pk_corp;
	}
	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}
	public String getCyear() {
		return cyear;
	}
	public void setCyear(String cyear) {
		this.cyear = cyear;
	}
	public String getCmonth() {
		return cmonth;
	}
	public void setCmonth(String cmonth) {
		this.cmonth = cmonth;
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
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ����ƻ������������ݱ���ʱУ��
	 * 2011-1-26����12:36:00
	 * @throws ValidationException
	 */
	public void validata() throws ValidationException{

		//����������=12�·ݷ���
		if(PuPubVO.getString_TrimZeroLenAsNull(getPk_invbasdoc())==null||PuPubVO.getString_TrimZeroLenAsNull(getCinventoryid())==null){
			throw new ValidationException("�����ϢΪ��");
		}
		
		String[] names = HgPubTool.PLAN_NUMKEYS;
		for(String name:names){
			if(PuPubVO.getUFDouble_NullAsZero(getAttributeValue(name)).doubleValue()<UFDouble.ZERO_DBL.doubleValue()){
				throw new ValidationException("����С����������ֶ�");
			}
		}
		
		UFDouble nnum = PuPubVO.getUFDouble_NullAsZero(getNnetnum());
		if(nnum.doubleValue() == UFDouble.ZERO_DBL.doubleValue())
			throw new ValidationException("������������Ϊ��");
		UFDouble nprice = PuPubVO.getUFDouble_NullAsZero(getNprice());
		if(nprice.doubleValue() == UFDouble.ZERO_DBL.doubleValue())
			throw new ValidationException("����Ϊ��");

		UFDouble nmny = getNmny().add(UFDouble.ZERO_DBL, 8);
		UFDouble nmny2 = getNnum().multiply(nprice, 8);
		if(nmny.doubleValue() != nmny2.doubleValue()){
			throw new ValidationException("���ۡ���������� �������ݲ�һ��");
		}
	
		if(getPk_billtype().equalsIgnoreCase(HgPubConst.PLAN_YEAR_BILLTYPE)){
			nnum = PuPubVO.getUFDouble_NullAsZero(getNnetnum());
			
			names = HgPubTool.PLAN_MONTH_NUMKEYS;
			for(String name:names){
				if(PuPubVO.getUFDouble_NullAsZero(getAttributeValue(name)).doubleValue()<UFDouble.ZERO_DBL.doubleValue()){
					throw new ValidationException("����С������·ݷ���");
				}
			}
			if(!(nnum.doubleValue()==getAllMonthNum().doubleValue())){
				throw new ValidationException("�·��������꾻������������");
			}	
		}else{
	        names = HgPubTool.PLAN_NUMKEYS;
	        for(String name:names){
	        	if(PuPubVO.getUFDouble_NullAsZero(getAttributeValue(name)).doubleValue()<UFDouble.ZERO_DBL.doubleValue()){
					throw new ValidationException("����С���������");
				}
	        }
		}		
	}
	public UFBoolean getFislimited() {
		return fislimited;
	}
	public void setFislimited(UFBoolean fislimited) {
		this.fislimited = fislimited;
	}	
}
