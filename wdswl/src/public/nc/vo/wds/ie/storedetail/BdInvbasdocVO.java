/***************************************************************\
 *     The skeleton of this class is generated by an automatic *
 * code generator for NC product. It is based on Velocity.     *
\***************************************************************/
package nc.vo.wds.ie.storedetail;

import java.util.ArrayList;
import nc.vo.pub.*;
import nc.vo.pub.lang.*;

/**
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b>
 * 
 * <p>
 * �ڴ˴����Ӵ����������Ϣ
 * </p>
 * 
 * ��������:2010-8-6
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
     * ����def5��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getDef5() {
	return def5;
    }

    /**
     * ����def5��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newDef5
     *                String
     */
    public void setDef5(String newDef5) {

	def5 = newDef5;
    }

    /**
     * ����pk_corp��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getPk_corp() {
	return pk_corp;
    }

    /**
     * ����pk_corp��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newPk_corp
     *                String
     */
    public void setPk_corp(String newPk_corp) {

	pk_corp = newPk_corp;
    }

    /**
     * ����pk_measdoc5��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getPk_measdoc5() {
	return pk_measdoc5;
    }

    /**
     * ����pk_measdoc5��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newPk_measdoc5
     *                String
     */
    public void setPk_measdoc5(String newPk_measdoc5) {

	pk_measdoc5 = newPk_measdoc5;
    }

    /**
     * ����pk_taxitems��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getPk_taxitems() {
	return pk_taxitems;
    }

    /**
     * ����pk_taxitems��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newPk_taxitems
     *                String
     */
    public void setPk_taxitems(String newPk_taxitems) {

	pk_taxitems = newPk_taxitems;
    }

    /**
     * ����width��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getWidth() {
	return width;
    }

    /**
     * ����width��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newWidth
     *                String
     */
    public void setWidth(String newWidth) {

	width = newWidth;
    }

    /**
     * ����invbarcode��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getInvbarcode() {
	return invbarcode;
    }

    /**
     * ����invbarcode��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newInvbarcode
     *                String
     */
    public void setInvbarcode(String newInvbarcode) {

	invbarcode = newInvbarcode;
    }

    /**
     * ����invcode��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getInvcode() {
	return invcode;
    }

    /**
     * ����invcode��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newInvcode
     *                String
     */
    public void setInvcode(String newInvcode) {

	invcode = newInvcode;
    }

    /**
     * ����discountflag��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return UFBoolean
     */
    public UFBoolean getDiscountflag() {
	return discountflag;
    }

    /**
     * ����discountflag��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newDiscountflag
     *                UFBoolean
     */
    public void setDiscountflag(UFBoolean newDiscountflag) {

	discountflag = newDiscountflag;
    }

    /**
     * ����pk_invcl��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getPk_invcl() {
	return pk_invcl;
    }

    /**
     * ����pk_invcl��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newPk_invcl
     *                String
     */
    public void setPk_invcl(String newPk_invcl) {

	pk_invcl = newPk_invcl;
    }

    /**
     * ����dr��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return Integer
     */
    public Integer getDr() {
	return dr;
    }

    /**
     * ����dr��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newDr
     *                Integer
     */
    public void setDr(Integer newDr) {

	dr = newDr;
    }

    /**
     * ����assistunit��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return UFBoolean
     */
    public UFBoolean getAssistunit() {
	return assistunit;
    }

    /**
     * ����assistunit��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newAssistunit
     *                UFBoolean
     */
    public void setAssistunit(UFBoolean newAssistunit) {

	assistunit = newAssistunit;
    }

    /**
     * ����invpinpai��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getInvpinpai() {
	return invpinpai;
    }

    /**
     * ����invpinpai��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newInvpinpai
     *                String
     */
    public void setInvpinpai(String newInvpinpai) {

	invpinpai = newInvpinpai;
    }

    /**
     * ����graphid��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getGraphid() {
	return graphid;
    }

    /**
     * ����graphid��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newGraphid
     *                String
     */
    public void setGraphid(String newGraphid) {

	graphid = newGraphid;
    }

    /**
     * ����def7��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getDef7() {
	return def7;
    }

    /**
     * ����def7��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newDef7
     *                String
     */
    public void setDef7(String newDef7) {

	def7 = newDef7;
    }

    /**
     * ����isstorebyconvert��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return UFBoolean
     */
    public UFBoolean getIsstorebyconvert() {
	return isstorebyconvert;
    }

    /**
     * ����isstorebyconvert��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newIsstorebyconvert
     *                UFBoolean
     */
    public void setIsstorebyconvert(UFBoolean newIsstorebyconvert) {

	isstorebyconvert = newIsstorebyconvert;
    }

    /**
     * ����def14��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getDef14() {
	return def14;
    }

    /**
     * ����def14��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newDef14
     *                String
     */
    public void setDef14(String newDef14) {

	def14 = newDef14;
    }

    /**
     * ����pk_assetcategory��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getPk_assetcategory() {
	return pk_assetcategory;
    }

    /**
     * ����pk_assetcategory��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newPk_assetcategory
     *                String
     */
    public void setPk_assetcategory(String newPk_assetcategory) {

	pk_assetcategory = newPk_assetcategory;
    }

    /**
     * ����shipunitnum��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return UFDouble
     */
    public UFDouble getShipunitnum() {
	return shipunitnum;
    }

    /**
     * ����shipunitnum��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newShipunitnum
     *                UFDouble
     */
    public void setShipunitnum(UFDouble newShipunitnum) {

	shipunitnum = newShipunitnum;
    }

    /**
     * ����ts��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return UFDateTime
     */
    public UFDateTime getTs() {
	return ts;
    }

    /**
     * ����ts��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newTs
     *                UFDateTime
     */
    public void setTs(UFDateTime newTs) {

	ts = newTs;
    }

    /**
     * ����def6��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getDef6() {
	return def6;
    }

    /**
     * ����def6��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newDef6
     *                String
     */
    public void setDef6(String newDef6) {

	def6 = newDef6;
    }

    /**
     * ����forinvname��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getForinvname() {
	return forinvname;
    }

    /**
     * ����forinvname��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newForinvname
     *                String
     */
    public void setForinvname(String newForinvname) {

	forinvname = newForinvname;
    }

    /**
     * ����sealflag��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getSealflag() {
	return sealflag;
    }

    /**
     * ����sealflag��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newSealflag
     *                String
     */
    public void setSealflag(String newSealflag) {

	sealflag = newSealflag;
    }

    /**
     * ����invtype��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getInvtype() {
	return invtype;
    }

    /**
     * ����invtype��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newInvtype
     *                String
     */
    public void setInvtype(String newInvtype) {

	invtype = newInvtype;
    }

    /**
     * ����def15��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getDef15() {
	return def15;
    }

    /**
     * ����def15��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newDef15
     *                String
     */
    public void setDef15(String newDef15) {

	def15 = newDef15;
    }

    /**
     * ����def18��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getDef18() {
	return def18;
    }

    /**
     * ����def18��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newDef18
     *                String
     */
    public void setDef18(String newDef18) {

	def18 = newDef18;
    }

    /**
     * ����setpartsflag��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return UFBoolean
     */
    public UFBoolean getSetpartsflag() {
	return setpartsflag;
    }

    /**
     * ����setpartsflag��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newSetpartsflag
     *                UFBoolean
     */
    public void setSetpartsflag(UFBoolean newSetpartsflag) {

	setpartsflag = newSetpartsflag;
    }

    /**
     * ����def1��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getDef1() {
	return def1;
    }

    /**
     * ����def1��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newDef1
     *                String
     */
    public void setDef1(String newDef1) {

	def1 = newDef1;
    }

    /**
     * ����def19��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getDef19() {
	return def19;
    }

    /**
     * ����def19��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newDef19
     *                String
     */
    public void setDef19(String newDef19) {

	def19 = newDef19;
    }

    /**
     * ����def20��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getDef20() {
	return def20;
    }

    /**
     * ����def20��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newDef20
     *                String
     */
    public void setDef20(String newDef20) {

	def20 = newDef20;
    }

    /**
     * ����free4��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getFree4() {
	return free4;
    }

    /**
     * ����free4��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newFree4
     *                String
     */
    public void setFree4(String newFree4) {

	free4 = newFree4;
    }

    /**
     * ����unitweight��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return UFDouble
     */
    public UFDouble getUnitweight() {
	return unitweight;
    }

    /**
     * ����unitweight��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newUnitweight
     *                UFDouble
     */
    public void setUnitweight(UFDouble newUnitweight) {

	unitweight = newUnitweight;
    }

    /**
     * ����invspec��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getInvspec() {
	return invspec;
    }

    /**
     * ����invspec��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newInvspec
     *                String
     */
    public void setInvspec(String newInvspec) {

	invspec = newInvspec;
    }

    /**
     * ����free3��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getFree3() {
	return free3;
    }

    /**
     * ����free3��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newFree3
     *                String
     */
    public void setFree3(String newFree3) {

	free3 = newFree3;
    }

    /**
     * ����free2��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getFree2() {
	return free2;
    }

    /**
     * ����free2��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newFree2
     *                String
     */
    public void setFree2(String newFree2) {

	free2 = newFree2;
    }

    /**
     * ����def17��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getDef17() {
	return def17;
    }

    /**
     * ����def17��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newDef17
     *                String
     */
    public void setDef17(String newDef17) {

	def17 = newDef17;
    }

    /**
     * ����def11��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getDef11() {
	return def11;
    }

    /**
     * ����def11��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newDef11
     *                String
     */
    public void setDef11(String newDef11) {

	def11 = newDef11;
    }

    /**
     * ����pk_invbasdoc��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getPk_invbasdoc() {
	return pk_invbasdoc;
    }

    /**
     * ����pk_invbasdoc��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newPk_invbasdoc
     *                String
     */
    public void setPk_invbasdoc(String newPk_invbasdoc) {

	pk_invbasdoc = newPk_invbasdoc;
    }

    /**
     * ����pk_measdoc3��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getPk_measdoc3() {
	return pk_measdoc3;
    }

    /**
     * ����pk_measdoc3��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newPk_measdoc3
     *                String
     */
    public void setPk_measdoc3(String newPk_measdoc3) {

	pk_measdoc3 = newPk_measdoc3;
    }

    /**
     * ����weitunitnum��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return UFDouble
     */
    public UFDouble getWeitunitnum() {
	return weitunitnum;
    }

    /**
     * ����weitunitnum��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newWeitunitnum
     *                UFDouble
     */
    public void setWeitunitnum(UFDouble newWeitunitnum) {

	weitunitnum = newWeitunitnum;
    }

    /**
     * ����height��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getHeight() {
	return height;
    }

    /**
     * ����height��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newHeight
     *                String
     */
    public void setHeight(String newHeight) {

	height = newHeight;
    }

    /**
     * ����pk_measdoc2��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getPk_measdoc2() {
	return pk_measdoc2;
    }

    /**
     * ����pk_measdoc2��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newPk_measdoc2
     *                String
     */
    public void setPk_measdoc2(String newPk_measdoc2) {

	pk_measdoc2 = newPk_measdoc2;
    }

    /**
     * ����def16��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getDef16() {
	return def16;
    }

    /**
     * ����def16��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newDef16
     *                String
     */
    public void setDef16(String newDef16) {

	def16 = newDef16;
    }

    /**
     * ����invmnecode��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getInvmnecode() {
	return invmnecode;
    }

    /**
     * ����invmnecode��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newInvmnecode
     *                String
     */
    public void setInvmnecode(String newInvmnecode) {

	invmnecode = newInvmnecode;
    }

    /**
     * ����invshortname��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getInvshortname() {
	return invshortname;
    }

    /**
     * ����invshortname��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newInvshortname
     *                String
     */
    public void setInvshortname(String newInvshortname) {

	invshortname = newInvshortname;
    }

    /**
     * ����free5��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getFree5() {
	return free5;
    }

    /**
     * ����free5��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newFree5
     *                String
     */
    public void setFree5(String newFree5) {

	free5 = newFree5;
    }

    /**
     * ����def10��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getDef10() {
	return def10;
    }

    /**
     * ����def10��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newDef10
     *                String
     */
    public void setDef10(String newDef10) {

	def10 = newDef10;
    }

    /**
     * ����pk_measdoc��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getPk_measdoc() {
	return pk_measdoc;
    }

    /**
     * ����pk_measdoc��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newPk_measdoc
     *                String
     */
    public void setPk_measdoc(String newPk_measdoc) {

	pk_measdoc = newPk_measdoc;
    }

    /**
     * ����storeunitnum��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return UFDouble
     */
    public UFDouble getStoreunitnum() {
	return storeunitnum;
    }

    /**
     * ����storeunitnum��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newStoreunitnum
     *                UFDouble
     */
    public void setStoreunitnum(UFDouble newStoreunitnum) {

	storeunitnum = newStoreunitnum;
    }

    /**
     * ����memo��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getMemo() {
	return memo;
    }

    /**
     * ����memo��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newMemo
     *                String
     */
    public void setMemo(String newMemo) {

	memo = newMemo;
    }

    /**
     * ����def13��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getDef13() {
	return def13;
    }

    /**
     * ����def13��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newDef13
     *                String
     */
    public void setDef13(String newDef13) {

	def13 = newDef13;
    }

    /**
     * ����def8��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getDef8() {
	return def8;
    }

    /**
     * ����def8��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newDef8
     *                String
     */
    public void setDef8(String newDef8) {

	def8 = newDef8;
    }

    /**
     * ����length��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getLength() {
	return length;
    }

    /**
     * ����length��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newLength
     *                String
     */
    public void setLength(String newLength) {

	length = newLength;
    }

    /**
     * ����invname��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getInvname() {
	return invname;
    }

    /**
     * ����invname��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newInvname
     *                String
     */
    public void setInvname(String newInvname) {

	invname = newInvname;
    }

    /**
     * ����pk_measdoc1��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getPk_measdoc1() {
	return pk_measdoc1;
    }

    /**
     * ����pk_measdoc1��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newPk_measdoc1
     *                String
     */
    public void setPk_measdoc1(String newPk_measdoc1) {

	pk_measdoc1 = newPk_measdoc1;
    }

    /**
     * ����pk_prodline��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getPk_prodline() {
	return pk_prodline;
    }

    /**
     * ����pk_prodline��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newPk_prodline
     *                String
     */
    public void setPk_prodline(String newPk_prodline) {

	pk_prodline = newPk_prodline;
    }

    /**
     * ����laborflag��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return UFBoolean
     */
    public UFBoolean getLaborflag() {
	return laborflag;
    }

    /**
     * ����laborflag��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newLaborflag
     *                UFBoolean
     */
    public void setLaborflag(UFBoolean newLaborflag) {

	laborflag = newLaborflag;
    }

    /**
     * ����def2��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getDef2() {
	return def2;
    }

    /**
     * ����def2��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newDef2
     *                String
     */
    public void setDef2(String newDef2) {

	def2 = newDef2;
    }

    /**
     * ����def12��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getDef12() {
	return def12;
    }

    /**
     * ����def12��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newDef12
     *                String
     */
    public void setDef12(String newDef12) {

	def12 = newDef12;
    }

    /**
     * ����free1��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getFree1() {
	return free1;
    }

    /**
     * ����free1��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newFree1
     *                String
     */
    public void setFree1(String newFree1) {

	free1 = newFree1;
    }

    /**
     * ����autobalancemeas��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return UFBoolean
     */
    public UFBoolean getAutobalancemeas() {
	return autobalancemeas;
    }

    /**
     * ����autobalancemeas��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newAutobalancemeas
     *                UFBoolean
     */
    public void setAutobalancemeas(UFBoolean newAutobalancemeas) {

	autobalancemeas = newAutobalancemeas;
    }

    /**
     * ����def4��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getDef4() {
	return def4;
    }

    /**
     * ����def4��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newDef4
     *                String
     */
    public void setDef4(String newDef4) {

	def4 = newDef4;
    }

    /**
     * ����ismngstockbygrswt��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return UFBoolean
     */
    public UFBoolean getIsmngstockbygrswt() {
	return ismngstockbygrswt;
    }

    /**
     * ����ismngstockbygrswt��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newIsmngstockbygrswt
     *                UFBoolean
     */
    public void setIsmngstockbygrswt(UFBoolean newIsmngstockbygrswt) {

	ismngstockbygrswt = newIsmngstockbygrswt;
    }

    /**
     * ����def3��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getDef3() {
	return def3;
    }

    /**
     * ����def3��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newDef3
     *                String
     */
    public void setDef3(String newDef3) {

	def3 = newDef3;
    }

    /**
     * ����def9��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getDef9() {
	return def9;
    }

    /**
     * ����def9��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newDef9
     *                String
     */
    public void setDef9(String newDef9) {

	def9 = newDef9;
    }

    /**
     * ����unitvolume��Getter����.
     * 
     * ��������:2010-8-6
     * 
     * @return UFDouble
     */
    public UFDouble getUnitvolume() {
	return unitvolume;
    }

    /**
     * ����unitvolume��Setter����.
     * 
     * ��������:2010-8-6
     * 
     * @param newUnitvolume
     *                UFDouble
     */
    public void setUnitvolume(UFDouble newUnitvolume) {

	unitvolume = newUnitvolume;
    }

    /**
     * ��֤���������֮��������߼���ȷ��.
     * 
     * ��������:2010-8-6
     * 
     * @exception nc.vo.pub.ValidationException
     *                    �����֤ʧ��,�׳� ValidationException,�Դ�����н���.
     */
    public void validate() throws ValidationException {

	ArrayList errFields = new ArrayList(); // errFields record those null

	// fields that cannot be null.
	// ����Ƿ�Ϊ�������յ��ֶθ��˿�ֵ,�������Ҫ�޸��������ʾ��Ϣ:

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
	message.append("�����ֶβ���Ϊ��:");
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
     * ȡ�ø�VO�����ֶ�.
     * <p>
     * ��������:2010-8-6
     * 
     * @return java.lang.String
     */
    public java.lang.String getParentPKFieldName() {

	return null;

    }

    /**
     * <p>
     * ȡ�ñ�����.
     * <p>
     * ��������:2010-8-6
     * 
     * @return java.lang.String
     */
    public java.lang.String getPKFieldName() {
	return "pk_invbasdoc";
    }

    /**
     * <p>
     * ���ر�����.
     * <p>
     * ��������:2010-8-6
     * 
     * @return java.lang.String
     */
    public java.lang.String getTableName() {

	return "bd_invbasdoc";
    }

    /**
     * ����Ĭ�Ϸ�ʽ����������.
     * 
     * ��������:2010-8-6
     */
    public BdInvbasdocVO() {

	super();
    }

    /**
     * ʹ���������г�ʼ���Ĺ�����.
     * 
     * ��������:2010-8-6
     * 
     * @param newPk_invbasdoc
     *                ����ֵ
     */
    public BdInvbasdocVO(String newPk_invbasdoc) {

	// Ϊ�����ֶθ�ֵ:
	pk_invbasdoc = newPk_invbasdoc;

    }

    /**
     * ���ض����ʶ,����Ψһ��λ����.
     * 
     * ��������:2010-8-6
     * 
     * @return String
     */
    public String getPrimaryKey() {

	return pk_invbasdoc;

    }

    /**
     * ���ö����ʶ,����Ψһ��λ����.
     * 
     * ��������:2010-8-6
     * 
     * @param newPk_invbasdoc
     *                String
     */
    public void setPrimaryKey(String newPk_invbasdoc) {

	pk_invbasdoc = newPk_invbasdoc;

    }

    /**
     * ������ֵ�������ʾ����.
     * 
     * ��������:2010-8-6
     * 
     * @return java.lang.String ������ֵ�������ʾ����.
     */
    public String getEntityName() {

	return "bd_invbasdoc";

    }
}