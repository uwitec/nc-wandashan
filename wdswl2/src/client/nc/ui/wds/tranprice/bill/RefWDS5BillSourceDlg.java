package nc.ui.wds.tranprice.bill;

import java.awt.Container;

import nc.ui.wl.pub.WdsBillSourceDLG;

public class RefWDS5BillSourceDlg extends WdsBillSourceDLG{

	public RefWDS5BillSourceDlg(String pkField, String pkCorp, String operator,
			String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType,
			Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType,
				templateId, currentBillType, parent);
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = -5789111463623753644L;
	@Override
	public String getTitle() {
		
		return "参照销售运单";
	}
	
	@Override
	public String getHeadCondition() {
		StringBuffer hsql = new StringBuffer();
		hsql.append(" isnull(wds_soorder.dr,0)=0 and wds_soorder.vbillstatus = 1 ");
		return hsql.toString();
	}
	@Override
	public String getBodyCondition() {
		StringBuffer bsql = new StringBuffer();
		bsql.append(" isnull(wds_soorder_b.dr,0)=0 ");
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
