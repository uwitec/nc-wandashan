package nc.vo.hg.to.pub;

import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;

public class StockNumParaVO extends SuperVO {
	
	/**
	 * zhf add  调拨订单 编辑时  使用的 八大量 数据vo
	 */
	private String coutcorpid = null;//调出公司
	private String cincorpid = null;//调入公司
	private String coutcalbodyid = null;//调出组织
	private String cincalbodyid =  null;//调入组织
	private String coutwarehouseid = null;//调出仓库
	private String cinwarehouseid = null;//调入仓库
	private String cinvbasid = null;//存货id
	private String coutinvid = null;//存货管理id
	private String cininvid = null;
	private String cbatchid = null;//批次号
	private UFDate dlogdate = null;//当前自然日期
	private UFDouble noutonhand = null;//调出方现存量
	private UFDouble ninonhand = null;//调入方库存量
	private UFDouble noutallnum = null;//调出方本月已有调拨订单数量  该存货当前月所有调拨订单数量之和
	private UFDouble ninallnum = null;//调入方本月已有调拨订单数量  该存货当前月针对该用户在所有调拨出库单数量之和
	private UFDouble noutnum = null;//调出方已发货数量  该存货当前月所有调拨订单已发货量之和
	private UFDouble ninnum = null;//调入方已出库数量  存货领用单位当月已经出库的数量之和=调入总数量+领用单位起初库存-当前库存
	private UFDouble nfund = null;//可用资金
	private UFDouble nmny = null;//可用限额
	private UFDouble nallfund = null;//总资金
	private UFDouble nallmny = null;//总限额

	private String vdef1 = null;
	private String vdef2 = null;
	private String vdef3 = null;
	private UFDouble ndef1 = null;//调拨订单调出方 行关闭 订单量
	private UFDouble ndef2 = null;//调拨订单调入方行关闭 订单量
	private UFDouble ndef3 = null;
	
	
	public void validation() throws ValidationException{
		if(PuPubVO.getString_TrimZeroLenAsNull(getCoutcorpid())==null)
			throw new ValidationException("调出公司不能为空");
		if(PuPubVO.getString_TrimZeroLenAsNull(getCincorpid())==null)
			throw new ValidationException("调入公司不能为空");
		if(PuPubVO.getString_TrimZeroLenAsNull(getCoutcalbodyid())==null)
			throw new ValidationException("调出组织不能为空");
		if(PuPubVO.getString_TrimZeroLenAsNull(getCincalbodyid())==null)
			throw new ValidationException("调入组织不能为空");
		if(PuPubVO.getString_TrimZeroLenAsNull(getCinvbasid())==null)
			throw new ValidationException("存货为空");
		if(PuPubVO.getString_TrimZeroLenAsNull(getCoutinvid())==null)
			throw new ValidationException("调出存货不能为空");
		if(PuPubVO.getString_TrimZeroLenAsNull(getCininvid())==null)
			throw new ValidationException("调入存货标示为空");
//		if(PuPubVO.getString_TrimZeroLenAsNull(getCoutcorpid())==null)
//			throw new ValidationException("");
//		if(PuPubVO.getString_TrimZeroLenAsNull(getCoutcorpid())==null)
//			throw new ValidationException("");
	}
	
	public UFDouble getNallfund() {
		return nallfund;
	}

	public void setNallfund(UFDouble nallfund) {
		this.nallfund = nallfund;
	}

	public UFDouble getNallmny() {
		return nallmny;
	}

	public void setNallmny(UFDouble nallmny) {
		this.nallmny = nallmny;
	}

	public String getCoutcorpid() {
		return coutcorpid;
	}

	public void setCoutcorpid(String coutcorpid) {
		this.coutcorpid = coutcorpid;
	}

	public String getCincorpid() {
		return cincorpid;
	}

	public void setCincorpid(String cincorpid) {
		this.cincorpid = cincorpid;
	}

	public String getCoutcalbodyid() {
		return coutcalbodyid;
	}

	public void setCoutcalbodyid(String coutcalbodyid) {
		this.coutcalbodyid = coutcalbodyid;
	}

	public String getCincalbodyid() {
		return cincalbodyid;
	}

	public void setCincalbodyid(String cincalbodyid) {
		this.cincalbodyid = cincalbodyid;
	}

	public String getCoutwarehouseid() {
		return coutwarehouseid;
	}

	public void setCoutwarehouseid(String coutwarehouseid) {
		this.coutwarehouseid = coutwarehouseid;
	}

	public String getCinwarehouseid() {
		return cinwarehouseid;
	}

	public void setCinwarehouseid(String cinwarehouseid) {
		this.cinwarehouseid = cinwarehouseid;
	}

	public String getCinvbasid() {
		return cinvbasid;
	}

	public void setCinvbasid(String cinvbasid) {
		this.cinvbasid = cinvbasid;
	}

	

	public String getCoutinvid() {
		return coutinvid;
	}

	public void setCoutinvid(String coutinvid) {
		this.coutinvid = coutinvid;
	}

	public String getCininvid() {
		return cininvid;
	}

	public void setCininvid(String cininvid) {
		this.cininvid = cininvid;
	}

	public String getCbatchid() {
		return cbatchid;
	}

	public void setCbatchid(String cbatchid) {
		this.cbatchid = cbatchid;
	}

	public UFDate getDlogdate() {
		return dlogdate;
	}

	public void setDlogdate(UFDate dlogdate) {
		this.dlogdate = dlogdate;
	}

	public UFDouble getNoutonhand() {
		return noutonhand;
	}

	public void setNoutonhand(UFDouble noutonhand) {
		this.noutonhand = noutonhand;
	}

	public UFDouble getNinonhand() {
		return ninonhand;
	}

	public void setNinonhand(UFDouble ninonhand) {
		this.ninonhand = ninonhand;
	}

	public UFDouble getNoutallnum() {
		return noutallnum;
	}

	public void setNoutallnum(UFDouble noutallnum) {
		this.noutallnum = noutallnum;
	}

	public UFDouble getNinallnum() {
		return ninallnum;
	}

	public void setNinallnum(UFDouble ninallnum) {
		this.ninallnum = ninallnum;
	}

	public UFDouble getNoutnum() {
		return noutnum;
	}

	public void setNoutnum(UFDouble noutnum) {
		this.noutnum = noutnum;
	}

	public UFDouble getNinnum() {
		return ninnum;
	}

	public void setNinnum(UFDouble ninnum) {
		this.ninnum = ninnum;
	}

	public UFDouble getNfund() {
		return nfund;
	}

	public void setNfund(UFDouble nfund) {
		this.nfund = nfund;
	}

	public UFDouble getNmny() {
		return nmny;
	}

	public void setNmny(UFDouble nmny) {
		this.nmny = nmny;
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

	public UFDouble getNdef3() {
		return ndef3;
	}

	public void setNdef3(UFDouble ndef3) {
		this.ndef3 = ndef3;
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

}
