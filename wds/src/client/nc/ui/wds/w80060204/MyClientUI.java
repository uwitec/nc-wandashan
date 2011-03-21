package nc.ui.wds.w80060204;

import java.util.Iterator;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.so.so001.SaleOrderVO;
import nc.ui.pub.beans.UIRefPane;
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
 public class MyClientUI extends AbstractMyClientUI implements
	ListSelectionListener{
       
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
		
		
		super.afterEdit(e);
	}

	@Override
	protected void initEventListener() {
		// TODO Auto-generated method stub
		super.initEventListener();
		getBillListPanel().getHeadTable().getSelectionModel()
				.addListSelectionListener(this);
	}

	//点击每一行的时候触发
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
//		if (getBillListPanel().getHeadTable().getSelectedRowCount() > 0) {
//			//获取单据状态
//			Object o = getBillListPanel().getHeadBillModel().getValueAt(
//					getBillListPanel().getHeadTable().getSelectedRow(),
//					"vbillstatus");
//			//判断是否为空
//			if (o != null && o != "") {
//				int i = Integer.parseInt(o.toString());
//				//如果为1就是单据制单完成 0 是制单未完成
//				if (i == 1) {
//					//设置修改按钮状态
//					getButtonManager().getButton(IBillButton.Edit).setEnabled(
//							false);
//				} else {
//					getButtonManager().getButton(IBillButton.Edit).setEnabled(
//							true);
//				}
//			}
//		}
	}
}
