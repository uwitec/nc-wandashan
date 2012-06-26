package nc.vo.wds.rdcl;

import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.scm.pu.PuPubVO;
/**
 * �շ����
 * @author yf
 * @date 2012-06-26
 * �ֶ���Ϣ ��˾  ������ �������  �Ƿ�ش�erp
 * 2. ֧���Զ������
 * 3. �������Ϊ�� ת��λ���� , ת��λ��� 
 * ������� ,�����˵�����  
 * �����˵���� ,�������(���ش�erp) 
 * ������⣨�ش�erp�������۳���,
 * ���۳��⣨���⣩������

 *
 */
public class RdclVO extends SuperVO {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4515927399519896468L;
	
	public String pk_rdcl;//�շ�����pk
	public String pk_corp;//��˾
	public Integer rdflag;//�����ʶ
	public String rdcode;//����
	public String rdname;//����
	public String pk_frdcl;//�ϼ�pk
	public UFBoolean sealflag;//����־
	public UFBoolean uisreturn;//�Ƿ�ش�erp
	public UFDateTime ts;
	public Integer dr;
	
	public void validate() throws ValidationException {
		if(PuPubVO.getString_TrimZeroLenAsNull(rdcode)==null){
			throw new ValidationException("���벻��Ϊ��");
		}
		if(PuPubVO.getString_TrimZeroLenAsNull(rdname)==null){
			throw new ValidationException("���Ʋ���Ϊ��");
		}
		if(PuPubVO.getString_TrimZeroLenAsNull(pk_corp)==null)
			throw new ValidationException("��˾Ϊ��");
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
