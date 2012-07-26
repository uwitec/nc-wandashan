package nc.ui.wds.ic.allocation.out;

import java.awt.Container;
import nc.ui.pub.ClientEnvironment;
import nc.ui.wl.pub.WdsBillQueryDlg;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * @author mlr 其他出库 参照 调出订单（WDSG）的查询对话框
 */
public class RefWDSGBillQueryDlg extends WdsBillQueryDlg {

	private static final long serialVersionUID = 1L;

	private static String pk_corp = ClientEnvironment.getInstance()
			.getCorporation().getPrimaryKey();

	private static String userid = ClientEnvironment.getInstance().getUser()
			.getPrimaryKey();

	public RefWDSGBillQueryDlg(Container parent) {
		super(parent, null, pk_corp, WdsWlPubConst.ALLO_OUT_FUNCODE, userid,
				null, WdsWlPubConst.ALLO_OUT_REFWDSG_NODECODE);
	}

	@Override
	public String getWhereSQL() {
		String sql = super.getWhereSQL();
		String sqlplus = " isnull(wds_sendorder."
				+ WdsWlPubConst.sendorder_close + " ,'N')= 'N' ";
		if (sql == null) {
			return sqlplus;
		}
		if (sql.contains("wds_sendorder.vbillstatus = 0")) {
			return sql.replace("wds_sendorder.vbillstatus = 0",
					"wds_sendorder.vbillstatus = 8");
		}
		return sql + "and" + sqlplus;
	}

	public void initData(String pkCorp, String operator, String funNode,
			String businessType, String currentBillType, String sourceBilltype,
			String nodeKey, Object userObj) throws Exception {
		// 增加默认值
		String sDate = ClientEnvironment.getInstance().getBusinessDate()
				.toString();
		setDefaultValue("wds_sendorder.dmakedate", null, sDate);// 单据日期
	}
}
