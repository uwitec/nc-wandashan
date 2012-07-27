package nc.vo.dm.db.deal;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
/**
 * 调拨安排vo
 * @author mlr
 */
public class DbDealVO extends SuperVO{	
	/**
	 * alter table  to_bill_b add(ndealnum number(20,8))//调拨订单新增字段  已安排数量
	 * alter table  to_bill_b add(ndealnumb number(20,8))//调拨订单新增字段  已安排辅数量
	 */	
	private static final long serialVersionUID = -5056426465421488586L;
	public static String[] num_fields = new String[]{"num","nassnum","nnum","nassistnum","ndealnum","ndealnumb"};//本次安排数量  订单数量     订单已安排数量
	private UFDouble num;//本次安排数量
	private UFDouble nassnum;//本次安排辅数量
	///-----------------------------订单主表字段
	private String cincorpid;//调入公司
	private String cincbid;//调入库存组织
	private Integer fallocflag;//调拨类型标志
	private String vnote;//备注
	private String vcode ;//单据号
	private UFDate dbilldate;//单据日期
	
	//----------------------------------订单表体字段
    private UFDouble nnum;//订单数量
    private UFDouble nassistnum;//订单辅数量
    private UFDouble ndealnum ;//订单已经安排的数量
    private UFDouble ndealnumb;//订单已经安排 的辅数量
    private String coutdeptid;//调出部门
    private String coutcorpid;//调出公司
    private String coutcbid;//调出库存组织
    private String coutwhid;//调出仓库
    private String cinwhid;//调入仓库
    private String coutpsnid;//调出部门业务员
    private UFBoolean bretractflag;//是否退回
    private String pk_sendtype;//发运方式
    private String vreceiveaddress;//收货地址
    private String ccustomerid;//整单关闭人 
    private String ctakeoutinvid;//存货管理id
    private String cinvbasid;//存货基本id
    private String cprojectphase;//项目阶段
    private String cprojectid;//项目主键
    private String cbillid;// 表头id
    private String cbill_bid;//表体id
    private String vbatch;//批次号
    private String crowno;//行号
    private String ctypecode;//订单类型
    private String castunitid;//辅计量单位
    private String creceieveid;//收货单位
    private UFDouble nchangerate;//换算率
    private String ctakeoutspaceid;//出库货位
    private String dvalidate;//失效日期
    private String cquoteunitid;//报价计量单位ID		
    private UFDouble nquoteunitrate;//报价计量单位换算率
    private UFBoolean flargess;//是否赠品
    private String cvendorid;//供应商

    //--------------------------其他信息
    private String vdef1;//存货状态
    private UFDouble nstorenumout ;//库存主数量
    private UFDouble anstorenumout;//库存辅数量
	private UFDouble ndrqstorenumout;//可用量辅数量 mlr
	private UFDouble ndrqarrstorenumout;//可用量 mlr

	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 表体合并
	 * @时间：2011-7-8下午09:53:42
	 * @param body2
	 */
	public void combin(DbDealVO body2){
		for(String name:num_fields){
			setAttributeValue(name, PuPubVO.getUFDouble_NullAsZero(getAttributeValue(name)).
					add(PuPubVO.getUFDouble_NullAsZero(body2.getAttributeValue(name))));
		}
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

	public transient static String[] m_headNames = new String[]{
		"h.cincorpid",
		"h.cincbid",
		"h.fallocflag",
		"h.ctypecode",
		"h.vnote",

	};
	public transient static String[] m_bodyNames = new String[]{
		"b.nnum",
		"b.nassistnum",
		"b.ndealnum",
		"b.ndealnumb",		
		"b.coutdeptid",
		"b.coutcorpid",
		"b.coutcbid",
		"b.coutwhid",		
		"b.cinwhid",
		"b.coutpsnid",
		"b.bretractflag", 
		"b.pk_sendtype", 
		"b.vreceiveaddress", 
//		"b.ccustomerid", 
		"b.ctakeoutinvid", 
		"b.cinvbasid", 
		"b.cprojectphase",
		"b.cprojectid",
		"b.cbillid",
		"b.cbill_bid",
	    "b.vbatch",
		"b.vcode",
		"b.crowno",
		"b.ctypecode",
		"b.castunitid",
		"b.creceieveid",
		"b.nchangerate",
		"b.ctakeoutspaceid",
		"b.dvalidate",
		"b.cquoteunitid",
	//	"b.nexchangeotoarate",
		"b.nquoteunitrate",
		"b.flargess",
		"b.cvendorid",
	};
	
	
	
	public UFDouble getNassnum() {
		return nassnum;
	}
	public void setNassnum(UFDouble nassnum) {
		this.nassnum = nassnum;
	}
	public UFDouble getNstorenumout() {
		return nstorenumout;
	}
	public void setNstorenumout(UFDouble nstorenumout) {
		this.nstorenumout = nstorenumout;
	}
	public UFDouble getAnstorenumout() {
		return anstorenumout;
	}
	public void setAnstorenumout(UFDouble anstorenumout) {
		this.anstorenumout = anstorenumout;
	}
	public UFDouble getNnum() {
		return nnum;
	}
	public void setNnum(UFDouble nnum) {
		this.nnum = nnum;
	}
	@Override
	public String getPKFieldName() {
		return null;
	}
	@Override
	public String getParentPKFieldName() {
		return null;
	}
	
	
	public String getVdef1() {
		return vdef1;
	}
	public void setVdef1(String vdef1) {
		this.vdef1 = vdef1;
	}
	@Override
	public String getTableName() {
		return null;
	}
	public String getVnote() {
		return vnote;
	}
	public UFDouble getNum() {
		return num;
	}
	public void setNum(UFDouble num) {
		this.num = num;
	}
	public String getCincorpid() {
		return cincorpid;
	}
	public void setCincorpid(String cincorpid) {
		this.cincorpid = cincorpid;
	}
	public String getCincbid() {
		return cincbid;
	}
	public void setCincbid(String cincbid) {
		this.cincbid = cincbid;
	}
	public Integer getFallocflag() {
		return fallocflag;
	}
	public void setFallocflag(Integer fallocflag) {
		this.fallocflag = fallocflag;
	}
	public UFDouble getNassistnum() {
		return nassistnum;
	}
	public void setNassistnum(UFDouble nassistnum) {
		this.nassistnum = nassistnum;
	}
	public UFDouble getNdealnum() {
		return ndealnum;
	}
	public void setNdealnum(UFDouble ndealnum) {
		this.ndealnum = ndealnum;
	}
	public UFDouble getNdealnumb() {
		return ndealnumb;
	}
	public void setNdealnumb(UFDouble ndealnumb) {
		this.ndealnumb = ndealnumb;
	}
	public String getCoutdeptid() {
		return coutdeptid;
	}
	public void setCoutdeptid(String coutdeptid) {
		this.coutdeptid = coutdeptid;
	}
	public String getCoutcorpid() {
		return coutcorpid;
	}
	public void setCoutcorpid(String coutcorpid) {
		this.coutcorpid = coutcorpid;
	}
	public String getCoutcbid() {
		return coutcbid;
	}
	public void setCoutcbid(String coutcbid) {
		this.coutcbid = coutcbid;
	}
	public String getCoutwhid() {
		return coutwhid;
	}
	public void setCoutwhid(String coutwhid) {
		this.coutwhid = coutwhid;
	}
	public String getCinwhid() {
		return cinwhid;
	}
	public void setCinwhid(String cinwhid) {
		this.cinwhid = cinwhid;
	}
	public String getCoutpsnid() {
		return coutpsnid;
	}
	public void setCoutpsnid(String coutpsnid) {
		this.coutpsnid = coutpsnid;
	}
	public UFBoolean getBretractflag() {
		return bretractflag;
	}
	public void setBretractflag(UFBoolean bretractflag) {
		this.bretractflag = bretractflag;
	}
	public String getPk_sendtype() {
		return pk_sendtype;
	}
	public void setPk_sendtype(String pk_sendtype) {
		this.pk_sendtype = pk_sendtype;
	}
	public String getVreceiveaddress() {
		return vreceiveaddress;
	}
	public void setVreceiveaddress(String vreceiveaddress) {
		this.vreceiveaddress = vreceiveaddress;
	}
	public String getCcustomerid() {
		return ccustomerid;
	}
	public void setCcustomerid(String ccustomerid) {
		this.ccustomerid = ccustomerid;
	}
	public String getCtakeoutinvid() {
		return ctakeoutinvid;
	}
	public void setCtakeoutinvid(String ctakeoutinvid) {
		this.ctakeoutinvid = ctakeoutinvid;
	}
	public String getCinvbasid() {
		return cinvbasid;
	}
	public void setCinvbasid(String cinvbasid) {
		this.cinvbasid = cinvbasid;
	}
	public String getCprojectphase() {
		return cprojectphase;
	}
	public void setCprojectphase(String cprojectphase) {
		this.cprojectphase = cprojectphase;
	}
	public String getCprojectid() {
		return cprojectid;
	}
	public void setCprojectid(String cprojectid) {
		this.cprojectid = cprojectid;
	}

	public String getCbillid() {
		return cbillid;
	}
	public void setCbillid(String cbillid) {
		this.cbillid = cbillid;
	}
	public String getCbill_bid() {
		return cbill_bid;
	}
	public void setCbill_bid(String cbill_bid) {
		this.cbill_bid = cbill_bid;
	}
	public String getVbatch() {
		return vbatch;
	}
	public void setVbatch(String vbatch) {
		this.vbatch = vbatch;
	}
	public String getVcode() {
		return vcode;
	}
	public void setVcode(String vcode) {
		this.vcode = vcode;
	}
	public String getCrowno() {
		return crowno;
	}
	public void setCrowno(String crowno) {
		this.crowno = crowno;
	}
	public String getCtypecode() {
		return ctypecode;
	}
	public void setCtypecode(String ctypecode) {
		this.ctypecode = ctypecode;
	}
	public String getCastunitid() {
		return castunitid;
	}
	public void setCastunitid(String castunitid) {
		this.castunitid = castunitid;
	}
	public String getCreceieveid() {
		return creceieveid;
	}
	public void setCreceieveid(String creceieveid) {
		this.creceieveid = creceieveid;
	}
	public UFDouble getNchangerate() {
		return nchangerate;
	}
	public void setNchangerate(UFDouble nchangerate) {
		this.nchangerate = nchangerate;
	}
	public String getCtakeoutspaceid() {
		return ctakeoutspaceid;
	}
	public void setCtakeoutspaceid(String ctakeoutspaceid) {
		this.ctakeoutspaceid = ctakeoutspaceid;
	}
	public String getDvalidate() {
		return dvalidate;
	}
	public void setDvalidate(String dvalidate) {
		this.dvalidate = dvalidate;
	}
	public String getCquoteunitid() {
		return cquoteunitid;
	}
	public void setCquoteunitid(String cquoteunitid) {
		this.cquoteunitid = cquoteunitid;
	}
	public UFDouble getNquoteunitrate() {
		return nquoteunitrate;
	}
	public void setNquoteunitrate(UFDouble nquoteunitrate) {
		this.nquoteunitrate = nquoteunitrate;
	}
	public UFBoolean getFlargess() {
		return flargess;
	}
	public void setFlargess(UFBoolean flargess) {
		this.flargess = flargess;
	}
	public String getCvendorid() {
		return cvendorid;
	}
	public void setCvendorid(String cvendorid) {
		this.cvendorid = cvendorid;
	}
	public void setVnote(String vnote) {
		this.vnote = vnote;
	}
	public UFDate getDbilldate() {
		return dbilldate;
	}
	public void setDbilldate(UFDate dbilldate) {
		this.dbilldate = dbilldate;
	}
	
	
	
}
