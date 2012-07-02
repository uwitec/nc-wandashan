package nc.ui.wdsnew.pub;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import nc.bs.logging.Logger;
import nc.ui.ic.ic001.BatchCodeDefSetTool;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillMouseEnent;
import nc.ui.zmpub.pub.tool.LongTimeTask;
import nc.vo.ic.pub.StockInvOnHandVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.session.ClientLink;
/**
 * ���κŲ��նԻ��� mlr
 */
public class LotNumbDlg extends UIDialog implements java.awt.event.ActionListener {
	private static final long serialVersionUID = -6435830803487288885L;
	private String m_strCorpID = null; // ��˾ID
	private String m_strWareHouseID = null; // �ֿ�ID
	private String m_spaceId = null; // ��λid
	private String m_strInventoryID = null; // ���ID
	private StockInvOnHandVO[] vos = null;// �ȴ���vo
	private List<StockInvOnHandVO> lis = new ArrayList<StockInvOnHandVO>();// ��ѡ�е��ȴ���vo
	private String m_sRNodeName = "PCDA";
	private javax.swing.JPanel ivjUIDialogContentPane = null;
	private UIPanel ivjUIPanel1 = null;
	private UIButton ivjBtnCancel = null;
	private UIButton ivjBtnOK = null;
	private ClientLink m_cl = null;
	private BillCardPanel m_card = null;
	/**
	 * LotNumbDlg ������ע�⡣
	 */
	public LotNumbDlg() {
		super();
		initialize();
	}

	/**
	 * LotNumbDlg ������ע�⡣
	 * 
	 * @param parent
	 *            java.awt.Container
	 */
	public LotNumbDlg(java.awt.Container parent) {
		super(parent);
		initialize();
	}

	/**
	 * LotNumbDlg ������ע�⡣
	 * 
	 * @param parent
	 *            java.awt.Container
	 * @param title
	 *            java.lang.String
	 */
	public LotNumbDlg(java.awt.Container parent, String title) {
		super(parent, title);
		initialize();
	}

	/**
	 * LotNumbDlg ������ע�⡣
	 * 
	 * @param owner
	 *            java.awt.Frame
	 */
	public LotNumbDlg(java.awt.Frame owner) {
		super(owner);
		initialize();
	}

	/**
	 * LotNumbDlg ������ע�⡣
	 * 
	 * @param owner
	 *            java.awt.Frame
	 * @param title
	 *            java.lang.String
	 */
	public LotNumbDlg(java.awt.Frame owner, String title) {
		super(owner, title);
		initialize();
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-5-8 20:47:36)
	 * 
	 * @return java.lang.String
	 */
	public String getAssQuant() {
		return null;
	}

	/**
	 * ���� BtnCancel ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIButton getBtnCancel() {
		if (ivjBtnCancel == null) {
			try {
				ivjBtnCancel = new nc.ui.pub.beans.UIButton();
				ivjBtnCancel.setName("BtnCancel");
				ivjBtnCancel.setText(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("common", "UC001-0000008")/* @res "ȡ��" */);
				ivjBtnCancel.setBounds(560, 321, 70, 21);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjBtnCancel;
	}

	/**
	 * ���� BtnOK ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIButton getBtnOK() {
		if (ivjBtnOK == null) {
			try {
				ivjBtnOK = new nc.ui.pub.beans.UIButton();
				ivjBtnOK.setName("BtnOK");
				ivjBtnOK.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"common", "UC001-0000044")/* @res "ȷ��" */);
				ivjBtnOK.setBounds(480, 321, 70, 21);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjBtnOK;
	}

	// /**
	// * �˴����뷽��˵����
	// * �����ߣ�����
	// * ���ܣ�
	// * ������
	// * ���أ�
	// * ���⣺
	// * ���ڣ�(2001-5-16 13:47:22)
	// * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	// *
	// * @return java.lang.String
	// */
	// public String getLotNumb() {
	//
	// m_strLotNumb = m_lnrvoSelVO==null? null:m_lnrvoSelVO.getVbatchcode();
	// return m_strLotNumb;
	//
	// }

	/**
	 * ���� UIDialogContentPane ����ֵ��
	 * 
	 * @return javax.swing.JPanel
	 */
	/* ���棺�˷������������ɡ� */
	private javax.swing.JPanel getUIDialogContentPane() {
		if (ivjUIDialogContentPane == null) {
			try {
				ivjUIDialogContentPane = new javax.swing.JPanel();
				ivjUIDialogContentPane.setName("UIDialogContentPane");
				// ivjUIDialogContentPane.setLayout(new BorderLayout());
				// ivjUIDialogContentPane.add(getbillListPanel(), "Center");
				// ivjUIDialogContentPane.add(getPanlCmd(), BorderLayout.SOUTH);
				ivjUIDialogContentPane.setLayout(new BorderLayout());
				// getUIDialogContentPane().add(getUIPanel1(),
				// getUIPanel1().getName());

				// getUIDialogContentPane().add(getBtnQueryAll(),
				// getBtnQueryAll().getName());
				getUIDialogContentPane().add(getPanlCmd(), BorderLayout.SOUTH);
				// getUIDialogContentPane().add(getBtnOK(), BorderLayout.SOUTH);
				// getUIDialogContentPane().add(getBtnCancel(),BorderLayout.SOUTH);
				getUIDialogContentPane().add(getBillCardPanel(), "Center");
				// getUIDialogContentPane().add(getBtnRefresh(),
				// getBtnRefresh().getName());
				// getUIDialogContentPane().add(getBtnNew(),
				// getBtnNew().getName());
				// getUIDialogContentPane().add(getBtnEdit(),
				// getBtnEdit().getName());

			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjUIDialogContentPane;
	}

	private UIPanel ivjPanlCmd = null;

	private UIPanel getPanlCmd() {
		if (ivjPanlCmd == null) {
			ivjPanlCmd = new UIPanel();
			ivjPanlCmd.setName("PanlCmd");
			ivjPanlCmd.setPreferredSize(new Dimension(0, 40));
			ivjPanlCmd.setLayout(new FlowLayout());
			ivjPanlCmd.add(getBtnOK(), getBtnOK().getName());
			ivjPanlCmd.add(getBtnCancel(), getBtnCancel().getName());
		}
		return ivjPanlCmd;
	}

	/**
	 * ���� UIPanel1 ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* ���棺�˷������������ɡ� */
	// private nc.ui.pub.beans.UIPanel getUIPanel1() {
	// if (ivjUIPanel1 == null) {
	// try {
	// ivjUIPanel1 = new nc.ui.pub.beans.UIPanel();
	// ivjUIPanel1.setName("UIPanel1");
	// ivjUIPanel1.setLayout(null);
	// ivjUIPanel1.setBounds(10, 0, 800, 80);
	// getUIPanel1().add(getLbWareHouseID(), getLbWareHouseID().getName());
	// getUIPanel1().add(getTfWareHouse(), getTfWareHouse().getName());
	// getUIPanel1().add(getLbUnitName(), getLbUnitName().getName());
	// getUIPanel1().add(getTfUnitName(), getTfUnitName().getName());
	// getUIPanel1().add(getLbFreeItem1(), getLbFreeItem1().getName());
	// getUIPanel1().add(getTfFreeItem1(), getTfFreeItem1().getName());
	// getUIPanel1().add(getLbInventory(), getLbInventory().getName());
	// getUIPanel1().add(getTfInventory(), getTfInventory().getName());
	// getUIPanel1().add(getckIsQueryZeroLot(),
	// getckIsQueryZeroLot().getName());
	// } catch (java.lang.Throwable ivjExc) {
	// handleException(ivjExc);
	// }
	// }
	// return ivjUIPanel1;
	// }
	/**
	 * ÿ�������׳��쳣ʱ������
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {

		/* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
		// nc.vo.scm.pub.SCMEnv.out("--------- δ��׽�����쳣 ---------");
		// nc.vo.scm.pub.SCMEnv.error(exception);
	}

	/**
	 * ��ʼ���ࡣ
	 */
	/* ���棺�˷������������ɡ� */
	private void initialize() {
		try {
			setModal(true);
			setName("LotNumbDlg");
			setSize(700, 450);
			// setResizable(false);
			setContentPane(getUIDialogContentPane());
			 initConnections1();
			// m_cardBodySortCtl = new ClientUISortCtl(this,true,BillItem.BODY);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// setUIEditable(false);
		// getckIsQueryZeroLot().addItemListener(this);
	}

	private void initConnections1() {
		getBtnOK().addActionListener(this);
		getBtnCancel().addActionListener(this);		
	}

	/**
	 * ����ڵ� - ��������ΪӦ�ó�������ʱ���������������
	 * 
	 * @param args
	 *            java.lang.String[]
	 */
	public static void main(java.lang.String[] args) {
		try {
			LotNumbDlg aLotNumbDlg = null;
			aLotNumbDlg = new LotNumbDlg();
			aLotNumbDlg.setModal(true);
			aLotNumbDlg.addWindowListener(new java.awt.event.WindowAdapter() {
				public void windowClosing(java.awt.event.WindowEvent e) {
					System.exit(0);
				};
			});
			aLotNumbDlg.show();
			java.awt.Insets insets = aLotNumbDlg.getInsets();
			aLotNumbDlg.setSize(aLotNumbDlg.getWidth() + insets.left
					+ insets.right, aLotNumbDlg.getHeight() + insets.top
					+ insets.bottom);
			aLotNumbDlg.setVisible(true);
		} catch (Throwable exception) {
			nc.vo.scm.pub.SCMEnv.out("nc.ui.pub.beans.UIDialog �� main() �з����쳣");
			nc.vo.scm.pub.SCMEnv.error(exception);
		}
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-5-8 18:27:15)
	 */
	public void onCancel() {
		// this.setVisible(false);
		// aLotNumbDlg.closeCancel();
		closeCancel();

	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-5-8 18:27:04)
	 */
	public void onOK() {
		// this.setVisible(false);
		// getSelVO();
		List<StockInvOnHandVO> list = new ArrayList<StockInvOnHandVO>();
		int length = getBillCardPanel().getRowCount();
		if (length > 0) {
			for (int i = 0; i < length; i++) {
				UFBoolean isxz = PuPubVO.getUFBoolean_NullAs(getBillCardPanel()
						.getBodyValueAt(i, "isxz"), new UFBoolean(false));
				if (isxz.booleanValue() == true) {
					list.add((StockInvOnHandVO) getBillCardPanel()
							.getBillModel().getBodyValueRowVO(i,
									StockInvOnHandVO.class.getName()));
				}
			}

		}
		setLis(list);
		if (list.size() == 0) {
			onCancel();
		} else {
			// getLotNumb();
			// getValidate();

			// getBillBodyID();
			// getBillHeaderID();
			// getBillCode();
			// getBillType();

			closeOK();
		}
	}

	//
	// private void onNew(){
	// String pk_invmandoc = getInvID();
	// getBatchCodeDlg().setData(pk_invmandoc,null);
	// if(getBatchCodeDlg().showModal()==UIDialog.ID_CANCEL)
	// return;
	//	
	// //����ˢ�½���
	// onRefresh();
	// }
	//
	// private void onEdit(){
	// if(getSelVO()==null)
	// return;
	// String pk_invmandoc = getInvID();
	// String vbatchcode = getSelVO().getVbatchcode();
	// getBatchCodeDlg().setData(pk_invmandoc,vbatchcode);
	// if(getBatchCodeDlg().showModal()==UIDialog.ID_CANCEL)
	// return;
	// onRefresh();
	// }

	public StockInvOnHandVO[] getVos() {
		return vos;
	}

	public void setVos(StockInvOnHandVO[] vos) {
		this.vos = vos;
	}

	public List<StockInvOnHandVO> getLis() {
		return lis;
	}

	public void setLis(List<StockInvOnHandVO> lis) {
		this.lis = lis;
	}

	/**
	 * �ڱ������ʾ���ݡ� �������ڣ�(2001-5-8 20:50:59)
	 */
	public void setVOtoBody() {
		// LotNumbRefVO[] voaAllData = null;

		// if (isTrackedBill()) {
		// getUITablePane1().getTable().setModel(getTrackedTableModel());
		// voaAllData = getTrackedTableModel().getAllData();
		//
		// } else {
		// getUITablePane1().getTable().setModel(getNotTrackedTableModel());
		// voaAllData = getNotTrackedTableModel().getAllData();
		// }
		// getUITablePane1().getTable().setModel(getBillCardPanel().getBillModel());

		// testע��
		// voaAllData =
		// (LotNumbRefVO[])getBillCardPanel().getBillModel().getBodyValueVOs(LotNumbRefVO.class.getName());
		//	
		// if (voaAllData != null && voaAllData.length > 0) {
		// getUITablePane1().getTable().setRowSelectionInterval(0, 0);
		// setSelVO(voaAllData[0]);
		// }

	}

	private UILabel ivjLbWareHouseID = null;

	/**
	 * ���� LbWareHouseID ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UILabel getLbWareHouseID() {
		if (ivjLbWareHouseID == null) {
			try {
				ivjLbWareHouseID = new nc.ui.pub.beans.UILabel();
				ivjLbWareHouseID.setName("LbWareHouseID");
				ivjLbWareHouseID.setText(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("common", "UC000-0000153")/* @res "�ֿ�" */);
				ivjLbWareHouseID.setBounds(0, 4, 80, 22);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjLbWareHouseID;
	}

	/**
	 * �����ˣ������� �������ڣ�2007-9-26����02:10:22 ����ԭ�� LotNumbDlg ������ע�⡣
	 * 
	 * @param parent
	 *            java.awt.Container
	 */
	public LotNumbDlg(java.awt.Container parent, boolean isMutiSel,
			boolean IsBodyMutiSel) {
		super(parent);
		initialize();
	}

	/**
	 * LotNumbDlg ������ע�⡣
	 * 
	 * @param parent
	 *            java.awt.Container
	 */
	public LotNumbDlg(java.awt.Container parent, boolean isMutiSel) {
		super(parent);
		initialize();
	}

	/**
	 * LotNumbDlg ������ע�⡣
	 */
	public LotNumbDlg(boolean isMutiSel) {
		super();
		initialize();
	}

	/**
	 * �˴����뷽��˵���� �����ߣ����� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-5-11 11:31:45)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return java.lang.String
	 */
	public String getTitle() {
		return nc.ui.ml.NCLangRes.getInstance().getStrByID("4008other",
				"UPP4008other-000452")/* @res "���κŲ���" */;
	}

	/**
	 * �˴����뷽��˵���� �����ߣ����� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-6-12 16:55:10)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 */
	public void onRefresh() {
		setData(); //
		setVOtoBody(); // ���½����ݷ������
	}

	/**
	 * ���ݴ���Ĳֿ�ʹ��ID�����ݿ��ͷ�������в��������� �����ߣ����� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-5-16
	 * 13:38:16) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 */

	public void setData() {
		if (((m_strWareHouseID == null || m_strWareHouseID.trim().length() <= 0) && (m_spaceId == null || m_spaceId
				.trim().length() <= 0))
				|| m_strInventoryID == null
				|| m_strInventoryID.trim().length() <= 0) {
			return;

		}
		String wheresql = " pk_corp = '" + m_strCorpID
				+ "'  and pk_customize1='" + m_strWareHouseID + "' "
				+ "and pk_cargdoc ='" + m_spaceId + "' and pk_invmandoc ='"
				+ m_strInventoryID + "' and isnull(dr,0)=0";

		try {
			Class[] ParameterTypes = new Class[] { String.class };
			Object[] ParameterValues = new Object[] { wheresql };
			Object o = LongTimeTask.calllongTimeService("wds", this, "���ڲ�ѯ...",
					1, "nc.vo.wdsnew.pub.BillStockBO1", null, "queryStock",
					ParameterTypes, ParameterValues);
			if (o != null) {
				vos = (StockInvOnHandVO[]) o;
			}
		} catch (Exception e) {
			Logger.error(e);
			MessageDialog.showErrorDlg(this, "����", e.getMessage());
		}
		getBillCardPanel().getBillModel().setBodyDataVO(vos);
		getBillCardPanel().getBillModel().execLoadFormula();
		getBillCardPanel().updateValue();
	}

	/**
	 * �˴����뷽��˵���� �����ߣ����� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-5-16 13:47:22)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param newM_strWareHouseID
	 *            java.lang.String
	 */
	public void setWareHouseID(java.lang.String newM_strWareHouseID) {
		m_strWareHouseID = newM_strWareHouseID;
	}

	/**
	 * ȡ��ϵͳ��Ϣ��
	 */
	private ClientLink getCEnvInfo() {
		if (m_cl == null) {
			nc.ui.pub.ClientEnvironment ce = nc.ui.pub.ClientEnvironment
					.getInstance();
			m_cl = new ClientLink(ce);
		}
		return m_cl;

	}

	public BillCardPanel getBillCardPanel() {
		if (m_card == null) {
			try {
				m_card = new nc.ui.pub.bill.BillCardPanel();
				m_card.setName("BillCardPanel");
				m_card.setBounds(10, 84, 700, 400);

				BillData bd = new BillData(m_card.getTempletData(m_sRNodeName,
						null, getCEnvInfo().getUser(), getCEnvInfo().getCorp(),
						null));
				if (bd == null) {
					nc.vo.scm.pub.SCMEnv.out("--> billdata null.");
					return m_card;
				}
				// �Զ�����
				BatchCodeDefSetTool.changeBillDataByBCDef(m_cl.getCorp(), bd);

				m_card.setBillData(bd);

				m_card.getBodyPanel().setBBodyMenuShow(false);
				m_card.getBodyPanel().setRowNOShow(false);
				m_card.setEnabled(true);
				m_card.setBodyMenuShow(false);

			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}

		}
		return m_card;
	}

	public void afterEdit(BillEditEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void bodyRowChange(BillEditEvent arg0) {
		int selrow = getBillCardPanel().getBillTable().getSelectedRow();

	}

	public void mouse_doubleclick(BillMouseEnent arg0) {
		onOK();

	}

	public String getM_spaceId() {
		return m_spaceId;
	}

	public void setM_spaceId(String id) {
		m_spaceId = id;
	}

	public String getM_strInventoryID() {
		return m_strInventoryID;
	}

	public void setM_strInventoryID(String inventoryID) {
		m_strInventoryID = inventoryID;
	}

	public javax.swing.JPanel getIvjUIDialogContentPane() {
		return ivjUIDialogContentPane;
	}

	public void setIvjUIDialogContentPane(
			javax.swing.JPanel ivjUIDialogContentPane) {
		this.ivjUIDialogContentPane = ivjUIDialogContentPane;
	}

	public UIPanel getIvjUIPanel1() {
		return ivjUIPanel1;
	}

	public void setIvjUIPanel1(UIPanel ivjUIPanel1) {
		this.ivjUIPanel1 = ivjUIPanel1;
	}

	public UIButton getIvjBtnCancel() {
		return ivjBtnCancel;
	}

	public void setIvjBtnCancel(UIButton ivjBtnCancel) {
		this.ivjBtnCancel = ivjBtnCancel;
	}

	public UIButton getIvjBtnOK() {
		return ivjBtnOK;
	}

	public void setIvjBtnOK(UIButton ivjBtnOK) {
		this.ivjBtnOK = ivjBtnOK;
	}

	public String getM_strCorpID() {
		return m_strCorpID;
	}

	public void setM_strCorpID(String corpID) {
		m_strCorpID = corpID;
	}

	public String getM_strWareHouseID() {
		return m_strWareHouseID;
	}

	public void setM_strWareHouseID(String wareHouseID) {
		m_strWareHouseID = wareHouseID;
	}

	public ClientLink getM_cl() {
		return m_cl;
	}

	public void setM_cl(ClientLink m_cl) {
		this.m_cl = m_cl;
	}

	public BillCardPanel getM_card() {
		return m_card;
	}

	public void setM_card(BillCardPanel m_card) {
		this.m_card = m_card;
	}

	public String getM_sRNodeName() {
		return m_sRNodeName;
	}

	public void setM_sRNodeName(String nodeName) {
		m_sRNodeName = nodeName;
	}

	public UILabel getIvjLbWareHouseID() {
		return ivjLbWareHouseID;
	}

	public void setIvjLbWareHouseID(UILabel ivjLbWareHouseID) {
		this.ivjLbWareHouseID = ivjLbWareHouseID;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == LotNumbDlg.this.getBtnOK())
			onOK();
		if (e.getSource() == LotNumbDlg.this.getBtnCancel())
			onCancel();		
	}

}