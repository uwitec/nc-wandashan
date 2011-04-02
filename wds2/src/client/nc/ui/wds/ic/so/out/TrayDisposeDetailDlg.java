package nc.ui.wds.ic.so.out;

import java.awt.BorderLayout;
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
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillMouseEnent;
import nc.ui.pub.bill.BillTableMouseListener;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.other.out.TbOutgeneralTVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFDouble;

public class TrayDisposeDetailDlg extends UIDialog implements ActionListener,
		BillEditListener, BillTableMouseListener, ListSelectionListener {

	// 存储传过来的表体对象
	private TbOutgeneralBVO[] generalBVO;

	private TbOutgeneralTVO[] generalTVO;

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

	public TrayDisposeDetailDlg(String m_billType, String m_operator,
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
		setName("BillSourceUI");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(750, 550);
		setTitle("查看托盘详细");
		setContentPane(getUIDialogContentPane());
	}

	public AggregatedValueObject[] getReturnVOs(java.awt.Container parent,
			TbOutgeneralBVO[] item) throws Exception {

		loadHeadData(item);
		addBillUI();
		if (showModal() == nc.ui.pub.beans.MessageDialog.ID_OK) {
			// 获取所选VO
			return getRetVos();
		}

		return null;
	}

	// 加载数据 为页面传值
	public void loadHeadData(TbOutgeneralBVO[] item) throws Exception {
		// 判断页面传来的表体对象是否为空
		if (null != item) {
			TbOutgeneralTVO generaltvo = new TbOutgeneralTVO();

			// 调用方法 根据传来的表体对象 查询数据库中的库存信息表，把符合的数据提取出来组装成当前单据中表体VO对象
			this.getWarehousestock(item);
			// 给当前单据表头赋值
			this.getbillListPanel().getBillListData().setHeaderValueVO(
					this.getGeneralBVO());
			// 给当前单据表体赋值
			// this.getbillListPanel().getBillListData()
			// .setBodyValueVO(generalTVO);
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
	public TbOutgeneralTVO[] getWarehousestock(TbOutgeneralBVO[] item)
			throws Exception {
		List<TbOutgeneralTVO> generaltvoList = new ArrayList<TbOutgeneralTVO>();
		IUAPQueryBS IUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		if (null != item && item.length > 0) {
			for (int i = 0; i < item.length; i++) {
				String sWhere = " dr = 0 and cfirstbillhid = '"
						+ item[i].getCsourcebillhid() + "' and pk_invbasdoc ='"
						+ item[i].getCinventoryid() + "' and cfirstbillbid='"
						+ item[i].getCsourcebillbid() + "'";
				// 操作数据库得到结果集
				ArrayList generaltList = (ArrayList) IUAPQueryBS
						.retrieveByClause(TbOutgeneralTVO.class, sWhere);
				// 判断结果集是否为空
				if (null != generaltList && generaltList.size() > 0) {

					TbOutgeneralTVO[] generaltvo = new TbOutgeneralTVO[generaltList
							.size()];
					generaltvo = (TbOutgeneralTVO[]) generaltList
							.toArray(generaltvo);
					double sum = 0;
					for (int j = 0; j < generaltvo.length; j++) {
						sum = sum + generaltvo[j].getNoutassistnum().toDouble();
						generaltvoList.add(generaltvo[j]);
					}
					// 给页面上实出辅数量赋值
					if (sum != 0)
						item[i].setNoutassistnum(new UFDouble(sum));
				}
			}
			TbOutgeneralTVO[] generaltvo = new TbOutgeneralTVO[generaltvoList
					.size()];
			generaltvo = (TbOutgeneralTVO[]) generaltvoList.toArray(generaltvo);
			this.setGeneralBVO(new TbOutgeneralBVO[item.length]);
			// 给存储表体赋值
			this.setGeneralBVO(item);
			this.setGeneralTVO(generaltvo);
			return generaltvo;
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
		// /getUIDialogContentPane().add(getbillListPanel(),
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

		// 表头列表 行切换事件处理器
		getbillListPanel().getParentListPanel().getTable().getSelectionModel()
				.addListSelectionListener(this);
	}

	protected JPanel getUIDialogContentPane() {
		if (ivjUIDialogContentPane == null) {

			ivjUIDialogContentPane = new JPanel();
			ivjUIDialogContentPane.setName("UIDialogContentPane");
			ivjUIDialogContentPane.setLayout(new BorderLayout());
			// 2003-05-12平台进行显示调用
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
					"UC001-0000008")/*
									 * @res "取消"
									 */);
		}
		return ivjbtnCancel;
	}

	public AggregatedValueObject[] getRetVos() {
		return retBillVos;
	}

	public UIPanel getIvjPanlCmd() {
		return ivjPanlCmd;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(getbtnCancel())) {
			this.closeCancel();
		}
		if (e.getSource().equals(getbtnOk())) {
			this.closeCancel();
		}

	}

	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub

	}

	public void bodyRowChange(BillEditEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouse_doubleclick(BillMouseEnent e) {
		// TODO Auto-generated method stub

	}

	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		if (getbillListPanel().getHeadTable().getSelectedRowCount() > 0) {
			double sum1 = 0; // zhu
			double sum2 = 0; // fu
			// 获取所选择行的Id
			Object o = getbillListPanel().getHeadBillModel().getValueAt(
					getbillListPanel().getHeadTable().getSelectedRow(),
					"csourcebillbid");
			// 判断是否为空
			if (o != null && o != "") {
				List<TbOutgeneralTVO> generaltvo = new ArrayList<TbOutgeneralTVO>();
				String genb_pk = o.toString();
				for (int i = 0; i < this.getGeneralTVO().length; i++) {
					String gent_pk = this.getGeneralTVO()[i].getCfirstbillbid();
					if (genb_pk.equals(gent_pk)) {
						// 如果所选择的行id不为空 根据它去缓存里查询出子表Vo 进行显示
						generaltvo.add(this.getGeneralTVO()[i]);
						sum1 = sum1
								+ this.getGeneralTVO()[i].getNoutassistnum()
										.toDouble();
						sum2 = sum2
								+ this.getGeneralTVO()[i].getNoutnum()
										.toDouble();
					}
				}
				TbOutgeneralTVO[] item = new TbOutgeneralTVO[generaltvo.size()];
				item = generaltvo.toArray(item);
				getbillListPanel().setBodyValueVO(item);
//				getbillListPanel().getBodyBillModel().getTotalTableModel()
//						.setValueAt(sum1, 0, 7); // 辅数量
//				getbillListPanel().getBodyBillModel().getTotalTableModel()
//						.setValueAt(
//								new BigDecimal(sum2).setScale(6,
//										BigDecimal.ROUND_HALF_UP), 0, 6); // 主数量
			}
		}

		// getbillListPanel().getHeadBillModel().execLoadFormula();
		this.getbillListPanel().getBodyBillModel().execLoadFormula();
	}

	public TbOutgeneralBVO[] getGeneralBVO() {
		return generalBVO;
	}

	public void setGeneralBVO(TbOutgeneralBVO[] generalBVO) {
		this.generalBVO = generalBVO;
	}

	public TbOutgeneralTVO[] getGeneralTVO() {
		return generalTVO;
	}

	public void setGeneralTVO(TbOutgeneralTVO[] generalTVO) {
		this.generalTVO = generalTVO;
	}

}
