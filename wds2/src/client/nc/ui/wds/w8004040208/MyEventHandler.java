package nc.ui.wds.w8004040208;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.wds.w8004040204.Iw8004040204;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
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
import nc.ui.wds.w8000.W8004040204Action;
import nc.ui.wds.w8004040204.TrayDisposeDetailDlg;
import nc.ui.wds.w8004040204.TrayDisposeDlg;
import nc.ui.wds.w8004040204.ssButtun.ISsButtun;
import nc.ui.wl.pub.LoginInforHelper;
import nc.vo.dm.order.SendorderBVO;
import nc.vo.dm.order.SendorderVO;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.to.pub.ConstVO;
import nc.vo.wds.pub.WDSTools;
import nc.vo.wds.pub.WdsWlPubConsts;
import nc.vo.wds.w80021030.TbQycgjh2VO;
import nc.vo.wds.w80021030.TbQycgjhVO;
import nc.vo.wds.w8004040204.TbOutgeneralBVO;
import nc.vo.wds.w8004040204.TbOutgeneralHVO;
import nc.vo.wds.w8004040208.MyBillVO;
import nc.vo.wds.w80060406.TbFydmxnewVO;
import nc.vo.wds.w80060406.TbFydnewVO;
import nc.vo.wds.w80060608.IcGeneralBVO;
import nc.vo.wds.w80060608.IcGeneralHVO;
import nc.vo.wl.pub.WdsWlPubTool;

/**
 * 
 * 其他出库
 * 
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	MyClientUI myClientUI = null;
	private String m_logUser = null;

	private Iw8004040204 iw04 = null;
	private Integer isControl = -1; // 是否有权限操作当前单据 1为可以签字的用户 0 修改操作 2 可以进行出库
	// 3所有权限

	// 根据当前登录者查询所属仓库和其仓库所存储的产品
	private List pkList = null;

	private FydnewDlg fydnewdlg = null; // 转库查询方法的类

	private CgqyDlg cgqydlg = null; // 其他出库中采购取样查询方法的类

	boolean isAdd = false;

	int isTypes = 0; // 记录操作类型 0默认值，1转库，2取样采购，3自制单据

	// private List hiddenList = null;
	private boolean isStock = false; // 是否为总仓 true 总仓 false 分仓
	private IUAPQueryBS iuap = null;

	private String pk_stock = ""; // 当前登录者对应的仓库主键
	String billType = null; // 0 转库 9采购取样 8自制单据
	
	private Iw8004040204 getIwBO(){
		if(iw04 == null){
			iw04 = (Iw8004040204) NCLocator.getInstance().getInstance().lookup(
					Iw8004040204.class.getName());
		}
		return iw04;
	}
	
	private IUAPQueryBS getQueryBO(){
		if(iuap == null){
			iuap = (IUAPQueryBS) NCLocator.getInstance().lookup(
					IUAPQueryBS.class.getName());
		}
		return iuap;
	}


	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
		myClientUI = (MyClientUI) billUI;
		m_logUser = myClientUI._getOperator();
		// 无论什么角色第一次加载托盘指定按钮和自动取货都设置不可用
		changeButton(false);
		try {
			// 获取当前登录者查询人员绑定表 是属于哪个仓库，
			pk_stock = LoginInforHelper.getWhidByUser(m_logUser);
			int iType = LoginInforHelper.getITypeByUser(m_logUser);
			if (iType == 0) {
				isStock = WdsWlPubTool.isZc(LoginInforHelper.getCwhid(m_logUser));
				pkList = Arrays.asList(LoginInforHelper.getInvBasDocIDsByUserID(m_logUser));
				isControl = 2;
			} else if (iType==1) {
				isControl = 1;
				getButtonManager().getButton(ISsButtun.zj).setEnabled(false);
				myClientUI.updateButtons();
			} else if (iType==2) {
				isControl = 0;

			} else if (iType==3) {
				isControl = 3;
				isStock = WdsWlPubTool.isZc(LoginInforHelper.getCwhid(m_logUser));
				pkList = Arrays.asList(LoginInforHelper.getInvBasDocIDsByUserID(m_logUser));
			}
			if (isControl != 1) {
				getButtonManager().getButton(
						nc.ui.wds.w80060206.buttun0206.ISsButtun.Qzqr)
						.setEnabled(false);
				getButtonManager().getButton(
						nc.ui.wds.w80060206.buttun0206.ISsButtun.Qxqz)
						.setEnabled(false);
				myClientUI.updateButtons();
			}
		} catch (Exception e) {
			e.printStackTrace();
			myClientUI.showErrorMessage(WdsWlPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
		}

	}

	private int lineNo = -1;

	@Override
	protected void onBoLinePaste() throws Exception {
		super.onBoLinePaste();
		if (lineNo != -1) {
			lineNo = lineNo + 10;
			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(
					lineNo + "",
					getBillCardPanelWrapper().getBillCardPanel().getBillTable()
							.getSelectedRow(), "crowno");
			// 来源单据类型
			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(
					"8",
					getBillCardPanelWrapper().getBillCardPanel().getBillTable()
							.getSelectedRow(), "csourcetype");
		}

	}

	// 插入行
	@Override
	protected void onBoLineIns() throws Exception {
		super.onBoLineIns();
		if (lineNo != -1) {
			lineNo = lineNo + 10;
			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(
					lineNo + "",
					getBillCardPanelWrapper().getBillCardPanel().getBillTable()
							.getSelectedRow(), "crowno");
			// 来源单据类型
			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(
					"8",
					getBillCardPanelWrapper().getBillCardPanel().getBillTable()
							.getSelectedRow(), "csourcetype");
		}
	}

	protected void onBoLineAdd() throws Exception {
		super.onBoLineAdd();
		if (lineNo == -1)
			lineNo = 10;
		else
			lineNo = lineNo + 10;
		// 行号
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(
				lineNo + "",
				getBillCardPanelWrapper().getBillCardPanel().getBillTable()
						.getSelectedRow(), "crowno");
		// 来源单据类型
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(
				"8",
				getBillCardPanelWrapper().getBillCardPanel().getBillTable()
						.getSelectedRow(), "csourcetype");
	}

	protected IBusinessController createBusinessAction() {
		switch (getUIController().getBusinessActionType()) {
		case IBusinessActionType.PLATFORM:
			return new BusinessAction(getBillUI());
		case IBusinessActionType.BD:
			return new W8004040204Action(getBillUI());
		default:
			return new BusinessAction(getBillUI());
		}
	}

	// 取消签字
	protected void onQxqz() throws Exception {
		myClientUI.showHintMessage("正在执行取消签字...");
		if (getBufferData().getCurrentRow() >= 0) {
			int retu = myClientUI.showOkCancelMessage("确认取消签字?");
			if (retu == 1) {
				TbOutgeneralHVO generalh = (TbOutgeneralHVO) getBufferData()
						.getCurrentVO().getParentVO();
				Object result = getQueryBO().retrieveByPK(TbOutgeneralHVO.class,
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

						ArrayList list = (ArrayList) getQueryBO().retrieveByClause(
								IcGeneralHVO.class, strWhere);
						if (null != list && list.size() > 0) {
							IcGeneralHVO header = (IcGeneralHVO) list.get(0);
							if (null != header) {
								strWhere = " dr = 0 and cgeneralhid = '"
										+ header.getCgeneralhid() + "'";
								list = (ArrayList) getQueryBO().retrieveByClause(
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
									getIwBO().whs_processAction("CANCELSIGN",
											"DELETE", "4I", _getDate()
													.toString(), billvo,
											generalh);

									myClientUI.showHintMessage("操作成功");
									// getBufferData().getCurrentVO().setParentVO(
									// generalh);
									// updateBuffer();
									super.onBoRefresh();
									getButtonManager()
											.getButton(
													nc.ui.wds.w80060206.buttun0206.ISsButtun.Qzqr)
											.setEnabled(true);
									getButtonManager()
											.getButton(
													nc.ui.wds.w80060206.buttun0206.ISsButtun.Qxqz)
											.setEnabled(false);
									getButtonManager().getButton(
											IBillButton.Edit).setEnabled(true);
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
				Object result = getQueryBO().retrieveByPK(TbOutgeneralHVO.class,
						generalh.getGeneral_pk());
				if (null != result) {
					generalh = (TbOutgeneralHVO) result;
					if (null != generalh.getVbillstatus()
							&& generalh.getVbillstatus() == 0) {
						myClientUI.showErrorMessage("该单据已经签字");
						return;
					}
					// 必须确认后的才能签字
					else {
						if (generalh.getVbilltype().equals("0")
								&& (null == generalh.getVbillstatus() || generalh
										.getVbillstatus() != 1)) {
							myClientUI.showErrorMessage("该单据还没有进行运单确认");
							return;
						}
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
						getIwBO().whs_processAction("SAVEPICKSIGN", null, "4I",
								_getDate().toString(), billvo, generalh);

						myClientUI.showHintMessage("签字成功");
						// getBufferData().getCurrentVO().setParentVO(generalh);
						// updateBuffer();
						super.onBoRefresh();
						getButtonManager().getButton(
								nc.ui.wds.w80060206.buttun0206.ISsButtun.Qzqr)
								.setEnabled(false);
						getButtonManager().getButton(
								nc.ui.wds.w80060206.buttun0206.ISsButtun.Qxqz)
								.setEnabled(true);
						getButtonManager().getButton(IBillButton.Edit)
								.setEnabled(false);
						myClientUI.updateButtons();
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

	/*
	 * 转库(non-Javadoc)
	 * 
	 * @see nc.ui.wds.w8004040208.AbstractMyEventHandler#onzk()
	 */
	protected void onzk() throws Exception {
		lineNo = -1;
		if (isControl == 2 || isControl == 3) {
			isAdd = true;
			if(fydnewdlg == null){
				fydnewdlg = new FydnewDlg(null, ClientEnvironment.getInstance()
							.getCorporation().getPrimaryKey(),
							ClientEnvironment.getInstance().getUser()
							.getPrimaryKey(), WdsWlPubConsts.DM_ORDER_NODECODE, "1=1", 
							WdsWlPubConsts.WDS3, null, null,"4C", myClientUI, pkList, isStock, pk_stock);
			}
			// 调用方法 获取查询后的聚合VO
			AggregatedValueObject[] vos = fydnewdlg
					.getReturnVOs(ClientEnvironment.getInstance()
							.getCorporation().getPrimaryKey(),
							ClientEnvironment.getInstance().getUser()
									.getPrimaryKey(), WdsWlPubConsts.WDS3,
							ConstVO.m_sBillDRSQ,WdsWlPubConsts.OTHER_OUT_FUNCODE, 
							WdsWlPubConsts.OTHER_OUT_REFWDS3_NODECODE,fydnewdlg);
			// 判断是否对查询模板有进行操作
			if (null == vos || vos.length == 0) {
				return;
			}
			// 调用转换类 把模板中获取的对象转换成自己的当前显示的对象，调用方法
			// queryLocaData方法先查询本地表是否有数据，有数据说明该单据不是第一次增加。
			MyBillVO voForSave = changeReqFydtoOutgeneral(vos);// 该方法是第一次增加进行数据转换

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
			setViewPro();
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
					"ccunhuobianma").setEnabled(false);
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
					"nshouldoutassistnum").setEnabled(false);
			billType = "0";
			changeButton(true);
			isTypes = 1;
		} else {
			myClientUI.showErrorMessage("操作失败,您无权操作");
			return;
		}
	}

	/**
	 * 给界面上一些属性赋值
	 * 
	 * @throws BusinessException
	 */
	private void setViewPro() throws BusinessException {
		// 设置制单人
		getBillCardPanelWrapper().getBillCardPanel().getHeadTailItem(
				"coperatorid").setValue(
				ClientEnvironment.getInstance().getUser().getPrimaryKey());
		// 设置修改人
		getBillCardPanelWrapper().getBillCardPanel().getHeadTailItem(
				"clastmodiid").setValue(
				ClientEnvironment.getInstance().getUser().getPrimaryKey());
		// 单据日期
		getBillCardPanelWrapper().getBillCardPanel().getHeadTailItem(
				"dbilldate").setValue(_getDate().toString());
		if (isControl == 3) {
			// 设置收发类别和备注可编辑
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"cdispatcherid").setEnabled(true);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vnote")
					.setEnabled(true);
			// 部门
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("cdptid")
					.setEnabled(true);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("cdptid")
					.setValue("1021B110000000000BN9");
			// 库管员
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"cwhsmanagerid").setEnabled(true);
		}
		getButtonManager().getButton(
				nc.ui.wds.w80060206.buttun0206.ISsButtun.Qzqr)
				.setEnabled(false);
		myClientUI.updateButtons();
	}

	/*
	 * 采购取样(non-Javadoc)
	 * 
	 * @see nc.ui.wds.w8004040208.AbstractMyEventHandler#oncgqy()
	 */
	protected void oncgqy() throws Exception {
		lineNo = -1;
		if (isControl == 2 || isControl == 3) {
			isAdd = true;

			cgqydlg = new CgqyDlg(myClientUI, pkList, isStock);

			// 调用方法 获取查询后的聚合VO
			AggregatedValueObject[] vos = cgqydlg
					.getReturnVOs(ClientEnvironment.getInstance()
							.getCorporation().getPrimaryKey(),
							ClientEnvironment.getInstance().getUser()
									.getPrimaryKey(), "4278",
							ConstVO.m_sBillDRSQ, "8004040208", "8004040278",
							myClientUI);
			// 判断是否对查询模板有进行操作
			if (null == vos || vos.length == 0) {
				return;
			}
			// 调用转换类 把模板中获取的对象转换成自己的当前显示的对象，调用方法
			// queryLocaDateByCgqy方法先查询本地表是否有数据，有数据说明该单据不是第一次增加。
			MyBillVO voForSave = changeReqQycgtoOutgeneral(vos);// 该方法是第一次增加进行数据转换

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
			setViewPro();

			getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
					"ccunhuobianma").setEnabled(false);
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
					"nshouldoutassistnum").setEnabled(false);
			billType = "9";
			changeButton();
			isTypes = 2;
		} else {
			myClientUI.showErrorMessage("操作失败,您无权操作");
			return;
		}
	}

	private void changeButton() {
		getButtonManager().getButton(ISsButtun.zdqh).setEnabled(false);
		getButtonManager().getButton(ISsButtun.tpzd).setEnabled(true);
		getButtonManager().getButton(ISsButtun.zj).setEnabled(false);
		myClientUI.updateButtons();
	}

	/**
	 * 验证单据日期，表体是否有空数据
	 * 
	 * @param generalb
	 * @return
	 */
	private boolean validate(TbOutgeneralBVO[] generalb) {
		String billdate = (String) getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("dbilldate").getValue();
		if (null == billdate || "".equals(billdate)) {
			myClientUI.showErrorMessage("请选择单据日期");
			return false;
		}
		for (int i = 0; i < generalb.length; i++) {
			TbOutgeneralBVO genb = generalb[i];
			if (null == genb.getCinventoryid()
					|| "".equals(genb.getCinventoryid())) {
				myClientUI.showErrorMessage("请填写表体数据");
				return false;
			}
			for (int j = 0; j < generalb.length; j++) {
				if (j == i)
					continue;
				if ((genb.getCinventoryid() + "1").equals(generalb[j]
						.getCinventoryid()
						+ "1")) {
					myClientUI.showErrorMessage("表体中包含相同单品,请去除");
					return false;
				}
			}
		}
		return true;
	}

	private boolean validate(TbOutgeneralHVO generalh) {
		// 进行判断弹出对话框提示
		if (generalh.getCdispatcherid() == null
				|| "".equals(generalh.getCdispatcherid().trim())) {
			getBillUI().showWarningMessage("请选择收发类别");
		} else if (generalh.getCwhsmanagerid() == null
				|| "".equals(generalh.getCwhsmanagerid())) {
			getBillUI().showWarningMessage("请选择库管员");
		} else if (generalh.getCdptid() == null
				|| "".equals(generalh.getCdptid())) {
			getBillUI().showWarningMessage("请选择部门");
		} else {
			return true;
		}
		return false;
	}

	@Override
	protected void onBoSave() throws Exception {
		
		// 获取当前页面中VO
		AggregatedValueObject myBillVO = getBillUI().getVOFromUI();
		setTSFormBufferToVO(myBillVO);
		TbOutgeneralBVO[] generalb = (TbOutgeneralBVO[]) myBillVO
				.getChildrenVO();
		TbOutgeneralHVO tmpgeneralh = null;
		TbOutgeneralHVO generalh = (TbOutgeneralHVO) myBillVO.getParentVO();
		// 验证表体是否有数据
		if (null == generalb || generalb.length < 0) {
			myClientUI.showErrorMessage("操作失败,表体无数据不能保存");
			return;
		}
		// 运单的Object,里面共三个值，[0]true or false false为生成运单失败
		// [1]运单主表对象集合List [2]运单子表对象集合List
		Object[] obj = null;
		// 如果类型是自制单据进行验证
		if (null != billType && billType.equals("8") && isAdd) {
			// 验证表体信息
			if (!validate(generalb))
				return;
			// 是否生成运单
			if (generalh.getIs_yundan().booleanValue())
				obj = insertFyd(generalh, generalb);
		} else {
			obj = new Object[1];
			obj[0] = false;
		}
		// 内勤人员
		if (isControl == 3) {
			// 如果是转库
			if (null != billType && billType.equals("0")) {
				// 根据来源单据号查询是否有做过出库，如果有已经做过出库则新增到已有的出库单上去
				String strWhere = " dr = 0 and vsourcebillcode = '"
						+ generalh.getVsourcebillcode()
						+ "' and csourcebillhid = '"
						+ generalh.getCsourcebillhid() + "'";
				ArrayList tmpList = (ArrayList) iuap.retrieveByClause(
						TbOutgeneralHVO.class, strWhere);
				if (null != tmpList && tmpList.size() > 0) {
					tmpgeneralh = (TbOutgeneralHVO) tmpList.get(0);
				}
//				// 该单据有过出库记录
//				if (null != tmpgeneralh) {
//					tmpgeneralh.setCdispatcherid(generalh.getCdispatcherid());// 收发类别
//					tmpgeneralh.setCwhsmanagerid(generalh.getCwhsmanagerid());// 库管员
//					tmpgeneralh.setCdptid(generalh.getCdptid());// 部门
//					tmpgeneralh.setVnote(generalh.getVnote());// 备注
//					// 把表头替换
//					generalh = tmpgeneralh;
//				} else 
				 if(tmpgeneralh ==null){
					isAdd = true;
					// 制单时间
					generalh.setTmaketime(myClientUI._getServerTime()
							.toString());
					// 设置单据号
					generalh.setVbillcode(CommonUnit.getBillCode("4I",
							ClientEnvironment.getInstance().getCorporation()
									.getPk_corp(), "", ""));
					// 单据类别
					generalh.setVbilltype(billType);
					// 公司
					generalh.setComp(ClientEnvironment.getInstance()
							.getCorporation().getPk_corp());
				}
				// 如果是新增设置表体状态“新增”
				if (isAdd) {
					// 循环表体更改状态
					for (int i = 0; i < generalb.length; i++) {
						generalb[i].setStatus(VOStatus.NEW);
					}
				} else { // 设置表体状态“修改”
					// 循环表体更改状态
					for (int i = 0; i < generalb.length; i++) {
						generalb[i].setStatus(VOStatus.UPDATED);
					}
				}

			} else {
				// 新增状态
				if (isAdd) {

					// 循环表体更改状态
					for (int i = 0; i < generalb.length; i++) {
						generalb[i].setStatus(VOStatus.NEW);
					}
					// 制单时间
					generalh.setTmaketime(myClientUI._getServerTime()
							.toString());
					// 设置单据号
					generalh.setVbillcode(CommonUnit.getBillCode("4I",
							ClientEnvironment.getInstance().getCorporation()
									.getPk_corp(), "", ""));
					// 单据类别
					generalh.setVbilltype(billType);
					// 公司
					generalh.setComp(ClientEnvironment.getInstance()
							.getCorporation().getPk_corp());
				} else {
					// 循环表体更改状态
					for (int i = 0; i < generalb.length; i++) {
						generalb[i].setStatus(VOStatus.UPDATED);
					}

				}
			}
		} else if (isControl == 1) { // 如果是信息科进行保存
			// 验证表头信息
			if (!validate(generalh))
				return;

		} else {
			// 根据来源单据号查询是否有做过出库
			String strWhere = " dr = 0 and vsourcebillcode = '"
					+ generalh.getVsourcebillcode()
					+ "' and csourcebillhid = '" + generalh.getCsourcebillhid()
					+ "'";
			ArrayList tmpList = (ArrayList)getQueryBO().retrieveByClause(
					TbOutgeneralHVO.class, strWhere);
			if (null != tmpList && tmpList.size() > 0) {
				tmpgeneralh = (TbOutgeneralHVO) tmpList.get(0);
			}
			// 该单据有过出库记录
			if (null != tmpgeneralh && !billType.equals("8")) {
				// 把表头替换
				generalh = tmpgeneralh;
			} else if (!billType.equals("8")) {
				isAdd = true;
				// 制单时间
				generalh.setTmaketime(myClientUI._getServerTime().toString());
				// 设置单据号
				generalh.setVbillcode(CommonUnit.getBillCode("4I",
						ClientEnvironment.getInstance().getCorporation()
								.getPk_corp(), "", ""));
			} else if (isAdd) {
				// 制单时间
				generalh.setTmaketime(myClientUI._getServerTime().toString());
			}
			// 如果是新增设置表体状态“新增”
			if (isAdd) {
				// 循环表体更改状态
				for (int i = 0; i < generalb.length; i++) {
					generalb[i].setStatus(VOStatus.NEW);
				}
			} else { // 设置表体状态“修改”
				// 循环表体更改状态
				for (int i = 0; i < generalb.length; i++) {
					generalb[i].setStatus(VOStatus.UPDATED);
				}
			}
			// 单据类别
			generalh.setVbilltype(billType);
			// 单据状态
			// generalh.setVbillstatus(new Integer(1));// 没有签字
			// 公司
			generalh.setComp(ClientEnvironment.getInstance().getCorporation()
					.getPk_corp());
		}
		// 最后修改时间
		generalh.setTlastmoditime(myClientUI._getServerTime().toString());
		// 设置修改人
		generalh.setClastmodiid(ClientEnvironment.getInstance().getUser()
				.getPrimaryKey());

		myBillVO.setParentVO(generalh);
		myBillVO.setChildrenVO(generalb);
		// 进行数据晴空
		Object o = null;
		ISingleController sCtrl = null;
		if (getUIController() instanceof ISingleController) {
			sCtrl = (ISingleController) getUIController();
			if (sCtrl.isSingleDetail()) {
				o = myBillVO.getParentVO();
				myBillVO.setParentVO(null);
			} else {
				o = myBillVO.getChildrenVO();
				myBillVO.setChildrenVO(null);
			}
		}

		boolean isSave = true;

		// 判断是否有存盘数据
		if (myBillVO.getParentVO() == null
				&& (myBillVO.getChildrenVO() == null || myBillVO
						.getChildrenVO().length == 0)) {
			isSave = false;
		} else {
			if (getBillUI().isSaveAndCommitTogether())
				myBillVO = getBusinessAction().saveAndCommit(myBillVO,
						getUIController().getBillType(), _getDate().toString(),
						getBillUI().getUserObject(), myBillVO);
			
			else {

				List objUser = new ArrayList();
				objUser.add(getBillUI().getUserObject());
				objUser.add(generalb);
				if (isAdd)
					objUser.add(true);
				else
					objUser.add(false);
				objUser.add(true);

				objUser.add(isStock);

				objUser.add(obj);
				// write to database
				myBillVO = getBusinessAction().save(myBillVO,
						getUIController().getBillType(), _getDate().toString(),
						objUser, myBillVO);
				// myBillVO.setChildrenVO(generalb);
				if (null == myBillVO) {
					myClientUI.showErrorMessage("操作失败,请重新操作该单据");
					return;
				}
				// 清除颜色
				noColor((TbOutgeneralBVO[]) myBillVO.getChildrenVO());
				// 托盘指定按钮和自动取货按钮不可用
				changeButton(false);
				// 判断如果是自制单据类型 增行按钮不可用表体参照和应发辅数量不可用
				if (null != billType && billType.equals("8")) {
					getButtonManager().getButton(IBillButton.Line).setVisible(
							false);

					getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
							"ccunhuobianma").setEnabled(false);
					getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
							"nshouldoutassistnum").setEnabled(false);
				}
				if (isControl == 1 || isControl == 3) {
					getButtonManager().getButton(
							nc.ui.wds.w80060206.buttun0206.ISsButtun.Qzqr)
							.setEnabled(true);
					myClientUI.updateButtons();
				}
			}
		}

		// 进行数据恢复处理
		if (sCtrl != null) {
			if (sCtrl.isSingleDetail())
				myBillVO.setParentVO((CircularlyAccessibleValueObject) o);
		}
		int nCurrentRow = -1;
		if (isSave) {
			if (isEditing()) {
				if (getBufferData().isVOBufferEmpty()) {
					getBufferData().addVOToBuffer(myBillVO);
					nCurrentRow = 0;

				} else {
					getBufferData().setCurrentVO(myBillVO);
					nCurrentRow = getBufferData().getCurrentRow();
				}
			}
			// 新增后操作处理
			setAddNewOperate(isAdding(), myBillVO);
		}
		// 设置保存后状态
		setSaveOperateState();
		if (nCurrentRow >= 0) {
			getBufferData().setCurrentRow(nCurrentRow);
		}
		myClientUI.updateButtons();	
		}

	/**
	 * 生成发运单
	 * 
	 * @throws Exception
	 */
	private Object[] insertFyd(TbOutgeneralHVO generalh,
			TbOutgeneralBVO[] generalb) throws Exception {
		Object[] o = new Object[3];
		o[0] = false;
		List<TbFydnewVO> fydList = new ArrayList<TbFydnewVO>();
		List<TbFydmxnewVO[]> fydmxList = new ArrayList<TbFydmxnewVO[]>();

		// 进行VO转换/////////////////////////////////////////////

		// ------------转换表头对象-----------------//
		TbFydnewVO fydvo = new TbFydnewVO();
		if (null != generalh && null != generalb && generalb.length > 0) {
			if (null != generalh.getVdiliveraddress()
					&& !"".equals(generalh.getVdiliveraddress())) {
				fydvo.setFyd_shdz(generalh.getVdiliveraddress()); // 收货地址
			}
			if (null != generalh.getVnote() && !"".equals(generalh.getVnote())) {
				fydvo.setFyd_bz(generalh.getVnote()); // 备注
			}
			if (null != generalh.getCdptid()
					&& !"".equals(generalh.getCdptid())) {
				fydvo.setCdeptid(generalh.getCdptid()); // 部门
			}
			// 设置运货方式
			fydvo.setFyd_yhfs("汽运");
			// 单据类型 0 发运制单 1 销售订单 2 分厂直流 3拆分订单4 合并订单 8 出库自制单据生成的运单
			fydvo.setBilltype(8);
			fydvo.setVbillstatus(1);
			// 单据号
			fydvo.setVbillno(generalh.getVbillcode());
			// 制单日期
			fydvo.setDmakedate(generalh.getDbilldate());
			fydvo.setVoperatorid(ClientEnvironment.getInstance().getUser()
					.getPrimaryKey()); // 设置制单人
			// 设置发货站
			fydvo.setSrl_pk(generalh.getSrl_pk());
			// 到货站
			fydvo.setSrl_pkr(generalh.getSrl_pkr());
			fydList.add(fydvo);
			// --------------转换表头结束---------------//
			// --------------转换表体----------------//
			List<TbFydmxnewVO> tbfydmxList = new ArrayList<TbFydmxnewVO>();
			for (int j = 0; j < generalb.length; j++) {
				TbFydmxnewVO fydmxnewvo = new TbFydmxnewVO();
				TbOutgeneralBVO genb = generalb[j];
				if (null != genb.getCinventoryid()
						&& !"".equals(genb.getCinventoryid())) {
					fydmxnewvo.setPk_invbasdoc(genb.getCinventoryid()); // 单品主键
				}
				if (null != genb.getNshouldoutnum()
						&& !"".equals(genb.getNshouldoutnum())) {
					fydmxnewvo.setCfd_yfsl(genb.getNshouldoutnum()); // 应发数量
				}
				if (null != genb.getNshouldoutassistnum()
						&& !"".equals(genb.getNshouldoutassistnum())) {
					fydmxnewvo.setCfd_xs(genb.getNshouldoutassistnum()); // 箱数
				}
				if (null != genb.getNoutnum() && !"".equals(genb.getNoutnum())) {
					fydmxnewvo.setCfd_sfsl(genb.getNoutnum()); // 实发数量
				}
				if (null != genb.getNoutassistnum()
						&& !"".equals(genb.getNoutassistnum())) {
					fydmxnewvo.setCfd_sffsl(genb.getNoutassistnum()); // 实发辅数量
				}
				if (null != genb.getCrowno() && !"".equals(genb.getCrowno())) {
					fydmxnewvo.setCrowno(genb.getCrowno()); // 行号
				}
				if (null != genb.getUnitid() && !"".equals(genb.getUnitid())) {
					fydmxnewvo.setCfd_dw(genb.getUnitid()); // 单位
				}
				fydmxnewvo.setCfd_pc(genb.getVbatchcode()); // 批次
				tbfydmxList.add(fydmxnewvo);
			}
			// ----------------转换表体结束---------------------//
			if (tbfydmxList.size() > 0) {
				TbFydmxnewVO[] fydmxVO = new TbFydmxnewVO[tbfydmxList.size()];
				tbfydmxList.toArray(fydmxVO);
				fydmxList.add(fydmxVO);
				o[0] = true;
			} else {
				fydmxList.add(null);
			}
		}
		o[1] = fydList;
		o[2] = fydmxList;
		return o;
	}

	@Override
	protected void onBoEdit() throws Exception {
		billType = ((TbOutgeneralHVO) getBufferData().getCurrentVO()
				.getParentVO()).getVbilltype();
		if (isControl == 3) {
			super.onBoEdit();
			// 设置托盘指定按钮可用，修改状态
			isAdd = false;

			// 有修改权限
			// 设置收发类别和备注可编辑
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"cdispatcherid").setEnabled(true);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vnote")
					.setEnabled(true);
			// 部门
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("cdptid")
					.setEnabled(true);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("cdptid")
					.setValue("1021B110000000000BN9");
			// 库管员
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"cwhsmanagerid").setEnabled(true);
			getButtonManager().getButton(ISsButtun.tpzd).setEnabled(true);// 托盘指定
			getButtonManager().getButton(ISsButtun.zj).setEnabled(false);// 增加按钮
			getButtonManager().getButton(IBillButton.Line).setEnabled(false);// 增行
			// 签字按钮不可用
			getButtonManager().getButton(
					nc.ui.wds.w80060206.buttun0206.ISsButtun.Qzqr).setEnabled(
					false);
			myClientUI.updateButtons();
			return;
		}

		// 发运科
		if (isControl == 0) {
			super.onBoEdit();
			// 设置托盘指定按钮可用，修改状态
			isAdd = false;
			getButtonManager().getButton(ISsButtun.tpzd).setEnabled(true);
			getButtonManager().getButton(ISsButtun.zj).setEnabled(false);// 增加按钮
			getButtonManager().getButton(IBillButton.Line).setEnabled(false);// 增行
			myClientUI.updateButtons();

		} else if (isControl == 1) {
			super.onBoEdit();
			// 设置托盘指定按钮可用，修改状态
			isAdd = false;
			// 有修改权限
			// 设置收发类别和备注可编辑
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"cdispatcherid").setEnabled(true);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vnote")
					.setEnabled(true);
			// 部门
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("cdptid")
					.setEnabled(true);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("cdptid")
					.setValue("1021B110000000000BN9");
			// 库管员
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"cwhsmanagerid").setEnabled(true);
			getButtonManager().getButton(
					nc.ui.wds.w80060206.buttun0206.ISsButtun.Qzqr).setEnabled(
					false);
			getButtonManager().getButton(ISsButtun.tpzd).setEnabled(false);// 托盘指定

			getButtonManager().getButton(IBillButton.Line).setEnabled(false);// 增行
			getButtonManager().getButton(ISsButtun.zj).setEnabled(false);// 增加按钮

			getBillUI().updateButtons();

		} else {
			myClientUI.showErrorMessage("您没有权限修改");
			return;
		}
	}

	/*
	 * 自制单据(non-Javadoc)
	 * 
	 * @see nc.ui.wds.w8004040208.AbstractMyEventHandler#onzzdj()
	 */
	protected void onzzdj(ButtonObject bo) throws Exception {
		if (isControl == 2 || isControl == 3) {
			lineNo = -1;
			getButtonManager().getButton(IBillButton.Line).setVisible(true);
			super.onBoAdd(bo);
			UIRefPane panel = (UIRefPane) getBillCardPanelWrapper()
					.getBillCardPanel().getBodyItem("ccunhuobianma")
					.getComponent();
			StringBuffer strWhere = new StringBuffer();
			// 根据总仓或者分仓进行过滤单品显示参照
			if (isStock) {
				if (null != pkList && pkList.size() > 0) {

					strWhere.append(" pk_invbasdoc in (");
					for (int i = 0; i < pkList.size(); i++) {
						if (i == pkList.size() - 1)
							strWhere.append("'" + pkList.get(i) + "')");
						else {
							strWhere.append("'" + pkList.get(i) + "'").append(
									" , ");
						}
					}
					panel.getRefModel().setWherePart(strWhere.toString());
				} else {
					panel.getRefModel().setWherePart(" 1=2");
				}
			} else {
				strWhere.append(" dr = 0 and ( def14 ='0' or def14 = '1') ");
				panel.getRefModel().setWherePart(strWhere.toString());
			}
			for (int i = 0; i < columnName.length; i++) {
				getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
						columnName[i]).setEnabled(true);
			}
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
					"ccunhuobianma").setEnabled(true);
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
					"nshouldoutassistnum").setEnabled(true);
			String pk_stock = CommonUnit.getStordocName(ClientEnvironment
					.getInstance().getUser().getPrimaryKey());
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("srl_pk")
					.setValue(pk_stock);
			setViewPro();
			// 设置单据号
			getBillCardPanelWrapper().getBillCardPanel().getHeadTailItem(
					"vbillcode").setValue(
					CommonUnit.getBillCode("4I", ClientEnvironment
							.getInstance().getCorporation().getPk_corp(), "",
							""));
			// getButtonManager().getButton(ISsButtun.zzdj).setEnabled(false);

			billType = "8";
			changeButton();
			isTypes = 3;
			isAdd = true;
		} else {
			myClientUI.showErrorMessage("操作失败,您无权操作");
			return;
		}
	}

	// 在自制单据状态下那些字段可以编辑
	String[] columnName = { "dbilldate", "srl_pkr", "vdiliveraddress", "vnote",
			"is_yundan" };

	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {

	}

	/**
	 * 根据参数改变托盘指定和自动取货按钮是否可用
	 * 
	 * @param type
	 */
	private void changeButton(boolean type) {
		getButtonManager().getButton(ISsButtun.zdqh).setEnabled(type);
		getButtonManager().getButton(ISsButtun.tpzd).setEnabled(type);
		getButtonManager().getButton(ISsButtun.zj).setEnabled(!type);

		myClientUI.updateButtons();
	}

	@Override
	protected void onBoCancel() throws Exception {
		getBillUI().initUI();
		getBufferData().clear();
		changeButton(false);
		if (isControl == 1) {
			getButtonManager().getButton(ISsButtun.zj).setEnabled(false);
			myClientUI.updateButtons();
		} else if (isControl == 2) {
			getButtonManager().getButton(ISsButtun.zj).setEnabled(true);
			myClientUI.updateButtons();
		}
	}

	/*
	 * 托盘指定(non-Javadoc)
	 * 
	 * @see nc.ui.wds.w8004040208.AbstractMyEventHandler#ontpzd()
	 */
	protected void ontpzd() throws Exception {

		// 获取所选择的行号
		int index = getBillCardPanelWrapper().getBillCardPanel().getBillTable()
				.getSelectedRow();
		if (index > -1) {
			// 获取表体多选择的VO对象
			TbOutgeneralBVO item = (TbOutgeneralBVO) getBillCardPanelWrapper()
					.getBillCardPanel().getBillModel().getBodyValueRowVO(index,
							TbOutgeneralBVO.class.getName());
			if (null != item) {
				String sNum = getRandomNum();
				if (null == item.getCinventoryid()
						|| "".equals(item.getCinventoryid())) {
					myClientUI.showErrorMessage("请选择单品");
					return;
				}
				if (null == item.getCsourcebillhid()
						|| "".equals(item.getCsourcebillhid())) {
					getBillCardPanelWrapper().getBillCardPanel()
							.setBodyValueAt(sNum, index, "csourcebillhid");
					item.setCsourcebillhid(sNum);
				}
				if (null == item.getCsourcebillbid()
						|| "".equals(item.getCsourcebillbid())) {
					getBillCardPanelWrapper().getBillCardPanel()
							.setBodyValueAt(sNum, index, "csourcebillbid");
					item.setCsourcebillbid(sNum);
				}
				Object pk_stordoc = getBillCardPanelWrapper()
						.getBillCardPanel().getHeadItem("srl_pk")
						.getValueObject();
				if (null == pk_stordoc || "".equals(pk_stordoc)) {
					myClientUI.showErrorMessage("操作失败,您无权操作");
					return;
				}
				TrayDisposeDlg tdpDlg = new TrayDisposeDlg("4209",
						ClientEnvironment.getInstance().getUser()
								.getPrimaryKey(),
						ClientEnvironment.getInstance().getCorporation()
								.getPrimaryKey(), "8004040208", myClientUI);
				tdpDlg.getReturnVOs(myClientUI, item, index, pk_stordoc
						.toString(), false);
				chaneColor();
			}
		} else {
			getBillUI().showWarningMessage("请选择表体行进行操作");
		}
	}

	/*
	 * 自动取货(non-Javadoc)
	 * 
	 * @see nc.ui.wds.w8004040208.AbstractMyEventHandler#onzdqh()
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
				}
				String msg = getIwBO().autoPickAction(ClientEnvironment
						.getInstance().getUser().getPrimaryKey(), generalbVO,
						pk_stordoc.toString(), "def16");
				if (null != msg) {
					myClientUI.showErrorMessage(msg);
					return;
				} else {
					tmpbVO = new TbOutgeneralBVO[generalbVO.length];
					for (int i = 0; i < generalbVO.length; i++) {
						getBillCardPanelWrapper().getBillCardPanel()
								.setBodyValueAt(null, i, "noutnum");
						getBillCardPanelWrapper().getBillCardPanel()
								.setBodyValueAt(null, i, "noutassistnum");
						getBillCardPanelWrapper().getBillCardPanel()
								.setBodyValueAt(null, i, "vbatchcode");
						// 调用接口查询缓存表中的实出主辅数量
						Object[] a = getIwBO().getNoutNum(generalbVO[i]
								.getCsourcebillbid(), generalbVO[i]
								.getCinventoryid().trim());
						if (null != a && a.length > 0) {
							// 实发数量
							if (null != a[0])
								getBillCardPanelWrapper().getBillCardPanel()
										.setBodyValueAt(a[0], i, "noutnum");
							// 实发辅数量
							if (null != a[1]) {
								getBillCardPanelWrapper().getBillCardPanel()
										.setBodyValueAt(a[1], i,
												"noutassistnum");
							}
							// 批次
							if (null != a[2]
									&& a[2].toString().trim().length() > 0) {
								getBillCardPanelWrapper().getBillCardPanel()
										.setBodyValueAt(a[2], i, "vbatchcode");
							}
							// 单价
							if (null != a[3]) {
								getBillCardPanelWrapper().getBillCardPanel()
										.setBodyValueAt(a[3], i, "nprice");
							}
							// 金额
							if (null != a[4]) {
								getBillCardPanelWrapper().getBillCardPanel()
										.setBodyValueAt(a[4], i, "nmny");
							}
							// 来源批次
							if (null != a[5]
									&& a[5].toString().trim().length() > 0) {
								getBillCardPanelWrapper().getBillCardPanel()
										.setBodyValueAt(a[5], i, "lvbatchcode");
							}
							// 货位ID
							getBillCardPanelWrapper()
									.getBillCardPanel()
									.setBodyValueAt(
											CommonUnit
													.getCargDocName(ClientEnvironment
															.getInstance()
															.getUser()
															.getPrimaryKey()),
											i, "cspaceid");
						}
					}
					chaneColor();
					this.onckmx();
				}
			} else {
				myClientUI.showErrorMessage("操作失败,您无权操作");
				return;
			}
		}

	}

	/*
	 * 查询明细(non-Javadoc)
	 * 
	 * @see nc.ui.wds.w8004040208.AbstractMyEventHandler#onckmx()
	 */
	protected void onckmx() throws Exception {
		TbOutgeneralBVO[] item = null;
		if (null != billType && billType.equals("8")) {
			// 获取当前页面中VO
			AggregatedValueObject myBillVO = getBillUI().getVOFromUI();
			item = (TbOutgeneralBVO[]) myBillVO.getChildrenVO();
			// 验证表体是否有数据
			if (null == item || item.length < 0) {
				myClientUI.showErrorMessage("操作失败,您无权操作");
				return;
			}
		} else {
			if (getBufferData().getVOBufferSize() <= 0) {

				myClientUI.showErrorMessage("操作失败,您无权操作");
				return;
			}
			item = (TbOutgeneralBVO[]) getBufferData().getCurrentVO()
					.getChildrenVO();
		}
		TrayDisposeDetailDlg tdpDlg = new TrayDisposeDetailDlg("4209",
				ClientEnvironment.getInstance().getUser().getPrimaryKey(),
				ClientEnvironment.getInstance().getCorporation()
						.getPrimaryKey(), "8004040208", myClientUI);

		tdpDlg.getReturnVOs(myClientUI, item);

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
	protected void onBoQuery() throws Exception {
		if (isControl == -1) {
			myClientUI.showErrorMessage("您没有权限操作");
			return;
		}
		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// 用户放弃了查询
		int tmp = strWhere.indexOf("tb_outgeneral_h.vbilltype");
		StringBuffer strtmp = new StringBuffer(strWhere.substring(tmp, strWhere
				.length()));
		StringBuffer a = new StringBuffer(strWhere.toString().toLowerCase()
				.substring(tmp, strWhere.length()));

		int tmpnum = a.indexOf("and");

		StringBuffer b = new StringBuffer(a.substring(0, tmpnum));
		for (int i = 28; i < b.length(); i++) {
			String stra = b.substring(i, i + 1);
			if (stra.equals("1"))
				b.replace(i, i + 1, "9");
			if (stra.equals("2"))
				b.replace(i, i + 1, "8");
		}
		strtmp.replace(0, tmpnum, b.toString());
		strWhere.replace(tmp, strWhere.length(), strtmp.toString());
		// strWhere.append(" and srl_pk = '" + pk_stock + "' ");
		SuperVO[] queryVos = null;
		if (isControl == 2 || isControl == 0) {
			// 获取开始索引
			tmp = strWhere.indexOf("tb_outgeneral_h.srl_pk");
			// 保管员后台给仓库赋值，就算前台有选择仓库也会默认当前登录人的。
			if (tmp > -1) {
				// 从开始索引到结束的所有字符串
				strtmp = new StringBuffer(strWhere.substring(tmp, strWhere
						.length()));
				strtmp.replace(strtmp.indexOf("'"), strtmp.indexOf("'") + 21,
						"'" + pk_stock);
				strWhere.replace(tmp, strWhere.length(), strtmp.toString());
			} else
				strWhere.append(" and srl_pk = '" +pk_stock + "' ");
			if (isControl == 0) // 发运科 查询全部本仓库
				queryVos = queryHeadVOs(strWhere.toString());
			else {
				if (isStock) // 总仓并且是保管员查询有本货位下单品的记录
					queryVos = CommonUnit.queryTbOutGeneral(pkList, strWhere);
				else
					// 分仓保管员查询全部不分单品
					queryVos = queryHeadVOs(strWhere.toString());
			}
		} else
			queryVos = queryHeadVOs(strWhere.toString());

		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);

		updateBuffer();

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

	/**
	 * 取样采购计划 把模板中的选中的VO 进行转换成取样采购出库的VO
	 * 
	 * @param vos
	 *            页面选中的聚合VO
	 * @return 聚合VO
	 */
	private MyBillVO changeReqQycgtoOutgeneral(AggregatedValueObject[] vos) {
		MyBillVO myBillVO = new MyBillVO();
		// hiddenList = new ArrayList();
//		int num = 0; // 为了计算数组的长度
		TbOutgeneralHVO generalHVO = new TbOutgeneralHVO();
		// 子表信息数组集合
		List<TbOutgeneralBVO> generalBList = new ArrayList<TbOutgeneralBVO>();

		TbQycgjhVO qycg = (TbQycgjhVO) vos[0].getParentVO();
		if (null != qycg.getQycgjh_ck() && !"".equals(qycg.getQycgjh_ck())) {
			generalHVO.setSrl_pk(qycg.getQycgjh_ck()); // 仓库
		}
		if (null != qycg.getQycgjh_ddh() && !"".equals(qycg.getQycgjh_ddh())) {
			generalHVO.setVsourcebillcode(qycg.getQycgjh_ddh()); // 来源单据号
		}
		if (null != qycg.getPk_qycgjh() && !"".equals(qycg.getPk_qycgjh())) {
			generalHVO.setCsourcebillhid(qycg.getPk_qycgjh()); // 来源单据表头主键
		}
		myBillVO.setParentVO(generalHVO);
		// 循环缓存中的子表信息
		if (null != cgqydlg.getQmxVO() && cgqydlg.getQmxVO().length > 0) {
			// 获取缓存 转换子表其他的信息
			for (int i = 0; i < cgqydlg.getQmxVO().length; i++) {
				String invpk = null;
				if (null != pkList && pkList.size() > 0) {
					TbQycgjh2VO qmxvo = cgqydlg.getQmxVO()[i];
					if (qycg.getPk_qycgjh().equals(qmxvo.getPk_qycgjh())) {
						TbOutgeneralBVO generalBVO = new TbOutgeneralBVO();
						if (null != qycg.getPk_qycgjh()
								&& !"".equals(qycg.getPk_qycgjh())) {
							generalBVO.setCsourcebillhid(qycg.getPk_qycgjh()); // 来源单据表头主键
							generalBVO.setCfirstbillhid(qycg.getPk_qycgjh()); // 源头单据表头主键
						}
						if (null != qycg.getQycgjh_ddh()
								&& !"".equals(qycg.getQycgjh_ddh())) {
							generalBVO.setVsourcebillcode(qycg.getQycgjh_ddh()); // 来源单据号
						}
						if (null != qmxvo.getPk_qycgjh()
								&& !"".equals(qmxvo.getPk_qycgjh())) {
							generalBVO.setCsourcebillbid(qmxvo.getPk_qycgjh2()); // 来源单据表体主键
							generalBVO.setCfirstbillbid(qmxvo.getPk_qycgjh2()); // 源头单据表体主键
						}
						if (null != qmxvo.getQycgjh2_fsl()
								&& !"".equals(qmxvo.getQycgjh2_fsl())) {
							generalBVO.setNshouldoutassistnum(qmxvo
									.getQycgjh2_fsl()); // 应发辅数量
						}
						if (null != qmxvo.getQycgjh2_zsl()
								&& !"".equals(qmxvo.getQycgjh2_zsl())) {
							generalBVO.setNshouldoutnum(qmxvo.getQycgjh2_zsl()); // 应发主数量
						}
						if (null != qmxvo.getQycgjh2_chzj()
								&& !"".equals(qmxvo.getQycgjh2_chzj())) {
							generalBVO.setCinventoryid(qmxvo.getQycgjh2_chzj()); // 存货主键
						}
						// 行号
						generalBVO.setIsoper(new UFBoolean("Y"));
						generalBVO.setCrowno(qmxvo.getQycgjh2_hh());
						generalBList.add(generalBVO);
						// for (int j = 0; j < pkList.size(); j++) {
						// invpk = pkList.get(j).toString();
						// if (invpk.equals(qmxvo.getQycgjh2_chzj())) {
						// // 给需要显示的表体数据中是否操作字段赋值
						// generalBVO.setIsoper(new UFBoolean("Y"));
						// generalBList.add(generalBVO);
						// break;
						// }
						// }
						// hiddenList.add(generalBVO); // 设置隐藏集合列表
					}
				}
			}
			TbOutgeneralBVO[] generalBVO = new TbOutgeneralBVO[generalBList
					.size()];
			generalBVO = generalBList.toArray(generalBVO);
			myBillVO.setChildrenVO(generalBVO);
		}

		return myBillVO;
	}

	/**
	 * 把模板中的选中的VO 进行转换成转库出库的VO
	 * 
	 * @param vos
	 *            页面选中的聚合VO
	 * @return 聚合VO
	 */
	private MyBillVO changeReqFydtoOutgeneral(AggregatedValueObject[] vos) {
		// hiddenList = new ArrayList();
		MyBillVO myBillVO = new MyBillVO();
//		int num = 0; // 为了计算数组的长度
		TbOutgeneralHVO generalHVO = new TbOutgeneralHVO();
		// 子表信息数组集合
		List<TbOutgeneralBVO> generalBList = new ArrayList<TbOutgeneralBVO>();

		SendorderVO fydnew = (SendorderVO) vos[0].getParentVO();
		if (null != fydnew.getPk_outwhouse() && !"".equals(fydnew.getPk_outwhouse())) {
			generalHVO.setSrl_pk(fydnew.getPk_outwhouse()); // 出库仓库
		}
		if (null != fydnew.getPk_inwhouse() && !"".equals(fydnew.getPk_inwhouse())) {
			generalHVO.setSrl_pkr(fydnew.getPk_inwhouse()); // 入库仓库
		}
		if (null != fydnew.getVinaddress() && !"".equals(fydnew.getVinaddress())) {
			generalHVO.setVdiliveraddress(fydnew.getVinaddress()); // 收货地址
		}
		if (null != fydnew.getVmemo() && !"".equals(fydnew.getVmemo())) {
			generalHVO.setVnote(fydnew.getVmemo()); // 备注
		}
		if (null != fydnew.getDapprovedate()
				&& !"".equals(fydnew.getDapprovedate())) {
			generalHVO.setDauditdate(fydnew.getDapprovedate()); // 审核日期
		}
		if (null != fydnew.getVbillno() && !"".equals(fydnew.getVbillno())) {
			generalHVO.setVsourcebillcode(fydnew.getVbillno()); // 来源单据号
		}
		if (null != fydnew.getPk_sendorder() && !"".equals(fydnew.getPk_sendorder())) {
			generalHVO.setCsourcebillhid(fydnew.getPk_sendorder()); // 来源单据表头主键
		}

		myBillVO.setParentVO(generalHVO);
		// 循环缓存中的子表信息
		if (null != fydnewdlg.getFydmxVO() && fydnewdlg.getFydmxVO().length > 0) {

			// 获取缓存 转换子表其他的信息
			for (int i = 0; i < fydnewdlg.getFydmxVO().length; i++) {
//				String invpk = null;
				if (null != pkList && pkList.size() > 0) {
					SendorderBVO fydmxnewvo = fydnewdlg.getFydmxVO()[i];
					if (fydnew.getPk_sendorder().equals(fydmxnewvo.getPk_sendorder())) {

						TbOutgeneralBVO generalBVO = new TbOutgeneralBVO();
						if (null != fydnew.getPk_sendorder()
								&& !"".equals(fydnew.getPk_sendorder())) {
							generalBVO.setCsourcebillhid(fydnew.getPk_sendorder()); // 来源单据表头主键
							generalBVO.setCfirstbillhid(fydmxnewvo.getPk_sendorder()); // 源头单据表头主键
						}
						if (null != fydnew.getVbillno()
								&& !"".equals(fydnew.getVbillno())) {
							generalBVO.setVsourcebillcode(fydnew.getVbillno()); // 来源单据号
						}
						if (null != fydnew.getPk_billtype()
								&& !"".equals(fydnew.getPk_billtype())) {
							generalBVO.setCsourcetype(fydnew.getPk_billtype()
									.toString()); // 来源单据类型
						}
						if (null != fydmxnewvo.getPk_sendorder_b()
								&& !"".equals(fydmxnewvo.getPk_sendorder_b())) {
							generalBVO
									.setCsourcebillbid(fydmxnewvo.getPk_sendorder_b()); // 来源单据表体主键
							generalBVO.setCfirstbillbid(fydmxnewvo.getPk_sendorder_b()); // 源头单据表体主键
						}
						if (null != fydmxnewvo.getNdealnum()
								&& !"".equals(fydmxnewvo.getNdealnum())) {
							generalBVO.setNshouldoutnum(fydmxnewvo
									.getNdealnum()); // 安排数量
						}
						if (null != fydmxnewvo.getNassdealnum()
								&& !"".equals(fydmxnewvo.getNassdealnum())) {
							generalBVO.setNshouldoutassistnum(fydmxnewvo
									.getNassdealnum()); // 安排辅数量
						}
						if (null != fydmxnewvo.getPk_invbasdoc()
								&& !"".equals(fydmxnewvo.getPk_invbasdoc())) {
							generalBVO.setCinventoryid(fydmxnewvo
									.getPk_invbasdoc()); // 存货主键
						}
						generalBVO.setIsoper(new UFBoolean("Y"));
						generalBVO.setCrowno(""+(i+1)*10);
						// 行号
						// generalBVO.setCrowno(getRowNo() + "");
						generalBList.add(generalBVO);
						// for (int j = 0; j < pkList.size(); j++) {
						// invpk = pkList.get(j).toString();
						// if (invpk.equals(fydmxnewvo.getPk_invbasdoc())) {
						// // 给需要显示的表体数据中是否操作字段赋值
						// generalBVO.setIsoper(new UFBoolean("Y"));
						// generalBList.add(generalBVO);
						// break;
						// }
						// }
						// hiddenList.add(generalBVO); // 设置隐藏集合列表
					}
				}
			}
			TbOutgeneralBVO[] generalBVO = new TbOutgeneralBVO[generalBList
					.size()];
			generalBVO = generalBList.toArray(generalBVO);
			myBillVO.setChildrenVO(generalBVO);
		}

		return myBillVO;
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
		TbOutgeneralHVO tmphvo = (TbOutgeneralHVO) getBufferData()
				.getCurrentVO().getParentVO();
		// 本地出库表 表体
		TbOutgeneralBVO[] tmpbvo = null;
		// 出库聚合VO
		GeneralBillVO gBillVO = new GeneralBillVO();
		// 出库表头VO
		GeneralBillHeaderVO generalhvo = null;
		// 出库子表集合
		List<GeneralBillItemVO> generalBVOList = new ArrayList<GeneralBillItemVO>();
		// 出库子表VO数组
		GeneralBillItemVO[] generalBVO = null;
		String sWhere = " dr = 0 and general_pk = '" + tmphvo.getGeneral_pk()
				+ "'";
		ArrayList list = (ArrayList) getQueryBO().retrieveByClause(
				TbOutgeneralHVO.class, sWhere);
		if (null != list && list.size() > 0) {
			TbOutgeneralHVO outhvo = (TbOutgeneralHVO) list.get(0);
			generalhvo = new GeneralBillHeaderVO();

			if (null != outhvo) {
				// 给出库单表头赋值

				generalhvo.setPk_corp(outhvo.getComp());// 公司主键
				generalhvo.setAttributeValue("cothercorpid", outhvo.getComp());// 对方公司
				// generalhvo.setCbiztypeid(outhvo.getCbiztype());// 业务类型
				generalhvo.setCbilltypecode("4I");// 库存单据类型编码
				generalhvo.setVbillcode(outhvo.getVbillcode());// 单据号
				generalhvo.setDbilldate(outhvo.getDbilldate());// 单据日期
				generalhvo.setCwarehouseid(outhvo.getSrl_pk());// 仓库ID
				generalhvo.setAttributeValue("cotherwhid", outhvo.getSrl_pkr());// 其他仓库
				generalhvo.setCdispatcherid(outhvo.getCdispatcherid());// 收发类型outhvo.getCdispatcherid()
				generalhvo.setCdptid(outhvo.getCdptid());// 部门ID1021B110000000000BN9
				generalhvo.setCwhsmanagerid(outhvo.getCwhsmanagerid());// 库管员ID
				generalhvo.setCoperatorid(ClientEnvironment.getInstance()
						.getUser().getPrimaryKey());// 制单人
				generalhvo.setAttributeValue("coperatoridnow",
						ClientEnvironment.getInstance().getUser()
								.getPrimaryKey());// 操作人
				generalhvo.setVdiliveraddress(outhvo.getVdiliveraddress());// 发运地址
				generalhvo.setCbizid(outhvo.getCbizid());// 业务员ID
				generalhvo.setVnote(outhvo.getVnote());// 备注
				generalhvo.setFbillflag(2);// 单据状态
				generalhvo.setAttributeValue("clastmodiid", outhvo
						.getClastmodiid());// 最后修改人
				generalhvo.setAttributeValue("tlastmoditime", outhvo
						.getTlastmoditime());// 最后修改时间
				generalhvo
						.setAttributeValue("tmaketime", outhvo.getTmaketime());// 制单时间
				sWhere = " dr = 0 and general_pk = '" + tmphvo.getGeneral_pk()
						+ "'";
				list = (ArrayList) getQueryBO().retrieveByClause(TbOutgeneralBVO.class,
						sWhere);
				if (null != list && list.size() > 0) {
					tmpbvo = new TbOutgeneralBVO[list.size()];
					tmpbvo = (TbOutgeneralBVO[]) list.toArray(tmpbvo);
					// 给表体赋值
					for (int i = 0; i < tmpbvo.length; i++) {
						// 单据表体附表--货位
						LocatorVO locatorvo = new LocatorVO();
						locatorvo.setPk_corp(tmpbvo[i].getComp());
						boolean isBatch = false;
						GeneralBillItemVO generalb = new GeneralBillItemVO();
						generalb.setAttributeValue("pk_corp", outhvo.getComp());// 公司

						generalb.setCinvbasid(tmpbvo[i].getCinventoryid());// 存货ID
						generalb.setVbatchcode(tmpbvo[i].getLvbatchcode());// 批次号
						// 查询生产日期和失效日期
						String sql = "select dproducedate ,dvalidate  from scm_batchcode where pk_invbasdoc='"
								+ tmpbvo[i].getCinventoryid()
								+ "' and vbatchcode='"
								+ tmpbvo[i].getLvbatchcode() + "' and dr=0";
						ArrayList batchList = (ArrayList) getQueryBO().executeQuery(
								sql, new ArrayListProcessor());
						if (null != batchList && batchList.size() > 0) {
							Object[] batch = (Object[]) batchList.get(0);
							// 生产日期
							if (null != batch[0] && !"".equals(batch[0]))
								generalb
										.setScrq(new UFDate(batch[0].toString()));
							// 失效日期
							if (null != batch[0] && !"".equals(batch[0]))
								generalb.setDvalidate(new UFDate(batch[1]
										.toString()));
							isBatch = true;
						}
						String pk_invmandoc = "select pk_invmandoc from bd_invmandoc  where pk_invbasdoc='"
								+ tmpbvo[i].getCinventoryid().trim()
								+ "' and pk_corp='"
								+ outhvo.getComp()
								+ "' and dr=0 ";
						ArrayList tmpList = (ArrayList) getQueryBO().executeQuery(
								pk_invmandoc, new ArrayListProcessor());
						if (null != tmpList && tmpList.size() > 0) {
							Object[] a = (Object[]) tmpList.get(0);
							generalb.setCinventoryid(WDSTools
									.getString_TrimZeroLenAsNull(a[0]));// 存货基本ID
						} else {
							isBatch = false;
						}
						generalb.setDbizdate(outhvo.getDbilldate());// 业务日期
						generalb.setNshouldoutnum(tmpbvo[i].getNshouldoutnum());// 应出数量
						generalb.setNshouldoutassistnum(tmpbvo[i]
								.getNshouldoutassistnum());// 应出辅数量
						generalb.setNoutnum(tmpbvo[i].getNoutnum());// 实出数量
						locatorvo.setNoutspacenum(tmpbvo[i].getNoutnum());
						generalb.setNoutassistnum(tmpbvo[i].getNoutassistnum());// 实出辅数量
						locatorvo.setNoutspaceassistnum(tmpbvo[i]
								.getNoutassistnum());
						locatorvo.setCspaceid(tmpbvo[i].getCspaceid());// 货位ID
						generalb.setCastunitid(tmpbvo[i].getCastunitid());// 辅计量单位ID
						generalb.setNprice(tmpbvo[i].getNprice());// 单价
						generalb.setNmny(tmpbvo[i].getNmny());// 金额
						generalb.setCsourcebillhid(tmpbvo[i]
								.getCsourcebillhid());// 来源单据表头序列号
						generalb.setCfirstbillhid(tmpbvo[i].getCfirstbillhid());// 源头单据表头ID
						generalb.setCfreezeid(tmpbvo[i].getCsourcebillhid());// 锁定来源
						generalb.setCsourcebillbid(tmpbvo[i]
								.getCsourcebillbid());// 来源单据表体序列号
						generalb.setCfirstbillbid(tmpbvo[i].getCfirstbillbid());// 源头单据表体ID
						// generalb.setCsourcetype();// 来源单据类型
						// generalb.setCfirsttype();// 源头单据类型
						generalb.setVsourcebillcode(tmpbvo[i]
								.getVsourcebillcode());// 来源单据号
						generalb.setVfirstbillcode(tmpbvo[i]
								.getVsourcebillcode());// 源头单据号
						generalb.setVsourcerowno(tmpbvo[i].getCrowno());// 来源单据行号
						generalb.setVfirstrowno(tmpbvo[i].getCrowno());// 源头单据行号
						generalb.setFlargess(tmpbvo[i].getFlargess());// 是否赠品
						generalb.setDfirstbilldate(tmpbvo[i]
								.getDfirstbilldate());// 源头单据制单日期
						generalb.setCrowno(tmpbvo[i].getCrowno());// 行号
						generalb.setHsl(tmpbvo[i].getHsl());// 换算率
						LocatorVO[] locatorVO = new LocatorVO[] { locatorvo };
						generalb.setLocator(locatorVO);
						if (isBatch)
							// 给出库子表集合添加对象
							generalBVOList.add(generalb);
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

//	private int getRowNo() {
//		if (lineNo == -1)
//			lineNo = 10;
//		else
//			lineNo = lineNo + 10;
//		return lineNo;
//	}

	/**
	 * 生成随机数20位 年月日小时分秒毫秒加上4位随机数
	 * 
	 * @return
	 */
	private String getRandomNum() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmssms");
		String tmp = format.format(new Date());
		tmp = tmp + Math.round((Math.random() * 1000000));
		tmp = tmp.substring(0, 20);
		return tmp;
	}

}