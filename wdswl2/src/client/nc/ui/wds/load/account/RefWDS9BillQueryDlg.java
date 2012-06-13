package nc.ui.wds.load.account;

import java.awt.Container;

import nc.ui.pub.ClientEnvironment;
import nc.ui.wl.pub.WdsBillQueryDlg;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * 
 * @author Administrator
 *其他出库 参照 调拨入库（WDS9）的查询对话框
 */
public class RefWDS9BillQueryDlg extends WdsBillQueryDlg{

	
	private static final long serialVersionUID = 1L;
	
	private static String pk_corp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
	
	private static String userid = ClientEnvironment.getInstance().getUser().getPrimaryKey();
	
	public RefWDS9BillQueryDlg(Container parent) {
		super(parent,null,pk_corp,
				WdsWlPubConst.LOAD_ACCOUNT,userid,null,WdsWlPubConst.LOAD_ACCOUNT_REFWDS9);
	}



	
	@Override
	public String getWhereSQL() {
		String sql=super.getWhereSQL();
		
		if(sql==null){
	      return null;
		}
		if(sql.contains("wds_sendorder.vbillstatus = 0")){
			
		  return sql.replace("wds_sendorder.vbillstatus = 0", "wds_sendorder.vbillstatus = 8");
		}
		return sql;
	}

	public void initData(String pkCorp, String operator, String funNode,
			String businessType, String currentBillType, String sourceBilltype,
			String nodeKey, Object userObj) throws Exception {
		//增加默认值
		String sDate = ClientEnvironment.getInstance().getBusinessDate().toString();
		setDefaultValue("wds_sendorder.dmakedate", null, sDate);//单据日期
	}


}
