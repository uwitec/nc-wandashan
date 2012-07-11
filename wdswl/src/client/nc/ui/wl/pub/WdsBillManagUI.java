package nc.ui.wl.pub;
import nc.ui.zmpub.pub.bill.DefBillManageUI;
/**
 * @author mlr
 */
public abstract class WdsBillManagUI extends DefBillManageUI{

	private LoginInforHelper helper = null;
	
	public LoginInforHelper getLoginInforHelper(){
		if(helper == null){
			helper = new LoginInforHelper();
		}
		return helper;
	}	
	
	public WdsBillManagUI() {
		super();
	}
	
	public WdsBillManagUI(Boolean useBillSource) {
		super(useBillSource);
	}

	public WdsBillManagUI(String pk_corp, String pk_billType, String pk_busitype,
			String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
	}
}
