package nc.ui.wds.w80060604;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BDBusinessDelegator;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wds.w8004040204.ssButtun.ISsButtun;

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
		getButtonManager().getButton(ISsButtun.cz).setEnabled(false);
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

	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
//		if (getBillListPanel().getHeadTable().getSelectedRowCount() > 0) {
//			// ��ȡ��Ҫ�õ����ֶ�
//			Object o = getBillListPanel().getHeadBillModel().getValueAt(
//					getBillListPanel().getHeadTable().getSelectedRow(),
//					"iprintcount");
//			// �ж��Ƿ�Ϊ�� ���ò������ť�Ƿ���ʾ
//			if (null != o && o != "") {
//				getButtonManager().getButton(
//						nc.ui.wds.w8004040204.ssButtun.ISsButtun.cz)
//						.setEnabled(false);
//				getBillListPanel().getHeadBillModel().setValueAt(false,
//						getBillListPanel().getHeadTable().getSelectedRow(),
//						"binitflag");
//				getBillListPanel().getHeadItem("binitflag").setEnabled(false);
//			} else {
//				getButtonManager().getButton(
//						nc.ui.wds.w8004040204.ssButtun.ISsButtun.cz)
//						.setEnabled(true);
//				getBillListPanel().getHeadItem("binitflag").setEnabled(true);
//			}
//		}
	}

}
