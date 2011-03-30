package nc.ui.wds.w8004040218;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.wds.w8004040210.W8004040210Impl;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.itf.wds.w8004040210.Iw8004040210;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.ml.NCLangRes;
import nc.ui.pf.pub.PfUIDataCache;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.beans.ValueChangedEvent;
import nc.ui.pub.beans.ValueChangedListener;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillListPanel; //import nc.ui.to.to103.BatchDisposeDlg.BtnListener;
import nc.ui.pub.bill.BillMouseEnent;
import nc.ui.pub.bill.BillTableMouseListener; //import nc.ui.pub.pf.BillSourceDLG.HeadRowStateListener;
import nc.ui.to.to103.Const; //import nc.ui.to.to103.BatchDisposeDlg.ComboxListener;
import nc.ui.trade.base.IBillOperate;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.to.pub.ConstVO;
import nc.vo.wds.w8004040204.TbOutgeneralBVO;
import nc.vo.wds.w8004040210.MyBillVO;
import nc.vo.wds.w8004040210.TbGeneralBBVO; //import nc.vo.wds.w8004040210.TbGeneralBVO;
//import nc.vo.wds.w8004040210.TbGeneralHVO;
import nc.vo.wds.w8004040212.TbWarehousestockVO;
import nc.vo.wds.w8004061002.BdCargdocTrayVO;

public class TrayDisposeDlg extends nc.ui.pub.beans.UIDialog implements
		ActionListener, BillEditListener, BillTableMouseListener,
		ListSelectionListener {

	private Container myClientUI;
	private JPanel ivjUIDialogContentPane = null;

	protected BillListPanel ivjbillListPanel = null;

	// ��˾Id
	private String m_pkcorp = null;

	// ������id
	private String m_operator = null;

	// ��������
	private String m_billType = null;

	// ���ܽڵ��ʶ
	private String m_nodeKey = null;

	private TbOutgeneralBVO child = null;

	private UIPanel ivjPanlCmd = null;

	// ���ؼ���VO����
	protected AggregatedValueObject[] retBillVos = null;

	// ȷ����ť
	private UIButton ivjbtnOk = null;

	// ȡ����ť
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

	public TbOutgeneralBVO getChild() {
		return child;
	}

	public void setChlid(TbOutgeneralBVO child) {
		this.child = child;
	}

	public TrayDisposeDlg(String m_billType, String m_operator,
			String m_pkcorp, String m_nodeKey, TbOutgeneralBVO chlid,
			Container myClientUI) {
		this.myClientUI = myClientUI;
		init(m_billType, m_operator, m_pkcorp, m_nodeKey, chlid);

	}

	private void init(String m_billType, String m_operator, String m_pkcorp,
			String m_nodeKey, TbOutgeneralBVO child) {
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
		setTitle("ѡ������");
		setContentPane(getUIDialogContentPane());
	}

	public TbOutgeneralBVO getReturnVOs(TbOutgeneralBVO child) {

		loadHeadData(child);
		addBillUI();
		if (showModal() == nc.ui.pub.beans.MessageDialog.ID_OK) {
			// ��ȡ��ѡVO
			return getM_tbGeneralBVO();
		}

		return null;
	}

	// �ж��û�����
	private String st_type = "";
	// �ж����ֻܲ��Ƿֲ�
	private boolean sotckIsTotal = true;
	// �ֿ�����
	private String stordocName = "";

	public void loadHeadData(TbOutgeneralBVO child) {

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
		}

		// ��λ
		String pk_cargdoc = "";
		try {
			pk_cargdoc = nc.ui.wds.w8000.CommonUnit
					.getCargDocName(ClientEnvironment.getInstance().getUser()
							.getPrimaryKey());
		} catch (BusinessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// ��ñ�ͷ
		TbOutgeneralBVO[] tbOutgeneralBVO = new TbOutgeneralBVO[1];
		tbOutgeneralBVO[0] = child;

		// String
		// ��ñ���
		StringBuffer sql = new StringBuffer();
		if (sotckIsTotal) {
			sql = new StringBuffer(
					"select cdt_pk from bd_cargdoc_tray where cdt_invbasdoc='"
							+ tbOutgeneralBVO[0].getCinventoryid()
							+ "' and dr=0 and cdt_traystatus=0 and  pk_cargdoc='"
							+ pk_cargdoc + "' ");
			//
			String mySql = " select distinct cdt_pk from tb_general_b_b where pk_invbasdoc='"
					+ tbOutgeneralBVO[0].getCinventoryid()
					+ "' and geb_pk !='"
					+ tbOutgeneralBVO[0].getCsourcebillbid()
					+ "' and dr=0 and geb_pk in ('";

			String mySql1 = " ";
			if (null != tbOutgeneralBVO[0].getCsourcebillhid()
					&& !"".equals(tbOutgeneralBVO[0].getCsourcebillhid())) {
				mySql1 = " csourcebillhid='"
						+ tbOutgeneralBVO[0].getCsourcebillhid()
						+ "' and dr=0 ";

				ArrayList tbgenbvos = new ArrayList();
				try {
					tbgenbvos = (ArrayList) query.retrieveByClause(
							TbOutgeneralBVO.class, mySql1);
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				boolean myBoolean = false;
				if (null != tbgenbvos) {
					for (int j = 0; j < tbgenbvos.size(); j++) {
						if (null != tbOutgeneralBVO[0].getCinventoryid()
								&& null != tbgenbvos.get(0)
								&& null != ((TbOutgeneralBVO) tbgenbvos.get(j))
										.getCinventoryid()
								&& null != tbOutgeneralBVO[0]
										.getCsourcebillbid()
								&& null != ((TbOutgeneralBVO) tbgenbvos.get(j))
										.getCsourcebillbid()) {
							if (tbOutgeneralBVO[0].getCinventoryid().equals(
									((TbOutgeneralBVO) tbgenbvos.get(j))
											.getCinventoryid())
									&& !tbOutgeneralBVO[0]
											.getCsourcebillbid()
											.equals(
													((TbOutgeneralBVO) tbgenbvos
															.get(j))
															.getCsourcebillbid())) {
								mySql += ((TbOutgeneralBVO) tbgenbvos.get(j))
										.getCsourcebillbid()
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
		// ��ñ���

		// ����������ȫ������
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
			// ����
			tbGeneralBBVO[i].setGebb_vbatchcode(child.getVbatchcode());
			// ��д����
			tbGeneralBBVO[i].setGebb_lvbatchcode(child.getLvbatchcode());
			// �к�
			tbGeneralBBVO[i].setGebb_rowno(i + 1 + "0");
			// �˵���ͷ����
			tbGeneralBBVO[i].setGeb_pk(child.getCsourcebillbid());
			// ������
			tbGeneralBBVO[i].setGebb_hsl(child.getHsl());
			// �˻���������
			tbGeneralBBVO[i].setPk_invbasdoc(child.getCinventoryid());
			// ��ⵥ�ӱ�����
			// tbGeneralBBVO[i].setGeb_pk(child.getGeb_pk());
			// ����
			tbGeneralBBVO[i].setGebb_nprice(child.getNprice());
			// ���
			tbGeneralBBVO[i].setGebb_nmny(child.getNmny());
			// �����˵���ͷ������ѯ���ر��������е�������
			ArrayList tbbvos = new ArrayList();
			StringBuffer tbbsql = new StringBuffer(" geb_pk='");
			tbbsql.append(child.getCsourcebillbid());
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
			// �ִ���
			StringBuffer numsql = new StringBuffer(" pplpt_pk='");
			numsql.append(tbGeneralBBVO[i].getCdt_pk());
			numsql.append("' and dr=0 and whs_status =0 ");
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
		getbillListPanel().setHeaderValueVO(tbOutgeneralBVO);
		getbillListPanel().getHeadBillModel().execLoadFormula();
	}

	protected BillListPanel getbillListPanel() {
		if (ivjbillListPanel == null) {
			try {
				ivjbillListPanel = new BillListPanel();
				ivjbillListPanel.setName("billListPanel");
				// �����ʾλ��ֵ
				// װ��ģ��
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
	 * ���ӵ���ģ��
	 * <li>�÷�����PfUtilClient.childButtonClicked()����
	 */
	public void addBillUI() {
		// ����ģ�����
		// /getUIDialogContentPane().add(getbillListPanel(),
		// BorderLayout.CENTER);
		// ���ӶԿؼ�����
		addListenerEvent();
	}

	// ����
	public void addListenerEvent() {
		getbillListPanel().addEditListener(this);
		getbillListPanel().addMouseListener(this);

		// ��ͷ�б� ���л��¼�������
		getbillListPanel().getParentListPanel().getTable().getSelectionModel()
				.addListSelectionListener(this);
		getbillListPanel().addBodyEditListener(this);
	}

	protected JPanel getUIDialogContentPane() {
		if (ivjUIDialogContentPane == null) {

			ivjUIDialogContentPane = new JPanel();
			ivjUIDialogContentPane.setName("UIDialogContentPane");
			ivjUIDialogContentPane.setLayout(new BorderLayout());
			// 2003-05-12ƽ̨������ʾ����
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

	// ����ȷ����ť
	private UIButton getbtnOk() {
		if (ivjbtnOk == null) {
			ivjbtnOk = new UIButton();
			ivjbtnOk.setName("btnOk");
			ivjbtnOk.setText(NCLangRes.getInstance().getStrByID("common",
					"UC001-0000044")/* @res "ȷ��" */);
		}
		return ivjbtnOk;
	}

	// ����ȡ����ť
	private UIButton getbtnCancel() {
		if (ivjbtnCancel == null) {

			ivjbtnCancel = new UIButton();
			ivjbtnCancel.setName("btnCancel");
			ivjbtnCancel.setText(NCLangRes.getInstance().getStrByID("common",
					"UC001-0000008")/* @res "ȡ��" */);
		}
		return ivjbtnCancel;
	}

	public AggregatedValueObject[] getRetVos() {
		return retBillVos;
	}

	public UIPanel getIvjPanlCmd() {
		return ivjPanlCmd;
	}

	// ����vo,����vo,�ӱ�vo
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

			// ����Զ���ӿ�
			Iw8004040210 iw = (Iw8004040210) NCLocator.getInstance().lookup(
					Iw8004040210.class.getName());
			// ��ñ�ͷVO
			TbOutgeneralBVO[] tbOutgeneralBVO = (TbOutgeneralBVO[]) getbillListPanel()
					.getHeadBillModel().getBodyValueVOs(
							TbOutgeneralBVO.class.getName());
			// ������б���VO
			TbGeneralBBVO[] tbGeneralBBVO = (TbGeneralBBVO[]) getbillListPanel()
					.getBodyBillModel().getBodyValueVOs(
							TbGeneralBBVO.class.getName());
			// ���billVO

			IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
					IUAPQueryBS.class.getName());

			// ���Ҫɾ����VO
			StringBuffer tbbsql = new StringBuffer(" geb_pk='");
			tbbsql.append(tbOutgeneralBVO[0].getCsourcebillbid());
			tbbsql.append("' and pk_invbasdoc='");
			tbbsql.append(tbOutgeneralBVO[0].getCinventoryid());
			tbbsql.append("' and dr = 0");
			ArrayList dtbbvos = new ArrayList();
			try {

				dtbbvos = (ArrayList) query.retrieveByClause(
						TbGeneralBBVO.class, tbbsql.toString());
			} catch (BusinessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// ʵ������
			double geb_banum = 0;
			if (null != getbillListPanel().getHeadBillModel().getValueAt(0,
					"noutassistnum")) {
				String geb_banums = getbillListPanel().getHeadBillModel()
						.getValueAt(0, "noutassistnum").toString();
				geb_banum = Double.parseDouble(getbillListPanel()
						.getHeadBillModel().getValueAt(0, "noutassistnum")
						.toString());
			}
			// Ӧ������
			double geb_bsnum = 0;
			if (null != getbillListPanel().getHeadBillModel().getValueAt(0,
					"nshouldoutassistnum")) {
				String geb_bsnums = getbillListPanel().getHeadBillModel()
						.getValueAt(0, "nshouldoutassistnum").toString();
				geb_bsnum = Double.parseDouble(getbillListPanel()
						.getHeadBillModel()
						.getValueAt(0, "nshouldoutassistnum").toString());
			}
			// ͨ��ʵ���������жϣ���øı�ı���VO
			// TbGeneralBBVO[] tbGeneralBBVO1 = new
			// TbGeneralBBVO[tbGeneralBBVO.length];

			ArrayList tbgbbss = new ArrayList();
			List tbbvos = new ArrayList();
			// �������������
			double traysum = 0;
			// ArrayList bdCargdocTrayVOs = new ArrayList();
			String Cdt_pk = "";
			for (int i = 0; i < tbGeneralBBVO.length; i++) {
				if (null != tbGeneralBBVO[i]) {
					tbGeneralBBVO[i].setDr(0);
					// tbGeneralBBVO[i].setGeb_pk(tbOutgeneralBVO[0].getGeb_pk());
					if (null != tbGeneralBBVO[i].getGebb_num()
							&& !("".equals(tbGeneralBBVO[i].getGebb_num()) || 0 == tbGeneralBBVO[i]
									.getGebb_num().toDouble())) {
						// tbGeneralBBVO1[i] = tbGeneralBBVO[i];
						tbgbbss.add(tbGeneralBBVO[i]);
						traysum += tbGeneralBBVO[i].getGebb_num().toDouble();
						// tbbvos.add(tbGeneralBBVO[i]);
						// ����״̬

						Cdt_pk = getbillListPanel().getBodyBillModel()
								.getValueAt(i, "cdt_pk").toString();
						BdCargdocTrayVO bdCargdocTrayVO = new BdCargdocTrayVO();
						try {
							bdCargdocTrayVO = (BdCargdocTrayVO) query
									.retrieveByPK(BdCargdocTrayVO.class, Cdt_pk);
						} catch (BusinessException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						// bdCargdocTrayVO.setCdt_traystatus(new Integer(1));
						// bdCargdocTrayVOs.add(bdCargdocTrayVO);
					}
				}
			}
			// ͨ��ʵ���������жϣ���øı�ı���VO
			TbGeneralBBVO[] tbGeneralBBVO1 = new TbGeneralBBVO[tbgbbss.size()];
			tbgbbss.toArray(tbGeneralBBVO1);

			if (traysum > Math.abs(geb_bsnum)) {
				((MyClientUI) myClientUI).showErrorMessage("���̴������������Ӧ��������");
				return;
			}
			// ������
			double geb_hsl = 0;
			if (null != getbillListPanel().getHeadBillModel().getValueAt(0,
					"hsl")) {
				String geb_hsls = getbillListPanel().getHeadBillModel()
						.getValueAt(0, "hsl").toString();
				geb_hsl = Double.parseDouble(getbillListPanel()
						.getHeadBillModel().getValueAt(0, "hsl").toString());
			}
			//

			// getbillListPanel().getHeadBillModel().setValueAt(traysum *
			// geb_hsl,
			// 0, "geb_anum");
			// getbillListPanel().getHeadBillModel().setValueAt("1",
			// 0, "geb_banum");
			// ��;��������
			// if (null != tbs[0].getGeb_pk() && !"".equals(tbs[0].getGeb_pk()))
			// {
			// String tbssql = " geb_pk='" + tbs[0].getGeb_pk()
			// + "' and dr=0 ";
			// // +
			// // " and cdt_pk in (select cdt_pk from bd_cargdoc_tray where
			// // cdt_traystatus !=1 ) ";
			// ArrayList dtbvos = new ArrayList();
			// try {
			//
			// dtbvos = (ArrayList) query.retrieveByClause(
			// TbGeneralBVO.class, tbssql.toString());
			// } catch (BusinessException e1) {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// }
			// if (null != dtbvos && dtbvos.size() > 0) {
			// if (null != dtbvos.get(0)) {
			// TbGeneralBVO dtbvo = (TbGeneralBVO) dtbvos.get(0);
			// if (null != dtbvo.getGeb_banum()) {
			// traysum += dtbvo.getGeb_banum().doubleValue();
			// }
			// }
			// }
			//
			// }
			//
			getbillListPanel().getHeadBillModel().setValueAt(traysum, 0,
					"noutassistnum");
			//
			tbOutgeneralBVO[0].setNoutassistnum(new UFDouble(0 - traysum));
			tbOutgeneralBVO[0].setNoutnum(new UFDouble(0 - traysum * geb_hsl));
			try {
				iw.delAndInsertTbGeneralBBVO(dtbbvos, tbGeneralBBVO1);
			} catch (BusinessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// ���ص�ǰҳ��VO
			this.m_tbGeneralBVO = tbOutgeneralBVO[0];
			this.closeOK();

		}

		if (e.getSource().equals(getbtnCancel())) {
			this.closeCancel();
		}

	}

	private TbOutgeneralBVO m_tbGeneralBVO = null;

	public TbOutgeneralBVO getM_tbGeneralBVO() {
		return m_tbGeneralBVO;
	}

	/**
	 * "ȷ��"��ť����Ӧ���ӽ����ȡ��ѡ����VO
	 */
	public void onOk() {
		if (getbillListPanel().getHeadBillModel().getRowCount() > 0) {
			AggregatedValueObject[] selectedBillVOs = getbillListPanel()
					.getMultiSelectedVOs(MyBillVO.class.getName(),
							TbOutgeneralBVO.class.getName(),
							TbGeneralBBVO.class.getName());
			retBillVos = selectedBillVOs;
		}
	}

	/**
	 * ��õ�������VO����Ϣ
	 * 
	 * <li>����[0]=���ݾۺ�Vo;����[1]=��������Vo;����[2]=�����ӱ�Vo;
	 */
	public void getBillVO() {
		try {
			String[] retString = PfUIDataCache.getStrBillVo(getBillType());
			// MatchTableBO_Client.querybillVo(getBillType());
			// 0--����vo;1-����Vo;2-�ӱ�Vo;
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
			// ��ñ�ѡ�е�����
			int selectRow = getbillListPanel().getBodyTable().getSelectedRow();
			// �����ִ����
			int traynum = 0;
			if (null != getbillListPanel().getBodyBillModel().getValueAt(
					selectRow, "traynum")) {
				traynum = Integer.parseInt(getbillListPanel()
						.getBodyBillModel().getValueAt(selectRow, "traynum")
						.toString());
			}
			// ��������ݻ�
			int traymax = 0;
			if (null != getbillListPanel().getBodyBillModel().getValueAt(
					selectRow, "traymax")) {
				traymax = Integer.parseInt(getbillListPanel()
						.getBodyBillModel().getValueAt(selectRow, "traymax")
						.toString());
			}
			// ʵ�������
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
							.showErrorMessage("���̴������������������ݻ���");

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