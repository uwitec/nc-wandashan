/***************************************************************\
 *     The skeleton of this class is generated by an automatic *
 * code generator for NC product. It is based on Velocity.     *
\***************************************************************/
package nc.vo.hg.pu.check.fund;
	
import nc.vo.pub.*;
import nc.vo.pub.lang.*;
	
/**
 * <b> 在此处简要描述此类的功能 </b>
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 * 创建日期:2011-11-01 13:39:47
 * @author Administrator
 * @version NCPrj 1.0
 */
@SuppressWarnings("serial")
public class FUNDSETVO extends SuperVO {
	private String pk_corp;//公司
	private UFDateTime ts;
	private String vdef3;
	private UFBoolean fcontrol;//是否控制
	private Integer ifundtype;
	private String vdef1;//客户
	private String cdeptid;//用作部门id
	private UFDouble nlockfund;//预扣
	private UFDouble nactfund;//实扣
	private String coperatorid;//录入人     
    private UFDate dmakedate;//录入日期
	private String  cmodifyman; //最后修改人       
	private UFDate  dmodifydate;//最后修改日期
	private Integer dr;
	private String pk_month;
	private String imonth;//月份
	private String vdef4;
	private String vdef2;
	private String pk_fundset;//主键
	private String vdef5;
	private UFDouble nfund;//资金
	private String pk_year;
	private String cyear;//年度
	public static final String PK_CORP = "pk_corp";
	public static final String VDEF3 = "vdef3";
	public static final String FCONTROL = "fcontrol";
	public static final String VDEF1 = "vdef1";
	public static final String IMONTH = "imonth";
	public static final String VDEF4 = "vdef4";
	public static final String VDEF2 = "vdef2";
	public static final String PK_FUNDSET = "pk_fundset";
	public static final String VDEF5 = "vdef5";
	public static final String NFUND = "nfund";
			
	
	public UFDouble getNactfund() {
		return nactfund;
	}
	public void setNactfund(UFDouble nactfund) {
		this.nactfund = nactfund;
	}
	public Integer getIfundtype() {
		return ifundtype;
	}
	public void setIfundtype(Integer ifundtype) {
		this.ifundtype = ifundtype;
	}
	public String getCdeptid() {
		return cdeptid;
	}
	public void setCdeptid(String cdeptid) {
		this.cdeptid = cdeptid;
	}

	public UFDouble getNlockfund() {
		return nlockfund;
	}
	public void setNlockfund(UFDouble nlockfund) {
		this.nlockfund = nlockfund;
	}
	/**
	 * 属性pk_corp的Getter方法.
	 * 创建日期:2011-11-01 13:39:47
	 * @return String
	 */
	public String getPk_corp () {
		return pk_corp;
	}   
	/**
	 * 属性pk_corp的Setter方法.
	 * 创建日期:2011-11-01 13:39:47
	 * @param newPk_corp String
	 */
	public void setPk_corp (String newPk_corp ) {
	 	this.pk_corp = newPk_corp;
	} 	  
	/**
	 * 属性ts的Getter方法.
	 * 创建日期:2011-11-01 13:39:47
	 * @return UFDateTime
	 */
	public UFDateTime getTs () {
		return ts;
	}   
	/**
	 * 属性ts的Setter方法.
	 * 创建日期:2011-11-01 13:39:47
	 * @param newTs UFDateTime
	 */
	public void setTs (UFDateTime newTs ) {
	 	this.ts = newTs;
	} 	  
	/**
	 * 属性vdef3的Getter方法.
	 * 创建日期:2011-11-01 13:39:47
	 * @return String
	 */
	public String getVdef3 () {
		return vdef3;
	}   
	/**
	 * 属性vdef3的Setter方法.
	 * 创建日期:2011-11-01 13:39:47
	 * @param newVdef3 String
	 */
	public void setVdef3 (String newVdef3 ) {
	 	this.vdef3 = newVdef3;
	} 	  
	/**
	 * 属性fcontrol的Getter方法.
	 * 创建日期:2011-11-01 13:39:47
	 * @return UFBoolean
	 */
	public UFBoolean getFcontrol () {
		return fcontrol;
	}   
	/**
	 * 属性fcontrol的Setter方法.
	 * 创建日期:2011-11-01 13:39:47
	 * @param newFcontrol UFBoolean
	 */
	public void setFcontrol (UFBoolean newFcontrol ) {
	 	this.fcontrol = newFcontrol;
	} 	  
	/**
	 * 属性vdef1的Getter方法.
	 * 创建日期:2011-11-01 13:39:47
	 * @return String
	 */
	public String getVdef1 () {
		return vdef1;
	}   
	/**
	 * 属性vdef1的Setter方法.
	 * 创建日期:2011-11-01 13:39:47
	 * @param newVdef1 String
	 */
	public void setVdef1 (String newVdef1 ) {
	 	this.vdef1 = newVdef1;
	} 	  
	/**
	 * 属性dr的Getter方法.
	 * 创建日期:2011-11-01 13:39:47
	 * @return UFDouble
	 */
	public Integer getDr () {
		return dr;
	}   
	/**
	 * 属性dr的Setter方法.
	 * 创建日期:2011-11-01 13:39:47
	 * @param newDr UFDouble
	 */
	public void setDr (Integer newDr ) {
	 	this.dr = newDr;
	} 	  
	/**
	 * 属性imonth的Getter方法.
	 * 创建日期:2011-11-01 13:39:47
	 * @return UFDouble
	 */
	public String getImonth () {
		return imonth;
	}   
	/**
	 * 属性imonth的Setter方法.
	 * 创建日期:2011-11-01 13:39:47
	 * @param newImonth UFDouble
	 */
	public void setImonth (String newImonth ) {
	 	this.imonth = newImonth;
	} 	  
	/**
	 * 属性vdef4的Getter方法.
	 * 创建日期:2011-11-01 13:39:47
	 * @return String
	 */
	public String getVdef4 () {
		return vdef4;
	}   
	/**
	 * 属性vdef4的Setter方法.
	 * 创建日期:2011-11-01 13:39:47
	 * @param newVdef4 String
	 */
	public void setVdef4 (String newVdef4 ) {
	 	this.vdef4 = newVdef4;
	} 	  
	/**
	 * 属性vdef2的Getter方法.
	 * 创建日期:2011-11-01 13:39:47
	 * @return String
	 */
	public String getVdef2 () {
		return vdef2;
	}   
	/**
	 * 属性vdef2的Setter方法.
	 * 创建日期:2011-11-01 13:39:47
	 * @param newVdef2 String
	 */
	public void setVdef2 (String newVdef2 ) {
	 	this.vdef2 = newVdef2;
	} 	  
	/**
	 * 属性pk_fundset的Getter方法.
	 * 创建日期:2011-11-01 13:39:47
	 * @return String
	 */
	public String getPk_fundset () {
		return pk_fundset;
	}   
	/**
	 * 属性pk_fundset的Setter方法.
	 * 创建日期:2011-11-01 13:39:47
	 * @param newPk_fundset String
	 */
	public void setPk_fundset (String newPk_fundset ) {
	 	this.pk_fundset = newPk_fundset;
	} 	  
	/**
	 * 属性vdef5的Getter方法.
	 * 创建日期:2011-11-01 13:39:47
	 * @return String
	 */
	public String getVdef5 () {
		return vdef5;
	}   
	/**
	 * 属性vdef5的Setter方法.
	 * 创建日期:2011-11-01 13:39:47
	 * @param newVdef5 String
	 */
	public void setVdef5 (String newVdef5 ) {
	 	this.vdef5 = newVdef5;
	} 	  
	/**
	 * 属性nfund的Getter方法.
	 * 创建日期:2011-11-01 13:39:47
	 * @return UFDouble
	 */
	public UFDouble getNfund () {
		return nfund;
	}   
	/**
	 * 属性nfund的Setter方法.
	 * 创建日期:2011-11-01 13:39:47
	 * @param newNfund UFDouble
	 */
	public void setNfund (UFDouble newNfund ) {
	 	this.nfund = newNfund;
	} 	  
 
	/**
	  * <p>取得父VO主键字段.
	  * <p>
	  * 创建日期:2011-11-01 13:39:47
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	    return null;
	}   
    
	/**
	  * <p>取得表主键.
	  * <p>
	  * 创建日期:2011-11-01 13:39:47
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	  return "pk_fundset";
	}
    
	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:2011-11-01 13:39:47
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "HG_FUNDSET";
	}    
    
    /**
	  * 按照默认方式创建构造子.
	  *
	  * 创建日期:2011-11-01 13:39:47
	  */
     public FUNDSETVO() {
		super();	
	}
	public String getCoperatorid() {
		return coperatorid;
	}
	public void setCoperatorid(String coperatorid) {
		this.coperatorid = coperatorid;
	}
	public UFDate getDmakedate() {
		return dmakedate;
	}
	public void setDmakedate(UFDate dmakedate) {
		this.dmakedate = dmakedate;
	}
	public String getCmodifyman() {
		return cmodifyman;
	}
	public void setCmodifyman(String cmodifyman) {
		this.cmodifyman = cmodifyman;
	}
	public UFDate getDmodifydate() {
		return dmodifydate;
	}
	public void setDmodifydate(UFDate dmodifydate) {
		this.dmodifydate = dmodifydate;
	}
	public String getCyear() {
		return cyear;
	}
	public void setCyear(String cyear) {
		this.cyear = cyear;
	}
	public String getPk_month() {
		return pk_month;
	}
	public void setPk_month(String pk_month) {
		this.pk_month = pk_month;
	}
	public String getPk_year() {
		return pk_year;
	}
	public void setPk_year(String pk_year) {
		this.pk_year = pk_year;
	}    
} 
