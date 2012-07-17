package nc.vo.wds.load.account;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

public class LoadpirceTVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6007948487893426938L;

	public String pk_loadprice_b2;// 父表主键
	public String pk_loadprice_t;// 主键
	public UFDouble nloadprice;// 装卸费
	public String pk_wds_teamdoc_b;// 人员主键
	public Integer dr;
	public UFDateTime ts;

	public UFDateTime getTs() {
		return ts;
	}

	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public String getPk_loadprice_t() {
		return pk_loadprice_t;
	}

	public void setPk_loadprice_t(String pk_loadprice_t) {
		this.pk_loadprice_t = pk_loadprice_t;
	}

	public String getPk_wds_teamdoc_b() {
		return pk_wds_teamdoc_b;
	}

	public void setPk_wds_teamdoc_b(String pk_wds_teamdoc_b) {
		this.pk_wds_teamdoc_b = pk_wds_teamdoc_b;
	}

	public String getPk_loadprice_b2() {
		return pk_loadprice_b2;
	}

	public void setPk_loadprice_b2(String pk_loadprice_b2) {
		this.pk_loadprice_b2 = pk_loadprice_b2;
	}

	public UFDouble getNloadprice() {
		return nloadprice;
	}

	public void setNloadprice(UFDouble nloadprice) {
		this.nloadprice = nloadprice;
	}

	@Override
	public String getPKFieldName() {
		return "pk_loadprice_t";
	}

	@Override
	public String getParentPKFieldName() {
		return "pk_loadprice_b2";
	}

	@Override
	public String getTableName() {
		return "wds_loadprice_t";
	}

	@Override
	public String getEntityName() {
		return "wds_loadprice_t";
	}

}
