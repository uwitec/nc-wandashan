/***************************************************************\
 *     The skeleton of this class is generated by an automatic *
 * code generator for NC product. It is based on Velocity.     *
\***************************************************************/
package nc.vo.zb.avnum;
	
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
	
/**
 * <b> 在此处简要描述此类的功能 </b>
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 * 创建日期:2011-05-21 13:11:50
 * @author Administrator
 * @version NCPrj 1.0
 */
@SuppressWarnings("serial")
public class AvNumBodyVO extends SuperVO {
	
	private String crowno;//行号
	private String cunitid;//计量档案
	private String vmemo;//备注
	private String cinvbasid;//存货基本档案ID
	private UFDouble nzbprice;//中标价格
	private UFDouble nzbnum;//招标数量
	private String cinvclid;//存货分类ID
	private String cinvmanid;//存货管理档案ID
	private AvVendorVO[] avVendorVO;//子表vo
	
	private String pk_defdoc19;
	private UFDateTime ts;
	private UFDouble reserve10;
	private String vdef9;
	private String pk_defdoc10;
	private UFBoolean reserve15;
	private String cupsourcebilltype;
	private String vdef10;
	private String pk_defdoc18;
	private String csourcebillhid;
	private String reserve2;
	private String vdef15;
	private UFDouble reserve8;
	private String pk_defdoc8;
	private String vdef14;
	private String pk_defdoc5;
	private String cavnumid;
	private UFDouble reserve7;
	private String vdef7;
	private String pk_defdoc7;
	private UFDate reserve11;
	private String vdef2;
	private String vdef16;
	private String vdef5;
	private UFDouble reserve9;
	private String pk_defdoc12;
	private String vdef19;
	private String pk_defdoc13;
	private String pk_defdoc6;
	private String vdef4;
	private String vdef18;
	private String vdef17;
	private String pk_defdoc15;
	private String cupsourcebillrowid;
	private String pk_defdoc2;
	private String vdef20;
	private String vdef1;
	private String vdef8;
	private String reserve5;
	private String pk_defdoc3;
	private String pk_defdoc17;
	private UFDate reserve12;
	private String pk_defdoc16;
	private UFDouble reserve6;
	private String csourcetype;
	private String reserve1;
	private String vdef13;
	private UFBoolean reserve16;
	private UFBoolean reserve14;
	private String pk_defdoc11;
	private String pk_defdoc9;
	private UFDate reserve13;
	private String vdef11;
	private String pk_defdoc14;
	private String cupsourcebillid;
	private String cavnumbid;
	private String pk_defdoc20;
	private String pk_defdoc4;
	private String vdef3;
	private String vdef12;
	private String vdef6;
	private String reserve3;
	private Integer dr;
	private String pk_defdoc1;
	private String csourcebillbid;
	private String reserve4;

	public static final String PK_DEFDOC19 = "pk_defdoc19";
	public static final String RESERVE10 = "reserve10";
	public static final String VDEF9 = "vdef9";
	public static final String PK_DEFDOC10 = "pk_defdoc10";
	public static final String RESERVE15 = "reserve15";
	public static final String CUPSOURCEBILLTYPE = "cupsourcebilltype";
	public static final String VDEF10 = "vdef10";
	public static final String PK_DEFDOC18 = "pk_defdoc18";
	public static final String CSOURCEBILLHID = "csourcebillhid";
	public static final String RESERVE2 = "reserve2";
	public static final String CROWNO = "crowno";
	public static final String VDEF15 = "vdef15";
	public static final String RESERVE8 = "reserve8";
	public static final String PK_DEFDOC8 = "pk_defdoc8";
	public static final String VDEF14 = "vdef14";
	public static final String PK_DEFDOC5 = "pk_defdoc5";
	public static final String CAVNUMID = "cavnumid";
	public static final String CUNITID = "cunitid";
	public static final String RESERVE7 = "reserve7";
	public static final String VDEF7 = "vdef7";
	public static final String PK_DEFDOC7 = "pk_defdoc7";
	public static final String RESERVE11 = "reserve11";
	public static final String VDEF2 = "vdef2";
	public static final String VDEF16 = "vdef16";
	public static final String VDEF5 = "vdef5";
	public static final String RESERVE9 = "reserve9";
	public static final String VMEMO = "vmemo";
	public static final String CINVBASID = "cinvbasid";
	public static final String PK_DEFDOC12 = "pk_defdoc12";
	public static final String VDEF19 = "vdef19";
	public static final String PK_DEFDOC13 = "pk_defdoc13";
	public static final String PK_DEFDOC6 = "pk_defdoc6";
	public static final String VDEF4 = "vdef4";
	public static final String VDEF18 = "vdef18";
	public static final String NZBPRICE = "nzbprice";
	public static final String NZBNUM = "nzbnum";
	public static final String VDEF17 = "vdef17";
	public static final String PK_DEFDOC15 = "pk_defdoc15";
	public static final String CUPSOURCEBILLROWID = "cupsourcebillrowid";
	public static final String PK_DEFDOC2 = "pk_defdoc2";
	public static final String VDEF20 = "vdef20";
	public static final String VDEF1 = "vdef1";
	public static final String VDEF8 = "vdef8";
	public static final String CINVCLID = "cinvclid";
	public static final String RESERVE5 = "reserve5";
	public static final String PK_DEFDOC3 = "pk_defdoc3";
	public static final String PK_DEFDOC17 = "pk_defdoc17";
	public static final String CINVMANID = "cinvmanid";
	public static final String RESERVE12 = "reserve12";
	public static final String PK_DEFDOC16 = "pk_defdoc16";
	public static final String RESERVE6 = "reserve6";
	public static final String CSOURCETYPE = "csourcetype";
	public static final String RESERVE1 = "reserve1";
	public static final String VDEF13 = "vdef13";
	public static final String RESERVE16 = "reserve16";
	public static final String RESERVE14 = "reserve14";
	public static final String PK_DEFDOC11 = "pk_defdoc11";
	public static final String PK_DEFDOC9 = "pk_defdoc9";
	public static final String RESERVE13 = "reserve13";
	public static final String VDEF11 = "vdef11";
	public static final String PK_DEFDOC14 = "pk_defdoc14";
	public static final String CUPSOURCEBILLID = "cupsourcebillid";
	public static final String CAVNUMBID = "cavnumbid";
	public static final String PK_DEFDOC20 = "pk_defdoc20";
	public static final String PK_DEFDOC4 = "pk_defdoc4";
	public static final String VDEF3 = "vdef3";
	public static final String VDEF12 = "vdef12";
	public static final String VDEF6 = "vdef6";
	public static final String RESERVE3 = "reserve3";
	public static final String PK_DEFDOC1 = "pk_defdoc1";
	public static final String CSOURCEBILLBID = "csourcebillbid";
	public static final String RESERVE4 = "reserve4";
			
	/**
	 * 属性pk_defdoc19的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getPk_defdoc19 () {
		return pk_defdoc19;
	}   
	/**
	 * 属性pk_defdoc19的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newPk_defdoc19 String
	 */
	public void setPk_defdoc19 (String newPk_defdoc19 ) {
	 	this.pk_defdoc19 = newPk_defdoc19;
	} 	  
	/**
	 * 属性ts的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return UFDateTime
	 */
	public UFDateTime getTs () {
		return ts;
	}   
	/**
	 * 属性ts的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newTs UFDateTime
	 */
	public void setTs (UFDateTime newTs ) {
	 	this.ts = newTs;
	} 	  
	/**
	 * 属性reserve10的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return UFDouble
	 */
	public UFDouble getReserve10 () {
		return reserve10;
	}   
	/**
	 * 属性reserve10的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newReserve10 UFDouble
	 */
	public void setReserve10 (UFDouble newReserve10 ) {
	 	this.reserve10 = newReserve10;
	} 	  
	/**
	 * 属性vdef9的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getVdef9 () {
		return vdef9;
	}   
	/**
	 * 属性vdef9的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newVdef9 String
	 */
	public void setVdef9 (String newVdef9 ) {
	 	this.vdef9 = newVdef9;
	} 	  
	/**
	 * 属性pk_defdoc10的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getPk_defdoc10 () {
		return pk_defdoc10;
	}   
	/**
	 * 属性pk_defdoc10的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newPk_defdoc10 String
	 */
	public void setPk_defdoc10 (String newPk_defdoc10 ) {
	 	this.pk_defdoc10 = newPk_defdoc10;
	} 	  
	/**
	 * 属性reserve15的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return UFBoolean
	 */
	public UFBoolean getReserve15 () {
		return reserve15;
	}   
	/**
	 * 属性reserve15的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newReserve15 UFBoolean
	 */
	public void setReserve15 (UFBoolean newReserve15 ) {
	 	this.reserve15 = newReserve15;
	} 	  
	/**
	 * 属性cupsourcebilltype的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getCupsourcebilltype () {
		return cupsourcebilltype;
	}   
	/**
	 * 属性cupsourcebilltype的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newCupsourcebilltype String
	 */
	public void setCupsourcebilltype (String newCupsourcebilltype ) {
	 	this.cupsourcebilltype = newCupsourcebilltype;
	} 	  
	/**
	 * 属性vdef10的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getVdef10 () {
		return vdef10;
	}   
	/**
	 * 属性vdef10的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newVdef10 String
	 */
	public void setVdef10 (String newVdef10 ) {
	 	this.vdef10 = newVdef10;
	} 	  
	/**
	 * 属性pk_defdoc18的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getPk_defdoc18 () {
		return pk_defdoc18;
	}   
	/**
	 * 属性pk_defdoc18的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newPk_defdoc18 String
	 */
	public void setPk_defdoc18 (String newPk_defdoc18 ) {
	 	this.pk_defdoc18 = newPk_defdoc18;
	} 	  
	/**
	 * 属性csourcebillhid的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getCsourcebillhid () {
		return csourcebillhid;
	}   
	/**
	 * 属性csourcebillhid的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newCsourcebillhid String
	 */
	public void setCsourcebillhid (String newCsourcebillhid ) {
	 	this.csourcebillhid = newCsourcebillhid;
	} 	  
	/**
	 * 属性reserve2的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getReserve2 () {
		return reserve2;
	}   
	/**
	 * 属性reserve2的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newReserve2 String
	 */
	public void setReserve2 (String newReserve2 ) {
	 	this.reserve2 = newReserve2;
	} 	  
	/**
	 * 属性crowno的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getCrowno () {
		return crowno;
	}   
	/**
	 * 属性crowno的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newCrowno String
	 */
	public void setCrowno (String newCrowno ) {
	 	this.crowno = newCrowno;
	} 	  
	/**
	 * 属性vdef15的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getVdef15 () {
		return vdef15;
	}   
	/**
	 * 属性vdef15的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newVdef15 String
	 */
	public void setVdef15 (String newVdef15 ) {
	 	this.vdef15 = newVdef15;
	} 	  
	/**
	 * 属性reserve8的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return UFDouble
	 */
	public UFDouble getReserve8 () {
		return reserve8;
	}   
	/**
	 * 属性reserve8的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newReserve8 UFDouble
	 */
	public void setReserve8 (UFDouble newReserve8 ) {
	 	this.reserve8 = newReserve8;
	} 	  
	/**
	 * 属性pk_defdoc8的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getPk_defdoc8 () {
		return pk_defdoc8;
	}   
	/**
	 * 属性pk_defdoc8的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newPk_defdoc8 String
	 */
	public void setPk_defdoc8 (String newPk_defdoc8 ) {
	 	this.pk_defdoc8 = newPk_defdoc8;
	} 	  
	/**
	 * 属性vdef14的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getVdef14 () {
		return vdef14;
	}   
	/**
	 * 属性vdef14的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newVdef14 String
	 */
	public void setVdef14 (String newVdef14 ) {
	 	this.vdef14 = newVdef14;
	} 	  
	/**
	 * 属性pk_defdoc5的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getPk_defdoc5 () {
		return pk_defdoc5;
	}   
	/**
	 * 属性pk_defdoc5的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newPk_defdoc5 String
	 */
	public void setPk_defdoc5 (String newPk_defdoc5 ) {
	 	this.pk_defdoc5 = newPk_defdoc5;
	} 	  
	/**
	 * 属性cavnumid的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getCavnumid () {
		return cavnumid;
	}   
	/**
	 * 属性cavnumid的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newCavnumid String
	 */
	public void setCavnumid (String newCavnumid ) {
	 	this.cavnumid = newCavnumid;
	} 	  
	/**
	 * 属性cunitid的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getCunitid () {
		return cunitid;
	}   
	/**
	 * 属性cunitid的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newCunitid String
	 */
	public void setCunitid (String newCunitid ) {
	 	this.cunitid = newCunitid;
	} 	  
	/**
	 * 属性reserve7的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return UFDouble
	 */
	public UFDouble getReserve7 () {
		return reserve7;
	}   
	/**
	 * 属性reserve7的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newReserve7 UFDouble
	 */
	public void setReserve7 (UFDouble newReserve7 ) {
	 	this.reserve7 = newReserve7;
	} 	  
	/**
	 * 属性vdef7的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getVdef7 () {
		return vdef7;
	}   
	/**
	 * 属性vdef7的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newVdef7 String
	 */
	public void setVdef7 (String newVdef7 ) {
	 	this.vdef7 = newVdef7;
	} 	  
	/**
	 * 属性pk_defdoc7的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getPk_defdoc7 () {
		return pk_defdoc7;
	}   
	/**
	 * 属性pk_defdoc7的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newPk_defdoc7 String
	 */
	public void setPk_defdoc7 (String newPk_defdoc7 ) {
	 	this.pk_defdoc7 = newPk_defdoc7;
	} 	  
	/**
	 * 属性reserve11的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return UFDate
	 */
	public UFDate getReserve11 () {
		return reserve11;
	}   
	/**
	 * 属性reserve11的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newReserve11 UFDate
	 */
	public void setReserve11 (UFDate newReserve11 ) {
	 	this.reserve11 = newReserve11;
	} 	  
	/**
	 * 属性vdef2的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getVdef2 () {
		return vdef2;
	}   
	/**
	 * 属性vdef2的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newVdef2 String
	 */
	public void setVdef2 (String newVdef2 ) {
	 	this.vdef2 = newVdef2;
	} 	  
	/**
	 * 属性vdef16的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getVdef16 () {
		return vdef16;
	}   
	/**
	 * 属性vdef16的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newVdef16 String
	 */
	public void setVdef16 (String newVdef16 ) {
	 	this.vdef16 = newVdef16;
	} 	  
	/**
	 * 属性vdef5的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getVdef5 () {
		return vdef5;
	}   
	/**
	 * 属性vdef5的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newVdef5 String
	 */
	public void setVdef5 (String newVdef5 ) {
	 	this.vdef5 = newVdef5;
	} 	  
	/**
	 * 属性reserve9的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return UFDouble
	 */
	public UFDouble getReserve9 () {
		return reserve9;
	}   
	/**
	 * 属性reserve9的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newReserve9 UFDouble
	 */
	public void setReserve9 (UFDouble newReserve9 ) {
	 	this.reserve9 = newReserve9;
	} 	  
	/**
	 * 属性vmemo的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getVmemo () {
		return vmemo;
	}   
	/**
	 * 属性vmemo的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newVmemo String
	 */
	public void setVmemo (String newVmemo ) {
	 	this.vmemo = newVmemo;
	} 	  
	/**
	 * 属性cinvbasid的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getCinvbasid () {
		return cinvbasid;
	}   
	/**
	 * 属性cinvbasid的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newCinvbasid String
	 */
	public void setCinvbasid (String newCinvbasid ) {
	 	this.cinvbasid = newCinvbasid;
	} 	  
	/**
	 * 属性pk_defdoc12的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getPk_defdoc12 () {
		return pk_defdoc12;
	}   
	/**
	 * 属性pk_defdoc12的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newPk_defdoc12 String
	 */
	public void setPk_defdoc12 (String newPk_defdoc12 ) {
	 	this.pk_defdoc12 = newPk_defdoc12;
	} 	  
	/**
	 * 属性vdef19的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getVdef19 () {
		return vdef19;
	}   
	/**
	 * 属性vdef19的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newVdef19 String
	 */
	public void setVdef19 (String newVdef19 ) {
	 	this.vdef19 = newVdef19;
	} 	  
	/**
	 * 属性pk_defdoc13的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getPk_defdoc13 () {
		return pk_defdoc13;
	}   
	/**
	 * 属性pk_defdoc13的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newPk_defdoc13 String
	 */
	public void setPk_defdoc13 (String newPk_defdoc13 ) {
	 	this.pk_defdoc13 = newPk_defdoc13;
	} 	  
	/**
	 * 属性pk_defdoc6的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getPk_defdoc6 () {
		return pk_defdoc6;
	}   
	/**
	 * 属性pk_defdoc6的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newPk_defdoc6 String
	 */
	public void setPk_defdoc6 (String newPk_defdoc6 ) {
	 	this.pk_defdoc6 = newPk_defdoc6;
	} 	  
	/**
	 * 属性vdef4的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getVdef4 () {
		return vdef4;
	}   
	/**
	 * 属性vdef4的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newVdef4 String
	 */
	public void setVdef4 (String newVdef4 ) {
	 	this.vdef4 = newVdef4;
	} 	  
	/**
	 * 属性vdef18的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getVdef18 () {
		return vdef18;
	}   
	/**
	 * 属性vdef18的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newVdef18 String
	 */
	public void setVdef18 (String newVdef18 ) {
	 	this.vdef18 = newVdef18;
	} 	  
	/**
	 * 属性nzbprice的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return UFDouble
	 */
	public UFDouble getNzbprice () {
		return nzbprice;
	}   
	/**
	 * 属性nzbprice的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newNzbprice UFDouble
	 */
	public void setNzbprice (UFDouble newNzbprice ) {
	 	this.nzbprice = newNzbprice;
	} 	  
	/**
	 * 属性nzbnum的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return UFDouble
	 */
	public UFDouble getNzbnum () {
		return nzbnum;
	}   
	/**
	 * 属性nzbnum的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newNzbnum UFDouble
	 */
	public void setNzbnum (UFDouble newNzbnum ) {
	 	this.nzbnum = newNzbnum;
	} 	  
	/**
	 * 属性vdef17的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getVdef17 () {
		return vdef17;
	}   
	/**
	 * 属性vdef17的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newVdef17 String
	 */
	public void setVdef17 (String newVdef17 ) {
	 	this.vdef17 = newVdef17;
	} 	  
	/**
	 * 属性pk_defdoc15的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getPk_defdoc15 () {
		return pk_defdoc15;
	}   
	/**
	 * 属性pk_defdoc15的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newPk_defdoc15 String
	 */
	public void setPk_defdoc15 (String newPk_defdoc15 ) {
	 	this.pk_defdoc15 = newPk_defdoc15;
	} 	  
	/**
	 * 属性cupsourcebillrowid的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getCupsourcebillrowid () {
		return cupsourcebillrowid;
	}   
	/**
	 * 属性cupsourcebillrowid的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newCupsourcebillrowid String
	 */
	public void setCupsourcebillrowid (String newCupsourcebillrowid ) {
	 	this.cupsourcebillrowid = newCupsourcebillrowid;
	} 	  
	/**
	 * 属性pk_defdoc2的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getPk_defdoc2 () {
		return pk_defdoc2;
	}   
	/**
	 * 属性pk_defdoc2的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newPk_defdoc2 String
	 */
	public void setPk_defdoc2 (String newPk_defdoc2 ) {
	 	this.pk_defdoc2 = newPk_defdoc2;
	} 	  
	/**
	 * 属性vdef20的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getVdef20 () {
		return vdef20;
	}   
	/**
	 * 属性vdef20的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newVdef20 String
	 */
	public void setVdef20 (String newVdef20 ) {
	 	this.vdef20 = newVdef20;
	} 	  
	/**
	 * 属性vdef1的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getVdef1 () {
		return vdef1;
	}   
	/**
	 * 属性vdef1的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newVdef1 String
	 */
	public void setVdef1 (String newVdef1 ) {
	 	this.vdef1 = newVdef1;
	} 	  
	/**
	 * 属性vdef8的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getVdef8 () {
		return vdef8;
	}   
	/**
	 * 属性vdef8的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newVdef8 String
	 */
	public void setVdef8 (String newVdef8 ) {
	 	this.vdef8 = newVdef8;
	} 	  
	/**
	 * 属性cinvclid的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getCinvclid () {
		return cinvclid;
	}   
	/**
	 * 属性cinvclid的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newCinvclid String
	 */
	public void setCinvclid (String newCinvclid ) {
	 	this.cinvclid = newCinvclid;
	} 	  
	/**
	 * 属性reserve5的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getReserve5 () {
		return reserve5;
	}   
	/**
	 * 属性reserve5的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newReserve5 String
	 */
	public void setReserve5 (String newReserve5 ) {
	 	this.reserve5 = newReserve5;
	} 	  
	/**
	 * 属性pk_defdoc3的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getPk_defdoc3 () {
		return pk_defdoc3;
	}   
	/**
	 * 属性pk_defdoc3的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newPk_defdoc3 String
	 */
	public void setPk_defdoc3 (String newPk_defdoc3 ) {
	 	this.pk_defdoc3 = newPk_defdoc3;
	} 	  
	/**
	 * 属性pk_defdoc17的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getPk_defdoc17 () {
		return pk_defdoc17;
	}   
	/**
	 * 属性pk_defdoc17的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newPk_defdoc17 String
	 */
	public void setPk_defdoc17 (String newPk_defdoc17 ) {
	 	this.pk_defdoc17 = newPk_defdoc17;
	} 	  
	/**
	 * 属性cinvmanid的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getCinvmanid () {
		return cinvmanid;
	}   
	/**
	 * 属性cinvmanid的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newCinvmanid String
	 */
	public void setCinvmanid (String newCinvmanid ) {
	 	this.cinvmanid = newCinvmanid;
	} 	  
	/**
	 * 属性reserve12的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return UFDate
	 */
	public UFDate getReserve12 () {
		return reserve12;
	}   
	/**
	 * 属性reserve12的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newReserve12 UFDate
	 */
	public void setReserve12 (UFDate newReserve12 ) {
	 	this.reserve12 = newReserve12;
	} 	  
	/**
	 * 属性pk_defdoc16的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getPk_defdoc16 () {
		return pk_defdoc16;
	}   
	/**
	 * 属性pk_defdoc16的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newPk_defdoc16 String
	 */
	public void setPk_defdoc16 (String newPk_defdoc16 ) {
	 	this.pk_defdoc16 = newPk_defdoc16;
	} 	  
	/**
	 * 属性reserve6的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return UFDouble
	 */
	public UFDouble getReserve6 () {
		return reserve6;
	}   
	/**
	 * 属性reserve6的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newReserve6 UFDouble
	 */
	public void setReserve6 (UFDouble newReserve6 ) {
	 	this.reserve6 = newReserve6;
	} 	  
	/**
	 * 属性csourcetype的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getCsourcetype () {
		return csourcetype;
	}   
	/**
	 * 属性csourcetype的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newCsourcetype String
	 */
	public void setCsourcetype (String newCsourcetype ) {
	 	this.csourcetype = newCsourcetype;
	} 	  
	/**
	 * 属性reserve1的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getReserve1 () {
		return reserve1;
	}   
	/**
	 * 属性reserve1的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newReserve1 String
	 */
	public void setReserve1 (String newReserve1 ) {
	 	this.reserve1 = newReserve1;
	} 	  
	/**
	 * 属性vdef13的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getVdef13 () {
		return vdef13;
	}   
	/**
	 * 属性vdef13的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newVdef13 String
	 */
	public void setVdef13 (String newVdef13 ) {
	 	this.vdef13 = newVdef13;
	} 	  
	/**
	 * 属性reserve16的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return UFBoolean
	 */
	public UFBoolean getReserve16 () {
		return reserve16;
	}   
	/**
	 * 属性reserve16的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newReserve16 UFBoolean
	 */
	public void setReserve16 (UFBoolean newReserve16 ) {
	 	this.reserve16 = newReserve16;
	} 	  
	/**
	 * 属性reserve14的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return UFBoolean
	 */
	public UFBoolean getReserve14 () {
		return reserve14;
	}   
	/**
	 * 属性reserve14的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newReserve14 UFBoolean
	 */
	public void setReserve14 (UFBoolean newReserve14 ) {
	 	this.reserve14 = newReserve14;
	} 	  
	/**
	 * 属性pk_defdoc11的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getPk_defdoc11 () {
		return pk_defdoc11;
	}   
	/**
	 * 属性pk_defdoc11的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newPk_defdoc11 String
	 */
	public void setPk_defdoc11 (String newPk_defdoc11 ) {
	 	this.pk_defdoc11 = newPk_defdoc11;
	} 	  
	/**
	 * 属性pk_defdoc9的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getPk_defdoc9 () {
		return pk_defdoc9;
	}   
	/**
	 * 属性pk_defdoc9的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newPk_defdoc9 String
	 */
	public void setPk_defdoc9 (String newPk_defdoc9 ) {
	 	this.pk_defdoc9 = newPk_defdoc9;
	} 	  
	/**
	 * 属性reserve13的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return UFDate
	 */
	public UFDate getReserve13 () {
		return reserve13;
	}   
	/**
	 * 属性reserve13的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newReserve13 UFDate
	 */
	public void setReserve13 (UFDate newReserve13 ) {
	 	this.reserve13 = newReserve13;
	} 	  
	/**
	 * 属性vdef11的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getVdef11 () {
		return vdef11;
	}   
	/**
	 * 属性vdef11的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newVdef11 String
	 */
	public void setVdef11 (String newVdef11 ) {
	 	this.vdef11 = newVdef11;
	} 	  
	/**
	 * 属性pk_defdoc14的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getPk_defdoc14 () {
		return pk_defdoc14;
	}   
	/**
	 * 属性pk_defdoc14的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newPk_defdoc14 String
	 */
	public void setPk_defdoc14 (String newPk_defdoc14 ) {
	 	this.pk_defdoc14 = newPk_defdoc14;
	} 	  
	/**
	 * 属性cupsourcebillid的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getCupsourcebillid () {
		return cupsourcebillid;
	}   
	/**
	 * 属性cupsourcebillid的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newCupsourcebillid String
	 */
	public void setCupsourcebillid (String newCupsourcebillid ) {
	 	this.cupsourcebillid = newCupsourcebillid;
	} 	  
	/**
	 * 属性cavnumbid的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getCavnumbid () {
		return cavnumbid;
	}   
	/**
	 * 属性cavnumbid的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newCavnumbid String
	 */
	public void setCavnumbid (String newCavnumbid ) {
	 	this.cavnumbid = newCavnumbid;
	} 	  
	/**
	 * 属性pk_defdoc20的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getPk_defdoc20 () {
		return pk_defdoc20;
	}   
	/**
	 * 属性pk_defdoc20的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newPk_defdoc20 String
	 */
	public void setPk_defdoc20 (String newPk_defdoc20 ) {
	 	this.pk_defdoc20 = newPk_defdoc20;
	} 	  
	/**
	 * 属性pk_defdoc4的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getPk_defdoc4 () {
		return pk_defdoc4;
	}   
	/**
	 * 属性pk_defdoc4的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newPk_defdoc4 String
	 */
	public void setPk_defdoc4 (String newPk_defdoc4 ) {
	 	this.pk_defdoc4 = newPk_defdoc4;
	} 	  
	/**
	 * 属性vdef3的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getVdef3 () {
		return vdef3;
	}   
	/**
	 * 属性vdef3的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newVdef3 String
	 */
	public void setVdef3 (String newVdef3 ) {
	 	this.vdef3 = newVdef3;
	} 	  
	/**
	 * 属性vdef12的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getVdef12 () {
		return vdef12;
	}   
	/**
	 * 属性vdef12的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newVdef12 String
	 */
	public void setVdef12 (String newVdef12 ) {
	 	this.vdef12 = newVdef12;
	} 	  
	/**
	 * 属性vdef6的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getVdef6 () {
		return vdef6;
	}   
	/**
	 * 属性vdef6的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newVdef6 String
	 */
	public void setVdef6 (String newVdef6 ) {
	 	this.vdef6 = newVdef6;
	} 	  
	/**
	 * 属性reserve3的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getReserve3 () {
		return reserve3;
	}   
	/**
	 * 属性reserve3的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newReserve3 String
	 */
	public void setReserve3 (String newReserve3 ) {
	 	this.reserve3 = newReserve3;
	} 	  
	/**
	 * 属性dr的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return UFDouble
	 */
	public Integer getDr () {
		return dr;
	}   
	/**
	 * 属性dr的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newDr UFDouble
	 */
	public void setDr (Integer newDr ) {
	 	this.dr = newDr;
	} 	  
	/**
	 * 属性pk_defdoc1的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getPk_defdoc1 () {
		return pk_defdoc1;
	}   
	/**
	 * 属性pk_defdoc1的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newPk_defdoc1 String
	 */
	public void setPk_defdoc1 (String newPk_defdoc1 ) {
	 	this.pk_defdoc1 = newPk_defdoc1;
	} 	  
	/**
	 * 属性csourcebillbid的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getCsourcebillbid () {
		return csourcebillbid;
	}   
	/**
	 * 属性csourcebillbid的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newCsourcebillbid String
	 */
	public void setCsourcebillbid (String newCsourcebillbid ) {
	 	this.csourcebillbid = newCsourcebillbid;
	} 	  
	/**
	 * 属性reserve4的Getter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @return String
	 */
	public String getReserve4 () {
		return reserve4;
	}   
	/**
	 * 属性reserve4的Setter方法.
	 * 创建日期:2011-05-21 13:11:50
	 * @param newReserve4 String
	 */
	public void setReserve4 (String newReserve4 ) {
	 	this.reserve4 = newReserve4;
	} 	  
 
	/**
	  * <p>取得父VO主键字段.
	  * <p>
	  * 创建日期:2011-05-21 13:11:50
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
		return "cavnumid";
	}   
    
	/**
	  * <p>取得表主键.
	  * <p>
	  * 创建日期:2011-05-21 13:11:50
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	  return "cavnumbid";
	}
    
	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:2011-05-21 13:11:50
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "zb_avnum_b";
	}    
    
    /**
	  * 按照默认方式创建构造子.
	  *
	  * 创建日期:2011-05-21 13:11:50
	  */
     public AvNumBodyVO() {
		super();	
	}
	public AvVendorVO[] getAvVendorVO() {
		return avVendorVO;
	}
	public void setAvVendorVO(AvVendorVO[] avVendorVO) {
		this.avVendorVO = avVendorVO;
	}    
} 
