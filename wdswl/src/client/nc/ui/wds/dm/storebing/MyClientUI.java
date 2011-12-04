package nc.ui.wds.dm.storebing;

import javax.swing.JComponent;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.wds.dm.storebing.GetCheck;

/**
 * <b> 在此处简要描述此类的功能 </b>
 * 
 * <p>
 * 分仓客商绑定
 * </p>
 * 
 * 
 * @author author
 * @version tempProject version
 */
public class MyClientUI extends BillManageUI {

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
		getBillCardPanel().getHeadItem("pk_corp").setValue(_getCorp().getPrimaryKey());
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
	public void afterEdit(BillEditEvent e) {
		String key = e.getKey();
		Object value =e.getValue();
		if ("custcode".equals(key)) {
			//选定客商后，清除分仓信息
			if(value != null && !"".equals(value)){
				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(), "pk_stordoc1");
				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(), "storname");
				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(), "storcode");
			}
		}else if ("storcode".equals(key)) {
			//选定分仓后，清除客商信息
			if(value != null && !"".equals(value)){
				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(), "pk_cumandoc ");
				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(), "pk_cubasdoc");
				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(), "custname");
				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(), "custcode");
				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(), "conaddr");
				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(), "areaclname");
			}
		}
	}
	@Override
	protected BusinessDelegator createBusinessDelegator() {
		// TODO Auto-generated method stub
		return new MyDelegator();
	}

	@Override
	protected AbstractManageController createController() {
		// TODO Auto-generated method stub
		return new MyClientUICtrl();
	}

	//校验
	public Object getUserObject() {
		return new GetCheck();
	}
	
}
