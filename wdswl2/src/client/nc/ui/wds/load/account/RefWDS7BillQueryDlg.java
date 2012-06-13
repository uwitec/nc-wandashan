package nc.ui.wds.load.account;

import java.awt.Container;

import nc.ui.pub.ClientEnvironment;
import nc.ui.wl.pub.WdsBillQueryDlg;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * 
 * @author Administrator
 *装卸费结算 参照 其他入库（WDS7）的查询对话框
 */
public class RefWDS7BillQueryDlg extends WdsBillQueryDlg{

	
	private static final long serialVersionUID = 1L;
	
	private static String pk_corp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
	
	private static String userid = ClientEnvironment.getInstance().getUser().getPrimaryKey();
	
	public RefWDS7BillQueryDlg(Container parent) {
		super(parent,null,pk_corp,
				WdsWlPubConst.LOAD_ACCOUNT,userid,null,WdsWlPubConst.LOAD_ACCOUNT_REFWDS7);
	}

	public void initData(String pkCorp, String operator, String funNode,
			String businessType, String currentBillType, String sourceBilltype,
			String nodeKey, Object userObj) throws Exception {
//		//增加默认值
//		String sDate = ClientEnvironment.getInstance().getBusinessDate().toString();
//		setDefaultValue("wds_sendorder.dmakedate", null, sDate);//单据日期
	}


}
