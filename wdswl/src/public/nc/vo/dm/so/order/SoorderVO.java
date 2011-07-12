/***************************************************************\
 *     The skeleton of this class is generated by an automatic *
 * code generator for NC product. It is based on Velocity.     *
\***************************************************************/
package nc.vo.dm.so.order;

import java.util.ArrayList;
import nc.vo.pub.*;
import nc.vo.pub.lang.*;
import nc.vo.scm.pu.PuPubVO;

/**
 * <b> 销售运单 </b>
 * 
 * <p>
 * 在此处添加此类的描述信息
 * </p>
 * 
 * 创建日期:2011-3-24
 * 
 * @author author
 * @version Your Project 1.0
 */
public class SoorderVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8299919978628715989L;

	// 打印次数
	public Integer iprintcount;
	// 主键
	public String pk_soorder;
	// 需货时间
	public UFDate denddate;
	// 调拨员
	public String pk_transer;
	// 客商基本id
	public String pk_cubasdoc;
	// 发货站
	public String pk_outwhouse;
	// 运输方式
	public Integer itranstype;
	// 装车时间
	public UFDate dbegindate;
	// 业务代表电话
	public String vyedbtel;
	// 本次共运（件）
	public UFDouble ntotalnum;
	// 监管员
	public String pk_manageperson;
	// 联系人id
	public String pk_receiveperson;
	// 收货站
	public String pk_inwhouse;
	// 业务代表
	public String pk_yedb;
	// 客商管理id
	public String pk_cumandoc;
	//客商联系人电话
	public String vtelphone;
	//是否冻结
	public UFBoolean fisended;
	//承运商:
	public String pk_transcorp;
	//车号：
	public String vcardno;
	//司机签字：
	public String vdriver;
	//司机电话： 
	public String vdrivertel;
	//接受件数：
	public UFDouble nacceptnum;
	//公里数：
	public UFDouble ngls;
	//运价：
	public UFDouble ntranprice;
	//运费调整值(元):
	public UFDouble nadjustprice;
	//运费调整 单位:
	public Integer iadjusttype;
	//运费（元）：
	public UFDouble ntransmny;
	//收货日期：
	public UFDate dacceptdate;
	//划价人签字：
	public String vcolpersonid;
	//审批时间：
	public UFDateTime vapprovetime;
	//收货地
	public String custareaid;
	// ------------------------------------------------------
	public String csalecorpid;// 销售组织
	public String ccalbodyid;// 库存组织
	public String creceiptcustomerid;// 收货单位
	public String vinaddress;// 收货地址

	// 预留字段
	public String reserve1;
	public String reserve2;
	public String reserve3;
	public String reserve4;
	public String reserve5;
	public String reserve6;
	public String reserve7;
	public UFDouble reserve8;
	public UFDouble reserve9;
	public UFDouble reserve10;
	public UFDate reserve11;
	public UFDate reserve12;
	public UFDate reserve13;
	public UFBoolean reserve14;
	public UFBoolean reserve15;
	public UFBoolean reserve16;
	// 自定义项
	public String vdef1;
	public String vdef2;
	public String vdef3;
	public String vdef4;
	public String vdef5;
	public String vdef6;
	public String vdef7;
	public String vdef8;
	public String vdef9;
	public String vdef10;
	public UFDateTime ts;
	public Integer dr;
	// 备注
	public String vmemo;

	// 公司
	public String pk_corp;
	// 单据号
	public String vbillno;
	// 单据日期
	public UFDate dbilldate;
	// 单据类型
	public String pk_billtype;
	// 单据状态
	public Integer vbillstatus;
	// 业务类型
	public String pk_busitype;
	// 业务员id
	public String vemployeeid;
	// 部门id
	public String pk_deptdoc;
	// 制单人
	public String voperatorid;
	// 制单日期
	public UFDate dmakedate;
	// 审批人
	public String vapproveid;
	// 审批日期
	public UFDate dapprovedate;
	// 审批批语
	public String vapprovenote;
	//发运状态
	public Integer 	itransstatus;
	//计算类型
	public Integer icoltype;
	
	public static final String ICOLTYPE ="icoltype";
//	public static final String ITRANSSTATUS ="itransstatus";	
//	public static final String CUSTAREAID ="custareaid";
//	public static final String FISENDED ="fisended";
//	public static final String PK_TRANSCORP="pk_transcorp";
//	public static final String VCARDNO="vcardno";
//	public static final String VDRIVER="vdriver";
//	public static final String VDRIVERTEL="vdrivertel";
//	public static final String NACCEPTNUM="nacceptnum";
//	public static final String NGLS="ngls";
//	public static final String NTRANPRICE="ntranprice";
//	public static final String NADJUSTPRICE="nadjustprice";
//	public static final String IADJUSTTYPE="iadjusttype";
//	public static final String NTRANSMNY="ntransmny";
//	public static final String DACCEPTDATE="dacceptdate";
//	public static final String VCOLPERSONID="vcolpersonid";
//	public static final String VAPPROVETIEM="vapprovetime";
//
//	public static final String RESERVE5 = "reserve5";
//	public static final String PK_CORP = "pk_corp";
//	public static final String TS = "ts";
//	public static final String VDEF4 = "vdef4";
//	public static final String VOPERATORID = "voperatorid";
//	public static final String VAPPROVENOTE = "vapprovenote";
//	public static final String VEMPLOYEEID = "vemployeeid";
//	public static final String RESERVE12 = "reserve12";
//	public static final String PK_SOORDER = "pk_soorder";
//	public static final String RESERVE6 = "reserve6";
//	public static final String RESERVE14 = "reserve14";
//	public static final String DENDDATE = "denddate";
//	public static final String RESERVE10 = "reserve10";
//	public static final String PK_TRANSER = "pk_transer";
//	public static final String PK_DEPTDOC = "pk_deptdoc";
//	public static final String PK_CUBASDOC = "pk_cubasdoc";
//	public static final String VDEF3 = "vdef3";
//	public static final String PK_OUTWHOUSE = "pk_outwhouse";
//	public static final String RESERVE1 = "reserve1";
//	public static final String RESERVE16 = "reserve16";
//	public static final String RESERVE15 = "reserve15";
//	public static final String ITRANSTYPE = "itranstype";
//	public static final String DBEGINDATE = "dbegindate";
//	public static final String RESERVE2 = "reserve2";
//	public static final String VDEF10 = "vdef10";
//	public static final String VDEF5 = "vdef5";
//	public static final String RESERVE4 = "reserve4";
//	public static final String DMAKEDATE = "dmakedate";
//	public static final String VDEF7 = "vdef7";
//	public static final String VYEDBTEL = "vyedbtel";
//	public static final String PK_BILLTYPE = "pk_billtype";
//	public static final String VBILLSTATUS = "vbillstatus";
//	public static final String NTOTALNUM = "ntotalnum";
//	public static final String VINADDRESS = "vinaddress";
//	public static final String VDEF2 = "vdef2";
//	public static final String RESERVE11 = "reserve11";
//	public static final String VMEMO = "vmemo";
//	public static final String PK_BUSITYPE = "pk_busitype";
//	public static final String PK_MANAGEPERSON = "pk_manageperson";
//	public static final String VDEF1 = "vdef1";
//	public static final String PK_RECEIVEPERSON = "pk_receiveperson";
//	public static final String RESERVE3 = "reserve3";
//	public static final String VBILLNO = "vbillno";
//	public static final String DBILLDATE = "dbilldate";
//	public static final String PK_INWHOUSE = "pk_inwhouse";
//	public static final String RESERVE13 = "reserve13";
//	public static final String RESERVE8 = "reserve8";
//	public static final String RESERVE9 = "reserve9";
//	public static final String VDEF9 = "vdef9";
//	public static final String PK_YEDB = "pk_yedb";
//	public static final String PK_CUMANDOC = "pk_cumandoc";
//	public static final String DAPPROVEDATE = "dapprovedate";
//	public static final String VDEF8 = "vdef8";
//	public static final String VTELPHONE = "vtelphone";
//	public static final String VAPPROVEID = "vapproveid";
//	public static final String RESERVE7 = "reserve7";
//	public static final String VDEF6 = "vdef6";
//	public static final String DR = "dr";

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
	 * 属性reserve5的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return String
	 */
	public String getReserve5() {
		return reserve5;
	}

	/**
	 * 属性reserve5的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newReserve5
	 *            String
	 */
	public void setReserve5(String newReserve5) {

		reserve5 = newReserve5;
	}

	/**
	 * 属性pk_corp的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return String
	 */
	public String getPk_corp() {
		return pk_corp;
	}

	/**
	 * 属性pk_corp的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newPk_corp
	 *            String
	 */
	public void setPk_corp(String newPk_corp) {

		pk_corp = newPk_corp;
	}

	/**
	 * 属性vdef4的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return String
	 */
	public String getVdef4() {
		return vdef4;
	}

	/**
	 * 属性vdef4的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newVdef4
	 *            String
	 */
	public void setVdef4(String newVdef4) {

		vdef4 = newVdef4;
	}

	/**
	 * 属性voperatorid的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return String
	 */
	public String getVoperatorid() {
		return voperatorid;
	}

	/**
	 * 属性voperatorid的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newVoperatorid
	 *            String
	 */
	public void setVoperatorid(String newVoperatorid) {

		voperatorid = newVoperatorid;
	}

	/**
	 * 属性vapprovenote的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return String
	 */
	public String getVapprovenote() {
		return vapprovenote;
	}

	/**
	 * 属性vapprovenote的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newVapprovenote
	 *            String
	 */
	public void setVapprovenote(String newVapprovenote) {

		vapprovenote = newVapprovenote;
	}

	/**
	 * 属性vemployeeid的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return String
	 */
	public String getVemployeeid() {
		return vemployeeid;
	}

	/**
	 * 属性vemployeeid的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newVemployeeid
	 *            String
	 */
	public void setVemployeeid(String newVemployeeid) {

		vemployeeid = newVemployeeid;
	}

	/**
	 * 属性reserve12的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return UFDate
	 */
	public UFDate getReserve12() {
		return reserve12;
	}

	/**
	 * 属性reserve12的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newReserve12
	 *            UFDate
	 */
	public void setReserve12(UFDate newReserve12) {

		reserve12 = newReserve12;
	}

	/**
	 * 属性pk_soorder的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return String
	 */
	public String getPk_soorder() {
		return pk_soorder;
	}

	/**
	 * 属性pk_soorder的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newPk_soorder
	 *            String
	 */
	public void setPk_soorder(String newPk_soorder) {

		pk_soorder = newPk_soorder;
	}

	/**
	 * 属性reserve6的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return String
	 */
	public String getReserve6() {
		return reserve6;
	}

	/**
	 * 属性reserve6的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newReserve6
	 *            String
	 */
	public void setReserve6(String newReserve6) {

		reserve6 = newReserve6;
	}

	/**
	 * 属性reserve14的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return UFBoolean
	 */
	public UFBoolean getReserve14() {
		return reserve14;
	}

	/**
	 * 属性reserve14的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newReserve14
	 *            UFBoolean
	 */
	public void setReserve14(UFBoolean newReserve14) {

		reserve14 = newReserve14;
	}

	/**
	 * 属性denddate的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return UFDate
	 */
	public UFDate getDenddate() {
		return denddate;
	}

	/**
	 * 属性denddate的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newDenddate
	 *            UFDate
	 */
	public void setDenddate(UFDate newDenddate) {

		denddate = newDenddate;
	}

	/**
	 * 属性reserve10的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return UFDouble
	 */
	public UFDouble getReserve10() {
		return reserve10;
	}

	/**
	 * 属性reserve10的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newReserve10
	 *            UFDouble
	 */
	public void setReserve10(UFDouble newReserve10) {

		reserve10 = newReserve10;
	}

	/**
	 * 属性pk_transer的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return String
	 */
	public String getPk_transer() {
		return pk_transer;
	}

	/**
	 * 属性pk_transer的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newPk_transer
	 *            String
	 */
	public void setPk_transer(String newPk_transer) {

		pk_transer = newPk_transer;
	}

	/**
	 * 属性pk_deptdoc的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return String
	 */
	public String getPk_deptdoc() {
		return pk_deptdoc;
	}

	/**
	 * 属性pk_deptdoc的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newPk_deptdoc
	 *            String
	 */
	public void setPk_deptdoc(String newPk_deptdoc) {

		pk_deptdoc = newPk_deptdoc;
	}

	/**
	 * 属性pk_cubasdoc的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return String
	 */
	public String getPk_cubasdoc() {
		return pk_cubasdoc;
	}

	/**
	 * 属性pk_cubasdoc的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newPk_cubasdoc
	 *            String
	 */
	public void setPk_cubasdoc(String newPk_cubasdoc) {

		pk_cubasdoc = newPk_cubasdoc;
	}

	/**
	 * 属性vdef3的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return String
	 */
	public String getVdef3() {
		return vdef3;
	}

	/**
	 * 属性vdef3的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newVdef3
	 *            String
	 */
	public void setVdef3(String newVdef3) {

		vdef3 = newVdef3;
	}

	/**
	 * 属性pk_outwhouse的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return String
	 */
	public String getPk_outwhouse() {
		return pk_outwhouse;
	}

	/**
	 * 属性pk_outwhouse的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newPk_outwhouse
	 *            String
	 */
	public void setPk_outwhouse(String newPk_outwhouse) {

		pk_outwhouse = newPk_outwhouse;
	}

	/**
	 * 属性reserve1的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return String
	 */
	public String getReserve1() {
		return reserve1;
	}

	/**
	 * 属性reserve1的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newReserve1
	 *            String
	 */
	public void setReserve1(String newReserve1) {

		reserve1 = newReserve1;
	}

	/**
	 * 属性reserve16的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return UFBoolean
	 */
	public UFBoolean getReserve16() {
		return reserve16;
	}

	/**
	 * 属性reserve16的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newReserve16
	 *            UFBoolean
	 */
	public void setReserve16(UFBoolean newReserve16) {

		reserve16 = newReserve16;
	}

	/**
	 * 属性reserve15的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return UFBoolean
	 */
	public UFBoolean getReserve15() {
		return reserve15;
	}

	/**
	 * 属性reserve15的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newReserve15
	 *            UFBoolean
	 */
	public void setReserve15(UFBoolean newReserve15) {

		reserve15 = newReserve15;
	}

	/**
	 * 属性itranstype的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return Integer
	 */
	public Integer getItranstype() {
		return itranstype;
	}

	/**
	 * 属性itranstype的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newItranstype
	 *            Integer
	 */
	public void setItranstype(Integer newItranstype) {

		itranstype = newItranstype;
	}

	/**
	 * 属性dbegindate的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return UFDate
	 */
	public UFDate getDbegindate() {
		return dbegindate;
	}

	/**
	 * 属性dbegindate的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newDbegindate
	 *            UFDate
	 */
	public void setDbegindate(UFDate newDbegindate) {

		dbegindate = newDbegindate;
	}

	/**
	 * 属性reserve2的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return String
	 */
	public String getReserve2() {
		return reserve2;
	}

	/**
	 * 属性reserve2的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newReserve2
	 *            String
	 */
	public void setReserve2(String newReserve2) {

		reserve2 = newReserve2;
	}

	/**
	 * 属性vdef10的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return String
	 */
	public String getVdef10() {
		return vdef10;
	}

	/**
	 * 属性vdef10的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newVdef10
	 *            String
	 */
	public void setVdef10(String newVdef10) {

		vdef10 = newVdef10;
	}

	/**
	 * 属性vdef5的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return String
	 */
	public String getVdef5() {
		return vdef5;
	}

	/**
	 * 属性vdef5的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newVdef5
	 *            String
	 */
	public void setVdef5(String newVdef5) {

		vdef5 = newVdef5;
	}

	/**
	 * 属性reserve4的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return String
	 */
	public String getReserve4() {
		return reserve4;
	}

	/**
	 * 属性reserve4的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newReserve4
	 *            String
	 */
	public void setReserve4(String newReserve4) {

		reserve4 = newReserve4;
	}

	/**
	 * 属性dmakedate的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return UFDate
	 */
	public UFDate getDmakedate() {
		return dmakedate;
	}

	/**
	 * 属性dmakedate的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newDmakedate
	 *            UFDate
	 */
	public void setDmakedate(UFDate newDmakedate) {

		dmakedate = newDmakedate;
	}

	/**
	 * 属性vdef7的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return String
	 */
	public String getVdef7() {
		return vdef7;
	}

	/**
	 * 属性vdef7的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newVdef7
	 *            String
	 */
	public void setVdef7(String newVdef7) {

		vdef7 = newVdef7;
	}

	/**
	 * 属性vyedbtel的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return String
	 */
	public String getVyedbtel() {
		return vyedbtel;
	}

	/**
	 * 属性vyedbtel的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newVyedbtel
	 *            String
	 */
	public void setVyedbtel(String newVyedbtel) {

		vyedbtel = newVyedbtel;
	}

	/**
	 * 属性pk_billtype的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return String
	 */
	public String getPk_billtype() {
		return pk_billtype;
	}

	/**
	 * 属性pk_billtype的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newPk_billtype
	 *            String
	 */
	public void setPk_billtype(String newPk_billtype) {

		pk_billtype = newPk_billtype;
	}

	/**
	 * 属性vbillstatus的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return Integer
	 */
	public Integer getVbillstatus() {
		return vbillstatus;
	}

	/**
	 * 属性vbillstatus的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newVbillstatus
	 *            Integer
	 */
	public void setVbillstatus(Integer newVbillstatus) {

		vbillstatus = newVbillstatus;
	}

	/**
	 * 属性ntotalnum的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return UFDouble
	 */
	public UFDouble getNtotalnum() {
		return ntotalnum;
	}

	/**
	 * 属性ntotalnum的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newNtotalnum
	 *            UFDouble
	 */
	public void setNtotalnum(UFDouble newNtotalnum) {

		ntotalnum = newNtotalnum;
	}

	/**
	 * 属性vinaddress的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return String
	 */
	public String getVinaddress() {
		return vinaddress;
	}

	/**
	 * 属性vinaddress的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newVinaddress
	 *            String
	 */
	public void setVinaddress(String newVinaddress) {

		vinaddress = newVinaddress;
	}

	/**
	 * 属性vdef2的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return String
	 */
	public String getVdef2() {
		return vdef2;
	}

	/**
	 * 属性vdef2的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newVdef2
	 *            String
	 */
	public void setVdef2(String newVdef2) {

		vdef2 = newVdef2;
	}

	/**
	 * 属性reserve11的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return UFDate
	 */
	public UFDate getReserve11() {
		return reserve11;
	}

	/**
	 * 属性reserve11的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newReserve11
	 *            UFDate
	 */
	public void setReserve11(UFDate newReserve11) {

		reserve11 = newReserve11;
	}

	/**
	 * 属性vmemo的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return String
	 */
	public String getVmemo() {
		return vmemo;
	}

	/**
	 * 属性vmemo的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newVmemo
	 *            String
	 */
	public void setVmemo(String newVmemo) {

		vmemo = newVmemo;
	}

	/**
	 * 属性pk_busitype的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return String
	 */
	public String getPk_busitype() {
		return pk_busitype;
	}

	/**
	 * 属性pk_busitype的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newPk_busitype
	 *            String
	 */
	public void setPk_busitype(String newPk_busitype) {

		pk_busitype = newPk_busitype;
	}

	/**
	 * 属性pk_manageperson的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return String
	 */
	public String getPk_manageperson() {
		return pk_manageperson;
	}

	/**
	 * 属性pk_manageperson的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newPk_manageperson
	 *            String
	 */
	public void setPk_manageperson(String newPk_manageperson) {

		pk_manageperson = newPk_manageperson;
	}

	/**
	 * 属性vdef1的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return String
	 */
	public String getVdef1() {
		return vdef1;
	}

	/**
	 * 属性vdef1的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newVdef1
	 *            String
	 */
	public void setVdef1(String newVdef1) {

		vdef1 = newVdef1;
	}

	/**
	 * 属性pk_receiveperson的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return String
	 */
	public String getPk_receiveperson() {
		return pk_receiveperson;
	}

	/**
	 * 属性pk_receiveperson的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newPk_receiveperson
	 *            String
	 */
	public void setPk_receiveperson(String newPk_receiveperson) {

		pk_receiveperson = newPk_receiveperson;
	}

	/**
	 * 属性reserve3的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return String
	 */
	public String getReserve3() {
		return reserve3;
	}

	/**
	 * 属性reserve3的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newReserve3
	 *            String
	 */
	public void setReserve3(String newReserve3) {

		reserve3 = newReserve3;
	}

	/**
	 * 属性vbillno的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return String
	 */
	public String getVbillno() {
		return vbillno;
	}

	/**
	 * 属性vbillno的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newVbillno
	 *            String
	 */
	public void setVbillno(String newVbillno) {

		vbillno = newVbillno;
	}

	/**
	 * 属性dbilldate的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return UFDate
	 */
	public UFDate getDbilldate() {
		return dbilldate;
	}

	/**
	 * 属性dbilldate的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newDbilldate
	 *            UFDate
	 */
	public void setDbilldate(UFDate newDbilldate) {

		dbilldate = newDbilldate;
	}

	/**
	 * 属性pk_inwhouse的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return String
	 */
	public String getPk_inwhouse() {
		return pk_inwhouse;
	}

	/**
	 * 属性pk_inwhouse的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newPk_inwhouse
	 *            String
	 */
	public void setPk_inwhouse(String newPk_inwhouse) {

		pk_inwhouse = newPk_inwhouse;
	}

	/**
	 * 属性reserve13的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return UFDate
	 */
	public UFDate getReserve13() {
		return reserve13;
	}

	/**
	 * 属性reserve13的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newReserve13
	 *            UFDate
	 */
	public void setReserve13(UFDate newReserve13) {

		reserve13 = newReserve13;
	}

	/**
	 * 属性reserve8的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return UFDouble
	 */
	public UFDouble getReserve8() {
		return reserve8;
	}

	/**
	 * 属性reserve8的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newReserve8
	 *            UFDouble
	 */
	public void setReserve8(UFDouble newReserve8) {

		reserve8 = newReserve8;
	}

	/**
	 * 属性reserve9的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return UFDouble
	 */
	public UFDouble getReserve9() {
		return reserve9;
	}

	/**
	 * 属性reserve9的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newReserve9
	 *            UFDouble
	 */
	public void setReserve9(UFDouble newReserve9) {

		reserve9 = newReserve9;
	}

	/**
	 * 属性vdef9的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return String
	 */
	public String getVdef9() {
		return vdef9;
	}

	/**
	 * 属性vdef9的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newVdef9
	 *            String
	 */
	public void setVdef9(String newVdef9) {

		vdef9 = newVdef9;
	}

	/**
	 * 属性pk_yedb的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return String
	 */
	public String getPk_yedb() {
		return pk_yedb;
	}

	/**
	 * 属性pk_yedb的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newPk_yedb
	 *            String
	 */
	public void setPk_yedb(String newPk_yedb) {

		pk_yedb = newPk_yedb;
	}

	/**
	 * 属性pk_cumandoc的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return String
	 */
	public String getPk_cumandoc() {
		return pk_cumandoc;
	}

	/**
	 * 属性pk_cumandoc的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newPk_cumandoc
	 *            String
	 */
	public void setPk_cumandoc(String newPk_cumandoc) {

		pk_cumandoc = newPk_cumandoc;
	}

	/**
	 * 属性dapprovedate的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return UFDate
	 */
	public UFDate getDapprovedate() {
		return dapprovedate;
	}

	/**
	 * 属性dapprovedate的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newDapprovedate
	 *            UFDate
	 */
	public void setDapprovedate(UFDate newDapprovedate) {

		dapprovedate = newDapprovedate;
	}

	/**
	 * 属性vdef8的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return String
	 */
	public String getVdef8() {
		return vdef8;
	}

	/**
	 * 属性vdef8的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newVdef8
	 *            String
	 */
	public void setVdef8(String newVdef8) {

		vdef8 = newVdef8;
	}

	/**
	 * 属性vtelphone的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return String
	 */
	public String getVtelphone() {
		return vtelphone;
	}

	/**
	 * 属性vtelphone的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newVtelphone
	 *            String
	 */
	public void setVtelphone(String newVtelphone) {

		vtelphone = newVtelphone;
	}

	/**
	 * 属性vapproveid的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return String
	 */
	public String getVapproveid() {
		return vapproveid;
	}

	/**
	 * 属性vapproveid的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newVapproveid
	 *            String
	 */
	public void setVapproveid(String newVapproveid) {

		vapproveid = newVapproveid;
	}

	/**
	 * 属性reserve7的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return String
	 */
	public String getReserve7() {
		return reserve7;
	}

	/**
	 * 属性reserve7的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newReserve7
	 *            String
	 */
	public void setReserve7(String newReserve7) {

		reserve7 = newReserve7;
	}

	/**
	 * 属性vdef6的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return String
	 */
	public String getVdef6() {
		return vdef6;
	}

	/**
	 * 属性vdef6的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newVdef6
	 *            String
	 */
	public void setVdef6(String newVdef6) {

		vdef6 = newVdef6;
	}

	/**
	 * 属性DR的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return Integer
	 */

	/**
	 * 验证对象各属性之间的数据逻辑正确性.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @exception nc.vo.pub.ValidationException
	 *                如果验证失败,抛出 ValidationException,对错误进行解释.
	 */
	public void validate() throws ValidationException {

		ArrayList errFields = new ArrayList(); // errFields record those null

		// fields that cannot be null.
		// 检查是否为不允许空的字段赋了空值,你可能需要修改下面的提示信息:

		if (pk_soorder == null) {
			errFields.add(new String("pk_soorder"));
		}

		StringBuffer message = new StringBuffer();
		message.append("下列字段不能为空:");
		if (errFields.size() > 0) {
			String[] temp = (String[]) errFields.toArray(new String[0]);
			message.append(temp[0]);
			for (int i = 1; i < temp.length; i++) {
				message.append(",");
				message.append(temp[i]);
			}
			throw new NullFieldException(message.toString());
		}
	}
	
	public void validateOnPushSave() throws ValidationException {
//		客户不能为空
		if(PuPubVO.getString_TrimZeroLenAsNull(getPk_cubasdoc())==null || PuPubVO.getString_TrimZeroLenAsNull(getPk_cumandoc())==null)
			throw new ValidationException("客户不能为空");
		if(PuPubVO.getString_TrimZeroLenAsNull(getPk_outwhouse())==null)
			throw new ValidationException("发货站为空");
		
	}

	/**
	 * <p>
	 * 取得父VO主键字段.
	 * <p>
	 * 创建日期:2011-3-24
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getParentPKFieldName() {

		return null;

	}

	/**
	 * <p>
	 * 取得表主键.
	 * <p>
	 * 创建日期:2011-3-24
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPKFieldName() {
		return "pk_soorder";
	}

	/**
	 * <p>
	 * 返回表名称.
	 * <p>
	 * 创建日期:2011-3-24
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {

		return "wds_soorder";
	}

	/**
	 * 按照默认方式创建构造子.
	 * 
	 * 创建日期:2011-3-24
	 */
	public SoorderVO() {

		super();
	}

	/**
	 * 使用主键进行初始化的构造子.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newPk_soorder
	 *            主键值
	 */
	public SoorderVO(String newPk_soorder) {

		// 为主键字段赋值:
		pk_soorder = newPk_soorder;

	}

	/**
	 * 返回对象标识,用来唯一定位对象.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return String
	 */
	public String getPrimaryKey() {

		return pk_soorder;

	}

	/**
	 * 设置对象标识,用来唯一定位对象.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newPk_soorder
	 *            String
	 */
	public void setPrimaryKey(String newPk_soorder) {

		pk_soorder = newPk_soorder;

	}

	/**
	 * 返回数值对象的显示名称.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return java.lang.String 返回数值对象的显示名称.
	 */
	public String getEntityName() {

		return "wds_soorder";

	}

	public String getCsalecorpid() {
		return csalecorpid;
	}

	public void setCsalecorpid(String csalecorpid) {
		this.csalecorpid = csalecorpid;
	}

	public String getCcalbodyid() {
		return ccalbodyid;
	}

	public void setCcalbodyid(String ccalbodyid) {
		this.ccalbodyid = ccalbodyid;
	}

	public String getCreceiptcustomerid() {
		return creceiptcustomerid;
	}

	public void setCreceiptcustomerid(String creceiptcustomerid) {
		this.creceiptcustomerid = creceiptcustomerid;
	}

	public Integer getIprintcount() {
		return iprintcount;
	}

	public void setIprintcount(Integer iprintcount) {
		this.iprintcount = iprintcount;
	}

	public UFBoolean getFisended() {
		return fisended;
	}

	public void setFisended(UFBoolean fisended) {
		this.fisended = fisended;
	}

	public String getPk_transcorp() {
		return pk_transcorp;
	}

	public void setPk_transcorp(String pk_transcorp) {
		this.pk_transcorp = pk_transcorp;
	}

	public String getVcardno() {
		return vcardno;
	}

	public void setVcardno(String vcardno) {
		this.vcardno = vcardno;
	}

	public String getVdriver() {
		return vdriver;
	}

	public void setVdriver(String vdriver) {
		this.vdriver = vdriver;
	}

	public String getVdrivertel() {
		return vdrivertel;
	}

	public void setVdrivertel(String vdrivertel) {
		this.vdrivertel = vdrivertel;
	}

	public UFDouble getNacceptnum() {
		return nacceptnum;
	}

	public void setNacceptnum(UFDouble nacceptnum) {
		this.nacceptnum = nacceptnum;
	}

	public UFDouble getNgls() {
		return ngls;
	}

	public void setNgls(UFDouble ngls) {
		this.ngls = ngls;
	}

	public UFDouble getNtranprice() {
		return ntranprice;
	}

	public void setNtranprice(UFDouble ntranprice) {
		this.ntranprice = ntranprice;
	}

	public UFDouble getNadjustprice() {
		return nadjustprice;
	}

	public void setNadjustprice(UFDouble nadjustprice) {
		this.nadjustprice = nadjustprice;
	}

	public Integer getIadjusttype() {
		return iadjusttype;
	}

	public void setIadjusttype(Integer iadjusttype) {
		this.iadjusttype = iadjusttype;
	}

	public UFDouble getNtransmny() {
		return ntransmny;
	}

	public void setNtransmny(UFDouble ntransmny) {
		this.ntransmny = ntransmny;
	}

	public UFDate getDacceptdate() {
		return dacceptdate;
	}

	public void setDacceptdate(UFDate dacceptdate) {
		this.dacceptdate = dacceptdate;
	}

	public String getVcolpersonid() {
		return vcolpersonid;
	}

	public void setVcolpersonid(String vcolpersonid) {
		this.vcolpersonid = vcolpersonid;
	}

	public UFDateTime getVapprovetime() {
		return vapprovetime;
	}

	public void setVapprovetime(UFDateTime vapprovetime) {
		this.vapprovetime = vapprovetime;
	}

	public String getCustareaid() {
		return custareaid;
	}

	public void setCustareaid(String custareaid) {
		this.custareaid = custareaid;
	}

	public Integer getItransstatus() {
		return itransstatus;
	}

	public void setItransstatus(Integer itransstatus) {
		this.itransstatus = itransstatus;
	}

	public Integer getIcoltype() {
		return icoltype;
	}

	public void setIcoltype(Integer icoltype) {
		this.icoltype = icoltype;
	}

	public static String getICOLTYPE() {
		return ICOLTYPE;
	}

}
