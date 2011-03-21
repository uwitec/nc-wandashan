package nc.ui.wds.w80060604;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.itf.wds.w80060604.Iw80060604;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.ic.pub.bill.query.QueryConditionDlgForBill;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pub.bill.BillTableSelectionEvent;
import nc.ui.pub.print.IDataSource;
import nc.ui.scm.pub.query.SCMQueryConditionDlg;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.pub.ListPanelPRTS;
import nc.ui.wds.w8000.CommonUnit;
import nc.ui.wds.w8004040204.ssButtun.ISsButtun;
import nc.ui.wds.w80060604.czlbButtun.czlbBtn;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFTime;
import nc.vo.pub.query.ConditionVO;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderHVO;
import nc.vo.wds.w80060406.TbFydmxnewVO;
import nc.vo.wds.w80060406.TbFydnewVO;
import nc.vo.wds.w80060604.MyBillVO;
import nc.vo.wds.w80060604.SoSaleVO;
import nc.vo.wds.w80060604.SoSaleorderBVO;

/**
 * 
 * 销售订单调度
 * 
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	MyClientUI myClientUI = null;
	private boolean isControl = false; // 是否有权限操作当前单据
	IVOPersistence ivo = (IVOPersistence) NCLocator.getInstance().lookup(
			IVOPersistence.class.getName());
	IUAPQueryBS iuap = (IUAPQueryBS) NCLocator.getInstance().lookup(
			IUAPQueryBS.class.getName());

	// 调用接口 操作数据库
	Iw80060604 iw = (Iw80060604) NCLocator.getInstance().lookup(
			Iw80060604.class.getName());

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
		myClientUI = (MyClientUI) billUI;
		String isType;
		try {
			isType = CommonUnit.getUserType(ClientEnvironment.getInstance()
					.getUser().getPrimaryKey());
			if ((null != isType && isType.equals("2"))
					|| (null != isType && isType.equals("3"))) {
				isControl = true;
			}
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String sWhere;

	@Override
	protected void onBoQuery() throws Exception {
		if (isControl) {
			MyQueryTemplate myQuery = new MyQueryTemplate(myClientUI);
			SCMQueryConditionDlg query = myQuery.getQueryDlg(ClientEnvironment
					.getInstance().getCorporation().getPrimaryKey(),
					"80060604", ClientEnvironment.getInstance().getUser()
							.getPrimaryKey(), "80060604");

			if (query.showModal() == nc.ui.pub.beans.MessageDialog.ID_OK) {
				ConditionVO[] tmpvoCons = null;
				// 获取查询条件
				ConditionVO[] voCons = query.getConditionVO();
				String tmpwhere = null; // 临时记录获取的仓库主键
				if (null != voCons && voCons.length > 0) {
					String pk = CommonUnit.getStordocName(ClientEnvironment
							.getInstance().getUser().getPrimaryKey());
					List<ConditionVO> tmplist = new ArrayList<ConditionVO>();
					for (int i = 0; i < voCons.length; i++) {
						tmpwhere = null;
						// 获取查询条件中的fieldcode
						String fieldCode = voCons[i].getFieldCode();
						// 如果是so_sale.tmpstock获取值根据仓库进行查询出它所有的订单
						if (fieldCode.equals("so_sale.tmpstock")) {
							tmpwhere = voCons[i].getValue();
						}
						if (fieldCode.equals("so_sale.fstatus")) {
							tmpwhere = voCons[i].getValue();
							// 状态 0 全部 ，1未审批，2已审批，3已冻结,4审批中
							if (tmpwhere.equals("0")) {
								voCons[i].setValue("1,2,6"); // 0无状态（未使用）1自由
																// 2审批 3冻结
																// 4关闭（未使用） 5作废
																// 6结束 7正在审批
																// 8审批未通过
								voCons[i].setOperaCode("in");
							} else if (tmpwhere.equals("3")) {
								voCons[i].setValue("6");
							} else if (tmpwhere.equals("4")) {
								voCons[i].setValue("7");
							}
						}
						tmpwhere = null;
						tmplist.add(voCons[i]);
					}
					if (null == tmpwhere)
						tmpwhere = pk;
					tmpvoCons = new ConditionVO[tmplist.size()];
					tmpvoCons = tmplist.toArray(tmpvoCons);
					StringBuffer strWhere = new StringBuffer(query
							.getWhereSQL(tmpvoCons));

					strWhere
							.append(" and so_sale.dr = 0 "
									+ " and so_sale.ccustomerid in ( select b.pk_cumandoc "
									+ " from bd_cumandoc b, tb_storcubasdoc t"
									+ " where t.pk_cubasdoc = b.pk_cubasdoc and t.pk_stordoc = '"
									+ tmpwhere
									+ "') and so_sale.vreceiptcode not like '%退%'"
									+ " order by so_sale.ccustomerid");
					this.setSWhere(strWhere.toString());
					SuperVO[] queryVos = queryHeadVOs(strWhere.toString());
					List<SuperVO> list = Arrays.asList(queryVos);
					if (null != list && list.size() > 0) {
						queryVos = new SuperVO[list.size()];
						queryVos = (SuperVO[]) list.toArray(queryVos);

						for (int i = 0; i < queryVos.length; i++) {
							((SoSaleVO) queryVos[i]).setPk_defdoc15(null);
							((SoSaleVO) queryVos[i]).setPk_defdoc15(pk);
						}
						getBufferData().clear();
						// 增加数据到Buffer
						addDataToBuffer(queryVos);

						updateBuffer();
						myClientUI.getBillListPanel().getHeadItem("binitflag")
								.setEnabled(true);

					} else {
						getBufferData().clear();
						updateBuffer();
					}

				}
				if (getBufferData().getVOBufferSize() > 0)
					getButtonManager().getButton(ISsButtun.cz).setEnabled(true);
				myClientUI.updateButtons();
			}
		} else {
			getBillUI().showErrorMessage("操作失败,当前登录者没有进行人员绑定");
			return;
		}
	}

	/**
	 * 拆分订单
	 * 
	 * @throws Exception
	 */
	protected void oncfdd() throws Exception {
		alertMsg("2", "拆分订单");
	}

	/**
	 * 合并订单
	 * 
	 * @throws Exception
	 */
	protected void onhbdd() throws Exception {
		alertMsg("3", "合并订单");
	}

	/**
	 * 分厂直流
	 */
	protected void onfczl() throws Exception {
		alertMsg("1", "分厂直流");

	}

	/*
	 * 正常订单(non-Javadoc)
	 * 
	 * @see nc.ui.wds.w80060604.AbstractMyEventHandler#onzcdd()
	 */
	protected void onzcdd() throws Exception {
		int results = myClientUI.showOkCancelMessage("确认执行正常订单操作?");
		if (results != 1)
			return;
		BillListPanel billList = ((BillManageUI) getBillUI())
				.getBillListPanel();
		// 获取当前的表头数据对象数组
		CircularlyAccessibleValueObject[] headVO = billList.getHeadBillModel()
				.getBodyValueVOs(SoSaleVO.class.getName());
		// 存储选中后的对象 方便后面会写
		List<SoSaleVO> tempList = new ArrayList<SoSaleVO>();
		// 判断是否为空
		if (null != headVO && headVO.length > 0) {
			// 循环
			for (int j = 0; j < headVO.length; j++) {
				// 获取单个表头对象
				SoSaleVO sale = (SoSaleVO) headVO[j];
				// 获取里面的一个属性 销售主表中的”期初标志“字段，现在变更为 表头中的“选择”复选框
				if (null != sale.getBinitflag()
						&& sale.getBinitflag().booleanValue()) {
					int count = CommonUnit.getIprintCount(sale.getCsaleid());
					if (count > 0) {
						myClientUI.showErrorMessage("操作失败!当前操作单据中已经生成运单");
						return;
					}
					/*
					 * // 判断类型 if (null != sale.getVdef5() &&
					 * !"".equals(sale.getVdef5())) { // 类型是拆分订单并且已经进行拆分和
					 * 类型是合并订单并且已经拆分的单据 不允许删除 if (sale.getVdef5().equals("2")) {
					 * if (null != sale.getVdef6() &&
					 * !"".equals(sale.getVdef6()) &&
					 * !sale.getVdef6().equals("0")) { myClientUI
					 * .showErrorMessage("操作失败!当前操作单据中已经生成运单"); return; } } if
					 * (sale.getVdef5().equals("3")) { if (null !=
					 * sale.getVdef6() && !"".equals(sale.getVdef6()) &&
					 * sale.getVdef6().equals("5")) { myClientUI
					 * .showErrorMessage("操作失败!当前操作单据中已经生成运单"); return; } }
					 * sale.setVdef5(null); // 类别 sale.setVdef6(null); // 状态
					 * sale.setIprintcount(null); // 打印次数 sale.setVdef7(null); //
					 * 打印时间 sale.setBinitflag(new UFBoolean(false)); // “选择”复选框
					 * tempList.add(sale); }
					 */
					sale.setVdef5(null); // 类别
					sale.setVdef6(null); // 状态
					sale.setIprintcount(null); // 打印次数
					sale.setVdef7(null); // 打印时间
					sale.setBinitflag(new UFBoolean(false)); // “选择”复选框
					tempList.add(sale);
				}
			}
			if (tempList.size() < 1) {
				myClientUI.showWarningMessage("请在前面复选框中选择一条信息进行操作");
				return;
			}
			iw.updateSosale(tempList);
			//this.onRefresh();
		}
	}

	/**
	 * 询问执行操作，调取修改方法
	 * 
	 * @param status
	 *            状态
	 * @param msg
	 *            提示信息
	 * @throws Exception
	 */
	private void alertMsg(String status, String msg) throws Exception {
		int result = myClientUI.showYesNoMessage("确认执行" + msg + "操作?");
		if (result == 4) {
			if (getchangelist(status)) {
				myClientUI.showHintMessage("操作成功");
				// this.onRefresh();
			} else {
				myClientUI.showHintMessage("操作失败");
			}
		}
	}

	/***************************************************************************
	 * 根据当前的查询条件再进行查询更新
	 * 
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	private void onRefresh() throws ClassNotFoundException, Exception {
		SuperVO[] queryVos = queryHeadVOs(this.getSWhere());
		List<SuperVO> list = Arrays.asList(queryVos);
		if (null != list && list.size() > 0) {
			queryVos = new SuperVO[list.size()];
			queryVos = (SuperVO[]) list.toArray(queryVos);
			String pk = CommonUnit.getStordocName(ClientEnvironment
					.getInstance().getUser().getPrimaryKey());
			for (int i = 0; i < queryVos.length; i++) {
				((SoSaleVO) queryVos[i]).setPk_defdoc15(null);
				((SoSaleVO) queryVos[i]).setPk_defdoc15(pk);
			}
			getBufferData().clear();
			// 增加数据到Buffer
			addDataToBuffer(queryVos);

			updateBuffer();

			myClientUI.getBillListPanel().getHeadItem("binitflag").setEnabled(
					true);
		} else {
			updateBuffer();
		}
	}

	/**
	 * 获取当前表头数据判断是否有选择的，根据参数修改状态进行回写ERP中销售订单表
	 * 
	 * @param status
	 *            状态 1 分厂直流 2 拆分订单 3 合并订单 空是正常订单
	 * @return boolean
	 * @throws Exception
	 */
	private boolean getchangelist(String status) throws Exception {

		List list = null;
		// 获取表体总行数
		int count = myClientUI.getBillListPanel().getHeadTable().getRowCount();
		if (count > 0) {
			list = new ArrayList();
			// 如果状态等于合并订单
			if (status.equals("3")) {
				String cusPK = ""; // 客户主键
				String cusAdd = ""; // 客商地址
				int temp = 0;
				for (int j = 0; j < count; j++) {
					// 获取是否选择
					Object o = myClientUI.getBillListPanel().getHeadBillModel()
							.getValueAt(j, "binitflag");
					if (null != o && Boolean.parseBoolean(o.toString())) {
						temp += 1;
						// 获取所有列表下的VO (只有表头数据，没有表ti)
						MyBillVO mybillvo = (MyBillVO) myClientUI
								.getBillListPanel().getBillListData()
								.getBillValueVO(j, MyBillVO.class.getName(),
										SoSaleVO.class.getName(),
										SoSaleorderBVO.class.getName());
						SoSaleVO salevo = (SoSaleVO) mybillvo.getParentVO();
						// 如果客商PK 为空 第一次赋值
						if ("".equals(cusPK)) {
							cusPK = salevo.getCcustomerid();
							cusAdd = salevo.getVreceiveaddress();
						} else {
							// 判断客商主键是否相等
							if (!cusPK.equals(salevo.getCcustomerid())) {
								myClientUI.showWarningMessage("客商信息不一致,不能合并订单");
								return false;
							} else if (!(cusAdd + "1").equals(salevo
									.getVreceiveaddress()
									+ "1")) {
								myClientUI.showWarningMessage("客商地址不一致,不能合并订单");
								return false;
							}
						}
					}
				}
				// 判断需要合并订单的数据个数
				// if (temp <= 1) {
				// myClientUI.showWarningMessage("请选择至少两条数据合并订单");
				// return false;
				// }
			}
			for (int i = 0; i < count; i++) {
				// 获取是否选择
				Object o = myClientUI.getBillListPanel().getHeadBillModel()
						.getValueAt(i, "binitflag");

				if (null != o && Boolean.parseBoolean(o.toString())) {
					// 获取所有列表下的VO (只有表头数据，没有表ti)
					MyBillVO mybillvo = (MyBillVO) myClientUI
							.getBillListPanel().getBillListData()
							.getBillValueVO(i, MyBillVO.class.getName(),
									SoSaleVO.class.getName(),
									SoSaleorderBVO.class.getName());
					SoSaleVO salevo = (SoSaleVO) mybillvo.getParentVO();
					// 如果状态为2 拆分订单 设置ERP占用自定义字段Vdef6
					// 拆分订单状态 （第一次拆分订单）0第一次拆分订单
					int results = CommonUnit
							.getIprintCount(salevo.getCsaleid());
					if (results > 0) {
						myClientUI.showErrorMessage("操作失败!当前操作单据中已经生成运单");
						return false;
					}
					if (status.equals("2")) {
						int index = salevo.getVreceiptcode().toLowerCase()
								.indexOf("m");
						if (index > -1) {
							myClientUI.showErrorMessage("单据中包含买赠订单,无法进行拆分");
							return false;
						}
						salevo.setVdef6("0");
					}
					if (status.equals("3"))
						salevo.setVdef6(null);
					// 添加状态
					salevo.setVdef5(status);
					salevo.setBinitflag(new UFBoolean(false));
					list.add(salevo);
				}
			}
			// 判断是否有数据
			if (null != list && list.size() > 0) {
				// 回写数据库
				ivo.updateVOList(list);
				return true;
			} else {
				myClientUI.showWarningMessage("请选择一条数据进行操作");
				return false;
			}
		}

		// BillListPanel panelList = ((BillManageUI)
		// getBillUI()).getBillListPanel();
		// if(panelList.getBillListData().getBillSelectValueVO(billVOName,
		// headVOName, bodyVOName))
		// System.out.println(123);
		//
		return false;

	}

	@Override
	protected void onBoPrint() throws Exception {
		int result = myClientUI.showOkCancelMessage("是否打印?");
		if (result == 1) {
			// 如果是列表界面，使用ListPanelPRTS数据源
			if (myClientUI.isListPanelSelected()) {
				// 存储数据源的集合
				List<IDataSource> dsList = new ArrayList<IDataSource>();
				BillListPanel billList = ((BillManageUI) getBillUI())
						.getBillListPanel();
				// 获取当前的表头数据对象数组
				CircularlyAccessibleValueObject[] headVO = billList
						.getHeadBillModel().getBodyValueVOs(
								SoSaleVO.class.getName());
				// 存储选中后的对象 方便后面回写
				List<SoSaleVO> tempList = new ArrayList<SoSaleVO>();
				List<SoSaleorderBVO[]> tempbList = new ArrayList<SoSaleorderBVO[]>();
				List<SoSaleVO> saletempList = new ArrayList<SoSaleVO>();
				// 判断类型
				boolean isType = false;
				// 判断是否为空
				if (null != headVO && headVO.length > 0) {
					// 循环
					for (int j = 0; j < headVO.length; j++) {
						// 获取单个表头对象
						SoSaleVO sale = (SoSaleVO) headVO[j];
						// 获取里面的一个属性 销售主表中的”期初标志“字段，现在变更为 表头中的“选择”复选框
						if (null != sale.getBinitflag()
								&& sale.getBinitflag().booleanValue()) {

							// 判断类型
							if (null != sale.getVdef5()
									&& !"".equals(sale.getVdef5()))
								isType = true;
							else {
								if (null == sale.getIprintcount()
										|| "".equals(sale.getIprintcount())) {
									// 给临时表头集合赋值
									tempList.add(sale);
									// 给临时表体集合赋值
									tempbList
											.add((SoSaleorderBVO[]) getBufferData()
													.getVOByRowNo(j)
													.getChildrenVO());
								}
								sale.setBinitflag(new UFBoolean(false));
								saletempList.add(sale);
							}

							nc.ui.pub.print.IDataSource dataSource = new MyPrint(
									getBillUI()._getModuleCode(), billList, j,
									getBufferData(), myClientUI);

							dsList.add(dataSource);
						}
					}
				}
				if (dsList.size() <= 0) {
					myClientUI.showWarningMessage("请在前面复选框中选择一条信息进行打印");
					return;
				}
				if (isType) {
					int results = myClientUI
							.showYesNoMessage("您选择的信息中包含非正常订单是否继续打印?");
					if (results != 4) {
						return;
					}
				}
				// 后台生成发运单
				insertFyd(tempList, tempbList, saletempList);
				// 获取打印对象
				nc.ui.pub.print.PrintEntry print = new nc.ui.pub.print.PrintEntry(
						null, null);
				// 开始批量打印
				print.beginBatchPrint();
				for (int i = 0; i < dsList.size(); i++) {
					IDataSource ds = dsList.get(i);
					print.setDataSource(ds);
				}
				print.endBatchPrint();
				print.setTemplateID(getBillUI()._getCorp().getPrimaryKey(),
						getBillUI()._getModuleCode(), getBillUI()
								._getOperator(), getBillUI().getBusinessType(),
						getBillUI().getNodeKey());
				if (print.selectTemplate() == 1)
					print.preview();
			}
			// 如果是卡片界面，使用CardPanelPRTS数据源
			else {
				super.onBoPrint();
			}
			// this.onRefresh();
		}
	}

	/**
	 * 生成发运单 （类别：正常订单）
	 * 
	 * @param tempList
	 *            正常订单的集合
	 * @param tempbList
	 *            正常订单的子集合
	 * @param saletempList
	 *            所有订单的集合
	 * @throws Exception
	 */
	private void insertFyd(List<SoSaleVO> tempList,
			List<SoSaleorderBVO[]> tempbList, List<SoSaleVO> saletempList)
			throws Exception {

		List<TbFydnewVO> fydList = new ArrayList<TbFydnewVO>();
		List<TbFydmxnewVO[]> fydmxList = new ArrayList<TbFydmxnewVO[]>();
		if (tempList.size() > 0) {
			// /////进行VO转换
			// 把当前集合中存储的销售主表对象转换成发运单对象的集合/////////////////////////////////////////////
			for (int i = 0; i < tempList.size(); i++) {
				SoSaleVO salevo = tempList.get(i);
				// ------------转换表头对象-----------------//
				TbFydnewVO fydvo = new TbFydnewVO();

				if (null != salevo.getCsaleid()
						&& !"".equals(salevo.getCsaleid())) {
					int num = CommonUnit.getIprintCount(salevo.getCsaleid());
					if (num == 0) {
						fydvo.setCsaleid(salevo.getCsaleid()); // 销售主表主键

						if (null != salevo.getVreceiptcode()
								&& !"".equals(salevo.getVreceiptcode())) {
							fydvo.setVbillno(salevo.getVreceiptcode()); // 订单号
						}
						if (null != salevo.getCbiztype()
								&& !"".equals(salevo.getCbiztype())) {
							fydvo.setPk_busitype(salevo.getCbiztype()); // 业务类型
						}
						if (null != salevo.getCcustomerid()
								&& !"".equals(salevo.getCcustomerid())) {
							fydvo.setPk_kh(salevo.getCcustomerid()); // 客户主键
							fydvo.setSrl_pkr(CommonUnit.getAreaclName(salevo
									.getCcustomerid())); // 收货站
						}
						if (null != salevo.getVreceiveaddress()
								&& !"".equals(salevo.getVreceiveaddress())) {
							fydvo.setFyd_shdz(salevo.getVreceiveaddress()); // 收货地址
						}
						if (null != salevo.getCemployeeid()
								&& !"".equals(salevo.getCemployeeid())) {
							fydvo.setPk_psndoc(salevo.getCemployeeid()); // 业务员
						}
						if (null != salevo.getVnote()
								&& !"".equals(salevo.getVnote())) {
							fydvo.setFyd_bz(salevo.getVnote()); // 备注
						}
						if (null != salevo.getCdeptid()
								&& !"".equals(salevo.getCdeptid())) {
							fydvo.setCdeptid(salevo.getCdeptid()); // 部门
						}
						if (null != salevo.getDaudittime()
								&& !"".equals(salevo.getDaudittime())) {
							fydvo
									.setFyd_spsj(salevo.getDaudittime()
											.toString()); // 审批时间
						}
						if (null != salevo.getDapprovedate()
								&& !"".equals(salevo.getDapprovedate())) {
							fydvo.setDapprovedate(salevo.getDapprovedate()); // 审批日期
						}
						// if (null != salevo.getVdef18()
						// && !"".equals(salevo.getVdef18())) {
						// UFDouble gls = null;
						// try {
						// gls = new UFDouble(salevo.getVdef18());
						// } catch (NumberFormatException e) {
						// // TODO Auto-generated catch block
						// gls = null;
						// }
						// fydvo.setFyd_yslc(gls); // 公里数
						// }
						if (null != salevo.getDbilldate()
								&& !"".equals(salevo.getDbilldate())) {
							fydvo.setFyd_xhsj(salevo.getDbilldate()); // 需货时间
							// 这里进行转换的是销售主表中的单据日期字段
						}
						// 设置运货方式
						fydvo.setFyd_yhfs("汽运");
						fydvo.setBilltype(1); // 单据类型 0 发运制单 1 销售订单 2 分厂直流 3
						// 拆分订单
						// 4 合并订单
						fydvo.setVbillstatus(1); // 单据状态 制单未完成 1
						// 制单完成
						fydvo.setFyd_fyzt(0); // 发运状态 0 待发运 1 已发运
						// 制单日期
						fydvo.setDmakedate(_getDate());
						fydvo.setFyd_dby(ClientEnvironment.getInstance()
								.getUser().getPrimaryKey()); // 设置调度员
						fydvo.setVoperatorid(ClientEnvironment.getInstance()
								.getUser().getPrimaryKey()); // 设置制单人
						// 设置发货站
						fydvo.setSrl_pk(CommonUnit
								.getStordocName(ClientEnvironment.getInstance()
										.getUser().getPrimaryKey()));
						fydvo.setIprintdate(_getDate()); // 打印日期
						fydvo.setIprintcount(1); // 打印次数
						// fydvo.setDmaketime(new UFTime()); // 制单时间
						fydList.add(fydvo);
						// --------------转换表头结束---------------//
						// --------------转换表体----------------//
						SoSaleorderBVO[] salebvo = tempbList.get(i);
						List<TbFydmxnewVO> tbfydmxList = new ArrayList<TbFydmxnewVO>();
						if (null != salebvo && salebvo.length > 0) {
							for (int j = 0; j < salebvo.length; j++) {
								SoSaleorderBVO saleb = salebvo[j];
								TbFydmxnewVO fydmxnewvo = new TbFydmxnewVO();
								if (null != salevo.getCsaleid()
										&& !"".equals(salevo.getCsaleid())) {
									fydmxnewvo.setCsaleid(salevo.getCsaleid()); // 销售主表主键
								}
								if (null != saleb.getCorder_bid()
										&& !"".equals(saleb.getCorder_bid())) {
									fydmxnewvo.setCorder_bid(saleb
											.getCorder_bid()); // 销售附表主键
								}
								if (null != saleb.getCinvbasdocid()
										&& !"".equals(saleb.getCinvbasdocid())) {
									fydmxnewvo.setPk_invbasdoc(saleb
											.getCinvbasdocid()); // 单品主键
								}
								if (null != saleb.getNnumber()
										&& !"".equals(saleb.getNnumber())) {
									fydmxnewvo.setCfd_yfsl(saleb.getNnumber()); // 应发数量
								}
								if (null != saleb.getNpacknumber()
										&& !"".equals(saleb.getNpacknumber())) {
									fydmxnewvo
											.setCfd_xs(saleb.getNpacknumber()); // 箱数
								}
								if (null != saleb.getCrowno()
										&& !"".equals(saleb.getCrowno())) {
									fydmxnewvo.setCrowno(saleb.getCrowno()); // 行号
								}
								if (null != saleb.getBlargessflag()
										&& !"".equals(saleb.getBlargessflag())) {
									fydmxnewvo.setBlargessflag(saleb
											.getBlargessflag()); // 是否赠品
								}
								if (null != saleb.getCunitid()
										&& !"".equals(saleb.getCunitid())) {
									fydmxnewvo.setCfd_dw(saleb.getCunitid()); // 单位
								}
								tbfydmxList.add(fydmxnewvo);
							}
						}
						if (tbfydmxList.size() > 0) {
							TbFydmxnewVO[] fydmxVO = new TbFydmxnewVO[tbfydmxList
									.size()];
							tbfydmxList.toArray(fydmxVO);
							fydmxList.add(fydmxVO);
						} else {
							fydmxList.add(null);
						}
					}

				}
				// ----------------转换表体结束---------------------//
			}

		}

		iw.insertFyd(fydList, fydmxList, saletempList);
	}

	@Override
	protected void onBoCard() throws Exception {
		// TODO Auto-generated method stub
		getButtonManager().getButton(
				nc.ui.wds.w8004040204.ssButtun.ISsButtun.cz).setEnabled(false);

		super.onBoCard();

	}

	@Override
	protected void onBoReturn() throws Exception {
		// TODO Auto-generated method stub
		if (getBufferData().getVOBufferSize() > 0)
			getButtonManager().getButton(
					nc.ui.wds.w8004040204.ssButtun.ISsButtun.cz).setEnabled(
					true);
		super.onBoReturn();
	}

	@Override
	protected void onBoRefresh() throws Exception {
		// TODO Auto-generated method stub
		super.onBoRefresh();
		getBillCardPanelWrapper().getBillCardPanel().execHeadTailLoadFormulas();
	}

	public String getSWhere() {
		return sWhere;
	}

	public void setSWhere(String where) {
		sWhere = where;
	}

}