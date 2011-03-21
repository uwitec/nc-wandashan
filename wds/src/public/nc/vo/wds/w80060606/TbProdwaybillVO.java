  /***************************************************************\
  * code generator for NC product. It is based on Velocity.     *
  \***************************************************************/
      	package nc.vo.wds.w80060606;
   	
	import java.util.ArrayList;
	import nc.vo.pub.*;
	import nc.vo.pub.lang.*;
	
/**
 * <b> 在此处简要描述此类的功能 </b>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 * 创建日期:2010-7-10
 * @author ${vmObject.author}
 * @version Your Project 1.0
 */
     public class TbProdwaybillVO extends SuperVO {
           
             public String pk_customize3;
             public Integer dr;
             public String cifb_driverphone1;
             public String pk_measware;
             public String cauditorid;
             public UFDate pwb_dbilldate;
             public String pwb_freplenishflag;
             public String pk_customize1;
             public String coperatorid;
             public String maxstock;
             public String cifb_drivername;
             public UFTime ts;
             public Integer iprintcount;
             public String pwb_comname;
             public UFDate clastmodedate;
             public String pwb_vbillcode;
             public String pk_customize7;
             public String pwb_customize2;
             public String pwb_cothercorpid;
             public String pwb_pk;
             public String pwb_cbiztype;
             public String safestock;
             public String pwb_customize1;
             public String pwb_cwarehouseid;
             public String pwb_cothercalbodyid;
             public String pwb_cbizid;
             public String pk_customize4;
             public String pwb_cwhsmanagerid;
             public String pwb_fbillflag;
             public String pwb_customize7;
             public String tc_pk;
             public String pk_customize9;
             public String pwb_cotherwhid;
             public String cif_carnum;
             public String pk_customize6;
             public String pwb_customize8;
             public String pwb_customize6;
             public String pwb_calbody;
             public String pwb_corp;
             public String pwb_libdate;
             public String clastmodiid;
             public String pwb_customize5;
             public String minstock;
             public String pwb_tranname;
             public String pwb_cdptid;
             public String pwb_customize3;
             public String pwb_libname;
             public String pwb_billcode;
             public String cif_pk;
             public String pk_customize8;
             public String vnote;
             public String pk_customize2;
             public String pwb_customize9;
             public UFDate pwb_stordate;
             public String pwb_customize4;
             public String cifb_pk;
             public String pwb_storname;
             public String pk_customize5;
             public String pwb_fallocflag;
             public UFDate copetadate;
            
             public static final String  PK_CUSTOMIZE3="pk_customize3";   
             public static final String  DR="dr";   
             public static final String  CIFB_DRIVERPHONE1="cifb_driverphone1";   
             public static final String  PK_MEASWARE="pk_measware";   
             public static final String  CAUDITORID="cauditorid";   
             public static final String  PWB_DBILLDATE="pwb_dbilldate";   
             public static final String  PWB_FREPLENISHFLAG="pwb_freplenishflag";   
             public static final String  PK_CUSTOMIZE1="pk_customize1";   
             public static final String  COPERATORID="coperatorid";   
             public static final String  MAXSTOCK="maxstock";   
             public static final String  CIFB_DRIVERNAME="cifb_drivername";   
             public static final String  TS="ts";   
             public static final String  IPRINTCOUNT="iprintcount";   
             public static final String  PWB_COMNAME="pwb_comname";   
             public static final String  CLASTMODEDATE="clastmodedate";   
             public static final String  PWB_VBILLCODE="pwb_vbillcode";   
             public static final String  PK_CUSTOMIZE7="pk_customize7";   
             public static final String  PWB_CUSTOMIZE2="pwb_customize2";   
             public static final String  PWB_COTHERCORPID="pwb_cothercorpid";   
             public static final String  PWB_PK="pwb_pk";   
             public static final String  PWB_CBIZTYPE="pwb_cbiztype";   
             public static final String  SAFESTOCK="safestock";   
             public static final String  PWB_CUSTOMIZE1="pwb_customize1";   
             public static final String  PWB_CWAREHOUSEID="pwb_cwarehouseid";   
             public static final String  PWB_COTHERCALBODYID="pwb_cothercalbodyid";   
             public static final String  PWB_CBIZID="pwb_cbizid";   
             public static final String  PK_CUSTOMIZE4="pk_customize4";   
             public static final String  PWB_CWHSMANAGERID="pwb_cwhsmanagerid";   
             public static final String  PWB_FBILLFLAG="pwb_fbillflag";   
             public static final String  PWB_CUSTOMIZE7="pwb_customize7";   
             public static final String  TC_PK="tc_pk";   
             public static final String  PK_CUSTOMIZE9="pk_customize9";   
             public static final String  PWB_COTHERWHID="pwb_cotherwhid";   
             public static final String  CIF_CARNUM="cif_carnum";   
             public static final String  PK_CUSTOMIZE6="pk_customize6";   
             public static final String  PWB_CUSTOMIZE8="pwb_customize8";   
             public static final String  PWB_CUSTOMIZE6="pwb_customize6";   
             public static final String  PWB_CALBODY="pwb_calbody";   
             public static final String  PWB_CORP="pwb_corp";   
             public static final String  PWB_LIBDATE="pwb_libdate";   
             public static final String  CLASTMODIID="clastmodiid";   
             public static final String  PWB_CUSTOMIZE5="pwb_customize5";   
             public static final String  MINSTOCK="minstock";   
             public static final String  PWB_TRANNAME="pwb_tranname";   
             public static final String  PWB_CDPTID="pwb_cdptid";   
             public static final String  PWB_CUSTOMIZE3="pwb_customize3";   
             public static final String  PWB_LIBNAME="pwb_libname";   
             public static final String  PWB_BILLCODE="pwb_billcode";   
             public static final String  CIF_PK="cif_pk";   
             public static final String  PK_CUSTOMIZE8="pk_customize8";   
             public static final String  VNOTE="vnote";   
             public static final String  PK_CUSTOMIZE2="pk_customize2";   
             public static final String  PWB_CUSTOMIZE9="pwb_customize9";   
             public static final String  PWB_STORDATE="pwb_stordate";   
             public static final String  PWB_CUSTOMIZE4="pwb_customize4";   
             public static final String  CIFB_PK="cifb_pk";   
             public static final String  PWB_STORNAME="pwb_storname";   
             public static final String  PK_CUSTOMIZE5="pk_customize5";   
             public static final String  PWB_FALLOCFLAG="pwb_fallocflag";   
             public static final String  COPETADATE="copetadate";   
      
    
        /**
	   * 属性pk_customize3的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getPk_customize3() {
		 return pk_customize3;
	  }   
	  
     /**
	   * 属性pk_customize3的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newPk_customize3 String
	   */
	public void setPk_customize3(String newPk_customize3) {
		
		pk_customize3 = newPk_customize3;
	 } 	  
       
        /**
	   * 属性dr的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return Integer
	   */
	 public Integer getDr() {
		 return dr;
	  }   
	  
     /**
	   * 属性dr的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newDr Integer
	   */
	public void setDr(Integer newDr) {
		
		dr = newDr;
	 } 	  
       
        /**
	   * 属性cifb_driverphone1的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getCifb_driverphone1() {
		 return cifb_driverphone1;
	  }   
	  
     /**
	   * 属性cifb_driverphone1的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newCifb_driverphone1 String
	   */
	public void setCifb_driverphone1(String newCifb_driverphone1) {
		
		cifb_driverphone1 = newCifb_driverphone1;
	 } 	  
       
        /**
	   * 属性pk_measware的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getPk_measware() {
		 return pk_measware;
	  }   
	  
     /**
	   * 属性pk_measware的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newPk_measware String
	   */
	public void setPk_measware(String newPk_measware) {
		
		pk_measware = newPk_measware;
	 } 	  
       
        /**
	   * 属性cauditorid的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getCauditorid() {
		 return cauditorid;
	  }   
	  
     /**
	   * 属性cauditorid的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newCauditorid String
	   */
	public void setCauditorid(String newCauditorid) {
		
		cauditorid = newCauditorid;
	 } 	  
       
        /**
	   * 属性pwb_dbilldate的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return UFDate
	   */
	 public UFDate getPwb_dbilldate() {
		 return pwb_dbilldate;
	  }   
	  
     /**
	   * 属性pwb_dbilldate的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newPwb_dbilldate UFDate
	   */
	public void setPwb_dbilldate(UFDate newPwb_dbilldate) {
		
		pwb_dbilldate = newPwb_dbilldate;
	 } 	  
       
        /**
	   * 属性pwb_freplenishflag的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getPwb_freplenishflag() {
		 return pwb_freplenishflag;
	  }   
	  
     /**
	   * 属性pwb_freplenishflag的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newPwb_freplenishflag String
	   */
	public void setPwb_freplenishflag(String newPwb_freplenishflag) {
		
		pwb_freplenishflag = newPwb_freplenishflag;
	 } 	  
       
        /**
	   * 属性pk_customize1的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getPk_customize1() {
		 return pk_customize1;
	  }   
	  
     /**
	   * 属性pk_customize1的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newPk_customize1 String
	   */
	public void setPk_customize1(String newPk_customize1) {
		
		pk_customize1 = newPk_customize1;
	 } 	  
       
        /**
	   * 属性coperatorid的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getCoperatorid() {
		 return coperatorid;
	  }   
	  
     /**
	   * 属性coperatorid的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newCoperatorid String
	   */
	public void setCoperatorid(String newCoperatorid) {
		
		coperatorid = newCoperatorid;
	 } 	  
       
        /**
	   * 属性maxstock的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getMaxstock() {
		 return maxstock;
	  }   
	  
     /**
	   * 属性maxstock的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newMaxstock String
	   */
	public void setMaxstock(String newMaxstock) {
		
		maxstock = newMaxstock;
	 } 	  
       
        /**
	   * 属性cifb_drivername的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getCifb_drivername() {
		 return cifb_drivername;
	  }   
	  
     /**
	   * 属性cifb_drivername的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newCifb_drivername String
	   */
	public void setCifb_drivername(String newCifb_drivername) {
		
		cifb_drivername = newCifb_drivername;
	 } 	  
       
        /**
	   * 属性ts的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return UFDateTime
	   */
	 public UFTime getTs() {
		 return ts;
	  }   
	  
     /**
	   * 属性ts的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newTs UFDateTime
	   */
	public void setTs(UFTime newTs) {
		
		ts = newTs;
	 } 	  
       
        /**
	   * 属性iprintcount的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return Integer
	   */
	 public Integer getIprintcount() {
		 return iprintcount;
	  }   
	  
     /**
	   * 属性iprintcount的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newIprintcount Integer
	   */
	public void setIprintcount(Integer newIprintcount) {
		
		iprintcount = newIprintcount;
	 } 	  
       
        /**
	   * 属性pwb_comname的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getPwb_comname() {
		 return pwb_comname;
	  }   
	  
     /**
	   * 属性pwb_comname的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newPwb_comname String
	   */
	public void setPwb_comname(String newPwb_comname) {
		
		pwb_comname = newPwb_comname;
	 } 	  
       
        /**
	   * 属性clastmodedate的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return UFDate
	   */
	 public UFDate getClastmodedate() {
		 return clastmodedate;
	  }   
	  
     /**
	   * 属性clastmodedate的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newClastmodedate UFDate
	   */
	public void setClastmodedate(UFDate newClastmodedate) {
		
		clastmodedate = newClastmodedate;
	 } 	  
       
        /**
	   * 属性pwb_vbillcode的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getPwb_vbillcode() {
		 return pwb_vbillcode;
	  }   
	  
     /**
	   * 属性pwb_vbillcode的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newPwb_vbillcode String
	   */
	public void setPwb_vbillcode(String newPwb_vbillcode) {
		
		pwb_vbillcode = newPwb_vbillcode;
	 } 	  
       
        /**
	   * 属性pk_customize7的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getPk_customize7() {
		 return pk_customize7;
	  }   
	  
     /**
	   * 属性pk_customize7的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newPk_customize7 String
	   */
	public void setPk_customize7(String newPk_customize7) {
		
		pk_customize7 = newPk_customize7;
	 } 	  
       
        /**
	   * 属性pwb_customize2的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getPwb_customize2() {
		 return pwb_customize2;
	  }   
	  
     /**
	   * 属性pwb_customize2的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newPwb_customize2 String
	   */
	public void setPwb_customize2(String newPwb_customize2) {
		
		pwb_customize2 = newPwb_customize2;
	 } 	  
       
        /**
	   * 属性pwb_cothercorpid的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getPwb_cothercorpid() {
		 return pwb_cothercorpid;
	  }   
	  
     /**
	   * 属性pwb_cothercorpid的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newPwb_cothercorpid String
	   */
	public void setPwb_cothercorpid(String newPwb_cothercorpid) {
		
		pwb_cothercorpid = newPwb_cothercorpid;
	 } 	  
       
        /**
	   * 属性pwb_pk的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getPwb_pk() {
		 return pwb_pk;
	  }   
	  
     /**
	   * 属性pwb_pk的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newPwb_pk String
	   */
	public void setPwb_pk(String newPwb_pk) {
		
		pwb_pk = newPwb_pk;
	 } 	  
       
        /**
	   * 属性pwb_cbiztype的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getPwb_cbiztype() {
		 return pwb_cbiztype;
	  }   
	  
     /**
	   * 属性pwb_cbiztype的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newPwb_cbiztype String
	   */
	public void setPwb_cbiztype(String newPwb_cbiztype) {
		
		pwb_cbiztype = newPwb_cbiztype;
	 } 	  
       
        /**
	   * 属性safestock的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getSafestock() {
		 return safestock;
	  }   
	  
     /**
	   * 属性safestock的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newSafestock String
	   */
	public void setSafestock(String newSafestock) {
		
		safestock = newSafestock;
	 } 	  
       
        /**
	   * 属性pwb_customize1的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getPwb_customize1() {
		 return pwb_customize1;
	  }   
	  
     /**
	   * 属性pwb_customize1的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newPwb_customize1 String
	   */
	public void setPwb_customize1(String newPwb_customize1) {
		
		pwb_customize1 = newPwb_customize1;
	 } 	  
       
        /**
	   * 属性pwb_cwarehouseid的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getPwb_cwarehouseid() {
		 return pwb_cwarehouseid;
	  }   
	  
     /**
	   * 属性pwb_cwarehouseid的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newPwb_cwarehouseid String
	   */
	public void setPwb_cwarehouseid(String newPwb_cwarehouseid) {
		
		pwb_cwarehouseid = newPwb_cwarehouseid;
	 } 	  
       
        /**
	   * 属性pwb_cothercalbodyid的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getPwb_cothercalbodyid() {
		 return pwb_cothercalbodyid;
	  }   
	  
     /**
	   * 属性pwb_cothercalbodyid的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newPwb_cothercalbodyid String
	   */
	public void setPwb_cothercalbodyid(String newPwb_cothercalbodyid) {
		
		pwb_cothercalbodyid = newPwb_cothercalbodyid;
	 } 	  
       
        /**
	   * 属性pwb_cbizid的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getPwb_cbizid() {
		 return pwb_cbizid;
	  }   
	  
     /**
	   * 属性pwb_cbizid的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newPwb_cbizid String
	   */
	public void setPwb_cbizid(String newPwb_cbizid) {
		
		pwb_cbizid = newPwb_cbizid;
	 } 	  
       
        /**
	   * 属性pk_customize4的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getPk_customize4() {
		 return pk_customize4;
	  }   
	  
     /**
	   * 属性pk_customize4的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newPk_customize4 String
	   */
	public void setPk_customize4(String newPk_customize4) {
		
		pk_customize4 = newPk_customize4;
	 } 	  
       
        /**
	   * 属性pwb_cwhsmanagerid的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getPwb_cwhsmanagerid() {
		 return pwb_cwhsmanagerid;
	  }   
	  
     /**
	   * 属性pwb_cwhsmanagerid的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newPwb_cwhsmanagerid String
	   */
	public void setPwb_cwhsmanagerid(String newPwb_cwhsmanagerid) {
		
		pwb_cwhsmanagerid = newPwb_cwhsmanagerid;
	 } 	  
       
        /**
	   * 属性pwb_fbillflag的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getPwb_fbillflag() {
		 return pwb_fbillflag;
	  }   
	  
     /**
	   * 属性pwb_fbillflag的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newPwb_fbillflag String
	   */
	public void setPwb_fbillflag(String newPwb_fbillflag) {
		
		pwb_fbillflag = newPwb_fbillflag;
	 } 	  
       
        /**
	   * 属性pwb_customize7的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getPwb_customize7() {
		 return pwb_customize7;
	  }   
	  
     /**
	   * 属性pwb_customize7的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newPwb_customize7 String
	   */
	public void setPwb_customize7(String newPwb_customize7) {
		
		pwb_customize7 = newPwb_customize7;
	 } 	  
       
        /**
	   * 属性tc_pk的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getTc_pk() {
		 return tc_pk;
	  }   
	  
     /**
	   * 属性tc_pk的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newTc_pk String
	   */
	public void setTc_pk(String newTc_pk) {
		
		tc_pk = newTc_pk;
	 } 	  
       
        /**
	   * 属性pk_customize9的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getPk_customize9() {
		 return pk_customize9;
	  }   
	  
     /**
	   * 属性pk_customize9的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newPk_customize9 String
	   */
	public void setPk_customize9(String newPk_customize9) {
		
		pk_customize9 = newPk_customize9;
	 } 	  
       
        /**
	   * 属性pwb_cotherwhid的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getPwb_cotherwhid() {
		 return pwb_cotherwhid;
	  }   
	  
     /**
	   * 属性pwb_cotherwhid的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newPwb_cotherwhid String
	   */
	public void setPwb_cotherwhid(String newPwb_cotherwhid) {
		
		pwb_cotherwhid = newPwb_cotherwhid;
	 } 	  
       
        /**
	   * 属性cif_carnum的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getCif_carnum() {
		 return cif_carnum;
	  }   
	  
     /**
	   * 属性cif_carnum的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newCif_carnum String
	   */
	public void setCif_carnum(String newCif_carnum) {
		
		cif_carnum = newCif_carnum;
	 } 	  
       
        /**
	   * 属性pk_customize6的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getPk_customize6() {
		 return pk_customize6;
	  }   
	  
     /**
	   * 属性pk_customize6的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newPk_customize6 String
	   */
	public void setPk_customize6(String newPk_customize6) {
		
		pk_customize6 = newPk_customize6;
	 } 	  
       
        /**
	   * 属性pwb_customize8的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getPwb_customize8() {
		 return pwb_customize8;
	  }   
	  
     /**
	   * 属性pwb_customize8的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newPwb_customize8 String
	   */
	public void setPwb_customize8(String newPwb_customize8) {
		
		pwb_customize8 = newPwb_customize8;
	 } 	  
       
        /**
	   * 属性pwb_customize6的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getPwb_customize6() {
		 return pwb_customize6;
	  }   
	  
     /**
	   * 属性pwb_customize6的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newPwb_customize6 String
	   */
	public void setPwb_customize6(String newPwb_customize6) {
		
		pwb_customize6 = newPwb_customize6;
	 } 	  
       
        /**
	   * 属性pwb_calbody的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getPwb_calbody() {
		 return pwb_calbody;
	  }   
	  
     /**
	   * 属性pwb_calbody的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newPwb_calbody String
	   */
	public void setPwb_calbody(String newPwb_calbody) {
		
		pwb_calbody = newPwb_calbody;
	 } 	  
       
        /**
	   * 属性pwb_corp的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getPwb_corp() {
		 return pwb_corp;
	  }   
	  
     /**
	   * 属性pwb_corp的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newPwb_corp String
	   */
	public void setPwb_corp(String newPwb_corp) {
		
		pwb_corp = newPwb_corp;
	 } 	  
       
        /**
	   * 属性pwb_libdate的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getPwb_libdate() {
		 return pwb_libdate;
	  }   
	  
     /**
	   * 属性pwb_libdate的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newPwb_libdate String
	   */
	public void setPwb_libdate(String newPwb_libdate) {
		
		pwb_libdate = newPwb_libdate;
	 } 	  
       
        /**
	   * 属性clastmodiid的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getClastmodiid() {
		 return clastmodiid;
	  }   
	  
     /**
	   * 属性clastmodiid的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newClastmodiid String
	   */
	public void setClastmodiid(String newClastmodiid) {
		
		clastmodiid = newClastmodiid;
	 } 	  
       
        /**
	   * 属性pwb_customize5的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getPwb_customize5() {
		 return pwb_customize5;
	  }   
	  
     /**
	   * 属性pwb_customize5的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newPwb_customize5 String
	   */
	public void setPwb_customize5(String newPwb_customize5) {
		
		pwb_customize5 = newPwb_customize5;
	 } 	  
       
        /**
	   * 属性minstock的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getMinstock() {
		 return minstock;
	  }   
	  
     /**
	   * 属性minstock的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newMinstock String
	   */
	public void setMinstock(String newMinstock) {
		
		minstock = newMinstock;
	 } 	  
       
        /**
	   * 属性pwb_tranname的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getPwb_tranname() {
		 return pwb_tranname;
	  }   
	  
     /**
	   * 属性pwb_tranname的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newPwb_tranname String
	   */
	public void setPwb_tranname(String newPwb_tranname) {
		
		pwb_tranname = newPwb_tranname;
	 } 	  
       
        /**
	   * 属性pwb_cdptid的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getPwb_cdptid() {
		 return pwb_cdptid;
	  }   
	  
     /**
	   * 属性pwb_cdptid的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newPwb_cdptid String
	   */
	public void setPwb_cdptid(String newPwb_cdptid) {
		
		pwb_cdptid = newPwb_cdptid;
	 } 	  
       
        /**
	   * 属性pwb_customize3的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getPwb_customize3() {
		 return pwb_customize3;
	  }   
	  
     /**
	   * 属性pwb_customize3的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newPwb_customize3 String
	   */
	public void setPwb_customize3(String newPwb_customize3) {
		
		pwb_customize3 = newPwb_customize3;
	 } 	  
       
        /**
	   * 属性pwb_libname的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getPwb_libname() {
		 return pwb_libname;
	  }   
	  
     /**
	   * 属性pwb_libname的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newPwb_libname String
	   */
	public void setPwb_libname(String newPwb_libname) {
		
		pwb_libname = newPwb_libname;
	 } 	  
       
        /**
	   * 属性pwb_billcode的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getPwb_billcode() {
		 return pwb_billcode;
	  }   
	  
     /**
	   * 属性pwb_billcode的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newPwb_billcode String
	   */
	public void setPwb_billcode(String newPwb_billcode) {
		
		pwb_billcode = newPwb_billcode;
	 } 	  
       
        /**
	   * 属性cif_pk的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getCif_pk() {
		 return cif_pk;
	  }   
	  
     /**
	   * 属性cif_pk的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newCif_pk String
	   */
	public void setCif_pk(String newCif_pk) {
		
		cif_pk = newCif_pk;
	 } 	  
       
        /**
	   * 属性pk_customize8的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getPk_customize8() {
		 return pk_customize8;
	  }   
	  
     /**
	   * 属性pk_customize8的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newPk_customize8 String
	   */
	public void setPk_customize8(String newPk_customize8) {
		
		pk_customize8 = newPk_customize8;
	 } 	  
       
        /**
	   * 属性vnote的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getVnote() {
		 return vnote;
	  }   
	  
     /**
	   * 属性vnote的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newVnote String
	   */
	public void setVnote(String newVnote) {
		
		vnote = newVnote;
	 } 	  
       
        /**
	   * 属性pk_customize2的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getPk_customize2() {
		 return pk_customize2;
	  }   
	  
     /**
	   * 属性pk_customize2的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newPk_customize2 String
	   */
	public void setPk_customize2(String newPk_customize2) {
		
		pk_customize2 = newPk_customize2;
	 } 	  
       
        /**
	   * 属性pwb_customize9的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getPwb_customize9() {
		 return pwb_customize9;
	  }   
	  
     /**
	   * 属性pwb_customize9的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newPwb_customize9 String
	   */
	public void setPwb_customize9(String newPwb_customize9) {
		
		pwb_customize9 = newPwb_customize9;
	 } 	  
       
        /**
	   * 属性pwb_stordate的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return UFDate
	   */
	 public UFDate getPwb_stordate() {
		 return pwb_stordate;
	  }   
	  
     /**
	   * 属性pwb_stordate的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newPwb_stordate UFDate
	   */
	public void setPwb_stordate(UFDate newPwb_stordate) {
		
		pwb_stordate = newPwb_stordate;
	 } 	  
       
        /**
	   * 属性pwb_customize4的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getPwb_customize4() {
		 return pwb_customize4;
	  }   
	  
     /**
	   * 属性pwb_customize4的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newPwb_customize4 String
	   */
	public void setPwb_customize4(String newPwb_customize4) {
		
		pwb_customize4 = newPwb_customize4;
	 } 	  
       
        /**
	   * 属性cifb_pk的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getCifb_pk() {
		 return cifb_pk;
	  }   
	  
     /**
	   * 属性cifb_pk的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newCifb_pk String
	   */
	public void setCifb_pk(String newCifb_pk) {
		
		cifb_pk = newCifb_pk;
	 } 	  
       
        /**
	   * 属性pwb_storname的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getPwb_storname() {
		 return pwb_storname;
	  }   
	  
     /**
	   * 属性pwb_storname的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newPwb_storname String
	   */
	public void setPwb_storname(String newPwb_storname) {
		
		pwb_storname = newPwb_storname;
	 } 	  
       
        /**
	   * 属性pk_customize5的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getPk_customize5() {
		 return pk_customize5;
	  }   
	  
     /**
	   * 属性pk_customize5的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newPk_customize5 String
	   */
	public void setPk_customize5(String newPk_customize5) {
		
		pk_customize5 = newPk_customize5;
	 } 	  
       
        /**
	   * 属性pwb_fallocflag的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return String
	   */
	 public String getPwb_fallocflag() {
		 return pwb_fallocflag;
	  }   
	  
     /**
	   * 属性pwb_fallocflag的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newPwb_fallocflag String
	   */
	public void setPwb_fallocflag(String newPwb_fallocflag) {
		
		pwb_fallocflag = newPwb_fallocflag;
	 } 	  
       
        /**
	   * 属性copetadate的Getter方法.
	   *
	   * 创建日期:2010-7-10
	   * @return UFDate
	   */
	 public UFDate getCopetadate() {
		 return copetadate;
	  }   
	  
     /**
	   * 属性copetadate的Setter方法.
	   *
	   * 创建日期:2010-7-10
	   * @param newCopetadate UFDate
	   */
	public void setCopetadate(UFDate newCopetadate) {
		
		copetadate = newCopetadate;
	 } 	  
       
       
    /**
	  * 验证对象各属性之间的数据逻辑正确性.
	  *
	  * 创建日期:2010-7-10
	  * @exception nc.vo.pub.ValidationException 如果验证失败,抛出
	  * ValidationException,对错误进行解释.
	 */
	 public void validate() throws ValidationException {
	
	 	ArrayList errFields = new ArrayList(); // errFields record those null

                                                      // fields that cannot be null.
       		  // 检查是否为不允许空的字段赋了空值,你可能需要修改下面的提示信息:
	
	   		if (pwb_pk == null) {
			errFields.add(new String("pwb_pk"));
			  }	
	   	
	    StringBuffer message = new StringBuffer();
		message.append("下列字段不能为空:");
		if (errFields.size() > 0) {
		String[] temp = (String[]) errFields.toArray(new String[0]);
		message.append(temp[0]);
		for ( int i= 1; i < temp.length; i++ ) {
			message.append(",");
			message.append(temp[i]);
		}
		throw new NullFieldException(message.toString());
		}
	 }
			   
       
   	/**
	  * <p>取得父VO主键字段.
	  * <p>
	  * 创建日期:2010-7-10
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	  	 
	 	    return null;
	 	
	}   
    
    /**
	  * <p>取得表主键.
	  * <p>
	  * 创建日期:2010-7-10
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	 	  return "pwb_pk";
	 	}
    
	/**
      * <p>返回表名称.
	  * <p>
	  * 创建日期:2010-7-10
	  * @return java.lang.String
	 */
	public java.lang.String getTableName() {
				
		return "tb_prodwaybill";
	}    
    
    /**
	  * 按照默认方式创建构造子.
	  *
	  * 创建日期:2010-7-10
	  */
	public TbProdwaybillVO() {
			
			   super();	
	  }    
    
            /**
	 * 使用主键进行初始化的构造子.
	 *
	 * 创建日期:2010-7-10
	 * @param newPwb_pk 主键值
	 */
	 public TbProdwaybillVO(String newPwb_pk) {
		
		// 为主键字段赋值:
		 pwb_pk = newPwb_pk;
	
    	}
    
     
     /**
	  * 返回对象标识,用来唯一定位对象.
	  *
	  * 创建日期:2010-7-10
	  * @return String
	  */
	   public String getPrimaryKey() {
				
		 return pwb_pk;
	   
	   }

     /**
	  * 设置对象标识,用来唯一定位对象.
	  *
	  * 创建日期:2010-7-10
	  * @param newPwb_pk  String    
	  */
	 public void setPrimaryKey(String newPwb_pk) {
				
				pwb_pk = newPwb_pk; 
				
	 } 
           
	  /**
       * 返回数值对象的显示名称.
	   *
	   * 创建日期:2010-7-10
	   * @return java.lang.String 返回数值对象的显示名称.
	   */
	 public String getEntityName() {
				
	   return "tb_prodwaybill"; 
				
	 } 
} 
