package nc.vo.dm.so.deal2;

import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;

/**
 * 
 * @author zhf
 * �ϲ���Ķ���ͷvo
 *
 */

public class SoDealHeaderVo extends SuperVO {
	
//	��Ҫ���ֶ� �ͻ�id  �������ڣ��ϲ�ǰ��������С���ڣ�  �Ƿ����ⰲ�� ������֯ ���۹�˾
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5983083411953222727L;
	private UFDate dbilldate;
	private String ccustomerid;
	private String csalecorpid;
	private String cbodywarehouseid; //�����ֿ�
	
	private UFDouble nminnum;//�ͻ���ͷ�����
	
//	private UFBoolean bisspecial= UFBoolean.FALSE;//�Ƿ����ⰲ��
	private UFBoolean isonsell = UFBoolean.FALSE;//�Ƿ����ⰲ��
	
	public static String[] split_fields = new String[]{"ccustomerid"};
	
	
	public UFDouble getNminnum() {
		return nminnum;
	}

	public void setNminnum(UFDouble nminnum) {
		this.nminnum = nminnum;
	}

	public String getCbodywarehouseid() {
		return cbodywarehouseid;
	}

	public void setCbodywarehouseid(String cbodywarehouseid) {
		this.cbodywarehouseid = cbodywarehouseid;
	}

	public UFDate getDbilldate() {
		return dbilldate;
	}

	public void setDbilldate(UFDate dbilldate) {
		this.dbilldate = dbilldate;
	}

	public String getCcustomerid() {
		return ccustomerid;
	}

	public void setCcustomerid(String ccustomerid) {
		this.ccustomerid = ccustomerid;
	}

	public String getCsalecorpid() {
		return csalecorpid;
	}

	public void setCsalecorpid(String csalecorpid) {
		this.csalecorpid = csalecorpid;
	}

	
	public UFBoolean getIsonsell() {
		return isonsell;
	}

	public void setIsonsell(UFBoolean isonsell) {
		this.isonsell = isonsell;
	}

	
	public void validate() throws ValidationException {
		if(PuPubVO.getString_TrimZeroLenAsNull(getCcustomerid())==null){
			throw new ValidationException("�ͻ�Ϊ��");
		}
		if(PuPubVO.getString_TrimZeroLenAsNull(getCbodywarehouseid())==null){
			throw new ValidationException("�����ֿ�Ϊ��");
		}
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
