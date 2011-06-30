package nc.vo.wds.ic.allo.in;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

/**
 * 
 * @author zhf  调拨入库打印 vo
 *
 */
public class AlloInBodyPrintVO extends SuperVO {
	
//	子表字段
	public String geb_cgeneralbid;
	public String geb_crowno;
	public String geb_space;//货位

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
//	public UFTime ts;
//	public Integer dr;


	public String  vfirstbillcode;
	public String cfirsttype;
	public String cfirstbillhid;
	public String cfirstbillbid;
	public String vsourcebillcode;
	public String csourcetype;
	public String csourcebillhid;
	public String csourcebillbid;
	
	
//	子表编码名称字段
	private String invcode;
	private String geb_cinvbasename;
//	private String geb_invtype;
	private String geb_invspec;
//	private String trayno;
	private String measdocname;
	private String bmeasdocname;
	
	
	
//	托盘流水明细表   扩展字段
	private String trayno;
	private UFDouble t_gebb_num;
	private UFDouble t_ninassistnum;
	private UFDouble t_traymax;
	private UFDouble t_gebb_nprice;
	private UFDouble t_gebb_nmny;
	private String t_cdt_pk;
	private String t_gebb_customize1;
	private String t_gebb_customize2;
	
	
	
	
	public String getGeb_cgeneralbid() {
		return geb_cgeneralbid;
	}
	public void setGeb_cgeneralbid(String geb_cgeneralbid) {
		this.geb_cgeneralbid = geb_cgeneralbid;
	}
	public String getGeb_crowno() {
		return geb_crowno;
	}
	public void setGeb_crowno(String geb_crowno) {
		this.geb_crowno = geb_crowno;
	}
	public String getGeb_space() {
		return geb_space;
	}
	public void setGeb_space(String geb_space) {
		this.geb_space = geb_space;
	}
	public UFBoolean getGeb_flargess() {
		return geb_flargess;
	}
	public void setGeb_flargess(UFBoolean geb_flargess) {
		this.geb_flargess = geb_flargess;
	}
	public String getGeb_pk() {
		return geb_pk;
	}
	public void setGeb_pk(String geb_pk) {
		this.geb_pk = geb_pk;
	}
	public String getGeb_cinvbasid() {
		return geb_cinvbasid;
	}
	public void setGeb_cinvbasid(String geb_cinvbasid) {
		this.geb_cinvbasid = geb_cinvbasid;
	}
	public String getCdt_pk() {
		return cdt_pk;
	}
	public void setCdt_pk(String cdt_pk) {
		this.cdt_pk = cdt_pk;
	}
	public String getGeh_pk() {
		return geh_pk;
	}
	public void setGeh_pk(String geh_pk) {
		this.geh_pk = geh_pk;
	}
	public String getGeb_cinventoryid() {
		return geb_cinventoryid;
	}
	public void setGeb_cinventoryid(String geb_cinventoryid) {
		this.geb_cinventoryid = geb_cinventoryid;
	}
	public UFDouble getGeb_nmny() {
		return geb_nmny;
	}
	public void setGeb_nmny(UFDouble geb_nmny) {
		this.geb_nmny = geb_nmny;
	}
	public UFDouble getGeb_snum() {
		return geb_snum;
	}
	public void setGeb_snum(UFDouble geb_snum) {
		this.geb_snum = geb_snum;
	}
	public UFDouble getGeb_bsnum() {
		return geb_bsnum;
	}
	public void setGeb_bsnum(UFDouble geb_bsnum) {
		this.geb_bsnum = geb_bsnum;
	}
	public UFDouble getGeb_anum() {
		return geb_anum;
	}
	public void setGeb_anum(UFDouble geb_anum) {
		this.geb_anum = geb_anum;
	}
	public UFDouble getGeb_banum() {
		return geb_banum;
	}
	public void setGeb_banum(UFDouble geb_banum) {
		this.geb_banum = geb_banum;
	}
	public UFDouble getGeb_tnum() {
		return geb_tnum;
	}
	public void setGeb_tnum(UFDouble geb_tnum) {
		this.geb_tnum = geb_tnum;
	}
	public UFDouble getGeb_tbnum() {
		return geb_tbnum;
	}
	public void setGeb_tbnum(UFDouble geb_tbnum) {
		this.geb_tbnum = geb_tbnum;
	}
	public UFDate getGeb_dbizdate() {
		return geb_dbizdate;
	}
	public void setGeb_dbizdate(UFDate geb_dbizdate) {
		this.geb_dbizdate = geb_dbizdate;
	}
	public UFDate getGeb_dvalidate() {
		return geb_dvalidate;
	}
	public void setGeb_dvalidate(UFDate geb_dvalidate) {
		this.geb_dvalidate = geb_dvalidate;
	}
	public UFDate getGeb_proddate() {
		return geb_proddate;
	}
	public void setGeb_proddate(UFDate geb_proddate) {
		this.geb_proddate = geb_proddate;
	}
	public String getCastunitid() {
		return castunitid;
	}
	public void setCastunitid(String castunitid) {
		this.castunitid = castunitid;
	}
	public UFDate getGeb_freightdate() {
		return geb_freightdate;
	}
	public void setGeb_freightdate(UFDate geb_freightdate) {
		this.geb_freightdate = geb_freightdate;
	}
	public String getPk_measdoc() {
		return pk_measdoc;
	}
	public void setPk_measdoc(String pk_measdoc) {
		this.pk_measdoc = pk_measdoc;
	}
	public String getGeb_vbatchcode() {
		return geb_vbatchcode;
	}
	public void setGeb_vbatchcode(String geb_vbatchcode) {
		this.geb_vbatchcode = geb_vbatchcode;
	}
	public String getVnote() {
		return vnote;
	}
	public void setVnote(String vnote) {
		this.vnote = vnote;
	}
	public UFDouble getGeb_hsl() {
		return geb_hsl;
	}
	public void setGeb_hsl(UFDouble geb_hsl) {
		this.geb_hsl = geb_hsl;
	}
	public UFDouble getGeb_nprice() {
		return geb_nprice;
	}
	public void setGeb_nprice(UFDouble geb_nprice) {
		this.geb_nprice = geb_nprice;
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
	public String getPk_customize5() {
		return pk_customize5;
	}
	public void setPk_customize5(String pk_customize5) {
		this.pk_customize5 = pk_customize5;
	}
	public String getGeb_customize5() {
		return geb_customize5;
	}
	public void setGeb_customize5(String geb_customize5) {
		this.geb_customize5 = geb_customize5;
	}
	public String getPk_customize8() {
		return pk_customize8;
	}
	public void setPk_customize8(String pk_customize8) {
		this.pk_customize8 = pk_customize8;
	}
	public String getPk_customize2() {
		return pk_customize2;
	}
	public void setPk_customize2(String pk_customize2) {
		this.pk_customize2 = pk_customize2;
	}
	public String getGeb_customize1() {
		return geb_customize1;
	}
	public void setGeb_customize1(String geb_customize1) {
		this.geb_customize1 = geb_customize1;
	}
	public String getPk_customize3() {
		return pk_customize3;
	}
	public void setPk_customize3(String pk_customize3) {
		this.pk_customize3 = pk_customize3;
	}
	public String getGeb_customize2() {
		return geb_customize2;
	}
	public void setGeb_customize2(String geb_customize2) {
		this.geb_customize2 = geb_customize2;
	}
	public String getPk_customize9() {
		return pk_customize9;
	}
	public void setPk_customize9(String pk_customize9) {
		this.pk_customize9 = pk_customize9;
	}
	public String getPk_customize6() {
		return pk_customize6;
	}
	public void setPk_customize6(String pk_customize6) {
		this.pk_customize6 = pk_customize6;
	}
	public String getGeb_customize9() {
		return geb_customize9;
	}
	public void setGeb_customize9(String geb_customize9) {
		this.geb_customize9 = geb_customize9;
	}
	public String getPk_customize4() {
		return pk_customize4;
	}
	public void setPk_customize4(String pk_customize4) {
		this.pk_customize4 = pk_customize4;
	}
	public String getGeb_customize4() {
		return geb_customize4;
	}
	public void setGeb_customize4(String geb_customize4) {
		this.geb_customize4 = geb_customize4;
	}
	public String getGeb_customize7() {
		return geb_customize7;
	}
	public void setGeb_customize7(String geb_customize7) {
		this.geb_customize7 = geb_customize7;
	}
	public String getPk_customize7() {
		return pk_customize7;
	}
	public void setPk_customize7(String pk_customize7) {
		this.pk_customize7 = pk_customize7;
	}
	public String getGeb_customize8() {
		return geb_customize8;
	}
	public void setGeb_customize8(String geb_customize8) {
		this.geb_customize8 = geb_customize8;
	}
	public String getPk_customize1() {
		return pk_customize1;
	}
	public void setPk_customize1(String pk_customize1) {
		this.pk_customize1 = pk_customize1;
	}
	public String getGeb_customize3() {
		return geb_customize3;
	}
	public void setGeb_customize3(String geb_customize3) {
		this.geb_customize3 = geb_customize3;
	}
	public String getGeb_customize6() {
		return geb_customize6;
	}
	public void setGeb_customize6(String geb_customize6) {
		this.geb_customize6 = geb_customize6;
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
	public String getInvcode() {
		return invcode;
	}
	public void setInvcode(String invcode) {
		this.invcode = invcode;
	}
	public String getGeb_cinvbasename() {
		return geb_cinvbasename;
	}
	public void setGeb_cinvbasename(String geb_cinvenroryname) {
		this.geb_cinvbasename = geb_cinvenroryname;
	}
	public String getGeb_invtype() {
		return geb_invtype;
	}
	public void setGeb_invtype(String geb_invtype) {
		this.geb_invtype = geb_invtype;
	}
	public String getGeb_invspec() {
		return geb_invspec;
	}
	public void setGeb_invspec(String geb_invspec) {
		this.geb_invspec = geb_invspec;
	}
	public String getMeasdocname() {
		return measdocname;
	}
	public void setMeasdocname(String measdocname) {
		this.measdocname = measdocname;
	}
	public String getBmeasdocname() {
		return bmeasdocname;
	}
	public void setBmeasdocname(String bmeasdocname) {
		this.bmeasdocname = bmeasdocname;
	}
	public String getTrayno() {
		return trayno;
	}
	public void setTrayno(String trayno) {
		this.trayno = trayno;
	}
	public UFDouble getT_gebb_num() {
		return t_gebb_num;
	}
	public void setT_gebb_num(UFDouble t_gebb_num) {
		this.t_gebb_num = t_gebb_num;
	}
	public UFDouble getT_ninassistnum() {
		return t_ninassistnum;
	}
	public void setT_ninassistnum(UFDouble t_ninassistnum) {
		this.t_ninassistnum = t_ninassistnum;
	}
	public UFDouble getT_traymax() {
		return t_traymax;
	}
	public void setT_traymax(UFDouble t_traymax) {
		this.t_traymax = t_traymax;
	}
	public UFDouble getT_gebb_nprice() {
		return t_gebb_nprice;
	}
	public void setT_gebb_nprice(UFDouble t_gebb_nprice) {
		this.t_gebb_nprice = t_gebb_nprice;
	}
	public UFDouble getT_gebb_nmny() {
		return t_gebb_nmny;
	}
	public void setT_gebb_nmny(UFDouble t_gebb_nmny) {
		this.t_gebb_nmny = t_gebb_nmny;
	}
	public String getT_cdt_pk() {
		return t_cdt_pk;
	}
	public void setT_cdt_pk(String t_cdt_pk) {
		this.t_cdt_pk = t_cdt_pk;
	}
	public String getT_gebb_customize1() {
		return t_gebb_customize1;
	}
	public void setT_gebb_customize1(String t_gebb_customize1) {
		this.t_gebb_customize1 = t_gebb_customize1;
	}
	public String getT_gebb_customize2() {
		return t_gebb_customize2;
	}
	public void setT_gebb_customize2(String t_gebb_customize2) {
		this.t_gebb_customize2 = t_gebb_customize2;
	}
	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "geb_pk";
	}
	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return "geh_pk";
	}
	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "tb_general_b";
	}
	
	
	
}
