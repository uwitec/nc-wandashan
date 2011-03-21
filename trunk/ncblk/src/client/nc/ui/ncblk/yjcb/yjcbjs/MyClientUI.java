package nc.ui.ncblk.yjcb.yjcbjs;

import nc.vo.pub.CircularlyAccessibleValueObject;
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
public class MyClientUI extends AbstractMyClientUI {

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

	@Override
	protected void initPrivateButton() {
		// TODO Auto-generated method stub
		super.initPrivateButton();
		addPrivateButton(PrivateButtonUtils.getJsButton());
		addPrivateButton(PrivateButtonUtils.getQrButton());
		addPrivateButton(PrivateButtonUtils.getTzButton());
	}
	


	public void setDefaultData() throws Exception {
		String pk_corp = getEnvironment().getCorporation().getPk_corp();
		getBillCardPanel().setHeadItem("pk_corp", pk_corp);
		getBillCardPanel().setTailItem("voperatorid",
				getEnvironment().getUser().getPrimaryKey());
		getBillCardPanel().setTailItem("dmakedate", getEnvironment().getDate());
	}

}
