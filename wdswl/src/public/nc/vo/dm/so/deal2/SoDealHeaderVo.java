package nc.vo.dm.so.deal2;

import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.scm.pu.PuPubVO;

/**
 * 
 * @author zhf
 * �ϲ���Ķ���ͷvo
 *
 */

public class SoDealHeaderVo extends SuperVO {
	
//	��Ҫ���ֶ� �ͻ�id  �������ڣ��ϲ�ǰ��������С���ڣ�  �Ƿ����ⰲ�� ������֯ ���۹�˾
	
	private UFDate dbilldate;
	private String ccustomerid;
	private String csalecorpid;
	private String cbodywarehouseid; //�����ֿ�
	
	private UFBoolean bisspecial= UFBoolean.FALSE;
	
	
	public static String[] split_fields = new String[]{"ccustomerid"};
	
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

	public UFBoolean getBisspecial() {
		return bisspecial;
	}

	public void setBisspecial(UFBoolean bisspecial) {
		this.bisspecial = bisspecial;
	}

	@Override
	public String getPKFieldName() {//��ʵ��vo
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getParentPKFieldName() {//��ʵ��vo
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {//��ʵ��vo
		// TODO Auto-generated method stub
		return null;
	}
	
	public void validate() throws ValidationException {
		if(PuPubVO.getString_TrimZeroLenAsNull(getCcustomerid())==null){
			throw new ValidationException("�ͻ�Ϊ��");
		}
		if(PuPubVO.getString_TrimZeroLenAsNull(getCbodywarehouseid())==null){
			throw new ValidationException("�����ֿ�Ϊ��");
		}
	}

}
