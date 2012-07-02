package nc.ui.wds.ic.other.out;

import java.awt.Container;

import nc.ui.pub.ClientEnvironment;
import nc.ui.wl.pub.WdsBillQueryDlg;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * 
 * @author yf
 *其他出库 参照 货位调整单（HWTZ）的查询对话框
 */
public class RefHWTZBillQueryDlg extends WdsBillQueryDlg{

	
	private static final long serialVersionUID = 1L;
	
	private static String pk_corp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
	
	private static String userid = ClientEnvironment.getInstance().getUser().getPrimaryKey();
	
	public RefHWTZBillQueryDlg(Container parent) {
		super(parent,null,pk_corp,
				WdsWlPubConst.OTHER_OUT_FUNCODE,userid,null,WdsWlPubConst.OTHER_OUT_REFHWTZ_NODECODE);
	}


	@Override
	public String getWhereSQL() {
		String sql=super.getWhereSQL();
		
		if(sql==null){
	      return null;
		}
		if(sql.contains("wds_transfer.vbillstatus = 0")){
			
		  return sql.replace("wds_transfer.vbillstatus = 0", "wds_transfer.vbillstatus = 8");
		}
		return sql;
	}
	public void initData(String pkCorp, String operator, String funNode,
			String businessType, String currentBillType, String sourceBilltype,
			String nodeKey, Object userObj) throws Exception {
		//增加默认值
		String sDate = ClientEnvironment.getInstance().getBusinessDate().toString();
		setDefaultValue("wds_transfer.dbilldate", null, sDate);//单据日期
	}


}
