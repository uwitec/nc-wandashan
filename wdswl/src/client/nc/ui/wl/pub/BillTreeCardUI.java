package nc.ui.wl.pub;

import java.util.HashMap;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UISplitPane;
import nc.ui.pub.beans.UITree;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.bsdelegate.BDBusinessDelegator;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.card.CardEventHandler;
import nc.ui.trade.pub.BillTableCreateTreeTableTool;
import nc.ui.trade.pub.IVOTreeData;
import nc.ui.trade.pub.IVOTreeDataByCode;
import nc.ui.trade.pub.IVOTreeDataByID;
import nc.ui.trade.pub.IVOTreeDataByIDEx;
import nc.ui.trade.pub.TableTreeNode;
import nc.ui.trade.pub.TreeCreateTool;
import nc.ui.trade.pub.VOTreeNode;
import nc.ui.trade.treecard.ITreeCardController;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;

/**
 * ���ݿ�Ƭ�����ͻ��� 
 * @author zpm
 */
public abstract class BillTreeCardUI extends nc.ui.trade.card.BillCardUI {

	/**
	 * TestBill ������ע�⡣
	 */
	public BillTreeCardUI() {
		super();
		initialize();
	}

	/**
	 * ���ý���Ĭ������ �������ڣ�(2001-12-17 14:40:29)
	 */
	public void setDefaultData() throws Exception {

		VOTreeNode node = getBillTreeSelectNode();
		if (node != null) {
			if (getTreeCardUIControl().isAutoManageTree()) {
				if (getCreateTreeData() instanceof IVOTreeDataByID) {
					IVOTreeDataByID idtree = (IVOTreeDataByID) getCreateTreeData();
					getBillCardPanel().setHeadItem(
							idtree.getParentIDFieldName(),
							node.getData().getAttributeValue(
									idtree.getIDFieldName()));

				} else {
					IVOTreeDataByCode idtree = (IVOTreeDataByCode) getCreateTreeData();
					getBillCardPanel().setHeadItem(
							idtree.getCodeFieldName(),
							node.getData().getAttributeValue(
									idtree.getCodeFieldName()));
				}
			}
		}
	}

	//����������
	private IVOTreeData m_billtreedata = null;

	private HashMap m_hasTreeToBuffer = null;

	private UISplitPane m_splitPane = null;

	//
	private BillTableCreateTreeTableTool m_tabledata = null;

	//��������
	private IVOTreeData m_tabletreedata = null;

	//5
	private UITree m_tree = null;

	//
	private TreeCreateTool m_treedata = null;

	//
	private UIScrollPane m_treeSP = null;

	public class BillTreeSelectAdapter implements
			javax.swing.event.TreeSelectionListener {
		public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
			onBillTreeSelect((TableTreeNode) e.getPath().getLastPathComponent());
		}
	}

	/**
	 * TestBill ������ע�⡣ ���ڵ��������������ʹ��
	 */
	public BillTreeCardUI(String pk_corp, String pk_billType,
			String pk_busitype, String operater, String billId) {
		super();
		initialize();
		setBusinessType(pk_busitype);
		//��������
		try {
			getBufferData().addVOToBuffer(loadHeadData(billId));
			getBufferData().setCurrentRow(getBufferData().getCurrentRow());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * ��������ӵ����塣 �������ڣ�(2004-02-03 15:15:40)
	 */
	protected void addBufferData(SuperVO[] queryVos) {

		try {
			if (queryVos != null && queryVos.length != 0) {
				for (int i = 0; i < queryVos.length; i++) {
					AggregatedValueObject aVo = (AggregatedValueObject) Class
							.forName(getUIControl().getBillVoName()[0])
							.newInstance();
					aVo.setParentVO(queryVos[i]);
					getBufferData().addVOToBuffer(aVo);

					int num = getBufferData().getVOBufferSize();
					if (num == -1)
						num = 0;
					else
						num = num - 1;
					getTreeToBuffer()
							.put(queryVos[i].getPrimaryKey(), num + "");
				}
			}
		} catch (java.lang.Exception e) {
			System.out.println("���û�������ʧ�ܣ�");
			e.printStackTrace();
		}
	}

	/**
	 * ��������ӵ����塣 �������ڣ�(2004-02-03 15:15:40)
	 */
	protected void addBufferData(SuperVO[] queryVos, String exIDField) {

		try {
			if (queryVos != null && queryVos.length != 0) {
				for (int i = 0; i < queryVos.length; i++) {
					AggregatedValueObject aVo = (AggregatedValueObject) Class
							.forName(getUIControl().getBillVoName()[0])
							.newInstance();
					aVo.setParentVO(queryVos[i]);
					getBufferData().addVOToBuffer(aVo);

					int num = getBufferData().getVOBufferSize();
					if (num == -1)
						num = 0;
					else
						num = num - 1;
					getTreeToBuffer().put(
							queryVos[i].getAttributeValue(exIDField).toString()
									+ queryVos[i].getPrimaryKey().toString(),
							num + "");
				}
			}
		} catch (java.lang.Exception e) {
			System.out.println("���û�������ʧ�ܣ�");
			e.printStackTrace();
		}
	}

	/**
	 * ���Ի���ɡ� �������ڣ�(2004-02-06 12:19:14)
	 */
	public void afterInit() throws java.lang.Exception {
	}

	/**
	 * ��ѡ���¼��� ���أ��Ƿ��������� �������ڣ�(2001-3-23 2:02:27)
	 *
	 * @param e
	 *            ufbill.BillEditEvent
	 */
	public boolean afterTreeSelected(VOTreeNode node) {
		return true;
	}

	/**
	 * ����������ݡ� �������ڣ�(2004-02-03 15:40:11)
	 *
	 * @return java.util.Hashtable
	 */
	public void clearBodyData() {
		if (getTreeCardUIControl().isTableTree()) {
			getBillTableTreeData().changeToNormalTable();
		}
		if(getBillCardPanel().getBillModel()!=null)
			getBillCardPanel().getBillModel().clearBodyData();
	}

	/**
	 * ����������ݡ� �������ڣ�(2004-02-03 15:40:11)
	 *
	 * @return java.util.Hashtable
	 */
	public void clearTreeSelect() {
		getBillTree().clearSelection();
	}

	/**
	 * ������ �������ڣ�(2004-1-3 18:13:36)
	 */
	public void createBillTree(IVOTreeData treeData) {
		m_billtreedata = treeData;
		getBillTree().setModel(getBillTreeModel(treeData));
	}

	/**
	 * ʵ����ǰ̨�����ҵ��ί���� ��������¼�������Ҫ���ظ÷��� �������ڣ�(2004-1-3 18:13:36)
	 */
	protected BusinessDelegator createBusinessDelegator() {
		return new BDBusinessDelegator();
	}

	/**
	 * ʵ��������༭ǰ���¼�����, ��������¼�������Ҫ���ظ÷��� �������ڣ�(2004-1-3 18:13:36)
	 */
	@Override
	protected CardEventHandler createEventHandler() {
		return new TreeCardEventHandler(this, getUIControl());
	}
	
	/**
	 * ��ʼ�������� �������ڣ�(2004-1-3 18:13:36)
	 */
	protected abstract nc.ui.trade.pub.IVOTreeData createTableTreeData();

	/**
	 * ��ʼ�������� �������ڣ�(2004-1-3 18:13:36)
	 */
	protected abstract nc.ui.trade.pub.IVOTreeData createTreeData();

	/**
	 * ����VO����ɾ����Ӧ�����ڵ㡣 �������ڣ�(2003-10-20 9:51:12)
	 *
	 * @return boolean
	 * @param vo
	 *            nc.vo.pub.CircularlyAccessibleValueObject
	 */
	protected void deleteNodeFromTree(CircularlyAccessibleValueObject vo) {
		getBillTreeData().deleteNodeFromTree(vo);
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-2-1 18:45:58)
	 *
	 * @return javax.swing.tree.DefaultTreeModel
	 */
	protected BillTableCreateTreeTableTool getBillTableTreeData() {

		if (m_tabledata == null)
			m_tabledata = new BillTableCreateTreeTableTool(this
					.getBillCardPanel());

		return m_tabledata;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-02-04 10:48:53)
	 */
	public final TableTreeNode getBillTableTreeRootNode() {
		if (getTreeCardUIControl().isTableTree())
			return (TableTreeNode) getBillTableTreeData().getTreeModel()
					.getRoot();
		else
			return null;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-1-30 9:33:51)
	 *
	 * @return nc.ui.pub.beans.UITree
	 */
	protected UITree getBillTree() {
		if (m_tree == null) {
			m_tree = new UITree();
			m_tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			m_tree.addTreeSelectionListener(new BillTreeSelectAdapter());
		}
		return m_tree;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-2-1 18:45:58)
	 *
	 * @return javax.swing.tree.DefaultTreeModel
	 */
	public TreeCreateTool getBillTreeData() {

		if (m_treedata == null)
			m_treedata = new TreeCreateTool();

		return m_treedata;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-2-1 18:45:58)
	 *
	 * @return javax.swing.tree.DefaultTreeModel
	 */
	public DefaultTreeModel getBillTreeModel(IVOTreeData voTreeData) {

		if (voTreeData == null)
			return null;

		if (m_treedata == null)
			m_treedata = new TreeCreateTool();

		//���Ի�����---------------------------zpm  ���� ע�͵�ԭ�����������
//		if (getTreeCardUIControl() instanceof ISingleController)
			initBufferData(voTreeData.getTreeVO());

		if (voTreeData instanceof IVOTreeDataByID) {
			IVOTreeDataByID idtree = (IVOTreeDataByID) voTreeData;
			return m_treedata.createTree(idtree.getTreeVO(), idtree
					.getIDFieldName(), idtree.getParentIDFieldName(), idtree
					.getShowFieldName());
		} else {
			IVOTreeDataByCode idtree = (IVOTreeDataByCode) voTreeData;
			return m_treedata.createTreeByCode(idtree.getTreeVO(), idtree
					.getCodeFieldName(), idtree.getCodeRule(), idtree
					.getShowFieldName());
		}

	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-02-04 10:48:53)
	 */
	public final VOTreeNode getBillTreeSelectNode() {
		VOTreeNode node = null;
		if (getBillTree().getSelectionPath() != null) {
			TableTreeNode tnode = (TableTreeNode) getBillTree()
					.getSelectionPath().getLastPathComponent();

			if (!tnode.isRoot()) {
				node = (VOTreeNode) tnode;
			}
		}
		return node;
	}

	/**
	 * ��ý���仯����VO�� �������ڣ�(2004-1-7 10:01:01)
	 *
	 * @return nc.vo.pub.AggregatedValueObject
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	public AggregatedValueObject getChangedVOFromUI()
			throws java.lang.Exception {
		AggregatedValueObject retVo = getBillCardWrapper().getChangedVOFromUI();
		ISingleController sCtrl = null;
		if (getTreeCardUIControl() instanceof ISingleController) {
			sCtrl = (ISingleController) getTreeCardUIControl();
			if (sCtrl.isSingleDetail()) {
				VOTreeNode node = getBillTreeSelectNode();
				if (node != null)
					retVo.setParentVO(node.getData());
			}
		}

		return retVo;

	}

	/**
	 * ��ʼ�������� �������ڣ�(2004-1-3 18:13:36)
	 */
	public nc.ui.trade.pub.IVOTreeData getCreateTableTreeData() {
		if (m_tabletreedata == null)
			m_tabletreedata = createTableTreeData();

		return m_tabletreedata;
	}

	/**
	 * ��ʼ�������� �������ڣ�(2004-1-3 18:13:36)
	 */
	public nc.ui.trade.pub.IVOTreeData getCreateTreeData() {
		if (m_billtreedata == null)
			m_billtreedata = createTreeData();

		return m_billtreedata;
	}
	
	//ˢ��ʱ����  zpm
	public nc.ui.trade.pub.IVOTreeData getCreateTreeDataRefresh() {
		return createTreeData();
	}
	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-02-03 16:42:29)
	 *
	 * @return nc.ui.pub.beans.UISplitPane
	 */
	public UISplitPane getSplitPane() {
		if (m_splitPane == null)
			m_splitPane = new UISplitPane(1);
		return m_splitPane;
	}

	/**
	 * ���ص�ǰUI��Ӧ�Ŀ�����ʵ���� �������ڣ�(2004-01-06 15:46:35)
	 *
	 * @return nc.ui.tm.pub.ControlBase
	 */
	protected final TreeCardEventHandler getTreeCardEventHandler() {
		return (TreeCardEventHandler) getCardEventHandler();
	}

	/**
	 * ���ص�ǰUI��Ӧ�Ŀ�����ʵ���� �������ڣ�(2003-5-27 15:46:35)
	 *
	 * @return nc.ui.tm.pub.ControlBase
	 */
	public final ITreeCardController getTreeCardUIControl() {
		return (ITreeCardController) getUIControl();
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-02-03 10:46:27)
	 *
	 * @return nc.ui.pub.beans.UIScrollPane
	 */
	public UIScrollPane getTreeSP() {
		if (m_treeSP == null) {
			m_treeSP = new UIScrollPane();
			m_treeSP.setAutoscrolls(true);
			m_treeSP
					.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			m_treeSP
					.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			m_treeSP.setMinimumSize(new java.awt.Dimension(3, 3));
			m_treeSP.setPreferredSize(new java.awt.Dimension(298, 469));
			m_treeSP.setViewportView(getBillTree());
		}

		return m_treeSP;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-02-03 15:40:11)
	 *
	 * @return java.util.Hashtable
	 */
	public HashMap getTreeToBuffer() {
		if (m_hasTreeToBuffer == null)
			m_hasTreeToBuffer = new HashMap();
		return m_hasTreeToBuffer;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-02-03 15:15:40)
	 */
	private void initBufferData(SuperVO[] queryVos) {

		try {
			//��ջ�������
			getBufferData().clear();
			if (queryVos != null && queryVos.length != 0) {
				addBufferData(queryVos);
				setBillOperate(IBillOperate.OP_NOTEDIT);
			} else {
				m_hasTreeToBuffer = new HashMap();

				getBufferData().setCurrentRow(-1);

				setBillOperate(IBillOperate.OP_INIT);
			}
		} catch (java.lang.Exception e) {
			System.out.println("���û�������ʧ�ܣ�");
			e.printStackTrace();
		}
	}

	private void initialize() {

		try {

			//�����
			if (!getTreeCardUIControl().isChildTree()) {
				this.add(getSplitPane());
				getSplitPane().setLeftComponent(getTreeSP());
				getSplitPane().setRightComponent(getBillCardPanel());

				getBillCardPanel().setPreferredSize(
						new java.awt.Dimension(298, 469));
			} else {

				this.add(this.getBillCardPanel());

				this.getBillCardPanel().getBodyUIPanel().add(getSplitPane(),
						java.awt.BorderLayout.CENTER);

				getSplitPane().setLeftComponent(getTreeSP());
				getSplitPane().setRightComponent(
						getBillCardPanel().getBodyTabbedPane());

				getBillCardPanel().getBodyTabbedPane().setPreferredSize(
						new java.awt.Dimension(298, 469));

			}

			createBillTree(getCreateTreeData());

			afterInit();
		} catch (Exception e) {
			e.printStackTrace();
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000109")/*@res "�����쳣�������ʼ������"*/);

		}

		//revalidate();
	}

	/**
	 * ����ڵ㡣 �������ڣ�(2004-2-1 18:45:58)
	 *
	 * @return javax.swing.tree.DefaultTreeModel
	 */
	protected void insertNodeToTree(IVOTreeDataByID vodata)
			throws java.lang.Exception {
		if (vodata instanceof IVOTreeDataByIDEx) {
			IVOTreeDataByIDEx vodataex = (IVOTreeDataByIDEx) vodata;
			getBillTreeData().insertNodeToTree(vodataex.getTreeVO(),
					vodataex.getIDFieldName(), vodataex.getParentIDFieldName(),
					vodataex.getShowFieldName(), vodataex.getExIDFieldName());
		} else {
			getBillTreeData().insertNodeToTree(vodata.getTreeVO(),
					vodata.getIDFieldName(), vodata.getParentIDFieldName(),
					vodata.getShowFieldName());
		}
	}

	/**
	 * ��VO������뵽���С� �������ڣ�(2004-02-03 9:34:39)
	 *
	 * @return nc.vo.pub.CircularlyAccessibleValueObject
	 */
	protected void insertNodeToTree(CircularlyAccessibleValueObject vo)
			throws java.lang.Exception {
		VOTreeNode node = getBillTreeData().insertNodeToTree(vo);

		String key = vo.getPrimaryKey();
		if (!getTreeToBuffer().containsKey(key)) {
			if (getBufferData().getVOBufferSize() > 0)
				getTreeToBuffer().put(key,
						getBufferData().getVOBufferSize() - 1 + "");
			else
				getTreeToBuffer().put(key, "0");
		}

		if (node != null)
			m_tree.setSelectionPath(new javax.swing.tree.TreePath(node
					.getPath()));

	}

	/**
	 * �޸������ڵ����� �������ڣ�(2003-9-6 14:23:10)
	 *
	 * @return java.lang.String
	 */
	public void modifyRootNodeShowName(String value) {
		getBillTreeData().modifyRootNodeShowName(value);
		getBillTree().revalidate();
		getBillTree().repaint();
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-02-04 9:41:03)
	 *
	 * @param node
	 *            nc.ui.pub.card.treetableex.VOTreeNode
	 */
	private void onBillTreeSelect(TableTreeNode selectnode) {

		if (selectnode.isRoot())
			if (getTreeCardUIControl().isChildTree())
				getBillCardPanel().getBillModel().clearBodyData();
			else
				getBillCardPanel().getBillData().clearViewData();

		if (selectnode instanceof VOTreeNode && getTreeToBuffer().size() > 0) {

			VOTreeNode node = (VOTreeNode) selectnode;

			if (afterTreeSelected(node)) {

				getTreeCardEventHandler().onTreeSelected(node);

				//����������������--------------------------------zpm <--->
				//setCurrentRow�Ѿ��������ѯ������[����vos]�Ѿ�set��getBufferData()�С�
				getBufferData().setCurrentRow(
						(new Integer(getTreeToBuffer().get(node.getNodeID())
								.toString()).intValue()));
			} else {
				if (getTreeCardUIControl().isChildTree())
					getBillCardPanel().getBillModel().clearBodyData();
				else
					getBillCardPanel().getBillData().clearViewData();
			}
		}

		onTreeSelectSetButtonState(selectnode);

	}

	/**
	 * ��ѡ������õ���״̬�� �������ڣ�(2004-02-04 9:41:03)
	 *
	 * @param node
	 *            nc.ui.pub.card.treetableex.VOTreeNode
	 */
	protected void onTreeSelectSetButtonState(TableTreeNode selectnode) {

		//���õ���״̬
		try {
			if (getTreeCardUIControl().isChildTree()) {
			} else {
				if (selectnode.isRoot()) {
					setBillOperate(nc.ui.trade.base.IBillOperate.OP_INIT);
				} else {
					if (getTreeCardUIControl() instanceof ISingleController
							&& getTreeToBuffer().size() > 0) {
						ISingleController sCtrl = (ISingleController) getTreeCardUIControl();
						if (sCtrl.isSingleDetail()
								&& getBufferData().getCurrentVO()
										.getChildrenVO() != null)
							setBillOperate(nc.ui.trade.base.IBillOperate.OP_NOADD_NOTEDIT);
						else
							setBillOperate(nc.ui.trade.base.IBillOperate.OP_NOTEDIT);
					} else {
						setBillOperate(nc.ui.trade.base.IBillOperate.OP_NOTEDIT);
					}
				}
			}
		} catch (java.lang.Exception e) {
			System.out.println("���õ���״̬ʧ�ܣ�");
			e.printStackTrace();
		}

	}

	/**
	 * ��������TreeToBuffer �������ڣ�(2004-02-03 15:15:40)
	 */
	public void resetTreeToBufferData() {
		try {
			if (getBufferData() != null) {

				m_hasTreeToBuffer = null;

				CircularlyAccessibleValueObject[] vos = getBufferData()
						.getAllHeadVOsFromBuffer();
				if (vos != null) {
					for (int i = 0; i < vos.length; i++) {
						getTreeToBuffer().put(vos[i].getPrimaryKey(), i + "");
					}
				}
			}

		} catch (java.lang.Exception e) {
			System.out.println("���û�������ʧ�ܣ�");
			e.printStackTrace();
		}

	}

	/**
	 * ������������ �������ڣ�(2004-1-3 18:13:36)
	 */
	public void setBodyValueVO(SuperVO[] vo, IVOTreeData data) {
		if (getTreeCardUIControl().isTableTree())
			getBillTableTreeData().changeToNormalTable();

		getBillCardPanel().updateValue();
		getBillCardPanel().getBillData().setBodyValueVO(vo);
		//���ر�����ʾ��ʽ
		if(getBillCardPanel().getBillModel()!=null)
			getBillCardPanel().getBillModel().execLoadFormula();

		if (getTreeCardUIControl().isTableTree())
			setTableToTreeTable(data);
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-2-1 18:45:58)
	 *
	 * @return javax.swing.tree.DefaultTreeModel
	 */
	public void setTableToTreeTable(IVOTreeData data) {
		if (data != null) {
			if (data instanceof IVOTreeDataByID) {
				IVOTreeDataByID idtree = (IVOTreeDataByID) data;
				getBillTableTreeData().changeToTreeTable(
						idtree.getIDFieldName(), idtree.getParentIDFieldName(),
						idtree.getShowFieldName());
			} else {
				IVOTreeDataByCode idtree = (IVOTreeDataByCode) data;
				getBillTableTreeData().changeToTreeTableByCode(
						idtree.getCodeFieldName(), idtree.getCodeRule(),
						idtree.getShowFieldName());
			}
		}
		getBillTableTreeData().expandToLevel(-1);
	}

	/**
	 * ���õ��ݰ�ť״̬�����ݴ����VO���ݽ����жϣ�����Ϊ�ա� �������ڣ�(2002-12-31 11:19:21)
	 *
	 * @param vo
	 *            nc.vo.pub.AggregatedValueObject
	 */
	protected void setTotalUIState(int intOpType) throws Exception {

		//���ð�ť״̬
		getButtonManager().setButtonByOperate(intOpType);
		updateButtons();
		//���ݲ�����������UI״̬
		switch (intOpType) {
		case OP_ADD: {
			getBillCardPanel().setEnabled(true);
			getBillCardPanel().addNew();
			if (getTreeCardUIControl().isChildTree())
				getBillTree().setEnabled(true);
			else
				getBillTree().setEnabled(false);
			setDefaultData();
			setBillNo();
			getBillCardPanel().transferFocusToFirstEditItem();
			break;
		}
		case OP_EDIT: {
			getBillCardPanel().setEnabled(true);
			if (getTreeCardUIControl().isChildTree())
				getBillTree().setEnabled(true);
			else
				getBillTree().setEnabled(false);
			getBillCardWrapper().setRowStateToNormal();
			getBillCardPanel().transferFocusToFirstEditItem();
			break;
		}
		case OP_REFADD: {
			getBillCardPanel().setEnabled(true);
			//getBillCardPanel().addNew();
			getBillTree().setEnabled(false);
			setDefaultData();
			setBillNo();
			break;
		}
		case OP_INIT: {
			getBillCardWrapper().setCardData(null);
			getBillCardPanel().setEnabled(false);
			if (getTreeCardUIControl().isChildTree())
			{
				getBillTree().setEnabled(false);
			}
			else
			{
				getBillTree().setEnabled(true);
			}
			break;
			
		}
		case OP_NOTEDIT: {
			getBillCardPanel().setEnabled(false);
			if (getTreeCardUIControl().isChildTree())
				getBillTree().setEnabled(false);
			else
				getBillTree().setEnabled(true);
			break;
		}
		default: {
			break;
		}
		}

	}

	/**
	 * This method is called whenever the observed object is changed. An
	 * application calls an <tt>Observable</tt> object's
	 * <code>notifyObservers</code> method to have all the object's observers
	 * notified of the change.
	 *
	 * @param o
	 *            the observable object.
	 * @param arg
	 *            an argument passed to the <code>notifyObservers</code>
	 *            method.
	 */
	public void update(java.util.Observable o, java.lang.Object arg) {

		if (beforeUpdate()) {
			try {

				if (getTreeCardUIControl().isTableTree()) {
					getBillTableTreeData().changeToNormalTable();
				}

				getBillCardWrapper()
						.setCardData(getBufferData().getCurrentVO());

				getBillCardPanel().updateValue();

				if (getTreeCardUIControl().isTableTree()) {
					setTableToTreeTable(getCreateTableTreeData());
				}

				//���õ���״̬
				updateBtnStateByCurrentVO();
			} catch (Exception e) {
				e.printStackTrace();
				showErrorMessage(e.getMessage());
			}
		}

		afterUpdate();

	}
}