package nc.ui.wds.w8006080806;

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

	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initEventListener() {
		// TODO Auto-generated method stub
		super.initEventListener();

		// 卡片下表体监听
		getBillCardPanel().getBillTable().getSelectionModel()
				.addListSelectionListener(this);
	}

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
		 */

		Object tnumbermin = getBillCardPanel().getBodyValueAt(
				getBillCardPanel().getBillTable().getSelectedRow(),
				"tnumbermin");
		Object tnumbermax = getBillCardPanel().getBodyValueAt(
				getBillCardPanel().getBillTable().getSelectedRow(),
				"tnumbermax");
		double d=-1;
		if (tnumbermin != null) {
			if (e.getKey().equals("tnumbermin")) {

				if (null == tnumbermax || "".equals(tnumbermax)) {

				} else if (Double.parseDouble(tnumbermax.toString()) == d ){

				} else {
					if (Double.parseDouble(tnumbermin.toString()) >= Double
							.parseDouble(tnumbermax.toString())) {
						this.showErrorMessage("吨数最小值不能大于等于最大值");
						getBillCardPanel().getBillModel().setValueAt(
								"",
								getBillCardPanel().getBillTable()
										.getSelectedRow(), "tnumbermin");
					}
				}
			}
		}
		System.out.println(Double.parseDouble(tnumbermax.toString()) ==(double)-1);
		System.out.println(Double.parseDouble(tnumbermax.toString()));
		System.out.println((double)-1);
		System.out.println(d);
		if (tnumbermax != null) {
			if (e.getKey().equals("tnumbermax")) {

				if (null == tnumbermax || "".equals(tnumbermax)) {

				}  else if (Double.parseDouble(tnumbermax.toString()) == d ){

					
				} else {
					if (Double.parseDouble(tnumbermin.toString()) >= Double
							.parseDouble(tnumbermax.toString())) {
						this.showErrorMessage("吨数最大值不能小于等于最小值");
						getBillCardPanel().getBillModel().setValueAt(
								"",
								getBillCardPanel().getBillTable()
										.getSelectedRow(), "tnumbermax");
					}
				}
			}
		}
		if (e.getKey().equals("qy")) {
			getBillCardPanel().execHeadEditFormulas();
		}
		super.afterEdit(e);
	}

}
