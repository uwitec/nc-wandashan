package nc.ui.wds.tranprice.bill;

import java.awt.Container;

import nc.ui.pub.ClientEnvironment;
import nc.ui.wl.pub.WdsBillQueryDlg;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * 
 * @author Administrator
 *�˷Ѻ������ ���� �������⣨WDS6���Ĳ�ѯ�Ի���
 *��ѯģ������� �˷Ѻ���ڵ㴦:WdsWlPubConst.LOAD_ACCOUNT
 */
public class RefWDS6BillQueryDlg extends WdsBillQueryDlg{

	
	private static final long serialVersionUID = 1L;
	
	private static String pk_corp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
	
	private static String userid = ClientEnvironment.getInstance().getUser().getPrimaryKey();
	
	public RefWDS6BillQueryDlg(Container parent) {
		super(parent,null,pk_corp,
				WdsWlPubConst.LOAD_ACCOUNT,userid,null,WdsWlPubConst.LOAD_ACCOUNT_REFWDS6);
	}


	public void initData(String pkCorp, String operator, String funNode,
			String businessType, String currentBillType, String sourceBilltype,
			String nodeKey, Object userObj) throws Exception {
//		//����Ĭ��ֵ
//		String sDate = ClientEnvironment.getInstance().getBusinessDate().toString();
//		setDefaultValue("wds_sendorder.dmakedate", null, sDate);//��������
	}


}
