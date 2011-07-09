package nc.ui.wds.dm.storetranscorp;
import javax.swing.JComponent;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;
/** 
 * <p>
 * 分仓承运商绑定
 * </p>
 * @author author
 * @version tempProject version
 */
public class MyClientUI extends BillManageUI {
	private static final long serialVersionUID = 1L;



	protected ManageEventHandler createEventHandler() {
		return new MyEventHandler(this, getUIControl());
	}

	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
	}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	}

	@Override
	protected void initSelfData() {
		//
		ButtonObject btn = getButtonManager().getButton(IBillButton.Line);
		if (btn != null) {
			btn.removeChildButton(getButtonManager().getButton(
					IBillButton.CopyLine));
			btn.removeChildButton(getButtonManager().getButton(
					IBillButton.PasteLine));
			btn.removeChildButton(getButtonManager().getButton(
					IBillButton.InsLine));
		}
	}

	public void setDefaultData() throws Exception {
	}
	@Override
	public boolean beforeEdit(BillEditEvent e) {
		//只能看到物流自己定义的档案
		if("areacode".equalsIgnoreCase(e.getKey())){
			JComponent jc = getBillCardPanel().getBodyItem("areacode").getComponent();
			if(jc instanceof UIRefPane){
				UIRefPane ref = (UIRefPane)jc;
				ref.getRefModel().addWherePart("and areaclcode like '00%'");
			}
		}
		return super.beforeEdit(e);
	}

	@Override
	protected AbstractManageController createController() {
		// TODO Auto-generated method stub
		return new MyClientUICtrl();
	}
	
	@Override
	public void bodyRowChange(BillEditEvent e) {
		// TODO Auto-generated method stub
		super.bodyRowChange(e);
	}

	@Override
	public Object getUserObject() {
		
		return new GetCheckClass();
	}
	


}
