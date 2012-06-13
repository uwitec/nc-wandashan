
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * �Զ���ĵ������ݲ�����
 * ����̳�DefaultDataFinder2�����
 * @author mlr
 */
public class BillDataFinder2 extends DefaultDataFinder2{
	
	private String currentBillType = null;//�趨��ǰ����
	
	private String srcBillType = null;//�趨��Դ����
	/**
	 * ������Դ�������ʹ��� ������Դ���ݵ�  ����id ���ݺ� ��sql
	 * billtype Ϊ��Դ��������
	 */
	@Override
	protected String createSQL1(String billType) {
		String sql = null;		
		if(WdsWlPubConst.WDS3.equals(billType)){//���˶���
			sql = " select distinct h1.PK_SENDORDER,h1.VBILLNO from wds_sendorder h1,wds_sendorder_b b1 where h1.PK_SENDORDER = b1.PK_SENDORDER and nvl(h1.dr,0) = 0 and nvl(b1.dr,0) = 0 and b1.CSOURCEBILLHID = ? ";
		}else if(WdsWlPubConst.BILLTYPE_OTHER_OUT.equals(billType)||WdsWlPubConst.BILLTYPE_SALE_OUT.equals(billType)){//���۳���
			sql = "  select distinct h1.GENERAL_PK,h1.VBILLCODE from tb_outgeneral_h h1,tb_outgeneral_b b1 where h1.GENERAL_PK = b1.GENERAL_PK and nvl(h1.dr,0) = 0 and nvl(b1.dr,0) = 0 and b1.CSOURCEBILLHID = ? ";
	   }else{
			super.createSQL1(billType);
		}
		return sql;
	}
	/**
	 * ���ݵ�ǰ�������� ����������Դ���� �ĵ������ͣ�����id,���ݺ� �Ĳ�ѯsql 
	 * �÷�������ע�������Դ���ݵ�sql
	 * billType Ϊ��ǰ�ĵ�������
	 */
	@Override
	protected String createSQL(String billType) {
		String sql = null;
		if(WdsWlPubConst.WDS3.equals(billType)){//���˶���
			sql = " select distinct ss.CSOURCETYPE,ss.CSOURCEBILLHID,ss.vsourcebillcode from wds_sendorder_b ss  where ss.PK_SENDORDER = ? and nvl(ss.dr,0) = 0 ";
		}else if(WdsWlPubConst.BILLTYPE_OTHER_OUT.equals(billType)||WdsWlPubConst.BILLTYPE_SALE_OUT.equals(billType)){//���۳���
			sql = " select distinct zz.CSOURCETYPE,zz.CSOURCEBILLHID,zz.vsourcebillcode from tb_outgeneral_b  zz where zz.GENERAL_PK = ?  and nvl(zz.dr,0) = 0 ";
		}else{
			super.createSQL(billType);
		}
		return sql;
	}	
	public String getCurrentBillType() {
		return currentBillType;
	}	
	public void setCurrentBillType(String currentBillType) {
		this.currentBillType = currentBillType;
	}
	
	public String getSrcBillType() {
		return srcBillType;
	}
	
	public void setSrcBillType(String srcBillType) {
		this.srcBillType = srcBillType;
	}
}
