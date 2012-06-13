package nc.ui.wds.tranprice.bill;

import java.awt.Container;

import nc.ui.pub.ClientEnvironment;
import nc.ui.wl.pub.WdsBillQueryDlg;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * 
 * 参照发运订单查询模板
 * 
 * @author mlr 
 *
 */
public class RefWDS3BillQueryDlg extends WdsBillQueryDlg{
	
 private static final long serialVersionUID = 7855100245783268048L;
 
 private static String pk_corp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
	
 private static String userid = ClientEnvironment.getInstance().getUser().getPrimaryKey();
	
	
	
	public RefWDS3BillQueryDlg(Container parent) {
		super(parent,null,pk_corp,
				WdsWlPubConst.TRANS_PRICE_NODECODE,userid,null,WdsWlPubConst.TRANS_PRICE_NODECODEWDS3);
	}


	public void initData(String pkCorp, String operator, String funNode,
			String businessType, String currentBillType, String sourceBilltype,
			String nodeKey, Object userObj) throws Exception {
		
		
	}

}
