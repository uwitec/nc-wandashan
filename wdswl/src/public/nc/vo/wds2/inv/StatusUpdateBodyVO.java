package nc.vo.wds2.inv;

import nc.vo.pub.lang.UFDouble;
import nc.vo.zmpub.pub.bill.HYChildSuperVO;

public class StatusUpdateBodyVO extends HYChildSuperVO {
	
	public String cbillid;
	public String cbill_bid;
	public String cinvbasid;
	public String cinvmanid;
	public String vbatchcode;
	public String cinvstatusid;//库存状态档案id
	public String cinvstatusid2;//库存状态档案id
	public UFDouble nnum = null;
	public UFDouble nassnum = null;
	public String cunitid;//主计量单位
	public String cassunitid;//辅计量单位
	
	public String vdef1; //入库日期
	public static String[] split_keys = new String[]{"cinvmanid","vbatchcode","cinvstatusid","cinvstatusid2"};
	

	public String getVdef1() {
		return vdef1;
	}

	public void setVdef1(String vdef1) {
		this.vdef1 = vdef1;
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

	public String getCinvbasid() {
		return cinvbasid;
	}

	public void setCinvbasid(String cinvbasid) {
		this.cinvbasid = cinvbasid;
	}

	public String getCinvmanid() {
		return cinvmanid;
	}

	public void setCinvmanid(String cinvmanid) {
		this.cinvmanid = cinvmanid;
	}

	public String getVbatchcode() {
		return vbatchcode;
	}

	public void setVbatchcode(String vbatchcode) {
		this.vbatchcode = vbatchcode;
	}

	public String getCinvstatusid() {
		return cinvstatusid;
	}

	public void setCinvstatusid(String cinvstatusid) {
		this.cinvstatusid = cinvstatusid;
	}

	public String getCinvstatusid2() {
		return cinvstatusid2;
	}

	public void setCinvstatusid2(String cinvstatusid2) {
		this.cinvstatusid2 = cinvstatusid2;
	}

	public UFDouble getNnum() {
		return nnum;
	}

	public void setNnum(UFDouble nnum) {
		this.nnum = nnum;
	}

	public UFDouble getNassnum() {
		return nassnum;
	}

	public void setNassnum(UFDouble nassnum) {
		this.nassnum = nassnum;
	}

	public String getCunitid() {
		return cunitid;
	}

	public void setCunitid(String cunitid) {
		this.cunitid = cunitid;
	}

	public String getCassunitid() {
		return cassunitid;
	}

	public void setCassunitid(String cassunitid) {
		this.cassunitid = cassunitid;
	}

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "cbill_bid";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return "cbillid";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "wds2_statusupdate_b";
	}

}
