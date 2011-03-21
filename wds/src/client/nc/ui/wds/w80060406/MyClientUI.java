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
		getButtonManager().getButton(nc.ui.wds.w800604.tcButtun.ITcButtun.cxmx)
				.setEnabled(false);
	}

	public void setDefaultData() throws Exception {
		// 主要作用 给页面上的制单人赋值
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
		if (e.getKey().equals("srl_pkr")) { // 如果选中到货站 执行公式
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
								this.showWarningMessage("拆分数量大于剩余数量无法拆分!");
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
		super.initEventListener(); // 初始化添加监听
		getBillListPanel().getHeadTable().getSelectionModel()
				.addListSelectionListener(this); // 列表下表头监听
		getBillCardPanel().getBillTable().getSelectionModel()
				.addListSelectionListener(this); // 卡片下表体监听
	}

	// 列表状态下Value发生改变
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		// getBillListPanel().getHeadTable().getSelectedRow();
		// 如果表头数据量大于0
		if (getBillListPanel().getHeadTable().getSelectedRowCount() > 0) {
			// Object i =
			// getBillListWrapper().getBillListPanel().getHeadItem("fyd_approstate").getValueObject();
			// 获取想要得到的字段
			Object o = getBillListPanel().getHeadBillModel().getValueAt(
					getBillListPanel().getHeadTable().getSelectedRow(),
					"fyd_approstate");
			// 判断是否为空
			if (o != null && o != "") {
				// 转换类型
				int i = Integer.parseInt(o.toString());
				// 判断是否为 1 。 0 待审批 1 审批通过 2 不通过
				if (i == 1) {
					// 设置按钮状态
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
