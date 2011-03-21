package nc.ui.wds.w8004040212;

import java.util.ArrayList;
import java.util.List;

import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.ui.pub.ClientEnvironment;
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

	@Override
	protected void initEventListener() {
		// TODO Auto-generated method stub
		super.initEventListener();
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
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
		if (e.getKey().equals("pk_invbasdoc")) {
			getBillCardPanel().execHeadEditFormulas();
		}
		// String tc_pk = (String) this.getBillCardPanel()
		// .getHeadItem("tc_pk").getValueObject();
		// if (tc_pk != null && tc_pk.length() > 0) {
		// getBillCardPanel().getHeadItem("cif_pk").setEnabled(true);
		// // �õ���ͬ����
		// UIRefPane panel = (UIRefPane) this.getBillCardPanel()
		// .getHeadItem("cif_pk").getComponent();
		// //���Ͽͻ���Ϊ����ȥ����
		// panel.getRefModel().setWherePart(" tc_pk = '" + tc_pk + "' ");
		// } else {
		// getBillCardPanel().getHeadItem("cif_pk").setEnabled(false);
		// }
		// }

		super.afterEdit(e);
	}

	public boolean beforeEdit(BillItemEvent e) {
		// TODO Auto-generated method stub
		if (getBillCardPanel().isEnabled() == false) {
			return false;
		}
		if ("pplpt_pk".equals(e.getItem().getKey())) {
			List invLisk = new ArrayList();
			try {
				invLisk = nc.ui.wds.w8000.CommonUnit
						.getInvbasdoc_Pk(ClientEnvironment.getInstance()
								.getUser().getPrimaryKey());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String pk_cargdoc = "";
			try {
				pk_cargdoc = nc.ui.wds.w8000.CommonUnit
						.getCargDocName(ClientEnvironment.getInstance()
								.getUser().getPrimaryKey());
			} catch (BusinessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String st_type = "";
			try {
				st_type = nc.ui.wds.w8000.CommonUnit
						.getUserType(ClientEnvironment.getInstance().getUser()
								.getPrimaryKey());
			} catch (BusinessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			String stordocName = "";
			boolean sotckIsTotal = true;
			if (null != st_type && !"".equals(st_type)) {
				if ("0".equals(st_type)) {
					try {
						stordocName = nc.ui.wds.w8000.CommonUnit
								.getStordocName(ClientEnvironment.getInstance()
										.getUser().getPrimaryKey());
					} catch (BusinessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if (null != stordocName && !"".equals(stordocName)) {
						try {
							sotckIsTotal = nc.ui.wds.w8000.CommonUnit
									.getSotckIsTotal(stordocName);

						} catch (BusinessException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			}

			UIRefPane panel = (UIRefPane) this.getBillCardPanel().getHeadItem(
					"pplpt_pk").getComponent();
			
			StringBuffer strWhere = new StringBuffer();
			if (null != st_type && "0".equals(st_type)) {
				if (sotckIsTotal) {

					strWhere.append("  pk_cargdoc = '" + pk_cargdoc.toString()
							+ "' and dr=0 " + " and cdt_traystatus=0 ");

				} else {

					if (null != pk_cargdoc && !"".equals(pk_cargdoc)) {
						strWhere.append(" pk_cargdoc = '"
								+ pk_cargdoc.toString() + "' and dr=0 ");
						/* cdt_traystatus=1 and */
					} else {
						strWhere.append(" 1=2 ");
					}
				}
			} else {
				strWhere.append(" 1=2 ");
			}
			panel.getRefModel().setWherePart(strWhere.toString());

		}
		// if ("pk_yjzbzj".equals(e.getItem().getKey())) {
		// String cif_pk = (String) this.getBillCardPanel().getHeadItem(
		// "cif_pk").getValueObject();
		// Object fyd_yjzl = this.getBillCardPanel().getHeadItem("fyd_yjzl")
		// .getValueObject();
		//
		// if (cif_pk != null && cif_pk.length() > 0 && fyd_yjzl != null) {
		// // �õ���ͬ����
		// UIRefPane panel = (UIRefPane) this.getBillCardPanel()
		// .getHeadItem("pk_yjzbzj").getComponent();
		// UIRefPane panelp = (UIRefPane) this.getBillCardPanel()
		// .getHeadItem("yjby1").getComponent();
		// UIRefPane panelt = (UIRefPane) this.getBillCardPanel()
		// .getHeadItem("yjby2").getComponent();
		// if (Integer.parseInt(fyd_yjzl + "") == 0) {
		// panel.setRefNodeName(panelt.getRefNodeName());
		// panel.setRefModel(panelt.getRefModel());
		// panel.getRefModel().setWherePart(
		// " tc_pk = '" + cif_pk + "' and dr=0 ");
		// }
		// if (Integer.parseInt(fyd_yjzl + "") == 1) {
		// panel.setRefNodeName(panelp.getRefNodeName());
		// panel.setRefModel(panelp.getRefModel());
		// panel.getRefModel().setWherePart(
		// " tc_pk = '" + cif_pk + "' and dr=0 ");
		// }
		//
		// } else {
		//
		// this.showErrorMessage("��û��ѡ�����乫˾���˼����࣬������ʾ�κλ�λ");
		// UIRefPane panel = (UIRefPane) this.getBillCardPanel()
		// .getHeadItem("pk_yjzbzj").getComponent();
		// // ���Ͽͻ���Ϊ����ȥ����
		// panel.getRefModel().setWherePart(" dr=0 and 1=2 ");
		//
		// }
		// }

		return true;
	}
}
