package nc.ui.wds.ic.so.out;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wds.w8000.CommonUnit;
import nc.vo.ic.other.out.TbOutgeneralHVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * <b> 销售出库 </b>
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

	/**
	 * 
	 */
	private static final long serialVersionUID = 6680473320457515722L;

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
		getBillListPanel().getHeadTable().getSelectionModel()
				.addListSelectionListener(this);
		getBillListPanel().getHeadTable().setSelectionMode(
				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

	}

	@Override
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub

		// if (e.getKey().equals("nmny")) {
		// int index = getBillCardPanel().getBillTable().getSelectedRow();
		// Object o = getBillCardPanel()
		// .getBodyValueAt(index, "noutassistnum");
		// if (null != o && !"".equals(o)) {
		// int num = Integer.parseInt(o.toString());
		// Object n = getBillCardPanel().getBodyValueAt(index, "nmny");
		// if (null != n && !"".equals(n)) {
		// double nmny = Double.parseDouble(n.toString());
		// getBillCardPanel().setBodyValueAt(nmny / num, index,
		// "nprice");
		// }
		// } else {
		// getBillCardPanel().setBodyValueAt(null, index, "nmny");
		// }
		// }

		super.afterEdit(e);
	}

	public void valueChanged(ListSelectionEvent arg0) {
		// TODO Auto-generated method stub
		String isType = null;
		try {
			isType = CommonUnit.getUserType(ClientEnvironment.getInstance()
					.getUser().getPrimaryKey());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int index = 0;
		TbOutgeneralHVO generalhvo = null;
		if (getBufferData().getVOBufferSize() > 0) {
			if (getBillListPanel().getHeadTable().getSelectedRow() != -1) {
				index = getBillListPanel().getHeadTable().getSelectedRow();
			}
			AggregatedValueObject billvo = getBufferData().getVOByRowNo(index);
			generalhvo = (TbOutgeneralHVO) billvo.getParentVO(); //
		} else
			return;

		if ((null != isType && isType.equals("1"))
				|| (null != isType && isType.equals("3"))) {

			// 控制拆分单据禁止在该界面签字
			if (null != generalhvo.getVbilltype()
					&& generalhvo.getVbilltype().equals("3")) {
				getButtonManager().getButton(
						nc.ui.wds.w80060206.buttun0206.ISsButtun.Qzqr)
						.setEnabled(false);
				getButtonManager().getButton(
						nc.ui.wds.w80060206.buttun0206.ISsButtun.Qxqz)
						.setEnabled(false);
				return;
			}
			// 签字后
			if (null != generalhvo.getVbillstatus()
					&& generalhvo.getVbillstatus() == 0) {
				this.setButtonEnabled(false);
				getButtonManager().getButton(IBillButton.Edit)
						.setEnabled(false);
			} else if (null != generalhvo.getVbillstatus()
					&& generalhvo.getVbillstatus() == 1) { // 签字前
				this.setButtonEnabled(true);
				getButtonManager().getButton(IBillButton.Edit)
						.setEnabled(false);
			} else { // 运单还没有确认的
				getButtonManager().getButton(
						nc.ui.wds.w80060206.buttun0206.ISsButtun.Qzqr)
						.setEnabled(false);
				getButtonManager().getButton(
						nc.ui.wds.w80060206.buttun0206.ISsButtun.Qxqz)
						.setEnabled(false);
				if (isType.equals("3")) {
					getButtonManager().getButton(IBillButton.Edit).setEnabled(
							true);
				} else
					getButtonManager().getButton(IBillButton.Edit).setEnabled(
							false);
			}
		} else if ((null != isType && isType.equals("2"))) {
			if (null == generalhvo.getVbillstatus()) {
				getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
			} else if (null != generalhvo.getVbillstatus()
					&& (generalhvo.getVbillstatus() == 1 || generalhvo
							.getVbillstatus() == 0)) {
				getButtonManager().getButton(IBillButton.Edit)
						.setEnabled(false);
			}
		}

	}

	/**
	 * 控制签字，取消签字和修改按钮状态
	 */
	private void setButtonEnabled(boolean value) {
		getButtonManager().getButton(
				nc.ui.wds.w80060206.buttun0206.ISsButtun.Qzqr)
				.setEnabled(value);
		getButtonManager().getButton(
				nc.ui.wds.w80060206.buttun0206.ISsButtun.Qxqz).setEnabled(
				!value);

	}
}
