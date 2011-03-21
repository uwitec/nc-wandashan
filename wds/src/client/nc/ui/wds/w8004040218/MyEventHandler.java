package nc.ui.wds.w8004040218;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.wds.w8004040204.Iw8004040204;
import nc.itf.wds.w8004040210.Iw8004040210;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.BusinessAction;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.businessaction.IBusinessController;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.query.INormalQuery;
import nc.ui.wds.w8000.CommonUnit;
import nc.ui.wds.w8004040204.ssButtun.ISsButtun;
import nc.ui.wds.w8004040210.TrayAction;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.bill.BillRendererVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;
import nc.vo.to.pub.ConstVO;
import nc.vo.wds.pub.WDSTools;
import nc.vo.wds.w8004040204.TbOutgeneralBVO;
import nc.vo.wds.w8004040204.TbOutgeneralHVO;
import nc.vo.wds.w8004040210.TbGeneralBBVO;
import nc.vo.wds.w8004040212.TbWarehousestockVO;
import nc.vo.wds.w8004040218.MyBillVO;
import nc.vo.wds.w8004061002.BdCargdocTrayVO;
import nc.vo.wds.w80060604.SoSaleVO;
import nc.vo.wds.w80060604.SoSaleorderBVO;
import nc.vo.wds.w80060608.IcGeneralBVO;
import nc.vo.wds.w80060608.IcGeneralHVO;
import nc.vo.wds.w8006080206.TbStorcubasdocVO;

/**
 * 
 * 销售出库
 * 
 * @author author xzs
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	MyClientUI myClientUI = null;

	// 调用接口
	private Iw8004040204 iw = null;

	private SaleOrderDlg saleOrderDlg = null; // 查询方法的类

	boolean isAdd = false;

	boolean opType = false; // 是否赠品
	private boolean isedit = true;// 是否是修改
	private String st_type = "";// 身份

	// private List hiddenList = null;
	private String pk_stock = ""; // 当前登录者对应的仓库主键
	IUAPQueryBS iuap = (IUAPQueryBS) NCLocator.getInstance().lookup(
			IUAPQueryBS.class.getName());

	private Integer isControl = 0; // 是否有权限操作当前单据 1为可以签字的用户 0 不能进行任何操作 2 可以进行出库
	private boolean isStock = false; // 是否为总仓 true 总仓 false 分仓
	// 根据当前登录者查询所属仓库和其仓库所存储的产品
	private List pkList = null;

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
		myClientUI = (MyClientUI) billUI;
		// 初始化接口
		this.setIw((Iw8004040204) NCLocator.getInstance().lookup(
				Iw8004040204.class.getName()));
		// 无论什么角色第一次加载托盘指定按钮和自动取货都设置不可用
		changeButton(false);
		try {
			// 获取当前登录者查询人员绑定表 是属于哪个仓库，
			pk_stock = CommonUnit.getStordocName(ClientEnvironment
					.getInstance().getUser().getPrimaryKey());
			// 获取用户类型
			String isType = CommonUnit.getUserType(ClientEnvironment
					.getInstance().getUser().getPrimaryKey());
			st_type = isType;
			if (null != isType && isType.equals("0")) {
				isStock = CommonUnit.getSotckIsTotal(pk_stock);
				pkList = CommonUnit.getInvbasdoc_Pk(ClientEnvironment
						.getInstance().getUser().getPrimaryKey());
				isControl = 2;
			} else if (null != isType && isType.equals("1")) {
				isControl = 1;
				// getButtonManager().getButton(IBillButton.Add).setEnabled(false);
				// myClientUI.updateButtons();
			} else {
				isControl = 0;
			}
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// // 判断用户身份
		// String st_type = "";
		// try {
		// st_type = nc.ui.wds.w8000.CommonUnit.getUserType(ClientEnvironment
		// .getInstance().getUser().getPrimaryKey());
		// } catch (BusinessException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// if (null != st_type && !"".equals(st_type)) {
		// if (!"0".equals(st_type)) {
		// getButtonManager().getButton(IBillButton.Add).setEnabled(false);
		// }
		// }

		// getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
	}

	protected IBusinessController createBusinessAction() {
		// TODO Auto-generated method stub
		switch (getUIController().getBusinessActionType()) {
		case IBusinessActionType.PLATFORM:

			return new BusinessAction(getBillUI());
		case IBusinessActionType.BD:

			return new TrayAction(getBillUI());

		default:

			return new BusinessAction(getBillUI());
		}

	}

	// 清除颜色
	private void noColor(TbOutgeneralBVO[] generalb) {
		if (null != generalb && generalb.length > 0) {
			for (int i = 0; i < generalb.length; i++) {
				getBillCardPanelWrapper().getBillCardPanel().getBodyPanel()
						.setCellBackGround(i, "ccunhuobianma", Color.white);
			}
		}
	}

	@Override
	protected void onBoEdit() throws Exception {
		// 总仓而且是库管员（暂时的）
		if (!"3".equals(st_type)) {

			if (isControl == 2) {
				super.onBoEdit();
				// 设置托盘指定按钮可用，修改状态
				isAdd = false;
				getButtonManager().getButton(ISsButtun.tpzd).setEnabled(true);
				myClientUI.updateButtons();
			} else {
				myClientUI.showErrorMessage("您没有权限");
			}
		}

	}

	/**
	 * 根据参数改变托盘指定和自动取货按钮是否可用
	 * 
	 * @param type
	 */
	private void changeButton(boolean type) {
		getButtonManager().getButton(ISsButtun.zdqh).setEnabled(type);
		getButtonManager().getButton(ISsButtun.tpzd).setEnabled(type);
		getButtonManager().getButton(
				nc.ui.wds.w80060206.buttun0206.ISsButtun.Qzqr).setEnabled(type);
		getButtonManager().getButton(
				nc.ui.wds.w80060206.buttun0206.ISsButtun.Qxqz).setEnabled(type);
		myClientUI.updateButtons();
	}

	// 刷新
	@Override
	protected void onBoRefresh() throws Exception {
		// TODO Auto-generated method stub
		super.onBoRefresh();
	}

	@Override
	protected void onBoQuery() throws Exception {
		String st_type = "";
		try {
			st_type = nc.ui.wds.w8000.CommonUnit.getUserType(ClientEnvironment
					.getInstance().getUser().getPrimaryKey());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// 用户放弃了查询
		strWhere.append(" and vsourcebillcode like '%退%' and dr=0 ");

		if ("0".equals(st_type)) {
			String pk_stordoc = nc.ui.wds.w8000.CommonUnit
					.getStordocName(ClientEnvironment.getInstance().getUser()
							.getPrimaryKey());

			strWhere.append(" and srl_pk='" + pk_stordoc + "' ");

		}
		if ("3".equals(st_type)) {
			if (!isStock) {
				String pk_stordoc = nc.ui.wds.w8000.CommonUnit
						.getStordocName(ClientEnvironment.getInstance()
								.getUser().getPrimaryKey());

				strWhere.append(" and srl_pk='" + pk_stordoc + "' ");
			}

		}

		SuperVO[] queryVos = queryHeadVOs(strWhere.toString());

		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
//		super.onBoReturn();
	}

	/**
	 * 重写该方法，修改(isnull(dr,0)=0) 弹出查询对话框向用户询问查询条件。
	 * 如果用户在对话框点击了“确定”，那么返回true,否则返回false 查询条件通过传入的StringBuffer返回给调用者
	 * 
	 * @param sqlWhereBuf
	 *            保存查询条件的StringBuffer
	 * @return 用户选确定返回true否则返回false
	 */
	protected boolean askForQueryCondition(StringBuffer sqlWhereBuf)
			throws Exception {
		if (sqlWhereBuf == null)
			throw new IllegalArgumentException(
					"askForQueryCondition().sqlWhereBuf cann't be null");
		UIDialog querydialog = getQueryUI();

		if (querydialog.showModal() != UIDialog.ID_OK)
			return false;
		INormalQuery query = (INormalQuery) querydialog;

		String strWhere = query.getWhereSql();
		if (strWhere == null)
			strWhere = "1=1";

		if (getButtonManager().getButton(IBillButton.Busitype) != null) {
			if (getBillIsUseBusiCode().booleanValue())
				// 业务类型编码
				strWhere = "(" + strWhere + ") and "
						+ getBillField().getField_BusiCode() + "='"
						+ getBillUI().getBusicode() + "'";

			else
				// 业务类型
				strWhere = "(" + strWhere + ") and "
						+ getBillField().getField_Busitype() + "='"
						+ getBillUI().getBusinessType() + "'";

		}

		strWhere = "(" + strWhere + ") and (isnull(tb_outgeneral_h.dr,0)=0)";

		if (getHeadCondition() != null)
			strWhere = strWhere + " and " + getHeadCondition();
		// 现在我先直接把这个拼好的串放到StringBuffer中而不去优化拼串的过程
		sqlWhereBuf.append(strWhere);
		return true;
	}

	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub

		// 判断用户身份
		String st_type = "";
		try {
			st_type = nc.ui.wds.w8000.CommonUnit.getUserType(ClientEnvironment
					.getInstance().getUser().getPrimaryKey());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (null != st_type && !"".equals(st_type)) {
			if (!"0".equals(st_type) && !"3".equals(st_type)) {
				getBillUI().showErrorMessage("您没有权限操作此单据！");
				return;
			}
		} else {
			getBillUI().showErrorMessage("您没有权限操作此单据！");
			return;
		}
		if ("0".equals(st_type)) {
			String pk_stordoc = nc.ui.wds.w8000.CommonUnit
					.getStordocName(ClientEnvironment.getInstance().getUser()
							.getPrimaryKey());
			String pk_stordocnow = "";
			if (null != getBillCardPanelWrapper().getBillCardPanel()
					.getHeadTailItem("srl_pk")) {
				pk_stordocnow = getBillCardPanelWrapper().getBillCardPanel()
						.getHeadTailItem("srl_pk").getValue();
			}

			if (!pk_stordoc.equals(pk_stordocnow)) {
				getBillUI().showErrorMessage("入库仓库和保管员仓库不符，不能保存！");
				return;
			}
		}
		if ("3".equals(st_type)) {
			String pk_stordoc = nc.ui.wds.w8000.CommonUnit
					.getStordocName(ClientEnvironment.getInstance().getUser()
							.getPrimaryKey());
			String pk_stordocnow = "";
			if (null != getBillCardPanelWrapper().getBillCardPanel()
					.getHeadTailItem("srl_pk")) {
				pk_stordocnow = getBillCardPanelWrapper().getBillCardPanel()
						.getHeadTailItem("srl_pk").getValue();
			}

			if (!pk_stordoc.equals(pk_stordocnow)) {
				getBillUI().showErrorMessage("入库仓库和保管员仓库不符，不能保存！");
				return;
			}
		}
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());

		int bodyrownum = getBillCardPanelWrapper().getBillCardPanel()
				.getBillTable().getRowCount();
		if (bodyrownum == 0) {
			getBillUI().showErrorMessage("子表不能为空！");
			return;
		}
		// 验证货品主键
		for (int i = 0; i < bodyrownum; i++) {
			String geb_cinvbasidy = (String) getBillCardPanelWrapper()
					.getBillCardPanel().getBillModel().getValueAt(i,
							"cinventoryid");
			if (null == geb_cinvbasidy || "".equals(geb_cinvbasidy)) {
				getBillUI().showErrorMessage("货品不能为空!");
				return;
			}
			// 验证批次号是否为空
			String geb_vbatchcodey = (String) getBillCardPanelWrapper()
					.getBillCardPanel().getBillModel().getValueAt(i,
							"vbatchcode");
			if (null == geb_vbatchcodey || "".equals(geb_vbatchcodey)) {
				getBillUI().showErrorMessage("批次号不能为空!");
				return;
			}
			// 验证应入数量
			UFDouble geb_bsnumy = (UFDouble) getBillCardPanelWrapper()
					.getBillCardPanel().getBillModel().getValueAt(i,
							"nshouldoutassistnum");
			if (null == geb_bsnumy) {
				getBillUI().showErrorMessage("应入数量不能为空!");
				return;
			}
			// 验证批次号是否正确
			if (geb_vbatchcodey.trim().length() < 8) {
				getBillUI().showErrorMessage("批次号不能小于8位!");
				return;
			}

			Pattern p = Pattern
					.compile(
							"^((((1[6-9]|[2-9]\\d)\\d{2})(0?[13578]|1[02])(0?[1-9]|[12]\\d|3[01]))|"
									+ "(((1[6-9]|[2-9]\\d)\\d{2})(0?[13456789]|1[012])(0?[1-9]|[12]\\d|30))|"
									+ "(((1[6-9]|[2-9]\\d)\\d{2})0?2(0?[1-9]|1\\d|2[0-8]))|"
									+ "(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))0?229))$",
							Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
			Matcher m = p.matcher(geb_vbatchcodey.trim().substring(0, 8));
			if (!m.find()) {
				getBillUI().showErrorMessage(
						"批次号输入的不正确,请您输入正确的日期!如：20100101XXXXXX");
				return;
			}

		}
		// 非发运入库

		// 判断调拨入库是否完成
		if ("0".equals(st_type)) {
			int rowNum = getBillCardPanelWrapper().getBillCardPanel()
					.getBillTable().getRowCount();
			for (int i = 0; i < rowNum; i++) {
				double geb_bsnum = 0;
				double geb_snum = 0;
				double geb_banum = 0;
				double geb_anum = 0;
				// 应入辅数量
				if (null != getBillCardPanelWrapper().getBillCardPanel()
						.getBillModel().getValueAt(i, "nshouldoutassistnum")) {
					geb_bsnum = Double.parseDouble(getBillCardPanelWrapper()
							.getBillCardPanel().getBillModel().getValueAt(i,
									"nshouldoutassistnum").toString());
				}
				// 应入数量
				if (null != getBillCardPanelWrapper().getBillCardPanel()
						.getBillModel().getValueAt(i, "nshouldoutnum")) {
					geb_snum = Double.parseDouble(getBillCardPanelWrapper()
							.getBillCardPanel().getBillModel().getValueAt(i,
									"nshouldoutnum").toString());
				}
				// 实入辅数量
				if (null != getBillCardPanelWrapper().getBillCardPanel()
						.getBillModel().getValueAt(i, "noutassistnum")) {
					geb_banum = Double.parseDouble(getBillCardPanelWrapper()
							.getBillCardPanel().getBillModel().getValueAt(i,
									"noutassistnum").toString());
				}
				// 实入数量
				if (null != getBillCardPanelWrapper().getBillCardPanel()
						.getBillModel().getValueAt(i, "noutnum")) {
					geb_anum = Double.parseDouble(getBillCardPanelWrapper()
							.getBillCardPanel().getBillModel().getValueAt(i,
									"noutnum").toString());
				}
				if (geb_bsnum != geb_banum || geb_snum != geb_anum) {
					getBillUI().showErrorMessage("应入库、实入库数量不等，不能保存！");
					return;
				}
			}
		}
		if ("3".equals(st_type)) {
			int rowNum = getBillCardPanelWrapper().getBillCardPanel()
					.getBillTable().getRowCount();
			for (int i = 0; i < rowNum; i++) {
				double geb_bsnum = 0;
				double geb_snum = 0;
				double geb_banum = 0;
				double geb_anum = 0;
				// 应入辅数量
				if (null != getBillCardPanelWrapper().getBillCardPanel()
						.getBillModel().getValueAt(i, "nshouldoutassistnum")) {
					geb_bsnum = Double.parseDouble(getBillCardPanelWrapper()
							.getBillCardPanel().getBillModel().getValueAt(i,
									"nshouldoutassistnum").toString());
				}
				// 应入数量
				if (null != getBillCardPanelWrapper().getBillCardPanel()
						.getBillModel().getValueAt(i, "nshouldoutnum")) {
					geb_snum = Double.parseDouble(getBillCardPanelWrapper()
							.getBillCardPanel().getBillModel().getValueAt(i,
									"nshouldoutnum").toString());
				}
				// 实入辅数量
				if (null != getBillCardPanelWrapper().getBillCardPanel()
						.getBillModel().getValueAt(i, "noutassistnum")) {
					geb_banum = Double.parseDouble(getBillCardPanelWrapper()
							.getBillCardPanel().getBillModel().getValueAt(i,
									"noutassistnum").toString());
				}
				// 实入数量
				if (null != getBillCardPanelWrapper().getBillCardPanel()
						.getBillModel().getValueAt(i, "noutnum")) {
					geb_anum = Double.parseDouble(getBillCardPanelWrapper()
							.getBillCardPanel().getBillModel().getValueAt(i,
									"noutnum").toString());
				}
				if (geb_bsnum != geb_banum || geb_snum != geb_anum) {
					getBillUI().showErrorMessage("应入库、实入库数量不等，不能保存！");
					return;
				}
			}
		}

		// 得到全部表体VO

		TbOutgeneralBVO[] tbGeneralBVOss = null;
		if (getBillManageUI().isListPanelSelected()) {
			// if (getBufferData().getVOBufferSize() != 0) {
			tbGeneralBVOss = (TbOutgeneralBVO[]) getBillManageUI()
					.getBillListWrapper().getVOFromUI().getChildrenVO();
			// }
		} else {
			// if (getBufferData().getVOBufferSize() != 0) {
			tbGeneralBVOss = (TbOutgeneralBVO[]) getBillCardPanelWrapper()
					.getBillVOFromUI().getChildrenVO();
			// }
		}
		// 判断表体是否有数据
		if (null == tbGeneralBVOss || tbGeneralBVOss.length == 0) {
			getBillUI().showErrorMessage("表体中没有数据，不能进行操作或无权操作！");
			return;
		}

		// if (null != st_type && !"".equals(st_type)) {
		// if ("1".equals(st_type)) {
		// super.onBoSave();
		// return;
		// }
		// }

		// getBillCardPanelWrapper().getBillCardPanel()
		// .getHeadItem("geh_billtype").setValue(new Integer(0));
		AggregatedValueObject billVO = getBillUI().getVOFromUI();

		// if (!addoredit) {
		// for (int i = 0; i < billVO.getChildrenVO().length; i++) {
		// billVO.getChildrenVO()[i].setStatus(VOStatus.UPDATED);
		// }
		// } else {
		// for (int i = 0; i < billVO.getChildrenVO().length; i++) {
		// billVO.getChildrenVO()[i].setStatus(VOStatus.NEW);
		//
		// }
		// }

		billVO.getChildrenVO()[0].setStatus(1);
		setTSFormBufferToVO(billVO);
		AggregatedValueObject checkVO = getBillUI().getVOFromUI();
		setTSFormBufferToVO(checkVO);

		//
		TbOutgeneralBVO[] tbGeneralBVOas = (TbOutgeneralBVO[]) billVO
				.getChildrenVO();
		// 验证表体是否有数据
		if (null == tbGeneralBVOas || tbGeneralBVOas.length < 0) {
			myClientUI.showErrorMessage("操作失败,您无权操作");
			return;
		}
		// 循环验证是否有没有出库的产品

		for (int i = 0; i < tbGeneralBVOas.length; i++) {
			TbOutgeneralBVO genb = tbGeneralBVOas[i];
			if (null != genb.getNshouldoutassistnum()
					&& !"".equals(genb.getNshouldoutassistnum())) {
				if (null != genb.getNoutassistnum()
						&& !"".equals(genb.getNoutassistnum())) {
					if (genb.getNshouldoutassistnum().doubleValue() != genb
							.getNoutassistnum().doubleValue()) {

						getBillUI().showErrorMessage("您有产品没有指定入库");
						return;
					}
				}
			}
		}

		// 总仓和分仓的
		boolean addoredit = true;

		TbOutgeneralHVO generalh = (TbOutgeneralHVO) billVO.getParentVO();
		if (isStock) {

			// 根据保管员货品，保存或修改自己货品子表
			List invLisk = nc.ui.wds.w8000.CommonUnit
					.getInvbasdoc_Pk(ClientEnvironment.getInstance().getUser()
							.getPrimaryKey());

			TbOutgeneralHVO tmpgeneralh = null;
			// 根据来源单据号查询是否有做过出库
			String strWhere = " vbillcode='" + generalh.getVbillcode()
					+ "' and csourcebillhid='" + generalh.getCsourcebillhid()
					+ "' and dr=0 ";
			ArrayList tmpList = (ArrayList) query.retrieveByClause(
					TbOutgeneralHVO.class, strWhere);
			if (null != tmpList && tmpList.size() > 0) {
				tmpgeneralh = (TbOutgeneralHVO) tmpList.get(0);
			}
			// boolean myisadd=true;
			// 该单据有过出库记录
			if (null != tmpgeneralh) {
				// 把表头替换
				generalh = tmpgeneralh;
				String gebbvosql = " dr=0 and general_pk ='"
						+ generalh.getGeneral_pk() + "' and cinventoryid in ('";
				for (int k = 0; k < invLisk.size(); k++) {
					if (null != invLisk && invLisk.size() > 0
							&& null != invLisk.get(k)
							&& !"".equals(invLisk.get(k))) {
						gebbvosql += invLisk.get(k) + "','";
					}
				}
				gebbvosql += "') ";
				ArrayList gebbvos = (ArrayList) query.retrieveByClause(
						TbOutgeneralBVO.class, gebbvosql);
				if (null != gebbvos && gebbvos.size() > 0) {
					addoredit = false;
				} else {
					addoredit = true;
				}

			} else {
				addoredit = true;
				// myisadd=true;
				// 制单时间
				generalh.setTmaketime(myClientUI._getServerTime().toString());
				// 制单日期，单据日期
				generalh.setDbilldate(new UFDate(new Date()));
				// generalh.setGeh_dbilldate(new UFDate(new Date()));
				// 设置单据号
				generalh.setVbillcode(CommonUnit.getBillCode("4A",
						ClientEnvironment.getInstance().getCorporation()
								.getPk_corp(), "", ""));
			}

			// 如果是新增设置表体状态“新增”
			if (addoredit) {
				// 循环表体更改状态
				for (int i = 0; i < tbGeneralBVOas.length; i++) {
					tbGeneralBVOas[i].setStatus(VOStatus.NEW);
				}
			} else { // 设置表体状态“修改”
				// 循环表体更改状态
				for (int i = 0; i < tbGeneralBVOas.length; i++) {
					tbGeneralBVOas[i].setStatus(VOStatus.UPDATED);
				}
			}
		} else {
			// 根据保管员货品，保存或修改自己货品子表
			List invLisk = nc.ui.wds.w8000.CommonUnit
					.getInvbasdoc_Pk(ClientEnvironment.getInstance().getUser()
							.getPrimaryKey());

			TbOutgeneralHVO tmpgeneralh = null;
			// 根据来源单据号查询是否有做过出库
			String strWhere = " vbillcode='" + generalh.getVbillcode()
					+ "' and csourcebillhid='" + generalh.getCsourcebillhid()
					+ "' and dr=0 ";
			ArrayList tmpList = (ArrayList) query.retrieveByClause(
					TbOutgeneralHVO.class, strWhere);
			if (null != tmpList && tmpList.size() > 0) {
				tmpgeneralh = (TbOutgeneralHVO) tmpList.get(0);
				addoredit = false;
			} else {
				addoredit = true;
				// 制单时间
				generalh.setTmaketime(myClientUI._getServerTime().toString());
				// 制单日期，单据日期
				generalh.setDbilldate(new UFDate(new Date()));
				// generalh.setGeh_dbilldate(new UFDate(new Date()));
				// 设置单据号
				generalh.setVbillcode(CommonUnit.getBillCode("4A",
						ClientEnvironment.getInstance().getCorporation()
								.getPk_corp(), "", ""));
			}
			// 如果是新增设置表体状态“新增”
			if (addoredit) {
				// 循环表体更改状态
				for (int i = 0; i < tbGeneralBVOas.length; i++) {
					tbGeneralBVOas[i].setStatus(VOStatus.NEW);
				}
			} else { // 设置表体状态“修改”
				// 循环表体更改状态
				for (int i = 0; i < tbGeneralBVOas.length; i++) {
					tbGeneralBVOas[i].setStatus(VOStatus.UPDATED);
				}
			}

		}
		// 最后修改时间
		generalh.setTlastmoditime(myClientUI._getServerTime().toString());
		// 设置修改人
		generalh.setClastmodiid(ClientEnvironment.getInstance().getUser()
				.getPrimaryKey());
		// 单据状态
		generalh.setVbillstatus(1);
		// 给聚合VO中表头赋值
		billVO.setParentVO(generalh);
		// 表体赋值
		billVO.setChildrenVO(tbGeneralBVOas);

		// // 单据是否关闭
		// TbGeneralBVO[] tbGenBVOs = (TbGeneralBVO[]) billVO.getChildrenVO();
		// // 在途集合子表
		// ArrayList vgebvos = new ArrayList();
		// // 在库存存储
		// double geb_virtualbnumy = 0;
		// double geb_virtualnumy = 0;
		//
		// for (int i = 0; i < tbGenBVOs.length; i++) {
		// if (null != tbGenBVOs[i].getGeb_banum()
		// && null != tbGenBVOs[i].getGeb_anum()) {
		// if (tbGenBVOs[i].getGeb_bsnum().toDouble().doubleValue() ==
		// tbGenBVOs[i]
		// .getGeb_banum().toDouble().doubleValue()
		// && tbGenBVOs[i].getGeb_anum().toDouble().doubleValue() ==
		// tbGenBVOs[i]
		// .getGeb_snum().toDouble().doubleValue()) {
		// ((TbGeneralBVO[]) billVO.getChildrenVO())[i]
		// .setGeb_isclose(new UFBoolean("Y"));
		// } else {
		// ((TbGeneralBVO[]) billVO.getChildrenVO())[i]
		// .setGeb_isclose(new UFBoolean("N"));
		// }
		// } else {
		// ((TbGeneralBVO[]) billVO.getChildrenVO())[i]
		// .setGeb_isclose(new UFBoolean("N"));
		// }
		// }

		for (int i = 0; i < billVO.getChildrenVO().length; i++) {
			if (null == ((TbOutgeneralBVO[]) billVO.getChildrenVO())[i]
					.getLvbatchcode()
					|| ""
							.equals(((TbOutgeneralBVO[]) billVO.getChildrenVO())[i]
									.getLvbatchcode().trim())) {
				((TbOutgeneralBVO[]) billVO.getChildrenVO())[i]
						.setLvbatchcode("2009");
			}

		}
		// 进行数据晴空
		Object o = null;
		ISingleController sCtrl = null;
		if (getUIController() instanceof ISingleController) {
			sCtrl = (ISingleController) getUIController();
			if (sCtrl.isSingleDetail()) {
				o = billVO.getParentVO();
				billVO.setParentVO(null);
			} else {
				o = billVO.getChildrenVO();
				billVO.setChildrenVO(null);
			}
		}

		boolean isSave = true;

		// 判断是否有存盘数据
		if (billVO.getParentVO() == null
				&& (billVO.getChildrenVO() == null || billVO.getChildrenVO().length == 0)) {
			isSave = false;
		} else {
			ArrayList params = new ArrayList();

			// 获得要回写的集合VO
			// GeneralBillVO voTempBill = getVoTempBill();
			// 保存方法

			params.add(getBillUI().getUserObject());
			// 回写数据
			params.add(null);
			// 库存表数据
			TbOutgeneralBVO[] tbGeneralBVOs = (TbOutgeneralBVO[]) getBillUI()
					.getVOFromUI().getChildrenVO();
			//
			// TbGeneralHVO abs = (TbGeneralHVO) getBillUI().getVOFromUI()
			// .getParentVO();

			// 通过出库单子表主键得到存库数据
			StringBuffer tgbbsql = new StringBuffer(" geb_pk in ('");
			if (null != tbGeneralBVOs && tbGeneralBVOs.length > 0) {
				for (int i = 0; i < tbGeneralBVOs.length; i++) {
					if (null != tbGeneralBVOs[i].getCsourcebillbid()
							&& !"".equals(tbGeneralBVOs[i].getCsourcebillbid())) {
						tgbbsql.append(tbGeneralBVOs[i].getCsourcebillbid());
						tgbbsql.append("','");
					}

				}
				tgbbsql.append("') and dr=0 ");
			}
			// 用户权限判断

			// 托盘缓存表

			ArrayList tbGeneralBBVOs = (ArrayList) query.retrieveByClause(
					TbGeneralBBVO.class, tgbbsql.toString());
			if (null == tbGeneralBBVOs || tbGeneralBBVOs.size() == 0) {
				getBillUI().showErrorMessage("请指定托盘后再保存！");
				return;
			}
			// 库存VO
			TbWarehousestockVO[] tbWarehousestockVO = new TbWarehousestockVO[tbGeneralBBVOs
					.size()];
			ArrayList bdCargdocTrayVOs = new ArrayList();
			String Cdt_pk = "";
			for (int i = 0; i < tbGeneralBBVOs.size(); i++) {
				if (null != tbGeneralBBVOs.get(i)) {
					TbGeneralBBVO tbbbvo = (TbGeneralBBVO) tbGeneralBBVOs
							.get(i);
					tbWarehousestockVO[i] = new TbWarehousestockVO();
					// 托盘主键
					if (null != tbbbvo.getCdt_pk()
							&& !"".equals(tbbbvo.getCdt_pk())) {
						tbWarehousestockVO[i].setPplpt_pk(tbbbvo.getCdt_pk());
					}
					// dr
					tbWarehousestockVO[i].setDr(0);
					// 辅数量
					if (null != tbbbvo.gebb_num) {
						tbWarehousestockVO[i].setWhs_stockpieces(tbbbvo
								.getGebb_num());
						tbWarehousestockVO[i].setWhs_oanum(tbbbvo.gebb_num);
					}
					// 换算率
					if (null != tbbbvo.getGebb_hsl()) {
						tbWarehousestockVO[i].setWhs_hsl(tbbbvo.getGebb_hsl());
					}
					// 单价
					if (null != tbbbvo.getGebb_nprice()) {
						tbWarehousestockVO[i].setWhs_nprice(tbbbvo
								.getGebb_nprice());
					}
					// 金额
					if (null != tbbbvo.getGebb_nmny()) {
						tbWarehousestockVO[i]
								.setWhs_nmny(tbbbvo.getGebb_nmny());
					}
					// 主数量
					if (null != tbbbvo.getGebb_num()
							&& null != tbbbvo.getGebb_hsl()) {
						tbWarehousestockVO[i].setWhs_stocktonnage(new UFDouble(
								tbbbvo.getGebb_num().toDouble()
										* tbbbvo.getGebb_hsl().toDouble()));
						tbWarehousestockVO[i].setWhs_omnum(new UFDouble(tbbbvo
								.getGebb_num().toDouble()
								* tbbbvo.getGebb_hsl().toDouble()));

					}

					// 库存检查状态(默认合适)
					tbWarehousestockVO[i].setSs_pk("0001AA100000000B1TYK");
					// 库存表状态
					tbWarehousestockVO[i].setWhs_status(0);
					// 类型
					tbWarehousestockVO[i].setWhs_type(1);
					//
					if (null != tbbbvo.getPwb_pk()
							&& !"".equals(tbbbvo.getPwb_pk())) {
						tbWarehousestockVO[i].setPk_bodysource(tbbbvo
								.getPwb_pk());
					}
					// 存货档案主键
					if (null != tbbbvo.getPk_invbasdoc()
							&& !"".equals(tbbbvo.getPk_invbasdoc())) {
						tbWarehousestockVO[i].setPk_invbasdoc(tbbbvo
								.getPk_invbasdoc());
					}
					// 批次号
					if (null != tbbbvo.getGebb_vbatchcode()
							&& !"".equals(tbbbvo.getGebb_vbatchcode())) {
						tbWarehousestockVO[i].setWhs_batchcode(tbbbvo
								.getGebb_vbatchcode());
					}
					// 回写批次号
					if (null != tbbbvo.getGebb_lvbatchcode()
							&& !"".equals(tbbbvo.getGebb_lvbatchcode())) {
						tbWarehousestockVO[i].setWhs_lbatchcode(tbbbvo
								.getGebb_lvbatchcode());
					} else {
						tbWarehousestockVO[i].setWhs_lbatchcode("2009");
					}
					// 操作时间
					tbWarehousestockVO[i].setOperatetime(new UFDateTime(
							new Date()));
					// 货位
					String cargdocPK = getCargdocPK(ClientEnvironment
							.getInstance().getUser().getPrimaryKey());
					tbWarehousestockVO[i].setPk_cargdoc(cargdocPK);
					// // 来源单据表体主键， 缓存表主键
					// tbWarehousestockVO[i].setPk_bodysource(tbbbvo
					// .getGebb_pk());
					//
					// if (!addoredit) {
					//						
					// }
					//
					// 托盘状态

					if (null != tbbbvo.getCdt_pk()
							&& !"".equals(tbbbvo.getCdt_pk())) {
						Cdt_pk = tbbbvo.getCdt_pk();
					}
					BdCargdocTrayVO bdCargdocTrayVO = (BdCargdocTrayVO) query
							.retrieveByPK(BdCargdocTrayVO.class, Cdt_pk);
					bdCargdocTrayVO.setCdt_traystatus(new Integer(1));
					bdCargdocTrayVOs.add(bdCargdocTrayVO);

				}

			}
			params.add(tbWarehousestockVO);
			params.add(bdCargdocTrayVOs);
			// 制单时间
			// if (addoredit) {
			// ((TbGeneralHVO) billVO.getParentVO())
			// .setTmaketime(getBillUI()._getServerTime()
			// .toString());
			// }

			//
			if (getBillUI().isSaveAndCommitTogether()) {
				billVO = getBusinessAction().saveAndCommit(billVO,
						getUIController().getBillType(), _getDate().toString(),
						getBillUI().getUserObject(), checkVO);
			} else {

				// write to database

				billVO = getBusinessAction().save(billVO,
						getUIController().getBillType(), _getDate().toString(),
						params, checkVO);

			}

		}

		// 进行数据恢复处理
		if (sCtrl != null) {
			if (sCtrl.isSingleDetail())
				billVO.setParentVO((CircularlyAccessibleValueObject) o);
		}
		int nCurrentRow = -1;
		if (isSave) {
			if (isEditing()) {
				if (getBufferData().isVOBufferEmpty()) {
					getBufferData().addVOToBuffer(billVO);
					nCurrentRow = 0;

				} else {
					getBufferData().setCurrentVO(billVO);
					nCurrentRow = getBufferData().getCurrentRow();
				}
			}
			// 新增后操作处理
			setAddNewOperate(isAdding(), billVO);
		}
		// 设置保存后状态
		setSaveOperateState();
		if (nCurrentRow >= 0) {
			getBufferData().setCurrentRow(nCurrentRow);
		}
		showZeroLikeNull(false);

		getButtonManager().getButton(
				nc.ui.wds.w8004040204.ssButtun.ISsButtun.zdqh)
				.setEnabled(false);
		getButtonManager().getButton(
				nc.ui.wds.w8004040204.ssButtun.ISsButtun.tpzd)
				.setEnabled(false);

		getButtonManager().getButton(IBillButton.Add).setEnabled(true);

		getBillUI().updateButtonUI();
		// 页面标签
		// getBillCardPanelWrapper().getBillCardPanel().getHeadItem("geh_corp")
		// .setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getHeadItem("geh_calbody")
		// .setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
		// "geh_cwarehouseid").setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
		// "geh_cwhsmanagerid").setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getHeadItem("geh_cbizid")
		// .setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
		// "geh_cdispatcherid").setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
		// "geh_cothercorpid").setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
		// "geh_cothercalbodyid").setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
		// "geh_cotherwhid").setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getHeadItem("fallocname")
		// .setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vnote")
		// .setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getBodyItem("geb_crowno")
		// .setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getBodyItem("invcode")
		// .setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
		// "geb_vbatchcode").setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getBodyItem("geb_bsnum")
		// .setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getBodyItem("geb_snum")
		// .setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getBodyItem("geb_banum")
		// .setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getBodyItem("geb_anum")
		// .setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getBodyItem("vnote")
		// .setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
		// "geb_virtualbnum").setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
		// "geb_customize2").setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getBodyItem("geb_nprice")
		// .setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getBodyItem("geb_nmny")
		// .setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
		// "geb_virtualbnum").setShow(false);

		// if (null != st_type && st_type.equals("1")
		// && getBufferData().getVOBufferSize() > 0) {
		//
		// AggregatedValueObject billvo = getBillUI().getVOFromUI();
		// TbGeneralHVO generalhvo = null;
		// if (null != billvo.getParentVO()) {
		// generalhvo = (TbGeneralHVO) billvo.getParentVO();
		// }
		//
		// //
		//
		// // 签字后
		// if (null != generalhvo.getPwb_fbillflag()
		// && generalhvo.getPwb_fbillflag() == 3) {
		// getButtonManager().getButton(
		// nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
		// .setEnabled(false);
		// getButtonManager().getButton(
		// nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
		// .setEnabled(true);
		// getBillUI().updateButtonUI();
		// } else { // 签字前
		// getButtonManager().getButton(
		// nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
		// .setEnabled(true);
		// getButtonManager().getButton(
		// nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
		// .setEnabled(false);
		// getBillUI().updateButtonUI();
		// }
		// }
	}

	/**
	 * 根据登录人员主键查询出对应的货位主键
	 * 
	 * @param pk
	 *            人员主键
	 * @return 仓库主键
	 * @throws BusinessException
	 */
	public static String getCargdocPK(String pk) throws BusinessException {
		String tmp = null;
		IUAPQueryBS iuap = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		String sql = "select pk_cargdoc from tb_stockstaff where dr = 0 and cuserid='"
				+ pk + "'";
		ArrayList list = (ArrayList) iuap.executeQuery(sql,
				new ArrayListProcessor());
		if (null != list && list.size() > 0) {
			Object[] a = (Object[]) list.get(0);
			if (null != a && a.length > 0 && null != a[0]) {
				tmp = a[0].toString();
			}
		}

		return tmp;
	}

	public void showZeroLikeNull(boolean bShowZeroLikeNull) {
		BillRendererVO vo = getBillCardPanelWrapper().getBillCardPanel()
				.getBodyPanel().getRendererVO();
		vo.setShowZeroLikeNull(bShowZeroLikeNull);
		getBillCardPanelWrapper().getBillCardPanel().getBodyPanel()
				.setShowFlags(vo);
	}

	// 验证方法
	public boolean validateItem(TbOutgeneralBVO[] generalBVO) throws Exception {
		// 单据日期
		Object bdilldate = getBillCardPanelWrapper().getBillCardPanel()
				.getHeadTailItem("dbilldate").getValueObject();
		if (null == bdilldate || "".equals(bdilldate)) {
			getBillUI().showWarningMessage("请设置单据日期");
			return false;
		}
		/***********************************************************************
		 * / 收发类别 Object cdispatcherid =
		 * getBillCardPanelWrapper().getBillCardPanel()
		 * .getHeadTailItem("cdispatcherid").getValueObject(); if (null ==
		 * cdispatcherid || "".equals(cdispatcherid)) {
		 * getBillUI().showWarningMessage("请设置收发类别"); return false; }
		 **********************************************************************/
		if (null != generalBVO && generalBVO.length > 0) {
			TbOutgeneralBVO generalbvo = null;
			boolean resultCount = true;
			for (int i = 0; i < generalBVO.length; i++) {
				generalbvo = generalBVO[i];
				if (null == generalbvo.getNoutassistnum()
						|| "".equals(generalbvo.getNoutassistnum())) {
					resultCount = false;
					break;
				}
			}
			int result = getBillUI().showOkCancelMessage("您有库存产品没有指定出库,是否保存?");
			if (result == 1)
				return true;
			else
				return false;
		}
		return true;
	}

	/**
	 * 根据当前页面上的应发数量和实发数量进行比较来显示颜色
	 * 
	 * @throws Exception
	 */
	public void chaneColor() throws Exception {
		TbOutgeneralBVO[] generalbVO = (TbOutgeneralBVO[]) getBillUI()
				.getVOFromUI().getChildrenVO();

		// 获取并判断表体是否有值，进行循环
		if (null != generalbVO && generalbVO.length > 0) {
			for (int i = 0; i < generalbVO.length; i++) {
				// 获取当前表体行的应发辅数量和实发辅数量进行比较，根据比较结果进行颜色显示
				// 红色：没有实发数量；灰色：有实发数量但是数量不够；白色：实发数量与应发数量相等
				Object num = getBillCardPanelWrapper().getBillCardPanel()
						.getBodyValueAt(i, "nshouldoutassistnum");// 应发辅数量
				Object tatonum = getBillCardPanelWrapper().getBillCardPanel()
						.getBodyValueAt(i, "noutassistnum");// 实发辅数量
				if (null != num && !"".equals(num) && null != tatonum
						&& !"".equals(tatonum)) {
					if (Double.parseDouble(num.toString().trim()) != Double
							.parseDouble(tatonum.toString().trim()))
						getBillCardPanelWrapper().getBillCardPanel()
								.getBodyPanel().setCellBackGround(i,
										"ccunhuobianma", Color.gray);
					else
						getBillCardPanelWrapper().getBillCardPanel()
								.getBodyPanel().setCellBackGround(i,
										"ccunhuobianma", Color.white);
				} else
					getBillCardPanelWrapper().getBillCardPanel().getBodyPanel()
							.setCellBackGround(i, "ccunhuobianma", Color.red);
			}

		}

	}

	// 辅助功能
	protected void onfzgn() {

	}

	/*
	 * 托盘指定(non-Javadoc)
	 * 
	 * @see nc.ui.wds.w8004040204.AbstractMyEventHandler#ontpzd()
	 */
	protected void ontpzd() throws Exception {
		int bodyrownum = getBillCardPanelWrapper().getBillCardPanel()
				.getBillTable().getRowCount();
		if (bodyrownum == 0) {
			getBillUI().showErrorMessage("子表不能为空！");
			return;
		}
		// 验证货品主键

		String geb_cinvbasidy = (String) getBillCardPanelWrapper()
				.getBillCardPanel().getBillModel().getValueAt(
						getBillCardPanelWrapper().getBillCardPanel()
								.getBillTable().getSelectedRow(),
						"cinventoryid");
		if (null == geb_cinvbasidy || "".equals(geb_cinvbasidy)) {
			getBillUI().showErrorMessage("货品不能为空!");
			return;
		}
		// 验证批次号是否为空
		String geb_vbatchcodey = (String) getBillCardPanelWrapper()
				.getBillCardPanel().getBillModel().getValueAt(
						getBillCardPanelWrapper().getBillCardPanel()
								.getBillTable().getSelectedRow(), "vbatchcode");
		if (null == geb_vbatchcodey || "".equals(geb_vbatchcodey)) {
			getBillUI().showErrorMessage("批次号不能为空!");
			return;
		}
		// 验证应入数量
		UFDouble geb_bsnumy = (UFDouble) getBillCardPanelWrapper()
				.getBillCardPanel().getBillModel().getValueAt(
						getBillCardPanelWrapper().getBillCardPanel()
								.getBillTable().getSelectedRow(),
						"nshouldoutassistnum");
		if (null == geb_bsnumy) {
			getBillUI().showErrorMessage("应入数量不能为空!");
			return;
		}
		// 验证批次号是否正确
		if (geb_vbatchcodey.trim().length() < 8) {
			getBillUI().showErrorMessage("批次号不能小于8位!");
			return;
		}

		Pattern p = Pattern
				.compile(
						"^((((1[6-9]|[2-9]\\d)\\d{2})(0?[13578]|1[02])(0?[1-9]|[12]\\d|3[01]))|"
								+ "(((1[6-9]|[2-9]\\d)\\d{2})(0?[13456789]|1[012])(0?[1-9]|[12]\\d|30))|"
								+ "(((1[6-9]|[2-9]\\d)\\d{2})0?2(0?[1-9]|1\\d|2[0-8]))|"
								+ "(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))0?229))$",
						Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
		Matcher m = p.matcher(geb_vbatchcodey.trim().substring(0, 8));
		if (!m.find()) {
			getBillUI()
					.showErrorMessage("批次号输入的不正确,请您输入正确的日期!如：20100101XXXXXX");
			return;
		}

		// 获得选中行号
		int[] b = getBillCardPanelWrapper().getBillCardPanel().getBillTable()
				.getSelectedRows();
		// 判断是否选中单行
		if (b.length > 1) {
			getBillUI().showErrorMessage("不能进行多行操作，请选择一行数据!");
			return;
		}
		if (null == b || b.length == 0) {
			getBillUI().showErrorMessage("您没有选择，请选择一行数据!");
			return;
		}
		// 获取所选择的行号
		int index = getBillCardPanelWrapper().getBillCardPanel().getBillTable()
				.getSelectedRow();
		if (index > -1) {
			// 获取表体多选择的VO对象
			TbOutgeneralBVO item = (TbOutgeneralBVO) getBillCardPanelWrapper()
					.getBillCardPanel().getBillModel().getBodyValueRowVO(index,
							TbOutgeneralBVO.class.getName());

			if (null != item) {
				Object pk_stordoc = getBillCardPanelWrapper()
						.getBillCardPanel().getHeadItem("srl_pk")
						.getValueObject();
				if (null == pk_stordoc || "".equals(pk_stordoc)) {
					myClientUI.showErrorMessage("操作失败,您无权操作");
					return;
				} else {
					if (!pk_stordoc.equals(pk_stock)) {
						myClientUI.showErrorMessage("操作失败,您无权操作");
						return;
					}
				}
				// TrayDisposeDlg tdpDlg = new TrayDisposeDlg("4203",
				// ClientEnvironment.getInstance().getUser()
				// .getPrimaryKey(),
				// ClientEnvironment.getInstance().getCorporation()
				// .getPrimaryKey(), "8004040204", myClientUI);
				// tdpDlg.getReturnVOs(myClientUI, item, index, pk_stordoc
				// .toString(), opType);
				TrayDisposeDlg tdpDlg = new TrayDisposeDlg("80EE",
						ClientEnvironment.getInstance().getUser()
								.getPrimaryKey(),
						ClientEnvironment.getInstance().getCorporation()
								.getPrimaryKey(), "8004040218", item,
						myClientUI);
				TbOutgeneralBVO vos = tdpDlg.getReturnVOs(item);

				if (null == vos) {
					return;
				}

				getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(
						vos.getNoutassistnum(), index, "noutassistnum");
				getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(
						vos.getNoutnum(), index, "noutnum");

				chaneColor();
			}
		} else {
			getBillUI().showWarningMessage("请选择表体行进行操作");
		}
	}

	/*
	 * 自动取货(non-Javadoc)
	 * 
	 * @see nc.ui.wds.w8004040204.AbstractMyEventHandler#onzdqh()
	 */
	protected void onzdqh() throws Exception {
		int results = myClientUI.showOkCancelMessage("确认自动拣货?");
		if (results == 1) {
			TbOutgeneralBVO[] generalbVO = (TbOutgeneralBVO[]) getBillUI()
					.getVOFromUI().getChildrenVO();
			TbOutgeneralBVO[] tmpbVO = null;
			if (null != generalbVO && generalbVO.length > 0) {
				Object pk_stordoc = getBillCardPanelWrapper()
						.getBillCardPanel().getHeadItem("srl_pk")
						.getValueObject();
				if (null == pk_stordoc || "".equals(pk_stordoc)) {
					myClientUI.showErrorMessage("操作失败,您无权操作");
					return;
				} else {
					if (!pk_stordoc.equals(pk_stock)) {
						myClientUI.showErrorMessage("操作失败,您无权操作");
						return;
					}
				}
				// 货位
				String pk_cargdoc = "";
				try {
					pk_cargdoc = nc.ui.wds.w8000.CommonUnit
							.getCargDocName(ClientEnvironment.getInstance()
									.getUser().getPrimaryKey());
				} catch (BusinessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance()
						.lookup(IUAPQueryBS.class.getName());
				int bodyrownum = getBillCardPanelWrapper().getBillCardPanel()
						.getBillTable().getRowCount();
				if (bodyrownum == 0) {
					getBillUI().showErrorMessage("子表不能为空！");
					return;
				}
				// 验证货品主键
				for (int i = 0; i < bodyrownum; i++) {
					String geb_cinvbasidy = (String) getBillCardPanelWrapper()
							.getBillCardPanel().getBillModel().getValueAt(i,
									"cinventoryid");
					if (null == geb_cinvbasidy || "".equals(geb_cinvbasidy)) {
						getBillUI().showErrorMessage("货品不能为空!");
						return;
					}
					// 验证批次号是否为空
					String geb_vbatchcodey = (String) getBillCardPanelWrapper()
							.getBillCardPanel().getBillModel().getValueAt(i,
									"vbatchcode");
					if (null == geb_vbatchcodey || "".equals(geb_vbatchcodey)) {
						getBillUI().showErrorMessage("批次号不能为空!");
						return;
					}
					// 验证应入数量
					UFDouble geb_bsnumy = (UFDouble) getBillCardPanelWrapper()
							.getBillCardPanel().getBillModel().getValueAt(i,
									"nshouldoutassistnum");
					if (null == geb_bsnumy) {
						getBillUI().showErrorMessage("应入数量不能为空!");
						return;
					}
					// 验证批次号是否正确
					if (geb_vbatchcodey.trim().length() < 8) {
						getBillUI().showErrorMessage("批次号不能小于8位!");
						return;
					}

					Pattern p = Pattern
							.compile(
									"^((((1[6-9]|[2-9]\\d)\\d{2})(0?[13578]|1[02])(0?[1-9]|[12]\\d|3[01]))|"
											+ "(((1[6-9]|[2-9]\\d)\\d{2})(0?[13456789]|1[012])(0?[1-9]|[12]\\d|30))|"
											+ "(((1[6-9]|[2-9]\\d)\\d{2})0?2(0?[1-9]|1\\d|2[0-8]))|"
											+ "(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))0?229))$",
									Pattern.CASE_INSENSITIVE
											| Pattern.UNICODE_CASE);
					Matcher m = p.matcher(geb_vbatchcodey.trim()
							.substring(0, 8));
					if (!m.find()) {
						getBillUI().showErrorMessage(
								"批次号输入的不正确,请您输入正确的日期!如：20100101XXXXXX");
						return;
					}

				}
				// 得到全部表体VO

				// 表头

				TbOutgeneralBVO[] tbOutgeneralBVOs = null;
				if (getBillManageUI().isListPanelSelected()) {

					tbOutgeneralBVOs = (TbOutgeneralBVO[]) getBillManageUI()
							.getBillListWrapper().getVOFromUI().getChildrenVO();

				} else {

					tbOutgeneralBVOs = (TbOutgeneralBVO[]) getBillCardPanelWrapper()
							.getBillVOFromUI().getChildrenVO();

				}

				// 判断表体是否有数据
				if (null == tbOutgeneralBVOs || tbOutgeneralBVOs.length == 0) {
					getBillUI().showErrorMessage("表体中没有数据，不能进行操作！");
					return;
				}
				// 判断托盘容积是否能存放货物
				if (isStock) {
					for (int i = 0; i < tbOutgeneralBVOs.length; i++) {
						// 获得表体
						StringBuffer sql = new StringBuffer(
								" select cdt_pk from bd_cargdoc_tray where cdt_invbasdoc='"
										+ tbOutgeneralBVOs[i].getCinventoryid()
										+ "' and cdt_traystatus=0 and dr=0 and  pk_cargdoc='"
										+ pk_cargdoc + "'  ");

						// 符合条件的全部托盘
						ArrayList cdts = new ArrayList();
						try {
							cdts = (ArrayList) query.executeQuery(sql
									.toString(), new ArrayListProcessor());

						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// 托盘容积
						StringBuffer sqlVolume = new StringBuffer(
								"select def20 from bd_invbasdoc where pk_invbasdoc='");
						sqlVolume.append(tbOutgeneralBVOs[i].getCinventoryid());
						sqlVolume.append("'");
						ArrayList cdtsVolume = new ArrayList();
						cdtsVolume = (ArrayList) query.executeQuery(sqlVolume
								.toString(), new ArrayListProcessor());
						// 单品当前总容积
						double invVolume = 0;
						if (null != cdts
								&& null != cdtsVolume
								&& null != cdtsVolume.get(0)
								&& null != ((Object[]) cdtsVolume.get(0))[0]
								&& !"".equals(((Object[]) cdtsVolume.get(0))[0]
										.toString())) {
							invVolume = Double
									.parseDouble(((Object[]) cdtsVolume.get(0))[0]
											.toString())
									* cdts.size();
						} else {
							getBillUI().showErrorMessage("请填写托盘或托盘容积！");
							return;
						}
						// 入库辅数量
						String geb_bsnum = "";
						if (null != tbOutgeneralBVOs[i]
								.getNshouldoutassistnum()) {
							geb_bsnum = tbOutgeneralBVOs[i]
									.getNshouldoutassistnum().toString();
						}
						// 入库辅数量
						double geb_bsnumd = 0;
						if (null != geb_bsnum && !"".equals(geb_bsnum)) {
							geb_bsnumd = Double.parseDouble(geb_bsnum);
						}

						if (Math.abs(geb_bsnumd) > invVolume) {
							getBillUI().showErrorMessage("入库数量大于当前库存容积！");
							return;
						}
					}

				} else {

					StringBuffer sql_cargtrays = new StringBuffer(
							" dr=0 and  pk_cargdoc='" + pk_cargdoc + "'  ");

					ArrayList cargtrays = (ArrayList) query.retrieveByClause(
							BdCargdocTrayVO.class, sql_cargtrays.toString());
					if (null == cargtrays || cargtrays.size() == 0
							|| null == cargtrays.get(0)) {
						getBillUI().showErrorMessage("分仓仓库没有虚拟托盘！");
						return;
					}

				}

				// 添加托盘信息
				for (int i = 0; i < tbOutgeneralBVOs.length; i++) {
					// 获得表体
					StringBuffer sql = new StringBuffer();
					if (isStock) {
						sql
								.append("select cdt_pk from bd_cargdoc_tray where cdt_invbasdoc='"
										+ tbOutgeneralBVOs[i].getCinventoryid()
										+ "' and cdt_traystatus=0 and dr=0 and  pk_cargdoc='"
										+ pk_cargdoc + "' ");
						String mySql = " select distinct cdt_pk from tb_general_b_b where pk_invbasdoc='"
								+ tbOutgeneralBVOs[i].getCinventoryid()
								+ "' and geb_pk !='"
								+ tbOutgeneralBVOs[i].getCsourcebillbid()
								+ "' and dr=0 and geb_pk in (' ";

						boolean myBoolean = false;
						for (int j = 0; j < tbOutgeneralBVOs.length; j++) {
							if (null != tbOutgeneralBVOs[i].getCinventoryid()
									&& null != tbOutgeneralBVOs[j]
											.getCinventoryid()
									&& null != tbOutgeneralBVOs[i]
											.getCsourcebillbid()
									&& null != tbOutgeneralBVOs[j]
											.getCsourcebillbid()) {
								if (tbOutgeneralBVOs[i].getCinventoryid()
										.equals(
												tbOutgeneralBVOs[j]
														.getCinventoryid())
										&& !tbOutgeneralBVOs[i]
												.getCsourcebillbid()
												.equals(
														tbOutgeneralBVOs[j]
																.getCsourcebillbid())) {
									mySql += tbOutgeneralBVOs[j]
											.getCsourcebillbid()
											+ "','";
									myBoolean = true;
								}
							}
						}
						mySql += "') ";
						if (myBoolean) {
							sql.append(" and cdt_pk not in (" + mySql + ")");
						}
					} else {
						sql
								.append("select cdt_pk from bd_cargdoc_tray where  dr=0 and  pk_cargdoc='"
										+ pk_cargdoc + "' ");
					}

					// 符合条件的全部托盘
					ArrayList cdts = new ArrayList();
					try {
						cdts = (ArrayList) query.executeQuery(sql.toString(),
								new ArrayListProcessor());

					} catch (BusinessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					// 托盘容积
					StringBuffer sqlVolume = new StringBuffer(
							"select def20 from bd_invbasdoc where pk_invbasdoc='");
					sqlVolume.append(tbOutgeneralBVOs[i].getCinventoryid());
					sqlVolume.append("' and dr=0 ");
					ArrayList cdtsVolume = new ArrayList();
					cdtsVolume = (ArrayList) query.executeQuery(sqlVolume
							.toString(), new ArrayListProcessor());

					double invTrayVolume = 0;
					if (isStock) {
						invTrayVolume = Double
								.parseDouble(((Object[]) cdtsVolume.get(0))[0]
										.toString());
					} else {
						invTrayVolume = -1;
					}
					// 入库辅数量
					String geb_bsnum = "";
					if (null != tbOutgeneralBVOs[i].getNshouldoutassistnum()) {
						geb_bsnum = tbOutgeneralBVOs[i]
								.getNshouldoutassistnum().toString();

					}
					// 当前入库辅数量
					double geb_bsnumd = 0;
					if (null != geb_bsnum && !"".equals(geb_bsnum)) {
						geb_bsnumd = Double.parseDouble(geb_bsnum);
					}
					// 存货托盘个数

					if (null != cdts) {

					}

					// 获得要删除的VO
					StringBuffer tbbsql = new StringBuffer("geb_pk='");
					tbbsql.append(tbOutgeneralBVOs[i].getCsourcebillbid());
					tbbsql.append("' and pk_invbasdoc='");
					tbbsql.append(tbOutgeneralBVOs[i].getCinventoryid());
					tbbsql.append("' and dr = 0");

					ArrayList dtbbvos = new ArrayList();
					try {

						dtbbvos = (ArrayList) query.retrieveByClause(
								TbGeneralBBVO.class, tbbsql.toString());
					} catch (BusinessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					// 
					ArrayList tbGeneralBBVOs = new ArrayList();
					// ArrayList bdCargdocTrayVOs = new ArrayList();
					String Cdt_pk = "";
					geb_bsnumd = Math.abs(geb_bsnumd);
					int k = 0;
					if (-1 != invTrayVolume) {

						while (geb_bsnumd > invTrayVolume) {
							TbGeneralBBVO tbgbbvo = new TbGeneralBBVO();
							// 批次
							tbgbbvo.setGebb_vbatchcode(tbOutgeneralBVOs[i]
									.getVbatchcode());
							// 回写批次
							tbgbbvo.setGebb_lvbatchcode(tbOutgeneralBVOs[i]
									.getLvbatchcode());
							// 行号
							tbgbbvo.setGebb_rowno(k + 1 + "0");
							// 出库单表体主键
							tbgbbvo.setGeb_pk(tbOutgeneralBVOs[i]
									.getCsourcebillbid());
							// 换算率
							tbgbbvo.setGebb_hsl(tbOutgeneralBVOs[i].getHsl());
							// 运货档案主键
							tbgbbvo.setPk_invbasdoc(tbOutgeneralBVOs[i]
									.getCinventoryid());
							// 入库单子表主键
							// tbgbbvo.setGeb_pk(tbGeneralBVOs[i].getGeb_pk());
							// 单价
							tbgbbvo.setGebb_nprice(tbOutgeneralBVOs[i]
									.getNprice());
							// 金额
							tbgbbvo.setGebb_nmny(tbOutgeneralBVOs[i].getNmny());
							// 托盘实际存放数量
							tbgbbvo.setGebb_num(new UFDouble(invTrayVolume));
							geb_bsnumd = geb_bsnumd - invTrayVolume;
							// 托盘主键
							if (null != ((Object[]) cdts.get(k))[0]) {
								tbgbbvo.setCdt_pk(((Object[]) cdts.get(k))[0]
										.toString());
							}
							// DR
							tbgbbvo.setDr(0);
							// tbgbbvo.setGeb_pk(tbGeneralBVOs[i].getGeb_pk());
							tbGeneralBBVOs.add(tbgbbvo);
							// 托盘状态

							k++;
						}
					}

					TbGeneralBBVO tbgbbvo1 = new TbGeneralBBVO();
					// 批次
					tbgbbvo1.setGebb_vbatchcode(tbOutgeneralBVOs[i]
							.getVbatchcode());
					// 回写批次
					tbgbbvo1.setGebb_lvbatchcode(tbOutgeneralBVOs[i]
							.getLvbatchcode());
					// 行号
					tbgbbvo1.setGebb_rowno(k + 1 + "0");
					// 出库单表体主键
					tbgbbvo1.setGeb_pk(tbOutgeneralBVOs[i].getCsourcebillbid());
					// 换算率
					tbgbbvo1.setGebb_hsl(tbOutgeneralBVOs[i].getHsl());
					// 运货档案主键
					tbgbbvo1.setPk_invbasdoc(tbOutgeneralBVOs[i]
							.getCinventoryid());
					// 入库单子表主键
					// tbgbbvo1.setGeb_pk(tbGeneralBVOs[i].getGeb_pk());
					// 单价
					tbgbbvo1.setGebb_nprice(tbOutgeneralBVOs[i].getNprice());
					// 金额
					tbgbbvo1.setGebb_nmny(tbOutgeneralBVOs[i].getNmny());
					// 托盘实际存放数量
					tbgbbvo1.setGebb_num(new UFDouble(geb_bsnumd));
					// 托盘主键
					if (null != ((Object[]) cdts.get(k))[0]) {
						tbgbbvo1.setCdt_pk(((Object[]) cdts.get(k))[0]
								.toString());
					}
					// DR
					tbgbbvo1.setDr(0);
					tbGeneralBBVOs.add(tbgbbvo1);
					// 托盘状态

					//
					TbGeneralBBVO[] tbGeneralBBVO = new TbGeneralBBVO[tbGeneralBBVOs
							.size()];
					tbGeneralBBVOs.toArray(tbGeneralBBVO);

					// 获得自定义接口
					Iw8004040210 iw = (Iw8004040210) NCLocator.getInstance()
							.lookup(Iw8004040210.class.getName());
					try {
						iw.delAndInsertTbGeneralBBVO(dtbbvos, tbGeneralBBVO);
					} catch (BusinessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

				TrayDisposeDetailDlg tdpDlg = new TrayDisposeDetailDlg("80FF",
						ClientEnvironment.getInstance().getUser()
								.getPrimaryKey(),
						ClientEnvironment.getInstance().getCorporation()
								.getPrimaryKey(), "8004040218",
						tbOutgeneralBVOs);
				AggregatedValueObject[] vos = tdpDlg
						.getReturnVOs(tbOutgeneralBVOs);
				int rownum = getBillCardPanelWrapper().getBillCardPanel()
						.getBillTable().getRowCount();
				for (int i = 0; i < rownum; i++) {
					getBillCardPanelWrapper().getBillCardPanel()
							.setBodyValueAt(
									getBillCardPanelWrapper()
											.getBillCardPanel().getBodyValueAt(
													i, "nshouldoutassistnum"),
									i, "noutassistnum");
					getBillCardPanelWrapper().getBillCardPanel()
							.setBodyValueAt(
									getBillCardPanelWrapper()
											.getBillCardPanel().getBodyValueAt(
													i, "nshouldoutnum"), i,
									"noutnum");

				}
				if (null == vos || vos.length == 0) {

					return;
				}
				chaneColor();

			} else {
				myClientUI.showErrorMessage("操作失败,您无权操作");
				return;
			}
		}

	}

	/*
	 * 查询明细(non-Javadoc)
	 * 
	 * @see nc.ui.wds.w8004040204.AbstractMyEventHandler#onckmx()
	 */
	protected void onckmx() throws Exception {

		if (getBufferData().getVOBufferSize() <= 0) {
			getBillUI().showErrorMessage("表体中没有数据或您没有选择表体单据，不能进行操作!");
			return;
		}
		// 得到全部表体VO
		TbOutgeneralBVO[] tbOutgeneralBVOs = null;
		int selectRow = getBillCardPanelWrapper().getBillCardPanel()
				.getBillTable().getSelectedRow();

		// if (-1 == selectRow) {
		// getBillUI().showErrorMessage("表体中没有数据或您没有选择表体单据，不能进行操作！");
		// return;
		// }
		if (getBillManageUI().isListPanelSelected()) {
			// if (getBufferData().getVOBufferSize() != 0) {

			tbOutgeneralBVOs = (TbOutgeneralBVO[]) getBillManageUI()
					.getBillListWrapper().getVOFromUI().getChildrenVO();
			// }

		} else {

			// if (getBufferData().getVOBufferSize() != 0) {
			tbOutgeneralBVOs = (TbOutgeneralBVO[]) getBillCardPanelWrapper()
					.getBillVOFromUI().getChildrenVO();
			// }

		}
		// 判断表体是否有数据
		if (null == tbOutgeneralBVOs || tbOutgeneralBVOs.length == 0) {
			getBillUI().showErrorMessage("表体中没有数据，不能进行操作！");
			return;
		}
		TrayDisposeDetailDlg tdpDlg = new TrayDisposeDetailDlg("80FF",
				ClientEnvironment.getInstance().getUser().getPrimaryKey(),
				ClientEnvironment.getInstance().getCorporation()
						.getPrimaryKey(), "8004040218", tbOutgeneralBVOs);
		AggregatedValueObject[] vos = tdpDlg.getReturnVOs(tbOutgeneralBVOs);

	}

	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		// TODO Auto-generated method stub
		if (isControl == 2 || "3".equals(st_type)) {
			isAdd = true;
			// 实例查询类
			saleOrderDlg = new SaleOrderDlg(myClientUI);
			// 调用方法 获取查询后的聚合VO
			// AggregatedValueObject[] vos = saleOrderDlg
			// .getReturnVOs(ClientEnvironment.getInstance()
			// .getCorporation().getPrimaryKey(),
			// ClientEnvironment.getInstance().getUser()
			// .getPrimaryKey(), "4202",
			// ConstVO.m_sBillDRSQ, "8004040204", "8004040294",
			// myClientUI);
			AggregatedValueObject[] vos = saleOrderDlg
					.getReturnVOs(ClientEnvironment.getInstance()
							.getCorporation().getPrimaryKey(),
							ClientEnvironment.getInstance().getUser()
									.getPrimaryKey(), "0208",
							ConstVO.m_sBillDRSQ, "8004040218", "8004040298",
							myClientUI);
			// 判断是否对查询模板有进行操作
			if (null == vos || vos.length == 0) {
				return;
			}
			// 调用转换类 把模板中获取的对象转换成自己的当前显示的对象，调用方法
			MyBillVO voForSave = changeReqFydtoOutgeneral(vos);// 该方法是第一次增加进行数据转换
			opType = false;
			TbOutgeneralHVO generalh = (TbOutgeneralHVO) voForSave
					.getParentVO();
			if (null != generalh && generalh.getVsourcebillcode().length() > 0) {
				// 判断源订单号是否包含M字母 有M字母是买赠业务，买赠业务里面包含单品有多条记录没法自动拣货，屏蔽自动拣货按钮
				int index = generalh.getVsourcebillcode().toLowerCase()
						.indexOf("m");
				if (index > -1) {
					opType = true;
					getButtonManager().getButton(ISsButtun.zdqh).setEnabled(
							false);
				}
			}
			// 进行数据情况 和按钮初始化
			getBufferData().clear();
			getBillUI().setBillOperate(IBillOperate.OP_INIT);
			// 填充数据
			getBufferData().addVOToBuffer(voForSave);
			// 更新数据
			updateBuffer();
			super.onBoEdit();
			// 执行公式表头公式
			getBillCardPanelWrapper().getBillCardPanel().execHeadEditFormulas();
			getBillCardPanelWrapper().getBillCardPanel()
					.execHeadTailEditFormulas();
			// 设置库管员
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"cwhsmanagerid").setValue(
					ClientEnvironment.getInstance().getUser().getPrimaryKey());
			// 设置制单人
			getBillCardPanelWrapper().getBillCardPanel().getHeadTailItem(
					"coperatorid").setValue(
					ClientEnvironment.getInstance().getUser().getPrimaryKey());
			// 单据日期
			getBillCardPanelWrapper().getBillCardPanel().getHeadTailItem(
					"dbilldate").setValue(_getDate().toString());

		} else {
			myClientUI.showErrorMessage("操作失败,您无权操作");
			return;
		}
	}

	@Override
	protected void onBoCancel() throws Exception {
		// TODO Auto-generated method stub
		getBillUI().initUI();
		getBufferData().clear();
		changeButton(false);
	}

	public void saveGeneral(AggregatedValueObject billvo) throws Exception {
		// 通过转换方法获取ERP中出库单聚合VO
		this.getBusinessAction().save(billvo, getUIController().getBillType(),
				_getDate().toString(), getBillUI().getUserObject(), billvo);
	}

	// 取消签字
	protected void onQxqz() throws Exception {
		myClientUI.showHintMessage("正在执行取消签字...");
		if (getBufferData().getCurrentRow() >= 0) {
			int retu = myClientUI.showOkCancelMessage("确认取消签字?");
			if (retu == 1) {
				TbOutgeneralHVO generalh = (TbOutgeneralHVO) getBufferData()
						.getCurrentVO().getParentVO();
				Object result = iuap.retrieveByPK(TbOutgeneralHVO.class,
						generalh.getGeneral_pk());
				if (null != result) {
					generalh = (TbOutgeneralHVO) result;
					if (generalh.getVbillstatus() == 0) {
						// 状态
						generalh.setVbillstatus(new Integer(1));
						// 签字人主键
						generalh.setCregister(null);
						// 签字时间
						generalh.setTaccounttime(null);
						// 签字日期
						generalh.setQianzidate(null);

						String strWhere = " dr = 0 and vbillcode = '"
								+ generalh.getVbillcode() + "'";

						ArrayList list = (ArrayList) iuap.retrieveByClause(
								IcGeneralHVO.class, strWhere);
						if (null != list && list.size() > 0) {
							IcGeneralHVO header = (IcGeneralHVO) list.get(0);
							if (null != header) {

								strWhere = " dr = 0 and cgeneralhid = '"
										+ header.getCgeneralhid() + "'";
								list = (ArrayList) iuap.retrieveByClause(
										IcGeneralBVO.class, strWhere);
								if (null != list && list.size() > 0) {
									IcGeneralBVO[] itemvo = new IcGeneralBVO[list
											.size()];
									GeneralBillItemVO[] generalbItem = new GeneralBillItemVO[itemvo.length];
									itemvo = (IcGeneralBVO[]) list
											.toArray(itemvo);
									AggregatedValueObject billvo = new GeneralBillVO();
									billvo.setParentVO(CommonUnit
											.setGeneralHVO(header));
									for (int i = 0; i < itemvo.length; i++) {
										generalbItem[i] = CommonUnit
												.setGeneralItemVO(itemvo[i]);
									}
									billvo.setChildrenVO(generalbItem);
									this.getIw().whs_processAction(
											"CANCELSIGN", "DELETE", "4C",
											_getDate().toString(), billvo,
											generalh);

									myClientUI.showHintMessage("操作成功");
									super.onBoRefresh();
									getButtonManager()
											.getButton(
													nc.ui.wds.w80060206.buttun0206.ISsButtun.Qzqr)
											.setEnabled(true);
									getButtonManager()
											.getButton(
													nc.ui.wds.w80060206.buttun0206.ISsButtun.Qxqz)
											.setEnabled(false);
									myClientUI.updateButtons();
									return;
								}
							}
						}
						myClientUI.showErrorMessage("签字失败,销售出库单已被删除");
						return;

					}
				}
			} else {
				myClientUI.showHintMessage(null);
			}
		} else {
			myClientUI.showErrorMessage("请选中一条单据进行签字");
		}

	}

	// 签字确认
	protected void onQzqr() throws Exception {
		myClientUI.showHintMessage("正在执行签字...");
		if (getBufferData().getCurrentRow() >= 0) {
			int retu = myClientUI.showOkCancelMessage("确认签字?");
			if (retu == 1) {
				TbOutgeneralHVO generalh = (TbOutgeneralHVO) getBufferData()
						.getCurrentVO().getParentVO();
				Object result = iuap.retrieveByPK(TbOutgeneralHVO.class,
						generalh.getGeneral_pk());
				if (null != result) {
					generalh = (TbOutgeneralHVO) result;
					if (null != generalh.getVbillstatus()
							&& generalh.getVbillstatus() == 0) {
						myClientUI.showErrorMessage("该单据已经签字");
						return;
					} else if (null != generalh.getVbillstatus()
							&& generalh.getVbillstatus() == 1) {
						// 这里的条件需要改，现在的运单确认还没有作做，所以这里的条件需要设置成必须确认后的才能签字

						// 状态
						generalh.setVbillstatus(new Integer(0));
						// 签字人主键
						generalh.setCregister(ClientEnvironment.getInstance()
								.getUser().getPrimaryKey());
						// 签字时间
						generalh.setTaccounttime(getBillUI()._getServerTime()
								.toString());
						// 签字日期
						generalh.setQianzidate(_getDate());
						AggregatedValueObject billvo = changeReqOutgeneraltoGeneral();
						this.getIw().whs_processAction("PUSHSAVE", "SIGN",
								"4C", _getDate().toString(), billvo, generalh);

						myClientUI.showHintMessage("签字成功");
						super.onBoRefresh();
						getButtonManager().getButton(
								nc.ui.wds.w80060206.buttun0206.ISsButtun.Qzqr)
								.setEnabled(false);
						getButtonManager().getButton(
								nc.ui.wds.w80060206.buttun0206.ISsButtun.Qxqz)
								.setEnabled(true);
						myClientUI.updateButtons();
					} else {
						myClientUI.showErrorMessage("该单据还没有进行运单确认");
					}
				}
			} else {
				myClientUI.showHintMessage(null);
			}
		} else {
			myClientUI.showErrorMessage("请选中一条单据进行签字");
		}

		// Object o = PfUtilClient.processAction("PUSHSAVE", "4C", _getDate()
		// .toString(), billvo);
		// Object[] arrayO = (Object[]) o;
		// billvo = (AggregatedValueObject) arrayO[0];
		// o = PfUtilClient.processAction("SIGN", "4C", _getDate()
		// .toString(), billvo);

	}

	/**
	 * 转换 把当前页面中的VO转换成ERP中的出库单聚合OV 调用方法 进行回写ERP中出库单
	 * 
	 * @return ERP中出库单聚合VO
	 * @throws Exception
	 */
	public GeneralBillVO changeReqOutgeneraltoGeneral() throws Exception {
		if (getBufferData().getCurrentRow() < 0) {
			myClientUI.showErrorMessage("请选择表头进行签字");
			return null;
		}
		// 本地出库表 表头
		TbOutgeneralHVO outhvo = (TbOutgeneralHVO) getBufferData()
				.getCurrentVO().getParentVO();
		// 需要查询本地数据，添加确认条件----------为完成
		// Object result = iuap.retrieveByPK(TbOutgeneralHVO.class, outhvo
		// .getGeneral_pk());
		//		
		// if(null!=result){
		// outhvo = (TbOutgeneralHVO) result;
		// if(outhvo.getVbillstatus())
		// }

		// 本地出库表 表体
		TbOutgeneralBVO[] outbvo = (TbOutgeneralBVO[]) getBufferData()
				.getCurrentVO().getChildrenVO();

		// 出库聚合VO
		GeneralBillVO gBillVO = new GeneralBillVO();
		// 出库表头VO
		GeneralBillHeaderVO generalhvo = null;
		// 出库子表集合
		List<GeneralBillItemVO> generalBVOList = new ArrayList<GeneralBillItemVO>();
		// 出库子表VO数组
		GeneralBillItemVO[] generalBVO = null;
		// Object o = myClientUI.getBillCardPanel().getBodyValueAt(0,
		// "cfirstbillhid");
		if (null != outbvo[0].getCfirstbillhid()
				&& !"".equals(outbvo[0].getCfirstbillhid())) {
			String sWhere = " dr = 0 and csaleid = '"
					+ outbvo[0].getCfirstbillhid() + "'";
			ArrayList list = (ArrayList) iuap.retrieveByClause(SoSaleVO.class,
					sWhere);
			if (null != list && list.size() > 0) {
				SoSaleVO salehvo = (SoSaleVO) list.get(0);
				generalhvo = new GeneralBillHeaderVO();
				opType = false;
				if (null != salehvo) {
					// 给出库单表头赋值
					// 判断赠品订单
					int index = salehvo.getVreceiptcode().toLowerCase()
							.indexOf("m");
					if (index > -1) {
						opType = true;
					}
					generalhvo.setPk_corp(salehvo.getPk_corp());// 公司主键
					generalhvo.setCbiztypeid(salehvo.getCbiztype());// 业务类型
					generalhvo.setCbilltypecode("4C");// 库存单据类型编码
					generalhvo.setVbillcode(outhvo.getVbillcode());// 单据号
					generalhvo.setDbilldate(outhvo.getDbilldate());// 单据日期
					generalhvo.setCwarehouseid(outhvo.getSrl_pk());// 仓库ID
					generalhvo.setCdispatcherid(outhvo.getCdispatcherid());// 收发类型ID
					generalhvo.setCdptid(salehvo.getCdeptid());// 部门ID
					generalhvo.setCwhsmanagerid(outhvo.getCwhsmanagerid());// 库管员ID
					generalhvo.setCoperatorid(ClientEnvironment.getInstance()
							.getUser().getPrimaryKey());// 制单人
					generalhvo.setAttributeValue("coperatoridnow",
							ClientEnvironment.getInstance().getUser()
									.getPrimaryKey());// 操作人
					generalhvo.setCinventoryid(outbvo[0].getCinventoryid());// 存货ID
					generalhvo.setAttributeValue("csalecorpid", salehvo
							.getCsalecorpid());// 销售组织
					generalhvo.setCcustomerid(salehvo.getCcustomerid());// 客户ID
					generalhvo.setVdiliveraddress(salehvo.getVreceiveaddress());// 发运地址
					generalhvo.setCbizid(salehvo.getCemployeeid());// 业务员ID
					generalhvo.setVnote(salehvo.getVnote());// 备注
					generalhvo.setFbillflag(2);// 单据状态
					generalhvo.setPk_calbody(salehvo.getCcalbodyid());// 库存组织PK
					generalhvo.setAttributeValue("clastmodiid", outhvo
							.getClastmodiid());// 最后修改人
					generalhvo.setAttributeValue("tlastmoditime", outhvo
							.getTlastmoditime());// 最后修改时间
					generalhvo.setAttributeValue("tmaketime", outhvo
							.getTmaketime());// 制单时间
					generalhvo.setPk_cubasdocC(outhvo.getPk_cubasdocc());// 客户基本档案ID
					// 给表体赋值
					for (int i = 0; i < outbvo.length; i++) {
						// 单据表体附表--货位
						LocatorVO locatorvo = new LocatorVO();
						locatorvo.setPk_corp(salehvo.getPk_corp());
						boolean isBatch = false;
						if (null != outbvo[i].getCfirstbillhid()
								&& !"".equals(outbvo[i].getCfirstbillhid())) {
							sWhere = " dr = 0 and corder_bid = '"
									+ outbvo[i].getCfirstbillbid() + "'";
							ArrayList saleblist = (ArrayList) iuap
									.retrieveByClause(SoSaleorderBVO.class,
											sWhere);
							if (null != saleblist && saleblist.size() > 0) {
								SoSaleorderBVO salebvo = (SoSaleorderBVO) saleblist
										.get(0);
								GeneralBillItemVO generalb = new GeneralBillItemVO();
								generalb.setAttributeValue("pk_corp", salebvo
										.getPk_corp());// 公司
								generalb
										.setCinvbasid(salebvo.getCinvbasdocid());// 存货基本ID
								if (opType) {
									// 根据销售附表ID查询 销售执行表 中的买赠策略hid 和 存货基本ID
									// 给出入库表中自定义10 和11 进行回写
									// 。因为后面在回写销售发票的时候被二次开发过，所以必须要回写自定义11字段(买赠业务)
									String sellSql = " select  vdef10,vdef11  from so_saleexecute where dr = 0 and csale_bid = '"
											+ salebvo.getCorder_bid() + "'";
									ArrayList sellList = (ArrayList) iuap
											.executeQuery(sellSql,
													new ArrayListProcessor());
									if (null != sellList && sellList.size() > 0) {
										Object[] tmpsell = (Object[]) sellList
												.get(0);
										generalb
												.setVuserdef10(WDSTools
														.getString_TrimZeroLenAsNull(tmpsell[0]));
										generalb
												.setAttributeValue(
														"vuserdef11",
														WDSTools
																.getString_TrimZeroLenAsNull(tmpsell[1]));
									}
								}
								generalb.setCinventoryid(salebvo
										.getCinventoryid());// 存货ID
								generalb.setVbatchcode(outbvo[i]
										.getLvbatchcode());// 批次号
								// 查询生产日期和失效日期
								String sql = "select dproducedate ,dvalidate  from scm_batchcode where pk_invbasdoc='"
										+ salebvo.getCinvbasdocid()
										+ "' and vbatchcode='"
										+ outbvo[i].getLvbatchcode()
										+ "' and dr=0";
								ArrayList batchList = (ArrayList) iuap
										.executeQuery(sql,
												new ArrayListProcessor());
								if (null != batchList && batchList.size() > 0) {
									Object[] batch = (Object[]) batchList
											.get(0);
									// 生产日期
									if (null != batch[0]
											&& !"".equals(batch[0]))
										generalb.setScrq(new UFDate(batch[0]
												.toString()));
									// 失效日期
									if (null != batch[0]
											&& !"".equals(batch[0]))
										generalb.setDvalidate(new UFDate(
												batch[1].toString()));
									isBatch = true;
								}
								generalb.setDbizdate(outhvo.getDbilldate());// 业务日期
								generalb.setNshouldoutnum(salebvo.getNnumber());// 应出数量
								generalb.setNshouldoutassistnum(salebvo
										.getNpacknumber());// 应出辅数量
								generalb.setNoutnum(outbvo[i].getNoutnum());// 实出数量
								locatorvo.setNoutspacenum(outbvo[i]
										.getNoutnum());
								generalb.setNoutassistnum(outbvo[i]
										.getNoutassistnum());// 实出辅数量
								locatorvo.setNoutspaceassistnum(outbvo[i]
										.getNoutassistnum());
								locatorvo.setCspaceid(outbvo[i].getCspaceid());// 货位ID
								generalb.setCastunitid(outbvo[i]
										.getCastunitid());// 辅计量单位ID
								generalb.setNprice(outbvo[i].getNprice());// 单价
								generalb.setNmny(outbvo[i].getNmny());// 金额
								generalb
										.setCsourcebillhid(salehvo.getCsaleid());// 来源单据表头序列号
								generalb.setCfirstbillhid(salehvo.getCsaleid());// 源头单据表头ID
								generalb.setCfreezeid(salebvo.getCorder_bid());// 锁定来源
								generalb.setCsourcebillbid(salebvo
										.getCorder_bid());// 来源单据表体序列号
								generalb.setCfirstbillbid(salebvo
										.getCorder_bid());// 源头单据表体ID
								generalb.setCsourcetype(salehvo
										.getCreceipttype());// 来源单据类型
								generalb.setCfirsttype(salehvo
										.getCreceipttype());// 源头单据类型
								generalb.setVsourcebillcode(salehvo
										.getVreceiptcode());// 来源单据号
								generalb.setVfirstbillcode(salehvo
										.getVreceiptcode());// 源头单据号
								generalb.setVsourcerowno(salebvo.getCrowno());// 来源单据行号
								generalb.setVfirstrowno(salebvo.getCrowno());// 源头单据行号
								generalb.setFlargess(salebvo.getBlargessflag());// 是否赠品
								generalb.setDfirstbilldate(salehvo
										.getDmakedate());// 源头单据制单日期
								generalb.setCreceieveid(salebvo
										.getCreceiptcorpid());// 收货单位
								generalb.setCrowno(outbvo[i].getCrowno());// 行号
								generalb.setHsl(outbvo[i].getHsl());// 换算率
								generalb.setNsaleprice(salebvo.getNnetprice());// 销售价格
								generalb.setNtaxprice(salebvo.getNtaxprice());// 含税单价
								generalb.setNtaxmny(salebvo.getNtaxmny());// 含税金额
								generalb.setAttributeValue("cquoteunitid",
										salebvo.getCquoteunitid());// 报价计量单位
								generalb.setNsalemny(salebvo.getNmny());// 不含税金额
								generalb.setAttributeValue("cquotecurrency",
										salebvo.getCcurrencytypeid());// 设置币种
								LocatorVO[] locatorVO = new LocatorVO[] { locatorvo };
								generalb.setLocator(locatorVO);
								if (isBatch)
									// 给出库子表集合添加对象
									generalBVOList.add(generalb);
							}
						}
					}
					// 转换数组
					generalBVO = new GeneralBillItemVO[generalBVOList.size()];
					generalBVO = generalBVOList.toArray(generalBVO);
					// 聚合VO表头赋值
					gBillVO.setParentVO(generalhvo);
					// 聚合VO表体赋值
					gBillVO.setChildrenVO(generalBVO);
				}
			}

		}

		return gBillVO;
	}

	/**
	 * 把模板中的选中的VO 进行转换成销售出库的VO
	 * 
	 * @param vos
	 *            页面选中的聚合VO
	 * @return 销售的聚合VO
	 * @throws BusinessException
	 */
	private MyBillVO changeReqFydtoOutgeneral(AggregatedValueObject[] vos)
			throws BusinessException {
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		// hiddenList = new ArrayList();
		MyBillVO myBillVO = new MyBillVO();
		TbOutgeneralHVO generalHVO = new TbOutgeneralHVO();
		// 子表信息数组集合
		List<TbOutgeneralBVO> generalBList = new ArrayList<TbOutgeneralBVO>();

		SaleorderHVO salehVO = (SaleorderHVO) vos[0].getParentVO();
		if (null != salehVO.getCwarehouseid()
				&& !"".endsWith(salehVO.getCwarehouseid())) {
			generalHVO.setSrl_pk(salehVO.getCwarehouseid()); // 出库仓库

		} else {
			if (null != salehVO.getCcustomerid()
					&& !"".equals(salehVO.getCcustomerid())) {
				String sql_stor = " pk_cubasdoc in "
						+ " (select pk_cubasdoc from bd_cumandoc where pk_cumandoc='"
						+ salehVO.getCcustomerid() + "')";
				ArrayList srl_pks = new ArrayList();
				try {
					srl_pks = (ArrayList) query.retrieveByClause(
							TbStorcubasdocVO.class, sql_stor.toString());

				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (null != srl_pks && srl_pks.size() > 0) {
					if (null != srl_pks.get(0)) {
						TbStorcubasdocVO tbStorcubasdocVO = (TbStorcubasdocVO) srl_pks
								.get(0);
						if (null != tbStorcubasdocVO.getPk_stordoc()
								&& !"".equals(tbStorcubasdocVO.getPk_stordoc())) {
							generalHVO.setSrl_pk(tbStorcubasdocVO
									.getPk_stordoc());
						}
					}
				}

			}
		}
		generalHVO.setCbiztype(salehVO.getCbiztype()); // 业务类型主键
		generalHVO.setCdptid(salehVO.getCdeptid()); // 部门
		generalHVO.setCbizid(salehVO.getCemployeeid()); // 业务员
		generalHVO.setCcustomerid(salehVO.getCcustomerid()); // 客户
		generalHVO.setVdiliveraddress(salehVO.getVreceiveaddress()); // 收货地址
		generalHVO.setVnote(salehVO.getVnote()); // 备注
		generalHVO.setVsourcebillcode(salehVO.getVreceiptcode()); // 来源单据号
		generalHVO.setDauditdate(salehVO.getDapprovedate()); // 审核日期
		generalHVO.setCsourcebillhid(salehVO.getCsaleid()); // 来源单据表头主键
		generalHVO.setVbilltype(salehVO.getCreceipttype().toString()); // 单据类型

		// 循环缓存中的子表信息
		for (int i = 0; i < saleOrderDlg.getSaleVO().length; i++) {
			if (salehVO.getCsaleid().equals(
					saleOrderDlg.getSaleVO()[i].getHeadVO().getCsaleid())) {
				vos[0].setChildrenVO(saleOrderDlg.getSaleVO()[i].getBodyVOs());
				break;
			}
		}
		myBillVO.setParentVO(generalHVO);
		// 循环缓存中的子表信息
		if (null != vos[0].getChildrenVO() && vos[0].getChildrenVO().length > 0) {
			SaleorderBVO[] salebVOs = (SaleorderBVO[]) vos[0].getChildrenVO();
			// 获取缓存 转换子表其他的信息
			for (int i = 0; i < salebVOs.length; i++) {
				SaleorderBVO salebVO = salebVOs[i];
				if (salehVO.getCsaleid().equals(salebVO.getCsaleid())) {
					TbOutgeneralBVO generalBVO = new TbOutgeneralBVO();
					setGeneralbVO(salehVO, salebVO, generalBVO);
					// generalBVO.setIsoper(new UFBoolean("Y"));
					generalBList.add(generalBVO);
				}
			}
			if (null != generalBList && generalBList.size() > 0) {
				TbOutgeneralBVO[] generalBVO = new TbOutgeneralBVO[generalBList
						.size()];
				generalBVO = generalBList.toArray(generalBVO);
				myBillVO.setChildrenVO(generalBVO);
			}
		}
		return myBillVO;
	}

	/**
	 * 给发运单子表中一些属性赋值 VO转换
	 * 
	 * @param fydnew
	 *            发运单主表
	 * @param fydmxnewvo
	 *            发运单子表
	 * @param generalBVO
	 *            出库单子表
	 */
	private void setGeneralbVO(SaleorderHVO salehVO, SaleorderBVO salebVO,
			TbOutgeneralBVO generalBVO) {
		generalBVO.setCsourcebillhid(salehVO.getCsaleid()); // 来源单据表头主键
		generalBVO.setVsourcebillcode(salehVO.getVreceiptcode()); // 来源单据号
		generalBVO.setCsourcetype(salehVO.getCreceipttype().toString()); // 来源单据类型
		generalBVO.setCsourcebillbid(salebVO.getCorder_bid()); // 来源单据表体主键
		generalBVO.setCfirstbillhid(salebVO.getCsaleid()); // 源头单据表头主键
		generalBVO.setCfirstbillbid(salebVO.getCorder_bid()); // 源头单据表体主键
		generalBVO.setCrowno(salebVO.getCrowno()); // 行号
		generalBVO.setNshouldoutnum(salebVO.getNnumber()); // 应发数量
		generalBVO.setNshouldoutassistnum(salebVO.getNpacknumber()); // 应发辅数量
		generalBVO.setCinventoryid(salebVO.getCinvbasdocid()); // 存货主键
		generalBVO.setFlargess(salebVO.getBlargessflag()); // 是否赠品
		generalBVO.setLvbatchcode("2009");// 回写批次
	}

	public Iw8004040204 getIw() {
		return iw;
	}

	public void setIw(Iw8004040204 iw) {
		this.iw = iw;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-1-9 8:37:34)
	 * 
	 * @return nc.ui.trade.pub.BillCardUI
	 */
	private BillManageUI getBillManageUI() {
		return (BillManageUI) getBillUI();
	}
}