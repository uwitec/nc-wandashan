package nc.vo.wds.xn;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

/**
 *
 * @author zhf  2011-07-04
 * 虚拟托盘绑定关系表
 *
 */

public class XnRelationVO extends SuperVO {
	private String cxnrelationid;//ID
	private String pk_corp;//公司
	private String ccalbodyid;//组织
	private String cwarehousid;//仓库
	private String cxncargdocid;//虚拟托盘所在货位
	private String cxntrayid;//虚拟托盘
	
	private String ccargdocid;//绑定的实际托盘所在货位
	private String ctrayid;//绑定的实际托盘
	
	private String pk_invbasdoc;
	private String pk_invmandoc;
	private String vbatchcode;//批次号
	private UFDouble  nnum;//数量--------------------------未维护  请使用辅数量zhf
	private UFDouble nassisnum;//辅数量
	
	private String chid;//关联库存主表id
	private String cbid;//关联库存子表id
	private String cbbid;//关联库存子子表id
	
//	预留字段
	
	private String vdef1;
	private String vdef2;
	private UFBoolean bdef1;
	private UFBoolean bdef2;
	private UFDouble ndef1;
	private UFDouble ndef2;	

	public String getCxnrelationid() {
		return cxnrelationid;
	}

	public void setCxnrelationid(String cxnrelationid) {
		this.cxnrelationid = cxnrelationid;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public String getCcalbodyid() {
		return ccalbodyid;
	}

	public void setCcalbodyid(String ccalbodyid) {
		this.ccalbodyid = ccalbodyid;
	}

	public String getCwarehousid() {
		return cwarehousid;
	}

	public void setCwarehousid(String cwarehousid) {
		this.cwarehousid = cwarehousid;
	}

	public String getCxncargdocid() {
		return cxncargdocid;
	}

	public void setCxncargdocid(String cxncargdocid) {
		this.cxncargdocid = cxncargdocid;
	}

	public String getCxntrayid() {
		return cxntrayid;
	}

	public void setCxntrayid(String cxntrayid) {
		this.cxntrayid = cxntrayid;
	}

	public String getCcargdocid() {
		return ccargdocid;
	}

	public void setCcargdocid(String ccargdocid) {
		this.ccargdocid = ccargdocid;
	}

	public String getCtrayid() {
		return ctrayid;
	}

	public void setCtrayid(String ctrayid) {
		this.ctrayid = ctrayid;
	}

	public String getPk_invbasdoc() {
		return pk_invbasdoc;
	}

	public void setPk_invbasdoc(String pk_invbasdoc) {
		this.pk_invbasdoc = pk_invbasdoc;
	}

	public String getPk_invmandoc() {
		return pk_invmandoc;
	}

	public void setPk_invmandoc(String pk_invmandoc) {
		this.pk_invmandoc = pk_invmandoc;
	}

	public String getVbatchcode() {
		return vbatchcode;
	}

	public void setVbatchcode(String vbatchcode) {
		this.vbatchcode = vbatchcode;
	}

	public UFDouble getNnum() {
		return nnum;
	}

	public void setNnum(UFDouble nnum) {
		this.nnum = nnum;
	}

	public UFDouble getNassisnum() {
		return nassisnum;
	}

	public void setNassisnum(UFDouble nassisnum) {
		this.nassisnum = nassisnum;
	}

	public String getChid() {
		return chid;
	}

	public void setChid(String chid) {
		this.chid = chid;
	}

	public String getCbid() {
		return cbid;
	}

	public void setCbid(String cbid) {
		this.cbid = cbid;
	}

	public String getCbbid() {
		return cbbid;
	}

	public void setCbbid(String cbbid) {
		this.cbbid = cbbid;
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

	public UFBoolean getBdef1() {
		return bdef1;
	}

	public void setBdef1(UFBoolean bdef1) {
		this.bdef1 = bdef1;
	}

	public UFBoolean getBdef2() {
		return bdef2;
	}

	public void setBdef2(UFBoolean bdef2) {
		this.bdef2 = bdef2;
	}

	public UFDouble getNdef1() {
		return ndef1;
	}

	public void setNdef1(UFDouble ndef1) {
		this.ndef1 = ndef1;
	}

	public UFDouble getNdef2() {
		return ndef2;
	}

	public void setNdef2(UFDouble ndef2) {
		this.ndef2 = ndef2;
	}

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "cxnrelationid";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "wds_xnrelation";
	}

}
