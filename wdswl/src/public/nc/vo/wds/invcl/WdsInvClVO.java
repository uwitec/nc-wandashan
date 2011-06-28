package nc.vo.wds.invcl;

import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;

public class WdsInvClVO extends SuperVO {

	private String pk_invcl;
	private String vinvclcode;
	private String vinvclname;
	private Integer invclasslev;//编码几次
	private String pk_father;//父节点编码
	private String pk_corp;
	private String ccalbodyid;//库存组织  预留字段
	private String cwarehouseid;//隶属仓库   预留字段
	private UFDateTime ts ;
	private Integer dr;
	private UFBoolean fisclose;//是否封存

	private String coperator;
	private UFDate dmakedate;
	private String cmodify;
	private UFDate dmodifydate;

	private UFDouble ndef1;
	private UFDouble ndef2;
	private String vdef1;
	private String vdef2;
	private UFBoolean fdef1;
	private UFBoolean fdef2;

	public void validate() throws ValidationException {
		if(PuPubVO.getString_TrimZeroLenAsNull(vinvclcode)==null){
			throw new ValidationException("分类编码不能为空");
		}
		if(PuPubVO.getString_TrimZeroLenAsNull(vinvclname)==null){
			throw new ValidationException("分类名称不能为空");
		}
		if(PuPubVO.getString_TrimZeroLenAsNull(pk_father)==null)
			throw new ValidationException("父类为空");
		if(PuPubVO.getString_TrimZeroLenAsNull(pk_corp)==null)
			throw new ValidationException("公司为空");
	}


	public String getCcalbodyid() {
		return ccalbodyid;
	}


	public void setCcalbodyid(String ccalbodyid) {
		this.ccalbodyid = ccalbodyid;
	}


	public String getCwarehouseid() {
		return cwarehouseid;
	}


	public void setCwarehouseid(String cwarehouseid) {
		this.cwarehouseid = cwarehouseid;
	}


	public String getCoperator() {
		return coperator;
	}

	public void setCoperator(String coperator) {
		this.coperator = coperator;
	}

	public UFDate getDmakedate() {
		return dmakedate;
	}

	public void setDmakedate(UFDate dmakedate) {
		this.dmakedate = dmakedate;
	}

	public String getCmodify() {
		return cmodify;
	}

	public void setCmodify(String cmodify) {
		this.cmodify = cmodify;
	}

	public UFDate getDmodifydate() {
		return dmodifydate;
	}

	public void setDmodifydate(UFDate dmodifydate) {
		this.dmodifydate = dmodifydate;
	}

	public String getPk_invcl() {
		return pk_invcl;
	}

	public void setPk_invcl(String pk_invcl) {
		this.pk_invcl = pk_invcl;
	}

	public String getVinvclcode() {
		return vinvclcode;
	}

	public void setVinvclcode(String vinvclcode) {
		this.vinvclcode = vinvclcode;
	}

	public String getVinvclname() {
		return vinvclname;
	}

	public void setVinvclname(String vinvclname) {
		this.vinvclname = vinvclname;
	}

	public Integer getInvclasslev() {
		return invclasslev;
	}

	public void setInvclasslev(Integer invclasslev) {
		this.invclasslev = invclasslev;
	}

	public String getPk_father() {
		return pk_father;
	}

	public void setPk_father(String pk_father) {
		this.pk_father = pk_father;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
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

	public UFBoolean getFisclose() {
		return fisclose;
	}

	public void setFisclose(UFBoolean fisclose) {
		this.fisclose = fisclose;
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

	public UFBoolean getFdef1() {
		return fdef1;
	}

	public void setFdef1(UFBoolean fdef1) {
		this.fdef1 = fdef1;
	}

	public UFBoolean getFdef2() {
		return fdef2;
	}

	public void setFdef2(UFBoolean fdef2) {
		this.fdef2 = fdef2;
	}

	public static String getBill_type() {
		return bill_type;
	}

	public static void setBill_type(String bill_type) {
		WdsInvClVO.bill_type = bill_type;
	}

	public static String bill_type = "invcl";
	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "pk_invcl";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "wds_invcl";
	}

}
