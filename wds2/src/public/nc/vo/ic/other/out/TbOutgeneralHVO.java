/***************************************************************\
 *     The skeleton of this class is generated by an automatic *
 * code generator for NC product. It is based on Velocity.     *
\***************************************************************/
package nc.vo.ic.other.out;

import java.util.ArrayList;
import nc.vo.pub.*;
import nc.vo.pub.lang.*;

/**
 * <b> 在此处简要描述此类的功能 </b>
 * 
 * <p>
 * 在此处添加此类的描述信息
 * </p>
 * 
 * 创建日期:2010-7-20
 * 
 * @author ${vmObject.author}
 * @version Your Project 1.0
 */
public class TbOutgeneralHVO extends SuperVO {

	public String vuserdef9;
	public String tmaketime;
	public String ccustomerid;
	public String vuserdef3;
	public String cauditorid;
	public String cwhsmanagerid;
	public String coperatorid;
	public Integer iprintcount;
	public String vuserdef14;
	public String comp;//公司
	public String pk_defdoc10;
	public String pk_defdoc6;
	public String cbiztype;
	public String pk_defdoc3;
	public String general_pk;
	public String srl_pkr;//入库仓库
	public String vuserdef10;
	public String pk_defdoc2;
	public String vdiliveraddress;
	public UFDate qianzidate;
	public String pk_defdoc7;
	public Integer state;
	public String pk_cubasdocc;
	public String vbillcode;
	public String vuserdef12;
	public UFDate dauditdate;
	public String vuserdef11;
	public Integer vbillstatus;
	public String vuserdef15;
	public String cdptid;
	public String cdispatcherid;
	public String cbizid;
	public String tlastmoditime;
	public String pk_defdoc9;
	public String vuserdef1;
	public String clastmodiid;
	public String pk_calbody;
	public String pk_defdoc1;
	public UFDate dbilldate;
	public String pk_defdoc4;
	public String pk_defdoc5;
	public String vuserdef2;
	public String vuserdef5;
	public String vnote;
	public String srl_pk;//出库仓库
	public String vbilltype;
	public String vuserdef8;
	public String vuserdef4;
	public String cregister;
	public String pk_defdoc8;
	public String vuserdef7;
	public String pk_calbodyr;
	public String taccounttime;
	public String vuserdef13;
	public String vuserdef6;
	public UFTime ts;
	public Integer dr;

	public String pk_cargdoc;// 货位主键
	public String pk_fcorp;// 分厂公司主键
	public String pk_fcalbody;// 分厂库存组织主键
	public String pk_fstordoc;// 分厂仓库主键
	public String mileage;// 运输里程
	public String unitprice;// 运费单价
	public String freight;// 运费
	public String vsourcebillcode; // 来源订单号
	public String csourcebillhid; // 来源单据表头
	public UFBoolean is_yundan; // 是否生成运单

	public static final String IS_YUNDAN = "is_yundan";
	public static final String CSOURCEBILLHID = "csourcebillhid";
	public static final String VSOURCEBILLCODE = "vsourcebillcode";
	public static final String FREIGHT = "freight";
	public static final String UNITPRICE = "unitprice";
	public static final String MILEAGE = "mileage";
	public static final String PK_FSTORDOC = "pk_fstordoc";
	public static final String PK_FCALBODY = "pk_fcalbody";
	public static final String PK_FCORP = "pk_fcorp";
	public static final String PK_CARGDOC = "pk_cargdoc";

	public static final String DR = "dr";
	public static final String TS = "ts";

	public static final String VUSERDEF9 = "vuserdef9";
	public static final String TMAKETIME = "tmaketime";
	public static final String CCUSTOMERID = "ccustomerid";
	public static final String VUSERDEF3 = "vuserdef3";
	public static final String CAUDITORID = "cauditorid";
	public static final String CWHSMANAGERID = "cwhsmanagerid";
	public static final String COPERATORID = "coperatorid";
	public static final String IPRINTCOUNT = "iprintcount";
	public static final String VUSERDEF14 = "vuserdef14";
	public static final String COMP = "comp";
	public static final String PK_DEFDOC10 = "pk_defdoc10";
	public static final String PK_DEFDOC6 = "pk_defdoc6";
	public static final String CBIZTYPE = "cbiztype";
	public static final String PK_DEFDOC3 = "pk_defdoc3";
	public static final String GENERAL_PK = "general_pk";
	public static final String SRL_PKR = "srl_pkr";
	public static final String VUSERDEF10 = "vuserdef10";
	public static final String PK_DEFDOC2 = "pk_defdoc2";
	public static final String VDILIVERADDRESS = "vdiliveraddress";
	public static final String QIANZIDATE = "qianzidate";
	public static final String PK_DEFDOC7 = "pk_defdoc7";
	public static final String STATE = "state";
	public static final String PK_CUBASDOCC = "pk_cubasdocc";
	public static final String VBILLCODE = "vbillcode";
	public static final String VUSERDEF12 = "vuserdef12";
	public static final String DAUDITDATE = "dauditdate";
	public static final String VUSERDEF11 = "vuserdef11";
	public static final String VBILLSTATUS = "vbillstatus";
	public static final String VUSERDEF15 = "vuserdef15";
	public static final String CDPTID = "cdptid";
	public static final String CDISPATCHERID = "cdispatcherid";
	public static final String CBIZID = "cbizid";
	public static final String TLASTMODITIME = "tlastmoditime";
	public static final String PK_DEFDOC9 = "pk_defdoc9";
	public static final String VUSERDEF1 = "vuserdef1";
	public static final String CLASTMODIID = "clastmodiid";
	public static final String PK_CALBODY = "pk_calbody";
	public static final String PK_DEFDOC1 = "pk_defdoc1";
	public static final String DBILLDATE = "dbilldate";
	public static final String PK_DEFDOC4 = "pk_defdoc4";
	public static final String PK_DEFDOC5 = "pk_defdoc5";
	public static final String VUSERDEF2 = "vuserdef2";
	public static final String VUSERDEF5 = "vuserdef5";
	public static final String VNOTE = "vnote";
	public static final String SRL_PK = "srl_pk";
	public static final String VBILLTYPE = "vbilltype";
	public static final String VUSERDEF8 = "vuserdef8";
	public static final String VUSERDEF4 = "vuserdef4";
	public static final String CREGISTER = "cregister";
	public static final String PK_DEFDOC8 = "pk_defdoc8";
	public static final String VUSERDEF7 = "vuserdef7";
	public static final String PK_CALBODYR = "pk_calbodyr";
	public static final String TACCOUNTTIME = "taccounttime";
	public static final String VUSERDEF13 = "vuserdef13";
	public static final String VUSERDEF6 = "vuserdef6";

	/**
	 * 属性vuserdef9的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getVuserdef9() {
		return vuserdef9;
	}

	/**
	 * 属性vuserdef9的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newVuserdef9
	 *            String
	 */
	public void setVuserdef9(String newVuserdef9) {

		vuserdef9 = newVuserdef9;
	}

	public String getTmaketime() {
		return tmaketime;
	}

	public void setTmaketime(String tmaketime) {
		this.tmaketime = tmaketime;
	}

	/**
	 * 属性ccustomerid的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getCcustomerid() {
		return ccustomerid;
	}

	/**
	 * 属性ccustomerid的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newCcustomerid
	 *            String
	 */
	public void setCcustomerid(String newCcustomerid) {

		ccustomerid = newCcustomerid;
	}

	/**
	 * 属性vuserdef3的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getVuserdef3() {
		return vuserdef3;
	}

	/**
	 * 属性vuserdef3的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newVuserdef3
	 *            String
	 */
	public void setVuserdef3(String newVuserdef3) {

		vuserdef3 = newVuserdef3;
	}

	/**
	 * 属性cauditorid的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getCauditorid() {
		return cauditorid;
	}

	/**
	 * 属性cauditorid的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newCauditorid
	 *            String
	 */
	public void setCauditorid(String newCauditorid) {

		cauditorid = newCauditorid;
	}

	/**
	 * 属性cwhsmanagerid的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getCwhsmanagerid() {
		return cwhsmanagerid;
	}

	/**
	 * 属性cwhsmanagerid的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newCwhsmanagerid
	 *            String
	 */
	public void setCwhsmanagerid(String newCwhsmanagerid) {

		cwhsmanagerid = newCwhsmanagerid;
	}

	/**
	 * 属性coperatorid的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getCoperatorid() {
		return coperatorid;
	}

	/**
	 * 属性coperatorid的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newCoperatorid
	 *            String
	 */
	public void setCoperatorid(String newCoperatorid) {

		coperatorid = newCoperatorid;
	}

	/**
	 * 属性iprintcount的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return Integer
	 */
	public Integer getIprintcount() {
		return iprintcount;
	}

	/**
	 * 属性iprintcount的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newIprintcount
	 *            Integer
	 */
	public void setIprintcount(Integer newIprintcount) {

		iprintcount = newIprintcount;
	}

	/**
	 * 属性vuserdef14的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getVuserdef14() {
		return vuserdef14;
	}

	/**
	 * 属性vuserdef14的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newVuserdef14
	 *            String
	 */
	public void setVuserdef14(String newVuserdef14) {

		vuserdef14 = newVuserdef14;
	}

	/**
	 * 属性comp的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getComp() {
		return comp;
	}

	/**
	 * 属性comp的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newComp
	 *            String
	 */
	public void setComp(String newComp) {

		comp = newComp;
	}

	/**
	 * 属性pk_defdoc10的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getPk_defdoc10() {
		return pk_defdoc10;
	}

	/**
	 * 属性pk_defdoc10的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newPk_defdoc10
	 *            String
	 */
	public void setPk_defdoc10(String newPk_defdoc10) {

		pk_defdoc10 = newPk_defdoc10;
	}

	/**
	 * 属性pk_defdoc6的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getPk_defdoc6() {
		return pk_defdoc6;
	}

	/**
	 * 属性pk_defdoc6的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newPk_defdoc6
	 *            String
	 */
	public void setPk_defdoc6(String newPk_defdoc6) {

		pk_defdoc6 = newPk_defdoc6;
	}

	/**
	 * 属性cbiztype的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getCbiztype() {
		return cbiztype;
	}

	/**
	 * 属性cbiztype的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newCbiztype
	 *            String
	 */
	public void setCbiztype(String newCbiztype) {

		cbiztype = newCbiztype;
	}

	/**
	 * 属性pk_defdoc3的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getPk_defdoc3() {
		return pk_defdoc3;
	}

	/**
	 * 属性pk_defdoc3的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newPk_defdoc3
	 *            String
	 */
	public void setPk_defdoc3(String newPk_defdoc3) {

		pk_defdoc3 = newPk_defdoc3;
	}

	/**
	 * 属性general_pk的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getGeneral_pk() {
		return general_pk;
	}

	/**
	 * 属性general_pk的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newGeneral_pk
	 *            String
	 */
	public void setGeneral_pk(String newGeneral_pk) {

		general_pk = newGeneral_pk;
	}

	/**
	 * 属性srl_pkr的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getSrl_pkr() {
		return srl_pkr;
	}

	/**
	 * 属性srl_pkr的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newSrl_pkr
	 *            String
	 */
	public void setSrl_pkr(String newSrl_pkr) {

		srl_pkr = newSrl_pkr;
	}

	/**
	 * 属性vuserdef10的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getVuserdef10() {
		return vuserdef10;
	}

	/**
	 * 属性vuserdef10的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newVuserdef10
	 *            String
	 */
	public void setVuserdef10(String newVuserdef10) {

		vuserdef10 = newVuserdef10;
	}

	/**
	 * 属性pk_defdoc2的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getPk_defdoc2() {
		return pk_defdoc2;
	}

	/**
	 * 属性pk_defdoc2的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newPk_defdoc2
	 *            String
	 */
	public void setPk_defdoc2(String newPk_defdoc2) {

		pk_defdoc2 = newPk_defdoc2;
	}

	/**
	 * 属性vdiliveraddress的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getVdiliveraddress() {
		return vdiliveraddress;
	}

	/**
	 * 属性vdiliveraddress的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newVdiliveraddress
	 *            String
	 */
	public void setVdiliveraddress(String newVdiliveraddress) {

		vdiliveraddress = newVdiliveraddress;
	}

	/**
	 * 属性qianzidate的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return UFDate
	 */
	public UFDate getQianzidate() {
		return qianzidate;
	}

	/**
	 * 属性qianzidate的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newQianzidate
	 *            UFDate
	 */
	public void setQianzidate(UFDate newQianzidate) {

		qianzidate = newQianzidate;
	}

	/**
	 * 属性pk_defdoc7的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getPk_defdoc7() {
		return pk_defdoc7;
	}

	/**
	 * 属性pk_defdoc7的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newPk_defdoc7
	 *            String
	 */
	public void setPk_defdoc7(String newPk_defdoc7) {

		pk_defdoc7 = newPk_defdoc7;
	}

	/**
	 * 属性state的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return Integer
	 */
	public Integer getState() {
		return state;
	}

	/**
	 * 属性state的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newState
	 *            Integer
	 */
	public void setState(Integer newState) {

		state = newState;
	}

	/**
	 * 属性pk_cubasdocc的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getPk_cubasdocc() {
		return pk_cubasdocc;
	}

	/**
	 * 属性pk_cubasdocc的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newPk_cubasdocc
	 *            String
	 */
	public void setPk_cubasdocc(String newPk_cubasdocc) {

		pk_cubasdocc = newPk_cubasdocc;
	}

	/**
	 * 属性vbillcode的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getVbillcode() {
		return vbillcode;
	}

	/**
	 * 属性vbillcode的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newVbillcode
	 *            String
	 */
	public void setVbillcode(String newVbillcode) {

		vbillcode = newVbillcode;
	}

	/**
	 * 属性vuserdef12的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getVuserdef12() {
		return vuserdef12;
	}

	/**
	 * 属性vuserdef12的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newVuserdef12
	 *            String
	 */
	public void setVuserdef12(String newVuserdef12) {

		vuserdef12 = newVuserdef12;
	}

	/**
	 * 属性dauditdate的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return UFDate
	 */
	public UFDate getDauditdate() {
		return dauditdate;
	}

	/**
	 * 属性dauditdate的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newDauditdate
	 *            UFDate
	 */
	public void setDauditdate(UFDate newDauditdate) {

		dauditdate = newDauditdate;
	}

	/**
	 * 属性vuserdef11的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getVuserdef11() {
		return vuserdef11;
	}

	/**
	 * 属性vuserdef11的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newVuserdef11
	 *            String
	 */
	public void setVuserdef11(String newVuserdef11) {

		vuserdef11 = newVuserdef11;
	}

	/**
	 * 属性vbillstatus的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return Integer
	 */
	public Integer getVbillstatus() {
		return vbillstatus;
	}

	/**
	 * 属性vbillstatus的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newVbillstatus
	 *            Integer
	 */
	public void setVbillstatus(Integer newVbillstatus) {

		vbillstatus = newVbillstatus;
	}

	/**
	 * 属性vuserdef15的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getVuserdef15() {
		return vuserdef15;
	}

	/**
	 * 属性vuserdef15的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newVuserdef15
	 *            String
	 */
	public void setVuserdef15(String newVuserdef15) {

		vuserdef15 = newVuserdef15;
	}

	/**
	 * 属性cdptid的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getCdptid() {
		return cdptid;
	}

	/**
	 * 属性cdptid的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newCdptid
	 *            String
	 */
	public void setCdptid(String newCdptid) {

		cdptid = newCdptid;
	}

	/**
	 * 属性cdispatcherid的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getCdispatcherid() {
		return cdispatcherid;
	}

	/**
	 * 属性cdispatcherid的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newCdispatcherid
	 *            String
	 */
	public void setCdispatcherid(String newCdispatcherid) {

		cdispatcherid = newCdispatcherid;
	}

	/**
	 * 属性cbizid的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getCbizid() {
		return cbizid;
	}

	/**
	 * 属性cbizid的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newCbizid
	 *            String
	 */
	public void setCbizid(String newCbizid) {

		cbizid = newCbizid;
	}

	/**
	 * 属性tlastmoditime的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getTlastmoditime() {
		return tlastmoditime;
	}

	/**
	 * 属性tlastmoditime的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newTlastmoditime
	 *            String
	 */
	public void setTlastmoditime(String newTlastmoditime) {

		tlastmoditime = newTlastmoditime;
	}

	/**
	 * 属性pk_defdoc9的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getPk_defdoc9() {
		return pk_defdoc9;
	}

	/**
	 * 属性pk_defdoc9的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newPk_defdoc9
	 *            String
	 */
	public void setPk_defdoc9(String newPk_defdoc9) {

		pk_defdoc9 = newPk_defdoc9;
	}

	/**
	 * 属性vuserdef1的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getVuserdef1() {
		return vuserdef1;
	}

	/**
	 * 属性vuserdef1的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newVuserdef1
	 *            String
	 */
	public void setVuserdef1(String newVuserdef1) {

		vuserdef1 = newVuserdef1;
	}

	/**
	 * 属性clastmodiid的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getClastmodiid() {
		return clastmodiid;
	}

	/**
	 * 属性clastmodiid的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newClastmodiid
	 *            String
	 */
	public void setClastmodiid(String newClastmodiid) {

		clastmodiid = newClastmodiid;
	}

	/**
	 * 属性pk_calbody的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getPk_calbody() {
		return pk_calbody;
	}

	/**
	 * 属性pk_calbody的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newPk_calbody
	 *            String
	 */
	public void setPk_calbody(String newPk_calbody) {

		pk_calbody = newPk_calbody;
	}

	/**
	 * 属性pk_defdoc1的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getPk_defdoc1() {
		return pk_defdoc1;
	}

	/**
	 * 属性pk_defdoc1的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newPk_defdoc1
	 *            String
	 */
	public void setPk_defdoc1(String newPk_defdoc1) {

		pk_defdoc1 = newPk_defdoc1;
	}

	/**
	 * 属性dbilldate的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return UFDate
	 */
	public UFDate getDbilldate() {
		return dbilldate;
	}

	/**
	 * 属性dbilldate的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newDbilldate
	 *            UFDate
	 */
	public void setDbilldate(UFDate newDbilldate) {

		dbilldate = newDbilldate;
	}

	/**
	 * 属性pk_defdoc4的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getPk_defdoc4() {
		return pk_defdoc4;
	}

	/**
	 * 属性pk_defdoc4的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newPk_defdoc4
	 *            String
	 */
	public void setPk_defdoc4(String newPk_defdoc4) {

		pk_defdoc4 = newPk_defdoc4;
	}

	/**
	 * 属性pk_defdoc5的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getPk_defdoc5() {
		return pk_defdoc5;
	}

	/**
	 * 属性pk_defdoc5的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newPk_defdoc5
	 *            String
	 */
	public void setPk_defdoc5(String newPk_defdoc5) {

		pk_defdoc5 = newPk_defdoc5;
	}

	/**
	 * 属性vuserdef2的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getVuserdef2() {
		return vuserdef2;
	}

	/**
	 * 属性vuserdef2的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newVuserdef2
	 *            String
	 */
	public void setVuserdef2(String newVuserdef2) {

		vuserdef2 = newVuserdef2;
	}

	/**
	 * 属性vuserdef5的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getVuserdef5() {
		return vuserdef5;
	}

	/**
	 * 属性vuserdef5的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newVuserdef5
	 *            String
	 */
	public void setVuserdef5(String newVuserdef5) {

		vuserdef5 = newVuserdef5;
	}

	/**
	 * 属性vnote的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getVnote() {
		return vnote;
	}

	/**
	 * 属性vnote的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newVnote
	 *            String
	 */
	public void setVnote(String newVnote) {

		vnote = newVnote;
	}

	/**
	 * 属性srl_pk的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getSrl_pk() {
		return srl_pk;
	}

	/**
	 * 属性srl_pk的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newSrl_pk
	 *            String
	 */
	public void setSrl_pk(String newSrl_pk) {

		srl_pk = newSrl_pk;
	}

	/**
	 * 属性vbilltype的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getVbilltype() {
		return vbilltype;
	}

	/**
	 * 属性vbilltype的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newVbilltype
	 *            String
	 */
	public void setVbilltype(String newVbilltype) {

		vbilltype = newVbilltype;
	}

	/**
	 * 属性vuserdef8的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getVuserdef8() {
		return vuserdef8;
	}

	/**
	 * 属性vuserdef8的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newVuserdef8
	 *            String
	 */
	public void setVuserdef8(String newVuserdef8) {

		vuserdef8 = newVuserdef8;
	}

	/**
	 * 属性vuserdef4的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getVuserdef4() {
		return vuserdef4;
	}

	/**
	 * 属性vuserdef4的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newVuserdef4
	 *            String
	 */
	public void setVuserdef4(String newVuserdef4) {

		vuserdef4 = newVuserdef4;
	}

	/**
	 * 属性cregister的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getCregister() {
		return cregister;
	}

	/**
	 * 属性cregister的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newCregister
	 *            String
	 */
	public void setCregister(String newCregister) {

		cregister = newCregister;
	}

	/**
	 * 属性pk_defdoc8的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getPk_defdoc8() {
		return pk_defdoc8;
	}

	/**
	 * 属性pk_defdoc8的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newPk_defdoc8
	 *            String
	 */
	public void setPk_defdoc8(String newPk_defdoc8) {

		pk_defdoc8 = newPk_defdoc8;
	}

	/**
	 * 属性vuserdef7的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getVuserdef7() {
		return vuserdef7;
	}

	/**
	 * 属性vuserdef7的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newVuserdef7
	 *            String
	 */
	public void setVuserdef7(String newVuserdef7) {

		vuserdef7 = newVuserdef7;
	}

	/**
	 * 属性pk_calbodyr的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getPk_calbodyr() {
		return pk_calbodyr;
	}

	/**
	 * 属性pk_calbodyr的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newPk_calbodyr
	 *            String
	 */
	public void setPk_calbodyr(String newPk_calbodyr) {

		pk_calbodyr = newPk_calbodyr;
	}

	/**
	 * 属性taccounttime的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getTaccounttime() {
		return taccounttime;
	}

	/**
	 * 属性taccounttime的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newTaccounttime
	 *            String
	 */
	public void setTaccounttime(String newTaccounttime) {

		taccounttime = newTaccounttime;
	}

	/**
	 * 属性vuserdef13的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getVuserdef13() {
		return vuserdef13;
	}

	/**
	 * 属性vuserdef13的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newVuserdef13
	 *            String
	 */
	public void setVuserdef13(String newVuserdef13) {

		vuserdef13 = newVuserdef13;
	}

	/**
	 * 属性vuserdef6的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getVuserdef6() {
		return vuserdef6;
	}

	/**
	 * 属性vuserdef6的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newVuserdef6
	 *            String
	 */
	public void setVuserdef6(String newVuserdef6) {

		vuserdef6 = newVuserdef6;
	}

	/**
	 * 验证对象各属性之间的数据逻辑正确性.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @exception nc.vo.pub.ValidationException
	 *                如果验证失败,抛出 ValidationException,对错误进行解释.
	 */
	public void validate() throws ValidationException {

		ArrayList errFields = new ArrayList(); // errFields record those null

		// fields that cannot be null.
		// 检查是否为不允许空的字段赋了空值,你可能需要修改下面的提示信息:

		if (general_pk == null) {
			errFields.add(new String("general_pk"));
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
	 * 创建日期:2010-7-20
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
	 * 创建日期:2010-7-20
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPKFieldName() {
		return "general_pk";
	}

	/**
	 * <p>
	 * 返回表名称.
	 * <p>
	 * 创建日期:2010-7-20
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {

		return "tb_outgeneral_h";
	}

	/**
	 * 按照默认方式创建构造子.
	 * 
	 * 创建日期:2010-7-20
	 */
	public TbOutgeneralHVO() {

		super();
	}

	/**
	 * 使用主键进行初始化的构造子.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newGeneral_pk
	 *            主键值
	 */
	public TbOutgeneralHVO(String newGeneral_pk) {

		// 为主键字段赋值:
		general_pk = newGeneral_pk;

	}

	/**
	 * 返回对象标识,用来唯一定位对象.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getPrimaryKey() {

		return general_pk;

	}

	/**
	 * 设置对象标识,用来唯一定位对象.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newGeneral_pk
	 *            String
	 */
	public void setPrimaryKey(String newGeneral_pk) {

		general_pk = newGeneral_pk;

	}

	/**
	 * 返回数值对象的显示名称.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return java.lang.String 返回数值对象的显示名称.
	 */
	public String getEntityName() {

		return "tb_outgeneral_h";

	}

	public UFTime getTs() {
		return ts;
	}

	public void setTs(UFTime ts) {
		this.ts = ts;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public String getPk_cargdoc() {
		return pk_cargdoc;
	}

	public void setPk_cargdoc(String pk_cargdoc) {
		this.pk_cargdoc = pk_cargdoc;
	}

	public String getPk_fcorp() {
		return pk_fcorp;
	}

	public void setPk_fcorp(String pk_fcorp) {
		this.pk_fcorp = pk_fcorp;
	}

	public String getPk_fcalbody() {
		return pk_fcalbody;
	}

	public void setPk_fcalbody(String pk_fcalbody) {
		this.pk_fcalbody = pk_fcalbody;
	}

	public String getPk_fstordoc() {
		return pk_fstordoc;
	}

	public void setPk_fstordoc(String pk_fstordoc) {
		this.pk_fstordoc = pk_fstordoc;
	}

	public String getMileage() {
		return mileage;
	}

	public void setMileage(String mileage) {
		this.mileage = mileage;
	}

	public String getUnitprice() {
		return unitprice;
	}

	public void setUnitprice(String unitprice) {
		this.unitprice = unitprice;
	}

	public String getFreight() {
		return freight;
	}

	public void setFreight(String freight) {
		this.freight = freight;
	}

	public String getVsourcebillcode() {
		return vsourcebillcode;
	}

	public void setVsourcebillcode(String vsourcebillcode) {
		this.vsourcebillcode = vsourcebillcode;
	}

	public String getCsourcebillhid() {
		return csourcebillhid;
	}

	public void setCsourcebillhid(String csourcebillhid) {
		this.csourcebillhid = csourcebillhid;
	}

	public UFBoolean getIs_yundan() {
		return is_yundan;
	}

	public void setIs_yundan(UFBoolean is_yundan) {
		this.is_yundan = is_yundan;
	}
}
