package nc.ui.wds.w8004040204;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.wds.w8004040204.Iw8004040204;
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
import nc.ui.wds.w8000.W8004040204Action;
import nc.ui.wds.w8004040204.ssButtun.ISsButtun;
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
import nc.vo.to.pub.ConstVO;
import nc.vo.wds.pub.WDSTools;
import nc.vo.wds.w8004040204.MyBillVO;
import nc.vo.wds.w8004040204.TbOutgeneralBVO;
import nc.vo.wds.w8004040204.TbOutgeneralHVO;
import nc.vo.wds.w80060406.TbFydmxnewVO;
import nc.vo.wds.w80060406.TbFydnewVO;
import nc.vo.wds.w80060604.SoSaleVO;
import nc.vo.wds.w80060604.SoSaleorderBVO;
import nc.vo.wds.w80060608.IcGeneralBVO;
import nc.vo.wds.w80060608.IcGeneralHVO;

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

	private FydnewDlg fydnewdlg = null; // 查询方法的类

	boolean isAdd = false;

	boolean opType = false; // 是否赠品

	private List hiddenList = null;
	private String pk_stock = ""; // 当前登录者对应的仓库主键
	IUAPQueryBS iuap = (IUAPQueryBS) NCLocator.getInstance().lookup(
			IUAPQueryBS.class.getName());

	private Integer isControl = -1; // 是否有权限操作当前单据 1为可以签字的用户 0 修改操作 2 可以进行出库
									// 3所有权限

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
			if (null != isType && isType.equals("0")) {
				isStock = CommonUnit.getSotckIsTotal(pk_stock);
				pkList = CommonUnit.getInvbasdoc_Pk(ClientEnvironment
						.getInstance().getUser().getPrimaryKey());
				isControl = 2;
			} else if (null != isType && isType.equals("1")) {
				isControl = 1;
				// getButtonManager().getButton(IBillButton.Add).setEnabled(false);
				// myClientUI.updateButtons();
			} else if (null != isType && isType.equals("2")) {
				isControl = 0;
			} else if (null != isType && isType.equals("3")) {
				isControl = 3;
				isStock = CommonUnit.getSotckIsTotal(pk_stock);
				pkList = CommonUnit.getInvbasdoc_Pk(ClientEnvironment
						.getInstance().getUser().getPrimaryKey());
			}
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
	}

	protected IBusinessController createBusinessAction() {
		// TODO Auto-generated method stub
		switch (getUIController().getBusinessActionType()) {
		case IBusinessActionType.PLATFORM:
			return new BusinessAction(getBillUI());
		case IBusinessActionType.BD:
			return new W8004040204Action(getBillUI());
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
		// 发运科或者内勤
		if (isControl == 0 || isControl == 3) {
			super.onBoEdit();
			// 设置托盘指定按钮可用，修改状态
			isAdd = false;
			getButtonManager().getButton(ISsButtun.tpzd).setEnabled(true);
			myClientUI.updateButtons();
		} else {
			myClientUI.showErrorMessage("您没有权限");
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
		StringBuffer strWhere = new StringBuffer();
		if (isControl == -1) {
			myClientUI.showErrorMessage("您没有权限操作");
			return;
		}
		if (askForQueryCondition(strWhere) == false)
			return;// 用户放弃了查询

		int tmp = strWhere.indexOf("tb_outgeneral_h.vbilltype");
		StringBuffer strtmp = new StringBuffer(strWhere.substring(tmp, strWhere
				.length()));
		StringBuffer a = new StringBuffer(strWhere.toString().toLowerCase()
				.substring(tmp, strWhere.length()));

		int tmpnum = a.indexOf("and");
		// 进行单据类型转换
		StringBuffer b = new StringBuffer(a.substring(0, tmpnum));
		for (int i = 28; i < b.length(); i++) {
			String stra = b.substring(i, i + 1);
			if (stra.equals("0"))
				b.replace(i, i + 1, "1");
			if (stra.equals("1"))
				b.replace(i, i + 1, "3");
			if (stra.equals("2"))
				b.replace(i, i + 1, "4");
		}
		strtmp.replace(0, tmpnum, b.toString());
		strWhere.replace(tmp, strWhere.length(), strtmp.toString());

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
				strWhere.append(" and srl_pk = '" + pk_stock + "' ");
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
			// 信息科查询全部
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

	@Override
	protected void onBoSave() throws Exception {
		// 获取当前页面中VO
		AggregatedValueObject myBillVO = getBillUI().getVOFromUI();
		setTSFormBufferToVO(myBillVO);
		TbOutgeneralHVO generalh = (TbOutgeneralHVO) myBillVO.getParentVO();
		TbOutgeneralBVO[] generalb = (TbOutgeneralBVO[]) myBillVO
				.getChildrenVO();
		// 验证表体是否有数据
		if (null == generalb || generalb.length < 0) {
			myClientUI.showErrorMessage("操作失败,您无权操作");
			return;
		}
		// 运单的Object,里面共三个值，[0]true or false false为生成运单失败
		// [1]运单主表对象集合List [2]运单子表对象集合List
		Object[] obj = null;
		if (!validate(generalb)){
				return;
		}
		//生成运单
		obj = insertFyd(generalh, generalb);
		TbOutgeneralHVO tmpgeneralh = null;
		// 根据来源单据号查询是否有做过出库
		String strWhere = " dr = 0 and vsourcebillcode = '"
				+ generalh.getVsourcebillcode() + "' and csourcebillhid = '"
				+ generalh.getCsourcebillhid() + "'";
		ArrayList tmpList = (ArrayList) iuap.retrieveByClause(
				TbOutgeneralHVO.class, strWhere);
		if (null != tmpList && tmpList.size() > 0) {
			tmpgeneralh = (TbOutgeneralHVO) tmpList.get(0);
		}
		// 该单据有过出库记录
		if (null != tmpgeneralh) {
			// 把表头替换
			generalh = tmpgeneralh;
		} else {
			isAdd = true;
			// 制单时间
			generalh.setTmaketime(myClientUI._getServerTime().toString());
			// 设置单据号
			generalh.setVbillcode(CommonUnit.getBillCode("4C",
					ClientEnvironment.getInstance().getCorporation()
							.getPk_corp(), "", ""));
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
		// 最后修改时间
		generalh.setTlastmoditime(myClientUI._getServerTime().toString());
		// 设置修改人
		generalh.setClastmodiid(ClientEnvironment.getInstance().getUser()
				.getPrimaryKey());
		// 给聚合VO中表头赋值
		myBillVO.setParentVO(generalh);
		// 表体赋值
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
			if (getBillUI().isSaveAndCommitTogether()){
				myBillVO = getBusinessAction().saveAndCommit(myBillVO,
						getUIController().getBillType(), _getDate().toString(),
						getBillUI().getUserObject(), myBillVO);
			}else {
				List<Object> objUser = new ArrayList<Object>();
				objUser.add(getBillUI().getUserObject());
				objUser.add(generalb);
				if (isAdd){
					objUser.add(true);
				}else{
					objUser.add(false);
				}
				objUser.add(false);
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
		// 获取所选择的行号
		int index = getBillCardPanelWrapper().getBillCardPanel().getBillTable()
				.getSelectedRow();
		if (index > -1) {
			// 获取表体选择的VO对象
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
				}
				TrayDisposeDlg tdpDlg = new TrayDisposeDlg("4203",
						ClientEnvironment.getInstance().getUser()
								.getPrimaryKey(),
						ClientEnvironment.getInstance().getCorporation()
								.getPrimaryKey(), "8004040204", myClientUI);
				tdpDlg.getReturnVOs(myClientUI, item, index, pk_stordoc
						.toString(), opType);
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
				}
				String msg = iw.autoPickAction(ClientEnvironment.getInstance()
						.getUser().getPrimaryKey(), generalbVO, pk_stordoc
						.toString(), "def19");
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
						Object[] a = iw.getNoutNum(generalbVO[i]
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
				}
				chaneColor();
				this.onckmx();

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
			myClientUI.showErrorMessage("操作失败,您无权操作");
			return;
		}
		TrayDisposeDetailDlg tdpDlg = new TrayDisposeDetailDlg("4203",
				ClientEnvironment.getInstance().getUser().getPrimaryKey(),
				ClientEnvironment.getInstance().getCorporation()
						.getPrimaryKey(), "8004040204", myClientUI);

		TbOutgeneralBVO[] item = (TbOutgeneralBVO[]) getBufferData()
				.getCurrentVO().getChildrenVO();

		tdpDlg.getReturnVOs(myClientUI, item);

	}

	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		// TODO Auto-generated method stub
		if (isControl == 2 || isControl == 3) {
			isAdd = true;
			// 实例查询类
			fydnewdlg = new FydnewDlg(myClientUI, pkList, isStock);
			// 调用方法 获取查询后的聚合VO
			AggregatedValueObject[] vos = fydnewdlg
					.getReturnVOs(ClientEnvironment.getInstance()
							.getCorporation().getPrimaryKey(),
							ClientEnvironment.getInstance().getUser()
									.getPrimaryKey(), "4202",
							ConstVO.m_sBillDRSQ, "8004040204", "8004040294",
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
									iw.whs_processAction("CANCELSIGN",
											"DELETE", "4C", _getDate()
													.toString(), billvo,
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
				int[] index = myClientUI.getBillListPanel().getHeadTable()
						.getSelectedRows();
				if (null != index && index.length > 0) {

					for (int i = 0; i < index.length; i++) {
						AggregatedValueObject value = getBufferData()
								.getVOByRowNo(index[i]);
						TbOutgeneralHVO generalh = (TbOutgeneralHVO) value
								.getParentVO();
						// TbOutgeneralBVO[] bvo = (TbOutgeneralBVO[]) value
						// .getChildrenVO();

						Object result = iuap
								.retrieveByPK(TbOutgeneralHVO.class, generalh
										.getGeneral_pk());
						if (null != result) {
							generalh = (TbOutgeneralHVO) result;
							if (null != generalh.getVbillstatus()
									&& generalh.getVbillstatus() == 0) {
								myClientUI.showErrorMessage("该单据已经签字");
								return;
							} else if (null != generalh.getVbillstatus()
									&& generalh.getVbillstatus() == 1) {
								// 状态
								generalh.setVbillstatus(new Integer(0));
								// 签字人主键
								generalh.setCregister(ClientEnvironment
										.getInstance().getUser()
										.getPrimaryKey());
								// 签字时间
								generalh.setTaccounttime(getBillUI()
										._getServerTime().toString());
								// 签字日期
								generalh.setQianzidate(_getDate());
								AggregatedValueObject billvo = changeReqOutgeneraltoGeneral(value);
								iw
										.whs_processAction("PUSHSAVE", "SIGN",
												"4C", _getDate().toString(),
												billvo, generalh);

								// super.onBoRefresh();

							} else {
								myClientUI.showHintMessage(null);
								myClientUI.showErrorMessage("该单据还没有进行运单确认");
								return;
							}
						}
					}
					myClientUI.showHintMessage("正在刷新数据");
					for (int i = 0; i < index.length; i++) {
						getBufferData().setCurrentRow(index[i]);
						super.onBoRefresh();
					}
					myClientUI.showHintMessage("签字成功");
					getButtonManager().getButton(
							nc.ui.wds.w80060206.buttun0206.ISsButtun.Qzqr)
							.setEnabled(false);
					getButtonManager().getButton(
							nc.ui.wds.w80060206.buttun0206.ISsButtun.Qxqz)
							.setEnabled(true);
					myClientUI.updateButtons();
				}
			} else {
				myClientUI.showHintMessage(null);
			}

		} else {
			myClientUI.showHintMessage(null);
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
	public GeneralBillVO changeReqOutgeneraltoGeneral(
			AggregatedValueObject value) throws Exception {
		if (getBufferData().getCurrentRow() < 0) {
			myClientUI.showErrorMessage("请选择表头进行签字");
			return null;
		}
		// 本地出库表 表头
		TbOutgeneralHVO outhvo = (TbOutgeneralHVO) value.getParentVO();
		// 需要查询本地数据，添加确认条件----------为完成
		// Object result = iuap.retrieveByPK(TbOutgeneralHVO.class, outhvo
		// .getGeneral_pk());
		//		
		// if(null!=result){
		// outhvo = (TbOutgeneralHVO) result;
		// if(outhvo.getVbillstatus())
		// }

		// 本地出库表 表体
		TbOutgeneralBVO[] outbvo = (TbOutgeneralBVO[]) value.getChildrenVO();

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
		// hiddenList = new ArrayList();
		MyBillVO myBillVO = new MyBillVO();
		TbOutgeneralHVO generalHVO = new TbOutgeneralHVO();
		// 子表信息数组集合
		List<TbOutgeneralBVO> generalBList = new ArrayList<TbOutgeneralBVO>();

		TbFydnewVO fydnew = (TbFydnewVO) vos[0].getParentVO();
		generalHVO.setSrl_pk(fydnew.getSrl_pk()); // 出库仓库
		generalHVO.setCbiztype(fydnew.getPk_busitype()); // 业务类型主键
		generalHVO.setCdptid(fydnew.getCdeptid()); // 部门
		generalHVO.setCbizid(fydnew.getPk_psndoc()); // 业务员
		generalHVO.setCcustomerid(fydnew.getPk_kh()); // 客户
		generalHVO.setVdiliveraddress(fydnew.getFyd_shdz()); // 收货地址
		generalHVO.setVnote(fydnew.getFyd_bz()); // 备注
		generalHVO.setVsourcebillcode(fydnew.getVbillno()); // 来源单据号
		generalHVO.setDauditdate(fydnew.getDapprovedate()); // 审核日期
		generalHVO.setCsourcebillhid(fydnew.getFyd_pk()); // 来源单据表头主键
		generalHVO.setVbilltype(fydnew.getBilltype().toString()); // 单据类型

		myBillVO.setParentVO(generalHVO);
		// 循环缓存中的子表信息
		if (null != fydnewdlg.getFydmxVO() && fydnewdlg.getFydmxVO().length > 0) {
			// 获取缓存 转换子表其他的信息
			for (int i = 0; i < fydnewdlg.getFydmxVO().length; i++) {
				TbFydmxnewVO fydmxnewvo = fydnewdlg.getFydmxVO()[i];
				if (fydnew.getFyd_pk().equals(fydmxnewvo.getFyd_pk())) {
					TbOutgeneralBVO generalBVO = new TbOutgeneralBVO();
					setGeneralbVO(fydnew, fydmxnewvo, generalBVO);
					generalBVO.setIsoper(new UFBoolean("Y"));
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
	private void setGeneralbVO(TbFydnewVO fydnew, TbFydmxnewVO fydmxnewvo,
			TbOutgeneralBVO generalBVO) {
		generalBVO.setCsourcebillhid(fydnew.getFyd_pk()); // 来源单据表头主键
		generalBVO.setVsourcebillcode(fydnew.getVbillno()); // 来源单据号
		generalBVO.setCsourcetype(fydnew.getBilltype().toString()); // 来源单据类型
		generalBVO.setCsourcebillbid(fydmxnewvo.getCfd_pk()); // 来源单据表体主键
		generalBVO.setCfirstbillhid(fydmxnewvo.getCsaleid()); // 源头单据表头主键
		generalBVO.setCfirstbillbid(fydmxnewvo.getCorder_bid()); // 源头单据表体主键
		generalBVO.setCrowno(fydmxnewvo.getCrowno()); // 行号
		generalBVO.setNshouldoutnum(fydmxnewvo.getCfd_yfsl()); // 应发数量
		generalBVO.setNshouldoutassistnum(fydmxnewvo.getCfd_xs()); // 应发辅数量
		generalBVO.setCinventoryid(fydmxnewvo.getPk_invbasdoc()); // 存货主键
		generalBVO.setFlargess(fydmxnewvo.getBlargessflag()); // 是否赠品
	}

	public Iw8004040204 getIw() {
		return iw;
	}

	public void setIw(Iw8004040204 iw) {
		this.iw = iw;
	}

}