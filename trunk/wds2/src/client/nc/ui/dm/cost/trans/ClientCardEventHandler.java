package nc.ui.dm.cost.trans;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.base.AbstractBillUI;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.dm.cost.trans.TransmlVO;

public class ClientCardEventHandler extends CardEventHandler {

	public ClientCardEventHandler(BillCardUI billUI, ICardController control) {
		super(billUI, control);

	}

	@Override
	protected void onBoEdit() throws Exception {

		int[] selectRows = getBillCardPanelWrapper().getBillCardPanel()
				.getBillTable().getSelectedRows();
		if (selectRows.length <= 0) {
			throw new Exception("��ѡ��ĳһ�к����޸�");
		}

		super.onBoEdit();

		BillModel model = getBillCardPanelWrapper().getBillCardPanel()
				.getBodyPanel().getTableModel();
		// �趨modelΪ�б༭״̬
		model.setRowEditState(true);
		// ���ѡ�ж���ֻ�����һ����Ϊ�༭״̬
		for (int i = 0; i < selectRows.length; i++) {

			model.setEditRow(selectRows[i]);

		}

	}

	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {

		// ��õ���UI���õ��ݵĲ���״̬Ϊ�ɱ༭
		getBillUI().setBillOperate(IBillOperate.OP_EDIT);
		// ����һ�еĲ���
		onBoLineAdd();
		// ��λ�ȡ��ǰ��ѡ�еĵ��ݵ�ĳ�м�¼
		int selectRow = getBillCardPanelWrapper().getBillCardPanel()
				.getBillTable().getSelectedRow();
		// ����ҵ��ģ��
		BillModel model = getBillCardPanelWrapper().getBillCardPanel()
				.getBodyPanel().getTableModel();
		// ���� modelΪ�б༭״̬
		model.setRowEditState(true);
		// ����model�Ŀɱ༭��Ϊ
		model.setEditRow(selectRow);
	}

	@Override
	protected void onBoDelete() throws Exception {
		// ɾ�������б�ѡ�е���
		getBillCardPanelWrapper().deleteSelectedLines();
		// ִ�б��淽����ȡ����仯VO�����̣�����buffer ���趨��ǰ�У�
		onBoSave();
	}

	// ��Ӧ��ť�¼�֮ǰ��Ԥ����
	@Override
	protected void buttonActionBefore(AbstractBillUI billUI, int intBtn)
			throws Exception {

		super.buttonActionBefore(billUI, intBtn);
		//������ѧϰ���Դ���
	BillItem[] bills=getBillCardPanelWrapper().getBillCardPanel().getBillData().getHeadTailItems();
	
		
		
		//ѧϰ���Դ������
		if (IBillButton.Save == intBtn) {
			Object[] obs = getBillCardPanelWrapper().getBillVOFromUI()
					.getChildrenVO();

			if (obs != null) {
				TransmlVO[] tras = (TransmlVO[]) obs;
				for (int i = 0; i < tras.length; i++) {
					if (tras[i].getDfirstdate() == null
							|| tras[i].getDlastdate() == null
							|| tras[i].getDmile() == null
							|| tras[i].getPk_vfirstrecord() == null
							|| tras[i].getPk_vlastrecord() == null
							|| tras[i].getPk_vrecplace() == null
							|| tras[i].getPk_vrecplace() == null) {

						throw new Exception("Ԫ�ز���Ϊ��");

					}

					if (tras[i].getDfirstdate().after(tras[i].getDlastdate())) {

						throw new Exception("���ά�����ڲ���С�ڳ��μ�¼����");
					}
					if (tras[i].getPk_vdelplace().equalsIgnoreCase(
							tras[i].getPk_vrecplace())) {

						throw new Exception("�ջ��ص㲻�ܺͷ����ص�һ����");
					}

				}
			}

		}

	}

}
