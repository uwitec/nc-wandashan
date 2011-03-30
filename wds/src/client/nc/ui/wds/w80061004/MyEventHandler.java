package nc.ui.wds.w80061004;

import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.query.HYQueryConditionDLG;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.query.ConditionVO;
import nc.vo.wds.w80021008.TbStockstaffVO;
import nc.vo.wds.w8004040204.TbOutgeneralHVO;

/**
 * 
 * 该类是AbstractMyEventHandler抽象类的实现类， 主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
 * 
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	// 判断用户身份
	private String st_type = "";
	// 判断是总仓还是分仓
	private boolean sotckIsTotal = true;

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {

		super(billUI, control);
		try {
			st_type = nc.ui.wds.w8000.CommonUnit.getUserType(ClientEnvironment
					.getInstance().getUser().getPrimaryKey());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 仓库主键
		String stordocName = "";
		if (null != st_type) {

			try {
				stordocName = nc.ui.wds.w8000.CommonUnit
						.getStordocName(ClientEnvironment.getInstance()
								.getUser().getPrimaryKey());
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (null != stordocName && !"".equals(stordocName)) {
				try {
					sotckIsTotal = nc.ui.wds.w8000.CommonUnit
							.getSotckIsTotal(stordocName);

				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	protected void onBoQuery() throws Exception {
		// TODO Auto-generated method stub
		// StringBuffer strWhere = new StringBuffer();
		// strWhere
		// .append(" billtype=1 and (fyd_constatus=0 or fyd_constatus is null)
		// and ");
		// if (askForQueryCondition(strWhere) == false)
		// return;// 用户放弃了查询
		// 2011.02.18 添加是否出库完毕查询

		UIDialog querydialog = getQueryUI();
		if (querydialog.showModal() != UIDialog.ID_OK) {
			return;
		}
		// 得到查询条件
		String strWhere = ((HYQueryConditionDLG) querydialog).getWhereSql();
		if (strWhere == null)
			strWhere = " 1=1 ";

		// 得到所有的查询标签
		ConditionVO[] ttc = ((HYQueryConditionDLG) querydialog)
				.getQryCondEditor().getGeneralCondtionVOs();
		// ConditionVO[] vos =
		// ((HYQueryConditionDLG)querydialog).getQryCondEditor().getGeneralCondtionVOs();
		String sendsignState = "";
		for (ConditionVO vo : ttc) {
			if ("xiugai".equals(vo.getFieldCode())) {
				sendsignState = vo.getValue();
				if ("Y".equals(sendsignState)) {
					strWhere = StringUtil
							.replaceIgnoreCase(strWhere, "xiugai = 'Y'",
									"fyd_pk in (select csourcebillhid from tb_outgeneral_b where dr=0 ) ");

				} else {
					strWhere = StringUtil
							.replaceIgnoreCase(strWhere, "xiugai = 'N'",
									" fyd_pk not in (select csourcebillhid from tb_outgeneral_b where dr=0 ) ");
				}
			}
			if ("xiugai".equals(vo.getFieldCode())) {

			}
		}

		// 添加条件限制
		strWhere += " and billtype=1 and (fyd_constatus=0 or fyd_constatus is null) and dr=0 ";
		SuperVO[] queryVos = queryHeadVOs(strWhere.toString());

		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
		// if (!sotckIsTotal) {
		// getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
		// } else {
		// getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
		//
		// }
		// if (sotckIsTotal) {
		// IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
		// IUAPQueryBS.class.getName());
		// Object o = getBillCardPanelWrapper().getBillCardPanel()
		// .getHeadItem("fyd_pk").getValueObject();
		// if (o != null && !"".equals(o.toString())) {
		//
		// String sqlout = " csourcebillhid ='" + o.toString()
		// + "' and dr=0 ";
		// ArrayList tboutVOs = null;
		// try {
		// tboutVOs = (ArrayList) query.retrieveByClause(
		// TbOutgeneralHVO.class, sqlout.toString());
		// } catch (BusinessException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		// if (null != tboutVOs && tboutVOs.size() > 0) {
		// getButtonManager().getButton(IBillButton.Edit).setEnabled(
		// false);
		//
		// } else {
		// getButtonManager().getButton(IBillButton.Edit).setEnabled(
		// true);
		// }
		// }
		// } else {
		// getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
		// }

		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		String sql = " cuserid ='"
				+ ClientEnvironment.getInstance().getUser().getPrimaryKey()
						.toString().trim() + "' and dr=0 ";

		ArrayList ttcs = new ArrayList();
		try {
			ttcs = (ArrayList) query
					.retrieveByClause(TbStockstaffVO.class, sql);
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
				Object o = getBillCardPanelWrapper().getBillCardPanel()
						.getHeadItem("fyd_pk").getValueObject();
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
				getButtonManager().getButton(IBillButton.Edit)
						.setEnabled(false);
			}
		} else {
			getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
		}
		getBillUI().updateButtonUI();
	}

	protected void addDataToBuffer(SuperVO[] queryVos) throws Exception {
		if (queryVos == null) {
			getBufferData().clear();
			return;
		}
		for (int i = 0; i < queryVos.length; i++) {
			AggregatedValueObject aVo = (AggregatedValueObject) Class.forName(
					getUIController().getBillVoName()[0]).newInstance();
			aVo.setParentVO(queryVos[i]);
			getBufferData().addVOToBuffer(aVo);
		}
	}

	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
		super.onBoSave();
		if (!sotckIsTotal) {
			getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
		} else {
			getButtonManager().getButton(IBillButton.Edit).setEnabled(true);

		}
		getBillUI().updateButtonUI();
	}

	@Override
	protected void onBoCancel() throws Exception {
		// TODO Auto-generated method stub
		super.onBoCancel();
		if (!sotckIsTotal) {
			getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
		} else {
			getButtonManager().getButton(IBillButton.Edit).setEnabled(true);

		}
		getBillUI().updateButtonUI();
	}

	@Override
	protected void onBoEdit() throws Exception {
		// TODO Auto-generated method stub
		// if (!sotckIsTotal) {
		// getBillUI().showErrorMessage("您没有权限修改单据！");
		// return;
		// }

		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		Object o = getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				"fyd_pk").getValueObject();
		if (o != null && !"".equals(o.toString())) {
			String sqlout = " csourcebillhid ='" + o.toString() + "' and dr=0 ";
			ArrayList tboutVOs = null;
			try {
				tboutVOs = (ArrayList) query.retrieveByClause(
						TbOutgeneralHVO.class, sqlout.toString());
			} catch (BusinessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (null != tboutVOs && tboutVOs.size() > 0) {
				getBillUI().showErrorMessage("单据以出库完毕，不能修改！");
				return;
			}
		}

		super.onBoEdit();

	}
}
