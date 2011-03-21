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
 * <b> 在此处简要描述此类的功能 </b>
 * 
 * <p>
 * 在此处添加此类的描述信息
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

		// 判断是否为车辆信息
		if (e.getKey().equals("cif_pk")) {
			// 获取车辆信息值
			String cif_pk = (String) this.getBillCardPanel().getHeadItem(
					"cif_pk").getValueObject();
			// 判断是否为空
			if (cif_pk != null && cif_pk.length() > 0) {
				// 获取司机电话的参照
				UIRefPane panel = (UIRefPane) this.getBillCardPanel()
						.getHeadItem("fyd_sjdh").getComponent();
				// 以车辆信息为条件作为查询条件 查询车辆信息联系人和电话
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

	// 点击每一行的时候触发
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		if (getBillListPanel().getHeadTable().getSelectedRowCount() > 0) {
			// //获取单据状态
			// Object o = getBillListPanel().getHeadBillModel().getValueAt(
			// getBillListPanel().getHeadTable().getSelectedRow(),
			// "vbillstatus");
			// //判断是否为空
			// if (o != null && o != "") {
			// int i = Integer.parseInt(o.toString());
			// //如果为1就是单据制单完成 0 是制单未完成
			// if (i == 1) {
			// //设置修改按钮状态
			// getButtonManager().getButton(IBillButton.Edit).setEnabled(
			// false);
			// } else {
			// getButtonManager().getButton(IBillButton.Edit).setEnabled(
			// true);
			// }
			// }
			// 判断是总仓还是分仓
			boolean sotckIsTotal = true;
			// 仓库主键
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
