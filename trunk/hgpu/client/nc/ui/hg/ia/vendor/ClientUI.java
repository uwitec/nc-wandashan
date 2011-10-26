package nc.ui.hg.ia.vendor;

import javax.swing.ListSelectionModel;

import nc.ui.pub.bill.BillItem;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.hg.pu.pub.HgPuBtnConst;
import nc.vo.trade.button.ButtonVO;

/**
 * ��Ӧ��Ӧ������
 * 
 * @author zhw
 * 
 */
public class ClientUI extends BillCardUI {

	private static final long serialVersionUID = 859146761348083688L;

	public ClientUI() {
		super();
		init();
	}
	public ClientUI(String pk_corp, String pk_billType, String pk_busitype,
			String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
	}

	@Override
	protected ICardController createController() {
		return new ClientUICtrl(_getCorp().getPrimaryKey());
	}

	protected CardEventHandler createEventHandler() {
		return new ClientEventHandler(this, getUIControl());
	}

	@Override
	public String getRefBillType() {
		return null;
	}

	@Override
	protected void initSelfData() {
		//
		getBillCardPanel().getBillTable().setRowSelectionAllowed(true);
		getBillCardPanel().getBillTable().setSelectionMode(
				ListSelectionModel.SINGLE_INTERVAL_SELECTION);
	}


	@Override
	public void setDefaultData() throws Exception {

	}

	@Override
	public Object getUserObject() {
		return new CritiCHK();
	}

	public void afterEdit(nc.ui.pub.bill.BillEditEvent e) {
		super.afterEdit(e);
	}

	public boolean beforeEdit(nc.ui.pub.bill.BillEditEvent e) {
		return super.beforeEdit(e);
	}
	
	// ����Զ��尴ť
	public void initPrivateButton() {
		ButtonVO btnvo = new ButtonVO();
		btnvo.setBtnNo(HgPuBtnConst.FREEZE);
		btnvo.setBtnName("����");
		btnvo.setBtnChinaName("����");
		btnvo.setBtnCode(null);// code�������Ϊ��
		btnvo.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT});
		addPrivateButton(btnvo);
		
		ButtonVO btnvo1 = new ButtonVO();
		btnvo1.setBtnNo(HgPuBtnConst.UNFREEZE);
		btnvo1.setBtnName("�ⶳ");
		btnvo1.setBtnChinaName("�ⶳ");
		btnvo1.setBtnCode(null);// code�������Ϊ��
		btnvo1.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT});
		addPrivateButton(btnvo1);
		
		ButtonVO btnvo2 = new ButtonVO();
		btnvo2.setBtnNo(HgPuBtnConst.VIEW);
		btnvo2.setBtnName("�鿴");
		btnvo2.setBtnChinaName("�鿴");
		btnvo2.setBtnCode(null);// code�������Ϊ��
		btnvo2.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT});
		addPrivateButton(btnvo2);
		super.initPrivateButton();
	}
	
	private void init() {
		// getBillCardPanel().setAutoExecHeadEditFormula(true);
		// ��Ƭ���治Ҫ����0��ʾΪ��
		BillItem[] items = getBillCardPanel().getBodyItems();
		if ((items != null) && (items.length > 0)) {
			for (int i = 0; i < items.length; i++) {
				BillItem item = items[i];
				if ((item.getDataType() == BillItem.INTEGER)
						|| (item.getDataType() == BillItem.DECIMAL))
					if (item.isShow() && item.getNumberFormat() != null) {
						item.getNumberFormat().setShowZeroLikeNull(false);
					}
			}
		}
	}
}
