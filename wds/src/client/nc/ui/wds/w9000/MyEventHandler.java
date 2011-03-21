package nc.ui.wds.w9000;

import org.apache.poi.hssf.record.formula.functions.Upper;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.ui.pub.bill.BillItem;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.IListController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.list.BillListUI;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.wds.w9000.BdInvbasdocVO;
import nc.vo.wds.w9000.MyBillVO;

/**
 *
 *������AbstractMyEventHandler�������ʵ���࣬
 *��Ҫ�������˰�ť��ִ�ж������û����Զ���Щ����������Ҫ�����޸�
 *@author author
 *@version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	private MyClientUI myUI = null;

	public MyEventHandler(BillListUI billUI, IListController control) {
		super(billUI, control);
		myUI = (MyClientUI) billUI;
	}

	@Override
	protected void onBoEdit() throws Exception {
		// TODO Auto-generated method stub
		super.onBoEdit();
		BillItem billItems[] = myUI.getBillListPanel().getHeadBillModel()
				.getBodyItems();
		billItems[64].setEnabled(true);
		billItems[64].setEdit(true);
	}

	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub

		int rownum = getBillListPanelWrapper().getBillListPanel()
				.getHeadBillModel().getRowCount();
		for (int i = 0; i < rownum; i++) {
			String def20 = (String) getBillListPanelWrapper()
					.getBillListPanel().getHeadTable().getValueAt(i, 4);
			if (null != def20) {
				if (!def20.matches("\\d+")) {
					getBillUI().showErrorMessage("��������������֣�");
					return;
				}
			}

		}
		// �ж��Ƿ��д�������
		BdInvbasdocVO[] b = (BdInvbasdocVO[]) getBillListPanelWrapper()
				.getBillListPanel().getHeadBillModel().getBodyValueVOs(
						BdInvbasdocVO.class.getName());
		IVOPersistence iPersistence=(IVOPersistence) NCLocator.getInstance().lookup(
				IVOPersistence.class.getName());
		iPersistence.updateVOArray(b);
		AggregatedValueObject billVO = new MyBillVO();
		billVO.setParentVO(b[0]);
		setTSFormBufferToVO(billVO);
		AggregatedValueObject checkVO = new MyBillVO();
		checkVO.setParentVO(b[0]);
		setTSFormBufferToVO(checkVO);
		// �����������
		Object o = null;
		ISingleController sCtrl = null;
		if (getUIController() instanceof ISingleController) {
			sCtrl = (ISingleController) getUIController();
			if (sCtrl.isSingleDetail()) {
				o = billVO.getParentVO();
				billVO.setParentVO(null);
			} else {
				o = billVO.getChildrenVO();
				billVO.setChildrenVO(null);
			}
		}

		boolean isSave = true;

		

		// �������ݻָ�����
		if (sCtrl != null) {
			if (sCtrl.isSingleDetail())
				billVO.setParentVO((CircularlyAccessibleValueObject) o);
		}
		int nCurrentRow = -1;
		if (isSave) {
			if (isEditing()) {
				if (getBufferData().isVOBufferEmpty()) {
					getBufferData().addVOToBuffer(billVO);
					nCurrentRow = 0;

				} else {
					getBufferData().setCurrentVO(billVO);
					nCurrentRow = getBufferData().getCurrentRow();
				}
			}
			// �������������
			setAddNewOperate(isAdding(), billVO);
		}
		// ���ñ����״̬
		getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
		if (nCurrentRow >= 0) {
			getBufferData().setCurrentRow(nCurrentRow);
		}

	}

}