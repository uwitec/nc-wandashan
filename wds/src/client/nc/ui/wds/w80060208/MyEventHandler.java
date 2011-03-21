package nc.ui.wds.w80060208;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.itf.wds.w8000.Iw8000;
import nc.itf.wds.w80060208.Iw80060208;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.BillCardPanelWrapper;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.BusinessAction;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.businessaction.IBusinessController;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.pub.CardPanelPRTS;
import nc.ui.trade.pub.ListPanelPRTS;
import nc.ui.wds.w80060208.MyClientUI;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;
import nc.vo.to.pub.ConstVO;
import nc.vo.wds.w80060208.MyBillVO;
import nc.vo.wds.w80060406.TbFydmxnewVO;
import nc.vo.wds.w80060406.TbFydnewVO;
import nc.vo.wds.w80060604.SoSaleVO;

import nc.ui.scm.pub.query.SCMQueryConditionDlg;
import nc.vo.pub.bill.BillRendererVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;

/**
 * 
 * 该类是AbstractMyEventHandler抽象类的实现类， 主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
 * 
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	MyClientUI myClientUI = null;

	private SaleOrderDlg saleOrderDlg = null; // 查询方法的类

	boolean isAdd = false;
	private boolean dbbool = true;

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
		myClientUI = (MyClientUI) billUI;

	}

	@Override
	protected IBusinessController createBusinessAction() {
		// TODO Auto-generated method stub
		switch (getUIController().getBusinessActionType()) {
		case IBusinessActionType.PLATFORM:
			return new BusinessAction(getBillUI());
		case IBusinessActionType.BD:
			return new W80060208Action(getBillUI());
		default:
			return new BusinessAction(getBillUI());
		}
	}

	@Override
	protected void onscpz() throws Exception {
		// TODO Auto-generated method stub

		// 订单号主键
		String csaleid = getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("csaleid").getValue();
		// 拆分单据主键
		String fyd_pk = getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("fyd_pk").getValue();
		// 查询对象
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		// 修改对象
		IVOPersistence iVOPersistence = (IVOPersistence) NCLocator
				.getInstance().lookup(IVOPersistence.class.getName());
		// 判断当前单据是否是最后一张拆分单据
		StringBuffer sbsql = new StringBuffer(
				"select fyd_pk from  (select * from tb_fydnew where csaleid='");
		sbsql.append(csaleid);
		sbsql
				.append("' and dr=0 order by splitvbillno desc) where rownum<2 and dr=0 ");
		ArrayList tbfydvos = (ArrayList) query.executeQuery(sbsql.toString(),
				new ArrayListProcessor());
		if (null != tbfydvos && tbfydvos.size() > 0) {
			Object[] tbfydvo = (Object[]) tbfydvos.get(0);
			if (null != tbfydvo && tbfydvo.length > 0 && null != tbfydvo[0]) {
				if (!fyd_pk.equals(tbfydvo[0].toString())) {
					getBillUI().showErrorMessage("本张单据不是最后一张拆分单据，请选则最后一张单据！");
					return;
				}
			}
		} else {
			getBillUI().showErrorMessage("没有单据！");
			return;
		}
		// 判断拆分是否结束
		SoSaleVO ob = (SoSaleVO) query.retrieveByPK(SoSaleVO.class, csaleid);
		if (null != ob) {
			if (null != ob.getVdef6() && !"".equals(ob.getVdef6())) {
				if ("2".equals(ob.getVdef6())) {
					getBillUI().showErrorMessage("单据拆分已经结束，请不要重复点击！");
					return;
				}
			}
		}
		// 单据拆分结束
		if (null != ob) {
			ob.setVdef6("2");
		}
		iVOPersistence.updateVO(ob);

		TbFydnewVO tbFydnewVO = (TbFydnewVO) getBufferData().getCurrentVO()
				.getParentVO();
		tbFydnewVO.setFyd_splitstatus(2);
		iVOPersistence.updateVO(tbFydnewVO);

	}

	@Override
	protected void oncfzj() throws Exception {
		// TODO Auto-generated method stub
		// 实例查询类
		saleOrderDlg = new SaleOrderDlg(myClientUI);

		// 调用方法 获取查询后的聚合VO
		AggregatedValueObject[] vos = saleOrderDlg.getReturnVOs(
				ClientEnvironment.getInstance().getCorporation()
						.getPrimaryKey(), ClientEnvironment.getInstance()
						.getUser().getPrimaryKey(), "0208",
				ConstVO.m_sBillDRSQ, "80060208", "8006020801", myClientUI);

		// 判断是否对查询模板有进行操作
		if (null == vos || vos.length == 0) {
			// getBillUI().showWarningMessage("您没有进行操作!");
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
				nc.ui.wds.w80060208.cfButtun.ICfButtun.cfzj).setEnabled(false);
		getButtonManager().getButton(
				nc.ui.wds.w80060208.cfButtun.ICfButtun.scpz).setEnabled(false);
		super.onBoEdit();
		// getBillUI().setBillOperate( // 因为点击自定义按钮后就会切到正常按钮模式 所以设置状态
		// nc.ui.trade.base.IBillOperate.OP_EDIT);
		// 执行公式表头公式
		getBillCardPanelWrapper().getBillCardPanel().execHeadEditFormulas();
		isAdd = true;
		showZeroLikeNull(false);
	}

	private MyBillVO changeReqSaleOrderYtoFyd(AggregatedValueObject[] vos) {
		MyBillVO myBillVO = new MyBillVO();
		int num = 0; // 为了计算数组的长度
		SaleorderHVO salehVO = null;
		TbFydnewVO fydnewVO = new TbFydnewVO();
		// 子表信息数组集合
		List<SaleorderBVO[]> salevoList = new ArrayList<SaleorderBVO[]>();
		// 如果聚合VO的长度大于1 说明是合并订单操作
		// if (vos.length > 1) {
		// String vercode = null; // 订单号
		// String saleid = null; // 销售订单主键
		// // 获取第一次查询后的数据缓存
		// SaleOrderVO[] salevo = saleOrderDlg.getSaleVO();
		// // 循环聚合VO的长度
		// for (int i = 0; i < vos.length; i++) {
		// // 把第一个主表提取出来
		// salehVO = (SaleorderHVO) vos[i].getParentVO();
		// if (null != salehVO.getCsaleid() // 判断销售主键是否为空
		// && !"".equals(salehVO.getCsaleid())) {
		// if (null == saleid || "".equals(saleid)) { // 判断主键是否为空，如果为空说明是第一次赋值
		// saleid = salehVO.getCsaleid();
		// } else { // 如果有值 进行累加，中间用逗号来区分，方便后面来拆分
		// saleid = saleid + "," + salehVO.getCsaleid();
		// }
		// }
		// // 判断订单号是否为空，同上
		// if (null != salehVO.getVreceiptcode()
		// && !"".equals(salehVO.getVreceiptcode())) {
		// if (null == vercode || "".equals(vercode)) {
		// vercode = salehVO.getVreceiptcode();
		// } else {
		// vercode = vercode + "," + salehVO.getVreceiptcode();
		// }
		// }
		// // 获取当前主表中的销售主键(不是累加后的主键)
		// String csaleid = salehVO.getCsaleid();
		// // 循环缓存
		// for (int j = 0; j < salevo.length; j++) {
		// // 获取缓存中的销售主键
		// String salehid = salevo[j].getHeadVO().getCsaleid();
		// // 判断当前的销售主键和缓存中的销售主键是否相同，
		// if (csaleid.equals(salehid)) {
		// // 如果两者主键相同就把缓存中对应主键的子表信息数组提取出来放到 子表信息集合中
		// salevoList.add((SaleorderBVO[]) salevo[j]
		// .getChildrenVO());
		// // 获取当前子表信息的长度 如果有多个 进行累加 为了下面的数组初始化
		// num = num + salevo[j].getChildrenVO().length;
		// break;
		// }
		// }
		// }
		// // 把累加后的订单号和主键赋值给当前的对象
		// fydnewVO.setVbillno(vercode); // 单据号
		// fydnewVO.setCsaleid(saleid); // 销售主表主键
		// // 子表信息集合
		// List<SaleorderBVO> salelist = new ArrayList<SaleorderBVO>();
		// // 循环子表信息数组集合，把集合中的数组给循环出来，放到子表信息集合中，下面就能转换成数组了
		// for (int i = 0; i < salevoList.size(); i++) {
		// SaleorderBVO[] tmp = salevoList.get(i);
		// for (int j = 0; j < tmp.length; j++) {
		// salelist.add(tmp[j]);
		// }
		// }
		// SaleorderBVO[] saleb = new SaleorderBVO[num]; //
		// 实例一个子表信息数组，其长度就是上面进行多次累加的
		// salelist.toArray(saleb); // 数组转换
		// // 把转换后的子表数组放到当前的聚合VO中
		// vos[0].setChildrenVO(saleb);
		// } else { // 如果不是合并订单的操作 就是正常赋值了
		salehVO = (SaleorderHVO) vos[0].getParentVO();
		// 拆分单据号
		StringBuffer sbsql = new StringBuffer("select h.splitvbillno from "
				+ "(select splitvbillno from tb_fydnew " + "where vbillno = '");
		if (null != salehVO.getVreceiptcode()
				&& !"".equals(salehVO.getVreceiptcode())) {
			fydnewVO.setVbillno(salehVO.getVreceiptcode()); // 单据号
			fydnewVO.setFyd_ddh(salehVO.getVreceiptcode());
			sbsql.append(salehVO.getVreceiptcode());
		}
		sbsql.append("' and dr=0 order by splitvbillno desc) h where rownum=1");
		// 获取访问数据库对象
		IUAPQueryBS IUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		// 查询数据库中的结果 结果是以Object为对象的数组
		ArrayList list = null;
		try {
			list = (ArrayList) IUAPQueryBS.executeQuery(sbsql.toString(),
					new ArrayListProcessor());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 判断结果是否为空
		String spno = null;
		if (list != null && list.size() > 0) {
			Object a[] = (Object[]) list.get(0);
			if (a != null && a.length > 0 && a[0] != null) {
				// 如果不为空进行赋值
				spno = a[0].toString();
			}
		}
		// 整数类型的拆分单据号
		int numno = 0;
		// 判断获得的订单号是否为空
		if (spno != null && !"".equals(spno)) {
			// 如果不为空进行字符串截取，截取到最后出现"-"到最后的值
			String n = spno.substring(spno.lastIndexOf("-") + 2, spno.length())
					.toString();
			numno = Integer.parseInt(n.trim());
			numno += 1;
			String splitvbillno = numno + "";
			while (splitvbillno.length() < 2) {
				splitvbillno = "0" + splitvbillno;
			}
			fydnewVO.setSplitvbillno(salehVO.getVreceiptcode() + "-S"
					+ splitvbillno);
		} else {
			fydnewVO.setSplitvbillno(salehVO.getVreceiptcode() + "-S01");
		}

		if (null != salehVO.getCsaleid() && !"".equals(salehVO.getCsaleid())) {
			fydnewVO.setCsaleid(salehVO.getCsaleid()); // 销售主键
		}
		// 循环缓存中的子表信息
		for (int i = 0; i < saleOrderDlg.getSaleVO().length; i++) {
			if (salehVO.getCsaleid().equals(
					saleOrderDlg.getSaleVO()[i].getHeadVO().getCsaleid())) {
				vos[0].setChildrenVO(saleOrderDlg.getSaleVO()[i].getBodyVOs());
				break;
			}
		}
		// }
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
		// 发货站
		fydnewVO.setSrl_pk("1021A91000000004YZ0P");
		if (null != salehVO.getDapprovedate()
				&& !"".equals(salehVO.getDapprovedate())) {
			fydnewVO.setDapprovedate(salehVO.getDapprovedate()); // 审批日期
		}
		if (null != salehVO.getAttributeValue("daudittime")) {
			fydnewVO.setFyd_spsj(salehVO.getAttributeValue("daudittime")// 审批时间
					.toString());
		}
		if (null != salehVO.getCsaleid() && !"".equals(salehVO.getCsaleid())) {
			fydnewVO.setCsaleid(salehVO.getCsaleid()); // 销售主键
		}
		if (null != salehVO.getVdef16() && !"".equals(salehVO.getVdef16())) {
			fydnewVO.setFyd_splitstatus(Integer.parseInt(salehVO.getVdef16()));// 拆分类型
		}
		fydnewVO.setFyd_dby(ClientEnvironment.getInstance().getUser()
				.getPrimaryKey()); // 设置调度员
		fydnewVO.setVoperatorid(ClientEnvironment.getInstance().getUser()
				.getPrimaryKey()); // 设置制单人
		fydnewVO.setBilltype(new Integer(3));
		fydnewVO.setFyd_xhsj(salehVO.getDbilldate());// 需货时间
		fydnewVO.setDmakedate(new UFDate(new Date()));// 单据日期

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
					fydmxnewvo.setCfd_ysyfsl(salebvo.getNnumber()); // 应发数量
					fydmxnewvo.setCfd_yfsl(salebvo.getNnumber());
					fydmxnewvo.setCfd_sysl(new UFDouble(0));
				}
				if (null != salebvo.getNpacknumber()
						&& !"".equals(salebvo.getNpacknumber())) {
					fydmxnewvo.setCfd_ysxs(salebvo.getNpacknumber()); // 箱数
					fydmxnewvo.setCfd_xs(salebvo.getNpacknumber());
					fydmxnewvo.setCfd_syfsl(new UFDouble(0));
				}
				if (null != salebvo.getCrowno()
						&& !"".equals(salebvo.getCrowno())) {
					fydmxnewvo.setCrowno(salebvo.getCrowno()); // 行号
				}
				if (null != salebvo.getBlargessflag()
						&& !"".equals(salebvo.getBlargessflag())) {
					fydmxnewvo.setBlargessflag(salebvo.getBlargessflag()); // 是否赠品
				}

				fydmxnewVO[i] = fydmxnewvo;

			}
			myBillVO.setChildrenVO(fydmxnewVO);
		}

		return myBillVO;
	}

	@Override
	protected void onBoSave() throws Exception {

		AggregatedValueObject mybillVO = getBillUI().getVOFromUI();
		boolean isnotnull = false;
		for (int i = 0; i < mybillVO.getChildrenVO().length; i++) {
			TbFydmxnewVO tbFydmxnewVO = ((TbFydmxnewVO[]) mybillVO
					.getChildrenVO())[i];
			if (null != tbFydmxnewVO) {
				if (null != tbFydmxnewVO.getCfd_xs()
						&& 0 != tbFydmxnewVO.getCfd_xs().doubleValue()) {
					isnotnull = true;
				}
			}
		}
		if (!isnotnull) {
			getBillUI().showErrorMessage("拆分数全部为零或全部为空不能保存！");
			return;
		}
		// 修改按钮状态
		getButtonManager().getButton(
				nc.ui.wds.w80060208.cfButtun.ICfButtun.scpz).setEnabled(true);
		// 获取页面聚合VO
		AggregatedValueObject billVO = getBillUI().getVOFromUI();
		String[] saleid = null;
		// 把销售主键拆分
		saleid = ((TbFydnewVO) billVO.getParentVO()).getCsaleid().split(",");
		// //表体行数
		// int rowNum =
		// getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		//		
		// //剩余数量
		// String
		// cfd_sysl=getBillCardPanelWrapper().getBillCardPanel().getBillModel()
		// //剩余辅数量

		// // 如果数组长度大于1说明是合并订单的操作
		// if (saleid.length > 1) {
		// // 提示
		// int result = getBillUI().showYesNoMessage("是否完成制单?");
		// // 如果选择Yes
		// if (result == 4) {
		// // 如果结果为是 类型是销售单，单据状态是完成
		// getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
		// "billtype").setValue(new Integer(3));
		// getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
		// "vbillstatus").setValue(new Integer(1));
		// getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
		// "fyd_fyzt").setValue(new Integer(0));
		// // 调用合并保存方法
		// onSplitSave();
		// }
		// } else {

		// 判断是新增还是修改
		if (isAdd) {
			// 提示
			int result = getBillUI().showOkCancelMessage("是否保存并打印?");
			int iprintcount = 0;
			if (null != getBillCardPanelWrapper().getBillCardPanel()
					.getHeadTailItem("iprintcount")
					&& !"".equals(getBillCardPanelWrapper().getBillCardPanel()
							.getHeadTailItem("iprintcount").getValue())) {
				iprintcount = Integer.parseInt(getBillCardPanelWrapper()
						.getBillCardPanel().getHeadTailItem("iprintcount")
						.getValue());
			}
			iprintcount++;
			// Yes
			if (result == 1) {
				// 如果结果为是 类型是销售单，单据状态是完成
				getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
						"billtype").setValue(new Integer(3));
				getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
						"vbillstatus").setValue(new Integer(1));
				getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
						"fyd_fyzt").setValue(new Integer(0));
				// getBillCardPanelWrapper().getBillCardPanel().getHeadTailItem(
				// "iprintcount").setValue(iprintcount);
				getBillCardPanelWrapper().getBillCardPanel().setHeadItem(
						"iprintdate", new UFDate(new Date()));
				onSave();
				onBoPrint();
			}
		} else {
			onSave();
		}

		// }

	}

	// 合并订单的保存方法
	private void onSplitSave() throws Exception {
		// 更改增加按钮状态
		getButtonManager().getButton(nc.ui.wds.w80060204.cfButtun.ICfButtun.zj)
				.setEnabled(true);
		// 获取页面聚合VO
		AggregatedValueObject billVO = getBillUI().getVOFromUI();
		AggregatedValueObject checkVO = null;
		// 设置TS
		setTSFormBufferToVO(billVO);
		String[] saleid = null; // 销售订单主键
		String[] vercode = null; // 订单号主键
		// 获取聚合VO中的主表信息
		TbFydnewVO fydVO = (TbFydnewVO) billVO.getParentVO();
		// 获取聚合VO中子表信息
		TbFydmxnewVO[] fydmxVO = (TbFydmxnewVO[]) billVO.getChildrenVO();
		// 声明一个子表信息的集合
		List<TbFydmxnewVO> fydmxList = null;
		// 设置标识列
		fydVO.setMergelogo(fydVO.getVbillno());
		saleid = fydVO.getCsaleid().split(","); // 进行截取销售订单主键
		vercode = fydVO.getVbillno().split(","); // 截取订单号主键
		// 循环销售订单主键的数组
		for (int i = 0; i < saleid.length; i++) {
			// 把截取后的销售订单主键再赋值给当前的主表VO中，这样子主表VO中的销售主键和订单号就给差分开了
			fydVO.setCsaleid(saleid[i]);
			fydVO.setVbillno(vercode[i]); // 订单号 同上
			// 把修改后的主表VO再装回聚合VO中
			billVO.setParentVO(fydVO);
			// 实例子表信息集合
			fydmxList = new ArrayList<TbFydmxnewVO>();
			// 循环子表数组，因为是多个订单的子表信息都存在了一起，所以要拆分开来，进行分别存储
			for (int j = 0; j < fydmxVO.length; j++) {
				// 判断 外层循环的是销售主表的的主键，在子表当中也存有主表的主键，所以判断这个两个主键是否相等
				if (saleid[i].equals(fydmxVO[j].getCsaleid())) {
					// 如果相等就把信息提取出来存储到 子表信息集合里面去
					fydmxList.add(fydmxVO[j]);
				}
			}
			// 定义一个子表信息的数组，它的长度和提取出来的子表信息的长度是一样的，因为下面要进行数组转换
			TbFydmxnewVO[] tmpVO = new TbFydmxnewVO[fydmxList.size()];
			// 数组转换后直接存储到聚合VO中
			billVO.setChildrenVO(fydmxList.toArray(tmpVO));
			// 因为没有获取checkVO 所以就把billVO 赋值给 checkVO
			checkVO = billVO;
			// //////////////调取保存方法；；；；；；；；；；；；
			getBusinessAction()
					.save(billVO, getUIController().getBillType(),
							_getDate().toString(), getBillUI().getUserObject(),
							checkVO);
		}
		// 进行数据清空，按钮初始化
		getBufferData().clear();
		getBillUI().setBillOperate(IBillOperate.OP_INIT);
		getBillUI().initUI();
	}

	// 把保存方法给提出来，修改getBillUI().getVOFromUI();
	private void onSave() throws Exception {
		getButtonManager().getButton(
				nc.ui.wds.w80060208.cfButtun.ICfButtun.cfzj).setEnabled(true);

		// 修改原有的保存方法 把checkVOFromUI 修改获取所有的 其他的不变
		AggregatedValueObject billVO = getBillUI().getVOFromUI();
		setTSFormBufferToVO(billVO);
		AggregatedValueObject checkVO = getBillUI().getVOFromUI();
		setTSFormBufferToVO(checkVO);
		// 但据状态
		((TbFydnewVO) billVO.getParentVO()).setVbillstatus(1);
		// 发运状态
		((TbFydnewVO) billVO.getParentVO()).setFyd_fyzt(0);
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

		ArrayList params = new ArrayList();
		// 判断是否有存盘数据
		try {
			if (billVO.getParentVO() == null
					&& (billVO.getChildrenVO() == null || billVO
							.getChildrenVO().length == 0)) {
				isSave = false;
			} else {
				// 参数设置

				String csaleid = getBillCardPanelWrapper().getBillCardPanel()
						.getHeadItem("csaleid").getValue();
				// 拆分结束，剩下货物自动生成一张拆分单
				String fyd_splitend = getBillCardPanelWrapper()
						.getBillCardPanel().getHeadItem("fyd_splitend")
						.getValue();
				// 表头VO
				TbFydnewVO tbFydnewVO = (TbFydnewVO) getBillUI().getVOFromUI()
						.getParentVO();
				// 表体VO
				TbFydmxnewVO[] tbFydmxnewVO = (TbFydmxnewVO[]) getBillUI()
						.getVOFromUI().getChildrenVO();
				//
				// Iw8000 iw = (Iw80060208) NCLocator.getInstance().lookup(
				// Iw80060208.class.getName());
				params.add(getBillUI().getUserObject());
				// params.add(iw);
				params.add(csaleid);
				params.add(fyd_splitend);
				params.add(tbFydnewVO);
				params.add(tbFydmxnewVO);
				// getBillUI().showErrorMessage("错误提示1");
				// Iw80060208 iw = (Iw80060208) NCLocator.getInstance().lookup(
				// Iw80060208.class.getName());
				// AggregatedValueObject retVo = iw.saveBD80060208(billVO,
				// params);
				if (getBillUI().isSaveAndCommitTogether())
					billVO = getBusinessAction().saveAndCommit(billVO,
							getUIController().getBillType(),
							_getDate().toString(), getBillUI().getUserObject(),
							checkVO);
				else

					// write to database
					billVO = getBusinessAction().save(billVO,
							getUIController().getBillType(),
							_getDate().toString(), params, checkVO);
				// getBillUI().showErrorMessage("错误提示2");
			}

		} catch (Exception ex1) {
			ex1.printStackTrace();
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

		// String csaleid = getBillCardPanelWrapper().getBillCardPanel()
		// .getHeadItem("csaleid").getValue();
		// IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
		// IUAPQueryBS.class.getName());
		// SoSaleVO ob = (SoSaleVO) query.retrieveByPK(SoSaleVO.class, csaleid);
		// ob.setVdef6("1");
		// // 修改对象
		// IVOPersistence iVOPersistence = (IVOPersistence) NCLocator
		// .getInstance().lookup(IVOPersistence.class.getName());
		// iVOPersistence.updateVO(ob);
		// // 拆分结束，剩下货物自动生成一张拆分单
		// String fyd_splitend = getBillCardPanelWrapper().getBillCardPanel()
		// .getHeadItem("fyd_splitend").getValue();
		// // 判断是否还剩有货物
		// boolean isSurplus = false;
		// if ("true".equals(fyd_splitend)) {
		// // 表头VO
		// TbFydnewVO tbFydnewVO = (TbFydnewVO) getBufferData().getCurrentVO()
		// .getParentVO();
		// // 表体VO
		// TbFydmxnewVO[] tbFydmxnewVO = (TbFydmxnewVO[]) getBufferData()
		// .getCurrentVO().getChildrenVO();
		// for (int i = 0; i < tbFydmxnewVO.length; i++) {
		// double cfd_syfsl = tbFydmxnewVO[i].getCfd_syfsl().doubleValue();
		// if (cfd_syfsl != 0) {
		// isSurplus = true;
		// }
		// }
		// if (!isSurplus) {
		// tbFydnewVO.setFyd_splitstatus(2);
		// iVOPersistence.updateVO(tbFydnewVO);
		// }
		// tbFydnewVO.setFyd_pk(null);
		// tbFydnewVO.setFyd_splitstatus(2);
		// // 拆分单据号
		// String spno = tbFydnewVO.getSplitvbillno();
		// // 判断获得的订单号是否为空
		// if (spno != null && !"".equals(spno)) {
		// // 如果不为空进行字符串截取，截取到最后出现"-"到最后的值
		// // 非数字部分
		// String nonum = spno.substring(0, spno.lastIndexOf("-") + 2);
		// // 整数部分
		// String n = spno.substring(spno.lastIndexOf("-") + 2,
		// spno.length()).toString();
		// int numno = Integer.parseInt(n.trim());
		// numno += 1;
		// String splitvbillno = numno + "";
		// while (splitvbillno.length() < 2) {
		// splitvbillno = "0" + splitvbillno;
		// }
		// tbFydnewVO.setSplitvbillno(nonum + splitvbillno);
		// }
		// // 单据日期
		// tbFydnewVO.setDmakedate(new UFDate(new Date()));
		//
		// // 添加主表
		// String fyd_pk = "";
		// if (isSurplus) {
		// fyd_pk = iVOPersistence.insertVO(tbFydnewVO);
		// }
		//
		// // 剩余VO
		// ArrayList newTbFydmxnewVO = new ArrayList();
		// for (int i = 0; i < tbFydmxnewVO.length; i++) {
		// double cfd_syfsl = tbFydmxnewVO[i].getCfd_syfsl().doubleValue();
		// if (cfd_syfsl != 0) {
		// tbFydmxnewVO[i]
		// .setCfd_ysyfsl(tbFydmxnewVO[i].getCfd_sysl());
		// tbFydmxnewVO[i].setCfd_yfsl(tbFydmxnewVO[i].getCfd_sysl());
		// tbFydmxnewVO[i].setCfd_ysxs(tbFydmxnewVO[i].getCfd_syfsl());
		// tbFydmxnewVO[i].setCfd_xs(tbFydmxnewVO[i].getCfd_syfsl());
		// tbFydmxnewVO[i].setCfd_sysl(new UFDouble(0));
		// tbFydmxnewVO[i].setCfd_syfsl(new UFDouble(0));
		// tbFydmxnewVO[i].setCfd_pk(null);
		// tbFydmxnewVO[i].setFyd_pk(fyd_pk);
		// newTbFydmxnewVO.add(tbFydmxnewVO[i]);
		// isSurplus = true;
		// }
		// }
		// if (isSurplus) {
		// iVOPersistence.insertVOList(newTbFydmxnewVO);
		// }
		//
		// ob.setVdef6("2");
		// iVOPersistence.updateVO(ob);
		// } else {
		// // 表头VO
		// TbFydnewVO tbFydnewVO = (TbFydnewVO) getBufferData().getCurrentVO()
		// .getParentVO();
		// // 表体VO
		// TbFydmxnewVO[] tbFydmxnewVO = (TbFydmxnewVO[]) getBufferData()
		// .getCurrentVO().getChildrenVO();
		// for (int i = 0; i < tbFydmxnewVO.length; i++) {
		// double cfd_syfsl = tbFydmxnewVO[i].getCfd_syfsl().doubleValue();
		// if (cfd_syfsl != 0) {
		// isSurplus = true;
		// }
		// }
		// if (!isSurplus) {
		// ob.setVdef6("2");
		// iVOPersistence.updateVO(ob);
		// tbFydnewVO.setFyd_splitstatus(2);
		// iVOPersistence.updateVO(tbFydnewVO);
		// }
		// }

	}

	@Override
	protected void onBoCancel() throws Exception {
		getButtonManager().getButton(
				nc.ui.wds.w80060208.cfButtun.ICfButtun.cfzj).setEnabled(true);
		getButtonManager().getButton(
				nc.ui.wds.w80060208.cfButtun.ICfButtun.scpz).setEnabled(true);
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

	@Override
	protected void onBoQuery() throws Exception {
		// TODO Auto-generated method stub
		// StringBuffer strWhere = new StringBuffer();
		//
		// strWhere.append(" billtype=3 and ");
		// if (askForQueryCondition(strWhere) == false)
		// return;// 用户放弃了查询
		//
		// SuperVO[] queryVos = queryHeadVOs(strWhere.toString());
		//
		// getBufferData().clear();
		// // 增加数据到Buffer
		// addDataToBuffer(queryVos);
		//
		// updateBuffer();

		MyQueryTemplate myQuery = new MyQueryTemplate(myClientUI);
		SCMQueryConditionDlg query = myQuery.getQueryDlg(ClientEnvironment
				.getInstance().getCorporation().getPrimaryKey(), "80060208",
				ClientEnvironment.getInstance().getUser().getPrimaryKey(),
				"80060208");

		if (query.showModal() == nc.ui.pub.beans.MessageDialog.ID_OK) {
			// 获取查询条件
			ConditionVO[] voCons = query.getConditionVO();

			StringBuffer strWhere = new StringBuffer(query.getWhereSQL(voCons));

			strWhere
					.append(" and tb_fydnew.billtype=3 and dr=0 order by tb_fydnew.vbillno");
			SuperVO[] queryVos = queryHeadVOs(strWhere.toString());
			List<SuperVO> list = Arrays.asList(queryVos);

			if (null != list && list.size() > 0) {
				queryVos = new SuperVO[list.size()];
				queryVos = (SuperVO[]) list.toArray(queryVos);
				getBufferData().clear();
				// 增加数据到Buffer
				addDataToBuffer(queryVos);

				updateBuffer();
				getBillCardPanelWrapper().getBillCardPanel()
						.execHeadTailLoadFormulas();

				// myClientUI.getBillListPanel().getHeadItem("binitflag")
				// .setEnabled(true);
			} else {
				getBufferData().clear();
				updateBuffer();
			}

		}
		showZeroLikeNull(false);

	}

	@Override
	protected void onBoEdit() throws Exception {
		// TODO Auto-generated method stub
		super.onBoCard();
		// 判断是否以出库
		// 表体VO
		TbFydmxnewVO[] tbFydmxnewVO = (TbFydmxnewVO[]) getBillUI()
				.getVOFromUI().getChildrenVO();
		if (null != tbFydmxnewVO && tbFydmxnewVO.length > 0) {
			// 遍历子表
			for (int i = 0; i < tbFydmxnewVO.length; i++) {
				if (null != tbFydmxnewVO[i]) {
					// 检查主数量
					if (null != tbFydmxnewVO[i].getCfd_sfsl()
							&& tbFydmxnewVO[i].getCfd_sfsl().doubleValue() != 0) {
						getBillUI().showErrorMessage("该单据以出库不能修改！");
						return;
					}
					// 检查辅数量
					if (null != tbFydmxnewVO[i].getCfd_sffsl()
							&& tbFydmxnewVO[i].getCfd_sffsl().doubleValue() != 0) {
						getBillUI().showErrorMessage("该单据以出库不能修改！");
						return;
					}
				}
			}
		}

		// 修改按钮状态
		getButtonManager().getButton(
				nc.ui.wds.w80060208.cfButtun.ICfButtun.scpz).setEnabled(false);
		// 订单号主键
		String csaleid = getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("csaleid").getValue();
		// 拆分单据主键
		String fyd_pk = getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("fyd_pk").getValue();
		// 查询对象
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		// 修改对象
		IVOPersistence iVOPersistence = (IVOPersistence) NCLocator
				.getInstance().lookup(IVOPersistence.class.getName());
		// 判断当前单据是否是最后一张拆分单据
		StringBuffer sbsql = new StringBuffer(
				"select fyd_pk from  (select * from tb_fydnew where csaleid='");
		sbsql.append(csaleid);
		sbsql
				.append("' and dr=0 order by splitvbillno desc) where rownum<2 and dr=0 ");
		ArrayList tbfydvos = (ArrayList) query.executeQuery(sbsql.toString(),
				new ArrayListProcessor());
		if (null != tbfydvos && tbfydvos.size() > 0) {
			Object[] tbfydvo = (Object[]) tbfydvos.get(0);
			if (null != tbfydvo && tbfydvo.length > 0 && null != tbfydvo[0]) {
				if (!fyd_pk.equals(tbfydvo[0].toString())) {
					getBillUI().showErrorMessage(
							"本张单据不是最后一张拆分单据，剩余货物已被拆分，请选怎最后一张拆分单据！");
					return;
				}
			}
		}
		// 判断拆分是否结束
		SoSaleVO ob = (SoSaleVO) query.retrieveByPK(SoSaleVO.class, csaleid);
		if (null != ob) {
			if (null != ob.getVdef6() && !"".equals(ob.getVdef6())) {
				if ("2".equals(ob.getVdef6())) {
					getBillUI().showErrorMessage("单据拆分已经结束，单据不能再修改！");
					return;
				}
			}
		}
		isAdd = false;
		super.onBoEdit();
	}

	@Override
	protected void onBoDelete() throws Exception {
		// TODO Auto-generated method stub
		super.onBoCard();

		// 判断是否以出库
		// 表体VO
		TbFydmxnewVO[] tbFydmxnewVO = (TbFydmxnewVO[]) getBillUI()
				.getVOFromUI().getChildrenVO();
		if (null != tbFydmxnewVO && tbFydmxnewVO.length > 0) {
			// 遍历子表
			for (int i = 0; i < tbFydmxnewVO.length; i++) {
				if (null != tbFydmxnewVO[i]) {
					// 检查主数量
					if (null != tbFydmxnewVO[i].getCfd_sfsl()
							&& tbFydmxnewVO[i].getCfd_sfsl().doubleValue() != 0) {
						getBillUI().showErrorMessage("该单据以出库不能修改！");
						return;
					}
					// 检查辅数量
					if (null != tbFydmxnewVO[i].getCfd_sffsl()
							&& tbFydmxnewVO[i].getCfd_sffsl().doubleValue() != 0) {
						getBillUI().showErrorMessage("该单据以出库不能修改！");
						return;
					}
				}
			}
		}

		// 订单号主键
		String csaleid = getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("csaleid").getValue();
		// 拆分单据主键
		String fyd_pk = getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("fyd_pk").getValue();
		// 查询对象
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		// 修改对象
		IVOPersistence iVOPersistence = (IVOPersistence) NCLocator
				.getInstance().lookup(IVOPersistence.class.getName());
		// 判断当前单据是否是最后一张拆分单据
		StringBuffer sbsql = new StringBuffer(
				"select fyd_pk,fyd_splitstatus from  (select * from tb_fydnew where csaleid='");
		sbsql.append(csaleid);
		sbsql
				.append("' and dr=0 order by splitvbillno desc) where rownum<2 and dr=0 ");
		ArrayList tbfydvos = (ArrayList) query.executeQuery(sbsql.toString(),
				new ArrayListProcessor());
		if (null != tbfydvos && tbfydvos.size() > 0) {
			Object[] tbfydvo = (Object[]) tbfydvos.get(0);
			if (null != tbfydvo && tbfydvo.length > 0 && null != tbfydvo[0]) {
				if (!fyd_pk.equals(tbfydvo[0].toString())) {

					getBillUI().showErrorMessage(
							"本张单据不是最后一张拆分单据，剩余货物已被拆分，请选怎最后一张拆分单据！");
					return;
				}
			}
		}
		// 判断拆分是否结束
		SoSaleVO ob = (SoSaleVO) query.retrieveByPK(SoSaleVO.class, csaleid);
		if (null != ob) {
			if (null != ob.getVdef6() && !"".equals(ob.getVdef6())) {
				if ("2".equals(ob.getVdef6())) {
					getBillUI().showErrorMessage("单据拆分已经结束，单据不能再删除！");
					return;
				}
			}
		}

		// // 删除第一次拆分的单据
		// if (null != tbfydvos && tbfydvos.size() > 0) {
		// Object[] tbfydvo = (Object[]) tbfydvos.get(0);
		// if (null != tbfydvo && tbfydvo.length > 0 && null != tbfydvo[0]) {
		// if (Integer.parseInt(tbfydvo[1].toString()) == 0) {
		// ob.setVdef6("0");
		// iVOPersistence.updateVO(ob);
		// }
		// }
		// }
		// 参数设置

		ArrayList params = new ArrayList();
		// Iw8000 iw = (Iw80060208) NCLocator.getInstance().lookup(
		// Iw80060208.class.getName());
		params.add(getBillUI().getUserObject());
		// params.add(iw);
		params.add(tbfydvos);
		params.add(ob);
		// 界面没有数据或者有数据但是没有选中任何行
		if (getBufferData().getCurrentVO() == null)
			return;

		if (MessageDialog.showOkCancelDlg(getBillUI(),
				nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
						"UPPuifactory-000064")/* @res "档案删除" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
						"UPPuifactory-000065")/* @res "是否确认删除该基本档案?" */
				, MessageDialog.ID_CANCEL) != UIDialog.ID_OK)
			return;

		AggregatedValueObject modelVo = getBufferData().getCurrentVO();
		try {
			getBusinessAction().delete(modelVo,
					getUIController().getBillType(),
					getBillUI()._getDate().toString(), params);

			// Iw80060208 iw = (Iw80060208) NCLocator.getInstance().lookup(
			// Iw80060208.class.getName());
			// AggregatedValueObject retVo = iw.deleteBD80060208(modelVo,
			// params);
		} catch (Exception ex1) {
			ex1.printStackTrace();
		}

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

		//
		super.onBoReturn();
	}

	public void showZeroLikeNull(boolean bShowZeroLikeNull) {
		BillRendererVO vo = getBillCardPanelWrapper().getBillCardPanel()
				.getBodyPanel().getRendererVO();
		vo.setShowZeroLikeNull(bShowZeroLikeNull);
		getBillCardPanelWrapper().getBillCardPanel().getBodyPanel()
				.setShowFlags(vo);
	}

	@Override
	protected void onBoPrint() throws Exception {
		// TODO Auto-generated method stub

		TbFydnewVO tbFydnewVO = new TbFydnewVO();
		// 打印缓存
		TbFydmxnewVO[] tbFydmxnewVO = null;
		if (getBillManageUI().isListPanelSelected()) {
			tbFydmxnewVO = (TbFydmxnewVO[]) getBillManageUI()
					.getBillListWrapper().getVOFromUI().getChildrenVO();
			tbFydnewVO = (TbFydnewVO) getBillManageUI().getBillListWrapper()
					.getVOFromUI().getParentVO();
		} else {
			tbFydmxnewVO = (TbFydmxnewVO[]) getBillCardPanelWrapper()
					.getBillVOFromUI().getChildrenVO();
			tbFydnewVO = (TbFydnewVO) getBillCardPanelWrapper()
					.getBillVOFromUI().getParentVO();
		}
		// 打印次数
		int iprintcount = 0;
		if (null != tbFydnewVO.getIprintcount()) {
			iprintcount = tbFydnewVO.getIprintcount();
		}
		iprintcount++;
		tbFydnewVO.setIprintcount(iprintcount);
		// 订单的打印次数
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		SoSaleVO ob = (SoSaleVO) query.retrieveByPK(SoSaleVO.class, tbFydnewVO
				.getCsaleid());
		// getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
		// "csaleid").getValue()
		int saleIprintcount = 0;
		if (null != ob.getIprintcount()) {
			saleIprintcount = ob.getIprintcount();
		} else {
			saleIprintcount = 0;
		}
		saleIprintcount++;
		ob.setIprintcount(saleIprintcount);
		Iw80060208 iw = (Iw80060208) NCLocator.getInstance().lookup(
				Iw80060208.class.getName());
		AggregatedValueObject retVo = iw.prinDB80060208(tbFydnewVO, ob);

		// 实际打印的集合
		ArrayList tbFydmxnewVOs = new ArrayList();
		for (int i = 0; i < tbFydmxnewVO.length; i++) {
			double cfd_xs = 0;
			if (null != tbFydmxnewVO[i].getCfd_xs()) {
				cfd_xs = tbFydmxnewVO[i].getCfd_xs().doubleValue();
			}
			if (0 != cfd_xs) {
				tbFydmxnewVOs.add(tbFydmxnewVO[i]);
			}
		}
		// 实际打印的VO
		TbFydmxnewVO[] tbFydmxnewPrintVO = new TbFydmxnewVO[tbFydmxnewVOs
				.size()];
		for (int i = 0; i < tbFydmxnewPrintVO.length; i++) {
			if (null != tbFydmxnewVOs.get(i)) {
				tbFydmxnewPrintVO[i] = (TbFydmxnewVO) tbFydmxnewVOs.get(i);
			}
		}
		// 要打印的数据源参数
		getBufferData().getCurrentVO().setChildrenVO(tbFydmxnewPrintVO);
		int nCurrentRow = getBufferData().getCurrentRow();
		updateBuffer();

		if (nCurrentRow >= 0) {
			getBufferData().setCurrentRow(nCurrentRow);
		}
		// 打印单据
		super.onBoPrint();
		// 恢复正常显示缓存
		getBufferData().getCurrentVO().setChildrenVO(tbFydmxnewVO);
		updateBuffer();

		if (nCurrentRow >= 0) {
			getBufferData().setCurrentRow(nCurrentRow);
		}
		super.onBoRefresh();
	}

	// 列表UI
	private BillManageUI getBillManageUI() {
		return (BillManageUI) getBillUI();
	}

}