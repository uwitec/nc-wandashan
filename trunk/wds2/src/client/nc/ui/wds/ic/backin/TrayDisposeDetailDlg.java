package nc.ui.wds.ic.backin;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillMouseEnent;
import nc.ui.pub.bill.BillTableMouseListener;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.pub.TbGeneralBBVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;

//import nc.vo.wds.w8004040210.TbGeneralBVO;

public class TrayDisposeDetailDlg extends nc.ui.pub.beans.UIDialog implements
		ActionListener, BillEditListener, BillTableMouseListener,
		ListSelectionListener {
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

	private TbOutgeneralBVO chlid = null;

	private TbOutgeneralBVO[] chlids = null;

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

	public TbOutgeneralBVO getChlid() {
		return chlid;
	}

	public void setChlid(TbOutgeneralBVO chlid) {
		this.chlid = chlid;
	}

	public TrayDisposeDetailDlg(String m_billType, String m_operator,
			String m_pkcorp, String m_nodeKey, TbOutgeneralBVO[] chlids) {
		init(m_billType, m_operator, m_pkcorp, m_nodeKey, chlids);
	}

	private void init(String m_billType, String m_operator, String m_pkcorp,
			String m_nodeKey, TbOutgeneralBVO[] chlids) {
		this.m_billType = m_billType;
		this.m_operator = m_operator;
		this.m_pkcorp = m_pkcorp;
		this.m_nodeKey = m_nodeKey;
		this.chlids = chlids;
		this.chlid = chlids[0];

		setName("BillSourceUI");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(750, 550);
		setTitle("查看明细");
		setContentPane(getUIDialogContentPane());
	}

	public AggregatedValueObject[] getReturnVOs(TbOutgeneralBVO[] childs) {

		loadHeadData(childs);
		addBillUI();
		if (showModal() == nc.ui.pub.beans.MessageDialog.ID_OK) {
			// 获取所选VO
			return getRetVos();
		}

		return null;
	}

	public void loadHeadData(TbOutgeneralBVO[] childs) {
		// 获得表头
		TbOutgeneralBVO[] tbOutgeneralBVOs = childs;

		// 获得表体
		StringBuffer sql = new StringBuffer(
				"select cdt_pk from bd_cargdoc_tray where cdt_invbasdoc='"
						+ tbOutgeneralBVOs[0].getCinventoryid() + "' and dr=0 ");
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		// 符合条件的全部托盘
		ArrayList cdts = new ArrayList();
		try {
			cdts = (ArrayList) query.executeQuery(sql.toString(),
					new ArrayListProcessor());

		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TbGeneralBBVO[] tbGeneralBBVO = null;
		// 子表
		StringBuffer tbbsql = new StringBuffer(" geb_pk='");
		tbbsql.append(childs[0].getCsourcebillbid());
		tbbsql.append("' and dr=0 ");
		ArrayList tbGeneralBBVOs = new ArrayList();
		try {
			tbGeneralBBVOs = (ArrayList) query.retrieveByClause(
					TbGeneralBBVO.class, tbbsql.toString());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (null != tbGeneralBBVOs && tbGeneralBBVOs.size() > 0) {
			tbGeneralBBVO = new TbGeneralBBVO[tbGeneralBBVOs.size()];
			tbGeneralBBVOs.toArray(tbGeneralBBVO);
		}

		//
		getbillListPanel().setBodyValueVO(tbGeneralBBVO);
		getbillListPanel().getBodyBillModel().execLoadFormula();
		getbillListPanel().setHeaderValueVO(tbOutgeneralBVOs);
		getbillListPanel().getHeadBillModel().execLoadFormula();
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
		// getbillListPanel().addBodyEditListener(this);
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

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(getbtnOk())) {
			this.closeOK();
		}

		if (e.getSource().equals(getbtnCancel())) {
			this.closeCancel();
		}

	}

	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		if (e.getKey().equals("gebb_num")) {
			// 获得被选中的行数
			int selectRow = getbillListPanel().getBodyTable().getRowCount();
			// 托盘现存货量
			int traynum = Integer.parseInt(getbillListPanel()
					.getBodyBillModel().getValueAt(selectRow, "traynum")
					.toString());
			//
			getbillListPanel().getBodyBillModel().execLoadFormula();
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

		if (getbillListPanel().getHeadTable().getSelectedRowCount() > 0) {
			IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
					IUAPQueryBS.class.getName());
			// 获取所选择行的Id
			Object o = getbillListPanel().getHeadBillModel().getValueAt(
					getbillListPanel().getHeadTable().getSelectedRow(),
					"csourcebillbid");
			String geh_pk = "";
			if (null != o) {
				geh_pk = o.toString();
			}
			TbGeneralBBVO[] tbGeneralBBVO = null;
			// 子表
			StringBuffer tbbsql = new StringBuffer(" geb_pk='");
			tbbsql.append(geh_pk);
			tbbsql.append("' and dr=0 ");
			ArrayList tbGeneralBBVOs = new ArrayList();
			try {
				tbGeneralBBVOs = (ArrayList) query.retrieveByClause(
						TbGeneralBBVO.class, tbbsql.toString());
			} catch (BusinessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (null != tbGeneralBBVOs && tbGeneralBBVOs.size() > 0) {
				tbGeneralBBVO = new TbGeneralBBVO[tbGeneralBBVOs.size()];
				tbGeneralBBVOs.toArray(tbGeneralBBVO);
			}

			// double sum1 = 0;
			// for (int i = 0; i < tbGeneralBBVOs.size(); i++) {
			// if (null != tbGeneralBBVOs.get(0)) {
			// TbGeneralBBVO tbgebbvo = (TbGeneralBBVO) tbGeneralBBVOs
			// .get(0);
			// if (null != tbgebbvo.getGebb_num()
			// && !"".equals(tbgebbvo.getGebb_num().doubleValue())) {
			// sum1 += tbgebbvo.getGebb_num().doubleValue();
			// }
			// }
			// }
			getbillListPanel().setBodyValueVO(tbGeneralBBVO);
			// getbillListPanel().getBodyBillModel().getTotalTableModel()
			// .setValueAt(sum1, 0, 6);
		}
		// getbillListPanel().getBodyBillModel().getTotalTableModel().setValueAt(
		// new BigDecimal(sum2).setScale(6, BigDecimal.ROUND_HALF_UP), 0,
		// 6);

		getbillListPanel().getBodyBillModel().execLoadFormula();
	}

	public TbOutgeneralBVO[] getChlids() {
		return chlids;
	}

	public void setChlids(TbOutgeneralBVO[] chlids) {
		this.chlids = chlids;
	}

}
