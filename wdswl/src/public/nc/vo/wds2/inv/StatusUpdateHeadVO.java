package nc.vo.wds2.inv;

import nc.vo.zmpub.pub.bill.HYHeadSuperVO;

public class StatusUpdateHeadVO extends HYHeadSuperVO {
	
	public String cbillid;//单据id
	public String cwarehouseid;//仓库id
	public String ccargdocid;//货位
	

	public String getCcargdocid() {
		return ccargdocid;
	}

	public void setCcargdocid(String ccargdocid) {
		this.ccargdocid = ccargdocid;
	}

	public String getCbillid() {
		return cbillid;
	}

	public void setCbillid(String cbillid) {
		this.cbillid = cbillid;
	}

	public String getCwarehouseid() {
		return cwarehouseid;
	}

	public void setCwarehouseid(String cwarehouseid) {
		this.cwarehouseid = cwarehouseid;
	}

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "cbillid";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "wds2_statusupdate";
	}

}
