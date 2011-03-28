package nc.ui.wds.w8004040208;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wds.w8000.CommonUnit;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.wds.w8004040204.TbOutgeneralHVO;

/**
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b>
 * 
 * <p>
 * ��������
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
		getBillCardPanel().getBillTable().getSelectionModel()
				.addListSelectionListener(this);
		getBillListPanel().getHeadTable().getSelectionModel()
				.addListSelectionListener(this);
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		if (e.getKey().equals("nshouldoutassistnum")) {
			// ������
			Object a = getBillCardPanel().getBodyValueAt(
					getBillCardPanel().getBillTable().getSelectedRow(), "hsl");
			// Ӧ��������
			Object b = getBillCardPanel().getBodyValueAt(
					getBillCardPanel().getBillTable().getSelectedRow(),
					"nshouldoutassistnum");
			if (null != a && !"".equals(a) && null != b && !"".equals(b)) {
				double num = Double.parseDouble(a.toString());
				double num1 = Double.parseDouble(b.toString());
				// ����Ӧ���������ͻ��������Ӧ��������
				if (num != 0 && num1 != 0)
					getBillCardPanel().setBodyValueAt(num * num1,
							getBillCardPanel().getBillTable().getSelectedRow(),
							"nshouldoutnum");
			}
		}
		if (e.getKey().equals("srl_pkr")) {
			// getBillCardPanel().getHeadItem("").getValueObject();
			// getBillCardPanel().setHeadItem("vdiliveraddress", "123");
			getBillCardPanel().execHeadEditFormulas();
		}
		if (e.getKey().equals("ccunhuobianma")) {
			String code = (String) getBillCardPanel().getBodyValueAt(
					getBillCardPanel().getBillTable().getSelectedRow(),
					"ccunhuobianma");
			if (null == code) {
				getBillCardPanel().setBodyValueAt(null,
						getBillCardPanel().getBillTable().getSelectedRow(),
						"vbatchcode");
				getBillCardPanel().setBodyValueAt(null,
						getBillCardPanel().getBillTable().getSelectedRow(),
						"nshouldoutassistnum");
				getBillCardPanel().setBodyValueAt(null,
						getBillCardPanel().getBillTable().getSelectedRow(),
						"nshouldoutnum");
				getBillCardPanel().setBodyValueAt(null,
						getBillCardPanel().getBillTable().getSelectedRow(),
						"noutnum");
				getBillCardPanel().setBodyValueAt(null,
						getBillCardPanel().getBillTable().getSelectedRow(),
						"noutassistnum");
			}

		}

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

			// ǩ�ֺ�
			if (null != generalhvo.getVbillstatus()
					&& generalhvo.getVbillstatus() == 0) {
				this.setButtonEnabled(false);
				getButtonManager().getButton(IBillButton.Edit)
						.setEnabled(false);
			} else if (null != generalhvo.getVbillstatus()
					&& generalhvo.getVbillstatus() == 1) { // ǩ��ǰ
				this.setButtonEnabled(true);
				getButtonManager().getButton(IBillButton.Edit)
						.setEnabled(false);
			} else { // �˵���û��ȷ�ϵ�
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
		} else if (null != isType && isType.equals("2")
				&& getBufferData().getVOBufferSize() > 0) {
			if (null != generalhvo.getVbilltype()) {
				if (null == generalhvo.getVbillstatus()) {
					getButtonManager().getButton(IBillButton.Edit).setEnabled(
							true);
				} else  {
					getButtonManager().getButton(IBillButton.Edit).setEnabled(
							false);
				} 
			}
		}
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
//		getButtonManager().getButton(IBillButton.Edit).setEnabled(value);
	}

}
