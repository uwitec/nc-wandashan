package nc.ui.wds.ic.other.out;
import java.awt.Container;
import nc.bs.logging.Logger;
import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.wdsnew.pub.MBillSourceDLG;
import nc.ui.wdsnew.pub.PowerGetTool;
/**
 * @author mlr
 *其他出库 参照 发运订单（WDS3）
 */
public class RefWDS3BillSourceDlg  extends MBillSourceDLG {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//获得权限过滤的sql
	String sql=null;
	
	private String getPowerSql(){
		if (sql == null || sql.length() == 0)
			try {
				sql = PowerGetTool.queryClassPowerSql(ClientEnvironment
						.getInstance().getUser().getPrimaryKey());
			} catch (Exception e) {
				this.getClientUI().showErrorMessage(e.getMessage());		
				e.printStackTrace();
			}
		return sql;
	}
	
	public RefWDS3BillSourceDlg(String pkField, String pkCorp, String operator,
			String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType,
			Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType,
				templateId, currentBillType, parent);
		init();
	}
	public RefWDS3BillSourceDlg(String pkField, String pkCorp, String operator,
			String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType,
			String nodeKey, Object userObj, Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType,
				templateId, currentBillType, nodeKey, userObj, parent);
		init();
	}
	
	public void init(){
		try{
	
		}catch(Exception e){
			Logger.error(e);
		}
	}
	
	
	@Override
	public String getTitle() {
		return "参照发运订单";
	}
	@Override
	public String getHeadCondition() {
		
		return "  coalesce(wds_sendorder_b.ndealnum,0)-coalesce(wds_sendorder_b.noutnum,0)>0 " +//安排数量-出库数量>0
		" and wds_sendorder_b.pk_invmandoc in ("+getPowerSql()+")";
	}
	
	
	@Override
	public String getBodyCondition() {
		return " isnull(wds_sendorder_b.dr,0)=0 and coalesce(wds_sendorder_b.ndealnum,0)-coalesce(wds_sendorder_b.noutnum,0)>0"+//安排数量-出库数量>0
		" and pk_invmandoc in ("+getPowerSql()+")";
	}
	
	@Override
	protected boolean isHeadCanMultiSelect() {
		return false;
	}
	@Override
	protected boolean isBodyCanSelected() {
		return false;
	}
	
	@Override
	public boolean getIsBusinessType() {
		return false;
	}

	@Override
	public IControllerBase getUIController() {
		return new nc.ui.dm.order.ClientController();
	}
	@Override
	public String getPk_invbasdocName() {
		
		return "pk_invbasdoc";
	}

	@Override
	public String getPk_invmandocName() {
		
		return "pk_invmandoc";
	}

}
