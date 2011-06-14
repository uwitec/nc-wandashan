package nc.ui.wds.ic.so.out;

import java.awt.Container;

import nc.ui.pub.ClientEnvironment;
import nc.ui.wl.pub.WdsBillQueryDlg;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * 销售出库参照销售运单(WDS5)查询对话框
 * @author zpm
 *
 */
public class RefBillQueryDlg extends WdsBillQueryDlg {
	
	private static final long serialVersionUID = 1L;
	
	private static String pk_corp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
	
	private static String userid = ClientEnvironment.getInstance().getUser().getPrimaryKey();
	
	public RefBillQueryDlg(Container parent) {
		super(parent,null,pk_corp,WdsWlPubConst.SO_OUT_FUNCODE,userid,null,WdsWlPubConst.SO_OUT_REFWDS5_NODECODE);
	}


	@Override
	public String getWhereSQL() {
		String sql=super.getWhereSQL();
		
		if(sql==null){
	      return null;
		}
		if(sql.contains("wds_soorder.vbillstatus = 0")){
			
		  return sql.replace("wds_soorder.vbillstatus = 0", "wds_soorder.vbillstatus = 8");
		}
		return sql;
	}

	public void initData(String pkCorp, String operator, String funNode,
			String businessType, String currentBillType, String sourceBilltype,
			String nodeKey, Object userObj) throws Exception {
		//增加默认值
		String sDate = ClientEnvironment.getInstance().getBusinessDate().toString();
		setDefaultValue("wds_soorder.dbilldate", null, sDate);//单据日期
	}
}
