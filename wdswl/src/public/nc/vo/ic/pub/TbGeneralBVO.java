/***************************************************************\
 *     The skeleton of this class is generated by an automatic *
 * code generator for NC product. It is based on Velocity.     *
\***************************************************************/
package nc.vo.ic.pub;

import java.util.ArrayList;
import java.util.List;

import nc.vo.pub.NullFieldException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFTime;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.WdsWlPubTool;

/**
 * <b> 在此处简要描述此类的功能 </b>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 * 创建日期:2010-7-19
 * @author ${vmObject.author}
 * @version Your Project 1.0
 */
public class TbGeneralBVO extends SuperVO {	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String geb_cgeneralbid;
	public String geb_crowno;
	public String geb_space;//货位

//	public String measdocname;



	
	public UFBoolean geb_flargess;//是否赠品
	public String geb_pk;//ID
	public String geb_cinvbasid;//存货基本id

	public String cdt_pk;
	public String geh_pk;
	public String geb_cinventoryid;//存货管理id

	public UFDouble geb_nmny;//金额
	public UFDouble geb_snum;//应收数量
	public UFDouble geb_bsnum;//应收辅数量
	public UFDouble geb_anum;//实收数量	
	public UFDouble geb_banum;//实收辅数量

	public UFDouble geb_tnum;//托盘存货量
	public UFDouble geb_tbnum;//托盘存货辅量

	public UFDate geb_dbizdate;//业务日期
	
	public UFDate geb_dvalidate;//失效日期
	public UFDate geb_proddate;//生产日期
//	public String geb_cinvbasename;

	public String castunitid;//辅计量单位ID


//	public String geb_cinvenroryname;
	public UFDate geb_freightdate;//运货日期
//	public String bmeasdocname;
	public String pk_measdoc;//单位主键

	public String geb_vbatchcode;//批次号
	public String geb_invtype;
	public String vnote;

	public UFDouble geb_hsl;//换算率
	public UFDouble geb_nprice;//单价
//	public String geb_invspec;//规格

	public String pwbb_pk;//运单表体主键
	public String pwb_pk;//运单表头主键
	public String geb_cgeneralhid;//调拨出库表头主键
	public String geb_backvbatchcode;//要回写的批次号
	public UFDouble geb_virtualnum;//虚拟在途主数量
	public UFDouble geb_virtualbnum;//虚拟在途辅数量
	public UFBoolean geb_isclose;//单据是否关闭

	public String gylbillcode;//供应链表头单据号
	public String gylbilltype;//供应链表头单据类型
	public String gylbillhid;//供应链表HID
	public String gylbillbid;//供应链表BID

	
	
	private List<TbGeneralBBVO> trayInfor = null;//托盘明细子表信息

	public List<TbGeneralBBVO> getTrayInfor(){
		return trayInfor;
	}
	public void setTrayInfor(List<TbGeneralBBVO> ltrayInfor){
		this.trayInfor = ltrayInfor;
	}

	public void addTray(TbGeneralBBVO tray){
		if(tray == null)
			return;
		if(trayInfor == null){
			trayInfor = new ArrayList<TbGeneralBBVO>();
		}
		trayInfor.add(tray);
	}

	public String pk_customize5;
	public String geb_customize5;
	public String pk_customize8;
	public String pk_customize2;
	public String geb_customize1;
	public String pk_customize3;
	public String geb_customize2;
	public String pk_customize9;
	public String pk_customize6;
	public String geb_customize9;
	public String pk_customize4;
	public String geb_customize4;
	public String geb_customize7;
	public String pk_customize7;
	public String geb_customize8;
	public String pk_customize1;
	public String geb_customize3;
	public String geb_customize6;
	public UFTime ts;
	public Integer dr;


	public String  vfirstbillcode;
	public String cfirsttype;
	public String cfirstbillhid;
	public String cfirstbillbid;
	public String vsourcebillcode;
	public String csourcetype;
	public String csourcebillhid;
	public String csourcebillbid;
	public static final String GEB_ISCLOSE="geb_isclose";
	public static final String GEB_VIRTUALBNUM="geb_virtualbnum";
	public static final String GEB_VIRTUALNUM="geb_virtualnum";
	public static final String GEB_BACKVBATCHCODE="geb_backvbatchcode";
	public static final String GEB_CGENERALHID="geb_cgeneralhid";
	public static final String GEB_CUSTOMIZE1 = "geb_customize1";
	public static final String PK_CUSTOMIZE3 = "pk_customize3";
	public static final String GEB_CUSTOMIZE2 = "geb_customize2";
	public static final String DR = "dr";
	public static final String GEB_CGENERALBID = "geb_cgeneralbid";
	public static final String GEB_CROWNO = "geb_crowno";
	public static final String GEB_SPACE = "geb_space";
	public static final String GEB_CUSTOMIZE6 = "geb_customize6";
	public static final String MEASDOCNAME = "measdocname";
	public static final String GEB_CUSTOMIZE3 = "geb_customize3";
	public static final String GEB_SNUM = "geb_snum";
	public static final String PK_CUSTOMIZE1 = "pk_customize1";
	public static final String TS = "ts";
	public static final String GEB_DVALIDATE = "geb_dvalidate";
	public static final String GEB_FLARGESS = "geb_flargess";
	public static final String GEB_PK = "geb_pk";
	public static final String GEB_CINVBASID = "geb_cinvbasid";
	public static final String GEB_CUSTOMIZE8 = "geb_customize8";
	public static final String CDT_PK = "cdt_pk";
	public static final String GEH_PK = "geh_pk";
	public static final String GEB_CINVENTORYID = "geb_cinventoryid";
	public static final String PK_CUSTOMIZE7 = "pk_customize7";
	public static final String GEB_NMNY = "geb_nmny";
	public static final String GEB_BSNUM = "geb_bsnum";
	public static final String GEB_TBNUM = "geb_tbnum";
	public static final String GEB_CUSTOMIZE4 = "geb_customize4";
	public static final String GEB_CUSTOMIZE7 = "geb_customize7";
	public static final String GEB_DBIZDATE = "geb_dbizdate";
	public static final String GEB_PRODDATE = "geb_proddate";
	public static final String GEB_CINVBASENAME = "geb_cinvbasename";
	public static final String GEB_CUSTOMIZE9 = "geb_customize9";
	public static final String PK_CUSTOMIZE4 = "pk_customize4";
	public static final String CASTUNITID = "castunitid";
	public static final String GEB_TNUM = "geb_tnum";
	public static final String PK_CUSTOMIZE9 = "pk_customize9";
	public static final String PK_CUSTOMIZE6 = "pk_customize6";
	public static final String GEB_CINVENRORYNAME = "geb_cinvenroryname";
	public static final String GEB_FREIGHTDATE = "geb_freightdate";
	public static final String BMEASDOCNAME = "bmeasdocname";
	public static final String PK_MEASDOC = "pk_measdoc";
	public static final String GEB_ANUM = "geb_anum";
	public static final String GEB_BANUM = "geb_banum";
	public static final String GEB_VBATCHCODE = "geb_vbatchcode";
	public static final String GEB_INVTYPE = "geb_invtype";
	public static final String VNOTE = "vnote";
	public static final String GEB_CUSTOMIZE5 = "geb_customize5";
	public static final String PK_CUSTOMIZE8 = "pk_customize8";
	public static final String PK_CUSTOMIZE2 = "pk_customize2";
	public static final String GEB_HSL = "geb_hsl";
	public static final String GEB_NPRICE = "geb_nprice";
	public static final String GEB_INVSPEC = "geb_invspec";
	public static final String PK_CUSTOMIZE5 = "pk_customize5";
	public static final String PWBB_PK="pwbb_pk";
	public static final String PWB_PK="pwb_pk";

	/**
	 * 属性geb_customize1的Getter方法.
	 *
	 * 创建日期:2010-7-19
	 * @return String
	 */
	public String getGeb_customize1() {
		return geb_customize1;
	}

	/**
	 * 属性geb_customize1的Setter方法.
	 *
	 * 创建日期:2010-7-19
	 * @param newGeb_customize1 String
	 */
	public void setGeb_customize1(String newGeb_customize1) {

		geb_customize1 = newGeb_customize1;
	}

	/**
	 * 属性pk_customize3的Getter方法.
	 *
	 * 创建日期:2010-7-19
	 * @return String
	 */
	public String getPk_customize3() {
		return pk_customize3;
	}

	/**
	 * 属性pk_customize3的Setter方法.
	 *
	 * 创建日期:2010-7-19
	 * @param newPk_customize3 String
	 */
	public void setPk_customize3(String newPk_customize3) {

		pk_customize3 = newPk_customize3;
	}

	/**
	 * 属性geb_customize2的Getter方法.
	 *
	 * 创建日期:2010-7-19
	 * @return String
	 */
	public String getGeb_customize2() {
		return geb_customize2;
	}

	/**
	 * 属性geb_customize2的Setter方法.
	 *
	 * 创建日期:2010-7-19
	 * @param newGeb_customize2 String
	 */
	public void setGeb_customize2(String newGeb_customize2) {

		geb_customize2 = newGeb_customize2;
	}

	/**
	 * 属性dr的Getter方法.
	 *
	 * 创建日期:2010-7-19
	 * @return Integer
	 */
	public Integer getDr() {
		return dr;
	}

	/**
	 * 属性dr的Setter方法.
	 *
	 * 创建日期:2010-7-19
	 * @param newDr Integer
	 */
	public void setDr(Integer newDr) {

		dr = newDr;
	}

	/**
	 * 属性geb_cgeneralbid的Getter方法.
	 *
	 * 创建日期:2010-7-19
	 * @return String
	 */
	public String getGeb_cgeneralbid() {
		return geb_cgeneralbid;
	}

	/**
	 * 属性geb_cgeneralbid的Setter方法.
	 *
	 * 创建日期:2010-7-19
	 * @param newGeb_cgeneralbid String
	 */
	public void setGeb_cgeneralbid(String newGeb_cgeneralbid) {

		geb_cgeneralbid = newGeb_cgeneralbid;
	}

	/**
	 * 属性geb_crowno的Getter方法.
	 *
	 * 创建日期:2010-7-19
	 * @return Integer
	 */
	public String getGeb_crowno() {
		return geb_crowno;
	}

	/**
	 * 属性geb_crowno的Setter方法.
	 *
	 * 创建日期:2010-7-19
	 * @param newGeb_crowno Integer
	 */
	public void setGeb_crowno(String newGeb_crowno) {

		geb_crowno = newGeb_crowno;
	}

	/**
	 * 属性geb_space的Getter方法.
	 *
	 * 创建日期:2010-7-19
	 * @return String
	 */
	public String getGeb_space() {
		return geb_space;
	}

	/**
	 * 属性geb_space的Setter方法.
	 *
	 * 创建日期:2010-7-19
	 * @param newGeb_space String
	 */
	public void setGeb_space(String newGeb_space) {

		geb_space = newGeb_space;
	}

	/**
	 * 属性geb_customize6的Getter方法.
	 *
	 * 创建日期:2010-7-19
	 * @return String
	 */
	public String getGeb_customize6() {
		return geb_customize6;
	}

	/**
	 * 属性geb_customize6的Setter方法.
	 *
	 * 创建日期:2010-7-19
	 * @param newGeb_customize6 String
	 */
	public void setGeb_customize6(String newGeb_customize6) {

		geb_customize6 = newGeb_customize6;
	}

	/**
	 * 属性geb_customize3的Getter方法.
	 *
	 * 创建日期:2010-7-19
	 * @return String
	 */
	public String getGeb_customize3() {
		return geb_customize3;
	}

	/**
	 * 属性geb_customize3的Setter方法.
	 *
	 * 创建日期:2010-7-19
	 * @param newGeb_customize3 String
	 */
	public void setGeb_customize3(String newGeb_customize3) {

		geb_customize3 = newGeb_customize3;
	}

	/**
	 * 属性geb_snum的Getter方法.
	 *
	 * 创建日期:2010-7-19
	 * @return UFDouble
	 */
	public UFDouble getGeb_snum() {
		return geb_snum;
	}

	/**
	 * 属性geb_snum的Setter方法.
	 *
	 * 创建日期:2010-7-19
	 * @param newGeb_snum UFDouble
	 */
	public void setGeb_snum(UFDouble newGeb_snum) {

		geb_snum = newGeb_snum;
	}

	/**
	 * 属性pk_customize1的Getter方法.
	 *
	 * 创建日期:2010-7-19
	 * @return String
	 */
	public String getPk_customize1() {
		return pk_customize1;
	}

	/**
	 * 属性pk_customize1的Setter方法.
	 *
	 * 创建日期:2010-7-19
	 * @param newPk_customize1 String
	 */
	public void setPk_customize1(String newPk_customize1) {

		pk_customize1 = newPk_customize1;
	}

	/**
	 * 属性ts的Getter方法.
	 *
	 * 创建日期:2010-7-19
	 * @return UFDateTime
	 */
	public UFTime getTs() {
		return ts;
	}

	/**
	 * 属性ts的Setter方法.
	 *
	 * 创建日期:2010-7-19
	 * @param newTs UFDateTime
	 */
	public void setTs(UFTime newTs) {

		ts = newTs;
	}

	/**
	 * 属性geb_dvalidate的Getter方法.
	 *
	 * 创建日期:2010-7-19
	 * @return UFDate
	 */
	public UFDate getGeb_dvalidate() {
		return geb_dvalidate;
	}

	/**
	 * 属性geb_dvalidate的Setter方法.
	 *
	 * 创建日期:2010-7-19
	 * @param newGeb_dvalidate UFDate
	 */
	public void setGeb_dvalidate(UFDate newGeb_dvalidate) {

		geb_dvalidate = newGeb_dvalidate;
	}

	/**
	 * 属性geb_flargess的Getter方法.
	 *
	 * 创建日期:2010-7-19
	 * @return String
	 */
	public UFBoolean getGeb_flargess() {
		return geb_flargess;
	}

	/**
	 * 属性geb_flargess的Setter方法.
	 *
	 * 创建日期:2010-7-19
	 * @param newGeb_flargess String
	 */
	public void setGeb_flargess(UFBoolean newGeb_flargess) {

		geb_flargess = newGeb_flargess;
	}

	/**
	 * 属性geb_pk的Getter方法.
	 *
	 * 创建日期:2010-7-19
	 * @return String
	 */
	public String getGeb_pk() {
		return geb_pk;
	}

	/**
	 * 属性geb_pk的Setter方法.
	 *
	 * 创建日期:2010-7-19
	 * @param newGeb_pk String
	 */
	public void setGeb_pk(String newGeb_pk) {

		geb_pk = newGeb_pk;
	}

	/**
	 * 属性geb_cinvbasid的Getter方法.
	 *
	 * 创建日期:2010-7-19
	 * @return String
	 */
	public String getGeb_cinvbasid() {
		return geb_cinvbasid;
	}

	/**
	 * 属性geb_cinvbasid的Setter方法.
	 *
	 * 创建日期:2010-7-19
	 * @param newGeb_cinvbasid String
	 */
	public void setGeb_cinvbasid(String newGeb_cinvbasid) {

		geb_cinvbasid = newGeb_cinvbasid;
	}

	/**
	 * 属性geb_customize8的Getter方法.
	 *
	 * 创建日期:2010-7-19
	 * @return String
	 */
	public String getGeb_customize8() {
		return geb_customize8;
	}

	/**
	 * 属性geb_customize8的Setter方法.
	 *
	 * 创建日期:2010-7-19
	 * @param newGeb_customize8 String
	 */
	public void setGeb_customize8(String newGeb_customize8) {

		geb_customize8 = newGeb_customize8;
	}

	/**
	 * 属性cdt_pk的Getter方法.
	 *
	 * 创建日期:2010-7-19
	 * @return String
	 */
	public String getCdt_pk() {
		return cdt_pk;
	}

	/**
	 * 属性cdt_pk的Setter方法.
	 *
	 * 创建日期:2010-7-19
	 * @param newCdt_pk String
	 */
	public void setCdt_pk(String newCdt_pk) {

		cdt_pk = newCdt_pk;
	}

	/**
	 * 属性geh_pk的Getter方法.
	 *
	 * 创建日期:2010-7-19
	 * @return String
	 */
	public String getGeh_pk() {
		return geh_pk;
	}

	/**
	 * 属性geh_pk的Setter方法.
	 *
	 * 创建日期:2010-7-19
	 * @param newGeh_pk String
	 */
	public void setGeh_pk(String newGeh_pk) {

		geh_pk = newGeh_pk;
	}

	/**
	 * 属性geb_cinventoryid的Getter方法.
	 *
	 * 创建日期:2010-7-19
	 * @return String
	 */
	public String getGeb_cinventoryid() {
		return geb_cinventoryid;
	}

	/**
	 * 属性geb_cinventoryid的Setter方法.
	 *
	 * 创建日期:2010-7-19
	 * @param newGeb_cinventoryid String
	 */
	public void setGeb_cinventoryid(String newGeb_cinventoryid) {

		geb_cinventoryid = newGeb_cinventoryid;
	}

	/**
	 * 属性pk_customize7的Getter方法.
	 *
	 * 创建日期:2010-7-19
	 * @return String
	 */
	public String getPk_customize7() {
		return pk_customize7;
	}

	/**
	 * 属性pk_customize7的Setter方法.
	 *
	 * 创建日期:2010-7-19
	 * @param newPk_customize7 String
	 */
	public void setPk_customize7(String newPk_customize7) {

		pk_customize7 = newPk_customize7;
	}

	/**
	 * 属性geb_nmny的Getter方法.
	 *
	 * 创建日期:2010-7-19
	 * @return UFDouble
	 */
	public UFDouble getGeb_nmny() {
		return geb_nmny;
	}

	/**
	 * 属性geb_nmny的Setter方法.
	 *
	 * 创建日期:2010-7-19
	 * @param newGeb_nmny UFDouble
	 */
	public void setGeb_nmny(UFDouble newGeb_nmny) {

		geb_nmny = newGeb_nmny;
	}

	/**
	 * 属性geb_bsnum的Getter方法.
	 *
	 * 创建日期:2010-7-19
	 * @return UFDouble
	 */
	public UFDouble getGeb_bsnum() {
		return geb_bsnum;
	}

	/**
	 * 属性geb_bsnum的Setter方法.
	 *
	 * 创建日期:2010-7-19
	 * @param newGeb_bsnum UFDouble
	 */
	public void setGeb_bsnum(UFDouble newGeb_bsnum) {

		geb_bsnum = newGeb_bsnum;
	}

	/**
	 * 属性geb_tbnum的Getter方法.
	 *
	 * 创建日期:2010-7-19
	 * @return UFDouble
	 */
	public UFDouble getGeb_tbnum() {
		return geb_tbnum;
	}

	/**
	 * 属性geb_tbnum的Setter方法.
	 *
	 * 创建日期:2010-7-19
	 * @param newGeb_tbnum UFDouble
	 */
	public void setGeb_tbnum(UFDouble newGeb_tbnum) {

		geb_tbnum = newGeb_tbnum;
	}

	/**
	 * 属性geb_customize4的Getter方法.
	 *
	 * 创建日期:2010-7-19
	 * @return String
	 */
	public String getGeb_customize4() {
		return geb_customize4;
	}

	/**
	 * 属性geb_customize4的Setter方法.
	 *
	 * 创建日期:2010-7-19
	 * @param newGeb_customize4 String
	 */
	public void setGeb_customize4(String newGeb_customize4) {

		geb_customize4 = newGeb_customize4;
	}

	/**
	 * 属性geb_customize7的Getter方法.
	 *
	 * 创建日期:2010-7-19
	 * @return String
	 */
	public String getGeb_customize7() {
		return geb_customize7;
	}

	/**
	 * 属性geb_customize7的Setter方法.
	 *
	 * 创建日期:2010-7-19
	 * @param newGeb_customize7 String
	 */
	public void setGeb_customize7(String newGeb_customize7) {

		geb_customize7 = newGeb_customize7;
	}

	/**
	 * 属性geb_dbizdate的Getter方法.
	 *
	 * 创建日期:2010-7-19
	 * @return UFDate
	 */
	public UFDate getGeb_dbizdate() {
		return geb_dbizdate;
	}

	/**
	 * 属性geb_dbizdate的Setter方法.
	 *
	 * 创建日期:2010-7-19
	 * @param newGeb_dbizdate UFDate
	 */
	public void setGeb_dbizdate(UFDate newGeb_dbizdate) {

		geb_dbizdate = newGeb_dbizdate;
	}

	/**
	 * 属性geb_proddate的Getter方法.
	 *
	 * 创建日期:2010-7-19
	 * @return UFDate
	 */
	public UFDate getGeb_proddate() {
		return geb_proddate;
	}

	/**
	 * 属性geb_proddate的Setter方法.
	 *
	 * 创建日期:2010-7-19
	 * @param newGeb_proddate UFDate
	 */
	public void setGeb_proddate(UFDate newGeb_proddate) {

		geb_proddate = newGeb_proddate;
	}


	/**
	 * 属性geb_customize9的Getter方法.
	 *
	 * 创建日期:2010-7-19
	 * @return String
	 */
	public String getGeb_customize9() {
		return geb_customize9;
	}

	/**
	 * 属性geb_customize9的Setter方法.
	 *
	 * 创建日期:2010-7-19
	 * @param newGeb_customize9 String
	 */
	public void setGeb_customize9(String newGeb_customize9) {

		geb_customize9 = newGeb_customize9;
	}

	/**
	 * 属性pk_customize4的Getter方法.
	 *
	 * 创建日期:2010-7-19
	 * @return String
	 */
	public String getPk_customize4() {
		return pk_customize4;
	}

	/**
	 * 属性pk_customize4的Setter方法.
	 *
	 * 创建日期:2010-7-19
	 * @param newPk_customize4 String
	 */
	public void setPk_customize4(String newPk_customize4) {

		pk_customize4 = newPk_customize4;
	}

	/**
	 * 属性castunitid的Getter方法.
	 *
	 * 创建日期:2010-7-19
	 * @return String
	 */
	public String getCastunitid() {
		return castunitid;
	}

	/**
	 * 属性castunitid的Setter方法.
	 *
	 * 创建日期:2010-7-19
	 * @param newCastunitid String
	 */
	public void setCastunitid(String newCastunitid) {

		castunitid = newCastunitid;
	}

	/**
	 * 属性geb_tnum的Getter方法.
	 *
	 * 创建日期:2010-7-19
	 * @return UFDouble
	 */
	public UFDouble getGeb_tnum() {
		return geb_tnum;
	}

	/**
	 * 属性geb_tnum的Setter方法.
	 *
	 * 创建日期:2010-7-19
	 * @param newGeb_tnum UFDouble
	 */
	public void setGeb_tnum(UFDouble newGeb_tnum) {

		geb_tnum = newGeb_tnum;
	}

	/**
	 * 属性pk_customize9的Getter方法.
	 *
	 * 创建日期:2010-7-19
	 * @return String
	 */
	public String getPk_customize9() {
		return pk_customize9;
	}

	/**
	 * 属性pk_customize9的Setter方法.
	 *
	 * 创建日期:2010-7-19
	 * @param newPk_customize9 String
	 */
	public void setPk_customize9(String newPk_customize9) {

		pk_customize9 = newPk_customize9;
	}

	/**
	 * 属性pk_customize6的Getter方法.
	 *
	 * 创建日期:2010-7-19
	 * @return String
	 */
	public String getPk_customize6() {
		return pk_customize6;
	}

	/**
	 * 属性pk_customize6的Setter方法.
	 *
	 * 创建日期:2010-7-19
	 * @param newPk_customize6 String
	 */
	public void setPk_customize6(String newPk_customize6) {

		pk_customize6 = newPk_customize6;
	}

	 /* 属性geb_freightdate的Getter方法.
	 *
	 * 创建日期:2010-7-19
	 * @return UFDate
	 */
	public UFDate getGeb_freightdate() {
		return geb_freightdate;
	}

	/**
	 * 属性geb_freightdate的Setter方法.
	 *
	 * 创建日期:2010-7-19
	 * @param newGeb_freightdate UFDate
	 */
	public void setGeb_freightdate(UFDate newGeb_freightdate) {

		geb_freightdate = newGeb_freightdate;
	}


	/**
	 * 属性pk_measdoc的Getter方法.
	 *
	 * 创建日期:2010-7-19
	 * @return String
	 */
	public String getPk_measdoc() {
		return pk_measdoc;
	}

	/**
	 * 属性pk_measdoc的Setter方法.
	 *
	 * 创建日期:2010-7-19
	 * @param newPk_measdoc String
	 */
	public void setPk_measdoc(String newPk_measdoc) {

		pk_measdoc = newPk_measdoc;
	}

	/**
	 * 属性geb_anum的Getter方法.
	 *
	 * 创建日期:2010-7-19
	 * @return UFDouble
	 */
	public UFDouble getGeb_anum() {
		return geb_anum;
	}

	/**
	 * 属性geb_anum的Setter方法.
	 *
	 * 创建日期:2010-7-19
	 * @param newGeb_anum UFDouble
	 */
	public void setGeb_anum(UFDouble newGeb_anum) {

		geb_anum = newGeb_anum;
	}

	/**
	 * 属性geb_banum的Getter方法.
	 *
	 * 创建日期:2010-7-19
	 * @return UFDouble
	 */
	public UFDouble getGeb_banum() {
		return geb_banum;
	}

	/**
	 * 属性geb_banum的Setter方法.
	 *
	 * 创建日期:2010-7-19
	 * @param newGeb_banum UFDouble
	 */
	public void setGeb_banum(UFDouble newGeb_banum) {

		geb_banum = newGeb_banum;
	}

	/**
	 * 属性geb_vbatchcode的Getter方法.
	 *
	 * 创建日期:2010-7-19
	 * @return String
	 */
	public String getGeb_vbatchcode() {
		return geb_vbatchcode;
	}

	/**
	 * 属性geb_vbatchcode的Setter方法.
	 *
	 * 创建日期:2010-7-19
	 * @param newGeb_vbatchcode String
	 */
	public void setGeb_vbatchcode(String newGeb_vbatchcode) {

		geb_vbatchcode = newGeb_vbatchcode;
	}

	/**
	 * 属性geb_invtype的Getter方法.
	 *
	 * 创建日期:2010-7-19
	 * @return String
	 */
	public String getGeb_invtype() {
		return geb_invtype;
	}

	/**
	 * 属性geb_invtype的Setter方法.
	 *
	 * 创建日期:2010-7-19
	 * @param newGeb_invtype String
	 */
	public void setGeb_invtype(String newGeb_invtype) {

		geb_invtype = newGeb_invtype;
	}

	/**
	 * 属性vnote的Getter方法.
	 *
	 * 创建日期:2010-7-19
	 * @return String
	 */
	public String getVnote() {
		return vnote;
	}

	/**
	 * 属性vnote的Setter方法.
	 *
	 * 创建日期:2010-7-19
	 * @param newVnote String
	 */
	public void setVnote(String newVnote) {

		vnote = newVnote;
	}

	/**
	 * 属性geb_customize5的Getter方法.
	 *
	 * 创建日期:2010-7-19
	 * @return String
	 */
	public String getGeb_customize5() {
		return geb_customize5;
	}

	/**
	 * 属性geb_customize5的Setter方法.
	 *
	 * 创建日期:2010-7-19
	 * @param newGeb_customize5 String
	 */
	public void setGeb_customize5(String newGeb_customize5) {

		geb_customize5 = newGeb_customize5;
	}

	/**
	 * 属性pk_customize8的Getter方法.
	 *
	 * 创建日期:2010-7-19
	 * @return String
	 */
	public String getPk_customize8() {
		return pk_customize8;
	}

	/**
	 * 属性pk_customize8的Setter方法.
	 *
	 * 创建日期:2010-7-19
	 * @param newPk_customize8 String
	 */
	public void setPk_customize8(String newPk_customize8) {

		pk_customize8 = newPk_customize8;
	}

	/**
	 * 属性pk_customize2的Getter方法.
	 *
	 * 创建日期:2010-7-19
	 * @return String
	 */
	public String getPk_customize2() {
		return pk_customize2;
	}

	/**
	 * 属性pk_customize2的Setter方法.
	 *
	 * 创建日期:2010-7-19
	 * @param newPk_customize2 String
	 */
	public void setPk_customize2(String newPk_customize2) {

		pk_customize2 = newPk_customize2;
	}

	/**
	 * 属性geb_hsl的Getter方法.
	 *
	 * 创建日期:2010-7-19
	 * @return UFDouble
	 */
	public UFDouble getGeb_hsl() {
		return geb_hsl;
	}

	/**
	 * 属性geb_hsl的Setter方法.
	 *
	 * 创建日期:2010-7-19
	 * @param newGeb_hsl UFDouble
	 */
	public void setGeb_hsl(UFDouble newGeb_hsl) {

		geb_hsl = newGeb_hsl;
	}

	/**
	 * 属性geb_nprice的Getter方法.
	 *
	 * 创建日期:2010-7-19
	 * @return UFDouble
	 */
	public UFDouble getGeb_nprice() {
		return geb_nprice;
	}

	/**
	 * 属性geb_nprice的Setter方法.
	 *
	 * 创建日期:2010-7-19
	 * @param newGeb_nprice UFDouble
	 */
	public void setGeb_nprice(UFDouble newGeb_nprice) {

		geb_nprice = newGeb_nprice;
	}


	/**
	 * 属性pk_customize5的Getter方法.
	 *
	 * 创建日期:2010-7-19
	 * @return String
	 */
	public String getPk_customize5() {
		return pk_customize5;
	}

	/**
	 * 属性pk_customize5的Setter方法.
	 *
	 * 创建日期:2010-7-19
	 * @param newPk_customize5 String
	 */
	public void setPk_customize5(String newPk_customize5) {

		pk_customize5 = newPk_customize5;
	}

	/**
	 * 验证对象各属性之间的数据逻辑正确性.
	 *
	 * 创建日期:2010-7-19
	 * @exception nc.vo.pub.ValidationException 如果验证失败,抛出
	 * ValidationException,对错误进行解释.
	 */
	public void validate() throws ValidationException {

		ArrayList errFields = new ArrayList(); // errFields record those null

		// fields that cannot be null.
		// 检查是否为不允许空的字段赋了空值,你可能需要修改下面的提示信息:

		if (geb_pk == null) {
			errFields.add(new String("geb_pk"));
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
	 * <p>取得父VO主键字段.
	 * <p>
	 * 创建日期:2010-7-19
	 * @return java.lang.String
	 */
	public java.lang.String getParentPKFieldName() {

		return "geh_pk";

	}

	/**
	 * <p>取得表主键.
	 * <p>
	 * 创建日期:2010-7-19
	 * @return java.lang.String
	 */
	public java.lang.String getPKFieldName() {
		return "geb_pk";
	}

	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:2010-7-19
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {

		return "tb_general_b";
	}

	/**
	 * 按照默认方式创建构造子.
	 *
	 * 创建日期:2010-7-19
	 */
	public TbGeneralBVO() {

		super();
	}

	/**
	 * 使用主键进行初始化的构造子.
	 *
	 * 创建日期:2010-7-19
	 * @param newGeb_pk 主键值
	 */
	public TbGeneralBVO(String newGeb_pk) {

		// 为主键字段赋值:
		geb_pk = newGeb_pk;

	}

	/**
	 * 返回对象标识,用来唯一定位对象.
	 *
	 * 创建日期:2010-7-19
	 * @return String
	 */
	public String getPrimaryKey() {

		return geb_pk;

	}

	/**
	 * 设置对象标识,用来唯一定位对象.
	 *
	 * 创建日期:2010-7-19
	 * @param newGeb_pk  String    
	 */
	public void setPrimaryKey(String newGeb_pk) {

		geb_pk = newGeb_pk;

	}

	/**
	 * 返回数值对象的显示名称.
	 *
	 * 创建日期:2010-7-19
	 * @return java.lang.String 返回数值对象的显示名称.
	 */
	public String getEntityName() {

		return "tb_general_b";

	}

	public String getPwbb_pk() {
		return pwbb_pk;
	}

	public void setPwbb_pk(String pwbb_pk) {
		this.pwbb_pk = pwbb_pk;
	}

	public String getPwb_pk() {
		return pwb_pk;
	}

	public void setPwb_pk(String pwb_pk) {
		this.pwb_pk = pwb_pk;
	}

	public String getGeb_cgeneralhid() {
		return geb_cgeneralhid;
	}

	public void setGeb_cgeneralhid(String geb_cgeneralhid) {
		this.geb_cgeneralhid = geb_cgeneralhid;
	}

	public String getGeb_backvbatchcode() {
		return geb_backvbatchcode;
	}

	public void setGeb_backvbatchcode(String geb_backvbatchcode) {
		this.geb_backvbatchcode = geb_backvbatchcode;
	}

	public UFDouble getGeb_virtualnum() {
		return geb_virtualnum;
	}

	public void setGeb_virtualnum(UFDouble geb_virtualnum) {
		this.geb_virtualnum = geb_virtualnum;
	}

	public UFDouble getGeb_virtualbnum() {
		return geb_virtualbnum;
	}

	public void setGeb_virtualbnum(UFDouble geb_virtualbnum) {
		this.geb_virtualbnum = geb_virtualbnum;
	}

	public UFBoolean getGeb_isclose() {
		return geb_isclose;
	}

	public void setGeb_isclose(UFBoolean geb_isclose) {
		this.geb_isclose = geb_isclose;
	}



	public String getVfirstbillcode() {
		return vfirstbillcode;
	}

	public void setVfirstbillcode(String vfirstbillcode) {
		this.vfirstbillcode = vfirstbillcode;
	}

	public String getCfirsttype() {
		return cfirsttype;
	}

	public void setCfirsttype(String cfirsttype) {
		this.cfirsttype = cfirsttype;
	}

	public String getCfirstbillhid() {
		return cfirstbillhid;
	}

	public void setCfirstbillhid(String cfirstbillhid) {
		this.cfirstbillhid = cfirstbillhid;
	}

	public String getCfirstbillbid() {
		return cfirstbillbid;
	}

	public void setCfirstbillbid(String cfirstbillbid) {
		this.cfirstbillbid = cfirstbillbid;
	}

	public String getVsourcebillcode() {
		return vsourcebillcode;
	}

	public void setVsourcebillcode(String vsourcebillcode) {
		this.vsourcebillcode = vsourcebillcode;
	}

	public String getCsourcetype() {
		return csourcetype;
	}

	public void setCsourcetype(String csourcetype) {
		this.csourcetype = csourcetype;
	}

	public String getCsourcebillhid() {
		return csourcebillhid;
	}

	public void setCsourcebillhid(String csourcebillhid) {
		this.csourcebillhid = csourcebillhid;
	}

	public String getCsourcebillbid() {
		return csourcebillbid;
	}

	public void setCsourcebillbid(String csourcebillbid) {
		this.csourcebillbid = csourcebillbid;
	}

	public void validateOnZdrk() throws ValidationException{
		if(PuPubVO.getString_TrimZeroLenAsNull(getGeb_cinvbasid())==null){
			throw new ValidationException("存货不能为空");
		}
		if(PuPubVO.getUFDouble_NullAsZero(getGeb_snum()).equals(new UFDouble(0.0))){
			throw new ValidationException("应收数量不能为空或为0");
		}
		if(PuPubVO.getUFDouble_NullAsZero(getGeb_bsnum()).equals(new UFDouble(0.0))){
			throw new ValidationException("应收辅数量不能为空或为0");
		}
		if(PuPubVO.getString_TrimZeroLenAsNull(getGeb_vbatchcode())==null){
			throw new ValidationException("批次号不能为空");
		}
//		// 验证批次号是否正确
//		if (getGeb_vbatchcode().trim().length() < 8) {
//			throw new ValidationException("批次号不能小于8位!");
//
//		}
//
//		Pattern p = Pattern
//		.compile(
//				"^((((1[6-9]|[2-9]\\d)\\d{2})(0?[13578]|1[02])(0?[1-9]|[12]\\d|3[01]))|"
//				+ "(((1[6-9]|[2-9]\\d)\\d{2})(0?[13456789]|1[012])(0?[1-9]|[12]\\d|30))|"
//				+ "(((1[6-9]|[2-9]\\d)\\d{2})0?2(0?[1-9]|1\\d|2[0-8]))|"
//				+ "(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))0?229))$",
//				Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
//		Matcher m = p.matcher(getGeb_vbatchcode().trim().substring(0, 8));
//		if (!m.find()) {
//			throw new ValidationException(
//			"批次号输入的不正确,请您输入正确的日期!如：20100101XXXXXX");
//
//		}
	}

	public void validateOnSave() throws ValidationException{
		validateOnZdrk();
		validateBodySave();
		if(PuPubVO.getUFDouble_NullAsZero(getGeb_anum()).equals(WdsWlPubTool.DOUBLE_ZERO))
			throw new ValidationException("实收数量不能为空");
	}
	
	public void validateBodySave()  throws ValidationException{
		List<TbGeneralBBVO> list = getTrayInfor();
		if(list == null || list.size() == 0)
			throw new ValidationException("托盘信息为空！请指定！");
//		UFDouble v = new UFDouble(0);//实入数量
		UFDouble v1 = new UFDouble(0);//实入辅数量
		for(TbGeneralBBVO l :list ){
//			UFDouble b = l.getGebb_num();//实入数量
//			if(b==null || b.doubleValue() == 0)
//				throw new ValidationException("托盘指定实入数量为0或者为空!");
			UFDouble b1 = l.getNinassistnum();//实入辅数量
			if(b1==null || b1.doubleValue() == 0)
				throw new ValidationException("托盘指定实入辅数量为0或者为空!");
//			v = v.add(b);
			v1 = v1.add(b1);
		}
		if(v1.sub(getGeb_bsnum()).doubleValue() > 0){
			throw new ValidationException("托盘指定实入数量大于应收数量!");
		}
	}
	
	
	public String getGylbillcode() {
		return gylbillcode;
	}
	public void setGylbillcode(String gylbillcode) {
		this.gylbillcode = gylbillcode;
	}
	public String getGylbilltype() {
		return gylbilltype;
	}
	public void setGylbilltype(String gylbilltype) {
		this.gylbilltype = gylbilltype;
	}
	public String getGylbillhid() {
		return gylbillhid;
	}
	public void setGylbillhid(String gylbillhid) {
		this.gylbillhid = gylbillhid;
	}
	public String getGylbillbid() {
		return gylbillbid;
	}
	public void setGylbillbid(String gylbillbid) {
		this.gylbillbid = gylbillbid;
	}



}
