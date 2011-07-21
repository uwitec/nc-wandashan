package nc.vo.zb.bidding;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;

/**
 * 
 * @author zhf
 * @说明：（鹤岗矿业）参照采购计划划分标段 专用的 供应商供货历史价格vo
 * 2011-5-18下午02:04:32
 */

public class HistoryPriceVO extends SuperVO {
//	合同头id  合同体id  合同号  合同日期  合同价  品种   供应商  注意合同的版本和退货标志
	
//	取自采购订单头得字段
	private String corderid;
	private String vordercode;//合同号
	private UFDate dorderdate;//合同日期
	private String cvendormangid;//供应商管理id               
	private String cvendorbaseid;//供应商基础id
	private UFDate dauditdate;//合同审批日期
	
//	表体
	private String corderb_id;
	private String cmangid;//存货管理id           
	private String cbaseid;//存货基础id 
	private UFDouble noriginalcurprice;// 原币无税单价  
//	private UFDouble nprice;// 本币无税单价  
	
	
	
	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "corder_bid";
	}

	public String getCorderid() {
		return corderid;
	}

	public void setCorderid(String corderid) {
		this.corderid = corderid;
	}

	public String getVordercode() {
		return vordercode;
	}

	public void setVordercode(String vordercode) {
		this.vordercode = vordercode;
	}

	public UFDate getDorderdate() {
		return dorderdate;
	}

	public void setDorderdate(UFDate dorderdate) {
		this.dorderdate = dorderdate;
	}

	public String getCvendormangid() {
		return cvendormangid;
	}

	public void setCvendormangid(String cvendormangid) {
		this.cvendormangid = cvendormangid;
	}

	public String getCvendorbaseid() {
		return cvendorbaseid;
	}

	public void setCvendorbaseid(String cvendorbaseid) {
		this.cvendorbaseid = cvendorbaseid;
	}

	public UFDate getDauditdate() {
		return dauditdate;
	}

	public void setDauditdate(UFDate dauditdate) {
		this.dauditdate = dauditdate;
	}

	public String getCorderb_id() {
		return corderb_id;
	}

	public void setCorderb_id(String corderb_id) {
		this.corderb_id = corderb_id;
	}

	public String getCmangid() {
		return cmangid;
	}

	public void setCmangid(String cmangid) {
		this.cmangid = cmangid;
	}

	public String getCbaseid() {
		return cbaseid;
	}

	public void setCbaseid(String cbaseid) {
		this.cbaseid = cbaseid;
	}

	public UFDouble getNoriginalcurprice() {
		return noriginalcurprice;
	}

	public void setNoriginalcurprice(UFDouble noriginalcurprice) {
		this.noriginalcurprice = noriginalcurprice;
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return "corderid";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return getTableName2();
	}
	
	public static String getTableName2(){
		return " po_order h  inner join po_order_b b on h.corderid = b.corderid ";
	}
	
	public  String[] getAttributeNames(){
		return getAttributeNames2(); 
	}
	
	public static String buildSelectSql(String whereSql,boolean isdistinct){
		StringBuffer strb = new StringBuffer();
		strb.append("select ");
		if(isdistinct)
			strb.append(" distinct ");
		String[] names = getAttributeNames2();
		for(String name:names){
			strb.append(name+",");
		}
		strb.append(" 'aaa' ");
		strb.append(" from ");
		strb.append(getTableName2());
		strb.append(" where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 ");
		if(PuPubVO.getString_TrimZeroLenAsNull(whereSql)!=null)
			strb.append(" and "+whereSql);
	
		return strb.toString();
	}
	
	/**
	 * @return java.lang.String[]
	 */
	public static String[] getAttributeNames2() {
		return new String[]{
				"h.corderid"
				,"h.vordercode"//合同号
				,"h.dorderdate"//合同日期
				,"h.cvendormangid"//供应商管理id               
				,"h.cvendorbaseid"//供应商基础id
				,"h.dauditdate"//合同审批日期
				
//				表体
				,"b.corder_bid"
				,"b.cmangid"//存货管理id           
				,"b.cbaseid"//存货基础id 
				,"b.noriginalcurprice"// 原币无税单价  
		};
	}

}
