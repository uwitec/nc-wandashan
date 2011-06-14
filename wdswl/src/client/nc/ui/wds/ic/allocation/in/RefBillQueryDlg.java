package nc.ui.wds.ic.allocation.in;

import java.awt.Container;

import nc.ui.pub.ClientEnvironment;
import nc.ui.wl.pub.WdsBillQueryDlg;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * 
 * @author Administrator
 *物流调拨入库(WDS9)参照-->erp调拨出库单
 */
public class RefBillQueryDlg extends WdsBillQueryDlg{

private static final long serialVersionUID = 1L;
	
	private static String pk_corp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
	
	private static String userid = ClientEnvironment.getInstance().getUser().getPrimaryKey();
	
	public RefBillQueryDlg(Container parent) {
		super(parent,null,pk_corp,WdsWlPubConst.IC_TRANSIN_NODECODE,userid,null,WdsWlPubConst.IC_TRANSIN_REF4Y_NODECODE);
	}


	@Override
	public String getWhereSQL() {
	String sql=super.getWhereSQL();
		if(sql==null){
	      return null;
		}
		if(sql.contains("head.vbillstatus = 0")){	
		  return sql.replace("head.vbillstatus = 0", "head.vbillstatus = 8");
		}
		return sql;
	}

	public void initData(String pkCorp, String operator, String funNode,
			String businessType, String currentBillType, String sourceBilltype,
			String nodeKey, Object userObj) throws Exception {
		//增加默认值
		String sDate = ClientEnvironment.getInstance().getBusinessDate().toString();
		setDefaultValue("head.dbilldate", null, sDate);//单据日期
	}

}
