package nc.ui.wds.w80060204;

import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.itf.wds.w80060204.Iw80060204;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.ic.ic009.CardPanelCtrl;
import nc.ui.ic.ic009.ListPanelCtrl;
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
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;
import nc.vo.to.pub.ConstVO;
import nc.vo.wds.w80060406.MyBillVO;
import nc.vo.wds.w80060406.TbFydmxnewVO;
import nc.vo.wds.w80060406.TbFydnewVO;

/**
 * 
 * 销售订单合并
 * 
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	MyClientUI myClientUI = null;
	private boolean isControl = false; // 是否有权限操作当前单据
	private SaleOrderDlg saleOrderDlg = null; // 查询方法的类

	boolean isAdd = false;

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
		myClientUI = (MyClientUI) billUI;
		String isType;
		try {
			isType = CommonUnit.getUserType(ClientEnvironment.getInstance()
					.getUser().getPrimaryKey());
			if (null != isType && isType.equals("2")||(null != isType && isType.equals("3"))) {
				isControl = true;
			}
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onBoEdit() throws Exception {
		// TODO Auto-generated method stub

		getButtonManager().getButton(nc.ui.wds.w80060204.cfButtun.ICfButtun.zj)
				.setEnabled(false); // 更改增加按钮状态
		super.onBoEdit();
	}

	@Override
	protected void onBoQuery() throws Exception {
		// TODO Auto-generated method stub
		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// 用户放弃了查询

		strWhere
				.append(" and billtype = 4 and vbillstatus = 1  and mergelogo is null");

		SuperVO[] queryVos = queryHeadVOs(strWhere.toString());

		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
	}

	/**
	 * 增加按钮的单击事件 操作查询类 进行查询和显示
	 */
	protected void onzj() throws Exception {
		if (isControl) {
			// 实例查询类
			saleOrderDlg = new SaleOrderDlg(myClientUI);

			// 调用方法 获取查询后的聚合VO
			AggregatedValueObject[] vos = saleOrderDlg.getReturnVOs(
					ClientEnvironment.getInstance().getCorporation()
							.getPrimaryKey(), ClientEnvironment.getInstance()
							.getUser().getPrimaryKey(), "0204",
					ConstVO.m_sBillDRSQ, "80060204", "8006020401", myClientUI);

			// 判断是否对查询模板有进行操作
			if (null == vos || vos.length == 0) {
				return;
			}

			// 调用转换类 把模板中获取的对象转换成自己的当前显示的对象，调用方法
			MyBillVO voForSave = changeReqSaleOrderYtoFyd(vos);
			// 进行数据情况 和按钮初始化
			getBufferData().clear();
			getBillUI().setBillOperate(IBillOperate.OP_INIT);
			// 填充数据
			getBufferData().addVOToBuffer(voForSave);
			// 更新数据
			updateBuffer();
			// 更改增加按钮状态
			getButtonManager().getButton(
					nc.ui.wds.w80060204.cfButtun.ICfButtun.zj)
					.setEnabled(false);
			super.onBoEdit();
			// getBillUI().setBillOperate( // 因为点击自定义按钮后就会切到正常按钮模式 所以设置状态
			// nc.ui.trade.base.IBillOperate.OP_EDIT);
			// 执行公式表头公式
			getBillCardPanelWrapper().getBillCardPanel().execHeadEditFormulas();
			isAdd = true;
		} else {
			getBillUI().showErrorMessage("操作失败,当前登录者没有进行人员绑定");
			return;
		}
	}

	@Override
	protected void onBoSave() throws Exception {

		// 提示
		int result = getBillUI().showYesNoMessage("是否制单完成并打印?");
		// 如果选择Yes
		if (result == 4) {
			// 如果结果为yes 设置： 类型是合并订单，单据状态是完成，发运状态是待发运
			getBillCardPanelWrapper().getBillCardPanel()
					.getHeadItem("billtype").setValue(new Integer(4));
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"vbillstatus").setValue(new Integer(1));
			getBillCardPanelWrapper().getBillCardPanel()
					.getHeadItem("fyd_fyzt").setValue(new Integer(0));
			this.onSave(getBillUI().getVOFromUI());
		}
		/*
		 * /////////////以下代码是正常订单流程 现在没有 只有合并订单的流程////////////////// // else { // //
		 * 提示 // int result = getBillUI().showYesNoCancelMessage("是否完成制单?"); // //
		 * No // if (result == 8) { // // 如果是NO 类型是销售单，单据状态是未完成 //
		 * getBillCardPanelWrapper().getBillCardPanel().getHeadItem( //
		 * "billtype").setValue(new Integer(1)); //
		 * getBillCardPanelWrapper().getBillCardPanel().getHeadItem( //
		 * "vbillstatus").setValue(new Integer(0)); // onSave(); // } // // Yes //
		 * if (result == 4) { // // 如果结果为是 类型是销售单，单据状态是完成 //
		 * getBillCardPanelWrapper().getBillCardPanel().getHeadItem( //
		 * "billtype").setValue(new Integer(1)); //
		 * getBillCardPanelWrapper().getBillCardPanel().getHeadItem( //
		 * "vbillstatus").setValue(new Integer(1)); //
		 * getBillCardPanelWrapper().getBillCardPanel().getHeadItem( //
		 * "fyd_fyzt").setValue(new Integer(0)); // onSave(); // } // }
		 * ///////////////////////////////////////////////////////////
		 */
	}

	IUAPQueryBS iuap = (IUAPQueryBS) NCLocator.getInstance().lookup(
			IUAPQueryBS.class.getName());

	// 把保存方法给提出来，修改getBillUI().getVOFromUI();
	private void onSave(AggregatedValueObject billVO) throws Exception {
		getButtonManager().getButton(nc.ui.wds.w80060204.cfButtun.ICfButtun.zj)
				.setEnabled(true);
		// 调用接口进行保存和回写
		Iw80060204 iw = (Iw80060204) NCLocator.getInstance().lookup(
				Iw80060204.class.getName());
		iw.saveFyd(billVO);

		// 打印
		onBoPrint();
		getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
		// onBoRefresh();

	}

	/*
	 * 删除方法 @Override protected void onBoDelete() throws Exception { // TODO
	 * Auto-generated method stub // 界面没有数据或者有数据但是没有选中任何行 if
	 * (getBufferData().getCurrentVO() == null) return;
	 * 
	 * if (MessageDialog.showOkCancelDlg(getBillUI(),
	 * nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
	 * "UPPuifactory-000064")/* @res "档案删除"
	 */
	/*
	 * , nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
	 * "UPPuifactory-000065")/* @res "是否确认删除该基本档案?"
	 */

	/*
	 * * , MessageDialog.ID_CANCEL) != UIDialog.ID_OK) return;
	 * 
	 * AggregatedValueObject modelVo = getBufferData().getCurrentVO();
	 * 
	 * TbFydnewVO fydvo = (TbFydnewVO) modelVo.getParentVO(); // 把pk合并标识拆开取出数组
	 * String[] pk = fydvo.getPk_mergelogo().split(",");
	 * 
	 * if (null != pk && pk.length > 0) { for (int i = 0; i < pk.length; i++) {
	 * String sql = "select count(csourcebillhid) from tb_outgeneral_b where
	 * csourcebillhid = '" + pk[i] + "'"; // 根据主键查询是否有生成出库单 ArrayList list =
	 * (ArrayList) iuap.executeQuery(sql, new ArrayListProcessor()); Object[]
	 * results = (Object[]) list.get(0); if (null != results[0] &&
	 * !"".equals(results[0]) && Integer.parseInt(results[0].toString()) > 0) {
	 * myClientUI.showErrorMessage("删除失败!该单据已生成出库单据,请先删除出库单据"); return; } } }
	 * List pman = new ArrayList(); pman.add(getBillUI().getUserObject());
	 * getBusinessAction().delete(modelVo, getUIController().getBillType(),
	 * getBillUI()._getDate().toString(), pman); if (PfUtilClient.isSuccess()) { //
	 * 清除界面数据 getBillUI().removeListHeadData(getBufferData().getCurrentRow());
	 * if (getUIController() instanceof ISingleController) { ISingleController
	 * sctl = (ISingleController) getUIController(); if (!sctl.isSingleDetail())
	 * getBufferData().removeCurrentRow(); } else {
	 * getBufferData().removeCurrentRow(); } } if
	 * (getBufferData().getVOBufferSize() == 0)
	 * getBillUI().setBillOperate(IBillOperate.OP_INIT); else
	 * getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
	 * getBufferData().setCurrentRow(getBufferData().getCurrentRow()); }
	 */

	@Override
	protected void onBoCancel() throws Exception {
		getButtonManager().getButton(nc.ui.wds.w80060204.cfButtun.ICfButtun.zj)
				.setEnabled(true);
		// 判断是否为新增后的 取消
		if (isAdd) {
			getBufferData().clear();
			getBillUI().setBillOperate(IBillOperate.OP_INIT);
			getBillUI().initUI();
			isAdd = false;
		} else {
			super.onBoCancel();
		}

	}

	/**
	 * 把模板中选中的VO 进行转换成发运单的VO
	 * 
	 * @param vos
	 *            页面选中的聚合VO
	 * @return 发运单的聚合VO
	 * @throws BusinessException
	 */
	private MyBillVO changeReqSaleOrderYtoFyd(AggregatedValueObject[] vos)
			throws BusinessException {
		MyBillVO myBillVO = new MyBillVO();
		// 获取查询数据库对象
		IUAPQueryBS iQuery = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		int num = 0; // 为了计算数组的长度
		SaleorderHVO salehVO = null;
		TbFydnewVO fydnewVO = new TbFydnewVO();
		// 子表信息数组集合
		List<SaleorderBVO[]> salevoList = new ArrayList<SaleorderBVO[]>();
		// 如果聚合VO的长度大于1 说明是合并订单操作
		if (vos.length > 1) {
			String vercode = null; // 订单号
			String saleid = null; // 合并后的销售订单主键
			String bustype = null; // 业务类型 中文 非主键
			// 获取第一次查询后的数据缓存
			SaleOrderVO[] salevo = saleOrderDlg.getSaleVO();
			// 循环聚合VO的长度
			for (int i = 0; i < vos.length; i++) {
				// 把第一个主表提取出来
				salehVO = (SaleorderHVO) vos[i].getParentVO();
				if (null != salehVO.getCsaleid() // 判断销售主键是否为空
						&& !"".equals(salehVO.getCsaleid())) {
					if (null == saleid || "".equals(saleid)) { // 判断主键是否为空，如果为空说明是第一次赋值
						saleid = salehVO.getCsaleid();
					} else { // 如果有值 进行累加，中间用逗号来区分，方便后面来拆分
						saleid = saleid + "," + salehVO.getCsaleid();
					}
				}
				// 判断订单号是否为空，同上
				if (null != salehVO.getVreceiptcode()
						&& !"".equals(salehVO.getVreceiptcode())) {
					if (null == vercode || "".equals(vercode)) {
						vercode = salehVO.getVreceiptcode();
					} else {
						vercode = vercode + "," + salehVO.getVreceiptcode();
					}
				}
				// 获取当前主表中的销售主键(不是累加后的主键)
				String csaleid = salehVO.getCsaleid();
				// 循环缓存
				for (int j = 0; j < salevo.length; j++) {
					// 获取缓存中的销售主键
					String salehid = salevo[j].getHeadVO().getCsaleid();
					// 判断当前的销售主键和缓存中的销售主键是否相同，
					if (csaleid.equals(salehid)) {
						// 如果两者主键相同就把缓存中对应主键的子表信息数组提取出来放到 子表信息集合中
						salevoList.add((SaleorderBVO[]) salevo[j]
								.getChildrenVO());
						// 获取当前子表信息的长度 如果有多个 进行累加 为了下面的数组初始化
						num = num + salevo[j].getChildrenVO().length;
						break;
					}
				}
				// 获取业务类型是否为空
				if (null != salehVO.getCbiztype()
						&& !"".equals(salehVO.getCbiztype())) {
					String sql = "select businame from bd_busitype where pk_busitype = '"
							+ salehVO.getCbiztype() + "'";
					ArrayList list = (ArrayList) iQuery.executeQuery(sql,
							new ArrayListProcessor());
					// 判断结果是否为空
					if (list != null && list.size() > 0) {
						Object a[] = (Object[]) list.get(0);
						if (a != null && a.length > 0 && a[0] != null) {
							if (null == bustype)// 如果第一次为空进行赋值
								bustype = a[0].toString();
							else
								// 不为空 添加逗号进行区分
								bustype = bustype + "," + a[0].toString();
						}
					}
				}
			}
			// 把累加后的订单号和主键赋值给当前的对象
			fydnewVO.setVbillno(vercode); // 单据号
			fydnewVO.setCsaleid(saleid); // 销售主表主键
			fydnewVO.setFyd_busitype(bustype); // 业务类型
			// 发货站
			fydnewVO.setSrl_pk(CommonUnit.getStordocName(ClientEnvironment
					.getInstance().getUser().getPrimaryKey()));
			// 子表信息集合
			List<SaleorderBVO> salelist = new ArrayList<SaleorderBVO>();
			// 循环子表信息数组集合，把集合中的数组给循环出来，放到子表信息集合中，下面就能转换成数组了
			for (int i = 0; i < salevoList.size(); i++) {
				SaleorderBVO[] tmp = salevoList.get(i);
				for (int j = 0; j < tmp.length; j++) {
					salelist.add(tmp[j]);
				}
			}
			SaleorderBVO[] saleb = new SaleorderBVO[num]; // 实例一个子表信息数组，其长度就是上面进行多次累加的
			salelist.toArray(saleb); // 数组转换
			// 把转换后的子表数组放到当前的聚合VO中
			vos[0].setChildrenVO(saleb);
		}
		/*
		 * // ///////////以下代码 为 原计划 该方法有两种操作 一种为合并订单 一种为正常订单 //
		 * 现在只有合并订单一种////////////////////// // else { // 如果不是合并订单的操作 就是正常赋值了 //
		 * salehVO = (SaleorderHVO) vos[0].getParentVO(); // if (null !=
		 * salehVO.getVreceiptcode() // &&
		 * !"".equals(salehVO.getVreceiptcode())) { //
		 * fydnewVO.setVbillno(salehVO.getVreceiptcode()); // 单据号 // } // if
		 * (null != salehVO.getCsaleid() // && !"".equals(salehVO.getCsaleid())) { //
		 * fydnewVO.setCsaleid(salehVO.getCsaleid()); // 主键 // } // //
		 * 循环缓存中的子表信息 // for (int i = 0; i < saleOrderDlg.getSaleVO().length;
		 * i++) { // if (salehVO.getCsaleid().equals( //
		 * saleOrderDlg.getSaleVO()[i].getHeadVO().getCsaleid())) { //
		 * vos[0].setChildrenVO(saleOrderDlg.getSaleVO()[i] // .getBodyVOs()); //
		 * break; // } // } // } //
		 * ///////////////////////////////////////////////////////////////////////////////////////////////////////
		 */
		// 这里为公用部分 因为在合并与正常的，下面的信息是不变的
		if (null != salehVO.getCcustomerid()
				&& !"".equals(salehVO.getCcustomerid())) {
			fydnewVO.setPk_kh(salehVO.getCcustomerid()); // 客户主键
		}
		if (null != salehVO.getVreceiveaddress()
				&& !"".equals(salehVO.getVreceiveaddress())) {
			fydnewVO.setFyd_shdz(salehVO.getVreceiveaddress()); // 收货地址
		}
		if (null != salehVO.getCemployeeid()
				&& !"".equals(salehVO.getCemployeeid())) {
			fydnewVO.setPk_psndoc(salehVO.getCemployeeid()); // 业务员
		}
		if (null != salehVO.getCbiztype() && !"".equals(salehVO.getCbiztype())) {
			fydnewVO.setPk_busitype(salehVO.getCbiztype()); // 业务类型
		}
		if (null != salehVO.getVnote() && !"".equals(salehVO.getVnote())) {
			fydnewVO.setFyd_bz(salehVO.getVnote()); // 备注
		}
		if (null != salehVO.getCdeptid() && !"".equals(salehVO.getCdeptid())) {
			fydnewVO.setCdeptid(salehVO.getCdeptid()); // 部门
		}
		if (null != salehVO.getAttributeValue("daudittime")
				&& !"".equals(salehVO.getAttributeValue("daudittime"))) {
			fydnewVO.setFyd_spsj(salehVO.getAttributeValue("daudittime")
					.toString()); // 审批时间
		}
		if (null != salehVO.getVdef18() && !"".equals(salehVO.getVdef18())) {
			UFDouble gls = null;
			try {
				gls = new UFDouble(salehVO.getVdef18());
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				gls = null;
			}
			fydnewVO.setFyd_yslc(gls); // 公里数
		}
		if (null != salehVO.getDbilldate()
				&& !"".equals(salehVO.getDbilldate())) {
			fydnewVO.setFyd_xhsj(salehVO.getDbilldate()); // 需货时间
			// 这里进行转换的是销售主表中的单据日期字段
		}
		// 设置运货方式
		fydnewVO.setFyd_yhfs("汽运");

		// 制单日期
		fydnewVO.setDmakedate(_getDate());
		fydnewVO.setFyd_dby(ClientEnvironment.getInstance().getUser()
				.getPrimaryKey()); // 设置调度员
		fydnewVO.setVoperatorid(ClientEnvironment.getInstance().getUser()
				.getPrimaryKey()); // 设置制单人
		fydnewVO.setBilltype(new Integer(1));
		myBillVO.setParentVO(fydnewVO);

		if (null != vos[0].getChildrenVO() && vos[0].getChildrenVO().length > 0) {
			SaleorderBVO[] salebVO = (SaleorderBVO[]) vos[0].getChildrenVO();
			TbFydmxnewVO[] fydmxnewVO = new TbFydmxnewVO[salebVO.length];
			// 获取缓存 转换子表其他的信息
			for (int i = 0; i < salebVO.length; i++) {
				SaleorderBVO salebvo = salebVO[i];
				TbFydmxnewVO fydmxnewvo = new TbFydmxnewVO();
				if (null != salebvo.getCorder_bid()
						&& !"".equals(salebvo.getCorder_bid())) {
					fydmxnewvo.setCorder_bid(salebvo.getCorder_bid()); // 销售附表主键
				}

				if (null != salebvo.getNnumber()
						&& !"".equals(salebvo.getNnumber())) {
					fydmxnewvo.setCfd_yfsl(salebvo.getNnumber()); // 应发数量
				}
				if (null != salebvo.getNpacknumber()
						&& !"".equals(salebvo.getNpacknumber())) {
					fydmxnewvo.setCfd_xs(salebvo.getNpacknumber()); // 箱数
				}
				if (null != salebvo.getCrowno()
						&& !"".equals(salebvo.getCrowno())) {
					fydmxnewvo.setCrowno(salebvo.getCrowno()); // 行号
				}
				if (null != salebvo.getBlargessflag()
						&& !"".equals(salebvo.getBlargessflag())) {
					fydmxnewvo.setBlargessflag(salebvo.getBlargessflag()); // 是否赠品
				}
				if (null != salebvo.getCunitid()
						&& !"".equals(salebvo.getCunitid())) {
					fydmxnewvo.setCfd_dw(salebvo.getCunitid()); // 单位
				}

				fydmxnewVO[i] = fydmxnewvo;

			}
			myBillVO.setChildrenVO(fydmxnewVO);
		}

		return myBillVO;
	}
}
