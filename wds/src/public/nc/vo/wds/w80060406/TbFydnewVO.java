/***************************************************************\
 *     The skeleton of this class is generated by an automatic *
 * code generator for NC product. It is based on Velocity.     *
\***************************************************************/
package nc.vo.wds.w80060406;

import java.util.ArrayList;

import nc.vo.pub.NullFieldException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFTime;

/**
 * <b> 在此处简要描述此类的功能 </b>
 * 
 * <p>
 * 在此处添加此类的描述信息
 * </p>
 * 
 * 创建日期:2010-6-30
 * 
 * @author ${vmObject.author}
 * @version Your Project 1.0
 */
public class TbFydnewVO extends SuperVO {

	public String vdef4;
	public UFDouble fyd_yslc;
	public String fyd_ddh;
	public String fyd_yhfs;
	public Integer dr;
	public String fyd_hjrqz;
	public String voperatorid;
	public UFDate fyd_zcsj;
	public String vapprovenote;
	public String fyd_lxdh;
	public UFDouble fyd_yf;
	public UFTime ts;
	public UFDate fyd_begints;
	public String fyd_ysrqz;
	public String srl_pk;
	public String fyd_pk;
	public String vdef3;
	public String pk_kh;
	public String fyd_khdm;
	public UFDouble fyd_bcgy;
	public String fyd_jsdwgz;
	public String fyd_spsj;
	public UFDouble fyd_js;
	public String vdef5;
	public String fyd_jzqz;
	public String srl_pkr;
	public String fyd_rq;
	public String fyd_shdw;
	public String fyd_dhz;
	public UFDate dmakedate;
	public String vdef7;
	public String fyd_lxr;
	public UFDate fyd_endts;
	public String fyd_ch;
	public Integer billtype;
	public String fyd_zprqz;
	public Integer vbillstatus;
	public String vdef2;
	public Integer fyd_fyzt;
	public String vdef1;
	public String vbillno;
	public Integer fyd_approstate;
	public String fyd_dby;
	public String fyd_jhy;
	public String fyd_shdz;
	public String fyd_sjqz;
	public String fyd_sjdh;
	public String vdef9;
	public UFDate dapprovedate;
	public String vdef8;
	public UFDouble fyd_yj;
	public String vapproveid;
	public String fyd_sj;
	public String vdef6;
	public String fyd_bz;
	public String cif_pk; // 车辆主键
	public UFDate fyd_xhsj; // 需货时间
	public String pk_psndoc; // 业务员主键
	public String pk_busitype; // 业务类型主键
	public String csaleid; // 销售主表主键
	public String se_pk; // 发运录入主键
	public String mergelogo; // 合并标识
	public String fyd_zdr; // 发运单制单人
	public UFDate fyd_zdsj; // 发运单制单时间
	public String cdeptid; // 部门主键
	public String splitvbillno;// 拆分单据号
	public String tc_pk;// 车辆主键
	public String fyd_yjzl;// 运价种类
	public Integer fyd_constatus;// 运单确认状态
	public String pk_yjzbzj;// 运价子表主键
	public String fyd_busitype; // 业务流程
	public Integer iprintcount;// 打印次数
	public Integer fyd_splitstatus;// 拆分状态
	public UFDate iprintdate;// 打印日期
	public String dmaketime;// 制单时间
	public String pk_mergelogo; // 主键合并标识
	public UFBoolean fyd_splitend;// 拆分是否结束
	public UFDouble fyd_fjf;// 附加费
	public UFBoolean isbig; // 是否大包粉

	public static final String ISBIG = "isbig";
	public static final String FYD_FJF = "fyd_fjf";
	public static final String FYD_SPLITEND = "fyd_splitend";
	public static final String PK_MERGELOGO = "pk_mergelogo";
	public static final String FYD_SPLITSTATUS = "fyd_splitstatus";
	public static final String DMAKETIME = "dmaketime";
	public static final String IPRINTDATE = "iprintdate";
	public static final String IPRINTCOUNT = "iprintcount ";
	public static final String FYD_BUSITYPE = "fyd_busitype";
	public static final String PK_YJZBZJ = "pk_yjzbzj";
	public static final String FYD_CONSTATUS = "fyd_constatus";
	public static final String FYD_YJZL = "fyd_yjzl";
	public static final String TC_PK = "tc_pk";
	public static final String SPLITVBILLNO = "splitvbillno";

	public static final String CDEPTID = "cdeptid";
	public static final String FYD_ZDSJ = "fyd_zdsj";
	public static final String FYD_ZDR = "fyd_zdr";
	public static final String MERGELOGO = "mergelogo";
	public static final String SE_PK = "se_pk";
	public static final String CSALEID = "csaleid";
	public static final String PK_BUSITYPE = "pk_busitype";
	public static final String PK_PSNDOC = "pk_psndoc";
	public static final String FYD_XHSJ = "fyd_xhsj";
	public static final String CIF_PK = "cif_pk";
	public static final String VDEF4 = "vdef4";
	public static final String FYD_YSLC = "fyd_yslc";
	public static final String FYD_DDH = "fyd_ddh";
	public static final String FYD_YHFS = "fyd_yhfs";
	public static final String DR = "dr";
	public static final String FYD_HJRQZ = "fyd_hjrqz";
	public static final String VOPERATORID = "voperatorid";
	public static final String FYD_ZCSJ = "fyd_zcsj";
	public static final String VAPPROVENOTE = "vapprovenote";
	public static final String FYD_LXDH = "fyd_lxdh";
	public static final String FYD_YF = "fyd_yf";
	public static final String TS = "ts";
	public static final String FYD_BEGINTS = "fyd_begints";
	public static final String FYD_YSRQZ = "fyd_ysrqz";
	public static final String SRL_PK = "srl_pk";
	public static final String FYD_PK = "fyd_pk";
	public static final String VDEF3 = "vdef3";
	public static final String PK_KH = "pk_kh";
	public static final String FYD_KHDM = "fyd_khdm";
	public static final String FYD_BCGY = "fyd_bcgy";
	public static final String FYD_JSDWGZ = "fyd_jsdwgz";
	public static final String FYD_SPSJ = "fyd_spsj";
	public static final String FYD_JS = "fyd_js";
	public static final String VDEF5 = "vdef5";
	public static final String FYD_JZQZ = "fyd_jzqz";
	public static final String SRL_PKR = "srl_pkr";
	public static final String FYD_RQ = "fyd_rq";
	public static final String FYD_SHDW = "fyd_shdw";
	public static final String FYD_DHZ = "fyd_dhz";
	public static final String DMAKEDATE = "dmakedate";
	public static final String VDEF7 = "vdef7";
	public static final String FYD_LXR = "fyd_lxr";
	public static final String FYD_ENDTS = "fyd_endts";
	public static final String FYD_CH = "fyd_ch";
	public static final String BILLTYPE = "billtype";
	public static final String FYD_ZPRQZ = "fyd_zprqz";
	public static final String VBILLSTATUS = "vbillstatus";
	public static final String VDEF2 = "vdef2";
	public static final String FYD_FYZT = "fyd_fyzt";
	public static final String VDEF1 = "vdef1";
	public static final String VBILLNO = "vbillno";
	public static final String FYD_APPROSTATE = "fyd_approstate";
	public static final String FYD_DBY = "fyd_dby";
	public static final String FYD_JHY = "fyd_jhy";
	public static final String FYD_SHDZ = "fyd_shdz";
	public static final String FYD_SJQZ = "fyd_sjqz";
	public static final String FYD_SJDH = "fyd_sjdh";
	public static final String VDEF9 = "vdef9";
	public static final String DAPPROVEDATE = "dapprovedate";
	public static final String VDEF8 = "vdef8";
	public static final String FYD_YJ = "fyd_yj";
	public static final String VAPPROVEID = "vapproveid";
	public static final String FYD_SJ = "fyd_sj";
	public static final String VDEF6 = "vdef6";
	public static final String FYD_BZ = "fyd_bz";

	/**
	 * 属性vdef4的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return String
	 */
	public String getVdef4() {
		return vdef4;
	}

	/**
	 * 属性vdef4的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newVdef4
	 *            String
	 */
	public void setVdef4(String newVdef4) {

		vdef4 = newVdef4;
	}

	/**
	 * 属性fyd_yslc的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return UFDouble
	 */
	public UFDouble getFyd_yslc() {
		return fyd_yslc;
	}

	/**
	 * 属性fyd_yslc的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newFyd_yslc
	 *            UFDouble
	 */
	public void setFyd_yslc(UFDouble newFyd_yslc) {

		fyd_yslc = newFyd_yslc;
	}

	/**
	 * 属性fyd_ddh的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return String
	 */
	public String getFyd_ddh() {
		return fyd_ddh;
	}

	/**
	 * 属性fyd_ddh的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newFyd_ddh
	 *            String
	 */
	public void setFyd_ddh(String newFyd_ddh) {

		fyd_ddh = newFyd_ddh;
	}

	/**
	 * 属性fyd_yhfs的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return String
	 */
	public String getFyd_yhfs() {
		return fyd_yhfs;
	}

	/**
	 * 属性fyd_yhfs的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newFyd_yhfs
	 *            String
	 */
	public void setFyd_yhfs(String newFyd_yhfs) {

		fyd_yhfs = newFyd_yhfs;
	}

	/**
	 * 属性dr的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return Integer
	 */
	public Integer getDr() {
		return dr;
	}

	/**
	 * 属性dr的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newDr
	 *            Integer
	 */
	public void setDr(Integer newDr) {

		dr = newDr;
	}

	/**
	 * 属性fyd_hjrqz的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return String
	 */
	public String getFyd_hjrqz() {
		return fyd_hjrqz;
	}

	/**
	 * 属性fyd_hjrqz的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newFyd_hjrqz
	 *            String
	 */
	public void setFyd_hjrqz(String newFyd_hjrqz) {

		fyd_hjrqz = newFyd_hjrqz;
	}

	/**
	 * 属性voperatorid的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return String
	 */
	public String getVoperatorid() {
		return voperatorid;
	}

	/**
	 * 属性voperatorid的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newVoperatorid
	 *            String
	 */
	public void setVoperatorid(String newVoperatorid) {

		voperatorid = newVoperatorid;
	}

	/**
	 * 属性fyd_zcsj的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return UFDate
	 */
	public UFDate getFyd_zcsj() {
		return fyd_zcsj;
	}

	/**
	 * 属性fyd_zcsj的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newFyd_zcsj
	 *            UFDate
	 */
	public void setFyd_zcsj(UFDate newFyd_zcsj) {

		fyd_zcsj = newFyd_zcsj;
	}

	/**
	 * 属性vapprovenote的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return String
	 */
	public String getVapprovenote() {
		return vapprovenote;
	}

	/**
	 * 属性vapprovenote的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newVapprovenote
	 *            String
	 */
	public void setVapprovenote(String newVapprovenote) {

		vapprovenote = newVapprovenote;
	}

	/**
	 * 属性fyd_lxdh的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return String
	 */
	public String getFyd_lxdh() {
		return fyd_lxdh;
	}

	/**
	 * 属性fyd_lxdh的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newFyd_lxdh
	 *            String
	 */
	public void setFyd_lxdh(String newFyd_lxdh) {

		fyd_lxdh = newFyd_lxdh;
	}

	/**
	 * 属性fyd_yf的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return UFDouble
	 */
	public UFDouble getFyd_yf() {
		return fyd_yf;
	}

	/**
	 * 属性fyd_yf的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newFyd_yf
	 *            UFDouble
	 */
	public void setFyd_yf(UFDouble newFyd_yf) {

		fyd_yf = newFyd_yf;
	}

	/**
	 * 属性ts的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return String
	 */
	public UFTime getTs() {
		return ts;
	}

	/**
	 * 属性ts的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newTs
	 *            String
	 */
	public void setTs(UFTime newTs) {

		ts = newTs;
	}

	/**
	 * 属性fyd_begints的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return UFDate
	 */
	public UFDate getFyd_begints() {
		return fyd_begints;
	}

	/**
	 * 属性fyd_begints的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newFyd_begints
	 *            String
	 */
	public void setFyd_begints(UFDate newFyd_begints) {

		fyd_begints = newFyd_begints;
	}

	/**
	 * 属性fyd_ysrqz的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return String
	 */
	public String getFyd_ysrqz() {
		return fyd_ysrqz;
	}

	/**
	 * 属性fyd_ysrqz的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newFyd_ysrqz
	 *            String
	 */
	public void setFyd_ysrqz(String newFyd_ysrqz) {

		fyd_ysrqz = newFyd_ysrqz;
	}

	/**
	 * 属性srl_pkr的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return String
	 */
	public String getSrl_pkr() {
		return srl_pkr;
	}

	/**
	 * 属性srl_pkr的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newSrl_pkr
	 *            String
	 */
	public void setSrl_pkr(String newSrl_pkr) {

		srl_pkr = newSrl_pkr;
	}

	/**
	 * 属性fyd_pk的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return String
	 */
	public String getFyd_pk() {
		return fyd_pk;
	}

	/**
	 * 属性fyd_pk的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newFyd_pk
	 *            String
	 */
	public void setFyd_pk(String newFyd_pk) {

		fyd_pk = newFyd_pk;
	}

	/**
	 * 属性vdef3的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return String
	 */
	public String getVdef3() {
		return vdef3;
	}

	/**
	 * 属性vdef3的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newVdef3
	 *            String
	 */
	public void setVdef3(String newVdef3) {

		vdef3 = newVdef3;
	}

	/**
	 * 属性pk_kh的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return String
	 */
	public String getPk_kh() {
		return pk_kh;
	}

	/**
	 * 属性pk_kh的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newPk_kh
	 *            String
	 */
	public void setPk_kh(String newPk_kh) {

		pk_kh = newPk_kh;
	}

	/**
	 * 属性fyd_khdm的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return String
	 */
	public String getFyd_khdm() {
		return fyd_khdm;
	}

	/**
	 * 属性fyd_khdm的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newFyd_khdm
	 *            String
	 */
	public void setFyd_khdm(String newFyd_khdm) {

		fyd_khdm = newFyd_khdm;
	}

	/**
	 * 属性fyd_bcgy的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return UFDouble
	 */
	public UFDouble getFyd_bcgy() {
		return fyd_bcgy;
	}

	/**
	 * 属性fyd_bcgy的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newFyd_bcgy
	 *            UFDouble
	 */
	public void setFyd_bcgy(UFDouble newFyd_bcgy) {

		fyd_bcgy = newFyd_bcgy;
	}

	/**
	 * 属性fyd_jsdwgz的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return String
	 */
	public String getFyd_jsdwgz() {
		return fyd_jsdwgz;
	}

	/**
	 * 属性fyd_jsdwgz的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newFyd_jsdwgz
	 *            String
	 */
	public void setFyd_jsdwgz(String newFyd_jsdwgz) {

		fyd_jsdwgz = newFyd_jsdwgz;
	}

	/**
	 * 属性fyd_spsj的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return String
	 */
	public String getFyd_spsj() {
		return fyd_spsj;
	}

	/**
	 * 属性fyd_spsj的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newFyd_spsj
	 *            String
	 */
	public void setFyd_spsj(String newFyd_spsj) {

		fyd_spsj = newFyd_spsj;
	}

	/**
	 * 属性fyd_js的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return UFDouble
	 */
	public UFDouble getFyd_js() {
		return fyd_js;
	}

	/**
	 * 属性fyd_js的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newFyd_js
	 *            UFDouble
	 */
	public void setFyd_js(UFDouble newFyd_js) {

		fyd_js = newFyd_js;
	}

	/**
	 * 属性vdef5的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return String
	 */
	public String getVdef5() {
		return vdef5;
	}

	/**
	 * 属性vdef5的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newVdef5
	 *            String
	 */
	public void setVdef5(String newVdef5) {

		vdef5 = newVdef5;
	}

	/**
	 * 属性fyd_jzqz的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return String
	 */
	public String getFyd_jzqz() {
		return fyd_jzqz;
	}

	/**
	 * 属性fyd_jzqz的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newFyd_jzqz
	 *            String
	 */
	public void setFyd_jzqz(String newFyd_jzqz) {

		fyd_jzqz = newFyd_jzqz;
	}

	/**
	 * 属性srl_pk的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return String
	 */
	public String getSrl_pk() {
		return srl_pk;
	}

	/**
	 * 属性srl_pk的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newSrl_pkr
	 *            String
	 */
	public void setSrl_pk(String newSrl_pk) {

		srl_pk = newSrl_pk;
	}

	/**
	 * 属性fyd_rq的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return String
	 */
	public String getFyd_rq() {
		return fyd_rq;
	}

	/**
	 * 属性fyd_rq的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newFyd_rq
	 *            String
	 */
	public void setFyd_rq(String newFyd_rq) {

		fyd_rq = newFyd_rq;
	}

	/**
	 * 属性fyd_shdw的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return String
	 */
	public String getFyd_shdw() {
		return fyd_shdw;
	}

	/**
	 * 属性fyd_shdw的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newFyd_shdw
	 *            String
	 */
	public void setFyd_shdw(String newFyd_shdw) {

		fyd_shdw = newFyd_shdw;
	}

	/**
	 * 属性fyd_dhz的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return String
	 */
	public String getFyd_dhz() {
		return fyd_dhz;
	}

	/**
	 * 属性fyd_dhz的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newFyd_dhz
	 *            String
	 */
	public void setFyd_dhz(String newFyd_dhz) {

		fyd_dhz = newFyd_dhz;
	}

	/**
	 * 属性dmakedate的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return UFDate
	 */
	public UFDate getDmakedate() {
		return dmakedate;
	}

	/**
	 * 属性dmakedate的Setter方法.
	 * 
	 * 创建日期:2010-6-30
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
	 * 创建日期:2010-6-30
	 * 
	 * @return String
	 */
	public String getVdef7() {
		return vdef7;
	}

	/**
	 * 属性vdef7的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newVdef7
	 *            String
	 */
	public void setVdef7(String newVdef7) {

		vdef7 = newVdef7;
	}

	/**
	 * 属性fyd_lxr的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return String
	 */
	public String getFyd_lxr() {
		return fyd_lxr;
	}

	/**
	 * 属性fyd_lxr的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newFyd_lxr
	 *            String
	 */
	public void setFyd_lxr(String newFyd_lxr) {

		fyd_lxr = newFyd_lxr;
	}

	/**
	 * 属性fyd_endts的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return UFDate
	 */
	public UFDate getFyd_endts() {
		return fyd_endts;
	}

	/**
	 * 属性fyd_endts的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newFyd_endts
	 *            UFDate
	 */
	public void setFyd_endts(UFDate newFyd_endts) {

		fyd_endts = newFyd_endts;
	}

	/**
	 * 属性fyd_ch的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return String
	 */
	public String getFyd_ch() {
		return fyd_ch;
	}

	/**
	 * 属性fyd_ch的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newFyd_ch
	 *            String
	 */
	public void setFyd_ch(String newFyd_ch) {

		fyd_ch = newFyd_ch;
	}

	/**
	 * 属性Billtype的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return Integer
	 */
	public Integer getBilltype() {
		return billtype;
	}

	/**
	 * 属性billtype的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newbilltype
	 *            Integer
	 */
	public void setBilltype(Integer newBilltype) {

		billtype = newBilltype;
	}

	/**
	 * 属性fyd_zprqz的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return String
	 */
	public String getFyd_zprqz() {
		return fyd_zprqz;
	}

	/**
	 * 属性fyd_zprqz的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newFyd_zprqz
	 *            String
	 */
	public void setFyd_zprqz(String newFyd_zprqz) {

		fyd_zprqz = newFyd_zprqz;
	}

	/**
	 * 属性vbillstatus的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return Integer
	 */
	public Integer getVbillstatus() {
		return vbillstatus;
	}

	/**
	 * 属性vbillstatus的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newVbillstatus
	 *            Integer
	 */
	public void setVbillstatus(Integer newVbillstatus) {

		vbillstatus = newVbillstatus;
	}

	/**
	 * 属性vdef2的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return String
	 */
	public String getVdef2() {
		return vdef2;
	}

	/**
	 * 属性vdef2的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newVdef2
	 *            String
	 */
	public void setVdef2(String newVdef2) {

		vdef2 = newVdef2;
	}

	/**
	 * 属性fyd_fyzt的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return Integer
	 */
	public Integer getFyd_fyzt() {
		return fyd_fyzt;
	}

	/**
	 * 属性fyd_fyzt的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newFyd_fyzt
	 *            Integer
	 */
	public void setFyd_fyzt(Integer newFyd_fyzt) {

		fyd_fyzt = newFyd_fyzt;
	}

	/**
	 * 属性vdef1的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return String
	 */
	public String getVdef1() {
		return vdef1;
	}

	/**
	 * 属性vdef1的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newVdef1
	 *            String
	 */
	public void setVdef1(String newVdef1) {

		vdef1 = newVdef1;
	}

	/**
	 * 属性vbillno的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return String
	 */
	public String getVbillno() {
		return vbillno;
	}

	/**
	 * 属性vbillno的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newVbillno
	 *            String
	 */
	public void setVbillno(String newVbillno) {

		vbillno = newVbillno;
	}

	/**
	 * 属性fyd_approstate的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return Integer
	 */
	public Integer getFyd_approstate() {
		return fyd_approstate;
	}

	/**
	 * 属性fyd_approstate的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newFyd_approstate
	 *            Integer
	 */
	public void setFyd_approstate(Integer newFyd_approstate) {

		fyd_approstate = newFyd_approstate;
	}

	/**
	 * 属性fyd_dby的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return String
	 */
	public String getFyd_dby() {
		return fyd_dby;
	}

	/**
	 * 属性fyd_dby的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newFyd_dby
	 *            String
	 */
	public void setFyd_dby(String newFyd_dby) {

		fyd_dby = newFyd_dby;
	}

	/**
	 * 属性fyd_jhy的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return String
	 */
	public String getFyd_jhy() {
		return fyd_jhy;
	}

	/**
	 * 属性fyd_jhy的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newFyd_jhy
	 *            String
	 */
	public void setFyd_jhy(String newFyd_jhy) {

		fyd_jhy = newFyd_jhy;
	}

	/**
	 * 属性fyd_shdz的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return String
	 */
	public String getFyd_shdz() {
		return fyd_shdz;
	}

	/**
	 * 属性fyd_shdz的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newFyd_shdz
	 *            String
	 */
	public void setFyd_shdz(String newFyd_shdz) {

		fyd_shdz = newFyd_shdz;
	}

	/**
	 * 属性fyd_sjqz的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return String
	 */
	public String getFyd_sjqz() {
		return fyd_sjqz;
	}

	/**
	 * 属性fyd_sjqz的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newFyd_sjqz
	 *            String
	 */
	public void setFyd_sjqz(String newFyd_sjqz) {

		fyd_sjqz = newFyd_sjqz;
	}

	/**
	 * 属性fyd_sjdh的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return String
	 */
	public String getFyd_sjdh() {
		return fyd_sjdh;
	}

	/**
	 * 属性fyd_sjdh的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newFyd_sjdh
	 *            String
	 */
	public void setFyd_sjdh(String newFyd_sjdh) {

		fyd_sjdh = newFyd_sjdh;
	}

	/**
	 * 属性vdef9的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return String
	 */
	public String getVdef9() {
		return vdef9;
	}

	/**
	 * 属性vdef9的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newVdef9
	 *            String
	 */
	public void setVdef9(String newVdef9) {

		vdef9 = newVdef9;
	}

	/**
	 * 属性dapprovedate的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return UFDate
	 */
	public UFDate getDapprovedate() {
		return dapprovedate;
	}

	/**
	 * 属性dapprovedate的Setter方法.
	 * 
	 * 创建日期:2010-6-30
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
	 * 创建日期:2010-6-30
	 * 
	 * @return String
	 */
	public String getVdef8() {
		return vdef8;
	}

	/**
	 * 属性vdef8的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newVdef8
	 *            String
	 */
	public void setVdef8(String newVdef8) {

		vdef8 = newVdef8;
	}

	/**
	 * 属性fyd_yj的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return UFDouble
	 */
	public UFDouble getFyd_yj() {
		return fyd_yj;
	}

	/**
	 * 属性fyd_yj的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newFyd_yj
	 *            UFDouble
	 */
	public void setFyd_yj(UFDouble newFyd_yj) {

		fyd_yj = newFyd_yj;
	}

	/**
	 * 属性vapproveid的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return String
	 */
	public String getVapproveid() {
		return vapproveid;
	}

	/**
	 * 属性vapproveid的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newVapproveid
	 *            String
	 */
	public void setVapproveid(String newVapproveid) {

		vapproveid = newVapproveid;
	}

	/**
	 * 属性fyd_sj的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return String
	 */
	public String getFyd_sj() {
		return fyd_sj;
	}

	/**
	 * 属性fyd_sj的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newFyd_sj
	 *            String
	 */
	public void setFyd_sj(String newFyd_sj) {

		fyd_sj = newFyd_sj;
	}

	/**
	 * 属性vdef6的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return String
	 */
	public String getVdef6() {
		return vdef6;
	}

	/**
	 * 属性vdef6的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newVdef6
	 *            String
	 */
	public void setVdef6(String newVdef6) {

		vdef6 = newVdef6;
	}

	/**
	 * 属性fyd_bz的Getter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return String
	 */
	public String getFyd_bz() {
		return fyd_bz;
	}

	/**
	 * 属性fyd_bz的Setter方法.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newFyd_bz
	 *            String
	 */
	public void setFyd_bz(String newFyd_bz) {

		fyd_bz = newFyd_bz;
	}

	/**
	 * 验证对象各属性之间的数据逻辑正确性.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @exception nc.vo.pub.ValidationException
	 *                如果验证失败,抛出 ValidationException,对错误进行解释.
	 */
	public void validate() throws ValidationException {

		ArrayList errFields = new ArrayList(); // errFields record those null

		// fields that cannot be null.
		// 检查是否为不允许空的字段赋了空值,你可能需要修改下面的提示信息:

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
	 * 创建日期:2010-6-30
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getParentPKFieldName() {

		return "csaleid";

	}

	/**
	 * <p>
	 * 取得表主键.
	 * <p>
	 * 创建日期:2010-6-30
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPKFieldName() {
		return "fyd_pk";
	}

	/**
	 * <p>
	 * 返回表名称.
	 * <p>
	 * 创建日期:2010-6-30
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {

		return "tb_fydnew";
	}

	/**
	 * 按照默认方式创建构造子.
	 * 
	 * 创建日期:2010-6-30
	 */
	public TbFydnewVO() {

		super();
	}

	/**
	 * 使用主键进行初始化的构造子.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newFyd_pk
	 *            主键值
	 */
	public TbFydnewVO(String newFyd_pk) {

		// 为主键字段赋值:
		fyd_pk = newFyd_pk;

	}

	/**
	 * 返回对象标识,用来唯一定位对象.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return String
	 */
	public String getPrimaryKey() {

		return fyd_pk;

	}

	/**
	 * 设置对象标识,用来唯一定位对象.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @param newFyd_pk
	 *            String
	 */
	public void setPrimaryKey(String newFyd_pk) {

		fyd_pk = newFyd_pk;

	}

	/**
	 * 返回数值对象的显示名称.
	 * 
	 * 创建日期:2010-6-30
	 * 
	 * @return java.lang.String 返回数值对象的显示名称.
	 */
	public String getEntityName() {

		return "tb_fydnew";

	}

	public String getCif_pk() {
		return cif_pk;
	}

	public void setCif_pk(String cif_pk) {
		this.cif_pk = cif_pk;
	}

	public UFDate getFyd_xhsj() {
		return fyd_xhsj;
	}

	public void setFyd_xhsj(UFDate fyd_xhsj) {
		this.fyd_xhsj = fyd_xhsj;
	}

	public String getPk_psndoc() {
		return pk_psndoc;
	}

	public void setPk_psndoc(String pk_psndoc) {
		this.pk_psndoc = pk_psndoc;
	}

	public String getPk_busitype() {
		return pk_busitype;
	}

	public void setPk_busitype(String pk_busitype) {
		this.pk_busitype = pk_busitype;
	}

	public String getCsaleid() {
		return csaleid;
	}

	public void setCsaleid(String csaleid) {
		this.csaleid = csaleid;
	}

	public String getSe_pk() {
		return se_pk;
	}

	public void setSe_pk(String se_pk) {
		this.se_pk = se_pk;
	}

	public String getMergelogo() {
		return mergelogo;
	}

	public void setMergelogo(String mergelogo) {
		this.mergelogo = mergelogo;
	}

	public String getFyd_zdr() {
		return fyd_zdr;
	}

	public void setFyd_zdr(String fyd_zdr) {
		this.fyd_zdr = fyd_zdr;
	}

	public UFDate getFyd_zdsj() {
		return fyd_zdsj;
	}

	public void setFyd_zdsj(UFDate fyd_zdsj) {
		this.fyd_zdsj = fyd_zdsj;
	}

	public String getCdeptid() {
		return cdeptid;
	}

	public void setCdeptid(String cdeptid) {
		this.cdeptid = cdeptid;
	}

	public String getSplitvbillno() {
		return splitvbillno;
	}

	public void setSplitvbillno(String splitvbillno) {
		this.splitvbillno = splitvbillno;
	}

	public String getTc_pk() {
		return tc_pk;
	}

	public void setTc_pk(String tc_pk) {
		this.tc_pk = tc_pk;
	}

	public String getFyd_yjzl() {
		return fyd_yjzl;
	}

	public void setFyd_yjzl(String fyd_yjzl) {
		this.fyd_yjzl = fyd_yjzl;
	}

	public Integer getFyd_constatus() {
		return fyd_constatus;
	}

	public void setFyd_constatus(Integer fyd_constatus) {
		this.fyd_constatus = fyd_constatus;
	}

	public String getPk_yjzbzj() {
		return pk_yjzbzj;
	}

	public void setPk_yjzbzj(String pk_yjzbzj) {
		this.pk_yjzbzj = pk_yjzbzj;
	}

	public String getFyd_busitype() {
		return fyd_busitype;
	}

	public void setFyd_busitype(String fyd_busitype) {
		this.fyd_busitype = fyd_busitype;
	}

	public Integer getIprintcount() {
		return iprintcount;
	}

	public void setIprintcount(Integer iprintcount) {
		this.iprintcount = iprintcount;
	}

	public Integer getFyd_splitstatus() {
		return fyd_splitstatus;
	}

	public void setFyd_splitstatus(Integer fyd_splitstatus) {
		this.fyd_splitstatus = fyd_splitstatus;
	}

	public UFDate getIprintdate() {
		return iprintdate;
	}

	public void setIprintdate(UFDate iprintdate) {
		this.iprintdate = iprintdate;
	}

	public String getDmaketime() {
		return dmaketime;
	}

	public void setDmaketime(String dmaketime) {
		this.dmaketime = dmaketime;
	}

	public String getPk_mergelogo() {
		return pk_mergelogo;
	}

	public void setPk_mergelogo(String pk_mergelogo) {
		this.pk_mergelogo = pk_mergelogo;
	}

	public UFBoolean getFyd_splitend() {
		return fyd_splitend;
	}

	public void setFyd_splitend(UFBoolean fyd_splitend) {
		this.fyd_splitend = fyd_splitend;
	}

	public UFDouble getFyd_fjf() {
		return fyd_fjf;
	}

	public void setFyd_fjf(UFDouble fyd_fjf) {
		this.fyd_fjf = fyd_fjf;
	}

	public UFBoolean getIsbig() {
		return isbig;
	}

	public void setIsbig(UFBoolean isbig) {
		this.isbig = isbig;
	}

}
