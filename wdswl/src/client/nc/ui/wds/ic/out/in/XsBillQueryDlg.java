package nc.ui.wds.ic.out.in;

import java.awt.Container;

import nc.ui.pub.ClientEnvironment;
import nc.ui.wl.pub.WdsBillQueryDlg;
import nc.vo.wl.pub.WdsWlPubConst;

public class XsBillQueryDlg extends WdsBillQueryDlg{

private static final long serialVersionUID = 1L;
	
	private static String pk_corp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
	
	private static String userid = ClientEnvironment.getInstance().getUser().getPrimaryKey();
	
	public XsBillQueryDlg(Container parent) {
		super(parent,null,pk_corp,WdsWlPubConst.IC_OUT_IN_NODECODE,userid,null,WdsWlPubConst.IC_OUT_IN_REF4I_NODECODE);
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
