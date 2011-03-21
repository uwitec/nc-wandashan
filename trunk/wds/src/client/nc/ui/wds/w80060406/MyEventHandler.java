package nc.ui.wds.w80060406;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.wds.w80060406.Iw80060406;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.query.INormalQuery;
import nc.ui.wds.w8000.CommonUnit;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.wds.w80060406.MyBillVO;
import nc.vo.wds.w80060406.TbFydmxnewVO;
import nc.vo.wds.w80060406.TbFydnewVO;

/**
 * 
 *  发运拆分 
 * @author author xzs
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	MyClientUI myClientUI = null;
	boolean isEdit = false;
	private String dhzPK = null;
	private boolean isControl = false; // 是否有权限操作当前单据

	public String getDhzPK() {
		return dhzPK;
	}

	public void setDhzPK(String dhzPK) {
		this.dhzPK = dhzPK;
	}

	public MyEventHandler(MyClientUI billUI, IControllerBase control) {
		super(billUI, control);
		myClientUI = billUI;
		String isType;
		try {
			isType = CommonUnit.getUserType(ClientEnvironment.getInstance()
					.getUser().getPrimaryKey());
			if ((null != isType && isType.equals("2")) ||(null != isType && isType.equals("3"))) {
				isControl = true;
			}
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean validate() {
		// 获取表头数据
		String stratts = getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("fyd_begints").getValueObject().toString();// 获取开始时间
		String endts = getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("fyd_endts").getValueObject().toString(); // 获取结束时间
		Object fhc = getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				"srl_pk").getValueObject(); // 获取发货站
		Object dhc = getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				"srl_pkr").getValueObject(); // 获取到货站
		// 进行判断弹出对话框提示
		if (stratts == null || "".equals(stratts)) {
			getBillUI().showWarningMessage("请选择开始时间");
		} else if (endts == null || "".equals(endts)) {
			getBillUI().showWarningMessage("请选择结束时间");
		} else if (fhc == null || "".equals(fhc)) {
			getBillUI().showWarningMessage("请选择发货站");
		} else if (dhc == null || "".equals(dhc)) {
			getBillUI().showWarningMessage("请选择到货站");
		} else if (fhc.toString().equals(dhc)) {
			getBillUI().showWarningMessage("发货站与到货站相同,请重新选择");
		} else {
			return true;
		}
		return false;
	}

	// 自定义按钮 查询明细 点击事件
	protected void oncxmx() throws Exception {
		getBufferData().clear();
		if (this.validate()) {
			// 获取表头数据
			String stratts = getBillCardPanelWrapper().getBillCardPanel()
					.getHeadItem("fyd_begints").getValueObject().toString();// 获取开始时间
			String endts = getBillCardPanelWrapper().getBillCardPanel()
					.getHeadItem("fyd_endts").getValueObject().toString(); // 获取结束时间
			Object fhc = getBillCardPanelWrapper().getBillCardPanel()
					.getHeadItem("srl_pk").getValueObject(); // 获取发货站
			Object dhc = getBillCardPanelWrapper().getBillCardPanel()
					.getHeadItem("srl_pkr").getValueObject(); // 获取到货站
			this.setDhzPK(dhc.toString());
			String strWhere = " and b.srl_pk= '"
					+ dhc.toString() // 拼装Where条件
					+ "' and b.doperatordate between  '" + stratts + "' and '"
					+ endts + "'";
			Iw80060406 iw = (Iw80060406) NCLocator.getInstance().lookup(
					Iw80060406.class.getName()); // 调用接口
			TbFydmxnewVO[] shvo = null;// 表体数据数组
			getBillCardPanelWrapper().getBillCardPanel().getBillData()
					.setBodyValueVO(new TbFydmxnewVO[0]);
			try {
				shvo = iw.queryFydmxnewVO(strWhere, dhc.toString(), stratts,
						endts, fhc.toString()); // 通过接口获取数据
				if (shvo != null) {
					MyBillVO myBillVO = (MyBillVO) this
							// 获取界面中的UI
							.getBillCardPanelWrapper().getBillCardPanel()
							.getBillValueVO(MyBillVO.class.getName(),
									TbFydnewVO.class.getName(),
									TbFydmxnewVO.class.getName());
					TbFydnewVO fydVO = (TbFydnewVO) myBillVO.getParentVO();
					fydVO.setSe_pk(shvo[0].getCfd_dw());
					myBillVO.setParentVO(fydVO); // 为表头赋值
					myBillVO.setChildrenVO(shvo); // 为表体赋值 传的表体数据数组
					getBufferData().addVOToBuffer(myBillVO); // 为界面UI添加数据
					updateBuffer(); // 更新
					getBillUI().setBillOperate( // 因为点击自定义按钮后就会切到正常按钮模式 所以设置状态
							nc.ui.trade.base.IBillOperate.OP_EDIT);
				} else {
					getBillUI().showWarningMessage("该到货站当前月没有计划");
					return;
				}

			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// 设置执行公式
			getBillCardPanelWrapper().getBillCardPanel().execTailLoadFormulas();
			getBillCardPanelWrapper().getBillCardPanel().execHeadLoadFormulas();
		}
	}

	@Override
	protected void onBoEdit() throws Exception {
		// TODO Auto-generated method stub
		if (isControl) {
			super.onBoEdit();

			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("srl_pk")
					.setEnabled(false);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("srl_pkr")
					.setEnabled(false);

			getButtonManager().getButton(
					nc.ui.wds.w800604.tcButtun.ITcButtun.cxmx)
					.setEnabled(false);

			// 设置执行公式
			getBillCardPanelWrapper().getBillCardPanel().execTailLoadFormulas();
			getBillCardPanelWrapper().getBillCardPanel().execHeadLoadFormulas();
			// 设置修改状态
			isEdit = true;
		} else {
			getBillUI().showErrorMessage("操作失败,当前登录者没有进行人员绑定");
			return;
		}
	}

	/*
	 * 生成订单号
	 */
	private void getOrderNo() throws Exception {

		Object fhz = getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				"srl_pk").getValueObject(); // 获取发货站
		Object dhz = getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				"srl_pkr").getValueObject(); // 获取到货站

		String fhzCode = ""; // 发货站编码
		String dhzCode = ""; // 到货站编码
		String top = ""; // 订单号头三位
		String ddh = null; // 最后生成的订单号
		String d = ""; // 订单号中间
		int num = 0; // 订单号后几位

		// 获取访问数据库对象
		IUAPQueryBS IUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		//
		// // 判断发货站和到货站是否为空
		// if ((fhz != null && !"".equals(fhz))
		// && (dhz != null && !"".equals(dhz))) {
		// // 查询数据库中的结果 结果是以Object为对象的数组
		// ArrayList list = (ArrayList) IUAPQueryBS
		// .executeQuery(
		// "select fyd_ddh from (select fyd_ddh from tb_fydnew where dr = 0 and
		// srl_pkr ='"
		// + dhz
		// + "' and srl_pk ='"
		// + fhz
		// + "' order by fyd_ddh desc) where rownum = 1 ",
		// new ArrayListProcessor());
		// // 判断结果是否为空
		// if (list != null && list.size() > 0) {
		// Object a[] = (Object[]) list.get(0);
		// if (a != null && a.length > 0 && a[0] != null) {
		// // 如果不为空进行赋值
		// ddh = a[0].toString();
		// }
		// }
		// }
		//
		// // 判断获得的订单号是否为空
		// if (ddh != null && !"".equals(ddh)) {
		// // 如果不为空进行字符串截取，截取到最后出现"-"到最后的值
		// String n = ddh.substring(ddh.lastIndexOf("-") + 1, ddh.length())
		// .toString();
		// num = Integer.parseInt(n.trim());
		// }
		// // 为订单号后几位增加1
		// num = num + 1;

		// 查询发货站Code 例如 成都 "CD"
		if (fhz != null && !fhz.equals("")) {
			ArrayList list = (ArrayList) IUAPQueryBS
					.executeQuery(
							"select distinct pk_stordoc,storcode  from tb_storareacl where dr = 0 and pk_stordoc = '"
									+ fhz.toString() + "'",
							new ArrayListProcessor());
			if (list != null && list.size() > 0) {
				// 判断是否为空
				Object a[] = (Object[]) list.get(0);
				if (a != null && a.length > 0 && a[0] != null) {
					// 如果不为空进行赋值
					fhzCode = a[1].toString();
					// 进行截取 例如"CD" 截取第一位"C"转换大写
					fhzCode = fhzCode.substring(0, 1).toUpperCase();
				}

			}
		}
		// 查询到货站Code
		if (dhz != null && !dhz.equals("")) {
			ArrayList list = (ArrayList) IUAPQueryBS
					.executeQuery(
							"select distinct pk_stordoc,storcode  from tb_storareacl where dr = 0 and  pk_stordoc = '"
									+ dhz.toString() + "'",
							new ArrayListProcessor());
			if (list != null && list.size() > 0) {
				Object a[] = (Object[]) list.get(0);
				if (a != null && a.length > 0 && a[0] != null) {
					// 如果不为空进行赋值
					dhzCode = a[1].toString();
					dhzCode = dhzCode.substring(0, 1).toUpperCase();
				}
			}
		}
		String tmpddh = CommonUnit.getBillCode("4206", ClientEnvironment
				.getInstance().getUser().getPrimaryKey(), "", "");
		// 进行组装 订单号头三位
		top = fhzCode + "Y" + dhzCode;
		Object isbig = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("isbig").getValueObject();
		if(null!=isbig){
			if(Boolean.parseBoolean(isbig.toString())){
				ddh = top + "-G" + tmpddh.substring(0, 6) + "-"
				+ tmpddh.substring(6, tmpddh.length());
			}else{
				ddh = top + "-" + tmpddh.substring(0, 6) + "-"
				+ tmpddh.substring(6, tmpddh.length());
			}
		} 
		
		// SimpleDateFormat sdFormat = new SimpleDateFormat("yy");
		// // 组装年
		// d = sdFormat.format(new Date());
		// // 组装年加C
		// // d = d + "C";
		// sdFormat = new SimpleDateFormat("MM");
		// // 组装月份
		// d = d + sdFormat.format(new Date());
		// sdFormat = new SimpleDateFormat("dd");
		// // 组装日
		// d = d + sdFormat.format(new Date());
		// // 最后组装订单号
		// ddh = top + "-" + d + "-" + num;
		// 设置订单号
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("fyd_ddh")
				.setValue(ddh);
		// 设置单据号
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillno")
				.setValue(ddh);
		getBillCardPanelWrapper().getBillCardPanel().setTailItem(
				"fyd_approstate", 0); // 设置审批状态
	}

	@Override
	protected void onBoSave() throws Exception {
		this.onSave();
	}

	private void onSave() throws Exception {
		if (this.validate()) {
			
			AggregatedValueObject mybillVO = getBillUI().getVOFromUI();
			TbFydmxnewVO[] fydmxVO = (TbFydmxnewVO[]) mybillVO.getChildrenVO();
			List<TbFydmxnewVO> fydmxVOList = new ArrayList<TbFydmxnewVO>();
			boolean isPlannum = false;
			// 循环子表数组
			if (null != fydmxVO && fydmxVO.length > 0) {
				for (int i = 0; i < fydmxVO.length; i++) {
					TbFydmxnewVO fydmxvo = fydmxVO[i];
					// 判断子表中是否有输入计划数的单品
					if (null != fydmxvo.getCfd_xs()
							&& !"".equals(fydmxvo.getCfd_xs())) {
						if (fydmxvo.getCfd_xs().toDouble() > 0) {
							isPlannum = true;
							fydmxVOList.add(fydmxvo);
						}
					}
				}
				if (!isPlannum) {
					myClientUI.showWarningMessage("请选择一项单品输入拆分数");
					return;
				}
			} else {
				myClientUI.showWarningMessage("您没有查询单品,请输入条件后点击查询明细");
				return;
			}
			// 调用判断是否为修改状态
			if (!isEdit) {
				getOrderNo();
			}
			// 设置单据类型
			getBillCardPanelWrapper().getBillCardPanel()
					.getHeadItem("billtype").setValue(new Integer(0));
			TbFydmxnewVO[] fydmxvo = null;
			if (fydmxVOList.size() > 0) {
				fydmxvo = new TbFydmxnewVO[fydmxVOList.size()];
				fydmxvo = fydmxVOList.toArray(fydmxvo);
			}
			AggregatedValueObject billVO = getBillUI().getChangedVOFromUI();
			// 调用判断是否为修改状态
			if (!isEdit) {
				if (!this.getDhzPK().equals(
						((TbFydnewVO) billVO.getParentVO()).getSrl_pkr())) {
					myClientUI.showWarningMessage("您修改了到货站,请重新查询");
					return;
				}
			}

			billVO.setChildrenVO(fydmxvo);
			setTSFormBufferToVO(billVO);
			AggregatedValueObject checkVO = billVO;
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
					&& (billVO.getChildrenVO() == null || billVO
							.getChildrenVO().length == 0)) {
				isSave = false;
			} else {
				if (getBillUI().isSaveAndCommitTogether())
					billVO = getBusinessAction().saveAndCommit(billVO,
							getUIController().getBillType(),
							_getDate().toString(), getBillUI().getUserObject(),
							checkVO);
				else

					// write to database
					billVO = getBusinessAction().save(billVO,
							getUIController().getBillType(),
							_getDate().toString(), getBillUI().getUserObject(),
							checkVO);
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
			// 设置执行公式
			getBillCardPanelWrapper().getBillCardPanel().execTailLoadFormulas();
			getBillCardPanelWrapper().getBillCardPanel().execHeadLoadFormulas();
			// 设置按钮状态
			getButtonManager().getButton(
					nc.ui.wds.w800604.tcButtun.ITcButtun.cxmx)
					.setEnabled(false);
			// 设置非修改状态
			isEdit = false;
			this.onBoRefresh();
		}
	}

	@Override
	protected void onBoDelete() throws Exception {
		// TODO Auto-generated method stub
		if (isControl) {
			super.onBoDelete();
			if (null != getBufferData().getCurrentVO()) {
				TbFydnewVO fydnewvo = (TbFydnewVO) getBufferData()
						.getCurrentVO().getParentVO();
				if (null != fydnewvo) {
					int tmp = fydnewvo.getFyd_approstate();
					// 判断是否为 1 。 0 待审批 1 审批通过 2 不通过
					if (tmp == 1) {
						// 设置按钮状态
						getButtonManager().getButton(IBillButton.Edit)
								.setEnabled(false);
						getButtonManager().getButton(IBillButton.Delete)
								.setEnabled(false);
					} else {
						getButtonManager().getButton(IBillButton.Edit)
								.setEnabled(true);
						getButtonManager().getButton(IBillButton.Delete)
								.setEnabled(true);
					}
				}
			}
		} else {
			getBillUI().showErrorMessage("操作失败,当前登录者没有进行人员绑定");
			return;
		}
	}

	@Override
	protected void onBoCancel() throws Exception {
		// TODO Auto-generated method stub
		isEdit = false;
		// 设置按钮状态
		getButtonManager().getButton(nc.ui.wds.w800604.tcButtun.ITcButtun.cxmx)
				.setEnabled(false);
		super.onBoCancel();
		this.getBillUI().initUI();
		// 设置执行公式
		getBillCardPanelWrapper().getBillCardPanel().execHeadLoadFormulas();
		getBillCardPanelWrapper().getBillCardPanel().execTailLoadFormulas();
	}

	/**
	 * 获取开始日期和结束日期 21号到下个月的20号
	 * 
	 * @param type
	 *            类型 true 开始日期 false 结束日期
	 * @return 日期
	 * @throws ParseException
	 */
	private String getStarOrEndDate(boolean type) throws ParseException {
		Calendar calen = Calendar.getInstance();

		int intDay = calen.get(Calendar.DATE);
		int intMonth = calen.get(Calendar.MONTH) + 1;
		int intYear = calen.get(Calendar.YEAR);
		String tmp = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		if (type) {
			if (intDay >= 21) {
				tmp = intYear + "-" + intMonth + "-" + 21;
				return format.format(format.parse(tmp));
			} else {
				intMonth = intMonth - 1;
				if (intMonth == 0) {
					intYear = intYear - 1;
					intMonth = 12;
				}
				tmp = intYear + "-" + intMonth + "-" + 21;
				return format.format(format.parse(tmp));
			}
		} else {
			if (intDay >= 21) {
				intMonth = intMonth + 1;
				if (intMonth == 13) {
					intYear = intYear + 1;
					intMonth = 1;
				}
				tmp = intYear + "-" + intMonth + "-" + 20;
				return format.format(format.parse(tmp));

			} else {
				tmp = intYear + "-" + intMonth + "-" + 20;
				return format.format(format.parse(tmp));
			}
		}

	}

	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		// TODO Auto-generated method stub
		if (isControl) {
			// 设置按钮状态
			getButtonManager().getButton(
					nc.ui.wds.w800604.tcButtun.ITcButtun.cxmx).setEnabled(true);
			super.onBoAdd(bo);
			String strDate = this.getStarOrEndDate(true);
			String endDate = this.getStarOrEndDate(false);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"fyd_begints").setValue(strDate);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"fyd_endts").setValue(endDate);
			// 设置制单时间
			getBillCardPanelWrapper().getBillCardPanel().getHeadTailItem(
					"dmakedate").setValue(_getDate());
			String pk_stockdoc = CommonUnit.getStordocName(ClientEnvironment
					.getInstance().getUser().getPrimaryKey());

			// 设置发货站
			getBillCardPanelWrapper().getBillCardPanel().getHeadTailItem(
					"srl_pk").setValue(pk_stockdoc);
			// getBillUI().setCardUIState();
			// 设置执行公式
			getBillCardPanelWrapper().getBillCardPanel().execHeadLoadFormulas();
			getBillCardPanelWrapper().getBillCardPanel().execTailLoadFormulas();
		} else {
			getBillUI().showErrorMessage("操作失败,当前登录者没有进行人员绑定");
			return;
		}
	}

	@Override
	protected void onBoCard() throws Exception {
		// TODO Auto-generated method stub
		super.onBoCard();
		// 设置执行公式
		getBillCardPanelWrapper().getBillCardPanel().execTailLoadFormulas();
		getBillCardPanelWrapper().getBillCardPanel().execHeadLoadFormulas();
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
	protected void onBoQuery() throws Exception {
		// TODO Auto-generated method stub
		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// 用户放弃了查询

		// strWhere.append(" tb_fydnew.billtype = 0 and ");

		SuperVO[] queryVos = queryHeadVOs(strWhere.toString());

		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);

		updateBuffer();

		// 设置执行公式
		getBillCardPanelWrapper().getBillCardPanel().execTailLoadFormulas();
	}

	@Override
	protected void onBoRefresh() throws Exception {
		// TODO Auto-generated method stub
		super.onBoRefresh();
		// 设置执行公式
		getBillCardPanelWrapper().getBillCardPanel().execTailLoadFormulas();
	}

}