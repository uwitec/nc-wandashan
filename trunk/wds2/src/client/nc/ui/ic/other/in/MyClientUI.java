package nc.ui.ic.other.in;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wds.w8000.CommonUnit;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.wds.w8004040210.TbGeneralHVO;

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

	public boolean beforeEdit(BillEditEvent e) {
		if (getBillCardPanel().isEnabled() == false) {
			return false;
		}
		// 这里如果单据上选择了客户，参选合同时，需要按客户过滤经销合同
		if ("invcode".equals(e.getKey())) {
			String st_type = "";
			String stordocName = "";
			boolean sotckIsTotal = true;
			try {
				st_type = nc.ui.wds.w8000.CommonUnit
						.getUserType(ClientEnvironment.getInstance().getUser()
								.getPrimaryKey());
			} catch (BusinessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
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
				if ("3".equals(st_type)) {
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

			String pk_cargdoc = "";
			try {
				pk_cargdoc = nc.ui.wds.w8000.CommonUnit
						.getCargDocName(ClientEnvironment.getInstance()
								.getUser().getPrimaryKey());
			} catch (BusinessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (pk_cargdoc != null && pk_cargdoc.length() > 0) {
				// 得到合同参照
				UIRefPane panel = (UIRefPane) this.getBillCardPanel()
						.getBodyItem("invcode").getComponent();
				// this.getBillCardPanel()
				// .getHeadItem("cif_pk").setDecimalDigits(iDecimalDigits);
				// 加上客户做为条件去过滤
				if (sotckIsTotal) {

					panel
							.getRefModel()
							.setWherePart(
									" pk_invbasdoc in (select pk_invbasdoc from tb_spacegoods where dr = 0 and pk_cargdoc = '"
											+ pk_cargdoc.toString()
											+ "') and dr=0 ");

				}
			}
		}
		if ("geb_customize2".equals(e.getKey())) {
			String st_type = "";
			String stordocName = "";
			boolean sotckIsTotal = true;
			try {
				st_type = nc.ui.wds.w8000.CommonUnit
						.getUserType(ClientEnvironment.getInstance().getUser()
								.getPrimaryKey());
			} catch (BusinessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
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
				if ("3".equals(st_type)) {
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

			String pk_cargdoc = "";
			try {
				pk_cargdoc = nc.ui.wds.w8000.CommonUnit
						.getCargDocName(ClientEnvironment.getInstance()
								.getUser().getPrimaryKey());
			} catch (BusinessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (pk_cargdoc != null && pk_cargdoc.length() > 0) {
				// 得到合同参照
				UIRefPane panel = (UIRefPane) this.getBillCardPanel()
						.getBodyItem("geb_customize2").getComponent();
				// this.getBillCardPanel()
				// .getHeadItem("cif_pk").setDecimalDigits(iDecimalDigits);
				// 加上客户做为条件去过滤

				panel.getRefModel().setWherePart(
						" pk_stordoc = '" + stordocName.toString()
								+ "' and dr=0 ");

			}
		}

		// if ("geb_virtualbnum".equals(e.getKey())) {
		// if (null != getBillCardPanel().getBillModel().getValueAt(
		// getBillCardPanel().getBillTable().getSelectedRow(),
		// "geb_isclose")) {
		// Boolean geb_iscloseuf = (Boolean) getBillCardPanel()
		// .getBillModel().getValueAt(
		// getBillCardPanel().getBillTable()
		// .getSelectedRow(), "geb_isclose");
		// boolean geb_isclose = geb_iscloseuf.booleanValue();
		// if (geb_isclose) {
		// getBillCardPanel().getBodyItem("geb_virtualbnum").setEdit(
		// false);
		// } else {
		// getBillCardPanel().getBodyItem("geb_virtualbnum").setEdit(
		// true);
		// }
		// // this.showErrorMessage("1");
		// } else {
		// getBillCardPanel().getBodyItem("geb_virtualbnum").setEdit(true);
		// }
		//
		// // this.showErrorMessage("1");
		// }

		// int i = 1;

		return true;
	}

	@Override
	protected void initEventListener() {
		// TODO Auto-generated method stub
		super.initEventListener();
		getBillListPanel().getHeadTable().getSelectionModel()
				.addListSelectionListener(this);
	}

	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		String isType = null;
		try {
			isType = CommonUnit.getUserType(ClientEnvironment.getInstance()
					.getUser().getPrimaryKey());
		} catch (BusinessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (null != isType && isType.equals("1")
				&& getBufferData().getVOBufferSize() > 0) {
			int index = 0;
			if (getBillListPanel().getHeadTable().getSelectedRow() != -1) {
				index = getBillListPanel().getHeadTable().getSelectedRow();
			}
			AggregatedValueObject billvo = getBufferData().getVOByRowNo(index);
			TbGeneralHVO generalhvo = (TbGeneralHVO) billvo.getParentVO(); //

			// 签字后
			if (null != generalhvo.getPwb_fbillflag()
					&& generalhvo.getPwb_fbillflag() == 3) {
				getButtonManager().getButton(
						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
						.setEnabled(false);
				getButtonManager().getButton(
						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
						.setEnabled(true);
				getButtonManager().getButton(IBillButton.Edit)
						.setEnabled(false);
			} else { // 签字前
				getButtonManager().getButton(
						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
						.setEnabled(true);
				getButtonManager().getButton(
						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
						.setEnabled(false);
				getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
			}
		} else if (null != isType && isType.equals("3")
				&& getBufferData().getVOBufferSize() > 0) {
			int index = 0;
			if (getBillListPanel().getHeadTable().getSelectedRow() != -1) {
				index = getBillListPanel().getHeadTable().getSelectedRow();
			}
			AggregatedValueObject billvo = getBufferData().getVOByRowNo(index);
			TbGeneralHVO generalhvo = (TbGeneralHVO) billvo.getParentVO(); //

			// 签字后
			if (null != generalhvo.getPwb_fbillflag()
					&& generalhvo.getPwb_fbillflag() == 3) {
				getButtonManager().getButton(
						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
						.setEnabled(false);
				getButtonManager().getButton(
						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
						.setEnabled(true);
				getButtonManager().getButton(IBillButton.Edit)
						.setEnabled(false);
			} else { // 签字前
				getButtonManager().getButton(
						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
						.setEnabled(true);
				getButtonManager().getButton(
						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
						.setEnabled(false);
				getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
			}
		}
		try {
			this.updateButtonUI();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
