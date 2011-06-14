package nc.ui.wds.load.account;

import java.awt.Container;

import nc.ui.wl.pub.LoginInforHelper;
import nc.ui.wl.pub.WdsBillSourceDLG;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * 
 * @author Administrator
 *装卸费结算 参照 其他入库（WDS7）
 */
public class RefWDS7BillSourceDlg  extends WdsBillSourceDLG {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private LoginInforHelper helper = null;
	
	public LoginInforHelper getLoginInforHelper(){
		if(helper == null){
			helper = new LoginInforHelper();
		}
		return helper;
	}
	
	public RefWDS7BillSourceDlg(String pkField, String pkCorp, String operator,
			String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType,
			Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType,
				templateId, currentBillType, parent);
	}
	public RefWDS7BillSourceDlg(String pkField, String pkCorp, String operator,
			String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType,
			String nodeKey, Object userObj, Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType,
				templateId, currentBillType, nodeKey, userObj, parent);
	}
		
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "参照其他入库";
	}
	@Override
	public String getHeadCondition() {
		StringBuffer hsql = new StringBuffer();
		hsql.append(" isnull(tb_general_h.dr,0)=0 and tb_general_h.pwb_fbillflag = 1 ");
		hsql.append(" and tb_general_h.geh_billtype='"+WdsWlPubConst.BILLTYPE_OTHER_IN+"'");
		hsql.append(" and upper(isnull(tb_general_h.fisload,'N'))='N'");
		return hsql.toString();
	}
	@Override
	public String getBodyCondition() {
		StringBuffer bsql = new StringBuffer();
		bsql.append(" isnull(tb_general_b.dr,0)=0 ");
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
