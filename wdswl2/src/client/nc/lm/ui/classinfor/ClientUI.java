package nc.lm.ui.classinfor;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;
public class ClientUI extends BillManageUI{
	private static final long serialVersionUID = 6714332441173414659L;
	@Override
	protected AbstractManageController createController() {
		
		return new Control();
	}
	@Override
	protected ManageEventHandler createEventHandler() {	
		return new EventHandler(this,getUIControl());
	}
	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {		
	}
	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {		
	}

	@Override
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {		
	}

	@Override
	protected void initSelfData() {
		
	}
	/**
	 * 单据新增按钮的时候 给表单赋默认值
	 */
	@Override
	public void setDefaultData() throws Exception {
		getBillCardPanel().getHeadItem("pk_corp").setValue(getCorpPrimaryKey());		
	}
	@Override
	public Object getUserObject() {
		return new GetCheck();
	}	
}
