package nc.ui.wds.w80061006;

import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * <b> 在此处简要描述此类的功能 </b>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 *
 * @author author
 * @version tempProject version
 */
public class MyClientUI extends AbstractMyClientUI implements BillCardBeforeEditListener {

	@Override
	protected void initEventListener() {
		// TODO Auto-generated method stub
		super.initEventListener();
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
		
		//		getBillCardPanel().addBillEditListenerHeadTail(billEditListener)


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

	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		if (e.getKey().equals("tc_pk")) {
			getBillCardPanel().execHeadEditFormulas();
			String tc_pk = (String) this.getBillCardPanel()
					.getHeadItem("tc_pk").getValueObject();
			if (tc_pk != null && tc_pk.length() > 0) {
				getBillCardPanel().getHeadItem("cif_pk").setEnabled(true);
				// 得到合同参照
				UIRefPane panel = (UIRefPane) this.getBillCardPanel()
						.getHeadItem("cif_pk").getComponent();
				//加上客户做为条件去过滤
				panel.getRefModel().setWherePart(" tc_pk = '" + tc_pk + "' ");
			} else {
				getBillCardPanel().getHeadItem("cif_pk").setEnabled(false);
			}
		}

		super.afterEdit(e);
	}

	public boolean beforeEdit(BillItemEvent e) {
		// 这里如果单据上选择了客户，参选合同时，需要按客户过滤经销合同
		if ("tc_pk".equals(e.getItem().getKey())) {
			String tc_pk = (String) this.getBillCardPanel()
					.getHeadItem("tc_pk").getValueObject();
			if (tc_pk != null && tc_pk.length() > 0) {
				// 得到合同参照
				UIRefPane panel = (UIRefPane) this.getBillCardPanel()
						.getHeadItem("cif_pk").getComponent();
				//加上客户做为条件去过滤
				panel.getRefModel().setWherePart(" tc_pk = '" + tc_pk + "' ");
			}else{
				return false;
			}
		}
		return true;
	}

}
