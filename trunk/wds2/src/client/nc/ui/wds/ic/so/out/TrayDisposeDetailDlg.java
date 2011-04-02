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

	// �洢�������ı������
	private TbOutgeneralBVO[] generalBVO;

	private TbOutgeneralTVO[] generalTVO;

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

	private java.awt.Container parent = null;

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
		setTitle("�鿴������ϸ");
		setContentPane(getUIDialogContentPane());
	}

	public AggregatedValueObject[] getReturnVOs(java.awt.Container parent,
			TbOutgeneralBVO[] item) throws Exception {

		loadHeadData(item);
		addBillUI();
		if (showModal() == nc.ui.pub.beans.MessageDialog.ID_OK) {
			// ��ȡ��ѡVO
			return getRetVos();
		}

		return null;
	}

	// �������� Ϊҳ�洫ֵ
	public void loadHeadData(TbOutgeneralBVO[] item) throws Exception {
		// �ж�ҳ�洫���ı�������Ƿ�Ϊ��
		if (null != item) {
			TbOutgeneralTVO generaltvo = new TbOutgeneralTVO();

			// ���÷��� ���ݴ����ı������ ��ѯ���ݿ��еĿ����Ϣ���ѷ��ϵ�������ȡ������װ�ɵ�ǰ�����б���VO����
			this.getWarehousestock(item);
			// ����ǰ���ݱ�ͷ��ֵ
			this.getbillListPanel().getBillListData().setHeaderValueVO(
					this.getGeneralBVO());
			// ����ǰ���ݱ��帳ֵ
			// this.getbillListPanel().getBillListData()
			// .setBodyValueVO(generalTVO);
			// ִ�б���ͱ�ͷ��ʽ
			this.getbillListPanel().getHeadBillModel().execLoadFormula();
			this.getbillListPanel().getBodyBillModel().execLoadFormula();
		}

	}

	/**
	 * ���ݴ��������ȡ�������Ϣ �����ǰ����ָ�������Ѿ��洢������ �͸���ѯ���� ��������󴫵������� ����������¼�Ĳ�ѯ ���������
	 * 
	 * @param item
	 *            �������ı������
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
				// �������ݿ�õ������
				ArrayList generaltList = (ArrayList) IUAPQueryBS
						.retrieveByClause(TbOutgeneralTVO.class, sWhere);
				// �жϽ�����Ƿ�Ϊ��
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
					// ��ҳ����ʵ����������ֵ
					if (sum != 0)
						item[i].setNoutassistnum(new UFDouble(sum));
				}
			}
			TbOutgeneralTVO[] generaltvo = new TbOutgeneralTVO[generaltvoList
					.size()];
			generaltvo = (TbOutgeneralTVO[]) generaltvoList.toArray(generaltvo);
			this.setGeneralBVO(new TbOutgeneralBVO[item.length]);
			// ���洢���帳ֵ
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
		getbtnOk().addActionListener(this);
		getbtnCancel().addActionListener(this);
		getbillListPanel().addEditListener(this);
		getbillListPanel().addMouseListener(this);

		// ��ͷ�б� ���л��¼�������
		getbillListPanel().getParentListPanel().getTable().getSelectionModel()
				.addListSelectionListener(this);
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

	// ���ȷ����ť
	private UIButton getbtnOk() {
		if (ivjbtnOk == null) {
			ivjbtnOk = new UIButton();
			ivjbtnOk.setName("btnOk");
			ivjbtnOk.setText(NCLangRes.getInstance().getStrByID("common",
					"UC001-0000044")/* @res "ȷ��" */);
		}
		return ivjbtnOk;
	}

	// ���ȡ����ť
	private UIButton getbtnCancel() {
		if (ivjbtnCancel == null) {

			ivjbtnCancel = new UIButton();
			ivjbtnCancel.setName("btnCancel");
			ivjbtnCancel.setText(NCLangRes.getInstance().getStrByID("common",
					"UC001-0000008")/*
									 * @res "ȡ��"
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
			// ��ȡ��ѡ���е�Id
			Object o = getbillListPanel().getHeadBillModel().getValueAt(
					getbillListPanel().getHeadTable().getSelectedRow(),
					"csourcebillbid");
			// �ж��Ƿ�Ϊ��
			if (o != null && o != "") {
				List<TbOutgeneralTVO> generaltvo = new ArrayList<TbOutgeneralTVO>();
				String genb_pk = o.toString();
				for (int i = 0; i < this.getGeneralTVO().length; i++) {
					String gent_pk = this.getGeneralTVO()[i].getCfirstbillbid();
					if (genb_pk.equals(gent_pk)) {
						// �����ѡ�����id��Ϊ�� ������ȥ�������ѯ���ӱ�Vo ������ʾ
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
//						.setValueAt(sum1, 0, 7); // ������
//				getbillListPanel().getBodyBillModel().getTotalTableModel()
//						.setValueAt(
//								new BigDecimal(sum2).setScale(6,
//										BigDecimal.ROUND_HALF_UP), 0, 6); // ������
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
