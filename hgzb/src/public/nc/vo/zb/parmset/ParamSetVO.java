/***************************************************************\
 *     The skeleton of this class is generated by an automatic *
 * code generator for NC product. It is based on Velocity.     *
\***************************************************************/
package nc.vo.zb.parmset;
	
import nc.vo.pub.*;
import nc.vo.pub.lang.*;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zb.pub.ZbPubTool;
	
/**
 * <b> 在此处简要描述此类的功能 </b>
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 * 创建日期:2011-05-20 12:41:27
 * @author Administrator
 * @version NCPrj 1.0
 */
@SuppressWarnings("serial")
public class ParamSetVO extends SuperVO {
	
	private UFDouble nmaxquotatpoints;//报价分最大值
	private String cparamsetid;//主键
	private UFBoolean fisbadquotation;//是否控制恶意报价
	private Integer ireferencelimits;//历史价参考范围
	private UFDouble nmaxqualipoints;//资质分最大值
	private UFDouble nquotationscoring;//恶意报价得分系数--标底价以下得分系数
	private Integer ibadquotanum;//恶意报价最大次数  如果没有值表示  一次都不允许恶意报价
	private UFDouble nquotationlower;//合理报价下限  (合理报价偏差率)  合理报价偏差比例=（标底价-恶意报价）/标底价  小数
	private UFDouble ndelaytime;//网络合理延迟时间
	private UFBoolean fiscoltotal;//是否按总分计算
	
	private String pk_corp;
	private UFDateTime ts;
	private UFDouble reserve10;
	private UFBoolean reserve15;
	private String reserve5;
	private String reserve2;
	private UFDouble reserve8;//报价得分系数  标底价以上得分系数
	private UFDate reserve12;
	private UFDouble reserve7;
	private UFDouble reserve6;
	private String reserve1;//用于区分招标年度
	private UFBoolean reserve16;
	private UFDate reserve11;
	private UFBoolean reserve14;
	private UFDate reserve13;
	private UFDouble reserve9;
	private String vmemo;
	private String reserve3;
	private Integer dr;
	private String reserve4;  
 
	public UFDouble getNmaxquotatpoints() {
		return nmaxquotatpoints;
	}

	public void setNmaxquotatpoints(UFDouble nmaxquotatpoints) {
		this.nmaxquotatpoints = nmaxquotatpoints;
	}

	public String getCparamsetid() {
		return cparamsetid;
	}

	public void setCparamsetid(String cparamsetid) {
		this.cparamsetid = cparamsetid;
	}

	public UFBoolean getFisbadquotation() {
		return fisbadquotation;
	}

	public void setFisbadquotation(UFBoolean fisbadquotation) {
		this.fisbadquotation = fisbadquotation;
	}

	public Integer getIreferencelimits() {
		return ireferencelimits;
	}

	public void setIreferencelimits(Integer ireferencelimits) {
		this.ireferencelimits = ireferencelimits;
	}

	public UFDouble getNmaxqualipoints() {
		return nmaxqualipoints;
	}

	public void setNmaxqualipoints(UFDouble nmaxqualipoints) {
		this.nmaxqualipoints = nmaxqualipoints;
	}

	public UFDouble getNquotationscoring() {
		return nquotationscoring;
	}

	public void setNquotationscoring(UFDouble nquotationscoring) {
		this.nquotationscoring = nquotationscoring;
	}

	public Integer getIbadquotanum() {
		return ibadquotanum;
	}

	public void setIbadquotanum(Integer ibadquotanum) {
		this.ibadquotanum = ibadquotanum;
	}

	public UFDouble getNquotationlower() {
		return nquotationlower;
	}

	public void setNquotationlower(UFDouble nquotationlower) {
		this.nquotationlower = nquotationlower;
	}

	public UFDouble getNdelaytime() {
		return ndelaytime;
	}

	public void setNdelaytime(UFDouble ndelaytime) {
		this.ndelaytime = ndelaytime;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public UFDateTime getTs() {
		return ts;
	}

	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	public UFDouble getReserve10() {
		return reserve10;
	}

	public void setReserve10(UFDouble reserve10) {
		this.reserve10 = reserve10;
	}

	public UFBoolean getReserve15() {
		return reserve15;
	}

	public void setReserve15(UFBoolean reserve15) {
		this.reserve15 = reserve15;
	}

	public String getReserve5() {
		return reserve5;
	}

	public void setReserve5(String reserve5) {
		this.reserve5 = reserve5;
	}

	public String getReserve2() {
		return reserve2;
	}

	public void setReserve2(String reserve2) {
		this.reserve2 = reserve2;
	}

	public UFDouble getReserve8() {
		return reserve8;
	}

	public void setReserve8(UFDouble reserve8) {
		this.reserve8 = reserve8;
	}

	public UFDate getReserve12() {
		return reserve12;
	}

	public void setReserve12(UFDate reserve12) {
		this.reserve12 = reserve12;
	}

	public UFDouble getReserve7() {
		return reserve7;
	}

	public void setReserve7(UFDouble reserve7) {
		this.reserve7 = reserve7;
	}

	public UFDouble getReserve6() {
		return reserve6;
	}

	public void setReserve6(UFDouble reserve6) {
		this.reserve6 = reserve6;
	}

	public String getReserve1() {
		return reserve1;
	}

	public void setReserve1(String reserve1) {
		this.reserve1 = reserve1;
	}

	public UFBoolean getReserve16() {
		return reserve16;
	}

	public void setReserve16(UFBoolean reserve16) {
		this.reserve16 = reserve16;
	}

	public UFDate getReserve11() {
		return reserve11;
	}

	public void setReserve11(UFDate reserve11) {
		this.reserve11 = reserve11;
	}

	public UFBoolean getReserve14() {
		return reserve14;
	}

	public void setReserve14(UFBoolean reserve14) {
		this.reserve14 = reserve14;
	}

	public UFDate getReserve13() {
		return reserve13;
	}

	public void setReserve13(UFDate reserve13) {
		this.reserve13 = reserve13;
	}

	public UFDouble getReserve9() {
		return reserve9;
	}

	public void setReserve9(UFDouble reserve9) {
		this.reserve9 = reserve9;
	}

	public String getVmemo() {
		return vmemo;
	}

	public void setVmemo(String vmemo) {
		this.vmemo = vmemo;
	}

	public String getReserve3() {
		return reserve3;
	}

	public void setReserve3(String reserve3) {
		this.reserve3 = reserve3;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public String getReserve4() {
		return reserve4;
	}

	public void setReserve4(String reserve4) {
		this.reserve4 = reserve4;
	}

	/**
	  * <p>取得父VO主键字段.
	  * <p>
	  * 创建日期:2011-05-20 12:41:27
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	    return null;
	}   
    
	/**
	  * <p>取得表主键.
	  * <p>
	  * 创建日期:2011-05-20 12:41:27
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	  return "cparamsetid";
	}
    
	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:2011-05-20 12:41:27
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "zb_parameter_settings";
	}    
    
    /**
	  * 按照默认方式创建构造子.
	  *
	  * 创建日期:2011-05-20 12:41:27
	  */
     public ParamSetVO() {
		super();	
	}    
     
     public void validateOnSave() throws BusinessException{
 		if(getNquotationlower()==null)
 			throw new BusinessException("合理报价偏差率不能为空");
 		if(PuPubVO.getUFDouble_NullAsZero(getNquotationlower()).compareTo(UFDouble.ZERO_DBL)<0)
 			throw new BusinessException("合理报价偏差率不能小于零");
 		if(PuPubVO.getUFDouble_NullAsZero(getNquotationlower()).compareTo(UFDouble.ONE_DBL)>0)
 			throw new BusinessException("合理报价偏差率不能大于壹");
 	}

	public UFBoolean getFiscoltotal() {
		return fiscoltotal;
	}

	public void setFiscoltotal(UFBoolean fiscoltotal) {
		this.fiscoltotal = fiscoltotal;
	}
} 
