package nc.ui.wds.w8004061002;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillBodyMenuListener;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BDBusinessDelegator;
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
		nc.ui.pub.bill.BillEditListener2,BillCardBeforeEditListener {

	@Override
	protected void initEventListener() {
		// TODO Auto-generated method stub
		super.initEventListener();
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
		getBillCardPanel().addBodyEditListener2(this);
//		 getBillCardPanel().addBillEditListenerHeadTail(billEditListener)
	
	}

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
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		if (e.getKey().equals("invname")) {
			getBillCardPanel().execHeadEditFormulas();
		}
		super.afterEdit(e);
	}
	public boolean beforeEdit(BillEditEvent e){
		if (getBillCardPanel().isEnabled() == false) {
			return false;
		}
		// �������������ѡ���˿ͻ�����ѡ��ͬʱ����Ҫ���ͻ����˾�����ͬ
		if ("invname".equals(e.getKey())) {
			String pk_cargdoc = (String) this.getBillCardPanel().getHeadItem(
					"pk_cargdoc").getValueObject();
			if (pk_cargdoc != null && pk_cargdoc.length() > 0) {
				// �õ���ͬ����
				UIRefPane panel = (UIRefPane) this.getBillCardPanel()
						.getBodyItem("invname").getComponent();
				// this.getBillCardPanel()
				// .getHeadItem("cif_pk").setDecimalDigits(iDecimalDigits);
				// ���Ͽͻ���Ϊ����ȥ����
				panel
						.getRefModel()
						.setWherePart(
								" pk_invbasdoc in (select pk_invbasdoc from tb_spacegoods where dr = 0 and pk_cargdoc = '"
										+ pk_cargdoc.toString() + "') and dr=0 ");
			}
		}
		return true;
	}

	public boolean beforeEdit(BillItemEvent e) {
		if (getBillCardPanel().isEnabled() == false) {
			return false;
		}
		// �������������ѡ���˿ͻ�����ѡ��ͬʱ����Ҫ���ͻ����˾�����ͬ
		if ("invname".equals(e.getItem().getKey())) {
			String pk_cargdoc = (String) this.getBillCardPanel().getHeadItem(
					"pk_cargdoc").getValueObject();
			if (pk_cargdoc != null && pk_cargdoc.length() > 0) {
				// �õ���ͬ����
				UIRefPane panel = (UIRefPane) this.getBillCardPanel()
						.getHeadItem("invname").getComponent();
				// this.getBillCardPanel()
				// .getHeadItem("cif_pk").setDecimalDigits(iDecimalDigits);
				// ���Ͽͻ���Ϊ����ȥ����
				panel
						.getRefModel()
						.setWherePart(
								" pk_invbasdoc in (select pk_invbasdoc from tb_spacestock where dr = 0 and pk_cargdoc = '"
										+ pk_cargdoc.toString() + "') and dr=0 ");
			} else {

				this.showErrorMessage("��û��ѡ���λ��������ʾ��Ϣ");
				UIRefPane panel = (UIRefPane) this.getBillCardPanel()
						.getHeadItem("invname").getComponent();
				// ���Ͽͻ���Ϊ����ȥ����
				panel.getRefModel().setWherePart(" dr=0 and 1=2  ");
			}
		}
		return true;
	}

}
