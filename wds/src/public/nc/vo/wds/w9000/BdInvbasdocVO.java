/***************************************************************\
 *     The skeleton of this class is generated by an automatic *
 * code generator for NC product. It is based on Velocity.     *
\***************************************************************/
package nc.vo.wds.w9000;

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
 * 创建日期:2010-8-6
 * 
 * @author ${vmObject.author}
 * @version Your Project 1.0
 */
public class BdInvbasdocVO extends SuperVO {

    public String def5;
    public String pk_corp;
    public String pk_measdoc5;
    public String pk_taxitems;
    public String width;
    public String invbarcode;
    public String invcode;
    public UFBoolean discountflag;
    public String pk_invcl;
    public Integer dr;
    public UFBoolean assistunit;
    public String invpinpai;
    public String graphid;
    public String def7;
    public UFBoolean isstorebyconvert;
    public String def14;
    public String pk_assetcategory;
    public UFDouble shipunitnum;
    public UFDateTime ts;
    public String def6;
    public String forinvname;
    public String sealflag;
    public String invtype;
    public String def15;
    public String def18;
    public UFBoolean setpartsflag;
    public String def1;
    public String def19;
    public String def20;
    public String free4;
    public UFDouble unitweight;
    public String invspec;
    public String free3;
    public String free2;
    public String def17;
    public String def11;
    public String pk_invbasdoc;
    public String pk_measdoc3;
    public UFDouble weitunitnum;
    public String height;
    public String pk_measdoc2;
    public String def16;
    public String invmnecode;
    public String invshortname;
    public String free5;
    public String def10;
    public String pk_measdoc;
    public UFDouble storeunitnum;
    public String memo;
    public String def13;
    public String def8;
    public String length;
    public String invname;
    public String pk_measdoc1;
    public String pk_prodline;
    public UFBoolean laborflag;
    public String def2;
    public String def12;
    public String free1;
    public UFBoolean autobalancemeas;
    public String def4;
    public UFBoolean ismngstockbygrswt;
    public String def3;
    public String def9;
    public UFDouble unitvolume;

    public static final String DEF5 = "def5";
    public static final String PK_CORP = "pk_corp";
    public static final String PK_MEASDOC5 = "pk_measdoc5";
    public static final String PK_TAXITEMS = "pk_taxitems";
    public static final String WIDTH = "width";
    public static final String INVBARCODE = "invbarcode";
    public static final String INVCODE = "invcode";
    public static final String DISCOUNTFLAG = "discountflag";
    public static final String PK_INVCL = "pk_invcl";
    public static final String DR = "dr";
    public static final String ASSISTUNIT = "assistunit";
    public static final String INVPINPAI = "invpinpai";
    public static final String GRAPHID = "graphid";
    public static final String DEF7 = "def7";
    public static final String ISSTOREBYCONVERT = "isstorebyconvert";
    public static final String DEF14 = "def14";
    public static final String PK_ASSETCATEGORY = "pk_assetcategory";
    public static final String SHIPUNITNUM = "shipunitnum";
    public static final String TS = "ts";
    public static final String DEF6 = "def6";
    public static final String FORINVNAME = "forinvname";
    public static final String SEALFLAG = "sealflag";
    public static final String INVTYPE = "invtype";
    public static final String DEF15 = "def15";
    public static final String DEF18 = "def18";
    public static final String SETPARTSFLAG = "setpartsflag";
    public static final String DEF1 = "def1";
    public static final String DEF19 = "def19";
    public static final String DEF20 = "def20";
    public static final String FREE4 = "free4";
    public static final String UNITWEIGHT = "unitweight";
    public static final String INVSPEC = "invspec";
    public static final String FREE3 = "free3";
    public static final String FREE2 = "free2";
    public static final String DEF17 = "def17";
    public static final String DEF11 = "def11";
    public static final String PK_INVBASDOC = "pk_invbasdoc";
    public static final String PK_MEASDOC3 = "pk_measdoc3";
    public static final String WEITUNITNUM = "weitunitnum";
    public static final String HEIGHT = "height";
    public static final String PK_MEASDOC2 = "pk_measdoc2";
    public static final String DEF16 = "def16";
    public static final String INVMNECODE = "invmnecode";
    public static final String INVSHORTNAME = "invshortname";
    public static final String FREE5 = "free5";
    public static final String DEF10 = "def10";
    public static final String PK_MEASDOC = "pk_measdoc";
    public static final String STOREUNITNUM = "storeunitnum";
    public static final String MEMO = "memo";
    public static final String DEF13 = "def13";
    public static final String DEF8 = "def8";
    public static final String LENGTH = "length";
    public static final String INVNAME = "invname";
    public static final String PK_MEASDOC1 = "pk_measdoc1";
    public static final String PK_PRODLINE = "pk_prodline";
    public static final String LABORFLAG = "laborflag";
    public static final String DEF2 = "def2";
    public static final String DEF12 = "def12";
    public static final String FREE1 = "free1";
    public static final String AUTOBALANCEMEAS = "autobalancemeas";
    public static final String DEF4 = "def4";
    public static final String ISMNGSTOCKBYGRSWT = "ismngstockbygrswt";
    public static final String DEF3 = "def3";
    public static final String DEF9 = "def9";
    public static final String UNITVOLUME = "unitvolume";

    /**
     * 属性def5的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getDef5() {
	return def5;
    }

    /**
     * 属性def5的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newDef5
     *                String
     */
    public void setDef5(String newDef5) {

	def5 = newDef5;
    }

    /**
     * 属性pk_corp的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getPk_corp() {
	return pk_corp;
    }

    /**
     * 属性pk_corp的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newPk_corp
     *                String
     */
    public void setPk_corp(String newPk_corp) {

	pk_corp = newPk_corp;
    }

    /**
     * 属性pk_measdoc5的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getPk_measdoc5() {
	return pk_measdoc5;
    }

    /**
     * 属性pk_measdoc5的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newPk_measdoc5
     *                String
     */
    public void setPk_measdoc5(String newPk_measdoc5) {

	pk_measdoc5 = newPk_measdoc5;
    }

    /**
     * 属性pk_taxitems的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getPk_taxitems() {
	return pk_taxitems;
    }

    /**
     * 属性pk_taxitems的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newPk_taxitems
     *                String
     */
    public void setPk_taxitems(String newPk_taxitems) {

	pk_taxitems = newPk_taxitems;
    }

    /**
     * 属性width的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getWidth() {
	return width;
    }

    /**
     * 属性width的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newWidth
     *                String
     */
    public void setWidth(String newWidth) {

	width = newWidth;
    }

    /**
     * 属性invbarcode的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getInvbarcode() {
	return invbarcode;
    }

    /**
     * 属性invbarcode的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newInvbarcode
     *                String
     */
    public void setInvbarcode(String newInvbarcode) {

	invbarcode = newInvbarcode;
    }

    /**
     * 属性invcode的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getInvcode() {
	return invcode;
    }

    /**
     * 属性invcode的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newInvcode
     *                String
     */
    public void setInvcode(String newInvcode) {

	invcode = newInvcode;
    }

    /**
     * 属性discountflag的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return UFBoolean
     */
    public UFBoolean getDiscountflag() {
	return discountflag;
    }

    /**
     * 属性discountflag的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newDiscountflag
     *                UFBoolean
     */
    public void setDiscountflag(UFBoolean newDiscountflag) {

	discountflag = newDiscountflag;
    }

    /**
     * 属性pk_invcl的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getPk_invcl() {
	return pk_invcl;
    }

    /**
     * 属性pk_invcl的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newPk_invcl
     *                String
     */
    public void setPk_invcl(String newPk_invcl) {

	pk_invcl = newPk_invcl;
    }

    /**
     * 属性dr的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return Integer
     */
    public Integer getDr() {
	return dr;
    }

    /**
     * 属性dr的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newDr
     *                Integer
     */
    public void setDr(Integer newDr) {

	dr = newDr;
    }

    /**
     * 属性assistunit的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return UFBoolean
     */
    public UFBoolean getAssistunit() {
	return assistunit;
    }

    /**
     * 属性assistunit的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newAssistunit
     *                UFBoolean
     */
    public void setAssistunit(UFBoolean newAssistunit) {

	assistunit = newAssistunit;
    }

    /**
     * 属性invpinpai的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getInvpinpai() {
	return invpinpai;
    }

    /**
     * 属性invpinpai的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newInvpinpai
     *                String
     */
    public void setInvpinpai(String newInvpinpai) {

	invpinpai = newInvpinpai;
    }

    /**
     * 属性graphid的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getGraphid() {
	return graphid;
    }

    /**
     * 属性graphid的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newGraphid
     *                String
     */
    public void setGraphid(String newGraphid) {

	graphid = newGraphid;
    }

    /**
     * 属性def7的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getDef7() {
	return def7;
    }

    /**
     * 属性def7的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newDef7
     *                String
     */
    public void setDef7(String newDef7) {

	def7 = newDef7;
    }

    /**
     * 属性isstorebyconvert的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return UFBoolean
     */
    public UFBoolean getIsstorebyconvert() {
	return isstorebyconvert;
    }

    /**
     * 属性isstorebyconvert的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newIsstorebyconvert
     *                UFBoolean
     */
    public void setIsstorebyconvert(UFBoolean newIsstorebyconvert) {

	isstorebyconvert = newIsstorebyconvert;
    }

    /**
     * 属性def14的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getDef14() {
	return def14;
    }

    /**
     * 属性def14的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newDef14
     *                String
     */
    public void setDef14(String newDef14) {

	def14 = newDef14;
    }

    /**
     * 属性pk_assetcategory的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getPk_assetcategory() {
	return pk_assetcategory;
    }

    /**
     * 属性pk_assetcategory的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newPk_assetcategory
     *                String
     */
    public void setPk_assetcategory(String newPk_assetcategory) {

	pk_assetcategory = newPk_assetcategory;
    }

    /**
     * 属性shipunitnum的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return UFDouble
     */
    public UFDouble getShipunitnum() {
	return shipunitnum;
    }

    /**
     * 属性shipunitnum的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newShipunitnum
     *                UFDouble
     */
    public void setShipunitnum(UFDouble newShipunitnum) {

	shipunitnum = newShipunitnum;
    }

    /**
     * 属性ts的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return UFDateTime
     */
    public UFDateTime getTs() {
	return ts;
    }

    /**
     * 属性ts的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newTs
     *                UFDateTime
     */
    public void setTs(UFDateTime newTs) {

	ts = newTs;
    }

    /**
     * 属性def6的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getDef6() {
	return def6;
    }

    /**
     * 属性def6的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newDef6
     *                String
     */
    public void setDef6(String newDef6) {

	def6 = newDef6;
    }

    /**
     * 属性forinvname的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getForinvname() {
	return forinvname;
    }

    /**
     * 属性forinvname的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newForinvname
     *                String
     */
    public void setForinvname(String newForinvname) {

	forinvname = newForinvname;
    }

    /**
     * 属性sealflag的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getSealflag() {
	return sealflag;
    }

    /**
     * 属性sealflag的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newSealflag
     *                String
     */
    public void setSealflag(String newSealflag) {

	sealflag = newSealflag;
    }

    /**
     * 属性invtype的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getInvtype() {
	return invtype;
    }

    /**
     * 属性invtype的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newInvtype
     *                String
     */
    public void setInvtype(String newInvtype) {

	invtype = newInvtype;
    }

    /**
     * 属性def15的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getDef15() {
	return def15;
    }

    /**
     * 属性def15的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newDef15
     *                String
     */
    public void setDef15(String newDef15) {

	def15 = newDef15;
    }

    /**
     * 属性def18的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getDef18() {
	return def18;
    }

    /**
     * 属性def18的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newDef18
     *                String
     */
    public void setDef18(String newDef18) {

	def18 = newDef18;
    }

    /**
     * 属性setpartsflag的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return UFBoolean
     */
    public UFBoolean getSetpartsflag() {
	return setpartsflag;
    }

    /**
     * 属性setpartsflag的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newSetpartsflag
     *                UFBoolean
     */
    public void setSetpartsflag(UFBoolean newSetpartsflag) {

	setpartsflag = newSetpartsflag;
    }

    /**
     * 属性def1的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getDef1() {
	return def1;
    }

    /**
     * 属性def1的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newDef1
     *                String
     */
    public void setDef1(String newDef1) {

	def1 = newDef1;
    }

    /**
     * 属性def19的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getDef19() {
	return def19;
    }

    /**
     * 属性def19的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newDef19
     *                String
     */
    public void setDef19(String newDef19) {

	def19 = newDef19;
    }

    /**
     * 属性def20的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getDef20() {
	return def20;
    }

    /**
     * 属性def20的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newDef20
     *                String
     */
    public void setDef20(String newDef20) {

	def20 = newDef20;
    }

    /**
     * 属性free4的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getFree4() {
	return free4;
    }

    /**
     * 属性free4的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newFree4
     *                String
     */
    public void setFree4(String newFree4) {

	free4 = newFree4;
    }

    /**
     * 属性unitweight的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return UFDouble
     */
    public UFDouble getUnitweight() {
	return unitweight;
    }

    /**
     * 属性unitweight的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newUnitweight
     *                UFDouble
     */
    public void setUnitweight(UFDouble newUnitweight) {

	unitweight = newUnitweight;
    }

    /**
     * 属性invspec的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getInvspec() {
	return invspec;
    }

    /**
     * 属性invspec的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newInvspec
     *                String
     */
    public void setInvspec(String newInvspec) {

	invspec = newInvspec;
    }

    /**
     * 属性free3的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getFree3() {
	return free3;
    }

    /**
     * 属性free3的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newFree3
     *                String
     */
    public void setFree3(String newFree3) {

	free3 = newFree3;
    }

    /**
     * 属性free2的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getFree2() {
	return free2;
    }

    /**
     * 属性free2的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newFree2
     *                String
     */
    public void setFree2(String newFree2) {

	free2 = newFree2;
    }

    /**
     * 属性def17的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getDef17() {
	return def17;
    }

    /**
     * 属性def17的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newDef17
     *                String
     */
    public void setDef17(String newDef17) {

	def17 = newDef17;
    }

    /**
     * 属性def11的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getDef11() {
	return def11;
    }

    /**
     * 属性def11的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newDef11
     *                String
     */
    public void setDef11(String newDef11) {

	def11 = newDef11;
    }

    /**
     * 属性pk_invbasdoc的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getPk_invbasdoc() {
	return pk_invbasdoc;
    }

    /**
     * 属性pk_invbasdoc的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newPk_invbasdoc
     *                String
     */
    public void setPk_invbasdoc(String newPk_invbasdoc) {

	pk_invbasdoc = newPk_invbasdoc;
    }

    /**
     * 属性pk_measdoc3的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getPk_measdoc3() {
	return pk_measdoc3;
    }

    /**
     * 属性pk_measdoc3的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newPk_measdoc3
     *                String
     */
    public void setPk_measdoc3(String newPk_measdoc3) {

	pk_measdoc3 = newPk_measdoc3;
    }

    /**
     * 属性weitunitnum的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return UFDouble
     */
    public UFDouble getWeitunitnum() {
	return weitunitnum;
    }

    /**
     * 属性weitunitnum的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newWeitunitnum
     *                UFDouble
     */
    public void setWeitunitnum(UFDouble newWeitunitnum) {

	weitunitnum = newWeitunitnum;
    }

    /**
     * 属性height的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getHeight() {
	return height;
    }

    /**
     * 属性height的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newHeight
     *                String
     */
    public void setHeight(String newHeight) {

	height = newHeight;
    }

    /**
     * 属性pk_measdoc2的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getPk_measdoc2() {
	return pk_measdoc2;
    }

    /**
     * 属性pk_measdoc2的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newPk_measdoc2
     *                String
     */
    public void setPk_measdoc2(String newPk_measdoc2) {

	pk_measdoc2 = newPk_measdoc2;
    }

    /**
     * 属性def16的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getDef16() {
	return def16;
    }

    /**
     * 属性def16的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newDef16
     *                String
     */
    public void setDef16(String newDef16) {

	def16 = newDef16;
    }

    /**
     * 属性invmnecode的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getInvmnecode() {
	return invmnecode;
    }

    /**
     * 属性invmnecode的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newInvmnecode
     *                String
     */
    public void setInvmnecode(String newInvmnecode) {

	invmnecode = newInvmnecode;
    }

    /**
     * 属性invshortname的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getInvshortname() {
	return invshortname;
    }

    /**
     * 属性invshortname的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newInvshortname
     *                String
     */
    public void setInvshortname(String newInvshortname) {

	invshortname = newInvshortname;
    }

    /**
     * 属性free5的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getFree5() {
	return free5;
    }

    /**
     * 属性free5的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newFree5
     *                String
     */
    public void setFree5(String newFree5) {

	free5 = newFree5;
    }

    /**
     * 属性def10的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getDef10() {
	return def10;
    }

    /**
     * 属性def10的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newDef10
     *                String
     */
    public void setDef10(String newDef10) {

	def10 = newDef10;
    }

    /**
     * 属性pk_measdoc的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getPk_measdoc() {
	return pk_measdoc;
    }

    /**
     * 属性pk_measdoc的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newPk_measdoc
     *                String
     */
    public void setPk_measdoc(String newPk_measdoc) {

	pk_measdoc = newPk_measdoc;
    }

    /**
     * 属性storeunitnum的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return UFDouble
     */
    public UFDouble getStoreunitnum() {
	return storeunitnum;
    }

    /**
     * 属性storeunitnum的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newStoreunitnum
     *                UFDouble
     */
    public void setStoreunitnum(UFDouble newStoreunitnum) {

	storeunitnum = newStoreunitnum;
    }

    /**
     * 属性memo的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getMemo() {
	return memo;
    }

    /**
     * 属性memo的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newMemo
     *                String
     */
    public void setMemo(String newMemo) {

	memo = newMemo;
    }

    /**
     * 属性def13的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getDef13() {
	return def13;
    }

    /**
     * 属性def13的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newDef13
     *                String
     */
    public void setDef13(String newDef13) {

	def13 = newDef13;
    }

    /**
     * 属性def8的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getDef8() {
	return def8;
    }

    /**
     * 属性def8的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newDef8
     *                String
     */
    public void setDef8(String newDef8) {

	def8 = newDef8;
    }

    /**
     * 属性length的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getLength() {
	return length;
    }

    /**
     * 属性length的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newLength
     *                String
     */
    public void setLength(String newLength) {

	length = newLength;
    }

    /**
     * 属性invname的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getInvname() {
	return invname;
    }

    /**
     * 属性invname的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newInvname
     *                String
     */
    public void setInvname(String newInvname) {

	invname = newInvname;
    }

    /**
     * 属性pk_measdoc1的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getPk_measdoc1() {
	return pk_measdoc1;
    }

    /**
     * 属性pk_measdoc1的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newPk_measdoc1
     *                String
     */
    public void setPk_measdoc1(String newPk_measdoc1) {

	pk_measdoc1 = newPk_measdoc1;
    }

    /**
     * 属性pk_prodline的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getPk_prodline() {
	return pk_prodline;
    }

    /**
     * 属性pk_prodline的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newPk_prodline
     *                String
     */
    public void setPk_prodline(String newPk_prodline) {

	pk_prodline = newPk_prodline;
    }

    /**
     * 属性laborflag的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return UFBoolean
     */
    public UFBoolean getLaborflag() {
	return laborflag;
    }

    /**
     * 属性laborflag的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newLaborflag
     *                UFBoolean
     */
    public void setLaborflag(UFBoolean newLaborflag) {

	laborflag = newLaborflag;
    }

    /**
     * 属性def2的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getDef2() {
	return def2;
    }

    /**
     * 属性def2的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newDef2
     *                String
     */
    public void setDef2(String newDef2) {

	def2 = newDef2;
    }

    /**
     * 属性def12的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getDef12() {
	return def12;
    }

    /**
     * 属性def12的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newDef12
     *                String
     */
    public void setDef12(String newDef12) {

	def12 = newDef12;
    }

    /**
     * 属性free1的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getFree1() {
	return free1;
    }

    /**
     * 属性free1的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newFree1
     *                String
     */
    public void setFree1(String newFree1) {

	free1 = newFree1;
    }

    /**
     * 属性autobalancemeas的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return UFBoolean
     */
    public UFBoolean getAutobalancemeas() {
	return autobalancemeas;
    }

    /**
     * 属性autobalancemeas的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newAutobalancemeas
     *                UFBoolean
     */
    public void setAutobalancemeas(UFBoolean newAutobalancemeas) {

	autobalancemeas = newAutobalancemeas;
    }

    /**
     * 属性def4的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getDef4() {
	return def4;
    }

    /**
     * 属性def4的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newDef4
     *                String
     */
    public void setDef4(String newDef4) {

	def4 = newDef4;
    }

    /**
     * 属性ismngstockbygrswt的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return UFBoolean
     */
    public UFBoolean getIsmngstockbygrswt() {
	return ismngstockbygrswt;
    }

    /**
     * 属性ismngstockbygrswt的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newIsmngstockbygrswt
     *                UFBoolean
     */
    public void setIsmngstockbygrswt(UFBoolean newIsmngstockbygrswt) {

	ismngstockbygrswt = newIsmngstockbygrswt;
    }

    /**
     * 属性def3的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getDef3() {
	return def3;
    }

    /**
     * 属性def3的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newDef3
     *                String
     */
    public void setDef3(String newDef3) {

	def3 = newDef3;
    }

    /**
     * 属性def9的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getDef9() {
	return def9;
    }

    /**
     * 属性def9的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newDef9
     *                String
     */
    public void setDef9(String newDef9) {

	def9 = newDef9;
    }

    /**
     * 属性unitvolume的Getter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @return UFDouble
     */
    public UFDouble getUnitvolume() {
	return unitvolume;
    }

    /**
     * 属性unitvolume的Setter方法.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newUnitvolume
     *                UFDouble
     */
    public void setUnitvolume(UFDouble newUnitvolume) {

	unitvolume = newUnitvolume;
    }

    /**
     * 验证对象各属性之间的数据逻辑正确性.
     * 
     * 创建日期:2010-8-6
     * 
     * @exception nc.vo.pub.ValidationException
     *                    如果验证失败,抛出 ValidationException,对错误进行解释.
     */
    public void validate() throws ValidationException {

	ArrayList errFields = new ArrayList(); // errFields record those null

	// fields that cannot be null.
	// 检查是否为不允许空的字段赋了空值,你可能需要修改下面的提示信息:

	if (pk_corp == null) {
	    errFields.add(new String("pk_corp"));
	}
	if (invcode == null) {
	    errFields.add(new String("invcode"));
	}
	if (pk_invcl == null) {
	    errFields.add(new String("pk_invcl"));
	}
	if (assistunit == null) {
	    errFields.add(new String("assistunit"));
	}
	if (pk_invbasdoc == null) {
	    errFields.add(new String("pk_invbasdoc"));
	}
	if (pk_measdoc == null) {
	    errFields.add(new String("pk_measdoc"));
	}
	if (invname == null) {
	    errFields.add(new String("invname"));
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
     * 创建日期:2010-8-6
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
     * 创建日期:2010-8-6
     * 
     * @return java.lang.String
     */
    public java.lang.String getPKFieldName() {
	return "pk_invbasdoc";
    }

    /**
     * <p>
     * 返回表名称.
     * <p>
     * 创建日期:2010-8-6
     * 
     * @return java.lang.String
     */
    public java.lang.String getTableName() {

	return "bd_invbasdoc";
    }

    /**
     * 按照默认方式创建构造子.
     * 
     * 创建日期:2010-8-6
     */
    public BdInvbasdocVO() {

	super();
    }

    /**
     * 使用主键进行初始化的构造子.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newPk_invbasdoc
     *                主键值
     */
    public BdInvbasdocVO(String newPk_invbasdoc) {

	// 为主键字段赋值:
	pk_invbasdoc = newPk_invbasdoc;

    }

    /**
     * 返回对象标识,用来唯一定位对象.
     * 
     * 创建日期:2010-8-6
     * 
     * @return String
     */
    public String getPrimaryKey() {

	return pk_invbasdoc;

    }

    /**
     * 设置对象标识,用来唯一定位对象.
     * 
     * 创建日期:2010-8-6
     * 
     * @param newPk_invbasdoc
     *                String
     */
    public void setPrimaryKey(String newPk_invbasdoc) {

	pk_invbasdoc = newPk_invbasdoc;

    }

    /**
     * 返回数值对象的显示名称.
     * 
     * 创建日期:2010-8-6
     * 
     * @return java.lang.String 返回数值对象的显示名称.
     */
    public String getEntityName() {

	return "bd_invbasdoc";

    }
}
