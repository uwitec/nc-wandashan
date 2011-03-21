package nc.ui.wds.w80060208;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BDBusinessDelegator;
import nc.ui.trade.manage.ManageEventHandler;

/**
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b>
 * 
 * <p>
 * �ڴ˴���Ӵ����������Ϣ
 * </p>
 * 
 * 
 * @author author
 * @version tempProject version
 */
public class MyClientUI extends AbstractMyClientUI implements
		ListSelectionListener, BillCardBeforeEditListener {

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
		super.initEventListener(); // ��ʼ����Ӽ���
		getBillCardPanel().getBillTable().getSelectionModel()
				.addListSelectionListener(this);
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		if ("cfd_xs".equals(e.getKey())) {
			getBillCardPanel().execHeadEditFormulas();
			double cfd_xs = Double.parseDouble(getBillCardPanel()
					.getBodyValueAt(
							getBillCardPanel().getBillTable().getSelectedRow(),
							"cfd_xs").toString());
			double cfd_ysxs = Double.parseDouble(getBillCardPanel()
					.getBodyValueAt(
							getBillCardPanel().getBillTable().getSelectedRow(),
							"cfd_ysxs").toString());
			double cfd_ysyfsl = Double.parseDouble(getBillCardPanel()
					.getBodyValueAt(
							getBillCardPanel().getBillTable().getSelectedRow(),
							"cfd_ysyfsl").toString());
			if (cfd_xs > cfd_ysxs) {
				this.showErrorMessage("����������ܴ���������");
				getBillCardPanel().setBodyValueAt(cfd_ysxs,
						getBillCardPanel().getBillTable().getSelectedRow(),
						"cfd_xs");
				getBillCardPanel().setBodyValueAt(cfd_ysyfsl,
						getBillCardPanel().getBillTable().getSelectedRow(),
						"cfd_yfsl");
				getBillCardPanel().setBodyValueAt(0,
						getBillCardPanel().getBillTable().getSelectedRow(),
						"cfd_syfsl");
				getBillCardPanel().setBodyValueAt(0,
						getBillCardPanel().getBillTable().getSelectedRow(),
						"cfd_sysl");
			}
		}

	}

	public void valueChanged(ListSelectionEvent arg0) {
		// TODO Auto-generated method stub

	}

	public boolean beforeEdit(BillItemEvent e) {
		// TODO Auto-generated method stub
		if ("fyd_sjdh".equals(e.getItem().getKey())) {
			String cif_pk = (String) this.getBillCardPanel()
					.getHeadItem("cif_pk").getValueObject();
			if (cif_pk != null && cif_pk.length() > 0) {
				// �õ���ͬ����
				UIRefPane panel = (UIRefPane) this.getBillCardPanel()
						.getHeadItem("fyd_sjdh").getComponent();
				// this.getBillCardPanel()
				// .getHeadItem("cif_pk").setDecimalDigits(iDecimalDigits);
				// ���Ͽͻ���Ϊ����ȥ����
				panel.getRefModel().setWherePart(
						" cif_pk = '" + cif_pk + "' and dr=0 ");
			} else {

				this.showErrorMessage("��û��ѡ������Ϣ��������ʾ�κ�˾���绰");
				UIRefPane panel = (UIRefPane) this.getBillCardPanel()
						.getHeadItem("fyd_sjdh").getComponent();
				// ���Ͽͻ���Ϊ����ȥ����
				panel.getRefModel().setWherePart(" dr=0 and 1=2  ");
			}
		}
		return false;
	}

}
