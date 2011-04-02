package nc.ui.wds.w8006080804;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BDBusinessDelegator;
import nc.ui.trade.manage.ManageEventHandler;

/**
 * <b> 在此处简要描述此类的功能 </b>
 * 
 * <p>
 * 在此处添加此类的描述信息
 * </p>
 * 
 * 
 * @author author
 * @version tempProject version
 */
public class MyClientUI extends AbstractMyClientUI implements
		ListSelectionListener {

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

	protected void initSelfData() {
	}

	public void setDefaultData() throws Exception {
	}

	@Override
	protected void initEventListener() {
		// TODO Auto-generated method stub
		super.initEventListener();

		// 卡片下表体监听
		getBillCardPanel().getBillTable().getSelectionModel()
				.addListSelectionListener(this);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		/*
		 * if (e.getKey().equals("cdhz")) { UIRefPane uipane = (UIRefPane)
		 * getBillCardPanel().getBodyItem( "cdhz").getComponent();
		 * 
		 * String ss = uipane.getRefModel().getPkValue();
		 * 
		 * getBillCardPanel().setBodyValueAt(ss,
		 * getBillCardPanel().getBillTable().getSelectedRow(), "pk_wlxx");
		 * 
		 * 
		 * if(e.getKey().equals("pk_wlxx")){
		 * getBillCardPanel().execHeadEditFormulas(); } }
		 * 
		 * String evennumbermin = getBillCardPanel().getBillModel().getValueAt(
		 * getBillCardPanel().getBillTable().getSelectedRow(),
		 * "evennumbermin").toString(); String evennumbermax =
		 * getBillCardPanel().getBillModel().getValueAt(
		 * getBillCardPanel().getBillTable().getSelectedRow(),
		 * "evennumbermax").toString(); String packnumbermin =
		 * getBillCardPanel().getBillModel().getValueAt(
		 * getBillCardPanel().getBillTable().getSelectedRow(),
		 * "packnumbermin").toString(); String packnumbermax =
		 * getBillCardPanel().getBillModel().getValueAt(
		 * getBillCardPanel().getBillTable().getSelectedRow(),
		 * "packnumbermax").toString();
		 */
		/*Object evennumbermin = getBillCardPanel().getBodyValueAt(
				getBillCardPanel().getBillTable().getSelectedRow(),
				"evennumbermin");
		Object evennumbermax = getBillCardPanel().getBodyValueAt(
				getBillCardPanel().getBillTable().getSelectedRow(),
				"evennumbermax");*/
		Object packnumbermin = getBillCardPanel().getBodyValueAt(
				getBillCardPanel().getBillTable().getSelectedRow(),
				"packnumbermin");
		Object packnumbermax = getBillCardPanel().getBodyValueAt(
				getBillCardPanel().getBillTable().getSelectedRow(),
				"packnumbermax");
		double d=-1;
		/*if (evennumbermin != null) {

			if (e.getKey().equals("evennumbermin")) {

				if (null == evennumbermax || "".equals(evennumbermax)) {

				}
				else 	if(Double.parseDouble(evennumbermax.toString())==d)
				{
					
				}else {
				
					if (Double.parseDouble(evennumbermin.toString()) >= Double
							.parseDouble(evennumbermax.toString())) {
						this.showErrorMessage("公里数最小值不能大于等于最大值");
						getBillCardPanel().setBodyValueAt(
								null,
								getBillCardPanel().getBillTable()
										.getSelectedRow(), "evennumbermin");

					}
				}
			}
		}*/
		if (packnumbermin != null) {
			if (e.getKey().equals("packnumbermin")) {

				if (null == packnumbermax || "".equals(packnumbermax)) {

				} 
				else 	if(Double.parseDouble(packnumbermax.toString())==d)
				{
					
				}else {
					if (Double.parseDouble(packnumbermin.toString()) >= Double
							.parseDouble(packnumbermax.toString())) {
						this.showErrorMessage("件数最小值不能大于等于最大值");
						getBillCardPanel().getBillModel().setValueAt(
								"",
								getBillCardPanel().getBillTable()
										.getSelectedRow(), "packnumbermin");
					}
				}
			}
		}
		/*if (evennumbermax != null) {
			if (e.getKey().equals("evennumbermax")) {

				if (null == evennumbermax || "".equals(evennumbermax)) {

				}
				else 	if(Double.parseDouble(evennumbermax.toString())==d)
				{
					
				}else {
					if (Double.parseDouble(evennumbermin.toString()) >= Double
							.parseDouble(evennumbermax.toString())) {
						this.showErrorMessage("公里数最大值不能小于等于最小值");
						getBillCardPanel().getBillModel().setValueAt(
								"",
								getBillCardPanel().getBillTable()
										.getSelectedRow(), "evennumbermax");

					}
				}
			}
		}*/
		if (packnumbermax != null) {
			if (e.getKey().equals("packnumbermax")) {

				if (null == packnumbermax || "".equals(packnumbermax)) {

				} 
				else 	if(Double.parseDouble(packnumbermax.toString())==d)
				{
					
				}else {
					if (Double.parseDouble(packnumbermin.toString()) >= Double
							.parseDouble(packnumbermax.toString())) {
						this.showErrorMessage("件数最大值不能小于等于最小值");
						getBillCardPanel().getBillModel().setValueAt(
								"",
								getBillCardPanel().getBillTable()
										.getSelectedRow(), "packnumbermax");
					}
				}
			}
		}
		if (e.getKey().equals("qy")) {
			getBillCardPanel().execHeadEditFormulas();
		}

		super.afterEdit(e);
	}

	public void valueChanged(ListSelectionEvent arg0) {
		// TODO Auto-generated method stub

	}

}
