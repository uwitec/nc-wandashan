package nc.ui.wds.ic.so.out;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.wds.w8004040204.Iw8004040204;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillMouseEnent;
import nc.ui.pub.bill.BillTableMouseListener;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.other.out.TbOutgeneralTVO;
import nc.vo.ic.pub.StockInvOnHandVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.wl.pub.CommonUnit;

public class TrayDisposeDlg extends nc.ui.pub.beans.UIDialog implements
		ActionListener, BillEditListener, BillTableMouseListener,
		ListSelectionListener {
	// 创建MyClientUI 方便弹出对话框 和 给调用页面传值
	Container myClientUI = null;
	// 调用页面所选择的行
	private int index;
	// 存储已经添加过的数据 在删除的时候作为参数
	private List<TbOutgeneralTVO> generaltvoList = new ArrayList<TbOutgeneralTVO>();
	// 调用接口 删除和添加方法
	private Iw8004040204 iw = null;

	private JPanel ivjUIDialogContentPane = null;

	protected BillListPanel ivjbillListPanel = null;

	// 公司Id
	private String m_pkcorp = null;

	// 操作人id
	private String m_operator = null;

	// 单据类型
	private String m_billType = null;

	// 功能节点标识
	private String m_nodeKey = null;

	private java.awt.Container parent = null;

	private UIPanel ivjPanlCmd = null;

	// 返回集合VO数组
	protected AggregatedValueObject[] retBillVos = null;

	// 确定按钮
	private UIButton ivjbtnOk = null;

	// 取消按钮
	private UIButton ivjbtnCancel = null;

	public BillListPanel getIvjbillListPanel() {
		return ivjbillListPanel;
	}

	public void setIvjbillListPanel(BillListPanel ivjbillListPanel) {
		this.ivjbillListPanel = ivjbillListPanel;
	}

	public UIButton getIvjbtnOk() {
		return ivjbtnOk;
	}

	public void setIvjbtnOk(UIButton ivjbtnOk) {
		this.ivjbtnOk = ivjbtnOk;
	}

	public String getBillType() {
		return m_billType;
	}

	public String getPkCorp() {
		return m_pkcorp;
	}

	public String getOperator() {
		return m_operator;
	}

	public String getNodeKey() {
		return m_nodeKey;
	}

	public java.awt.Container getParent() {
		return parent;
	}

	// 存储传过来的表体对象
	private TbOutgeneralBVO generalbvo;

	public TrayDisposeDlg(String m_billType, String m_operator,
			String m_pkcorp, String m_nodeKey, java.awt.Container parent) {
		init(m_billType, m_operator, m_pkcorp, m_nodeKey, parent);
	}

	private void init(String m_billType, String m_operator, String m_pkcorp,
			String m_nodeKey, java.awt.Container parent) {
		this.m_billType = m_billType;
		this.m_operator = m_operator;
		this.m_pkcorp = m_pkcorp;
		this.m_nodeKey = m_nodeKey;
		this.parent = parent;
		this.myClientUI = parent;
		// getbtnOk().addActionListener(this);
		// getbtnCancel().addActionListener(this);
		setName("BillSourceUI");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(800, 650);
		setTitle("选择托盘");
		setContentPane(getUIDialogContentPane());
		// 初始化接口
		this.setIw((Iw8004040204) NCLocator.getInstance().lookup(
				Iw8004040204.class.getName()));
	}

	/**
	 * 托盘指定按钮调用方法 进行操作 弹出单据模板
	 * 
	 * @param parent
	 * @param item
	 *            表体对象
	 * @param index
	 *            选择行的索引
	 * @return
	 * @throws Exception
	 */
	public AggregatedValueObject[] getReturnVOs(java.awt.Container parent,
			TbOutgeneralBVO item, int index, String pk_stordoc, boolean type)
			throws Exception {
		// 给索引行赋值
		this.setIndex(index);
		// 查询出数据
		loadHeadData(item, pk_stordoc, type);
		// 弹出单据
		addBillUI();
		if (showModal() == nc.ui.pub.beans.MessageDialog.ID_OK) {
			// 获取所选VO
			return getRetVos();
		}

		return null;
	}

	// 加载数据 为页面传值
	public void loadHeadData(TbOutgeneralBVO item, String pk_stordoc,
			boolean type) throws Exception {
		// 判断页面传来的表体对象是否为空
		if (null != item) {
			TbOutgeneralTVO generaltvo = new TbOutgeneralTVO();
			// 给存储表体赋值
			this.setGeneralbvo(item);
			TbOutgeneralBVO[] generalBVO = new TbOutgeneralBVO[1];
			// 转换数组
			generalBVO[0] = item;
			// 给当前单据表头赋值
			this.getbillListPanel().getBillListData().setHeaderValueVO(
					generalBVO);
			// 调用方法 根据传来的表体对象 查询数据库中的库存信息表，把符合的数据提取出来组装成当前单据中表体VO对象
			TbOutgeneralTVO[] generalTVO = this.getWarehousestock(item,
					pk_stordoc, type);
			// 给当前单据表体赋值
			this.getbillListPanel().getBillListData()
					.setBodyValueVO(generalTVO);
			// 执行表体和表头公式
			this.getbillListPanel().getHeadBillModel().execLoadFormula();
			this.getbillListPanel().getBodyBillModel().execLoadFormula();
		}

	}

	/**
	 * 根据存货主键获取库存中信息 如果当前托盘指定表中已经存储过数据 就给查询出来 把这个对象传到数组中 跳过此条记录的查询 继续下面的
	 * 
	 * @param item
	 *            传过来的表体对象
	 * @return
	 * @throws Exception
	 */
	public TbOutgeneralTVO[] getWarehousestock(TbOutgeneralBVO item,
			String pk_stordoc, boolean type) throws Exception {
		// 库存表中数组对象
		StockInvOnHandVO[] stockVO = null;
		// 设置where语句
		String sWhere = null;
		// 获取访问数据库对象
		IUAPQueryBS IUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		// 查询出来的集合
		List list = CommonUnit.getStockDetailByPk_User(ClientEnvironment
				.getInstance().getUser().getPrimaryKey(), item
				.getCinventoryid());
		// 托盘指定表数组
		TbOutgeneralTVO[] generalTVO = null;
		ArrayList<TbOutgeneralTVO> genltList = new ArrayList<TbOutgeneralTVO>();
		// 判断结果集是否为空
		if (null != list && list.size() > 0) {
			// 库存表数组赋值
			stockVO = new StockInvOnHandVO[list.size()];
			stockVO = (StockInvOnHandVO[]) list.toArray(stockVO);

			// 循环库存表
			for (int i = 0; i < stockVO.length; i++) {
				// 是否赠品，控制显示本品或者赠品的托盘
				if (type) {
					sWhere = " dr = 0 and cfirstbillhid = '"
							+ item.getCsourcebillhid()
							+ "' and  cfirstbillbid <> '"
							+ item.getCsourcebillbid()
							+ "' and pk_invbasdoc ='" + item.getCinventoryid()
							+ "' and cdt_pk='" + stockVO[i].getPplpt_pk()
							+ "' ";
					ArrayList tmpList = (ArrayList) IUAPQueryBS
							.retrieveByClause(TbOutgeneralTVO.class, sWhere);
					if (null != tmpList && tmpList.size() > 0)
						continue;
				}
				// 创建单个对象
				TbOutgeneralTVO generaltvo = new TbOutgeneralTVO();

				// 如果上面没有查询出记录 说明此条记录还没有存储过 现在逐个判断字段是否为空 再给单个对象赋值
				// 来源单据主键 这条数据不是查询数来的 而是调用者传过来的对象里面的值，这里需要记录
				// 在上面查询的where语句中需要这个条件
				if (null != item.getCsourcebillhid()
						&& !"".equals(item.getCsourcebillhid())) {
					generaltvo.setCfirstbillhid(item.getCsourcebillhid()); // 源头主表
				}
				if (null != item.getCsourcebillbid()
						&& !"".equals(item.getCsourcebillbid())) { // 源头子表
					generaltvo.setCfirstbillbid(item.getCsourcebillbid());
				}
				if (null != item.getVsourcebillcode()
						&& !"".equals(item.getVsourcebillcode())) {
					generaltvo.setVsourcebillcode(item.getVsourcebillcode()); // 来源单据号
				}
				if (null != stockVO[i].getPplpt_pk()
						&& !"".equals(stockVO[i].getPplpt_pk())) {
					generaltvo.setCdt_pk(stockVO[i].getPplpt_pk()); // 托盘主键
				}
				if (null != stockVO[i].getWhs_stockpieces()
						&& !"".equals(stockVO[i].getWhs_stockpieces())) {
					generaltvo.setStockpieces(stockVO[i].getWhs_stockpieces()); // 库存数量
				}
				if (null != stockVO[i].getWhs_stocktonnage()
						&& !"".equals(stockVO[i].getWhs_stocktonnage())) {
					generaltvo
							.setStocktonnage(stockVO[i].getWhs_stocktonnage()); // 库存辅数量
				}
				if (null != stockVO[i].getPk_invbasdoc()
						&& !"".equals(stockVO[i].getPk_invbasdoc())) {
					generaltvo.setPk_invbasdoc(stockVO[i].getPk_invbasdoc()); // 库存主键
				}
				if (null != stockVO[i].getWhs_batchcode()
						&& !"".equals(stockVO[i].getWhs_batchcode())) {
					generaltvo.setVbatchcode(stockVO[i].getWhs_batchcode()); // 批次
				}
				if (null != stockVO[i].getWhs_pk()
						&& !"".equals(stockVO[i].getWhs_pk())) {
					generaltvo.setWhs_pk(stockVO[i].getWhs_pk()); // 库存表主键
				}
				if (null != stockVO[i].getWhs_lbatchcode()
						&& !"".equals(stockVO[i].getWhs_lbatchcode())) {
					generaltvo.setLvbatchcode(stockVO[i].getWhs_lbatchcode()); // 来源批次
				}
				if (null != stockVO[i].getWhs_nprice()
						&& !"".equals(stockVO[i].getWhs_nprice())) {
					generaltvo.setNprice(stockVO[i].getWhs_nprice()); // 单价
				}
				if (null != stockVO[i].getWhs_nmny()
						&& !"".equals(stockVO[i].getWhs_nmny())) {
					generaltvo.setNmny(stockVO[i].getWhs_nmny()); // 金额
				}
				genltList.add(generaltvo);
			}
		}
		if (genltList.size() > 0) {
			generalTVO = new TbOutgeneralTVO[genltList.size()];
			generalTVO = genltList.toArray(generalTVO);
			genltList.clear();
		}
		// 现在的操作是查询出已经在托盘指定表中存储的数据 进行查询
		// 设置where语句 删除标志为0的 和 来源表头单据主键 和 库存档案主键
		sWhere = " dr = 0 and cfirstbillbid = '" + item.getCsourcebillbid()
				+ "' and pk_invbasdoc ='" + item.getCinventoryid() + "' ";
		// 操作数据库得到结果集
		ArrayList generaltList = (ArrayList) IUAPQueryBS.retrieveByClause(
				TbOutgeneralTVO.class, sWhere);
		// 判断结果集是否为空
		if (null != generaltList && generaltList.size() > 0) {
			// 给属性赋值 记录那些数据是存过的 等到后面删除的时候好作为参数传递
			this.generaltvoList = generaltList;
			for (int i = 0; i < generaltList.size(); i++) {
				boolean isadd = false;
				if (null != generalTVO) {
					for (int j = 0; j < generalTVO.length; j++) {
						if (((TbOutgeneralTVO) generaltList.get(i))
								.getWhs_pk().equals(
										generalTVO[j].getWhs_pk())) {
							generalTVO[j] = ((TbOutgeneralTVO) generaltList
									.get(i));
							isadd = true;
							break;
						}
					}
				}
				if (!isadd)
					genltList.add((TbOutgeneralTVO) generaltList.get(i));
			}
		}
		if (null != generalTVO) {
			for (int i = 0; i < generalTVO.length; i++) {
				genltList.add(generalTVO[i]);
			}
		}
		if (genltList.size() > 0) {
			generalTVO = new TbOutgeneralTVO[genltList.size()];
			generalTVO = genltList.toArray(generalTVO);

			return generalTVO;
		}
		return null;
	}

	protected BillListPanel getbillListPanel() {
		if (ivjbillListPanel == null) {
			try {
				ivjbillListPanel = new BillListPanel();
				ivjbillListPanel.setName("billListPanel");
				// 获的显示位数值
				// 装载模板
				nc.vo.pub.bill.BillTempletVO vo = ivjbillListPanel
						.getDefaultTemplet(getBillType(), null,
						/* getBusinessType(), */getOperator(), getPkCorp(),
								getNodeKey());

				BillListData billDataVo = new BillListData(vo);

				ivjbillListPanel.setListData(billDataVo);

				ivjbillListPanel.setMultiSelect(true);
				ivjbillListPanel.getChildListPanel().setTotalRowShow(true);
				ivjbillListPanel.setEnabled(true);
			} catch (java.lang.Throwable e) {
				Logger.error(e.getMessage(), e);
			}
		}
		return ivjbillListPanel;
	}

	/**
	 * 增加单据模版
	 * <li>该方法被PfUtilClient.childButtonClicked()调用
	 */
	public void addBillUI() {
		// 增加模版调用
		// getUIDialogContentPane().add(getbillListPanel(),
		// BorderLayout.CENTER);
		// 增加对控件监听
		addListenerEvent();
	}

	// 监听
	public void addListenerEvent() {
		getbtnOk().addActionListener(this);
		getbtnCancel().addActionListener(this);
		getbillListPanel().addEditListener(this);
		getbillListPanel().addMouseListener(this);
		getbillListPanel().addBodyEditListener(this); // 表体编辑后事件监听
		// 表头列表 行切换事件处理器
		getbillListPanel().getParentListPanel().getTable().getSelectionModel()
				.addListSelectionListener(this);
	}

	protected JPanel getUIDialogContentPane() {
		if (ivjUIDialogContentPane == null) {

			ivjUIDialogContentPane = new JPanel();
			ivjUIDialogContentPane.setName("UIDialogContentPane");
			ivjUIDialogContentPane.setLayout(new BorderLayout());
			//
			ivjUIDialogContentPane.add(getbillListPanel(), "Center");
			ivjUIDialogContentPane.add(getPanlCmd(), BorderLayout.SOUTH);
		}
		return ivjUIDialogContentPane;
	}

	private UIPanel getPanlCmd() {
		if (ivjPanlCmd == null) {
			ivjPanlCmd = new UIPanel();
			ivjPanlCmd.setName("PanlCmd");
			ivjPanlCmd.setPreferredSize(new Dimension(0, 40));
			ivjPanlCmd.setLayout(new FlowLayout());
			ivjPanlCmd.add(getbtnOk(), getbtnOk().getName());
			ivjPanlCmd.add(getbtnCancel(), getbtnCancel().getName());
		}
		return ivjPanlCmd;
	}

	// 添加确定按钮
	private UIButton getbtnOk() {
		if (ivjbtnOk == null) {
			ivjbtnOk = new UIButton();
			ivjbtnOk.setName("btnOk");
			ivjbtnOk.setText(NCLangRes.getInstance().getStrByID("common",
					"UC001-0000044")/* @res "确定" */);
		}
		return ivjbtnOk;
	}

	// 添加取消按钮
	private UIButton getbtnCancel() {
		if (ivjbtnCancel == null) {

			ivjbtnCancel = new UIButton();
			ivjbtnCancel.setName("btnCancel");
			ivjbtnCancel.setText(NCLangRes.getInstance().getStrByID("common",
					"UC001-0000008")/* @res "取消" */);
		}
		return ivjbtnCancel;
	}

	public AggregatedValueObject[] getRetVos() {
		return retBillVos;
	}

	public UIPanel getIvjPanlCmd() {
		return ivjPanlCmd;
	}

	// 点击按钮后的监听事件
	public void actionPerformed(ActionEvent e) {
		// 判断是否为取消按钮
		if (e.getSource().equals(getbtnCancel())) {
			// 关闭窗体
			this.closeCancel();
		}
		// 判断是否为确定按钮
		if (e.getSource().equals(getbtnOk())) {
			// 获取当前页面中表体的对象
			TbOutgeneralTVO[] item = (TbOutgeneralTVO[]) getbillListPanel()
					.getBillListData().getBodyBillModel()
					.getBodyValueChangeVOs(TbOutgeneralTVO.class.getName());
			// 判断是否为空
			if (null != item && item.length > 0) {
				double total = 0; // 实出辅数量
				double totalnum = 0; // 实出数量
				// 存储的批次集合
				List<String> batch = new ArrayList<String>();
				String vbatchcode = "";// 最终批次号
				String lvbatchcode = ""; // 来源批次
				double nprice = 0; // 单价
				double nmny = 0; // 金额
				// 循环表体数组
				for (int i = 0; i < item.length; i++) {
					// 创建单个对象
					TbOutgeneralTVO generalvo = item[i];
					// 判断对象中的实出辅数量是否为空
					if (null != generalvo.getNoutassistnum()
							&& !"".equals(generalvo.getNoutassistnum())) {
						// 如果不是空 实出辅数量进行数据累加
						total = total
								+ Double.parseDouble(generalvo
										.getNoutassistnum().toString());
						// 实出数量
						totalnum = totalnum
								+ Double.parseDouble(generalvo.getNoutnum()
										.toString());
						// 为批次集合添加值
						batch.add(generalvo.getVbatchcode());
						// 来源批次
						lvbatchcode = generalvo.getLvbatchcode();
						// 单价
						if (null != generalvo.getNprice())
							nprice = generalvo.getNprice().toDouble();
						// 金额
						if (null != generalvo.getNmny())
							nmny = generalvo.getNmny().toDouble();
					}
				}
				// 判断表头上的应出辅数量是否为空
				if (null != this.getGeneralbvo().getNshouldoutassistnum()
						&& !"".equals(this.getGeneralbvo()
								.getNshouldoutassistnum())) {
					// 判断实出辅数量是否大于应出辅数量
					if (total > this.getGeneralbvo().getNshouldoutassistnum()
							.toDouble()) {
						// 如果大于弹出对话框进行提示
						((ToftPanel) myClientUI)
								.showWarningMessage("实发数量超出应发数量,请重新输入!");
					} else {
						// 如果没有大于 创建存储用户选择托盘集合
						List<TbOutgeneralTVO> generaltList = new ArrayList<TbOutgeneralTVO>();
						// 循环表体数据
						for (int i = 0; i < item.length; i++) {
							// 获取单个对象
							TbOutgeneralTVO generalvo = item[i];
							// 最后判断实出辅数量是否为空
							if (null != generalvo.getNoutassistnum()
									&& !"".equals(generalvo.getNoutassistnum())) {
								// 给对象中删除标志赋值 因为该单据不会为删除标志赋值 所以需要现在赋值
								generalvo.setDr(0);
								// 把当前的对象传给集合
								generaltList.add(generalvo);
							}
						}
						// 调用接口添加方法
						try {
							// 调用接口中的添加方法 在这个方法中包含了删除 先是删除以前所添加过的记录
							// 因为当前页面中的数据可能会执行两个动作，更新和添加，所以数据不好操作，所以需要传递第一个参数
							// 第二个参数是为了添加用的，事物同步
							iw.insertGeneralTVO(this.getGeneraltvoList(),
									generaltList);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						// 判断如果实出辅数量总和是否为空
						if (total > 0) {
							// 判断批次集合是否为空
							if (null != batch && batch.size() > 0) {
								// 最终批次赋值
								vbatchcode = batch.get(0);
								// 循环批次集合
								for (int i = 0; i < batch.size(); i++) {
									// 判断批次集合是否有一个
									if (i == batch.size() - 1
											|| batch.size() == 1)
										break;
									// 如果批次集合中的批次不是同一个批次的进行批次相加
									if (!batch.get(i).equals(batch.get(i + 1)))
										// 为最终批次相加
										vbatchcode = vbatchcode + ","
												+ batch.get(i + 1);
								}
							}
							// 给页面赋值
							try {
								setUIData(
										total,
										totalnum,
										vbatchcode,
										lvbatchcode,
										nprice,
										nmny,
										CommonUnit
												.getCargDocName(ClientEnvironment
														.getInstance()
														.getUser()
														.getPrimaryKey()));
							} catch (BusinessException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						} else {
							// 给页面赋值
							setUIData(null, null, null, null, null, null, null);
						}
						this.closeCancel();
					}
				}

			}
		}
	}

	/**
	 * 给调用者单据页面赋值
	 * 
	 * @param noutassistnum
	 *            实出辅数量
	 * @param noutnum
	 *            实出主数量
	 * @param vbatchcode
	 *            批次
	 */
	private void setUIData(Object noutassistnum, Object noutnum,
			Object vbatchcode, Object lvbatchcode, Object nprice, Object nmny,
			Object cspaceid) {
		// 给调用者的实出辅数量赋值 this.getIndex()是调用者所选择的行
		((BillManageUI) myClientUI).getBillCardPanel().setBodyValueAt(
				noutassistnum, this.getIndex(), "noutassistnum");
		// 给实出数量赋值
		((BillManageUI) myClientUI).getBillCardPanel().setBodyValueAt(noutnum,
				this.getIndex(), "noutnum");
		// 给调用者批次赋值
		((BillManageUI) myClientUI).getBillCardPanel().setBodyValueAt(
				vbatchcode, this.getIndex(), "vbatchcode");
		// 给调用者来源批次赋值
		((BillManageUI) myClientUI).getBillCardPanel().setBodyValueAt(
				lvbatchcode, this.getIndex(), "lvbatchcode");
		// 给调用者来单价赋值
		((BillManageUI) myClientUI).getBillCardPanel().setBodyValueAt(nprice,
				this.getIndex(), "nprice");
		// 给调用者来金额赋值
		((BillManageUI) myClientUI).getBillCardPanel().setBodyValueAt(nmny,
				this.getIndex(), "nmny");
		// 给调用者来货位ID赋值
		((BillManageUI) myClientUI).getBillCardPanel().setBodyValueAt(cspaceid,
				this.getIndex(), "cspaceid");
	}

	// 编辑后事件
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub

		if (e.getKey().equals("noutassistnum")) {
			// 获取实出辅数量
			Object o = getbillListPanel().getBillListData().getBodyItem(
					"noutassistnum").getValueObject();
			// 给实出数量清空
			getbillListPanel().getBillListData().getBodyBillModel().setValueAt(
					null, getbillListPanel().getBodyTable().getSelectedRow(),
					"noutnum");
			double num1 = 0; // 实出辅数量
			double num2 = 0; // 库存辅数量
			if (null != o && !"".equals(o)) {
				num1 = Double.parseDouble(o.toString()); // 转换类型
				if (num1 == 0) {
					getbillListPanel().getBillListData().getBodyBillModel()
							.setValueAt(
									null,
									getbillListPanel().getBodyTable()
											.getSelectedRow(), "noutassistnum");
					return;
				}
				if (null != this.getGeneralbvo() // 判断换算率是否为空
						&& !"".equals(this.getGeneralbvo().getHsl())) {
					// 获取库存辅数量
					Object stockpieces = getbillListPanel().getBillListData()
							.getBodyBillModel().getValueAt(
									getbillListPanel().getBodyTable()
											.getSelectedRow(), "stockpieces");
					// 判断应出辅数量是否为空
					if (null != stockpieces && !"".equals(stockpieces)) {
						// 转换类型
						double stock = Double.parseDouble(stockpieces
								.toString());
						// 判断实出辅数量是否大于库存辅数量
						if (num1 > stock) {
							// 如果大于把页面中的数值清空
							getbillListPanel().getBillListData()
									.getBodyBillModel().setValueAt(
											null,
											getbillListPanel().getBodyTable()
													.getSelectedRow(),
											"noutassistnum");
							// 弹出警告
							((ToftPanel) myClientUI)
									.showWarningMessage("实发数量超出库存数量,请重新输入!");

							return;
						}
					}
					// 算出实出数量
					num2 = Double.parseDouble(this.getGeneralbvo().getHsl()
							.toString());
					// 为页面中填冲值
					getbillListPanel().getBillListData().getBodyBillModel()
							.setValueAt(
									num1 * num2,
									getbillListPanel().getBodyTable()
											.getSelectedRow(), "noutnum");

				}
			}

		}
	}

	public void bodyRowChange(BillEditEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouse_doubleclick(BillMouseEnent e) {
		// TODO Auto-generated method stub

	}

	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub

	}

	public TbOutgeneralBVO getGeneralbvo() {
		return generalbvo;
	}

	public void setGeneralbvo(TbOutgeneralBVO generalbvo) {
		this.generalbvo = generalbvo;
	}

	public List<TbOutgeneralTVO> getGeneraltvoList() {
		return generaltvoList;
	}

	public void setGeneraltvoList(List<TbOutgeneralTVO> generaltvoList) {
		this.generaltvoList = generaltvoList;
	}

	public Iw8004040204 getIw() {
		return iw;
	}

	public void setIw(Iw8004040204 iw) {
		this.iw = iw;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
