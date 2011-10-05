package nc.vo.ic.other.out;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFTime;

public class TbOutgeneralB2VO extends SuperVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8909999581964039676L;

	public String general_pk;//表头主键
	public String general_b2_pk;//主键
	public String vmemo;//备注
	public String pk_wds_teamdoc_h;//班组id
	public UFDouble nloadprice;
	public UFDateTime ts;
	public Integer dr;
	
	public String reserve1;
	public String reserve2;
	public String reserve3;
	public String reserve4;
	public String reserve5;
	public UFDouble reserve6;
	public UFDouble reserve7;
	public UFDouble reserve8;
	public UFDouble reserve9;
	public UFDouble reserve10;
	public String reserve11;
	public String reserve12;
	public String reserve13;
	public UFBoolean reserve14;
	public UFBoolean reserve15;
	public UFBoolean reserve16;
	
	public String vdef1;
	public String vdef2;
	public String vdef3;
	public String vdef4;
	public String vdef5;
	public String vdef6;
	public String vdef7;
	public String vdef8;
	public String vdef9;
	public String vdef10;
	@Override
	public String getPKFieldName() {
		return "general_b2_pk";
	}

	@Override
	public String getParentPKFieldName() {
		return "general_pk";
	}

	@Override
	public String getTableName() {
		return "tb_outgeneral_b2";
	}

	@Override
	public String getEntityName() {
		// TODO Auto-generated method stub
		return "tb_outgeneral_b2";
	}

	public String getGeneral_pk() {
		return general_pk;
	}

	public void setGeneral_pk(String general_pk) {
		this.general_pk = general_pk;
	}

	public String getGeneral_b2_pk() {
		return general_b2_pk;
	}

	public void setGeneral_b2_pk(String general_b2_pk) {
		this.general_b2_pk = general_b2_pk;
	}

	public String getVmemo() {
		return vmemo;
	}

	public void setVmemo(String vmemo) {
		this.vmemo = vmemo;
	}

	public String getPk_wds_teamdoc_h() {
		return pk_wds_teamdoc_h;
	}

	public void setPk_wds_teamdoc_h(String pk_wds_teamdoc_h) {
		this.pk_wds_teamdoc_h = pk_wds_teamdoc_h;
	}

	public UFDouble getNloadprice() {
		return nloadprice;
	}

	public void setNloadprice(UFDouble nloadprice) {
		this.nloadprice = nloadprice;
	}


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

	public String getReserve1() {
		return reserve1;
	}

	public void setReserve1(String reserve1) {
		this.reserve1 = reserve1;
	}

	public String getReserve2() {
		return reserve2;
	}

	public void setReserve2(String reserve2) {
		this.reserve2 = reserve2;
	}

	public String getReserve3() {
		return reserve3;
	}

	public void setReserve3(String reserve3) {
		this.reserve3 = reserve3;
	}

	public String getReserve4() {
		return reserve4;
	}

	public void setReserve4(String reserve4) {
		this.reserve4 = reserve4;
	}

	public String getReserve5() {
		return reserve5;
	}

	public void setReserve5(String reserve5) {
		this.reserve5 = reserve5;
	}

	public UFDouble getReserve6() {
		return reserve6;
	}

	public void setReserve6(UFDouble reserve6) {
		this.reserve6 = reserve6;
	}

	public UFDouble getReserve7() {
		return reserve7;
	}

	public void setReserve7(UFDouble reserve7) {
		this.reserve7 = reserve7;
	}

	public UFDouble getReserve8() {
		return reserve8;
	}

	public void setReserve8(UFDouble reserve8) {
		this.reserve8 = reserve8;
	}

	public UFDouble getReserve9() {
		return reserve9;
	}

	public void setReserve9(UFDouble reserve9) {
		this.reserve9 = reserve9;
	}

	public UFDouble getReserve10() {
		return reserve10;
	}

	public void setReserve10(UFDouble reserve10) {
		this.reserve10 = reserve10;
	}

	public String getReserve11() {
		return reserve11;
	}

	public void setReserve11(String reserve11) {
		this.reserve11 = reserve11;
	}

	public String getReserve12() {
		return reserve12;
	}

	public void setReserve12(String reserve12) {
		this.reserve12 = reserve12;
	}

	public String getReserve13() {
		return reserve13;
	}

	public void setReserve13(String reserve13) {
		this.reserve13 = reserve13;
	}

	public UFBoolean getReserve14() {
		return reserve14;
	}

	public void setReserve14(UFBoolean reserve14) {
		this.reserve14 = reserve14;
	}

	public UFBoolean getReserve15() {
		return reserve15;
	}

	public void setReserve15(UFBoolean reserve15) {
		this.reserve15 = reserve15;
	}

	public UFBoolean getReserve16() {
		return reserve16;
	}

	public void setReserve16(UFBoolean reserve16) {
		this.reserve16 = reserve16;
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
	

}
