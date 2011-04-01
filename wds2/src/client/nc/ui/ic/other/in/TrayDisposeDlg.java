package nc.ui.ic.other.in;

import java.awt.BorderLayout;
import java.awt.Container;
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
import nc.ui.pf.pub.PfUIDataCache;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillMouseEnent;
import nc.ui.pub.bill.BillTableMouseListener;
import nc.vo.ic.pub.TbGeneralBBVO;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.wds.w8004040210.MyBillVO;
import nc.vo.wds.w8004040212.TbWarehousestockVO;

public class TrayDisposeDlg extends nc.ui.pub.beans.UIDialog implements
		ActionListener, BillEditListener, BillTableMouseListener,
		ListSelectionListener {

	private Container myClientUI;
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

	private TbGeneralBVO child = null;

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

	public TbGeneralBVO getChild() {
		return child;
	}

	public void setChlid(TbGeneralBVO child) {
		this.child = child;
	}

	public TrayDisposeDlg(String m_billType, String m_operator,
			String m_pkcorp, String m_nodeKey, TbGeneralBVO chlid,
			Container myClientUI) {
		this.myClientUI = myClientUI;
		init(m_billType, m_operator, m_pkcorp, m_nodeKey, chlid);

	}
	
	/**
	 * zhf add
	 * @param m_billType
	 * @param m_operator
	 * @param m_pkcorp
	 * @param m_nodeKey
	 * @param myClientUI
	 */
	public TrayDisposeDlg(String m_billType, String m_operator,
			String m_pkcorp, String m_nodeKey, 
			Container myClientUI) {
		this.myClientUI = myClientUI;
		init(m_billType, m_operator, m_pkcorp, m_nodeKey);

	}

	private void init(String m_billType, String m_operator, String m_pkcorp,
			String m_nodeKey, TbGeneralBVO child) {
		this.m_billType = m_billType;
		this.m_operator = m_operator;
		this.m_pkcorp = m_pkcorp;
		this.m_nodeKey = m_nodeKey;
		this.child = child;
		getbtnOk().addActionListener(this);
		getbtnCancel().addActionListener(this);
		setName("BillSourceUI");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(750, 550);
		setTitle("选择托盘");
		setContentPane(getUIDialogContentPane());
	}
	private void init(String m_billType, String m_operator, String m_pkcorp,
			String m_nodeKey) {
		this.m_billType = m_billType;
		this.m_operator = m_operator;
		this.m_pkcorp = m_pkcorp;
		this.m_nodeKey = m_nodeKey;
//		this.child = child;
		getbtnOk().addActionListener(this);
		getbtnCancel().addActionListener(this);
		setName("BillSourceUI");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(750, 550);
		setTitle("选择托盘");
		setContentPane(getUIDialogContentPane());
	}


	public TbGeneralBVO getReturnVOs(TbGeneralBVO child) {

		loadHeadData(child);
		addBillUI();
		if (showModal() == nc.ui.pub.beans.MessageDialog.ID_OK) {
			// 获取所选VO
			return getM_tbGeneralBVO();
		}

		return null;
	}

	// 判断用户身份
	private String st_type = "";
	// 判断是总仓还是分仓
	private boolean sotckIsTotal = true;
	// 仓库主键
	private String stordocName = "";

	public void loadHeadData(TbGeneralBVO child) {

		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		//
		try {
			st_type = nc.ui.wds.w8000.CommonUnit.getUserType(ClientEnvironment
					.getInstance().getUser().getPrimaryKey());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (null != st_type && !"".equals(st_type)) {
			if ("0".equals(st_type)) {
				try {
					stordocName = nc.ui.wds.w8000.CommonUnit
							.getStordocName(ClientEnvironment.getInstance()
									.getUser().getPrimaryKey());
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (null != stordocName && !"".equals(stordocName)) {
					try {
						sotckIsTotal = nc.ui.wds.w8000.CommonUnit
								.getSotckIsTotal(stordocName);

					} catch (BusinessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			if ("3".equals(st_type)) {
				try {
					stordocName = nc.ui.wds.w8000.CommonUnit
							.getStordocName(ClientEnvironment.getInstance()
									.getUser().getPrimaryKey());
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (null != stordocName && !"".equals(stordocName)) {
					try {
						sotckIsTotal = nc.ui.wds.w8000.CommonUnit
								.getSotckIsTotal(stordocName);

					} catch (BusinessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

		// 货位
		String pk_cargdoc = "";
		try {
			pk_cargdoc = nc.ui.wds.w8000.CommonUnit
					.getCargDocName(ClientEnvironment.getInstance().getUser()
							.getPrimaryKey());
		} catch (BusinessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// 获得表头
		TbGeneralBVO[] tbGeneralBVO = new TbGeneralBVO[1];
		tbGeneralBVO[0] = child;

		// String
		// 获得表体
		StringBuffer sql = new StringBuffer();
		if (sotckIsTotal) {
			sql = new StringBuffer(
					"select cdt_pk from bd_cargdoc_tray where cdt_invbasdoc='"
							+ tbGeneralBVO[0].getGeb_cinvbasid()
							+ "' and dr=0 and cdt_traystatus=0 and  pk_cargdoc='"
							+ pk_cargdoc + "' ");
			//
			String mySql = " select distinct cdt_pk from tb_general_b_b where pk_invbasdoc='"
					+ tbGeneralBVO[0].getGeb_cinvbasid()
					+ "' and pwb_pk !='"
					+ tbGeneralBVO[0].getGeb_cgeneralbid()
					+ "' and dr=0 and pwb_pk in ('";

			String mySql1 = " ";
			if (null != tbGeneralBVO[0].getGeb_cgeneralhid()
					&& !"".equals(tbGeneralBVO[0].getGeb_cgeneralhid())) {
				mySql1 = " geb_cgeneralhid='"
						+ tbGeneralBVO[0].getGeb_cgeneralhid() + "' and dr=0 ";

				ArrayList tbgenbvos = new ArrayList();
				try {
					tbgenbvos = (ArrayList) query.retrieveByClause(
							TbGeneralBVO.class, mySql1);
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				boolean myBoolean = false;
				if (null != tbgenbvos) {
					for (int j = 0; j < tbgenbvos.size(); j++) {
						if (null != tbGeneralBVO[0].getGeb_cinvbasid()
								&& null != tbgenbvos.get(0)
								&& null != ((TbGeneralBVO) tbgenbvos.get(j))
										.getGeb_cinvbasid()
								&& null != tbGeneralBVO[0].getGeb_cgeneralbid()
								&& null != ((TbGeneralBVO) tbgenbvos.get(j))
										.getGeb_cgeneralbid()) {
							if (tbGeneralBVO[0].getGeb_cinvbasid().equals(
									((TbGeneralBVO) tbgenbvos.get(j))
											.getGeb_cinvbasid())
									&& !tbGeneralBVO[0]
											.getGeb_cgeneralbid()
											.equals(
													((TbGeneralBVO) tbgenbvos
															.get(j))
															.getGeb_cgeneralbid())) {
								mySql += ((TbGeneralBVO) tbgenbvos.get(j))
										.getGeb_cgeneralbid()
										+ "','";
								myBoolean = true;
							}
						}
					}
				}
				mySql += "') ";
				if (myBoolean) {
					sql.append(" and cdt_pk not in (" + mySql + ")");
				}
			}

		} else {
			sql = new StringBuffer(
					"select cdt_pk from bd_cargdoc_tray where  dr=0 and  pk_cargdoc='"
							+ pk_cargdoc + "' ");
		}
		// 获得表体

		// 符合条件的全部托盘
		ArrayList cdts = new ArrayList();
		try {
			cdts = (ArrayList) query.executeQuery(sql.toString(),
					new ArrayListProcessor());

		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TbGeneralBBVO[] tbGeneralBBVO = new TbGeneralBBVO[cdts.size()];

		for (int i = 0; i < tbGeneralBBVO.length; i++) {
			Object[] cdtvo = (Object[]) cdts.get(i);
			tbGeneralBBVO[i] = new TbGeneralBBVO();
			if (null != cdtvo[0]) {
				tbGeneralBBVO[i].setCdt_pk(cdtvo[0].toString());
			}
			// 批次
			tbGeneralBBVO[i].setGebb_vbatchcode(child.getGeb_vbatchcode());
			// 回写批次
			tbGeneralBBVO[i].setGebb_lvbatchcode(child.getGeb_backvbatchcode());
			// 行号
			tbGeneralBBVO[i].setGebb_rowno(i + 1 + "0");
			// 运单表头主键
			tbGeneralBBVO[i].setPwb_pk(child.getGeb_cgeneralbid());
			// 换算率
			tbGeneralBBVO[i].setGebb_hsl(child.getGeb_hsl());
			// 运货档案主键
			tbGeneralBBVO[i].setPk_invbasdoc(child.getGeb_cinvbasid());
			// 入库单子表主键
			tbGeneralBBVO[i].setGeb_pk(child.getGeb_pk());
			// 单价
			tbGeneralBBVO[i].setGebb_nprice(child.getGeb_nprice());
			// 金额
			tbGeneralBBVO[i].setGebb_nmny(child.getGeb_nmny());
			// 根据运单表头主键查询本地表，将已有的项添加
			ArrayList tbbvos = new ArrayList();
			StringBuffer tbbsql = new StringBuffer(" pwb_pk='");
			tbbsql.append(child.getGeb_cgeneralbid());
			tbbsql.append("' and dr=0 ");
			if (null != cdtvo[0]) {
				tbbsql.append(" and cdt_pk='");
				tbbsql.append(cdtvo[0].toString());
				tbbsql.append("'");
			}
			try {
				tbbvos = (ArrayList) query.retrieveByClause(
						TbGeneralBBVO.class, tbbsql.toString());
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (sotckIsTotal) {
				if (tbbvos.size() != 0) {
					tbGeneralBBVO[i]
							.setGebb_num(((TbGeneralBBVO) tbbvos.get(0)).gebb_num);
				}
			} else {
				// tbGeneralBBVO[i].setGebb_num(new UFDouble());
				if (tbbvos.size() != 0) {
					tbGeneralBBVO[i]
							.setGebb_num(((TbGeneralBBVO) tbbvos.get(0)).gebb_num);
				}
			}
			// 现存量
			StringBuffer numsql = new StringBuffer(" pplpt_pk='");
			numsql.append(tbGeneralBBVO[i].getCdt_pk());
			numsql.append("' and dr=0");
			ArrayList whss = new ArrayList();
			try {
				whss = (ArrayList) query.retrieveByClause(
						TbWarehousestockVO.class, numsql.toString());
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (null != whss && whss.size() > 0) {
				tbGeneralBBVO[i].setGebb_customize1(((TbWarehousestockVO) whss
						.get(0)).getWhs_stockpieces().toString());
			}
			// if (sotckIsTotal) {
			// tbGeneralBBVO[i].setGebb_customize2("0");
			// } else {
			// tbGeneralBBVO[i].setGebb_customize2("1");
			// }

		}

		//
		getbillListPanel().setBodyValueVO(tbGeneralBBVO);
		getbillListPanel().getBodyBillModel().execLoadFormula();
		getbillListPanel().setHeaderValueVO(tbGeneralBVO);
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
		getbillListPanel().addEditListener(this);
		getbillListPanel().addMouseListener(this);

		// 表头列表 行切换事件处理器
		getbillListPanel().getParentListPanel().getTable().getSelectionModel()
				.addListSelectionListener(this);
		getbillListPanel().addBodyEditListener(this);
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

	// 单据vo,主表vo,子表vo
	protected String m_billVo = null;

	protected String m_billHeadVo = null;

	protected String m_billBodyVo = null;

	public String getM_billVo() {
		return m_billVo;
	}

	public String getM_billHeadVo() {
		return m_billHeadVo;
	}

	public String getM_billBodyVo() {
		return m_billBodyVo;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(getbtnOk())) {

//			// 获得自定义接口
//			Iw8004040210 iw = (Iw8004040210) NCLocator.getInstance().lookup(
//					Iw8004040210.class.getName());
//			// 获得表头VO
//			TbGeneralBVO[] tbs = (TbGeneralBVO[]) getbillListPanel()
//					.getHeadBillModel().getBodyValueVOs(
//							TbGeneralBVO.class.getName());
//			// 获得所有表体VO
//			TbGeneralBBVO[] tbGeneralBBVO = (TbGeneralBBVO[]) getbillListPanel()
//					.getBodyBillModel().getBodyValueVOs(
//							TbGeneralBBVO.class.getName());
//			// 获得billVO
//
//			IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
//					IUAPQueryBS.class.getName());
//
//			// 获得要删除的VO
//			StringBuffer tbbsql = new StringBuffer("pwb_pk='");
//			tbbsql.append(tbs[0].getGeb_cgeneralbid());
//			tbbsql.append("' and pk_invbasdoc='");
//			tbbsql.append(tbs[0].getGeb_cinvbasid());
//			tbbsql.append("' and dr = 0");
//			ArrayList dtbbvos = new ArrayList();
//			try {
//
//				dtbbvos = (ArrayList) query.retrieveByClause(
//						TbGeneralBBVO.class, tbbsql.toString());
//			} catch (BusinessException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			// 实入数量
//			double geb_banum = 0;
//			if (null != getbillListPanel().getHeadBillModel().getValueAt(0,
//					"geb_banum")) {
//				String geb_banums = getbillListPanel().getHeadBillModel()
//						.getValueAt(0, "geb_banum").toString();
//				geb_banum = Double.parseDouble(getbillListPanel()
//						.getHeadBillModel().getValueAt(0, "geb_banum")
//						.toString());
//			}
//			// 应入数量
//			double geb_bsnum = 0;
//			if (null != getbillListPanel().getHeadBillModel().getValueAt(0,
//					"geb_bsnum")) {
//				String geb_bsnums = getbillListPanel().getHeadBillModel()
//						.getValueAt(0, "geb_bsnum").toString();
//				geb_bsnum = Double.parseDouble(getbillListPanel()
//						.getHeadBillModel().getValueAt(0, "geb_bsnum")
//						.toString());
//			}
//			// 通过实存数量的判断，获得改变的表体VO
//			// TbGeneralBBVO[] tbGeneralBBVO1 = new
//			// TbGeneralBBVO[tbGeneralBBVO.length];
//
//			ArrayList tbgbbss = new ArrayList();
//			List tbbvos = new ArrayList();
//			// 托盘中入库数量
//			double traysum = 0;
//			// ArrayList bdCargdocTrayVOs = new ArrayList();
//			String Cdt_pk = "";
//			for (int i = 0; i < tbGeneralBBVO.length; i++) {
//				if (null != tbGeneralBBVO[i]) {
//					tbGeneralBBVO[i].setDr(0);
//					tbGeneralBBVO[i].setGeb_pk(tbs[0].getGeb_pk());
//					if (null != tbGeneralBBVO[i].getGebb_num()
//							&& !("".equals(tbGeneralBBVO[i].getGebb_num()) || 0 == tbGeneralBBVO[i]
//									.getGebb_num().toDouble())) {
//						// tbGeneralBBVO1[i] = tbGeneralBBVO[i];
//						tbgbbss.add(tbGeneralBBVO[i]);
//						traysum += tbGeneralBBVO[i].getGebb_num().toDouble();
//						// tbbvos.add(tbGeneralBBVO[i]);
//						// 托盘状态
//
//						Cdt_pk = getbillListPanel().getBodyBillModel()
//								.getValueAt(i, "cdt_pk").toString();
//						BdCargdocTrayVO bdCargdocTrayVO = new BdCargdocTrayVO();
//						try {
//							bdCargdocTrayVO = (BdCargdocTrayVO) query
//									.retrieveByPK(BdCargdocTrayVO.class, Cdt_pk);
//						} catch (BusinessException e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						}
//						// bdCargdocTrayVO.setCdt_traystatus(new Integer(1));
//						// bdCargdocTrayVOs.add(bdCargdocTrayVO);
//					}
//				}
//			}
//			// 通过实存数量的判断，获得改变的表体VO
//			TbGeneralBBVO[] tbGeneralBBVO1 = new TbGeneralBBVO[tbgbbss.size()];
//			tbgbbss.toArray(tbGeneralBBVO1);
//			if (traysum > geb_bsnum) {
//				((MyClientUI) myClientUI).showErrorMessage("托盘存放总数量大于应入数量！");
//				return;
//			}
//			// 换算率
//			double geb_hsl = 0;
//			if (null != getbillListPanel().getHeadBillModel().getValueAt(0,
//					"geb_hsl")) {
//				String geb_hsls = getbillListPanel().getHeadBillModel()
//						.getValueAt(0, "geb_hsl").toString();
//				geb_hsl = Double
//						.parseDouble(getbillListPanel().getHeadBillModel()
//								.getValueAt(0, "geb_hsl").toString());
//			}
//			//
//
//			// getbillListPanel().getHeadBillModel().setValueAt(traysum *
//			// geb_hsl,
//			// 0, "geb_anum");
//			// getbillListPanel().getHeadBillModel().setValueAt("1",
//			// 0, "geb_banum");
//			// 在途数量添加
//			if (null != tbs[0].getGeb_pk() && !"".equals(tbs[0].getGeb_pk())) {
//				String tbssql = " geb_pk='" + tbs[0].getGeb_pk()
//						+ "' and dr=0 ";
//				// +
//				// " and cdt_pk in (select cdt_pk from bd_cargdoc_tray where
//				// cdt_traystatus !=1 ) ";
//				ArrayList dtbvos = new ArrayList();
//				try {
//
//					dtbvos = (ArrayList) query.retrieveByClause(
//							TbGeneralBVO.class, tbssql.toString());
//				} catch (BusinessException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//				if (null != dtbvos && dtbvos.size() > 0) {
//					if (null != dtbvos.get(0)) {
//						TbGeneralBVO dtbvo = (TbGeneralBVO) dtbvos.get(0);
//						if (null != dtbvo.getGeb_banum()) {
//							traysum += dtbvo.getGeb_banum().doubleValue();
//						}
//					}
//				}
//
//			}
//			//
//			getbillListPanel().getHeadBillModel().setValueAt(traysum, 0,
//					"geb_banum");
//			//
//			tbs[0].setGeb_banum(new UFDouble(traysum));
//			tbs[0].setGeb_anum(new UFDouble(traysum * geb_hsl));
//			try {
//				iw.delAndInsertTbGeneralBBVO(dtbbvos, tbGeneralBBVO1);
//			} catch (BusinessException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			// 返回当前页面VO
//			this.m_tbGeneralBVO = tbs[0];
			this.closeOK();

		}

		if (e.getSource().equals(getbtnCancel())) {
			this.closeCancel();
		}

	}

	private TbGeneralBVO m_tbGeneralBVO = null;

	public TbGeneralBVO getM_tbGeneralBVO() {
		return m_tbGeneralBVO;
	}

	/**
	 * "确定"按钮的响应，从界面获取被选单据VO
	 */
	public void onOk() {
		if (getbillListPanel().getHeadBillModel().getRowCount() > 0) {
			AggregatedValueObject[] selectedBillVOs = getbillListPanel()
					.getMultiSelectedVOs(MyBillVO.class.getName(),
							TbGeneralBVO.class.getName(),
							TbGeneralBBVO.class.getName());
			retBillVos = selectedBillVOs;
		}
	}

	/**
	 * 获得单据类型VO类信息
	 * 
	 * <li>数组[0]=单据聚合Vo;数组[1]=单据主表Vo;数组[2]=单据子表Vo;
	 */
	public void getBillVO() {
		try {
			String[] retString = PfUIDataCache.getStrBillVo(getBillType());
			// MatchTableBO_Client.querybillVo(getBillType());
			// 0--单据vo;1-主表Vo;2-子表Vo;
			m_billVo = retString[0];
			m_billHeadVo = retString[1];
			m_billBodyVo = retString[2];
		} catch (Exception e1) {
			Logger.error(e1.getMessage(), e1);
		}
	}

	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		if (e.getKey().equals("gebb_num")) {
			// 获得被选中的行数
			int selectRow = getbillListPanel().getBodyTable().getSelectedRow();
			// 托盘现存货量
			int traynum = 0;
			if (null != getbillListPanel().getBodyBillModel().getValueAt(
					selectRow, "traynum")) {
				traynum = Integer.parseInt(getbillListPanel()
						.getBodyBillModel().getValueAt(selectRow, "traynum")
						.toString());
			}
			// 托盘最大容积
			int traymax = 0;
			if (null != getbillListPanel().getBodyBillModel().getValueAt(
					selectRow, "traymax")) {
				traymax = Integer.parseInt(getbillListPanel()
						.getBodyBillModel().getValueAt(selectRow, "traymax")
						.toString());
			}
			// 实存放数量
			double gebb_num = 0;
			if (null != getbillListPanel().getBodyBillModel().getValueAt(
					selectRow, "gebb_num")) {
				gebb_num = Double.parseDouble((getbillListPanel()
						.getBodyBillModel().getValueAt(selectRow, "gebb_num")
						.toString()));
			}

			if (sotckIsTotal) {
				if (gebb_num > (traymax - traynum)) {
					getbillListPanel().getBodyBillModel().setValueAt(null,
							selectRow, "gebb_num");
					((MyClientUI) myClientUI)
							.showErrorMessage("托盘存放数量大于托盘最大容积！");

				}
			}

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

	}

}
