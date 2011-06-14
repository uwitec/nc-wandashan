package nc.ui.wds.tranprice.bill;

import java.awt.Container;

import nc.ui.pub.ClientEnvironment;
import nc.ui.wl.pub.WdsBillQueryDlg;
import nc.vo.wl.pub.WdsWlPubConst;

public class RefWDS5BillQueryDlg extends WdsBillQueryDlg{
	private static String pk_corp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
	
	 private static String userid = ClientEnvironment.getInstance().getUser().getPrimaryKey();
		

	public RefWDS5BillQueryDlg(Container parent) {
		super(parent,null,pk_corp,
				WdsWlPubConst.TRANS_PRICE_NODECODE,userid,null,WdsWlPubConst.TRANS_PRICE_NODECODEWDS5);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -6363240668749432185L;

	public void initData(String pkCorp, String operator, String funNode,
			String businessType, String currentBillType, String sourceBilltype,
			String nodeKey, Object userObj) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
