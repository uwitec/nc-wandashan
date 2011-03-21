package nc.ui.wds.w80061004;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.wds.w80021008.TbStockstaffVO;
import nc.vo.wds.w8004040204.TbOutgeneralHVO;
import nc.vo.wds.w80060406.TbFydmxnewVO;
import nc.vo.wds.w80060804.TbTranscompanyVO;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BDBusinessDelegator;
import nc.ui.trade.button.IBillButton;
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

		// �ж��Ƿ�Ϊ������Ϣ
		if (e.getKey().equals("cif_pk")) {
			// ��ȡ������Ϣֵ
			String cif_pk = (String) this.getBillCardPanel().getHeadItem(
					"cif_pk").getValueObject();
			// �ж��Ƿ�Ϊ��
			if (cif_pk != null && cif_pk.length() > 0) {
				// ��ȡ˾���绰�Ĳ���
				UIRefPane panel = (UIRefPane) this.getBillCardPanel()
						.getHeadItem("fyd_sjdh").getComponent();
				// �Գ�����ϢΪ������Ϊ��ѯ���� ��ѯ������Ϣ��ϵ�˺͵绰
				panel.getRefModel().setWherePart(" cif_pk = '" + cif_pk + "' ");
			}
		}
		super.afterEdit(e);
	}

	@Override
	protected void initEventListener() {
		// TODO Auto-generated method stub
		super.initEventListener();
		getBillListPanel().getHeadTable().getSelectionModel()
				.addListSelectionListener(this);
	}

	// ���ÿһ�е�ʱ�򴥷�
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		if (getBillListPanel().getHeadTable().getSelectedRowCount() > 0) {
			// //��ȡ����״̬
			// Object o = getBillListPanel().getHeadBillModel().getValueAt(
			// getBillListPanel().getHeadTable().getSelectedRow(),
			// "vbillstatus");
			// //�ж��Ƿ�Ϊ��
			// if (o != null && o != "") {
			// int i = Integer.parseInt(o.toString());
			// //���Ϊ1���ǵ����Ƶ���� 0 ���Ƶ�δ���
			// if (i == 1) {
			// //�����޸İ�ť״̬
			// getButtonManager().getButton(IBillButton.Edit).setEnabled(
			// false);
			// } else {
			// getButtonManager().getButton(IBillButton.Edit).setEnabled(
			// true);
			// }
			// }
			// �ж����ֻܲ��Ƿֲ�
			boolean sotckIsTotal = true;
			// �ֿ�����
			String stordocName = "";

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
			String sql = " cuserid ='"
					+ ClientEnvironment.getInstance().getUser().getPrimaryKey()
							.toString().trim() + "' and dr=0 ";

			ArrayList ttcs = new ArrayList();
			try {
				ttcs = (ArrayList) query.retrieveByClause(TbStockstaffVO.class,
						sql);
			} catch (BusinessException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			TbStockstaffVO[] ttcVO = new TbStockstaffVO[ttcs.size()];
			ttcs.toArray(ttcVO);
			if (ttcVO.length > 0 && null != ttcVO[0]
					&& null != ttcVO[0].getIstepi()) {
				UFBoolean tepi = ttcVO[0].getIstepi();
				if (tepi.booleanValue()) {
					Object o = getBillListPanel().getHeadBillModel()
							.getValueAt(
									getBillListPanel().getHeadTable()
											.getSelectedRow(), "fyd_pk");
					if (o != null && !"".equals(o.toString())) {
						String sqlout = " csourcebillhid ='" + o.toString()
								+ "' and dr=0 ";
						ArrayList tboutVOs = null;
						try {
							tboutVOs = (ArrayList) query.retrieveByClause(
									TbOutgeneralHVO.class, sqlout.toString());
						} catch (BusinessException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						if (null != tboutVOs && tboutVOs.size() > 0) {
							getButtonManager().getButton(IBillButton.Edit)
									.setEnabled(false);
						} else {
							getButtonManager().getButton(IBillButton.Edit)
									.setEnabled(true);
						}
					}
				} else {
					getButtonManager().getButton(IBillButton.Edit).setEnabled(
							false);
				}
			} else {
				getButtonManager().getButton(IBillButton.Edit)
						.setEnabled(false);
			}
		}
	}
}
