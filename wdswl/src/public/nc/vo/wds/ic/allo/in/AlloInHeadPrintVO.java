package nc.vo.wds.ic.allo.in;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFTime;

/**
 * 
 * @author zhf  调拨入库打印vo
 *
 */
public class AlloInHeadPrintVO extends SuperVO{
//	原主表字段
	public String geh_cgeneralhid;//上游 运单主表id
    public String geh_customize3;
    public String pk_customize3;
    /** 库管员*/
    public String geh_cwhsmanagerid;
    public Integer dr;
    public String geh_customize6;
    public String cauditorid;
    public String pk_measware;
    //单据日期
    public UFDate geh_dbilldate;
    /**库存组织*/
    public String geh_calbody;
    public String geh_customize5;
    /**制单人Id */
    public String coperatorid;
    public String maxstock;
    public String pk_customize1;
    public UFTime ts;
    public String geh_vbillcode;
    public Integer iprintcount;
    public String geh_customize4;
    /**收发类型 */
    public String geh_cdispatcherid;
    public UFDate clastmodedate;
    public String geh_pk;
    public Integer geh_fallocflag;
    public String pk_customize7;
    /** 出库公司*/
    public String geh_cothercorpid;
    /**单据号 */
    public String geh_billcode;
    public String geh_storname;
    /**部门id */
    public String geh_cdptid;
    public String safestock;
    public UFDate geh_stordate;
    public String geh_libname;
    public String pk_customize4;
    /**单据状态 */
    public Integer pwb_fbillflag;
    /**公司 */
    public String geh_corp;
    public String pk_corp;

    public String geh_customize9;
    public String pk_customize9;
    public UFBoolean geh_freplenishflag;
    public String pk_customize6;
    /**出库仓库 */
    public String geh_cotherwhid;
    public String geh_libdate;
    public String clastmodiid;
    public String geh_customize8;
    public String minstock;
    public String geh_cbiztype;
    public String pk_customize8;
    public String vnote;
    /**出库库存组织 */
    public String geh_cothercalbodyid;
    public String pk_customize2;
    public String geh_customize2;
    /**业务员 */
    public String geh_cbizid;
    /**入库仓库 */
    public String geh_cwarehouseid;
    /**单据类型编码 */
    public String geh_cbilltypecode;
    public String geh_customize1;
    public String geh_tranname;
    public String geh_customize7;
    public String pk_customize5;
    /**制单日期 */
    public UFDate copetadate;
    public String pwb_pk;//生产倒入运单主键
    public String geh_billtype;//入库单类型
    public String tmaketime;//制单时间
    public String taccounttime;//签字时间
    public String clastmodetime;//最后修改时间
    /** 入库货位主键 */
    private String pk_cargdoc;
    
    public String getGeh_cgeneralhid() {
		return geh_cgeneralhid;
	}

	public void setGeh_cgeneralhid(String geh_cgeneralhid) {
		this.geh_cgeneralhid = geh_cgeneralhid;
	}

	public String getGeh_customize3() {
		return geh_customize3;
	}

	public void setGeh_customize3(String geh_customize3) {
		this.geh_customize3 = geh_customize3;
	}

	public String getPk_customize3() {
		return pk_customize3;
	}

	public void setPk_customize3(String pk_customize3) {
		this.pk_customize3 = pk_customize3;
	}

	public String getGeh_cwhsmanagerid() {
		return geh_cwhsmanagerid;
	}

	public void setGeh_cwhsmanagerid(String geh_cwhsmanagerid) {
		this.geh_cwhsmanagerid = geh_cwhsmanagerid;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public String getGeh_customize6() {
		return geh_customize6;
	}

	public void setGeh_customize6(String geh_customize6) {
		this.geh_customize6 = geh_customize6;
	}

	public String getCauditorid() {
		return cauditorid;
	}

	public void setCauditorid(String cauditorid) {
		this.cauditorid = cauditorid;
	}

	public String getPk_measware() {
		return pk_measware;
	}

	public void setPk_measware(String pk_measware) {
		this.pk_measware = pk_measware;
	}

	public UFDate getGeh_dbilldate() {
		return geh_dbilldate;
	}

	public void setGeh_dbilldate(UFDate geh_dbilldate) {
		this.geh_dbilldate = geh_dbilldate;
	}

	public String getGeh_calbody() {
		return geh_calbody;
	}

	public void setGeh_calbody(String geh_calbody) {
		this.geh_calbody = geh_calbody;
	}

	public String getGeh_customize5() {
		return geh_customize5;
	}

	public void setGeh_customize5(String geh_customize5) {
		this.geh_customize5 = geh_customize5;
	}

	public String getCoperatorid() {
		return coperatorid;
	}

	public void setCoperatorid(String coperatorid) {
		this.coperatorid = coperatorid;
	}

	public String getMaxstock() {
		return maxstock;
	}

	public void setMaxstock(String maxstock) {
		this.maxstock = maxstock;
	}

	public String getPk_customize1() {
		return pk_customize1;
	}

	public void setPk_customize1(String pk_customize1) {
		this.pk_customize1 = pk_customize1;
	}

	public UFTime getTs() {
		return ts;
	}

	public void setTs(UFTime ts) {
		this.ts = ts;
	}

	public String getGeh_vbillcode() {
		return geh_vbillcode;
	}

	public void setGeh_vbillcode(String geh_vbillcode) {
		this.geh_vbillcode = geh_vbillcode;
	}

	public Integer getIprintcount() {
		return iprintcount;
	}

	public void setIprintcount(Integer iprintcount) {
		this.iprintcount = iprintcount;
	}

	public String getGeh_customize4() {
		return geh_customize4;
	}

	public void setGeh_customize4(String geh_customize4) {
		this.geh_customize4 = geh_customize4;
	}

	public String getGeh_cdispatcherid() {
		return geh_cdispatcherid;
	}

	public void setGeh_cdispatcherid(String geh_cdispatcherid) {
		this.geh_cdispatcherid = geh_cdispatcherid;
	}

	public UFDate getClastmodedate() {
		return clastmodedate;
	}

	public void setClastmodedate(UFDate clastmodedate) {
		this.clastmodedate = clastmodedate;
	}

	public String getGeh_pk() {
		return geh_pk;
	}

	public void setGeh_pk(String geh_pk) {
		this.geh_pk = geh_pk;
	}

	public Integer getGeh_fallocflag() {
		return geh_fallocflag;
	}

	public void setGeh_fallocflag(Integer geh_fallocflag) {
		this.geh_fallocflag = geh_fallocflag;
	}

	public String getPk_customize7() {
		return pk_customize7;
	}

	public void setPk_customize7(String pk_customize7) {
		this.pk_customize7 = pk_customize7;
	}

	public String getGeh_cothercorpid() {
		return geh_cothercorpid;
	}

	public void setGeh_cothercorpid(String geh_cothercorpid) {
		this.geh_cothercorpid = geh_cothercorpid;
	}

	public String getGeh_billcode() {
		return geh_billcode;
	}

	public void setGeh_billcode(String geh_billcode) {
		this.geh_billcode = geh_billcode;
	}

	public String getGeh_storname() {
		return geh_storname;
	}

	public void setGeh_storname(String geh_storname) {
		this.geh_storname = geh_storname;
	}

	public String getGeh_cdptid() {
		return geh_cdptid;
	}

	public void setGeh_cdptid(String geh_cdptid) {
		this.geh_cdptid = geh_cdptid;
	}

	public String getSafestock() {
		return safestock;
	}

	public void setSafestock(String safestock) {
		this.safestock = safestock;
	}

	public UFDate getGeh_stordate() {
		return geh_stordate;
	}

	public void setGeh_stordate(UFDate geh_stordate) {
		this.geh_stordate = geh_stordate;
	}

	public String getGeh_libname() {
		return geh_libname;
	}

	public void setGeh_libname(String geh_libname) {
		this.geh_libname = geh_libname;
	}

	public String getPk_customize4() {
		return pk_customize4;
	}

	public void setPk_customize4(String pk_customize4) {
		this.pk_customize4 = pk_customize4;
	}

	public Integer getPwb_fbillflag() {
		return pwb_fbillflag;
	}

	public void setPwb_fbillflag(Integer pwb_fbillflag) {
		this.pwb_fbillflag = pwb_fbillflag;
	}

	public String getGeh_corp() {
		return geh_corp;
	}

	public void setGeh_corp(String geh_corp) {
		this.geh_corp = geh_corp;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public String getGeh_customize9() {
		return geh_customize9;
	}

	public void setGeh_customize9(String geh_customize9) {
		this.geh_customize9 = geh_customize9;
	}

	public String getPk_customize9() {
		return pk_customize9;
	}

	public void setPk_customize9(String pk_customize9) {
		this.pk_customize9 = pk_customize9;
	}

	public UFBoolean getGeh_freplenishflag() {
		return geh_freplenishflag;
	}

	public void setGeh_freplenishflag(UFBoolean geh_freplenishflag) {
		this.geh_freplenishflag = geh_freplenishflag;
	}

	public String getPk_customize6() {
		return pk_customize6;
	}

	public void setPk_customize6(String pk_customize6) {
		this.pk_customize6 = pk_customize6;
	}

	public String getGeh_cotherwhid() {
		return geh_cotherwhid;
	}

	public void setGeh_cotherwhid(String geh_cotherwhid) {
		this.geh_cotherwhid = geh_cotherwhid;
	}

	public String getGeh_libdate() {
		return geh_libdate;
	}

	public void setGeh_libdate(String geh_libdate) {
		this.geh_libdate = geh_libdate;
	}

	public String getClastmodiid() {
		return clastmodiid;
	}

	public void setClastmodiid(String clastmodiid) {
		this.clastmodiid = clastmodiid;
	}

	public String getGeh_customize8() {
		return geh_customize8;
	}

	public void setGeh_customize8(String geh_customize8) {
		this.geh_customize8 = geh_customize8;
	}

	public String getMinstock() {
		return minstock;
	}

	public void setMinstock(String minstock) {
		this.minstock = minstock;
	}

	public String getGeh_cbiztype() {
		return geh_cbiztype;
	}

	public void setGeh_cbiztype(String geh_cbiztype) {
		this.geh_cbiztype = geh_cbiztype;
	}

	public String getPk_customize8() {
		return pk_customize8;
	}

	public void setPk_customize8(String pk_customize8) {
		this.pk_customize8 = pk_customize8;
	}

	public String getVnote() {
		return vnote;
	}

	public void setVnote(String vnote) {
		this.vnote = vnote;
	}

	public String getGeh_cothercalbodyid() {
		return geh_cothercalbodyid;
	}

	public void setGeh_cothercalbodyid(String geh_cothercalbodyid) {
		this.geh_cothercalbodyid = geh_cothercalbodyid;
	}

	public String getPk_customize2() {
		return pk_customize2;
	}

	public void setPk_customize2(String pk_customize2) {
		this.pk_customize2 = pk_customize2;
	}

	public String getGeh_customize2() {
		return geh_customize2;
	}

	public void setGeh_customize2(String geh_customize2) {
		this.geh_customize2 = geh_customize2;
	}

	public String getGeh_cbizid() {
		return geh_cbizid;
	}

	public void setGeh_cbizid(String geh_cbizid) {
		this.geh_cbizid = geh_cbizid;
	}

	public String getGeh_cwarehouseid() {
		return geh_cwarehouseid;
	}

	public void setGeh_cwarehouseid(String geh_cwarehouseid) {
		this.geh_cwarehouseid = geh_cwarehouseid;
	}

	public String getGeh_cbilltypecode() {
		return geh_cbilltypecode;
	}

	public void setGeh_cbilltypecode(String geh_cbilltypecode) {
		this.geh_cbilltypecode = geh_cbilltypecode;
	}

	public String getGeh_customize1() {
		return geh_customize1;
	}

	public void setGeh_customize1(String geh_customize1) {
		this.geh_customize1 = geh_customize1;
	}

	public String getGeh_tranname() {
		return geh_tranname;
	}

	public void setGeh_tranname(String geh_tranname) {
		this.geh_tranname = geh_tranname;
	}

	public String getGeh_customize7() {
		return geh_customize7;
	}

	public void setGeh_customize7(String geh_customize7) {
		this.geh_customize7 = geh_customize7;
	}

	public String getPk_customize5() {
		return pk_customize5;
	}

	public void setPk_customize5(String pk_customize5) {
		this.pk_customize5 = pk_customize5;
	}

	public UFDate getCopetadate() {
		return copetadate;
	}

	public void setCopetadate(UFDate copetadate) {
		this.copetadate = copetadate;
	}

	public String getPwb_pk() {
		return pwb_pk;
	}

	public void setPwb_pk(String pwb_pk) {
		this.pwb_pk = pwb_pk;
	}

	public String getGeh_billtype() {
		return geh_billtype;
	}

	public void setGeh_billtype(String geh_billtype) {
		this.geh_billtype = geh_billtype;
	}

	public String getTmaketime() {
		return tmaketime;
	}

	public void setTmaketime(String tmaketime) {
		this.tmaketime = tmaketime;
	}

	public String getTaccounttime() {
		return taccounttime;
	}

	public void setTaccounttime(String taccounttime) {
		this.taccounttime = taccounttime;
	}

	public String getClastmodetime() {
		return clastmodetime;
	}

	public void setClastmodetime(String clastmodetime) {
		this.clastmodetime = clastmodetime;
	}

	public String getPk_cargdoc() {
		return pk_cargdoc;
	}

	public void setPk_cargdoc(String pk_cargdoc) {
		this.pk_cargdoc = pk_cargdoc;
	}

	public UFBoolean getFisnewcode() {
		return fisnewcode;
	}

	public void setFisnewcode(UFBoolean fisnewcode) {
		this.fisnewcode = fisnewcode;
	}

	public UFBoolean getFisload() {
		return fisload;
	}

	public void setFisload(UFBoolean fisload) {
		this.fisload = fisload;
	}

	public UFBoolean getFistran() {
		return fistran;
	}

	public void setFistran(UFBoolean fistran) {
		this.fistran = fistran;
	}

	public UFBoolean getIscaltrans() {
		return iscaltrans;
	}

	public void setIscaltrans(UFBoolean iscaltrans) {
		this.iscaltrans = iscaltrans;
	}
	private UFBoolean fisnewcode;//回写供应链是否使用参数设置的批次号

	public UFBoolean fisload;//是否装卸费计算完成
	public UFBoolean fistran;//是否运费计算完成
	public UFBoolean iscaltrans;//是否计算运费
//	扩展字段
	private String cotherunitname;//出货公司
	private String cotherwhname;//出库仓库
	private String cotherbodyname;//出库库存组织
	private String cdispatchername;//收发类型
	private String cwhsmanagername;//库管员
	private String cdptname;//部门
	private String cbizname;//业务员
	private String cropname;//入库公司
	private String vcalbodyname;//入库库存组织
	private String cwarehousename;//入库仓库
	private String cname;//货位
	
	
	
	/**
	  * <p>取得父VO主键字段.
	  * <p>
	  * 创建日期:2010-7-19
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	  	 
	 	    return null;
	 	
	}   
   
   /**
	  * <p>取得表主键.
	  * <p>
	  * 创建日期:2010-7-19
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	 	  return "geh_pk";
	 	}
   
	/**
     * <p>返回表名称.
	  * <p>
	  * 创建日期:2010-7-19
	  * @return java.lang.String
	 */
	public java.lang.String getTableName() {
				
		return "tb_general_h";
	} 
	
	
	public String getCotherunitname() {
		return cotherunitname;
	}
	public void setCotherunitname(String cotherunitname) {
		this.cotherunitname = cotherunitname;
	}
	public String getCotherwhname() {
		return cotherwhname;
	}
	public void setCotherwhname(String cotherwhname) {
		this.cotherwhname = cotherwhname;
	}
	public String getCotherbodyname() {
		return cotherbodyname;
	}
	public void setCotherbodyname(String cotherbodyname) {
		this.cotherbodyname = cotherbodyname;
	}
	public String getCdispatchername() {
		return cdispatchername;
	}
	public void setCdispatchername(String cdispatchername) {
		this.cdispatchername = cdispatchername;
	}
	public String getCwhsmanagername() {
		return cwhsmanagername;
	}
	public void setCwhsmanagername(String cwhsmanagername) {
		this.cwhsmanagername = cwhsmanagername;
	}
	public String getCdptname() {
		return cdptname;
	}
	public void setCdptname(String cdptname) {
		this.cdptname = cdptname;
	}
	public String getCbizname() {
		return cbizname;
	}
	public void setCbizname(String cbizname) {
		this.cbizname = cbizname;
	}
	public String getCropname() {
		return cropname;
	}
	public void setCropname(String cropname) {
		this.cropname = cropname;
	}
	public String getVcalbodyname() {
		return vcalbodyname;
	}
	public void setVcalbodyname(String vcalbodyname) {
		this.vcalbodyname = vcalbodyname;
	}
	public String getCwarehousename() {
		return cwarehousename;
	}
	public void setCwarehousename(String cwarehousename) {
		this.cwarehousename = cwarehousename;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	
	
}
