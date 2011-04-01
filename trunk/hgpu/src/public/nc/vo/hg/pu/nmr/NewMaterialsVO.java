/***************************************************************\
 *     The skeleton of this class is generated by an automatic *
 * code generator for NC product. It is based on Velocity.     *
\***************************************************************/
package nc.vo.hg.pu.nmr;

	
import nc.vo.pub.*;
import nc.vo.pub.lang.*;
	
/**
 * <b> 在此处简要描述此类的功能 </b>
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 * 创建日期:2011-02-24 12:02:03
 * @author Administrator
 * @version NCPrj 1.0
 */
@SuppressWarnings("serial")
public class NewMaterialsVO extends SuperVO {
	private String pk_corp;//公司
	private String vemployeeid;//申请人
	private Integer ijjway;//集结方式
	private String vinvtype;//类型
	private String pk_taxitems;//税目
	private UFDate dbilldate;//申请日期
	private String vinvname;//名称
	private String vmemo;//备注
	private String pk_invbasdoc;//正式物资基本ID
	private String voperatorid;////编码人
	private String vbillno;//单据号
	private String pk_invcl;//存货分类
	private String pk_measdoc;//计量单位
	private UFBoolean bisjjwz;//是否交旧物资
	private String cinvcode;//物资临时编码
	private String vapproveid;//审批人
	private String pk_deptdoc;//申请部门
	private String vinvspec;//型号
	private UFDate dapprovedate;//审批日期
	private UFBoolean bisdcdx;//是否代储代销
	private String pk_billtype;//单据类型
	private Integer vbillstatus;//大家状态
	private UFDate dmakedate;//制单日期
	private String pk_invbasdoc_temp;//临时物资基本ID
	private UFBoolean biszywz;//是否重要物资
	private String pk_invmandoc;//正式物资管理ID
	private String pk_materials;//主键
	private UFDouble nplanprice;//计划价
	private String invmnecode;//助记码
	private String vtechstan;//技术标准
	private String vmaterial;//材质
	private String invcode;//存货编码
	private UFBoolean discountflag;//价格折扣
	private UFBoolean laborflag;//应税劳务
	private UFDate reserve12;///编码日期
	private String reserve5;//制单人
	private String ccorpid;//申请公司
	
	
	private UFDateTime ts;
	private UFDouble reserve10;
	private UFBoolean reserve15;
	private String reserve2;
	private String reserve17;
	private UFDouble reserve8;
	private UFDouble reserve7;
	private UFDate reserve11;
	private String reserve20;
	private UFDouble reserve9;
	private String pk_busitype;
	private String reserve19;
	private String vapprovenote;
	private String reserve18;
	private UFDouble reserve6;
	private String reserve1;
	private UFBoolean reserve16;
	private UFBoolean reserve14;
	private UFDate reserve13;
	private String reserve3;
	private Integer dr;
	private String reserve4;
	

	public static final String PK_CORP = "pk_corp";
	public static final String RESERVE10 = "reserve10";
	public static final String RESERVE15 = "reserve15";
	public static final String VEMPLOYEEID = "vemployeeid";
	public static final String IJJWAY = "ijjway";
	public static final String RESERVE2 = "reserve2";
	public static final String RESERVE17 = "reserve17";
	public static final String RESERVE8 = "reserve8";
	public static final String VINVTYPE = "vinvtype";
	public static final String PK_TAXITEMS = "pk_taxitems";
	public static final String DBILLDATE = "dbilldate";
	public static final String RESERVE7 = "reserve7";
	public static final String RESERVE11 = "reserve11";
	public static final String RESERVE20 = "reserve20";
	public static final String VINVNAME = "vinvname";
	public static final String RESERVE9 = "reserve9";
	public static final String VMEMO = "vmemo";
	public static final String PK_INVBASDOC = "pk_invbasdoc";
	public static final String VOPERATORID = "voperatorid";
	public static final String VBILLNO = "vbillno";
	public static final String PK_INVCL = "pk_invcl";
	public static final String PK_MEASDOC = "pk_measdoc";
	public static final String BISJJWZ = "bisjjwz";
	public static final String CINVCODE = "cinvcode";
	public static final String RESERVE5 = "reserve5";
	public static final String PK_BUSITYPE = "pk_busitype";
	public static final String RESERVE19 = "reserve19";
	public static final String VAPPROVEID = "vapproveid";
	public static final String PK_DEPTDOC = "pk_deptdoc";
	public static final String VINVSPEC = "vinvspec";
	public static final String VAPPROVENOTE = "vapprovenote";
	public static final String RESERVE18 = "reserve18";
	public static final String RESERVE12 = "reserve12";
	public static final String RESERVE6 = "reserve6";
	public static final String DAPPROVEDATE = "dapprovedate";
	public static final String RESERVE1 = "reserve1";
	public static final String RESERVE16 = "reserve16";
	public static final String BISDCDX = "bisdcdx";
	public static final String RESERVE14 = "reserve14";
	public static final String RESERVE13 = "reserve13";
	public static final String PK_BILLTYPE = "pk_billtype";
	public static final String VBILLSTATUS = "vbillstatus";
	public static final String DMAKEDATE = "dmakedate";
	public static final String PK_INVBASDOC_TEMP = "pk_invbasdoc_temp";
	public static final String RESERVE3 = "reserve3";
	public static final String BISZYWZ = "biszywz";
	public static final String PK_INVMANDOC = "pk_invmandoc";
	public static final String RESERVE4 = "reserve4";
	public static final String PK_MATERIALS = "pk_materials";
			
	/**
	 * 属性pk_corp的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return String
	 */
	public String getPk_corp () {
		return pk_corp;
	}   
	/**
	 * 属性pk_corp的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newPk_corp String
	 */
	public void setPk_corp (String newPk_corp ) {
	 	this.pk_corp = newPk_corp;
	} 	  
	/**
	 * 属性ts的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return UFDateTime
	 */
	public UFDateTime getTs () {
		return ts;
	}   
	/**
	 * 属性ts的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newTs UFDateTime
	 */
	public void setTs (UFDateTime newTs ) {
	 	this.ts = newTs;
	} 	  
	/**
	 * 属性reserve10的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return UFDouble
	 */
	public UFDouble getReserve10 () {
		return reserve10;
	}   
	/**
	 * 属性reserve10的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newReserve10 UFDouble
	 */
	public void setReserve10 (UFDouble newReserve10 ) {
	 	this.reserve10 = newReserve10;
	} 	  
	/**
	 * 属性reserve15的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return UFBoolean
	 */
	public UFBoolean getReserve15 () {
		return reserve15;
	}   
	/**
	 * 属性reserve15的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newReserve15 UFBoolean
	 */
	public void setReserve15 (UFBoolean newReserve15 ) {
	 	this.reserve15 = newReserve15;
	} 	  
	/**
	 * 属性vemployeeid的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return String
	 */
	public String getVemployeeid () {
		return vemployeeid;
	}   
	/**
	 * 属性vemployeeid的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newVemployeeid String
	 */
	public void setVemployeeid (String newVemployeeid ) {
	 	this.vemployeeid = newVemployeeid;
	} 	  
	/**
	 * 属性reserve2的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return String
	 */
	public String getReserve2 () {
		return reserve2;
	}   
	/**
	 * 属性reserve2的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newReserve2 String
	 */
	public void setReserve2 (String newReserve2 ) {
	 	this.reserve2 = newReserve2;
	} 	  
	/**
	 * 属性reserve17的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return String
	 */
	public String getReserve17 () {
		return reserve17;
	}   
	/**
	 * 属性reserve17的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newReserve17 String
	 */
	public void setReserve17 (String newReserve17 ) {
	 	this.reserve17 = newReserve17;
	} 	  
	/**
	 * 属性reserve8的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return UFDouble
	 */
	public UFDouble getReserve8 () {
		return reserve8;
	}   
	/**
	 * 属性reserve8的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newReserve8 UFDouble
	 */
	public void setReserve8 (UFDouble newReserve8 ) {
	 	this.reserve8 = newReserve8;
	} 	  
	/**
	 * 属性vinvtype的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return String
	 */
	public String getVinvtype () {
		return vinvtype;
	}   
	/**
	 * 属性vinvtype的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newVinvtype String
	 */
	public void setVinvtype (String newVinvtype ) {
	 	this.vinvtype = newVinvtype;
	} 	  
	/**
	 * 属性pk_taxitems的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return String
	 */
	public String getPk_taxitems () {
		return pk_taxitems;
	}   
	/**
	 * 属性pk_taxitems的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newPk_taxitems String
	 */
	public void setPk_taxitems (String newPk_taxitems ) {
	 	this.pk_taxitems = newPk_taxitems;
	} 	  
	/**
	 * 属性dbilldate的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return UFDate
	 */
	public UFDate getDbilldate () {
		return dbilldate;
	}   
	/**
	 * 属性dbilldate的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newDbilldate UFDate
	 */
	public void setDbilldate (UFDate newDbilldate ) {
	 	this.dbilldate = newDbilldate;
	} 	  
	/**
	 * 属性reserve7的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return UFDouble
	 */
	public UFDouble getReserve7 () {
		return reserve7;
	}   
	/**
	 * 属性reserve7的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newReserve7 UFDouble
	 */
	public void setReserve7 (UFDouble newReserve7 ) {
	 	this.reserve7 = newReserve7;
	} 	  
	/**
	 * 属性reserve11的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return UFDate
	 */
	public UFDate getReserve11 () {
		return reserve11;
	}   
	/**
	 * 属性reserve11的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newReserve11 UFDate
	 */
	public void setReserve11 (UFDate newReserve11 ) {
	 	this.reserve11 = newReserve11;
	} 	  
	/**
	 * 属性reserve20的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return String
	 */
	public String getReserve20 () {
		return reserve20;
	}   
	/**
	 * 属性reserve20的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newReserve20 String
	 */
	public void setReserve20 (String newReserve20 ) {
	 	this.reserve20 = newReserve20;
	} 	  
	/**
	 * 属性vinvname的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return String
	 */
	public String getVinvname () {
		return vinvname;
	}   
	/**
	 * 属性vinvname的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newVinvname String
	 */
	public void setVinvname (String newVinvname ) {
	 	this.vinvname = newVinvname;
	} 	  
	/**
	 * 属性reserve9的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return UFDouble
	 */
	public UFDouble getReserve9 () {
		return reserve9;
	}   
	/**
	 * 属性reserve9的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newReserve9 UFDouble
	 */
	public void setReserve9 (UFDouble newReserve9 ) {
	 	this.reserve9 = newReserve9;
	} 	  
	/**
	 * 属性vmemo的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return String
	 */
	public String getVmemo () {
		return vmemo;
	}   
	/**
	 * 属性vmemo的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newVmemo String
	 */
	public void setVmemo (String newVmemo ) {
	 	this.vmemo = newVmemo;
	} 	  
	/**
	 * 属性pk_invbasdoc的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return String
	 */
	public String getPk_invbasdoc () {
		return pk_invbasdoc;
	}   
	/**
	 * 属性pk_invbasdoc的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newPk_invbasdoc String
	 */
	public void setPk_invbasdoc (String newPk_invbasdoc ) {
	 	this.pk_invbasdoc = newPk_invbasdoc;
	} 	  
	/**
	 * 属性voperatorid的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return String
	 */
	public String getVoperatorid () {
		return voperatorid;
	}   
	/**
	 * 属性voperatorid的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newVoperatorid String
	 */
	public void setVoperatorid (String newVoperatorid ) {
	 	this.voperatorid = newVoperatorid;
	} 	  
	/**
	 * 属性vbillno的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return String
	 */
	public String getVbillno () {
		return vbillno;
	}   
	/**
	 * 属性vbillno的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newVbillno String
	 */
	public void setVbillno (String newVbillno ) {
	 	this.vbillno = newVbillno;
	} 	  
	/**
	 * 属性pk_invcl的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return String
	 */
	public String getPk_invcl () {
		return pk_invcl;
	}   
	/**
	 * 属性pk_invcl的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newPk_invcl String
	 */
	public void setPk_invcl (String newPk_invcl ) {
	 	this.pk_invcl = newPk_invcl;
	} 	  
	/**
	 * 属性pk_measdoc的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return String
	 */
	public String getPk_measdoc () {
		return pk_measdoc;
	}   
	/**
	 * 属性pk_measdoc的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newPk_measdoc String
	 */
	public void setPk_measdoc (String newPk_measdoc ) {
	 	this.pk_measdoc = newPk_measdoc;
	} 	  
	/**
	 * 属性bisjjwz的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return UFBoolean
	 */
	public UFBoolean getBisjjwz () {
		return bisjjwz;
	}   
	/**
	 * 属性bisjjwz的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newBisjjwz UFBoolean
	 */
	public void setBisjjwz (UFBoolean newBisjjwz ) {
	 	this.bisjjwz = newBisjjwz;
	} 	  
	/**
	 * 属性cinvcode的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return String
	 */
	public String getCinvcode () {
		return cinvcode;
	}   
	/**
	 * 属性cinvcode的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newCinvcode String
	 */
	public void setCinvcode (String newCinvcode ) {
	 	this.cinvcode = newCinvcode;
	} 	  
	/**
	 * 属性reserve5的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return String
	 */
	public String getReserve5 () {
		return reserve5;
	}   
	/**
	 * 属性reserve5的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newReserve5 String
	 */
	public void setReserve5 (String newReserve5 ) {
	 	this.reserve5 = newReserve5;
	} 	  
	/**
	 * 属性pk_busitype的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return String
	 */
	public String getPk_busitype () {
		return pk_busitype;
	}   
	/**
	 * 属性pk_busitype的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newPk_busitype String
	 */
	public void setPk_busitype (String newPk_busitype ) {
	 	this.pk_busitype = newPk_busitype;
	} 	  
	/**
	 * 属性reserve19的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return String
	 */
	public String getReserve19 () {
		return reserve19;
	}   
	/**
	 * 属性reserve19的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newReserve19 String
	 */
	public void setReserve19 (String newReserve19 ) {
	 	this.reserve19 = newReserve19;
	} 	  
	/**
	 * 属性vapproveid的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return String
	 */
	public String getVapproveid () {
		return vapproveid;
	}   
	/**
	 * 属性vapproveid的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newVapproveid String
	 */
	public void setVapproveid (String newVapproveid ) {
	 	this.vapproveid = newVapproveid;
	} 	  
	/**
	 * 属性pk_deptdoc的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return String
	 */
	public String getPk_deptdoc () {
		return pk_deptdoc;
	}   
	/**
	 * 属性pk_deptdoc的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newPk_deptdoc String
	 */
	public void setPk_deptdoc (String newPk_deptdoc ) {
	 	this.pk_deptdoc = newPk_deptdoc;
	} 	  
	/**
	 * 属性vinvspec的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return String
	 */
	public String getVinvspec () {
		return vinvspec;
	}   
	/**
	 * 属性vinvspec的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newVinvspec String
	 */
	public void setVinvspec (String newVinvspec ) {
	 	this.vinvspec = newVinvspec;
	} 	  
	/**
	 * 属性vapprovenote的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return String
	 */
	public String getVapprovenote () {
		return vapprovenote;
	}   
	/**
	 * 属性vapprovenote的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newVapprovenote String
	 */
	public void setVapprovenote (String newVapprovenote ) {
	 	this.vapprovenote = newVapprovenote;
	} 	  
	/**
	 * 属性reserve18的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return String
	 */
	public String getReserve18 () {
		return reserve18;
	}   
	/**
	 * 属性reserve18的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newReserve18 String
	 */
	public void setReserve18 (String newReserve18 ) {
	 	this.reserve18 = newReserve18;
	} 	  
	/**
	 * 属性reserve12的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return UFDate
	 */
	public UFDate getReserve12 () {
		return reserve12;
	}   
	/**
	 * 属性reserve12的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newReserve12 UFDate
	 */
	public void setReserve12 (UFDate newReserve12 ) {
	 	this.reserve12 = newReserve12;
	} 	  
	/**
	 * 属性reserve6的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return UFDouble
	 */
	public UFDouble getReserve6 () {
		return reserve6;
	}   
	/**
	 * 属性reserve6的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newReserve6 UFDouble
	 */
	public void setReserve6 (UFDouble newReserve6 ) {
	 	this.reserve6 = newReserve6;
	} 	  
	/**
	 * 属性dapprovedate的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return UFDate
	 */
	public UFDate getDapprovedate () {
		return dapprovedate;
	}   
	/**
	 * 属性dapprovedate的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newDapprovedate UFDate
	 */
	public void setDapprovedate (UFDate newDapprovedate ) {
	 	this.dapprovedate = newDapprovedate;
	} 	  
	/**
	 * 属性reserve1的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return String
	 */
	public String getReserve1 () {
		return reserve1;
	}   
	/**
	 * 属性reserve1的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newReserve1 String
	 */
	public void setReserve1 (String newReserve1 ) {
	 	this.reserve1 = newReserve1;
	} 	  
	/**
	 * 属性reserve16的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return UFBoolean
	 */
	public UFBoolean getReserve16 () {
		return reserve16;
	}   
	/**
	 * 属性reserve16的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newReserve16 UFBoolean
	 */
	public void setReserve16 (UFBoolean newReserve16 ) {
	 	this.reserve16 = newReserve16;
	} 	  
	/**
	 * 属性bisdcdx的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return UFBoolean
	 */
	public UFBoolean getBisdcdx () {
		return bisdcdx;
	}   
	/**
	 * 属性bisdcdx的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newBisdcdx UFBoolean
	 */
	public void setBisdcdx (UFBoolean newBisdcdx ) {
	 	this.bisdcdx = newBisdcdx;
	} 	  
	/**
	 * 属性reserve14的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return UFBoolean
	 */
	public UFBoolean getReserve14 () {
		return reserve14;
	}   
	/**
	 * 属性reserve14的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newReserve14 UFBoolean
	 */
	public void setReserve14 (UFBoolean newReserve14 ) {
	 	this.reserve14 = newReserve14;
	} 	  
	/**
	 * 属性reserve13的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return UFDate
	 */
	public UFDate getReserve13 () {
		return reserve13;
	}   
	/**
	 * 属性reserve13的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newReserve13 UFDate
	 */
	public void setReserve13 (UFDate newReserve13 ) {
	 	this.reserve13 = newReserve13;
	} 	  
	/**
	 * 属性pk_billtype的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return String
	 */
	public String getPk_billtype () {
		return pk_billtype;
	}   
	/**
	 * 属性pk_billtype的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newPk_billtype String
	 */
	public void setPk_billtype (String newPk_billtype ) {
	 	this.pk_billtype = newPk_billtype;
	} 	  
	/**
	 * 属性vbillstatus的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return UFDouble
	 */
	public Integer getVbillstatus () {
		return vbillstatus;
	}   
	/**
	 * 属性vbillstatus的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newVbillstatus UFDouble
	 */
	public void setVbillstatus (Integer newVbillstatus ) {
	 	this.vbillstatus = newVbillstatus;
	} 	  
	/**
	 * 属性dmakedate的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return UFDate
	 */
	public UFDate getDmakedate () {
		return dmakedate;
	}   
	/**
	 * 属性dmakedate的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newDmakedate UFDate
	 */
	public void setDmakedate (UFDate newDmakedate ) {
	 	this.dmakedate = newDmakedate;
	} 	  
	/**
	 * 属性pk_invbasdoc_temp的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return String
	 */
	public String getPk_invbasdoc_temp () {
		return pk_invbasdoc_temp;
	}   
	/**
	 * 属性pk_invbasdoc_temp的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newPk_invbasdoc_temp String
	 */
	public void setPk_invbasdoc_temp (String newPk_invbasdoc_temp ) {
	 	this.pk_invbasdoc_temp = newPk_invbasdoc_temp;
	} 	  
	/**
	 * 属性reserve3的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return String
	 */
	public String getReserve3 () {
		return reserve3;
	}   
	/**
	 * 属性reserve3的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newReserve3 String
	 */
	public void setReserve3 (String newReserve3 ) {
	 	this.reserve3 = newReserve3;
	} 	  
	/**
	 * 属性dr的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return UFDouble
	 */
	public Integer getDr () {
		return dr;
	}   
	/**
	 * 属性dr的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newDr UFDouble
	 */
	public void setDr (Integer newDr ) {
	 	this.dr = newDr;
	} 	  
	/**
	 * 属性biszywz的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return UFBoolean
	 */
	public UFBoolean getBiszywz () {
		return biszywz;
	}   
	/**
	 * 属性biszywz的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newBiszywz UFBoolean
	 */
	public void setBiszywz (UFBoolean newBiszywz ) {
	 	this.biszywz = newBiszywz;
	} 	  
	/**
	 * 属性pk_invmandoc的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return String
	 */
	public String getPk_invmandoc () {
		return pk_invmandoc;
	}   
	/**
	 * 属性pk_invmandoc的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newPk_invmandoc String
	 */
	public void setPk_invmandoc (String newPk_invmandoc ) {
	 	this.pk_invmandoc = newPk_invmandoc;
	} 	  
	/**
	 * 属性reserve4的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return String
	 */
	public String getReserve4 () {
		return reserve4;
	}   
	/**
	 * 属性reserve4的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newReserve4 String
	 */
	public void setReserve4 (String newReserve4 ) {
	 	this.reserve4 = newReserve4;
	} 	  
	/**
	 * 属性pk_materials的Getter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @return String
	 */
	public String getPk_materials () {
		return pk_materials;
	}   
	/**
	 * 属性pk_materials的Setter方法.
	 * 创建日期:2011-02-24 12:02:03
	 * @param newPk_materials String
	 */
	public void setPk_materials (String newPk_materials ) {
	 	this.pk_materials = newPk_materials;
	} 	  
 
	/**
	  * <p>取得父VO主键字段.
	  * <p>
	  * 创建日期:2011-02-24 12:02:03
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	    return null;
	}   
    
	/**
	  * <p>取得表主键.
	  * <p>
	  * 创建日期:2011-02-24 12:02:03
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	  return "pk_materials";
	}
    
	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:2011-02-24 12:02:03
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "HG_NEW_MATERIALS";
	}    
    
    /**
	  * 按照默认方式创建构造子.
	  *
	  * 创建日期:2011-02-24 12:02:03
	  */
     public NewMaterialsVO() {
		super();	
	}
	public String getInvcode() {
		return invcode;
	}
	public void setInvcode(String invcode) {
		this.invcode = invcode;
	}
	public Integer getIjjway() {
		return ijjway;
	}
	public void setIjjway(Integer ijjway) {
		this.ijjway = ijjway;
	}
	public UFDouble getNplanprice() {
		return nplanprice;
	}
	public void setNplanprice(UFDouble nplanprice) {
		this.nplanprice = nplanprice;
	}
	public String getInvmnecode() {
		return invmnecode;
	}
	public void setInvmnecode(String invmnecode) {
		this.invmnecode = invmnecode;
	}
	public String getVtechstan() {
		return vtechstan;
	}
	public void setVtechstan(String vtechstan) {
		this.vtechstan = vtechstan;
	}
	public String getVmaterial() {
		return vmaterial;
	}
	public void setVmaterial(String vmaterial) {
		this.vmaterial = vmaterial;
	}
	public UFBoolean getDiscountflag() {
		return discountflag;
	}
	public void setDiscountflag(UFBoolean discountflag) {
		this.discountflag = discountflag;
	}
	public UFBoolean getLaborflag() {
		return laborflag;
	}
	public void setLaborflag(UFBoolean laborflag) {
		this.laborflag = laborflag;
	}
	public String getCcorpid() {
		return ccorpid;
	}
	public void setCcorpid(String ccorpid) {
		this.ccorpid = ccorpid;
	}    
} 

