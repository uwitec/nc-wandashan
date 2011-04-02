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
	// ����MyClientUI ���㵯���Ի��� �� ������ҳ�洫ֵ
	Container myClientUI = null;
	// ����ҳ����ѡ�����
	private int index;
	// �洢�Ѿ���ӹ������� ��ɾ����ʱ����Ϊ����
	private List<TbOutgeneralTVO> generaltvoList = new ArrayList<TbOutgeneralTVO>();
	// ���ýӿ� ɾ������ӷ���
	private Iw8004040204 iw = null;

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

	// �洢�������ı������
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
		setTitle("ѡ������");
		setContentPane(getUIDialogContentPane());
		// ��ʼ���ӿ�
		this.setIw((Iw8004040204) NCLocator.getInstance().lookup(
				Iw8004040204.class.getName()));
	}

	/**
	 * ����ָ����ť���÷��� ���в��� ��������ģ��
	 * 
	 * @param parent
	 * @param item
	 *            �������
	 * @param index
	 *            ѡ���е�����
	 * @return
	 * @throws Exception
	 */
	public AggregatedValueObject[] getReturnVOs(java.awt.Container parent,
			TbOutgeneralBVO item, int index, String pk_stordoc, boolean type)
			throws Exception {
		// �������и�ֵ
		this.setIndex(index);
		// ��ѯ������
		loadHeadData(item, pk_stordoc, type);
		// ��������
		addBillUI();
		if (showModal() == nc.ui.pub.beans.MessageDialog.ID_OK) {
			// ��ȡ��ѡVO
			return getRetVos();
		}

		return null;
	}

	// �������� Ϊҳ�洫ֵ
	public void loadHeadData(TbOutgeneralBVO item, String pk_stordoc,
			boolean type) throws Exception {
		// �ж�ҳ�洫���ı�������Ƿ�Ϊ��
		if (null != item) {
			TbOutgeneralTVO generaltvo = new TbOutgeneralTVO();
			// ���洢���帳ֵ
			this.setGeneralbvo(item);
			TbOutgeneralBVO[] generalBVO = new TbOutgeneralBVO[1];
			// ת������
			generalBVO[0] = item;
			// ����ǰ���ݱ�ͷ��ֵ
			this.getbillListPanel().getBillListData().setHeaderValueVO(
					generalBVO);
			// ���÷��� ���ݴ����ı������ ��ѯ���ݿ��еĿ����Ϣ���ѷ��ϵ�������ȡ������װ�ɵ�ǰ�����б���VO����
			TbOutgeneralTVO[] generalTVO = this.getWarehousestock(item,
					pk_stordoc, type);
			// ����ǰ���ݱ��帳ֵ
			this.getbillListPanel().getBillListData()
					.setBodyValueVO(generalTVO);
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
	public TbOutgeneralTVO[] getWarehousestock(TbOutgeneralBVO item,
			String pk_stordoc, boolean type) throws Exception {
		// �������������
		StockInvOnHandVO[] stockVO = null;
		// ����where���
		String sWhere = null;
		// ��ȡ�������ݿ����
		IUAPQueryBS IUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		// ��ѯ�����ļ���
		List list = CommonUnit.getStockDetailByPk_User(ClientEnvironment
				.getInstance().getUser().getPrimaryKey(), item
				.getCinventoryid());
		// ����ָ��������
		TbOutgeneralTVO[] generalTVO = null;
		ArrayList<TbOutgeneralTVO> genltList = new ArrayList<TbOutgeneralTVO>();
		// �жϽ�����Ƿ�Ϊ��
		if (null != list && list.size() > 0) {
			// �������鸳ֵ
			stockVO = new StockInvOnHandVO[list.size()];
			stockVO = (StockInvOnHandVO[]) list.toArray(stockVO);

			// ѭ������
			for (int i = 0; i < stockVO.length; i++) {
				// �Ƿ���Ʒ��������ʾ��Ʒ������Ʒ������
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
				// ������������
				TbOutgeneralTVO generaltvo = new TbOutgeneralTVO();

				// �������û�в�ѯ����¼ ˵��������¼��û�д洢�� ��������ж��ֶ��Ƿ�Ϊ�� �ٸ���������ֵ
				// ��Դ�������� �������ݲ��ǲ�ѯ������ ���ǵ����ߴ������Ķ��������ֵ��������Ҫ��¼
				// �������ѯ��where�������Ҫ�������
				if (null != item.getCsourcebillhid()
						&& !"".equals(item.getCsourcebillhid())) {
					generaltvo.setCfirstbillhid(item.getCsourcebillhid()); // Դͷ����
				}
				if (null != item.getCsourcebillbid()
						&& !"".equals(item.getCsourcebillbid())) { // Դͷ�ӱ�
					generaltvo.setCfirstbillbid(item.getCsourcebillbid());
				}
				if (null != item.getVsourcebillcode()
						&& !"".equals(item.getVsourcebillcode())) {
					generaltvo.setVsourcebillcode(item.getVsourcebillcode()); // ��Դ���ݺ�
				}
				if (null != stockVO[i].getPplpt_pk()
						&& !"".equals(stockVO[i].getPplpt_pk())) {
					generaltvo.setCdt_pk(stockVO[i].getPplpt_pk()); // ��������
				}
				if (null != stockVO[i].getWhs_stockpieces()
						&& !"".equals(stockVO[i].getWhs_stockpieces())) {
					generaltvo.setStockpieces(stockVO[i].getWhs_stockpieces()); // �������
				}
				if (null != stockVO[i].getWhs_stocktonnage()
						&& !"".equals(stockVO[i].getWhs_stocktonnage())) {
					generaltvo
							.setStocktonnage(stockVO[i].getWhs_stocktonnage()); // ��渨����
				}
				if (null != stockVO[i].getPk_invbasdoc()
						&& !"".equals(stockVO[i].getPk_invbasdoc())) {
					generaltvo.setPk_invbasdoc(stockVO[i].getPk_invbasdoc()); // �������
				}
				if (null != stockVO[i].getWhs_batchcode()
						&& !"".equals(stockVO[i].getWhs_batchcode())) {
					generaltvo.setVbatchcode(stockVO[i].getWhs_batchcode()); // ����
				}
				if (null != stockVO[i].getWhs_pk()
						&& !"".equals(stockVO[i].getWhs_pk())) {
					generaltvo.setWhs_pk(stockVO[i].getWhs_pk()); // ��������
				}
				if (null != stockVO[i].getWhs_lbatchcode()
						&& !"".equals(stockVO[i].getWhs_lbatchcode())) {
					generaltvo.setLvbatchcode(stockVO[i].getWhs_lbatchcode()); // ��Դ����
				}
				if (null != stockVO[i].getWhs_nprice()
						&& !"".equals(stockVO[i].getWhs_nprice())) {
					generaltvo.setNprice(stockVO[i].getWhs_nprice()); // ����
				}
				if (null != stockVO[i].getWhs_nmny()
						&& !"".equals(stockVO[i].getWhs_nmny())) {
					generaltvo.setNmny(stockVO[i].getWhs_nmny()); // ���
				}
				genltList.add(generaltvo);
			}
		}
		if (genltList.size() > 0) {
			generalTVO = new TbOutgeneralTVO[genltList.size()];
			generalTVO = genltList.toArray(generalTVO);
			genltList.clear();
		}
		// ���ڵĲ����ǲ�ѯ���Ѿ�������ָ�����д洢������ ���в�ѯ
		// ����where��� ɾ����־Ϊ0�� �� ��Դ��ͷ�������� �� ��浵������
		sWhere = " dr = 0 and cfirstbillbid = '" + item.getCsourcebillbid()
				+ "' and pk_invbasdoc ='" + item.getCinventoryid() + "' ";
		// �������ݿ�õ������
		ArrayList generaltList = (ArrayList) IUAPQueryBS.retrieveByClause(
				TbOutgeneralTVO.class, sWhere);
		// �жϽ�����Ƿ�Ϊ��
		if (null != generaltList && generaltList.size() > 0) {
			// �����Ը�ֵ ��¼��Щ�����Ǵ���� �ȵ�����ɾ����ʱ�����Ϊ��������
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
		// getUIDialogContentPane().add(getbillListPanel(),
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
		getbillListPanel().addBodyEditListener(this); // ����༭���¼�����
		// ��ͷ�б� ���л��¼�������
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

	// �����ť��ļ����¼�
	public void actionPerformed(ActionEvent e) {
		// �ж��Ƿ�Ϊȡ����ť
		if (e.getSource().equals(getbtnCancel())) {
			// �رմ���
			this.closeCancel();
		}
		// �ж��Ƿ�Ϊȷ����ť
		if (e.getSource().equals(getbtnOk())) {
			// ��ȡ��ǰҳ���б���Ķ���
			TbOutgeneralTVO[] item = (TbOutgeneralTVO[]) getbillListPanel()
					.getBillListData().getBodyBillModel()
					.getBodyValueChangeVOs(TbOutgeneralTVO.class.getName());
			// �ж��Ƿ�Ϊ��
			if (null != item && item.length > 0) {
				double total = 0; // ʵ��������
				double totalnum = 0; // ʵ������
				// �洢�����μ���
				List<String> batch = new ArrayList<String>();
				String vbatchcode = "";// �������κ�
				String lvbatchcode = ""; // ��Դ����
				double nprice = 0; // ����
				double nmny = 0; // ���
				// ѭ����������
				for (int i = 0; i < item.length; i++) {
					// ������������
					TbOutgeneralTVO generalvo = item[i];
					// �ж϶����е�ʵ���������Ƿ�Ϊ��
					if (null != generalvo.getNoutassistnum()
							&& !"".equals(generalvo.getNoutassistnum())) {
						// ������ǿ� ʵ�����������������ۼ�
						total = total
								+ Double.parseDouble(generalvo
										.getNoutassistnum().toString());
						// ʵ������
						totalnum = totalnum
								+ Double.parseDouble(generalvo.getNoutnum()
										.toString());
						// Ϊ���μ������ֵ
						batch.add(generalvo.getVbatchcode());
						// ��Դ����
						lvbatchcode = generalvo.getLvbatchcode();
						// ����
						if (null != generalvo.getNprice())
							nprice = generalvo.getNprice().toDouble();
						// ���
						if (null != generalvo.getNmny())
							nmny = generalvo.getNmny().toDouble();
					}
				}
				// �жϱ�ͷ�ϵ�Ӧ���������Ƿ�Ϊ��
				if (null != this.getGeneralbvo().getNshouldoutassistnum()
						&& !"".equals(this.getGeneralbvo()
								.getNshouldoutassistnum())) {
					// �ж�ʵ���������Ƿ����Ӧ��������
					if (total > this.getGeneralbvo().getNshouldoutassistnum()
							.toDouble()) {
						// ������ڵ����Ի��������ʾ
						((ToftPanel) myClientUI)
								.showWarningMessage("ʵ����������Ӧ������,����������!");
					} else {
						// ���û�д��� �����洢�û�ѡ�����̼���
						List<TbOutgeneralTVO> generaltList = new ArrayList<TbOutgeneralTVO>();
						// ѭ����������
						for (int i = 0; i < item.length; i++) {
							// ��ȡ��������
							TbOutgeneralTVO generalvo = item[i];
							// ����ж�ʵ���������Ƿ�Ϊ��
							if (null != generalvo.getNoutassistnum()
									&& !"".equals(generalvo.getNoutassistnum())) {
								// ��������ɾ����־��ֵ ��Ϊ�õ��ݲ���Ϊɾ����־��ֵ ������Ҫ���ڸ�ֵ
								generalvo.setDr(0);
								// �ѵ�ǰ�Ķ��󴫸�����
								generaltList.add(generalvo);
							}
						}
						// ���ýӿ���ӷ���
						try {
							// ���ýӿ��е���ӷ��� ����������а�����ɾ�� ����ɾ����ǰ����ӹ��ļ�¼
							// ��Ϊ��ǰҳ���е����ݿ��ܻ�ִ���������������º���ӣ��������ݲ��ò�����������Ҫ���ݵ�һ������
							// �ڶ���������Ϊ������õģ�����ͬ��
							iw.insertGeneralTVO(this.getGeneraltvoList(),
									generaltList);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						// �ж����ʵ���������ܺ��Ƿ�Ϊ��
						if (total > 0) {
							// �ж����μ����Ƿ�Ϊ��
							if (null != batch && batch.size() > 0) {
								// �������θ�ֵ
								vbatchcode = batch.get(0);
								// ѭ�����μ���
								for (int i = 0; i < batch.size(); i++) {
									// �ж����μ����Ƿ���һ��
									if (i == batch.size() - 1
											|| batch.size() == 1)
										break;
									// ������μ����е����β���ͬһ�����εĽ����������
									if (!batch.get(i).equals(batch.get(i + 1)))
										// Ϊ�����������
										vbatchcode = vbatchcode + ","
												+ batch.get(i + 1);
								}
							}
							// ��ҳ�渳ֵ
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
							// ��ҳ�渳ֵ
							setUIData(null, null, null, null, null, null, null);
						}
						this.closeCancel();
					}
				}

			}
		}
	}

	/**
	 * �������ߵ���ҳ�渳ֵ
	 * 
	 * @param noutassistnum
	 *            ʵ��������
	 * @param noutnum
	 *            ʵ��������
	 * @param vbatchcode
	 *            ����
	 */
	private void setUIData(Object noutassistnum, Object noutnum,
			Object vbatchcode, Object lvbatchcode, Object nprice, Object nmny,
			Object cspaceid) {
		// �������ߵ�ʵ����������ֵ this.getIndex()�ǵ�������ѡ�����
		((BillManageUI) myClientUI).getBillCardPanel().setBodyValueAt(
				noutassistnum, this.getIndex(), "noutassistnum");
		// ��ʵ��������ֵ
		((BillManageUI) myClientUI).getBillCardPanel().setBodyValueAt(noutnum,
				this.getIndex(), "noutnum");
		// �����������θ�ֵ
		((BillManageUI) myClientUI).getBillCardPanel().setBodyValueAt(
				vbatchcode, this.getIndex(), "vbatchcode");
		// ����������Դ���θ�ֵ
		((BillManageUI) myClientUI).getBillCardPanel().setBodyValueAt(
				lvbatchcode, this.getIndex(), "lvbatchcode");
		// �������������۸�ֵ
		((BillManageUI) myClientUI).getBillCardPanel().setBodyValueAt(nprice,
				this.getIndex(), "nprice");
		// ������������ֵ
		((BillManageUI) myClientUI).getBillCardPanel().setBodyValueAt(nmny,
				this.getIndex(), "nmny");
		// ������������λID��ֵ
		((BillManageUI) myClientUI).getBillCardPanel().setBodyValueAt(cspaceid,
				this.getIndex(), "cspaceid");
	}

	// �༭���¼�
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub

		if (e.getKey().equals("noutassistnum")) {
			// ��ȡʵ��������
			Object o = getbillListPanel().getBillListData().getBodyItem(
					"noutassistnum").getValueObject();
			// ��ʵ���������
			getbillListPanel().getBillListData().getBodyBillModel().setValueAt(
					null, getbillListPanel().getBodyTable().getSelectedRow(),
					"noutnum");
			double num1 = 0; // ʵ��������
			double num2 = 0; // ��渨����
			if (null != o && !"".equals(o)) {
				num1 = Double.parseDouble(o.toString()); // ת������
				if (num1 == 0) {
					getbillListPanel().getBillListData().getBodyBillModel()
							.setValueAt(
									null,
									getbillListPanel().getBodyTable()
											.getSelectedRow(), "noutassistnum");
					return;
				}
				if (null != this.getGeneralbvo() // �жϻ������Ƿ�Ϊ��
						&& !"".equals(this.getGeneralbvo().getHsl())) {
					// ��ȡ��渨����
					Object stockpieces = getbillListPanel().getBillListData()
							.getBodyBillModel().getValueAt(
									getbillListPanel().getBodyTable()
											.getSelectedRow(), "stockpieces");
					// �ж�Ӧ���������Ƿ�Ϊ��
					if (null != stockpieces && !"".equals(stockpieces)) {
						// ת������
						double stock = Double.parseDouble(stockpieces
								.toString());
						// �ж�ʵ���������Ƿ���ڿ�渨����
						if (num1 > stock) {
							// ������ڰ�ҳ���е���ֵ���
							getbillListPanel().getBillListData()
									.getBodyBillModel().setValueAt(
											null,
											getbillListPanel().getBodyTable()
													.getSelectedRow(),
											"noutassistnum");
							// ��������
							((ToftPanel) myClientUI)
									.showWarningMessage("ʵ�����������������,����������!");

							return;
						}
					}
					// ���ʵ������
					num2 = Double.parseDouble(this.getGeneralbvo().getHsl()
							.toString());
					// Ϊҳ�������ֵ
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
