package nc.vo.wds.transfer;

import java.util.ArrayList;
import java.util.List;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.other.out.TbOutgeneralTVO;
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
 * 转货位
 * @author yf
 * 
 */
public class TransferBVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String cfirstbillhid;//销售订单表头id
	public String vfirstbillcode;//源头单据号
	public String cfirstbillbid;//销售订单表头id
	public String cfirsttype;//--销售订单 
	public String vuserdef9;//存货状态 
	public String castunitid;//辅单位
	public String csourcebillhid;
	public String vuserdef12;
	public UFBoolean flargess;
	public String csourcetype;
	public String vuserdef3;
	public String cinventoryid;//存货管理ID
	public String vuserdef11;
	public UFDouble nmny;
	public String cinvbasid;//存货基本ID
	public String vuserdef15;
	public UFDouble noutnum;//实发数量
	public UFDouble noutassistnum;//实发辅数量
	public UFDouble nacceptnum;//已入库数量
	public UFDouble nassacceptnum;//已入库辅数量
	public String vsourcebillcode;
	public String vuserdef14;
	public String pk_defdoc9;
	public String vuserdef1;
	public String general_b_pk;
	public String comp;
	public String pk_defdoc10;
	public String pk_defdoc6;
	public String csourcebillbid;
	public String pk_defdoc1;//表体调入货位
	public UFDouble hsl;
	public String pk_defdoc4;

	public String pk_defdoc5;
	public String vuserdef2;
	public String vuserdef5;
	public String vbatchcode;//批次
	public UFDate dfirstbilldate;
	public String pk_defdoc3;
	public String vuserdef8;//货架id
	
	public String general_pk;
	public String vuserdef4;
	public UFDouble nprice;
	public String pk_defdoc8;
	public String vuserdef10;
	public UFDouble nshouldoutnum;//应发数量
	public String pk_defdoc2;//表体调出货位-默认登录人绑定货位
	public String pk_defdoc7;
	public String vuserdef7;//生产日期
	public UFDouble nshouldoutassistnum;//应发辅数量
	public String vuserdef6;
	public String vuserdef13;
	public UFTime ts;
	public Integer dr;
	public String crowno;  //行号
	public String unitid;	//单位
	public UFBoolean isoper; //是否进行操作
	public String lvbatchcode; //源批次
	public String cspaceid; //货位ID
	public UFDate dbizdate;//业务日期
	public UFDouble ntagnum;//贴签数量
	public UFBoolean fistag;//是否贴签
	public UFBoolean isxnap;//是否虚拟安排 liuys add
	
//	public List<TbOutgeneralTVO> trayInfor = null;
//	
//	public List<TbOutgeneralTVO> getTrayInfor(){
//		return trayInfor;
//	}
//	public void setTrayInfor(List<TbOutgeneralTVO> ltrayInfor){
//		this.trayInfor = ltrayInfor;
//	}
	
//	public void addTray(TbOutgeneralTVO tray){
//		if(tray == null)
//			return;
//		if(trayInfor == null){
//			trayInfor = new ArrayList<TbOutgeneralTVO>();
//		}
//		trayInfor.add(tray);
//	
	
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

	/**
	 * 属性castunitid的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getCastunitid() {
		return castunitid;
	}

	/**
	 * 属性castunitid的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newCastunitid
	 *            String
	 */
	public void setCastunitid(String newCastunitid) {

		castunitid = newCastunitid;
	}

	/**
	 * 属性csourcebillhid的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getCsourcebillhid() {
		return csourcebillhid;
	}

	/**
	 * 属性csourcebillhid的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newCsourcebillhid
	 *            String
	 */
	public void setCsourcebillhid(String newCsourcebillhid) {

		csourcebillhid = newCsourcebillhid;
	}

	/**
	 * 属性cfirstbillhid的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getCfirstbillhid() {
		return cfirstbillhid;
	}

	/**
	 * 属性cfirstbillhid的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newCfirstbillhid
	 *            String
	 */
	public void setCfirstbillhid(String newCfirstbillhid) {

		cfirstbillhid = newCfirstbillhid;
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
	 * 属性flargess的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return UFBoolean
	 */
	public UFBoolean getFlargess() {
		return flargess;
	}

	/**
	 * 属性flargess的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newFlargess
	 *            UFBoolean
	 */
	public void setFlargess(UFBoolean newFlargess) {

		flargess = newFlargess;
	}

	/**
	 * 属性csourcetype的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getCsourcetype() {
		return csourcetype;
	}

	/**
	 * 属性csourcetype的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newCsourcetype
	 *            String
	 */
	public void setCsourcetype(String newCsourcetype) {

		csourcetype = newCsourcetype;
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
	 * 属性cinventoryid的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getCinventoryid() {
		return cinventoryid;
	}

	/**
	 * 属性cinventoryid的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newCinventoryid
	 *            String
	 */
	public void setCinventoryid(String newCinventoryid) {

		cinventoryid = newCinventoryid;
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
	 * 属性nmny的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return UFDouble
	 */
	public UFDouble getNmny() {
		return nmny;
	}

	/**
	 * 属性nmny的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newNmny
	 *            UFDouble
	 */
	public void setNmny(UFDouble newNmny) {

		nmny = newNmny;
	}

	/**
	 * 属性cinvbasid的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getCinvbasid() {
		return cinvbasid;
	}

	/**
	 * 属性cinvbasid的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newCinvbasid
	 *            String
	 */
	public void setCinvbasid(String newCinvbasid) {

		cinvbasid = newCinvbasid;
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
	 * 属性cfirsttyp的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getCfirsttype() {
		return cfirsttype;
	}

	/**
	 * 属性cfirsttyp的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newCfirsttyp
	 *            String
	 */
	public void setCfirsttype(String newCfirsttype) {

		cfirsttype = newCfirsttype;
	}

	/**
	 * 属性noutnum的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return UFDouble
	 */
	public UFDouble getNoutnum() {
		return noutnum;
	}

	/**
	 * 属性noutnum的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newNoutnum
	 *            UFDouble
	 */
	public void setNoutnum(UFDouble newNoutnum) {

		noutnum = newNoutnum;
	}

	/**
	 * 属性vsourcebillcode的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getVsourcebillcode() {
		return vsourcebillcode;
	}

	/**
	 * 属性vsourcebillcode的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newVsourcebillcode
	 *            String
	 */
	public void setVsourcebillcode(String newVsourcebillcode) {

		vsourcebillcode = newVsourcebillcode;
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
	 * 属性general_b_pk的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getGeneral_b_pk() {
		return general_b_pk;
	}

	/**
	 * 属性general_b_pk的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newGeneral_b_pk
	 *            String
	 */
	public void setGeneral_b_pk(String newGeneral_b_pk) {

		general_b_pk = newGeneral_b_pk;
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
	 * 属性csourcebillbid的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getCsourcebillbid() {
		return csourcebillbid;
	}

	/**
	 * 属性csourcebillbid的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newCsourcebillbid
	 *            String
	 */
	public void setCsourcebillbid(String newCsourcebillbid) {

		csourcebillbid = newCsourcebillbid;
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
	 * 属性hsl的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return UFDouble
	 */
	public UFDouble getHsl() {
		return hsl;
	}

	/**
	 * 属性hsl的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newHsl
	 *            UFDouble
	 */
	public void setHsl(UFDouble newHsl) {

		hsl = newHsl;
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
	 * 属性noutassistnum的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return UFDouble
	 */
	public UFDouble getNoutassistnum() {
		return noutassistnum;
	}

	/**
	 * 属性noutassistnum的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newNoutassistnum
	 *            UFDouble
	 */
	public void setNoutassistnum(UFDouble newNoutassistnum) {

		noutassistnum = newNoutassistnum;
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
	 * 属性vbatchcode的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getVbatchcode() {
		return vbatchcode;
	}

	/**
	 * 属性vbatchcode的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newVbatchcode
	 *            String
	 */
	public void setVbatchcode(String newVbatchcode) {

		vbatchcode = newVbatchcode;
	}

	/**
	 * 属性dfirstbilldate的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return UFDate
	 */
	public UFDate getDfirstbilldate() {
		return dfirstbilldate;
	}

	/**
	 * 属性dfirstbilldate的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newDfirstbilldate
	 *            UFDate
	 */
	public void setDfirstbilldate(UFDate newDfirstbilldate) {

		dfirstbilldate = newDfirstbilldate;
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
	 * 属性nprice的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return UFDouble
	 */
	public UFDouble getNprice() {
		return nprice;
	}

	/**
	 * 属性nprice的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newNprice
	 *            UFDouble
	 */
	public void setNprice(UFDouble newNprice) {

		nprice = newNprice;
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
	 * 属性nshouldoutnum的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return UFDouble
	 */
	public UFDouble getNshouldoutnum() {
		return nshouldoutnum;
	}

	/**
	 * 属性nshouldoutnum的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newNshouldoutnum
	 *            UFDouble
	 */
	public void setNshouldoutnum(UFDouble newNshouldoutnum) {

		nshouldoutnum = newNshouldoutnum;
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
	 * 属性cfirstbillbid的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getCfirstbillbid() {
		return cfirstbillbid;
	}

	/**
	 * 属性cfirstbillbid的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newCfirstbillbid
	 *            String
	 */
	public void setCfirstbillbid(String newCfirstbillbid) {

		cfirstbillbid = newCfirstbillbid;
	}

	/**
	 * 属性nshouldoutassistnum的Getter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return UFDouble
	 */
	public UFDouble getNshouldoutassistnum() {
		return nshouldoutassistnum;
	}

	/**
	 * 属性nshouldoutassistnum的Setter方法.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newNshouldoutassistnum
	 *            UFDouble
	 */
	public void setNshouldoutassistnum(UFDouble newNshouldoutassistnum) {

		nshouldoutassistnum = newNshouldoutassistnum;
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

		if (general_b_pk == null) {
			errFields.add(new String("general_b_pk"));
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

		return "general_pk";

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
		return "general_b_pk";
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

		return "wds_transfer_b";
	}

	/**
	 * 按照默认方式创建构造子.
	 * 
	 * 创建日期:2010-7-20
	 */
	public TransferBVO() {

		super();
	}

	/**
	 * 使用主键进行初始化的构造子.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newGeneral_b_pk
	 *            主键值
	 */
	public TransferBVO(String newGeneral_b_pk) {

		// 为主键字段赋值:
		general_b_pk = newGeneral_b_pk;

	}

	/**
	 * 返回对象标识,用来唯一定位对象.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @return String
	 */
	public String getPrimaryKey() {

		return general_b_pk;

	}

	/**
	 * 设置对象标识,用来唯一定位对象.
	 * 
	 * 创建日期:2010-7-20
	 * 
	 * @param newGeneral_b_pk
	 *            String
	 */
	public void setPrimaryKey(String newGeneral_b_pk) {

		general_b_pk = newGeneral_b_pk;

	}

	public String getEntityName() {

		return "tb_outgeneral_b";

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

	public String getCrowno() {
		return crowno;
	}

	public void setCrowno(String crowno) {
		this.crowno = crowno;
	}

	public String getUnitid() {
		return unitid;
	}

	public void setUnitid(String unitid) {
		this.unitid = unitid;
	}

	public UFBoolean getIsoper() {
		return isoper;
	}

	public void setIsoper(UFBoolean isoper) {
		this.isoper = isoper;
	}

	public String getLvbatchcode() {
		return lvbatchcode;
	}

	public void setLvbatchcode(String lvbatchcode) {
		this.lvbatchcode = lvbatchcode;
	}

	public String getCspaceid() {
		return cspaceid;
	}

	public void setCspaceid(String cspaceid) {
		this.cspaceid = cspaceid;
	}
	
	public void validationOnSave() throws ValidationException{
		validationOnZdck();
		if(PuPubVO.getUFDouble_NullAsZero(getNoutassistnum()).equals(WdsWlPubTool.DOUBLE_ZERO))
			throw new ValidationException("实出辅数量不能为空，行号为："+getCrowno());
		if(PuPubVO.getUFDouble_NullAsZero(getNoutnum()).equals(WdsWlPubTool.DOUBLE_ZERO)){
			throw new ValidationException("实出数量不能为空，行号为："+getCrowno());
		}
//		if(getTrayInfor() == null || getTrayInfor().size() == 0){
//			throw new ValidationException("托盘信息为空，行号为"+getCrowno());
//		}
	}
	
	public void validationOnZdck() throws ValidationException{
		if(PuPubVO.getString_TrimZeroLenAsNull(getCinventoryid())==null)
			throw new ValidationException("货品不能为空，行号为："+getCrowno());
		if(PuPubVO.getString_TrimZeroLenAsNull(getUnitid())==null)
			throw new ValidationException("计量单位为空，行号为："+getCrowno());
		if(PuPubVO.getString_TrimZeroLenAsNull(getCastunitid())==null)
			throw new ValidationException("辅计量单位为空，行号为："+getCrowno());
		if(PuPubVO.getUFDouble_NullAsZero(getNshouldoutnum()).equals(WdsWlPubTool.DOUBLE_ZERO))
			throw new ValidationException("应发数量不能为空，行号为："+getCrowno());
		if(PuPubVO.getUFDouble_NullAsZero(getNshouldoutassistnum()).equals(WdsWlPubTool.DOUBLE_ZERO))
			throw new ValidationException("应发辅数量不能为空，行号为："+getCrowno());
//		if(PuPubVO.getString_TrimZeroLenAsNull(getVbatchcode())==null)
//			throw new ValidationException("批次号不能为空，行号为："+getCrowno());
	}
	public String getVfirstbillcode() {
		return vfirstbillcode;
	}
	public void setVfirstbillcode(String vfirstbillcode) {
		this.vfirstbillcode = vfirstbillcode;
	}
	public UFDouble getNacceptnum() {
		return nacceptnum;
	}
	public void setNacceptnum(UFDouble nacceptnum) {
		this.nacceptnum = nacceptnum;
	}
	public UFDouble getNassacceptnum() {
		return nassacceptnum;
	}
	public void setNassacceptnum(UFDouble nassacceptnum) {
		this.nassacceptnum = nassacceptnum;
	}
	public UFDate getDbizdate() {
		return dbizdate;
	}
	public void setDbizdate(UFDate dbizdate) {
		this.dbizdate = dbizdate;
	}
//	public String getPk_cargdoc() {
//		return pk_cargdoc;
//	}
//	public void setPk_cargdoc(String pk_cargdoc) {
//		this.pk_cargdoc = pk_cargdoc;
//	}
	public UFDouble getNtagnum() {
		return ntagnum;
	}
	public void setNtagnum(UFDouble ntagnum) {
		this.ntagnum = ntagnum;
	}
	public UFBoolean getFistag() {
		return fistag;
	}
	public void setFistag(UFBoolean fistag) {
		this.fistag = fistag;
	}
	public UFBoolean getIsxnap() {
		return isxnap;
	}
	public void setIsxnap(UFBoolean isxnap) {
		this.isxnap = isxnap;
	}
	
	/***************货位调整单TransferBVO*****************/
	//public String pk_defdoc2;//表体调出货位-默认登录人绑定货位
	private String pk_cargdoc2;//调入货位
	private String vlastbilltype;// 上层单据类型
	private String vlastbillid;// 上层单据ID
	private String vlastbillrowid;// 上层单据RowID
	private String vsourcebilltype;// 来源单据类型
	private String vsourcebillid;// 来源单据ID
	private String vsourcebillrowid;// 来源单据RowID
	public String getPk_cargdoc2() {
		return pk_cargdoc2;
	}
	public void setPk_cargdoc2(String pk_cargdoc2) {
		this.pk_cargdoc2 = pk_cargdoc2;
	}
	public String getVlastbilltype() {
		return vlastbilltype;
	}
	public void setVlastbilltype(String vlastbilltype) {
		this.vlastbilltype = vlastbilltype;
	}
	public String getVlastbillid() {
		return vlastbillid;
	}
	public void setVlastbillid(String vlastbillid) {
		this.vlastbillid = vlastbillid;
	}
	public String getVlastbillrowid() {
		return vlastbillrowid;
	}
	public void setVlastbillrowid(String vlastbillrowid) {
		this.vlastbillrowid = vlastbillrowid;
	}
	public String getVsourcebilltype() {
		return vsourcebilltype;
	}
	public void setVsourcebilltype(String vsourcebilltype) {
		this.vsourcebilltype = vsourcebilltype;
	}
	public String getVsourcebillid() {
		return vsourcebillid;
	}
	public void setVsourcebillid(String vsourcebillid) {
		this.vsourcebillid = vsourcebillid;
	}
	public String getVsourcebillrowid() {
		return vsourcebillrowid;
	}
	public void setVsourcebillrowid(String vsourcebillrowid) {
		this.vsourcebillrowid = vsourcebillrowid;
	}
	/***************货位调整单TransferBVO*****************/
}
