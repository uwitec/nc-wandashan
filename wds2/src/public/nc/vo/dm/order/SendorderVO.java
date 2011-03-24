  /***************************************************************\
  *     The skeleton of this class is generated by an automatic *
  * code generator for NC product. It is based on Velocity.     *
  \***************************************************************/
      	package nc.vo.dm.order;
   	
	import java.util.ArrayList;

import nc.vo.pub.NullFieldException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
	
/**
 * <b> 在此处简要描述此类的功能 </b>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 * 创建日期:2011-3-24
 * @author author
 * @version Your Project 1.0
 */
     public class SendorderVO extends SuperVO {
           
             public String reserve5;
             //公司主键
             public String pk_corp;
             public UFDateTime ts;
             //
             public String csourcebillhid;
             public String vdef4;
             //源头单据号
             public String vfirstbillcode;
             // 来源单据类型
             public String csourcetype;
             //制单人
             public String voperatorid;
             //审批批语
             public String vapprovenote;
             //是否大包粉
             public UFBoolean fisbigflour;
             public String vemployeeid;
             public UFDate reserve12;
             public String reserve6;
             public UFBoolean reserve14;
             //结束时间
             public UFDate denddate;
             public UFDouble reserve10;
             
             public String pk_deptdoc;
             public String vdef3;
             public String pk_sendorder;
             //发货站
             public String pk_outwhouse;
             public String reserve1;
             public UFBoolean reserve16;
             public UFBoolean reserve15;
             public String vsourcerowno;
             //开始时间
             public UFDate dbegindate;
             public String reserve2;
             public String vdef10;
             public String cfirstbillbid;
             public String vdef5;
             public String cfirstbillhid;
             public String reserve4;
             //制单日期
             public UFDate dmakedate;
             public String vfirstrowno;
             public String vdef7;
             //单据类型
             public String pk_billtype;
             //单据状态
             public Integer vbillstatus;
             public String vdef2;
             public String vinaddress;
             public UFDate reserve11;
             public String vmemo;
             //业务类型
             public String pk_busitype;
             public String vsourcebillcode;
             public String vdef1;
             //联系人
             public String pk_receiveperson;
             public String reserve3;
             //
             public String csourcebillbid;
             //订单号
             public String vbillno;
             //单据日期
             public UFDate dbilldate;
             //收货站
             public String pk_inwhouse;
             public UFDate reserve13;
             public UFDouble reserve8;
             public UFDouble reserve9;
             public UFDate dfirstbilldate;
             public String vdef9;
             public String vdef8;
             //
             public UFDate dapprovedate;
             public String cfirsttype;
             //联系电话
             public String vtelphone;
             public String vapproveid;
             public String reserve7;
             public String vdef6;
             public Integer dr;
            
             public static final String  RESERVE5="reserve5";   
             public static final String  PK_CORP="pk_corp";   
             public static final String  TS="ts";   
             public static final String  CSOURCEBILLHID="csourcebillhid";   
             public static final String  VDEF4="vdef4";   
             public static final String  VFIRSTBILLCODE="vfirstbillcode";   
             public static final String  CSOURCETYPE="csourcetype";   
             public static final String  VOPERATORID="voperatorid";   
             public static final String  VAPPROVENOTE="vapprovenote";   
             public static final String  FISBIGFLOUR="fisbigflour";   
             public static final String  VEMPLOYEEID="vemployeeid";   
             public static final String  RESERVE12="reserve12";   
             public static final String  RESERVE6="reserve6";   
             public static final String  RESERVE14="reserve14";   
             public static final String  DENDDATE="denddate";   
             public static final String  RESERVE10="reserve10";   
             public static final String  PK_DEPTDOC="pk_deptdoc";   
             public static final String  VDEF3="vdef3";   
             public static final String  PK_SENDORDER="pk_sendorder";   
             public static final String  PK_OUTWHOUSE="pk_outwhouse";   
             public static final String  RESERVE1="reserve1";   
             public static final String  RESERVE16="reserve16";   
             public static final String  RESERVE15="reserve15";   
             public static final String  VSOURCEROWNO="vsourcerowno";   
             public static final String  DBEGINDATE="dbegindate";   
             public static final String  RESERVE2="reserve2";   
             public static final String  VDEF10="vdef10";   
             public static final String  CFIRSTBILLBID="cfirstbillbid";   
             public static final String  VDEF5="vdef5";   
             public static final String  CFIRSTBILLHID="cfirstbillhid";   
             public static final String  RESERVE4="reserve4";   
             public static final String  DMAKEDATE="dmakedate";   
             public static final String  VFIRSTROWNO="vfirstrowno";   
             public static final String  VDEF7="vdef7";   
             public static final String  PK_BILLTYPE="pk_billtype";   
             public static final String  VBILLSTATUS="vbillstatus";   
             public static final String  VDEF2="vdef2";   
             public static final String  VINADDRESS="vinaddress";   
             public static final String  RESERVE11="reserve11";   
             public static final String  VMEMO="vmemo";   
             public static final String  PK_BUSITYPE="pk_busitype";   
             public static final String  VSOURCEBILLCODE="vsourcebillcode";   
             public static final String  VDEF1="vdef1";   
             public static final String  PK_RECEIVEPERSON="pk_receiveperson";   
             public static final String  RESERVE3="reserve3";   
             public static final String  CSOURCEBILLBID="csourcebillbid";   
             public static final String  VBILLNO="vbillno";   
             public static final String  DBILLDATE="dbilldate";   
             public static final String  PK_INWHOUSE="pk_inwhouse";   
             public static final String  RESERVE13="reserve13";   
             public static final String  RESERVE8="reserve8";   
             public static final String  RESERVE9="reserve9";   
             public static final String  DFIRSTBILLDATE="dfirstbilldate";   
             public static final String  VDEF9="vdef9";   
             public static final String  VDEF8="vdef8";   
             public static final String  DAPPROVEDATE="dapprovedate";   
             public static final String  CFIRSTTYPE="cfirsttype";   
             public static final String  VTELPHONE="vtelphone";   
             public static final String  VAPPROVEID="vapproveid";   
             public static final String  RESERVE7="reserve7";   
             public static final String  VDEF6="vdef6";   
             public static final String  DR="dr";   
      
    
        /**
	   * 属性reserve5的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return String
	   */
	 public String getReserve5() {
		 return reserve5;
	  }   
	  
     /**
	   * 属性reserve5的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newReserve5 String
	   */
	public void setReserve5(String newReserve5) {
		
		reserve5 = newReserve5;
	 } 	  
       
        /**
	   * 属性pk_corp的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return String
	   */
	 public String getPk_corp() {
		 return pk_corp;
	  }   
	  
     /**
	   * 属性pk_corp的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newPk_corp String
	   */
	public void setPk_corp(String newPk_corp) {
		
		pk_corp = newPk_corp;
	 } 	  
       
       
        public UFDateTime getTs() {
		return ts;
	}

	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

		/**
	   * 属性csourcebillhid的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return String
	   */
	 public String getCsourcebillhid() {
		 return csourcebillhid;
	  }   
	  
     /**
	   * 属性csourcebillhid的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newCsourcebillhid String
	   */
	public void setCsourcebillhid(String newCsourcebillhid) {
		
		csourcebillhid = newCsourcebillhid;
	 } 	  
       
        /**
	   * 属性vdef4的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return String
	   */
	 public String getVdef4() {
		 return vdef4;
	  }   
	  
     /**
	   * 属性vdef4的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newVdef4 String
	   */
	public void setVdef4(String newVdef4) {
		
		vdef4 = newVdef4;
	 } 	  
       
        /**
	   * 属性vfirstbillcode的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return String
	   */
	 public String getVfirstbillcode() {
		 return vfirstbillcode;
	  }   
	  
     /**
	   * 属性vfirstbillcode的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newVfirstbillcode String
	   */
	public void setVfirstbillcode(String newVfirstbillcode) {
		
		vfirstbillcode = newVfirstbillcode;
	 } 	  
       
        /**
	   * 属性csourcetype的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return String
	   */
	 public String getCsourcetype() {
		 return csourcetype;
	  }   
	  
     /**
	   * 属性csourcetype的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newCsourcetype String
	   */
	public void setCsourcetype(String newCsourcetype) {
		
		csourcetype = newCsourcetype;
	 } 	  
       
        /**
	   * 属性voperatorid的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return String
	   */
	 public String getVoperatorid() {
		 return voperatorid;
	  }   
	  
     /**
	   * 属性voperatorid的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newVoperatorid String
	   */
	public void setVoperatorid(String newVoperatorid) {
		
		voperatorid = newVoperatorid;
	 } 	  
       
        /**
	   * 属性vapprovenote的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return String
	   */
	 public String getVapprovenote() {
		 return vapprovenote;
	  }   
	  
     /**
	   * 属性vapprovenote的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newVapprovenote String
	   */
	public void setVapprovenote(String newVapprovenote) {
		
		vapprovenote = newVapprovenote;
	 } 	  
       
        /**
	   * 属性fisbigflour的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return UFBoolean
	   */
	 public UFBoolean getFisbigflour() {
		 return fisbigflour;
	  }   
	  
     /**
	   * 属性fisbigflour的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newFisbigflour UFBoolean
	   */
	public void setFisbigflour(UFBoolean newFisbigflour) {
		
		fisbigflour = newFisbigflour;
	 } 	  
       
        /**
	   * 属性vemployeeid的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return String
	   */
	 public String getVemployeeid() {
		 return vemployeeid;
	  }   
	  
     /**
	   * 属性vemployeeid的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newVemployeeid String
	   */
	public void setVemployeeid(String newVemployeeid) {
		
		vemployeeid = newVemployeeid;
	 } 	  
       
        /**
	   * 属性reserve12的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return UFDate
	   */
	 public UFDate getReserve12() {
		 return reserve12;
	  }   
	  
     /**
	   * 属性reserve12的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newReserve12 UFDate
	   */
	public void setReserve12(UFDate newReserve12) {
		
		reserve12 = newReserve12;
	 } 	  
       
        /**
	   * 属性reserve6的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return String
	   */
	 public String getReserve6() {
		 return reserve6;
	  }   
	  
     /**
	   * 属性reserve6的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newReserve6 String
	   */
	public void setReserve6(String newReserve6) {
		
		reserve6 = newReserve6;
	 } 	  
       
        /**
	   * 属性reserve14的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return UFBoolean
	   */
	 public UFBoolean getReserve14() {
		 return reserve14;
	  }   
	  
     /**
	   * 属性reserve14的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newReserve14 UFBoolean
	   */
	public void setReserve14(UFBoolean newReserve14) {
		
		reserve14 = newReserve14;
	 } 	  
       
        /**
	   * 属性denddate的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return UFDate
	   */
	 public UFDate getDenddate() {
		 return denddate;
	  }   
	  
     /**
	   * 属性denddate的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newDenddate UFDate
	   */
	public void setDenddate(UFDate newDenddate) {
		
		denddate = newDenddate;
	 } 	  
       
        /**
	   * 属性reserve10的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return UFDouble
	   */
	 public UFDouble getReserve10() {
		 return reserve10;
	  }   
	  
     /**
	   * 属性reserve10的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newReserve10 UFDouble
	   */
	public void setReserve10(UFDouble newReserve10) {
		
		reserve10 = newReserve10;
	 } 	  
       
        /**
	   * 属性pk_deptdoc的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return String
	   */
	 public String getPk_deptdoc() {
		 return pk_deptdoc;
	  }   
	  
     /**
	   * 属性pk_deptdoc的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newPk_deptdoc String
	   */
	public void setPk_deptdoc(String newPk_deptdoc) {
		
		pk_deptdoc = newPk_deptdoc;
	 } 	  
       
        /**
	   * 属性vdef3的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return String
	   */
	 public String getVdef3() {
		 return vdef3;
	  }   
	  
     /**
	   * 属性vdef3的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newVdef3 String
	   */
	public void setVdef3(String newVdef3) {
		
		vdef3 = newVdef3;
	 } 	  
       
        /**
	   * 属性pk_sendorder的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return String
	   */
	 public String getPk_sendorder() {
		 return pk_sendorder;
	  }   
	  
     /**
	   * 属性pk_sendorder的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newPk_sendorder String
	   */
	public void setPk_sendorder(String newPk_sendorder) {
		
		pk_sendorder = newPk_sendorder;
	 } 	  
       
        /**
	   * 属性pk_outwhouse的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return String
	   */
	 public String getPk_outwhouse() {
		 return pk_outwhouse;
	  }   
	  
     /**
	   * 属性pk_outwhouse的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newPk_outwhouse String
	   */
	public void setPk_outwhouse(String newPk_outwhouse) {
		
		pk_outwhouse = newPk_outwhouse;
	 } 	  
       
        /**
	   * 属性reserve1的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return String
	   */
	 public String getReserve1() {
		 return reserve1;
	  }   
	  
     /**
	   * 属性reserve1的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newReserve1 String
	   */
	public void setReserve1(String newReserve1) {
		
		reserve1 = newReserve1;
	 } 	  
       
        /**
	   * 属性reserve16的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return UFBoolean
	   */
	 public UFBoolean getReserve16() {
		 return reserve16;
	  }   
	  
     /**
	   * 属性reserve16的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newReserve16 UFBoolean
	   */
	public void setReserve16(UFBoolean newReserve16) {
		
		reserve16 = newReserve16;
	 } 	  
       
        /**
	   * 属性reserve15的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return UFBoolean
	   */
	 public UFBoolean getReserve15() {
		 return reserve15;
	  }   
	  
     /**
	   * 属性reserve15的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newReserve15 UFBoolean
	   */
	public void setReserve15(UFBoolean newReserve15) {
		
		reserve15 = newReserve15;
	 } 	  
       
        /**
	   * 属性vsourcerowno的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return String
	   */
	 public String getVsourcerowno() {
		 return vsourcerowno;
	  }   
	  
     /**
	   * 属性vsourcerowno的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newVsourcerowno String
	   */
	public void setVsourcerowno(String newVsourcerowno) {
		
		vsourcerowno = newVsourcerowno;
	 } 	  
       
        /**
	   * 属性dbegindate的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return UFDate
	   */
	 public UFDate getDbegindate() {
		 return dbegindate;
	  }   
	  
     /**
	   * 属性dbegindate的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newDbegindate UFDate
	   */
	public void setDbegindate(UFDate newDbegindate) {
		
		dbegindate = newDbegindate;
	 } 	  
       
        /**
	   * 属性reserve2的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return String
	   */
	 public String getReserve2() {
		 return reserve2;
	  }   
	  
     /**
	   * 属性reserve2的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newReserve2 String
	   */
	public void setReserve2(String newReserve2) {
		
		reserve2 = newReserve2;
	 } 	  
       
        /**
	   * 属性vdef10的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return String
	   */
	 public String getVdef10() {
		 return vdef10;
	  }   
	  
     /**
	   * 属性vdef10的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newVdef10 String
	   */
	public void setVdef10(String newVdef10) {
		
		vdef10 = newVdef10;
	 } 	  
       
        /**
	   * 属性cfirstbillbid的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return String
	   */
	 public String getCfirstbillbid() {
		 return cfirstbillbid;
	  }   
	  
     /**
	   * 属性cfirstbillbid的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newCfirstbillbid String
	   */
	public void setCfirstbillbid(String newCfirstbillbid) {
		
		cfirstbillbid = newCfirstbillbid;
	 } 	  
       
        /**
	   * 属性vdef5的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return String
	   */
	 public String getVdef5() {
		 return vdef5;
	  }   
	  
     /**
	   * 属性vdef5的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newVdef5 String
	   */
	public void setVdef5(String newVdef5) {
		
		vdef5 = newVdef5;
	 } 	  
       
        /**
	   * 属性cfirstbillhid的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return String
	   */
	 public String getCfirstbillhid() {
		 return cfirstbillhid;
	  }   
	  
     /**
	   * 属性cfirstbillhid的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newCfirstbillhid String
	   */
	public void setCfirstbillhid(String newCfirstbillhid) {
		
		cfirstbillhid = newCfirstbillhid;
	 } 	  
       
        /**
	   * 属性reserve4的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return String
	   */
	 public String getReserve4() {
		 return reserve4;
	  }   
	  
     /**
	   * 属性reserve4的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newReserve4 String
	   */
	public void setReserve4(String newReserve4) {
		
		reserve4 = newReserve4;
	 } 	  
       
        /**
	   * 属性dmakedate的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return UFDate
	   */
	 public UFDate getDmakedate() {
		 return dmakedate;
	  }   
	  
     /**
	   * 属性dmakedate的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newDmakedate UFDate
	   */
	public void setDmakedate(UFDate newDmakedate) {
		
		dmakedate = newDmakedate;
	 } 	  
       
        /**
	   * 属性vfirstrowno的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return String
	   */
	 public String getVfirstrowno() {
		 return vfirstrowno;
	  }   
	  
     /**
	   * 属性vfirstrowno的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newVfirstrowno String
	   */
	public void setVfirstrowno(String newVfirstrowno) {
		
		vfirstrowno = newVfirstrowno;
	 } 	  
       
        /**
	   * 属性vdef7的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return String
	   */
	 public String getVdef7() {
		 return vdef7;
	  }   
	  
     /**
	   * 属性vdef7的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newVdef7 String
	   */
	public void setVdef7(String newVdef7) {
		
		vdef7 = newVdef7;
	 } 	  
       
        /**
	   * 属性pk_billtype的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return String
	   */
	 public String getPk_billtype() {
		 return pk_billtype;
	  }   
	  
     /**
	   * 属性pk_billtype的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newPk_billtype String
	   */
	public void setPk_billtype(String newPk_billtype) {
		
		pk_billtype = newPk_billtype;
	 } 	  
       
        /**
	   * 属性vbillstatus的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return Integer
	   */
	 public Integer getVbillstatus() {
		 return vbillstatus;
	  }   
	  
     /**
	   * 属性vbillstatus的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newVbillstatus Integer
	   */
	public void setVbillstatus(Integer newVbillstatus) {
		
		vbillstatus = newVbillstatus;
	 } 	  
       
        /**
	   * 属性vdef2的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return String
	   */
	 public String getVdef2() {
		 return vdef2;
	  }   
	  
     /**
	   * 属性vdef2的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newVdef2 String
	   */
	public void setVdef2(String newVdef2) {
		
		vdef2 = newVdef2;
	 } 	  
       
        /**
	   * 属性vinaddress的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return String
	   */
	 public String getVinaddress() {
		 return vinaddress;
	  }   
	  
     /**
	   * 属性vinaddress的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newVinaddress String
	   */
	public void setVinaddress(String newVinaddress) {
		
		vinaddress = newVinaddress;
	 } 	  
       
        /**
	   * 属性reserve11的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return UFDate
	   */
	 public UFDate getReserve11() {
		 return reserve11;
	  }   
	  
     /**
	   * 属性reserve11的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newReserve11 UFDate
	   */
	public void setReserve11(UFDate newReserve11) {
		
		reserve11 = newReserve11;
	 } 	  
       
        /**
	   * 属性vmemo的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return String
	   */
	 public String getVmemo() {
		 return vmemo;
	  }   
	  
     /**
	   * 属性vmemo的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newVmemo String
	   */
	public void setVmemo(String newVmemo) {
		
		vmemo = newVmemo;
	 } 	  
       
        /**
	   * 属性pk_busitype的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return String
	   */
	 public String getPk_busitype() {
		 return pk_busitype;
	  }   
	  
     /**
	   * 属性pk_busitype的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newPk_busitype String
	   */
	public void setPk_busitype(String newPk_busitype) {
		
		pk_busitype = newPk_busitype;
	 } 	  
       
        /**
	   * 属性vsourcebillcode的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return String
	   */
	 public String getVsourcebillcode() {
		 return vsourcebillcode;
	  }   
	  
     /**
	   * 属性vsourcebillcode的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newVsourcebillcode String
	   */
	public void setVsourcebillcode(String newVsourcebillcode) {
		
		vsourcebillcode = newVsourcebillcode;
	 } 	  
       
        /**
	   * 属性vdef1的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return String
	   */
	 public String getVdef1() {
		 return vdef1;
	  }   
	  
     /**
	   * 属性vdef1的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newVdef1 String
	   */
	public void setVdef1(String newVdef1) {
		
		vdef1 = newVdef1;
	 } 	  
       
        /**
	   * 属性pk_receiveperson的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return String
	   */
	 public String getPk_receiveperson() {
		 return pk_receiveperson;
	  }   
	  
     /**
	   * 属性pk_receiveperson的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newPk_receiveperson String
	   */
	public void setPk_receiveperson(String newPk_receiveperson) {
		
		pk_receiveperson = newPk_receiveperson;
	 } 	  
       
        /**
	   * 属性reserve3的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return String
	   */
	 public String getReserve3() {
		 return reserve3;
	  }   
	  
     /**
	   * 属性reserve3的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newReserve3 String
	   */
	public void setReserve3(String newReserve3) {
		
		reserve3 = newReserve3;
	 } 	  
       
        /**
	   * 属性csourcebillbid的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return String
	   */
	 public String getCsourcebillbid() {
		 return csourcebillbid;
	  }   
	  
     /**
	   * 属性csourcebillbid的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newCsourcebillbid String
	   */
	public void setCsourcebillbid(String newCsourcebillbid) {
		
		csourcebillbid = newCsourcebillbid;
	 } 	  
       
        /**
	   * 属性vbillno的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return String
	   */
	 public String getVbillno() {
		 return vbillno;
	  }   
	  
     /**
	   * 属性vbillno的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newVbillno String
	   */
	public void setVbillno(String newVbillno) {
		
		vbillno = newVbillno;
	 } 	  
       
        /**
	   * 属性dbilldate的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return UFDate
	   */
	 public UFDate getDbilldate() {
		 return dbilldate;
	  }   
	  
     /**
	   * 属性dbilldate的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newDbilldate UFDate
	   */
	public void setDbilldate(UFDate newDbilldate) {
		
		dbilldate = newDbilldate;
	 } 	  
       
        /**
	   * 属性pk_inwhouse的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return String
	   */
	 public String getPk_inwhouse() {
		 return pk_inwhouse;
	  }   
	  
     /**
	   * 属性pk_inwhouse的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newPk_inwhouse String
	   */
	public void setPk_inwhouse(String newPk_inwhouse) {
		
		pk_inwhouse = newPk_inwhouse;
	 } 	  
       
        /**
	   * 属性reserve13的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return UFDate
	   */
	 public UFDate getReserve13() {
		 return reserve13;
	  }   
	  
     /**
	   * 属性reserve13的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newReserve13 UFDate
	   */
	public void setReserve13(UFDate newReserve13) {
		
		reserve13 = newReserve13;
	 } 	  
       
        /**
	   * 属性reserve8的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return UFDouble
	   */
	 public UFDouble getReserve8() {
		 return reserve8;
	  }   
	  
     /**
	   * 属性reserve8的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newReserve8 UFDouble
	   */
	public void setReserve8(UFDouble newReserve8) {
		
		reserve8 = newReserve8;
	 } 	  
       
        /**
	   * 属性reserve9的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return UFDouble
	   */
	 public UFDouble getReserve9() {
		 return reserve9;
	  }   
	  
     /**
	   * 属性reserve9的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newReserve9 UFDouble
	   */
	public void setReserve9(UFDouble newReserve9) {
		
		reserve9 = newReserve9;
	 } 	  
       
        /**
	   * 属性dfirstbilldate的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return UFDate
	   */
	 public UFDate getDfirstbilldate() {
		 return dfirstbilldate;
	  }   
	  
     /**
	   * 属性dfirstbilldate的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newDfirstbilldate UFDate
	   */
	public void setDfirstbilldate(UFDate newDfirstbilldate) {
		
		dfirstbilldate = newDfirstbilldate;
	 } 	  
       
        /**
	   * 属性vdef9的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return String
	   */
	 public String getVdef9() {
		 return vdef9;
	  }   
	  
     /**
	   * 属性vdef9的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newVdef9 String
	   */
	public void setVdef9(String newVdef9) {
		
		vdef9 = newVdef9;
	 } 	  
       
        /**
	   * 属性vdef8的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return String
	   */
	 public String getVdef8() {
		 return vdef8;
	  }   
	  
     /**
	   * 属性vdef8的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newVdef8 String
	   */
	public void setVdef8(String newVdef8) {
		
		vdef8 = newVdef8;
	 } 	  
       
        /**
	   * 属性dapprovedate的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return UFDate
	   */
	 public UFDate getDapprovedate() {
		 return dapprovedate;
	  }   
	  
     /**
	   * 属性dapprovedate的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newDapprovedate UFDate
	   */
	public void setDapprovedate(UFDate newDapprovedate) {
		
		dapprovedate = newDapprovedate;
	 } 	  
       
        /**
	   * 属性cfirsttype的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return String
	   */
	 public String getCfirsttype() {
		 return cfirsttype;
	  }   
	  
     /**
	   * 属性cfirsttype的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newCfirsttype String
	   */
	public void setCfirsttype(String newCfirsttype) {
		
		cfirsttype = newCfirsttype;
	 } 	  
       
        /**
	   * 属性vtelphone的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return String
	   */
	 public String getVtelphone() {
		 return vtelphone;
	  }   
	  
     /**
	   * 属性vtelphone的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newVtelphone String
	   */
	public void setVtelphone(String newVtelphone) {
		
		vtelphone = newVtelphone;
	 } 	  
       
        /**
	   * 属性vapproveid的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return String
	   */
	 public String getVapproveid() {
		 return vapproveid;
	  }   
	  
     /**
	   * 属性vapproveid的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newVapproveid String
	   */
	public void setVapproveid(String newVapproveid) {
		
		vapproveid = newVapproveid;
	 } 	  
       
        /**
	   * 属性reserve7的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return String
	   */
	 public String getReserve7() {
		 return reserve7;
	  }   
	  
     /**
	   * 属性reserve7的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newReserve7 String
	   */
	public void setReserve7(String newReserve7) {
		
		reserve7 = newReserve7;
	 } 	  
       
        /**
	   * 属性vdef6的Getter方法.
	   *
	   * 创建日期:2011-3-24
	   * @return String
	   */
	 public String getVdef6() {
		 return vdef6;
	  }   
	  
     /**
	   * 属性vdef6的Setter方法.
	   *
	   * 创建日期:2011-3-24
	   * @param newVdef6 String
	   */
	public void setVdef6(String newVdef6) {
		
		vdef6 = newVdef6;
	 } 	  
       
     
       
    /**
	  * 验证对象各属性之间的数据逻辑正确性.
	  *
	  * 创建日期:2011-3-24
	  * @exception nc.vo.pub.ValidationException 如果验证失败,抛出
	  * ValidationException,对错误进行解释.
	 */
	 public void validate() throws ValidationException {
	
	 	ArrayList errFields = new ArrayList(); // errFields record those null

                                                      // fields that cannot be null.
       		  // 检查是否为不允许空的字段赋了空值,你可能需要修改下面的提示信息:
	
	   		if (pk_sendorder == null) {
			errFields.add(new String("pk_sendorder"));
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
	  * 创建日期:2011-3-24
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	  	 
	 	    return null;
	 	
	}   
    
    /**
	  * <p>取得表主键.
	  * <p>
	  * 创建日期:2011-3-24
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	 	  return "pk_sendorder";
	 	}
    
	/**
      * <p>返回表名称.
	  * <p>
	  * 创建日期:2011-3-24
	  * @return java.lang.String
	 */
	public java.lang.String getTableName() {
				
		return "wds_sendorder";
	}    
    
    /**
	  * 按照默认方式创建构造子.
	  *
	  * 创建日期:2011-3-24
	  */
	public SendorderVO() {
			
			   super();	
	  }    
    
            /**
	 * 使用主键进行初始化的构造子.
	 *
	 * 创建日期:2011-3-24
	 * @param newPk_sendorder 主键值
	 */
	 public SendorderVO(String newPk_sendorder) {
		
		// 为主键字段赋值:
		 pk_sendorder = newPk_sendorder;
	
    	}
    
     
     /**
	  * 返回对象标识,用来唯一定位对象.
	  *
	  * 创建日期:2011-3-24
	  * @return String
	  */
	   public String getPrimaryKey() {
				
		 return pk_sendorder;
	   
	   }

     /**
	  * 设置对象标识,用来唯一定位对象.
	  *
	  * 创建日期:2011-3-24
	  * @param newPk_sendorder  String    
	  */
	 public void setPrimaryKey(String newPk_sendorder) {
				
				pk_sendorder = newPk_sendorder; 
				
	 } 
           
	  /**
       * 返回数值对象的显示名称.
	   *
	   * 创建日期:2011-3-24
	   * @return java.lang.String 返回数值对象的显示名称.
	   */
	 public String getEntityName() {
				
	   return "wds_sendorder"; 
				
	 } 
} 
