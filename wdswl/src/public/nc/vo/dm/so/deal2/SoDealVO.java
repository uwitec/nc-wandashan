package nc.vo.dm.so.deal2;

import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.Wds2WlPubConst;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;

public class SoDealVO extends SuperVO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5056426465421488586L;

	public static String[] num_fields = new String[]{"nnumber","npacknumber","nnum","nassnum",WdsWlPubConst.DM_SO_DEALNUM_FIELD_NAME};
	
	public static String[] sort_fields = new String[]{"dbilldate"};
	
	private UFDouble nnum = null;//本次安排数量
	private UFDouble nassnum = null;
	private UFBoolean isonsell;
	
	///-----------------------------订单主表字段
	private String pk_corp;
	private String vreceiptcode;
	private String cbiztype;
	private UFDate dbilldate;
	private String ccustomerid;
	private String cdeptid;
	private String cemployeeid;
	private String coperatorid;
	private String ctermprotocolid;
	private String csalecorpid;
	private String creceiptcustomerid;
	private String creceiptcorpid;
	private String cwarehouseid;
	private UFBoolean bfreecustflag;//是否赠品
	private String cfreecustid;
	private UFDate dmakedate;
	private String capproveid;
	private UFDate dapprovedate;
	private Integer fstatus;
	private String vnote;
	
	//----------------------------------订单表体字段
	private UFDouble ntaldcnum;//销售订单累积安排主数量
	private String corder_bid;
	private String csaleid;
	private String creceipttype;
	private String csourcebillid;
	private String csourcebillbodyid;
	private String cinventoryid;
	private String cunitid;
	private String cpackunitid;
	private Integer frowstatus;
	private String frownote;
	private UFBoolean bdericttrans; // 是否直运   用于是否自提
	private String cadvisecalbody; // 建议发货库存组织
//	private String cbodywarehousename; // 仓库
	private String cconsigncorpid; // 发货公司id
	private String cprolineid; // 生产线id
	private String crecaddrnode; // 收货地点id
	private String creccalbodyid; // 收货库存组织id
	private String crecwareid; // 收货仓库id
	// 建议库存组织
	private String cadvisecalbodyid;

	// 批次
	private String cbatchid;
	//
	private String cinvbasdocid;
	private String cprojectid;
	// 收货地区
	private String creceiptareaid;
	private String vdef1;
	private String vdef2;
	private String vdef3;
	private String vdef4;
	private String vdef5;
	private String vdef6;
	// 新增的自定义项
	private String vdef7;
	private String vdef8;
	private String vdef9;
	private String vdef10;
	private String vdef11;
	private String vdef12;
	private String vdef13;
	private String vdef14;
	private String vdef15;
	private String vdef16;
	private String vdef17;
	private String vdef18;
	private String vdef19;
	private String vdef20;
    private UFDouble nnumber;//订单数量
	private UFDouble npacknumber;//订单辅数量
	private String cbodywarehouseid;//发货仓库
	private UFDate dconsigndate;
	private UFDate ddeliverdate;
	private UFBoolean blargessflag = new UFBoolean(false);//是否赠品
	private String ccurrencytypeid;
	private UFDouble nitemdiscountrate;
	private UFDouble ndiscountrate;
	private UFDouble nexchangeotobrate;
	private UFDouble nexchangeotoarate;
	private UFDouble ntaxrate;
	private UFDouble noriginalcurprice;
	private UFDouble noriginalcurtaxprice;
	private UFDouble noriginalcurnetprice;
	private UFDouble noriginalcurtaxnetprice;
	private UFDouble noriginalcurtaxmny;
	private UFDouble noriginalcurmny;
	private UFDouble noriginalcursummny;
	private UFDouble noriginalcurdiscountmny;
	private UFDouble nprice;
	private UFDouble ntaxprice;
	private UFDouble nnetprice;
	private UFDouble ntaxnetprice;
	private UFDouble ntaxmny;
	private UFDouble nmny;
	private UFDouble nsummny; // 本币价税合计
	private UFDouble ndiscountmny;
	private String crowno;
	private UFBoolean disdate;    //是否大日期
	private UFBoolean isxnap;//是否虚拟安排 liuys add
	private UFDouble nstorenumout;//库存主数量
	private UFDouble narrstorenumout;//安排后库存主数量
	private UFDouble nusefulnumout;//可用量
	private UFDouble narrusefulnumout;//安排后库存可用量
	private UFDouble ndrqstorenumout;//大日期库存主数量
	private UFDouble ndrqarrstorenumout;//大日期安排后库存主数量
	private UFDouble ndrqusefulnumout;//大日期库可用量
	private UFDouble ndrqarrusefulnumout;//大日期安排后可用量
	
	
	private UFDouble nwdsnum;//累计安排数量
	public static String[] getSort_fields() {
		return sort_fields;
	}
	public static void setSort_fields(String[] sort_fields) {
		SoDealVO.sort_fields = sort_fields;
	}
	public UFDouble getNstorenumout() {
		return nstorenumout;
	}
	public void setNstorenumout(UFDouble nstorenumout) {
		this.nstorenumout = nstorenumout;
	}
	public UFDouble getNarrstorenumout() {
		return narrstorenumout;
	}
	public void setNarrstorenumout(UFDouble narrstorenumout) {
		this.narrstorenumout = narrstorenumout;
	}
	public UFDouble getNusefulnumout() {
		return nusefulnumout;
	}
	public void setNusefulnumout(UFDouble nusefulnumout) {
		this.nusefulnumout = nusefulnumout;
	}
	public UFDouble getNarrusefulnumout() {
		return narrusefulnumout;
	}
	public void setNarrusefulnumout(UFDouble narrusefulnumout) {
		this.narrusefulnumout = narrusefulnumout;
	}
	public UFDouble getNdrqstorenumout() {
		return ndrqstorenumout;
	}
	public void setNdrqstorenumout(UFDouble ndrqstorenumout) {
		this.ndrqstorenumout = ndrqstorenumout;
	}
	public UFDouble getNdrqarrstorenumout() {
		return ndrqarrstorenumout;
	}
	public void setNdrqarrstorenumout(UFDouble ndrqarrstorenumout) {
		this.ndrqarrstorenumout = ndrqarrstorenumout;
	}
	public UFDouble getNdrqusefulnumout() {
		return ndrqusefulnumout;
	}
	public void setNdrqusefulnumout(UFDouble ndrqusefulnumout) {
		this.ndrqusefulnumout = ndrqusefulnumout;
	}
	public UFDouble getNdrqarrusefulnumout() {
		return ndrqarrusefulnumout;
	}
	public void setNdrqarrusefulnumout(UFDouble ndrqarrusefulnumout) {
		this.ndrqarrusefulnumout = ndrqarrusefulnumout;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public UFBoolean getIsxnap() {
		return isxnap;
	}
	public void setIsxnap(UFBoolean isxnap) {
		this.isxnap = isxnap;
	}
	public void setSxnap(String ssxnap) {
		setIsxnap(WdsWlPubTool.getString_NullAsTrimZeroLen(ssxnap)
				.equalsIgnoreCase(WdsWlPubConst.WDS_IC_FLAG_wu) ? UFBoolean.TRUE
				: UFBoolean.FALSE);
	}
	public UFBoolean getDisdate() {
		return disdate;
	}
	public void setDisdate(UFBoolean disdate) {
		this.disdate = disdate;
	}
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 表体合并
	 * @时间：2011-7-8下午09:53:42
	 * @param body2
	 */
	public void combin(SoDealVO body2){
		for(String name:num_fields){
			setAttributeValue(name, PuPubVO.getUFDouble_NullAsZero(getAttributeValue(name)).
					add(PuPubVO.getUFDouble_NullAsZero(body2.getAttributeValue(name))));
		}
	}
	
	public transient static String[] m_headNames = new String[]{
		"h.pk_corp",
		"h.vreceiptcode",
		"h.cbiztype",
		"h.dbilldate",
		"h.ccustomerid",
		"h.cdeptid",
		"h.cemployeeid",
		"h.coperatorid",
		"h.ctermprotocolid",
		"h.csalecorpid",
		"h.creceiptcustomerid",
		"h.creceiptcorpid",
		"h.cwarehouseid",
		"h.bfreecustflag",
		"h.cfreecustid",
		"h.dmakedate",
		"h.capproveid",
		"h.dapprovedate",
		"h.fstatus",
		"h.vnote",
		"h."+Wds2WlPubConst.so_virtual+ " sxnap ",//zhf add 是否虚拟
	};
	public transient static String[] m_bodyNames = new String[]{
		"b.corder_bid",
		"b.csaleid",
		"b.creceipttype",
		"b.csourcebillid",
		"b.csourcebillbodyid",
		"b.cinventoryid",
		"b.cunitid",
		"b.cpackunitid",
		"b.frowstatus",
		"b.frownote",
		"b.bdericttrans", // 是否直运
//		"b.cadvisecalbody", // 建议发货库存组织
//		"b.cbodywarehousename", // 仓库
		"b.cconsigncorpid", // 发货公司id
		"b.cprolineid", // 生产线id
		"b.crecaddrnode", // 收货地点id
		"b.creccalbodyid", // 收货库存组织id
		"b.crecwareid", // 收货仓库id
		// 建议库存组织
		"b.cadvisecalbodyid",

		// 批次
		"b.cbatchid",
		//
		"b.cinvbasdocid",
//		"b.cprojectid",
		// 收货地区
		"b.creceiptareaid",
//		"c.vdef1",
//		"c.vdef2",
//		"c.vdef3",
//		"c.vdef4",
//		"c.vdef5",
//		"c.vdef6",
//		// 新增的自定义项
//		"c.vdef7",
//		"c.vdef8",
//		"c.vdef9",
//		"c.vdef10",
//		"c.vdef11",
//		"c.vdef12",
//		"c.vdef13",
//		"c.vdef14",
//		"c.vdef15",
//		"c.vdef16",
//		"c.vdef17",
//		"c.vdef18",
//		"c.vdef19",
//		"c.vdef20",
	    "b.nnumber",
		"b.npacknumber",
		"b.cbodywarehouseid",
		"b.dconsigndate",
		"b.ddeliverdate",
		"b.blargessflag",
		"b.ccurrencytypeid",
		"b.nitemdiscountrate",
		"b.ndiscountrate",
		"b.nexchangeotobrate",
		"b.nexchangeotoarate",
		"b.ntaxrate",
		"b.noriginalcurprice",
		"b.noriginalcurtaxprice",
		"b.noriginalcurnetprice",
		"b.noriginalcurtaxnetprice",
		"b.noriginalcurtaxmny",
		"b.noriginalcurmny",
		"b.noriginalcursummny",
		"b.noriginalcurdiscountmny",
		"b.nprice",
		"b.ntaxprice",
		"b.nnetprice",
		"b.ntaxnetprice",
		"b.ntaxmny",
		"b.nmny",
		"b.nsummny", // 本币价税合计
		"b.ndiscountmny",
		"b.ntaldcnum",
		"b.crowno",
		"b."+WdsWlPubConst.DM_SO_DEALNUM_FIELD_NAME
	};
	
	
	public UFDouble getNwdsnum() {
		return nwdsnum;
	}
	public void setNwdsnum(UFDouble nwdsnum) {
		this.nwdsnum = nwdsnum;
	}
	public UFBoolean getIsonsell() {
		return isonsell;
	}
	public void setIsonsell(UFBoolean isonsell) {
		this.isonsell = isonsell;
	}
	public UFDouble getNtaldcnum() {
		return ntaldcnum;
	}
	public void setNtaldcnum(UFDouble ntaldcnum) {
		this.ntaldcnum = ntaldcnum;
	}
	public UFDouble getNassnum() {
		return nassnum;
	}
	public void setNassnum(UFDouble nassnum) {
		this.nassnum = nassnum;
	}
	public UFDouble getNnum() {
		return nnum;
	}
	public void setNnum(UFDouble nnum) {
		this.nnum = nnum;
	}
	
	public String getPk_corp() {
		return pk_corp;
	}
	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}
	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getVreceiptcode() {
		return vreceiptcode;
	}
	public void setVreceiptcode(String vreceiptcode) {
		this.vreceiptcode = vreceiptcode;
	}
	public String getCbiztype() {
		return cbiztype;
	}
	public void setCbiztype(String cbiztype) {
		this.cbiztype = cbiztype;
	}
	public UFDate getDbilldate() {
		return dbilldate;
	}
	public void setDbilldate(UFDate dbilldate) {
		this.dbilldate = dbilldate;
	}
	public String getCcustomerid() {
		return ccustomerid;
	}
	public void setCcustomerid(String ccustomerid) {
		this.ccustomerid = ccustomerid;
	}
	public String getCdeptid() {
		return cdeptid;
	}
	public void setCdeptid(String cdeptid) {
		this.cdeptid = cdeptid;
	}
	public String getCemployeeid() {
		return cemployeeid;
	}
	public void setCemployeeid(String cemployeeid) {
		this.cemployeeid = cemployeeid;
	}
	public String getCoperatorid() {
		return coperatorid;
	}
	public void setCoperatorid(String coperatorid) {
		this.coperatorid = coperatorid;
	}
	public String getCtermprotocolid() {
		return ctermprotocolid;
	}
	public void setCtermprotocolid(String ctermprotocolid) {
		this.ctermprotocolid = ctermprotocolid;
	}
	public String getCsalecorpid() {
		return csalecorpid;
	}
	public void setCsalecorpid(String csalecorpid) {
		this.csalecorpid = csalecorpid;
	}
	public String getCreceiptcustomerid() {
		return creceiptcustomerid;
	}
	public void setCreceiptcustomerid(String creceiptcustomerid) {
		this.creceiptcustomerid = creceiptcustomerid;
	}
	public String getCreceiptcorpid() {
		return creceiptcorpid;
	}
	public void setCreceiptcorpid(String creceiptcorpid) {
		this.creceiptcorpid = creceiptcorpid;
	}
	public String getCwarehouseid() {
		return cwarehouseid;
	}
	public void setCwarehouseid(String cwarehouseid) {
		this.cwarehouseid = cwarehouseid;
	}
	public UFBoolean getBfreecustflag() {
		return bfreecustflag;
	}
	public void setBfreecustflag(UFBoolean bfreecustflag) {
		this.bfreecustflag = bfreecustflag;
	}
	public String getCfreecustid() {
		return cfreecustid;
	}
	public void setCfreecustid(String cfreecustid) {
		this.cfreecustid = cfreecustid;
	}
	public UFDate getDmakedate() {
		return dmakedate;
	}
	public void setDmakedate(UFDate dmakedate) {
		this.dmakedate = dmakedate;
	}
	public String getCapproveid() {
		return capproveid;
	}
	public void setCapproveid(String capproveid) {
		this.capproveid = capproveid;
	}
	public UFDate getDapprovedate() {
		return dapprovedate;
	}
	public void setDapprovedate(UFDate dapprovedate) {
		this.dapprovedate = dapprovedate;
	}
	public Integer getFstatus() {
		return fstatus;
	}
	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}
	public String getVnote() {
		return vnote;
	}
	public void setVnote(String vnote) {
		this.vnote = vnote;
	}
	public String getCorder_bid() {
		return corder_bid;
	}
	public void setCorder_bid(String corder_bid) {
		this.corder_bid = corder_bid;
	}
	public String getCsaleid() {
		return csaleid;
	}
	public void setCsaleid(String csaleid) {
		this.csaleid = csaleid;
	}
	public String getCreceipttype() {
		return creceipttype;
	}
	public void setCreceipttype(String creceipttype) {
		this.creceipttype = creceipttype;
	}
	public String getCsourcebillid() {
		return csourcebillid;
	}
	public void setCsourcebillid(String csourcebillid) {
		this.csourcebillid = csourcebillid;
	}
	public String getCsourcebillbodyid() {
		return csourcebillbodyid;
	}
	public void setCsourcebillbodyid(String csourcebillbodyid) {
		this.csourcebillbodyid = csourcebillbodyid;
	}
	public String getCinventoryid() {
		return cinventoryid;
	}
	public void setCinventoryid(String cinventoryid) {
		this.cinventoryid = cinventoryid;
	}
	public String getCunitid() {
		return cunitid;
	}
	public void setCunitid(String cunitid) {
		this.cunitid = cunitid;
	}
	public String getCpackunitid() {
		return cpackunitid;
	}
	public void setCpackunitid(String cpackunitid) {
		this.cpackunitid = cpackunitid;
	}
	public Integer getFrowstatus() {
		return frowstatus;
	}
	public void setFrowstatus(Integer frowstatus) {
		this.frowstatus = frowstatus;
	}
	public String getFrownote() {
		return frownote;
	}
	public void setFrownote(String frownote) {
		this.frownote = frownote;
	}
	public UFBoolean getBdericttrans() {
		return bdericttrans;
	}
	public void setBdericttrans(UFBoolean bdericttrans) {
		this.bdericttrans = bdericttrans;
	}
	public String getCadvisecalbody() {
		return cadvisecalbody;
	}
	public void setCadvisecalbody(String cadvisecalbody) {
		this.cadvisecalbody = cadvisecalbody;
	}
	public String getCconsigncorpid() {
		return cconsigncorpid;
	}
	public void setCconsigncorpid(String cconsigncorpid) {
		this.cconsigncorpid = cconsigncorpid;
	}
	public String getCprolineid() {
		return cprolineid;
	}
	public void setCprolineid(String cprolineid) {
		this.cprolineid = cprolineid;
	}
	public String getCrecaddrnode() {
		return crecaddrnode;
	}
	public void setCrecaddrnode(String crecaddrnode) {
		this.crecaddrnode = crecaddrnode;
	}
	public String getCreccalbodyid() {
		return creccalbodyid;
	}
	public void setCreccalbodyid(String creccalbodyid) {
		this.creccalbodyid = creccalbodyid;
	}
	public String getCrecwareid() {
		return crecwareid;
	}
	public void setCrecwareid(String crecwareid) {
		this.crecwareid = crecwareid;
	}
	public String getCadvisecalbodyid() {
		return cadvisecalbodyid;
	}
	public void setCadvisecalbodyid(String cadvisecalbodyid) {
		this.cadvisecalbodyid = cadvisecalbodyid;
	}
	public String getCbatchid() {
		return cbatchid;
	}
	public void setCbatchid(String cbatchid) {
		this.cbatchid = cbatchid;
	}
	public String getCinvbasdocid() {
		return cinvbasdocid;
	}
	public void setCinvbasdocid(String cinvbasdocid) {
		this.cinvbasdocid = cinvbasdocid;
	}
	public String getCprojectid() {
		return cprojectid;
	}
	public void setCprojectid(String cprojectid) {
		this.cprojectid = cprojectid;
	}
	public String getCreceiptareaid() {
		return creceiptareaid;
	}
	public void setCreceiptareaid(String creceiptareaid) {
		this.creceiptareaid = creceiptareaid;
	}
	public String getVdef1() {
		return vdef1;
	}
	public void setVdef1(String vdef1) {
		this.vdef1 = vdef1;
	}
	public String getVdef2() {
		return vdef2;
	}
	public void setVdef2(String vdef2) {
		this.vdef2 = vdef2;
	}
	public String getVdef3() {
		return vdef3;
	}
	public void setVdef3(String vdef3) {
		this.vdef3 = vdef3;
	}
	public String getVdef4() {
		return vdef4;
	}
	public void setVdef4(String vdef4) {
		this.vdef4 = vdef4;
	}
	public String getVdef5() {
		return vdef5;
	}
	public void setVdef5(String vdef5) {
		this.vdef5 = vdef5;
	}
	public String getVdef6() {
		return vdef6;
	}
	public void setVdef6(String vdef6) {
		this.vdef6 = vdef6;
	}
	public String getVdef7() {
		return vdef7;
	}
	public void setVdef7(String vdef7) {
		this.vdef7 = vdef7;
	}
	public String getVdef8() {
		return vdef8;
	}
	public void setVdef8(String vdef8) {
		this.vdef8 = vdef8;
	}
	public String getVdef9() {
		return vdef9;
	}
	public void setVdef9(String vdef9) {
		this.vdef9 = vdef9;
	}
	public String getVdef10() {
		return vdef10;
	}
	public void setVdef10(String vdef10) {
		this.vdef10 = vdef10;
	}
	public String getVdef11() {
		return vdef11;
	}
	public void setVdef11(String vdef11) {
		this.vdef11 = vdef11;
	}
	public String getVdef12() {
		return vdef12;
	}
	public void setVdef12(String vdef12) {
		this.vdef12 = vdef12;
	}
	public String getVdef13() {
		return vdef13;
	}
	public void setVdef13(String vdef13) {
		this.vdef13 = vdef13;
	}
	public String getVdef14() {
		return vdef14;
	}
	public void setVdef14(String vdef14) {
		this.vdef14 = vdef14;
	}
	public String getVdef15() {
		return vdef15;
	}
	public void setVdef15(String vdef15) {
		this.vdef15 = vdef15;
	}
	public String getVdef16() {
		return vdef16;
	}
	public void setVdef16(String vdef16) {
		this.vdef16 = vdef16;
	}
	public String getVdef17() {
		return vdef17;
	}
	public void setVdef17(String vdef17) {
		this.vdef17 = vdef17;
	}
	public String getVdef18() {
		return vdef18;
	}
	public void setVdef18(String vdef18) {
		this.vdef18 = vdef18;
	}
	public String getVdef19() {
		return vdef19;
	}
	public void setVdef19(String vdef19) {
		this.vdef19 = vdef19;
	}
	public String getVdef20() {
		return vdef20;
	}
	public void setVdef20(String vdef20) {
		this.vdef20 = vdef20;
	}
	public UFDouble getNnumber() {
		return nnumber;
	}
	public void setNnumber(UFDouble nnumber) {
		this.nnumber = nnumber;
	}
	public UFDouble getNpacknumber() {
		return npacknumber;
	}
	public void setNpacknumber(UFDouble npacknumber) {
		this.npacknumber = npacknumber;
	}
	public String getCbodywarehouseid() {
		return cbodywarehouseid;
	}
	public void setCbodywarehouseid(String cbodywarehouseid) {
		this.cbodywarehouseid = cbodywarehouseid;
	}
	public UFDate getDconsigndate() {
		return dconsigndate;
	}
	public void setDconsigndate(UFDate dconsigndate) {
		this.dconsigndate = dconsigndate;
	}
	public UFDate getDdeliverdate() {
		return ddeliverdate;
	}
	public void setDdeliverdate(UFDate ddeliverdate) {
		this.ddeliverdate = ddeliverdate;
	}
	public UFBoolean getBlargessflag() {
		return blargessflag;
	}
	public void setBlargessflag(UFBoolean blargessflag) {
		this.blargessflag = blargessflag;
	}
	public String getCcurrencytypeid() {
		return ccurrencytypeid;
	}
	public void setCcurrencytypeid(String ccurrencytypeid) {
		this.ccurrencytypeid = ccurrencytypeid;
	}
	public UFDouble getNitemdiscountrate() {
		return nitemdiscountrate;
	}
	public void setNitemdiscountrate(UFDouble nitemdiscountrate) {
		this.nitemdiscountrate = nitemdiscountrate;
	}
	public UFDouble getNdiscountrate() {
		return ndiscountrate;
	}
	public void setNdiscountrate(UFDouble ndiscountrate) {
		this.ndiscountrate = ndiscountrate;
	}
	public UFDouble getNexchangeotobrate() {
		return nexchangeotobrate;
	}
	public void setNexchangeotobrate(UFDouble nexchangeotobrate) {
		this.nexchangeotobrate = nexchangeotobrate;
	}
	public UFDouble getNexchangeotoarate() {
		return nexchangeotoarate;
	}
	public void setNexchangeotoarate(UFDouble nexchangeotoarate) {
		this.nexchangeotoarate = nexchangeotoarate;
	}
	public UFDouble getNtaxrate() {
		return ntaxrate;
	}
	public void setNtaxrate(UFDouble ntaxrate) {
		this.ntaxrate = ntaxrate;
	}
	public UFDouble getNoriginalcurprice() {
		return noriginalcurprice;
	}
	public void setNoriginalcurprice(UFDouble noriginalcurprice) {
		this.noriginalcurprice = noriginalcurprice;
	}
	public UFDouble getNoriginalcurtaxprice() {
		return noriginalcurtaxprice;
	}
	public void setNoriginalcurtaxprice(UFDouble noriginalcurtaxprice) {
		this.noriginalcurtaxprice = noriginalcurtaxprice;
	}
	public UFDouble getNoriginalcurnetprice() {
		return noriginalcurnetprice;
	}
	public void setNoriginalcurnetprice(UFDouble noriginalcurnetprice) {
		this.noriginalcurnetprice = noriginalcurnetprice;
	}
	public UFDouble getNoriginalcurtaxnetprice() {
		return noriginalcurtaxnetprice;
	}
	public void setNoriginalcurtaxnetprice(UFDouble noriginalcurtaxnetprice) {
		this.noriginalcurtaxnetprice = noriginalcurtaxnetprice;
	}
	public UFDouble getNoriginalcurtaxmny() {
		return noriginalcurtaxmny;
	}
	public void setNoriginalcurtaxmny(UFDouble noriginalcurtaxmny) {
		this.noriginalcurtaxmny = noriginalcurtaxmny;
	}
	public UFDouble getNoriginalcurmny() {
		return noriginalcurmny;
	}
	public void setNoriginalcurmny(UFDouble noriginalcurmny) {
		this.noriginalcurmny = noriginalcurmny;
	}
	public UFDouble getNoriginalcursummny() {
		return noriginalcursummny;
	}
	public void setNoriginalcursummny(UFDouble noriginalcursummny) {
		this.noriginalcursummny = noriginalcursummny;
	}
	public UFDouble getNoriginalcurdiscountmny() {
		return noriginalcurdiscountmny;
	}
	public void setNoriginalcurdiscountmny(UFDouble noriginalcurdiscountmny) {
		this.noriginalcurdiscountmny = noriginalcurdiscountmny;
	}
	public UFDouble getNprice() {
		return nprice;
	}
	public void setNprice(UFDouble nprice) {
		this.nprice = nprice;
	}
	public UFDouble getNtaxprice() {
		return ntaxprice;
	}
	public void setNtaxprice(UFDouble ntaxprice) {
		this.ntaxprice = ntaxprice;
	}
	public UFDouble getNnetprice() {
		return nnetprice;
	}
	public void setNnetprice(UFDouble nnetprice) {
		this.nnetprice = nnetprice;
	}
	public UFDouble getNtaxnetprice() {
		return ntaxnetprice;
	}
	public void setNtaxnetprice(UFDouble ntaxnetprice) {
		this.ntaxnetprice = ntaxnetprice;
	}
	public UFDouble getNtaxmny() {
		return ntaxmny;
	}
	public void setNtaxmny(UFDouble ntaxmny) {
		this.ntaxmny = ntaxmny;
	}
	public UFDouble getNmny() {
		return nmny;
	}
	public void setNmny(UFDouble nmny) {
		this.nmny = nmny;
	}
	public UFDouble getNsummny() {
		return nsummny;
	}
	public void setNsummny(UFDouble nsummny) {
		this.nsummny = nsummny;
	}
	public UFDouble getNdiscountmny() {
		return ndiscountmny;
	}
	public void setNdiscountmny(UFDouble ndiscountmny) {
		this.ndiscountmny = ndiscountmny;
	}

	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 调出仓库不能为空   调入仓库不能为空 两个仓库不能相同  
	 *        本次安排数量不能大于 计划数量-累计安排数量
	 * @时间：2011-3-23下午08:19:19
	 */
	public void validataOnDeal() throws ValidationException{
		if(PuPubVO.getString_TrimZeroLenAsNull(getCbodywarehouseid())==null){
			throw new ValidationException("发货站不能为空");
		}
		if(PuPubVO.getString_TrimZeroLenAsNull(getCcustomerid())==null){
			throw new ValidationException("客户不能为空");
		}
		if(PuPubVO.getString_TrimZeroLenAsNull(getCinvbasdocid())==null || PuPubVO.getString_TrimZeroLenAsNull(getCinventoryid()) == null){
			throw new ValidationException("存货为空");
		}
		if(PuPubVO.getUFDouble_NullAsZero(getNnum()).equals(WdsWlPubTool.DOUBLE_ZERO)){
			throw new ValidationException("待安排量为空或0");
		}
		UFDouble nchecknum = PuPubVO.getUFDouble_NullAsZero(getNnumber()).sub(PuPubVO.getUFDouble_NullAsZero(getAttributeValue(WdsWlPubConst.DM_SO_DEALNUM_FIELD_NAME)));
		if(PuPubVO.getUFDouble_NullAsZero(getNnum()).sub(nchecknum).doubleValue()>0)
			throw new ValidationException("不能超销售计划安排");
		UFBoolean fisgift = PuPubVO.getUFBoolean_NullAs(getBlargessflag(), UFBoolean.FALSE);
		if(fisgift.booleanValue()){
			if(PuPubVO.getUFDouble_NullAsZero(getNnum()).sub(nchecknum).doubleValue() !=0)
				throw new ValidationException("赠品不允许拆分");
		}
		
		if(PuPubVO.getString_TrimZeroLenAsNull(getVdef1()) == null){
			throw new ValidationException("存货状态未指定");
		}
	}
	public String getCrowno() {
		return crowno;
	}
	public void setCrowno(String crowno) {
		this.crowno = crowno;
	}
	
	public void setAttributeValue(String attributeName, Object value) {
		if(attributeName.equalsIgnoreCase("sxnap"))
			setSxnap(WdsWlPubTool.getString_NullAsTrimZeroLen(value));
		else 
			super.setAttributeValue(attributeName, value);
	}
	
}
