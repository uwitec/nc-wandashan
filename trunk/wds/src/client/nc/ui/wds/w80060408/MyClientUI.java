package nc.ui.wds.w80060408;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.field.BillField;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BDBusinessDelegator;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;


/**
 * <b> 在此处简要描述此类的功能 </b>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 *
 * @author author
 * @version tempProject version
 */
 public class MyClientUI extends AbstractMyClientUI implements ListSelectionListener{
       
       protected ManageEventHandler createEventHandler() {
		return new MyEventHandler(this, getUIControl());
	}
       
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {	}

	protected void initSelfData() {	}

	public void setDefaultData() throws Exception {
		
	}
	
	@Override
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		if(e.getKey().equals("cshenpizhuangtai")){
			//获取下拉审批状态，给隐藏审批状态赋值
			int i = Integer.parseInt(getBillCardPanel().getHeadTailItem("cshenpizhuangtai").getValueObject().toString());
			getBillCardPanel().getHeadTailItem("fyd_approstate").setValue(i);
		}
		super.afterEdit(e);
	}


	@Override
	protected void initEventListener() {
		// TODO Auto-generated method stub
		super.initEventListener();
		getBillListPanel().getHeadTable().getSelectionModel().addListSelectionListener(this);
	}
	
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		//getBillListPanel().getHeadTable().getSelectedRow();
//		if(getBillListPanel().getHeadTable().getSelectedRowCount()>0){
//			
//			Object o = getBillListPanel().getHeadBillModel().getValueAt(getBillListPanel().getHeadTable().getSelectedRow(), "fyd_approstate");
//
//			if(o!=null&&o!="") {
//				int i = Integer.parseInt(o.toString());
//				if(i==1){
//					getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
//				}else{
//					getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
//				}
//			}
//		}
//		
	}
}
