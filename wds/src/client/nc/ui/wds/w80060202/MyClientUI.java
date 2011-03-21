package nc.ui.wds.w80060202;

import java.util.ArrayList;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.wds.w80060406.TbFydmxnewVO;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BDBusinessDelegator;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
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
		ListSelectionListener {

	MyEventHandler myEventHandler = (MyEventHandler) this.createEventHandler();

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

	//���ÿһ�е�ʱ�򴥷�
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
//		if (getBillListPanel().getHeadTable().getSelectedRowCount() > 0) {
//			//��ȡ����״̬
//			Object o = getBillListPanel().getHeadBillModel().getValueAt(
//					getBillListPanel().getHeadTable().getSelectedRow(),
//					"vbillstatus");
//			//�ж��Ƿ�Ϊ��
//			if (o != null && o != "") {
//				int i = Integer.parseInt(o.toString());
//				//���Ϊ1���ǵ����Ƶ���� 0 ���Ƶ�δ���
//				if (i == 1) {
//					//�����޸İ�ť״̬
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
