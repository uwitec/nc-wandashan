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
 * 发运订单子表
 * </p>
 * 
 * 创建日期:2011-3-24
 * 
 * @author author
 * @version Your Project 1.0
 */
public class SendorderBVO extends SuperVO {
	private static final long serialVersionUID = 4752008406168776524L;
	public String reserve5;
	public UFDateTime ts;
	//辅计量单位id
	public String assunit;
	public String reserve4;
	//计划主数量
	public UFDouble nplannum;

	public String vdef4;
	public String vdef7;
	//收货站待接收数量
	public UFDouble nonlinenum;
	//计划辅数量
	public UFDouble nassplannum;
	//存货基本档案
	public String pk_invbasdoc;
	public String vdef2;
	public UFDate reserve11;
	public UFDate reserve12;
	//备注
	public String vmemo;
	public UFDouble reserve6;
	public String vdef1;
	public String reserve3;
	//子表主键
	public String pk_sendorder_b;
	public UFBoolean reserve14;
	public UFDouble reserve10;
	public UFDate reserve13;
	//发货站已发运数量
	public UFDouble noutsendnum;
	//主计量单位id
	public String unit;
	public UFDouble reserve8;
	public UFDouble reserve9;
	public String vdef3;
	public String vdef9;
	//主表主键
	public String pk_sendorder;
	public String vdef8;
	public String reserve1;
	//发货站待发运数量
	public UFDouble noutkeepnum;
	public UFBoolean reserve16;
	public UFDouble reserve7;
	public UFBoolean reserve15;
	//存货管理档案
	public String pk_invmandoc;
	public String vdef6;
	public String reserve2;
	public Integer dr;
	//收货站接受数量
	public UFDouble ninacceptnum;
	public String vdef10;
	public String vdef5;
	//源头单据表体id
	public String cfirstbillbid;
	//来源单据行号
	public String vsourcerowno;
	//源头单据表头id
	public String cfirstbillhid;
	//源头单据行号
	public String vfirstrowno;
	//来源单据号
	public String vsourcebillcode;
	//源头单据制单日期
	public UFDate dfirstbilldate;
	//来源单据表体序列号	
	public String csourcebillbid;
	//源头单据类型
	public String cfirsttype;
	//来源单据表头序列号
	public String csourcebillhid;
	//源头单据号
	public String vfirstbillcode;
	//来源单据类型
	public String csourcetype;
	//安排数量
	public UFDouble ndealnum;
	//安排辅数量
	public UFDouble nassdealnum;
	//已出库数量
	public UFDouble noutnum;
	//已出库辅数量
	public UFDouble nassoutnum;

	public static final String RESERVE5 = "reserve5";
	public static final String TS = "ts";
	public static final String ASSUNIT = "assunit";
	public static final String RESERVE4 = "reserve4";
	public static final String VDEF4 = "vdef4";
	public static final String VDEF7 = "vdef7";
	public static final String NONLINENUM = "nonlinenum";
	public static final String NASSPLANNUM = "nassplannum";
	public static final String PK_INVBASDOC = "pk_invbasdoc";
	public static final String VDEF2 = "vdef2";
	public static final String RESERVE11 = "reserve11";
	public static final String RESERVE12 = "reserve12";
	public static final String VMEMO = "vmemo";
	public static final String RESERVE6 = "reserve6";
	public static final String VDEF1 = "vdef1";
	public static final String RESERVE3 = "reserve3";
	public static final String PK_SENDORDER_B = "pk_sendorder_b";
	public static final String RESERVE14 = "reserve14";
	public static final String RESERVE10 = "reserve10";
	public static final String RESERVE13 = "reserve13";
	public static final String NOUTSENDNUM = "noutsendnum";
	public static final String UNIT = "unit";
	public static final String RESERVE8 = "reserve8";
	public static final String RESERVE9 = "reserve9";
	public static final String VDEF3 = "vdef3";
	public static final String VDEF9 = "vdef9";
	public static final String PK_SENDORDER = "pk_sendorder";
	public static final String VDEF8 = "vdef8";
	public static final String RESERVE1 = "reserve1";
	public static final String NOUTKEEPNUM = "noutkeepnum";
	public static final String RESERVE16 = "reserve16";
	public static final String RESERVE7 = "reserve7";
	public static final String RESERVE15 = "reserve15";
	public static final String PK_INVMANDOC = "pk_invmandoc";
	public static final String VDEF6 = "vdef6";
	public static final String RESERVE2 = "reserve2";
	public static final String DR = "dr";
	public static final String NINACCEPTNUM = "ninacceptnum";
	public static final String VDEF10 = "vdef10";
	public static final String VDEF5 = "vdef5";

	public UFDouble getNplannum() {
		return nplannum;
	}

	public void setNplannum(UFDouble nplannum) {
		this.nplannum = nplannum;
	}

	public String getCfirstbillbid() {
		return cfirstbillbid;
	}

	public void setCfirstbillbid(String cfirstbillbid) {
		this.cfirstbillbid = cfirstbillbid;
	}

	public String getVsourcerowno() {
		return vsourcerowno;
	}

	public void setVsourcerowno(String vsourcerowno) {
		this.vsourcerowno = vsourcerowno;
	}

	public String getCfirstbillhid() {
		return cfirstbillhid;
	}

	public void setCfirstbillhid(String cfirstbillhid) {
		this.cfirstbillhid = cfirstbillhid;
	}

	public String getVfirstrowno() {
		return vfirstrowno;
	}

	public void setVfirstrowno(String vfirstrowno) {
		this.vfirstrowno = vfirstrowno;
	}

	public String getVsourcebillcode() {
		return vsourcebillcode;
	}

	public void setVsourcebillcode(String vsourcebillcode) {
		this.vsourcebillcode = vsourcebillcode;
	}

	public UFDate getDfirstbilldate() {
		return dfirstbilldate;
	}

	public void setDfirstbilldate(UFDate dfirstbilldate) {
		this.dfirstbilldate = dfirstbilldate;
	}

	public String getCsourcebillbid() {
		return csourcebillbid;
	}

	public void setCsourcebillbid(String csourcebillbid) {
		this.csourcebillbid = csourcebillbid;
	}

	public String getCfirsttype() {
		return cfirsttype;
	}

	public void setCfirsttype(String cfirsttype) {
		this.cfirsttype = cfirsttype;
	}

	public String getCsourcebillhid() {
		return csourcebillhid;
	}

	public void setCsourcebillhid(String csourcebillhid) {
		this.csourcebillhid = csourcebillhid;
	}

	public String getVfirstbillcode() {
		return vfirstbillcode;
	}

	public void setVfirstbillcode(String vfirstbillcode) {
		this.vfirstbillcode = vfirstbillcode;
	}

	public String getCsourcetype() {
		return csourcetype;
	}

	public void setCsourcetype(String csourcetype) {
		this.csourcetype = csourcetype;
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
	 * 属性assunit的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return String
	 */
	public String getAssunit() {
		return assunit;
	}

	/**
	 * 属性assunit的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newAssunit
	 *            String
	 */
	public void setAssunit(String newAssunit) {

		assunit = newAssunit;
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
	 * 属性nonlinenum的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return UFDouble
	 */
	public UFDouble getNonlinenum() {
		return nonlinenum;
	}

	/**
	 * 属性nonlinenum的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newNonlinenum
	 *            UFDouble
	 */
	public void setNonlinenum(UFDouble newNonlinenum) {

		nonlinenum = newNonlinenum;
	}

	/**
	 * 属性nassplannum的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return UFDouble
	 */
	public UFDouble getNassplannum() {
		return nassplannum;
	}

	/**
	 * 属性nassplannum的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newNassplannum
	 *            UFDouble
	 */
	public void setNassplannum(UFDouble newNassplannum) {

		nassplannum = newNassplannum;
	}

	/**
	 * 属性pk_invbasdoc的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return String
	 */
	public String getPk_invbasdoc() {
		return pk_invbasdoc;
	}

	/**
	 * 属性pk_invbasdoc的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newPk_invbasdoc
	 *            String
	 */
	public void setPk_invbasdoc(String newPk_invbasdoc) {

		pk_invbasdoc = newPk_invbasdoc;
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
	 * 属性reserve6的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return UFDouble
	 */
	public UFDouble getReserve6() {
		return reserve6;
	}

	/**
	 * 属性reserve6的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newReserve6
	 *            UFDouble
	 */
	public void setReserve6(UFDouble newReserve6) {

		reserve6 = newReserve6;
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
	 * 属性pk_sendorder_b的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return String
	 */
	public String getPk_sendorder_b() {
		return pk_sendorder_b;
	}

	/**
	 * 属性pk_sendorder_b的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newPk_sendorder_b
	 *            String
	 */
	public void setPk_sendorder_b(String newPk_sendorder_b) {

		pk_sendorder_b = newPk_sendorder_b;
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
	 * 属性noutsendnum的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return UFDouble
	 */
	public UFDouble getNoutsendnum() {
		return noutsendnum;
	}

	/**
	 * 属性noutsendnum的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newNoutsendnum
	 *            UFDouble
	 */
	public void setNoutsendnum(UFDouble newNoutsendnum) {

		noutsendnum = newNoutsendnum;
	}

	/**
	 * 属性unit的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return String
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * 属性unit的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newUnit
	 *            String
	 */
	public void setUnit(String newUnit) {

		unit = newUnit;
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
	 * 属性pk_sendorder的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return String
	 */
	public String getPk_sendorder() {
		return pk_sendorder;
	}

	/**
	 * 属性pk_sendorder的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newPk_sendorder
	 *            String
	 */
	public void setPk_sendorder(String newPk_sendorder) {

		pk_sendorder = newPk_sendorder;
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
	 * 属性noutkeepnum的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return UFDouble
	 */
	public UFDouble getNoutkeepnum() {
		return noutkeepnum;
	}

	/**
	 * 属性noutkeepnum的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newNoutkeepnum
	 *            UFDouble
	 */
	public void setNoutkeepnum(UFDouble newNoutkeepnum) {

		noutkeepnum = newNoutkeepnum;
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
	 * 属性reserve7的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return UFDouble
	 */
	public UFDouble getReserve7() {
		return reserve7;
	}

	/**
	 * 属性reserve7的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newReserve7
	 *            UFDouble
	 */
	public void setReserve7(UFDouble newReserve7) {

		reserve7 = newReserve7;
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
	 * 属性pk_invmandoc的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return String
	 */
	public String getPk_invmandoc() {
		return pk_invmandoc;
	}

	/**
	 * 属性pk_invmandoc的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newPk_invmandoc
	 *            String
	 */
	public void setPk_invmandoc(String newPk_invmandoc) {

		pk_invmandoc = newPk_invmandoc;
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
	 * 属性ninacceptnum的Getter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return UFDouble
	 */
	public UFDouble getNinacceptnum() {
		return ninacceptnum;
	}

	/**
	 * 属性ninacceptnum的Setter方法.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newNinacceptnum
	 *            UFDouble
	 */
	public void setNinacceptnum(UFDouble newNinacceptnum) {

		ninacceptnum = newNinacceptnum;
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

		if (pk_sendorder_b == null) {
			errFields.add(new String("pk_sendorder_b"));
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

	/**
	 * <p>
	 * 取得父VO主键字段.
	 * <p>
	 * 创建日期:2011-3-24
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getParentPKFieldName() {

		return "pk_sendorder";

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
		return "pk_sendorder_b";
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

		return "wds_sendorder_b";
	}

	/**
	 * 按照默认方式创建构造子.
	 * 
	 * 创建日期:2011-3-24
	 */
	public SendorderBVO() {

		super();
	}

	/**
	 * 使用主键进行初始化的构造子.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newPk_sendorder_b
	 *            主键值
	 */
	public SendorderBVO(String newPk_sendorder_b) {

		// 为主键字段赋值:
		pk_sendorder_b = newPk_sendorder_b;

	}

	/**
	 * 返回对象标识,用来唯一定位对象.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return String
	 */
	public String getPrimaryKey() {

		return pk_sendorder_b;

	}

	/**
	 * 设置对象标识,用来唯一定位对象.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @param newPk_sendorder_b
	 *            String
	 */
	public void setPrimaryKey(String newPk_sendorder_b) {

		pk_sendorder_b = newPk_sendorder_b;

	}

	/**
	 * 返回数值对象的显示名称.
	 * 
	 * 创建日期:2011-3-24
	 * 
	 * @return java.lang.String 返回数值对象的显示名称.
	 */
	public String getEntityName() {

		return "wds_sendorder_b";

	}

	public UFDouble getNdealnum() {
		return ndealnum;
	}

	public void setNdealnum(UFDouble ndealnum) {
		this.ndealnum = ndealnum;
	}

	public UFDouble getNassdealnum() {
		return nassdealnum;
	}

	public void setNassdealnum(UFDouble nassdealnum) {
		this.nassdealnum = nassdealnum;
	}

	public UFDouble getNoutnum() {
		return noutnum;
	}

	public void setNoutnum(UFDouble noutnum) {
		this.noutnum = noutnum;
	}

	public UFDouble getNassoutnum() {
		return nassoutnum;
	}

	public void setNassoutnum(UFDouble nassoutnum) {
		this.nassoutnum = nassoutnum;
	}
	
}
