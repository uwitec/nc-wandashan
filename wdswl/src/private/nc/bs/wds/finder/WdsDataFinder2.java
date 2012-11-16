package nc.bs.wds.finder;
import nc.vo.wl.pub.Wds2WlPubConst;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * DataFinder
 * @author mlr
 */
public class WdsDataFinder2 extends DefaultDataFinder2{
	
	private String currentBillType = null;//�趨��ǰ����
	
	private String srcBillType = null;//�趨��Դ����
	//��������ע����
	@Override
	protected String createSQL1(String billType) {//���billtype����Ϊ���ε�������
		String sql = null;
		//�������ε���ID����ѯbilltype���ڵ��ݵ���������˾�����ݺ�
		if(WdsWlPubConst.WDS3.equals(billType)){//���˶���
			sql = " select distinct h1.PK_SENDORDER,h1.VBILLNO from wds_sendorder h1,wds_sendorder_b b1 where h1.PK_SENDORDER = b1.PK_SENDORDER and nvl(h1.dr,0) = 0 and nvl(b1.dr,0) = 0 and b1.CSOURCEBILLHID = ? ";
		}else if(WdsWlPubConst.BILLTYPE_OTHER_OUT.equals(billType)||//�������ⵥ
				WdsWlPubConst.BILLTYPE_SALE_OUT.equals(billType) || WdsWlPubConst.BILLTYPE_ALLO_OUT.equals(billType)){
			sql = "  select distinct h1.GENERAL_PK,h1.VBILLCODE from tb_outgeneral_h h1,tb_outgeneral_b b1 where h1.GENERAL_PK = b1.GENERAL_PK and nvl(h1.dr,0) = 0 and nvl(b1.dr,0) = 0 and b1.CSOURCEBILLHID = ? ";
		}else if(WdsWlPubConst.WDS5.equals(billType)){//�����˵�
			sql = "  select distinct h1.PK_SOORDER,h1.VBILLNO from WDS_SOORDER h1 ,WDS_SOORDER_B b1 where h1.PK_SOORDER = b1.PK_SOORDER and nvl(h1.dr,0) = 0 and nvl(b1.dr,0) = 0 and b1.CSOURCEBILLHID = ? ";
		}else if(WdsWlPubConst.BILLTYPE_OTHER_IN.equals(billType)){//�������
			sql = " select distinct h1.geh_pk ,h1.geh_billcode from tb_general_h h1 ,tb_general_b b1 where h1.geh_pk = b1.geh_pk and nvl(h1.dr,0) = 0 and nvl(b1.dr,0) = 0 and b1.csourcebillhid = ? ";
		}else if(WdsWlPubConst.WDSP.equals(billType)){//wds�������ᴫ
			sql = " select distinct h1.pk_wds_writeback4Y_h, h1.vbillno from wds_writeback4y_h h1, wds_writeback4y_b2 b1 where h1.pk_wds_writeback4Y_h = b1.pk_wds_writeback4y_h and nvl(h1.dr, 0) = 0 and nvl(b1.dr, 0) = 0 and b1.csourcebillhid = ?  ";
		}else if(WdsWlPubConst.BILLTYPE_ALLO_IN.equals(billType)){//����������ⵥ
			sql = " select distinct h1.geh_pk, h1.geh_billcode from tb_general_h h1, tb_general_b b1 where h1.geh_pk = b1.geh_pk  and nvl(h1.dr, 0) = 0 and nvl(b1.dr, 0) = 0 and b1.gylbillhid = ? ";
		}else if(WdsWlPubConst.WDSF.equals(billType)){//WDSF װж��
			sql = "select distinct h1.pk_loadprice, h1.vbillno from wds_loadprice_h h1, wds_loadprice_b1 b1 where h1.pk_loadprice = b1.pk_loadprice  and nvl(h1.dr, 0) = 0 and nvl(b1.dr, 0) = 0 and b1.csourcebillhid = ? ";
		}else if(WdsWlPubConst.WDSO.equals(billType) || WdsWlPubConst.WDSX.equals(billType)){//WDSO ���۳���ش���
			sql = " select distinct h1.pk_wds_writeback4c_h, h1.vbillno from wds_writeback4c_h h1, wds_writeback4c_b2 b1 where h1.pk_wds_writeback4c_h = b1.pk_wds_writeback4c_h and nvl(h1.dr, 0) = 0 and nvl(b1.dr, 0) = 0 and b1.csourcebillhid = ?  ";
		}else if("4C".equals(billType)){//��Ӧ�����۳���
			sql = " select distinct h1.cgeneralhid,h1.vbillcode from ic_general_h h1,ic_general_b b1 where h1.cgeneralhid = b1.cgeneralhid and nvl(h1.dr,0) = 0 and nvl(b1.dr,0) = 0 and b1."+WdsWlPubConst.csourcehid_wds+" = ?  ";
		}else if(WdsWlPubConst.GYL4E.equals(billType) || WdsWlPubConst.GYL4Y.equals(billType)){//��Ӧ��������� ��������
			sql = " select distinct h1.cgeneralhid,h1.vbillcode from ic_general_h h1,ic_general_b b1 where h1.cgeneralhid = b1.cgeneralhid and nvl(h1.dr,0) = 0 and nvl(b1.dr,0) = 0 and b1."+WdsWlPubConst.csourcehid_wds+" = ?  ";
		}else if(WdsWlPubConst.GYL4I.equals(billType)){//��Ӧ����������
			sql = " select distinct h1.cgeneralhid,h1.vbillcode from ic_general_h h1,ic_general_b b1 where h1.cgeneralhid = b1.cgeneralhid and nvl(h1.dr,0) = 0 and nvl(b1.dr,0) = 0 and b1."+WdsWlPubConst.csourcehid_wds+" = ?  ";
		}else if(WdsWlPubConst.GYL4A.equals(billType)){//��Ӧ���������
			sql = " select distinct h1.cgeneralhid,h1.vbillcode from ic_general_h h1,ic_general_b b1 where h1.cgeneralhid = b1.cgeneralhid and nvl(h1.dr,0) = 0 and nvl(b1.dr,0) = 0 and b1."+WdsWlPubConst.csourcehid_wds+" = ?  ";
		}else if(Wds2WlPubConst.billtype_alloinsendorder.equals(billType)){//�����˵�
			sql = " select distinct h1.pk_sendorder,h1.vbillno from wds_sendorder h1,wds_sendorder_b b1 where h1.pk_sendorder = b1.pk_sendorder and nvl(h1.dr,0) = 0 and nvl(b1.dr,0) = 0 and b1.csourcebillhid = ?  ";
		}else if(WdsWlPubConst.WDSG.equals(billType)){//�����˵�
			sql = " select distinct h1.PK_SENDORDER,h1.VBILLNO from wds_sendorder h1,wds_sendorder_b b1 where h1.PK_SENDORDER = b1.PK_SENDORDER and nvl(h1.dr,0) = 0 and nvl(b1.dr,0) = 0 and b1.CSOURCEBILLHID = ? ";	
		}
		else{
			super.createSQL1(billType);
		}
		return sql;
	}
	@Override
	protected String createSQL(String billType) {//���billtype����Ϊ��ǰ��������
		//��������Ϊ������billtype���ڵ���ID����ѯ��ǰ���ݳ��� ��Դ�������͡���Դ����ID����
		String sql = null;
		if(WdsWlPubConst.WDS3.equals(billType)|| WdsWlPubConst.WDSG.equals(billType)){//���˶��� �����˵�
			sql = " select distinct ss.CSOURCETYPE,ss.CSOURCEBILLHID,ss.vsourcebillcode from wds_sendorder_b ss  where ss.PK_SENDORDER = ? and nvl(ss.dr,0) = 0 ";
		}else if(WdsWlPubConst.BILLTYPE_OTHER_OUT.equals(billType)||//�������ⵥ
				WdsWlPubConst.BILLTYPE_SALE_OUT.equals(billType)|| WdsWlPubConst.BILLTYPE_ALLO_OUT.equals(billType)){//���۳��� ��������
			sql = " select distinct zz.CSOURCETYPE,zz.CSOURCEBILLHID,zz.vsourcebillcode from tb_outgeneral_b  zz where zz.GENERAL_PK = ?  and nvl(zz.dr,0) = 0 ";
		}else if(WdsWlPubConst.WDS5.equals(billType)){ //�����˵�//30
			sql="  select distinct zz.CSOURCETYPE,zz.CSOURCEBILLHID, zz.vsourcebillcode from WDS_SOORDER_b zz where zz.PK_SOORDER = ? and nvl(zz.dr,0) = 0 ";
		}else if(WdsWlPubConst.WDS1.equals(billType)//���˼ƻ�¼��
				|| WdsWlPubConst.WDSC.equals(billType)
				){//�ɹ�ȡ��
			sql = null;
		}else if(WdsWlPubConst.BILLTYPE_OTHER_IN.equals(billType)){//�������
			sql = " select distinct zz.csourcetype,zz.csourcebillhid,zz.vsourcebillcode from  tb_general_b zz where zz.geh_pk = ? and nvl(zz.dr,0) = 0 ";
		
		}else if(WdsWlPubConst.WDSF.equals(billType)){//װж�Ѻ���
			sql = " select distinct zz.csourcetype,zz.csourcebillhid,zz.vsourcebillcode from  wds_loadprice_b1 zz where zz.pk_loadprice = ? and nvl(zz.dr,0) = 0 ";
		}else if(WdsWlPubConst.WDSO.equals(billType) || WdsWlPubConst.WDSX.equals(billType)){//���۳���ش�
			sql = " select distinct zz.csourcetype,zz.csourcebillhid,zz.vsourcebillcode from  wds_writeback4c_b2 zz where zz.pk_wds_writeback4c_h = ? and nvl(zz.dr,0) = 0 ";
		}else if(WdsWlPubConst.BILLTYPE_ALLO_IN.equals(billType)){//�������
			sql = " select distinct zz.gylbilltype, zz.gylbillhid, zz.gylbillcode from tb_general_b zz where zz.geh_pk = ? and nvl(zz.dr, 0) = 0 ";
		}else if(WdsWlPubConst.WDSP.equals(billType)){//�����������
			sql = " select distinct zz.csourcetype, zz.csourcebillhid, zz.vsourcebillcode from wds_writeback4y_b2 zz where zz.pk_wds_writeback4y_h = ? and nvl(zz.dr, 0) = 0 ";
		}else if(WdsWlPubConst.BILLTYPE_OUT_IN.equals(billType)){//�˻����
			sql = " select distinct zz.csourcetype,zz.csourcebillhid,zz.vsourcebillcode from  tb_general_b zz where zz.geh_pk = ? and nvl(zz.dr,0) = 0 ";
		
		}else if(WdsWlPubConst.GYL4Y.equals(billType)){//��Ӧ����������
			sql = " select distinct zz.csourcetype, zz.csourcebillhid, zz.vsourcebillcode from ic_general_b zz where zz.cgeneralhid = ? and nvl(zz.dr, 0) = 0 ";
		}else if(WdsWlPubConst.GYL4E.equals(billType)){//��Ӧ��
			sql = " select distinct zz.csourcetype, zz.csourcebillhid, zz.vsourcebillcode from ic_general_b zz where zz.cgeneralhid = ? and nvl(zz.dr, 0) = 0 ";
		}else if(WdsWlPubConst.GYL4I.equals(billType)){//��Ӧ����������
			sql = " select distinct zz.csourcetype, zz.csourcebillhid, zz.vsourcebillcode from ic_general_b zz where zz.cgeneralhid = ? and nvl(zz.dr, 0) = 0 ";
		}else if(WdsWlPubConst.GYL4A.equals(billType)){//��Ӧ���������
			sql = " select distinct zz.csourcetypecode, zz.csourceid, zz.vsourcecode from to_bill_b zz where zz.cbillid = ? and nvl(zz.dr, 0) = 0 ";
		}else if(Wds2WlPubConst.billtype_alloinsendorder.equals(billType)){//�����˵�
			sql = " select distinct zz.vsourcebillcode, zz.csourcebillhid, zz.vsourcecode from wds_sendorder_b zz where zz.pk_sendorder = ? and nvl(zz.dr, 0) = 0 ";
		}
		else{
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
