  /***************************************************************\
  * code generator for NC product. It is based on Velocity.     *
  \***************************************************************/
      	package nc.vo.wds.w80060606;
   	
	import java.util.ArrayList;
	import nc.vo.pub.*;
	import nc.vo.pub.lang.*;
	
/**
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b>
 *
 * <p>
 *     �ڴ˴���Ӵ����������Ϣ
 * </p>
 *
 * ��������:2010-7-10
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
	   * ����pk_customize3��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getPk_customize3() {
		 return pk_customize3;
	  }   
	  
     /**
	   * ����pk_customize3��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newPk_customize3 String
	   */
	public void setPk_customize3(String newPk_customize3) {
		
		pk_customize3 = newPk_customize3;
	 } 	  
       
        /**
	   * ����dr��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return Integer
	   */
	 public Integer getDr() {
		 return dr;
	  }   
	  
     /**
	   * ����dr��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newDr Integer
	   */
	public void setDr(Integer newDr) {
		
		dr = newDr;
	 } 	  
       
        /**
	   * ����cifb_driverphone1��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getCifb_driverphone1() {
		 return cifb_driverphone1;
	  }   
	  
     /**
	   * ����cifb_driverphone1��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newCifb_driverphone1 String
	   */
	public void setCifb_driverphone1(String newCifb_driverphone1) {
		
		cifb_driverphone1 = newCifb_driverphone1;
	 } 	  
       
        /**
	   * ����pk_measware��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getPk_measware() {
		 return pk_measware;
	  }   
	  
     /**
	   * ����pk_measware��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newPk_measware String
	   */
	public void setPk_measware(String newPk_measware) {
		
		pk_measware = newPk_measware;
	 } 	  
       
        /**
	   * ����cauditorid��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getCauditorid() {
		 return cauditorid;
	  }   
	  
     /**
	   * ����cauditorid��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newCauditorid String
	   */
	public void setCauditorid(String newCauditorid) {
		
		cauditorid = newCauditorid;
	 } 	  
       
        /**
	   * ����pwb_dbilldate��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return UFDate
	   */
	 public UFDate getPwb_dbilldate() {
		 return pwb_dbilldate;
	  }   
	  
     /**
	   * ����pwb_dbilldate��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newPwb_dbilldate UFDate
	   */
	public void setPwb_dbilldate(UFDate newPwb_dbilldate) {
		
		pwb_dbilldate = newPwb_dbilldate;
	 } 	  
       
        /**
	   * ����pwb_freplenishflag��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getPwb_freplenishflag() {
		 return pwb_freplenishflag;
	  }   
	  
     /**
	   * ����pwb_freplenishflag��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newPwb_freplenishflag String
	   */
	public void setPwb_freplenishflag(String newPwb_freplenishflag) {
		
		pwb_freplenishflag = newPwb_freplenishflag;
	 } 	  
       
        /**
	   * ����pk_customize1��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getPk_customize1() {
		 return pk_customize1;
	  }   
	  
     /**
	   * ����pk_customize1��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newPk_customize1 String
	   */
	public void setPk_customize1(String newPk_customize1) {
		
		pk_customize1 = newPk_customize1;
	 } 	  
       
        /**
	   * ����coperatorid��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getCoperatorid() {
		 return coperatorid;
	  }   
	  
     /**
	   * ����coperatorid��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newCoperatorid String
	   */
	public void setCoperatorid(String newCoperatorid) {
		
		coperatorid = newCoperatorid;
	 } 	  
       
        /**
	   * ����maxstock��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getMaxstock() {
		 return maxstock;
	  }   
	  
     /**
	   * ����maxstock��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newMaxstock String
	   */
	public void setMaxstock(String newMaxstock) {
		
		maxstock = newMaxstock;
	 } 	  
       
        /**
	   * ����cifb_drivername��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getCifb_drivername() {
		 return cifb_drivername;
	  }   
	  
     /**
	   * ����cifb_drivername��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newCifb_drivername String
	   */
	public void setCifb_drivername(String newCifb_drivername) {
		
		cifb_drivername = newCifb_drivername;
	 } 	  
       
        /**
	   * ����ts��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return UFDateTime
	   */
	 public UFTime getTs() {
		 return ts;
	  }   
	  
     /**
	   * ����ts��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newTs UFDateTime
	   */
	public void setTs(UFTime newTs) {
		
		ts = newTs;
	 } 	  
       
        /**
	   * ����iprintcount��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return Integer
	   */
	 public Integer getIprintcount() {
		 return iprintcount;
	  }   
	  
     /**
	   * ����iprintcount��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newIprintcount Integer
	   */
	public void setIprintcount(Integer newIprintcount) {
		
		iprintcount = newIprintcount;
	 } 	  
       
        /**
	   * ����pwb_comname��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getPwb_comname() {
		 return pwb_comname;
	  }   
	  
     /**
	   * ����pwb_comname��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newPwb_comname String
	   */
	public void setPwb_comname(String newPwb_comname) {
		
		pwb_comname = newPwb_comname;
	 } 	  
       
        /**
	   * ����clastmodedate��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return UFDate
	   */
	 public UFDate getClastmodedate() {
		 return clastmodedate;
	  }   
	  
     /**
	   * ����clastmodedate��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newClastmodedate UFDate
	   */
	public void setClastmodedate(UFDate newClastmodedate) {
		
		clastmodedate = newClastmodedate;
	 } 	  
       
        /**
	   * ����pwb_vbillcode��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getPwb_vbillcode() {
		 return pwb_vbillcode;
	  }   
	  
     /**
	   * ����pwb_vbillcode��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newPwb_vbillcode String
	   */
	public void setPwb_vbillcode(String newPwb_vbillcode) {
		
		pwb_vbillcode = newPwb_vbillcode;
	 } 	  
       
        /**
	   * ����pk_customize7��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getPk_customize7() {
		 return pk_customize7;
	  }   
	  
     /**
	   * ����pk_customize7��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newPk_customize7 String
	   */
	public void setPk_customize7(String newPk_customize7) {
		
		pk_customize7 = newPk_customize7;
	 } 	  
       
        /**
	   * ����pwb_customize2��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getPwb_customize2() {
		 return pwb_customize2;
	  }   
	  
     /**
	   * ����pwb_customize2��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newPwb_customize2 String
	   */
	public void setPwb_customize2(String newPwb_customize2) {
		
		pwb_customize2 = newPwb_customize2;
	 } 	  
       
        /**
	   * ����pwb_cothercorpid��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getPwb_cothercorpid() {
		 return pwb_cothercorpid;
	  }   
	  
     /**
	   * ����pwb_cothercorpid��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newPwb_cothercorpid String
	   */
	public void setPwb_cothercorpid(String newPwb_cothercorpid) {
		
		pwb_cothercorpid = newPwb_cothercorpid;
	 } 	  
       
        /**
	   * ����pwb_pk��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getPwb_pk() {
		 return pwb_pk;
	  }   
	  
     /**
	   * ����pwb_pk��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newPwb_pk String
	   */
	public void setPwb_pk(String newPwb_pk) {
		
		pwb_pk = newPwb_pk;
	 } 	  
       
        /**
	   * ����pwb_cbiztype��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getPwb_cbiztype() {
		 return pwb_cbiztype;
	  }   
	  
     /**
	   * ����pwb_cbiztype��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newPwb_cbiztype String
	   */
	public void setPwb_cbiztype(String newPwb_cbiztype) {
		
		pwb_cbiztype = newPwb_cbiztype;
	 } 	  
       
        /**
	   * ����safestock��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getSafestock() {
		 return safestock;
	  }   
	  
     /**
	   * ����safestock��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newSafestock String
	   */
	public void setSafestock(String newSafestock) {
		
		safestock = newSafestock;
	 } 	  
       
        /**
	   * ����pwb_customize1��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getPwb_customize1() {
		 return pwb_customize1;
	  }   
	  
     /**
	   * ����pwb_customize1��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newPwb_customize1 String
	   */
	public void setPwb_customize1(String newPwb_customize1) {
		
		pwb_customize1 = newPwb_customize1;
	 } 	  
       
        /**
	   * ����pwb_cwarehouseid��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getPwb_cwarehouseid() {
		 return pwb_cwarehouseid;
	  }   
	  
     /**
	   * ����pwb_cwarehouseid��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newPwb_cwarehouseid String
	   */
	public void setPwb_cwarehouseid(String newPwb_cwarehouseid) {
		
		pwb_cwarehouseid = newPwb_cwarehouseid;
	 } 	  
       
        /**
	   * ����pwb_cothercalbodyid��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getPwb_cothercalbodyid() {
		 return pwb_cothercalbodyid;
	  }   
	  
     /**
	   * ����pwb_cothercalbodyid��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newPwb_cothercalbodyid String
	   */
	public void setPwb_cothercalbodyid(String newPwb_cothercalbodyid) {
		
		pwb_cothercalbodyid = newPwb_cothercalbodyid;
	 } 	  
       
        /**
	   * ����pwb_cbizid��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getPwb_cbizid() {
		 return pwb_cbizid;
	  }   
	  
     /**
	   * ����pwb_cbizid��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newPwb_cbizid String
	   */
	public void setPwb_cbizid(String newPwb_cbizid) {
		
		pwb_cbizid = newPwb_cbizid;
	 } 	  
       
        /**
	   * ����pk_customize4��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getPk_customize4() {
		 return pk_customize4;
	  }   
	  
     /**
	   * ����pk_customize4��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newPk_customize4 String
	   */
	public void setPk_customize4(String newPk_customize4) {
		
		pk_customize4 = newPk_customize4;
	 } 	  
       
        /**
	   * ����pwb_cwhsmanagerid��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getPwb_cwhsmanagerid() {
		 return pwb_cwhsmanagerid;
	  }   
	  
     /**
	   * ����pwb_cwhsmanagerid��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newPwb_cwhsmanagerid String
	   */
	public void setPwb_cwhsmanagerid(String newPwb_cwhsmanagerid) {
		
		pwb_cwhsmanagerid = newPwb_cwhsmanagerid;
	 } 	  
       
        /**
	   * ����pwb_fbillflag��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getPwb_fbillflag() {
		 return pwb_fbillflag;
	  }   
	  
     /**
	   * ����pwb_fbillflag��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newPwb_fbillflag String
	   */
	public void setPwb_fbillflag(String newPwb_fbillflag) {
		
		pwb_fbillflag = newPwb_fbillflag;
	 } 	  
       
        /**
	   * ����pwb_customize7��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getPwb_customize7() {
		 return pwb_customize7;
	  }   
	  
     /**
	   * ����pwb_customize7��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newPwb_customize7 String
	   */
	public void setPwb_customize7(String newPwb_customize7) {
		
		pwb_customize7 = newPwb_customize7;
	 } 	  
       
        /**
	   * ����tc_pk��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getTc_pk() {
		 return tc_pk;
	  }   
	  
     /**
	   * ����tc_pk��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newTc_pk String
	   */
	public void setTc_pk(String newTc_pk) {
		
		tc_pk = newTc_pk;
	 } 	  
       
        /**
	   * ����pk_customize9��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getPk_customize9() {
		 return pk_customize9;
	  }   
	  
     /**
	   * ����pk_customize9��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newPk_customize9 String
	   */
	public void setPk_customize9(String newPk_customize9) {
		
		pk_customize9 = newPk_customize9;
	 } 	  
       
        /**
	   * ����pwb_cotherwhid��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getPwb_cotherwhid() {
		 return pwb_cotherwhid;
	  }   
	  
     /**
	   * ����pwb_cotherwhid��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newPwb_cotherwhid String
	   */
	public void setPwb_cotherwhid(String newPwb_cotherwhid) {
		
		pwb_cotherwhid = newPwb_cotherwhid;
	 } 	  
       
        /**
	   * ����cif_carnum��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getCif_carnum() {
		 return cif_carnum;
	  }   
	  
     /**
	   * ����cif_carnum��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newCif_carnum String
	   */
	public void setCif_carnum(String newCif_carnum) {
		
		cif_carnum = newCif_carnum;
	 } 	  
       
        /**
	   * ����pk_customize6��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getPk_customize6() {
		 return pk_customize6;
	  }   
	  
     /**
	   * ����pk_customize6��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newPk_customize6 String
	   */
	public void setPk_customize6(String newPk_customize6) {
		
		pk_customize6 = newPk_customize6;
	 } 	  
       
        /**
	   * ����pwb_customize8��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getPwb_customize8() {
		 return pwb_customize8;
	  }   
	  
     /**
	   * ����pwb_customize8��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newPwb_customize8 String
	   */
	public void setPwb_customize8(String newPwb_customize8) {
		
		pwb_customize8 = newPwb_customize8;
	 } 	  
       
        /**
	   * ����pwb_customize6��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getPwb_customize6() {
		 return pwb_customize6;
	  }   
	  
     /**
	   * ����pwb_customize6��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newPwb_customize6 String
	   */
	public void setPwb_customize6(String newPwb_customize6) {
		
		pwb_customize6 = newPwb_customize6;
	 } 	  
       
        /**
	   * ����pwb_calbody��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getPwb_calbody() {
		 return pwb_calbody;
	  }   
	  
     /**
	   * ����pwb_calbody��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newPwb_calbody String
	   */
	public void setPwb_calbody(String newPwb_calbody) {
		
		pwb_calbody = newPwb_calbody;
	 } 	  
       
        /**
	   * ����pwb_corp��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getPwb_corp() {
		 return pwb_corp;
	  }   
	  
     /**
	   * ����pwb_corp��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newPwb_corp String
	   */
	public void setPwb_corp(String newPwb_corp) {
		
		pwb_corp = newPwb_corp;
	 } 	  
       
        /**
	   * ����pwb_libdate��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getPwb_libdate() {
		 return pwb_libdate;
	  }   
	  
     /**
	   * ����pwb_libdate��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newPwb_libdate String
	   */
	public void setPwb_libdate(String newPwb_libdate) {
		
		pwb_libdate = newPwb_libdate;
	 } 	  
       
        /**
	   * ����clastmodiid��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getClastmodiid() {
		 return clastmodiid;
	  }   
	  
     /**
	   * ����clastmodiid��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newClastmodiid String
	   */
	public void setClastmodiid(String newClastmodiid) {
		
		clastmodiid = newClastmodiid;
	 } 	  
       
        /**
	   * ����pwb_customize5��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getPwb_customize5() {
		 return pwb_customize5;
	  }   
	  
     /**
	   * ����pwb_customize5��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newPwb_customize5 String
	   */
	public void setPwb_customize5(String newPwb_customize5) {
		
		pwb_customize5 = newPwb_customize5;
	 } 	  
       
        /**
	   * ����minstock��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getMinstock() {
		 return minstock;
	  }   
	  
     /**
	   * ����minstock��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newMinstock String
	   */
	public void setMinstock(String newMinstock) {
		
		minstock = newMinstock;
	 } 	  
       
        /**
	   * ����pwb_tranname��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getPwb_tranname() {
		 return pwb_tranname;
	  }   
	  
     /**
	   * ����pwb_tranname��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newPwb_tranname String
	   */
	public void setPwb_tranname(String newPwb_tranname) {
		
		pwb_tranname = newPwb_tranname;
	 } 	  
       
        /**
	   * ����pwb_cdptid��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getPwb_cdptid() {
		 return pwb_cdptid;
	  }   
	  
     /**
	   * ����pwb_cdptid��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newPwb_cdptid String
	   */
	public void setPwb_cdptid(String newPwb_cdptid) {
		
		pwb_cdptid = newPwb_cdptid;
	 } 	  
       
        /**
	   * ����pwb_customize3��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getPwb_customize3() {
		 return pwb_customize3;
	  }   
	  
     /**
	   * ����pwb_customize3��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newPwb_customize3 String
	   */
	public void setPwb_customize3(String newPwb_customize3) {
		
		pwb_customize3 = newPwb_customize3;
	 } 	  
       
        /**
	   * ����pwb_libname��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getPwb_libname() {
		 return pwb_libname;
	  }   
	  
     /**
	   * ����pwb_libname��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newPwb_libname String
	   */
	public void setPwb_libname(String newPwb_libname) {
		
		pwb_libname = newPwb_libname;
	 } 	  
       
        /**
	   * ����pwb_billcode��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getPwb_billcode() {
		 return pwb_billcode;
	  }   
	  
     /**
	   * ����pwb_billcode��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newPwb_billcode String
	   */
	public void setPwb_billcode(String newPwb_billcode) {
		
		pwb_billcode = newPwb_billcode;
	 } 	  
       
        /**
	   * ����cif_pk��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getCif_pk() {
		 return cif_pk;
	  }   
	  
     /**
	   * ����cif_pk��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newCif_pk String
	   */
	public void setCif_pk(String newCif_pk) {
		
		cif_pk = newCif_pk;
	 } 	  
       
        /**
	   * ����pk_customize8��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getPk_customize8() {
		 return pk_customize8;
	  }   
	  
     /**
	   * ����pk_customize8��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newPk_customize8 String
	   */
	public void setPk_customize8(String newPk_customize8) {
		
		pk_customize8 = newPk_customize8;
	 } 	  
       
        /**
	   * ����vnote��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getVnote() {
		 return vnote;
	  }   
	  
     /**
	   * ����vnote��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newVnote String
	   */
	public void setVnote(String newVnote) {
		
		vnote = newVnote;
	 } 	  
       
        /**
	   * ����pk_customize2��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getPk_customize2() {
		 return pk_customize2;
	  }   
	  
     /**
	   * ����pk_customize2��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newPk_customize2 String
	   */
	public void setPk_customize2(String newPk_customize2) {
		
		pk_customize2 = newPk_customize2;
	 } 	  
       
        /**
	   * ����pwb_customize9��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getPwb_customize9() {
		 return pwb_customize9;
	  }   
	  
     /**
	   * ����pwb_customize9��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newPwb_customize9 String
	   */
	public void setPwb_customize9(String newPwb_customize9) {
		
		pwb_customize9 = newPwb_customize9;
	 } 	  
       
        /**
	   * ����pwb_stordate��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return UFDate
	   */
	 public UFDate getPwb_stordate() {
		 return pwb_stordate;
	  }   
	  
     /**
	   * ����pwb_stordate��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newPwb_stordate UFDate
	   */
	public void setPwb_stordate(UFDate newPwb_stordate) {
		
		pwb_stordate = newPwb_stordate;
	 } 	  
       
        /**
	   * ����pwb_customize4��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getPwb_customize4() {
		 return pwb_customize4;
	  }   
	  
     /**
	   * ����pwb_customize4��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newPwb_customize4 String
	   */
	public void setPwb_customize4(String newPwb_customize4) {
		
		pwb_customize4 = newPwb_customize4;
	 } 	  
       
        /**
	   * ����cifb_pk��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getCifb_pk() {
		 return cifb_pk;
	  }   
	  
     /**
	   * ����cifb_pk��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newCifb_pk String
	   */
	public void setCifb_pk(String newCifb_pk) {
		
		cifb_pk = newCifb_pk;
	 } 	  
       
        /**
	   * ����pwb_storname��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getPwb_storname() {
		 return pwb_storname;
	  }   
	  
     /**
	   * ����pwb_storname��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newPwb_storname String
	   */
	public void setPwb_storname(String newPwb_storname) {
		
		pwb_storname = newPwb_storname;
	 } 	  
       
        /**
	   * ����pk_customize5��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getPk_customize5() {
		 return pk_customize5;
	  }   
	  
     /**
	   * ����pk_customize5��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newPk_customize5 String
	   */
	public void setPk_customize5(String newPk_customize5) {
		
		pk_customize5 = newPk_customize5;
	 } 	  
       
        /**
	   * ����pwb_fallocflag��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return String
	   */
	 public String getPwb_fallocflag() {
		 return pwb_fallocflag;
	  }   
	  
     /**
	   * ����pwb_fallocflag��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newPwb_fallocflag String
	   */
	public void setPwb_fallocflag(String newPwb_fallocflag) {
		
		pwb_fallocflag = newPwb_fallocflag;
	 } 	  
       
        /**
	   * ����copetadate��Getter����.
	   *
	   * ��������:2010-7-10
	   * @return UFDate
	   */
	 public UFDate getCopetadate() {
		 return copetadate;
	  }   
	  
     /**
	   * ����copetadate��Setter����.
	   *
	   * ��������:2010-7-10
	   * @param newCopetadate UFDate
	   */
	public void setCopetadate(UFDate newCopetadate) {
		
		copetadate = newCopetadate;
	 } 	  
       
       
    /**
	  * ��֤���������֮��������߼���ȷ��.
	  *
	  * ��������:2010-7-10
	  * @exception nc.vo.pub.ValidationException �����֤ʧ��,�׳�
	  * ValidationException,�Դ�����н���.
	 */
	 public void validate() throws ValidationException {
	
	 	ArrayList errFields = new ArrayList(); // errFields record those null

                                                      // fields that cannot be null.
       		  // ����Ƿ�Ϊ������յ��ֶθ��˿�ֵ,�������Ҫ�޸��������ʾ��Ϣ:
	
	   		if (pwb_pk == null) {
			errFields.add(new String("pwb_pk"));
			  }	
	   	
	    StringBuffer message = new StringBuffer();
		message.append("�����ֶβ���Ϊ��:");
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
	  * <p>ȡ�ø�VO�����ֶ�.
	  * <p>
	  * ��������:2010-7-10
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	  	 
	 	    return null;
	 	
	}   
    
    /**
	  * <p>ȡ�ñ�����.
	  * <p>
	  * ��������:2010-7-10
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	 	  return "pwb_pk";
	 	}
    
	/**
      * <p>���ر�����.
	  * <p>
	  * ��������:2010-7-10
	  * @return java.lang.String
	 */
	public java.lang.String getTableName() {
				
		return "tb_prodwaybill";
	}    
    
    /**
	  * ����Ĭ�Ϸ�ʽ����������.
	  *
	  * ��������:2010-7-10
	  */
	public TbProdwaybillVO() {
			
			   super();	
	  }    
    
            /**
	 * ʹ���������г�ʼ���Ĺ�����.
	 *
	 * ��������:2010-7-10
	 * @param newPwb_pk ����ֵ
	 */
	 public TbProdwaybillVO(String newPwb_pk) {
		
		// Ϊ�����ֶθ�ֵ:
		 pwb_pk = newPwb_pk;
	
    	}
    
     
     /**
	  * ���ض����ʶ,����Ψһ��λ����.
	  *
	  * ��������:2010-7-10
	  * @return String
	  */
	   public String getPrimaryKey() {
				
		 return pwb_pk;
	   
	   }

     /**
	  * ���ö����ʶ,����Ψһ��λ����.
	  *
	  * ��������:2010-7-10
	  * @param newPwb_pk  String    
	  */
	 public void setPrimaryKey(String newPwb_pk) {
				
				pwb_pk = newPwb_pk; 
				
	 } 
           
	  /**
       * ������ֵ�������ʾ����.
	   *
	   * ��������:2010-7-10
	   * @return java.lang.String ������ֵ�������ʾ����.
	   */
	 public String getEntityName() {
				
	   return "tb_prodwaybill"; 
				
	 } 
} 
