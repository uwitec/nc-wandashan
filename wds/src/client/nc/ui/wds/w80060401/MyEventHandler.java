package nc.ui.wds.w80060401;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.wds.w80060401.Iw80060401;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.BusinessAction;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.businessaction.IBusinessController;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.wds.w8000.CommonUnit;
import nc.ui.wds.w8000.W80060401Action;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.bill.BillRendererVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.wds.w80060401.MyBillVO;
import nc.vo.wds.w80060401.TbShipentryBVO;
import nc.vo.wds.w80060401.TbShipentryVO;

/**
 * 
 *发运录入 
 *
 * @author author xzs
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	protected boolean isAdd = false;
	MyClientUI myClientUI = null;
	Iw80060401 iw = null;
	// 数据库查询对象
	IUAPQueryBS iuap = (IUAPQueryBS) NCLocator.getInstance().lookup(
			IUAPQueryBS.class.getName());
	private boolean isControl = false; // 是否有权限操作当前单据

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
		myClientUI = (MyClientUI) billUI;
		String isType;
		try {
			isType = CommonUnit.getUserType(ClientEnvironment.getInstance()
					.getUser().getPrimaryKey());
			if (null != isType && isType.equals("2")) {
				isControl = true;
			}
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected IBusinessController createBusinessAction() {
		// TODO Auto-generated method stub
		switch (getUIController().getBusinessActionType()) {
		case IBusinessActionType.PLATFORM:
			return new BusinessAction(getBillUI());
		case IBusinessActionType.BD:
			return new W80060401Action(getBillUI());
		default:
			return new BusinessAction(getBillUI());
		}
	}

	@Override
	protected void onBoQuery() throws Exception {
		// TODO Auto-generated method stub
		super.onBoQuery();
		showZeroLikeNull(false);
	}

	// 验证方法
	private boolean validate(AggregatedValueObject billVO) throws Exception {
		Object srl_pk = getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("srl_pk").getValueObject();
		// 仓库是否为空
		if (null == srl_pk || "".equals(srl_pk)) {
			myClientUI.showWarningMessage("请选择仓库");
			return false;
		}
		// 计划类别
		Object se_type = getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("se_type").getValueObject();
		if (null == se_type || "".equals(se_type)) {
			myClientUI.showWarningMessage("请选择计划类型");
			return false;
		}
		// 制单日期
		Object doperatordate = getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("doperatordate").getValueObject();
		if (null == doperatordate || "".equals(doperatordate)) {
			myClientUI.showWarningMessage("请选择制单日期");
			return false;
		}
		// 是否选择单品
		TbShipentryBVO[] shbVO = (TbShipentryBVO[]) billVO.getChildrenVO();
		boolean isPlannum = false;
		// 循环子表数组
		if (null != shbVO && shbVO.length > 0) {
			for (int i = 0; i < shbVO.length; i++) {
				TbShipentryBVO shbvo = shbVO[i];
				// 判断子表中是否有输入计划数的单品
				if (null != shbvo.getSeb_plannum()
						&& !"".equals(shbvo.getSeb_plannum())) {
					isPlannum = true;
					break;
				}
			}
			if (!isPlannum) {
				myClientUI.showWarningMessage("请选择一项单品输入计划数");
				return false;
			}
		}
		// 设置where条件
		Date date = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		// 日期转换
		try {
			date = format.parse(doperatordate.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			myClientUI.showWarningMessage("输入日期不合法,请重新输入");
			return false;
		}

		if (date != null) {
			// 转换开始时间和结束时间
			String[] dateformat = this.dateFormat(date);

			// 转换
			String begindate = dateformat[0];
			String enddate = dateformat[1];
			String sWhere = "";
			Object se_pk = getBillCardPanelWrapper().getBillCardPanel()
					.getHeadItem("se_pk").getValueObject();
			// 判断操作类别 修改
			if (null != se_pk && !"".equals(se_pk)) {
				sWhere = " dr = 0 and se_type = 0 and doperatordate between '"
						+ begindate + "' and '" + enddate + "' and se_pk <> '"
						+ se_pk + "' and srl_pk = '" + srl_pk + "'";
			} else { // 新增
				sWhere = " dr = 0 and se_type = 0 and doperatordate between '"
						+ begindate + "' and '" + enddate + "' and srl_pk = '"
						+ srl_pk + "'";
			}
			ArrayList list = (ArrayList) iuap.retrieveByClause(
					TbShipentryVO.class, sWhere);

			// 判断输入的计划类别
			if (Integer.parseInt(se_type.toString()) == 0) {
				// 判断 如果当前输入的计划类别是月计划 就不能再录入月计划了
				if (null != list && list.size() >= 1) {
					myClientUI.showWarningMessage("该月已有月计划,请修改录入类型");
					return false;
				} else {
					List pman = new ArrayList();
					pman.add(getBillUI().getUserObject());
					pman.add(begindate);
					pman.add(enddate);
					pman.add(list);
					billVO = getBusinessAction().save(billVO,
							getUIController().getBillType(),
							_getDate().toString(), pman, billVO);
					return true;
				}
			} else {
				// 如果计划类别为追加计划，就必须先有月计划
				if (null != list && list.size() >= 1) {
					List pman = new ArrayList();
					pman.add(getBillUI().getUserObject());
					pman.add(begindate);
					pman.add(enddate);
					pman.add(list);
					billVO = getBusinessAction().save(billVO,
							getUIController().getBillType(),
							_getDate().toString(), pman, billVO);
					// iw.insertShipertryVO(billVO, begindate, enddate, list);
					return true;
				} else {
					myClientUI.showWarningMessage("该月还没有月计划,请先录入月计划");
					return false;
				}
			}
		}
		return false;
	}

	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
		AggregatedValueObject billVO = getBillUI().getVOFromUI();
		setTSFormBufferToVO(billVO);
		AggregatedValueObject checkVO = getBillUI().getVOFromUI();
		setTSFormBufferToVO(checkVO);
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
			if (getBillUI().isSaveAndCommitTogether())
				billVO = getBusinessAction().saveAndCommit(billVO,
						getUIController().getBillType(), _getDate().toString(),
						getBillUI().getUserObject(), checkVO);
			else {

				// write to database
				Object srl_pk = getBillCardPanelWrapper().getBillCardPanel()
						.getHeadItem("srl_pk").getValueObject();
				// 仓库是否为空
				if (null == srl_pk || "".equals(srl_pk)) {
					myClientUI.showWarningMessage("请选择仓库");
					return;
				}
				// 计划类别
				Object se_type = getBillCardPanelWrapper().getBillCardPanel()
						.getHeadItem("se_type").getValueObject();
				if (null == se_type || "".equals(se_type)) {
					myClientUI.showWarningMessage("请选择计划类型");
					return;
				}
				// 制单日期
				Object doperatordate = getBillCardPanelWrapper()
						.getBillCardPanel().getHeadItem("doperatordate")
						.getValueObject();
				if (null == doperatordate || "".equals(doperatordate)) {
					myClientUI.showWarningMessage("请选择制单日期");
					return;
				}
				// 是否选择单品
				TbShipentryBVO[] shbVO = (TbShipentryBVO[]) billVO
						.getChildrenVO();
				boolean isPlannum = false;
				// 循环子表数组
				if (null != shbVO && shbVO.length > 0) {
					for (int i = 0; i < shbVO.length; i++) {
						TbShipentryBVO shbvo = shbVO[i];
						// 判断子表中是否有输入计划数的单品
						if (null != shbvo.getSeb_plannum()
								&& !"".equals(shbvo.getSeb_plannum())) {
							isPlannum = true;
							break;
						}
					}
					if (!isPlannum) {
						myClientUI.showWarningMessage("请选择一项单品输入计划数");
						return;
					}
				}

				// 设置where条件
				Date date = null;
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

				// 日期转换
				try {
					date = format.parse(doperatordate.toString());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					myClientUI.showWarningMessage("输入日期不合法,请重新输入");
					return;
				}

				if (date != null) {
					// 转换开始时间和结束时间
					String[] dateformat = this.dateFormat(date);

					// 转换
					String begindate = dateformat[0];
					String enddate = dateformat[1];
					String sWhere = "";
					Object se_pk = getBillCardPanelWrapper().getBillCardPanel()
							.getHeadItem("se_pk").getValueObject();
					// 判断操作类别 修改
					if (null != se_pk && !"".equals(se_pk)) {
						sWhere = " dr = 0 and se_type = 0 and doperatordate between '"
								+ begindate
								+ "' and '"
								+ enddate
								+ "' and se_pk <> '"
								+ se_pk
								+ "' and srl_pk = '" + srl_pk + "'";
					} else { // 新增
						sWhere = " dr = 0 and se_type = 0 and doperatordate between '"
								+ begindate
								+ "' and '"
								+ enddate
								+ "' and srl_pk = '" + srl_pk + "'";
					}
					ArrayList list = (ArrayList) iuap.retrieveByClause(
							TbShipentryVO.class, sWhere);

					// 判断输入的计划类别
					if (Integer.parseInt(se_type.toString()) == 0) {
						// 判断 如果当前输入的计划类别是月计划 就不能再录入月计划了
						if (null != list && list.size() >= 1) {
							myClientUI.showWarningMessage("该月已有月计划,请修改录入类型");
							return;
						} else {
							List pman = new ArrayList();
							pman.add(getBillUI().getUserObject());
							pman.add(begindate);
							pman.add(enddate);
							pman.add(list);
							billVO = getBusinessAction().save(billVO,
									getUIController().getBillType(),
									_getDate().toString(), pman, billVO);
						}
					} else {
						// 如果计划类别为追加计划，就必须先有月计划
						if (null != list && list.size() >= 1) {
							List pman = new ArrayList();
							pman.add(getBillUI().getUserObject());
							pman.add(begindate);
							pman.add(enddate);
							pman.add(list);
							billVO = getBusinessAction().save(billVO,
									getUIController().getBillType(),
									_getDate().toString(), pman, billVO);
							// iw.insertShipertryVO(billVO, begindate, enddate,
							// list);
						} else {
							myClientUI.showWarningMessage("该月还没有月计划,请先录入月计划");
							return;
						}
					}
				}
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
	}

	/**
	 * 进行时间转换 开始时间和结束时间
	 * 
	 * @param date
	 *            时间对象
	 * @return 数组开始时间和结束时间
	 * @throws ParseException
	 */
	private String[] dateFormat(Date date) throws ParseException {
		String[] dateformat = new String[2];
		SimpleDateFormat format = new SimpleDateFormat("dd");
		int day = Integer.parseInt(format.format(date)); // 当前天
		format = new SimpleDateFormat("MM");
		int month = Integer.parseInt(format.format(date)); // 当前月
		format = new SimpleDateFormat("yyyy");
		int year = Integer.parseInt(format.format(date)); // 当前年
		// 判断 如果当前天数小于21
		if (day < 21) {
			// 月份减一
			month = month - 1;
		}
		format = new SimpleDateFormat("yyyy-MM-dd");
		// 拼装开始时间
		if (month == 0) {
			month = 12;
			year = year - 1;
		}
		String begindate = year + "-" + month + "-21";
		// 结束时间
		if (month + 1 == 13) {
			month = 1;
			year = year + 1;
		} else {
			month = month + 1;
		}
		String enddate = year + "-" + month + "-20";
		format = new SimpleDateFormat("yyyy-MM-dd");
		dateformat[0] = format.format(format.parse(begindate));
		dateformat[1] = format.format(format.parse(enddate));
		return dateformat;
	}

	@Override
	protected void onBoDelete() throws Exception {
		// TODO Auto-generated method stub
		if (isControl) {
			// 界面没有数据或者有数据但是没有选中任何行
			if (getBufferData().getCurrentVO() == null)
				return;

			if (MessageDialog.showOkCancelDlg(getBillUI(), nc.ui.ml.NCLangRes
					.getInstance().getStrByID("uifactory",
							"UPPuifactory-000064")/* @res "档案删除" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000065")/* @res "是否确认删除该基本档案?" */
					, MessageDialog.ID_CANCEL) != UIDialog.ID_OK)
				return;

			AggregatedValueObject modelVo = getBufferData().getCurrentVO();

			AggregatedValueObject item = getBillUI().getBufferData()
					.getCurrentVO();

			String pk = ((TbShipentryVO) item.getParentVO()).getSe_pk();
			String sql = "select count(se_pk) from tb_fydnew where dr = 0 and se_pk = '"
					+ pk + "'";

			ArrayList list = (ArrayList) iuap.executeQuery(sql,
					new ArrayListProcessor());
			Object[] results = (Object[]) list.get(0);
			if (null != results[0] && !"".equals(results[0])
					&& Integer.parseInt(results[0].toString()) > 0) {
				myClientUI.showErrorMessage("删除失败!该单据已进行拆分,请先删除拆分单据");
				return;
			}
			boolean isupdate = false;
			ArrayList<TbShipentryBVO> shibList = new ArrayList<TbShipentryBVO>();
			// 录入类型
			int type = ((TbShipentryVO) item.getParentVO()).getSe_type();
			// 到货站
			String srl_pk = ((TbShipentryVO) item.getParentVO()).getSrl_pk();
			// 录入日期
			UFDate datets = ((TbShipentryVO) item.getParentVO())
					.getDoperatordate();
			String[] dateformat = this.dateFormat(datets.toDate());
			// 如果是月计划 进行查询看有没有追加计划
			if (type == 0) {
				sql = "select count(se_pk) from tb_shipentry where dr = 0 and se_type = 1 and  doperatordate between '"
						+ dateformat[0]
						+ "' and '"
						+ dateformat[1]
						+ "' and srl_pk = '" + srl_pk + "'";
				list = (ArrayList) iuap.executeQuery(sql,
						new ArrayListProcessor());
				results = (Object[]) list.get(0);
				if (null != results[0] && !"".equals(results[0])
						&& Integer.parseInt(results[0].toString()) > 0) {
					myClientUI.showErrorMessage("删除失败!该月有追加计划,请先删除追加计划");
					return;
				}
			} else if (type == 1) { // 追加计划 ，判断是否有过一次拆分，有过一次都不可用删除
				String strWhere = " dr = 0  and se_type = 0 and "
						+ " doperatordate between '" + dateformat[0]
						+ "' and '" + dateformat[1] + "'";
				// 查询出当前追加计划的月计划
				list = (ArrayList) iuap.retrieveByClause(TbShipentryVO.class,
						strWhere);
				if (null != list && list.size() > 0) {
					// 根据查询出来的月计划主键查询是否有过拆分，有过就不可以删除了。因为现在删除的是追加计划，后台里面的数据对不上，防止差分后在删除
					String se_pk = ((TbShipentryVO) list.get(0)).getSe_pk();
					sql = "select count(se_pk) from tb_fydnew where dr = 0 and se_pk = '"
							+ se_pk + "'";
					list = (ArrayList) iuap.executeQuery(sql,
							new ArrayListProcessor());
					results = (Object[]) list.get(0);
					if (null != results[0] && !"".equals(results[0])
							&& Integer.parseInt(results[0].toString()) > 0) {
						myClientUI.showErrorMessage("删除失败!该单据已进行拆分,请先删除拆分单据");
						return;
					}
					strWhere = " dr = 0 and se_pk = '" + se_pk + "'";
					list = (ArrayList) iuap.retrieveByClause(
							TbShipentryBVO.class, strWhere);

					if (null != list && list.size() > 0) {
						TbShipentryBVO[] shibVO = (TbShipentryBVO[]) item
								.getChildrenVO();
						if (null != shibVO && shibVO.length > 0) {
							for (int i = 0; i < shibVO.length; i++) {
								for (int j = 0; j < list.size(); j++) {
									TbShipentryBVO shib = (TbShipentryBVO) list
											.get(j);
									// 如果两个单品相等
									if (shibVO[i].getPk_invbasdoc().equals(
											shib.getPk_invbasdoc())) {
										if (null != shibVO[i].getSeb_plannum()
												&& !"".equals(shibVO[i]
														.getSeb_plannum())) {
											// 设置剩余数 = 当前剩余数 - 被选中删除的计划数
											shib
													.setSeb_surplus(new UFDouble(
															shib
																	.getSeb_surplus()
																	.toDouble()
																	- shibVO[i]
																			.getSeb_plannum()
																			.toDouble()));
											shib.setDr(1); // 设置删除
											shibList.add(shib);// 给最后需要更新的集合赋值
											isupdate = true;
										}
									}
								}
							}
						}
					}
				}
			}
			// // 调用接口删除方法
			// iw.deleteShipertryVO(item, shibList, isupdate);
			List pman = new ArrayList();
			pman.add(getBillUI().getUserObject());
			pman.add(shibList);
			pman.add(isupdate);
			getBusinessAction().delete(modelVo,
					getUIController().getBillType(),
					getBillUI()._getDate().toString(), pman);

			if (PfUtilClient.isSuccess()) {
				// 清除界面数据
				getBillUI().removeListHeadData(getBufferData().getCurrentRow());
				if (getUIController() instanceof ISingleController) {
					ISingleController sctl = (ISingleController) getUIController();
					if (!sctl.isSingleDetail())
						getBufferData().removeCurrentRow();
				} else {
					getBufferData().removeCurrentRow();
				}

			}
			if (getBufferData().getVOBufferSize() == 0)
				getBillUI().setBillOperate(IBillOperate.OP_INIT);
			else
				getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
			getBufferData().setCurrentRow(getBufferData().getCurrentRow());
		} else {
			getBillUI().showErrorMessage("操作失败,当前登录者没有进行人员绑定");
			return;
		}
	}

	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		// TODO Auto-generated method stub
		if (isControl) {
			if (CommonUnit.getSotckIsTotal(CommonUnit
					.getStordocName(ClientEnvironment.getInstance().getUser()
							.getPrimaryKey()))) {
				super.onBoAdd(bo);
				getBufferData().clear();
				Object[] values = new Object[] { ClientEnvironment
						.getInstance().getUser().getPrimaryKey() };
				getBillCardPanelWrapper().getBillCardPanel().getHeadTailItem(
						"voperatorid").setValue(values[0]);
				getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
						"doperatordate").setValue(_getDate());

				Iw80060401 iw = (Iw80060401) NCLocator.getInstance().lookup(
						Iw80060401.class.getName()); // 调用接口
				TbShipentryBVO[] shvo = null;// 表体数据数组
				try {
					shvo = iw.queryShipentryBVO(""); // 通过接口获取数据
					if (shvo != null) {
						MyBillVO myBillVO = (MyBillVO) this
								// 获取界面中的UI
								.getBillCardPanelWrapper().getBillCardPanel()
								.getBillValueChangeVO(MyBillVO.class.getName(),
										TbShipentryVO.class.getName(),
										TbShipentryBVO.class.getName());
						// myBillVO.setParentVO(new TbShipentryVO());
						myBillVO.setChildrenVO(shvo); // 为表体赋值 传的表体数据
						getBufferData().addVOToBuffer(myBillVO); // 为界面UI添加数据
						updateBuffer(); // 更新

						getBillUI().setBillOperate( // 因为点击自定义按钮后就会切到正常按钮模式
								// 所以设置状态
								nc.ui.trade.base.IBillOperate.OP_EDIT);
						showZeroLikeNull(false);
					}

				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				isAdd = true;
			} else {
				getBillUI().showErrorMessage("操作失败,当前登录者没有权限进行操作");
				return;
			}
		} else {
			getBillUI().showErrorMessage("操作失败,当前登录者没有进行人员绑定");
			return;
		}

	}

	@Override
	protected void onBoCancel() throws Exception {
		// TODO Auto-generated method stub
		if (isAdd) { // 判断是否为增加后 如果是 清空数据
			getBufferData().clear();
			getBillUI().setBillOperate(IBillOperate.OP_INIT);
			getBillUI().initUI();
			isAdd = false;
		} else {
			super.onBoCancel();
		}
	}

	// 0显示
	public void showZeroLikeNull(boolean bShowZeroLikeNull) {
		BillRendererVO vo = getBillCardPanelWrapper().getBillCardPanel()
				.getBodyPanel().getRendererVO();
		vo.setShowZeroLikeNull(bShowZeroLikeNull);
		getBillCardPanelWrapper().getBillCardPanel().getBodyPanel()
				.setShowFlags(vo);
	}

	@Override
	protected void onBoEdit() throws Exception {
		// TODO Auto-generated method stub
		super.onBoEdit();
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("srl_pk")
				.setEnabled(false);
		showZeroLikeNull(false);
	}
}