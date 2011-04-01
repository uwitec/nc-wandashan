package nc.ui.dm.confirm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.BusinessAction;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.businessaction.IBusinessController;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.query.INormalQuery;
import nc.ui.wl.pub.LoginInforHelper;
import nc.vo.ic.other.out.TbOutgeneralHVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.wds.w80060406.TbFydnewVO;

/**
 * 
 * 该类是AbstractMyEventHandler抽象类的实现类， 主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
 * 
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	MyClientUI myClientUI = null;
	// 查询接口
	IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
			IUAPQueryBS.class.getName());

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
		myClientUI = (MyClientUI) billUI;
	}

	@Override
	protected void onBoReturn() throws Exception {
		// TODO Auto-generated method stub

		super.onBoReturn();
		getBillCardPanelWrapper().getBillCardPanel().execHeadLoadFormulas();
	}

	@Override
	protected IBusinessController createBusinessAction() {
		// TODO Auto-generated method stub
		switch (getUIController().getBusinessActionType()) {
		case IBusinessActionType.PLATFORM:
			return new BusinessAction(getBillUI());
		case IBusinessActionType.BD:
			return new W80060210Action(getBillUI());
		default:
			return new BusinessAction(getBillUI());
		}
	}

	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
		// // 运单确认
		String ydqr = getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				"ydqr").getValue();
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem(
				"fyd_constatus", ydqr);

		int selectRowNum = getBillCardPanelWrapper().getBillCardPanel()
				.getBillTable().getRowCount();
		boolean isallzero = true;
		double dw = 0d;
		double xs = 0d;
		for (int i = 0; i < selectRowNum; i++) {
			UFDouble cfd_sffsl = (UFDouble) getBillCardPanelWrapper()
					.getBillCardPanel().getBillModel().getValueAt(i,
							"cfd_sffsl");
			UFDouble cfd_sfsl = (UFDouble) getBillCardPanelWrapper()
					.getBillCardPanel().getBillModel()
					.getValueAt(i, "cfd_sfsl");
			if (null != cfd_sffsl && !"".equals(cfd_sfsl)) {
				dw += cfd_sfsl.doubleValue();
			}
			if (null != cfd_sffsl && !"".equals(cfd_sffsl)) {
				xs += cfd_sffsl.doubleValue();
			}

			/*
			 * BigDecimal dw1 = new BigDecimal(Double.toString(dw)); BigDecimal
			 * xs1 = new BigDecimal(Double.toString(xs));
			 */
			if (null != cfd_sffsl && cfd_sffsl.doubleValue() != 0) {
				isallzero = false;
			}

		}
		if (isallzero) {
			getBillUI().showErrorMessage("实出库数量全部为为零不能确认！");
			return;
		}

		Object ydqrzt = getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("ydqr").getValueObject();

		if (null == ydqrzt || "".equals(ydqrzt)) {
			getBillUI().showErrorMessage("运单未确认");
			return;
		} else if ("1".equals(ydqrzt)) {

			// 保存之前对页面赋值**************************
			Object tc_pk = getBillCardPanelWrapper().getBillCardPanel()
					.getHeadItem("tc_pk").getValueObject();
			// 运价
			Object pk_yjzbzj = getBillCardPanelWrapper().getBillCardPanel()
					.getHeadItem("pk_yjzbzj").getValueObject();
			// 运价种类
			Object fyd_yjzl = getBillCardPanelWrapper().getBillCardPanel()
					.getHeadItem("fyd_yjzl").getValueObject();
			// 附加费
			Object fyd_fjf = getBillCardPanelWrapper().getBillCardPanel()
					.getHeadItem("fyd_fjf").getValueObject();
			// 公路里程
			Object fyd_yslc = getBillCardPanelWrapper().getBillCardPanel()
					.getHeadItem("fyd_yslc").getValueObject();
			// 获得当前登陆人员的主键
			ClientEnvironment.getInstance().getUser().getPrimaryKey();
			// 计算运费
			double yf = 0d;
			if (null != fyd_yjzl && !"".equals(fyd_yjzl)) {
				// 0按吨计算1按箱计算
				if ("0".equals(fyd_yjzl.toString())) {
					if (null != pk_yjzbzj && null != fyd_fjf
							&& null != fyd_yjzl && null != fyd_yslc) {
						double a = 0d;
						double c = 0d;
						double d = 0d;
						if (!"".equals(pk_yjzbzj)) {
							a = Double.parseDouble(pk_yjzbzj.toString());
						}

						if (!"".equals(fyd_fjf)) {
							c = Double.parseDouble(fyd_fjf.toString());
						}
						if (!"".equals(fyd_yslc)) {
							d = Double.parseDouble(fyd_yslc.toString());
						}
						/*
						 * BigDecimal b1 = new BigDecimal(Double.toString(a));
						 * BigDecimal b2 = new BigDecimal(Double.toString(c));
						 * BigDecimal b3 = new BigDecimal(Double.toString(d));
						 */
						double test1 = a * d;
						yf = a * d * dw + c;
						getBillCardPanelWrapper().getBillCardPanel()
								.getHeadItem("fyd_yf").setValue(yf);
					}
				} else if ("1".equals(fyd_yjzl.toString())) {
					if (null != pk_yjzbzj && null != fyd_fjf
							&& null != fyd_yjzl) {
						double a = 0d;
						double b = 0d;
						double c = 0d;
						if (!"".equals(pk_yjzbzj)) {
							a = Double.parseDouble(pk_yjzbzj.toString());
						}
						if (!"".equals(fyd_fjf)) {
							c = Double.parseDouble(fyd_fjf.toString());
						}
						/*
						 * BigDecimal b1 = new BigDecimal(Double.toString(a));
						 * BigDecimal b2 = new BigDecimal(Double.toString(b));
						 * BigDecimal b3 = new BigDecimal(Double.toString(c));
						 */
						yf = a * xs + c;
						getBillCardPanelWrapper().getBillCardPanel()
								.getHeadItem("fyd_yf").setValue(yf);
					}

				}
			} else {
				double c = 0d;
				if (null != fyd_fjf && !"".equals(fyd_fjf)) {
					c = Double.parseDouble(fyd_fjf.toString());
				}
				getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
						"fyd_yf").setValue(c);
			}

			AggregatedValueObject billVO = getBillUI().getVOFromUI();
			setTSFormBufferToVO(billVO);
			AggregatedValueObject checkVO = getBillUI().getVOFromUI();
			setTSFormBufferToVO(checkVO);
			if (null != ydqr && !"".equals(ydqr)) {
				if ("1".equals(ydqr)) {

					((TbFydnewVO) billVO.getParentVO()).setFyd_fyzt(1);
					((TbFydnewVO) billVO.getParentVO())
							.setDmaketime(getBillUI()._getServerTime()
									.toString());
					((TbFydnewVO) billVO.getParentVO()).setVdef9(getBillUI()
							._getDate().toString().trim());
				}

			}

			// 进行数据清空
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
					&& (billVO.getChildrenVO() == null || billVO
							.getChildrenVO().length == 0)) {
				isSave = false;
			} else {
				if (getBillUI().isSaveAndCommitTogether()) {
					billVO = getBusinessAction().saveAndCommit(billVO,
							getUIController().getBillType(),
							_getDate().toString(), getBillUI().getUserObject(),
							checkVO);
				} else {
					// write to database
					billVO = getBusinessAction().save(billVO,
							getUIController().getBillType(),
							_getDate().toString(), getBillUI().getUserObject(),
							checkVO);
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

		} else {
			getBillUI().showErrorMessage("运单未确认！");
			return;
		}
	}

	@Override
	protected void onBoQuery() throws Exception {
		// TODO Auto-generated method stub

		String s =LoginInforHelper.getCwhid(myClientUI._getOperator());
		
		StringBuffer strWhere = new StringBuffer();
		strWhere
				.append(" ((vbillstatus=1 and billtype=4 and pk_mergelogo is not null) or (vbillstatus=1 and billtype !=4 )) and srl_pk='"
						+ s + "' and  ");
		if (askForQueryCondition(strWhere) == false)
			return;// 用户放弃了查询

		SuperVO[] queryVos = queryHeadVOs(strWhere.toString());

		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
		// getBillManageUI().getBillListPanel().getHeadBillModel()
		// .execLoadFormula();

	}

	// 列表UI
	private BillManageUI getBillManageUI() {
		return (BillManageUI) getBillUI();
	}

	/**
	 * 弹出查询对话框向用户询问查询条件。 如果用户在对话框点击了“确定”，那么返回true,否则返回false
	 * 查询条件通过传入的StringBuffer返回给调用者
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
		// 运单确认状态
		if (null != strWhere && !("".equals(strWhere))) {
			int fyd_constatus = strWhere
					.indexOf("tb_fydnew.fyd_constatus = '0'");
			int fyd_constatus1 = strWhere
					.indexOf("tb_fydnew.fyd_constatus in ('0','1')");
			if (fyd_constatus1 == -1) {
				if (fyd_constatus > 0) {
					String newStrWhere = strWhere.substring(0,
							fyd_constatus - 1).toString();
					newStrWhere += " ( tb_fydnew.fyd_constatus='0' or"
							+ " tb_fydnew.fyd_constatus is null )) ";
					strWhere = newStrWhere;
				} else if (fyd_constatus == 0) {
					String newStrWhere = " ( tb_fydnew.fyd_constatus='0' or"
							+ " tb_fydnew.fyd_constatus is null ) ";
					strWhere = newStrWhere;
				}

			} else {
				if (fyd_constatus1 > 0) {
					String newStrWhere = strWhere.substring(0,
							fyd_constatus1 - 1).toString();
					newStrWhere += " ( tb_fydnew.fyd_constatus in ('0','1') or"
							+ " tb_fydnew.fyd_constatus is null )) ";
					strWhere = newStrWhere;
				} else if (fyd_constatus1 == 0) {
					String newStrWhere = " ( tb_fydnew.fyd_constatus in ('0','1') or"
							+ " tb_fydnew.fyd_constatus is null ) ";
					strWhere = newStrWhere;
				}
			}
		}

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

		strWhere = "(" + strWhere + ") and (isnull(dr,0)=0)";

		if (getHeadCondition() != null)
			strWhere = strWhere + " and " + getHeadCondition();
		// 现在我先直接把这个拼好的串放到StringBuffer中而不去优化拼串的过程
		sqlWhereBuf.append(strWhere);
		return true;

	}

	@Override
	protected void onBoEdit() throws Exception {
		// TODO Auto-generated method stub
		super.onBoEdit();

		/*
		 * String s
		 * =CommonUnit.getStordocName(ClientEnvironment.getInstance().getUser().getPrimaryKey());
		 * try { s=
		 * CommonUnit.getStordocName(ClientEnvironment.getInstance().getUser().getPrimaryKey()); }
		 * catch (BusinessException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */

		Object ydqrzt = getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("ydqr").getValueObject();

		if (null == ydqrzt || "".equals(ydqrzt)) {
			getBillUI().showErrorMessage("运单未确认");
			return;
		} else if ("1".equals(ydqrzt)) {
			// 如果运单已确认就不允许再次修改
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ydqr")
					.setEnabled(false);
		}

		// //运输里程
		// Object fyd_yslc =
		// getBillCardPanelWrapper().getBillCardPanel().getHeadItem("fyd_yslc")
		// .getValueObject();
		// if(null==fyd_yslc||"".equals(fyd_yslc)){
		// getBillUI().showYesNoMessage("没有运输里程，请填写运输里程!");
		// }

		// 获得表头的主键
		String fyd_pk = getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("fyd_pk").getValue();
		// 获得吨位和箱数
		String sql = "select sum(tb_fydmxnew.cfd_sffsl) xs,sum(tb_fydmxnew.cfd_sfsl) ds from tb_fydnew,tb_fydmxnew where tb_fydnew.fyd_pk=tb_fydmxnew.fyd_pk and tb_fydmxnew.dr=0 and tb_fydnew.fyd_pk='"
				+ fyd_pk + "'";
		List list = (List) query.executeQuery(sql, new ArrayListProcessor());
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Object[] o = (Object[]) it.next();
			String o1 = "";
			String o2 = "";
			StringBuffer ls = new StringBuffer();
			if (null != o[0] && !"".equals(o[0])) {
				o1 = o[0].toString();
				ls.append(o1);
			} else {
				ls.append(0);
			}
			ls.append("箱/");
			if (null != o[1] && !"".equals(o[1])) {
				o2 = o[1].toString();
				ls.append(o2);
			} else {
				ls.append(0);
			}
			ls.append("吨");
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("xds")
					.setValue(ls.toString());
		}

		// 运单确认
		// String ydqr =
		// getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
		// "ydqr").getValue();
		//
		// if (null != ydqr && !"".equals(ydqr)) {
		// if ("1".equals(ydqr)) {
		// getBillCardPanelWrapper().getBillCardPanel()
		// .getHeadItem("ydqr").setEdit(false);
		// }else{
		// getBillCardPanelWrapper().getBillCardPanel()
		// .getHeadItem("ydqr").setEdit(true);
		// }
		// }else{
		// getBillCardPanelWrapper().getBillCardPanel()
		// .getHeadItem("ydqr").setEdit(true);
		// }
	}

	@Override
	protected void onBoDelete() throws Exception {
		// TODO Auto-generated method stub
		AggregatedValueObject modelVo = getBufferData().getCurrentVO();
		TbFydnewVO tbFydnewVO = (TbFydnewVO) modelVo.getParentVO();
		if (null != tbFydnewVO.getBilltype()) {
			if (tbFydnewVO.getBilltype().intValue() != 1) {
				getBillUI().showErrorMessage("非销售运单不能删除！");
				return;
			}
		}

		String wSql = " csourcebillhid='" + tbFydnewVO.getFyd_pk()
				+ "' and dr=0 ";
		ArrayList os = (ArrayList) query.retrieveByClause(TbOutgeneralHVO.class,
				wSql);
		if (null != os && os.size() > 0) {
			getBillUI().showErrorMessage("以生成出库单不能删除！");
			return;
		}
		super.onBoDelete();
	}

}