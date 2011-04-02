package nc.ui.wds.infor;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillEditEvent;
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
		BillCardBeforeEditListener {

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
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
	}

	public boolean beforeEdit(BillItemEvent e) {
		// TODO Auto-generated method stub
		if (e.getItem().getKey().equals("pk_cargdoc")) {
			Object a = getBillCardPanel().getHeadItem("pk_stordoc")
					.getValueObject();
			UIRefPane panel = (UIRefPane) this.getBillCardPanel().getHeadItem(
					"pk_cargdoc").getComponent();
			if (null != a && !"".equals(a)) {

				panel.getRefModel().setWherePart("pk_stordoc='" + a + "'");
			} else {
				this.showErrorMessage("����ѡ��ֿ���Ϣ");
				panel.getRefModel().setWherePart("pk_stordoc='-1'");
			}
		}

		return false;
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		super.afterEdit(e);
		if (e.getKey().equals("pk_stordoc")) {
			getBillCardPanel().setHeadItem("pk_cargdoc", null);
		}
	}
}
