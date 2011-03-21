package nc.ui.wds.w80060406;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wds.w800604.tcButtun.cxmxBtn;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.field.BillField;

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
		getButtonManager().getButton(nc.ui.wds.w800604.tcButtun.ITcButtun.cxmx)
				.setEnabled(false);
	}

	public void setDefaultData() throws Exception {
		// ��Ҫ���� ��ҳ���ϵ��Ƶ��˸�ֵ
		BillField fileDef = BillField.getInstance();
		String billtype = getUIControl().getBillType();
		String pkCorp = ClientEnvironment.getInstance().getCorporation()
				.getPrimaryKey();

		String[] itemkeys = new String[] { fileDef.getField_Operator() };
		Object[] values = new Object[] { ClientEnvironment.getInstance()
				.getUser().getPrimaryKey() };

		for (int i = 0; i < itemkeys.length; i++) {
			BillItem item = null;
			item = getBillCardPanel().getHeadItem(itemkeys[i]);
			if (item == null)
				item = getBillCardPanel().getTailItem(itemkeys[i]);
			if (item != null)
				item.setValue(values[i]);
		}
		// getButtonManager().getButton(nc.ui.wds.w800604.tcButtun.ITcButtun.cxmx).setEnabled(true);
	}

	@Override
	public boolean beforeEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		return super.beforeEdit(e);
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		if (e.getKey().equals("srl_pkr")) { // ���ѡ�е���վ ִ�й�ʽ
			getBillCardPanel().execHeadEditFormulas();
		}
		if (e.getKey().equals("cfd_xs")) {
			Object xs = getBillCardPanel().getBodyValueAt(
					getBillCardPanel().getBillTable().getSelectedRow(),
					"cfd_xs");
			Object sy = getBillCardPanel().getBodyValueAt(
					getBillCardPanel().getBillTable().getSelectedRow(),
					"cshengyu");
			if (null != sy && !"".equals(sy)) {
				double result = Double.parseDouble(sy.toString());
				if (result > 0) {
					if (null != xs && !"".equals(xs)) {
						double num = Double.parseDouble(xs.toString());
						if (num > 0) {
							if (result - num < 0) {
								this.showWarningMessage("�����������ʣ�������޷����!");
								getBillCardPanel().setBodyValueAt(null, getBillCardPanel().getBillTable().getSelectedRow(), "cfd_xs");
								getBillCardPanel().setBodyValueAt(null, getBillCardPanel().getBillTable().getSelectedRow(), "cfd_yfsl");
								return;
							}
						}
					}
				}
			}
		}
		super.afterEdit(e);
	}

	@Override
	protected void initEventListener() {
		// TODO Auto-generated method stub
		super.initEventListener(); // ��ʼ����Ӽ���
		getBillListPanel().getHeadTable().getSelectionModel()
				.addListSelectionListener(this); // �б��±�ͷ����
		getBillCardPanel().getBillTable().getSelectionModel()
				.addListSelectionListener(this); // ��Ƭ�±������
	}

	// �б�״̬��Value�����ı�
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		// getBillListPanel().getHeadTable().getSelectedRow();
		// �����ͷ����������0
		if (getBillListPanel().getHeadTable().getSelectedRowCount() > 0) {
			// Object i =
			// getBillListWrapper().getBillListPanel().getHeadItem("fyd_approstate").getValueObject();
			// ��ȡ��Ҫ�õ����ֶ�
			Object o = getBillListPanel().getHeadBillModel().getValueAt(
					getBillListPanel().getHeadTable().getSelectedRow(),
					"fyd_approstate");
			// �ж��Ƿ�Ϊ��
			if (o != null && o != "") {
				// ת������
				int i = Integer.parseInt(o.toString());
				// �ж��Ƿ�Ϊ 1 �� 0 ������ 1 ����ͨ�� 2 ��ͨ��
				if (i == 1) {
					// ���ð�ť״̬
					getButtonManager().getButton(IBillButton.Edit).setEnabled(
							false);
					getButtonManager().getButton(IBillButton.Delete)
							.setEnabled(false);
				} else {
					getButtonManager().getButton(IBillButton.Edit).setEnabled(
							true);
					getButtonManager().getButton(IBillButton.Delete)
							.setEnabled(true);
				}
			}
		}

	}

}
