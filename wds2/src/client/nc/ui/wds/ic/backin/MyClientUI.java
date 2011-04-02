package nc.ui.wds.ic.backin;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.ic.other.out.TbOutgeneralHVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.wl.pub.CommonUnit;

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
	}

	public void setDefaultData() throws Exception {
	}

	@Override
	protected void initEventListener() {
		// TODO Auto-generated method stub
		super.initEventListener();
		getBillListPanel().getHeadTable().getSelectionModel()
				.addListSelectionListener(this);
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
		if (null != isType && isType.equals("1")
				&& getBufferData().getVOBufferSize() > 0) {
			int index = 0;
			if (getBillListPanel().getHeadTable().getSelectedRow() != -1) {
				index = getBillListPanel().getHeadTable().getSelectedRow();
			}
			AggregatedValueObject billvo = getBufferData().getVOByRowNo(index);
			TbOutgeneralHVO generalhvo = (TbOutgeneralHVO) billvo.getParentVO(); //
			// ���Ʋ�ֵ��ݽ�ֹ�ڸý���ǩ��
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
			// ǩ�ֺ�
			if (null != generalhvo.getVbillstatus()
					&& generalhvo.getVbillstatus() == 0) {
				this.setButtonEnabled(false);
			} else if (null != generalhvo.getVbillstatus()
					&& generalhvo.getVbillstatus() == 1) { // ǩ��ǰ
				this.setButtonEnabled(true);
			}
		} else if (null != isType && isType.equals("0")
				&& getBufferData().getVOBufferSize() > 0) {
			int index = 0;
			if (getBillListPanel().getHeadTable().getSelectedRow() != -1) {
				index = getBillListPanel().getHeadTable().getSelectedRow();
			}
			AggregatedValueObject billvo = getBufferData().getVOByRowNo(index);
			TbOutgeneralHVO generalhvo = (TbOutgeneralHVO) billvo.getParentVO();
			if (null == generalhvo.getVbillstatus()) {
				// getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
			} else if (null != generalhvo.getVbillstatus()
					&& (generalhvo.getVbillstatus() == 1 || generalhvo
							.getVbillstatus() == 0)) {
				// getButtonManager().getButton(IBillButton.Edit)
				// .setEnabled(false);
			}
		} else if (null != isType && isType.equals("3")
				&& getBufferData().getVOBufferSize() > 0) {
			int index = 0;
			if (getBillListPanel().getHeadTable().getSelectedRow() != -1) {
				index = getBillListPanel().getHeadTable().getSelectedRow();
			}
			AggregatedValueObject billvo = getBufferData().getVOByRowNo(index);
			TbOutgeneralHVO generalhvo = (TbOutgeneralHVO) billvo.getParentVO(); //
			// ���Ʋ�ֵ��ݽ�ֹ�ڸý���ǩ��
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
			// ǩ�ֺ�
			if (null != generalhvo.getVbillstatus()
					&& generalhvo.getVbillstatus() == 0) {
				this.setButtonEnabled(false);
			} else if (null != generalhvo.getVbillstatus()
					&& generalhvo.getVbillstatus() == 1) { // ǩ��ǰ
				this.setButtonEnabled(true);
			}
		}
		// try {
		// this.updateButtonUI();
		// } catch (Exception e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		//		}

	}

	/**
	 * ����ǩ�֣�ȡ��ǩ�ֺ��޸İ�ť״̬
	 */
	private void setButtonEnabled(boolean value) {
		getButtonManager().getButton(
				nc.ui.wds.w80060206.buttun0206.ISsButtun.Qzqr)
				.setEnabled(value);
		getButtonManager().getButton(
				nc.ui.wds.w80060206.buttun0206.ISsButtun.Qxqz).setEnabled(
				!value);
		// getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
	}
}
