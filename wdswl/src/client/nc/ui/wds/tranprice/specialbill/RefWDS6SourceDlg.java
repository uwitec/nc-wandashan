package nc.ui.wds.tranprice.specialbill;

import java.awt.Container;

import nc.ui.wl.pub.WdsBillSourceDLG;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * 
 * @author Administrator
 *װж�ѽ��� ���� �������⣨WDS6��
 */
public class RefWDS6SourceDlg  extends WdsBillSourceDLG {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public RefWDS6SourceDlg(String pkField, String pkCorp, String operator,
			String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType,
			Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType,
				templateId, currentBillType, parent);
	}
	public RefWDS6SourceDlg(String pkField, String pkCorp, String operator,
			String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType,
			String nodeKey, Object userObj, Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType,
				templateId, currentBillType, nodeKey, userObj, parent);
	}
	
	
	
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "�����������ⵥ";
	}
	
	@Override
	public String getHeadCondition() {
		StringBuffer hsql = new StringBuffer();
		hsql.append(" isnull(tb_outgeneral_h.dr,0)=0 and tb_outgeneral_h.vbillstatus = 1 ");
		hsql.append(" and tb_outgeneral_h.vbilltype='"+WdsWlPubConst.BILLTYPE_OTHER_OUT+"'");
		hsql.append(" and upper(isnull(tb_outgeneral_h.fistran,'N'))='N'");
		hsql.append(" and upper(tb_outgeneral_h.iscaltrans)='Y'");
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