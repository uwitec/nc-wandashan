package nc.ui.wds.load.account;

import java.awt.Container;

import nc.ui.wl.pub.WdsBillSourceDLG;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * 
 * @author Administrator
 *装卸费结算 参照 销售出库（WDS8）
 */
public class RefWDS8BillSourceDlg  extends WdsBillSourceDLG {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public RefWDS8BillSourceDlg(String pkField, String pkCorp, String operator,
			String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType,
			Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType,
				templateId, currentBillType, parent);
	}
	public RefWDS8BillSourceDlg(String pkField, String pkCorp, String operator,
			String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType,
			String nodeKey, Object userObj, Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType,
				templateId, currentBillType, nodeKey, userObj, parent);
	}
	
	
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "参照销售出库";
	}
	@Override
	public String getHeadCondition() {
		StringBuffer hsql = new StringBuffer();
		//表单参照交换vo添加pk_corp
		hsql.append("tb_outgeneral_h.pk_corp = '"+getPkCorp()+"' and");

		hsql.append(" isnull(tb_outgeneral_h.dr,0)=0 and tb_outgeneral_h.vbillstatus = 1 ");
		hsql.append(" and tb_outgeneral_h.vbilltype='"+WdsWlPubConst.BILLTYPE_SALE_OUT+"'");
		hsql.append(" and upper(isnull(tb_outgeneral_h.fisload,'N'))='N'");
		return hsql.toString();
	}
	@Override
	public String getBodyCondition() {
		StringBuffer bsql = new StringBuffer();
		bsql.append(" isnull(tb_outgeneral_b.dr,0)=0 ");
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
