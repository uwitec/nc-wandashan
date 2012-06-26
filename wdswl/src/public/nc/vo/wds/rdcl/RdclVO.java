package nc.vo.wds.rdcl;

import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.scm.pu.PuPubVO;
/**
 * 收发类别
 * @author yf
 * @date 2012-06-26
 * 字段信息 公司  类别编码 类别名称  是否回传erp
 * 2. 支持自定义参照
 * 3. 类别内容为： 转货位出库 , 转货位入库 
 * 调拨入库 ,特殊运单出库  
 * 特殊运单入库 ,虚拟出库(不回传erp) 
 * 虚拟入库（回传erp），销售出库,
 * 销售出库（虚拟），其他

 *
 */
public class RdclVO extends SuperVO {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4515927399519896468L;
	
	public String pk_rdcl;//收发类型pk
	public String pk_corp;//公司
	public Integer rdflag;//出入标识
	public String rdcode;//编码
	public String rdname;//名称
	public String pk_frdcl;//上级pk
	public UFBoolean sealflag;//封存标志
	public UFBoolean uisreturn;//是否回传erp
	public UFDateTime ts;
	public Integer dr;
	
	public void validate() throws ValidationException {
		if(PuPubVO.getString_TrimZeroLenAsNull(rdcode)==null){
			throw new ValidationException("编码不能为空");
		}
		if(PuPubVO.getString_TrimZeroLenAsNull(rdname)==null){
			throw new ValidationException("名称不能为空");
		}
		if(PuPubVO.getString_TrimZeroLenAsNull(pk_corp)==null)
			throw new ValidationException("公司为空");
	}
	
	@Override
	public String getPKFieldName() {
		return "pk_rdcl";
	}

	@Override
	public String getParentPKFieldName() {
		return null;
	}

	@Override
	public String getTableName() {
		return "wds_rdcl";
	}

	public String getPk_rdcl() {
		return pk_rdcl;
	}

	public void setPk_rdcl(String pk_rdcl) {
		this.pk_rdcl = pk_rdcl;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public Integer getRdflag() {
		return rdflag;
	}

	public void setRdflag(Integer rdflag) {
		this.rdflag = rdflag;
	}

	public String getRdcode() {
		return rdcode;
	}

	public void setRdcode(String rdcode) {
		this.rdcode = rdcode;
	}

	public String getRdname() {
		return rdname;
	}

	public void setRdname(String rdname) {
		this.rdname = rdname;
	}

	public String getPk_frdcl() {
		return pk_frdcl;
	}

	public void setPk_frdcl(String pk_frdcl) {
		this.pk_frdcl = pk_frdcl;
	}

	public UFBoolean getSealflag() {
		return sealflag;
	}

	public void setSealflag(UFBoolean sealflag) {
		this.sealflag = sealflag;
	}

	public UFBoolean getUisreturn() {
		return uisreturn;
	}

	public void setUisreturn(UFBoolean uisreturn) {
		this.uisreturn = uisreturn;
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

}
