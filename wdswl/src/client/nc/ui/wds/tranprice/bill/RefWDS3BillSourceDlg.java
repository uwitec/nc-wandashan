package nc.ui.wds.tranprice.bill;

import java.awt.Container;

import nc.ui.wl.pub.WdsBillSourceDLG;
/**
 * 
 * 参照（发运订单对话框）对话框
 * @author mlr
 *
 */
public class RefWDS3BillSourceDlg extends WdsBillSourceDLG{

	public RefWDS3BillSourceDlg(String pkField, String pkCorp, String operator,
			String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType,
			Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType,
				templateId, currentBillType, parent);
		
	}

	private static final long serialVersionUID = 5731199093034145034L;

	@Override
	public String getTitle() {
		
		return "参照发运订单";
	}
	
	@Override
	public String getHeadCondition() {
		StringBuffer hsql = new StringBuffer();
		hsql.append(" isnull(wds_sendorder.dr,0)=0 and wds_sendorder.vbillstatus = 1 ");
		return hsql.toString();
	}
	@Override
	public String getBodyCondition() {
		StringBuffer bsql = new StringBuffer();
		bsql.append(" isnull(wds_sendorder_b.dr,0)=0 ");
		return bsql.toString();
	}
	@Override
	protected boolean isHeadCanMultiSelect() {
		return true;
	}
	@Override
	protected boolean isBodyCanSelected() {
		return true;
	}		
	@Override
	public boolean getIsBusinessType() {
		return false;
	}
}
